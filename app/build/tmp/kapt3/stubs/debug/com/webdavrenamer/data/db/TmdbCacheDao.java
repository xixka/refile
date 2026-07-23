package com.webdavrenamer.data.db;

/**
 * TMDB 响应缓存 DAO（Task 2.3.4）。
 * 
 * 仅做单表 KV 式 CRUD：按业务键 [getByKey] 命中、[insert] 覆盖写入（同键 REPLACE）、
 * [deleteOlderThan] 清理过期、[clearAll] 全清。命中后的过期判定由仓库层按 `cachedAt` 计算。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0010\u0002\n\u0002\u0008\u0004\n\u0002\u0010\t\n\u0002\u0008\u0004\u0008g\u0012\u0001\u0000\u0018\u0000J\u0012\u0010\u00022\u0004\u0010\u0004(\u00028\u0001H\u00A7@\u00A2\u0006\u0002\u0010\u0006J\u0012\u0010\u00072\u0004\u0010\t(\u00048\u0003H\u00A7@\u00A2\u0006\u0002\u0010\nJ\u0012\u0010\u000B2\u0004\u0010\u000C(\u00058\u0003H\u00A7@\u00A2\u0006\u0002\u0010\u000EJ\u000C\u0010\u000F8\u0003H\u00A7@\u00A2\u0006\u0002\u0010\u0010\u00F2\u0001\u001A\n\u00020\u0001\n\u0004\u0018\u00010\u0003\n\u00020\u0005\n\u00020\u0008\n\u00020\u0003\n\u00020\r\u00A8\u0006\u0011"}, d2 = {"Lcom/webdavrenamer/data/db/TmdbCacheDao;", "", "getByKey", "Lcom/webdavrenamer/data/db/TmdbCacheEntity;", "key", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "", "entity", "(Lcom/webdavrenamer/data/db/TmdbCacheEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteOlderThan", "before", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.room.Dao()
public abstract interface TmdbCacheDao {

    /**
     * 按业务键取单条缓存（最多一条，唯一索引保证）。
     */
    @androidx.room.Query(value = "SELECT * FROM tmdb_cache WHERE cacheKey = :key LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByKey(@org.jetbrains.annotations.NotNull() java.lang.String key, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.data.db.TmdbCacheEntity> $completion);

    /**
     * 写入/覆盖缓存（同 cacheKey 由唯一索引 + REPLACE 覆盖）。
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.TmdbCacheEntity entity, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);

    /**
     * 删除早于 [before]（epoch millis）的缓存，用于过期清理。
     */
    @androidx.room.Query(value = "DELETE FROM tmdb_cache WHERE cachedAt < :before")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOlderThan(long before, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);

    /**
     * 清空全部缓存（设置页"清除 TMDB 缓存"调用）。
     */
    @androidx.room.Query(value = "DELETE FROM tmdb_cache")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAll(@org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
