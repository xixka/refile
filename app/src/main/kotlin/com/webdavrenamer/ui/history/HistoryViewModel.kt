package com.webdavrenamer.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webdavrenamer.data.db.RenameBatchEntity
import com.webdavrenamer.data.db.RenameEntryEntity
import com.webdavrenamer.data.repository.HistoryRepository
import com.webdavrenamer.data.repository.RevertResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 历史记录页 ViewModel（计划 §M5 SubTask 5.1.3）。
 *
 * 状态：
 * - [batches]：所有批次（按 createdAt 倒序，[HistoryRepository.observeBatches] 直供）。
 * - [selectedBatch] / [selectedEntries]：当前选中批次的详情。
 * - [reverting]：撤销进行中标志（驱动 UI 显示 CircularProgressIndicator 并禁用按钮）。
 * - [revertResult]：最近一次撤销结果（成功 N/M 或失败原因），由 UI 弹 Snackbar。
 *
 * [selectBatch] / [revertBatch] / [clearRevertResult] 为 UI 调用入口。
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: HistoryRepository,
) : ViewModel() {

    val batches: StateFlow<List<RenameBatchEntity>> = repo.observeBatches()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    private val _selectedBatch = MutableStateFlow<RenameBatchEntity?>(null)
    val selectedBatch: StateFlow<RenameBatchEntity?> = _selectedBatch.asStateFlow()

    private val _selectedEntries = MutableStateFlow<List<RenameEntryEntity>>(emptyList())
    val selectedEntries: StateFlow<List<RenameEntryEntity>> = _selectedEntries.asStateFlow()

    private val _reverting = MutableStateFlow(false)
    val reverting: StateFlow<Boolean> = _reverting.asStateFlow()

    private val _revertResult = MutableStateFlow<RevertResult?>(null)
    val revertResult: StateFlow<RevertResult?> = _revertResult.asStateFlow()

    /**
     * 选中某批次：加载批次详情与条目列表。
     */
    fun selectBatch(id: Long) {
        viewModelScope.launch {
            _selectedBatch.value = repo.getBatch(id)
            _selectedEntries.value = repo.getEntries(id)
        }
    }

    /** 退出详情视图（清空选中态）。 */
    fun clearSelection() {
        _selectedBatch.value = null
        _selectedEntries.value = emptyList()
    }

    /**
     * 撤销整批：调用 [HistoryRepository.revertBatch]，过程中置 [reverting]=true，
     * 结果写入 [revertResult]，并刷新当前选中批次（更新 isReverted 标记）。
     */
    fun revertBatch(id: Long) {
        viewModelScope.launch {
            _reverting.value = true
            try {
                val result = repo.revertBatch(id)
                _revertResult.value = result
                // 撤销后刷新选中批次（isReverted 已置 true）。
                if (result !is RevertResult.Failure || _selectedBatch.value?.id == id) {
                    _selectedBatch.value = repo.getBatch(id)
                    _selectedEntries.value = repo.getEntries(id)
                }
            } finally {
                _reverting.value = false
            }
        }
    }

    /** 清除一次性撤销结果（Snackbar 消费后调用）。 */
    fun clearRevertResult() {
        _revertResult.value = null
    }

    /** 把 [RevertResult] 转成给用户看的简短文案。 */
    fun revertResultMessage(result: RevertResult): String = when (result) {
        is RevertResult.Success -> "已回滚 ${result.rolledBack}/${result.total} 条"
        is RevertResult.Partial -> {
            val head = "已回滚 ${result.rolledBack}/${result.total} 条，失败 ${result.failedEntries.size} 项"
            val tail = result.failedEntries.take(3).joinToString("\n") { "· ${it.targetPath}" }
            if (tail.isBlank()) head else "$head\n$tail"
        }
        is RevertResult.Failure -> result.reason
    }

    /** 把 [RenameEntryEntity.status] 映射为 UI 状态枚举（驱动状态图标着色）。 */
    fun entryStatus(status: String): EntryStatus = when (status) {
        "SUCCESS" -> EntryStatus.SUCCESS
        "PARTIAL" -> EntryStatus.PARTIAL
        "FAILED" -> EntryStatus.FAILED
        "SKIPPED" -> EntryStatus.SKIPPED
        else -> EntryStatus.SKIPPED
    }

    /** 条目状态枚举。 */
    enum class EntryStatus { SUCCESS, PARTIAL, FAILED, SKIPPED }
}
