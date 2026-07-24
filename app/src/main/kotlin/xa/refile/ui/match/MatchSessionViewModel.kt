package xa.refile.ui.match

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 跨匹配系列页面共享的会话 ViewModel（Task 2.4 导航接入 / Task 2.5 Edit Match 数据桥 / Task 3.4 预览数据桥）。
 *
 * 在 [xa.refile.ui.navigation.AppNavHost] 中以 `hiltViewModel()`（Activity 作用域）
 * 创建，浏览器 `onProceedToMatch` 通过 [setFiles] 写入用户选中的视频完整路径，
 * [MatchScreen] 读取 [selectedPaths] 后转交 [MatchViewModel]。
 *
 * Task 2.5 扩展：[matchedFiles] 作为 MatchScreen ↔ EditMatchScreen 之间的匹配结果桥。
 * Task 3.4 扩展：[matches] 作为 MatchScreen → 预览页的已匹配结果桥，供预览页渲染目标路径。
 *
 * 用 Activity 作用域而非目的地图作用域，是为了让路径在 servers → browser → match → edit_match → preview
 * 的整个回退栈生命周期内保持，且无需把 List<String> 编码进导航参数（路径含特殊字符）。
 */
@HiltViewModel
class MatchSessionViewModel @Inject constructor() : ViewModel() {

    private val _selectedPaths = MutableStateFlow<List<String>>(emptyList())
    val selectedPaths: StateFlow<List<String>> = _selectedPaths.asStateFlow()

    /** 浏览器跳转匹配页前写入选中文件完整路径列表。 */
    fun setFiles(paths: List<String>) {
        _selectedPaths.value = paths
    }

    // ---- Task 3.4：匹配结果传递到预览页 ----

    private val _matches = MutableStateFlow<List<MatchViewModel.FileMatch>>(emptyList())

    /** 已匹配（含 TMDB 元数据）的文件列表，由匹配页在跳转预览页前写入。 */
    val matches: StateFlow<List<MatchViewModel.FileMatch>> = _matches.asStateFlow()

    /** 匹配页跳转预览页前写入已匹配文件列表，供预览页渲染目标路径。 */
    fun setMatches(matches: List<MatchViewModel.FileMatch>) {
        _matches.value = matches
    }

    // ---- Task 2.5：匹配结果跨页共享 ----

    private val _matchedFiles = MutableStateFlow<List<MatchViewModel.FileMatch>>(emptyList())
    val matchedFiles: StateFlow<List<MatchViewModel.FileMatch>> = _matchedFiles.asStateFlow()

    /**
     * 脏标记：仅当 EditMatch 回写后置 true。MatchScreen 消费后 [clearDirty]。
     * 跳转编辑前的 [setMatchedFiles] 不置脏，避免回退时误触发覆盖。
     */
    private val _dirty = MutableStateFlow(false)
    val dirty: StateFlow<Boolean> = _dirty.asStateFlow()

    /** 跳转 EditMatch 前写入当前匹配结果快照。默认不置脏。 */
    fun setMatchedFiles(files: List<MatchViewModel.FileMatch>, markDirty: Boolean = false) {
        _matchedFiles.value = files
        _dirty.value = markDirty
    }

    /** EditMatch 单条保存后按索引回写（Task 2.5.1/2.5.2/2.5.3）。 */
    fun updateMatchedFile(index: Int, file: MatchViewModel.FileMatch) {
        val list = _matchedFiles.value.toMutableList()
        if (index in list.indices) {
            list[index] = file
            _matchedFiles.value = list
            _dirty.value = true
        }
    }

    /** EditMatch 线性对齐批量保存后回写整表（Task 2.5.4）。 */
    fun replaceMatchedFiles(files: List<MatchViewModel.FileMatch>) {
        _matchedFiles.value = files
        _dirty.value = true
    }

    /** MatchScreen 消费完编辑结果后清除脏标记。 */
    fun clearDirty() {
        _dirty.value = false
    }
}
