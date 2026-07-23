package com.webdavrenamer.ui.match;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000B\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0007\n\u0002\u0010\u000E\n\u0000\u001A2\u0010\u00002\u0004\u0010\u0002(\u00022\u0004\u0010\u0005(\u00042\u0004\u0010\u0008(\u00052\u0004\u0010\n(\u00062\u0004\u0010\u000C(\u00072\u0004\u0010\u000E(\u00082\u0006\u0008\u0002\u0010\u0010(\t8\u0000H\u0007\u001A$\u0010\u00122\u0004\u0010\u0013(\u00012\u0004\u0010\u0005(\u00052\u0004\u0010\u0014(\u00052\u0004\u0010\u0015(\u00082\u0004\u0010\u0016(\u00088\u0000H\u0003\u001A\u000C\u0010\u00172\u0004\u0010\u0018(\n8\u0000H\u0003\u00F2\u0001N\n\u00020\u0001\n\u00020\u0004\n\u0006\u0012\u0002\u0018\u00010\u0003\n\u00020\u0007\n\u0006\u0012\u0002\u0018\u00030\u0006\n\u00020\t\n\n\u0012\u0002\u0018\u0003\u0012\u0002\u0018\u00000\u000B\n\u000E\u0012\u0002\u0018\u0003\u0012\u0002\u0018\u0003\u0012\u0002\u0018\u00000\r\n\u0006\u0012\u0002\u0018\u00000\u000F\n\u00020\u0011\n\u0004\u0018\u00010\u0019\u00A8\u0006\u001A"}, d2 = {"EpisodesPanel", "", "episodes", "", "Lcom/webdavrenamer/ui/match/EditMatchViewModel$EpisodeInfo;", "selected", "", "", "multiSelect", "", "onToggle", "Lkotlin/Function1;", "onRangeSelect", "Lkotlin/Function2;", "onGenerateBundle", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "EpisodeRow", "episode", "showCheckbox", "onClick", "onLongClick", "StillThumbnail", "stillUrl", "", "app_debug"}, xs= "", pn = "", xi = 48)
public final class EpisodesPanelKt {

    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void EpisodeRow(com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo episode, boolean selected, boolean showCheckbox, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onLongClick) {
    }

    /**
     * Episodes 面板（Task 2.5.3）。
     * 
     * 浏览全季集列表（集号 + 标题 + 海报缩略图 + 简介 + 上映日期），支持：
     * - 单击勾选/取消（单集或多集模式由 [multiSelect] 决定）
     * - **长按起点 → 点击终点** 自动选中连续区间（连续多选）
     * - 「生成多集组合」按钮：基于选中集生成 `S01E01-E02` 形式条目（调用 [onGenerateBundle]）
     * 
     * 作为 EditMatchScreen 的子组件嵌入；亦可通过外层包 BottomSheet 复用。
     * 
     * @param episodes 全季集列表
     * @param selected 已选集号集合
     * @param multiSelect 是否多选模式（多集组合/对齐用）
     * @param onToggle 单集勾选回调
     * @param onRangeSelect 连续区间选择回调（起点/终点集号，双向区间）
     * @param onGenerateBundle 生成多集组合回调（仅当 selected.size >= 2 时由调用方决定可用性）
     */
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    public static final void EpisodesPanel(@org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.EditMatchViewModel.EpisodeInfo> episodes, @org.jetbrains.annotations.NotNull() java.util.Set<java.lang.Integer> selected, boolean multiSelect, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onToggle, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onRangeSelect, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onGenerateBundle, @org.jetbrains.annotations.NotNull() androidx.compose.ui.Modifier modifier) {
    }

    /**
     * 剧集 still 缩略图（无图时占位）。
     */
    @androidx.compose.runtime.Composable()
    private static final void StillThumbnail(java.lang.String stillUrl) {
    }
}
