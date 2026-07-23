package com.webdavrenamer.data.db;

/**
 * TMDB 响应缓存实体（Task 2.3.4）。
 * 
 * TMDB API 有 40 req/10s 限流，详情类请求（movie/tv detail、season、episode group）
 * 内容稳定且会重复访问（批量匹配多集同剧集、Edit Match 反复切季），缓存可显著减少重复请求。
 * 
 * 复合业务键 [cacheKey] 唯一（`"{mediaType}:{tmdbId}:{language}[:{season}]"`），
 * 自增 [id] 仅作 Room 主键。命中时由调用方按 [cachedAt] 判断是否过期（默认 7 天）。
 * 
 * @property id            自增主键。
 * @property cacheKey      复合业务键，如 `MOVIE:123:zh-CN` / `SEASON:456:1:zh-CN`。
 * @property mediaType     缓存类别：`MOVIE`/`TV`/`SEASON`/`EPISODE_GROUP`。
 * @property tmdbId        TMDB 资源 id（剧集季缓存用 tvId）。
 * @property language      请求语言（影响翻译/标题，纳入键）。
 * @property seasonNumber  季号（仅 SEASON 类别使用）。
 * @property responseJson  完整 JSON 响应（序列化后的 DTO/MediaMetadata 快照）。
 * @property cachedAt      缓存写入时间戳（epoch millis），用于过期判定。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0011\n\u0002\u0010\u000B\n\u0002\u0008\u0004\u0008\u0087\u0008\u0012\u0001\u0000\u0018\u0000B;\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0002\u0012\u0004\u0010\u0007(\u0003\u0012\u0004\u0010\t(\u0002\u0012\u0006\u0008\u0002\u0010\n(\u0004\u0012\u0004\u0010\u000B(\u0002\u0012\u0004\u0010\u000C(\u0001\u00A2\u0006\u0004\u0008\r\u0010\u000EJ\u0007\u0010\u00108\u0001H\u00C6\u0003J\u0007\u0010\u00118\u0002H\u00C6\u0003J\u0007\u0010\u00128\u0002H\u00C6\u0003J\u0007\u0010\u00138\u0003H\u00C6\u0003J\u0007\u0010\u00148\u0002H\u00C6\u0003J\u0007\u0010\u00158\u0004H\u00C6\u0003J\u0007\u0010\u00168\u0002H\u00C6\u0003J\u0007\u0010\u00178\u0001H\u00C6\u0003JG\u0010\u00182\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00022\u0006\u0008\u0002\u0010\u0007(\u00032\u0006\u0008\u0002\u0010\t(\u00022\u0006\u0008\u0002\u0010\n(\u00042\u0006\u0008\u0002\u0010\u000B(\u00022\u0006\u0008\u0002\u0010\u000C(\u00018\u0005H\u00C6\u0001J\r\u0010\u00192\u0004\u0010\u001B(\u00078\u0006H\u00D6\u0003J\u0007\u0010\u001C8\u0003H\u00D6\u0001J\u0007\u0010\u001D8\u0002H\u00D6\u0001R\u000E\u0010\u00028\u0006H\u0001X\u0087\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0007H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0002\u00A2\u0006\u0002\n\u0000R\u000B\u0010\nH\u0004\u00A2\u0006\u0004\n\u0002\u0010\u000FR\t\u0010\u000BH\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u000CH\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001$\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0008\n\u0004\u0018\u00010\u0008\n\u00020\u0000\n\u00020\u001A\n\u0004\u0018\u00010\u0001\u00A8\u0006\u001E"}, d2 = {"Lcom/webdavrenamer/data/db/TmdbCacheEntity;", "", "id", "", "cacheKey", "", "mediaType", "tmdbId", "", "language", "seasonNumber", "responseJson", "cachedAt", "<init>", "(JLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/Integer;Ljava/lang/String;J)V", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.room.Entity(tableName = "tmdb_cache", indices = {@androidx.room.Index(value = {"cacheKey"}, unique = true) })
public final class TmdbCacheEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String cacheKey = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String mediaType = null;

    private final int tmdbId = 0;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String language = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer seasonNumber = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String responseJson = null;

    private final long cachedAt = 0L;

    /**
     * TMDB 响应缓存实体（Task 2.3.4）。
     * 
     * TMDB API 有 40 req/10s 限流，详情类请求（movie/tv detail、season、episode group）
     * 内容稳定且会重复访问（批量匹配多集同剧集、Edit Match 反复切季），缓存可显著减少重复请求。
     * 
     * 复合业务键 [cacheKey] 唯一（`"{mediaType}:{tmdbId}:{language}[:{season}]"`），
     * 自增 [id] 仅作 Room 主键。命中时由调用方按 [cachedAt] 判断是否过期（默认 7 天）。
     * 
     * @property id            自增主键。
     * @property cacheKey      复合业务键，如 `MOVIE:123:zh-CN` / `SEASON:456:1:zh-CN`。
     * @property mediaType     缓存类别：`MOVIE`/`TV`/`SEASON`/`EPISODE_GROUP`。
     * @property tmdbId        TMDB 资源 id（剧集季缓存用 tvId）。
     * @property language      请求语言（影响翻译/标题，纳入键）。
     * @property seasonNumber  季号（仅 SEASON 类别使用）。
     * @property responseJson  完整 JSON 响应（序列化后的 DTO/MediaMetadata 快照）。
     * @property cachedAt      缓存写入时间戳（epoch millis），用于过期判定。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.db.TmdbCacheEntity copy(long id, @org.jetbrains.annotations.NotNull() java.lang.String cacheKey, @org.jetbrains.annotations.NotNull() java.lang.String mediaType, int tmdbId, @org.jetbrains.annotations.NotNull() java.lang.String language, @org.jetbrains.annotations.Nullable() java.lang.Integer seasonNumber, @org.jetbrains.annotations.NotNull() java.lang.String responseJson, long cachedAt) {
        return null;
    }

    /**
     * TMDB 响应缓存实体（Task 2.3.4）。
     * 
     * TMDB API 有 40 req/10s 限流，详情类请求（movie/tv detail、season、episode group）
     * 内容稳定且会重复访问（批量匹配多集同剧集、Edit Match 反复切季），缓存可显著减少重复请求。
     * 
     * 复合业务键 [cacheKey] 唯一（`"{mediaType}:{tmdbId}:{language}[:{season}]"`），
     * 自增 [id] 仅作 Room 主键。命中时由调用方按 [cachedAt] 判断是否过期（默认 7 天）。
     * 
     * @property id            自增主键。
     * @property cacheKey      复合业务键，如 `MOVIE:123:zh-CN` / `SEASON:456:1:zh-CN`。
     * @property mediaType     缓存类别：`MOVIE`/`TV`/`SEASON`/`EPISODE_GROUP`。
     * @property tmdbId        TMDB 资源 id（剧集季缓存用 tvId）。
     * @property language      请求语言（影响翻译/标题，纳入键）。
     * @property seasonNumber  季号（仅 SEASON 类别使用）。
     * @property responseJson  完整 JSON 响应（序列化后的 DTO/MediaMetadata 快照）。
     * @property cachedAt      缓存写入时间戳（epoch millis），用于过期判定。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * TMDB 响应缓存实体（Task 2.3.4）。
     * 
     * TMDB API 有 40 req/10s 限流，详情类请求（movie/tv detail、season、episode group）
     * 内容稳定且会重复访问（批量匹配多集同剧集、Edit Match 反复切季），缓存可显著减少重复请求。
     * 
     * 复合业务键 [cacheKey] 唯一（`"{mediaType}:{tmdbId}:{language}[:{season}]"`），
     * 自增 [id] 仅作 Room 主键。命中时由调用方按 [cachedAt] 判断是否过期（默认 7 天）。
     * 
     * @property id            自增主键。
     * @property cacheKey      复合业务键，如 `MOVIE:123:zh-CN` / `SEASON:456:1:zh-CN`。
     * @property mediaType     缓存类别：`MOVIE`/`TV`/`SEASON`/`EPISODE_GROUP`。
     * @property tmdbId        TMDB 资源 id（剧集季缓存用 tvId）。
     * @property language      请求语言（影响翻译/标题，纳入键）。
     * @property seasonNumber  季号（仅 SEASON 类别使用）。
     * @property responseJson  完整 JSON 响应（序列化后的 DTO/MediaMetadata 快照）。
     * @property cachedAt      缓存写入时间戳（epoch millis），用于过期判定。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * TMDB 响应缓存实体（Task 2.3.4）。
     * 
     * TMDB API 有 40 req/10s 限流，详情类请求（movie/tv detail、season、episode group）
     * 内容稳定且会重复访问（批量匹配多集同剧集、Edit Match 反复切季），缓存可显著减少重复请求。
     * 
     * 复合业务键 [cacheKey] 唯一（`"{mediaType}:{tmdbId}:{language}[:{season}]"`），
     * 自增 [id] 仅作 Room 主键。命中时由调用方按 [cachedAt] 判断是否过期（默认 7 天）。
     * 
     * @property id            自增主键。
     * @property cacheKey      复合业务键，如 `MOVIE:123:zh-CN` / `SEASON:456:1:zh-CN`。
     * @property mediaType     缓存类别：`MOVIE`/`TV`/`SEASON`/`EPISODE_GROUP`。
     * @property tmdbId        TMDB 资源 id（剧集季缓存用 tvId）。
     * @property language      请求语言（影响翻译/标题，纳入键）。
     * @property seasonNumber  季号（仅 SEASON 类别使用）。
     * @property responseJson  完整 JSON 响应（序列化后的 DTO/MediaMetadata 快照）。
     * @property cachedAt      缓存写入时间戳（epoch millis），用于过期判定。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public TmdbCacheEntity(long id, @org.jetbrains.annotations.NotNull() java.lang.String cacheKey, @org.jetbrains.annotations.NotNull() java.lang.String mediaType, int tmdbId, @org.jetbrains.annotations.NotNull() java.lang.String language, @org.jetbrains.annotations.Nullable() java.lang.Integer seasonNumber, @org.jetbrains.annotations.NotNull() java.lang.String responseJson, long cachedAt) {
        super();
    }

    public final long component1() {
        return 0L;
    }

    public final long getId() {
        return 0L;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCacheKey() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMediaType() {
        return null;
    }

    public final int component4() {
        return 0;
    }

    public final int getTmdbId() {
        return 0;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLanguage() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component6() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getSeasonNumber() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getResponseJson() {
        return null;
    }

    public final long component8() {
        return 0L;
    }

    public final long getCachedAt() {
        return 0L;
    }
}
