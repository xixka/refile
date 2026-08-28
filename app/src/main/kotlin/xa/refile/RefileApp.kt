package xa.refile

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import xa.refile.core.tmdb.TmdbImages
import xa.refile.data.prefs.SettingsRepository
import javax.inject.Inject

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
 *
 * TMDB 反代同步：[onCreate] 启动一个应用级协程观察 [SettingsRepository.tmdbProxyUrl]，
 * 把最新值写入 [TmdbImages.proxyUrl]，使图片请求跟随反代设置（API 请求由
 * [xa.refile.data.repository.TmdbClientProvider] 构造 client 时读取同源设置）。
 */
@HiltAndroidApp
class RefileApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var settings: SettingsRepository

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // 应用级协程：随进程存活，不随任何 Activity/ViewModel 销毁。
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        settings.tmdbProxyUrl
            .distinctUntilChanged()
            .onEach { TmdbImages.proxyUrl = it }
            .launchIn(appScope)

        // 用户体验修复（「第一次重命名卡在正在准备…」）：本应用实现 Configuration.Provider，
        // WorkManager 按需初始化——首次 WorkManager.getInstance() 需创建其内部 Room 数据库
        // 与调度线程池（数百毫秒），此前发生在用户第一次点「执行」时，表现为进度页长时间
        // 「正在准备…」。启动时在后台线程提前触发初始化，首次重命名即可立即开始执行。
        appScope.launch(Dispatchers.IO) {
            runCatching { WorkManager.getInstance(this@RefileApp) }
        }
    }
}
