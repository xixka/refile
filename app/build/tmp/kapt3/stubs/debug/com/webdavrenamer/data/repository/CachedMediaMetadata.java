package com.webdavrenamer.data.repository;

/**
 * [MediaMetadata] 的可序列化缓存快照（Task 2.3.4）。
 * 
 * [MediaMetadata] 本身在 :core 为普通 data class（非 @Serializable，不可改），故在 app 层镜像其全部
 * 字段以支持 Room JSON 缓存。[MediaType] 已 @Serializable，其余字段均为基本类型/集合，可安全序列化。
 * 字段与 [MediaMetadata] 一一对应，通过 [toCached]/[toMediaMetadata] 无损往返。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0002\u0008\u0004\n\u0002\u0010 \n\u0002\u0008\u000F\n\u0002\u0010\u0006\n\u0002\u0008\r\n\u0002\u0010$\n\u0002\u0008.\n\u0002\u0010\u000B\n\u0002\u0008\u0006\u0008\u0083\u0008\u0012\u0001\u0000\u0018\u0000 _:\u0002^_B\u00B7\u0002\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0006\u0008\u0002\u0010\u0004(\u0002\u0012\u0006\u0008\u0002\u0010\u0006(\u0002\u0012\u0006\u0008\u0002\u0010\u0007(\u0003\u0012\u0006\u0008\u0002\u0010\t(\u0003\u0012\u0006\u0008\u0002\u0010\n(\u0003\u0012\u0006\u0008\u0002\u0010\u000B(\u0003\u0012\u0006\u0008\u0002\u0010\u000C(\u0005\u0012\u0006\u0008\u0002\u0010\u000E(\u0002\u0012\u0006\u0008\u0002\u0010\u000F(\u0003\u0012\u0006\u0008\u0002\u0010\u0010(\u0003\u0012\u0006\u0008\u0002\u0010\u0011(\u0003\u0012\u0006\u0008\u0002\u0010\u0012(\u0002\u0012\u0006\u0008\u0002\u0010\u0013(\u0002\u0012\u0006\u0008\u0002\u0010\u0014(\u0007\u0012\u0006\u0008\u0002\u0010\u0015(\u0005\u0012\u0006\u0008\u0002\u0010\u0016(\u0003\u0012\u0006\u0008\u0002\u0010\u0017(\u0005\u0012\u0006\u0008\u0002\u0010\u0018(\u0005\u0012\u0006\u0008\u0002\u0010\u0019(\u0005\u0012\u0006\u0008\u0002\u0010\u001A(\u0002\u0012\u0006\u0008\u0002\u0010\u001B(\u0003\u0012\u0006\u0008\u0002\u0010\u001C(\u0008\u0012\u0006\u0008\u0002\u0010\u001E(\u0002\u0012\u0006\u0008\u0002\u0010\u001F(\u0003\u0012\u0006\u0008\u0002\u0010 (\u0005\u0012\u0006\u0008\u0002\u0010!(\u0002\u0012\u0006\u0008\u0002\u0010\"(\u0002\u0012\u0006\u0008\u0002\u0010#(\u0007\u0012\u0006\u0008\u0002\u0010$(\u0005\u0012\u0006\u0008\u0002\u0010%(\u0005\u0012\u0006\u0008\u0002\u0010&(\u0003\u0012\u0006\u0008\u0002\u0010'(\u0007\u0012\u0006\u0008\u0002\u0010((\u0007\u0012\u0006\u0008\u0002\u0010)(\u0002\u0012\u0006\u0008\u0002\u0010*(\t\u0012\u0006\u0008\u0002\u0010,(\u000B\u0012\u0006\u0008\u0002\u0010-(\r\u00A2\u0006\u0004\u0008.\u0010/J\u0007\u001028\u0001H\u00C6\u0003J\u0007\u001038\u0002H\u00C6\u0003J\u0007\u001048\u0002H\u00C6\u0003J\u0007\u001058\u0003H\u00C6\u0003J\u0007\u001068\u0003H\u00C6\u0003J\u0007\u001078\u0003H\u00C6\u0003J\u0007\u001088\u0003H\u00C6\u0003J\u0007\u001098\u0005H\u00C6\u0003J\u0007\u0010:8\u0002H\u00C6\u0003J\u0007\u0010;8\u0003H\u00C6\u0003J\u0007\u0010<8\u0003H\u00C6\u0003J\u0007\u0010=8\u0003H\u00C6\u0003J\u0007\u0010>8\u0002H\u00C6\u0003J\u0007\u0010?8\u0002H\u00C6\u0003J\u0007\u0010@8\u0007H\u00C6\u0003J\u0007\u0010A8\u0005H\u00C6\u0003J\u0007\u0010B8\u0003H\u00C6\u0003J\u0007\u0010C8\u0005H\u00C6\u0003J\u0007\u0010D8\u0005H\u00C6\u0003J\u0007\u0010E8\u0005H\u00C6\u0003J\u0007\u0010F8\u0002H\u00C6\u0003J\u0007\u0010G8\u0003H\u00C6\u0003J\u0007\u0010H8\u0008H\u00C6\u0003J\u0007\u0010I8\u0002H\u00C6\u0003J\u0007\u0010J8\u0003H\u00C6\u0003J\u0007\u0010K8\u0005H\u00C6\u0003J\u0007\u0010L8\u0002H\u00C6\u0003J\u0007\u0010M8\u0002H\u00C6\u0003J\u0007\u0010N8\u0007H\u00C6\u0003J\u0007\u0010O8\u0005H\u00C6\u0003J\u0007\u0010P8\u0005H\u00C6\u0003J\u0007\u0010Q8\u0003H\u00C6\u0003J\u0007\u0010R8\u0007H\u00C6\u0003J\u0007\u0010S8\u0007H\u00C6\u0003J\u0007\u0010T8\u0002H\u00C6\u0003J\u0007\u0010U8\tH\u00C6\u0003J\u0007\u0010V8\u000BH\u00C6\u0003J\u0007\u0010W8\rH\u00C6\u0003J\u00B7\u0002\u0010X2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00022\u0006\u0008\u0002\u0010\u0007(\u00032\u0006\u0008\u0002\u0010\t(\u00032\u0006\u0008\u0002\u0010\n(\u00032\u0006\u0008\u0002\u0010\u000B(\u00032\u0006\u0008\u0002\u0010\u000C(\u00052\u0006\u0008\u0002\u0010\u000E(\u00022\u0006\u0008\u0002\u0010\u000F(\u00032\u0006\u0008\u0002\u0010\u0010(\u00032\u0006\u0008\u0002\u0010\u0011(\u00032\u0006\u0008\u0002\u0010\u0012(\u00022\u0006\u0008\u0002\u0010\u0013(\u00022\u0006\u0008\u0002\u0010\u0014(\u00072\u0006\u0008\u0002\u0010\u0015(\u00052\u0006\u0008\u0002\u0010\u0016(\u00032\u0006\u0008\u0002\u0010\u0017(\u00052\u0006\u0008\u0002\u0010\u0018(\u00052\u0006\u0008\u0002\u0010\u0019(\u00052\u0006\u0008\u0002\u0010\u001A(\u00022\u0006\u0008\u0002\u0010\u001B(\u00032\u0006\u0008\u0002\u0010\u001C(\u00082\u0006\u0008\u0002\u0010\u001E(\u00022\u0006\u0008\u0002\u0010\u001F(\u00032\u0006\u0008\u0002\u0010 (\u00052\u0006\u0008\u0002\u0010!(\u00022\u0006\u0008\u0002\u0010\"(\u00022\u0006\u0008\u0002\u0010#(\u00072\u0006\u0008\u0002\u0010$(\u00052\u0006\u0008\u0002\u0010%(\u00052\u0006\u0008\u0002\u0010&(\u00032\u0006\u0008\u0002\u0010'(\u00072\u0006\u0008\u0002\u0010((\u00072\u0006\u0008\u0002\u0010)(\u00022\u0006\u0008\u0002\u0010*(\t2\u0006\u0008\u0002\u0010,(\u000B2\u0006\u0008\u0002\u0010-(\r8\u000EH\u00C6\u0001J\r\u0010Y2\u0004\u0010[(\u00108\u000FH\u00D6\u0003J\u0007\u0010\\8\u0006H\u00D6\u0001J\u0007\u0010]8\u0004H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u0004H\u0002\u00A2\u0006\u0004\n\u0002\u00100R\u000B\u0010\u0006H\u0002\u00A2\u0006\u0004\n\u0002\u00100R\t\u0010\u0007H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u000CH\u0005\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u000EH\u0002\u00A2\u0006\u0004\n\u0002\u00100R\t\u0010\u000FH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0010H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0011H\u0003\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u0012H\u0002\u00A2\u0006\u0004\n\u0002\u00100R\u000B\u0010\u0013H\u0002\u00A2\u0006\u0004\n\u0002\u00100R\t\u0010\u0014H\u0007\u00A2\u0006\u0002\n\u0000R\t\u0010\u0015H\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\u0016H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0017H\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\u0018H\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\u0019H\u0005\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u001AH\u0002\u00A2\u0006\u0004\n\u0002\u00100R\t\u0010\u001BH\u0003\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u001CH\u0008\u00A2\u0006\u0004\n\u0002\u00101R\u000B\u0010\u001EH\u0002\u00A2\u0006\u0004\n\u0002\u00100R\t\u0010\u001FH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010 H\u0005\u00A2\u0006\u0002\n\u0000R\u000B\u0010!H\u0002\u00A2\u0006\u0004\n\u0002\u00100R\u000B\u0010\"H\u0002\u00A2\u0006\u0004\n\u0002\u00100R\t\u0010#H\u0007\u00A2\u0006\u0002\n\u0000R\t\u0010$H\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010%H\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010&H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010'H\u0007\u00A2\u0006\u0002\n\u0000R\t\u0010(H\u0007\u00A2\u0006\u0002\n\u0000R\u000B\u0010)H\u0002\u00A2\u0006\u0004\n\u0002\u00100R\t\u0010*H\t\u00A2\u0006\u0002\n\u0000R\t\u0010,H\u000B\u00A2\u0006\u0002\n\u0000R\t\u0010-H\r\u00A2\u0006\u0002\n\u0000\u00F2\u0001|\n\u00020\u0001\n\u00020\u0003\n\u0004\u0018\u00010\u0005\n\u0004\u0018\u00010\u0008\n\u00020\u0008\n\u0006\u0012\u0002\u0018\u00040\r\n\u00020\u0005\n\u0006\u0012\u0002\u0018\u00060\r\n\u0004\u0018\u00010\u001D\n\n\u0012\u0002\u0018\u0004\u0012\u0002\u0018\u00030+\n\n\u0012\u0002\u0018\u0004\u0012\u0002\u0018\u00040+\n\n\u0012\u0002\u0018\u0004\u0012\u0002\u0018\n0+\n\n\u0012\u0002\u0018\u0004\u0012\u0002\u0018\u00060+\n\n\u0012\u0002\u0018\u0004\u0012\u0002\u0018\u000C0+\n\u00020\u0000\n\u00020Z\n\u0004\u0018\u00010\u0001\u00A8\u0006`"}, d2 = {"Lcom/webdavrenamer/data/repository/CachedMediaMetadata;", "", "type", "Lcom/webdavrenamer/core/model/MediaType;", "id", "", "tmdbId", "imdbId", "", "tvdbId", "name", "originalName", "aliases", "", "year", "releaseDate", "firstAirDate", "collectionName", "collectionId", "collectionIndex", "collectionYears", "genres", "originalLanguage", "spokenLanguages", "originCountries", "productionCountries", "runtime", "certification", "rating", "", "votes", "director", "actors", "numberOfSeasons", "seasonNumber", "episodeNumbers", "episodeTitles", "episodeAirDates", "seasonName", "seasonYears", "seasonAbsoluteStarts", "special", "info", "", "localize", "order", "<init>", "(Lcom/webdavrenamer/core/model/MediaType;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Integer;Ljava/lang/String;Ljava/util/List;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/lang/Integer;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;)V", "Ljava/lang/Integer;", "Ljava/lang/Double;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component37", "component38", "copy", "equals", "", "other", "hashCode", "toString", "$serializer", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@kotlinx.serialization.Serializable()
final class CachedMediaMetadata {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.model.MediaType type = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer id = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer tmdbId = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String imdbId = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String tvdbId = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String name = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String originalName = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> aliases = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer year = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String releaseDate = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String firstAirDate = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String collectionName = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer collectionId = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer collectionIndex = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Integer> collectionYears = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> genres = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String originalLanguage = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> spokenLanguages = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> originCountries = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> productionCountries = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer runtime = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String certification = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double rating = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer votes = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String director = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> actors = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer numberOfSeasons = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer seasonNumber = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Integer> episodeNumbers = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> episodeTitles = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> episodeAirDates = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String seasonName = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Integer> seasonYears = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Integer> seasonAbsoluteStarts = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer special = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.String> info = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> localize = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> order = null;

    public CachedMediaMetadata() {
        super();
    }

    /**
     * [MediaMetadata] 的可序列化缓存快照（Task 2.3.4）。
     * 
     * [MediaMetadata] 本身在 :core 为普通 data class（非 @Serializable，不可改），故在 app 层镜像其全部
     * 字段以支持 Room JSON 缓存。[MediaType] 已 @Serializable，其余字段均为基本类型/集合，可安全序列化。
     * 字段与 [MediaMetadata] 一一对应，通过 [toCached]/[toMediaMetadata] 无损往返。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.repository.CachedMediaMetadata copy(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType type, @org.jetbrains.annotations.Nullable() java.lang.Integer id, @org.jetbrains.annotations.Nullable() java.lang.Integer tmdbId, @org.jetbrains.annotations.Nullable() java.lang.String imdbId, @org.jetbrains.annotations.Nullable() java.lang.String tvdbId, @org.jetbrains.annotations.Nullable() java.lang.String name, @org.jetbrains.annotations.Nullable() java.lang.String originalName, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> aliases, @org.jetbrains.annotations.Nullable() java.lang.Integer year, @org.jetbrains.annotations.Nullable() java.lang.String releaseDate, @org.jetbrains.annotations.Nullable() java.lang.String firstAirDate, @org.jetbrains.annotations.Nullable() java.lang.String collectionName, @org.jetbrains.annotations.Nullable() java.lang.Integer collectionId, @org.jetbrains.annotations.Nullable() java.lang.Integer collectionIndex, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> collectionYears, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> genres, @org.jetbrains.annotations.Nullable() java.lang.String originalLanguage, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> spokenLanguages, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> originCountries, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> productionCountries, @org.jetbrains.annotations.Nullable() java.lang.Integer runtime, @org.jetbrains.annotations.Nullable() java.lang.String certification, @org.jetbrains.annotations.Nullable() java.lang.Double rating, @org.jetbrains.annotations.Nullable() java.lang.Integer votes, @org.jetbrains.annotations.Nullable() java.lang.String director, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> actors, @org.jetbrains.annotations.Nullable() java.lang.Integer numberOfSeasons, @org.jetbrains.annotations.Nullable() java.lang.Integer seasonNumber, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> episodeNumbers, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> episodeTitles, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> episodeAirDates, @org.jetbrains.annotations.Nullable() java.lang.String seasonName, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> seasonYears, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> seasonAbsoluteStarts, @org.jetbrains.annotations.Nullable() java.lang.Integer special, @org.jetbrains.annotations.NotNull() java.util.Map<java.lang.String, java.lang.String> info, @org.jetbrains.annotations.NotNull() java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.String>> localize, @org.jetbrains.annotations.NotNull() java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.Integer>> order) {
        return null;
    }

    /**
     * [MediaMetadata] 的可序列化缓存快照（Task 2.3.4）。
     * 
     * [MediaMetadata] 本身在 :core 为普通 data class（非 @Serializable，不可改），故在 app 层镜像其全部
     * 字段以支持 Room JSON 缓存。[MediaType] 已 @Serializable，其余字段均为基本类型/集合，可安全序列化。
     * 字段与 [MediaMetadata] 一一对应，通过 [toCached]/[toMediaMetadata] 无损往返。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * [MediaMetadata] 的可序列化缓存快照（Task 2.3.4）。
     * 
     * [MediaMetadata] 本身在 :core 为普通 data class（非 @Serializable，不可改），故在 app 层镜像其全部
     * 字段以支持 Room JSON 缓存。[MediaType] 已 @Serializable，其余字段均为基本类型/集合，可安全序列化。
     * 字段与 [MediaMetadata] 一一对应，通过 [toCached]/[toMediaMetadata] 无损往返。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * [MediaMetadata] 的可序列化缓存快照（Task 2.3.4）。
     * 
     * [MediaMetadata] 本身在 :core 为普通 data class（非 @Serializable，不可改），故在 app 层镜像其全部
     * 字段以支持 Room JSON 缓存。[MediaType] 已 @Serializable，其余字段均为基本类型/集合，可安全序列化。
     * 字段与 [MediaMetadata] 一一对应，通过 [toCached]/[toMediaMetadata] 无损往返。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public CachedMediaMetadata(@org.jetbrains.annotations.NotNull() com.webdavrenamer.core.model.MediaType type, @org.jetbrains.annotations.Nullable() java.lang.Integer id, @org.jetbrains.annotations.Nullable() java.lang.Integer tmdbId, @org.jetbrains.annotations.Nullable() java.lang.String imdbId, @org.jetbrains.annotations.Nullable() java.lang.String tvdbId, @org.jetbrains.annotations.Nullable() java.lang.String name, @org.jetbrains.annotations.Nullable() java.lang.String originalName, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> aliases, @org.jetbrains.annotations.Nullable() java.lang.Integer year, @org.jetbrains.annotations.Nullable() java.lang.String releaseDate, @org.jetbrains.annotations.Nullable() java.lang.String firstAirDate, @org.jetbrains.annotations.Nullable() java.lang.String collectionName, @org.jetbrains.annotations.Nullable() java.lang.Integer collectionId, @org.jetbrains.annotations.Nullable() java.lang.Integer collectionIndex, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> collectionYears, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> genres, @org.jetbrains.annotations.Nullable() java.lang.String originalLanguage, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> spokenLanguages, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> originCountries, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> productionCountries, @org.jetbrains.annotations.Nullable() java.lang.Integer runtime, @org.jetbrains.annotations.Nullable() java.lang.String certification, @org.jetbrains.annotations.Nullable() java.lang.Double rating, @org.jetbrains.annotations.Nullable() java.lang.Integer votes, @org.jetbrains.annotations.Nullable() java.lang.String director, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> actors, @org.jetbrains.annotations.Nullable() java.lang.Integer numberOfSeasons, @org.jetbrains.annotations.Nullable() java.lang.Integer seasonNumber, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> episodeNumbers, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> episodeTitles, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> episodeAirDates, @org.jetbrains.annotations.Nullable() java.lang.String seasonName, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> seasonYears, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.Integer> seasonAbsoluteStarts, @org.jetbrains.annotations.Nullable() java.lang.Integer special, @org.jetbrains.annotations.NotNull() java.util.Map<java.lang.String, java.lang.String> info, @org.jetbrains.annotations.NotNull() java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.String>> localize, @org.jetbrains.annotations.NotNull() java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.Integer>> order) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.model.MediaType component1() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.model.MediaType getType() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component2() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getId() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component3() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getTmdbId() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getImdbId() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTvdbId() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getName() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOriginalName() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component8() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getAliases() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component9() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getYear() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getReleaseDate() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getFirstAirDate() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCollectionName() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component13() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getCollectionId() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component14() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getCollectionIndex() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> component15() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> getCollectionYears() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component16() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getGenres() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component17() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOriginalLanguage() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component18() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getSpokenLanguages() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component19() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getOriginCountries() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component20() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getProductionCountries() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component21() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getRuntime() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component22() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCertification() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component23() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getRating() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component24() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getVotes() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component25() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDirector() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component26() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getActors() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component27() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getNumberOfSeasons() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component28() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getSeasonNumber() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> component29() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> getEpisodeNumbers() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component30() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getEpisodeTitles() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component31() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getEpisodeAirDates() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component32() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSeasonName() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> component33() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> getSeasonYears() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> component34() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> getSeasonAbsoluteStarts() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component35() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getSpecial() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.String> component36() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.String> getInfo() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> component37() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getLocalize() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> component38() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> getOrder() {
        return null;
    }

    public static final class Companion {

        private Companion() {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.KSerializer<com.webdavrenamer.data.repository.CachedMediaMetadata> serializer() {
            return null;
        }
    }
    @kotlin.Deprecated(message = "This synthesized declaration should not be used directly", level = kotlin.DeprecationLevel.HIDDEN)
    @java.lang.Deprecated
    public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.webdavrenamer.data.repository.CachedMediaMetadata> {
        @org.jetbrains.annotations.NotNull()
        @java.lang.Deprecated
        public static final com.webdavrenamer.data.repository.CachedMediaMetadata.$serializer INSTANCE = null;

        @org.jetbrains.annotations.NotNull()
        private static final kotlinx.serialization.descriptors.SerialDescriptor descriptor = null;

        private $serializer() {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public final kotlinx.serialization.KSerializer<?>[] childSerializers() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public final com.webdavrenamer.data.repository.CachedMediaMetadata deserialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Decoder decoder) {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
            return null;
        }

        @java.lang.Override()
        public final void serialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.CachedMediaMetadata value) {
        }
    }
}
