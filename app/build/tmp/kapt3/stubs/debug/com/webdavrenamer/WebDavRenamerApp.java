package com.webdavrenamer;

/**
 * Application entry point.
 * 
 * - `@HiltAndroidApp` triggers Hilt's code generation and dependency container.
 * - Implements [Configuration.Provider] so WorkManager picks up the
 *   [HiltWorkerFactory] (workers can use `@HiltWorker` + `@AssistedInject`).
 * 
 * The default `WorkManagerInitializer` from `androidx.startup` is removed in the
 * manifest (see `AndroidManifest.xml`); when an app implements
 * `Configuration.Provider`, WorkManager defers initialization until first use
 * and consults this configuration. No explicit `WorkManager.initialize(...)`
 * call is needed in [onCreate].
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0000\u0008\u0007\u0012\u0002\u0000\u0001\u0018\u0000B\u0007\u00A2\u0006\u0004\u0008\u0003\u0010\u0004J\u0006\u0010\u000F8\u0004H\u0016R\u001C\u0010\u00058\u0006@\u0006H\u0002X\u0087.\u00A2\u0006\u000E\n\u0000\u001A\u0004\u0008\u0007\u0010\u0008\"\u0004\u0008\t\u0010\nR\u0012\u0010\u000B8VH\u0003X\u0096\u0004\u00A2\u0006\u0006\u001A\u0004\u0008\r\u0010\u000E\u00F2\u0001\u0014\n\u00020\u0001\n\u00020\u0002\n\u00020\u0006\n\u00020\u000C\n\u00020\u0010\u00A8\u0006\u0011"}, d2 = {"Lcom/webdavrenamer/WebDavRenamerApp;", "Landroid/app/Application;", "Landroidx/work/Configuration$Provider;", "<init>", "()V", "workerFactory", "Landroidx/hilt/work/HiltWorkerFactory;", "getWorkerFactory", "()Landroidx/hilt/work/HiltWorkerFactory;", "setWorkerFactory", "(Landroidx/hilt/work/HiltWorkerFactory;)V", "workManagerConfiguration", "Landroidx/work/Configuration;", "getWorkManagerConfiguration", "()Landroidx/work/Configuration;", "onCreate", "", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.HiltAndroidApp()
public final class WebDavRenamerApp extends android.app.Application implements androidx.work.Configuration.Provider {
    @javax.inject.Inject()
    public androidx.hilt.work.HiltWorkerFactory workerFactory;

    public WebDavRenamerApp() {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final androidx.hilt.work.HiltWorkerFactory getWorkerFactory() {
        return null;
    }

    public final void setWorkerFactory(@org.jetbrains.annotations.NotNull() androidx.hilt.work.HiltWorkerFactory p0) {
    }

    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.work.Configuration getWorkManagerConfiguration() {
        return null;
    }

    @java.lang.Override()
    public void onCreate() {
    }
}
