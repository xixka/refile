package xa.refile.data.repository

import xa.refile.core.backup.HostsDnsFactory
import xa.refile.core.webdav.ConnectionResult
import xa.refile.core.webdav.WebDavClient
import xa.refile.data.crypto.KeystoreCrypto
import xa.refile.data.db.ServerConfigDao
import xa.refile.data.db.ServerConfigEntity
import xa.refile.data.prefs.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

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
class ServerRepository @Inject constructor(
    private val dao: ServerConfigDao,
    private val crypto: KeystoreCrypto,
    private val settings: SettingsRepository,
) {

    /** 观察所有服务器配置（按 updatedAt 倒序）。 */
    fun observeServers(): Flow<List<ServerConfigEntity>> = dao.observeAll()

    /** 按 id 取单条配置。 */
    suspend fun getServer(id: Long): ServerConfigEntity? = dao.getById(id)

    /**
     * 新增服务器配置。明文密码先经 Keystore 加密再落盘。
     *
     * @return 新插入行的 id。
     */
    suspend fun addServer(
        name: String,
        baseUrl: String,
        port: Int?,
        rootPath: String,
        username: String?,
        password: String?,
        authType: String,
        https: Boolean,
    ): Long {
        val encrypted = password?.takeIf { it.isNotBlank() }?.let { crypto.encrypt(it) }
        val entity = ServerConfigEntity(
            name = name,
            baseUrl = baseUrl,
            port = port,
            rootPath = rootPath,
            username = username,
            encryptedPassword = encrypted,
            authType = authType,
            https = https,
        )
        return dao.insert(entity)
    }

    /**
     * 更新服务器配置。
     * - [newPassword] 非空且非空白：重新加密并替换 [ServerConfigEntity.encryptedPassword]。
     * - 否则：保留原密文，仅更新其它字段与 [ServerConfigEntity.updatedAt]。
     */
    suspend fun updateServer(entity: ServerConfigEntity, newPassword: String?) {
        val now = System.currentTimeMillis()
        val updated = if (!newPassword.isNullOrBlank()) {
            entity.copy(
                encryptedPassword = crypto.encrypt(newPassword),
                updatedAt = now,
            )
        } else {
            entity.copy(updatedAt = now)
        }
        dao.update(updated)
    }

    /** 按 id 删除服务器配置。 */
    suspend fun deleteServer(id: Long) {
        dao.deleteById(id)
    }

    /**
     * 测试与目标服务器的连通性。
     *
     * 解密存储的密码，根据 [ServerConfigEntity.https] + host + port 拼出完整 baseUrl，
     * 构造 [WebDavClient] 并对其 rootPath 发起 PROPFIND Depth 0 测试。
     *
     * 仅做连通性/认证探测，不下载文件内容。
     */
    suspend fun testConnection(entity: ServerConfigEntity): ConnectionResult {
        val decryptedPassword = entity.encryptedPassword?.let { crypto.decrypt(it) }
        val fullBaseUrl = buildFullBaseUrl(entity)
        val client = WebDavClient(fullBaseUrl, entity.username, decryptedPassword)
        return client.testConnection(entity.rootPath)
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
    suspend fun clientFor(entity: ServerConfigEntity): WebDavClient {
        val decryptedPassword = entity.encryptedPassword?.let { crypto.decrypt(it) }
        val fullBaseUrl = buildFullBaseUrl(entity)
        val hostsConfig = settings.hostsConfig.first()
        val client = HostsDnsFactory.createOkHttpClientWithHosts(hostsConfig)
        return WebDavClient(fullBaseUrl, entity.username, decryptedPassword, client)
    }

    /**
     * 根据 [ServerConfigEntity.https] 标志、host（自 baseUrl 去掉 scheme）与可选 port
     * 拼出完整 baseUrl，如 `https://dav.example.com:8443`。
     */
    private fun buildFullBaseUrl(entity: ServerConfigEntity): String {
        val scheme = if (entity.https) "https" else "http"
        val host = entity.baseUrl
            .trim()
            .removePrefix("https://")
            .removePrefix("http://")
            .trimEnd('/')
        return if (entity.port != null) {
            "$scheme://$host:${entity.port}"
        } else {
            "$scheme://$host"
        }
    }
}
