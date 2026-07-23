package com.webdavrenamer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * App-wide Compose theme.
 *
 * Dark-only by design: the [darkTheme] parameter is accepted for API symmetry
 * but the app always renders with [DarkColorScheme] regardless of the value.
 * Dynamic color (Material You) is disabled — the cinema palette is fixed.
 *
 * Status bar / system bars are handled by `enableEdgeToEdge()` in
 * [com.webdavrenamer.MainActivity]; no extra WindowCompat manipulation is
 * needed here.
 */
@Composable
fun WebDavRenamerTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
