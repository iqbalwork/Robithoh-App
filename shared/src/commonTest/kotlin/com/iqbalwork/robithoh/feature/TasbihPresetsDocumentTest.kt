package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihDzikirPreset
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel
import com.iqbalwork.robithoh.feature.tasbih.presentation.defaultTasbihPresets
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class TasbihPresetsDocumentTest {

    @Test
    fun testParseMarkdownToTasbihPresets_parsesAllStandardAndCustomItems() {
        val repo = MarkdownDocumentRepository()
        val sampleMarkdown = """
            # Daftar Wirid & Dzikir Tasbih
            
            Koleksi wirid dan dzikir untuk tasbih digital.
            
            ---
            
            ## Tahlil
            (165x)
            
            لَا إِلٰهَ إِلَّا اللَّهُ
            
            *Pengagungan kebesaran Allah di atas seluruh alam.*
            
            ---
            
            ## Wirid Kemalaikatan: Ahad (222x)
            
            حَيٌّ قَيُّومٌ
            
            *HAYYUN QOYYUUM (222x) • Malaikat Syamsayaa Yayil • Setiap Sabtu malam bagi lahir hari Ahad.*
            
            ---
            
            ## Sholawat Nariyah
            (4444x)
            
            اللَّهُمَّ صَلِّ صَلَاةً كَامِلَةً وَسَلِّمْ سَلَامًا تَامًّا
            
            *Membuka pintu rezeki dan melapangkan kesempitan hidup.*
        """.trimIndent()

        val presets = repo.parseMarkdownToTasbihPresets(sampleMarkdown)

        assertEquals(3, presets.size)

        // 1. Tahlil
        val tahlil = presets[0]
        assertEquals("tahlil_tqn", tahlil.id)
        assertEquals("Tahlil", tahlil.title)
        assertEquals(165, tahlil.defaultTarget)
        assertEquals("لَا إِلٰهَ إِلَّا اللَّهُ", tahlil.arabic)
        assertEquals("Pengagungan kebesaran Allah di atas seluruh alam.", tahlil.virtue)

        // 2. Wirid Kemalaikatan Ahad (count in heading)
        val ahad = presets[1]
        assertEquals("wirid_kemalaikatan_ahad", ahad.id)
        assertEquals("Wirid Kemalaikatan: Ahad", ahad.title)
        assertEquals(222, ahad.defaultTarget)
        assertEquals("حَيٌّ قَيُّومٌ", ahad.arabic)
        assertTrue(ahad.virtue.contains("Malaikat Syamsayaa Yayil"))

        // 3. Sholawat Nariyah (custom item dynamically added)
        val nariyah = presets[2]
        assertEquals("sholawat_nariyah", nariyah.id)
        assertEquals("Sholawat Nariyah", nariyah.title)
        assertEquals(4444, nariyah.defaultTarget)
        assertTrue(nariyah.arabic.contains("اللَّهُمَّ صَلِّ صَلَاةً كَامِلَةً"))
        assertEquals("Membuka pintu rezeki dan melapangkan kesempitan hidup.", nariyah.virtue)
    }

    @Test
    fun testDefaultTasbihPresets_containsThirteenItems() {
        assertEquals(13, defaultTasbihPresets.size)
        val ids = defaultTasbihPresets.map { it.id }
        assertTrue(ids.contains("tahlil_tqn"))
        assertTrue(ids.contains("wirid_kemalaikatan_ahad"))
        assertTrue(ids.contains("wirid_kemalaikatan_senin"))
        assertTrue(ids.contains("wirid_kemalaikatan_selasa"))
        assertTrue(ids.contains("wirid_kemalaikatan_rabu"))
        assertTrue(ids.contains("wirid_kemalaikatan_kamis"))
        assertTrue(ids.contains("wirid_kemalaikatan_jumat"))
        assertTrue(ids.contains("wirid_kemalaikatan_sabtu"))
    }

    @Test
    fun testReloadPresetsIntent_updatesAvailablePresets() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val fakeHaptic = FakeHapticFeedback()
        val repo = MarkdownDocumentRepository()

        val viewModel = TasbihViewModel(
            hapticFeedback = fakeHaptic,
            repository = repo,
            dispatcher = testDispatcher
        )

        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.currentState.availablePresets.isNotEmpty())

        viewModel.onIntent(TasbihUiIntent.ReloadPresets)
        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.currentState.availablePresets.isNotEmpty())
    }
}
