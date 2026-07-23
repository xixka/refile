package com.webdavrenamer.ui.settings;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010\u0000\n\u0000\u001A\u0014\u0010\u00002\u0004\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00028\u0000H\u0007\u001A\u001E\u0010\u00062\u0004\u0010\u0007(\u00032\u0004\u0010\t(\u00012\u0004\u0010\n(\u00012\u0004\u0010\u000B(\u00048\u0000H\u0003\u001A\u0012\u0010\r2\u0004\u0010\u000E(\u00052\u0004\u0010\u0010(\u00068\u0000H\u0003\u00F2\u0001 \n\u00020\u0001\n\u0006\u0012\u0002\u0018\u00000\u0003\n\u00020\u0005\n\u00020\u0008\n\u00020\u000C\n\u00020\u000F\n\u00020\u0011\u00A8\u0006\u0012"}, d2 = {"BackupScreen", "", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/webdavrenamer/ui/settings/BackupViewModel;", "ImportPreviewCard", "changes", "Lcom/webdavrenamer/data/backup/ImportChanges;", "onApply", "onCancel", "applyEnabled", "", "PreviewLine", "label", "", "value", "", "app_debug"}, xs= "", pn = "", xi = 48)
public final class BackupScreenKt {

    /**
     * 备份与恢复页（计划 §M5 SubTask 5.2）。
     * 
     * 布局：
     * - TopAppBar「备份与恢复」+ 返回。
     * - 导出区：可选口令输入框、「包含密码」开关（需口令非空才启用）、「导出」按钮。
     * - 导入区：「选择备份文件」按钮 → 解析后显示变更预览 → 「应用导入」按钮。
     * - 进行中显示 [CircularProgressIndicator]，结果以 Snackbar 反馈。
     * 
     * SAF 由 [rememberLauncherForActivityResult] 持有，ViewModel 通过一次性事件触发其启动，
     * 解耦 ViewModel 与 Activity 结果 API。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void BackupScreen(@org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.settings.BackupViewModel viewModel) {
    }

    /**
     * 导入变更预览卡片。
     */
    @androidx.compose.runtime.Composable()
    private static final void ImportPreviewCard(com.webdavrenamer.data.backup.ImportChanges changes, kotlin.jvm.functions.Function0<kotlin.Unit> onApply, kotlin.jvm.functions.Function0<kotlin.Unit> onCancel, boolean applyEnabled) {
    }

    @androidx.compose.runtime.Composable()
    private static final void PreviewLine(java.lang.String label, java.lang.Object value) {
    }
}
