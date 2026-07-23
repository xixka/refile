package com.webdavrenamer.data;

@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0008\u00C7\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\u0018\u0010\u00042\u0004\u0010\u0006(\u00022\u0004\u0010\u0008(\u00032\u0004\u0010\n(\u00048\u0001H\u0007\u00F2\u0001\u0014\n\u00020\u0001\n\u00020\u0005\n\u00020\u0007\n\u00020\t\n\u00020\u000B\u00A8\u0006\u000C"}, d2 = {"Lcom/webdavrenamer/data/RepositoryModule;", "", "<init>", "()V", "provideServerRepository", "Lcom/webdavrenamer/data/repository/ServerRepository;", "dao", "Lcom/webdavrenamer/data/db/ServerConfigDao;", "crypto", "Lcom/webdavrenamer/data/crypto/KeystoreCrypto;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.Module()
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class RepositoryModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.data.RepositoryModule INSTANCE = null;

    private RepositoryModule() {
        super();
    }

    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.repository.ServerRepository provideServerRepository(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigDao dao, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.crypto.KeystoreCrypto crypto, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings) {
        return null;
    }
}
