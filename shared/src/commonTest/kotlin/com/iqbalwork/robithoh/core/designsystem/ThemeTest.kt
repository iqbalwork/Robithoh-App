package com.iqbalwork.robithoh.core.designsystem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.IslamicDividerMotif
import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.core.designsystem.theme.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ThemeTest {

    @Test
    fun testMerahPutihColorPaletteValues() {
        assertEquals(Color(0xFFCE1126), MerahMerdeka)
        assertEquals(Color(0xFF7A0914), MerahMarunGelap)
        assertEquals(Color(0xFFFFFFFF), PutihBersih)
        assertEquals(Color(0xFFF8F9FA), PutihAbuBackground)
        assertEquals(Color(0xFFD4AF37), EmasKhidmat)
        assertEquals(Color(0xFF1A1D20), SlateCharcoalText)
        assertEquals(Color(0xFF111113), DarkCanvas)
        assertEquals(Color(0xFF1A1A1E), DarkSurface)
    }

    @Test
    fun testLightAndDarkColorSchemeContrast() {
        assertEquals(MerahMerdeka, LightColorScheme.primary)
        assertEquals(PutihBersih, LightColorScheme.onPrimary)
        assertEquals(PutihAbuBackground, LightColorScheme.background)
        assertEquals(SlateCharcoalText, LightColorScheme.onBackground)

        assertEquals(MerahMerdeka, DarkColorScheme.primary)
        assertEquals(PutihBersih, DarkColorScheme.onPrimary)
        assertEquals(DarkCanvas, DarkColorScheme.background)
        assertEquals(PutihBersih, DarkColorScheme.onBackground)
    }

    @Test
    fun testRabithohColorsExtensions() {
        assertFalse(LightRabithohColors.isDark)
        assertTrue(DarkRabithohColors.isDark)

        assertEquals(EmasKhidmat, LightRabithohColors.goldAccent)
        assertEquals(MerahMerdeka, LightRabithohColors.primaryRed)

        assertEquals(GoldSecondaryDark, DarkRabithohColors.goldAccent)
        assertEquals(MerahPrimaryDark, DarkRabithohColors.primaryRed)
        assertEquals(DarkSurface, DarkRabithohColors.cardBackground)
    }

    @Test
    fun testTypographyScaling() {
        val baseTypography = DefaultRabithohTypography
        assertEquals(26.sp, baseTypography.arabicLarge.fontSize)
        assertEquals(50.sp, baseTypography.arabicLarge.lineHeight)

        val scaled = baseTypography.withScale(1.5f)
        assertEquals(39.sp, scaled.arabicLarge.fontSize)
        assertEquals(75.sp, scaled.arabicLarge.lineHeight)
        assertEquals(1.5f, scaled.scaleFactor)

        val unscaled = baseTypography.withScale(1.0f)
        assertEquals(baseTypography, unscaled)
    }

    @Test
    fun testIslamicDividerMotifs() {
        assertEquals("☪", IslamicDividerMotif.CRESCENT_STAR.symbol)
        assertEquals("۞", IslamicDividerMotif.RUB_EL_HIZB.symbol)
        assertEquals("❖", IslamicDividerMotif.ARABESQUE_DIAMOND.symbol)
        assertEquals("✦", IslamicDividerMotif.FLORAL_KNOT.symbol)
        assertEquals("", IslamicDividerMotif.CLEAN_LINE.symbol)
    }

    @Test
    fun testLiturgyLanguageEnum() {
        val entries = LiturgyLanguage.entries
        assertEquals(3, entries.size)
        assertEquals("Arab", LiturgyLanguage.ARABIC.label)
        assertEquals("العربية", LiturgyLanguage.ARABIC.nativeLabel)
        assertEquals("Indonesia", LiturgyLanguage.INDONESIAN.label)
        assertEquals("Terjemahan", LiturgyLanguage.INDONESIAN.nativeLabel)
        assertEquals("Sunda", LiturgyLanguage.SUNDANESE.label)
        assertEquals("Basa Sunda", LiturgyLanguage.SUNDANESE.nativeLabel)
    }

    @Test
    fun testShapeTokens() {
        val shapes = RabithohShapes()
        assertTrue(shapes.cardRounded.topStart.toPx(androidx.compose.ui.geometry.Size(100f, 100f), androidx.compose.ui.unit.Density(1f)) > 0f)
    }
}
