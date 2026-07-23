package com.webdavrenamer.worker;

/**
 * 重命名任务入队辅助（计划 §M4 Task 4.2.2）。
 * 
 * 把 [List]<[RenameOperation]> 经 [RenameOperationJson] 序列化后塞入 WorkData，
 * 构造 [Constraints]（需联网，与 WebDAV MOVE/MKCOL 一致），
 * 通过 [WorkManager] 入队一次性 [RenameWorker]，返回 work.id 供 UI 观察进度。
 * 
 * WorkData 键常量复用 [RenameWorker] 的 companion，保证入参与读取一致。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\u0012\u0001\u0000\u0018\u0000B\u0011\u0008\u0007\u0012\u0006\u0008\u0001\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0018\u0010\u00062\u0004\u0010\u0008(\u00032\u0004\u0010\n(\u00052\u0006\u0008\u0002\u0010\r(\u00068\u0002J\n\u0010\u000F2\u0004\u0010\u0012(\u00028\u0008R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u00010\n\u00020\u0001\n\u00020\u0003\n\u00020\u0007\n\u00020\t\n\u00020\u000C\n\u0006\u0012\u0002\u0018\u00040\u000B\n\u0004\u0018\u00010\u000E\n\u0004\u0018\u00010\u0011\n\u0006\u0012\u0002\u0018\u00070\u0010\u00A8\u0006\u0013"}, d2 = {"Lcom/webdavrenamer/worker/RenameWorkScheduler;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "enqueue", "Ljava/util/UUID;", "serverId", "", "operations", "", "Lcom/webdavrenamer/core/rename/RenameOperation;", "batchName", "", "observeWork", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/work/WorkInfo;", "workId", "app_debug"}, xs= "", pn = "", xi = 48)
public final class RenameWorkScheduler {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;

    @javax.inject.Inject()
    public RenameWorkScheduler(@dagger.hilt.android.qualifiers.ApplicationContext() @org.jetbrains.annotations.NotNull() android.content.Context context) {
        super();
    }

    /**
     * 入队一批重命名任务。
     * 
     * @param serverId   目标服务器配置 id。
     * @param operations 待执行的重命名操作列表（将序列化为 JSON 存入 WorkData）。
     * @param batchName  批次名（可选，用于通知标题展示）。
     * @return WorkRequest 的 [UUID]，供 [observeWork] 观察进度/状态。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.UUID enqueue(long serverId, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.core.rename.RenameOperation> operations, @org.jetbrains.annotations.Nullable() java.lang.String batchName) {
        return null;
    }

    /**
     * 观察某 work id 的状态/进度（[WorkInfo] 含 state 与 progress WorkData）。
     * 
     * WorkManager 2.9.x 的 [WorkManager.getWorkInfoByIdFlow] 返回 [Flow]<[WorkInfo]?>（可空）：
     * WorkInfo 在 work 不存在/被清理时为 null，故元素类型可空。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<androidx.work.WorkInfo> observeWork(@org.jetbrains.annotations.NotNull() java.util.UUID workId) {
        return null;
    }
}
