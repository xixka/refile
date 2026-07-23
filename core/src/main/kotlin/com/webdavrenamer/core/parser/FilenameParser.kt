package com.webdavrenamer.core.parser

import com.webdavrenamer.core.model.MediaType

/**
 * 文件名解析引擎（计划 §5.3）。
 *
 * 纯 Kotlin 无 Android 依赖，便于 JVM 单元测试。
 * 规则表驱动：季集模式 → 技术标签 → 清洗。
 *
 * 实现要点：
 * 1. 去扩展名；将 `.`、`_` 替换为空格（保留年份/小数判断）。
 * 2. 剔除方括号/圆括号内发布信息（分辨率/编码/组名/站点名），保留含年份圆括号。
 * 3. 剔除连续技术标签串尾巴（`720p BluRay x264 AAC-Group` 这种）。
 * 4. 标题首尾去空格、合并连续空格。
 */
class FilenameParser {

    /** 视频扩展名（不区分大小写），见 §5.2。iso 默认不参与重命名但可解析。 */
    val videoExtensions: Set<String> = setOf(
        "mkv", "mp4", "m4v", "avi", "mov", "wmv", "flv", "ts", "m2ts", "webm", "mpg", "mpeg", "rmvb", "iso",
    )

    /** 字幕扩展名（伴随文件）。 */
    val subtitleExtensions: Set<String> = setOf("srt", "ass", "ssa", "sub", "idx")

    /** 海报/nfo 等伴随文件扩展名。 */
    val companionExtensions: Set<String> = setOf("nfo", "jpg", "jpeg", "png", "tbn", "bnr")

    fun parse(fileName: String): ParsedFilename {
        val raw = fileName.trim()
        // 1. 去扩展名
        val (baseName, _) = splitExtension(raw)
        // 2. 取出方括号/圆括号内的发布信息，但保留含年份的圆括号
        val tokens = extractBracketed(baseName)
        // 3. 统一分隔符：. _ -> 空格
        val spaced = normalizeSeparators(tokens.cleaned)
        // 4. 解析季集（日期型在归一化前的原文上检测，因日期分隔符为 . _ -）
        val seasonEpisode = parseSeasonEpisode(spaced, tokens.brackets, baseName)
        // 5. 解析技术标签
        val tech = parseTech(baseName)
        // 6. 解析年份
        val year = parseYear(spaced)
        // 7. 解析分片序号
        val part = parsePart(spaced)
        // 8. 剔除技术标签尾巴得到标题候选
        val title = cleanTitle(spaced)
        // 9. 推断媒体类型
        val mediaType = if (seasonEpisode.hasSeasonOrEpisode()) MediaType.EPISODE else MediaType.MOVIE

        return ParsedFilename(
            title = title?.takeIf { it.isNotBlank() },
            year = year,
            season = seasonEpisode.season,
            episodes = seasonEpisode.episodes,
            resolution = tech.resolution,
            source = tech.source,
            videoCodec = tech.videoCodec,
            audioCodec = tech.audioCodec,
            group = tech.group ?: tokens.group,
            partIndex = part,
            isDailyShow = seasonEpisode.daily,
            mediaType = mediaType,
        )
    }

    // ---- 扩展名 ----
    fun splitExtension(name: String): Pair<String, String> {
        val lastDot = name.lastIndexOf('.')
        if (lastDot <= 0) return name to ""
        val ext = name.substring(lastDot + 1).lowercase()
        if (ext.length in 1..5 && ext.none { it.isWhitespace() }) {
            return name.substring(0, lastDot) to ext
        }
        return name to ""
    }

    // ---- 方括号/圆括号 ----
    private data class BracketResult(val cleaned: String, val brackets: List<String>, val group: String?)

    private fun extractBracketed(input: String): BracketResult {
        val brackets = mutableListOf<String>()
        var group: String? = null
        val sb = StringBuilder()
        var i = 0
        while (i < input.length) {
            val c = input[i]
            if (c == '[' || c == '(') {
                val close = if (c == '[') ']' else ')'
                val end = input.indexOf(close, i + 1)
                if (end != -1) {
                    val inner = input.substring(i + 1, end)
                    brackets.add(inner)
                    val isYearParen = c == '(' && YEAR_IN_PARENS.containsMatchIn(inner)
                    if (isYearParen) {
                        sb.append(' ').append(inner).append(' ')
                    } else {
                        if (c == '[' && GROUP_TOKEN.matches(inner) && group == null) {
                            group = inner
                        }
                    }
                    i = end + 1
                    continue
                }
            }
            sb.append(c)
            i++
        }
        return BracketResult(sb.toString(), brackets, group)
    }

    // ---- 分隔符归一 ----
    private fun normalizeSeparators(input: String): String =
        input.replace('_', ' ').replace('.', ' ')

    // ---- 季集解析 ----
    private data class SeasonEpisodeResult(val season: Int?, val episodes: List<Int>, val daily: Boolean) {
        fun hasSeasonOrEpisode() = season != null || episodes.isNotEmpty() || daily
    }

    private fun parseSeasonEpisode(spaced: String, brackets: List<String>, rawBase: String): SeasonEpisodeResult {
        // S01E01E02 / S01E01-E03 / s1e2
        SEASON_EPISODE.find(spaced)?.let { m ->
            val season = m.groupValues[1].toInt()
            val episodes = parseEpisodeList(m.groupValues[2])
            if (episodes.isNotEmpty()) return SeasonEpisodeResult(season, episodes, false)
        }
        // 1x02
        NX_N.find(spaced)?.let { m ->
            return SeasonEpisodeResult(m.groupValues[1].toInt(), listOf(m.groupValues[2].toInt()), false)
        }
        // 第X季第X集
        CHINESE_SEASON_EP.find(spaced)?.let { m ->
            return SeasonEpisodeResult(m.groupValues[1].toIntOrNull(), listOf(m.groupValues[2].toInt()), false)
        }
        // 第X集
        CHINESE_EP_ONLY.find(spaced)?.let { m ->
            return SeasonEpisodeResult(null, listOf(m.groupValues[1].toInt()), false)
        }
        // 独立集号 E02 / EP02（需 E/EP 前缀，避免误判 Apollo 13 / 2012）
        STANDALONE_EP.find(spaced)?.let { m ->
            return SeasonEpisodeResult(null, listOf(m.groupValues[1].toInt()), false)
        }
        // [02] 方括号形式：括号内纯 1-3 位数字视为集号
        if (brackets.any { BRACKET_EP.matches(it) }) {
            val ep = brackets.first { BRACKET_EP.matches(it) }.toInt()
            return SeasonEpisodeResult(null, listOf(ep), false)
        }
        // 日期型剧集 2024.01.15 / 2024-01-15（在归一化前的原文上检测）
        if (DAILY_SHOW.containsMatchIn(rawBase)) {
            return SeasonEpisodeResult(null, emptyList(), daily = true)
        }
        return SeasonEpisodeResult(null, emptyList(), false)
    }

    private fun parseEpisodeList(raw: String): List<Int> {
        // raw 如 "01E02E03"、"01-E03"、"01"
        if ('-' in raw) {
            val nums = EPISODE_NUM.findAll(raw).map { it.value.toInt() }.toList()
            if (nums.size >= 2) {
                val start = nums.first()
                val end = nums.last()
                if (end >= start) return (start..end).toList()
            }
        }
        return EPISODE_NUM.findAll(raw).map { it.value.toInt() }.toList().distinct().sorted()
    }

    // ---- 技术标签 ----
    private data class TechResult(
        val resolution: String?,
        val source: String?,
        val videoCodec: String?,
        val audioCodec: String?,
        val group: String?,
    )

    private fun parseTech(input: String): TechResult {
        val resolution = RESOLUTION.find(input)?.value?.lowercase()
        val source = findSource(input)
        val videoCodec = VIDEO_CODEC.find(input)?.value?.lowercase()
        val audioCodec = AUDIO_CODEC.find(input)?.value?.lowercase()
        val group = GROUP_SUFFIX.find(input)?.groupValues?.get(1)
        return TechResult(resolution, source, videoCodec, audioCodec, group)
    }

    private fun findSource(input: String): String? {
        for (match in SOURCE_TOKEN.findAll(input)) {
            val token = match.value.lowercase()
            return when {
                token.startsWith("blu-ray") || token.startsWith("bluray") || token.startsWith("bdrip") ||
                    token.startsWith("bd25") || token.startsWith("bd50") || token.startsWith("brrip") -> "BluRay"
                token.startsWith("web-dl") || token == "webdl" || token == "web" -> "WEB-DL"
                token.startsWith("webrip") -> "WEBRip"
                token.startsWith("hdtv") -> "HDTV"
                token.startsWith("dvdrip") || token == "dvd" -> "DVDRip"
                token.startsWith("remux") -> "Remux"
                else -> token
            }
        }
        return null
    }

    // ---- 年份 ----
    private fun parseYear(input: String): Int? {
        YEAR.find(input)?.let { m ->
            val y = m.value.toInt()
            if (y in 1900..2099) return y
        }
        return null
    }

    // ---- 分片序号 ----
    private fun parsePart(input: String): Int? {
        PART.find(input)?.let { return it.groupValues[1].toInt() }
        return null
    }

    // ---- 标题清洗 ----
    private fun cleanTitle(spaced: String): String? {
        var t = spaced
        t = SEASON_EPISODE.replace(t, " ")
        t = NX_N.replace(t, " ")
        t = CHINESE_SEASON_EP.replace(t, " ")
        t = CHINESE_EP_ONLY.replace(t, " ")
        t = STANDALONE_EP.replace(t, " ")
        t = DAILY_SHOW.replace(t, " ")
        // 剔除技术标签串尾巴：从首个技术标签到结尾
        TECH_TAIL.find(t)?.let { techMatch ->
            t = t.substring(0, techMatch.range.first).trimEnd()
        }
        t = YEAR.replace(t, " ")
        t = PART.replace(t, " ")
        t = GROUP_SUFFIX.replace(t, " ")
        t = t.replace(Regex("\\s+"), " ").trim()
        return t.ifBlank { null }
    }

    companion object {
        // S01E01E02 / S01E01-E03 / s1e2 — group2 捕获集号串（支持 E 与 - 分隔，含混合 01-E03）
        private val SEASON_EPISODE = Regex("(?i)S(\\d{1,2})E(\\d{1,3}(?:[-]?E?\\d{1,3})*)")
        private val EPISODE_NUM = Regex("\\d{1,3}")
        private val NX_N = Regex("(?i)(?<!\\d)(\\d{1,2})x(\\d{1,3})(?!\\d)")
        private val CHINESE_SEASON_EP = Regex("第(\\d{1,2})季第(\\d{1,3})集")
        private val CHINESE_EP_ONLY = Regex("第(\\d{1,3})集")
        // E02 / EP02（必须 E/EP 前缀）
        private val STANDALONE_EP = Regex("(?i)(?<![A-Za-z])EP?(\\d{1,3})(?!\\d)")
        private val BRACKET_EP = Regex("^\\d{1,3}$")
        private val DAILY_SHOW = Regex("(?<!\\d)(19|20)\\d{2}[._-](0?[1-9]|1[0-2])[._-]([0-2]?[0-9]|3[01])(?!\\d)")
        private val YEAR = Regex("(?<!\\d)(19\\d{2}|20\\d{2})(?!\\d)")
        private val YEAR_IN_PARENS = Regex("(?i)(19\\d{2}|20\\d{2})")
        private val RESOLUTION = Regex("(?i)\\b(2160p|1080p|720p|540p|480p|360p|4320p|4K|8K)\\b")
        private val SOURCE_TOKEN = Regex("(?i)\\b(Blu-?Ray|BDRip|BD25|BD50|BRRip|WEB-?DL|WEBDL|WEBRip|WEB|HDTV|DVDRip|DVD|R5|CAM|REMUX|HD-?TS|HD-?TC|PDVD)\\b")
        private val VIDEO_CODEC = Regex("(?i)\\b(x264|x265|h264|h265|hevc|av1|vp9|divx|xvid|mpeg-?2|mpeg-?4|vc1)\\b")
        private val AUDIO_CODEC = Regex("(?i)\\b(AAC|AC3|EAC3|DDP|DDPA|DD|DTS|DTS-?HD|DTS-?MA|TrueHD|Atmos|FLAC|MP3|PCM|Opus)\\b")
        private val GROUP_TOKEN = Regex("^[A-Za-z0-9]{2,}$")
        private val GROUP_SUFFIX = Regex("(?i)[\\-\\.]([A-Za-z0-9]{2,})$")
        private val PART = Regex("(?i)(?:^|\\s)(?:CD|DISC|PART|PT)\\s?(\\d{1,2})(?:$|\\s)")
        // 技术标签尾巴：匹配从首个技术标签起的位置
        private val TECH_TAIL = Regex("(?i)\\b(2160p|1080p|720p|480p|540p|Blu-?Ray|WEB-?DL|WEBRip|HDTV|DVDRip|REMUX|x264|x265|h264|h265|hevc|av1|AAC|AC3|DTS|TrueHD|Atmos|FLAC)")
    }
}
