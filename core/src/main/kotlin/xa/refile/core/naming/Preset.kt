package xa.refile.core.naming

import kotlinx.serialization.Serializable

/**
 * 内置命名预设（计划 §5.5 内置预设表）。
 * 路径相对于用户选择的库根目录。
 *
 * 按测试反馈 Item 9：默认预设仅保留 Emby 与 Infuse，其余（Plex/Kodi/Jellyfin）移除。
 */
enum class Preset(val displayName: String, val movieTemplate: String, val episodeTemplate: String) {
    EMBY(
        "Emby",
        "Movies/{n} ({y})/{n} ({y})",
        "TV Shows/{n}/Season {s00}/{n} - S{s00}E{e00} - {t}",
    ),
    INFUSE(
        "Infuse",
        "Movies/{n} ({y})/{n} ({y})",
        "TV Shows/{n}/Season {s00}/{n} - {s00e00} - {t}",
    ),
    ;

    companion object {
        /** 默认预设。 */
        val DEFAULT: Preset = EMBY

        /** 按 ID 查找。 */
        fun byId(id: String): Preset = entries.firstOrNull { it.name.equals(id, true) } ?: DEFAULT
    }
}

/**
 * 自定义预设（用户在模板编辑器「另存为预设」，测试反馈 Item 9）。
 *
 * 与内置 [Preset] 区别：可由用户新建/删除，movie/episode 模板独立保存，
 * 用于在模板编辑器顶部快速切换。持久化由 app 层 DataStore 以 JSON 列表存储。
 *
 * @property id 唯一 ID（UUID 或时间戳字面量）。
 * @property name 显示名（用户输入）。
 * @property movieTemplate 电影模板字符串。
 * @property episodeTemplate 剧集模板字符串。
 */
@Serializable
data class CustomPreset(
    val id: String,
    val name: String,
    val movieTemplate: String,
    val episodeTemplate: String,
)

/**
 * 预设与自定义模板仓库（计划 §5.5 + §3.2）。
 * 内置预设为内存常量；自定义模板由调用方持久化（app 层 DataStore/Room）。
 */
class PresetRepository {
    fun builtinPresets(): List<Preset> = Preset.entries.toList()

    fun templateFor(preset: Preset, isEpisode: Boolean): String =
        if (isEpisode) preset.episodeTemplate else preset.movieTemplate
}
