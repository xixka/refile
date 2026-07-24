package xa.refile.ui.match

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

/**
 * Episodes 面板（Task 2.5.3）。
 *
 * 浏览全季集列表（集号 + 标题 + 海报缩略图 + 简介 + 上映日期），支持：
 * - 单击勾选/取消（单集或多集模式由 [multiSelect] 决定）
 * - **长按起点 → 点击终点** 自动选中连续区间（连续多选）
 * - 「生成多集组合」按钮：基于选中集生成 `S01E01-E02` 形式条目（调用 [onGenerateBundle]）
 *
 * 作为 EditMatchScreen 的子组件嵌入；亦可通过外层包 BottomSheet 复用。
 *
 * @param episodes 全季集列表
 * @param selected 已选集号集合
 * @param multiSelect 是否多选模式（多集组合/对齐用）
 * @param onToggle 单集勾选回调
 * @param onRangeSelect 连续区间选择回调（起点/终点集号，双向区间）
 * @param onGenerateBundle 生成多集组合回调（仅当 selected.size >= 2 时由调用方决定可用性）
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodesPanel(
    episodes: List<EditMatchViewModel.EpisodeInfo>,
    selected: Set<Int>,
    multiSelect: Boolean,
    onToggle: (Int) -> Unit,
    onRangeSelect: (Int, Int) -> Unit,
    onGenerateBundle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 连续多选：长按记起点，再点击记终点
    var rangeStart by remember { mutableStateOf<Int?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        if (episodes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "暂无集数据，请先选择剧集与季",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(episodes, key = { it.episodeNumber }) { ep ->
                    val isSelected = ep.episodeNumber in selected
                    EpisodeRow(
                        episode = ep,
                        selected = isSelected,
                        showCheckbox = multiSelect,
                        onClick = {
                            val start = rangeStart
                            if (multiSelect && start != null && start != ep.episodeNumber) {
                                // 长按起点已记 → 当前为终点，选中区间并清除起点
                                onRangeSelect(start, ep.episodeNumber)
                                rangeStart = null
                            } else {
                                onToggle(ep.episodeNumber)
                                rangeStart = null
                            }
                        },
                        onLongClick = {
                            // 长按记起点
                            rangeStart = ep.episodeNumber
                            if (!multiSelect) onToggle(ep.episodeNumber)
                        },
                    )
                    HorizontalDivider()
                }
            }
        }

        if (rangeStart != null) {
            Text(
                text = "已选起点 E${"%02d".format(rangeStart)}，点击终点选中区间",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        if (multiSelect && selected.size >= 2) {
            Button(
                onClick = onGenerateBundle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                Text("生成多集组合（${selected.size} 集）")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EpisodeRow(
    episode: EditMatchViewModel.EpisodeInfo,
    selected: Boolean,
    showCheckbox: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val bg = if (selected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showCheckbox) {
            Checkbox(
                checked = selected,
                onCheckedChange = { onClick() },
            )
            Spacer(Modifier.width(4.dp))
        } else if (selected) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
            Spacer(Modifier.width(8.dp))
        }
        StillThumbnail(stillUrl = episode.stillUrl)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "E${"%02d".format(episode.episodeNumber)}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = episode.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            episode.airDate?.takeIf { it.isNotBlank() }?.let { d ->
                Text(
                    text = d,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (episode.overview.isNotBlank()) {
                Text(
                    text = episode.overview,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/** 剧集 still 缩略图（无图时占位）。 */
@Composable
private fun StillThumbnail(stillUrl: String?) {
    Box(
        modifier = Modifier
            .size(width = 64.dp, height = 36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if (!stillUrl.isNullOrBlank()) {
            AsyncImage(
                model = stillUrl,
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
