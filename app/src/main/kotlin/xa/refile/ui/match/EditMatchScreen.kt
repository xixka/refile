package xa.refile.ui.match

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import xa.refile.core.model.MediaType
import xa.refile.ui.match.EditMatchViewModel.EditMode
import xa.refile.ui.match.EditMatchViewModel.EpisodeInfo
import xa.refile.ui.match.EditMatchViewModel.MediaCandidate

/**
 * Edit Match 页（Task 2.5.1–2.5.4）。
 *
 * 由 [xa.refile.ui.navigation.AppNavHost] 经 `edit_match/{matchIndex}` 路由进入。
 * 从 Activity 作用域 [MatchSessionViewModel.matchedFiles] 取索引对应文件，载入
 * [EditMatchViewModel]；保存后单条回写 [MatchSessionViewModel.updateMatchedFile]，
 * 线性对齐批量回写 [MatchSessionViewModel.replaceMatchedFiles]，再 pop 返回。
 *
 * 三种编辑模式（顶部切换）：
 * - [EditMode.SINGLE]：单集勾选（互斥）
 * - [EditMode.MULTI]：多集组合，连续/手动勾选 → `S01E01-E02`
 * - [EditMode.ALIGNMENT]：线性对齐，左文件右集顺序对齐，上下箭头调整、解绑、批量应用
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMatchScreen(
    matchIndex: Int,
    matchSessionVm: MatchSessionViewModel,
    onBack: () -> Unit,
    viewModel: EditMatchViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val files by matchSessionVm.matchedFiles.collectAsStateWithLifecycle()

    // 进入时载入待编辑文件 + 同批次文件（线性对齐用）
    LaunchedEffect(files, matchIndex) {
        val current = files.getOrNull(matchIndex)
        if (current != null && viewModel.currentMatch.value == null) {
            viewModel.load(current, files)
        }
    }

    // 单条保存 → 回写 + 返回
    LaunchedEffect(state.saved) {
        val saved = state.saved
        if (saved != null) {
            matchSessionVm.updateMatchedFile(matchIndex, saved)
            viewModel.consumeSaved()
            onBack()
        }
    }
    // 批量保存 → 回写整表 + 返回
    LaunchedEffect(state.batchSaved) {
        val batch = state.batchSaved
        if (batch != null) {
            matchSessionVm.replaceMatchedFiles(batch)
            viewModel.consumeBatchSaved()
            onBack()
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
                    IconButton(onClick = {
                        viewModel.cancel()
                        onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "取消")
                    }
                },
                title = { Text("编辑匹配") },
                actions = {
                    IconButton(
                        onClick = {
                            if (state.editMode == EditMode.ALIGNMENT) viewModel.batchApply()
                            else viewModel.applyEdit()
                        },
                        enabled = !state.loading,
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "保存")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            val current = state.currentMatch
            if (current == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "无可编辑条目（索引 $matchIndex 越界）",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                EditMatchContent(
                    state = state,
                    onSwitchType = viewModel::switchMediaType,
                    onSetMode = viewModel::setEditMode,
                    onSearchMedia = viewModel::searchMedia,
                    onSelectMedia = viewModel::selectMedia,
                    onSetSeason = viewModel::setSeason,
                    onSearchEpisodes = viewModel::search,
                    onToggleEpisode = viewModel::toggleEpisode,
                    onSelectRange = viewModel::selectRange,
                    onApply = viewModel::applyEdit,
                    onBatchApply = viewModel::batchApply,
                    onMoveUp = viewModel::moveFileUp,
                    onMoveDown = viewModel::moveFileDown,
                    onUnbind = viewModel::unbindFile,
                )
            }
            if (state.loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMatchContent(
    state: EditMatchViewModel.UiState,
    onSwitchType: (MediaType) -> Unit,
    onSetMode: (EditMode) -> Unit,
    onSearchMedia: (String) -> Unit,
    onSelectMedia: (MediaCandidate) -> Unit,
    onSetSeason: (Int) -> Unit,
    onSearchEpisodes: (String) -> Unit,
    onToggleEpisode: (Int) -> Unit,
    onSelectRange: (Int, Int) -> Unit,
    onApply: () -> Unit,
    onBatchApply: () -> Unit,
    onMoveUp: (Int) -> Unit,
    onMoveDown: (Int) -> Unit,
    onUnbind: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        // 顶部模式切换：单集 / 多集组合 / 线性对齐
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            EditMode.entries.forEachIndexed { i, mode ->
                val label = when (mode) {
                    EditMode.SINGLE -> "单集"
                    EditMode.MULTI -> "多集组合"
                    EditMode.ALIGNMENT -> "线性对齐"
                }
                SegmentedButton(
                    selected = state.editMode == mode,
                    onClick = { onSetMode(mode) },
                    shape = SegmentedButtonDefaults.itemShape(index = i, count = EditMode.entries.size),
                ) { Text(label) }
            }
        }
        Spacer(Modifier.height(8.dp))

        if (state.editMode == EditMode.ALIGNMENT) {
            AlignmentView(
                state = state,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
                onUnbind = onUnbind,
                onBatchApply = onBatchApply,
                onSearchMedia = onSearchMedia,
                onSelectMedia = onSelectMedia,
                onSetSeason = onSetSeason,
            )
        } else {
            SingleMultiView(
                state = state,
                onSwitchType = onSwitchType,
                onSearchMedia = onSearchMedia,
                onSelectMedia = onSelectMedia,
                onSetSeason = onSetSeason,
                onSearchEpisodes = onSearchEpisodes,
                onToggleEpisode = onToggleEpisode,
                onSelectRange = onSelectRange,
                onApply = onApply,
            )
        }
    }
}

// ---- 单集 / 多集组合 ----

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleMultiView(
    state: EditMatchViewModel.UiState,
    onSwitchType: (MediaType) -> Unit,
    onSearchMedia: (String) -> Unit,
    onSelectMedia: (MediaCandidate) -> Unit,
    onSetSeason: (Int) -> Unit,
    onSearchEpisodes: (String) -> Unit,
    onToggleEpisode: (Int) -> Unit,
    onSelectRange: (Int, Int) -> Unit,
    onApply: () -> Unit,
) {
    // 类型切换：电影 / 剧集
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = state.mediaType == MediaType.MOVIE,
            onClick = { onSwitchType(MediaType.MOVIE) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
        ) { Text("电影") }
        SegmentedButton(
            selected = state.mediaType == MediaType.EPISODE,
            onClick = { onSwitchType(MediaType.EPISODE) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
        ) { Text("剧集") }
    }
    Spacer(Modifier.height(8.dp))

    // 已选候选摘要
    state.selectedMedia?.let { m ->
        SelectedMediaSummary(media = m)
        Spacer(Modifier.height(8.dp))
    }

    // 影视搜索框 + 候选列表
    MediaSearchSection(
        query = state.mediaSearchQuery,
        results = state.mediaSearchResults,
        loading = state.loading,
        onSearch = onSearchMedia,
        onSelect = onSelectMedia,
    )
    Spacer(Modifier.height(8.dp))

    // 剧集态：季选择器 + 集列表（多集模式下用 EpisodesPanel 支持长按区间多选）
    if (state.mediaType == MediaType.EPISODE) {
        SeasonPicker(season = state.seasonNumber ?: 1, onSetSeason = onSetSeason)
        Spacer(Modifier.height(8.dp))

        // find-as-you-type 集过滤
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onSearchEpisodes,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("过滤集号/标题/简介") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = "已选 ${state.selectedEpisodeNumbers.size} 集" +
                state.multiEpisodeRangePreview(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(4.dp))

        EpisodesPanel(
            episodes = state.filteredEpisodes,
            selected = state.selectedEpisodeNumbers,
            multiSelect = state.multiSelect,
            onToggle = onToggleEpisode,
            onRangeSelect = onSelectRange,
            onGenerateBundle = onApply,
        )
    }
}

/** 多集组合预览文本（仅多集时显示）。 */
private fun EditMatchViewModel.UiState.multiEpisodeRangePreview(): String {
    if (!multiSelect || selectedEpisodeNumbers.size < 2) return ""
    val range = EditMatchViewModel.formatEpisodeRange(seasonNumber ?: 1, selectedEpisodeNumbers.toList())
    return " · 组合 $range"
}

@Composable
private fun SelectedMediaSummary(media: MediaCandidate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PosterThumb(posterUrl = media.posterUrl, sizeW = 32.dp, sizeH = 48.dp)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            val title = if (media.year != null) "${media.name} (${media.year})" else media.name
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(
                if (media.mediaType == MediaType.EPISODE) "剧集" else "电影",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MediaSearchSection(
    query: String,
    results: List<MediaCandidate>,
    loading: Boolean,
    onSearch: (String) -> Unit,
    onSelect: (MediaCandidate) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onSearch,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text("搜索${if (results.isNotEmpty()) "" else "标题"}关键词")
        },
        singleLine = true,
        leadingIcon = {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        },
    )
    if (results.isNotEmpty()) {
        Spacer(Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth().height((results.size.coerceAtMost(4) * 76).dp),
        ) {
            items(results, key = { it.tmdbId }) { c ->
                CandidateRow(candidate = c, onClick = { onSelect(c) })
            }
        }
    }
}

@Composable
private fun CandidateRow(candidate: MediaCandidate, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PosterThumb(posterUrl = candidate.posterUrl, sizeW = 40.dp, sizeH = 60.dp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            val title = if (candidate.year != null) "${candidate.name} (${candidate.year})" else candidate.name
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            candidate.overview?.takeIf { it.isNotBlank() }?.let { ov ->
                Text(
                    ov,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun SeasonPicker(season: Int, onSetSeason: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("季", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.width(12.dp))
        IconButton(onClick = { if (season > 1) onSetSeason(season - 1) }) {
            Icon(Icons.Default.ArrowDownward, contentDescription = "减季")
        }
        Text(
            "第 $season 季",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.width(80.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        IconButton(onClick = { if (season < 50) onSetSeason(season + 1) }) {
            Icon(Icons.Default.ArrowUpward, contentDescription = "加季")
        }
        Spacer(Modifier.weight(1f))
    }
}

// ---- 线性对齐模式 ----

@Composable
private fun AlignmentView(
    state: EditMatchViewModel.UiState,
    onMoveUp: (Int) -> Unit,
    onMoveDown: (Int) -> Unit,
    onUnbind: (Int) -> Unit,
    onBatchApply: () -> Unit,
    onSearchMedia: (String) -> Unit,
    onSelectMedia: (MediaCandidate) -> Unit,
    onSetSeason: (Int) -> Unit,
) {
    if (state.mediaType != MediaType.EPISODE || state.selectedMedia == null) {
        // 对齐模式仅适用于剧集：先选剧集
        Text(
            "线性对齐仅适用于剧集，请先切换为剧集并搜索选择剧集",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(8.dp))
        MediaSearchSection(
            query = state.mediaSearchQuery,
            results = state.mediaSearchResults,
            loading = state.loading,
            onSearch = onSearchMedia,
            onSelect = onSelectMedia,
        )
        return
    }
    if (state.episodeList.isEmpty()) {
        Column {
            SelectedMediaSummary(media = state.selectedMedia)
            Spacer(Modifier.height(8.dp))
            SeasonPicker(season = state.seasonNumber ?: 1, onSetSeason = onSetSeason)
            Spacer(Modifier.height(8.dp))
            Text(
                "请等待集列表加载…",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SelectedMediaSummary(media = state.selectedMedia)
        Spacer(Modifier.height(4.dp))
        SeasonPicker(season = state.seasonNumber ?: 1, onSetSeason = onSetSeason)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "左列文件按顺序对齐右列集列表，用 ↑↓ 调整文件顺序，" +
                "LinkOff 解绑单条；顶栏 ✓ 批量应用",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))

        // 表头
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("文件", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
            Text("集", modifier = Modifier.width(120.dp), style = MaterialTheme.typography.labelMedium)
        }
        Spacer(Modifier.height(4.dp))

        // 左文件列 + 右集列（按索引位置对齐）
        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            // 左列：文件 + 控件
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                itemsIndexed(state.alignmentRows, key = { i, r -> r.file.filePath + i }) { i, row ->
                    AlignmentFileRow(
                        fileName = row.file.filePath.substringAfterLast('/'),
                        boundEpisodeNumber = row.boundEpisodeNumber,
                        canMoveUp = i > 0,
                        canMoveDown = i < state.alignmentRows.size - 1,
                        onMoveUp = { onMoveUp(i) },
                        onMoveDown = { onMoveDown(i) },
                        onUnbind = { onUnbind(i) },
                    )
                }
            }
            // 右列：集列表（按位置对齐）
            LazyColumn(
                modifier = Modifier.width(120.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                itemsIndexed(state.episodeList, key = { _, e -> e.episodeNumber }) { _, ep ->
                    AlignmentEpisodeRow(episode = ep)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onBatchApply,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading,
        ) {
            Text("批量应用")
        }
    }
}

@Composable
private fun AlignmentFileRow(
    fileName: String,
    boundEpisodeNumber: Int?,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onUnbind: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
        ) {
            Text(
                fileName,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                if (boundEpisodeNumber != null) "→ E${"%02d".format(boundEpisodeNumber)}"
                else "（已解绑）",
                style = MaterialTheme.typography.labelSmall,
                color = if (boundEpisodeNumber != null) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onMoveUp, enabled = canMoveUp) {
            Icon(Icons.Default.ArrowUpward, contentDescription = "上移")
        }
        IconButton(onClick = onMoveDown, enabled = canMoveDown) {
            Icon(Icons.Default.ArrowDownward, contentDescription = "下移")
        }
        IconButton(onClick = onUnbind) {
            Icon(Icons.Default.LinkOff, contentDescription = "解绑")
        }
    }
}

@Composable
private fun AlignmentEpisodeRow(episode: EpisodeInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "E${"%02d".format(episode.episodeNumber)}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(40.dp),
        )
        Text(
            episode.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// ---- 通用 ----

@Composable
private fun PosterThumb(posterUrl: String?, sizeW: androidx.compose.ui.unit.Dp, sizeH: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(width = sizeW, height = sizeH)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if (!posterUrl.isNullOrBlank()) {
            AsyncImage(
                model = posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Icon(
                Icons.Default.Movie,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
