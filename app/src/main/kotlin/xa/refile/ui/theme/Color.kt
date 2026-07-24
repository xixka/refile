package xa.refile.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Infuse-style light palette.
 *
 * White background, soft-gray surfaces, amber/orange accent (Infuse signature).
 * The active scheme is [LightColorScheme]; [DarkColors] is kept as an alias
 * for source-level compatibility but resolves to [LightColorScheme].
 */

// Backgrounds — pure Infuse white
val InfuseWhite = Color(0xFFFFFFFF)
val InfuseBackground = Color(0xFFF7F7FA)

// Surfaces
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF2F2F7)
val CardBackground = Color(0xFFFFFFFF)
// 海报墙加载占位浅灰框
val PosterPlaceholder = Color(0xFFE5E5EA)

// Accents — Infuse-style amber/orange
val AccentAmber = Color(0xFFE8941D)
val SecondaryGold = Color(0xFFFFA630)
val AccentTeal = Color(0xFF1F8C8C)

// Text
val TextPrimary = Color(0xFF1C1C1E)
val TextSecondary = Color(0xFF6C6C70)
val TextDisabled = Color(0xFFB0B0B5)

// Status
val ErrorRed = Color(0xFFE53935)
val SuccessGreen = Color(0xFF34C759)
val WarningAmber = Color(0xFFFF9500)

// Legacy aliases kept for source compatibility (resolve to light equivalents).
val CinemaBlack = InfuseBackground
val CinemaDark = LightSurface
val DarkSurface = LightSurface
val DarkSurfaceVariant = LightSurfaceVariant

/**
 * The active color scheme. Light Infuse.
 */
val LightColorScheme = lightColorScheme(
    primary = AccentAmber,
    onPrimary = Color.White,
    primaryContainer = SecondaryGold,
    onPrimaryContainer = Color.White,
    secondary = SecondaryGold,
    onSecondary = Color.White,
    secondaryContainer = LightSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentTeal,
    onTertiary = Color.White,
    tertiaryContainer = LightSurfaceVariant,
    onTertiaryContainer = TextPrimary,
    background = InfuseWhite,
    onBackground = TextPrimary,
    surface = LightSurface,
    onSurface = TextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    surfaceTint = AccentAmber,
    inverseSurface = TextPrimary,
    inverseOnSurface = InfuseWhite,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed,
    onErrorContainer = Color.White,
    outline = TextSecondary,
    outlineVariant = TextDisabled,
    scrim = Color(0x99000000),
)

/**
 * Alias kept for source compatibility; resolves to [LightColorScheme].
 */
val DarkColors = LightColorScheme
val DarkColorScheme = LightColorScheme
