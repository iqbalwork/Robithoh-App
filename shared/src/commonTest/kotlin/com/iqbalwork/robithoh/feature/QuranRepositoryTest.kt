package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.quran.data.QuranRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class QuranRepositoryTest {

    private lateinit var repository: QuranRepositoryImpl

    @BeforeTest
    fun setup() {
        repository = QuranRepositoryImpl()
    }

    @Test
    fun testAll114SurahsArePresent() = runTest {
        val surahs = repository.getAllSurahs().first()
        assertEquals(114, surahs.size, "Al-Qur'an catalog must have exactly 114 surahs")

        assertEquals(1, surahs[0].number)
        assertEquals("Al-Fatihah", surahs[0].nameLatin)
        assertEquals("الفاتحة", surahs[0].nameArabic)
        assertEquals(7, surahs[0].numberOfAyahs)

        val yasin = surahs.find { it.number == 36 }
        assertNotNull(yasin)
        assertEquals("Yasin", yasin.nameLatin)
        assertEquals(83, yasin.numberOfAyahs)

        val alMulk = surahs.find { it.number == 67 }
        assertNotNull(alMulk)
        assertEquals("Al-Mulk", alMulk.nameLatin)
        assertEquals(30, alMulk.numberOfAyahs)

        assertEquals(114, surahs[113].number)
        assertEquals("An-Nas", surahs[113].nameLatin)
    }

    @Test
    fun testSearchSurahs() = runTest {
        val searchYasin = repository.searchSurahs("Yasin").first()
        assertTrue(searchYasin.any { it.number == 36 })

        val searchByNum = repository.searchSurahs("112").first()
        assertTrue(searchByNum.any { it.nameLatin == "Al-Ikhlas" })

        val searchByMeaning = repository.searchSurahs("Pembukaan").first()
        assertTrue(searchByMeaning.any { it.number == 1 })
    }

    @Test
    fun testGetAyahsForSurah() = runTest {
        val fatihahAyahs = repository.getAyahs(1).first()
        assertEquals(7, fatihahAyahs.size, "Al-Fatihah must have 7 ayahs")
        assertTrue(fatihahAyahs[0].textArabic.contains("الرَّحِيمِ"))
        assertTrue(fatihahAyahs[0].translationIndonesian.contains("Pengasih"))

        val ikhlasAyahs = repository.getAyahs(112).first()
        assertEquals(4, ikhlasAyahs.size, "Al-Ikhlas must have 4 ayahs")
        assertTrue(ikhlasAyahs[0].textArabic.contains("قُلْ هُوَ اللَّهُ أَحَدٌ"))
    }

    @Test
    fun testShalawatCollection() {
        val list = repository.getShalawatList()
        assertTrue(list.size >= 3, "Must have at least 3 Shalawat entries")

        val baniHasyim = list.find { it.id == "bani_hasyim" }
        assertNotNull(baniHasyim)
        assertTrue(baniHasyim.arabicText.contains("الْهَاشِمِيِّ"))
        assertEquals("bani_hasyim.mp3", baniHasyim.audioPath)

        val badriyah = list.find { it.id == "badriyah" }
        assertNotNull(badriyah)
        assertTrue(badriyah.arabicText.contains("أَهْلِ الْبَدْرِ"))
    }

    @Test
    fun testZiarahSections() {
        val sections = repository.getZiarahSections()
        assertEquals(2, sections.size, "Must have 2 Ziarah sections (Umum and Waliyullah)")

        val umum = sections.find { it.id == "ziarah_umum" }
        assertNotNull(umum)
        assertTrue(umum.adabSteps.isNotEmpty())

        val wali = sections.find { it.id == "ziarah_waliyullah" }
        assertNotNull(wali)
        assertTrue(wali.arabicPrayer.contains("وَلِيَّ اللَّهِ"))
    }
}
