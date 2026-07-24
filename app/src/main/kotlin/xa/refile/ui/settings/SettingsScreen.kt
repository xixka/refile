package xa.refile.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import xa.refile.core.naming.Preset
import xa.refile.ui.theme.ErrorRed
import xa.refile.ui.theme.SuccessGreen

/**
 * 设置中心页（计划 §M5 Task 5.4）。
 *
 * 作为所有子设置功能的统一入口，按分组卡片组织：
 * - 组 1 TMDB 配置：API Key 输入（密码态可切换）+ 校验状态 + 语言偏好下拉 + 强制目录类型开关（测试反馈 Item 2：移除申请 TMDB API 按钮，国内无需）。
 * - 组 2 命名与模板：模板编辑器入口（展示当前预设）+ 命名选项入口。
 * - 组 3 数据管理：备份与恢复 + 历史记录。
 * - 组 4 网络：Hosts 设置。
 *
 * 列表项统一用 [SettingsRow]（图标 + 标题 + 副标题 + 右箭头 + 点击）。
 * 子页跳转通过 [SettingsViewModel.events] 一次性事件驱动（除历史记录直接走 [onOpenHistory]）。
 *
 * @param onBack 返回服务器列表。
 * @param onOpenTemplateEditor 跳转模板编辑器。
 * @param onOpenBackup 跳转备份与恢复。
 * @param onOpenHostsSettings 跳转 Hosts 设置。
 * @param onOpenHistory 跳转历史记录。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenTemplateEditor: () -> Unit,
    onOpenBackup: () -> Unit,
    onOpenHostsSettings: () -> Unit,
    onOpenHistory: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()
    val apiKeyValid by viewModel.apiKeyValid.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val presetId by viewModel.presetId.collectAsStateWithLifecycle()
    val forceType by viewModel.forceType.collectAsStateWithLifecycle()
    val versionName by viewModel.versionName.collectAsStateWithLifecycle()
    val exportingLog by viewModel.exportingLog.collectAsStateWithLifecycle()
    val logExportResult by viewModel.logExportResult.collectAsStateWithLifecycle()

    // API Key 输入框本地态：避免每次按键直接回写 DataStore 造成光标跳动
    var apiKeyInput by rememberSaveable { mutableStateOf("") }
    // 仅在本地为空且远端有值时同步一次（首次加载已保存的 Key），之后由用户输入驱动
    LaunchedEffect(apiKey) {
        if (apiKeyInput.isEmpty() && apiKey.isNotEmpty()) apiKeyInput = apiKey
    }
    var showApiKey by rememberSaveable { mutableStateOf(false) }

    // SAF 启动器：导出调试日志（CreateDocument，文本 .log）
    val logLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain"),
    ) { uri ->
        if (uri != null) viewModel.writeDebugLog(uri)
    }
    val snackbarHostState = remember { SnackbarHostState() }

    // 收集一次性导航事件，分发到对应导航回调
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SettingsNavEvent.OpenTemplateEditor -> onOpenTemplateEditor()
                SettingsNavEvent.OpenBackup -> onOpenBackup()
                SettingsNavEvent.OpenHostsSettings -> onOpenHostsSettings()
                SettingsNavEvent.PickLogFile ->
                    logLauncher.launch("refile-debug-${System.currentTimeMillis()}.log")
            }
        }
    }

    // 调试日志导出结果弹 Snackbar
    LaunchedEffect(logExportResult) {
        logExportResult?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearLogExportResult()
        }
    }

    val presetLabel = remember(presetId) {
        // 内置预设（EMBY/INFUSE）按 id 取显示名；其余（含旧版 CUSTOM 与用户新建预设）显示「自定义」
        Preset.entries.firstOrNull { it.name == presetId }?.displayName ?: "自定义"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // ---------- 组 1：TMDB 配置 ----------
            item {
                SettingsSection(title = "TMDB 配置") {
                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = {
                            apiKeyInput = it
                            viewModel.setApiKey(it)
                        },
                        label = { Text("API Key") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { showApiKey = !showApiKey }) {
                                Icon(
                                    if (showApiKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (showApiKey) "隐藏 Key" else "显示 Key",
                                )
                            }
                        },
                        visualTransformation = if (showApiKey) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    // API Key 校验状态
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (apiKeyValid) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (apiKeyValid) SuccessGreen else ErrorRed,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (apiKeyValid) "Key 已配置" else "未配置",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (apiKeyValid) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Spacer(Modifier.size(4.dp))

                    // 语言偏好下拉
                    LanguageDropdown(
                        selectedCode = language,
                        options = viewModel.availableLanguages,
                        onSelect = viewModel::setLanguage,
                    )

                    Spacer(Modifier.size(4.dp))

                    // 强制指定目录类型
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "强制指定目录类型",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "开启后匹配时强制按所选类型，不自动判定",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Switch(
                            checked = forceType,
                            onCheckedChange = viewModel::setForceType,
                        )
                    }
                }
            }

            // ---------- 组 2：命名与模板 ----------
            item {
                SettingsSection(title = "命名与模板") {
                    SettingsRow(
                        icon = Icons.Default.Description,
                        title = "模板编辑器",
                        subtitle = "当前：$presetLabel · 分隔符 / 大小写 / 非法字符 / 补零",
                        onClick = viewModel::openTemplateEditor,
                    )
                }
            }

            // ---------- 组 3：数据管理 ----------
            item {
                SettingsSection(title = "数据管理") {
                    SettingsRow(
                        icon = Icons.Default.Backup,
                        title = "备份与恢复",
                        subtitle = "导出/导入设置与服务器",
                        onClick = viewModel::openBackup,
                    )
                    SettingsRow(
                        icon = Icons.Default.History,
                        title = "历史记录",
                        subtitle = "查看重命名历史与撤销",
                        onClick = onOpenHistory,
                    )
                }
            }

            // ---------- 组 4：网络 ----------
            item {
                SettingsSection(title = "网络") {
                    SettingsRow(
                        icon = Icons.Default.Dns,
                        title = "Hosts 设置",
                        subtitle = "自定义 DNS 与直连",
                        onClick = viewModel::openHostsSettings,
                    )
                }
            }

            // ---------- 组 5：关于 ----------
            item {
                SettingsSection(title = "关于") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "WebDAV Renamer",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                text = "版本 $versionName",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    Button(
                        onClick = viewModel::pickLogFile,
                        enabled = !exportingLog,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (exportingLog) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("导出中…")
                        } else {
                            Text("导出调试日志")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 分组容器：小字灰色组标题 + 卡片内容。
 *
 * 卡片内统一以 12.dp 垂直间距排列子项。
 */
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                content()
            }
        }
    }
}

/**
 * 通用设置列表项：图标 + 标题 + 副标题 + 右箭头 + 点击。
 */
@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * 语言偏好下拉选择器（ExposedDropdownMenuBox）。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageDropdown(
    selectedCode: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.first == selectedCode }?.second
        ?: options.firstOrNull()?.second
        ?: selectedCode

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "语言偏好",
            style = MaterialTheme.typography.labelLarge,
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                value = selectedLabel,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { (code, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            onSelect(code)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
