package com.webdavrenamer.data

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.webdavrenamer.core.backup.HostsSpeedTest
import com.webdavrenamer.core.naming.PresetRepository
import com.webdavrenamer.data.crypto.KeystoreCrypto
import com.webdavrenamer.data.db.AppDatabase
import com.webdavrenamer.data.db.RenameBatchDao
import com.webdavrenamer.data.db.ServerConfigDao
import com.webdavrenamer.data.db.TmdbCacheDao
import com.webdavrenamer.data.prefs.SettingsRepository
import com.webdavrenamer.data.repository.ServerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI 模块集合（计划 §M1 SubTask 1.3.4）。
 *
 * 拆为多个 [Module]：数据库、加解密、服务器仓库、命名预设、WorkManager。
 * 全部安装到 [SingletonComponent]，应用进程级单例。
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "webdav_renamer.db")
            .fallbackToDestructiveMigration() // 开发期简化：版本变更直接重建
            .build()

    @Provides
    fun provideServerConfigDao(db: AppDatabase): ServerConfigDao = db.serverConfigDao()

    /** Task 5.1.1：重命名批次/条目 DAO。 */
    @Provides
    fun provideRenameBatchDao(db: AppDatabase): RenameBatchDao = db.renameBatchDao()

    /** Task 2.3.4：TMDB 响应缓存 DAO。 */
    @Provides
    fun provideTmdbCacheDao(db: AppDatabase): TmdbCacheDao = db.tmdbCacheDao()
}

@Module
@InstallIn(SingletonComponent::class)
object CryptoModule {

    @Provides
    @Singleton
    fun provideKeystoreCrypto(@ApplicationContext context: Context): KeystoreCrypto =
        KeystoreCrypto(context)
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideServerRepository(
        dao: ServerConfigDao,
        crypto: KeystoreCrypto,
        settings: SettingsRepository,
    ): ServerRepository = ServerRepository(dao, crypto, settings)
}

@Module
@InstallIn(SingletonComponent::class)
object HostsModule {

    /**
     * Hosts 自动测速（Task 5.3.3）。无状态工具类，单例即可；内部 OkHttpClient 也单例复用。
     */
    @Provides
    @Singleton
    fun provideHostsSpeedTest(): HostsSpeedTest = HostsSpeedTest()
}

@Module
@InstallIn(SingletonComponent::class)
object NamingPresetModule {

    /** 命名预设仓库（Task 3.3 模板编辑器注入）。无状态，单例即可。 */
    @Provides
    @Singleton
    fun providePresetRepository(): PresetRepository = PresetRepository()
}

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}
