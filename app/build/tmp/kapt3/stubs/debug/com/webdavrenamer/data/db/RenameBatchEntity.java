package com.webdavrenamer.data.db;

/**
 * 重命名批次实体（计划 §M5 SubTask 5.1.1）。
 * 
 * 一次批量重命名（[com.webdavrenamer.worker.RenameWorker] 执行完成）对应一条批次记录，
 * 其下挂多条 [RenameEntryEntity]（每条对应一个 [com.webdavrenamer.core.rename.RenameOperation]）。
 * 
 * 字段冗余 [serverName] 快照：避免服务器配置被删除后历史列表丢失可读名称。
 * 
 * @property id               自增主键。
 * @property serverId         关联服务器配置 id（服务器可能已被删除）。
 * @property serverName        服务器名快照（创建批次时的名称）。
 * @property batchName        用户可读批次名（与通知标题一致）。
 * @property createdAt        创建时间戳（毫秒）。
 * @property totalOperations  操作总数（= entries 条数）。
 * @property succeededCount    成功数（[com.webdavrenamer.core.rename.RenameResult.Success] + [com.webdavrenamer.core.rename.RenameResult.Partial]）。
 * @property failedCount       失败数（[com.webdavrenamer.core.rename.RenameResult.Failed]）。
 * @property isReverted       是否已整批撤销（撤销后置 true，UI 置灰且禁用撤销按钮）。
 * @property revertedAt       撤销时间戳（毫秒，可空）。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0002\u0008\u0003\n\u0002\u0010\u0008\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0002\u0008\u0014\u0008\u0087\u0008\u0012\u0001\u0000\u0018\u0000BK\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0004\u0010\u0005(\u0002\u0012\u0004\u0010\u0007(\u0002\u0012\u0006\u0008\u0002\u0010\u0008(\u0001\u0012\u0004\u0010\t(\u0003\u0012\u0004\u0010\u000B(\u0003\u0012\u0004\u0010\u000C(\u0003\u0012\u0006\u0008\u0002\u0010\r(\u0004\u0012\u0006\u0008\u0002\u0010\u000F(\u0005\u00A2\u0006\u0004\u0008\u0010\u0010\u0011J\u0007\u0010\u00138\u0001H\u00C6\u0003J\u0007\u0010\u00148\u0001H\u00C6\u0003J\u0007\u0010\u00158\u0002H\u00C6\u0003J\u0007\u0010\u00168\u0002H\u00C6\u0003J\u0007\u0010\u00178\u0001H\u00C6\u0003J\u0007\u0010\u00188\u0003H\u00C6\u0003J\u0007\u0010\u00198\u0003H\u00C6\u0003J\u0007\u0010\u001A8\u0003H\u00C6\u0003J\u0007\u0010\u001B8\u0004H\u00C6\u0003J\u0007\u0010\u001C8\u0005H\u00C6\u0003JW\u0010\u001D2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00022\u0006\u0008\u0002\u0010\u0007(\u00022\u0006\u0008\u0002\u0010\u0008(\u00012\u0006\u0008\u0002\u0010\t(\u00032\u0006\u0008\u0002\u0010\u000B(\u00032\u0006\u0008\u0002\u0010\u000C(\u00032\u0006\u0008\u0002\u0010\r(\u00042\u0006\u0008\u0002\u0010\u000F(\u00058\u0006H\u00C6\u0001J\r\u0010\u001E2\u0004\u0010\u001F(\u00078\u0004H\u00D6\u0003J\u0007\u0010 8\u0003H\u00D6\u0001J\u0007\u0010!8\u0002H\u00D6\u0001R\u000E\u0010\u00028\u0006H\u0001X\u0087\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0007H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u000CH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\rH\u0004\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u000FH\u0005\u00A2\u0006\u0004\n\u0002\u0010\u0012\u00F2\u0001$\n\u00020\u0001\n\u00020\u0003\n\u00020\u0006\n\u00020\n\n\u00020\u000E\n\u0004\u0018\u00010\u0003\n\u00020\u0000\n\u0004\u0018\u00010\u0001\u00A8\u0006\""}, d2 = {"Lcom/webdavrenamer/data/db/RenameBatchEntity;", "", "id", "", "serverId", "serverName", "", "batchName", "createdAt", "totalOperations", "", "succeededCount", "failedCount", "isReverted", "", "revertedAt", "<init>", "(JJLjava/lang/String;Ljava/lang/String;JIIIZLjava/lang/Long;)V", "Ljava/lang/Long;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "copy", "equals", "other", "hashCode", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.room.Entity(tableName = "rename_batches")
public final class RenameBatchEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;

    private final long serverId = 0L;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String serverName = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String batchName = null;

    private final long createdAt = 0L;

    private final int totalOperations = 0;

    private final int succeededCount = 0;

    private final int failedCount = 0;

    private final boolean isReverted = false;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long revertedAt = null;

    /**
     * 重命名批次实体（计划 §M5 SubTask 5.1.1）。
     * 
     * 一次批量重命名（[com.webdavrenamer.worker.RenameWorker] 执行完成）对应一条批次记录，
     * 其下挂多条 [RenameEntryEntity]（每条对应一个 [com.webdavrenamer.core.rename.RenameOperation]）。
     * 
     * 字段冗余 [serverName] 快照：避免服务器配置被删除后历史列表丢失可读名称。
     * 
     * @property id               自增主键。
     * @property serverId         关联服务器配置 id（服务器可能已被删除）。
     * @property serverName        服务器名快照（创建批次时的名称）。
     * @property batchName        用户可读批次名（与通知标题一致）。
     * @property createdAt        创建时间戳（毫秒）。
     * @property totalOperations  操作总数（= entries 条数）。
     * @property succeededCount    成功数（[com.webdavrenamer.core.rename.RenameResult.Success] + [com.webdavrenamer.core.rename.RenameResult.Partial]）。
     * @property failedCount       失败数（[com.webdavrenamer.core.rename.RenameResult.Failed]）。
     * @property isReverted       是否已整批撤销（撤销后置 true，UI 置灰且禁用撤销按钮）。
     * @property revertedAt       撤销时间戳（毫秒，可空）。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.db.RenameBatchEntity copy(long id, long serverId, @org.jetbrains.annotations.NotNull() java.lang.String serverName, @org.jetbrains.annotations.NotNull() java.lang.String batchName, long createdAt, int totalOperations, int succeededCount, int failedCount, boolean isReverted, @org.jetbrains.annotations.Nullable() java.lang.Long revertedAt) {
        return null;
    }

    /**
     * 重命名批次实体（计划 §M5 SubTask 5.1.1）。
     * 
     * 一次批量重命名（[com.webdavrenamer.worker.RenameWorker] 执行完成）对应一条批次记录，
     * 其下挂多条 [RenameEntryEntity]（每条对应一个 [com.webdavrenamer.core.rename.RenameOperation]）。
     * 
     * 字段冗余 [serverName] 快照：避免服务器配置被删除后历史列表丢失可读名称。
     * 
     * @property id               自增主键。
     * @property serverId         关联服务器配置 id（服务器可能已被删除）。
     * @property serverName        服务器名快照（创建批次时的名称）。
     * @property batchName        用户可读批次名（与通知标题一致）。
     * @property createdAt        创建时间戳（毫秒）。
     * @property totalOperations  操作总数（= entries 条数）。
     * @property succeededCount    成功数（[com.webdavrenamer.core.rename.RenameResult.Success] + [com.webdavrenamer.core.rename.RenameResult.Partial]）。
     * @property failedCount       失败数（[com.webdavrenamer.core.rename.RenameResult.Failed]）。
     * @property isReverted       是否已整批撤销（撤销后置 true，UI 置灰且禁用撤销按钮）。
     * @property revertedAt       撤销时间戳（毫秒，可空）。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * 重命名批次实体（计划 §M5 SubTask 5.1.1）。
     * 
     * 一次批量重命名（[com.webdavrenamer.worker.RenameWorker] 执行完成）对应一条批次记录，
     * 其下挂多条 [RenameEntryEntity]（每条对应一个 [com.webdavrenamer.core.rename.RenameOperation]）。
     * 
     * 字段冗余 [serverName] 快照：避免服务器配置被删除后历史列表丢失可读名称。
     * 
     * @property id               自增主键。
     * @property serverId         关联服务器配置 id（服务器可能已被删除）。
     * @property serverName        服务器名快照（创建批次时的名称）。
     * @property batchName        用户可读批次名（与通知标题一致）。
     * @property createdAt        创建时间戳（毫秒）。
     * @property totalOperations  操作总数（= entries 条数）。
     * @property succeededCount    成功数（[com.webdavrenamer.core.rename.RenameResult.Success] + [com.webdavrenamer.core.rename.RenameResult.Partial]）。
     * @property failedCount       失败数（[com.webdavrenamer.core.rename.RenameResult.Failed]）。
     * @property isReverted       是否已整批撤销（撤销后置 true，UI 置灰且禁用撤销按钮）。
     * @property revertedAt       撤销时间戳（毫秒，可空）。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * 重命名批次实体（计划 §M5 SubTask 5.1.1）。
     * 
     * 一次批量重命名（[com.webdavrenamer.worker.RenameWorker] 执行完成）对应一条批次记录，
     * 其下挂多条 [RenameEntryEntity]（每条对应一个 [com.webdavrenamer.core.rename.RenameOperation]）。
     * 
     * 字段冗余 [serverName] 快照：避免服务器配置被删除后历史列表丢失可读名称。
     * 
     * @property id               自增主键。
     * @property serverId         关联服务器配置 id（服务器可能已被删除）。
     * @property serverName        服务器名快照（创建批次时的名称）。
     * @property batchName        用户可读批次名（与通知标题一致）。
     * @property createdAt        创建时间戳（毫秒）。
     * @property totalOperations  操作总数（= entries 条数）。
     * @property succeededCount    成功数（[com.webdavrenamer.core.rename.RenameResult.Success] + [com.webdavrenamer.core.rename.RenameResult.Partial]）。
     * @property failedCount       失败数（[com.webdavrenamer.core.rename.RenameResult.Failed]）。
     * @property isReverted       是否已整批撤销（撤销后置 true，UI 置灰且禁用撤销按钮）。
     * @property revertedAt       撤销时间戳（毫秒，可空）。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public RenameBatchEntity(long id, long serverId, @org.jetbrains.annotations.NotNull() java.lang.String serverName, @org.jetbrains.annotations.NotNull() java.lang.String batchName, long createdAt, int totalOperations, int succeededCount, int failedCount, boolean isReverted, @org.jetbrains.annotations.Nullable() java.lang.Long revertedAt) {
        super();
    }

    public final long component1() {
        return 0L;
    }

    public final long getId() {
        return 0L;
    }

    public final long component2() {
        return 0L;
    }

    public final long getServerId() {
        return 0L;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getServerName() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBatchName() {
        return null;
    }

    public final long component5() {
        return 0L;
    }

    public final long getCreatedAt() {
        return 0L;
    }

    public final int component6() {
        return 0;
    }

    public final int getTotalOperations() {
        return 0;
    }

    public final int component7() {
        return 0;
    }

    public final int getSucceededCount() {
        return 0;
    }

    public final int component8() {
        return 0;
    }

    public final int getFailedCount() {
        return 0;
    }

    public final boolean component9() {
        return false;
    }

    public final boolean isReverted() {
        return false;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component10() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getRevertedAt() {
        return null;
    }
}
