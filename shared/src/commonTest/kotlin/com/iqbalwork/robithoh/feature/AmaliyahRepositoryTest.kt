package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.feature.amaliyah.data.AmaliyahRepository
import kotlin.test.*

class AmaliyahRepositoryTest {

    private val repository = AmaliyahRepository()

    @Test
    fun testDzikirJahrCompleteness() {
        val jahrList = repository.getDzikirJahrList()
        assertTrue(jahrList.isNotEmpty())
        assertEquals(13, jahrList.size)

        // Check Dzikir Jahr (165x)
        val dzikirJahr = jahrList.first { it.id == "dzikir_harian_6" }
        assertEquals(165, dzikirJahr.repetitionCount)
        assertTrue(dzikirJahr.arabicText.contains("لَا إِلٰهَ إِلَّا اللّٰهُ") || dzikirJahr.arabicText.contains("لَا إِلَهَ إِلَّا اللَّهُ"))
        assertTrue(dzikirJahr.latinText.contains("LAA ILAAHA ILLALLOOH"))
        assertTrue(dzikirJahr.indonesianText.contains("Zikir sekurang-kurangnya 165x"))
        assertTrue(dzikirJahr.sundaneseText.contains("sakirang-kirangna 165x"))

        // Check 3-language text extraction
        assertEquals(dzikirJahr.arabicText, dzikirJahr.getTextForLanguage(LiturgyLanguage.ARABIC))
        assertEquals(dzikirJahr.indonesianText, dzikirJahr.getTextForLanguage(LiturgyLanguage.INDONESIAN))
        assertEquals(dzikirJahr.sundaneseText, dzikirJahr.getTextForLanguage(LiturgyLanguage.SUNDANESE))
    }

    @Test
    fun testDzikirKhofiAuthenticity() {
        val khofiList = repository.getDzikirKhofiList()
        assertEquals(2, khofiList.size)

        val tawajuh = khofiList.first { it.id == "dzikir_khofi_tawajuh" }
        assertEquals(129, tawajuh.repetitionCount)
        assertTrue(tawajuh.arabicText.contains("\u0627\u0644\u0644\u0651\u064e\u0647\u064f"))
        assertTrue(tawajuh.latinText.contains("Lathifah Qolbi"))
        assertTrue(tawajuh.kaifiyatNote.contains("Guru Mursyid 38"))

        val asySyura = khofiList.first { it.id == "dzikir_khofi_asy_syura" }
        assertEquals(38, asySyura.repetitionCount)
        assertTrue(asySyura.arabicText.contains("\u0644\u064e\u0637\u0650\u064a\u0652\u0641\u064c\u06e2 \u0628\u0650\u0639\u0650\u0628\u064e\u0627\u062f\u0650\u0647\u0656"))
        assertTrue(asySyura.latinText.contains("yarzuqu"))
    }

    @Test
    fun testDailyPrayers() {
        val daily = repository.getDailyPrayersList()
        assertTrue(daily.isNotEmpty())
        assertTrue(daily.any { it.id == "doa_sebelum_tidur" })
        assertTrue(daily.any { it.id == "tarhim_subuh" })
        assertTrue(daily.any { it.id == "salam_wali_mursyid" })
        assertTrue(daily.any { it.id == "doa_istighotsah" })
        assertTrue(daily.any { it.id == "doa_rajab_syaban" })
    }

    @Test
    fun test12BulanHijriyahList() {
        val hijriyah = repository.get12BulanHijriyahList()
        assertEquals(12, hijriyah.size)
        assertEquals("Muharram", hijriyah[0].monthName)
        assertEquals("Shafar", hijriyah[1].monthName)
        assertEquals("Rabi'ul Awwal", hijriyah[2].monthName)
        assertEquals("Rabi'ul Akhir", hijriyah[3].monthName)
        assertEquals("Rajab", hijriyah[6].monthName)
        assertEquals("Sya'ban", hijriyah[7].monthName)
        assertEquals("Ramadhan", hijriyah[8].monthName)
        assertEquals("Syawwal", hijriyah[9].monthName)
        assertEquals("Dzulhijjah", hijriyah[11].monthName)
    }

    @Test
    fun testSholatSunnahList() {
        val sunnah = repository.getSholatSunnahList()
        assertTrue(sunnah.isNotEmpty())
        assertTrue(sunnah.any { it.id == "sholat_rajab" })
        assertTrue(sunnah.any { it.id == "sholat_nisfu_syaban" })
        assertTrue(sunnah.any { it.id == "sholat_tarawih_tqn" })
        assertTrue(sunnah.any { it.id == "sholat_lailatul_qadar" })
        assertTrue(sunnah.any { it.id == "sholat_lidafil_bala" })
    }
}
