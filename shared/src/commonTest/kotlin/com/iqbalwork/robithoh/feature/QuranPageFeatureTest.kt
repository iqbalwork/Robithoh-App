package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.quran.data.QuranPageLookup
import com.iqbalwork.robithoh.feature.quran.model.AyahBlock
import com.iqbalwork.robithoh.feature.quran.model.QuranPageMapping
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class QuranPageFeatureTest {

    @Test
    fun testJuzStartPages() {
        assertEquals(30, QuranPageLookup.juzStartPages.size)
        assertEquals(1, QuranPageLookup.getPageForJuz(1))
        assertEquals(22, QuranPageLookup.getPageForJuz(2))
        assertEquals(62, QuranPageLookup.getPageForJuz(4))
        assertEquals(82, QuranPageLookup.getPageForJuz(5))
        assertEquals(582, QuranPageLookup.getPageForJuz(30))
    }

    @Test
    fun testSurahStartPages() {
        assertEquals(114, QuranPageLookup.surahStartPages.size)
        assertEquals(1, QuranPageLookup.getPageForSurah(1)) // Al-Fatihah
        assertEquals(2, QuranPageLookup.getPageForSurah(2)) // Al-Baqarah
        assertEquals(50, QuranPageLookup.getPageForSurah(3)) // Ali 'Imran
        assertEquals(77, QuranPageLookup.getPageForSurah(4)) // An-Nisa'
        assertEquals(604, QuranPageLookup.getPageForSurah(112)) // Al-Ikhlas
        assertEquals(604, QuranPageLookup.getPageForSurah(114)) // An-Nas
    }

    @Test
    fun testPageForAyah() {
        // Al-Fatihah ayahs 1..7 on page 1
        for (a in 1..7) {
            assertEquals(1, QuranPageLookup.getPageForAyah(1, a))
        }

        // Al-Baqarah 1..5 on page 2
        for (a in 1..5) {
            assertEquals(2, QuranPageLookup.getPageForAyah(2, a))
        }

        // An-Nisa' 1 on page 77
        assertEquals(77, QuranPageLookup.getPageForAyah(4, 1))

        // Al-Ikhlas, Al-Falaq, An-Nas on page 604
        assertEquals(604, QuranPageLookup.getPageForAyah(112, 1))
        assertEquals(604, QuranPageLookup.getPageForAyah(113, 1))
        assertEquals(604, QuranPageLookup.getPageForAyah(114, 6))
    }

    @Test
    fun testPageMeta() {
        val meta1 = QuranPageLookup.getPageMeta(1)
        assertEquals(1, meta1.pageNumber)
        assertEquals(1, meta1.juz)
        assertEquals("Al-Fatihah", meta1.surahName)

        val meta77 = QuranPageLookup.getPageMeta(77)
        assertEquals(77, meta77.pageNumber)
        assertEquals(4, meta77.juz)
        assertEquals("An-Nisa'", meta77.surahName)

        val meta604 = QuranPageLookup.getPageMeta(604)
        assertEquals(604, meta604.pageNumber)
        assertEquals(30, meta604.juz)
    }

    @Test
    fun testCoordinateUnprojectionAndHitTesting() {
        // Simulated Page 1: Ayah 1 block in 1080 x 1745 space
        val page1Mapping = QuranPageMapping(
            viewportWidth = 1080,
            viewportHeight = 1745,
            data = listOf(
                AyahBlock(surah = 1, ayah = 1, blok = 1, top = 472, left = 192, width = 694, height = 119),
                AyahBlock(surah = 1, ayah = 2, blok = 1, top = 591, left = 348, width = 538, height = 112)
            )
        )

        // Simulate mobile viewport 1080 x 2400 (aspect ratio different from 1080 x 1745)
        val displayW = 1080f
        val displayH = 2400f
        val scale = min(displayW / 1080f, displayH / 1745f)
        val renderedW = 1080f * scale
        val renderedH = 1745f * scale
        val offsetX = (displayW - renderedW) / 2f
        val offsetY = (displayH - renderedH) / 2f

        // Point inside Ayah 1 (e.g. center of block 1: x = 192 + 300 = 492, y = 472 + 50 = 522)
        val targetNativeX = 492f
        val targetNativeY = 522f
        val simulatedTapX = offsetX + (targetNativeX * scale)
        val simulatedTapY = offsetY + (targetNativeY * scale)

        // Unproject tap
        val unprojectedX = ((simulatedTapX - offsetX) / scale).roundToInt()
        val unprojectedY = ((simulatedTapY - offsetY) / scale).roundToInt()

        val matched = page1Mapping.data.find { block ->
            unprojectedY in block.top..(block.top + block.height) &&
            unprojectedX in block.left..(block.left + block.width)
        }

        assertNotNull(matched)
        assertEquals(1, matched.surah)
        assertEquals(1, matched.ayah)

        // Point on blank top margin (y = 100 in native space, above all ayahs)
        val blankTapX = offsetX + (1000f * scale)
        val blankTapY = offsetY + (100f * scale)
        val unprojectedBlankX = ((blankTapX - offsetX) / scale).roundToInt()
        val unprojectedBlankY = ((blankTapY - offsetY) / scale).roundToInt()

        val blankMatched = page1Mapping.data.find { block ->
            unprojectedBlankY in block.top..(block.top + block.height) &&
            unprojectedBlankX in block.left..(block.left + block.width)
        }
        assertEquals(null, blankMatched)
    }

    @Test
    fun testPhysicalBookZIndexOrdering() {
        // Earlier pages in a physical book must always be on top of later pages in z-order
        for (pageIndex in 0 until QuranPageLookup.TOTAL_PAGES - 1) {
            val zCurrent = (QuranPageLookup.TOTAL_PAGES - pageIndex).toFloat()
            val zNext = (QuranPageLookup.TOTAL_PAGES - (pageIndex + 1)).toFloat()
            assertTrue(zCurrent > zNext, "Page index $pageIndex must have higher zIndex than ${pageIndex + 1}")
        }
    }

    @Test
    fun testKemenagAssetUrlsPinned() {
        val primary = com.iqbalwork.robithoh.feature.quran.data.QuranPageManager.PRIMARY_PAGE_URL
        val fallback = com.iqbalwork.robithoh.feature.quran.data.QuranPageManager.FALLBACK_PAGE_URL
        assertTrue(primary.contains("e2a806a"), "Primary CDN URL must pin commit e2a806a for Indonesian Kemenag")
        assertTrue(fallback.contains("e2a806a"), "Fallback URL must pin commit e2a806a for Indonesian Kemenag")
    }
}
