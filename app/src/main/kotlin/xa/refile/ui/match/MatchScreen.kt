package xa.refile.ui.match

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import xa.refile.core.matcher.MatchCandidate
import xa.refile.ui.common.EmptyState
import xa.refile.ui.common.FanartHeader
import xa.refile.ui.match.MatchViewModel.MatchStatus
import xa.refile.ui.match.MatchViewModel.MatchType
import xa.refile.ui.match.MatchViewModel.Progress
import xa.refile.ui.theme.PosterPlaceholder

/**
 * TMDB 匹配页（计划 §M2 Task 2.4）。
 *
 * 三阶段：
 * 1. [Progress.Idle]：匹配方式选择（自动/强制电影/强制剧集）+ 文件计数 + 开始按钮。
 * 2. [Progress.Running]：进度条 + 海报墙（已匹配项亮起海报）。
 * 3. [Progress.Done]：已匹配列表 + 待确认列表（候选展开/手动搜索/确认）+ 下一步按钮。
 *
 * [selectedPaths] 由导航层从 [MatchSessionViewModel] 传入，落地到 [MatchViewModel.setFiles]。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(
    serverId: Long,
    selectedPaths: List<String>,
    matchSessionVm: MatchSessionViewModel,
    onBack: () -> Unit,
    onProceedToPreview: (serverId: Long) -> Unit,
    onEditMatch: (Int) -> Unit,
    viewModel: MatchViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(selectedPaths) { viewModel.setFiles(selectedPaths) }

    // Task 2.5：EditMatch 回写后脏标记触发，把编辑结果拉回 MatchViewModel。
    val dirty by matchSessionVm.dirty.collectAsStateWithLifecycle()
    LaunchedEffect(dirty) {
        if (dirty) {
            viewModel.applyEditedResults(matchSessionVm.matchedFiles.value)
            matchSessionVm.clearDirty()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error) {
        val err = state.error
        if (!err.isNullOrBlank()) {
            snackbarHostState.showSnackbar(err)
            viewModel.clearError()
        }
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
                    Text(
                        text = "匹配",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (state.progress is Progress.Done) {
                NextStepBar(
                    enabled = state.allResolved && state.results.isNotEmpty(),
                    onProceed = {
                        // Task 3.4：跳转预览页前把已匹配结果写入会话 VM，供预览页渲染目标路径。
                        matchSessionVm.setMatches(state.results)
                        onProceedToPreview(serverId)
                    },
                )
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            // Task 5.5：阶段切换 slideInHorizontally 动画（从右侧滑入）。
            // 用阶段枚举作为 key，避免 Running(current,total) 每次进度上报触发重绘。
            val stageKey = when (state.progress) {
                Progress.Idle -> 0
                is Progress.Running -> 1
                Progress.Done -> 2
            }
            AnimatedContent(
                targetState = stageKey,
                transitionSpec = {
                    (slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn(tween(300))) togetherWith
                        (slideOutHorizontally(animationSpec = tween(300)) { -it } + fadeOut(tween(300)))
                },
                label = "matchStage",
            ) { stage ->
                when (stage) {
                    0 -> MatchTypeSelectionStage(
                        fileCount = state.selectedFiles.size,
                        selectedType = state.matchType,
                        onSelectType = viewModel::setMatchType,
                        onStart = { viewModel.startMatch(state.matchType) },
                    )
                    1 -> {
                        val p = state.progress
                        if (p is Progress.Running) {
                            MatchRunningStage(
                                current = p.current,
                                total = p.total,
                                selectedFiles = state.selectedFiles,
                                results = state.results,
                            )
                        }
                    }
                    2 -> MatchResultsStage(
                        state = state,
                        onConfirm = viewModel::confirmMatch,
                        onManualSearch = viewModel::manualSearch,
                        onEditMatch = { index ->
                            // 跳转前把当前已匹配结果快照写入会话 VM（不置脏）
                            matchSessionVm.setMatchedFiles(state.results, markDirty = false)
                            onEditMatch(index)
                        },
                    )
                }
            }
        }
    }
}

// ---- 阶段 1：匹配方式选择 ----

@Composable
private fun MatchTypeSelectionStage(
    fileCount: Int,
    selectedType: MatchType,
    onSelectType: (MatchType) -> Unit,
    onStart: () -> Unit,
) {
    // Task 5.5：未选择文件时友好空状态。
    if (fileCount == 0) {
        EmptyState(
            icon = Icons.Default.Movie,
            title = "未选择文件",
            subtitle = "请先在浏览器选择视频文件",
        )
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "已选 $fileCount 个文件",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "匹配方式",
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(Modifier.height(8.dp))
        MatchTypeOption(
            type = MatchType.AUTO,
            title = "自动识别",
            subtitle = "按文件名中的季/集信息自动判定电影或剧集",
            selected = selectedType == MatchType.AUTO,
            icon = Icons.Default.Movie,
            onSelect = onSelectType,
        )
        Spacer(Modifier.height(8.dp))
        MatchTypeOption(
            type = MatchType.MOVIE,
            title = "强制电影",
            subtitle = "全部当作电影匹配",
            selected = selectedType == MatchType.MOVIE,
            icon = Icons.Default.Movie,
            onSelect = onSelectType,
        )
        Spacer(Modifier.height(8.dp))
        MatchTypeOption(
            type = MatchType.TV,
            title = "强制剧集",
            subtitle = "全部当作剧集匹配",
            selected = selectedType == MatchType.TV,
            icon = Icons.Default.Tv,
            onSelect = onSelectType,
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(),
            enabled = fileCount > 0,
        ) { Text("开始匹配") }
    }
}

@Composable
private fun MatchTypeOption(
    type: MatchType,
    title: String,
    subtitle: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onSelect: (MatchType) -> Unit,
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(type) },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            borderColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (selected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// ---- 阶段 2：匹配进行中 ----

@Composable
private fun MatchRunningStage(
    current: Int,
    total: Int,
    selectedFiles: List<String>,
    results: List<MatchViewModel.FileMatch>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("匹配中 $current/$total", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { if (total > 0) current.toFloat() / total.toFloat() else 0f },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        PosterWall(selectedFiles = selectedFiles, results = results)
    }
}

/** 海报墙：每个文件一格，已匹配亮起海报，未匹配暗色占位。 */
@Composable
private fun PosterWall(
    selectedFiles: List<String>,
    results: List<MatchViewModel.FileMatch>,
) {
    val matchedByPath = results.associateBy { it.filePath }
    val columns = 3
    Column(Modifier.fillMaxWidth()) {
        selectedFiles.chunked(columns).forEach { rowFiles ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rowFiles.forEach { path ->
                    val fm = matchedByPath[path]
                    val posterUrl = fm?.candidates?.firstOrNull()?.posterUrl
                    PosterCell(
                        modifier = Modifier.weight(1f),
                        posterUrl = if (fm != null) posterUrl else null,
                        matched = fm != null,
                        fileName = path.substringAfterLast('/'),
                    )
                }
                repeat(columns - rowFiles.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PosterCell(
    modifier: Modifier = Modifier,
    posterUrl: String?,
    matched: Boolean,
    fileName: String,
) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (matched) Color.Transparent else PosterPlaceholder),
            contentAlignment = Alignment.Center,
        ) {
            val url = posterUrl
            when {
                !url.isNullOrBlank() -> SubcomposeAsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(PosterPlaceholder),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(PosterPlaceholder),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Default.Movie,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                )
                matched -> Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                else -> CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = fileName,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (matched) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---- 阶段 3：结果 + 待确认 ----

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MatchResultsStage(
    state: MatchViewModel.UiState,
    onConfirm: (filePath: String, candidate: MatchCandidate) -> Unit,
    onManualSearch: (filePath: String, query: String, type: MatchType) -> Unit,
    onEditMatch: (Int) -> Unit,
) {
    // Task 5.5：fanart 渐变遮罩背景取首个已匹配项的海报作为最佳可用图。
    val fanartBackdrop = state.results.firstOrNull()?.candidates?.firstOrNull()?.posterUrl
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            // Task 5.5：fanart 渐变遮罩头部，叠加「已匹配」分区标题。
            FanartHeader(
                backdropUrl = fanartBackdrop,
                height = 140.dp,
            ) {
                Text(
                    text = "已匹配 ✅ (${state.results.size})",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                )
            }
        }
        itemsIndexed(state.results, key = { _, fm -> fm.filePath }) { index, fm ->
            MatchedRow(
                fm = fm,
                onEdit = { onEditMatch(index) },
                modifier = Modifier.animateItemPlacement(),
            )
            HorizontalDivider()
        }
        if (state.pending.isNotEmpty()) {
            item {
                Spacer(Modifier.height(8.dp))
                SectionHeader("待确认 ⚠️", state.pending.size)
            }
            items(state.pending, key = { it.filePath }) { fm ->
                PendingRow(
                    fm = fm,
                    searching = state.manualSearchingPath == fm.filePath,
                    onConfirm = onConfirm,
                    onManualSearch = onManualSearch,
                    modifier = Modifier.animateItemPlacement(),
                )
                HorizontalDivider()
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun SectionHeader(title: String, count: Int) {
    Text(
        text = "$title ($count)",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun MatchedRow(
    fm: MatchViewModel.FileMatch,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = when (fm.status) {
                MatchStatus.CONFIRMED -> Icons.Default.Check
                MatchStatus.AUTO -> Icons.Default.Check
                else -> Icons.Default.HelpOutline
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            // Task 5.5：文件名按「路径」层级用 bodySmall monospace 辅助文字。
            Text(
                text = fm.filePath.substringAfterLast('/'),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            val matched = fm.matched
            val title = matched?.name ?: "（未匹配）"
            val year = matched?.year
            val label = if (year != null) "$title ($year)" else title
            // Task 5.5：匹配标题用 titleMedium（行内卡片标题层级）。
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // 多集组合标签（Task 2.5.2）或手动编辑标记
            val tag = fm.multiEpisodeRange
            if (tag != null) {
                Text(
                    text = tag + if (fm.manuallyEdited) " · 已手动修正" else "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            } else if (fm.manuallyEdited) {
                Text(
                    text = "已手动修正",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        // 编辑入口（Task 2.5.1）
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "编辑匹配")
        }
    }
}

@Composable
private fun PendingRow(
    fm: MatchViewModel.FileMatch,
    searching: Boolean,
    onConfirm: (filePath: String, candidate: MatchCandidate) -> Unit,
    onManualSearch: (filePath: String, query: String, type: MatchType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val defaultSearchType = if (fm.parsed.season != null || fm.parsed.episodes.isNotEmpty()) {
        MatchType.TV
    } else {
        MatchType.MOVIE
    }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = when (fm.status) {
                    MatchStatus.NO_MATCH -> Icons.Default.HelpOutline
                    else -> Icons.Default.WarningAmber
                },
                contentDescription = null,
                tint = if (fm.status == MatchStatus.NO_MATCH) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.error
                },
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                // Task 5.5：文件名按「路径」层级用 bodySmall monospace。
                Text(
                    text = fm.filePath.substringAfterLast('/'),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val hint = when (fm.status) {
                    MatchStatus.NO_MATCH -> fm.error ?: "无匹配，点此手动搜索"
                    else -> "${fm.candidates.size} 个候选，点此选择"
                }
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Task 5.5：展开面板从右侧滑入。
        AnimatedVisibility(
            visible = expanded,
            enter = slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn(tween(300)),
            exit = slideOutHorizontally(animationSpec = tween(300)) { it } + fadeOut(tween(300)),
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("手动搜索关键词") },
                        singleLine = true,
                    )
                    Spacer(Modifier.width(8.dp))
                    if (searching) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        IconButton(onClick = { onManualSearch(fm.filePath, query, defaultSearchType) }) {
                            Icon(Icons.Default.Search, contentDescription = "搜索")
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                if (fm.candidates.isEmpty()) {
                    Text(
                        text = "暂无候选",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    // Task 5.5：候选列表改为 2 列海报网格。
                    CandidatePosterGrid(
                        candidates = fm.candidates,
                        onConfirm = { c -> onConfirm(fm.filePath, c.candidate) },
                    )
                }
            }
        }
    }
}

/**
 * Task 5.5：候选海报墙网格（2 列）。候选数通常较少，使用 chunked 行布局以避免
 * 在外层 LazyColumn 内嵌套 LazyVerticalGrid 的高度约束问题，视觉效果与网格一致。
 */
@Composable
private fun CandidatePosterGrid(
    candidates: List<MatchViewModel.Candidate>,
    onConfirm: (MatchViewModel.Candidate) -> Unit,
) {
    val columns = 2
    candidates.chunked(columns).forEach { rowCandidates ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            rowCandidates.forEach { c ->
                CandidatePosterCard(
                    candidate = c,
                    onClick = { onConfirm(c) },
                    modifier = Modifier.weight(1f),
                )
            }
            repeat(columns - rowCandidates.size) {
                Spacer(Modifier.weight(1f))
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

/** Task 5.5：单张候选海报卡片：海报（2:3 圆角）+ 标题（1 行省略）+ 年份小字。 */
@Composable
private fun CandidatePosterCard(
    candidate: MatchViewModel.Candidate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(10.dp))
                .background(PosterPlaceholder),
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                model = candidate.posterUrl,
                contentDescription = candidate.candidate.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PosterPlaceholder),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PosterPlaceholder),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Default.Movie,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                },
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = candidate.candidate.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        candidate.candidate.year?.let { year ->
            Text(
                text = year.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun NextStepBar(
    enabled: Boolean,
    onProceed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        FilledTonalButton(onClick = onProceed, enabled = enabled) {
            Text("下一步：预览重命名")
            Spacer(Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}
