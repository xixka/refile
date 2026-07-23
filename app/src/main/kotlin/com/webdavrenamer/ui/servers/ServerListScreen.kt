package com.webdavrenamer.ui.servers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.webdavrenamer.data.db.ServerConfigEntity
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 服务器列表页（计划 §M1 SubTask 1.4.1）。
 *
 * - 顶部栏标题"服务器" + 右上角添加按钮。
 * - 每项一张卡片：名称、Base URL、用户名、密码（仅显示 ••••••，不回显明文，红线）。
 * - 卡片点击进入文件浏览器；左滑露出编辑/删除操作。
 * - 删除前用 [AlertDialog] 二次确认。
 * - 空状态居中提示。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerListScreen(
    onAddServer: () -> Unit,
    onEditServer: (Long) -> Unit,
    onOpenBrowser: (Long) -> Unit,
    onOpenHistory: () -> Unit = {},
    onOpenHosts: () -> Unit = {},
    onOpenBackup: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    viewModel: ServerListViewModel = hiltViewModel(),
) {
    val servers by viewModel.servers.collectAsStateWithLifecycle()
    var pendingDeleteId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("服务器") },
                actions = {
                    IconButton(onClick = onOpenHistory) {
                        Icon(Icons.Default.History, contentDescription = "历史记录")
                    }
                    IconButton(onClick = onOpenHosts) {
                        Icon(Icons.Default.Public, contentDescription = "Hosts 设置")
                    }
                    IconButton(onClick = onOpenBackup) {
                        Icon(Icons.Default.Backup, contentDescription = "备份与恢复")
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                    IconButton(onClick = onAddServer) {
                        Icon(Icons.Default.Add, contentDescription = "添加服务器")
                    }
                },
            )
        },
    ) { padding ->
        if (servers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CloudOff,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "还没有服务器，点击右上角添加",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(servers, key = { it.id }) { server ->
                    ServerRow(
                        server = server,
                        onClick = { onOpenBrowser(server.id) },
                        onEdit = { onEditServer(server.id) },
                        onDelete = { pendingDeleteId = server.id },
                    )
                }
            }
        }
    }

    pendingDeleteId?.let { id ->
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text("删除服务器") },
            text = { Text("确定删除该服务器配置吗？此操作不可撤销。") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteServer(id)
                    pendingDeleteId = null
                }) {
                    Text("删除", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteId = null }) { Text("取消") }
            },
        )
    }
}

/** 单条服务器卡片，支持左滑露出编辑/删除操作。 */
@Composable
private fun ServerRow(
    server: ServerConfigEntity,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val revealWidth = 168.dp
    val revealWidthPx = with(LocalDensity.current) { revealWidth.toPx() }
    val offsetX = remember(server.id) { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        // 背景：左滑后露出的编辑/删除操作（右对齐）
        Row(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .width(revealWidth)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    scope.launch { offsetX.animateTo(0f, spring()) }
                    onEdit()
                }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "编辑",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = {
                    scope.launch { offsetX.animateTo(0f, spring()) }
                    onDelete()
                }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }

        // 前景卡片：水平拖动时向左偏移以露出背景操作
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(-offsetX.value.roundToInt(), 0) }
                .pointerInput(server.id) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                offsetX.animateTo(
                                    targetValue = if (offsetX.value < -revealWidthPx / 2f) {
                                        -revealWidthPx
                                    } else {
                                        0f
                                    },
                                    animationSpec = spring(),
                                )
                            }
                        },
                    ) { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo(
                                (offsetX.value + dragAmount).coerceIn(-revealWidthPx, 0f),
                            )
                        }
                    }
                }
                .clickable {
                    if (offsetX.value != 0f) {
                        scope.launch { offsetX.animateTo(0f, spring()) }
                    } else {
                        onClick()
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Cloud,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = server.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = server.baseUrl,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                server.port?.let {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "端口：$it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "用户名：${server.username ?: "匿名"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "密码：${if (server.encryptedPassword != null) "••••••" else "未设置"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "根路径：${server.rootPath}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "协议：${if (server.https) "HTTPS" else "HTTP"} · 认证：${server.authType}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
