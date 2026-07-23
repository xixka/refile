package com.webdavrenamer.data.db;

/**
 * 重命名条目实体（计划 §M5 SubTask 5.1.1）。
 * 
 * 每条记录对应一次 [com.webdavrenamer.core.rename.RenameOperation] 的执行结果，
 * 隶属于某个 [RenameBatchEntity]。撤销时按 [id] 倒序逐条反向 MOVE（targetPath → sourcePath）。
 * 
 * [companionsJson] 存 [com.webdavrenamer.core.rename.CompanionRename] 列表的 JSON 字符串
 * （由 [com.webdavrenamer.data.repository.HistoryRepository] 内的简易编解码器处理），
 * 撤销时反向把每个 companion 的 targetPath 移回 sourcePath。
 * 
 * @property id             自增主键（撤销顺序依据）。
 * @property batchId         所属批次 id（外键 + 索引）。
 * @property sourcePath     主文件源路径。
 * @property targetPath     主文件目标路径。
 * @property mediaType      媒体类型字符串（"MOVIE"/"EPISODE"，对应 [com.webdavrenamer.core.model.MediaType]）。
 * @property companionsJson 伴随文件列表 JSON（[com.webdavrenamer.core.rename.CompanionRename] 数组）。
 * @property status         结果状态字符串（"SUCCESS"/"PARTIAL"/"FAILED"/"SKIPPED"）。
 * @property errorMessage   失败原因（仅 FAILED/PARTIAL 时非空）。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0002\u0008\u0011\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0087\u0008\u0012\u0001\u0000\u0018\u0000B;\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0004\u0010\u0005(\u0002\u0012\u0004\u0010\u0007(\u0002\u0012\u0004\u0010\u0008(\u0002\u0012\u0004\u0010\t(\u0002\u0012\u0004\u0010\n(\u0002\u0012\u0006\u0008\u0002\u0010\u000B(\u0003\u00A2\u0006\u0004\u0008\u000C\u0010\rJ\u0007\u0010\u000E8\u0001H\u00C6\u0003J\u0007\u0010\u000F8\u0001H\u00C6\u0003J\u0007\u0010\u00108\u0002H\u00C6\u0003J\u0007\u0010\u00118\u0002H\u00C6\u0003J\u0007\u0010\u00128\u0002H\u00C6\u0003J\u0007\u0010\u00138\u0002H\u00C6\u0003J\u0007\u0010\u00148\u0002H\u00C6\u0003J\u0007\u0010\u00158\u0003H\u00C6\u0003JG\u0010\u00162\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00022\u0006\u0008\u0002\u0010\u0007(\u00022\u0006\u0008\u0002\u0010\u0008(\u00022\u0006\u0008\u0002\u0010\t(\u00022\u0006\u0008\u0002\u0010\n(\u00022\u0006\u0008\u0002\u0010\u000B(\u00038\u0004H\u00C6\u0001J\r\u0010\u00172\u0004\u0010\u0019(\u00068\u0005H\u00D6\u0003J\u0007\u0010\u001A8\u0007H\u00D6\u0001J\u0007\u0010\u001C8\u0002H\u00D6\u0001R\u000E\u0010\u00028\u0006H\u0001X\u0087\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0007H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0003\u00A2\u0006\u0002\n\u0000\u00F2\u0001$\n\u00020\u0001\n\u00020\u0003\n\u00020\u0006\n\u0004\u0018\u00010\u0006\n\u00020\u0000\n\u00020\u0018\n\u0004\u0018\u00010\u0001\n\u00020\u001B\u00A8\u0006\u001D"}, d2 = {"Lcom/webdavrenamer/data/db/RenameEntryEntity;", "", "id", "", "batchId", "sourcePath", "", "targetPath", "mediaType", "companionsJson", "status", "errorMessage", "<init>", "(JJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.room.Entity(tableName = "rename_entries", foreignKeys = {@androidx.room.ForeignKey(entity = com.webdavrenamer.data.db.RenameBatchEntity.class, parentColumns = {"id"}, childColumns = {"batchId"}, onDelete = 5) }, indices = {@androidx.room.Index(value = {"batchId"}) })
public final class RenameEntryEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;

    private final long batchId = 0L;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String sourcePath = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String targetPath = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String mediaType = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String companionsJson = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String status = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;

    /**
     * 重命名条目实体（计划 §M5 SubTask 5.1.1）。
     * 
     * 每条记录对应一次 [com.webdavrenamer.core.rename.RenameOperation] 的执行结果，
     * 隶属于某个 [RenameBatchEntity]。撤销时按 [id] 倒序逐条反向 MOVE（targetPath → sourcePath）。
     * 
     * [companionsJson] 存 [com.webdavrenamer.core.rename.CompanionRename] 列表的 JSON 字符串
     * （由 [com.webdavrenamer.data.repository.HistoryRepository] 内的简易编解码器处理），
     * 撤销时反向把每个 companion 的 targetPath 移回 sourcePath。
     * 
     * @property id             自增主键（撤销顺序依据）。
     * @property batchId         所属批次 id（外键 + 索引）。
     * @property sourcePath     主文件源路径。
     * @property targetPath     主文件目标路径。
     * @property mediaType      媒体类型字符串（"MOVIE"/"EPISODE"，对应 [com.webdavrenamer.core.model.MediaType]）。
     * @property companionsJson 伴随文件列表 JSON（[com.webdavrenamer.core.rename.CompanionRename] 数组）。
     * @property status         结果状态字符串（"SUCCESS"/"PARTIAL"/"FAILED"/"SKIPPED"）。
     * @property errorMessage   失败原因（仅 FAILED/PARTIAL 时非空）。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.db.RenameEntryEntity copy(long id, long batchId, @org.jetbrains.annotations.NotNull() java.lang.String sourcePath, @org.jetbrains.annotations.NotNull() java.lang.String targetPath, @org.jetbrains.annotations.NotNull() java.lang.String mediaType, @org.jetbrains.annotations.NotNull() java.lang.String companionsJson, @org.jetbrains.annotations.NotNull() java.lang.String status, @org.jetbrains.annotations.Nullable() java.lang.String errorMessage) {
        return null;
    }

    /**
     * 重命名条目实体（计划 §M5 SubTask 5.1.1）。
     * 
     * 每条记录对应一次 [com.webdavrenamer.core.rename.RenameOperation] 的执行结果，
     * 隶属于某个 [RenameBatchEntity]。撤销时按 [id] 倒序逐条反向 MOVE（targetPath → sourcePath）。
     * 
     * [companionsJson] 存 [com.webdavrenamer.core.rename.CompanionRename] 列表的 JSON 字符串
     * （由 [com.webdavrenamer.data.repository.HistoryRepository] 内的简易编解码器处理），
     * 撤销时反向把每个 companion 的 targetPath 移回 sourcePath。
     * 
     * @property id             自增主键（撤销顺序依据）。
     * @property batchId         所属批次 id（外键 + 索引）。
     * @property sourcePath     主文件源路径。
     * @property targetPath     主文件目标路径。
     * @property mediaType      媒体类型字符串（"MOVIE"/"EPISODE"，对应 [com.webdavrenamer.core.model.MediaType]）。
     * @property companionsJson 伴随文件列表 JSON（[com.webdavrenamer.core.rename.CompanionRename] 数组）。
     * @property status         结果状态字符串（"SUCCESS"/"PARTIAL"/"FAILED"/"SKIPPED"）。
     * @property errorMessage   失败原因（仅 FAILED/PARTIAL 时非空）。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * 重命名条目实体（计划 §M5 SubTask 5.1.1）。
     * 
     * 每条记录对应一次 [com.webdavrenamer.core.rename.RenameOperation] 的执行结果，
     * 隶属于某个 [RenameBatchEntity]。撤销时按 [id] 倒序逐条反向 MOVE（targetPath → sourcePath）。
     * 
     * [companionsJson] 存 [com.webdavrenamer.core.rename.CompanionRename] 列表的 JSON 字符串
     * （由 [com.webdavrenamer.data.repository.HistoryRepository] 内的简易编解码器处理），
     * 撤销时反向把每个 companion 的 targetPath 移回 sourcePath。
     * 
     * @property id             自增主键（撤销顺序依据）。
     * @property batchId         所属批次 id（外键 + 索引）。
     * @property sourcePath     主文件源路径。
     * @property targetPath     主文件目标路径。
     * @property mediaType      媒体类型字符串（"MOVIE"/"EPISODE"，对应 [com.webdavrenamer.core.model.MediaType]）。
     * @property companionsJson 伴随文件列表 JSON（[com.webdavrenamer.core.rename.CompanionRename] 数组）。
     * @property status         结果状态字符串（"SUCCESS"/"PARTIAL"/"FAILED"/"SKIPPED"）。
     * @property errorMessage   失败原因（仅 FAILED/PARTIAL 时非空）。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * 重命名条目实体（计划 §M5 SubTask 5.1.1）。
     * 
     * 每条记录对应一次 [com.webdavrenamer.core.rename.RenameOperation] 的执行结果，
     * 隶属于某个 [RenameBatchEntity]。撤销时按 [id] 倒序逐条反向 MOVE（targetPath → sourcePath）。
     * 
     * [companionsJson] 存 [com.webdavrenamer.core.rename.CompanionRename] 列表的 JSON 字符串
     * （由 [com.webdavrenamer.data.repository.HistoryRepository] 内的简易编解码器处理），
     * 撤销时反向把每个 companion 的 targetPath 移回 sourcePath。
     * 
     * @property id             自增主键（撤销顺序依据）。
     * @property batchId         所属批次 id（外键 + 索引）。
     * @property sourcePath     主文件源路径。
     * @property targetPath     主文件目标路径。
     * @property mediaType      媒体类型字符串（"MOVIE"/"EPISODE"，对应 [com.webdavrenamer.core.model.MediaType]）。
     * @property companionsJson 伴随文件列表 JSON（[com.webdavrenamer.core.rename.CompanionRename] 数组）。
     * @property status         结果状态字符串（"SUCCESS"/"PARTIAL"/"FAILED"/"SKIPPED"）。
     * @property errorMessage   失败原因（仅 FAILED/PARTIAL 时非空）。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public RenameEntryEntity(long id, long batchId, @org.jetbrains.annotations.NotNull() java.lang.String sourcePath, @org.jetbrains.annotations.NotNull() java.lang.String targetPath, @org.jetbrains.annotations.NotNull() java.lang.String mediaType, @org.jetbrains.annotations.NotNull() java.lang.String companionsJson, @org.jetbrains.annotations.NotNull() java.lang.String status, @org.jetbrains.annotations.Nullable() java.lang.String errorMessage) {
        super();
    }

    public final long component1() {
        return 0L;
    }

    public final long getId() {
        return 0L;
    }

    public final long component2() {
        return 0L;
    }

    public final long getBatchId() {
        return 0L;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSourcePath() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTargetPath() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMediaType() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCompanionsJson() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStatus() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
}
