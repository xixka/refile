package com.webdavrenamer.ui.settings;

/**
 * 设置中心 ViewModel（计划 §M5 Task 5.4）。
 * 
 * 作为所有子设置功能的统一入口状态持有者：
 * - [apiKey] / [apiKeyValid]：TMDB API Key 及其校验状态（非空且长度 ≥ 32 视为有效）。
 * - [language]：TMDB 请求语言偏好（如 `zh-CN`/`en-US`/`ja-JP`）。
 * - [availableLanguages]：可选语言列表（code → 显示名），供下拉选择。
 * - [presetId]：当前命名预设 ID，用于在「命名与模板」分组展示「当前：Emby」之类文案。
 * - [forceType]：是否强制指定目录类型（派生自 [SettingsRepository.forceType]：非 null 且非 `auto` 视为开启）。
 * 
 * 导航跳转子设置页通过一次性 [events] 事件驱动，与 [BackupViewModel] 的 SAF 事件模式一致，
 * 解耦 ViewModel 与 NavigationController。打开浏览器申请 API Key 由 [openTmdbApiKeyApply]
 * 直接发起 ACTION_VIEW Intent（需 Context，已注入）。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000E\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0002\u0008\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0002\u0008\u000B\u0008\u0007\u0012\u0001\u0000\u0018\u0000 -:\u0001-B\u0017\u0008\u0007\u0012\u0006\u0008\u0001\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\n\u0010\"2\u0004\u0010$(\u00038\u000CJ\n\u0010%2\u0004\u0010&(\u00038\u000CJ\n\u0010'2\u0004\u0010((\u00058\u000CJ\u0004\u0010)8\u000CJ\u0004\u0010*8\u000CJ\u0004\u0010+8\u000CJ\u0004\u0010,8\u000CR\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0008H\u0004\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000B\u0010\u000CR\u000F\u0010\rH\u0006\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000F\u0010\u000CR\u000F\u0010\u0010H\u0004\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0011\u0010\u000CR\u000F\u0010\u0012H\u0008\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0015\u0010\u0016R\u000F\u0010\u0017H\u0004\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0018\u0010\u000CR\u000F\u0010\u0019H\u0006\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001A\u0010\u000CR\u000C\u0010\u001BH\nX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u001EH\u000B\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008 \u0010!\u00F2\u0001P\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\n\n\u0006\u0012\u0002\u0018\u00030\t\n\u00020\u000E\n\u0006\u0012\u0002\u0018\u00050\t\n\n\u0012\u0002\u0018\u0003\u0012\u0002\u0018\u00030\u0014\n\u0006\u0012\u0002\u0018\u00070\u0013\n\u00020\u001D\n\u0006\u0012\u0002\u0018\t0\u001C\n\u0006\u0012\u0002\u0018\t0\u001F\n\u00020#\u00A8\u0006."}, d2 = {"Lcom/webdavrenamer/ui/settings/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "<init>", "(Landroid/content/Context;Lcom/webdavrenamer/data/prefs/SettingsRepository;)V", "apiKey", "Lkotlinx/coroutines/flow/StateFlow;", "", "getApiKey", "()Lkotlinx/coroutines/flow/StateFlow;", "apiKeyValid", "", "getApiKeyValid", "language", "getLanguage", "availableLanguages", "", "Lkotlin/Pair;", "getAvailableLanguages", "()Ljava/util/List;", "presetId", "getPresetId", "forceType", "getForceType", "_events", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/webdavrenamer/ui/settings/SettingsNavEvent;", "events", "Lkotlinx/coroutines/flow/SharedFlow;", "getEvents", "()Lkotlinx/coroutines/flow/SharedFlow;", "setApiKey", "", "value", "setLanguage", "code", "setForceType", "enabled", "openTemplateEditor", "openBackup", "openHostsSettings", "openTmdbApiKeyApply", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    /**
     * TMDB API Key；未设置返回空串。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> apiKey = null;

    /**
     * API Key 是否有效：非空且长度 ≥ 32（TMDB v3 Key 固定 32 位）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> apiKeyValid = null;

    /**
     * TMDB 请求语言偏好，默认简体中文。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> language = null;

    /**
     * 可选语言列表（语言码 → 显示名），供下拉选择。
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> availableLanguages = null;

    /**
     * 当前命名预设 ID（PLEX/KODI/EMBY/JELLYFIN/CUSTOM）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> presetId = null;

    /**
     * 是否强制指定目录类型（仓库值为 null/auto 表示自动识别）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> forceType = null;

    /**
     * 一次性导航事件，由 Composable 收集后调用对应导航回调。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.webdavrenamer.ui.settings.SettingsNavEvent> _events = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<com.webdavrenamer.ui.settings.SettingsNavEvent> events = null;

    @org.jetbrains.annotations.NotNull()
    private static final com.webdavrenamer.ui.settings.SettingsViewModel.Companion Companion = null;

    public static final int TMDB_API_KEY_LENGTH = 32;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT_LANGUAGE = "zh-CN";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT_PRESET = "PLEX";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FORCE_TYPE_AUTO = "auto";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FORCE_TYPE_MOVIE = "movie";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TMDB_API_APPLY_URL = "https://www.themoviedb.org/settings/api";

    @javax.inject.Inject()
    public SettingsViewModel(@dagger.hilt.android.qualifiers.ApplicationContext() @org.jetbrains.annotations.NotNull() android.content.Context context, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings) {
        super();
    }

    /**
     * TMDB API Key；未设置返回空串。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getApiKey() {
        return null;
    }

    /**
     * API Key 是否有效：非空且长度 ≥ 32（TMDB v3 Key 固定 32 位）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getApiKeyValid() {
        return null;
    }

    /**
     * TMDB 请求语言偏好，默认简体中文。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getLanguage() {
        return null;
    }

    /**
     * 可选语言列表（语言码 → 显示名），供下拉选择。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> getAvailableLanguages() {
        return null;
    }

    /**
     * 当前命名预设 ID（PLEX/KODI/EMBY/JELLYFIN/CUSTOM）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getPresetId() {
        return null;
    }

    /**
     * 是否强制指定目录类型（仓库值为 null/auto 表示自动识别）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getForceType() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<com.webdavrenamer.ui.settings.SettingsNavEvent> getEvents() {
        return null;
    }

    /**
     * 保存 API Key 到 DataStore。
     */
    public final void setApiKey(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    /**
     * 保存语言偏好到 DataStore。
     */
    public final void setLanguage(@org.jetbrains.annotations.NotNull() java.lang.String code) {
    }

    /**
     * 开关「强制指定目录类型」。
     * 
     * 开启时保留已有非 auto 的强制值（movie/tv），否则默认设为电影类型；
     * 关闭时清除（恢复自动识别）。本页不提供 movie/tv 选择器，类型细化由匹配流程负责。
     */
    public final void setForceType(boolean enabled) {
    }

    /**
     * 触发跳转模板编辑器事件。
     */
    public final void openTemplateEditor() {
    }

    /**
     * 触发跳转备份与恢复事件。
     */
    public final void openBackup() {
    }

    /**
     * 触发跳转 Hosts 设置事件。
     */
    public final void openHostsSettings() {
    }

    /**
     * 打开系统浏览器跳转 TMDB API 申请页。
     */
    public final void openTmdbApiKeyApply() {
    }

    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u001A\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0005\u0008\u0082\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086TR\u0007\u0010\u0006H\u0002X\u0086TR\u0007\u0010\u0008H\u0002X\u0086TR\u0007\u0010\tH\u0002X\u0086TR\u0007\u0010\nH\u0002X\u0086TR\u0007\u0010\u000BH\u0002X\u0086T\u00F2\u0001\u000C\n\u00020\u0001\n\u00020\u0005\n\u00020\u0007\u00A8\u0006\u000C"}, d2 = {"Lcom/webdavrenamer/ui/settings/SettingsViewModel$Companion;", "", "<init>", "()V", "TMDB_API_KEY_LENGTH", "", "DEFAULT_LANGUAGE", "", "DEFAULT_PRESET", "FORCE_TYPE_AUTO", "FORCE_TYPE_MOVIE", "TMDB_API_APPLY_URL", "app_debug"}, xs= "", pn = "", xi = 48)
    private static final class Companion {

        private Companion() {
            super();
        }
    }
}
