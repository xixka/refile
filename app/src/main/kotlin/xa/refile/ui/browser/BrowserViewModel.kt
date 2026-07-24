package xa.refile.ui.browser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xa.refile.core.webdav.MediaFileTypes
import xa.refile.core.webdav.WebDavClient
import xa.refile.core.webdav.WebDavEntry
import xa.refile.data.crypto.KeystoreCrypto
import xa.refile.data.repository.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 文件浏览器 ViewModel（计划 §M1 SubTask 1.5.1–1.5.6）。
 *
 * 职责：
 * - [init] 按 serverId 取配置，解密密码构造 [WebDavClient]，加载服务器 rootPath。
 * - [loadDirectory] 对 path 发 PROPFIND Depth 1，过滤掉返回的第一项（当前目录本身），
 *   按当前排序规则排序。目录始终排在文件前。
 * - 导航：[navigateInto] / [navigateToBreadcrumb] / [goUp] / [refresh]。
 * - 排序：[toggleSort] / [toggleSortOrder]，改变规则即对当前列表重排。
 * - 多选：长按视频进入多选，仅 [MediaFileTypes.isSelectableVideo] 的文件可被勾选；
 *   目录/字幕/nfo/图片/iso 仅显示，不可选。
 *
 * 安全约束：密码仅在 [init] 内解密用于构造 [WebDavClient]，绝不进入日志/UI 状态（红线）。
 */
@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val serverRepo: ServerRepository,
    private val crypto: KeystoreCrypto,
) : ViewModel() {

    /** 排序字段。 */
    enum class SortField { NAME, SIZE, TIME }

    /** 浏览器 UI 状态。 */
    data class UiState(
        val serverName: String = "",
        val rootPath: String = "/",
        val currentPath: String = "/",
        val entries: List<WebDavEntry> = emptyList(),
        val loading: Boolean = true,
        val error: String? = null,
        val sortField: SortField = SortField.NAME,
        val sortAsc: Boolean = true,
        val multiSelectMode: Boolean = false,
        val selectedPaths: Set<String> = emptySet(),
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    @Volatile
    private var webDavClient: WebDavClient? = null

    /**
     * 取服务器配置，构造 [WebDavClient] 并加载根目录。
     * 构造 baseUrl 的方式与 [ServerRepository] 内部 buildFullBaseUrl 一致。
     */
    suspend fun init(serverId: Long) {
        _uiState.update { it.copy(loading = true, error = null) }
        val entity = serverRepo.getServer(serverId)
        if (entity == null) {
            _uiState.update { it.copy(loading = false, error = "未找到服务器配置") }
            return
        }
        val scheme = if (entity.https) "https" else "http"
        val host = entity.baseUrl
            .trim()
            .removePrefix("https://")
            .removePrefix("http://")
            .trimEnd('/')
        val fullBaseUrl = if (entity.port != null) "$scheme://$host:${entity.port}" else "$scheme://$host"
        val password = entity.encryptedPassword?.let { crypto.decrypt(it) }
        webDavClient = WebDavClient(fullBaseUrl, entity.username, password)
        val root = normalizePath(entity.rootPath)
        _uiState.update { it.copy(serverName = entity.name, rootPath = root, currentPath = root) }
        loadDirectory(root)
    }

    /**
     * 对 [path] 发 PROPFIND Depth 1，过滤掉返回的第一项（当前目录本身）并排序后写入状态。
     *
     * SubTask 1.5.6：PROPFIND 一次性返回，列表项 > [LARGE_DIR_THRESHOLD] 时仅记 warning；
     * LazyColumn 自身虚拟化，无需分页。
     */
    fun loadDirectory(path: String) {
        val client = webDavClient ?: run {
            _uiState.update { it.copy(loading = false, error = "客户端未初始化") }
            return
        }
        val normalized = normalizePath(path)
        _uiState.update { it.copy(loading = true, error = null, currentPath = normalized) }
        viewModelScope.launch {
            try {
                val raw = client.propfind(normalized, depth = 1)
                if (raw.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            entries = emptyList(),
                            error = "无法读取目录，请检查路径或服务器配置",
                        )
                    }
                    return@launch
                }
                // 过滤掉返回的第一项（当前目录本身）
                val children = raw.drop(1)
                if (children.size > LARGE_DIR_THRESHOLD) {
                    Log.w(TAG, "Large directory detected: ${children.size} entries under '$normalized'")
                }
                _uiState.update {
                    it.copy(
                        loading = false,
                        entries = sortEntries(children, it.sortField, it.sortAsc),
                        error = null,
                    )
                }
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(loading = false, error = "加载失败：${t.message ?: "未知错误"}")
                }
            }
        }
    }

    /** 进入子目录。非目录忽略。 */
    fun navigateInto(entry: WebDavEntry) {
        if (!entry.isCollection) return
        val name = entry.displayName ?: nameFromHref(entry.href)
        loadDirectory(joinPath(_uiState.value.currentPath, name))
    }

    /** 跳到面包屑某层。 */
    fun navigateToBreadcrumb(path: String) {
        loadDirectory(path)
    }

    /** 返回上一级。若已在根目录返回 false（由调用方决定回退到上一屏）。 */
    fun goUp(): Boolean {
        val s = _uiState.value
        if (isRoot(s.currentPath, s.rootPath)) return false
        loadDirectory(parentPath(s.currentPath, s.rootPath))
        return true
    }

    /** 重新加载当前目录。 */
    fun refresh() {
        loadDirectory(_uiState.value.currentPath)
    }

    /** 切换排序字段，并对当前列表重排。 */
    fun toggleSort(field: SortField) {
        _uiState.update {
            it.copy(sortField = field, entries = sortEntries(it.entries, field, it.sortAsc))
        }
    }

    /** 切换升/降序，并对当前列表重排。 */
    fun toggleSortOrder() {
        _uiState.update {
            val asc = !it.sortAsc
            it.copy(sortAsc = asc, entries = sortEntries(it.entries, it.sortField, asc))
        }
    }

    /** 长按可勾选视频进入多选模式并预选该项；非可勾选视频忽略。 */
    fun enterMultiSelect(seedPath: String) {
        if (!MediaFileTypes.isSelectableVideo(fileNameOf(seedPath))) return
        _uiState.update { it.copy(multiSelectMode = true, selectedPaths = setOf(seedPath)) }
    }

    /** 勾选/取消勾选某个视频路径；非可勾选视频忽略。 */
    fun toggleSelected(path: String) {
        if (!MediaFileTypes.isSelectableVideo(fileNameOf(path))) return
        _uiState.update {
            val next = if (path in it.selectedPaths) it.selectedPaths - path else it.selectedPaths + path
            it.copy(selectedPaths = next)
        }
    }

    /** 全选当前列表中的可勾选视频（保留其它目录已选中的项）。 */
    fun selectAll() {
        _uiState.update { s ->
            s.copy(selectedPaths = s.selectedPaths + currentVideoPaths(s))
        }
    }

    /** 反选当前列表中的可勾选视频（保留其它目录已选中的项）。 */
    fun invertSelection() {
        _uiState.update { s ->
            val currentVideos = currentVideoPaths(s).toSet()
            val keepOutOfView = s.selectedPaths - currentVideos
            val invertedCurrent = currentVideos - s.selectedPaths
            s.copy(selectedPaths = invertedCurrent + keepOutOfView)
        }
    }

    /** 退出多选并清空选中。 */
    fun exitMultiSelect() {
        _uiState.update { it.copy(multiSelectMode = false, selectedPaths = emptySet()) }
    }

    /** 返回当前选中的视频完整路径（供后续匹配页使用）。 */
    fun selectedVideoFiles(): List<String> = _uiState.value.selectedPaths.toList()

    /** 当前列表中可勾选视频的完整路径集合。 */
    private fun currentVideoPaths(s: UiState): List<String> =
        s.entries
            .filter { MediaFileTypes.isSelectableVideo(it.displayName ?: nameFromHref(it.href)) }
            .map { joinPath(s.currentPath, it.displayName ?: nameFromHref(it.href)) }

    /**
     * 排序：目录始终排在文件前；组内按 [field] 比较，升/降序。
     * - NAME：按 displayName 忽略大小写。
     * - SIZE：按 contentLength（null 视为最大，排末尾）。
     * - TIME：按 lastModified 字符串（RFC1123 同格式下字典序与时间序一致）。
     */
    private fun sortEntries(entries: List<WebDavEntry>, field: SortField, asc: Boolean): List<WebDavEntry> {
        val cmp = Comparator<WebDavEntry> { a, b ->
            when (field) {
                SortField.NAME -> (a.displayName ?: "").compareTo(b.displayName ?: "", ignoreCase = true)
                SortField.SIZE ->
                    (a.contentLength ?: Long.MAX_VALUE).compareTo(b.contentLength ?: Long.MAX_VALUE)
                SortField.TIME -> (a.lastModified ?: "").compareTo(b.lastModified ?: "")
            }
        }
        val ordered = if (asc) cmp else cmp.reversed()
        val dirs = entries.filter { it.isCollection }.sortedWith(ordered)
        val files = entries.filterNot { it.isCollection }.sortedWith(ordered)
        return dirs + files
    }

    companion object {
        private const val TAG = "BrowserViewModel"

        /** SubTask 1.5.6：超大目录阈值，超过仅记 warning，LazyColumn 已虚拟化无需分页。 */
        private const val LARGE_DIR_THRESHOLD = 2000
    }
}

// ---- 路径工具（供 ViewModel 与 Screen 共用） ----

/** 规范化路径：保证以 "/" 开头，去除多余末尾斜杠（根 "/" 保留）。 */
internal fun normalizePath(p: String): String {
    var s = p.trim()
    if (!s.startsWith("/")) s = "/$s"
    while (s.length > 1 && s.endsWith("/")) s = s.removeSuffix("/")
    if (s.isEmpty()) s = "/"
    return s
}

/** 拼接父路径与子段，结果规范化。 */
internal fun joinPath(parent: String, child: String): String {
    val p = normalizePath(parent)
    val c = child.trim().trim('/')
    return if (c.isEmpty()) p else normalizePath("$p/$c")
}

/** 当前路径是否即根路径。 */
internal fun isRoot(current: String, root: String): Boolean = normalizePath(current) == normalizePath(root)

/** 返回上一级路径，且不低于根。 */
internal fun parentPath(current: String, root: String): String {
    val c = normalizePath(current)
    val r = normalizePath(root)
    if (c == r || c == "/") return r
    val idx = c.lastIndexOf('/')
    val parent = if (idx <= 0) "/" else c.substring(0, idx)
    return if (parent == r || parent.length >= r.length) parent else r
}

/**
 * 计算面包屑各级：返回 (label, path) 列表，首项为根。
 * 根标签取 rootPath 末段，根为 "/" 时显示 "/"。
 */
internal fun breadcrumbs(current: String, root: String): List<Pair<String, String>> {
    val c = normalizePath(current)
    val r = normalizePath(root)
    val rootTrimmed = r.trim('/')
    val rootLabel = if (rootTrimmed.isEmpty()) "/" else rootTrimmed.substringAfterLast('/')
    val result = mutableListOf(rootLabel to r)
    if (c == r) return result
    val rel = if (c.startsWith(r)) c.removePrefix(r).trimStart('/') else c.trimStart('/')
    if (rel.isEmpty()) return result
    var acc = r
    for (seg in rel.split('/').filter { it.isNotBlank() }) {
        acc = joinPath(acc, seg)
        result.add(seg to acc)
    }
    return result
}

/** 从完整路径取末段文件名。 */
internal fun fileNameOf(path: String): String = path.trimEnd('/').substringAfterLast('/')

/** 从 WebDAV href 取末段并做最小 %20 解码（仅当 displayName 缺失时回退用）。 */
internal fun nameFromHref(href: String): String =
    href.trimEnd('/').substringAfterLast('/').replace("%20", " ")
