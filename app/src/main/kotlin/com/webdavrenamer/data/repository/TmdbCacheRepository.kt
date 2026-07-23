package com.webdavrenamer.data.repository

import com.webdavrenamer.core.backup.HostsDnsFactory
import com.webdavrenamer.core.model.MediaType
import com.webdavrenamer.core.naming.MediaMetadata
import com.webdavrenamer.core.tmdb.EpisodeGroupDetail
import com.webdavrenamer.core.tmdb.SeasonDetail
import com.webdavrenamer.core.tmdb.TmdbClient
import com.webdavrenamer.data.db.TmdbCacheDao
import com.webdavrenamer.data.db.TmdbCacheEntity
import com.webdavrenamer.data.prefs.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TMDB 响应缓存仓库（Task 2.3.4）。
 *
 * 包装 [TmdbClient]：详情类请求（[getMovie]/[getTv]/[getSeason]/[getEpisodeGroup]）先查 Room
 * 缓存命中且未过期则直接反序列化返回，否则走网络并回写缓存；搜索类请求（[searchMovie]/[searchTv]）
 * 不缓存（结果随用户查询变化、且与详情缓存键维度不同），直接透传。
 *
 * 缓存键：`"{mediaType}:{tmdbId}:{language}[:{season}]"`，TTL 7 天。命中后由本仓库按 `cachedAt`
 * 判定过期，过期视为未命中并覆盖回写。
 *
 * DI：[TmdbClient] 无法作为稳定单例提供（其构造依赖 DataStore 中的 apiKey/hostsConfig，属动态
 * 设置），故本仓库注入 [SettingsRepository] 自行按需构造 [TmdbClient]（与 MatchViewModel 原逻辑
 * 一致：读 hostsConfig → [HostsDnsFactory] → [TmdbClient.create]）。apiKey 为空时抛
 * [IllegalStateException]，由调用方捕获提示用户。
 */
@Singleton
class TmdbCacheRepository @Inject constructor(
    private val dao: TmdbCacheDao,
    private val settings: SettingsRepository,
) {

    /** 缓存有效期：7 天（毫秒）。 */
    private val ttlMillis: Long = 7L * 24L * 60L * 60L * 1000L

    /**
     * 缓存 JSON 实例：容错（未知字段忽略，便于 CachedMediaMetadata 字段演进向前兼容），
     * 写默认值保证空集合/空 map 也能正确往返。
     */
    private val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    /** 电影详情：键 `MOVIE:$tmdbId:$language`。 */
    suspend fun getMovie(tmdbId: Int, language: String): MediaMetadata =
        cached(CACHE_MOVIE, tmdbId, language, seasonNumber = null, key = movieKey(tmdbId, language)) {
            buildTmdbClient().getMovie(tmdbId, language)
        }

    /** 剧集详情：键 `TV:$tmdbId:$language`。 */
    suspend fun getTv(tmdbId: Int, language: String): MediaMetadata =
        cached(CACHE_TV, tmdbId, language, seasonNumber = null, key = tvKey(tmdbId, language)) {
            buildTmdbClient().getTv(tmdbId, language)
        }

    /** 季详情：键 `SEASON:$tvId:$season:$language`。 */
    suspend fun getSeason(tvId: Int, seasonNumber: Int, language: String): SeasonDetail =
        cachedSerializable(
            CACHE_SEASON,
            tmdbId = tvId,
            language = language,
            seasonNumber = seasonNumber,
            key = seasonKey(tvId, seasonNumber, language),
            fetch = { buildTmdbClient().getSeason(tvId, seasonNumber, language) },
            serializer = SeasonDetail.serializer(),
        )

    /** Episode Group 详情：键 `EPISODE_GROUP:$id`（无 language 维度）。 */
    suspend fun getEpisodeGroup(id: Int): EpisodeGroupDetail =
        cachedSerializable(
            CACHE_EPISODE_GROUP,
            tmdbId = id,
            language = "",
            seasonNumber = null,
            key = episodeGroupKey(id),
            fetch = { buildTmdbClient().getEpisodeGroup(id) },
            serializer = EpisodeGroupDetail.serializer(),
        )

    /**
     * 合集补全：直接透传 [TmdbClient.enrichWithCollection]，不缓存。
     *
     * 该方法输入为已fetch的 [MediaMetadata]、输出基于合集端点 + movie 详情重新映射，缓存维度与
     * [getMovie] 重叠且结果含动态 collectionIndex，未列入 Task 2.3.4 详情缓存清单，故不缓存。
     */
    suspend fun enrichWithCollection(movie: MediaMetadata, language: String): MediaMetadata =
        buildTmdbClient().enrichWithCollection(movie, language)

    /** 搜索电影：不缓存，直接透传。 */
    suspend fun searchMovie(
        query: String,
        year: Int? = null,
        language: String = "zh-CN",
    ): List<MediaMetadata> = buildTmdbClient().searchMovie(query, year, language)

    /** 搜索剧集：不缓存，直接透传。 */
    suspend fun searchTv(
        query: String,
        year: Int? = null,
        language: String = "zh-CN",
    ): List<MediaMetadata> = buildTmdbClient().searchTv(query, year, language)

    /** 清空全部 TMDB 缓存（设置页"清除 TMDB 缓存"调用）。 */
    suspend fun clearCache() = dao.clearAll()

    /** 清理已过期缓存（可由设置页或定期 Worker 调用；读时已按 TTL 判定，非必须）。 */
    suspend fun evictExpired() = dao.deleteOlderThan(System.currentTimeMillis() - ttlMillis)

    // ---- 内部：缓存读写 ----

    /**
     * [MediaMetadata] 类缓存通用流程（movie/tv）：查缓存→未过期则反序列化 [CachedMediaMetadata]→
     * 否则网络获取→序列化回写→返回。反序列化失败（缓存损坏/字段演进不兼容）静默回退到网络。
     */
    private suspend fun cached(
        mediaType: String,
        tmdbId: Int,
        language: String,
        seasonNumber: Int?,
        key: String,
        fetch: suspend () -> MediaMetadata,
    ): MediaMetadata {
        val now = System.currentTimeMillis()
        dao.getByKey(key)?.let { existing ->
            if (now - existing.cachedAt < ttlMillis) {
                runCatching {
                    json.decodeFromString(CachedMediaMetadata.serializer(), existing.responseJson)
                }.getOrNull()?.let { return it.toMediaMetadata() }
            }
        }
        val fresh = fetch()
        store(key, mediaType, tmdbId, language, seasonNumber, fresh)
        return fresh
    }

    /**
     * 可直接序列化 DTO（[SeasonDetail]/[EpisodeGroupDetail]）的缓存通用流程。
     */
    private suspend fun <T : Any> cachedSerializable(
        mediaType: String,
        tmdbId: Int,
        language: String,
        seasonNumber: Int?,
        key: String,
        fetch: suspend () -> T,
        serializer: KSerializer<T>,
    ): T {
        val now = System.currentTimeMillis()
        dao.getByKey(key)?.let { existing ->
            if (now - existing.cachedAt < ttlMillis) {
                runCatching { json.decodeFromString(serializer, existing.responseJson) }
                    .getOrNull()?.let { return it }
            }
        }
        val fresh = fetch()
        storeJson(key, mediaType, tmdbId, language, seasonNumber, json.encodeToString(serializer, fresh))
        return fresh
    }

    /** 序列化 [MediaMetadata] 为 [CachedMediaMetadata] JSON 并入库。 */
    private suspend fun store(
        key: String,
        mediaType: String,
        tmdbId: Int,
        language: String,
        seasonNumber: Int?,
        value: MediaMetadata,
    ) {
        val body = json.encodeToString(CachedMediaMetadata.serializer(), value.toCached())
        storeJson(key, mediaType, tmdbId, language, seasonNumber, body)
    }

    private suspend fun storeJson(
        key: String,
        mediaType: String,
        tmdbId: Int,
        language: String,
        seasonNumber: Int?,
        body: String,
    ) {
        dao.insert(
            TmdbCacheEntity(
                cacheKey = key,
                mediaType = mediaType,
                tmdbId = tmdbId,
                language = language,
                seasonNumber = seasonNumber,
                responseJson = body,
                cachedAt = System.currentTimeMillis(),
            ),
        )
    }

    // ---- 内部：TmdbClient 构造（与 MatchViewModel 原逻辑一致） ----

    /** 读 apiKey + hostsConfig 构造 [TmdbClient]；apiKey 为空抛 [IllegalStateException]。 */
    private suspend fun buildTmdbClient(): TmdbClient {
        val apiKey = settings.apiKey.first()
        if (apiKey.isBlank()) throw IllegalStateException("请先在设置中填入 TMDB API Key")
        val hostsConfig = settings.hostsConfig.first()
        val baseClient = HostsDnsFactory.createOkHttpClientWithHosts(hostsConfig)
        return TmdbClient.create(baseClient, apiKey)
    }

    // ---- 内部：缓存键 ----

    private fun movieKey(tmdbId: Int, language: String): String = "$CACHE_MOVIE:$tmdbId:$language"
    private fun tvKey(tmdbId: Int, language: String): String = "$CACHE_TV:$tmdbId:$language"
    private fun seasonKey(tvId: Int, season: Int, language: String): String =
        "$CACHE_SEASON:$tvId:$season:$language"
    private fun episodeGroupKey(id: Int): String = "$CACHE_EPISODE_GROUP:$id"

    private companion object {
        const val CACHE_MOVIE = "MOVIE"
        const val CACHE_TV = "TV"
        const val CACHE_SEASON = "SEASON"
        const val CACHE_EPISODE_GROUP = "EPISODE_GROUP"
    }
}

// ---- MediaMetadata 缓存快照 ----

/**
 * [MediaMetadata] 的可序列化缓存快照（Task 2.3.4）。
 *
 * [MediaMetadata] 本身在 :core 为普通 data class（非 @Serializable，不可改），故在 app 层镜像其全部
 * 字段以支持 Room JSON 缓存。[MediaType] 已 @Serializable，其余字段均为基本类型/集合，可安全序列化。
 * 字段与 [MediaMetadata] 一一对应，通过 [toCached]/[toMediaMetadata] 无损往返。
 */
@Serializable
private data class CachedMediaMetadata(
    val type: MediaType = MediaType.MOVIE,
    val id: Int? = null,
    val tmdbId: Int? = null,
    val imdbId: String? = null,
    val tvdbId: String? = null,
    val name: String? = null,
    val originalName: String? = null,
    val aliases: List<String> = emptyList(),
    val year: Int? = null,
    val releaseDate: String? = null,
    val firstAirDate: String? = null,
    val collectionName: String? = null,
    val collectionId: Int? = null,
    val collectionIndex: Int? = null,
    val collectionYears: List<Int> = emptyList(),
    val genres: List<String> = emptyList(),
    val originalLanguage: String? = null,
    val spokenLanguages: List<String> = emptyList(),
    val originCountries: List<String> = emptyList(),
    val productionCountries: List<String> = emptyList(),
    val runtime: Int? = null,
    val certification: String? = null,
    val rating: Double? = null,
    val votes: Int? = null,
    val director: String? = null,
    val actors: List<String> = emptyList(),
    val numberOfSeasons: Int? = null,
    val seasonNumber: Int? = null,
    val episodeNumbers: List<Int> = emptyList(),
    val episodeTitles: List<String> = emptyList(),
    val episodeAirDates: List<String> = emptyList(),
    val seasonName: String? = null,
    val seasonYears: List<Int> = emptyList(),
    val seasonAbsoluteStarts: List<Int> = emptyList(),
    val special: Int? = null,
    val info: Map<String, String?> = emptyMap(),
    val localize: Map<String, Map<String, String>> = emptyMap(),
    val order: Map<String, Map<String, Int>> = emptyMap(),
)

/** [MediaMetadata] → 缓存快照。 */
private fun MediaMetadata.toCached(): CachedMediaMetadata = CachedMediaMetadata(
    type = type,
    id = id,
    tmdbId = tmdbId,
    imdbId = imdbId,
    tvdbId = tvdbId,
    name = name,
    originalName = originalName,
    aliases = aliases,
    year = year,
    releaseDate = releaseDate,
    firstAirDate = firstAirDate,
    collectionName = collectionName,
    collectionId = collectionId,
    collectionIndex = collectionIndex,
    collectionYears = collectionYears,
    genres = genres,
    originalLanguage = originalLanguage,
    spokenLanguages = spokenLanguages,
    originCountries = originCountries,
    productionCountries = productionCountries,
    runtime = runtime,
    certification = certification,
    rating = rating,
    votes = votes,
    director = director,
    actors = actors,
    numberOfSeasons = numberOfSeasons,
    seasonNumber = seasonNumber,
    episodeNumbers = episodeNumbers,
    episodeTitles = episodeTitles,
    episodeAirDates = episodeAirDates,
    seasonName = seasonName,
    seasonYears = seasonYears,
    seasonAbsoluteStarts = seasonAbsoluteStarts,
    special = special,
    info = info,
    localize = localize,
    order = order,
)

/** 缓存快照 → [MediaMetadata]。 */
private fun CachedMediaMetadata.toMediaMetadata(): MediaMetadata = MediaMetadata(
    type = type,
    id = id,
    tmdbId = tmdbId,
    imdbId = imdbId,
    tvdbId = tvdbId,
    name = name,
    originalName = originalName,
    aliases = aliases,
    year = year,
    releaseDate = releaseDate,
    firstAirDate = firstAirDate,
    collectionName = collectionName,
    collectionId = collectionId,
    collectionIndex = collectionIndex,
    collectionYears = collectionYears,
    genres = genres,
    originalLanguage = originalLanguage,
    spokenLanguages = spokenLanguages,
    originCountries = originCountries,
    productionCountries = productionCountries,
    runtime = runtime,
    certification = certification,
    rating = rating,
    votes = votes,
    director = director,
    actors = actors,
    numberOfSeasons = numberOfSeasons,
    seasonNumber = seasonNumber,
    episodeNumbers = episodeNumbers,
    episodeTitles = episodeTitles,
    episodeAirDates = episodeAirDates,
    seasonName = seasonName,
    seasonYears = seasonYears,
    seasonAbsoluteStarts = seasonAbsoluteStarts,
    special = special,
    info = info,
    localize = localize,
    order = order,
)
