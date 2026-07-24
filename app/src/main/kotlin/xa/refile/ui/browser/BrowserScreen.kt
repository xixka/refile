package xa.refile.ui.browser

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import xa.refile.core.webdav.MediaFileTypes
import xa.refile.core.webdav.WebDavEntry
import xa.refile.ui.common.EmptyState

/** 目录图标用琥珀色，对齐 MiXplorer 视觉。 */
private val AmberColor = Color(0xFFFFC107)

/**
 * WebDAV 文件浏览器（计划 §M1 SubTask 1.5）。
 *
 * - 顶部 TopAppBar：返回 + 可点击面包屑 + 刷新/排序菜单。
 * - 列表：每行图标（目录/视频/字幕/其它）+ 名称 + 大小 + 修改日期；iso 仅显示并置灰。
 * - 选择规则：所有类型都显示；仅 [MediaFileTypes.isSelectableVideo] 显示复选框且可勾选；
 *   非视频（目录/字幕/nfo/图片/iso）置灰、无复选框，目录点击进入。
 * - 多选：长按视频进入，底栏显示计数 + 全选/反选 + 「下一步：匹配」。
 * - 下拉刷新（material3 [PullToRefreshBox]）；空目录居中提示；加载中转圈。
 * - 系统返回键：多选先退出，否则逐级回退，根目录回退到上一屏。
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BrowserScreen(
    serverId: Long,
    onBack: () -> Unit,
    onProceedToMatch: (serverId: Long, selectedPaths: List<String>) -> Unit,
    viewModel: BrowserViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showSortMenu by remember { mutableStateOf(false) }

    LaunchedEffect(serverId) { viewModel.init(serverId) }

    BackHandler {
        if (state.multiSelectMode) viewModel.exitMultiSelect()
        else if (!viewModel.goUp()) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                title = {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        breadcrumbs(state.currentPath, state.rootPath).forEachIndexed { index, (label, path) ->
                            if (index > 0) {
                                Text(
                                    text = " / ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            TextButton(
                                onClick = { viewModel.navigateToBreadcrumb(path) },
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "刷新")
                    }
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "排序")
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                    ) {
                        val sf = state.sortField
                        DropdownMenuItem(
                            text = { Text("按名称${if (sf == BrowserViewModel.SortField.NAME) " ✓" else ""}") },
                            onClick = {
                                viewModel.toggleSort(BrowserViewModel.SortField.NAME)
                                showSortMenu = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("按大小${if (sf == BrowserViewModel.SortField.SIZE) " ✓" else ""}") },
                            onClick = {
                                viewModel.toggleSort(BrowserViewModel.SortField.SIZE)
                                showSortMenu = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("按时间${if (sf == BrowserViewModel.SortField.TIME) " ✓" else ""}") },
                            onClick = {
                                viewModel.toggleSort(BrowserViewModel.SortField.TIME)
                                showSortMenu = false
                            },
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text(if (state.sortAsc) "切换为降序" else "切换为升序") },
                            onClick = {
                                viewModel.toggleSortOrder()
                                showSortMenu = false
                            },
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (state.multiSelectMode) {
                MultiSelectBottomBar(
                    selectedCount = state.selectedPaths.size,
                    onSelectAll = { viewModel.selectAll() },
                    onInvert = { viewModel.invertSelection() },
                    onExit = { viewModel.exitMultiSelect() },
                    onProceed = { onProceedToMatch(serverId, viewModel.selectedVideoFiles()) },
                )
            }
        },
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.loading && state.entries.isNotEmpty(),
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                state.loading && state.entries.isEmpty() && state.error == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null && state.entries.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.error!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = { viewModel.refresh() }) { Text("重试") }
                        }
                    }
                }
                state.entries.isEmpty() -> {
                    // Task 5.5：空目录友好空状态。
                    EmptyState(
                        icon = Icons.Default.Folder,
                        title = "空文件夹",
                        subtitle = "此目录没有文件",
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.entries, key = { it.href }) { entry ->
                            val name = entry.displayName ?: nameFromHref(entry.href)
                            val fullPath = joinPath(state.currentPath, name)
                            BrowserEntryRow(
                                modifier = Modifier.animateItemPlacement(),
                                entry = entry,
                                name = name,
                                multiSelectMode = state.multiSelectMode,
                                isSelected = fullPath in state.selectedPaths,
                                onClick = {
                                    if (entry.isCollection) {
                                        viewModel.navigateInto(entry)
                                    } else if (state.multiSelectMode &&
                                        MediaFileTypes.isSelectableVideo(name)
                                    ) {
                                        viewModel.toggleSelected(fullPath)
                                    }
                                },
                                onLongClick = {
                                    if (!state.multiSelectMode &&
                                        MediaFileTypes.isSelectableVideo(name)
                                    ) {
                                        viewModel.enterMultiSelect(fullPath)
                                    }
                                },
                                onToggle = { viewModel.toggleSelected(fullPath) },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

/** 多选模式底部栏：退出 + 已选计数 + 全选/反选 + 下一步（仅当选中>0）。 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MultiSelectBottomBar(
    selectedCount: Int,
    onSelectAll: () -> Unit,
    onInvert: () -> Unit,
    onExit: () -> Unit,
    onProceed: () -> Unit,
) {
    BottomAppBar {
        IconButton(onClick = onExit) {
            Icon(Icons.Default.Close, contentDescription = "退出多选")
        }
        Text(
            text = "已选 $selectedCount",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.weight(1f))
        TextButton(onClick = onSelectAll) { Text("全选") }
        TextButton(onClick = onInvert) { Text("反选") }
        if (selectedCount > 0) {
            Spacer(Modifier.width(8.dp))
            Button(onClick = onProceed) {
                Text("下一步：匹配")
                Spacer(Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}

/** 单条浏览器项。 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BrowserEntryRow(
    entry: WebDavEntry,
    name: String,
    multiSelectMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSelectableVideo = MediaFileTypes.isSelectableVideo(name)
    val isVideo = MediaFileTypes.isVideo(name)
    val isSubtitle = MediaFileTypes.isSubtitle(name)
    val isDisplayOnly = MediaFileTypes.isDisplayOnly(name)

    val icon = when {
        entry.isCollection -> Icons.Default.Folder
        isVideo -> Icons.Default.Movie
        isSubtitle -> Icons.Default.Subtitles
        else -> Icons.Default.InsertDriveFile
    }
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val iconTint = when {
        entry.isCollection && !multiSelectMode -> AmberColor
        multiSelectMode && !isSelectableVideo -> onSurfaceVariant
        isDisplayOnly -> onSurfaceVariant
        else -> onSurface
    }
    val nameColor = when {
        multiSelectMode && !isSelectableVideo -> onSurfaceVariant
        isDisplayOnly -> onSurfaceVariant
        else -> onSurface
    }
    // Task 5.5：多选选中行加 primaryContainer 半透明高亮（Infuse 风格卡片态）。
    val rowBackground = if (isSelected && multiSelectMode) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(rowBackground)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (multiSelectMode && isSelectableVideo) {
            Checkbox(checked = isSelected, onCheckedChange = { onToggle() })
        } else if (multiSelectMode) {
            // 占位对齐：非可选行无复选框，留出等宽空白
            Spacer(Modifier.width(48.dp))
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(28.dp),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = nameColor,
                fontWeight = if (isSelectableVideo) FontWeight.Medium else FontWeight.Normal,
                // 测试反馈 Item 6：文件名太长时换行完整显示，不截断
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = formatSize(entry.contentLength),
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceVariant,
                )
                entry.lastModified?.let { lm ->
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = formatDate(lm),
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/** 人性化字节大小，如 `1.2 GB`。null 或目录返回 `—`。 */
private fun formatSize(bytes: Long?): String {
    if (bytes == null || bytes < 0) return "—"
    if (bytes < 1024L) return "$bytes B"
    val units = arrayOf("KB", "MB", "GB", "TB")
    var value = bytes.toDouble() / 1024.0
    var idx = 0
    while (value >= 1024.0 && idx < units.size - 1) {
        value /= 1024.0
        idx++
    }
    return "%.1f %s".format(value, units[idx])
}

/** 截取 RFC1123 修改时间的日期部分（`dd MMM yyyy`）；格式不符时回退到首段。 */
private fun formatDate(raw: String?): String {
    if (raw.isNullOrBlank()) return "—"
    val parts = raw.split(" ").filter { it.isNotBlank() }
    return if (parts.size >= 4 && parts[0].endsWith(",")) {
        "${parts[1]} ${parts[2]} ${parts[3]}"
    } else {
        raw.substringBefore(" ")
    }
}
