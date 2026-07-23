package com.webdavrenamer.data.repository;

/**
 * 服务器配置仓库（计划 §M1 SubTask 1.3.3）。
 * 
 * 职责：
 * - 透传 DAO 的 Flow 观察与单条查询。
 * - 写入/更新时调用 [KeystoreCrypto] 加密密码，确保明文密码不落盘（红线）。
 * - [testConnection] 解密密码后构造 [WebDavClient]，仅做 PROPFIND 连通性测试，
 *   不读取/下载文件内容（红线，重命名仅经 MOVE/MKCOL 完成）。
 * 
 * 简化：直接使用 [ServerConfigEntity] 作为领域模型，UI 层后续可自行映射。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\t\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u0002\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\u0012\u0001\u0000\u0018\u0000B\u001B\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0003\u00A2\u0006\u0004\u0008\u0008\u0010\tJ\u0004\u0010\n8\u0006J\u0012\u0010\u000E2\u0004\u0010\u000F(\u00088\u0007H\u0086@\u00A2\u0006\u0002\u0010\u0011J<\u0010\u00122\u0004\u0010\u0013(\t2\u0004\u0010\u0015(\t2\u0004\u0010\u0016(\n2\u0004\u0010\u0018(\t2\u0004\u0010\u0019(\u000B2\u0004\u0010\u001A(\u000B2\u0004\u0010\u001B(\t2\u0004\u0010\u001C(\u000C8\u0008H\u0086@\u00A2\u0006\u0002\u0010\u001EJ\u0018\u0010\u001F2\u0004\u0010!(\u00042\u0004\u0010\"(\u000B8\rH\u0086@\u00A2\u0006\u0002\u0010#J\u0012\u0010$2\u0004\u0010\u000F(\u00088\rH\u0086@\u00A2\u0006\u0002\u0010\u0011J\u0012\u0010%2\u0004\u0010!(\u00048\u000EH\u0086@\u00A2\u0006\u0002\u0010'J\u0012\u0010(2\u0004\u0010!(\u00048\u000FH\u0086@\u00A2\u0006\u0002\u0010'J\u000C\u0010*2\u0004\u0010!(\u00048\tH\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0006H\u0003X\u0082\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001N\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\r\n\u0006\u0012\u0002\u0018\u00040\u000C\n\u0006\u0012\u0002\u0018\u00050\u000B\n\u0004\u0018\u00010\r\n\u00020\u0010\n\u00020\u0014\n\u0004\u0018\u00010\u0017\n\u0004\u0018\u00010\u0014\n\u00020\u001D\n\u00020 \n\u00020&\n\u00020)\u00A8\u0006+"}, d2 = {"Lcom/webdavrenamer/data/repository/ServerRepository;", "", "dao", "Lcom/webdavrenamer/data/db/ServerConfigDao;", "crypto", "Lcom/webdavrenamer/data/crypto/KeystoreCrypto;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "<init>", "(Lcom/webdavrenamer/data/db/ServerConfigDao;Lcom/webdavrenamer/data/crypto/KeystoreCrypto;Lcom/webdavrenamer/data/prefs/SettingsRepository;)V", "observeServers", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/webdavrenamer/data/db/ServerConfigEntity;", "getServer", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addServer", "name", "", "baseUrl", "port", "", "rootPath", "username", "password", "authType", "https", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateServer", "", "entity", "newPassword", "(Lcom/webdavrenamer/data/db/ServerConfigEntity;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteServer", "testConnection", "Lcom/webdavrenamer/core/webdav/ConnectionResult;", "(Lcom/webdavrenamer/data/db/ServerConfigEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clientFor", "Lcom/webdavrenamer/core/webdav/WebDavClient;", "buildFullBaseUrl", "app_debug"}, xs= "", pn = "", xi = 48)
public final class ServerRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.db.ServerConfigDao dao = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.crypto.KeystoreCrypto crypto = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    @javax.inject.Inject()
    public ServerRepository(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigDao dao, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.crypto.KeystoreCrypto crypto, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings) {
        super();
    }

    /**
     * 观察所有服务器配置（按 updatedAt 倒序）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.webdavrenamer.data.db.ServerConfigEntity>> observeServers() {
        return null;
    }

    /**
     * 按 id 取单条配置。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getServer(long id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.data.db.ServerConfigEntity> $completion) {
        return null;
    }

    /**
     * 新增服务器配置。明文密码先经 Keystore 加密再落盘。
     * 
     * @return 新插入行的 id。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addServer(@org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String baseUrl, @org.jetbrains.annotations.Nullable() java.lang.Integer port, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.Nullable() java.lang.String username, @org.jetbrains.annotations.Nullable() java.lang.String password, @org.jetbrains.annotations.NotNull() java.lang.String authType, boolean https, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }

    /**
     * 更新服务器配置。
     * - [newPassword] 非空且非空白：重新加密并替换 [ServerConfigEntity.encryptedPassword]。
     * - 否则：保留原密文，仅更新其它字段与 [ServerConfigEntity.updatedAt]。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateServer(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigEntity entity, @org.jetbrains.annotations.Nullable() java.lang.String newPassword, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 按 id 删除服务器配置。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteServer(long id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 测试与目标服务器的连通性。
     * 
     * 解密存储的密码，根据 [ServerConfigEntity.https] + host + port 拼出完整 baseUrl，
     * 构造 [WebDavClient] 并对其 rootPath 发起 PROPFIND Depth 0 测试。
     * 
     * 仅做连通性/认证探测，不下载文件内容。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object testConnection(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigEntity entity, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.core.webdav.ConnectionResult> $completion) {
        return null;
    }

    /**
     * 构造已带认证拦截器的 [WebDavClient]（Task 3.4 预览页冲突检测/伴随文件发现复用）。
     * 
     * 解密存储的密码，按 [buildFullBaseUrl] 拼出完整 baseUrl 后构造 [WebDavClient]。
     * 与 [testConnection] 共用同一套 baseUrl 构造逻辑，避免在调用方（预览页 ViewModel）
     * 重复实现。
     * 
     * Hosts：读取 [SettingsRepository.hostsConfig]，通过 [HostsDnsFactory] 把 hosts
     * 解析挂到 OkHttpClient（开关关闭或 hostname 未命中时回退系统 DNS）。
     * 
     * 仅用于预览阶段的 PROPFIND 探测（冲突检测、伴随文件发现），不在此执行 MOVE/MKCOL。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clientFor(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigEntity entity, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.core.webdav.WebDavClient> $completion) {
        return null;
    }

    /**
     * 根据 [ServerConfigEntity.https] 标志、host（自 baseUrl 去掉 scheme）与可选 port
     * 拼出完整 baseUrl，如 `https://dav.example.com:8443`。
     */
    private final java.lang.String buildFullBaseUrl(com.webdavrenamer.data.db.ServerConfigEntity entity) {
        return null;
    }
}
