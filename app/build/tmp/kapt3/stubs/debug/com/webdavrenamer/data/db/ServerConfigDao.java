package com.webdavrenamer.data.db;

/**
 * 服务器配置 DAO（计划 §M1 SubTask 1.3.1）。
 * 
 * 仅提供本地 CRUD 与 Flow 观察；不在此处处理加解密（由 Repository 层负责）。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\t\n\u0002\u0008\u0005\n\u0002\u0010\u0002\n\u0002\u0008\u0003\u0008g\u0012\u0001\u0000\u0018\u0000J\u0006\u0010\u00028\u0003H'J\u0012\u0010\u00062\u0004\u0010\u0007(\u00058\u0004H\u00A7@\u00A2\u0006\u0002\u0010\tJ\u0012\u0010\n2\u0004\u0010\u000B(\u00018\u0005H\u00A7@\u00A2\u0006\u0002\u0010\u000CJ\u0012\u0010\r2\u0004\u0010\u000B(\u00018\u0006H\u00A7@\u00A2\u0006\u0002\u0010\u000CJ\u0012\u0010\u000F2\u0004\u0010\u000B(\u00018\u0006H\u00A7@\u00A2\u0006\u0002\u0010\u000CJ\u0012\u0010\u00102\u0004\u0010\u0007(\u00058\u0006H\u00A7@\u00A2\u0006\u0002\u0010\t\u00F2\u0001&\n\u00020\u0001\n\u00020\u0005\n\u0006\u0012\u0002\u0018\u00010\u0004\n\u0006\u0012\u0002\u0018\u00020\u0003\n\u0004\u0018\u00010\u0005\n\u00020\u0008\n\u00020\u000E\u00A8\u0006\u0011"}, d2 = {"Lcom/webdavrenamer/data/db/ServerConfigDao;", "", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/webdavrenamer/data/db/ServerConfigEntity;", "getById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "server", "(Lcom/webdavrenamer/data/db/ServerConfigEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "", "delete", "deleteById", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.room.Dao()
public abstract interface ServerConfigDao {

    @androidx.room.Query(value = "SELECT * FROM server_configs ORDER BY updatedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.webdavrenamer.data.db.ServerConfigEntity>> observeAll();

    @androidx.room.Query(value = "SELECT * FROM server_configs WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.data.db.ServerConfigEntity> $completion);

    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigEntity server, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.lang.Long> $completion);

    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigEntity server, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);

    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigEntity server, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);

    @androidx.room.Query(value = "DELETE FROM server_configs WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
