package com.webdavrenamer.ui.history;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000n\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0006\u001A\u0014\u0010\u00002\u0004\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00028\u0000H\u0007\u001A\u0018\u0010\u00062\u0004\u0010\u0007(\u00042\u0004\u0010\n(\u00052\u0004\u0010\u000C(\u00078\u0000H\u0003\u001A\u001A\u0010\u000F2\u0004\u0010\u0010(\u00032\u0004\u0010\u0011(\u00012\u0006\u0008\u0002\u0010\u0012(\u00088\u0000H\u0003\u001A\u0006\u0010\u00148\u0000H\u0003\u001A\u001E\u0010\u00152\u0004\u0010\u0016(\t2\u0004\u0010\u0018(\n2\u0004\u0010\u001A(\u000B2\u0004\u0010\u001C(\u000C8\u0000H\u0003\u001A\u001E\u0010\u001E2\u0004\u0010\u0010(\u00032\u0004\u0010\u001F(\u000E2\u0004\u0010!(\u00102\u0004\u0010\n(\u00058\u0000H\u0003\u001A\u000C\u0010#2\u0004\u0010\u0010(\u00038\u0000H\u0003\u001A\u0018\u0010$2\u0004\u0010%(\r2\u0004\u0010&(\u000F2\u0004\u0010\u001C(\u000C8\u0000H\u0003\u001A\u0012\u0010'2\u0004\u0010&(\u000F2\u0004\u0010\u001C(\u000C8\u0012H\u0003\u001A\u0018\u0010*2\u0004\u0010\u0010(\u00032\u0004\u0010+(\u000C2\u0004\u0010,(\u00018\u0000H\u0003\u001A\u000C\u0010-2\u0004\u0010.(\u00068\tH\u0002\u00F2\u0001p\n\u00020\u0001\n\u0006\u0012\u0002\u0018\u00000\u0003\n\u00020\u0005\n\u00020\t\n\u0006\u0012\u0002\u0018\u00030\u0008\n\u00020\u000B\n\u00020\u000E\n\n\u0012\u0002\u0018\u0006\u0012\u0002\u0018\u00000\r\n\u00020\u0013\n\u00020\u0017\n\u00020\u0019\n\u00020\u001B\n\u00020\u001D\n\u00020 \n\u0006\u0012\u0002\u0018\r0\u0008\n\u00020\"\n\n\u0012\u0002\u0018\t\u0012\u0002\u0018\u000F0\r\n\u00020)\n\n\u0012\u0002\u0018\u0011\u0012\u0002\u0018\u000B0(\u00A8\u0006/"}, d2 = {"HistoryScreen", "", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/webdavrenamer/ui/history/HistoryViewModel;", "BatchListContent", "batches", "", "Lcom/webdavrenamer/data/db/RenameBatchEntity;", "contentPadding", "Landroidx/compose/foundation/layout/PaddingValues;", "onOpen", "Lkotlin/Function1;", "", "BatchCard", "batch", "onClick", "modifier", "Landroidx/compose/ui/Modifier;", "RevertedChip", "StatText", "label", "", "count", "", "color", "Landroidx/compose/ui/graphics/Color;", "disabled", "", "BatchDetailContent", "entries", "Lcom/webdavrenamer/data/db/RenameEntryEntity;", "statusOf", "Lcom/webdavrenamer/ui/history/HistoryViewModel$EntryStatus;", "BatchSummaryCard", "EntryRow", "entry", "status", "statusIcon", "Lkotlin/Pair;", "Landroidx/compose/ui/graphics/vector/ImageVector;", "RevertButtons", "reverting", "onRevert", "formatTimestamp", "ms", "app_debug"}, xs= "", pn = "", xi = 48)
public final class HistoryScreenKt {

    /**
     * 单条批次卡片：批次名 + 服务器名 + 时间 + 成功/失败统计 + 已撤销标签。
     */
    @androidx.compose.runtime.Composable()
    private static final void BatchCard(com.webdavrenamer.data.db.RenameBatchEntity batch, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }

    @androidx.compose.runtime.Composable()
    private static final void BatchDetailContent(com.webdavrenamer.data.db.RenameBatchEntity batch, java.util.List<com.webdavrenamer.data.db.RenameEntryEntity> entries, kotlin.jvm.functions.Function1<? super java.lang.String, ? extends com.webdavrenamer.ui.history.HistoryViewModel.EntryStatus> statusOf, androidx.compose.foundation.layout.PaddingValues contentPadding) {
    }

    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void BatchListContent(java.util.List<com.webdavrenamer.data.db.RenameBatchEntity> batches, androidx.compose.foundation.layout.PaddingValues contentPadding, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onOpen) {
    }

    @androidx.compose.runtime.Composable()
    private static final void BatchSummaryCard(com.webdavrenamer.data.db.RenameBatchEntity batch) {
    }

    /**
     * 单条 entry 行：状态图标 + 原路径→新路径（monospace 小字）。
     */
    @androidx.compose.runtime.Composable()
    private static final void EntryRow(com.webdavrenamer.data.db.RenameEntryEntity entry, com.webdavrenamer.ui.history.HistoryViewModel.EntryStatus status, boolean disabled) {
    }

    /**
     * 历史记录页（计划 §M5 SubTask 5.1.3）。
     * 
     * 两态共用一个 Composable：
     * - 列表态（[selectedBatchId] == null）：批次卡片倒序展示，点击进入详情态。
     * - 详情态（[selectedBatchId] != null）：条目列表 + 底部「撤销整批」按钮（已撤销禁用）。
     * 
     * 撤销进行中显示 CircularProgressIndicator 并禁用按钮；完成弹 Snackbar 反馈「已回滚 N/M 条」。
     * 已撤销的批次卡片置灰并显示「已撤销」标签。
     * 
     * [onBack]：列表态返回上层；详情态返回列表态。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void HistoryScreen(@org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.history.HistoryViewModel viewModel) {
    }

    @androidx.compose.runtime.Composable()
    private static final void RevertButtons(com.webdavrenamer.data.db.RenameBatchEntity batch, boolean reverting, kotlin.jvm.functions.Function0<kotlin.Unit> onRevert) {
    }

    @androidx.compose.runtime.Composable()
    private static final void RevertedChip() {
    }

    /**
     * 格式化时间戳为 yyyy-MM-dd HH:mm。
     */
    private static final java.lang.String formatTimestamp(long ms) {
        return null;
    }

    @androidx.compose.runtime.Composable()
    private static final kotlin.Pair<androidx.compose.ui.graphics.vector.ImageVector, androidx.compose.ui.graphics.Color> statusIcon(com.webdavrenamer.ui.history.HistoryViewModel.EntryStatus status, boolean disabled) {
        return null;
    }
}
