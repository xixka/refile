package com.webdavrenamer.ui.settings

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.webdavrenamer.core.backup.HostEntry
import com.webdavrenamer.core.backup.HostsSpeedTest.IpSpeedTestResult
import com.webdavrenamer.ui.theme.ErrorRed
import com.webdavrenamer.ui.theme.SuccessGreen
import com.webdavrenamer.ui.theme.WarningAmber

/**
 * Hosts 设置页（spec §5.3.4 连接测试按钮 UI + §5.3.5 总开关 UI）。
 *
 * 布局：
 * - TopAppBar「Hosts 设置」+ 返回。
 * - 总开关 [Switch]（启用/禁用 [com.webdavrenamer.core.backup.HostsDns]）。
 * - 预设按钮行：TMDB API / TMDB Image / 默认候选，点击填入对应 hostname（ips 留空待测速）。
 * - 「新增 Host」按钮 → 弹出编辑对话框。
 * - hostname 列表（LazyColumn）：每行 hostname + IP 列表 + 测试/自动选优/编辑/删除按钮 + 测速结果。
 * - 测速中显示 [CircularProgressIndicator]。
 * - 底部「测试所有连接」按钮。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostsSettingsScreen(
    onBack: () -> Unit,
    viewModel: HostsSettingsViewModel = hiltViewModel(),
) {
    val config by viewModel.hostsConfig.collectAsStateWithLifecycle()
    val testing by viewModel.testing.collectAsStateWithLifecycle()
    val testResults by viewModel.testResults.collectAsStateWithLifecycle()

    // 新增对话框状态：非 null 表示正在新增
    var addingNew by remember { mutableStateOf(false) }
    // 编辑对话框状态：非 null hostname 表示正在编辑该 hostname 的 ips
    var editingHost by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hosts 设置") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = { viewModel.testAllConnections() },
                    enabled = !testing && config.entries.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (testing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("测试中...")
                    } else {
                        Text("测试所有连接")
                    }
                }
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // 总开关卡片
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "启用 Hosts",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "开启后命中下方条目的域名将按 hosts 解析；关闭则全部走系统 DNS",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Switch(
                            checked = config.enabled,
                            onCheckedChange = viewModel::toggleEnabled,
                        )
                    }
                }
            }

            // 预设按钮行
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "预设",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        viewModel.presetOptions.forEach { preset ->
                            AssistChip(
                                onClick = { viewModel.applyPreset(preset.name) },
                                label = { Text(preset.label) },
                            )
                        }
                    }
                }
            }

            // 新增按钮
            item {
                OutlinedButton(
                    onClick = { addingNew = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("新增 Host")
                }
            }

            // hostname 条目列表
            items(config.entries, key = { it.hostname.lowercase() }) { entry ->
                HostEntryCard(
                    entry = entry,
                    testing = testing,
                    results = testResults[entry.hostname],
                    onTest = { viewModel.testConnection(entry.hostname) },
                    onAutoPick = { viewModel.autoPickFastest(entry.hostname) },
                    onEdit = { editingHost = entry.hostname },
                    onRemove = { viewModel.removeHost(entry.hostname) },
                )
            }
        }
    }

    // 新增对话框
    if (addingNew) {
        HostEditDialog(
            initialHostname = "",
            initialIps = "",
            title = "新增 Host",
            hostnameEditable = true,
            onConfirm = { host, ips ->
                viewModel.addHost(host, ips)
                addingNew = false
            },
            onDismiss = { addingNew = false },
        )
    }

    // 编辑对话框
    editingHost?.let { hostname ->
        val existing = config.entries.firstOrNull { it.hostname.equals(hostname, ignoreCase = true) }
        if (existing != null) {
            HostEditDialog(
                initialHostname = existing.hostname,
                initialIps = existing.ips.joinToString("\n"),
                title = "编辑 Host",
                hostnameEditable = false,
                onConfirm = { _, ips ->
                    viewModel.editHost(hostname, ips)
                    editingHost = null
                },
                onDismiss = { editingHost = null },
            )
        }
    }
}

/** 单条 hostname 卡片：标题 + IP 列表 + 测速结果 + 操作按钮。 */
@Composable
private fun HostEntryCard(
    entry: HostEntry,
    testing: Boolean,
    results: List<IpSpeedTestResult>?,
    onTest: () -> Unit,
    onAutoPick: () -> Unit,
    onEdit: () -> Unit,
    onRemove: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = entry.hostname,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "编辑",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }

            // IP 列表
            if (entry.ips.isEmpty()) {
                Text(
                    text = "（未配置 IP，点击编辑添加候选 IP，每行一个）",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                entry.ips.forEach { ip ->
                    Text(
                        text = "• $ip",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            // 测速结果
            if (!results.isNullOrEmpty()) {
                Spacer(Modifier.size(4.dp))
                Text(
                    text = "测速结果",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                results.forEach { result ->
                    IpResultRow(result)
                }
            }

            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onTest,
                    enabled = !testing && entry.ips.isNotEmpty(),
                ) {
                    Text("测试")
                }
                OutlinedButton(
                    onClick = onAutoPick,
                    enabled = !testing && entry.ips.size > 1,
                ) {
                    Text("自动选优")
                }
            }
        }
    }
}

/** 单个 IP 测速结果行：延迟（颜色编码）+ 状态码 + 错误信息。 */
@Composable
private fun IpResultRow(result: IpSpeedTestResult) {
    val latency = result.latencyMs
    val color = when {
        !result.isAvailable -> ErrorRed
        latency == null -> ErrorRed
        latency < 200L -> SuccessGreen
        latency < 1000L -> WarningAmber
        else -> ErrorRed
    }
    val latencyText = result.latencyMs?.let { "${it} ms" } ?: "—"
    val statusText = result.statusCode?.let { "HTTP $it" } ?: ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = result.ip,
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = latencyText,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.SemiBold,
        )
        if (statusText.isNotEmpty()) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    result.errorMessage?.let { msg ->
        Text(
            text = msg,
            style = MaterialTheme.typography.labelSmall,
            color = ErrorRed,
        )
    }
}

/** 新增/编辑对话框。hostname 在新增时可编辑，编辑时只读。 */
@Composable
private fun HostEditDialog(
    initialHostname: String,
    initialIps: String,
    title: String,
    hostnameEditable: Boolean,
    onConfirm: (hostname: String, ips: List<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    var hostField by remember { mutableStateOf(initialHostname) }
    var ipsField by remember { mutableStateOf(initialIps) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = hostField,
                    onValueChange = { hostField = it },
                    label = { Text("域名") },
                    singleLine = true,
                    enabled = hostnameEditable,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = ipsField,
                    onValueChange = { ipsField = it },
                    label = { Text("IP 列表（每行一个或逗号分隔）") },
                    supportingText = {
                        Text("测速后可点「自动选优」从候选中选最快 IP")
                    },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val host = hostField.trim()
                    if (host.isEmpty()) return@TextButton
                    val ips = ipsField
                        .split(",", "\n")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                    onConfirm(host, ips)
                },
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}
