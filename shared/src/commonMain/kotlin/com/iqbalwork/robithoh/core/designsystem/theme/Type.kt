package com.iqbalwork.robithoh.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import robithohapp.shared.generated.resources.Res
import robithohapp.shared.generated.resources.amiri_quran_regular
import robithohapp.shared.generated.resources.plus_jakarta_sans_bold
import robithohapp.shared.generated.resources.plus_jakarta_sans_medium
import robithohapp.shared.generated.resources.plus_jakarta_sans_regular
import robithohapp.shared.generated.resources.scheherazade_new_bold
import robithohapp.shared.generated.resources.scheherazade_new_regular

// =========================================================================
// 1. FONT FAMILIES
// =========================================================================

@Composable
fun getAmiriQuranFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.amiri_quran_regular, FontWeight.Normal)
    )
}

@Composable
fun getPlusJakartaSansFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.plus_jakarta_sans_regular, FontWeight.Normal),
        Font(Res.font.plus_jakarta_sans_medium, FontWeight.Medium),
        Font(Res.font.plus_jakarta_sans_bold, FontWeight.Bold)
    )
}

@Composable
fun getScheherazadeNewFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.scheherazade_new_regular, FontWeight.Normal),
        Font(Res.font.scheherazade_new_bold, FontWeight.Bold)
    )
}

// Fallback / default typography without composable requirement
val DefaultLatinFontFamily = FontFamily.SansSerif
val DefaultArabicFontFamily = FontFamily.Serif

// =========================================================================
// 2. MATERIAL 3 TYPOGRAPHY
// =========================================================================

@Composable
fun getRabithohM3Typography(): Typography {
    val sans = getPlusJakartaSansFontFamily()
    return Typography(
        displayLarge = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Bold,
            fontSize = 45.sp,
            lineHeight = 52.sp
        ),
        displaySmall = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 36.sp,
            lineHeight = 44.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        titleLarge = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}

// =========================================================================
// 3. CUSTOM RABITHOH TYPOGRAPHY (ARABIC & LITURGICAL TOKENS)
// =========================================================================

@Immutable
data class RabithohTypography(
    val arabicDisplay: TextStyle,
    val arabicLarge: TextStyle,
    val arabicMedium: TextStyle,
    val arabicSmall: TextStyle,
    val transliteration: TextStyle,
    val translation: TextStyle,
    val sundanese: TextStyle,
    val kitabHeader: TextStyle,
    val tanbihTitle: TextStyle,
    val hapticCounterText: TextStyle,
    val scaleFactor: Float = 1.0f
) {
    fun withScale(scale: Float): RabithohTypography {
        if (scale == 1.0f) return this
        return copy(
            arabicDisplay = arabicDisplay.copy(
                fontSize = arabicDisplay.fontSize * scale,
                lineHeight = arabicDisplay.lineHeight * scale
            ),
            arabicLarge = arabicLarge.copy(
                fontSize = arabicLarge.fontSize * scale,
                lineHeight = arabicLarge.lineHeight * scale
            ),
            arabicMedium = arabicMedium.copy(
                fontSize = arabicMedium.fontSize * scale,
                lineHeight = arabicMedium.lineHeight * scale
            ),
            arabicSmall = arabicSmall.copy(
                fontSize = arabicSmall.fontSize * scale,
                lineHeight = arabicSmall.lineHeight * scale
            ),
            transliteration = transliteration.copy(
                fontSize = transliteration.fontSize * scale,
                lineHeight = transliteration.lineHeight * scale
            ),
            translation = translation.copy(
                fontSize = translation.fontSize * scale,
                lineHeight = translation.lineHeight * scale
            ),
            sundanese = sundanese.copy(
                fontSize = sundanese.fontSize * scale,
                lineHeight = sundanese.lineHeight * scale
            ),
            kitabHeader = kitabHeader.copy(
                fontSize = kitabHeader.fontSize * scale,
                lineHeight = kitabHeader.lineHeight * scale
            ),
            tanbihTitle = tanbihTitle.copy(
                fontSize = tanbihTitle.fontSize * scale,
                lineHeight = tanbihTitle.lineHeight * scale
            ),
            scaleFactor = scale
        )
    }
}

@Composable
fun getRabithohCustomTypography(): RabithohTypography {
    val amiri = getAmiriQuranFontFamily()
    val sans = getPlusJakartaSansFontFamily()

    return RabithohTypography(
        arabicDisplay = TextStyle(
            fontFamily = amiri,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 60.sp,
            textDirection = TextDirection.Rtl,
            textAlign = TextAlign.Right
        ),
        arabicLarge = TextStyle(
            fontFamily = amiri,
            fontWeight = FontWeight.Normal,
            fontSize = 26.sp,
            lineHeight = 50.sp,
            textDirection = TextDirection.Rtl,
            textAlign = TextAlign.Right
        ),
        arabicMedium = TextStyle(
            fontFamily = amiri,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 44.sp,
            textDirection = TextDirection.Rtl,
            textAlign = TextAlign.Right
        ),
        arabicSmall = TextStyle(
            fontFamily = amiri,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 36.sp,
            textDirection = TextDirection.Rtl,
            textAlign = TextAlign.Right
        ),
        transliteration = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            lineHeight = 22.sp
        ),
        translation = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp
        ),
        sundanese = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp
        ),
        kitabHeader = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        tanbihTitle = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 26.sp
        ),
        hapticCounterText = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Bold,
            fontSize = 56.sp,
            lineHeight = 64.sp,
            textAlign = TextAlign.Center
        )
    )
}

val DefaultRabithohTypography = RabithohTypography(
    arabicDisplay = TextStyle(
        fontFamily = DefaultArabicFontFamily,
        fontSize = 32.sp,
        lineHeight = 60.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right
    ),
    arabicLarge = TextStyle(
        fontFamily = DefaultArabicFontFamily,
        fontSize = 26.sp,
        lineHeight = 50.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right
    ),
    arabicMedium = TextStyle(
        fontFamily = DefaultArabicFontFamily,
        fontSize = 22.sp,
        lineHeight = 44.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right
    ),
    arabicSmall = TextStyle(
        fontFamily = DefaultArabicFontFamily,
        fontSize = 18.sp,
        lineHeight = 36.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right
    ),
    transliteration = TextStyle(
        fontFamily = DefaultLatinFontFamily,
        fontStyle = FontStyle.Italic,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    translation = TextStyle(
        fontFamily = DefaultLatinFontFamily,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    sundanese = TextStyle(
        fontFamily = DefaultLatinFontFamily,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    kitabHeader = TextStyle(
        fontFamily = DefaultLatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    tanbihTitle = TextStyle(
        fontFamily = DefaultLatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    hapticCounterText = TextStyle(
        fontFamily = DefaultLatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 56.sp,
        lineHeight = 64.sp,
        textAlign = TextAlign.Center
    )
)

val LocalRabithohTypography = staticCompositionLocalOf { DefaultRabithohTypography }
