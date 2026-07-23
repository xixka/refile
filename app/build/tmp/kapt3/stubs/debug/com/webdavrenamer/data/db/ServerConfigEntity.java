package com.webdavrenamer.data.db;

/**
 * WebDAV 服务器配置实体（计划 §M1 SubTask 1.3.1）。
 * 
 * 密码字段 [encryptedPassword] 存储经 Android Keystore 加密后的 base64 密文，
 * 禁止明文落盘（红线）。加密/解密由 [com.webdavrenamer.data.crypto.KeystoreCrypto] 完成。
 * 
 * @property id                自增主键。
 * @property name              别名（UI 展示）。
 * @property baseUrl           scheme + host，如 `https://dav.example.com`。
 * @property port              端口（可空，用默认 80/443）。
 * @property rootPath          根路径，默认 `/`。
 * @property username          用户名（可空，匿名访问）。
 * @property encryptedPassword Keystore 加密后的密文（base64），明文密码绝不落盘。
 * @property authType          认证类型：auto/basic/digest（auto=自动协商）。
 * @property https             是否启用 HTTPS。
 * @property createdAt         创建时间戳（毫秒）。
 * @property updatedAt         更新时间戳（毫秒）。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0002\u0008\u0016\u0008\u0087\u0008\u0012\u0001\u0000\u0018\u0000B[\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0002\u0012\u0006\u0008\u0002\u0010\u0007(\u0003\u0012\u0006\u0008\u0002\u0010\t(\u0002\u0012\u0006\u0008\u0002\u0010\n(\u0004\u0012\u0006\u0008\u0002\u0010\u000B(\u0004\u0012\u0006\u0008\u0002\u0010\u000C(\u0002\u0012\u0006\u0008\u0002\u0010\r(\u0005\u0012\u0006\u0008\u0002\u0010\u000F(\u0001\u0012\u0006\u0008\u0002\u0010\u0010(\u0001\u00A2\u0006\u0004\u0008\u0011\u0010\u0012J\u0007\u0010\u00148\u0001H\u00C6\u0003J\u0007\u0010\u00158\u0002H\u00C6\u0003J\u0007\u0010\u00168\u0002H\u00C6\u0003J\u0007\u0010\u00178\u0003H\u00C6\u0003J\u0007\u0010\u00188\u0002H\u00C6\u0003J\u0007\u0010\u00198\u0004H\u00C6\u0003J\u0007\u0010\u001A8\u0004H\u00C6\u0003J\u0007\u0010\u001B8\u0002H\u00C6\u0003J\u0007\u0010\u001C8\u0005H\u00C6\u0003J\u0007\u0010\u001D8\u0001H\u00C6\u0003J\u0007\u0010\u001E8\u0001H\u00C6\u0003J_\u0010\u001F2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00022\u0006\u0008\u0002\u0010\u0007(\u00032\u0006\u0008\u0002\u0010\t(\u00022\u0006\u0008\u0002\u0010\n(\u00042\u0006\u0008\u0002\u0010\u000B(\u00042\u0006\u0008\u0002\u0010\u000C(\u00022\u0006\u0008\u0002\u0010\r(\u00052\u0006\u0008\u0002\u0010\u000F(\u00012\u0006\u0008\u0002\u0010\u0010(\u00018\u0006H\u00C6\u0001J\r\u0010 2\u0004\u0010!(\u00078\u0005H\u00D6\u0003J\u0007\u0010\"8\u0008H\u00D6\u0001J\u0007\u0010#8\u0002H\u00D6\u0001R\u000E\u0010\u00028\u0006H\u0001X\u0087\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0002\u00A2\u0006\u0002\n\u0000R\u000B\u0010\u0007H\u0003\u00A2\u0006\u0004\n\u0002\u0010\u0013R\t\u0010\tH\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0004\u00A2\u0006\u0002\n\u0000R\t\u0010\u000CH\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\rH\u0005\u00A2\u0006\u0002\n\u0000R\t\u0010\u000FH\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0010H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001*\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u0004\u0018\u00010\u0008\n\u0004\u0018\u00010\u0005\n\u00020\u000E\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020\u0008\u00A8\u0006$"}, d2 = {"Lcom/webdavrenamer/data/db/ServerConfigEntity;", "", "id", "", "name", "", "baseUrl", "port", "", "rootPath", "username", "encryptedPassword", "authType", "https", "", "createdAt", "updatedAt", "<init>", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZJJ)V", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "copy", "equals", "other", "hashCode", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
@androidx.room.Entity(tableName = "server_configs")
public final class ServerConfigEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;

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

    @org.jetbrains.annotations.Nullable()
    private final java.lang.String encryptedPassword = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String authType = null;

    private final boolean https = false;

    private final long createdAt = 0L;

    private final long updatedAt = 0L;

    /**
     * WebDAV 服务器配置实体（计划 §M1 SubTask 1.3.1）。
     * 
     * 密码字段 [encryptedPassword] 存储经 Android Keystore 加密后的 base64 密文，
     * 禁止明文落盘（红线）。加密/解密由 [com.webdavrenamer.data.crypto.KeystoreCrypto] 完成。
     * 
     * @property id                自增主键。
     * @property name              别名（UI 展示）。
     * @property baseUrl           scheme + host，如 `https://dav.example.com`。
     * @property port              端口（可空，用默认 80/443）。
     * @property rootPath          根路径，默认 `/`。
     * @property username          用户名（可空，匿名访问）。
     * @property encryptedPassword Keystore 加密后的密文（base64），明文密码绝不落盘。
     * @property authType          认证类型：auto/basic/digest（auto=自动协商）。
     * @property https             是否启用 HTTPS。
     * @property createdAt         创建时间戳（毫秒）。
     * @property updatedAt         更新时间戳（毫秒）。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.db.ServerConfigEntity copy(long id, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String baseUrl, @org.jetbrains.annotations.Nullable() java.lang.Integer port, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.Nullable() java.lang.String username, @org.jetbrains.annotations.Nullable() java.lang.String encryptedPassword, @org.jetbrains.annotations.NotNull() java.lang.String authType, boolean https, long createdAt, long updatedAt) {
        return null;
    }

    /**
     * WebDAV 服务器配置实体（计划 §M1 SubTask 1.3.1）。
     * 
     * 密码字段 [encryptedPassword] 存储经 Android Keystore 加密后的 base64 密文，
     * 禁止明文落盘（红线）。加密/解密由 [com.webdavrenamer.data.crypto.KeystoreCrypto] 完成。
     * 
     * @property id                自增主键。
     * @property name              别名（UI 展示）。
     * @property baseUrl           scheme + host，如 `https://dav.example.com`。
     * @property port              端口（可空，用默认 80/443）。
     * @property rootPath          根路径，默认 `/`。
     * @property username          用户名（可空，匿名访问）。
     * @property encryptedPassword Keystore 加密后的密文（base64），明文密码绝不落盘。
     * @property authType          认证类型：auto/basic/digest（auto=自动协商）。
     * @property https             是否启用 HTTPS。
     * @property createdAt         创建时间戳（毫秒）。
     * @property updatedAt         更新时间戳（毫秒）。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * WebDAV 服务器配置实体（计划 §M1 SubTask 1.3.1）。
     * 
     * 密码字段 [encryptedPassword] 存储经 Android Keystore 加密后的 base64 密文，
     * 禁止明文落盘（红线）。加密/解密由 [com.webdavrenamer.data.crypto.KeystoreCrypto] 完成。
     * 
     * @property id                自增主键。
     * @property name              别名（UI 展示）。
     * @property baseUrl           scheme + host，如 `https://dav.example.com`。
     * @property port              端口（可空，用默认 80/443）。
     * @property rootPath          根路径，默认 `/`。
     * @property username          用户名（可空，匿名访问）。
     * @property encryptedPassword Keystore 加密后的密文（base64），明文密码绝不落盘。
     * @property authType          认证类型：auto/basic/digest（auto=自动协商）。
     * @property https             是否启用 HTTPS。
     * @property createdAt         创建时间戳（毫秒）。
     * @property updatedAt         更新时间戳（毫秒）。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * WebDAV 服务器配置实体（计划 §M1 SubTask 1.3.1）。
     * 
     * 密码字段 [encryptedPassword] 存储经 Android Keystore 加密后的 base64 密文，
     * 禁止明文落盘（红线）。加密/解密由 [com.webdavrenamer.data.crypto.KeystoreCrypto] 完成。
     * 
     * @property id                自增主键。
     * @property name              别名（UI 展示）。
     * @property baseUrl           scheme + host，如 `https://dav.example.com`。
     * @property port              端口（可空，用默认 80/443）。
     * @property rootPath          根路径，默认 `/`。
     * @property username          用户名（可空，匿名访问）。
     * @property encryptedPassword Keystore 加密后的密文（base64），明文密码绝不落盘。
     * @property authType          认证类型：auto/basic/digest（auto=自动协商）。
     * @property https             是否启用 HTTPS。
     * @property createdAt         创建时间戳（毫秒）。
     * @property updatedAt         更新时间戳（毫秒）。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public ServerConfigEntity(long id, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String baseUrl, @org.jetbrains.annotations.Nullable() java.lang.Integer port, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.Nullable() java.lang.String username, @org.jetbrains.annotations.Nullable() java.lang.String encryptedPassword, @org.jetbrains.annotations.NotNull() java.lang.String authType, boolean https, long createdAt, long updatedAt) {
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
    public final java.lang.String getName() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBaseUrl() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component4() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getPort() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRootPath() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUsername() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }

    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEncryptedPassword() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAuthType() {
        return null;
    }

    public final boolean component9() {
        return false;
    }

    public final boolean getHttps() {
        return false;
    }

    public final long component10() {
        return 0L;
    }

    public final long getCreatedAt() {
        return 0L;
    }

    public final long component11() {
        return 0L;
    }

    public final long getUpdatedAt() {
        return 0L;
    }
}
