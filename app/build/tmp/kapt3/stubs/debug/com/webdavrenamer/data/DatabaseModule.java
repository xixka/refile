package com.webdavrenamer.data;

/**
 * Hilt DI 模块集合（计划 §M1 SubTask 1.3.4）。
 * 
 * 拆为多个 [Module]：数据库、加解密、服务器仓库、命名预设、WorkManager。
 * 全部安装到 [SingletonComponent]，应用进程级单例。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0008\u00C7\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\u000E\u0010\u00042\u0006\u0008\u0001\u0010\u0006(\u00028\u0001H\u0007J\u000C\u0010\u00082\u0004\u0010\n(\u00018\u0003H\u0007J\u000C\u0010\u000B2\u0004\u0010\n(\u00018\u0004H\u0007J\u000C\u0010\r2\u0004\u0010\n(\u00018\u0005H\u0007\u00F2\u0001\u0018\n\u00020\u0001\n\u00020\u0005\n\u00020\u0007\n\u00020\t\n\u00020\u000C\n\u00020\u000E\u00A8\u0006\u000F"}, d2 = {"Lcom/webdavrenamer/data/DatabaseModule;", "", "<init>", "()V", "provideAppDatabase", "Lcom/webdavrenamer/data/db/AppDatabase;", "context", "Landroid/content/Context;", "provideServerConfigDao", "Lcom/webdavrenamer/data/db/ServerConfigDao;", "db", "provideRenameBatchDao", "Lcom/webdavrenamer/data/db/RenameBatchDao;", "provideTmdbCacheDao", "Lcom/webdavrenamer/data/db/TmdbCacheDao;", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.Module()
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DatabaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.data.DatabaseModule INSTANCE = null;

    private DatabaseModule() {
        super();
    }

    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.db.AppDatabase provideAppDatabase(@dagger.hilt.android.qualifiers.ApplicationContext() @org.jetbrains.annotations.NotNull() android.content.Context context) {
        return null;
    }

    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.db.ServerConfigDao provideServerConfigDao(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.AppDatabase db) {
        return null;
    }

    /**
     * Task 5.1.1：重命名批次/条目 DAO。
     */
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.db.RenameBatchDao provideRenameBatchDao(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.AppDatabase db) {
        return null;
    }

    /**
     * Task 2.3.4：TMDB 响应缓存 DAO。
     */
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.db.TmdbCacheDao provideTmdbCacheDao(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.AppDatabase db) {
        return null;
    }
}
