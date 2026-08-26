package com.iqbalwork.robithoh.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// =========================================================================
// 1. PRIMARY PALETTE: MERAH PUTIH & EMAS KHIDMAT (CIRI KHAS ROBITHOH)
// =========================================================================

/** Merah Bendera Indonesia & Merah Marun Agung */
val MerahMerdeka = Color(0xFFCE1126)
val MerahMarunGelap = Color(0xFF8B0014)
val MerahPrimaryContainerLight = Color(0xFFFFDAD6)
val MerahOnPrimaryContainerLight = Color(0xFF410006)

val MerahPrimaryDark = Color(0xFFFFB3B8)
val MerahOnPrimaryDark = Color(0xFF680010)
val MerahPrimaryContainerDark = Color(0xFF93001B)
val MerahOnPrimaryContainerDark = Color(0xFFFFDAD6)

/** Warm Gold & Emas Khidmat */
val EmasKhidmat = Color(0xFFD4AF37)
val EmasMuda = Color(0xFFF9E8B2)
val EmasTua = Color(0xFF8F7100)
val GoldContainerLight = Color(0xFFFFF1CC)
val GoldOnContainerLight = Color(0xFF2C2100)

val GoldSecondaryDark = Color(0xFFECC248)
val GoldOnSecondaryDark = Color(0xFF3D2E00)
val GoldSecondaryContainerDark = Color(0xFF584400)
val GoldOnSecondaryContainerDark = Color(0xFFFFE086)

/** Soft Cream, Warm Paper & Surface Tones - Light */
val PaperBackgroundLight = Color(0xFFFBF8F3) // Warm paper subtle grid
val PureWhite = Color(0xFFFFFFFF)
val PutihBersih = Color(0xFFFFFFFF)
val PutihAbuBackground = PaperBackgroundLight
val SurfaceCardLight = Color(0xFFFFFFFF)
val SurfaceCardTinted = Color(0xFFFFF5F5)
val SurfaceContainerLight = Color(0xFFF5EFE6)
val TextCharcoal = Color(0xFF1E2124)
val SlateCharcoalText = Color(0xFF1E2124)
val TextMuted = Color(0xFF64748B)
val SlateMuted = Color(0xFF64748B)
val BorderSubtle = Color(0xFFEBE5DF)
val SlateBorder = Color(0xFFEBE5DF)
val BorderMerahLight = Color(0xFFFFCDD2)

/** Dark Mode Canvas & Surfaces */
val DarkCanvas = Color(0xFF141212)
val DarkSurface = Color(0xFF1E1A1A)
val DarkSurfaceVariant = Color(0xFF2A2424)
val DarkBorder = Color(0xFF3E3636)
val DarkMuted = Color(0xFFA1A1AA)

/** Badge & Highlight Colors */
val HijauKhasRobithoh = Color(0xFF16A34A)
val MerahSundaBadge = Color(0xFFD32F2F)
val AmberHighlight = Color(0xFFF59E0B)
val BlueKiblat = Color(0xFF0284C7)
val PurpleTracker = Color(0xFF9333EA)

// =========================================================================
// 2. MATERIAL 3 COLOR SCHEMES
// =========================================================================

val LightColorScheme: ColorScheme = lightColorScheme(
    primary = MerahMerdeka,
    onPrimary = PureWhite,
    primaryContainer = MerahPrimaryContainerLight,
    onPrimaryContainer = MerahOnPrimaryContainerLight,
    secondary = EmasKhidmat,
    onSecondary = PureWhite,
    secondaryContainer = GoldContainerLight,
    onSecondaryContainer = GoldOnContainerLight,
    tertiary = MerahMarunGelap,
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFFFFE5E5),
    onTertiaryContainer = Color(0xFF400008),
    background = PaperBackgroundLight,
    onBackground = TextCharcoal,
    surface = SurfaceCardLight,
    onSurface = TextCharcoal,
    surfaceVariant = SurfaceContainerLight,
    onSurfaceVariant = TextMuted,
    outline = BorderSubtle,
    outlineVariant = BorderMerahLight
)

val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = MerahPrimaryDark,
    onPrimary = MerahOnPrimaryDark,
    primaryContainer = MerahPrimaryContainerDark,
    onPrimaryContainer = MerahOnPrimaryContainerDark,
    secondary = GoldSecondaryDark,
    onSecondary = GoldOnSecondaryDark,
    secondaryContainer = GoldSecondaryContainerDark,
    onSecondaryContainer = GoldOnSecondaryContainerDark,
    tertiary = EmasMuda,
    onTertiary = DarkCanvas,
    tertiaryContainer = Color(0xFF5C000B),
    onTertiaryContainer = Color(0xFFFFDAD6),
    background = DarkCanvas,
    onBackground = PureWhite,
    surface = DarkSurface,
    onSurface = PureWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMuted,
    outline = DarkBorder,
    outlineVariant = Color(0xFF4A3838)
)

// =========================================================================
// 3. CUSTOM RABITHOH EXTENSION COLORS
// =========================================================================

@Immutable
data class RabithohColors(
    val primaryRed: Color,
    val goldAccent: Color,
    val cardBackground: Color,
    val cardBorder: Color,
    val paperBackground: Color,
    val arabicText: Color,
    val transliterationText: Color,
    val translationText: Color,
    val sundaBadge: Color,
    val audioBarBackground: Color,
    val isDark: Boolean
)

val LightRabithohColors = RabithohColors(
    primaryRed = MerahMerdeka,
    goldAccent = EmasKhidmat,
    cardBackground = SurfaceCardLight,
    cardBorder = BorderSubtle,
    paperBackground = PaperBackgroundLight,
    arabicText = Color(0xFF1A1D20),
    transliterationText = Color(0xFF475569),
    translationText = Color(0xFF1E293B),
    sundaBadge = MerahSundaBadge,
    audioBarBackground = Color(0xFFFFF7F7),
    isDark = false
)

val DarkRabithohColors = RabithohColors(
    primaryRed = MerahPrimaryDark,
    goldAccent = GoldSecondaryDark,
    cardBackground = DarkSurface,
    cardBorder = DarkBorder,
    paperBackground = DarkCanvas,
    arabicText = Color(0xFFF8FAFC),
    transliterationText = Color(0xFF94A3B8),
    translationText = Color(0xFFE2E8F0),
    sundaBadge = MerahSundaBadge,
    audioBarBackground = DarkSurfaceVariant,
    isDark = true
)

val LocalRabithohColors = staticCompositionLocalOf { LightRabithohColors }


