package xa.refile.ui.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xa.refile.core.model.MediaType
import xa.refile.core.naming.MediaMetadata
import xa.refile.core.tmdb.Episode
import xa.refile.core.tmdb.TmdbClient
import xa.refile.core.tmdb.TmdbImages
import xa.refile.data.prefs.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.Collections
import javax.inject.Inject

/**
 * Edit Match ViewModel（Task 2.5.1–2.5.4）。
 *
 * 单条手动修正编排：切换电影/剧集 → 搜索候选 → 选定 → （剧集）选季选集 → 保存。
 * 支持多集组合（连续 [1,2]→`S01E01-E02`；非连续 [1,3]→`S01E01,E03`，标题 `A & B` 合并）、
 * Episodes 面板连续多选预制、线性对齐批量匹配。
 *
 * 数据流：[MatchSessionViewModel]（Activity 作用域）持有 matchedFiles 快照；
 * EditMatchScreen 按导航参数 `matchIndex` 取出对应 [MatchViewModel.FileMatch] 调 [load]，
 * 保存后由 EditMatchScreen 回写 [MatchSessionViewModel]。本 VM 不直接持有会话 VM，
 * 避免跨 ViewModel 注入复杂度（Hilt 不便将一个 @HiltViewModel 注入另一个）。
 *
 * 安全：API Key 仅从 [SettingsRepository] 读取构造 [TmdbClient]，不进 UI 状态或日志。
 */
@HiltViewModel
class EditMatchViewModel @Inject constructor(
    private val settings: SettingsRepository,
) : ViewModel() {

    /** 线性对齐模式（Task 2.5.4）。 */
    enum class AlignmentMode { OFF, SEQUENTIAL }

    /** 顶部编辑模式：单集 / 多集组合 / 线性对齐。 */
    enum class EditMode { SINGLE, MULTI, ALIGNMENT }

    /** UI 友好的集信息（从 TMDB [Episode] 映射）。 */
    data class EpisodeInfo(
        val episodeNumber: Int,
        val name: String,
        val overview: String,
        val airDate: String?,
        val stillUrl: String?,
    )

    /** 影视搜索候选（电影/剧集通用，带海报）。 */
    data class MediaCandidate(
        val tmdbId: Int,
        val name: String,
        val year: Int?,
        val overview: String?,
        val posterUrl: String?,
        val mediaType: MediaType,
    )

    /** 线性对齐行：文件 + 绑定的集号（null=已解绑）。 */
    data class AlignmentRow(
        val file: MatchViewModel.FileMatch,
        val boundEpisodeNumber: Int?,
    )

    /** 编辑页 UI 状态。 */
    data class UiState(
        val currentMatch: MatchViewModel.FileMatch? = null,
        val mediaType: MediaType = MediaType.MOVIE,
        val editMode: EditMode = EditMode.SINGLE,
        val alignmentMode: AlignmentMode = AlignmentMode.OFF,
        val multiSelect: Boolean = false,
        val seasonNumber: Int? = null,
        val episodeList: List<EpisodeInfo> = emptyList(),
        val selectedEpisodeNumbers: Set<Int> = emptySet(),
        val searchQuery: String = "",
        val filteredEpisodes: List<EpisodeInfo> = emptyList(),
        val mediaSearchQuery: String = "",
        val mediaSearchResults: List<MediaCandidate> = emptyList(),
        val selectedMedia: MediaCandidate? = null,
        val alignmentRows: List<AlignmentRow> = emptyList(),
        val loading: Boolean = false,
        val error: String? = null,
        val saved: MatchViewModel.FileMatch? = null,
        val batchSaved: List<MatchViewModel.FileMatch>? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // 任务规约要求的细分 StateFlow（由 uiState 派生，便于外部按字段订阅）。
    val currentMatch: StateFlow<MatchViewModel.FileMatch?> =
        _uiState.map { it.currentMatch }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val mediaType: StateFlow<MediaType> =
        _uiState.map { it.mediaType }.stateIn(viewModelScope, SharingStarted.Eagerly, MediaType.MOVIE)
    val seasonNumber: StateFlow<Int?> =
        _uiState.map { it.seasonNumber }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val episodeList: StateFlow<List<EpisodeInfo>> =
        _uiState.map { it.episodeList }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val selectedEpisodeNumbers: StateFlow<Set<Int>> =
        _uiState.map { it.selectedEpisodeNumbers }.stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())
    val searchQuery: StateFlow<String> =
        _uiState.map { it.searchQuery }.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val filteredEpisodes: StateFlow<List<EpisodeInfo>> =
        _uiState.map { it.filteredEpisodes }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val alignmentMode: StateFlow<AlignmentMode> =
        _uiState.map { it.alignmentMode }.stateIn(viewModelScope, SharingStarted.Eagerly, AlignmentMode.OFF)

    private var mediaSearchJob: Job? = null

    /**
     * 由 EditMatchScreen 在进入时调用：载入待编辑的 [fileMatch] 与同批次 [allFiles]
     * （线性对齐用）。若已有剧集匹配，预加载该季集列表。
     */
    fun load(fileMatch: MatchViewModel.FileMatch, allFiles: List<MatchViewModel.FileMatch>) {
        if (_uiState.value.currentMatch != null) return
        val matched = fileMatch.matched
        val type = matched?.type
            ?: if (fileMatch.parsed.isEpisode) MediaType.EPISODE else MediaType.MOVIE
        val season = matched?.seasonNumber ?: fileMatch.parsed.season
        _uiState.update { s ->
            s.copy(
                currentMatch = fileMatch,
                mediaType = type,
                seasonNumber = if (type == MediaType.EPISODE) season ?: 1 else null,
                selectedEpisodeNumbers = (matched?.episodeNumbers ?: fileMatch.parsed.episodes).toSet(),
                selectedMedia = matched?.toMediaCandidate(type),
                alignmentRows = allFiles.map { AlignmentRow(it, it.matched?.episodeNumbers?.firstOrNull()) },
            )
        }
        val tvId = matched?.id ?: matched?.tmdbId
        if (type == MediaType.EPISODE && tvId != null) {
            loadSeason(tvId, season ?: 1)
        }
    }

    /** 切换电影/剧集类型（Task 2.5.1）。切换时清空已选候选与集列表。 */
    fun switchMediaType(type: MediaType) {
        _uiState.update {
            it.copy(
                mediaType = type,
                seasonNumber = if (type == MediaType.EPISODE) (it.seasonNumber ?: 1) else null,
                episodeList = if (type == MediaType.EPISODE) it.episodeList else emptyList(),
                filteredEpisodes = if (type == MediaType.EPISODE) it.filteredEpisodes else emptyList(),
                selectedEpisodeNumbers = if (type == MediaType.EPISODE) it.selectedEpisodeNumbers else emptySet(),
                selectedMedia = null,
                mediaSearchResults = emptyList(),
                mediaSearchQuery = "",
                searchQuery = "",
                alignmentMode = AlignmentMode.OFF,
                editMode = EditMode.SINGLE,
                multiSelect = false,
            )
        }
    }

    /** 顶部编辑模式切换：单集 / 多集组合 / 线性对齐（Task 2.5.2 / 2.5.4）。 */
    fun setEditMode(mode: EditMode) {
        _uiState.update {
            it.copy(
                editMode = mode,
                multiSelect = mode == EditMode.MULTI,
                alignmentMode = if (mode == EditMode.ALIGNMENT) AlignmentMode.SEQUENTIAL else AlignmentMode.OFF,
            )
        }
        if (mode == EditMode.ALIGNMENT) rebuildAlignmentRows()
    }

    /**
     * find-as-you-type 集过滤（Task 2.5.1）：按集号/标题/简介包含匹配。
     * 仅作用于已加载的 [episodeList]。
     */
    fun search(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyEpisodeFilter()
    }

    private fun applyEpisodeFilter() {
        val q = _uiState.value.searchQuery.trim().lowercase()
        val list = _uiState.value.episodeList
        val filtered = if (q.isEmpty()) list else list.filter { e ->
            e.name.lowercase().contains(q) ||
                e.overview.lowercase().contains(q) ||
                e.episodeNumber.toString() == q
        }
        _uiState.update { it.copy(filteredEpisodes = filtered) }
    }

    /**
     * 影视候选搜索（电影/剧集标题）。手动 debounce 350ms，避免连击打爆 API。
     * 空查询清空结果。
     */
    fun searchMedia(query: String) {
        _uiState.update { it.copy(mediaSearchQuery = query) }
        mediaSearchJob?.cancel()
        val q = query.trim()
        if (q.isEmpty()) {
            _uiState.update { it.copy(mediaSearchResults = emptyList()) }
            return
        }
        mediaSearchJob = viewModelScope.launch {
            delay(350)
            try {
                _uiState.update { it.copy(loading = true, error = null) }
                val client = createClientOrNull() ?: return@launch
                val language = settings.language.first()
                val type = _uiState.value.mediaType
                val results = if (type == MediaType.EPISODE) {
                    client.searchTv(q, null, language)
                } else {
                    client.searchMovie(q, null, language)
                }
                val candidates = results.map { it.toMediaCandidate(type) }
                _uiState.update { it.copy(mediaSearchResults = candidates, loading = false) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(loading = false, error = t.message ?: "搜索失败") }
            }
        }
    }

    /** 选定一个搜索候选；剧集则自动加载首季集列表。 */
    fun selectMedia(candidate: MediaCandidate) {
        _uiState.update {
            it.copy(
                selectedMedia = candidate,
                mediaSearchResults = emptyList(),
                mediaSearchQuery = "",
            )
        }
        if (candidate.mediaType == MediaType.EPISODE) {
            loadSeason(candidate.tmdbId, _uiState.value.seasonNumber ?: 1)
        }
    }

    /** 改变季号并重新加载集列表。 */
    fun setSeason(season: Int) {
        val tvId = _uiState.value.selectedMedia?.tmdbId
            ?: _uiState.value.currentMatch?.matched?.id
            ?: _uiState.value.currentMatch?.matched?.tmdbId
            ?: return
        loadSeason(tvId, season)
    }

    /** 加载某季集列表（Task 2.5.1/2.5.3）。 */
    fun loadSeason(tvId: Int, season: Int) {
        _uiState.update { it.copy(seasonNumber = season, loading = true, error = null) }
        viewModelScope.launch {
            try {
                val client = createClientOrNull() ?: return@launch
                val language = settings.language.first()
                val detail = client.getSeason(tvId, season, language)
                val episodes = detail.episodes.map { it.toEpisodeInfo() }
                _uiState.update {
                    it.copy(episodeList = episodes, filteredEpisodes = episodes, loading = false)
                }
                applyEpisodeFilter()
                rebuildAlignmentRows()
            } catch (t: Throwable) {
                _uiState.update { it.copy(loading = false, error = t.message ?: "加载季失败") }
            }
        }
    }

    /** 勾选/取消勾选单集（Task 2.5.2）。单集模式下互斥替换；多集模式下累加。 */
    fun toggleEpisode(num: Int) {
        _uiState.update { s ->
            if (s.multiSelect) {
                val set = s.selectedEpisodeNumbers.toMutableSet()
                if (!set.add(num)) set.remove(num)
                s.copy(selectedEpisodeNumbers = set)
            } else {
                s.copy(
                    selectedEpisodeNumbers =
                        if (s.selectedEpisodeNumbers == setOf(num)) emptySet() else setOf(num),
                )
            }
        }
    }

    /** 连续多选 [start]..[end]（双向区间），覆盖式选中（Task 2.5.2/2.5.3）。 */
    fun selectRange(start: Int, end: Int) {
        val range = if (start <= end) start..end else end..start
        _uiState.update { it.copy(selectedEpisodeNumbers = range.toSet(), multiSelect = true) }
    }

    /**
     * 应用单条编辑（Task 2.5.1/2.5.2/2.5.3）。
     *
     * 电影 → 拉详情写回；剧集 → 按所选集号合并元数据，多集时合并标题 `A & B`、
     * 集号标签 `S01E01-E02`，写入 [MatchViewModel.FileMatch.multiEpisodeRange]。
     * 结果通过 [UiState.saved] 暴露，由 EditMatchScreen 回写会话 VM。
     */
    fun applyEdit() {
        val s = _uiState.value
        val current = s.currentMatch ?: return
        val media = s.selectedMedia
        if (media == null) {
            _uiState.update { it.copy(error = if (s.mediaType == MediaType.EPISODE) "请先选择剧集" else "请先选择电影") }
            return
        }
        if (s.mediaType == MediaType.EPISODE && s.selectedEpisodeNumbers.isEmpty()) {
            _uiState.update { it.copy(error = "请至少选择一集") }
            return
        }
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val client = createClientOrNull() ?: return@launch
                val language = settings.language.first()
                val meta = if (s.mediaType == MediaType.EPISODE) {
                    buildEpisodeMetadata(
                        client, media.tmdbId,
                        s.seasonNumber ?: 1, s.selectedEpisodeNumbers, language,
                    )
                } else {
                    client.getMovie(media.tmdbId, language)
                }
                val range = if (s.mediaType == MediaType.EPISODE && s.selectedEpisodeNumbers.isNotEmpty()) {
                    formatEpisodeRange(s.seasonNumber ?: 1, s.selectedEpisodeNumbers.toList())
                } else {
                    null
                }
                val edited = current.copy(
                    status = MatchViewModel.MatchStatus.CONFIRMED,
                    matched = meta,
                    manuallyEdited = true,
                    multiEpisodeRange = range,
                    candidates = emptyList(),
                    error = null,
                )
                _uiState.update { it.copy(loading = false, saved = edited) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(loading = false, error = t.message ?: "保存失败") }
            }
        }
    }

    /**
     * 线性对齐批量应用（Task 2.5.4）。
     *
     * 按 [UiState.alignmentRows] 顺序，将每行绑定集号写回对应文件；已解绑行保持原状。
     * 结果通过 [UiState.batchSaved] 暴露整表，由 EditMatchScreen 回写 [MatchSessionViewModel.replaceMatchedFiles]。
     */
    fun batchApply() {
        val s = _uiState.value
        if (s.episodeList.isEmpty()) {
            _uiState.update { it.copy(error = "请先加载季集列表") }
            return
        }
        val media = s.selectedMedia
        if (media == null || media.mediaType != MediaType.EPISODE) {
            _uiState.update { it.copy(error = "线性对齐仅适用于剧集") }
            return
        }
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val client = createClientOrNull() ?: return@launch
                val language = settings.language.first()
                val seasonNumber = s.seasonNumber ?: 1
                val season = runCatching { client.getSeason(media.tmdbId, seasonNumber, language) }.getOrNull()
                val byNum = season?.episodes
                    ?.filter { it.episodeNumber != null }
                    ?.associateBy { it.episodeNumber!! }
                    ?: emptyMap()
                val tv = client.getTv(media.tmdbId, language)
                val edited = s.alignmentRows.map { row ->
                    val epNum = row.boundEpisodeNumber ?: return@map row.file
                    val titles = listOfNotNull(byNum[epNum]?.name)
                    val meta = tv.copy(
                        seasonNumber = seasonNumber,
                        episodeNumbers = listOf(epNum),
                        episodeTitles = titles,
                        episodeAirDates = listOfNotNull(byNum[epNum]?.airDate),
                        seasonName = season?.name,
                    )
                    row.file.copy(
                        status = MatchViewModel.MatchStatus.CONFIRMED,
                        matched = meta,
                        manuallyEdited = true,
                        multiEpisodeRange = formatEpisodeRange(seasonNumber, listOf(epNum)),
                        candidates = emptyList(),
                        error = null,
                    )
                }
                _uiState.update { it.copy(loading = false, batchSaved = edited) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(loading = false, error = t.message ?: "批量应用失败") }
            }
        }
    }

    // ---- 线性对齐操作 ----

    /** 重新按当前集列表顺序为对齐行绑定集号（保持文件顺序，集顺序对齐）。 */
    private fun rebuildAlignmentRows() {
        val files = _uiState.value.alignmentRows.map { it.file }
        val episodes = _uiState.value.episodeList
        if (files.isEmpty() || episodes.isEmpty()) return
        val rows = files.mapIndexed { i, f -> AlignmentRow(f, episodes.getOrNull(i)?.episodeNumber) }
        _uiState.update { it.copy(alignmentRows = rows) }
    }

    fun moveFileUp(index: Int) {
        if (index <= 0) return
        val rows = _uiState.value.alignmentRows.toMutableList()
        Collections.swap(rows, index - 1, index)
        _uiState.update { it.copy(alignmentRows = rows) }
    }

    fun moveFileDown(index: Int) {
        val rows = _uiState.value.alignmentRows
        if (index < 0 || index >= rows.size - 1) return
        val list = rows.toMutableList()
        Collections.swap(list, index, index + 1)
        _uiState.update { it.copy(alignmentRows = list) }
    }

    /** 单条解绑（Task 2.5.4）：清除该行的集号绑定。 */
    fun unbindFile(index: Int) {
        val rows = _uiState.value.alignmentRows.toMutableList()
        if (index !in rows.indices) return
        rows[index] = rows[index].copy(boundEpisodeNumber = null)
        _uiState.update { it.copy(alignmentRows = rows) }
    }

    // ---- 生命周期收尾 ----

    fun consumeSaved() = _uiState.update { it.copy(saved = null) }
    fun consumeBatchSaved() = _uiState.update { it.copy(batchSaved = null) }
    fun clearError() = _uiState.update { it.copy(error = null) }

    /** 取消编辑：清空待保存信号与错误，不写回。 */
    fun cancel() {
        _uiState.update { it.copy(saved = null, batchSaved = null, error = null, loading = false) }
    }

    // ---- 内部工具 ----

    /** 读 API Key 构造 [TmdbClient]；空 key 写错误返回 null。 */
    private suspend fun createClientOrNull(): TmdbClient? {
        val apiKey = settings.apiKey.first()
        if (apiKey.isBlank()) {
            _uiState.update { it.copy(loading = false, error = "请先在设置中填入 TMDB API Key") }
            return null
        }
        return TmdbClient.create(OkHttpClient(), apiKey)
    }

    /**
     * 构造剧集 [MediaMetadata]：拉 TV 详情 + 当季详情，按所选集号合并
     * `seasonNumber / episodeNumbers / episodeTitles(多集 A & B) / episodeAirDates / seasonName`。
     * 对齐 [MatchViewModel] 的 fetchDetail 规则。
     */
    private suspend fun buildEpisodeMetadata(
        client: TmdbClient,
        tvId: Int,
        seasonNumber: Int,
        episodes: Set<Int>,
        language: String,
    ): MediaMetadata {
        val tv = client.getTv(tvId, language)
        val season = runCatching { client.getSeason(tvId, seasonNumber, language) }.getOrNull()
        val byNum = season?.episodes
            ?.filter { it.episodeNumber != null }
            ?.associateBy { it.episodeNumber!! }
            ?: emptyMap()
        val sortedEps = episodes.sorted()
        val titles = sortedEps.mapNotNull { byNum[it]?.name }
        val airDates = sortedEps.mapNotNull { byNum[it]?.airDate }
        return tv.copy(
            seasonNumber = seasonNumber,
            episodeNumbers = sortedEps,
            episodeTitles = if (titles.size > 1) listOf(titles.joinToString(" & ")) else titles,
            episodeAirDates = airDates,
            seasonName = season?.name,
        )
    }

    private fun Episode.toEpisodeInfo(): EpisodeInfo = EpisodeInfo(
        episodeNumber = episodeNumber ?: 0,
        name = name?.takeIf { it.isNotBlank() } ?: "第 ${episodeNumber ?: 0} 集",
        overview = overview ?: "",
        airDate = airDate,
        stillUrl = stillPath?.let { TmdbImages.still(path = it) },
    )

    private fun MediaMetadata.toMediaCandidate(type: MediaType): MediaCandidate = MediaCandidate(
        tmdbId = tmdbId ?: id ?: 0,
        name = name ?: "",
        year = year,
        overview = info["overview"],
        posterUrl = info["posterPath"]?.let { TmdbImages.poster(path = it) },
        mediaType = type,
    )

    companion object {
        /**
         * 多集集号标签格式化（Task 2.5.2）。
         * - 连续 [1,2] → `S01E01-E02`
         * - 非连续 [1,3] → `S01E01,E03`
         * - 混合 [1,2,4] → `S01E01-E02,E04`
         */
        fun formatEpisodeRange(season: Int, episodes: List<Int>): String {
            if (episodes.isEmpty()) return ""
            val sorted = episodes.distinct().sorted()
            val seasonStr = "S%02d".format(season)
            val segments = mutableListOf<String>()
            var start = sorted.first()
            var prev = start
            for (i in 1 until sorted.size) {
                val cur = sorted[i]
                if (cur == prev + 1) {
                    prev = cur
                } else {
                    segments += formatSegment(start, prev)
                    start = cur
                    prev = cur
                }
            }
            segments += formatSegment(start, prev)
            return seasonStr + segments.joinToString(",")
        }

        private fun formatSegment(start: Int, end: Int): String =
            if (start == end) "E%02d".format(start) else "E%02d-E%02d".format(start, end)
    }
}
