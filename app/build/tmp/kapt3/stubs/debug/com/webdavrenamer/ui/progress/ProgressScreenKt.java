package com.webdavrenamer.ui.progress;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000^\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0007\n\u0002\u0018\u0002\n\u0002\u0008\u0007\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0004\u001A\u0014\u0010\u00002\u0004\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00028\u0000H\u0007\u001A\u001E\u0010\u00062\u0004\u0010\u0007(\u00032\u0004\u0010\t(\u00032\u0004\u0010\n(\u00042\u0004\u0010\u000C(\u00058\u0000H\u0003\u001A\u0018\u0010\u000E2\u0004\u0010\u000F(\u00062\u0004\u0010\u0011(\u00072\u0004\u0010\u0013(\u00048\u0000H\u0003\u001A\u0012\u0010\u00142\u0004\u0010\u000F(\u00062\u0004\u0010\u0013(\u00048\u0000H\u0003\u001A\u000C\u0010\u00152\u0004\u0010\u0011(\u00088\u0000H\u0003\u001A\u0018\u0010\u00162\u0004\u0010\u0017(\t2\u0004\u0010\u0018(\u00032\u0004\u0010\u0019(\n8\u0000H\u0003\u001A\u0018\u0010\u001B2\u0004\u0010\u001C(\t2\u0004\u0010\u0018(\u00032\u0004\u0010\u0019(\n8\u0000H\u0003\u001A%\u0010\u001D2\u0004\u0010\u001E(\t2\u0004\u0010\u001F(\t2\u0004\u0010 (\u000B2\u0006\u0008\u0002\u0010!(\u000C8\u0000H\u0003\u00A2\u0006\u0002\u0010#\u001A\u000C\u0010$2\u0004\u0010%(\u00108\u0000H\u0003\u001A\u0018\u0010*2\u0004\u0010+(\u00052\u0004\u0010,(\u00012\u0004\u0010\u0002(\u00018\u0000H\u0003\u00F2\u0001Z\n\u00020\u0001\n\u0006\u0012\u0002\u0018\u00000\u0003\n\u00020\u0005\n\u00020\u0008\n\u0004\u0018\u00010\u000B\n\u00020\r\n\u00020\u0010\n\u0004\u0018\u00010\u0012\n\u00020\u0012\n\u00020\u000B\n\u00020\u001A\n\u0004\u0018\u00010\u0008\n\u00020\"\n\u00020(\n\u00020)\n\n\u0012\u0002\u0018\r\u0012\u0002\u0018\u000E0'\n\u0006\u0012\u0002\u0018\u000F0&\u00A8\u0006-"}, d2 = {"ProgressScreen", "", "onBackHome", "Lkotlin/Function0;", "viewModel", "Lcom/webdavrenamer/ui/progress/ProgressViewModel;", "RunningContent", "current", "", "total", "filename", "", "pending", "", "ResultContent", "resultKind", "Lcom/webdavrenamer/ui/progress/ResultKind;", "report", "Lcom/webdavrenamer/core/rename/RenameReport;", "errorMessage", "ResultHeader", "StatsCard", "StatCell", "label", "count", "color", "Landroidx/compose/ui/graphics/Color;", "SectionHeader", "title", "FailedItemRow", "sourcePath", "reason", "httpCode", "modifier", "Landroidx/compose/ui/Modifier;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Landroidx/compose/ui/Modifier;)V", "SkippedSection", "skipped", "", "Lkotlin/Pair;", "Lcom/webdavrenamer/core/rename/RenameOperation;", "Lcom/webdavrenamer/core/rename/RenameResult$Skipped;", "ResultButtons", "hasFailures", "onRetry", "app_debug"}, xs= "", pn = "", xi = 48)
public final class ProgressScreenKt {

    @androidx.compose.runtime.Composable()
    private static final void FailedItemRow(java.lang.String sourcePath, java.lang.String reason, java.lang.Integer httpCode, androidx.compose.ui.Modifier modifier) {
    }

    /**
     * 执行进度页 + 结果报告页（计划 §M4 Task 4.3）。
     * 
     * 单页面承载两态：
     * - 执行中：线性进度条 progressCurrent/progressTotal + 当前文件名（monospace 灰色小字）+ 取消按钮。
     * - 完成：顶部大图标（✅ 全部成功 / ⚠️ 部分失败 / ❌ 全部失败 / 取消 / 出错）+ 统计卡片
     *   （成功 N / 失败 M / 跳过 K）+ 失败项列表（原文件名 + 原因 + HTTP 状态码）+ 跳过项可折叠 +
     *   底部「重试失败项」「返回首页」按钮。
     * 
     * 状态全部来自 [ProgressViewModel]；[onBackHome] 由导航层提供（返回服务器列表首页）。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ProgressScreen(@org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBackHome, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.progress.ProgressViewModel viewModel) {
    }

    @androidx.compose.runtime.Composable()
    private static final void ResultButtons(boolean hasFailures, kotlin.jvm.functions.Function0<kotlin.Unit> onRetry, kotlin.jvm.functions.Function0<kotlin.Unit> onBackHome) {
    }

    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void ResultContent(com.webdavrenamer.ui.progress.ResultKind resultKind, com.webdavrenamer.core.rename.RenameReport report, java.lang.String errorMessage) {
    }

    @androidx.compose.runtime.Composable()
    private static final void ResultHeader(com.webdavrenamer.ui.progress.ResultKind resultKind, java.lang.String errorMessage) {
    }

    @androidx.compose.runtime.Composable()
    private static final void RunningContent(int current, int total, java.lang.String filename, boolean pending) {
    }

    /**
     * 跳过项可折叠列表。
     */
    @androidx.compose.runtime.Composable()
    private static final void SkippedSection(java.util.List<kotlin.Pair<com.webdavrenamer.core.rename.RenameOperation, com.webdavrenamer.core.rename.RenameResult.Skipped>> skipped) {
    }

    /**
     * 统计卡片：成功 N / 失败 M / 跳过 K。
     */
    @androidx.compose.runtime.Composable()
    private static final void StatsCard(com.webdavrenamer.core.rename.RenameReport report) {
    }
}
