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
        assertEquals(8, jahrList.size)

        // Check Nafi Itsbat (item 7)
        val nafiItsbat = jahrList.first { it.id == "dzikir_jahr_7" }
        assertEquals(165, nafiItsbat.repetitionCount)
        assertEquals("لَا إِلَهَ إِلَّا اللَّهُ", nafiItsbat.arabicText)
        assertTrue(nafiItsbat.latinText.contains("LAA ILAAHA ILLALLAAH"))
        assertTrue(nafiItsbat.indonesianText.contains("Tiada Tuhan selain Allah"))
        assertTrue(nafiItsbat.sundaneseText.contains("Teu aya deui Pangeran"))

        // Check 3-language text extraction
        assertEquals(nafiItsbat.arabicText, nafiItsbat.getTextForLanguage(LiturgyLanguage.ARABIC))
        assertEquals(nafiItsbat.indonesianText, nafiItsbat.getTextForLanguage(LiturgyLanguage.INDONESIAN))
        assertEquals(nafiItsbat.sundaneseText, nafiItsbat.getTextForLanguage(LiturgyLanguage.SUNDANESE))
    }

    @Test
    fun testDzikirKhofiAuthenticity() {
        val khofiList = repository.getDzikirKhofiList()
        assertTrue(khofiList.isNotEmpty())
        val ismuDzat = khofiList.first { it.id == "dzikir_khofi_1" }
        assertTrue(ismuDzat.arabicText.contains("اللَّهُ"))
        assertTrue(ismuDzat.kaifiyatNote.contains("Lathifah Qolbi"))
        assertTrue(ismuDzat.kaifiyatNote.contains("Abah Aos"))

        val munajat = khofiList.first { it.id == "dzikir_khofi_2" }
        assertTrue(munajat.arabicText.contains("إِلَهِي أَنْتَ مَقْصُودِي"))
        assertTrue(munajat.latinText.contains("Ilaahii Anta Maqshuudii"))
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
        assertEquals("Syawal", hijriyah[9].monthName)
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
