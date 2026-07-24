package xa.refile.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import xa.refile.ui.browser.BrowserScreen
import xa.refile.ui.history.HistoryScreen
import xa.refile.ui.match.EditMatchScreen
import xa.refile.ui.match.MatchScreen
import xa.refile.ui.match.MatchSessionViewModel
import xa.refile.ui.preview.PreviewScreen
import xa.refile.ui.servers.ServerEditScreen
import xa.refile.ui.servers.ServerListScreen
import xa.refile.ui.settings.BackupScreen
import xa.refile.ui.settings.HostsSettingsScreen
import xa.refile.ui.settings.SettingsScreen
import xa.refile.ui.settings.TemplateEditorScreen

/**
 * 应用导航图（计划 §M1 SubTask 1.4 导航接入 / §M2 Task 2.4 匹配流程接入 / §M3 Task 3.4 预览接入）。
 *
 * 起始目的地为服务器列表。编辑路由用可选 query 参数 `id`（<=0 或缺省表示新增）。
 * 浏览器路由接 [xa.refile.ui.browser.BrowserScreen]；匹配路由接 [MatchScreen]；
 * 预览路由接 [PreviewScreen]；进度路由为占位（Task 4.3 实现完整页面后替换）。
 *
 * selectedPaths 传递：浏览器 `onProceedToMatch` 把选中视频完整路径写入 Activity 作用域的
 * [MatchSessionViewModel]，匹配页读取后转交 [xa.refile.ui.match.MatchViewModel]。
 * 避免把含特殊字符的 List<String> 编码进导航参数。
 *
 * matches 传递（Task 3.4）：匹配页 `onProceedToPreview` 前把已匹配结果写入会话 VM 的
 * `matches`，预览页读取后渲染目标路径，同样避免把复杂对象编码进导航参数。
 */
object Routes {
    const val SERVERS = "servers"

    const val SERVER_EDIT_ROUTE = "servers/edit?id={id}"
    private const val SERVER_EDIT_BASE = "servers/edit"

    const val BROWSER_ROUTE = "browser/{serverId}"
    private const val BROWSER_BASE = "browser"

    /** 匹配页路由（Task 2.4）。 */
    const val MATCH = "match/{serverId}"
    private const val MATCH_BASE = "match"

    /** Edit Match 路由（Task 2.5）：`edit_match/{matchIndex}`，索引指向会话 VM 的 matchedFiles。 */
    const val EDIT_MATCH = "edit_match/{matchIndex}"
    private const val EDIT_MATCH_BASE = "edit_match"

    /** 预览页路由（Task 3.4 接入）。 */
    const val PREVIEW = "preview/{serverId}"
    private const val PREVIEW_BASE = "preview"

    /** 执行进度页路由（Task 4.3 实现完整页面，此处先占位接通预览页跳转）。 */
    const val PROGRESS = "progress/{workId}"
    private const val PROGRESS_BASE = "progress"

    /** 模板编辑器路由（Task 3.3）。从设置页跳转。 */
    const val TEMPLATE_EDITOR = "template_editor"

    /** Hosts 设置路由（Task 5.3.4/5.3.5）。从设置页跳转。 */
    const val HOSTS_SETTINGS = "hosts_settings"

    /** 历史记录路由（Task 5.1.3）。单页承载列表 + 详情展开/折叠。 */
    const val HISTORY = "history"

    /** 备份与恢复路由（Task 5.2）。从服务器列表页跳转。 */
    const val BACKUP = "backup"

    /** 设置中心路由（Task 5.4）。作为所有子设置功能的统一入口，从服务器列表页齿轮图标跳转。 */
    const val SETTINGS = "settings"

    /** 编辑页跳转串。id 为 null 或 <=0 表示新增。 */
    fun serverEdit(id: Long?): String =
        "$SERVER_EDIT_BASE?id=${id ?: 0L}"

    /** 文件浏览器跳转串。 */
    fun browser(serverId: Long): String = "$BROWSER_BASE/$serverId"

    /** 匹配页跳转串。 */
    fun match(serverId: Long): String = "$MATCH_BASE/$serverId"

    /** Edit Match 跳转串（Task 2.5）。 */
    fun editMatch(matchIndex: Int): String = "$EDIT_MATCH_BASE/$matchIndex"

    /** 预览页跳转串（Task 3.4 接入）。 */
    fun preview(serverId: Long): String = "$PREVIEW_BASE/$serverId"

    /** 执行进度页跳转串（Task 3.4 预览页入队后跳转；workId 为 WorkManager UUID 字符串）。 */
    fun progress(workId: String): String = "$PROGRESS_BASE/$workId"

    /** 模板编辑器跳转串。 */
    fun templateEditor(): String = TEMPLATE_EDITOR

    /** Hosts 设置页跳转串。 */
    fun hostsSettings(): String = HOSTS_SETTINGS

    /** 历史记录页跳转串。 */
    fun history(): String = HISTORY

    /** 备份与恢复页跳转串。 */
    fun backup(): String = BACKUP

    /** 设置中心页跳转串。 */
    fun settings(): String = SETTINGS
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    // Activity 作用域：servers → browser → match → preview 整个回退栈生命周期内共享。
    val sessionVm: MatchSessionViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.SERVERS) {

        composable(Routes.SERVERS) {
            ServerListScreen(
                onAddServer = { navController.navigate(Routes.serverEdit(null)) },
                onEditServer = { id -> navController.navigate(Routes.serverEdit(id)) },
                onOpenBrowser = { id -> navController.navigate(Routes.browser(id)) },
                onOpenSettings = { navController.navigate(Routes.settings()) },
            )
        }

        composable(
            route = Routes.SERVER_EDIT_ROUTE,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0L
                },
            ),
        ) { backStackEntry ->
            val idArg = backStackEntry.arguments?.getLong("id") ?: 0L
            val serverId = if (idArg > 0L) idArg else null
            ServerEditScreen(
                serverId = serverId,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.BROWSER_ROUTE,
            arguments = listOf(
                navArgument("serverId") { type = NavType.LongType },
            ),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("serverId") ?: 0L
            BrowserScreen(
                serverId = id,
                onBack = { navController.popBackStack() },
                onProceedToMatch = { sid, selectedPaths ->
                    sessionVm.setFiles(selectedPaths)
                    navController.navigate(Routes.match(sid))
                },
            )
        }

        composable(
            route = Routes.MATCH,
            arguments = listOf(
                navArgument("serverId") { type = NavType.LongType },
            ),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("serverId") ?: 0L
            val selectedPaths by sessionVm.selectedPaths.collectAsStateWithLifecycle()
            MatchScreen(
                serverId = id,
                selectedPaths = selectedPaths,
                matchSessionVm = sessionVm,
                onBack = { navController.popBackStack() },
                onProceedToPreview = { sid -> navController.navigate(Routes.preview(sid)) },
                onEditMatch = { index -> navController.navigate(Routes.editMatch(index)) },
            )
        }

        composable(
            route = Routes.EDIT_MATCH,
            arguments = listOf(
                navArgument("matchIndex") { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("matchIndex") ?: 0
            EditMatchScreen(
                matchIndex = index,
                matchSessionVm = sessionVm,
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.PREVIEW,
            arguments = listOf(
                navArgument("serverId") { type = NavType.LongType },
            ),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("serverId") ?: 0L
            // 从会话 VM 读取匹配页写入的已匹配结果，供预览页渲染目标路径
            val matches by sessionVm.matches.collectAsStateWithLifecycle()
            PreviewScreen(
                serverId = id,
                matches = matches,
                onBack = { navController.popBackStack() },
                onProceedToProgress = { workId -> navController.navigate(Routes.progress(workId)) },
            )
        }

        composable(
            route = Routes.PROGRESS,
            arguments = listOf(
                navArgument("workId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val workId = backStackEntry.arguments?.getString("workId").orEmpty()
            ProgressPlaceholder(
                workId = workId,
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.TEMPLATE_EDITOR) {
            TemplateEditorScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.HOSTS_SETTINGS) {
            HostsSettingsScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.BACKUP) {
            BackupScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onOpenTemplateEditor = { navController.navigate(Routes.templateEditor()) },
                onOpenBackup = { navController.navigate(Routes.backup()) },
                onOpenHostsSettings = { navController.navigate(Routes.hostsSettings()) },
                onOpenHistory = { navController.navigate(Routes.history()) },
            )
        }
    }
}

/**
 * 进度页占位（Task 4.3 实现完整进度/结果页后替换）。
 *
 * Task 3.4 预览页入队后导航到此，先以最小占位接通流程，避免阻塞预览页落地。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressPlaceholder(workId: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                title = { Text("执行进度") },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "任务已入队（workId=$workId）\n进度页待实现（Task 4.3）",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
