package com.webdavrenamer.ui.browser;

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
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u000B\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0008\n\u0002\u0010 \n\u0002\u0008\t\u0008\u0007\u0012\u0001\u0000\u0018\u0000 3:\u0003123B\u0015\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\u0012\u0010\u00112\u0004\u0010\u0013(\u00088\u0007H\u0086@\u00A2\u0006\u0002\u0010\u0015J\n\u0010\u00162\u0004\u0010\u0017(\t8\u0007J\n\u0010\u00192\u0004\u0010\u001A(\n8\u0007J\n\u0010\u001C2\u0004\u0010\u0017(\t8\u0007J\u0004\u0010\u001D8\u000BJ\u0004\u0010\u001F8\u0007J\n\u0010 2\u0004\u0010!(\u000C8\u0007J\u0004\u0010#8\u0007J\n\u0010$2\u0004\u0010%(\t8\u0007J\n\u0010&2\u0004\u0010\u0017(\t8\u0007J\u0004\u0010'8\u0007J\u0004\u0010(8\u0007J\u0004\u0010)8\u0007J\u0004\u0010*8\rJ\u000C\u0010,2\u0004\u0010-(\u00038\rH\u0002J\u0018\u0010.2\u0004\u0010/(\u000E2\u0004\u0010!(\u000C2\u0004\u00100(\u000B8\u000EH\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u000BH\u0005\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\r\u0010\u000ER\u000C\u0010\u000FH\u0006X\u0082\u000E\u00A2\u0006\u0002\n\u0000\u00F2\u0001N\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\n\n\u0006\u0012\u0002\u0018\u00030\t\n\u0006\u0012\u0002\u0018\u00030\u000C\n\u0004\u0018\u00010\u0010\n\u00020\u0012\n\u00020\u0014\n\u00020\u0018\n\u00020\u001B\n\u00020\u001E\n\u00020\"\n\u0006\u0012\u0002\u0018\t0+\n\u0006\u0012\u0002\u0018\n0+\u00A8\u00064"}, d2 = {"Lcom/webdavrenamer/ui/browser/BrowserViewModel;", "Landroidx/lifecycle/ViewModel;", "serverRepo", "Lcom/webdavrenamer/data/repository/ServerRepository;", "crypto", "Lcom/webdavrenamer/data/crypto/KeystoreCrypto;", "<init>", "(Lcom/webdavrenamer/data/repository/ServerRepository;Lcom/webdavrenamer/data/crypto/KeystoreCrypto;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/webdavrenamer/ui/browser/BrowserViewModel$UiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "webDavClient", "Lcom/webdavrenamer/core/webdav/WebDavClient;", "init", "", "serverId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadDirectory", "path", "", "navigateInto", "entry", "Lcom/webdavrenamer/core/webdav/WebDavEntry;", "navigateToBreadcrumb", "goUp", "", "refresh", "toggleSort", "field", "Lcom/webdavrenamer/ui/browser/BrowserViewModel$SortField;", "toggleSortOrder", "enterMultiSelect", "seedPath", "toggleSelected", "selectAll", "invertSelection", "exitMultiSelect", "selectedVideoFiles", "", "currentVideoPaths", "s", "sortEntries", "entries", "asc", "SortField", "UiState", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class BrowserViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.ServerRepository serverRepo = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.crypto.KeystoreCrypto crypto = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.ui.browser.BrowserViewModel.UiState> _uiState = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.browser.BrowserViewModel.UiState> uiState = null;

    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile com.webdavrenamer.core.webdav.WebDavClient webDavClient = null;

    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.ui.browser.BrowserViewModel.Companion Companion = null;

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "BrowserViewModel";

    /**
     * SubTask 1.5.6：超大目录阈值，超过仅记 warning，LazyColumn 已虚拟化无需分页。
     */
    private static final int LARGE_DIR_THRESHOLD = 2000;

    @javax.inject.Inject()
    public BrowserViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.ServerRepository serverRepo, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.crypto.KeystoreCrypto crypto) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.browser.BrowserViewModel.UiState> getUiState() {
        return null;
    }

    /**
     * 取服务器配置，构造 [WebDavClient] 并加载根目录。
     * 构造 baseUrl 的方式与 [ServerRepository] 内部 buildFullBaseUrl 一致。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object init(long serverId, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 对 [path] 发 PROPFIND Depth 1，过滤掉返回的第一项（当前目录本身）并排序后写入状态。
     * 
     * SubTask 1.5.6：PROPFIND 一次性返回，列表项 > [LARGE_DIR_THRESHOLD] 时仅记 warning；
     * LazyColumn 自身虚拟化，无需分页。
     */
    public final void loadDirectory(@org.jetbrains.annotations.NotNull() java.lang.String path) {
    }

    /**
     * 进入子目录。非目录忽略。
     */
    public final void navigateInto(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.webdav.WebDavEntry entry) {
    }

    /**
     * 跳到面包屑某层。
     */
    public final void navigateToBreadcrumb(@org.jetbrains.annotations.NotNull() java.lang.String path) {
    }

    /**
     * 返回上一级。若已在根目录返回 false（由调用方决定回退到上一屏）。
     */
    public final boolean goUp() {
        return false;
    }

    /**
     * 重新加载当前目录。
     */
    public final void refresh() {
    }

    /**
     * 切换排序字段，并对当前列表重排。
     */
    public final void toggleSort(@org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.browser.BrowserViewModel.SortField field) {
    }

    /**
     * 切换升/降序，并对当前列表重排。
     */
    public final void toggleSortOrder() {
    }

    /**
     * 长按可勾选视频进入多选模式并预选该项；非可勾选视频忽略。
     */
    public final void enterMultiSelect(@org.jetbrains.annotations.NotNull() java.lang.String seedPath) {
    }

    /**
     * 勾选/取消勾选某个视频路径；非可勾选视频忽略。
     */
    public final void toggleSelected(@org.jetbrains.annotations.NotNull() java.lang.String path) {
    }

    /**
     * 全选当前列表中的可勾选视频（保留其它目录已选中的项）。
     */
    public final void selectAll() {
    }

    /**
     * 反选当前列表中的可勾选视频（保留其它目录已选中的项）。
     */
    public final void invertSelection() {
    }

    /**
     * 退出多选并清空选中。
     */
    public final void exitMultiSelect() {
    }

    /**
     * 返回当前选中的视频完整路径（供后续匹配页使用）。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> selectedVideoFiles() {
        return null;
    }

    /**
     * 当前列表中可勾选视频的完整路径集合。
     */
    private final java.util.List<java.lang.String> currentVideoPaths(com.webdavrenamer.ui.browser.BrowserViewModel.UiState s) {
        return null;
    }

    /**
     * 排序：目录始终排在文件前；组内按 [field] 比较，升/降序。
     * - NAME：按 displayName 忽略大小写。
     * - SIZE：按 contentLength（null 视为最大，排末尾）。
     * - TIME：按 lastModified 字符串（RFC1123 同格式下字典序与时间序一致）。
     */
    private final java.util.List<com.webdavrenamer.core.webdav.WebDavEntry> sortEntries(java.util.List<com.webdavrenamer.core.webdav.WebDavEntry> entries, com.webdavrenamer.ui.browser.BrowserViewModel.SortField field, boolean asc) {
        return null;
    }

    /**
     * 排序字段。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0006\u0008\u0086\u0081\u0002\u0012\u0001\u0001\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005j\u0002\u0008\u0006\u00F2\u0001\u000C\n\u00020\u0000\n\u0006\u0012\u0002\u0018\u00000\u0001\u00A8\u0006\u0007"}, d2 = {"Lcom/webdavrenamer/ui/browser/BrowserViewModel$SortField;", "", "<init>", "(Ljava/lang/String;I)V", "NAME", "SIZE", "TIME", "app_debug"}, xs= "", pn = "", xi = 48)
    public static enum SortField {
        NAME,
        SIZE,
        TIME;


        SortField() {
        }

        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.webdavrenamer.ui.browser.BrowserViewModel.SortField> getEntries() {
            return null;
        }
    }
    /**
     * 浏览器 UI 状态。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\"\n\u0002\u0008\u0010\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000BW\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0006\u0008\u0002\u0010\u0004(\u0001\u0012\u0006\u0008\u0002\u0010\u0005(\u0001\u0012\u0006\u0008\u0002\u0010\u0006(\u0003\u0012\u0006\u0008\u0002\u0010\t(\u0004\u0012\u0006\u0008\u0002\u0010\u000B(\u0005\u0012\u0006\u0008\u0002\u0010\u000C(\u0006\u0012\u0006\u0008\u0002\u0010\u000E(\u0004\u0012\u0006\u0008\u0002\u0010\u000F(\u0004\u0012\u0006\u0008\u0002\u0010\u0010(\u0007\u00A2\u0006\u0004\u0008\u0012\u0010\u0013J\u0007\u0010\u00148\u0001H\u00C6\u0003J\u0007\u0010\u00158\u0001H\u00C6\u0003J\u0007\u0010\u00168\u0001H\u00C6\u0003J\u0007\u0010\u00178\u0003H\u00C6\u0003J\u0007\u0010\u00188\u0004H\u00C6\u0003J\u0007\u0010\u00198\u0005H\u00C6\u0003J\u0007\u0010\u001A8\u0006H\u00C6\u0003J\u0007\u0010\u001B8\u0004H\u00C6\u0003J\u0007\u0010\u001C8\u0004H\u00C6\u0003J\u0007\u0010\u001D8\u0007H\u00C6\u0003JW\u0010\u001E2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00012\u0006\u0008\u0002\u0010\u0006(\u00032\u0006\u0008\u0002\u0010\t(\u00042\u0006\u0008\u0002\u0010\u000B(\u00052\u0006\u0008\u0002\u0010\u000C(\u00062\u0006\u0008\u0002\u0010\u000E(\u00042\u0006\u0008\u0002\u0010\u000F(\u00042\u0006\u0008\u0002\u0010\u0010(\u00078\u0008H\u00C6\u0001J\r\u0010\u001F2\u0004\u0010 (\t8\u0004H\u00D6\u0003J\u0007\u0010!8\nH\u00D6\u0001J\u0007\u0010#8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\u000CH\u0006\u00A2\u0006\u0002\n\u0000R\t\u0010\u000EH\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u000FH\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u0010H\u0007\u00A2\u0006\u0002\n\u0000\u00F2\u00018\n\u00020\u0001\n\u00020\u0003\n\u00020\u0008\n\u0006\u0012\u0002\u0018\u00020\u0007\n\u00020\n\n\u0004\u0018\u00010\u0003\n\u00020\r\n\u0006\u0012\u0002\u0018\u00010\u0011\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020\"\u00A8\u0006$"}, d2 = {"Lcom/webdavrenamer/ui/browser/BrowserViewModel$UiState;", "", "serverName", "", "rootPath", "currentPath", "entries", "", "Lcom/webdavrenamer/core/webdav/WebDavEntry;", "loading", "", "error", "sortField", "Lcom/webdavrenamer/ui/browser/BrowserViewModel$SortField;", "sortAsc", "multiSelectMode", "selectedPaths", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;ZLjava/lang/String;Lcom/webdavrenamer/ui/browser/BrowserViewModel$SortField;ZZLjava/util/Set;)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class UiState {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String serverName = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String rootPath = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String currentPath = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.core.webdav.WebDavEntry> entries = null;

        private final boolean loading = false;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String error = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.ui.browser.BrowserViewModel.SortField sortField = null;

        private final boolean sortAsc = false;

        private final boolean multiSelectMode = false;

        @org.jetbrains.annotations.NotNull()
        private final java.util.Set<java.lang.String> selectedPaths = null;

        public UiState() {
            super();
        }

        /**
         * 浏览器 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.browser.BrowserViewModel.UiState copy(@org.jetbrains.annotations.NotNull() java.lang.String serverName, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.NotNull() java.lang.String currentPath, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.core.webdav.WebDavEntry> entries, boolean loading, @org.jetbrains.annotations.Nullable() java.lang.String error, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.browser.BrowserViewModel.SortField sortField, boolean sortAsc, boolean multiSelectMode, @org.jetbrains.annotations.NotNull() java.util.Set<java.lang.String> selectedPaths) {
            return null;
        }

        /**
         * 浏览器 UI 状态。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 浏览器 UI 状态。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 浏览器 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public UiState(@org.jetbrains.annotations.NotNull() java.lang.String serverName, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.NotNull() java.lang.String currentPath, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.core.webdav.WebDavEntry> entries, boolean loading, @org.jetbrains.annotations.Nullable() java.lang.String error, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.browser.BrowserViewModel.SortField sortField, boolean sortAsc, boolean multiSelectMode, @org.jetbrains.annotations.NotNull() java.util.Set<java.lang.String> selectedPaths) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getServerName() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getRootPath() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getCurrentPath() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.core.webdav.WebDavEntry> component4() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.core.webdav.WebDavEntry> getEntries() {
            return null;
        }

        public final boolean component5() {
            return false;
        }

        public final boolean getLoading() {
            return false;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component6() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getError() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.browser.BrowserViewModel.SortField component7() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.browser.BrowserViewModel.SortField getSortField() {
            return null;
        }

        public final boolean component8() {
            return false;
        }

        public final boolean getSortAsc() {
            return false;
        }

        public final boolean component9() {
            return false;
        }

        public final boolean getMultiSelectMode() {
            return false;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.Set<java.lang.String> component10() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.Set<java.lang.String> getSelectedPaths() {
            return null;
        }
    }
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010\u0008\n\u0000\u0008\u0086\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0082TR\u0007\u0010\u0006H\u0002X\u0082T\u00F2\u0001\u000C\n\u00020\u0001\n\u00020\u0005\n\u00020\u0007\u00A8\u0006\u0008"}, d2 = {"Lcom/webdavrenamer/ui/browser/BrowserViewModel$Companion;", "", "<init>", "()V", "TAG", "", "LARGE_DIR_THRESHOLD", "", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Companion {

        private Companion() {
            super();
        }
    }
}
