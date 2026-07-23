package com.webdavrenamer.data.backup;

/**
 * 备份文件根结构。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000E\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000B\n\u0002\u0008\u0018\u0008\u0087\u0008\u0012\u0001\u0000\u0018\u0000 (:\u0002()B]\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0006\u0008\u0002\u0010\u0006(\u0003\u0012\u0006\u0008\u0002\u0010\u0008(\u0004\u0012\u0006\u0008\u0002\u0010\n(\u0006\u0012\u0006\u0008\u0002\u0010\r(\u0008\u0012\u0006\u0008\u0002\u0010\u000F(\t\u0012\u0006\u0008\u0002\u0010\u0011(\n\u0012\u0006\u0008\u0002\u0010\u0013(\u000B\u0012\u0006\u0008\u0002\u0010\u0014(\u000B\u0012\u0006\u0008\u0002\u0010\u0015(\u000B\u00A2\u0006\u0004\u0008\u0016\u0010\u0017J\u0007\u0010\u00188\u0001H\u00C6\u0003J\u0007\u0010\u00198\u0002H\u00C6\u0003J\u0007\u0010\u001A8\u0003H\u00C6\u0003J\u0007\u0010\u001B8\u0004H\u00C6\u0003J\u0007\u0010\u001C8\u0006H\u00C6\u0003J\u0007\u0010\u001D8\u0008H\u00C6\u0003J\u0007\u0010\u001E8\tH\u00C6\u0003J\u0007\u0010\u001F8\nH\u00C6\u0003J\u0007\u0010 8\u000BH\u00C6\u0003J\u0007\u0010!8\u000BH\u00C6\u0003J\u0007\u0010\"8\u000BH\u00C6\u0003J_\u0010#2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00032\u0006\u0008\u0002\u0010\u0008(\u00042\u0006\u0008\u0002\u0010\n(\u00062\u0006\u0008\u0002\u0010\r(\u00082\u0006\u0008\u0002\u0010\u000F(\t2\u0006\u0008\u0002\u0010\u0011(\n2\u0006\u0008\u0002\u0010\u0013(\u000B2\u0006\u0008\u0002\u0010\u0014(\u000B2\u0006\u0008\u0002\u0010\u0015(\u000B8\u000CH\u00C6\u0001J\r\u0010$2\u0004\u0010%(\r8\nH\u00D6\u0003J\u0007\u0010&8\u0001H\u00D6\u0001J\u0007\u0010'8\u0003H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0006\u00A2\u0006\u0002\n\u0000R\t\u0010\rH\u0008\u00A2\u0006\u0002\n\u0000R\t\u0010\u000FH\t\u00A2\u0006\u0002\n\u0000R\t\u0010\u0011H\n\u00A2\u0006\u0002\n\u0000R\t\u0010\u0013H\u000B\u00A2\u0006\u0002\n\u0000R\t\u0010\u0014H\u000B\u00A2\u0006\u0002\n\u0000R\t\u0010\u0015H\u000B\u00A2\u0006\u0002\n\u0000\u00F2\u0001H\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u0004\u0018\u00010\t\n\u00020\u000C\n\u0006\u0012\u0002\u0018\u00050\u000B\n\u00020\u000E\n\u0006\u0012\u0002\u0018\u00070\u000B\n\u0004\u0018\u00010\u0010\n\u00020\u0012\n\u0004\u0018\u00010\u0007\n\u00020\u0000\n\u0004\u0018\u00010\u0001\u00A8\u0006*"}, d2 = {"Lcom/webdavrenamer/data/backup/BackupFile;", "", "formatVersion", "", "exportedAt", "", "appVersion", "", "settings", "Lcom/webdavrenamer/data/backup/SettingsSnapshot;", "servers", "", "Lcom/webdavrenamer/data/backup/ServerSnapshot;", "templates", "Lcom/webdavrenamer/data/backup/TemplateSnapshot;", "hosts", "Lcom/webdavrenamer/core/backup/HostsConfig;", "encrypted", "", "salt", "iv", "cipherText", "<init>", "(IJLjava/lang/String;Lcom/webdavrenamer/data/backup/SettingsSnapshot;Ljava/util/List;Ljava/util/List;Lcom/webdavrenamer/core/backup/HostsConfig;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "copy", "equals", "other", "hashCode", "toString", "Companion", "$serializer", "app_debug"}, xs= "", pn = "", xi = 48)
@kotlinx.serialization.Serializable()
public final class BackupFile {
    /**
     * 备份格式版本，当前固定为 [CURRENT_FORMAT_VERSION]。
     */
    private final int formatVersion = 0;

    /**
     * 导出时间（epoch millis）。
     */
    private final long exportedAt = 0L;

    /**
     * 应用版本（信息字段，不参与兼容性校验）。
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String appVersion = null;

    /**
     * 设置快照；口令加密时为 null（数据在 [cipherText] 内）。
     */
    @org.jetbrains.annotations.Nullable()
    private final com.webdavrenamer.data.backup.SettingsSnapshot settings = null;

    /**
     * 服务器快照列表；口令加密时为空。
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> servers = null;

    /**
     * 自定义模板快照列表；口令加密时为空。
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> templates = null;

    /**
     * Hosts 配置；口令加密时为 null。
     */
    @org.jetbrains.annotations.Nullable()
    private final com.webdavrenamer.core.backup.HostsConfig hosts = null;

    /**
     * 是否口令加密。
     */
    private final boolean encrypted = false;

    /**
     * PBKDF2 盐（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String salt = null;

    /**
     * AES-GCM IV（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String iv = null;

    /**
     * 加密后的 [BackupPayload] 密文（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String cipherText = null;

    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.data.backup.BackupFile.Companion Companion = null;

    /**
     * 当前支持的备份格式版本。
     */
    public static final int CURRENT_FORMAT_VERSION = 1;

    /**
     * 备份文件根结构。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.backup.BackupFile copy(int formatVersion, long exportedAt, @org.jetbrains.annotations.NotNull() java.lang.String appVersion, @org.jetbrains.annotations.Nullable() com.webdavrenamer.data.backup.SettingsSnapshot settings, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> servers, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> templates, @org.jetbrains.annotations.Nullable() com.webdavrenamer.core.backup.HostsConfig hosts, boolean encrypted, @org.jetbrains.annotations.Nullable() java.lang.String salt, @org.jetbrains.annotations.Nullable() java.lang.String iv, @org.jetbrains.annotations.Nullable() java.lang.String cipherText) {
        return null;
    }

    /**
     * 备份文件根结构。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * 备份文件根结构。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * 备份文件根结构。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public BackupFile(int formatVersion, long exportedAt, @org.jetbrains.annotations.NotNull() java.lang.String appVersion, @org.jetbrains.annotations.Nullable() com.webdavrenamer.data.backup.SettingsSnapshot settings, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> servers, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> templates, @org.jetbrains.annotations.Nullable() com.webdavrenamer.core.backup.HostsConfig hosts, boolean encrypted, @org.jetbrains.annotations.Nullable() java.lang.String salt, @org.jetbrains.annotations.Nullable() java.lang.String iv, @org.jetbrains.annotations.Nullable() java.lang.String cipherText) {
        super();
    }

    /**
     * 备份格式版本，当前固定为 [CURRENT_FORMAT_VERSION]。
     */
    public final int component1() {
        return 0;
    }

    /**
     * 备份格式版本，当前固定为 [CURRENT_FORMAT_VERSION]。
     */
    public final int getFormatVersion() {
        return 0;
    }

    /**
     * 导出时间（epoch millis）。
     */
    public final long component2() {
        return 0L;
    }

    /**
     * 导出时间（epoch millis）。
     */
    public final long getExportedAt() {
        return 0L;
    }

    /**
     * 应用版本（信息字段，不参与兼容性校验）。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }

    /**
     * 应用版本（信息字段，不参与兼容性校验）。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAppVersion() {
        return null;
    }

    /**
     * 设置快照；口令加密时为 null（数据在 [cipherText] 内）。
     */
    @org.jetbrains.annotations.Nullable()
    public final com.webdavrenamer.data.backup.SettingsSnapshot component4() {
        return null;
    }

    /**
     * 设置快照；口令加密时为 null（数据在 [cipherText] 内）。
     */
    @org.jetbrains.annotations.Nullable()
    public final com.webdavrenamer.data.backup.SettingsSnapshot getSettings() {
        return null;
    }

    /**
     * 服务器快照列表；口令加密时为空。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> component5() {
        return null;
    }

    /**
     * 服务器快照列表；口令加密时为空。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> getServers() {
        return null;
    }

    /**
     * 自定义模板快照列表；口令加密时为空。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> component6() {
        return null;
    }

    /**
     * 自定义模板快照列表；口令加密时为空。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> getTemplates() {
        return null;
    }

    /**
     * Hosts 配置；口令加密时为 null。
     */
    @org.jetbrains.annotations.Nullable()
    public final com.webdavrenamer.core.backup.HostsConfig component7() {
        return null;
    }

    /**
     * Hosts 配置；口令加密时为 null。
     */
    @org.jetbrains.annotations.Nullable()
    public final com.webdavrenamer.core.backup.HostsConfig getHosts() {
        return null;
    }

    /**
     * 是否口令加密。
     */
    public final boolean component8() {
        return false;
    }

    /**
     * 是否口令加密。
     */
    public final boolean getEncrypted() {
        return false;
    }

    /**
     * PBKDF2 盐（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }

    /**
     * PBKDF2 盐（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSalt() {
        return null;
    }

    /**
     * AES-GCM IV（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }

    /**
     * AES-GCM IV（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getIv() {
        return null;
    }

    /**
     * 加密后的 [BackupPayload] 密文（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }

    /**
     * 加密后的 [BackupPayload] 密文（Base64），仅 [encrypted]=true 时有值。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCipherText() {
        return null;
    }

    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u001C\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u0008\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0008\u0086\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\u0004\u0010\u00068\u0003R\u0007\u0010\u0004H\u0001X\u0086T\u00F2\u0001\u0014\n\u00020\u0001\n\u00020\u0005\n\u00020\u0008\n\u0006\u0012\u0002\u0018\u00020\u0007\u00A8\u0006\t"}, d2 = {"Lcom/webdavrenamer/data/backup/BackupFile$Companion;", "", "<init>", "()V", "CURRENT_FORMAT_VERSION", "", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/webdavrenamer/data/backup/BackupFile;", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Companion {

        private Companion() {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.KSerializer<com.webdavrenamer.data.backup.BackupFile> serializer() {
            return null;
        }
    }
    @kotlin.Deprecated(message = "This synthesized declaration should not be used directly", level = kotlin.DeprecationLevel.HIDDEN)
    @java.lang.Deprecated
    public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.webdavrenamer.data.backup.BackupFile> {
        @org.jetbrains.annotations.NotNull()
        @java.lang.Deprecated
        public static final com.webdavrenamer.data.backup.BackupFile.$serializer INSTANCE = null;

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
        public final com.webdavrenamer.data.backup.BackupFile deserialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Decoder decoder) {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
            return null;
        }

        @java.lang.Override()
        public final void serialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.BackupFile value) {
        }
    }
}
