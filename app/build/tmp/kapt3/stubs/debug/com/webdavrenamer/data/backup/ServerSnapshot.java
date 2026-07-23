package com.webdavrenamer.data.backup;

/**
 * 服务器快照（Task 5.2.1）。
 * 
 * @param password 明文密码：默认空串（不含密文）。
 * 仅当用户勾选「包含密码」并提供了口令时，导出才在此字段填入解密后的明文密码，
 * 随整个 [BackupPayload] 一并口令加密。未加密导出始终为空串。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0002\u0008\u0013\u0008\u0087\u0008\u0012\u0001\u0000\u0018\u0000 \u001E:\u0002\u001D\u001EBC\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0006\u0008\u0002\u0010\u0005(\u0002\u0012\u0006\u0008\u0002\u0010\u0007(\u0001\u0012\u0006\u0008\u0002\u0010\u0008(\u0003\u0012\u0006\u0008\u0002\u0010\t(\u0001\u0012\u0006\u0008\u0002\u0010\n(\u0001\u0012\u0006\u0008\u0002\u0010\u000B(\u0004\u00A2\u0006\u0004\u0008\r\u0010\u000EJ\u0007\u0010\u00108\u0001H\u00C6\u0003J\u0007\u0010\u00118\u0001H\u00C6\u0003J\u0007\u0010\u00128\u0002H\u00C6\u0003J\u0007\u0010\u00138\u0001H\u00C6\u0003J\u0007\u0010\u00148\u0003H\u00C6\u0003J\u0007\u0010\u00158\u0001H\u00C6\u0003J\u0007\u0010\u00168\u0001H\u00C6\u0003J\u0007\u0010\u00178\u0004H\u00C6\u0003JG\u0010\u00182\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00022\u0006\u0008\u0002\u0010\u0007(\u00012\u0006\u0008\u0002\u0010\u0008(\u00032\u0006\u0008\u0002\u0010\t(\u00012\u0006\u0008\u0002\u0010\n(\u00012\u0006\u0008\u0002\u0010\u000B(\u00048\u0005H\u00C6\u0001J\r\u0010\u00192\u0004\u0010\u001A(\u00068\u0004H\u00D6\u0003J\u0007\u0010\u001B8\u0007H\u00D6\u0001J\u0007\u0010\u001C8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u0005H\u0002\u00A2\u0006\u0004\n\u0002\u0010\u000FR\t\u0010\u0007H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001&\n\u00020\u0001\n\u00020\u0003\n\u0004\u0018\u00010\u0006\n\u0004\u0018\u00010\u0003\n\u00020\u000C\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020\u0006\u00A8\u0006\u001F"}, d2 = {"Lcom/webdavrenamer/data/backup/ServerSnapshot;", "", "name", "", "baseUrl", "port", "", "rootPath", "username", "password", "authType", "https", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "$serializer", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@kotlinx.serialization.Serializable()
public final class ServerSnapshot {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String baseUrl = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer port = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String rootPath = null;

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String username = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String password = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String authType = null;

    private final boolean https = false;

    /**
     * 服务器快照（Task 5.2.1）。
     * 
     * @param password 明文密码：默认空串（不含密文）。
     * 仅当用户勾选「包含密码」并提供了口令时，导出才在此字段填入解密后的明文密码，
     * 随整个 [BackupPayload] 一并口令加密。未加密导出始终为空串。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.backup.ServerSnapshot copy(@org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String baseUrl, @org.jetbrains.annotations.Nullable() java.lang.Integer port, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.Nullable() java.lang.String username, @org.jetbrains.annotations.NotNull() java.lang.String password, @org.jetbrains.annotations.NotNull() java.lang.String authType, boolean https) {
        return null;
    }

    /**
     * 服务器快照（Task 5.2.1）。
     * 
     * @param password 明文密码：默认空串（不含密文）。
     * 仅当用户勾选「包含密码」并提供了口令时，导出才在此字段填入解密后的明文密码，
     * 随整个 [BackupPayload] 一并口令加密。未加密导出始终为空串。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * 服务器快照（Task 5.2.1）。
     * 
     * @param password 明文密码：默认空串（不含密文）。
     * 仅当用户勾选「包含密码」并提供了口令时，导出才在此字段填入解密后的明文密码，
     * 随整个 [BackupPayload] 一并口令加密。未加密导出始终为空串。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * 服务器快照（Task 5.2.1）。
     * 
     * @param password 明文密码：默认空串（不含密文）。
     * 仅当用户勾选「包含密码」并提供了口令时，导出才在此字段填入解密后的明文密码，
     * 随整个 [BackupPayload] 一并口令加密。未加密导出始终为空串。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public ServerSnapshot(@org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String baseUrl, @org.jetbrains.annotations.Nullable() java.lang.Integer port, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.Nullable() java.lang.String username, @org.jetbrains.annotations.NotNull() java.lang.String password, @org.jetbrains.annotations.NotNull() java.lang.String authType, boolean https) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBaseUrl() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component3() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getPort() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRootPath() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUsername() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPassword() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAuthType() {
        return null;
    }

    public final boolean component8() {
        return false;
    }

    public final boolean getHttps() {
        return false;
    }

    public static final class Companion {

        private Companion() {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.KSerializer<com.webdavrenamer.data.backup.ServerSnapshot> serializer() {
            return null;
        }
    }
    @kotlin.Deprecated(message = "This synthesized declaration should not be used directly", level = kotlin.DeprecationLevel.HIDDEN)
    @java.lang.Deprecated
    public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.webdavrenamer.data.backup.ServerSnapshot> {
        @org.jetbrains.annotations.NotNull()
        @java.lang.Deprecated
        public static final com.webdavrenamer.data.backup.ServerSnapshot.$serializer INSTANCE = null;

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
        public final com.webdavrenamer.data.backup.ServerSnapshot deserialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Decoder decoder) {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
            return null;
        }

        @java.lang.Override()
        public final void serialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.ServerSnapshot value) {
        }
    }
}
