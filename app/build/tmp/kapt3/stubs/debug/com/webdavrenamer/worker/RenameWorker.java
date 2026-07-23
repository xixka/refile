package com.webdavrenamer.worker;

/**
 * 批量重命名后台 Worker（计划 §M4 Task 4.2.1）。
 * 
 * 经 WorkManager 调度，[RenameWorkScheduler] 入队时把 [List]<[RenameOperation]> 序列化为
 * JSON 存入 WorkData（[KEY_OPERATIONS_JSON]），App 被杀后 WorkManager 可恢复继续执行。
 * 
 * 流程：
 * 1. 取 serverId + operationsJson，按 [ServerConfigEntity] 解密密码并构造 [WebDavClient]
 *    （构造方式参照 [ServerRepository] 内部 buildFullBaseUrl）。
 * 2. [setForeground] 提升为前台服务（dataSync 类型，manifest 已声明），保证长任务不被回收。
 * 3. [RenameExecutor.execute] 执行，进度回调里更新通知 + [setProgress] 供 UI 观察。
 * 4. 结果：
 *    - 执行完成（全成功或部分失败） → [Result.success] 携带 [KEY_RESULT_REPORT_JSON]（完整报告 JSON，
 *      含统计与失败原因，供 UI 展示/重试），并回传 [KEY_SERVER_ID]/[KEY_BATCH_NAME] 供结果页重新入队
 *    - 网络可重试错误（IOException） → [Result.retry]
 *    - 不可恢复（配置缺失/输入非法/其它异常） → [Result.failure] 携带 [KEY_ERROR]
 * 
 * 用 [HiltWorker] + [AssistedInject] 注入 [ServerRepository]/[KeystoreCrypto]/[HistoryRepository]；
 * [WebDavRenamerApp] 已实现 Configuration.Provider 绑定 HiltWorkerFactory。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\u0008\u0007\u0012\u0001\u0000\u0018\u0000 #:\u0001#B+\u0008\u0007\u0012\u0006\u0008\u0001\u0010\u0002(\u0001\u0012\u0006\u0008\u0001\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0003\u0012\u0004\u0010\u0008(\u0004\u0012\u0004\u0010\n(\u0005\u00A2\u0006\u0004\u0008\u000C\u0010\rJ\u000C\u0010\u000E8\u0006H\u0096@\u00A2\u0006\u0002\u0010\u0010J\u000C\u0010\u00112\u0004\u0010\u0013(\u00088\u0007H\u0002J\u0006\u0010\u00158\tH\u0002J\u001E\u0010\u00172\u0004\u0010\u0019(\u000B2\u0004\u0010\u001B(\u000C2\u0004\u0010\u001D(\u000C2\u0004\u0010\u001E(\r8\nH\u0002J\u001E\u0010 2\u0004\u0010\u0019(\u000B2\u0004\u0010\u001B(\u000C2\u0004\u0010\u001D(\u000C2\u0004\u0010\u001E(\r8\u000EH\u0002J\u001E\u0010\"2\u0004\u0010\u0019(\u000B2\u0004\u0010\u001B(\u000C2\u0004\u0010\u001D(\u000C2\u0004\u0010\u001E(\u000F8\tH\u0002R\u000C\u0010\u0006H\u0003X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\nH\u0005X\u0082\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001D\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\t\n\u00020\u000B\n\u00020\u000F\n\u00020\u0012\n\u00020\u0014\n\u00020\u0016\n\u00020\u0018\n\u0004\u0018\u00010\u001A\n\u00020\u001C\n\u0004\u0018\u00010\u001F\n\u00020!\n\u00020\u001F\u00A8\u0006$"}, d2 = {"Lcom/webdavrenamer/worker/RenameWorker;", "Landroidx/work/CoroutineWorker;", "appContext", "Landroid/content/Context;", "params", "Landroidx/work/WorkerParameters;", "serverRepo", "Lcom/webdavrenamer/data/repository/ServerRepository;", "crypto", "Lcom/webdavrenamer/data/crypto/KeystoreCrypto;", "historyRepo", "Lcom/webdavrenamer/data/repository/HistoryRepository;", "<init>", "(Landroid/content/Context;Landroidx/work/WorkerParameters;Lcom/webdavrenamer/data/repository/ServerRepository;Lcom/webdavrenamer/data/crypto/KeystoreCrypto;Lcom/webdavrenamer/data/repository/HistoryRepository;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buildClient", "Lcom/webdavrenamer/core/webdav/WebDavClient;", "entity", "Lcom/webdavrenamer/data/db/ServerConfigEntity;", "ensureChannel", "", "buildNotification", "Landroidx/core/app/NotificationCompat$Builder;", "batchName", "", "current", "", "total", "op", "Lcom/webdavrenamer/core/rename/RenameOperation;", "buildForegroundInfo", "Landroidx/work/ForegroundInfo;", "notifyProgress", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.hilt.work.HiltWorker()
public final class RenameWorker extends androidx.work.CoroutineWorker {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.ServerRepository serverRepo = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.crypto.KeystoreCrypto crypto = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.HistoryRepository historyRepo = null;

    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.worker.RenameWorker.Companion Companion = null;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_SERVER_ID = "server_id";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_OPERATIONS_JSON = "operations_json";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_BATCH_NAME = "batch_name";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_RESULT_REPORT_JSON = "result_report_json";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_ERROR = "error";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_PROGRESS_CURRENT = "progress_current";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_PROGRESS_TOTAL = "progress_total";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_PROGRESS_FILENAME = "progress_filename";

    private static final long INVALID_SERVER_ID = -1L;

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_RENAME = "rename_channel";

    private static final int NOTIFICATION_ID = 4242;

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DEFAULT_TITLE = "\u6B63\u5728\u91CD\u547D\u540D";

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String NOTIFICATION_CHANNEL_NAME = "\u91CD\u547D\u540D\u4EFB\u52A1";

    @dagger.assisted.AssistedInject()
    public RenameWorker(@dagger.assisted.Assisted() @org.jetbrains.annotations.NotNull() android.content.Context appContext, @dagger.assisted.Assisted() @org.jetbrains.annotations.NotNull() androidx.work.WorkerParameters params, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.ServerRepository serverRepo, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.crypto.KeystoreCrypto crypto, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.HistoryRepository historyRepo) {
        super(null, null);
    }

    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public java.lang.Object doWork(@org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
        return null;
    }

    /**
     * 按 [ServerConfigEntity] 拼出完整 baseUrl（参照 ServerRepository.buildFullBaseUrl）
     * 并解密密码，构造已带认证拦截的 [WebDavClient]。
     */
    private final com.webdavrenamer.core.webdav.WebDavClient buildClient(com.webdavrenamer.data.db.ServerConfigEntity entity) {
        return null;
    }

    /**
     * 创建 LOW 优先级通知渠道（仅首次），不发声、状态栏可见。
     */
    private final void ensureChannel() {
    }

    /**
     * 构造进度通知：标题含 batchName 或 "正在重命名" + (current/total)，内容为当前文件名。
     */
    private final androidx.core.app.NotificationCompat.Builder buildNotification(java.lang.String batchName, int current, int total, com.webdavrenamer.core.rename.RenameOperation op) {
        return null;
    }

    /**
     * 构造前台信息，API 29+ 标注 dataSync 服务类型。
     */
    private final androidx.work.ForegroundInfo buildForegroundInfo(java.lang.String batchName, int current, int total, com.webdavrenamer.core.rename.RenameOperation op) {
        return null;
    }

    /**
     * 进度回调中刷新已显示的通知（同一 NOTIFICATION_ID 原地更新）。
     */
    private final void notifyProgress(java.lang.String batchName, int current, int total, com.webdavrenamer.core.rename.RenameOperation op) {
    }

    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0008\n\u0002\u0010\t\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0003\u0008\u0086\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086TR\u0007\u0010\u0006H\u0001X\u0086TR\u0007\u0010\u0007H\u0001X\u0086TR\u0007\u0010\u0008H\u0001X\u0086TR\u0007\u0010\tH\u0001X\u0086TR\u0007\u0010\nH\u0001X\u0086TR\u0007\u0010\u000BH\u0001X\u0086TR\u0007\u0010\u000CH\u0001X\u0086TR\u0007\u0010\rH\u0002X\u0082TR\u0007\u0010\u000FH\u0001X\u0082TR\u0007\u0010\u0010H\u0003X\u0082TR\u0007\u0010\u0012H\u0001X\u0082TR\u0007\u0010\u0013H\u0001X\u0082T\u00F2\u0001\u0010\n\u00020\u0001\n\u00020\u0005\n\u00020\u000E\n\u00020\u0011\u00A8\u0006\u0014"}, d2 = {"Lcom/webdavrenamer/worker/RenameWorker$Companion;", "", "<init>", "()V", "KEY_SERVER_ID", "", "KEY_OPERATIONS_JSON", "KEY_BATCH_NAME", "KEY_RESULT_REPORT_JSON", "KEY_ERROR", "KEY_PROGRESS_CURRENT", "KEY_PROGRESS_TOTAL", "KEY_PROGRESS_FILENAME", "INVALID_SERVER_ID", "", "CHANNEL_RENAME", "NOTIFICATION_ID", "", "DEFAULT_TITLE", "NOTIFICATION_CHANNEL_NAME", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Companion {

        private Companion() {
            super();
        }
    }
}
