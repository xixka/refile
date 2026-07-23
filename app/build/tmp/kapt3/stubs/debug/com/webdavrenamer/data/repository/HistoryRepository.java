package com.webdavrenamer.data.repository;

/**
 * 历史记录仓库（计划 §M5 SubTask 5.1.2 + 5.1.4）。
 * 
 * 职责：
 * - 透传 [RenameBatchDao] 的 Flow 观察 / 单条查询。
 * - [recordBatch]：[RenameWorker] 执行完成后把 [RenameReport] 落库为一条批次 + 多条条目。
 * - [revertBatch]：取批次条目，**按 id 倒序**反向 MOVE（targetPath → sourcePath，含 companions 反向），
 *   中途失败不中断，最终标记 isReverted=true，返回 [RevertResult]。
 * 
 * 安全：密码仅经 [ServerRepository.clientFor] 解密用于构造 [WebDavClient]，绝不进入历史/日志。
 * 反向 MOVE 用 `Overwrite: F`，避免覆盖目标已存在文件。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\t\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0004\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0007\u0012\u0001\u0000\u0018\u0000 ':\u0001'B\u0015\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\u0004\u0010\u00088\u0005J\u0012\u0010\u000C2\u0004\u0010\r(\u00078\u0006H\u0086@\u00A2\u0006\u0002\u0010\u000FJ\u0012\u0010\u00102\u0004\u0010\u0012(\u00078\tH\u0086@\u00A2\u0006\u0002\u0010\u000FJ*\u0010\u00132\u0004\u0010\u0014(\u00072\u0004\u0010\u0015(\n2\u0004\u0010\u0017(\u000B2\u0004\u0010\u0018(\u000C2\u0004\u0010\u001A(\u000E8\u0007H\u0086@\u00A2\u0006\u0002\u0010\u001CJ\u0012\u0010\u001D2\u0004\u0010\u0012(\u00078\u000FH\u0086@\u00A2\u0006\u0002\u0010\u000FJ\u000C\u0010\u001F2\u0004\u0010 (\u00108\nH\u0002J\u000C\u0010\"2\u0004\u0010 (\u00108\u000BH\u0002J\u000C\u0010#2\u0004\u0010$(\u00078\nH\u0002J\u000C\u0010%2\u0004\u0010&(\u00078\nH\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001X\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u000B\n\u0006\u0012\u0002\u0018\u00030\n\n\u0006\u0012\u0002\u0018\u00040\t\n\u0004\u0018\u00010\u000B\n\u00020\u000E\n\u00020\u0011\n\u0006\u0012\u0002\u0018\u00080\n\n\u00020\u0016\n\u0004\u0018\u00010\u0016\n\u00020\u0019\n\u00020\u001B\n\u0006\u0012\u0002\u0018\r0\n\n\u00020\u001E\n\u00020!\u00A8\u0006("}, d2 = {"Lcom/webdavrenamer/data/repository/HistoryRepository;", "", "dao", "Lcom/webdavrenamer/data/db/RenameBatchDao;", "serverRepository", "Lcom/webdavrenamer/data/repository/ServerRepository;", "<init>", "(Lcom/webdavrenamer/data/db/RenameBatchDao;Lcom/webdavrenamer/data/repository/ServerRepository;)V", "observeBatches", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/webdavrenamer/data/db/RenameBatchEntity;", "getBatch", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntries", "Lcom/webdavrenamer/data/db/RenameEntryEntity;", "batchId", "recordBatch", "serverId", "serverName", "", "batchName", "report", "Lcom/webdavrenamer/core/rename/RenameReport;", "operations", "Lcom/webdavrenamer/core/rename/RenameOperation;", "(JLjava/lang/String;Ljava/lang/String;Lcom/webdavrenamer/core/rename/RenameReport;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "revertBatch", "Lcom/webdavrenamer/data/repository/RevertResult;", "statusOf", "result", "Lcom/webdavrenamer/core/rename/RenameResult;", "errorMessageOf", "defaultBatchName", "now", "formatMillis", "ms", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
public final class HistoryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.db.RenameBatchDao dao = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.ServerRepository serverRepository = null;

    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.data.repository.HistoryRepository.Companion Companion = null;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_SUCCESS = "SUCCESS";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_PARTIAL = "PARTIAL";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_FAILED = "FAILED";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_SKIPPED = "SKIPPED";

    @javax.inject.Inject()
    public HistoryRepository(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.RenameBatchDao dao, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.ServerRepository serverRepository) {
        super();
    }

    /**
     * 观察所有批次（按 createdAt 倒序）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.webdavrenamer.data.db.RenameBatchEntity>> observeBatches() {
        return null;
    }

    /**
     * 按 id 取单条批次。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBatch(long id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.data.db.RenameBatchEntity> $completion) {
        return null;
    }

    /**
     * 取某批次下所有条目（按 id 升序）。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEntries(long batchId, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.util.List<com.webdavrenamer.data.db.RenameEntryEntity>> $completion) {
        return null;
    }

    /**
     * 把一次批量执行的结果落库为历史。
     * 
     * 从 [report].results 逐条映射为 [RenameEntryEntity]（status 取自 [RenameResult] 子类型），
     * 连同批次统计写入单事务。在 IO dispatcher 执行。
     * 
     * 调用方（[com.webdavrenamer.worker.RenameWorker]）应在执行成功后调用本方法。
     * 若记录失败被吞掉，不影响 Worker 的成功返回（历史为辅助功能）。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object recordBatch(long serverId, @org.jetbrains.annotations.NotNull() java.lang.String serverName, @org.jetbrains.annotations.Nullable() java.lang.String batchName, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.rename.RenameReport report, @kotlin.Suppress(names = {"UNUSED_PARAMETER"}) @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.core.rename.RenameOperation> operations, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }

    /**
     * 整批撤销（SubTask 5.1.4）。
     * 
     * 流程：
     * 1. 取批次；不存在 → [RevertResult.Failure]。已撤销 → [RevertResult.Failure]（避免重复撤销）。
     * 2. 取服务器配置；已删除 → [RevertResult.Failure]（提示「服务器已删除」）。
     * 3. 取条目，按 id 倒序，仅对 status ∈ {SUCCESS, PARTIAL} 的条目执行反向 MOVE
     *    （FAILED/SKIPPED 主文件未落地，无需撤销）。companions 同样反向 MOVE。
     * 4. 中途失败不中断，记录到 [FailedEntry]。
     * 5. 全部尝试完后标记 batch.isReverted=true, revertedAt=now。
     * 6. 返回 [RevertResult.Success] / [RevertResult.Partial]。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object revertBatch(long batchId, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.data.repository.RevertResult> $completion) {
        return null;
    }

    /**
     * 取 [RenameResult] 子类型对应的 status 字符串。
     */
    private final java.lang.String statusOf(com.webdavrenamer.core.rename.RenameResult result) {
        return null;
    }

    /**
     * 取失败/部分失败时的可读错误信息。
     */
    private final java.lang.String errorMessageOf(com.webdavrenamer.core.rename.RenameResult result) {
        return null;
    }

    /**
     * 默认批次名：按时间戳生成。
     */
    private final java.lang.String defaultBatchName(long now) {
        return null;
    }

    private final java.lang.String formatMillis(long ms) {
        return null;
    }

    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0004\u0008\u0086\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086TR\u0007\u0010\u0006H\u0001X\u0086TR\u0007\u0010\u0007H\u0001X\u0086TR\u0007\u0010\u0008H\u0001X\u0086T\u00F2\u0001\u0008\n\u00020\u0001\n\u00020\u0005\u00A8\u0006\t"}, d2 = {"Lcom/webdavrenamer/data/repository/HistoryRepository$Companion;", "", "<init>", "()V", "STATUS_SUCCESS", "", "STATUS_PARTIAL", "STATUS_FAILED", "STATUS_SKIPPED", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Companion {

        private Companion() {
            super();
        }
    }
}
