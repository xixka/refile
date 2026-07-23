package com.webdavrenamer.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Infuse-style cinema-grade dark palette.
 *
 * Only the dark scheme is shipped (the app is dark-only); a [LightColors]
 * alias is kept for source-level compatibility with code that branches on
 * `darkTheme`, but [WebDavRenamerTheme] always uses [DarkColors].
 */

// Backgrounds — deep cinema black
val CinemaBlack = Color(0xFF0B0B0F)
val CinemaDark = Color(0xFF141418)

// Surfaces
val DarkSurface = Color(0xFF1C1C22)
val DarkSurfaceVariant = Color(0xFF25252B)
val CardBackground = Color(0xFF1A1A20)
// Task 5.5：海报墙加载占位灰框
val PosterPlaceholder = Color(0xFF2A2A31)

// Accents — Infuse-style amber/gold
val AccentAmber = Color(0xFFE8A33D)
val SecondaryGold = Color(0xFFC89B3C)
val AccentTeal = Color(0xFF2DD4BF)

// Text
val TextPrimary = Color(0xFFF5F5F7)
val TextSecondary = Color(0xFF9A9AA2)
val TextDisabled = Color(0xFF5C5C66)

// Status
val ErrorRed = Color(0xFFCF6679)
val SuccessGreen = Color(0xFF4ADE80)
val WarningAmber = Color(0xFFFBBF24)

/**
 * The active color scheme. Dark-only.
 */
val DarkColorScheme = darkColorScheme(
    primary = AccentAmber,
    onPrimary = CinemaBlack,
    primaryContainer = SecondaryGold,
    onPrimaryContainer = CinemaBlack,
    secondary = SecondaryGold,
    onSecondary = CinemaBlack,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentTeal,
    onTertiary = CinemaBlack,
    tertiaryContainer = DarkSurfaceVariant,
    onTertiaryContainer = TextPrimary,
    background = CinemaBlack,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    surfaceTint = AccentAmber,
    inverseSurface = TextPrimary,
    inverseOnSurface = CinemaBlack,
    error = ErrorRed,
    onError = CinemaBlack,
    errorContainer = ErrorRed,
    onErrorContainer = CinemaBlack,
    outline = TextSecondary,
    outlineVariant = TextDisabled,
    scrim = CinemaBlack,
)

/**
 * Alias kept for source compatibility; resolves to [DarkColorScheme].
 */
val LightColors = DarkColorScheme
