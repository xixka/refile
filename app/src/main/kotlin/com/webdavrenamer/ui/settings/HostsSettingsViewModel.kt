package com.webdavrenamer.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webdavrenamer.core.backup.HostEntry
import com.webdavrenamer.core.backup.HostPresets
import com.webdavrenamer.core.backup.HostsConfig
import com.webdavrenamer.core.backup.HostsSpeedTest
import com.webdavrenamer.core.backup.HostsSpeedTest.IpSpeedTestResult
import com.webdavrenamer.data.prefs.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Hosts 设置 ViewModel（spec §5.3.3–5.3.5）。
 *
 * 状态：
 * - [hostsConfig]：从 [SettingsRepository.hostsConfig] 派生的可观察配置（开关 + 条目）。
 * - [testing]：测速进行中标志，UI 用以禁用按钮/显示进度。
 * - [testResults]：每个 hostname 的测速结果列表（hostname → 各 IP 结果）。
 *
 * 方法覆盖 spec §5.3.4 UI 的所有操作：
 * - [toggleEnabled]：总开关持久化（§5.3.5）。
 * - [addHost]/[editHost]/[removeHost]：条目 CRUD。
 * - [testConnection]：测指定 hostname 全部 IP。
 * - [testAllConnections]：测所有 hostname，并行。
 * - [autoPickFastest]：测速后选延迟最低可用 IP 设为该 hostname 唯一 ips（§5.3.3）。
 * - [applyPreset]：把预设域名填入条目（ips 留空待用户测速）。
 *
 * Hosts 写入全部经 [SettingsRepository.setHostsConfig] 落盘，OkHttpClient 在使用方
 * （[com.webdavrenamer.core.tmdb.TmdbClient] 与 [com.webdavrenamer.data.repository.ServerRepository]）
 * 构造时读取该 Flow 应用 [com.webdavrenamer.core.backup.HostsDns]。
 */
@HiltViewModel
class HostsSettingsViewModel @Inject constructor(
    private val settings: SettingsRepository,
    private val speedTest: HostsSpeedTest,
) : ViewModel() {

    /** 预设按钮展示用：name → 标签。 */
    data class PresetOption(val name: String, val label: String)

    /** 当前 hosts 配置（开关 + 条目）。 */
    val hostsConfig: StateFlow<HostsConfig> = settings.hostsConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), HostsConfig())

    /** 是否正在测速。 */
    private val _testing = MutableStateFlow(false)
    val testing: StateFlow<Boolean> = _testing.asStateFlow()

    /** 各 hostname 的测速结果。 */
    private val _testResults = MutableStateFlow<Map<String, List<IpSpeedTestResult>>>(emptyMap())
    val testResults: StateFlow<Map<String, List<IpSpeedTestResult>>> = _testResults.asStateFlow()

    /** 预设列表，供 UI 渲染按钮行。 */
    val presetOptions: List<PresetOption> = listOf(
        PresetOption(PRESET_TMDB_API, "TMDB API"),
        PresetOption(PRESET_TMDB_IMAGE, "TMDB Image"),
        PresetOption(PRESET_DEFAULT_CANDIDATES, "默认候选"),
    )

    /** 切换总开关（§5.3.5）。 */
    fun toggleEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            settings.setHostsConfig(current.copy(enabled = enabled))
        }
    }

    /** 新增 hostname（若已存在则覆盖 ips）。 */
    fun addHost(hostname: String, ips: List<String>) {
        val name = hostname.trim()
        if (name.isEmpty()) return
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            val newEntries = upsertEntry(current.entries, name, ips)
            settings.setHostsConfig(current.copy(entries = newEntries))
        }
    }

    /** 编辑指定 hostname 的 ips 列表。 */
    fun editHost(hostname: String, newIps: List<String>) {
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            val newEntries = upsertEntry(current.entries, hostname, newIps)
            settings.setHostsConfig(current.copy(entries = newEntries))
        }
    }

    /** 删除指定 hostname 条目并清除其测速结果。 */
    fun removeHost(hostname: String) {
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            val newEntries = current.entries
                .filterNot { it.hostname.equals(hostname, ignoreCase = true) }
            settings.setHostsConfig(current.copy(entries = newEntries))
            _testResults.update { results ->
                results.filterKeys { !it.equals(hostname, ignoreCase = true) }
            }
        }
    }

    /** 测指定 hostname 的所有 IP（§5.3.4 连接测试按钮）。 */
    fun testConnection(hostname: String) {
        viewModelScope.launch {
            val ips = currentIpsFor(hostname)
            if (ips.isEmpty()) return@launch
            _testing.value = true
            try {
                val results = speedTest.testAllIps(hostname, ips)
                _testResults.update { it + (hostname to results) }
            } finally {
                _testing.value = false
            }
        }
    }

    /** 测所有 hostname 的所有 IP，并行（§5.3.4 测试所有连接）。 */
    fun testAllConnections() {
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            if (current.entries.isEmpty()) return@launch
            _testing.value = true
            try {
                val all = coroutineScope {
                    current.entries.map { entry ->
                        async { entry.hostname to speedTest.testAllIps(entry.hostname, entry.ips) }
                    }.awaitAll().toMap()
                }
                _testResults.value = all
            } finally {
                _testing.value = false
            }
        }
    }

    /** 测速后选延迟最低可用 IP 设为该 hostname 唯一 ips（§5.3.3 自动测速）。 */
    fun autoPickFastest(hostname: String) {
        viewModelScope.launch {
            val ips = currentIpsFor(hostname)
            if (ips.isEmpty()) return@launch
            _testing.value = true
            try {
                val fastest = speedTest.pickFastest(hostname, ips)
                if (fastest != null) {
                    editHost(hostname, listOf(fastest.ip))
                    _testResults.update { it + (hostname to listOf(fastest)) }
                }
            } finally {
                _testing.value = false
            }
        }
    }

    /** 应用预设（添加对应 hostname 条目，ips 留空待测速）。 */
    fun applyPreset(presetName: String) {
        val hostnamesToAdd = when (presetName) {
            PRESET_TMDB_API -> listOf(HostPresets.TMDB_API)
            PRESET_TMDB_IMAGE -> listOf(HostPresets.TMDB_IMAGE)
            PRESET_DEFAULT_CANDIDATES -> HostPresets.DEFAULT_CANDIDATES
            else -> emptyList()
        }
        if (hostnamesToAdd.isEmpty()) return
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            val newEntries = hostnamesToAdd.fold(current.entries) { acc, hostname ->
                if (acc.any { it.hostname.equals(hostname, ignoreCase = true) }) {
                    acc // 已存在则不覆盖（保留用户已配置的 ips）
                } else {
                    acc + HostEntry(hostname = hostname, ips = emptyList())
                }
            }
            settings.setHostsConfig(current.copy(entries = newEntries))
        }
    }

    /** 取当前 hostname 的 ips 列表。 */
    private suspend fun currentIpsFor(hostname: String): List<String> =
        settings.hostsConfig.first()
            .entries
            .firstOrNull { it.hostname.equals(hostname, ignoreCase = true) }
            ?.ips
            ?: emptyList()

    /** 插入或更新条目（按 hostname 忽略大小写匹配）。 */
    private fun upsertEntry(
        entries: List<HostEntry>,
        hostname: String,
        ips: List<String>,
    ): List<HostEntry> {
        val existing = entries.firstOrNull { it.hostname.equals(hostname, ignoreCase = true) }
        val cleanedIps = ips.map { it.trim() }.filter { it.isNotEmpty() }
        return if (existing != null) {
            entries.map { if (it.hostname.equals(hostname, ignoreCase = true)) it.copy(ips = cleanedIps) else it }
        } else {
            entries + HostEntry(hostname = hostname, ips = cleanedIps)
        }
    }

    private companion object {
        const val PRESET_TMDB_API = "TMDB_API"
        const val PRESET_TMDB_IMAGE = "TMDB_IMAGE"
        const val PRESET_DEFAULT_CANDIDATES = "DEFAULT_CANDIDATES"
    }
}
