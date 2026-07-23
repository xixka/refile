package com.webdavrenamer.data.db;

/**
 * 重命名批次 DAO（计划 §M5 SubTask 5.1.1）。
 * 
 * - [insertBatchWithEntries] 在单事务内插入批次与其下所有条目，保证历史一致性。
 * - [observeBatches] 按 createdAt 倒序供历史页实时刷新。
 * - [markReverted] 撤销完成后更新 isReverted + revertedAt。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\t\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0010\u0002\n\u0002\u0008\t\u0008g\u0012\u0001\u0000\u0018\u0000J\u0006\u0010\u00028\u0003H'J\u0012\u0010\u00062\u0004\u0010\u0007(\u00058\u0004H\u00A7@\u00A2\u0006\u0002\u0010\tJ\u0012\u0010\n2\u0004\u0010\u000C(\u00058\u0007H\u00A7@\u00A2\u0006\u0002\u0010\tJ\u0012\u0010\r2\u0004\u0010\u000E(\u00018\u0005H\u00A7@\u00A2\u0006\u0002\u0010\u000FJ\u0012\u0010\u00102\u0004\u0010\u0012(\u00078\u0008H\u00A7@\u00A2\u0006\u0002\u0010\u0013J\u0018\u0010\u00142\u0004\u0010\u000E(\u00012\u0004\u0010\u0012(\u00078\u0005H\u0097@\u00A2\u0006\u0002\u0010\u0015J\u0018\u0010\u00162\u0004\u0010\u0007(\u00052\u0004\u0010\u0017(\u00058\u0008H\u00A7@\u00A2\u0006\u0002\u0010\u0018J\u0012\u0010\u00192\u0004\u0010\u0007(\u00058\u0008H\u00A7@\u00A2\u0006\u0002\u0010\t\u00F2\u00012\n\u00020\u0001\n\u00020\u0005\n\u0006\u0012\u0002\u0018\u00010\u0004\n\u0006\u0012\u0002\u0018\u00020\u0003\n\u0004\u0018\u00010\u0005\n\u00020\u0008\n\u00020\u000B\n\u0006\u0012\u0002\u0018\u00060\u0004\n\u00020\u0011\u00A8\u0006\u001A"}, d2 = {"Lcom/webdavrenamer/data/db/RenameBatchDao;", "", "observeBatches", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/webdavrenamer/data/db/RenameBatchEntity;", "getBatch", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntries", "Lcom/webdavrenamer/data/db/RenameEntryEntity;", "batchId", "insertBatch", "batch", "(Lcom/webdavrenamer/data/db/RenameBatchEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertEntries", "", "entries", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertBatchWithEntries", "(Lcom/webdavrenamer/data/db/RenameBatchEntity;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markReverted", "revertedAt", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBatch", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.room.Dao()
public abstract interface RenameBatchDao {

    /**
     * 观察所有批次（按 createdAt 倒序，最新在前）。
     */
    @androidx.room.Query(value = "SELECT * FROM rename_batches ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.webdavrenamer.data.db.RenameBatchEntity>> observeBatches();

    /**
     * 按 id 取单条批次。
     */
    @androidx.room.Query(value = "SELECT * FROM rename_batches WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBatch(long id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.data.db.RenameBatchEntity> $completion);

    /**
     * 取某批次下所有条目（按 id 升序，与执行顺序一致；撤销时由调用方倒序处理）。
     */
    @androidx.room.Query(value = "SELECT * FROM rename_entries WHERE batchId = :batchId ORDER BY id ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntries(long batchId, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.util.List<com.webdavrenamer.data.db.RenameEntryEntity>> $completion);

    /**
     * 插入批次。
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBatch(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.RenameBatchEntity batch, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.lang.Long> $completion);

    /**
     * 插入条目列表。
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEntries(@org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.db.RenameEntryEntity> entries, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);

    /**
     * 在单事务内插入批次与条目：先插批次拿到 id，再把 batchId 回填到每条 entry 后批量插入。
     */
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBatchWithEntries(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.RenameBatchEntity batch, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.db.RenameEntryEntity> entries, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.lang.Long> $completion);

    /**
     * 标记批次已撤销：isReverted=true 并记录撤销时间。
     */
    @androidx.room.Query(value = "UPDATE rename_batches SET isReverted = 1, revertedAt = :revertedAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markReverted(long id, long revertedAt, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);

    /**
     * 删除某批次（外键 CASCADE 会连带删除其下条目）。
     */
    @androidx.room.Query(value = "DELETE FROM rename_batches WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteBatch(long id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);

    /**
     * 重命名批次 DAO（计划 §M5 SubTask 5.1.1）。
     * 
     * - [insertBatchWithEntries] 在单事务内插入批次与其下所有条目，保证历史一致性。
     * - [observeBatches] 按 createdAt 倒序供历史页实时刷新。
     * - [markReverted] 撤销完成后更新 isReverted + revertedAt。
     */
    @kotlin.Metadata(k = 3, mv = {2, 0, 0}, d1 = {}, d2 = {}, xs= "", pn = "", xi = 48)
    public static final class DefaultImpls {

        /**
         * 在单事务内插入批次与条目：先插批次拿到 id，再把 batchId 回填到每条 entry 后批量插入。
         */
        @androidx.room.Transaction()
        @org.jetbrains.annotations.Nullable()
        public static java.lang.Object insertBatchWithEntries(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.RenameBatchDao $this, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.RenameBatchEntity batch, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.db.RenameEntryEntity> entries, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
            return null;
        }
    }
}
