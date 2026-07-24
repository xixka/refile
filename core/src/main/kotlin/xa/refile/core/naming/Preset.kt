package xa.refile.core.naming

/**
 * 内置命名预设（计划 §5.5 内置预设表）。
 * 路径相对于用户选择的库根目录。
 */
enum class Preset(val displayName: String, val movieTemplate: String, val episodeTemplate: String) {
    PLEX(
        "Plex（默认）",
        "Movies/{n} ({y})/{n} ({y})",
        "TV Shows/{n} ({y})/Season {s00}/{n} - {s00e00} - {t}",
    ),
    KODI(
        "Kodi",
        "Movies/{n} ({y})/{n} ({y})",
        "TV Shows/{n}/Season {s00}/{n} {s00e00} {t}",
    ),
    EMBY(
        "Emby",
        "Movies/{n} ({y})/{n} ({y})",
        "TV Shows/{n}/Season {s00}/{n} - S{s00}E{e00} - {t}",
    ),
    JELLYFIN(
        "Jellyfin",
        "Movies/{n} ({y})/{n} ({y})",
        "TV Shows/{n}/Season {s00}/{n} - S{s00}E{e00} - {t}",
    ),
    ;

    companion object {
        /** 默认预设。 */
        val DEFAULT: Preset = PLEX

        /** 按 ID 查找。 */
        fun byId(id: String): Preset = entries.firstOrNull { it.name.equals(id, true) } ?: DEFAULT
    }
}

/**
 * 自定义模板（用户「另存为」）。
 */
data class CustomTemplate(
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
