package xa.refile.ui.settings

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import xa.refile.core.naming.NamingOptions
import xa.refile.core.naming.Preset
import xa.refile.data.prefs.VisualOptions
import xa.refile.ui.theme.WarningAmber
import kotlinx.coroutines.launch

/**
 * 模板编辑器页（计划 §M3 SubTask 3.3.1）。
 *
 * - 顶部栏：返回 + 保存按钮。
 * - 预设选择器（Plex/Kodi/Emby/Jellyfin/自定义）。
 * - 模板字符串输入框（多行 monospace），绑定 [TextFieldValue] 跟踪光标。
 * - 变量插入 chip 行（按组横向滚动，点击在光标处插入 `{token}`）。
 * - 可视化选项：分隔符 / 大小写 / 非法字符处理 / 补零位数 Slider。
 * - 实时预览卡片：电影 + 剧集两份示例渲染结果。
 * - 保存：写入 DataStore，Snackbar 反馈。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateEditorScreen(
    onBack: () -> Unit,
    viewModel: TemplateEditorViewModel = hiltViewModel(),
) {
    val templateField by viewModel.templateField.collectAsStateWithLifecycle()
    val presetId by viewModel.presetId.collectAsStateWithLifecycle()
    val visualOptions by viewModel.visualOptions.collectAsStateWithLifecycle()
    val preview by viewModel.previewResult.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isSaving by remember { mutableStateOf(false) }

    val save: () -> Unit = {
        if (!isSaving) {
            isSaving = true
            scope.launch {
                try {
                    viewModel.save()
                    snackbarHostState.showSnackbar("已保存")
                } catch (e: Exception) {
                    snackbarHostState.showSnackbar("保存失败：${e.message ?: "未知错误"}")
                } finally {
                    isSaving = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("模板编辑器") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = save, enabled = !isSaving) {
                        Icon(Icons.Default.Save, contentDescription = "保存")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PresetSelector(
                selectedId = presetId,
                onSelect = viewModel::selectPreset,
            )

            OutlinedTextField(
                value = templateField,
                onValueChange = viewModel::updateTemplate,
                label = { Text("模板字符串") },
                supportingText = {
                    Text("变量用 {n} {y} {s00e00} 等，管道 {n|upper}，路径用 / 分段")
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.Monospace,
                ),
                minLines = 4,
                maxLines = 8,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp),
            )

            VariableChips(
                tokens = viewModel.availableVariables,
                onInsert = viewModel::insertVariable,
            )

            VisualOptionsSection(
                options = visualOptions,
                onUpdate = viewModel::saveVisualOptions,
            )

            PreviewCard(preview = preview)

            Button(
                onClick = save,
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("保存中...")
                } else {
                    Text("保存")
                }
            }
        }
    }
}

/** 预设选择下拉。Plex/Kodi/Emby/Jellyfin 来自 [Preset]，外加"自定义"。 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresetSelector(
    selectedId: String,
    onSelect: (String) -> Unit,
) {
    val options: List<Pair<String, String>> = Preset.entries.map { it.name to it.displayName } +
        ("CUSTOM" to "自定义")
    val selectedLabel = options.firstOrNull { it.first == selectedId }?.second
        ?: options.first().second
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("预设", style = MaterialTheme.typography.labelLarge)
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
                options.forEach { (id, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            onSelect(id)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

/** 变量插入 chip：按组分行，每组横向滚动。 */
@Composable
private fun VariableChips(
    tokens: List<TemplateEditorViewModel.VariableToken>,
    onInsert: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("插入变量", style = MaterialTheme.typography.labelLarge)
        val grouped = tokens.groupBy { it.group }
        grouped.forEach { (group, items) ->
            Text(
                text = group,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items.forEach { token ->
                    AssistChip(
                        onClick = { onInsert(token.token) },
                        label = { Text("{${token.token}} · ${token.label}") },
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }
            }
        }
    }
}

/** 可视化选项：分隔符 / 大小写 / 非法字符 / 补零位数。 */
@Composable
private fun VisualOptionsSection(
    options: VisualOptions,
    onUpdate: (VisualOptions) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("可视化选项", style = MaterialTheme.typography.labelLarge)

        // 分隔符
        Text("分隔符", style = MaterialTheme.typography.labelMedium)
        OptionChipRow(
            options = listOf(
                ' ' to "空格",
                '_' to "下划线",
                '.' to "点",
                '-' to "横线",
            ),
            selected = options.separator,
            onSelect = { onUpdate(options.copy(separator = it)) },
        )

        // 大小写
        Text("大小写", style = MaterialTheme.typography.labelMedium)
        OptionChipRow(
            options = listOf(
                NamingOptions.Casing.AS_IS to "原样",
                NamingOptions.Casing.UPPER to "大写",
                NamingOptions.Casing.LOWER to "小写",
                NamingOptions.Casing.TITLE to "首字母大写",
            ),
            selected = options.caseMode,
            onSelect = { onUpdate(options.copy(caseMode = it)) },
        )

        // 非法字符处理
        Text("非法字符处理", style = MaterialTheme.typography.labelMedium)
        OptionChipRow(
            options = listOf(
                NamingOptions.IllegalCharHandling.REMOVE to "删除",
                NamingOptions.IllegalCharHandling.REPLACE_UNDERSCORE to "替换为下划线",
                NamingOptions.IllegalCharHandling.REPLACE_DASH to "替换为横线",
            ),
            selected = options.illegalCharHandling,
            onSelect = { onUpdate(options.copy(illegalCharHandling = it)) },
        )

        // 补零位数
        Text(
            "补零位数：${options.padDigits}",
            style = MaterialTheme.typography.labelMedium,
        )
        Slider(
            value = options.padDigits.toFloat(),
            onValueChange = { onUpdate(options.copy(padDigits = it.toInt())) },
            valueRange = 1f..3f,
            steps = 1,
        )
    }
}

/** 通用 Chip 行：横向滚动，单选。 */
@Composable
private fun <T> OptionChipRow(
    options: List<Pair<T, String>>,
    selected: T,
    onSelect: (T) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { (value, text) ->
            FilterChip(
                selected = value == selected,
                onClick = { onSelect(value) },
                label = { Text(text) },
            )
        }
    }
}

/** 实时预览卡片：电影 + 剧集示例。 */
@Composable
private fun PreviewCard(preview: TemplateEditorViewModel.PreviewUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("实时预览", style = MaterialTheme.typography.titleSmall)

            PreviewItem(label = "电影示例", value = preview.movie)
            PreviewItem(label = "剧集示例", value = preview.episode)

            if (preview.warnings.isNotEmpty()) {
                Spacer(Modifier.size(4.dp))
                preview.warnings.forEach { w ->
                    Text(
                        text = "⚠ $w",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarningAmber,
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviewItem(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value.ifBlank { "（空）" },
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
