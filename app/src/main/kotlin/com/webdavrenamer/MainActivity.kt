package com.webdavrenamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.webdavrenamer.ui.navigation.AppNavHost
import com.webdavrenamer.ui.theme.WebDavRenamerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebDavRenamerTheme {
                // 根容器仅提供背景色与全屏尺寸；每个目的地页面各自持有 Scaffold/TopAppBar
                // 并自行处理系统栏 insets，避免顶层 Scaffold 与页面 Scaffold 叠加产生双重内边距。
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavHost()
                }
            }
        }
    }
}
