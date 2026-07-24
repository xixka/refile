package xa.refile.ui.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xa.refile.core.model.MediaType
import xa.refile.core.naming.BatchContext
import xa.refile.core.naming.BindingResolver
import xa.refile.core.naming.FileContext
import xa.refile.core.naming.MediaMetadata
import xa.refile.core.naming.NamingOptions
import xa.refile.core.naming.Preset
import xa.refile.core.naming.PresetRepository
import xa.refile.core.naming.TemplateEngine
import xa.refile.core.rename.CompanionRename
import xa.refile.core.rename.CompanionResolver
import xa.refile.core.rename.RenameOperation
import xa.refile.core.webdav.MediaFileTypes
import xa.refile.core.webdav.WebDavClient
import xa.refile.data.prefs.SettingsRepository
import xa.refile.data.repository.ServerRepository
import xa.refile.ui.match.MatchViewModel
import xa.refile.worker.RenameWorkScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * 重命名预览页 ViewModel（计划 §M3 Task 3.4，只预览不执行）。
 *
 * 流程：取已匹配文件（来自 [MatchSessionViewModel.matches]，经 [load] 入参传入）
 * → 对每个用 [TemplateEngine] 渲染 targetPath → [CompanionResolver] 发现伴随文件
 * → 两轮冲突检测（目标目录 PROPFIND + 同批次内重名）→ 供 UI 展示。
 *
 * 「只预览不执行」：本页不直接 MOVE/MKCOL，仅在用户确认后经 [RenameWorkScheduler]
 * 把 [RenameOperation] 列表入队 WorkManager，再导航到进度页。
 *
 * 安全：密码仅在 [ServerRepository.clientFor] 内解密用于构造 [WebDavClient]，绝不进入 UI 状态/日志。
 *
 * 依赖注入：[ServerRepository]/[SettingsRepository]/[PresetRepository]/[RenameWorkScheduler]
 * 均由 Hilt 提供；[CompanionResolver]/[TemplateEngine]/[BindingResolver] 为无状态/每项构造，不注入。
 */
@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val serverRepo: ServerRepository,
    private val settings: SettingsRepository,
    private val presetRepo: PresetRepository,
    private val workScheduler: RenameWorkScheduler,
) : ViewModel() {

    /** 预览项状态：自动✅ / 待确认⚠️ / 冲突❌。 */
    enum class PreviewStatus { AUTO, NEEDS_CONFIRM, CONFLICT }

    /**
     * 单条预览项（对应 LazyColumn 一行）。
     *
     * @property sourcePath     主文件源路径（小字灰色展示）。
     * @property targetPath     主文件目标路径（大字主题色展示；冲突时标红）。
     * @property companions     伴随文件重命名（字幕/nfo/图片，跟随主文件改名）。
     * @property mediaType      媒体类型，用于构造 [RenameOperation]。
     * @property status         当前状态（自动/待确认/冲突），由渲染与冲突检测共同决定。
     * @property warnings       模板渲染警告（缺失变量等），驱动「待确认」状态。
     * @property conflictReason 冲突原因（仅 [PreviewStatus.CONFLICT] 时非空）。
     * @property manuallyEdited 是否经用户手动修改过目标路径。
     */
    data class PreviewItem(
        val sourcePath: String,
        val targetPath: String,
        val companions: List<CompanionRename>,
        val mediaType: MediaType,
        val status: PreviewStatus,
        val warnings: List<String>,
        val conflictReason: String? = null,
        val manuallyEdited: Boolean = false,
    )

    /** 预览页 UI 状态。 */
    data class UiState(
        val loading: Boolean = false,
        val detecting: Boolean = false,
        val previewItems: List<PreviewItem> = emptyList(),
        val excludedPaths: Set<String> = emptySet(),
        val error: String? = null,
    ) {
        /** 未被排除的可见项（LazyColumn 渲染依据）。 */
        val activeItems: List<PreviewItem> get() = previewItems.filter { it.sourcePath !in excludedPaths }

        /** 自动✅ 数（仅统计可见项）。 */
        val autoCount: Int get() = activeItems.count { it.status == PreviewStatus.AUTO }

        /** 待确认⚠️ 数。 */
        val needsConfirmCount: Int get() = activeItems.count { it.status == PreviewStatus.NEEDS_CONFIRM }

        /** 冲突❌ 数。 */
        val conflictCount: Int get() = activeItems.count { it.status == PreviewStatus.CONFLICT }

        /** 已排除数。 */
        val excludedCount: Int get() = excludedPaths.size
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    @Volatile
    private var webDavClient: WebDavClient? = null

    @Volatile
    private var serverId: Long = 0L

    @Volatile
    private var initialized: Boolean = false

    /**
     * 加载预览：取服务器配置构造 [WebDavClient]，逐项渲染目标路径 + 发现伴随文件，再触发冲突检测。
     *
     * 用 [initialized] 守卫，避免 [matches] 变化导致重复加载（Activity 作用域的 matches Flow
     * 可能在首次组合时先空后非空，触发两次 [load]）。
     */
    fun load(serverId: Long, matches: List<MatchViewModel.FileMatch>) {
        if (initialized) return
        if (matches.isEmpty()) return
        initialized = true
        this.serverId = serverId
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            try {
                val entity = serverRepo.getServer(serverId)
                if (entity == null) {
                    _uiState.update { it.copy(loading = false, error = "未找到服务器配置") }
                    return@launch
                }
                val client = serverRepo.clientFor(entity)
                webDavClient = client
                // baseUrl 已含路径，浏览/重命名根固定为 "/"（不再追加 entity.rootPath，
                // 否则会把路径重复拼接）
                val rootPath = "/"

                val presetId = settings.presetId.first()
                val customTemplate = settings.templateString.first()
                val movieTemplate = settings.movieTemplateString.first()
                val episodeTemplate = settings.episodeTemplateString.first()
                val namingOptions = settings.visualOptions.first().toNamingOptions()
                val preset = Preset.byId(presetId)
                val today = LocalDate.now().toString()

                val items = matches.map { fm ->
                    // 测试反馈 Item 9：优先用对应类型的独立模板，回退到旧版单模板，再回退到预设。
                    val resolvedTemplate = when {
                        fm.parsed.season != null || fm.parsed.episodes.isNotEmpty() ->
                            episodeTemplate.takeIf { it.isNotBlank() }
                                ?: customTemplate.takeIf { it.isNotBlank() }
                        else ->
                            movieTemplate.takeIf { it.isNotBlank() }
                                ?: customTemplate.takeIf { it.isNotBlank() }
                    }
                    renderItem(client, fm, rootPath, preset, resolvedTemplate, namingOptions, today)
                }
                _uiState.update { it.copy(loading = false, previewItems = items) }
                detectConflicts()
            } catch (t: Throwable) {
                _uiState.update { it.copy(loading = false, error = "加载预览失败：${t.message ?: "未知错误"}") }
            }
        }
    }

    /**
     * 渲染单个文件的目标路径并发现伴随文件。
     *
     * 模板字符串优先取用户自定义（[SettingsRepository.templateString]），为空则按预设 ID 与
     * 媒体类型（电影/剧集）从 [PresetRepository.templateFor] 取内置预设模板。
     * 渲染结果为相对库根的路径（如 `Movies/The Movie (2023)/The Movie (2023)`），
     * 追加主文件扩展名后拼到 [rootPath] 之下得到完整目标路径。
     *
     * 渲染为空（缺关键变量）时保持源路径不变，并标 [PreviewStatus.NEEDS_CONFIRM]。
     */
    private suspend fun renderItem(
        client: WebDavClient,
        fm: MatchViewModel.FileMatch,
        rootPath: String,
        preset: Preset,
        resolvedTemplate: String,
        namingOptions: NamingOptions,
        today: String,
    ): PreviewItem {
        val fileName = fm.filePath.substringAfterLast('/')
        val ext = MediaFileTypes.extension(fileName) ?: ""
        val media = fm.matched ?: MediaMetadata(
            type = if (fm.parsed.season != null || fm.parsed.episodes.isNotEmpty()) {
                MediaType.EPISODE
            } else {
                MediaType.MOVIE
            },
        )
        val isEpisode = media.isEpisode
        // resolvedTemplate 为空时回退到内置预设对应类型的模板。
        val template = resolvedTemplate.takeIf { it.isNotBlank() }
            ?: presetRepo.templateFor(preset, isEpisode)
        val fileCtx = FileContext(
            displayName = fileName,
            ext = ext,
            fullPath = fm.filePath,
            parsed = fm.parsed,
        )
        val batchCtx = BatchContext(today = today)
        val resolver = BindingResolver(media, fileCtx, batchCtx, namingOptions)
        val engine = TemplateEngine(resolver, namingOptions)
        val rendered = engine.render(template)
        val targetRel = rendered.path
        val targetFull = if (targetRel.isNotBlank()) {
            val withExt = if (ext.isBlank()) targetRel else "$targetRel.$ext"
            joinPath(rootPath, withExt)
        } else {
            // 渲染为空（缺关键变量）→ 保持源路径，标待确认
            fm.filePath
        }
        val companions = try {
            CompanionResolver(client).resolve(fm.filePath, targetFull)
        } catch (e: Exception) {
            emptyList()
        }
        val status = if (rendered.warnings.isNotEmpty() || targetRel.isBlank()) {
            PreviewStatus.NEEDS_CONFIRM
        } else {
            PreviewStatus.AUTO
        }
        return PreviewItem(
            sourcePath = fm.filePath,
            targetPath = targetFull,
            companions = companions,
            mediaType = if (isEpisode) MediaType.EPISODE else MediaType.MOVIE,
            status = status,
            warnings = rendered.warnings,
        )
    }

    /**
     * SubTask 3.4.2：两轮冲突检测。
     *
     * 第一轮：对每个唯一目标父目录发 PROPFIND Depth 1，收集已存在文件名；目标名命中即冲突。
     * 第二轮：统计同批次内相同 targetPath，出现 >1 次即冲突（同批次重名）。
     *
     * 目标与源同路径（未改名）不算冲突。冲突项标 [PreviewStatus.CONFLICT] 并填 [PreviewItem.conflictReason]。
     */
    fun detectConflicts() {
        val client = webDavClient ?: return
        val items = _uiState.value.previewItems
        if (items.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(detecting = true) }
            try {
                // 第一轮：PROPFIND 各目标父目录，收集已存在文件名（按目录分组）。
                val targetDirs = items.map { parentDir(it.targetPath) }.distinct()
                val existingNames = mutableMapOf<String, Set<String>>()
                for (dir in targetDirs) {
                    val entries = try {
                        client.propfind(dir, 1)
                    } catch (e: Exception) {
                        emptyList()
                    }
                    existingNames[dir] = entries
                        .filterNot { it.isCollection }
                        .mapNotNull { entry ->
                            entry.displayName?.takeIf { n -> n.isNotEmpty() }
                                ?: nameFromHref(entry.href)
                        }
                        .toSet()
                }
                // 第二轮：同批次内重名统计。
                val targetCounts = items.groupingBy { it.targetPath }.eachCount()

                val updated = items.map { item ->
                    val dir = parentDir(item.targetPath)
                    val targetName = fileNameOf(item.targetPath)
                    val existsOnServer = existingNames[dir]?.contains(targetName) == true
                    val isDuplicate = (targetCounts[item.targetPath] ?: 0) > 1
                    // 目标与源同路径（未改名）不算冲突
                    val unchanged = item.targetPath == item.sourcePath
                    when {
                        unchanged -> item.copy(
                            status = if (item.warnings.isNotEmpty()) PreviewStatus.NEEDS_CONFIRM else PreviewStatus.AUTO,
                            conflictReason = null,
                        )
                        existsOnServer -> item.copy(
                            status = PreviewStatus.CONFLICT,
                            conflictReason = "目标路径在服务器已存在",
                        )
                        isDuplicate -> item.copy(
                            status = PreviewStatus.CONFLICT,
                            conflictReason = "批次内目标路径重复",
                        )
                        item.warnings.isNotEmpty() -> item.copy(
                            status = PreviewStatus.NEEDS_CONFIRM,
                            conflictReason = null,
                        )
                        else -> item.copy(
                            status = PreviewStatus.AUTO,
                            conflictReason = null,
                        )
                    }
                }
                _uiState.update { it.copy(previewItems = updated, detecting = false) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(detecting = false, error = "冲突检测失败：${t.message ?: "未知错误"}") }
            }
        }
    }

    /**
     * SubTask 3.4.2：一键自动加序号后缀解决冲突。
     *
     * 对每个冲突项，在主文件名扩展名前插入 ` (n)`（n 从 1 递增）直到目标既不与同批次已占用
     * 目标重复、也不与服务器已存在文件名重复。解决后状态置 [PreviewStatus.AUTO] 并标记手动编辑。
     */
    fun autoResolveConflicts() {
        val items = _uiState.value.previewItems
        if (items.none { it.status == PreviewStatus.CONFLICT }) return
        val client = webDavClient
        viewModelScope.launch {
            _uiState.update { it.copy(detecting = true) }
            try {
                // 收集服务器已存在文件名（按目标父目录），用于避免新后缀仍撞名。
                val existingNames = mutableMapOf<String, MutableSet<String>>()
                if (client != null) {
                    val dirs = items.map { parentDir(it.targetPath) }.distinct()
                    for (dir in dirs) {
                        val entries = try {
                            client.propfind(dir, 1)
                        } catch (e: Exception) {
                            emptyList()
                        }
                        existingNames[dir] = entries
                            .filterNot { it.isCollection }
                            .mapNotNull { entry ->
                                entry.displayName?.takeIf { n -> n.isNotEmpty() }
                                    ?: nameFromHref(entry.href)
                            }
                            .toMutableSet()
                    }
                }
                // 非冲突项目标先占位
                val usedTargets = mutableSetOf<String>()
                items.filter { it.status != PreviewStatus.CONFLICT }.forEach { usedTargets.add(it.targetPath) }

                val resolved = items.map { item ->
                    if (item.status != PreviewStatus.CONFLICT) return@map item
                    val dir = parentDir(item.targetPath)
                    val existing = existingNames[dir] ?: mutableSetOf()
                    var attempt = item.targetPath
                    var n = 1
                    while (attempt in usedTargets || existing.contains(fileNameOf(attempt))) {
                        attempt = appendSuffix(item.targetPath, n)
                        n++
                    }
                    usedTargets.add(attempt)
                    existing.add(fileNameOf(attempt))
                    item.copy(
                        targetPath = attempt,
                        status = PreviewStatus.AUTO,
                        conflictReason = null,
                        manuallyEdited = true,
                    )
                }
                _uiState.update { it.copy(previewItems = resolved, detecting = false) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(detecting = false, error = "解决冲突失败：${t.message ?: "未知错误"}") }
            }
        }
    }

    /** SubTask 3.4.3：左滑排除单条（加入 [UiState.excludedPaths]，从可见列表移除）。 */
    fun excludeItem(sourcePath: String) {
        _uiState.update { it.copy(excludedPaths = it.excludedPaths + sourcePath) }
    }

    /** 恢复已排除的单条（从 [UiState.excludedPaths] 移除，重新出现在可见列表）。 */
    fun includeItem(sourcePath: String) {
        _uiState.update { it.copy(excludedPaths = it.excludedPaths - sourcePath) }
    }

    /**
     * SubTask 3.4.3：手动修改单条目标路径，修改后重新触发冲突检测。
     */
    fun editItemTarget(sourcePath: String, newTarget: String) {
        val trimmed = newTarget.trim()
        if (trimmed.isEmpty()) return
        val items = _uiState.value.previewItems
        val updated = items.map { item ->
            if (item.sourcePath == sourcePath) {
                item.copy(targetPath = trimmed, manuallyEdited = true)
            } else {
                item
            }
        }
        _uiState.update { it.copy(previewItems = updated) }
        detectConflicts()
    }

    /**
     * 入队执行：把可见、非冲突项构造为 [RenameOperation] 列表经 [RenameWorkScheduler] 入队。
     *
     * 返回 workId（UUID 字符串）供 UI 导航到进度页；无可执行项或仍有冲突时返回 null 并写错误状态。
     * 本页不直接 MOVE/MKCOL（只预览不执行），实际执行由 [xa.refile.worker.RenameWorker] 完成。
     */
    fun enqueueRename(): String? {
        val state = _uiState.value
        if (state.conflictCount > 0) {
            _uiState.update { it.copy(error = "存在 ${state.conflictCount} 个冲突，请先解决") }
            return null
        }
        val items = state.activeItems
        if (items.isEmpty()) {
            _uiState.update { it.copy(error = "无可执行的重命名项") }
            return null
        }
        return try {
            val ops = items.map {
                RenameOperation(it.sourcePath, it.targetPath, it.companions, it.mediaType)
            }
            workScheduler.enqueue(serverId, ops, batchName = "重命名 ${items.size} 项").toString()
        } catch (t: Throwable) {
            _uiState.update { it.copy(error = "入队失败：${t.message ?: "未知错误"}") }
            null
        }
    }

    /** 清除一次性错误提示。 */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // ---- 路径工具（与 BrowserViewModel 同语义的本地实现，避免跨包依赖） ----

    /** 规范化路径：保证以 "/" 开头，去除多余末尾斜杠（根 "/" 保留）。 */
    private fun normalizePath(p: String): String {
        var s = p.trim()
        if (!s.startsWith("/")) s = "/$s"
        while (s.length > 1 && s.endsWith("/")) s = s.removeSuffix("/")
        if (s.isEmpty()) s = "/"
        return s
    }

    /** 拼接目录与子路径（子路径可含 `/` 分层）。根目录 "/" 时不产生重复斜杠。 */
    private fun joinPath(dir: String, child: String): String {
        val d = normalizePath(dir)
        val c = child.trim().trimStart('/')
        if (c.isEmpty()) return d
        val base = if (d == "/") "" else d
        return normalizePath("$base/$c")
    }

    /** 取路径的父目录。无 `/` 或仅根 `/` 时返回 `/`。 */
    private fun parentDir(path: String): String {
        val idx = path.lastIndexOf('/')
        return if (idx <= 0) "/" else path.substring(0, idx)
    }

    /** 取路径末段文件名。 */
    private fun fileNameOf(path: String): String = path.trimEnd('/').substringAfterLast('/')

    /** 从 WebDAV href 取末段并做最小 %20 解码（仅当 displayName 缺失时回退用）。 */
    private fun nameFromHref(href: String): String =
        href.trimEnd('/').substringAfterLast('/').replace("%20", " ")

    /** 在文件名扩展名前插入 ` (n)` 后缀：`/d/a.mkv` → `/d/a (1).mkv`。无扩展名则追加到末尾。 */
    private fun appendSuffix(path: String, n: Int): String {
        val dir = parentDir(path)
        val name = fileNameOf(path)
        val dot = name.lastIndexOf('.')
        val (base, ext) = if (dot > 0) name.substring(0, dot) to name.substring(dot) else name to ""
        return joinPath(dir, "$base ($n)$ext")
    }
}
