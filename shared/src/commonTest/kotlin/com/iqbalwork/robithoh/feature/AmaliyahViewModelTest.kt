package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.feature.amaliyah.data.AmaliyahRepository
import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.model.AmaliyahCategory
import com.iqbalwork.robithoh.feature.amaliyah.model.DzikirType
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiEffect
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class AmaliyahViewModelTest {

    private val repository = AmaliyahRepository()
    private val calculator = PrayerTimesCalculator()

    @Test
    fun testInitialState() {
        val viewModel = AmaliyahViewModel(repository, calculator)
        val state = viewModel.currentState

        assertEquals(LiturgyLanguage.ARABIC, state.selectedLanguage)
        assertEquals(AmaliyahCategory.DZIKIR_BA_DA_SHOLAT, state.selectedCategory)
        assertEquals(DzikirType.JAHR, state.activeDzikirType)
        assertTrue(state.dzikirJahrList.isNotEmpty())
        assertTrue(state.dzikirKhofiList.isNotEmpty())
        assertTrue(state.dailyPrayersList.isNotEmpty())
        assertTrue(state.hijriyahList.isNotEmpty())
        assertTrue(state.sholatSunnahList.isNotEmpty())
        assertNotNull(state.prayerSchedule)
        assertNotNull(state.qiblaInfo)
    }

    @Test
    fun testSelectLanguage() {
        val viewModel = AmaliyahViewModel(repository, calculator)

        viewModel.onIntent(AmaliyahUiIntent.SelectLanguage(LiturgyLanguage.SUNDANESE))
        assertEquals(LiturgyLanguage.SUNDANESE, viewModel.currentState.selectedLanguage)

        viewModel.onIntent(AmaliyahUiIntent.SelectLanguage(LiturgyLanguage.INDONESIAN))
        assertEquals(LiturgyLanguage.INDONESIAN, viewModel.currentState.selectedLanguage)
    }

    @Test
    fun testSelectCategory() {
        val viewModel = AmaliyahViewModel(repository, calculator)

        viewModel.onIntent(AmaliyahUiIntent.SelectCategory(AmaliyahCategory.BULAN_HIJRIYAH))
        assertEquals(AmaliyahCategory.BULAN_HIJRIYAH, viewModel.currentState.selectedCategory)

        viewModel.onIntent(AmaliyahUiIntent.SelectCategory(AmaliyahCategory.DOA_HARIAN))
        assertEquals(AmaliyahCategory.DOA_HARIAN, viewModel.currentState.selectedCategory)
    }

    @Test
    fun testSelectLocation() {
        val viewModel = AmaliyahViewModel(repository, calculator)
        val jakarta = PrayerTimesCalculator.PRESET_LOCATIONS.first { it.name.contains("Jakarta") }

        viewModel.onIntent(AmaliyahUiIntent.SelectLocation(jakarta))
        assertEquals(jakarta.name, viewModel.currentState.selectedLocation.name)
        assertEquals(jakarta.name, viewModel.currentState.prayerSchedule?.locationName)
    }

    @Test
    fun testOpenTasbihWithTargetEffect() = runTest {
        val viewModel = AmaliyahViewModel(repository, calculator)
        viewModel.onIntent(AmaliyahUiIntent.OpenTasbihWithTarget(165, "Dzikir Jahr"))

        val effect = viewModel.effect.first()
        assertTrue(effect is AmaliyahUiEffect.NavigateToTasbih)
        assertEquals(165, effect.targetCount)
        assertEquals("Dzikir Jahr", effect.dzikirTitle)
    }
}
