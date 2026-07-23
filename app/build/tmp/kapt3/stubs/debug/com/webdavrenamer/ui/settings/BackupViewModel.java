package com.webdavrenamer.ui.settings;

/**
 * 备份与恢复 ViewModel（计划 §M5 SubTask 5.2）。
 * 
 * 持有导出/导入 UI 状态，并通过 SAF 事件驱动 Composable 启动文档选择器。
 * - [pickExportFile] / [pickImportFile]：发出一次性事件，由 Composable 收集后启动 SAF。
 * - [export] / [importFromUri]：在 SAF 回调返回 Uri 后执行实际读写。
 * - [applyImport]：确认后落库。
 * 
 * 口令与「包含密码」联动：UI 仅在口令非空时启用「包含密码」开关。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000B\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0006\n\u0002\u0010\u000E\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0002\u0008\u0005\u0008\u0007\u0012\u0001\u0000\u0018\u0000B\u0017\u0008\u0007\u0012\u0006\u0008\u0001\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\n\u0010'2\u0004\u0010)(\u00068\u0012J\n\u0010*2\u0004\u0010)(\u00038\u0012J\u0004\u0010+8\u0012J\u0004\u0010,8\u0012J\n\u0010-2\u0004\u0010.(\u00138\u0012J\n\u001002\u0004\u0010.(\u00138\u0012J\u0004\u001018\u0012J\u0004\u001028\u0012J\u0004\u001038\u0012R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u000BH\u0005\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\r\u0010\u000ER\u000C\u0010\u000FH\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0010H\u0005\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0011\u0010\u000ER\u000C\u0010\u0012H\u0007X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0014H\u0008\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0015\u0010\u000ER\u000C\u0010\u0016H\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0017H\u0005\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0018\u0010\u000ER\u000C\u0010\u0019H\nX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u001BH\u000B\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001C\u0010\u000ER\u000C\u0010\u001DH\rX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u001EH\u000E\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001F\u0010\u000ER\u000C\u0010 H\u0010X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010#H\u0011\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008%\u0010&\u00F2\u0001|\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\n\n\u0006\u0012\u0002\u0018\u00030\t\n\u0006\u0012\u0002\u0018\u00030\u000C\n\u00020\u0013\n\u0006\u0012\u0002\u0018\u00060\t\n\u0006\u0012\u0002\u0018\u00060\u000C\n\u0004\u0018\u00010\u001A\n\u0006\u0012\u0002\u0018\t0\t\n\u0006\u0012\u0002\u0018\t0\u000C\n\u0004\u0018\u00010\u0013\n\u0006\u0012\u0002\u0018\u000C0\t\n\u0006\u0012\u0002\u0018\u000C0\u000C\n\u00020\"\n\u0006\u0012\u0002\u0018\u000F0!\n\u0006\u0012\u0002\u0018\u000F0$\n\u00020(\n\u00020/\u00A8\u00064"}, d2 = {"Lcom/webdavrenamer/ui/settings/BackupViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "repository", "Lcom/webdavrenamer/data/backup/BackupRepository;", "<init>", "(Landroid/content/Context;Lcom/webdavrenamer/data/backup/BackupRepository;)V", "_exporting", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "exporting", "Lkotlinx/coroutines/flow/StateFlow;", "getExporting", "()Lkotlinx/coroutines/flow/StateFlow;", "_importing", "importing", "getImporting", "_passphrase", "", "passphrase", "getPassphrase", "_includePasswords", "includePasswords", "getIncludePasswords", "_importPreview", "Lcom/webdavrenamer/data/backup/ImportResult;", "importPreview", "getImportPreview", "_result", "result", "getResult", "_events", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/webdavrenamer/ui/settings/BackupEvent;", "events", "Lkotlinx/coroutines/flow/SharedFlow;", "getEvents", "()Lkotlinx/coroutines/flow/SharedFlow;", "setPassphrase", "", "value", "toggleIncludePasswords", "pickExportFile", "pickImportFile", "export", "uri", "Landroid/net/Uri;", "importFromUri", "applyImport", "cancelImportPreview", "clearResult", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class BackupViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.backup.BackupRepository repository = null;

    /**
     * 是否正在导出。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _exporting = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> exporting = null;

    /**
     * 是否正在导入。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _importing = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> importing = null;

    /**
     * 口令（可选）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _passphrase = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> passphrase = null;

    /**
     * 是否在口令加密时包含服务器明文密码。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _includePasswords = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> includePasswords = null;

    /**
     * 导入预览（解析成功后展示，确认后落库）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.data.backup.ImportResult> _importPreview = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.data.backup.ImportResult> importPreview = null;

    /**
     * Snackbar 文案。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _result = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> result = null;

    /**
     * 一次性 SAF 事件，由 Composable 收集后启动对应的文档选择器。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.webdavrenamer.ui.settings.BackupEvent> _events = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<com.webdavrenamer.ui.settings.BackupEvent> events = null;

    @javax.inject.Inject()
    public BackupViewModel(@dagger.hilt.android.qualifiers.ApplicationContext() @org.jetbrains.annotations.NotNull() android.content.Context context, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.BackupRepository repository) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getExporting() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getImporting() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getPassphrase() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getIncludePasswords() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.data.backup.ImportResult> getImportPreview() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getResult() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<com.webdavrenamer.ui.settings.BackupEvent> getEvents() {
        return null;
    }

    public final void setPassphrase(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    public final void toggleIncludePasswords(boolean value) {
    }

    /**
     * 请求启动 SAF CreateDocument 选择导出保存位置。
     */
    public final void pickExportFile() {
    }

    /**
     * 请求启动 SAF OpenDocument 选择导入备份文件。
     */
    public final void pickImportFile() {
    }

    /**
     * SAF 回调返回 Uri 后执行导出并写入文件。
     */
    public final void export(@org.jetbrains.annotations.NotNull() android.net.Uri uri) {
    }

    /**
     * SAF 回调返回 Uri 后读取并解析备份文件，生成导入预览。
     */
    public final void importFromUri(@org.jetbrains.annotations.NotNull() android.net.Uri uri) {
    }

    /**
     * 确认应用导入预览。
     */
    public final void applyImport() {
    }

    /**
     * 取消导入预览（不落库）。
     */
    public final void cancelImportPreview() {
    }

    /**
     * 清除 Snackbar 文案。
     */
    public final void clearResult() {
    }
}
