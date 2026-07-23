package com.webdavrenamer.ui.preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.webdavrenamer.core.rename.CompanionRename
import com.webdavrenamer.ui.match.MatchViewModel
import com.webdavrenamer.ui.theme.AccentAmber
import com.webdavrenamer.ui.theme.CinemaBlack
import com.webdavrenamer.ui.theme.ErrorRed
import com.webdavrenamer.ui.theme.SuccessGreen
import com.webdavrenamer.ui.theme.WarningAmber

/**
 * 重命名预览页（计划 §M3 Task 3.4，只预览不执行）。
 *
 * 顶部 [TopAppBar] 返回；统计芯片行展示 自动✅/待确认⚠️/冲突❌/已排除 计数；
 * 存在冲突时显示「一键解决冲突」按钮；[LazyColumn] 逐行展示原路径（小字灰色）→ 新路径
 * （大字主题色，冲突标红）+ 状态图标 + 伴随文件可折叠；左滑排除单条，点击单条弹窗手动修改；
 * 底部 [BottomAppBar] 执行按钮把操作入队 WorkManager 后导航到进度页。
 *
 * [matches] 由导航层从 Activity 作用域的 [com.webdavrenamer.ui.match.MatchSessionViewModel.matches]
 * 传入，落地到 [PreviewViewModel.load]。
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PreviewScreen(
    serverId: Long,
    matches: List<MatchViewModel.FileMatch>,
    onBack: () -> Unit,
    onProceedToProgress: (workId: String) -> Unit,
    viewModel: PreviewViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // matches 非空时触发一次加载（VM 内 initialized 守卫避免重复）
    LaunchedEffect(matches) {
        if (matches.isNotEmpty()) viewModel.load(serverId, matches)
    }

    var editingItem by remember { mutableStateOf<PreviewViewModel.PreviewItem?>(null) }
    var showExcludedDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        val err = state.error
        if (!err.isNullOrBlank()) {
            snackbarHostState.showSnackbar(err)
            viewModel.clearError()
        }
    }

    val executableCount = state.activeItems.count { it.status != PreviewViewModel.PreviewStatus.CONFLICT }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                title = {
                    Text(
                        text = "预览重命名",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "可执行 $executableCount 项",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(
                        onClick = {
                            val id = viewModel.enqueueRename()
                            if (id != null) onProceedToProgress(id)
                        },
                        enabled = state.conflictCount == 0 && executableCount > 0,
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("执行重命名")
                    }
                }
            }
        },
    ) { padding ->
        // Task 5.5：加载 → 内容用 crossfade(300)。
        Crossfade(
            targetState = state.loading,
            animationSpec = tween(300),
            label = "previewLoad",
        ) { loading ->
            when {
                loading -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }

                state.previewItems.isEmpty() -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "无可预览的匹配项",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                ) {
                    StatsRow(
                        autoCount = state.autoCount,
                        needsConfirmCount = state.needsConfirmCount,
                        conflictCount = state.conflictCount,
                        excludedCount = state.excludedCount,
                        onShowExcluded = { if (state.excludedCount > 0) showExcludedDialog = true },
                    )
                    if (state.conflictCount > 0) {
                        Button(
                            onClick = viewModel::autoResolveConflicts,
                            enabled = !state.detecting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                        ) {
                            Icon(Icons.Filled.AutoFixHigh, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("一键解决冲突（${state.conflictCount}）")
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    ) {
                        items(state.activeItems, key = { it.sourcePath }) { item ->
                            SwipeToDismissItem(
                                item = item,
                                onExclude = { viewModel.excludeItem(item.sourcePath) },
                                onClick = { editingItem = item },
                                modifier = Modifier.animateItemPlacement(),
                            )
                        }
                    }
                }
            }
        }
    }

    editingItem?.let { item ->
        EditTargetDialog(
            item = item,
            onDismiss = { editingItem = null },
            onConfirm = { newTarget ->
                viewModel.editItemTarget(item.sourcePath, newTarget)
                editingItem = null
            },
        )
    }

    if (showExcludedDialog) {
        ExcludedDialog(
            excludedItems = state.previewItems.filter { it.sourcePath in state.excludedPaths },
            onDismiss = { showExcludedDialog = false },
            onRestore = { path ->
                viewModel.includeItem(path)
            },
        )
    }
}

/**
 * 统计芯片行：自动✅ / 待确认⚠️ / 冲突❌ / 已排除（点击查看并恢复）。
 */
@Composable
private fun StatsRow(
    autoCount: Int,
    needsConfirmCount: Int,
    conflictCount: Int,
    excludedCount: Int,
    onShowExcluded: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatChip(
            icon = Icons.Filled.Check,
            iconTint = SuccessGreen,
            label = "自动 $autoCount",
        )
        StatChip(
            icon = Icons.Filled.WarningAmber,
            iconTint = WarningAmber,
            label = "待确认 $needsConfirmCount",
        )
        StatChip(
            icon = Icons.Filled.Close,
            iconTint = ErrorRed,
            label = "冲突 $conflictCount",
        )
        if (excludedCount > 0) {
            AssistChip(
                onClick = onShowExcluded,
                label = { Text("已排除 $excludedCount") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Restore,
                        contentDescription = null,
                        modifier = Modifier,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                colors = AssistChipDefaults.assistChipColors(),
            )
        }
    }
}

@Composable
private fun StatChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    label: String,
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier)
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * 单条预览项容器：[SwipeToDismissBox] 左滑（EndToStart）排除，点击卡片打开编辑弹窗。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissItem(
    item: PreviewViewModel.PreviewItem,
    onExclude: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onExclude()
                true
            } else {
                false
            }
        },
    )
    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(ErrorRed)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "排除", tint = CinemaBlack)
            }
        },
        content = { PreviewCard(item = item, onClick = onClick) },
    )
}

/**
 * 预览卡片：状态图标 + 原路径（小字灰色）→ 新路径（大字主题色/冲突标红）+ 冲突原因 + 伴随文件可折叠 + 渲染警告。
 */
@Composable
private fun PreviewCard(
    item: PreviewViewModel.PreviewItem,
    onClick: () -> Unit,
) {
    val statusColor = when (item.status) {
        PreviewViewModel.PreviewStatus.AUTO -> SuccessGreen
        PreviewViewModel.PreviewStatus.NEEDS_CONFIRM -> WarningAmber
        PreviewViewModel.PreviewStatus.CONFLICT -> ErrorRed
    }
    val statusIcon = when (item.status) {
        PreviewViewModel.PreviewStatus.AUTO -> Icons.Filled.Check
        PreviewViewModel.PreviewStatus.NEEDS_CONFIRM -> Icons.Filled.WarningAmber
        PreviewViewModel.PreviewStatus.CONFLICT -> Icons.Filled.Close
    }
    val targetColor = if (item.status == PreviewViewModel.PreviewStatus.CONFLICT) ErrorRed else AccentAmber
    var companionsExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(statusIcon, contentDescription = null, tint = statusColor)
                Spacer(Modifier.width(8.dp))
                // Task 5.5：原路径按「路径」层级用 bodySmall monospace。
                Text(
                    text = item.sourcePath,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                if (item.companions.isNotEmpty()) {
                    IconButton(onClick = { companionsExpanded = !companionsExpanded }) {
                        Icon(
                            if (companionsExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (companionsExpanded) "收起伴随文件" else "展开伴随文件",
                        )
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            // Task 5.5：目标路径用 titleLarge（卡片标题层级）。
            Text(
                text = item.targetPath,
                style = MaterialTheme.typography.titleLarge,
                color = targetColor,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (item.conflictReason != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.conflictReason,
                    style = MaterialTheme.typography.bodySmall,
                    color = ErrorRed,
                )
            }
            if (item.warnings.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.warnings.joinToString("；"),
                    style = MaterialTheme.typography.bodySmall,
                    color = WarningAmber,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            AnimatedVisibility(visible = companionsExpanded && item.companions.isNotEmpty()) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "伴随文件（${item.companions.size}）",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    item.companions.forEach { CompanionRow(it) }
                }
            }
        }
    }
}

@Composable
private fun CompanionRow(companion: CompanionRename) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = companion.sourcePath,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "→ ${companion.targetPath}",
            style = MaterialTheme.typography.bodySmall,
            color = AccentAmber,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * 手动修改目标路径弹窗：预填当前 [item.targetPath]，确认后回写并重新触发冲突检测。
 */
@Composable
private fun EditTargetDialog(
    item: PreviewViewModel.PreviewItem,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var text by remember { mutableStateOf(item.targetPath) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("修改目标路径") },
        text = {
            Column {
                Text(
                    text = "原路径：${item.sourcePath}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("目标路径") },
                    singleLine = false,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) },
                enabled = text.isNotBlank() && text != item.targetPath,
            ) { Text("保存") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

/**
 * 已排除项弹窗：列出被左滑排除的条目，每条可单独恢复（调用 [PreviewViewModel.includeItem]）。
 */
@Composable
private fun ExcludedDialog(
    excludedItems: List<PreviewViewModel.PreviewItem>,
    onDismiss: () -> Unit,
    onRestore: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("已排除 ${excludedItems.size} 项") },
        text = {
            if (excludedItems.isEmpty()) {
                Text("无已排除项", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Column {
                    excludedItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = item.sourcePath,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f),
                            )
                            TextButton(onClick = { onRestore(item.sourcePath) }) {
                                Icon(Icons.Filled.Restore, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text("恢复")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("关闭") }
        },
    )
}
