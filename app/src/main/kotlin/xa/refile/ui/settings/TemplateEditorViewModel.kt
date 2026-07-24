package xa.refile.ui.settings

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xa.refile.core.model.MediaType
import xa.refile.core.naming.BatchContext
import xa.refile.core.naming.BindingResolver
import xa.refile.core.naming.CustomPreset
import xa.refile.core.naming.FileContext
import xa.refile.core.naming.MediaMetadata
import xa.refile.core.naming.Preset
import xa.refile.core.naming.PresetRepository
import xa.refile.core.naming.TemplateEngine
import xa.refile.core.parser.ParsedFilename
import xa.refile.data.prefs.SettingsRepository
import xa.refile.data.prefs.VisualOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 模板编辑器 ViewModel（计划 §M3 SubTask 3.3.1 + 测试反馈 Item 9）。
 *
 * 按测试反馈 Item 9 改造为 FileBot 风格：电影/剧集模板分离编辑。
 *
 * 持有：
 * - [movieTemplateField] / [episodeTemplateField]：电影/剧集模板字符串 + 光标位置。
 * - [activeTab]：当前编辑的标签（电影/剧集）。
 * - [presetId]：当前选中的预设（内置 EMBY/INFUSE 或自定义预设 ID）。
 * - [customPresets]：用户保存的自定义预设列表，可在顶部快速切换。
 * - [visualOptions]：分隔符/大小写/非法字符处理/补零位数。
 * - [previewResult]：用固定电影 + 剧集示例实时渲染的结果（各自用对应模板）。
 *
 * 渲染走 [TemplateEngine]，每次创建新的 [BindingResolver] 避免警告累积。
 */
@HiltViewModel
class TemplateEditorViewModel @Inject constructor(
    private val settings: SettingsRepository,
    @Suppress("unused") private val presets: PresetRepository,
) : ViewModel() {

    /** 编辑标签：电影模板 / 剧集模板。 */
    enum class EditorTab(val label: String) {
        MOVIE("电影模板"),
        EPISODE("剧集模板"),
    }

    /** 可插入变量 token 描述。 */
    data class VariableToken(
        val token: String,
        val label: String,
        val group: String,
    )

    /** 预览结果（电影 + 剧集两份示例，各自用对应模板渲染）。 */
    data class PreviewUi(
        val movie: String,
        val episode: String,
        val warnings: List<String> = emptyList(),
    )

    /** 固定示例上下文（电影 + 剧集），避免依赖实际选中文件。 */
    private data class SampleContext(
        val media: MediaMetadata,
        val file: FileContext,
        val batch: BatchContext,
    )

    /** 电影模板字段（含光标）。 */
    private val _movieTemplateField = MutableStateFlow(TextFieldValue(""))
    val movieTemplateField: StateFlow<TextFieldValue> = _movieTemplateField.asStateFlow()

    /** 剧集模板字段（含光标）。 */
    private val _episodeTemplateField = MutableStateFlow(TextFieldValue(""))
    val episodeTemplateField: StateFlow<TextFieldValue> = _episodeTemplateField.asStateFlow()

    /** 当前编辑标签。 */
    private val _activeTab = MutableStateFlow(EditorTab.MOVIE)
    val activeTab: StateFlow<EditorTab> = _activeTab.asStateFlow()

    /** 当前预设 ID（内置 Preset.name 或自定义预设 id）。 */
    private val _presetId = MutableStateFlow(Preset.DEFAULT.name)
    val presetId: StateFlow<String> = _presetId.asStateFlow()

    /** 用户保存的自定义预设列表。 */
    private val _customPresets = MutableStateFlow<List<CustomPreset>>(emptyList())
    val customPresets: StateFlow<List<CustomPreset>> = _customPresets.asStateFlow()

    /** 可视化选项。 */
    private val _visualOptions = MutableStateFlow(VisualOptions())
    val visualOptions: StateFlow<VisualOptions> = _visualOptions.asStateFlow()

    /** 实时预览：任一模板或可视化选项变化即重渲染（电影/剧集各自用对应模板）。 */
    val previewResult: StateFlow<PreviewUi> =
        combine(_movieTemplateField, _episodeTemplateField, _visualOptions) { movie, episode, opts ->
            renderPreview(movie.text, episode.text, opts)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PreviewUi("", ""),
        )

    /** 可插入变量列表（基于实际 [BindingResolver] 支持的 token）。 */
    val availableVariables: List<VariableToken> = VARIABLE_TOKENS

    init {
        viewModelScope.launch {
            val savedMovie = settings.movieTemplateString.first()
            val savedEpisode = settings.episodeTemplateString.first()
            val savedLegacy = settings.templateString.first()
            val savedOpts = settings.visualOptions.first()
            val savedPreset = settings.presetId.first()
            val savedCustomPresets = settings.customPresets.first()
            _visualOptions.value = savedOpts
            _presetId.value = savedPreset
            _customPresets.value = savedCustomPresets
            val preset = Preset.byId(savedPreset)
            // 优先用已保存的独立模板，回退到旧版单模板，再回退到预设默认模板
            _movieTemplateField.value = TextFieldValue(
                savedMovie.ifBlank { savedLegacy.ifBlank { preset.movieTemplate } },
            )
            _episodeTemplateField.value = TextFieldValue(
                savedEpisode.ifBlank { savedLegacy.ifBlank { preset.episodeTemplate } },
            )
        }
    }

    /** 切换编辑标签。 */
    fun selectTab(tab: EditorTab) {
        _activeTab.value = tab
    }

    /** 更新当前标签对应的模板字段（含光标）。 */
    fun updateTemplate(value: TextFieldValue) {
        when (_activeTab.value) {
            EditorTab.MOVIE -> _movieTemplateField.value = value
            EditorTab.EPISODE -> _episodeTemplateField.value = value
        }
    }

    /** 在当前光标位置插入 `{token}`，并把光标移到插入内容之后。 */
    fun insertVariable(token: String) {
        val insert = "{$token}"
        when (_activeTab.value) {
            EditorTab.MOVIE -> {
                val current = _movieTemplateField.value
                _movieTemplateField.value = insertAtCursor(current, insert)
            }
            EditorTab.EPISODE -> {
                val current = _episodeTemplateField.value
                _episodeTemplateField.value = insertAtCursor(current, insert)
            }
        }
    }

    private fun insertAtCursor(current: TextFieldValue, insert: String): TextFieldValue {
        val pos = current.selection.min.coerceIn(0, current.text.length)
        val newText = buildString {
            append(current.text.substring(0, pos))
            append(insert)
            append(current.text.substring(pos))
        }
        val newCursor = pos + insert.length
        return TextFieldValue(text = newText, selection = TextRange(newCursor))
    }

    /**
     * 选择预设（内置 EMBY/INFUSE 或自定义预设）。
     *
     * 内置预设：加载该预设的电影/剧集模板到编辑器。
     * 自定义预设：加载该自定义预设保存的电影/剧集模板。
     */
    fun selectPreset(id: String) {
        _presetId.value = id
        // 内置预设
        Preset.entries.firstOrNull { it.name == id }?.let { preset ->
            _movieTemplateField.value = TextFieldValue(preset.movieTemplate)
            _episodeTemplateField.value = TextFieldValue(preset.episodeTemplate)
            return
        }
        // 自定义预设
        _customPresets.value.firstOrNull { it.id == id }?.let { cp ->
            _movieTemplateField.value = TextFieldValue(cp.movieTemplate)
            _episodeTemplateField.value = TextFieldValue(cp.episodeTemplate)
        }
    }

    /**
     * 把当前编辑器中的电影/剧集模板另存为自定义预设（测试反馈 Item 9）。
     *
     * @param name 预设显示名（用户输入）；重名时追加序号。
     * @return 新建预设的 id（失败返回 null）。
     */
    suspend fun savePresetAs(name: String): String? {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return null
        val id = "custom_${System.currentTimeMillis()}"
        val newPreset = CustomPreset(
            id = id,
            name = uniqueName(trimmed, _customPresets.value.map { it.name }),
            movieTemplate = _movieTemplateField.value.text,
            episodeTemplate = _episodeTemplateField.value.text,
        )
        val updated = _customPresets.value + newPreset
        _customPresets.value = updated
        settings.setCustomPresets(updated)
        _presetId.value = id
        return id
    }

    /**
     * 删除自定义预设（测试反馈 Item 9）。
     *
     * 删除后若当前选中的正是被删预设，回退到默认内置预设 [Preset.DEFAULT]。
     */
    suspend fun deletePreset(id: String) {
        val updated = _customPresets.value.filterNot { it.id == id }
        _customPresets.value = updated
        settings.setCustomPresets(updated)
        if (_presetId.value == id) {
            selectPreset(Preset.DEFAULT.name)
        }
    }

    /** 更新可视化选项（实时影响预览）。 */
    fun saveVisualOptions(opts: VisualOptions) {
        _visualOptions.value = opts
    }

    /**
     * 持久化当前电影/剧集模板、预设、可视化选项到 DataStore。
     *
     * 同时把旧版单模板字段 [SettingsRepository.templateString] 同步为电影模板，
     * 以保证未升级到分模板逻辑的调用方仍可用。
     */
    suspend fun save() {
        val movie = _movieTemplateField.value.text
        val episode = _episodeTemplateField.value.text
        settings.setMovieTemplateString(movie)
        settings.setEpisodeTemplateString(episode)
        settings.setTemplateString(movie)
        settings.setPresetId(_presetId.value)
        settings.setVisualOptions(_visualOptions.value)
    }

    /** 用固定示例渲染：电影模板渲染电影示例，剧集模板渲染剧集示例。 */
    private fun renderPreview(movieTemplate: String, episodeTemplate: String, opts: VisualOptions): PreviewUi {
        val namingOptions = opts.toNamingOptions()
        val movie = TemplateEngine(
            BindingResolver(MOVIE_SAMPLE.media, MOVIE_SAMPLE.file, MOVIE_SAMPLE.batch, namingOptions),
            namingOptions,
        ).render(movieTemplate)
        val episode = TemplateEngine(
            BindingResolver(EPISODE_SAMPLE.media, EPISODE_SAMPLE.file, EPISODE_SAMPLE.batch, namingOptions),
            namingOptions,
        ).render(episodeTemplate)
        return PreviewUi(
            movie = movie.path,
            episode = episode.path,
            warnings = (movie.warnings + episode.warnings).distinct(),
        )
    }

    /** 重名时追加序号（如「我的预设 2」）。 */
    private fun uniqueName(base: String, existing: List<String>): String {
        if (base !in existing) return base
        var i = 2
        while ("$base $i" in existing) i++
        return "$base $i"
    }

    private companion object {
        /** 可插入变量（基于实际 [BindingResolver] 支持的 token，按组分类）。 */
        val VARIABLE_TOKENS = listOf(
            VariableToken("n", "标题（电影名/剧集名）", "通用"),
            VariableToken("y", "年份", "通用"),
            VariableToken("ny", "标题 (年份)", "通用"),
            VariableToken("collection", "合集", "通用"),
            VariableToken("genre", "类型", "通用"),
            VariableToken("director", "导演", "通用"),
            VariableToken("rating", "评分", "通用"),
            VariableToken("s", "季号", "剧集"),
            VariableToken("e", "集号", "剧集"),
            VariableToken("s00", "季号补零", "剧集"),
            VariableToken("e00", "集号补零", "剧集"),
            VariableToken("s00e00", "S01E01 格式", "剧集"),
            VariableToken("sxe", "1x01 格式", "剧集"),
            VariableToken("t", "集标题", "剧集"),
            VariableToken("absolute", "绝对集号", "剧集"),
            VariableToken("d", "首播日期", "剧集"),
            VariableToken("fn", "原始文件名", "文件/媒体"),
            VariableToken("ext", "扩展名", "文件/媒体"),
            VariableToken("vf", "分辨率", "文件/媒体"),
            VariableToken("vc", "视频编码器", "文件/媒体"),
            VariableToken("ac", "音频编码器", "文件/媒体"),
            VariableToken("group", "发布组", "文件/媒体"),
            VariableToken("folder", "父目录", "文件/媒体"),
            VariableToken("bytes", "文件大小", "文件/媒体"),
            VariableToken("today", "今天日期", "文件/媒体"),
        )

        val MOVIE_SAMPLE = SampleContext(
            media = MediaMetadata(
                type = MediaType.MOVIE,
                name = "盗梦空间",
                year = 2010,
                collectionName = "诺兰合集",
                genres = listOf("科幻", "动作"),
                director = "克里斯托弗·诺兰",
                rating = 8.8,
            ),
            file = FileContext(
                displayName = "Inception.2010.1080p.BluRay.x265.AC3.mkv",
                ext = "mkv",
                folder = "电影",
                parsed = ParsedFilename(
                    title = "Inception",
                    year = 2010,
                    resolution = "1080p",
                    source = "BluRay",
                    videoCodec = "x265",
                    audioCodec = "AC3",
                    group = "GROUP",
                ),
            ),
            batch = BatchContext(today = "2026-07-23"),
        )

        val EPISODE_SAMPLE = SampleContext(
            media = MediaMetadata(
                type = MediaType.EPISODE,
                name = "权力的游戏",
                year = 2011,
                seasonNumber = 1,
                episodeNumbers = listOf(1),
                episodeTitles = listOf("凛冬将至"),
                genres = listOf("奇幻", "剧情"),
                rating = 9.4,
            ),
            file = FileContext(
                displayName = "Game.of.Thrones.S01E01.1080p.WEB-DL.x264.AAC.mkv",
                ext = "mkv",
                folder = "权力的游戏",
                parsed = ParsedFilename(
                    title = "Game of Thrones",
                    year = 2011,
                    season = 1,
                    episodes = listOf(1),
                    resolution = "1080p",
                    source = "WEB-DL",
                    videoCodec = "x264",
                    audioCodec = "AAC",
                    group = "GROUP",
                ),
            ),
            batch = BatchContext(today = "2026-07-23"),
        )
    }
}
