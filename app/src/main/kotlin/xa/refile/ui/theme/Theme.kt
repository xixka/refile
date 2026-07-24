package xa.refile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * App-wide Compose theme.
 *
 * Light Infuse-style by design: the [darkTheme] parameter is accepted for
 * API symmetry but the app always renders with [LightColorScheme] regardless
 * of the value. Dynamic color (Material You) is disabled — the Infuse palette
 * is fixed.
 *
 * Status bar / system bars are handled by `enableEdgeToEdge()` in
 * [xa.refile.MainActivity]; no extra WindowCompat manipulation is needed here.
 */
@Composable
fun WebDavRenamerTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
