package com.webdavrenamer.ui.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webdavrenamer.core.webdav.ConnectionResult
import com.webdavrenamer.data.crypto.KeystoreCrypto
import com.webdavrenamer.data.db.ServerConfigEntity
import com.webdavrenamer.data.repository.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 添加/编辑服务器页 ViewModel（计划 §M1 SubTask 1.4.2）。
 *
 * 持有表单的可变 UI 状态，负责：
 * - [load]：编辑模式预填（密码字段留空表示"保留原密码"，明文密码绝不回显，红线）。
 * - [testConnection]：用当前输入构造临时 [ServerConfigEntity] 调 [ServerRepository.testConnection]，
 *   把 [ConnectionResult] 映射为可读文案。
 * - [save]：新增调 [ServerRepository.addServer]，编辑调 [ServerRepository.updateServer]；返回 id 或抛异常。
 *
 * 说明：[ServerRepository.testConnection] 会对 [ServerConfigEntity.encryptedPassword] 解密后使用，
 * 因此为了用"当前输入的明文密码"测试连接，需先经 [KeystoreCrypto] 加密形成临时 entity
 * （加密→仓库内解密为同一明文，round-trip），否则无法对尚未保存的新密码做连通性测试。
 * [KeystoreCrypto] 仅用于此 round-trip，不在 UI 层持久化任何密文。
 */
@HiltViewModel
class ServerEditViewModel @Inject constructor(
    private val repo: ServerRepository,
    private val crypto: KeystoreCrypto,
) : ViewModel() {

    /** 测试连接结果的 UI 投影。 */
    sealed interface TestResultUi {
        data class Success(val message: String = "连接成功") : TestResultUi
        data class Error(val message: String) : TestResultUi
    }

    /** 表单 UI 状态。 */
    data class UiState(
        val id: Long? = null,
        val name: String = "",
        val baseUrl: String = "",
        val port: String = "",
        val rootPath: String = "/",
        val username: String = "",
        val password: String = "",
        val authType: String = "auto",
        val https: Boolean = true,
        val isEditing: Boolean = false,
        val isTesting: Boolean = false,
        val isSaving: Boolean = false,
        val testResult: TestResultUi? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    /** 编辑模式预填时缓存的原始实体，用于"密码留空=保留原密文"分支。 */
    private var loadedEntity: ServerConfigEntity? = null

    fun updateName(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun updateBaseUrl(value: String) {
        _uiState.update { it.copy(baseUrl = value) }
    }

    fun updatePort(value: String) {
        _uiState.update { it.copy(port = value.filter { ch -> ch.isDigit() }) }
    }

    fun updateRootPath(value: String) {
        _uiState.update { it.copy(rootPath = value) }
    }

    fun updateUsername(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun updateAuthType(value: String) {
        _uiState.update { it.copy(authType = value) }
    }

    fun updateHttps(value: Boolean) {
        _uiState.update { it.copy(https = value) }
    }

    /** 编辑模式预填。id 为 null 或 <=0 视为新增。 */
    suspend fun load(id: Long?) {
        if (id == null || id <= 0L) {
            loadedEntity = null
            _uiState.update { it.copy(isEditing = false) }
            return
        }
        val entity = repo.getServer(id) ?: run {
            _uiState.update { it.copy(isEditing = false) }
            return
        }
        loadedEntity = entity
        _uiState.update {
            it.copy(
                id = entity.id,
                name = entity.name,
                baseUrl = entity.baseUrl,
                port = entity.port?.toString() ?: "",
                rootPath = entity.rootPath,
                username = entity.username ?: "",
                password = "", // 明文密码不回显（红线）
                authType = entity.authType,
                https = entity.https,
                isEditing = true,
            )
        }
    }

    /** 构造用于"测试连接"的临时实体。密码用当前输入（加密后 round-trip），留空则沿用原密文。 */
    private fun buildTempEntity(): ServerConfigEntity {
        val s = _uiState.value
        val portInt = s.port.trim().toIntOrNull()
        val encrypted = if (s.password.isNotEmpty()) {
            crypto.encrypt(s.password)
        } else {
            loadedEntity?.encryptedPassword
        }
        return ServerConfigEntity(
            id = s.id ?: 0L,
            name = s.name,
            baseUrl = s.baseUrl,
            port = portInt,
            rootPath = s.rootPath.ifBlank { "/" },
            username = s.username.trim().ifBlank { null },
            encryptedPassword = encrypted,
            authType = s.authType,
            https = s.https,
        )
    }

    /** 用当前输入测试连接，结果写入 [uiState.testResult]。 */
    fun testConnection() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTesting = true, testResult = null) }
            val mapped = try {
                mapResult(repo.testConnection(buildTempEntity()))
            } catch (t: Throwable) {
                TestResultUi.Error("网络错误：${t.message ?: "未知错误"}")
            }
            _uiState.update { it.copy(isTesting = false, testResult = mapped) }
        }
    }

    private fun mapResult(result: ConnectionResult): TestResultUi = when (result) {
        is ConnectionResult.Success -> TestResultUi.Success()
        is ConnectionResult.AuthFailure ->
            TestResultUi.Error("认证失败（${result.code}），请检查用户名密码")
        is ConnectionResult.NotWebDav -> TestResultUi.Error("目标不是 WebDAV 服务")
        is ConnectionResult.HttpError -> TestResultUi.Error("HTTP 错误 ${result.code}")
        is ConnectionResult.NetworkError -> TestResultUi.Error("网络错误：${result.message}")
    }

    /**
     * 新增或更新。返回 id；校验失败或仓库异常时抛出，由 UI 捕获展示。
     */
    suspend fun save(): Long {
        val s = _uiState.value
        require(s.name.isNotBlank()) { "名称不能为空" }
        require(s.baseUrl.isNotBlank()) { "Base URL 不能为空" }
        val portStr = s.port.trim()
        val portInt = if (portStr.isEmpty()) null else portStr.toIntOrNull()
        require(portStr.isEmpty() || portInt != null) { "端口格式错误" }

        _uiState.update { it.copy(isSaving = true) }
        try {
            val rootPath = s.rootPath.ifBlank { "/" }
            val username = s.username.trim().ifBlank { null }
            val newPassword = s.password.ifBlank { null }

            return if (s.isEditing && s.id != null && loadedEntity != null) {
                val entity = loadedEntity!!.copy(
                    name = s.name,
                    baseUrl = s.baseUrl,
                    port = portInt,
                    rootPath = rootPath,
                    username = username,
                    authType = s.authType,
                    https = s.https,
                )
                repo.updateServer(entity, newPassword)
                s.id
            } else {
                repo.addServer(
                    name = s.name,
                    baseUrl = s.baseUrl,
                    port = portInt,
                    rootPath = rootPath,
                    username = username,
                    password = newPassword,
                    authType = s.authType,
                    https = s.https,
                )
            }
        } finally {
            _uiState.update { it.copy(isSaving = false) }
        }
    }
}
