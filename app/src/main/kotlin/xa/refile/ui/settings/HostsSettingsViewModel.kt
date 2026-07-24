package xa.refile.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xa.refile.core.backup.HostEntry
import xa.refile.core.backup.HostPresets
import xa.refile.core.backup.HostsConfig
import xa.refile.core.backup.HostsIpResolver
import xa.refile.core.backup.HostsSpeedTest
import xa.refile.core.backup.HostsSpeedTest.IpSpeedTestResult
import xa.refile.data.prefs.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Hosts 设置 ViewModel（spec §5.3.3–5.3.5）。
 *
 * 状态：
 * - [hostsConfig]：从 [SettingsRepository.hostsConfig] 派生的可观察配置（开关 + 条目）。
 * - [testing]：测速进行中标志，UI 用以禁用按钮/显示进度。
 * - [testResults]：每个 hostname 的测速结果列表（hostname → 各 IP 结果）。
 *
 * 方法覆盖 spec §5.3.4 UI 的所有操作：
 * - [toggleEnabled]：总开关持久化（§5.3.5）。
 * - [addHost]/[editHost]/[removeHost]：条目 CRUD。
 * - [testConnection]：测指定 hostname 全部 IP。
 * - [testAllConnections]：测所有 hostname，并行。
 * - [autoPickFastest]：测速后选延迟最低可用 IP 设为该 hostname 唯一 ips（§5.3.3）。
 * - [applyPreset]：把预设域名填入条目，并通过 DoH 自动解析候选 IP（测试反馈 Item 13）。
 *
 * Hosts 写入全部经 [SettingsRepository.setHostsConfig] 落盘，OkHttpClient 在使用方
 * （[xa.refile.core.tmdb.TmdbClient] 与 [xa.refile.data.repository.ServerRepository]）
 * 构造时读取该 Flow 应用 [xa.refile.core.backup.HostsDns]。
 */
@HiltViewModel
class HostsSettingsViewModel @Inject constructor(
    private val settings: SettingsRepository,
    private val speedTest: HostsSpeedTest,
    private val ipResolver: HostsIpResolver,
) : ViewModel() {

    /** 预设按钮展示用：name → 标签。 */
    data class PresetOption(val name: String, val label: String)

    /** 当前 hosts 配置（开关 + 条目）。 */
    val hostsConfig: StateFlow<HostsConfig> = settings.hostsConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), HostsConfig())

    /** 是否正在测速。 */
    private val _testing = MutableStateFlow(false)
    val testing: StateFlow<Boolean> = _testing.asStateFlow()

    /** 是否正在解析 IP（DoH）。 */
    private val _resolving = MutableStateFlow(false)
    val resolving: StateFlow<Boolean> = _resolving.asStateFlow()

    /** 各 hostname 的测速结果。 */
    private val _testResults = MutableStateFlow<Map<String, List<IpSpeedTestResult>>>(emptyMap())
    val testResults: StateFlow<Map<String, List<IpSpeedTestResult>>> = _testResults.asStateFlow()

    /** 解析/测速的提示消息（成功/失败），由 Composable 弹 Snackbar。 */
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    /** 预设列表，供 UI 渲染按钮行。 */
    val presetOptions: List<PresetOption> = listOf(
        PresetOption(PRESET_TMDB_API, "TMDB API"),
        PresetOption(PRESET_TMDB_IMAGE, "TMDB Image"),
        PresetOption(PRESET_DEFAULT_CANDIDATES, "默认候选"),
    )

    /** 切换总开关（§5.3.5）。 */
    fun toggleEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            settings.setHostsConfig(current.copy(enabled = enabled))
        }
    }

    /** 新增 hostname（若已存在则覆盖 ips）。 */
    fun addHost(hostname: String, ips: List<String>) {
        val name = hostname.trim()
        if (name.isEmpty()) return
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            val newEntries = upsertEntry(current.entries, name, ips)
            settings.setHostsConfig(current.copy(entries = newEntries))
        }
    }

    /** 编辑指定 hostname 的 ips 列表。 */
    fun editHost(hostname: String, newIps: List<String>) {
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            val newEntries = upsertEntry(current.entries, hostname, newIps)
            settings.setHostsConfig(current.copy(entries = newEntries))
        }
    }

    /** 删除指定 hostname 条目并清除其测速结果。 */
    fun removeHost(hostname: String) {
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            val newEntries = current.entries
                .filterNot { it.hostname.equals(hostname, ignoreCase = true) }
            settings.setHostsConfig(current.copy(entries = newEntries))
            _testResults.update { results ->
                results.filterKeys { !it.equals(hostname, ignoreCase = true) }
            }
        }
    }

    /** 测指定 hostname 的所有 IP（§5.3.4 连接测试按钮）。 */
    fun testConnection(hostname: String) {
        viewModelScope.launch {
            val ips = currentIpsFor(hostname)
            if (ips.isEmpty()) return@launch
            _testing.value = true
            try {
                val results = speedTest.testAllIps(hostname, ips)
                _testResults.update { it + (hostname to results) }
            } finally {
                _testing.value = false
            }
        }
    }

    /** 测所有 hostname 的所有 IP，并行（§5.3.4 测试所有连接）。 */
    fun testAllConnections() {
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            if (current.entries.isEmpty()) return@launch
            _testing.value = true
            try {
                val all = coroutineScope {
                    current.entries.map { entry ->
                        async { entry.hostname to speedTest.testAllIps(entry.hostname, entry.ips) }
                    }.awaitAll().toMap()
                }
                _testResults.value = all
            } finally {
                _testing.value = false
            }
        }
    }

    /** 测速后选延迟最低可用 IP 设为该 hostname 唯一 ips（§5.3.3 自动测速）。 */
    fun autoPickFastest(hostname: String) {
        viewModelScope.launch {
            val ips = currentIpsFor(hostname)
            if (ips.isEmpty()) return@launch
            _testing.value = true
            try {
                val fastest = speedTest.pickFastest(hostname, ips)
                if (fastest != null) {
                    editHost(hostname, listOf(fastest.ip))
                    _testResults.update { it + (hostname to listOf(fastest)) }
                }
            } finally {
                _testing.value = false
            }
        }
    }

    /**
     * 应用预设（添加对应 hostname 条目，并通过 DoH 自动解析候选 IP）。
     *
     * 测试反馈 Item 13：原实现 ips 留空导致「测试」「自动选优」按钮不可用。
     * 现在添加 hostname 后立即通过 [HostsIpResolver] 解析候选 IP 并填入，
     * 解析失败时 ips 仍为空，用户可手动填入或稍后点「解析 IP」重试。
     */
    fun applyPreset(presetName: String) {
        val hostnamesToAdd = when (presetName) {
            PRESET_TMDB_API -> listOf(HostPresets.TMDB_API)
            PRESET_TMDB_IMAGE -> listOf(HostPresets.TMDB_IMAGE)
            PRESET_DEFAULT_CANDIDATES -> HostPresets.DEFAULT_CANDIDATES
            else -> emptyList()
        }
        if (hostnamesToAdd.isEmpty()) return
        viewModelScope.launch {
            val current = settings.hostsConfig.first()
            // 先插入 hostname 条目（ips 暂空），再逐个 DoH 解析填充
            val newEntries = hostnamesToAdd.fold(current.entries) { acc, hostname ->
                if (acc.any { it.hostname.equals(hostname, ignoreCase = true) }) {
                    acc // 已存在则不覆盖（保留用户已配置的 ips）
                } else {
                    acc + HostEntry(hostname = hostname, ips = emptyList())
                }
            }
            settings.setHostsConfig(current.copy(entries = newEntries))

            // 对新增（ips 为空）的 hostname 自动 DoH 解析候选 IP
            _resolving.value = true
            try {
                val toResolve = hostnamesToAdd.filter { hostname ->
                    newEntries.firstOrNull { it.hostname.equals(hostname, ignoreCase = true) }?.ips.isNullOrEmpty()
                }
                var resolvedCount = 0
                toResolve.forEach { hostname ->
                    val ips = ipResolver.resolve(hostname)
                    if (ips.isNotEmpty()) {
                        editHost(hostname, ips)
                        resolvedCount++
                    }
                }
                _message.value = if (toResolve.isEmpty()) {
                    "预设已应用（域名已存在）"
                } else if (resolvedCount == toResolve.size) {
                    "预设已应用，已解析 ${toResolve.size} 个域名的候选 IP"
                } else {
                    "预设已应用，${resolvedCount}/${toResolve.size} 个域名解析成功，未成功的可手动填入 IP"
                }
            } finally {
                _resolving.value = false
            }
        }
    }

    /**
     * 通过 DoH 解析指定 hostname 的候选 IP 并更新条目（测试反馈 Item 13）。
     *
     * 供 UI「解析 IP」按钮调用：当 ips 为空或需刷新时重新解析。
     */
    fun resolveIps(hostname: String) {
        viewModelScope.launch {
            _resolving.value = true
            try {
                val ips = ipResolver.resolve(hostname)
                if (ips.isNotEmpty()) {
                    editHost(hostname, ips)
                    _message.value = "已解析 ${ips.size} 个候选 IP"
                } else {
                    _message.value = "解析失败，请检查网络或手动填入 IP"
                }
            } finally {
                _resolving.value = false
            }
        }
    }

    /** 清除提示消息。 */
    fun clearMessage() {
        _message.value = null
    }

    /** 取当前 hostname 的 ips 列表。 */
    private suspend fun currentIpsFor(hostname: String): List<String> =
        settings.hostsConfig.first()
            .entries
            .firstOrNull { it.hostname.equals(hostname, ignoreCase = true) }
            ?.ips
            ?: emptyList()

    /** 插入或更新条目（按 hostname 忽略大小写匹配）。 */
    private fun upsertEntry(
        entries: List<HostEntry>,
        hostname: String,
        ips: List<String>,
    ): List<HostEntry> {
        val existing = entries.firstOrNull { it.hostname.equals(hostname, ignoreCase = true) }
        val cleanedIps = ips.map { it.trim() }.filter { it.isNotEmpty() }
        return if (existing != null) {
            entries.map { if (it.hostname.equals(hostname, ignoreCase = true)) it.copy(ips = cleanedIps) else it }
        } else {
            entries + HostEntry(hostname = hostname, ips = cleanedIps)
        }
    }

    private companion object {
        const val PRESET_TMDB_API = "TMDB_API"
        const val PRESET_TMDB_IMAGE = "TMDB_IMAGE"
        const val PRESET_DEFAULT_CANDIDATES = "DEFAULT_CANDIDATES"
    }
}
