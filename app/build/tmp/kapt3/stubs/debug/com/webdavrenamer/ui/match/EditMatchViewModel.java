package com.webdavrenamer.ui.match;

/**
 * Edit Match ViewModel（Task 2.5.1–2.5.4）。
 * 
 * 单条手动修正编排：切换电影/剧集 → 搜索候选 → 选定 → （剧集）选季选集 → 保存。
 * 支持多集组合（连续 [1,2]→`S01E01-E02`；非连续 [1,3]→`S01E01,E03`，标题 `A & B` 合并）、
 * Episodes 面板连续多选预制、线性对齐批量匹配。
 * 
 * 数据流：[MatchSessionViewModel]（Activity 作用域）持有 matchedFiles 快照；
 * EditMatchScreen 按导航参数 `matchIndex` 取出对应 [MatchViewModel.FileMatch] 调 [load]，
 * 保存后由 EditMatchScreen 回写 [MatchSessionViewModel]。本 VM 不直接持有会话 VM，
 * 避免跨 ViewModel 注入复杂度（Hilt 不便将一个 @HiltViewModel 注入另一个）。
 * 
 * 安全：API Key 仅从 [SettingsRepository] 读取构造 [TmdbClient]，不进 UI 状态或日志。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\"\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0002\u0008\u0015\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0008\t\u0008\u0007\u0012\u0001\u0000\u0018\u0000 ]:\u0007WXYZ[\\]B\u000F\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0010\u0010'2\u0004\u0010)(\u00172\u0004\u0010*(\u00188\u0016J\n\u0010+2\u0004\u0010,(\u00078\u0016J\n\u0010-2\u0004\u0010.(\u00198\u0016J\n\u001002\u0004\u00101(\u00118\u0016J\u0006\u001028\u0016H\u0002J\n\u001032\u0004\u00101(\u00118\u0016J\n\u001042\u0004\u00105(\u001A8\u0016J\n\u001072\u0004\u00108(\u000E8\u0016J\u0010\u001092\u0004\u0010:(\u000E2\u0004\u00108(\u000E8\u0016J\n\u0010;2\u0004\u0010<(\u000E8\u0016J\u0010\u0010=2\u0004\u0010>(\u000E2\u0004\u0010?(\u000E8\u0016J\u0004\u0010@8\u0016J\u0004\u0010A8\u0016J\u0006\u0010B8\u0016H\u0002J\n\u0010C2\u0004\u0010D(\u000E8\u0016J\n\u0010E2\u0004\u0010D(\u000E8\u0016J\n\u0010F2\u0004\u0010D(\u000E8\u0016J\u0004\u0010G8\u0016J\u0004\u0010H8\u0016J\u0004\u0010I8\u0016J\u0004\u0010J8\u0016J\u000C\u0010K8\u001BH\u0082@\u00A2\u0006\u0002\u0010MJ*\u0010N2\u0004\u0010P(\u001D2\u0004\u0010:(\u000E2\u0004\u0010\u0013(\u000E2\u0004\u0010Q(\u000F2\u0004\u0010R(\u00118\u001CH\u0082@\u00A2\u0006\u0002\u0010SJ\u0008\u0010T8\u000B@\u001EH\u0002J\u000E\u0010V2\u0004\u0010,(\u00078\u001A@\u001CH\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0006H\u0003X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\tH\u0004\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000B\u0010\u000CR\u000F\u0010\rH\u0006\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000F\u0010\u000CR\u000F\u0010\u0010H\u0008\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0012\u0010\u000CR\u000F\u0010\u0013H\n\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0015\u0010\u000CR\u000F\u0010\u0016H\r\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0019\u0010\u000CR\u000F\u0010\u001AH\u0010\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001C\u0010\u000CR\u000F\u0010\u001DH\u0012\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001F\u0010\u000CR\u000F\u0010 H\r\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008!\u0010\u000CR\u000F\u0010\"H\u0014\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008$\u0010\u000CR\u000C\u0010%H\u0015X\u0082\u000E\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u00B4\u0001\n\u00020\u0001\n\u00020\u0003\n\u00020\u0008\n\u0006\u0012\u0002\u0018\u00020\u0007\n\u0006\u0012\u0002\u0018\u00020\n\n\u0004\u0018\u00010\u000E\n\u0006\u0012\u0002\u0018\u00050\n\n\u00020\u0011\n\u0006\u0012\u0002\u0018\u00070\n\n\u0004\u0018\u00010\u0014\n\u0006\u0012\u0002\u0018\t0\n\n\u00020\u0018\n\u0006\u0012\u0002\u0018\u000B0\u0017\n\u0006\u0012\u0002\u0018\u000C0\n\n\u00020\u0014\n\u0006\u0012\u0002\u0018\u000E0\u001B\n\u0006\u0012\u0002\u0018\u000F0\n\n\u00020\u001E\n\u0006\u0012\u0002\u0018\u00110\n\n\u00020#\n\u0006\u0012\u0002\u0018\u00130\n\n\u0004\u0018\u00010&\n\u00020(\n\u00020\u000E\n\u0006\u0012\u0002\u0018\u00170\u0017\n\u00020/\n\u000206\n\u0004\u0018\u00010L\n\u00020O\n\u00020L\n\u00020U\u00A8\u0006^"}, d2 = {"Lcom/webdavrenamer/ui/match/EditMatchViewModel;", "Landroidx/lifecycle/ViewModel;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "<init>", "(Lcom/webdavrenamer/data/prefs/SettingsRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$UiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "currentMatch", "Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "getCurrentMatch", "mediaType", "Lcom/webdavrenamer/core/model/MediaType;", "getMediaType", "seasonNumber", "", "getSeasonNumber", "episodeList", "", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$EpisodeInfo;", "getEpisodeList", "selectedEpisodeNumbers", "", "getSelectedEpisodeNumbers", "searchQuery", "", "getSearchQuery", "filteredEpisodes", "getFilteredEpisodes", "alignmentMode", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$AlignmentMode;", "getAlignmentMode", "mediaSearchJob", "Lkotlinx/coroutines/Job;", "load", "", "fileMatch", "allFiles", "switchMediaType", "type", "setEditMode", "mode", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$EditMode;", "search", "query", "applyEpisodeFilter", "searchMedia", "selectMedia", "candidate", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$MediaCandidate;", "setSeason", "season", "loadSeason", "tvId", "toggleEpisode", "num", "selectRange", "start", "end", "applyEdit", "batchApply", "rebuildAlignmentRows", "moveFileUp", "index", "moveFileDown", "unbindFile", "consumeSaved", "consumeBatchSaved", "clearError", "cancel", "createClientOrNull", "Lcom/webdavrenamer/core/tmdb/TmdbClient;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buildEpisodeMetadata", "Lcom/webdavrenamer/core/naming/MediaMetadata;", "client", "episodes", "language", "(Lcom/webdavrenamer/core/tmdb/TmdbClient;IILjava/util/Set;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toEpisodeInfo", "Lcom/webdavrenamer/core/tmdb/Episode;", "toMediaCandidate", "AlignmentMode", "EditMode", "EpisodeInfo", "MediaCandidate", "AlignmentRow", "UiState", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class EditMatchViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.ui.match.EditMatchViewModel.UiState> _uiState = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.match.EditMatchViewModel.UiState> uiState = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> currentMatch = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.core.model.MediaType> mediaType = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> seasonNumber = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo>> episodeList = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.Integer>> selectedEpisodeNumbers = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo>> filteredEpisodes = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentMode> alignmentMode = null;

    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job mediaSearchJob = null;

    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.ui.match.EditMatchViewModel.Companion Companion = null;

    @javax.inject.Inject()
    public EditMatchViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.match.EditMatchViewModel.UiState> getUiState() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> getCurrentMatch() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.core.model.MediaType> getMediaType() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getSeasonNumber() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo>> getEpisodeList() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.Integer>> getSelectedEpisodeNumbers() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo>> getFilteredEpisodes() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentMode> getAlignmentMode() {
        return null;
    }

    /**
     * 由 EditMatchScreen 在进入时调用：载入待编辑的 [fileMatch] 与同批次 [allFiles]
     * （线性对齐用）。若已有剧集匹配，预加载该季集列表。
     */
    public final void load(@org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.FileMatch fileMatch, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> allFiles) {
    }

    /**
     * 切换电影/剧集类型（Task 2.5.1）。切换时清空已选候选与集列表。
     */
    public final void switchMediaType(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType type) {
    }

    /**
     * 顶部编辑模式切换：单集 / 多集组合 / 线性对齐（Task 2.5.2 / 2.5.4）。
     */
    public final void setEditMode(@org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.EditMatchViewModel.EditMode mode) {
    }

    /**
     * find-as-you-type 集过滤（Task 2.5.1）：按集号/标题/简介包含匹配。
     * 仅作用于已加载的 [episodeList]。
     */
    public final void search(@org.jetbrains.annotations.NotNull() java.lang.String query) {
    }

    private final void applyEpisodeFilter() {
    }

    /**
     * 影视候选搜索（电影/剧集标题）。手动 debounce 350ms，避免连击打爆 API。
     * 空查询清空结果。
     */
    public final void searchMedia(@org.jetbrains.annotations.NotNull() java.lang.String query) {
    }

    /**
     * 选定一个搜索候选；剧集则自动加载首季集列表。
     */
    public final void selectMedia(@org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate candidate) {
    }

    /**
     * 改变季号并重新加载集列表。
     */
    public final void setSeason(int season) {
    }

    /**
     * 加载某季集列表（Task 2.5.1/2.5.3）。
     */
    public final void loadSeason(int tvId, int season) {
    }

    /**
     * 勾选/取消勾选单集（Task 2.5.2）。单集模式下互斥替换；多集模式下累加。
     */
    public final void toggleEpisode(int num) {
    }

    /**
     * 连续多选 [start]..[end]（双向区间），覆盖式选中（Task 2.5.2/2.5.3）。
     */
    public final void selectRange(int start, int end) {
    }

    /**
     * 应用单条编辑（Task 2.5.1/2.5.2/2.5.3）。
     * 
     * 电影 → 拉详情写回；剧集 → 按所选集号合并元数据，多集时合并标题 `A & B`、
     * 集号标签 `S01E01-E02`，写入 [MatchViewModel.FileMatch.multiEpisodeRange]。
     * 结果通过 [UiState.saved] 暴露，由 EditMatchScreen 回写会话 VM。
     */
    public final void applyEdit() {
    }

    /**
     * 线性对齐批量应用（Task 2.5.4）。
     * 
     * 按 [UiState.alignmentRows] 顺序，将每行绑定集号写回对应文件；已解绑行保持原状。
     * 结果通过 [UiState.batchSaved] 暴露整表，由 EditMatchScreen 回写 [MatchSessionViewModel.replaceMatchedFiles]。
     */
    public final void batchApply() {
    }

    /**
     * 重新按当前集列表顺序为对齐行绑定集号（保持文件顺序，集顺序对齐）。
     */
    private final void rebuildAlignmentRows() {
    }

    public final void moveFileUp(int index) {
    }

    public final void moveFileDown(int index) {
    }

    /**
     * 单条解绑（Task 2.5.4）：清除该行的集号绑定。
     */
    public final void unbindFile(int index) {
    }

    public final void consumeSaved() {
    }

    public final void consumeBatchSaved() {
    }

    public final void clearError() {
    }

    /**
     * 取消编辑：清空待保存信号与错误，不写回。
     */
    public final void cancel() {
    }

    /**
     * 读 API Key 构造 [TmdbClient]；空 key 写错误返回 null。
     */
    private final java.lang.Object createClientOrNull(kotlin.coroutines.Continuation<? super com.webdavrenamer.core.tmdb.TmdbClient> $completion) {
        return null;
    }

    /**
     * 构造剧集 [MediaMetadata]：拉 TV 详情 + 当季详情，按所选集号合并
     * `seasonNumber / episodeNumbers / episodeTitles(多集 A & B) / episodeAirDates / seasonName`。
     * 对齐 [MatchViewModel] 的 fetchDetail 规则。
     */
    private final java.lang.Object buildEpisodeMetadata(com.webdavrenamer.core.tmdb.TmdbClient client, int tvId, int seasonNumber, java.util.Set<java.lang.Integer> episodes, java.lang.String language, kotlin.coroutines.Continuation<? super com.webdavrenamer.core.naming.MediaMetadata> $completion) {
        return null;
    }

    private final com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo toEpisodeInfo(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.tmdb.Episode $this$toEpisodeInfo) {
        return null;
    }

    private final com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate toMediaCandidate(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.MediaMetadata $this$toMediaCandidate, com.webdavrenamer.core.model.MediaType type) {
        return null;
    }

    /**
     * 线性对齐模式（Task 2.5.4）。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0005\u0008\u0086\u0081\u0002\u0012\u0001\u0001\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005\u00F2\u0001\u000C\n\u00020\u0000\n\u0006\u0012\u0002\u0018\u00000\u0001\u00A8\u0006\u0006"}, d2 = {"Lcom/webdavrenamer/ui/match/EditMatchViewModel$AlignmentMode;", "", "<init>", "(Ljava/lang/String;I)V", "OFF", "SEQUENTIAL", "app_debug"}, xs= "", pn = "", xi = 48)
    public static enum AlignmentMode {
        OFF,
        SEQUENTIAL;


        AlignmentMode() {
        }

        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentMode> getEntries() {
            return null;
        }
    }
    /**
     * 顶部编辑模式：单集 / 多集组合 / 线性对齐。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0006\u0008\u0086\u0081\u0002\u0012\u0001\u0001\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005j\u0002\u0008\u0006\u00F2\u0001\u000C\n\u00020\u0000\n\u0006\u0012\u0002\u0018\u00000\u0001\u00A8\u0006\u0007"}, d2 = {"Lcom/webdavrenamer/ui/match/EditMatchViewModel$EditMode;", "", "<init>", "(Ljava/lang/String;I)V", "SINGLE", "MULTI", "ALIGNMENT", "app_debug"}, xs= "", pn = "", xi = 48)
    public static enum EditMode {
        SINGLE,
        MULTI,
        ALIGNMENT;


        EditMode() {
        }

        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.webdavrenamer.ui.match.EditMatchViewModel.EditMode> getEntries() {
            return null;
        }
    }
    /**
     * UI 友好的集信息（从 TMDB [Episode] 映射）。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u000C\n\u0002\u0010\u000B\n\u0002\u0008\u0004\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B%\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0002\u0012\u0004\u0010\u0007(\u0003\u0012\u0004\u0010\u0008(\u0003\u00A2\u0006\u0004\u0008\t\u0010\nJ\u0007\u0010\u000B8\u0001H\u00C6\u0003J\u0007\u0010\u000C8\u0002H\u00C6\u0003J\u0007\u0010\r8\u0002H\u00C6\u0003J\u0007\u0010\u000E8\u0003H\u00C6\u0003J\u0007\u0010\u000F8\u0003H\u00C6\u0003J/\u0010\u00102\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00022\u0006\u0008\u0002\u0010\u0007(\u00032\u0006\u0008\u0002\u0010\u0008(\u00038\u0004H\u00C6\u0001J\r\u0010\u00112\u0004\u0010\u0013(\u00068\u0005H\u00D6\u0003J\u0007\u0010\u00148\u0001H\u00D6\u0001J\u0007\u0010\u00158\u0002H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0007H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0003\u00A2\u0006\u0002\n\u0000\u00F2\u0001 \n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u0004\u0018\u00010\u0005\n\u00020\u0000\n\u00020\u0012\n\u0004\u0018\u00010\u0001\u00A8\u0006\u0016"}, d2 = {"Lcom/webdavrenamer/ui/match/EditMatchViewModel$EpisodeInfo;", "", "episodeNumber", "", "name", "", "overview", "airDate", "stillUrl", "<init>", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class EpisodeInfo {
        private final int episodeNumber = 0;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String name = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String overview = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String airDate = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String stillUrl = null;

        /**
         * UI 友好的集信息（从 TMDB [Episode] 映射）。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo copy(int episodeNumber, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String overview, @org.jetbrains.annotations.Nullable() java.lang.String airDate, @org.jetbrains.annotations.Nullable() java.lang.String stillUrl) {
            return null;
        }

        /**
         * UI 友好的集信息（从 TMDB [Episode] 映射）。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * UI 友好的集信息（从 TMDB [Episode] 映射）。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * UI 友好的集信息（从 TMDB [Episode] 映射）。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public EpisodeInfo(int episodeNumber, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String overview, @org.jetbrains.annotations.Nullable() java.lang.String airDate, @org.jetbrains.annotations.Nullable() java.lang.String stillUrl) {
            super();
        }

        public final int component1() {
            return 0;
        }

        public final int getEpisodeNumber() {
            return 0;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getName() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getOverview() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component4() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getAirDate() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component5() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getStillUrl() {
            return null;
        }
    }
    /**
     * 影视搜索候选（电影/剧集通用，带海报）。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u000B\n\u0002\u0010\u000B\n\u0002\u0008\u0004\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B+\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0003\u0012\u0004\u0010\u0007(\u0004\u0012\u0004\u0010\u0008(\u0004\u0012\u0004\u0010\t(\u0005\u00A2\u0006\u0004\u0008\u000B\u0010\u000CJ\u0007\u0010\u000E8\u0001H\u00C6\u0003J\u0007\u0010\u000F8\u0002H\u00C6\u0003J\u0007\u0010\u00108\u0003H\u00C6\u0003J\u0007\u0010\u00118\u0004H\u00C6\u0003J\u0007\u0010\u00128\u0004H\u00C6\u0003J\u0007\u0010\u00138\u0005H\u00C6\u0003J7\u0010\u00142\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00032\u0006\u0008\u0002\u0010\u0007(\u00042\u0006\u0008\u0002\u0010\u0008(\u00042\u0006\u0008\u0002\u0010\t(\u00058\u0006H\u00C6\u0001J\r\u0010\u00152\u0004\u0010\u0017(\u00088\u0007H\u00D6\u0003J\u0007\u0010\u00188\u0001H\u00D6\u0001J\u0007\u0010\u00198\u0002H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u0006H\u0003\u00A2\u0006\u0004\n\u0002\u0010\rR\t\u0010\u0007H\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0005\u00A2\u0006\u0002\n\u0000\u00F2\u0001*\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u0004\u0018\u00010\u0003\n\u0004\u0018\u00010\u0005\n\u00020\n\n\u00020\u0000\n\u00020\u0016\n\u0004\u0018\u00010\u0001\u00A8\u0006\u001A"}, d2 = {"Lcom/webdavrenamer/ui/match/EditMatchViewModel$MediaCandidate;", "", "tmdbId", "", "name", "", "year", "overview", "posterUrl", "mediaType", "Lcom/webdavrenamer/core/model/MediaType;", "<init>", "(ILjava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Lcom/webdavrenamer/core/model/MediaType;)V", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class MediaCandidate {
        private final int tmdbId = 0;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String name = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer year = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String overview = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String posterUrl = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.core.model.MediaType mediaType = null;

        /**
         * 影视搜索候选（电影/剧集通用，带海报）。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate copy(int tmdbId, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.Nullable() java.lang.Integer year, @org.jetbrains.annotations.Nullable() java.lang.String overview, @org.jetbrains.annotations.Nullable() java.lang.String posterUrl, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType mediaType) {
            return null;
        }

        /**
         * 影视搜索候选（电影/剧集通用，带海报）。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 影视搜索候选（电影/剧集通用，带海报）。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 影视搜索候选（电影/剧集通用，带海报）。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public MediaCandidate(int tmdbId, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.Nullable() java.lang.Integer year, @org.jetbrains.annotations.Nullable() java.lang.String overview, @org.jetbrains.annotations.Nullable() java.lang.String posterUrl, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType mediaType) {
            super();
        }

        public final int component1() {
            return 0;
        }

        public final int getTmdbId() {
            return 0;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getName() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer component3() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getYear() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component4() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getOverview() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component5() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getPosterUrl() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.model.MediaType component6() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.model.MediaType getMediaType() {
            return null;
        }
    }
    /**
     * 线性对齐行：文件 + 绑定的集号（null=已解绑）。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u0013\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\u0007\u0010\t8\u0001H\u00C6\u0003J\u0007\u0010\n8\u0002H\u00C6\u0003J\u0017\u0010\u000B2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00028\u0003H\u00C6\u0001J\r\u0010\u000C2\u0004\u0010\u000E(\u00058\u0004H\u00D6\u0003J\u0007\u0010\u000F8\u0006H\u00D6\u0001J\u0007\u0010\u00108\u0007H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u0004H\u0002\u00A2\u0006\u0004\n\u0002\u0010\u0008\u00F2\u0001$\n\u00020\u0001\n\u00020\u0003\n\u0004\u0018\u00010\u0005\n\u00020\u0000\n\u00020\r\n\u0004\u0018\u00010\u0001\n\u00020\u0005\n\u00020\u0011\u00A8\u0006\u0012"}, d2 = {"Lcom/webdavrenamer/ui/match/EditMatchViewModel$AlignmentRow;", "", "file", "Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "boundEpisodeNumber", "", "<init>", "(Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class AlignmentRow {
        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.ui.match.MatchViewModel.FileMatch file = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer boundEpisodeNumber = null;

        /**
         * 线性对齐行：文件 + 绑定的集号（null=已解绑）。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentRow copy(@org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.FileMatch file, @org.jetbrains.annotations.Nullable() java.lang.Integer boundEpisodeNumber) {
            return null;
        }

        /**
         * 线性对齐行：文件 + 绑定的集号（null=已解绑）。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 线性对齐行：文件 + 绑定的集号（null=已解绑）。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 线性对齐行：文件 + 绑定的集号（null=已解绑）。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public AlignmentRow(@org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.FileMatch file, @org.jetbrains.annotations.Nullable() java.lang.Integer boundEpisodeNumber) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.FileMatch component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.FileMatch getFile() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer component2() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getBoundEpisodeNumber() {
            return null;
        }
    }
    /**
     * 编辑页 UI 状态。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u001F\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u0097\u0001\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0006\u0008\u0002\u0010\u0004(\u0002\u0012\u0006\u0008\u0002\u0010\u0006(\u0003\u0012\u0006\u0008\u0002\u0010\u0008(\u0004\u0012\u0006\u0008\u0002\u0010\n(\u0005\u0012\u0006\u0008\u0002\u0010\u000C(\u0006\u0012\u0006\u0008\u0002\u0010\u000E(\u0008\u0012\u0006\u0008\u0002\u0010\u0011(\n\u0012\u0006\u0008\u0002\u0010\u0013(\u000B\u0012\u0006\u0008\u0002\u0010\u0015(\u0008\u0012\u0006\u0008\u0002\u0010\u0016(\u000B\u0012\u0006\u0008\u0002\u0010\u0017(\r\u0012\u0006\u0008\u0002\u0010\u0019(\u000E\u0012\u0006\u0008\u0002\u0010\u001A(\u0010\u0012\u0006\u0008\u0002\u0010\u001C(\u0005\u0012\u0006\u0008\u0002\u0010\u001D(\u0011\u0012\u0006\u0008\u0002\u0010\u001E(\u0001\u0012\u0006\u0008\u0002\u0010\u001F(\u0013\u00A2\u0006\u0004\u0008 \u0010!J\u0007\u0010#8\u0001H\u00C6\u0003J\u0007\u0010$8\u0002H\u00C6\u0003J\u0007\u0010%8\u0003H\u00C6\u0003J\u0007\u0010&8\u0004H\u00C6\u0003J\u0007\u0010'8\u0005H\u00C6\u0003J\u0007\u0010(8\u0006H\u00C6\u0003J\u0007\u0010)8\u0008H\u00C6\u0003J\u0007\u0010*8\nH\u00C6\u0003J\u0007\u0010+8\u000BH\u00C6\u0003J\u0007\u0010,8\u0008H\u00C6\u0003J\u0007\u0010-8\u000BH\u00C6\u0003J\u0007\u0010.8\rH\u00C6\u0003J\u0007\u0010/8\u000EH\u00C6\u0003J\u0007\u001008\u0010H\u00C6\u0003J\u0007\u001018\u0005H\u00C6\u0003J\u0007\u001028\u0011H\u00C6\u0003J\u0007\u001038\u0001H\u00C6\u0003J\u0007\u001048\u0013H\u00C6\u0003J\u0097\u0001\u001052\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00032\u0006\u0008\u0002\u0010\u0008(\u00042\u0006\u0008\u0002\u0010\n(\u00052\u0006\u0008\u0002\u0010\u000C(\u00062\u0006\u0008\u0002\u0010\u000E(\u00082\u0006\u0008\u0002\u0010\u0011(\n2\u0006\u0008\u0002\u0010\u0013(\u000B2\u0006\u0008\u0002\u0010\u0015(\u00082\u0006\u0008\u0002\u0010\u0016(\u000B2\u0006\u0008\u0002\u0010\u0017(\r2\u0006\u0008\u0002\u0010\u0019(\u000E2\u0006\u0008\u0002\u0010\u001A(\u00102\u0006\u0008\u0002\u0010\u001C(\u00052\u0006\u0008\u0002\u0010\u001D(\u00112\u0006\u0008\u0002\u0010\u001E(\u00012\u0006\u0008\u0002\u0010\u001F(\u00138\u0014H\u00C6\u0001J\r\u001062\u0004\u00107(\u00158\u0005H\u00D6\u0003J\u0007\u001088\tH\u00D6\u0001J\u0007\u001098\u000BH\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0005\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u000CH\u0006\u00A2\u0006\u0004\n\u0002\u0010\"R\t\u0010\u000EH\u0008\u00A2\u0006\u0002\n\u0000R\t\u0010\u0011H\n\u00A2\u0006\u0002\n\u0000R\t\u0010\u0013H\u000B\u00A2\u0006\u0002\n\u0000R\t\u0010\u0015H\u0008\u00A2\u0006\u0002\n\u0000R\t\u0010\u0016H\u000B\u00A2\u0006\u0002\n\u0000R\t\u0010\u0017H\r\u00A2\u0006\u0002\n\u0000R\t\u0010\u0019H\u000E\u00A2\u0006\u0002\n\u0000R\t\u0010\u001AH\u0010\u00A2\u0006\u0002\n\u0000R\t\u0010\u001CH\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\u001DH\u0011\u00A2\u0006\u0002\n\u0000R\t\u0010\u001EH\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u001FH\u0013\u00A2\u0006\u0002\n\u0000\u00F2\u0001x\n\u00020\u0001\n\u0004\u0018\u00010\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\t\n\u00020\u000B\n\u0004\u0018\u00010\r\n\u00020\u0010\n\u0006\u0012\u0002\u0018\u00070\u000F\n\u00020\r\n\u0006\u0012\u0002\u0018\t0\u0012\n\u00020\u0014\n\u00020\u0018\n\u0006\u0012\u0002\u0018\u000C0\u000F\n\u0004\u0018\u00010\u0018\n\u00020\u001B\n\u0006\u0012\u0002\u0018\u000F0\u000F\n\u0004\u0018\u00010\u0014\n\u00020\u0003\n\u0008\u0012\u0002\u0018\u0012\u0018\u00010\u000F\n\u00020\u0000\n\u0004\u0018\u00010\u0001\u00A8\u0006:"}, d2 = {"Lcom/webdavrenamer/ui/match/EditMatchViewModel$UiState;", "", "currentMatch", "Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "mediaType", "Lcom/webdavrenamer/core/model/MediaType;", "editMode", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$EditMode;", "alignmentMode", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$AlignmentMode;", "multiSelect", "", "seasonNumber", "", "episodeList", "", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$EpisodeInfo;", "selectedEpisodeNumbers", "", "searchQuery", "", "filteredEpisodes", "mediaSearchQuery", "mediaSearchResults", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$MediaCandidate;", "selectedMedia", "alignmentRows", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$AlignmentRow;", "loading", "error", "saved", "batchSaved", "<init>", "(Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;Lcom/webdavrenamer/core/model/MediaType;Lcom/webdavrenamer/ui/match/EditMatchViewModel$EditMode;Lcom/webdavrenamer/ui/match/EditMatchViewModel$AlignmentMode;ZLjava/lang/Integer;Ljava/util/List;Ljava/util/Set;Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Ljava/util/List;Lcom/webdavrenamer/ui/match/EditMatchViewModel$MediaCandidate;Ljava/util/List;ZLjava/lang/String;Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;Ljava/util/List;)V", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "copy", "equals", "other", "hashCode", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class UiState {
        @org.jetbrains.annotations.Nullable()
        private final com.webdavrenamer.ui.match.MatchViewModel.FileMatch currentMatch = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.core.model.MediaType mediaType = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.ui.match.EditMatchViewModel.EditMode editMode = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentMode alignmentMode = null;

        private final boolean multiSelect = false;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer seasonNumber = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> episodeList = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.Set<java.lang.Integer> selectedEpisodeNumbers = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String searchQuery = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> filteredEpisodes = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String mediaSearchQuery = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate> mediaSearchResults = null;

        @org.jetbrains.annotations.Nullable()
        private final com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate selectedMedia = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentRow> alignmentRows = null;

        private final boolean loading = false;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String error = null;

        @org.jetbrains.annotations.Nullable()
        private final com.webdavrenamer.ui.match.MatchViewModel.FileMatch saved = null;

        @org.jetbrains.annotations.Nullable()
        private final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> batchSaved = null;

        public UiState() {
            super();
        }

        /**
         * 编辑页 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.UiState copy(@org.jetbrains.annotations.Nullable() com.webdavrenamer.ui.match.MatchViewModel.FileMatch currentMatch, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType mediaType, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.EditMatchViewModel.EditMode editMode, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentMode alignmentMode, boolean multiSelect, @org.jetbrains.annotations.Nullable() java.lang.Integer seasonNumber, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> episodeList, @org.jetbrains.annotations.NotNull() java.util.Set<java.lang.Integer> selectedEpisodeNumbers, @org.jetbrains.annotations.NotNull() java.lang.String searchQuery, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> filteredEpisodes, @org.jetbrains.annotations.NotNull() java.lang.String mediaSearchQuery, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate> mediaSearchResults, @org.jetbrains.annotations.Nullable() com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate selectedMedia, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentRow> alignmentRows, boolean loading, @org.jetbrains.annotations.Nullable() java.lang.String error, @org.jetbrains.annotations.Nullable() com.webdavrenamer.ui.match.MatchViewModel.FileMatch saved, @org.jetbrains.annotations.Nullable() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> batchSaved) {
            return null;
        }

        /**
         * 编辑页 UI 状态。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 编辑页 UI 状态。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 编辑页 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public UiState(@org.jetbrains.annotations.Nullable() com.webdavrenamer.ui.match.MatchViewModel.FileMatch currentMatch, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType mediaType, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.EditMatchViewModel.EditMode editMode, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentMode alignmentMode, boolean multiSelect, @org.jetbrains.annotations.Nullable() java.lang.Integer seasonNumber, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> episodeList, @org.jetbrains.annotations.NotNull() java.util.Set<java.lang.Integer> selectedEpisodeNumbers, @org.jetbrains.annotations.NotNull() java.lang.String searchQuery, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> filteredEpisodes, @org.jetbrains.annotations.NotNull() java.lang.String mediaSearchQuery, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate> mediaSearchResults, @org.jetbrains.annotations.Nullable() com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate selectedMedia, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentRow> alignmentRows, boolean loading, @org.jetbrains.annotations.Nullable() java.lang.String error, @org.jetbrains.annotations.Nullable() com.webdavrenamer.ui.match.MatchViewModel.FileMatch saved, @org.jetbrains.annotations.Nullable() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> batchSaved) {
            super();
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.ui.match.MatchViewModel.FileMatch component1() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.ui.match.MatchViewModel.FileMatch getCurrentMatch() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.model.MediaType component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.model.MediaType getMediaType() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.EditMode component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.EditMode getEditMode() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentMode component4() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentMode getAlignmentMode() {
            return null;
        }

        public final boolean component5() {
            return false;
        }

        public final boolean getMultiSelect() {
            return false;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer component6() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getSeasonNumber() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> component7() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> getEpisodeList() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.Set<java.lang.Integer> component8() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.Set<java.lang.Integer> getSelectedEpisodeNumbers() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component9() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSearchQuery() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> component10() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> getFilteredEpisodes() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component11() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMediaSearchQuery() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate> component12() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate> getMediaSearchResults() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate component13() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.ui.match.EditMatchViewModel.MediaCandidate getSelectedMedia() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentRow> component14() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.AlignmentRow> getAlignmentRows() {
            return null;
        }

        public final boolean component15() {
            return false;
        }

        public final boolean getLoading() {
            return false;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component16() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getError() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.ui.match.MatchViewModel.FileMatch component17() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.ui.match.MatchViewModel.FileMatch getSaved() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> component18() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> getBatchSaved() {
            return null;
        }
    }
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010 \n\u0002\u0008\u0004\u0008\u0086\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\u0010\u0010\u00042\u0004\u0010\u0006(\u00022\u0004\u0010\u0008(\u00038\u0001J\u0012\u0010\n2\u0004\u0010\u000B(\u00022\u0004\u0010\u000C(\u00028\u0001H\u0002\u00F2\u0001\u0014\n\u00020\u0001\n\u00020\u0005\n\u00020\u0007\n\u0006\u0012\u0002\u0018\u00020\t\u00A8\u0006\r"}, d2 = {"Lcom/webdavrenamer/ui/match/EditMatchViewModel$Companion;", "", "<init>", "()V", "formatEpisodeRange", "", "season", "", "episodes", "", "formatSegment", "start", "end", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Companion {

        private Companion() {
            super();
        }

        /**
         * 多集集号标签格式化（Task 2.5.2）。
         * - 连续 [1,2] → `S01E01-E02`
         * - 非连续 [1,3] → `S01E01,E03`
         * - 混合 [1,2,4] → `S01E01-E02,E04`
         */
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String formatEpisodeRange(int season, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> episodes) {
            return null;
        }

        private final java.lang.String formatSegment(int start, int end) {
            return null;
        }
    }
}
