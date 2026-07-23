package com.webdavrenamer.data.backup;

/**
 * 需要口令加密/解密的内部数据载荷（不含元信息）。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0008\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0003\u0008\u0087\u0008\u0012\u0001\u0000\u0018\u0000 \u001A:\u0002\u0019\u001AB\u001F\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0003\u0012\u0004\u0010\u0007(\u0005\u0012\u0004\u0010\t(\u0006\u00A2\u0006\u0004\u0008\u000B\u0010\u000CJ\u0007\u0010\r8\u0001H\u00C6\u0003J\u0007\u0010\u000E8\u0003H\u00C6\u0003J\u0007\u0010\u000F8\u0005H\u00C6\u0003J\u0007\u0010\u00108\u0006H\u00C6\u0003J'\u0010\u00112\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00032\u0006\u0008\u0002\u0010\u0007(\u00052\u0006\u0008\u0002\u0010\t(\u00068\u0007H\u00C6\u0001J\r\u0010\u00122\u0004\u0010\u0014(\t8\u0008H\u00D6\u0003J\u0007\u0010\u00158\nH\u00D6\u0001J\u0007\u0010\u00178\u000BH\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0007H\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0006\u00A2\u0006\u0002\n\u0000\u00F2\u0001:\n\u00020\u0001\n\u00020\u0003\n\u00020\u0006\n\u0006\u0012\u0002\u0018\u00020\u0005\n\u00020\u0008\n\u0006\u0012\u0002\u0018\u00040\u0005\n\u00020\n\n\u00020\u0000\n\u00020\u0013\n\u0004\u0018\u00010\u0001\n\u00020\u0016\n\u00020\u0018\u00A8\u0006\u001B"}, d2 = {"Lcom/webdavrenamer/data/backup/BackupPayload;", "", "settings", "Lcom/webdavrenamer/data/backup/SettingsSnapshot;", "servers", "", "Lcom/webdavrenamer/data/backup/ServerSnapshot;", "templates", "Lcom/webdavrenamer/data/backup/TemplateSnapshot;", "hosts", "Lcom/webdavrenamer/core/backup/HostsConfig;", "<init>", "(Lcom/webdavrenamer/data/backup/SettingsSnapshot;Ljava/util/List;Ljava/util/List;Lcom/webdavrenamer/core/backup/HostsConfig;)V", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "", "$serializer", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@kotlinx.serialization.Serializable()
public final class BackupPayload {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.backup.SettingsSnapshot settings = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> servers = null;

    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> templates = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.backup.HostsConfig hosts = null;

    /**
     * 需要口令加密/解密的内部数据载荷（不含元信息）。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.backup.BackupPayload copy(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.SettingsSnapshot settings, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> servers, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> templates, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.backup.HostsConfig hosts) {
        return null;
    }

    /**
     * 需要口令加密/解密的内部数据载荷（不含元信息）。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * 需要口令加密/解密的内部数据载荷（不含元信息）。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * 需要口令加密/解密的内部数据载荷（不含元信息）。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public BackupPayload(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.SettingsSnapshot settings, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> servers, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> templates, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.backup.HostsConfig hosts) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.backup.SettingsSnapshot component1() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.backup.SettingsSnapshot getSettings() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> component2() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.data.backup.ServerSnapshot> getServers() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> component3() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.data.backup.TemplateSnapshot> getTemplates() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.backup.HostsConfig component4() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.backup.HostsConfig getHosts() {
        return null;
    }

    public static final class Companion {

        private Companion() {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.KSerializer<com.webdavrenamer.data.backup.BackupPayload> serializer() {
            return null;
        }
    }
    @kotlin.Deprecated(message = "This synthesized declaration should not be used directly", level = kotlin.DeprecationLevel.HIDDEN)
    @java.lang.Deprecated
    public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.webdavrenamer.data.backup.BackupPayload> {
        @org.jetbrains.annotations.NotNull()
        @java.lang.Deprecated
        public static final com.webdavrenamer.data.backup.BackupPayload.$serializer INSTANCE = null;

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
        public final com.webdavrenamer.data.backup.BackupPayload deserialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Decoder decoder) {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
            return null;
        }

        @java.lang.Override()
        public final void serialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.BackupPayload value) {
        }
    }
}
