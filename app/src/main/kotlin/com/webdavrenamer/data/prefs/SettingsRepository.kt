package com.webdavrenamer.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.webdavrenamer.core.backup.HostsConfig
import com.webdavrenamer.core.naming.NamingOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

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
@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    /** TMDB API Key；未设置返回空串。 */
    val apiKey: Flow<String> = context.dataStore.data.map { it[KEY_API_KEY] ?: "" }

    /** TMDB 请求语言，默认简体中文。 */
    val language: Flow<String> = context.dataStore.data.map { it[KEY_LANGUAGE] ?: DEFAULT_LANGUAGE }

    /** 命名预设 ID，默认 PLEX。 */
    val presetId: Flow<String> = context.dataStore.data.map { it[KEY_PRESET_ID] ?: DEFAULT_PRESET }

    /** 强制目录类型；null/auto 表示自动识别。 */
    val forceType: Flow<String?> = context.dataStore.data.map { it[KEY_FORCE_TYPE] }

    /** 用户自定义模板字符串；空串表示尚未设置（由调用方回退到预设）。 */
    val templateString: Flow<String> = context.dataStore.data.map { it[KEY_TEMPLATE_STRING] ?: "" }

    /** 命名可视化选项（分隔符/大小写/非法字符处理/补零位数）。 */
    val visualOptions: Flow<VisualOptions> = context.dataStore.data.map { prefs ->
        VisualOptions(
            separator = prefs[KEY_SEPARATOR]?.firstOrNull() ?: ' ',
            caseMode = prefs[KEY_CASE_MODE]
                ?.let { runCatching { NamingOptions.Casing.valueOf(it) }.getOrNull() }
                ?: NamingOptions.Casing.AS_IS,
            illegalCharHandling = prefs[KEY_ILLEGAL]
                ?.let { runCatching { NamingOptions.IllegalCharHandling.valueOf(it) }.getOrNull() }
                ?: NamingOptions.IllegalCharHandling.REPLACE_DASH,
            padDigits = prefs[KEY_PAD_DIGITS] ?: 2,
        )
    }

    /**
     * 自定义 Hosts 配置（Task 5.3.5）。
     *
     * 以 JSON 字符串持久化；未设置或反序列化失败时返回默认 [HostsConfig]（enabled=true, 空 entries）。
     * 备份导出/导入由 Task 5.2 通用备份机制处理，此处只负责持久化与读取。
     */
    val hostsConfig: Flow<HostsConfig> = context.dataStore.data.map { prefs ->
        prefs[KEY_HOSTS_CONFIG]?.let { json ->
            runCatching { hostsJson.decodeFromString<HostsConfig>(json) }.getOrNull()
        } ?: HostsConfig()
    }

    suspend fun setApiKey(value: String) {
        context.dataStore.edit { it[KEY_API_KEY] = value }
    }

    suspend fun setLanguage(value: String) {
        context.dataStore.edit { it[KEY_LANGUAGE] = value }
    }

    suspend fun setPresetId(value: String) {
        context.dataStore.edit { it[KEY_PRESET_ID] = value }
    }

    /** 传 null 清除（恢复自动识别）。 */
    suspend fun setForceType(value: String?) {
        context.dataStore.edit {
            if (value.isNullOrBlank()) it.remove(KEY_FORCE_TYPE) else it[KEY_FORCE_TYPE] = value
        }
    }

    /** 保存模板字符串。 */
    suspend fun setTemplateString(value: String) {
        context.dataStore.edit { it[KEY_TEMPLATE_STRING] = value }
    }

    /** 保存可视化选项（分隔符以单字符字符串存储）。 */
    suspend fun setVisualOptions(value: VisualOptions) {
        context.dataStore.edit {
            it[KEY_SEPARATOR] = value.separator.toString()
            it[KEY_CASE_MODE] = value.caseMode.name
            it[KEY_ILLEGAL] = value.illegalCharHandling.name
            it[KEY_PAD_DIGITS] = value.padDigits
        }
    }

    /** 保存自定义 Hosts 配置（序列化为 JSON 字符串存储）。 */
    suspend fun setHostsConfig(value: HostsConfig) {
        val json = hostsJson.encodeToString(HostsConfig.serializer(), value)
        context.dataStore.edit { it[KEY_HOSTS_CONFIG] = json }
    }

    private companion object {
        const val DEFAULT_LANGUAGE = "zh-CN"
        const val DEFAULT_PRESET = "PLEX"
        private val KEY_API_KEY = stringPreferencesKey("api_key")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_PRESET_ID = stringPreferencesKey("preset_id")
        private val KEY_FORCE_TYPE = stringPreferencesKey("force_type")
        private val KEY_TEMPLATE_STRING = stringPreferencesKey("template_string")
        private val KEY_SEPARATOR = stringPreferencesKey("visual_separator")
        private val KEY_CASE_MODE = stringPreferencesKey("visual_case_mode")
        private val KEY_ILLEGAL = stringPreferencesKey("visual_illegal_char")
        private val KEY_PAD_DIGITS = intPreferencesKey("visual_pad_digits")
        private val KEY_HOSTS_CONFIG = stringPreferencesKey("hosts_config")

        /** Hosts 配置 JSON 实例（容错：未知字段忽略，便于备份文件向前兼容）。 */
        private val hostsJson = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}

/**
 * 命名可视化选项（Task 3.3 模板编辑器）。
 *
 * 与 [com.webdavrenamer.core.naming.NamingOptions] 一一对应，但放在 data 层以便 DataStore 持久化。
 * UI 层通过 [toNamingOptions] 转换后传给 [com.webdavrenamer.core.naming.TemplateEngine]。
 */
data class VisualOptions(
    val separator: Char = ' ',
    val caseMode: NamingOptions.Casing = NamingOptions.Casing.AS_IS,
    val illegalCharHandling: NamingOptions.IllegalCharHandling =
        NamingOptions.IllegalCharHandling.REPLACE_DASH,
    val padDigits: Int = 2,
) {
    /** 转为 core 层 [NamingOptions] 供模板引擎使用。 */
    fun toNamingOptions(): NamingOptions = NamingOptions(
        wordSeparator = separator,
        casing = caseMode,
        illegalCharHandling = illegalCharHandling,
        padLength = padDigits,
    )
}

/** 进程级 Preferences DataStore 单例（名称 "settings"）。 */
private val Context.dataStore by preferencesDataStore("settings")
