package com.webdavrenamer.data;

@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0008\u00C7\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\u000E\u0010\u00042\u0006\u0008\u0001\u0010\u0006(\u00028\u0001H\u0007\u00F2\u0001\u000C\n\u00020\u0001\n\u00020\u0005\n\u00020\u0007\u00A8\u0006\u0008"}, d2 = {"Lcom/webdavrenamer/data/CryptoModule;", "", "<init>", "()V", "provideKeystoreCrypto", "Lcom/webdavrenamer/data/crypto/KeystoreCrypto;", "context", "Landroid/content/Context;", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.Module()
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class CryptoModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.data.CryptoModule INSTANCE = null;

    private CryptoModule() {
        super();
    }

    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.crypto.KeystoreCrypto provideKeystoreCrypto(@dagger.hilt.android.qualifiers.ApplicationContext() @org.jetbrains.annotations.NotNull() android.content.Context context) {
        return null;
    }
}
