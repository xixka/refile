package com.webdavrenamer.ui.settings;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0004\u001A\u0014\u0010\u00002\u0004\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00028\u0000H\u0007\u001A\u0012\u0010\u00062\u0004\u0010\u0007(\u00032\u0004\u0010\t(\u00048\u0000H\u0003\u001A\u0012\u0010\u000B2\u0004\u0010\u000C(\u00062\u0004\u0010\u000F(\u00048\u0000H\u0003\u001A\u0012\u0010\u00102\u0004\u0010\u0011(\u00072\u0004\u0010\u0013(\u00088\u0000H\u0003\u001A#\u0010\u0014\"\u0004\u0008\u0000\u0010\u00152\u0004\u0010\u0011(\u000B2\u0004\u0010\u0017(\t2\u0004\u0010\t(\u000C8\u0000H\u0003\u00A2\u0006\u0002\u0010\u0018\u001A\u000C\u0010\u00192\u0004\u0010\u001A(\r8\u0000H\u0003\u001A\u0012\u0010\u001C2\u0004\u0010\u001D(\u00032\u0004\u0010\u001E(\u00038\u0000H\u0003\u00F2\u0001d\n\u00020\u0001\n\u0006\u0012\u0002\u0018\u00000\u0003\n\u00020\u0005\n\u00020\u0008\n\n\u0012\u0002\u0018\u0003\u0012\u0002\u0018\u00000\n\n\u00020\u000E\n\u0006\u0012\u0002\u0018\u00050\r\n\u00020\u0012\n\n\u0012\u0002\u0018\u0007\u0012\u0002\u0018\u00000\n\n\u0002H\u0015\n\n\u0012\u0002\u0018\t\u0012\u0002\u0018\u00030\u0016\n\u0006\u0012\u0002\u0018\n0\r\n\n\u0012\u0002\u0018\t\u0012\u0002\u0018\u00000\n\n\u00020\u001B\u00A8\u0006\u001F"}, d2 = {"TemplateEditorScreen", "", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel;", "PresetSelector", "selectedId", "", "onSelect", "Lkotlin/Function1;", "VariableChips", "tokens", "", "Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$VariableToken;", "onInsert", "VisualOptionsSection", "options", "Lcom/webdavrenamer/data/prefs/VisualOptions;", "onUpdate", "OptionChipRow", "T", "Lkotlin/Pair;", "selected", "(Ljava/util/List;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "PreviewCard", "preview", "Lcom/webdavrenamer/ui/settings/TemplateEditorViewModel$PreviewUi;", "PreviewItem", "label", "value", "app_debug"}, xs= "", pn = "", xi = 48)
public final class TemplateEditorScreenKt {

    /**
     * 通用 Chip 行：横向滚动，单选。
     */
    @androidx.compose.runtime.Composable()
    private static final <T extends java.lang.Object>void OptionChipRow(java.util.List<? extends kotlin.Pair<? extends T, java.lang.String>> options, T selected, kotlin.jvm.functions.Function1<? super T, kotlin.Unit> onSelect) {
    }

    /**
     * 预设选择下拉。Plex/Kodi/Emby/Jellyfin 来自 [Preset]，外加"自定义"。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void PresetSelector(java.lang.String selectedId, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelect) {
    }

    /**
     * 实时预览卡片：电影 + 剧集示例。
     */
    @androidx.compose.runtime.Composable()
    private static final void PreviewCard(com.webdavrenamer.ui.settings.TemplateEditorViewModel.PreviewUi preview) {
    }

    @androidx.compose.runtime.Composable()
    private static final void PreviewItem(java.lang.String label, java.lang.String value) {
    }

    /**
     * 模板编辑器页（计划 §M3 SubTask 3.3.1）。
     * 
     * - 顶部栏：返回 + 保存按钮。
     * - 预设选择器（Plex/Kodi/Emby/Jellyfin/自定义）。
     * - 模板字符串输入框（多行 monospace），绑定 [TextFieldValue] 跟踪光标。
     * - 变量插入 chip 行（按组横向滚动，点击在光标处插入 `{token}`）。
     * - 可视化选项：分隔符 / 大小写 / 非法字符处理 / 补零位数 Slider。
     * - 实时预览卡片：电影 + 剧集两份示例渲染结果。
     * - 保存：写入 DataStore，Snackbar 反馈。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void TemplateEditorScreen(@org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.settings.TemplateEditorViewModel viewModel) {
    }

    /**
     * 变量插入 chip：按组分行，每组横向滚动。
     */
    @androidx.compose.runtime.Composable()
    private static final void VariableChips(java.util.List<com.webdavrenamer.ui.settings.TemplateEditorViewModel.VariableToken> tokens, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onInsert) {
    }

    /**
     * 可视化选项：分隔符 / 大小写 / 非法字符 / 补零位数。
     */
    @androidx.compose.runtime.Composable()
    private static final void VisualOptionsSection(com.webdavrenamer.data.prefs.VisualOptions options, kotlin.jvm.functions.Function1<? super com.webdavrenamer.data.prefs.VisualOptions, kotlin.Unit> onUpdate) {
    }
}
