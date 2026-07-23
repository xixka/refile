package com.webdavrenamer.ui.progress;

/**
 * 执行进度/结果页 ViewModel（计划 §M4 Task 4.3）。
 * 
 * 从导航参数取得 workId（[KEY_WORK_ID]），观察 [RenameWorkScheduler.observeWork] 返回的
 * [WorkInfo] 流，把其中的 progress WorkData（[RenameWorker.KEY_PROGRESS_CURRENT]/TOTAL/
 * FILENAME）与 outputData（[RenameWorker.KEY_RESULT_REPORT_JSON]）解析成可观察的进度与
 * 报告状态。
 * 
 * 结果态提供：
 * - [retryFailed]：取报告 [RenameReport.failedOperations] 重新入队一批仅含失败项的操作，
 *   并切换 workId 观察新批次（复用当前页面，不新增回退栈）。
 * - [cancelWork]：调用 [WorkManager.cancelWorkById] 取消当前任务。
 * 
 * WorkInfo? 元素可空（work 不存在/被清理时为 null），故 [workInfo] 类型为 StateFlow<WorkInfo?>。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0008\n\u0002\u0008\u0006\n\u0002\u0010\u000E\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0006\n\u0002\u0010\t\n\u0002\u0008\u0002\n\u0002\u0010\u0002\n\u0002\u0008\u0008\u0008\u0007\u0012\u0001\u0000\u0018\u0000 7:\u00017B\u001B\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0003\u00A2\u0006\u0004\u0008\u0008\u0010\tJ\u0006\u0010/8\u0016H\u0002J\u000C\u001012\u0004\u00102(\u00178\u0016H\u0002J\u000C\u001032\u0004\u00102(\u00178\u0016H\u0002J\u0004\u001048\u0016J\u0004\u001058\u0016J\u0006\u001068\u0016H\u0014R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\nH\u0004X\u0082\u000E\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u000CH\u0005X\u0082\u000E\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u000EH\u0007X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0011H\u0008\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0013\u0010\u0014R\u000C\u0010\u0015H\nX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0017H\u000B\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0018\u0010\u0014R\u000C\u0010\u0019H\nX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u001AH\u000B\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001B\u0010\u0014R\u000C\u0010\u001CH\rX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u001EH\u000E\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001F\u0010\u0014R\u000C\u0010 H\u0010X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\"H\u0011\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\"\u0010\u0014R\u000C\u0010#H\u0010X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010$H\u0011\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008$\u0010\u0014R\u000C\u0010%H\u0013X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010'H\u0014\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008(\u0010\u0014R\u000C\u0010)H\rX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010*H\u000E\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008+\u0010\u0014R\u000C\u0010,H\u0015X\u0082\u000E\u00A2\u0006\u0002\n\u0000R\u000C\u0010.H\u000CX\u0082\u000E\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u0090\u0001\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\u000B\n\u0004\u0018\u00010\r\n\u0004\u0018\u00010\u0010\n\u0006\u0012\u0002\u0018\u00060\u000F\n\u0006\u0012\u0002\u0018\u00060\u0012\n\u00020\u0016\n\u0006\u0012\u0002\u0018\t0\u000F\n\u0006\u0012\u0002\u0018\t0\u0012\n\u0004\u0018\u00010\u001D\n\u0006\u0012\u0002\u0018\u000C0\u000F\n\u0006\u0012\u0002\u0018\u000C0\u0012\n\u00020!\n\u0006\u0012\u0002\u0018\u000F0\u000F\n\u0006\u0012\u0002\u0018\u000F0\u0012\n\u0004\u0018\u00010&\n\u0006\u0012\u0002\u0018\u00120\u000F\n\u0006\u0012\u0002\u0018\u00120\u0012\n\u00020-\n\u000200\n\u00020\u0010\u00A8\u00068"}, d2 = {"Lcom/webdavrenamer/ui/progress/ProgressViewModel;", "Landroidx/lifecycle/ViewModel;", "scheduler", "Lcom/webdavrenamer/worker/RenameWorkScheduler;", "workManager", "Landroidx/work/WorkManager;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "<init>", "(Lcom/webdavrenamer/worker/RenameWorkScheduler;Landroidx/work/WorkManager;Landroidx/lifecycle/SavedStateHandle;)V", "workId", "Ljava/util/UUID;", "collectJob", "Lkotlinx/coroutines/Job;", "_workInfo", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Landroidx/work/WorkInfo;", "workInfo", "Lkotlinx/coroutines/flow/StateFlow;", "getWorkInfo", "()Lkotlinx/coroutines/flow/StateFlow;", "_progressCurrent", "", "progressCurrent", "getProgressCurrent", "_progressTotal", "progressTotal", "getProgressTotal", "_currentFilename", "", "currentFilename", "getCurrentFilename", "_isFinished", "", "isFinished", "_isCancelled", "isCancelled", "_report", "Lcom/webdavrenamer/core/rename/RenameReport;", "report", "getReport", "_errorMessage", "errorMessage", "getErrorMessage", "retryServerId", "", "retryBatchName", "startObserving", "", "handleWorkInfo", "info", "parseReport", "retryFailed", "cancelWork", "onCleared", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ProgressViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.worker.RenameWorkScheduler scheduler = null;

    @org.jetbrains.annotations.NotNull()
    private final androidx.work.WorkManager workManager = null;

    @org.jetbrains.annotations.NotNull()
    private java.util.UUID workId;

    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job collectJob = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<androidx.work.WorkInfo> _workInfo = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<androidx.work.WorkInfo> workInfo = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _progressCurrent = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> progressCurrent = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _progressTotal = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> progressTotal = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _currentFilename = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> currentFilename = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isFinished = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isFinished = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isCancelled = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isCancelled = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.core.rename.RenameReport> _report = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.core.rename.RenameReport> report = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _errorMessage = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> errorMessage = null;

    /**
     * 重试入队所需参数（完成时从 outputData 回传）。
     */
    private long retryServerId = -1L;

    @org.jetbrains.annotations.Nullable()
    private java.lang.String retryBatchName = null;

    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.ui.progress.ProgressViewModel.Companion Companion = null;

    /**
     * 导航参数键：workId 的字符串形式。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_WORK_ID = "workId";

    @javax.inject.Inject()
    public ProgressViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.worker.RenameWorkScheduler scheduler, @org.jetbrains.annotations.NotNull() androidx.work.WorkManager workManager, @org.jetbrains.annotations.NotNull() androidx.lifecycle.SavedStateHandle savedStateHandle) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<androidx.work.WorkInfo> getWorkInfo() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getProgressCurrent() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getProgressTotal() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getCurrentFilename() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isFinished() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isCancelled() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.core.rename.RenameReport> getReport() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getErrorMessage() {
        return null;
    }

    /**
     * 订阅当前 [workId] 的 WorkInfo 流并驱动各派生状态。可被 [retryFailed] 重启。
     */
    private final void startObserving() {
    }

    /**
     * 从 [WorkInfo] 的 progress/outputData 解析进度与结果。
     */
    private final void handleWorkInfo(androidx.work.WorkInfo info) {
    }

    /**
     * 解码完整报告 JSON，并记录重试入队所需的 serverId/batchName。
     */
    private final void parseReport(androidx.work.WorkInfo info) {
    }

    /**
     * 仅重试失败项：取报告 [RenameReport.failedOperations] 重新入队一批只含失败项的操作，
     * 然后切换 [workId] 观察新批次（页面回到执行中态）。
     */
    public final void retryFailed() {
    }

    /**
     * 取消当前任务（WorkManager 异步处理，本调用立即返回）。
     */
    public final void cancelWork() {
    }

    @java.lang.Override()
    protected void onCleared() {
    }

    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\u0008\u0086\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086T\u00F2\u0001\u0008\n\u00020\u0001\n\u00020\u0005\u00A8\u0006\u0006"}, d2 = {"Lcom/webdavrenamer/ui/progress/ProgressViewModel$Companion;", "", "<init>", "()V", "KEY_WORK_ID", "", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Companion {

        private Companion() {
            super();
        }
    }
}
