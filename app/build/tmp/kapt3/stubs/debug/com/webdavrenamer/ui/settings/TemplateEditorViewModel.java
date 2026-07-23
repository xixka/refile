package com.webdavrenamer.ui.settings;

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
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0002\u0008\u0011\u0008\u0007\u0012\u0001\u0000\u0018\u0000 2:\u0004/012B\u0015\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\n\u0010!2\u0004\u0010#(\u00038\u0010J\n\u0010$2\u0004\u0010%(\u00068\u0010J\n\u0010&2\u0004\u0010'(\u00068\u0010J\n\u0010(2\u0004\u0010'(\u00068\u0010J\n\u0010)2\u0004\u0010*(\t8\u0010J\u000C\u0010+8\u0010H\u0086@\u00A2\u0006\u0002\u0010,J\u0012\u0010-2\u0004\u0010.(\u00062\u0004\u0010*(\t8\u000CH\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u000BH\u0005\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\r\u0010\u000ER\u000F\u0010\u000FH\u0007\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0011\u0010\u000ER\u000C\u0010\u0012H\u0008X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0013H\u0007\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0014\u0010\u000ER\u000C\u0010\u0015H\nX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0017H\u000B\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0018\u0010\u000ER\u000F\u0010\u0019H\r\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001B\u0010\u000ER\u000F\u0010\u001CH\u000F\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001F\u0010 \u00F2\u0001d\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\n\n\u0006\u0012\u0002\u0018\u00030\t\n\u0006\u0012\u0002\u0018\u00030\u000C\n\u00020\u0010\n\u0006\u0012\u0002\u0018\u00060\u000C\n\u0006\u0012\u0002\u0018\u00060\t\n\u00020\u0016\n\u0006\u0012\u0002\u0018\t0\t\n\u0006\u0012\u0002\u0018\t0\u000C\n\u00020\u001A\n\u0006\u0012\u0002\u0018\u000C0\u000C\n\u00020\u001E\n\u0006\u0012\u0002\u0018\u000E0\u001D\n\u00020\"\u00A8\u00063"}, d2 = {"Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel;", "Landroidx/lifecycle/ViewModel;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "presets", "Lcom/webdavrenamer/core/naming/PresetRepository;", "<init>", "(Lcom/webdavrenamer/data/prefs/SettingsRepository;Lcom/webdavrenamer/core/naming/PresetRepository;)V", "_templateField", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Landroidx/compose/ui/text/input/TextFieldValue;", "templateField", "Lkotlinx/coroutines/flow/StateFlow;", "getTemplateField", "()Lkotlinx/coroutines/flow/StateFlow;", "templateString", "", "getTemplateString", "_presetId", "presetId", "getPresetId", "_visualOptions", "Lcom/webdavrenamer/data/prefs/VisualOptions;", "visualOptions", "getVisualOptions", "previewResult", "Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$PreviewUi;", "getPreviewResult", "availableVariables", "", "Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$VariableToken;", "getAvailableVariables", "()Ljava/util/List;", "updateTemplate", "", "value", "insertVariable", "token", "selectPreset", "id", "loadPreset", "saveVisualOptions", "opts", "save", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "renderPreview", "template", "VariableToken", "PreviewUi", "SampleContext", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TemplateEditorViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.naming.PresetRepository presets = null;

    /**
     * 模板字段（含光标），UI 直接绑定到 OutlinedTextField。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<androidx.compose.ui.text.input.TextFieldValue> _templateField = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<androidx.compose.ui.text.input.TextFieldValue> templateField = null;

    /**
     * 纯模板字符串（派生）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> templateString = null;

    /**
     * 当前预设 ID。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _presetId = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> presetId = null;

    /**
     * 可视化选项。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.data.prefs.VisualOptions> _visualOptions = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.data.prefs.VisualOptions> visualOptions = null;

    /**
     * 实时预览：模板或可视化选项变化即重渲染。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.settings.TemplateEditorViewModel.PreviewUi> previewResult = null;

    /**
     * 可插入变量列表（基于实际 [BindingResolver] 支持的 token）。
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.webdavrenamer.ui.settings.TemplateEditorViewModel.VariableToken> availableVariables = null;

    @org.jetbrains.annotations.NotNull()
    private static final com.webdavrenamer.ui.settings.TemplateEditorViewModel.Companion Companion = null;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRESET_CUSTOM = "CUSTOM";

    /**
     * 可插入变量（基于实际 [BindingResolver] 支持的 token，按组分类）。
     */
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.webdavrenamer.ui.settings.TemplateEditorViewModel.VariableToken> VARIABLE_TOKENS = null;

    @org.jetbrains.annotations.NotNull()
    private static final com.webdavrenamer.ui.settings.TemplateEditorViewModel.SampleContext MOVIE_SAMPLE = null;

    @org.jetbrains.annotations.NotNull()
    private static final com.webdavrenamer.ui.settings.TemplateEditorViewModel.SampleContext EPISODE_SAMPLE = null;

    @javax.inject.Inject()
    public TemplateEditorViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings, @kotlin.Suppress(names = {"unused"}) @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.PresetRepository presets) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<androidx.compose.ui.text.input.TextFieldValue> getTemplateField() {
        return null;
    }

    /**
     * 纯模板字符串（派生）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getTemplateString() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getPresetId() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.data.prefs.VisualOptions> getVisualOptions() {
        return null;
    }

    /**
     * 实时预览：模板或可视化选项变化即重渲染。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.settings.TemplateEditorViewModel.PreviewUi> getPreviewResult() {
        return null;
    }

    /**
     * 可插入变量列表（基于实际 [BindingResolver] 支持的 token）。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.ui.settings.TemplateEditorViewModel.VariableToken> getAvailableVariables() {
        return null;
    }

    /**
     * 更新模板字段（含光标）。
     */
    public final void updateTemplate(@org.jetbrains.annotations.NotNull() androidx.compose.ui.text.input.TextFieldValue value) {
    }

    /**
     * 在当前光标位置插入 `{token}`，并把光标移到插入内容之后。
     */
    public final void insertVariable(@org.jetbrains.annotations.NotNull() java.lang.String token) {
    }

    /**
     * 选择预设：[selectPreset] 的别名，加载该预设的电影模板。
     */
    public final void selectPreset(@org.jetbrains.annotations.NotNull() java.lang.String id) {
    }

    /**
     * 加载预设模板。CUSTOM 表示自定义，保留当前模板不覆盖。
     */
    public final void loadPreset(@org.jetbrains.annotations.NotNull() java.lang.String id) {
    }

    /**
     * 更新可视化选项（实时影响预览）。
     */
    public final void saveVisualOptions(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.VisualOptions opts) {
    }

    /**
     * 持久化模板、预设、可视化选项到 DataStore。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object save(@org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 用固定示例渲染模板。
     */
    private final com.webdavrenamer.ui.settings.TemplateEditorViewModel.PreviewUi renderPreview(java.lang.String template, com.webdavrenamer.data.prefs.VisualOptions opts) {
        return null;
    }

    /**
     * 可插入变量 token 描述。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\t\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u0019\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0004\u0010\u0005(\u0001\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\u0007\u0010\u00088\u0001H\u00C6\u0003J\u0007\u0010\t8\u0001H\u00C6\u0003J\u0007\u0010\n8\u0001H\u00C6\u0003J\u001F\u0010\u000B2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00018\u0002H\u00C6\u0001J\r\u0010\u000C2\u0004\u0010\u000E(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000F8\u0005H\u00D6\u0001J\u0007\u0010\u00118\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\r\n\u0004\u0018\u00010\u0001\n\u00020\u0010\u00A8\u0006\u0012"}, d2 = {"Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$VariableToken;", "", "token", "", "label", "group", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class VariableToken {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String token = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String label = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String group = null;

        /**
         * 可插入变量 token 描述。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.settings.TemplateEditorViewModel.VariableToken copy(@org.jetbrains.annotations.NotNull() java.lang.String token, @org.jetbrains.annotations.NotNull() java.lang.String label, @org.jetbrains.annotations.NotNull() java.lang.String group) {
            return null;
        }

        /**
         * 可插入变量 token 描述。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 可插入变量 token 描述。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 可插入变量 token 描述。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public VariableToken(@org.jetbrains.annotations.NotNull() java.lang.String token, @org.jetbrains.annotations.NotNull() java.lang.String label, @org.jetbrains.annotations.NotNull() java.lang.String group) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getToken() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getLabel() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getGroup() {
            return null;
        }
    }
    /**
     * 预览结果（电影 + 剧集两份示例）。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0010 \n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u001B\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0006\u0008\u0002\u0010\u0005(\u0002\u00A2\u0006\u0004\u0008\u0007\u0010\u0008J\u0007\u0010\t8\u0001H\u00C6\u0003J\u0007\u0010\n8\u0001H\u00C6\u0003J\u0007\u0010\u000B8\u0002H\u00C6\u0003J\u001F\u0010\u000C2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00028\u0003H\u00C6\u0001J\r\u0010\r2\u0004\u0010\u000F(\u00058\u0004H\u00D6\u0003J\u0007\u0010\u00108\u0006H\u00D6\u0001J\u0007\u0010\u00128\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0002\u00A2\u0006\u0002\n\u0000\u00F2\u0001\"\n\u00020\u0001\n\u00020\u0003\n\u0006\u0012\u0002\u0018\u00010\u0006\n\u00020\u0000\n\u00020\u000E\n\u0004\u0018\u00010\u0001\n\u00020\u0011\u00A8\u0006\u0013"}, d2 = {"Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$PreviewUi;", "", "movie", "", "episode", "warnings", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class PreviewUi {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String movie = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String episode = null;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.String> warnings = null;

        /**
         * 预览结果（电影 + 剧集两份示例）。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.settings.TemplateEditorViewModel.PreviewUi copy(@org.jetbrains.annotations.NotNull() java.lang.String movie, @org.jetbrains.annotations.NotNull() java.lang.String episode, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> warnings) {
            return null;
        }

        /**
         * 预览结果（电影 + 剧集两份示例）。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 预览结果（电影 + 剧集两份示例）。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 预览结果（电影 + 剧集两份示例）。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public PreviewUi(@org.jetbrains.annotations.NotNull() java.lang.String movie, @org.jetbrains.annotations.NotNull() java.lang.String episode, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> warnings) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMovie() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getEpisode() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getWarnings() {
            return null;
        }
    }
    /**
     * 固定示例上下文（电影 + 剧集），避免依赖实际选中文件。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0000\u0008\u0082\u0008\u0012\u0001\u0000\u0018\u0000B\u0019\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0003\u00A2\u0006\u0004\u0008\u0008\u0010\tJ\u0007\u0010\n8\u0001H\u00C6\u0003J\u0007\u0010\u000B8\u0002H\u00C6\u0003J\u0007\u0010\u000C8\u0003H\u00C6\u0003J\u001F\u0010\r2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00038\u0004H\u00C6\u0001J\r\u0010\u000E2\u0004\u0010\u0010(\u00068\u0005H\u00D6\u0003J\u0007\u0010\u00118\u0007H\u00D6\u0001J\u0007\u0010\u00138\u0008H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0003\u00A2\u0006\u0002\n\u0000\u00F2\u0001&\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\u0000\n\u00020\u000F\n\u0004\u0018\u00010\u0001\n\u00020\u0012\n\u00020\u0014\u00A8\u0006\u0015"}, d2 = {"Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$SampleContext;", "", "media", "Lcom/webdavrenamer/core/naming/MediaMetadata;", "file", "Lcom/webdavrenamer/core/naming/FileContext;", "batch", "Lcom/webdavrenamer/core/naming/BatchContext;", "<init>", "(Lcom/webdavrenamer/core/naming/MediaMetadata;Lcom/webdavrenamer/core/naming/FileContext;Lcom/webdavrenamer/core/naming/BatchContext;)V", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
    private static final class SampleContext {
        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.core.naming.MediaMetadata media = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.core.naming.FileContext file = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.core.naming.BatchContext batch = null;

        /**
         * 固定示例上下文（电影 + 剧集），避免依赖实际选中文件。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.settings.TemplateEditorViewModel.SampleContext copy(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.MediaMetadata media, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.FileContext file, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.BatchContext batch) {
            return null;
        }

        /**
         * 固定示例上下文（电影 + 剧集），避免依赖实际选中文件。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 固定示例上下文（电影 + 剧集），避免依赖实际选中文件。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 固定示例上下文（电影 + 剧集），避免依赖实际选中文件。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public SampleContext(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.MediaMetadata media, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.FileContext file, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.BatchContext batch) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.naming.MediaMetadata component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.naming.MediaMetadata getMedia() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.naming.FileContext component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.naming.FileContext getFile() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.naming.BatchContext component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.core.naming.BatchContext getBatch() {
            return null;
        }
    }
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0005\u0008\u0082\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086TR\r\u0010\u0006H\u0003\u00A2\u0006\u0006\u001A\u0004\u0008\t\u0010\nR\r\u0010\u000BH\u0004\u00A2\u0006\u0006\u001A\u0004\u0008\r\u0010\u000ER\r\u0010\u000FH\u0004\u00A2\u0006\u0006\u001A\u0004\u0008\u0010\u0010\u000E\u00F2\u0001\u0018\n\u00020\u0001\n\u00020\u0005\n\u00020\u0008\n\u0006\u0012\u0002\u0018\u00020\u0007\n\u00020\u000C\u00A8\u0006\u0011"}, d2 = {"Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$Companion;", "", "<init>", "()V", "PRESET_CUSTOM", "", "VARIABLE_TOKENS", "", "Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$VariableToken;", "getVARIABLE_TOKENS", "()Ljava/util/List;", "MOVIE_SAMPLE", "Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$SampleContext;", "getMOVIE_SAMPLE", "()Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$SampleContext;", "EPISODE_SAMPLE", "getEPISODE_SAMPLE", "app_debug"}, xs= "", pn = "", xi = 48)
    private static final class Companion {

        private Companion() {
            super();
        }

        /**
         * 可插入变量（基于实际 [BindingResolver] 支持的 token，按组分类）。
         */
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.ui.settings.TemplateEditorViewModel.VariableToken> getVARIABLE_TOKENS() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.settings.TemplateEditorViewModel.SampleContext getMOVIE_SAMPLE() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.settings.TemplateEditorViewModel.SampleContext getEPISODE_SAMPLE() {
            return null;
        }
    }
}
