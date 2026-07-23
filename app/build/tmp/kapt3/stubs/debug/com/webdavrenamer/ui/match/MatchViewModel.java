package com.webdavrenamer.ui.match;

/**
 * TMDB 匹配编排 ViewModel（计划 §M2 Task 2.4）。
 * 
 * 流程：浏览器选中的视频路径 → [FilenameParser] 解析 → 判定类型（强制/自动）
 * → [TmdbCacheRepository] 搜索 → [MatchEngine] 决策：
 * - [MatchDecision.Auto]：拉详情（剧集补 [MediaMetadata.seasonNumber]/[MediaMetadata.episodeTitles]）→ 自动✅
 * - [MatchDecision.NeedsConfirm]：待确认⚠️，保留候选供 UI 选择
 * - [MatchDecision.NoMatch]：无匹配❌，用户可手动搜索
 * 
 * Task 2.3.4：所有 TMDB 访问改走 [TmdbCacheRepository]（详情类请求自动走 Room 缓存，
 * 搜索类请求透传）。API Key 仅从 [SettingsRepository] 读取用于网络请求，绝不进入 UI 状态或日志。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0008\u0008\u0007\u0012\u0001\u0000\u0018\u0000:\u00069:;<=>B\u001D\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0006\u0008\u0001\u0010\u0006(\u0003\u00A2\u0006\u0004\u0008\u0008\u0010\tJ\n\u0010\u00172\u0004\u0010\u0019(\u000C8\nJ\n\u0010\u001C2\u0004\u0010\u001D(\r8\nJ\u0004\u0010\u001F8\nJ\n\u0010 2\u0004\u0010!(\r8\nJ\u0012\u0010\"2\u0004\u0010!(\r2\u0004\u0010#(\u000E8\rH\u0002J*\u0010%2\u0004\u0010\u0004(\u00022\u0004\u0010#(\u000E2\u0004\u0010\u001D(\r2\u0004\u0010'(\u000B2\u0004\u0010((\u000B8\u000FH\u0082@\u00A2\u0006\u0002\u0010)J\u0010\u0010*2\u0004\u0010((\u000B2\u0004\u0010+(\u00108\nJ\u0016\u0010-2\u0004\u0010((\u000B2\u0004\u0010.(\u000B2\u0004\u0010\u001D(\r8\nJ\n\u0010/2\u0004\u0010\u0019(\u00118\nJ$\u001002\u0004\u0010\u0004(\u00022\u0004\u0010+(\u00102\u0004\u0010#(\u000E2\u0004\u0010'(\u000B8\u0012H\u0082@\u00A2\u0006\u0002\u00102J\u0008\u001038\u0010@\u0012H\u0002J\u0012\u001042\u0004\u00106(\u00142\u0004\u00108(\u00158\u0013H\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0006H\u0003X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\nH\u0005X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\rH\u0006\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000F\u0010\u0010R\u000C\u0010\u0011H\u0007X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0013H\u0008X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0015H\tX\u0082\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001l\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\u000C\n\u0006\u0012\u0002\u0018\u00040\u000B\n\u0006\u0012\u0002\u0018\u00040\u000E\n\u00020\u0012\n\u00020\u0014\n\u00020\u0016\n\u00020\u0018\n\u00020\u001B\n\u0006\u0012\u0002\u0018\u000B0\u001A\n\u00020\u001E\n\u00020$\n\u00020&\n\u00020,\n\u0006\u0012\u0002\u0018\u000F0\u001A\n\u000201\n\u000205\n\u000207\n\u0006\u0012\u0002\u0018\u00120\u001A\u00A8\u0006?"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel;", "Landroidx/lifecycle/ViewModel;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "tmdbCache", "Lcom/webdavrenamer/data/repository/TmdbCacheRepository;", "context", "Landroid/content/Context;", "<init>", "(Lcom/webdavrenamer/data/prefs/SettingsRepository;Lcom/webdavrenamer/data/repository/TmdbCacheRepository;Landroid/content/Context;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/webdavrenamer/ui/match/MatchViewModel$UiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "parser", "Lcom/webdavrenamer/core/parser/FilenameParser;", "engine", "Lcom/webdavrenamer/core/matcher/MatchEngine;", "scorer", "Lcom/webdavrenamer/core/matcher/ConfidenceScorer;", "setFiles", "", "files", "", "", "setMatchType", "type", "Lcom/webdavrenamer/ui/match/MatchViewModel$MatchType;", "clearError", "startMatch", "forceType", "resolveType", "parsed", "Lcom/webdavrenamer/core/parser/ParsedFilename;", "runMatchForFile", "Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "language", "filePath", "(Lcom/webdavrenamer/data/repository/TmdbCacheRepository;Lcom/webdavrenamer/core/parser/ParsedFilename;Lcom/webdavrenamer/ui/match/MatchViewModel$MatchType;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "confirmMatch", "candidate", "Lcom/webdavrenamer/core/matcher/MatchCandidate;", "manualSearch", "query", "applyEditedResults", "fetchDetail", "Lcom/webdavrenamer/core/naming/MediaMetadata;", "(Lcom/webdavrenamer/data/repository/TmdbCacheRepository;Lcom/webdavrenamer/core/matcher/MatchCandidate;Lcom/webdavrenamer/core/parser/ParsedFilename;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toMatchCandidate", "toCandidate", "Lcom/webdavrenamer/ui/match/MatchViewModel$Candidate;", "scored", "Lcom/webdavrenamer/core/matcher/ScoredCandidate;", "searchResults", "MatchType", "MatchStatus", "Progress", "Candidate", "FileMatch", "UiState", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MatchViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.TmdbCacheRepository tmdbCache = null;

    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.ui.match.MatchViewModel.UiState> _uiState = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.match.MatchViewModel.UiState> uiState = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.parser.FilenameParser parser = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.matcher.MatchEngine engine = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.matcher.ConfidenceScorer scorer = null;

    @javax.inject.Inject()
    public MatchViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.TmdbCacheRepository tmdbCache, @dagger.hilt.android.qualifiers.ApplicationContext() @org.jetbrains.annotations.NotNull() android.content.Context context) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.match.MatchViewModel.UiState> getUiState() {
        return null;
    }

    /**
     * 接收浏览器选中的视频完整路径列表。
     */
    public final void setFiles(@org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> files) {
    }

    /**
     * 阶段 1：用户切换匹配方式。
     */
    public final void setMatchType(@org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.MatchType type) {
    }

    /**
     * 清除顶部一次性错误提示。
     */
    public final void clearError() {
    }

    /**
     * 阶段 1 → 阶段 2：开始匹配。
     * 
     * 1. 读 API Key（空 → 报错不进行）。
     * 2. 逐文件解析 → 判定类型 → 搜索 → 决策 → 拉详情。
     * 3. 实时更新 [Progress.Running] 与 results/pending。
     */
    public final void startMatch(@org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.MatchType forceType) {
    }

    /**
     * 强制 > 自动（按季/集推断）。
     */
    private final com.webdavrenamer.ui.match.MatchViewModel.MatchType resolveType(com.webdavrenamer.ui.match.MatchViewModel.MatchType forceType, com.webdavrenamer.core.parser.ParsedFilename parsed) {
        return null;
    }

    /**
     * 单文件匹配：搜索 → 决策 → 拉详情。
     */
    private final java.lang.Object runMatchForFile(com.webdavrenamer.data.repository.TmdbCacheRepository tmdbCache, com.webdavrenamer.core.parser.ParsedFilename parsed, com.webdavrenamer.ui.match.MatchViewModel.MatchType type, java.lang.String language, java.lang.String filePath, kotlin.coroutines.Continuation<? super com.webdavrenamer.ui.match.MatchViewModel.FileMatch> $completion) {
        return null;
    }

    /**
     * 阶段 3：用户从候选中选择一个 → 拉详情填充 → 状态 CONFIRMED，从 pending 移到 results。
     */
    public final void confirmMatch(@org.jetbrains.annotations.NotNull() java.lang.String filePath, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.matcher.MatchCandidate candidate) {
    }

    /**
     * 阶段 3：待确认/无匹配条目手动搜索关键词，刷新候选列表（按置信度排序）。
     */
    public final void manualSearch(@org.jetbrains.annotations.NotNull() java.lang.String filePath, @org.jetbrains.annotations.NotNull() java.lang.String query, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.MatchType type) {
    }

    /**
     * Task 2.5：从 EditMatch 回写后，把外部编辑过的结果列表合并回当前 UI 状态。
     * 
     * 按 [FileMatch.status] 重新分流到 results（AUTO/CONFIRMED）或 pending（PENDING/NO_MATCH），
     * 保持与 [startMatch] 一致的分区规则。filePath 不变，便于预览页继续按路径查找。
     */
    public final void applyEditedResults(@org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> files) {
    }

    /**
     * 拉详情：电影 → [TmdbCacheRepository.getMovie]；剧集 → [TmdbCacheRepository.getTv] + [TmdbCacheRepository.getSeason]
     * 填 [MediaMetadata.seasonNumber]/[MediaMetadata.episodeNumbers]/[MediaMetadata.episodeTitles]
     * /[MediaMetadata.episodeAirDates]（多集标题 `A & B` 合并，对齐 [TmdbMapper] 规则）。
     * Task 2.3.4：详情请求经 [TmdbCacheRepository] 自动走 Room 缓存（7 天 TTL）。
     */
    private final java.lang.Object fetchDetail(com.webdavrenamer.data.repository.TmdbCacheRepository tmdbCache, com.webdavrenamer.core.matcher.MatchCandidate candidate, com.webdavrenamer.core.parser.ParsedFilename parsed, java.lang.String language, kotlin.coroutines.Continuation<? super com.webdavrenamer.core.naming.MediaMetadata> $completion) {
        return null;
    }

    /**
     * 搜索结果轻量 [MediaMetadata] → [MatchCandidate]（popularity 轻量映射不含，置 0.0）。
     */
    private final com.webdavrenamer.core.matcher.MatchCandidate toMatchCandidate(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.MediaMetadata $this$toMatchCandidate) {
        return null;
    }

    /**
     * 把评分候选 + 搜索结果拼成 UI [Candidate]（海报/简介从搜索结果 info 取）。
     */
    private final com.webdavrenamer.ui.match.MatchViewModel.Candidate toCandidate(com.webdavrenamer.core.matcher.ScoredCandidate scored, java.util.List<com.webdavrenamer.core.naming.MediaMetadata> searchResults) {
        return null;
    }

    /**
     * 匹配方式：自动识别 / 强制电影 / 强制剧集。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0006\u0008\u0086\u0081\u0002\u0012\u0001\u0001\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005j\u0002\u0008\u0006\u00F2\u0001\u000C\n\u00020\u0000\n\u0006\u0012\u0002\u0018\u00000\u0001\u00A8\u0006\u0007"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$MatchType;", "", "<init>", "(Ljava/lang/String;I)V", "AUTO", "MOVIE", "TV", "app_debug"}, xs= "", pn = "", xi = 48)
    public static enum MatchType {
        AUTO,
        MOVIE,
        TV;


        MatchType() {
        }

        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.webdavrenamer.ui.match.MatchViewModel.MatchType> getEntries() {
            return null;
        }
    }
    /**
     * 单文件匹配状态。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0007\u0008\u0086\u0081\u0002\u0012\u0001\u0001\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005j\u0002\u0008\u0006j\u0002\u0008\u0007\u00F2\u0001\u000C\n\u00020\u0000\n\u0006\u0012\u0002\u0018\u00000\u0001\u00A8\u0006\u0008"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$MatchStatus;", "", "<init>", "(Ljava/lang/String;I)V", "AUTO", "PENDING", "NO_MATCH", "CONFIRMED", "app_debug"}, xs= "", pn = "", xi = 48)
    public static enum MatchStatus {
        AUTO,
        PENDING,
        NO_MATCH,
        CONFIRMED;


        MatchStatus() {
        }

        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.webdavrenamer.ui.match.MatchViewModel.MatchStatus> getEntries() {
            return null;
        }
    }
    /**
     * 整体进度。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u001A\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u00086\u0012\u0001\u0000\u0018\u0000:\u0003\u0004\u0005\u0006B\t\u0008\u0004\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u0082\u0001\u0003\u0007\u0008\t\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\n"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$Progress;", "", "<init>", "()V", "Idle", "Running", "Done", "Lcom/webdavrenamer/ui/match/MatchViewModel$Progress$Done;", "Lcom/webdavrenamer/ui/match/MatchViewModel$Progress$Idle;", "Lcom/webdavrenamer/ui/match/MatchViewModel$Progress$Running;", "app_debug"}, xs= "", pn = "", xi = 48)
    public static abstract class Progress {

        private Progress() {
            super();
        }

        @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0000\u0008\u00C6\n\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\r\u0010\u00042\u0004\u0010\u0006(\u00028\u0001H\u00D6\u0003J\u0007\u0010\u00088\u0003H\u00D6\u0001J\u0007\u0010\n8\u0004H\u00D6\u0001\u00F2\u0001\u0016\n\u00020\u0001\n\u00020\u0005\n\u0004\u0018\u00010\u0007\n\u00020\t\n\u00020\u000B\u00A8\u0006\u000C"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$Progress$Idle;", "Lcom/webdavrenamer/ui/match/MatchViewModel$Progress;", "<init>", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
        public static final class Idle extends com.webdavrenamer.ui.match.MatchViewModel.Progress {
            @org.jetbrains.annotations.NotNull()
            public static final com.webdavrenamer.ui.match.MatchViewModel.Progress.Idle INSTANCE = null;

            private Idle() {
            }

            public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
                return false;
            }

            public int hashCode() {
                return 0;
            }

            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }
        }
        @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0000\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u0013\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u00A2\u0006\u0004\u0008\u0005\u0010\u0006J\u0007\u0010\u00078\u0001H\u00C6\u0003J\u0007\u0010\u00088\u0001H\u00C6\u0003J\u0017\u0010\t2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00018\u0002H\u00C6\u0001J\r\u0010\n2\u0004\u0010\u000C(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000E8\u0001H\u00D6\u0001J\u0007\u0010\u000F8\u0005H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\u000B\n\u0004\u0018\u00010\r\n\u00020\u0010\u00A8\u0006\u0011"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$Progress$Running;", "Lcom/webdavrenamer/ui/match/MatchViewModel$Progress;", "current", "", "total", "<init>", "(II)V", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
        public static final class Running extends com.webdavrenamer.ui.match.MatchViewModel.Progress {
            private final int current = 0;

            private final int total = 0;

            @org.jetbrains.annotations.NotNull()
            public final com.webdavrenamer.ui.match.MatchViewModel.Progress.Running copy(int current, int total) {
                return null;
            }

            public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
                return false;
            }

            public int hashCode() {
                return 0;
            }

            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }

            public Running(int current, int total) {
            }

            public final int component1() {
                return 0;
            }

            public final int getCurrent() {
                return 0;
            }

            public final int component2() {
                return 0;
            }

            public final int getTotal() {
                return 0;
            }
        }
        @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0000\u0008\u00C6\n\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\r\u0010\u00042\u0004\u0010\u0006(\u00028\u0001H\u00D6\u0003J\u0007\u0010\u00088\u0003H\u00D6\u0001J\u0007\u0010\n8\u0004H\u00D6\u0001\u00F2\u0001\u0016\n\u00020\u0001\n\u00020\u0005\n\u0004\u0018\u00010\u0007\n\u00020\t\n\u00020\u000B\u00A8\u0006\u000C"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$Progress$Done;", "Lcom/webdavrenamer/ui/match/MatchViewModel$Progress;", "<init>", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
        public static final class Done extends com.webdavrenamer.ui.match.MatchViewModel.Progress {
            @org.jetbrains.annotations.NotNull()
            public static final com.webdavrenamer.ui.match.MatchViewModel.Progress.Done INSTANCE = null;

            private Done() {
            }

            public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
                return false;
            }

            public int hashCode() {
                return 0;
            }

            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }
        }
    }
    /**
     * UI 候选：在 [MatchCandidate] 之上附加海报 URL、简介首行与置信度得分，
     * 供待确认列表展示（[MatchCandidate] 本身无海报字段）。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0010\u0006\n\u0002\u0008\u0008\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u001F\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0002\u0012\u0004\u0010\u0007(\u0003\u00A2\u0006\u0004\u0008\t\u0010\nJ\u0007\u0010\u000B8\u0001H\u00C6\u0003J\u0007\u0010\u000C8\u0002H\u00C6\u0003J\u0007\u0010\r8\u0002H\u00C6\u0003J\u0007\u0010\u000E8\u0003H\u00C6\u0003J'\u0010\u000F2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00022\u0006\u0008\u0002\u0010\u0007(\u00038\u0004H\u00C6\u0001J\r\u0010\u00102\u0004\u0010\u0012(\u00068\u0005H\u00D6\u0003J\u0007\u0010\u00138\u0007H\u00D6\u0001J\u0007\u0010\u00158\u0008H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0007H\u0003\u00A2\u0006\u0002\n\u0000\u00F2\u0001(\n\u00020\u0001\n\u00020\u0003\n\u0004\u0018\u00010\u0005\n\u00020\u0008\n\u00020\u0000\n\u00020\u0011\n\u0004\u0018\u00010\u0001\n\u00020\u0014\n\u00020\u0005\u00A8\u0006\u0016"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$Candidate;", "", "candidate", "Lcom/webdavrenamer/core/matcher/MatchCandidate;", "posterUrl", "", "overview", "score", "", "<init>", "(Lcom/webdavrenamer/core/matcher/MatchCandidate;Ljava/lang/String;Ljava/lang/String;D)V", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Candidate {
        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.core.matcher.MatchCandidate candidate = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String posterUrl = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String overview = null;

        private final double score = 0.0;

        /**
         * UI 候选：在 [MatchCandidate] 之上附加海报 URL、简介首行与置信度得分，
         * 供待确认列表展示（[MatchCandidate] 本身无海报字段）。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.Candidate copy(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.matcher.MatchCandidate candidate, @org.jetbrains.annotations.Nullable() java.lang.String posterUrl, @org.jetbrains.annotations.Nullable() java.lang.String overview, double score) {
            return null;
        }

        /**
         * UI 候选：在 [MatchCandidate] 之上附加海报 URL、简介首行与置信度得分，
         * 供待确认列表展示（[MatchCandidate] 本身无海报字段）。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * UI 候选：在 [MatchCandidate] 之上附加海报 URL、简介首行与置信度得分，
         * 供待确认列表展示（[MatchCandidate] 本身无海报字段）。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * UI 候选：在 [MatchCandidate] 之上附加海报 URL、简介首行与置信度得分，
         * 供待确认列表展示（[MatchCandidate] 本身无海报字段）。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public Candidate(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.matcher.MatchCandidate candidate, @org.jetbrains.annotations.Nullable() java.lang.String posterUrl, @org.jetbrains.annotations.Nullable() java.lang.String overview, double score) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.matcher.MatchCandidate component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.matcher.MatchCandidate getCandidate() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getPosterUrl() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component3() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getOverview() {
            return null;
        }

        public final double component4() {
            return 0.0;
        }

        public final double getScore() {
            return 0.0;
        }
    }
    /**
     * 单文件匹配结果。
     * @param filePath 视频完整路径
     * @param parsed 文件名解析结果
     * @param status 匹配状态
     * @param matched 已拉取详情的元数据（AUTO/CONFIRMED 时非空）
     * @param candidates 待确认候选（PENDING 时非空）
     * @param error 搜索/拉详情异常信息
     * @param manuallyEdited 是否经 Edit Match 手动修正（Task 2.5.1）
     * @param multiEpisodeRange 多集组合显示标签，如 `S01E01-E02` / `S01E01,E03`（Task 2.5.2）；
     *                           单集或电影为 null。便于 UI 与预览直接渲染，无需重新计算。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000E\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u000B\n\u0002\u0008\u000F\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000BA\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0003\u0012\u0006\u0008\u0002\u0010\u0008(\u0004\u0012\u0006\u0008\u0002\u0010\n(\u0006\u0012\u0006\u0008\u0002\u0010\r(\u0007\u0012\u0006\u0008\u0002\u0010\u000E(\u0008\u0012\u0006\u0008\u0002\u0010\u0010(\u0007\u00A2\u0006\u0004\u0008\u0011\u0010\u0012J\u0007\u0010\u00138\u0001H\u00C6\u0003J\u0007\u0010\u00148\u0002H\u00C6\u0003J\u0007\u0010\u00158\u0003H\u00C6\u0003J\u0007\u0010\u00168\u0004H\u00C6\u0003J\u0007\u0010\u00178\u0006H\u00C6\u0003J\u0007\u0010\u00188\u0007H\u00C6\u0003J\u0007\u0010\u00198\u0008H\u00C6\u0003J\u0007\u0010\u001A8\u0007H\u00C6\u0003JG\u0010\u001B2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00032\u0006\u0008\u0002\u0010\u0008(\u00042\u0006\u0008\u0002\u0010\n(\u00062\u0006\u0008\u0002\u0010\r(\u00072\u0006\u0008\u0002\u0010\u000E(\u00082\u0006\u0008\u0002\u0010\u0010(\u00078\tH\u00C6\u0001J\r\u0010\u001C2\u0004\u0010\u001D(\n8\u0008H\u00D6\u0003J\u0007\u0010\u001E8\u000BH\u00D6\u0001J\u0007\u0010 8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0006\u00A2\u0006\u0002\n\u0000R\t\u0010\rH\u0007\u00A2\u0006\u0002\n\u0000R\t\u0010\u000EH\u0008\u00A2\u0006\u0002\n\u0000R\t\u0010\u0010H\u0007\u00A2\u0006\u0002\n\u0000\u00F2\u0001:\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u0004\u0018\u00010\t\n\u00020\u000C\n\u0006\u0012\u0002\u0018\u00050\u000B\n\u0004\u0018\u00010\u0003\n\u00020\u000F\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020\u001F\u00A8\u0006!"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "", "filePath", "", "parsed", "Lcom/webdavrenamer/core/parser/ParsedFilename;", "status", "Lcom/webdavrenamer/ui/match/MatchViewModel$MatchStatus;", "matched", "Lcom/webdavrenamer/core/naming/MediaMetadata;", "candidates", "", "Lcom/webdavrenamer/ui/match/MatchViewModel$Candidate;", "error", "manuallyEdited", "", "multiEpisodeRange", "<init>", "(Ljava/lang/String;Lcom/webdavrenamer/core/parser/ParsedFilename;Lcom/webdavrenamer/ui/match/MatchViewModel$MatchStatus;Lcom/webdavrenamer/core/naming/MediaMetadata;Ljava/util/List;Ljava/lang/String;ZLjava/lang/String;)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class FileMatch {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String filePath = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.core.parser.ParsedFilename parsed = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.ui.match.MatchViewModel.MatchStatus status = null;

        @org.jetbrains.annotations.Nullable()
        private final com.webdavrenamer.core.naming.MediaMetadata matched = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.Candidate> candidates = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String error = null;

        private final boolean manuallyEdited = false;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String multiEpisodeRange = null;

        /**
         * 单文件匹配结果。
         * @param filePath 视频完整路径
         * @param parsed 文件名解析结果
         * @param status 匹配状态
         * @param matched 已拉取详情的元数据（AUTO/CONFIRMED 时非空）
         * @param candidates 待确认候选（PENDING 时非空）
         * @param error 搜索/拉详情异常信息
         * @param manuallyEdited 是否经 Edit Match 手动修正（Task 2.5.1）
         * @param multiEpisodeRange 多集组合显示标签，如 `S01E01-E02` / `S01E01,E03`（Task 2.5.2）；
         *                           单集或电影为 null。便于 UI 与预览直接渲染，无需重新计算。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.FileMatch copy(@org.jetbrains.annotations.NotNull() java.lang.String filePath, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.parser.ParsedFilename parsed, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.MatchStatus status, @org.jetbrains.annotations.Nullable() com.webdavrenamer.core.naming.MediaMetadata matched, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.Candidate> candidates, @org.jetbrains.annotations.Nullable() java.lang.String error, boolean manuallyEdited, @org.jetbrains.annotations.Nullable() java.lang.String multiEpisodeRange) {
            return null;
        }

        /**
         * 单文件匹配结果。
         * @param filePath 视频完整路径
         * @param parsed 文件名解析结果
         * @param status 匹配状态
         * @param matched 已拉取详情的元数据（AUTO/CONFIRMED 时非空）
         * @param candidates 待确认候选（PENDING 时非空）
         * @param error 搜索/拉详情异常信息
         * @param manuallyEdited 是否经 Edit Match 手动修正（Task 2.5.1）
         * @param multiEpisodeRange 多集组合显示标签，如 `S01E01-E02` / `S01E01,E03`（Task 2.5.2）；
         *                           单集或电影为 null。便于 UI 与预览直接渲染，无需重新计算。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 单文件匹配结果。
         * @param filePath 视频完整路径
         * @param parsed 文件名解析结果
         * @param status 匹配状态
         * @param matched 已拉取详情的元数据（AUTO/CONFIRMED 时非空）
         * @param candidates 待确认候选（PENDING 时非空）
         * @param error 搜索/拉详情异常信息
         * @param manuallyEdited 是否经 Edit Match 手动修正（Task 2.5.1）
         * @param multiEpisodeRange 多集组合显示标签，如 `S01E01-E02` / `S01E01,E03`（Task 2.5.2）；
         *                           单集或电影为 null。便于 UI 与预览直接渲染，无需重新计算。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 单文件匹配结果。
         * @param filePath 视频完整路径
         * @param parsed 文件名解析结果
         * @param status 匹配状态
         * @param matched 已拉取详情的元数据（AUTO/CONFIRMED 时非空）
         * @param candidates 待确认候选（PENDING 时非空）
         * @param error 搜索/拉详情异常信息
         * @param manuallyEdited 是否经 Edit Match 手动修正（Task 2.5.1）
         * @param multiEpisodeRange 多集组合显示标签，如 `S01E01-E02` / `S01E01,E03`（Task 2.5.2）；
         *                           单集或电影为 null。便于 UI 与预览直接渲染，无需重新计算。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public FileMatch(@org.jetbrains.annotations.NotNull() java.lang.String filePath, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.parser.ParsedFilename parsed, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.MatchStatus status, @org.jetbrains.annotations.Nullable() com.webdavrenamer.core.naming.MediaMetadata matched, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.Candidate> candidates, @org.jetbrains.annotations.Nullable() java.lang.String error, boolean manuallyEdited, @org.jetbrains.annotations.Nullable() java.lang.String multiEpisodeRange) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getFilePath() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.parser.ParsedFilename component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.parser.ParsedFilename getParsed() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.MatchStatus component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.MatchStatus getStatus() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.core.naming.MediaMetadata component4() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.core.naming.MediaMetadata getMatched() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.Candidate> component5() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.Candidate> getCandidates() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component6() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getError() {
            return null;
        }

        public final boolean component7() {
            return false;
        }

        public final boolean getManuallyEdited() {
            return false;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component8() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getMultiEpisodeRange() {
            return null;
        }
    }
    /**
     * 匹配页 UI 状态。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000E\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0006\n\u0002\u0010\u000B\n\u0002\u0008\r\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B?\u0012\u0006\u0008\u0002\u0010\u0002(\u0002\u0012\u0006\u0008\u0002\u0010\u0005(\u0003\u0012\u0006\u0008\u0002\u0010\u0007(\u0004\u0012\u0006\u0008\u0002\u0010\t(\u0006\u0012\u0006\u0008\u0002\u0010\u000B(\u0006\u0012\u0006\u0008\u0002\u0010\u000C(\u0007\u0012\u0006\u0008\u0002\u0010\r(\u0007\u00A2\u0006\u0004\u0008\u000E\u0010\u000FJ\u0007\u0010\u00148\u0002H\u00C6\u0003J\u0007\u0010\u00158\u0003H\u00C6\u0003J\u0007\u0010\u00168\u0004H\u00C6\u0003J\u0007\u0010\u00178\u0006H\u00C6\u0003J\u0007\u0010\u00188\u0006H\u00C6\u0003J\u0007\u0010\u00198\u0007H\u00C6\u0003J\u0007\u0010\u001A8\u0007H\u00C6\u0003J?\u0010\u001B2\u0006\u0008\u0002\u0010\u0002(\u00022\u0006\u0008\u0002\u0010\u0005(\u00032\u0006\u0008\u0002\u0010\u0007(\u00042\u0006\u0008\u0002\u0010\t(\u00062\u0006\u0008\u0002\u0010\u000B(\u00062\u0006\u0008\u0002\u0010\u000C(\u00072\u0006\u0008\u0002\u0010\r(\u00078\tH\u00C6\u0001J\r\u0010\u001C2\u0004\u0010\u001D(\n8\u0008H\u00D6\u0003J\u0007\u0010\u001E8\u000BH\u00D6\u0001J\u0007\u0010 8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0007H\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0006\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0006\u00A2\u0006\u0002\n\u0000R\t\u0010\u000CH\u0007\u00A2\u0006\u0002\n\u0000R\t\u0010\rH\u0007\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u00108FH\u0008\u00A2\u0006\u0006\u001A\u0004\u0008\u0012\u0010\u0013\u00F2\u0001<\n\u00020\u0001\n\u00020\u0004\n\u0006\u0012\u0002\u0018\u00010\u0003\n\u00020\u0006\n\u00020\u0008\n\u00020\n\n\u0006\u0012\u0002\u0018\u00050\u0003\n\u0004\u0018\u00010\u0004\n\u00020\u0011\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020\u001F\u00A8\u0006!"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchViewModel$UiState;", "", "selectedFiles", "", "", "matchType", "Lcom/webdavrenamer/ui/match/MatchViewModel$MatchType;", "progress", "Lcom/webdavrenamer/ui/match/MatchViewModel$Progress;", "results", "Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "pending", "error", "manualSearchingPath", "<init>", "(Ljava/util/List;Lcom/webdavrenamer/ui/match/MatchViewModel$MatchType;Lcom/webdavrenamer/ui/match/MatchViewModel$Progress;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)V", "allResolved", "", "getAllResolved", "()Z", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class UiState {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.String> selectedFiles = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.ui.match.MatchViewModel.MatchType matchType = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.ui.match.MatchViewModel.Progress progress = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> results = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> pending = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String error = null;

        @org.jetbrains.annotations.Nullable()
        private final java.lang.String manualSearchingPath = null;

        public UiState() {
            super();
        }

        /**
         * 匹配页 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.UiState copy(@org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> selectedFiles, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.MatchType matchType, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.Progress progress, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> results, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> pending, @org.jetbrains.annotations.Nullable() java.lang.String error, @org.jetbrains.annotations.Nullable() java.lang.String manualSearchingPath) {
            return null;
        }

        /**
         * 匹配页 UI 状态。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 匹配页 UI 状态。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 匹配页 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public UiState(@org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> selectedFiles, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.MatchType matchType, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.Progress progress, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> results, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> pending, @org.jetbrains.annotations.Nullable() java.lang.String error, @org.jetbrains.annotations.Nullable() java.lang.String manualSearchingPath) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getSelectedFiles() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.MatchType component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.MatchType getMatchType() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.Progress component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.match.MatchViewModel.Progress getProgress() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> component4() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> getResults() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> component5() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> getPending() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component6() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getError() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component7() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getManualSearchingPath() {
            return null;
        }

        /**
         * 待确认是否已全部处理（无 PENDING 残留）。
         */
        public final boolean getAllResolved() {
            return false;
        }
    }
}
