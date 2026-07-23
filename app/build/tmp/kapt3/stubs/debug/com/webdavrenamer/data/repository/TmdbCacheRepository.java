package com.webdavrenamer.data.repository;

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
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0006\n\u0002\u0010 \n\u0002\u0008\u0005\n\u0002\u0010\u0002\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0008\n\u0002\u0018\u0002\n\u0002\u0008\u0007\u0008\u0007\u0012\u0001\u0000\u0018\u0000 C:\u0001CB\u0015\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\u0018\u0010\u000C2\u0004\u0010\u000E(\u00062\u0004\u0010\u0010(\u00078\u0005H\u0086@\u00A2\u0006\u0002\u0010\u0012J\u0018\u0010\u00132\u0004\u0010\u000E(\u00062\u0004\u0010\u0010(\u00078\u0005H\u0086@\u00A2\u0006\u0002\u0010\u0012J\u001E\u0010\u00142\u0004\u0010\u0016(\u00062\u0004\u0010\u0017(\u00062\u0004\u0010\u0010(\u00078\u0008H\u0086@\u00A2\u0006\u0002\u0010\u0018J\u0012\u0010\u00192\u0004\u0010\u001B(\u00068\tH\u0086@\u00A2\u0006\u0002\u0010\u001CJ\u0018\u0010\u001D2\u0004\u0010\u001E(\u00052\u0004\u0010\u0010(\u00078\u0005H\u0086@\u00A2\u0006\u0002\u0010\u001FJ\"\u0010 2\u0004\u0010\"(\u00072\u0006\u0008\u0002\u0010#(\u000B2\u0006\u0008\u0002\u0010\u0010(\u00078\nH\u0086@\u00A2\u0006\u0002\u0010$J\"\u0010%2\u0004\u0010\"(\u00072\u0006\u0008\u0002\u0010#(\u000B2\u0006\u0008\u0002\u0010\u0010(\u00078\nH\u0086@\u00A2\u0006\u0002\u0010$J\u000C\u0010&8\u000CH\u0086@\u00A2\u0006\u0002\u0010(J\u000C\u0010)8\u000CH\u0086@\u00A2\u0006\u0002\u0010(J0\u0010*2\u0004\u0010+(\u00072\u0004\u0010\u000E(\u00062\u0004\u0010\u0010(\u00072\u0004\u0010\u0017(\u000B2\u0004\u0010,(\u00072\u0004\u0010-(\u000F8\u0005H\u0082@\u00A2\u0006\u0002\u00100J?\u00101\"\u0007\u0008\u0000\u001022\u0001\u00002\u0004\u0010+(\u00072\u0004\u0010\u000E(\u00062\u0004\u0010\u0010(\u00072\u0004\u0010\u0017(\u000B2\u0004\u0010,(\u00072\u0004\u0010-(\u00122\u0004\u00103(\u00138\u0010H\u0082@\u00A2\u0006\u0002\u00105J0\u001062\u0004\u0010,(\u00072\u0004\u0010+(\u00072\u0004\u0010\u000E(\u00062\u0004\u0010\u0010(\u00072\u0004\u0010\u0017(\u000B2\u0004\u00107(\u00058\u000CH\u0082@\u00A2\u0006\u0002\u00108J0\u001092\u0004\u0010,(\u00072\u0004\u0010+(\u00072\u0004\u0010\u000E(\u00062\u0004\u0010\u0010(\u00072\u0004\u0010\u0017(\u000B2\u0004\u0010:(\u00078\u000CH\u0082@\u00A2\u0006\u0002\u0010;J\u000C\u0010<8\u0014H\u0082@\u00A2\u0006\u0002\u0010(J\u0012\u0010>2\u0004\u0010\u000E(\u00062\u0004\u0010\u0010(\u00078\u0007H\u0002J\u0012\u0010?2\u0004\u0010\u000E(\u00062\u0004\u0010\u0010(\u00078\u0007H\u0002J\u0018\u0010@2\u0004\u0010\u0016(\u00062\u0004\u0010A(\u00062\u0004\u0010\u0010(\u00078\u0007H\u0002J\u000C\u0010B2\u0004\u0010\u001B(\u00068\u0007H\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0003X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\nH\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001|\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\t\n\u00020\u000B\n\u00020\r\n\u00020\u000F\n\u00020\u0011\n\u00020\u0015\n\u00020\u001A\n\u0006\u0012\u0002\u0018\u00050!\n\u0004\u0018\u00010\u000F\n\u00020'\n\u0006\u0012\u0002\u0018\u00050/\n\u0004\u0018\u00010\u0001\n\u000C\u0008\u0001\u0012\u0002\u0018\r\u0012\u0002\u0018\u000E0.\n\u0002H2\n\u0006\u0012\u0002\u0018\u00100/\n\u000C\u0008\u0001\u0012\u0002\u0018\u0011\u0012\u0002\u0018\u000E0.\n\u0006\u0012\u0002\u0018\u001004\n\u00020=\u00A8\u0006D"}, d2 = {"Lcom/webdavrenamer/data/repository/TmdbCacheRepository;", "", "dao", "Lcom/webdavrenamer/data/db/TmdbCacheDao;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "<init>", "(Lcom/webdavrenamer/data/db/TmdbCacheDao;Lcom/webdavrenamer/data/prefs/SettingsRepository;)V", "ttlMillis", "", "json", "Lkotlinx/serialization/json/Json;", "getMovie", "Lcom/webdavrenamer/core/naming/MediaMetadata;", "tmdbId", "", "language", "", "(ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTv", "getSeason", "Lcom/webdavrenamer/core/tmdb/SeasonDetail;", "tvId", "seasonNumber", "(IILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEpisodeGroup", "Lcom/webdavrenamer/core/tmdb/EpisodeGroupDetail;", "id", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "enrichWithCollection", "movie", "(Lcom/webdavrenamer/core/naming/MediaMetadata;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchMovie", "", "query", "year", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchTv", "clearCache", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "evictExpired", "cached", "mediaType", "key", "fetch", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cachedSerializable", "T", "serializer", "Lkotlinx/serialization/KSerializer;", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlinx/serialization/KSerializer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "store", "value", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/Integer;Lcom/webdavrenamer/core/naming/MediaMetadata;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "storeJson", "body", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buildTmdbClient", "Lcom/webdavrenamer/core/tmdb/TmdbClient;", "movieKey", "tvKey", "seasonKey", "season", "episodeGroupKey", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@javax.inject.Singleton()
public final class TmdbCacheRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.db.TmdbCacheDao dao = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    /**
     * 缓存有效期：7 天（毫秒）。
     */
    private final long ttlMillis = 604800000L;

    /**
     * 缓存 JSON 实例：容错（未知字段忽略，便于 CachedMediaMetadata 字段演进向前兼容），
     * 写默认值保证空集合/空 map 也能正确往返。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.serialization.json.Json json = null;

    @org.jetbrains.annotations.NotNull()
    private static final com.webdavrenamer.data.repository.TmdbCacheRepository.Companion Companion = null;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CACHE_MOVIE = "MOVIE";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CACHE_TV = "TV";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CACHE_SEASON = "SEASON";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CACHE_EPISODE_GROUP = "EPISODE_GROUP";

    @javax.inject.Inject()
    public TmdbCacheRepository(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.TmdbCacheDao dao, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings) {
        super();
    }

    /**
     * 电影详情：键 `MOVIE:$tmdbId:$language`。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getMovie(int tmdbId, @org.jetbrains.annotations.NotNull() java.lang.String language, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.core.naming.MediaMetadata> $completion) {
        return null;
    }

    /**
     * 剧集详情：键 `TV:$tmdbId:$language`。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTv(int tmdbId, @org.jetbrains.annotations.NotNull() java.lang.String language, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.core.naming.MediaMetadata> $completion) {
        return null;
    }

    /**
     * 季详情：键 `SEASON:$tvId:$season:$language`。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSeason(int tvId, int seasonNumber, @org.jetbrains.annotations.NotNull() java.lang.String language, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.core.tmdb.SeasonDetail> $completion) {
        return null;
    }

    /**
     * Episode Group 详情：键 `EPISODE_GROUP:$id`（无 language 维度）。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEpisodeGroup(int id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.core.tmdb.EpisodeGroupDetail> $completion) {
        return null;
    }

    /**
     * 合集补全：直接透传 [TmdbClient.enrichWithCollection]，不缓存。
     * 
     * 该方法输入为已fetch的 [MediaMetadata]、输出基于合集端点 + movie 详情重新映射，缓存维度与
     * [getMovie] 重叠且结果含动态 collectionIndex，未列入 Task 2.3.4 详情缓存清单，故不缓存。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object enrichWithCollection(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.MediaMetadata movie, @org.jetbrains.annotations.NotNull() java.lang.String language, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.core.naming.MediaMetadata> $completion) {
        return null;
    }

    /**
     * 搜索电影：不缓存，直接透传。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchMovie(@org.jetbrains.annotations.NotNull() java.lang.String query, @org.jetbrains.annotations.Nullable() java.lang.Integer year, @org.jetbrains.annotations.NotNull() java.lang.String language, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.util.List<com.webdavrenamer.core.naming.MediaMetadata>> $completion) {
        return null;
    }

    /**
     * 搜索剧集：不缓存，直接透传。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchTv(@org.jetbrains.annotations.NotNull() java.lang.String query, @org.jetbrains.annotations.Nullable() java.lang.Integer year, @org.jetbrains.annotations.NotNull() java.lang.String language, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.util.List<com.webdavrenamer.core.naming.MediaMetadata>> $completion) {
        return null;
    }

    /**
     * 清空全部 TMDB 缓存（设置页"清除 TMDB 缓存"调用）。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearCache(@org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 清理已过期缓存（可由设置页或定期 Worker 调用；读时已按 TTL 判定，非必须）。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object evictExpired(@org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * [MediaMetadata] 类缓存通用流程（movie/tv）：查缓存→未过期则反序列化 [CachedMediaMetadata]→
     * 否则网络获取→序列化回写→返回。反序列化失败（缓存损坏/字段演进不兼容）静默回退到网络。
     */
    private final java.lang.Object cached(java.lang.String mediaType, int tmdbId, java.lang.String language, java.lang.Integer seasonNumber, java.lang.String key, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super com.webdavrenamer.core.naming.MediaMetadata>, ? extends java.lang.Object> fetch, kotlin.coroutines.Continuation<? super com.webdavrenamer.core.naming.MediaMetadata> $completion) {
        return null;
    }

    /**
     * 可直接序列化 DTO（[SeasonDetail]/[EpisodeGroupDetail]）的缓存通用流程。
     */
    private final <T extends java.lang.Object>java.lang.Object cachedSerializable(java.lang.String mediaType, int tmdbId, java.lang.String language, java.lang.Integer seasonNumber, java.lang.String key, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> fetch, kotlinx.serialization.KSerializer<T> serializer, kotlin.coroutines.Continuation<? super T> $completion) {
        return null;
    }

    /**
     * 序列化 [MediaMetadata] 为 [CachedMediaMetadata] JSON 并入库。
     */
    private final java.lang.Object store(java.lang.String key, java.lang.String mediaType, int tmdbId, java.lang.String language, java.lang.Integer seasonNumber, com.webdavrenamer.core.naming.MediaMetadata value, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    private final java.lang.Object storeJson(java.lang.String key, java.lang.String mediaType, int tmdbId, java.lang.String language, java.lang.Integer seasonNumber, java.lang.String body, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 读 apiKey + hostsConfig 构造 [TmdbClient]；apiKey 为空抛 [IllegalStateException]。
     */
    private final java.lang.Object buildTmdbClient(kotlin.coroutines.Continuation<? super com.webdavrenamer.core.tmdb.TmdbClient> $completion) {
        return null;
    }

    private final java.lang.String movieKey(int tmdbId, java.lang.String language) {
        return null;
    }

    private final java.lang.String tvKey(int tmdbId, java.lang.String language) {
        return null;
    }

    private final java.lang.String seasonKey(int tvId, int season, java.lang.String language) {
        return null;
    }

    private final java.lang.String episodeGroupKey(int id) {
        return null;
    }

    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0004\u0008\u0082\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086TR\u0007\u0010\u0006H\u0001X\u0086TR\u0007\u0010\u0007H\u0001X\u0086TR\u0007\u0010\u0008H\u0001X\u0086T\u00F2\u0001\u0008\n\u00020\u0001\n\u00020\u0005\u00A8\u0006\t"}, d2 = {"Lcom/webdavrenamer/data/repository/TmdbCacheRepository$Companion;", "", "<init>", "()V", "CACHE_MOVIE", "", "CACHE_TV", "CACHE_SEASON", "CACHE_EPISODE_GROUP", "app_debug"}, xs= "", pn = "", xi = 48)
    private static final class Companion {

        private Companion() {
            super();
        }
    }
}
