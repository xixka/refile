package com.webdavrenamer.data.db;

/**
 * 应用主数据库（计划 §M1 SubTask 1.3.1 / §M5 SubTask 5.1.1）。
 * 
 * 当前包含 [ServerConfigEntity]、[RenameBatchEntity]、[RenameEntryEntity] 与 [TmdbCacheEntity]。
 * 
 * v2 变更（Task 5.1.1）：新增 rename_batches / rename_entries 两表。
 * v3 变更（Task 2.3.4）：新增 tmdb_cache 表（TMDB 详情响应缓存，减少限流压力）。
 * 用 [fallbackToDestructiveMigration]（在 [com.webdavrenamer.data.DatabaseModule] 内配置）
 * 简化开发期迁移，bump version 后旧库直接重建。
 * 
 * `exportSchema = false`：开发期简化，不导出 schema JSON。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u001E\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0008'\u0012\u0001\u0000\u0018\u0000B\u0007\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\u0006\u0010\u00048\u0001H&J\u0006\u0010\u00068\u0002H&J\u0006\u0010\u00088\u0003H&\u00F2\u0001\u0010\n\u00020\u0001\n\u00020\u0005\n\u00020\u0007\n\u00020\t\u00A8\u0006\n"}, d2 = {"Lcom/webdavrenamer/data/db/AppDatabase;", "Landroidx/room/RoomDatabase;", "<init>", "()V", "serverConfigDao", "Lcom/webdavrenamer/data/db/ServerConfigDao;", "renameBatchDao", "Lcom/webdavrenamer/data/db/RenameBatchDao;", "tmdbCacheDao", "Lcom/webdavrenamer/data/db/TmdbCacheDao;", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.room.Database(entities = {com.webdavrenamer.data.db.ServerConfigEntity.class, com.webdavrenamer.data.db.RenameBatchEntity.class, com.webdavrenamer.data.db.RenameEntryEntity.class, com.webdavrenamer.data.db.TmdbCacheEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {

    public AppDatabase() {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public abstract com.webdavrenamer.data.db.ServerConfigDao serverConfigDao();

    /**
     * 重命名批次/条目 DAO（Task 5.1.1）。
     */
    @org.jetbrains.annotations.NotNull()
    public abstract com.webdavrenamer.data.db.RenameBatchDao renameBatchDao();

    /**
     * TMDB 响应缓存 DAO（Task 2.3.4）。
     */
    @org.jetbrains.annotations.NotNull()
    public abstract com.webdavrenamer.data.db.TmdbCacheDao tmdbCacheDao();
}
