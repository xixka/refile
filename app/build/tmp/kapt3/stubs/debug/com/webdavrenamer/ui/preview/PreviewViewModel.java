package com.webdavrenamer.ui.preview;

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
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0017\n\u0002\u0010\u0008\n\u0002\u0008\u0004\u0008\u0007\u0012\u0001\u0000\u0018\u0000:\u0003ABCB!\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0003\u0012\u0004\u0010\u0008(\u0004\u00A2\u0006\u0004\u0008\n\u0010\u000BJ\u0010\u0010\u00192\u0004\u0010\u0015(\t2\u0004\u0010\u001B(\r8\u000BJ6\u0010\u001E2\u0004\u0010 (\u000F2\u0004\u0010!(\u000C2\u0004\u0010\"(\u00102\u0004\u0010$(\u00112\u0004\u0010&(\u00102\u0004\u0010'(\u00122\u0004\u0010)(\u00108\u000EH\u0082@\u00A2\u0006\u0002\u0010*J\u0004\u0010+8\u000BJ\u0004\u0010,8\u000BJ\n\u0010-2\u0004\u0010.(\u00108\u000BJ\n\u0010/2\u0004\u0010.(\u00108\u000BJ\u0010\u001002\u0004\u0010.(\u00102\u0004\u00101(\u00108\u000BJ\u0004\u001028\u0013J\u0004\u001038\u000BJ\u000C\u001042\u0004\u00105(\u00108\u0010H\u0002J\u0012\u001062\u0004\u00107(\u00102\u0004\u00108(\u00108\u0010H\u0002J\u000C\u001092\u0004\u0010:(\u00108\u0010H\u0002J\u000C\u0010;2\u0004\u0010:(\u00108\u0010H\u0002J\u000C\u0010<2\u0004\u0010=(\u00108\u0010H\u0002J\u0012\u0010>2\u0004\u0010:(\u00102\u0004\u0010?(\u00148\u0010H\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0006H\u0003X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u000CH\u0006X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u000FH\u0007\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0011\u0010\u0012R\u000C\u0010\u0013H\u0008X\u0082\u000E\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0015H\tX\u0082\u000E\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0017H\nX\u0082\u000E\u00A2\u0006\u0002\n\u0000\u00F2\u0001d\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\t\n\u00020\u000E\n\u0006\u0012\u0002\u0018\u00050\r\n\u0006\u0012\u0002\u0018\u00050\u0010\n\u0004\u0018\u00010\u0014\n\u00020\u0016\n\u00020\u0018\n\u00020\u001A\n\u00020\u001D\n\u0006\u0012\u0002\u0018\u000C0\u001C\n\u00020\u001F\n\u00020\u0014\n\u00020#\n\u00020%\n\u00020(\n\u0004\u0018\u00010#\n\u00020@\u00A8\u0006D"}, d2 = {"Lcom/webdavrenamer/ui/preview/PreviewViewModel;", "Landroidx/lifecycle/ViewModel;", "serverRepo", "Lcom/webdavrenamer/data/repository/ServerRepository;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "presetRepo", "Lcom/webdavrenamer/core/naming/PresetRepository;", "workScheduler", "Lcom/webdavrenamer/worker/RenameWorkScheduler;", "<init>", "(Lcom/webdavrenamer/data/repository/ServerRepository;Lcom/webdavrenamer/data/prefs/SettingsRepository;Lcom/webdavrenamer/core/naming/PresetRepository;Lcom/webdavrenamer/worker/RenameWorkScheduler;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/webdavrenamer/ui/preview/PreviewViewModel$UiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "webDavClient", "Lcom/webdavrenamer/core/webdav/WebDavClient;", "serverId", "", "initialized", "", "load", "", "matches", "", "Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "renderItem", "Lcom/webdavrenamer/ui/preview/PreviewViewModel$PreviewItem;", "client", "fm", "rootPath", "", "preset", "Lcom/webdavrenamer/core/naming/Preset;", "customTemplate", "namingOptions", "Lcom/webdavrenamer/core/naming/NamingOptions;", "today", "(Lcom/webdavrenamer/core/webdav/WebDavClient;Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;Ljava/lang/String;Lcom/webdavrenamer/core/naming/Preset;Ljava/lang/String;Lcom/webdavrenamer/core/naming/NamingOptions;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "detectConflicts", "autoResolveConflicts", "excludeItem", "sourcePath", "includeItem", "editItemTarget", "newTarget", "enqueueRename", "clearError", "normalizePath", "p", "joinPath", "dir", "child", "parentDir", "path", "fileNameOf", "nameFromHref", "href", "appendSuffix", "n", "", "PreviewStatus", "PreviewItem", "UiState", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class PreviewViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.ServerRepository serverRepo = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.naming.PresetRepository presetRepo = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.worker.RenameWorkScheduler workScheduler = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.ui.preview.PreviewViewModel.UiState> _uiState = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.preview.PreviewViewModel.UiState> uiState = null;

    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile com.webdavrenamer.core.webdav.WebDavClient webDavClient = null;

    @kotlin.jvm.Volatile()
    private volatile long serverId = 0L;

    @kotlin.jvm.Volatile()
    private volatile boolean initialized = false;

    @javax.inject.Inject()
    public PreviewViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.ServerRepository serverRepo, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.PresetRepository presetRepo, @org.jetbrains.annotations.NotNull() com.webdavrenamer.worker.RenameWorkScheduler workScheduler) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.preview.PreviewViewModel.UiState> getUiState() {
        return null;
    }

    /**
     * 加载预览：取服务器配置构造 [WebDavClient]，逐项渲染目标路径 + 发现伴随文件，再触发冲突检测。
     * 
     * 用 [initialized] 守卫，避免 [matches] 变化导致重复加载（Activity 作用域的 matches Flow
     * 可能在首次组合时先空后非空，触发两次 [load]）。
     */
    public final void load(long serverId, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> matches) {
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
    private final java.lang.Object renderItem(com.webdavrenamer.core.webdav.WebDavClient client, com.webdavrenamer.ui.match.MatchViewModel.FileMatch fm, java.lang.String rootPath, com.webdavrenamer.core.naming.Preset preset, java.lang.String customTemplate, com.webdavrenamer.core.naming.NamingOptions namingOptions, java.lang.String today, kotlin.coroutines.Continuation<? super com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem> $completion) {
        return null;
    }

    /**
     * SubTask 3.4.2：两轮冲突检测。
     * 
     * 第一轮：对每个唯一目标父目录发 PROPFIND Depth 1，收集已存在文件名；目标名命中即冲突。
     * 第二轮：统计同批次内相同 targetPath，出现 >1 次即冲突（同批次重名）。
     * 
     * 目标与源同路径（未改名）不算冲突。冲突项标 [PreviewStatus.CONFLICT] 并填 [PreviewItem.conflictReason]。
     */
    public final void detectConflicts() {
    }

    /**
     * SubTask 3.4.2：一键自动加序号后缀解决冲突。
     * 
     * 对每个冲突项，在主文件名扩展名前插入 ` (n)`（n 从 1 递增）直到目标既不与同批次已占用
     * 目标重复、也不与服务器已存在文件名重复。解决后状态置 [PreviewStatus.AUTO] 并标记手动编辑。
     */
    public final void autoResolveConflicts() {
    }

    /**
     * SubTask 3.4.3：左滑排除单条（加入 [UiState.excludedPaths]，从可见列表移除）。
     */
    public final void excludeItem(@org.jetbrains.annotations.NotNull() java.lang.String sourcePath) {
    }

    /**
     * 恢复已排除的单条（从 [UiState.excludedPaths] 移除，重新出现在可见列表）。
     */
    public final void includeItem(@org.jetbrains.annotations.NotNull() java.lang.String sourcePath) {
    }

    /**
     * SubTask 3.4.3：手动修改单条目标路径，修改后重新触发冲突检测。
     */
    public final void editItemTarget(@org.jetbrains.annotations.NotNull() java.lang.String sourcePath, @org.jetbrains.annotations.NotNull() java.lang.String newTarget) {
    }

    /**
     * 入队执行：把可见、非冲突项构造为 [RenameOperation] 列表经 [RenameWorkScheduler] 入队。
     * 
     * 返回 workId（UUID 字符串）供 UI 导航到进度页；无可执行项或仍有冲突时返回 null 并写错误状态。
     * 本页不直接 MOVE/MKCOL（只预览不执行），实际执行由 [com.webdavrenamer.worker.RenameWorker] 完成。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String enqueueRename() {
        return null;
    }

    /**
     * 清除一次性错误提示。
     */
    public final void clearError() {
    }

    /**
     * 规范化路径：保证以 "/" 开头，去除多余末尾斜杠（根 "/" 保留）。
     */
    private final java.lang.String normalizePath(java.lang.String p) {
        return null;
    }

    /**
     * 拼接目录与子路径（子路径可含 `/` 分层）。根目录 "/" 时不产生重复斜杠。
     */
    private final java.lang.String joinPath(java.lang.String dir, java.lang.String child) {
        return null;
    }

    /**
     * 取路径的父目录。无 `/` 或仅根 `/` 时返回 `/`。
     */
    private final java.lang.String parentDir(java.lang.String path) {
        return null;
    }

    /**
     * 取路径末段文件名。
     */
    private final java.lang.String fileNameOf(java.lang.String path) {
        return null;
    }

    /**
     * 从 WebDAV href 取末段并做最小 %20 解码（仅当 displayName 缺失时回退用）。
     */
    private final java.lang.String nameFromHref(java.lang.String href) {
        return null;
    }

    /**
     * 在文件名扩展名前插入 ` (n)` 后缀：`/d/a.mkv` → `/d/a (1).mkv`。无扩展名则追加到末尾。
     */
    private final java.lang.String appendSuffix(java.lang.String path, int n) {
        return null;
    }

    /**
     * 预览项状态：自动✅ / 待确认⚠️ / 冲突❌。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0006\u0008\u0086\u0081\u0002\u0012\u0001\u0001\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005j\u0002\u0008\u0006\u00F2\u0001\u000C\n\u00020\u0000\n\u0006\u0012\u0002\u0018\u00000\u0001\u00A8\u0006\u0007"}, d2 = {"Lcom/webdavrenamer/ui/preview/PreviewViewModel$PreviewStatus;", "", "<init>", "(Ljava/lang/String;I)V", "AUTO", "NEEDS_CONFIRM", "CONFLICT", "app_debug"}, xs= "", pn = "", xi = 48)
    public static enum PreviewStatus {
        AUTO,
        NEEDS_CONFIRM,
        CONFLICT;


        PreviewStatus() {
        }

        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.webdavrenamer.ui.preview.PreviewViewModel.PreviewStatus> getEntries() {
            return null;
        }
    }
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
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0002\u0008\u000E\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B;\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0004\u0010\u0005(\u0003\u0012\u0004\u0010\u0008(\u0004\u0012\u0004\u0010\n(\u0005\u0012\u0004\u0010\u000C(\u0006\u0012\u0006\u0008\u0002\u0010\r(\u0007\u0012\u0006\u0008\u0002\u0010\u000E(\u0008\u00A2\u0006\u0004\u0008\u0010\u0010\u0011J\u0007\u0010\u00128\u0001H\u00C6\u0003J\u0007\u0010\u00138\u0001H\u00C6\u0003J\u0007\u0010\u00148\u0003H\u00C6\u0003J\u0007\u0010\u00158\u0004H\u00C6\u0003J\u0007\u0010\u00168\u0005H\u00C6\u0003J\u0007\u0010\u00178\u0006H\u00C6\u0003J\u0007\u0010\u00188\u0007H\u00C6\u0003J\u0007\u0010\u00198\u0008H\u00C6\u0003JG\u0010\u001A2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00032\u0006\u0008\u0002\u0010\u0008(\u00042\u0006\u0008\u0002\u0010\n(\u00052\u0006\u0008\u0002\u0010\u000C(\u00062\u0006\u0008\u0002\u0010\r(\u00072\u0006\u0008\u0002\u0010\u000E(\u00088\tH\u00C6\u0001J\r\u0010\u001B2\u0004\u0010\u001C(\n8\u0008H\u00D6\u0003J\u0007\u0010\u001D8\u000BH\u00D6\u0001J\u0007\u0010\u001F8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\u000CH\u0006\u00A2\u0006\u0002\n\u0000R\t\u0010\rH\u0007\u00A2\u0006\u0002\n\u0000R\t\u0010\u000EH\u0008\u00A2\u0006\u0002\n\u0000\u00F2\u0001<\n\u00020\u0001\n\u00020\u0003\n\u00020\u0007\n\u0006\u0012\u0002\u0018\u00020\u0006\n\u00020\t\n\u00020\u000B\n\u0006\u0012\u0002\u0018\u00010\u0006\n\u0004\u0018\u00010\u0003\n\u00020\u000F\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020\u001E\u00A8\u0006 "}, d2 = {"Lcom/webdavrenamer/ui/preview/PreviewViewModel$PreviewItem;", "", "sourcePath", "", "targetPath", "companions", "", "Lcom/webdavrenamer/core/rename/CompanionRename;", "mediaType", "Lcom/webdavrenamer/core/model/MediaType;", "status", "Lcom/webdavrenamer/ui/preview/PreviewViewModel$PreviewStatus;", "warnings", "conflictReason", "manuallyEdited", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lcom/webdavrenamer/core/model/MediaType;Lcom/webdavrenamer/ui/preview/PreviewViewModel$PreviewStatus;Ljava/util/List;Ljava/lang/String;Z)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class PreviewItem {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String sourcePath = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String targetPath = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.core.rename.CompanionRename> companions = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.core.model.MediaType mediaType = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.ui.preview.PreviewViewModel.PreviewStatus status = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.String> warnings = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String conflictReason = null;

        private final boolean manuallyEdited = false;

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
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem copy(@org.jetbrains.annotations.NotNull() java.lang.String sourcePath, @org.jetbrains.annotations.NotNull() java.lang.String targetPath, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.core.rename.CompanionRename> companions, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType mediaType, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.preview.PreviewViewModel.PreviewStatus status, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> warnings, @org.jetbrains.annotations.Nullable() java.lang.String conflictReason, boolean manuallyEdited) {
            return null;
        }

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
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

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
        public int hashCode() {
            return 0;
        }

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
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public PreviewItem(@org.jetbrains.annotations.NotNull() java.lang.String sourcePath, @org.jetbrains.annotations.NotNull() java.lang.String targetPath, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.core.rename.CompanionRename> companions, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType mediaType, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.preview.PreviewViewModel.PreviewStatus status, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> warnings, @org.jetbrains.annotations.Nullable() java.lang.String conflictReason, boolean manuallyEdited) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSourcePath() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTargetPath() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.core.rename.CompanionRename> component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.core.rename.CompanionRename> getCompanions() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.model.MediaType component4() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.model.MediaType getMediaType() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.preview.PreviewViewModel.PreviewStatus component5() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.preview.PreviewViewModel.PreviewStatus getStatus() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> component6() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getWarnings() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component7() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getConflictReason() {
            return null;
        }

        public final boolean component8() {
            return false;
        }

        public final boolean getManuallyEdited() {
            return false;
        }
    }
    /**
     * 预览页 UI 状态。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000E\n\u0002\u0008\u0007\n\u0002\u0010\u0008\n\u0002\u0008\u0013\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B/\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0006\u0008\u0002\u0010\u0004(\u0001\u0012\u0006\u0008\u0002\u0010\u0005(\u0003\u0012\u0006\u0008\u0002\u0010\u0008(\u0005\u0012\u0006\u0008\u0002\u0010\u000B(\u0006\u00A2\u0006\u0004\u0008\u000C\u0010\rJ\u0007\u0010\u001B8\u0001H\u00C6\u0003J\u0007\u0010\u001C8\u0001H\u00C6\u0003J\u0007\u0010\u001D8\u0003H\u00C6\u0003J\u0007\u0010\u001E8\u0005H\u00C6\u0003J\u0007\u0010\u001F8\u0006H\u00C6\u0003J/\u0010 2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00032\u0006\u0008\u0002\u0010\u0008(\u00052\u0006\u0008\u0002\u0010\u000B(\u00068\u0008H\u00C6\u0001J\r\u0010!2\u0004\u0010\"(\t8\u0001H\u00D6\u0003J\u0007\u0010#8\u0007H\u00D6\u0001J\u0007\u0010$8\u0004H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0006\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u000E8FH\u0003\u00A2\u0006\u0006\u001A\u0004\u0008\u000F\u0010\u0010R\u000F\u0010\u00118FH\u0007\u00A2\u0006\u0006\u001A\u0004\u0008\u0013\u0010\u0014R\u000F\u0010\u00158FH\u0007\u00A2\u0006\u0006\u001A\u0004\u0008\u0016\u0010\u0014R\u000F\u0010\u00178FH\u0007\u00A2\u0006\u0006\u001A\u0004\u0008\u0018\u0010\u0014R\u000F\u0010\u00198FH\u0007\u00A2\u0006\u0006\u001A\u0004\u0008\u001A\u0010\u0014\u00F2\u00014\n\u00020\u0001\n\u00020\u0003\n\u00020\u0007\n\u0006\u0012\u0002\u0018\u00020\u0006\n\u00020\n\n\u0006\u0012\u0002\u0018\u00040\t\n\u0004\u0018\u00010\n\n\u00020\u0012\n\u00020\u0000\n\u0004\u0018\u00010\u0001\u00A8\u0006%"}, d2 = {"Lcom/webdavrenamer/ui/preview/PreviewViewModel$UiState;", "", "loading", "", "detecting", "previewItems", "", "Lcom/webdavrenamer/ui/preview/PreviewViewModel$PreviewItem;", "excludedPaths", "", "", "error", "<init>", "(ZZLjava/util/List;Ljava/util/Set;Ljava/lang/String;)V", "activeItems", "getActiveItems", "()Ljava/util/List;", "autoCount", "", "getAutoCount", "()I", "needsConfirmCount", "getNeedsConfirmCount", "conflictCount", "getConflictCount", "excludedCount", "getExcludedCount", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class UiState {
        private final boolean loading = false;

        private final boolean detecting = false;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem> previewItems = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.Set<java.lang.String> excludedPaths = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String error = null;

        public UiState() {
            super();
        }

        /**
         * 预览页 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.preview.PreviewViewModel.UiState copy(boolean loading, boolean detecting, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem> previewItems, @org.jetbrains.annotations.NotNull() java.util.Set<java.lang.String> excludedPaths, @org.jetbrains.annotations.Nullable() java.lang.String error) {
            return null;
        }

        /**
         * 预览页 UI 状态。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 预览页 UI 状态。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 预览页 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public UiState(boolean loading, boolean detecting, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem> previewItems, @org.jetbrains.annotations.NotNull() java.util.Set<java.lang.String> excludedPaths, @org.jetbrains.annotations.Nullable() java.lang.String error) {
            super();
        }

        public final boolean component1() {
            return false;
        }

        public final boolean getLoading() {
            return false;
        }

        public final boolean component2() {
            return false;
        }

        public final boolean getDetecting() {
            return false;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem> component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem> getPreviewItems() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.Set<java.lang.String> component4() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.Set<java.lang.String> getExcludedPaths() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component5() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getError() {
            return null;
        }

        /**
         * 未被排除的可见项（LazyColumn 渲染依据）。
         */
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem> getActiveItems() {
            return null;
        }

        /**
         * 自动✅ 数（仅统计可见项）。
         */
        public final int getAutoCount() {
            return 0;
        }

        /**
         * 待确认⚠️ 数。
         */
        public final int getNeedsConfirmCount() {
            return 0;
        }

        /**
         * 冲突❌ 数。
         */
        public final int getConflictCount() {
            return 0;
        }

        /**
         * 已排除数。
         */
        public final int getExcludedCount() {
            return 0;
        }
    }
}
