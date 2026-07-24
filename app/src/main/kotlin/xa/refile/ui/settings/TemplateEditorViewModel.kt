package xa.refile.ui.settings

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xa.refile.core.model.MediaType
import xa.refile.core.naming.BatchContext
import xa.refile.core.naming.BindingResolver
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 模板编辑器 ViewModel（计划 §M3 SubTask 3.3.1）。
 *
 * 持有：
 * - [templateField]：模板字符串 + 光标位置（[TextFieldValue]），用于在光标处插入变量。
 * - [templateString]：由 [templateField] 派生的纯字符串。
 * - [presetId]：当前预设（PLEX/KODI/EMBY/JELLYFIN/CUSTOM）。
 * - [visualOptions]：分隔符/大小写/非法字符处理/补零位数。
 * - [previewResult]：用固定电影 + 剧集示例实时渲染的结果（不依赖实际选中文件）。
 *
 * 渲染走 [TemplateEngine]，每次创建新的 [BindingResolver] 避免警告累积。
 */
@HiltViewModel
class TemplateEditorViewModel @Inject constructor(
    private val settings: SettingsRepository,
    @Suppress("unused") private val presets: PresetRepository,
) : ViewModel() {

    /** 可插入变量 token 描述。 */
    data class VariableToken(
        val token: String,
        val label: String,
        val group: String,
    )

    /** 预览结果（电影 + 剧集两份示例）。 */
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

    /** 模板字段（含光标），UI 直接绑定到 OutlinedTextField。 */
    private val _templateField = MutableStateFlow(TextFieldValue(""))
    val templateField: StateFlow<TextFieldValue> = _templateField.asStateFlow()

    /** 纯模板字符串（派生）。 */
    val templateString: StateFlow<String> = _templateField
        .map { it.text }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    /** 当前预设 ID。 */
    private val _presetId = MutableStateFlow(Preset.DEFAULT.name)
    val presetId: StateFlow<String> = _presetId.asStateFlow()

    /** 可视化选项。 */
    private val _visualOptions = MutableStateFlow(VisualOptions())
    val visualOptions: StateFlow<VisualOptions> = _visualOptions.asStateFlow()

    /** 实时预览：模板或可视化选项变化即重渲染。 */
    val previewResult: StateFlow<PreviewUi> =
        combine(_templateField, _visualOptions) { field, opts ->
            renderPreview(field.text, opts)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PreviewUi("", ""),
        )

    /** 可插入变量列表（基于实际 [BindingResolver] 支持的 token）。 */
    val availableVariables: List<VariableToken> = VARIABLE_TOKENS

    init {
        // 从 DataStore 加载已保存的模板与可视化选项
        viewModelScope.launch {
            val savedTemplate = settings.templateString.first()
            val savedOpts = settings.visualOptions.first()
            val savedPreset = settings.presetId.first()
            _visualOptions.value = savedOpts
            _presetId.value = savedPreset
            // 无已保存模板时回退到默认预设的电影模板
            _templateField.value = TextFieldValue(savedTemplate.ifBlank { Preset.DEFAULT.movieTemplate })
        }
    }

    /** 更新模板字段（含光标）。 */
    fun updateTemplate(value: TextFieldValue) {
        _templateField.value = value
    }

    /** 在当前光标位置插入 `{token}`，并把光标移到插入内容之后。 */
    fun insertVariable(token: String) {
        val current = _templateField.value
        val insert = "{$token}"
        val pos = current.selection.min.coerceIn(0, current.text.length)
        val newText = buildString {
            append(current.text.substring(0, pos))
            append(insert)
            append(current.text.substring(pos))
        }
        val newCursor = pos + insert.length
        _templateField.value = TextFieldValue(
            text = newText,
            selection = TextRange(newCursor),
        )
    }

    /** 选择预设：[selectPreset] 的别名，加载该预设的电影模板。 */
    fun selectPreset(id: String) = loadPreset(id)

    /** 加载预设模板。CUSTOM 表示自定义，保留当前模板不覆盖。 */
    fun loadPreset(id: String) {
        _presetId.value = id
        if (id == PRESET_CUSTOM) return
        val preset = Preset.byId(id)
        // 以电影模板作为编辑起点；剧集预览也会用同一模板渲染
        _templateField.value = TextFieldValue(preset.movieTemplate)
    }

    /** 更新可视化选项（实时影响预览）。 */
    fun saveVisualOptions(opts: VisualOptions) {
        _visualOptions.value = opts
    }

    /** 持久化模板、预设、可视化选项到 DataStore。 */
    suspend fun save() {
        settings.setTemplateString(_templateField.value.text)
        settings.setPresetId(_presetId.value)
        settings.setVisualOptions(_visualOptions.value)
    }

    /** 用固定示例渲染模板。 */
    private fun renderPreview(template: String, opts: VisualOptions): PreviewUi {
        val namingOptions = opts.toNamingOptions()
        val movie = TemplateEngine(
            BindingResolver(MOVIE_SAMPLE.media, MOVIE_SAMPLE.file, MOVIE_SAMPLE.batch, namingOptions),
            namingOptions,
        ).render(template)
        val episode = TemplateEngine(
            BindingResolver(EPISODE_SAMPLE.media, EPISODE_SAMPLE.file, EPISODE_SAMPLE.batch, namingOptions),
            namingOptions,
        ).render(template)
        return PreviewUi(
            movie = movie.path,
            episode = episode.path,
            warnings = (movie.warnings + episode.warnings).distinct(),
        )
    }

    private companion object {
        const val PRESET_CUSTOM = "CUSTOM"

        /** 可插入变量（基于实际 [BindingResolver] 支持的 token，按组分类）。 */
        val VARIABLE_TOKENS = listOf(
            VariableToken("n", "标题（电影名/剧集名）", "通用"),
            VariableToken("y", "年份", "通用"),
            VariableToken("collection", "合集", "通用"),
            VariableToken("genre", "类型", "通用"),
            VariableToken("director", "导演", "通用"),
            VariableToken("rating", "评分", "通用"),
            VariableToken("s", "季号", "剧集"),
            VariableToken("e", "集号", "剧集"),
            VariableToken("s00", "季号补零", "剧集"),
            VariableToken("e00", "集号补零", "剧集"),
            VariableToken("s00e00", "S01E01 格式", "剧集"),
            VariableToken("t", "集标题", "剧集"),
            VariableToken("absolute", "绝对集号", "剧集"),
            VariableToken("fn", "原始文件名", "文件/媒体"),
            VariableToken("ext", "扩展名", "文件/媒体"),
            VariableToken("vf", "分辨率", "文件/媒体"),
            VariableToken("vc", "视频编码器", "文件/媒体"),
            VariableToken("ac", "音频编码器", "文件/媒体"),
            VariableToken("group", "发布组", "文件/媒体"),
            VariableToken("folder", "父目录", "文件/媒体"),
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
