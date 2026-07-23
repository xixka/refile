package com.webdavrenamer.ui.preview;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000d\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000E\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0007\u001A&\u0010\u00002\u0004\u0010\u0002(\u00012\u0004\u0010\u0004(\u00032\u0004\u0010\u0007(\u00042\u0004\u0010\t(\u00062\u0006\u0008\u0002\u0010\u000F(\u00078\u0000H\u0007\u001A$\u0010\u00112\u0004\u0010\u0012(\u00082\u0004\u0010\u0014(\u00082\u0004\u0010\u0015(\u00082\u0004\u0010\u0016(\u00082\u0004\u0010\u0017(\u00048\u0000H\u0003\u001A\u0018\u0010\u00182\u0004\u0010\u0019(\t2\u0004\u0010\u001B(\n2\u0004\u0010\u001D(\u000B8\u0000H\u0003\u001A \u0010\u001E2\u0004\u0010\u001F(\u000C2\u0004\u0010!(\u00042\u0004\u0010\"(\u00042\u0006\u0008\u0002\u0010#(\r8\u0000H\u0003\u001A\u0012\u0010%2\u0004\u0010\u001F(\u000C2\u0004\u0010\"(\u00048\u0000H\u0003\u001A\u000C\u0010&2\u0004\u0010'(\u000E8\u0000H\u0003\u001A\u0018\u0010)2\u0004\u0010\u001F(\u000C2\u0004\u0010*(\u00042\u0004\u0010+(\u000F8\u0000H\u0003\u001A\u0018\u0010,2\u0004\u0010-(\u00102\u0004\u0010*(\u00042\u0004\u0010.(\u000F8\u0000H\u0003\u00F2\u0001o\n\u00020\u0001\n\u00020\u0003\n\u00020\u0006\n\u0006\u0012\u0002\u0018\u00020\u0005\n\u0006\u0012\u0002\u0018\u00000\u0008\n\u00110\u000B\u00A2\u0006\u000C\u0008\u000C\u0012\u0008\u0008\r\u0012\u0004\u0008\u0008(\u000E\n\n\u0012\u0002\u0018\u0005\u0012\u0002\u0018\u00000\n\n\u00020\u0010\n\u00020\u0013\n\u00020\u001A\n\u00020\u001C\n\u00020\u000B\n\u00020 \n\u00020$\n\u00020(\n\n\u0012\u0002\u0018\u000B\u0012\u0002\u0018\u00000\n\n\u0006\u0012\u0002\u0018\u000C0\u0005\u00A8\u0006/"}, d2 = {"PreviewScreen", "", "serverId", "", "matches", "", "Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "onBack", "Lkotlin/Function0;", "onProceedToProgress", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "workId", "viewModel", "Lcom/webdavrenamer/ui/preview/PreviewViewModel;", "StatsRow", "autoCount", "", "needsConfirmCount", "conflictCount", "excludedCount", "onShowExcluded", "StatChip", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "iconTint", "Landroidx/compose/ui/graphics/Color;", "label", "SwipeToDismissItem", "item", "Lcom/webdavrenamer/ui/preview/PreviewViewModel$PreviewItem;", "onExclude", "onClick", "modifier", "Landroidx/compose/ui/Modifier;", "PreviewCard", "CompanionRow", "companion", "Lcom/webdavrenamer/core/rename/CompanionRename;", "EditTargetDialog", "onDismiss", "onConfirm", "ExcludedDialog", "excludedItems", "onRestore", "app_debug"}, xs= "", pn = "", xi = 48)
public final class PreviewScreenKt {

    @androidx.compose.runtime.Composable()
    private static final void CompanionRow(com.webdavrenamer.core.rename.CompanionRename companion) {
    }

    /**
     * 手动修改目标路径弹窗：预填当前 [item.targetPath]，确认后回写并重新触发冲突检测。
     */
    @androidx.compose.runtime.Composable()
    private static final void EditTargetDialog(com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem item, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onConfirm) {
    }

    /**
     * 已排除项弹窗：列出被左滑排除的条目，每条可单独恢复（调用 [PreviewViewModel.includeItem]）。
     */
    @androidx.compose.runtime.Composable()
    private static final void ExcludedDialog(java.util.List<com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem> excludedItems, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onRestore) {
    }

    /**
     * 预览卡片：状态图标 + 原路径（小字灰色）→ 新路径（大字主题色/冲突标红）+ 冲突原因 + 伴随文件可折叠 + 渲染警告。
     */
    @androidx.compose.runtime.Composable()
    private static final void PreviewCard(com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem item, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }

    /**
     * 重命名预览页（计划 §M3 Task 3.4，只预览不执行）。
     * 
     * 顶部 [TopAppBar] 返回；统计芯片行展示 自动✅/待确认⚠️/冲突❌/已排除 计数；
     * 存在冲突时显示「一键解决冲突」按钮；[LazyColumn] 逐行展示原路径（小字灰色）→ 新路径
     * （大字主题色，冲突标红）+ 状态图标 + 伴随文件可折叠；左滑排除单条，点击单条弹窗手动修改；
     * 底部 [BottomAppBar] 执行按钮把操作入队 WorkManager 后导航到进度页。
     * 
     * [matches] 由导航层从 Activity 作用域的 [com.webdavrenamer.ui.match.MatchSessionViewModel.matches]
     * 传入，落地到 [PreviewViewModel.load]。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    public static final void PreviewScreen(long serverId, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> matches, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onProceedToProgress, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.preview.PreviewViewModel viewModel) {
    }

    /**
     * 统计芯片行：自动✅ / 待确认⚠️ / 冲突❌ / 已排除（点击查看并恢复）。
     */
    @androidx.compose.runtime.Composable()
    private static final void StatsRow(int autoCount, int needsConfirmCount, int conflictCount, int excludedCount, kotlin.jvm.functions.Function0<kotlin.Unit> onShowExcluded) {
    }

    /**
     * 单条预览项容器：[SwipeToDismissBox] 左滑（EndToStart）排除，点击卡片打开编辑弹窗。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void SwipeToDismissItem(com.webdavrenamer.ui.preview.PreviewViewModel.PreviewItem item, kotlin.jvm.functions.Function0<kotlin.Unit> onExclude, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
}
