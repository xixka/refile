package com.webdavrenamer.data.prefs;

/**
 * 应用级设置仓库（计划 §M2 / Task 2.4 依赖）。
 * 
 * 基于 Preferences DataStore 持久化：
 * - [apiKey]：TMDB API Key（敏感，不进日志）。
 * - [language]：TMDB 请求语言，默认 `zh-CN`。
 * - [presetId]：命名预设，默认 `PLEX`。
 * - [forceType]：强制目录类型（`null`/`auto`/`movie`/`tv`）。
 * - [templateString]：用户自定义模板字符串（Task 3.3 模板编辑器）。
 * - [visualOptions]：命名可视化选项（分隔符/大小写/非法字符处理/补零位数，Task 3.3）。
 * - [hostsConfig]：自定义 Hosts 配置（开关 + 域名→IP 条目，Task 5.3.5），以 JSON 字符串持久化。
 * 
 * 用 `@Inject constructor` + `@Singleton`，Hilt 直接构造，无需 @Provides。
 * DataStore 通过顶层 [Context.dataStore] 扩展按进程单例创建。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000E\n\u0002\u0008\u000B\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u0002\n\u0002\u0008\u000C\u0008\u0007\u0012\u0001\u0000\u0018\u0000 %:\u0001%B\u0011\u0008\u0007\u0012\u0006\u0008\u0001\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0012\u0010\u00192\u0004\u0010\u001B(\u00028\nH\u0086@\u00A2\u0006\u0002\u0010\u001CJ\u0012\u0010\u001D2\u0004\u0010\u001B(\u00028\nH\u0086@\u00A2\u0006\u0002\u0010\u001CJ\u0012\u0010\u001E2\u0004\u0010\u001B(\u00028\nH\u0086@\u00A2\u0006\u0002\u0010\u001CJ\u0012\u0010\u001F2\u0004\u0010\u001B(\u00048\nH\u0086@\u00A2\u0006\u0002\u0010\u001CJ\u0012\u0010 2\u0004\u0010\u001B(\u00028\nH\u0086@\u00A2\u0006\u0002\u0010\u001CJ\u0012\u0010!2\u0004\u0010\u001B(\u00068\nH\u0086@\u00A2\u0006\u0002\u0010\"J\u0012\u0010#2\u0004\u0010\u001B(\u00088\nH\u0086@\u00A2\u0006\u0002\u0010$R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0006H\u0003\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\t\u0010\nR\u000F\u0010\u000BH\u0003\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000C\u0010\nR\u000F\u0010\rH\u0003\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000E\u0010\nR\u000F\u0010\u000FH\u0005\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0010\u0010\nR\u000F\u0010\u0011H\u0003\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0012\u0010\nR\u000F\u0010\u0013H\u0007\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0015\u0010\nR\u000F\u0010\u0016H\t\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0018\u0010\n\u00F2\u0001>\n\u00020\u0001\n\u00020\u0003\n\u00020\u0008\n\u0006\u0012\u0002\u0018\u00020\u0007\n\u0004\u0018\u00010\u0008\n\u0006\u0012\u0002\u0018\u00040\u0007\n\u00020\u0014\n\u0006\u0012\u0002\u0018\u00060\u0007\n\u00020\u0017\n\u0006\u0012\u0002\u0018\u00080\u0007\n\u00020\u001A\u00A8\u0006&"}, d2 = {"Lcom/webdavrenamer/data/prefs/SettingsRepository;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "apiKey", "Lkotlinx/coroutines/flow/Flow;", "", "getApiKey", "()Lkotlinx/coroutines/flow/Flow;", "language", "getLanguage", "presetId", "getPresetId", "forceType", "getForceType", "templateString", "getTemplateString", "visualOptions", "Lcom/webdavrenamer/data/prefs/VisualOptions;", "getVisualOptions", "hostsConfig", "Lcom/webdavrenamer/core/backup/HostsConfig;", "getHostsConfig", "setApiKey", "", "value", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLanguage", "setPresetId", "setForceType", "setTemplateString", "setVisualOptions", "(Lcom/webdavrenamer/data/prefs/VisualOptions;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setHostsConfig", "(Lcom/webdavrenamer/core/backup/HostsConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@javax.inject.Singleton()
public final class SettingsRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;

    /**
     * TMDB API Key；未设置返回空串。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.String> apiKey = null;

    /**
     * TMDB 请求语言，默认简体中文。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.String> language = null;

    /**
     * 命名预设 ID，默认 PLEX。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.String> presetId = null;

    /**
     * 强制目录类型；null/auto 表示自动识别。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.String> forceType = null;

    /**
     * 用户自定义模板字符串；空串表示尚未设置（由调用方回退到预设）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.String> templateString = null;

    /**
     * 命名可视化选项（分隔符/大小写/非法字符处理/补零位数）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.webdavrenamer.data.prefs.VisualOptions> visualOptions = null;

    /**
     * 自定义 Hosts 配置（Task 5.3.5）。
     * 
     * 以 JSON 字符串持久化；未设置或反序列化失败时返回默认 [HostsConfig]（enabled=true, 空 entries）。
     * 备份导出/导入由 Task 5.2 通用备份机制处理，此处只负责持久化与读取。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.webdavrenamer.core.backup.HostsConfig> hostsConfig = null;

    @org.jetbrains.annotations.NotNull()
    private static final com.webdavrenamer.data.prefs.SettingsRepository.Companion Companion = null;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT_LANGUAGE = "zh-CN";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT_PRESET = "PLEX";

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_API_KEY = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_LANGUAGE = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_PRESET_ID = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_FORCE_TYPE = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_TEMPLATE_STRING = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_SEPARATOR = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_CASE_MODE = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_ILLEGAL = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Integer> KEY_PAD_DIGITS = null;

    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_HOSTS_CONFIG = null;

    /**
     * Hosts 配置 JSON 实例（容错：未知字段忽略，便于备份文件向前兼容）。
     */
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.serialization.json.Json hostsJson = null;

    @javax.inject.Inject()
    public SettingsRepository(@dagger.hilt.android.qualifiers.ApplicationContext() @org.jetbrains.annotations.NotNull() android.content.Context context) {
        super();
    }

    /**
     * TMDB API Key；未设置返回空串。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getApiKey() {
        return null;
    }

    /**
     * TMDB 请求语言，默认简体中文。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getLanguage() {
        return null;
    }

    /**
     * 命名预设 ID，默认 PLEX。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getPresetId() {
        return null;
    }

    /**
     * 强制目录类型；null/auto 表示自动识别。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getForceType() {
        return null;
    }

    /**
     * 用户自定义模板字符串；空串表示尚未设置（由调用方回退到预设）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getTemplateString() {
        return null;
    }

    /**
     * 命名可视化选项（分隔符/大小写/非法字符处理/补零位数）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.webdavrenamer.data.prefs.VisualOptions> getVisualOptions() {
        return null;
    }

    /**
     * 自定义 Hosts 配置（Task 5.3.5）。
     * 
     * 以 JSON 字符串持久化；未设置或反序列化失败时返回默认 [HostsConfig]（enabled=true, 空 entries）。
     * 备份导出/导入由 Task 5.2 通用备份机制处理，此处只负责持久化与读取。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.webdavrenamer.core.backup.HostsConfig> getHostsConfig() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setApiKey(@org.jetbrains.annotations.NotNull() java.lang.String value, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setLanguage(@org.jetbrains.annotations.NotNull() java.lang.String value, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setPresetId(@org.jetbrains.annotations.NotNull() java.lang.String value, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 传 null 清除（恢复自动识别）。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setForceType(@org.jetbrains.annotations.Nullable() java.lang.String value, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 保存模板字符串。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setTemplateString(@org.jetbrains.annotations.NotNull() java.lang.String value, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 保存可视化选项（分隔符以单字符字符串存储）。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setVisualOptions(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.VisualOptions value, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 保存自定义 Hosts 配置（序列化为 JSON 字符串存储）。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setHostsConfig(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.backup.HostsConfig value, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0008\n\u0002\u0010\u0008\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\u0008\u0082\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086TR\u0007\u0010\u0006H\u0001X\u0086TR\u0007\u0010\u0007H\u0002X\u0082\u0004R\u0007\u0010\tH\u0002X\u0082\u0004R\u0007\u0010\nH\u0002X\u0082\u0004R\u0007\u0010\u000BH\u0002X\u0082\u0004R\u0007\u0010\u000CH\u0002X\u0082\u0004R\u0007\u0010\rH\u0002X\u0082\u0004R\u0007\u0010\u000EH\u0002X\u0082\u0004R\u0007\u0010\u000FH\u0002X\u0082\u0004R\u0007\u0010\u0010H\u0004X\u0082\u0004R\u0007\u0010\u0012H\u0002X\u0082\u0004R\u0007\u0010\u0013H\u0005X\u0082\u0004\u00F2\u0001 \n\u00020\u0001\n\u00020\u0005\n\u0006\u0012\u0002\u0018\u00010\u0008\n\u00020\u0011\n\u0006\u0012\u0002\u0018\u00030\u0008\n\u00020\u0014\u00A8\u0006\u0015"}, d2 = {"Lcom/webdavrenamer/data/prefs/SettingsRepository$Companion;", "", "<init>", "()V", "DEFAULT_LANGUAGE", "", "DEFAULT_PRESET", "KEY_API_KEY", "Landroidx/datastore/preferences/core/Preferences$Key;", "KEY_LANGUAGE", "KEY_PRESET_ID", "KEY_FORCE_TYPE", "KEY_TEMPLATE_STRING", "KEY_SEPARATOR", "KEY_CASE_MODE", "KEY_ILLEGAL", "KEY_PAD_DIGITS", "", "KEY_HOSTS_CONFIG", "hostsJson", "Lkotlinx/serialization/json/Json;", "app_debug"}, xs= "", pn = "", xi = 48)
    private static final class Companion {

        private Companion() {
            super();
        }
    }
}
