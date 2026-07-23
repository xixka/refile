package com.webdavrenamer.data;

@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\u0008\u00C7\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\u0006\u0010\u00048\u0001H\u0007\u00F2\u0001\u0008\n\u00020\u0001\n\u00020\u0005\u00A8\u0006\u0006"}, d2 = {"Lcom/webdavrenamer/data/HostsModule;", "", "<init>", "()V", "provideHostsSpeedTest", "Lcom/webdavrenamer/core/backup/HostsSpeedTest;", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.Module()
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class HostsModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.data.HostsModule INSTANCE = null;

    private HostsModule() {
        super();
    }

    /**
     * Hosts 自动测速（Task 5.3.3）。无状态工具类，单例即可；内部 OkHttpClient 也单例复用。
     */
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.backup.HostsSpeedTest provideHostsSpeedTest() {
        return null;
    }
}
