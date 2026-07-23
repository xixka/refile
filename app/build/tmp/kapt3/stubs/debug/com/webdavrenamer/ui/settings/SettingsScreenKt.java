package com.webdavrenamer.ui.settings;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000@\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\u001A,\u0010\u00002\u0004\u0010\u0002(\u00012\u0004\u0010\u0004(\u00012\u0004\u0010\u0005(\u00012\u0004\u0010\u0006(\u00012\u0004\u0010\u0007(\u00012\u0006\u0008\u0002\u0010\u0008(\u00028\u0000H\u0007\u001A\u0012\u0010\n2\u0004\u0010\u000B(\u00032\u0004\u0010\r(\u00048\u0000H\u0003\u001A \u0010\u000F2\u0004\u0010\u0010(\u00052\u0004\u0010\u000B(\u00032\u0006\u0008\u0002\u0010\u0012(\u00062\u0004\u0010\u0013(\u00018\u0000H\u0003\u001A\u0018\u0010\u00142\u0004\u0010\u0015(\u00032\u0004\u0010\u0016(\u00082\u0004\u0010\u0019(\t8\u0000H\u0003\"\u000C\u0010\u001BH\u0003X\u0082T\u00A2\u0006\u0002\n\u0000\u00F2\u0001K\n\u00020\u0001\n\u0006\u0012\u0002\u0018\u00000\u0003\n\u00020\t\n\u00020\u000C\n\u000B\u0012\u0002\u0018\u00000\u0003\u00A2\u0006\u0002\u0008\u000E\n\u00020\u0011\n\u0004\u0018\u00010\u000C\n\n\u0012\u0002\u0018\u0003\u0012\u0002\u0018\u00030\u0018\n\u0006\u0012\u0002\u0018\u00070\u0017\n\n\u0012\u0002\u0018\u0003\u0012\u0002\u0018\u00000\u001A\u00A8\u0006\u001C"}, d2 = {"SettingsScreen", "", "onBack", "Lkotlin/Function0;", "onOpenTemplateEditor", "onOpenBackup", "onOpenHostsSettings", "onOpenHistory", "viewModel", "Lcom/webdavrenamer/ui/settings/SettingsViewModel;", "SettingsSection", "title", "", "content", "Landroidx/compose/runtime/Composable;", "SettingsRow", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "subtitle", "onClick", "LanguageDropdown", "selectedCode", "options", "", "Lkotlin/Pair;", "onSelect", "Lkotlin/Function1;", "PRESET_CUSTOM", "app_debug"}, xs= "", pn = "", xi = 48)
public final class SettingsScreenKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PRESET_CUSTOM = "CUSTOM";

    /**
     * 语言偏好下拉选择器（ExposedDropdownMenuBox）。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void LanguageDropdown(java.lang.String selectedCode, java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> options, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelect) {
    }

    /**
     * 通用设置列表项：图标 + 标题 + 副标题 + 右箭头 + 点击。
     */
    @androidx.compose.runtime.Composable()
    private static final void SettingsRow(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String subtitle, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }

    /**
     * 设置中心页（计划 §M5 Task 5.4）。
     * 
     * 作为所有子设置功能的统一入口，按分组卡片组织：
     * - 组 1 TMDB 配置：API Key 输入（密码态可切换）+ 校验状态 + 申请按钮 + 语言偏好下拉 + 强制目录类型开关。
     * - 组 2 命名与模板：模板编辑器入口（展示当前预设）+ 命名选项入口。
     * - 组 3 数据管理：备份与恢复 + 历史记录。
     * - 组 4 网络：Hosts 设置。
     * 
     * 列表项统一用 [SettingsRow]（图标 + 标题 + 副标题 + 右箭头 + 点击）。
     * 子页跳转通过 [SettingsViewModel.events] 一次性事件驱动（除历史记录直接走 [onOpenHistory]）。
     * 
     * @param onBack 返回服务器列表。
     * @param onOpenTemplateEditor 跳转模板编辑器。
     * @param onOpenBackup 跳转备份与恢复。
     * @param onOpenHostsSettings 跳转 Hosts 设置。
     * @param onOpenHistory 跳转历史记录。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SettingsScreen(@org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onOpenTemplateEditor, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onOpenBackup, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHostsSettings, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHistory, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.settings.SettingsViewModel viewModel) {
    }

    /**
     * 分组容器：小字灰色组标题 + 卡片内容。
     * 
     * 卡片内统一以 12.dp 垂直间距排列子项。
     */
    @androidx.compose.runtime.Composable()
    private static final void SettingsSection(java.lang.String title, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
}
