package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.manaqib.data.ManaqibRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class ManaqibRepositoryTest {

    private lateinit var repository: ManaqibRepositoryImpl

    @BeforeTest
    fun setup() {
        repository = ManaqibRepositoryImpl()
    }

    @Test
    fun testAll56ChaptersArePresent() = runTest {
        val chapters = repository.getAllChapters().first()
        assertEquals(56, chapters.size, "Must have exactly 56 Manqobah chapters")

        for (i in 1..56) {
            val ch = chapters.find { it.chapterNumber == i }
            assertNotNull(ch, "Chapter $i must exist")
            assertTrue(ch.titleArabic.isNotBlank(), "Chapter $i must have Arabic title")
            assertTrue(ch.titleIndonesian.isNotBlank(), "Chapter $i must have Indonesian title")
            assertTrue(ch.titleSundanese.isNotBlank(), "Chapter $i must have Sundanese title")
            assertTrue(ch.contentArabic.isNotBlank(), "Chapter $i must have Arabic content")
            assertTrue(ch.contentIndonesian.isNotBlank(), "Chapter $i must have Indonesian content")
            assertTrue(ch.contentSundanese.isNotBlank(), "Chapter $i must have Sundanese content")
        }
    }

    @Test
    fun testSearchChapters() = runTest {
        val searchByNum = repository.searchChapters("1").first()
        assertTrue(searchByNum.isNotEmpty(), "Search by '1' should find chapter 1")

        val searchByNasab = repository.searchChapters("Nasab").first()
        assertTrue(searchByNasab.any { it.chapterNumber == 1 }, "Search 'Nasab' should find chapter 1")

        val searchBySunda = repository.searchChapters("Turunan").first()
        assertTrue(searchBySunda.any { it.chapterNumber == 1 }, "Search 'Turunan' should find chapter 1")
    }

    @Test
    fun testGetChapterByNumber() = runTest {
        val chapter1 = repository.getChapter(1)
        assertNotNull(chapter1)
        assertEquals(1, chapter1.chapterNumber)
        assertTrue(chapter1.titleIndonesian.contains("Nasab"))

        val chapter56 = repository.getChapter(56)
        assertNotNull(chapter56)
        assertEquals(56, chapter56.chapterNumber)

        val invalid = repository.getChapter(999)
        assertNull(invalid)
    }

    @Test
    fun testTanbihData() {
        val tanbih = repository.getTanbih()
        assertNotNull(tanbih)
        assertTrue(tanbih.indonesianText.contains("Patapan Suryalaya"))
        assertTrue(tanbih.sundaneseText.contains("Patapan Suryalaya"))
        assertTrue(tanbih.openingArabic.contains("بِسْمِ اللَّهِ"))
        assertTrue(tanbih.closingArabic.contains("رَبَّنَا آتِنَا"))
    }

    @Test
    fun testMcProgramList() {
        val mcList = repository.getMcProgramList()
        assertEquals(7, mcList.size, "MC program protocol must have 7 sequence steps")
        assertEquals(1, mcList.first().stepNumber)
        assertEquals(7, mcList.last().stepNumber)
        assertTrue(mcList.all { it.protocolId.isNotBlank() && it.protocolSu.isNotBlank() })
    }

    @Test
    fun testSilsilah38Completeness() {
        val silsilah = repository.getSilsilahNodes()
        assertEquals(38, silsilah.size, "Silsilah must contain exactly 38 nodes")

        assertEquals(1, silsilah[0].orderNumber)
        assertTrue(silsilah[0].name.contains("Muhammad Rosulullah"))

        assertEquals(2, silsilah[1].orderNumber)
        assertTrue(silsilah[1].name.contains("'Ali bin Abi Tholib"))

        val syekhAbdulQodir = silsilah.find { it.orderNumber == 17 }
        assertNotNull(syekhAbdulQodir)
        assertTrue(syekhAbdulQodir.name.contains("Abdul Qodir Al-Jailani"))

        val abahSepuh = silsilah.find { it.orderNumber == 36 }
        assertNotNull(abahSepuh)
        assertTrue(abahSepuh.name.contains("Abdullah Mubarok") || abahSepuh.name.contains("Abah Sepuh"))

        val abahAnom = silsilah.find { it.orderNumber == 37 }
        assertNotNull(abahAnom)
        assertTrue(abahAnom.name.contains("Ahmad Shohibulwafa Tajul Arifin") || abahAnom.name.contains("Abah Anom"))

        val abahAos38 = silsilah.find { it.orderNumber == 38 }
        assertNotNull(abahAos38)
        assertTrue(abahAos38.name.contains("Abdul Gaos") || abahAos38.name.contains("Abah Aos"))
    }

    @Test
    fun testDoaSpiritualItems() {
        val doas = repository.getDoaList()
        assertEquals(3, doas.size, "Must have 3 primary spiritual prayers (Manaqobah, Rijalul Ghoib, Ashabul Kahfi)")

        val doaManaqobah = repository.getDoaById("doa_manaqobah")
        assertNotNull(doaManaqobah)
        assertTrue(doaManaqobah.arabicText.isNotBlank())

        val doaRijalulGhoib = repository.getDoaById("doa_rijalul_ghoib")
        assertNotNull(doaRijalulGhoib)
        assertTrue(doaRijalulGhoib.arabicText.contains("رِجَالَ الْغَيْبِ"))

        val doaAshabulKahfi = repository.getDoaById("doa_ashabul_kahfi")
        assertNotNull(doaAshabulKahfi)
        assertTrue(doaAshabulKahfi.arabicText.contains("قِطْمِيرْ"))
    }

    @Test
    fun testKhotamanSteps() {
        val steps = repository.getKhotamanSteps()
        assertTrue(steps.size >= 6, "Khotaman sequence must have at least 6 steps")
        assertTrue(steps.any { it.title.contains("Fatihah", ignoreCase = true) })
        assertTrue(steps.any { it.title.contains("Istighfar", ignoreCase = true) })
        assertTrue(steps.any { it.title.contains("Shalawat", ignoreCase = true) })
        assertTrue(steps.any { it.title.contains("Hasbunallah", ignoreCase = true) })
        assertTrue(steps.any { it.title.contains("Tahlil", ignoreCase = true) })
    }
}
