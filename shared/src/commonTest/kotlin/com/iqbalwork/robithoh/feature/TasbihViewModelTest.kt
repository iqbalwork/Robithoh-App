package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.core.designsystem.KmpHapticFeedback
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiEffect
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class FakeHapticFeedback : KmpHapticFeedback {
    var clickCount = 0
    var milestoneCount = 0
    var successCount = 0

    override fun performClick() { clickCount++ }
    override fun performMilestone() { milestoneCount++ }
    override fun performSuccess() { successCount++ }
}

class TasbihViewModelTest {

    @Test
    fun testInitialState() {
        val fakeHaptic = FakeHapticFeedback()
        val viewModel = TasbihViewModel(hapticFeedback = fakeHaptic)
        val state = viewModel.currentState

        assertEquals(0, state.currentCount)
        assertEquals(0, state.lapCount)
        assertEquals(0, state.totalCount)
        assertTrue(state.isHapticEnabled)
        assertTrue(state.isSoundEnabled)
        assertFalse(state.isTargetReached)
        assertTrue(state.availablePresets.isNotEmpty())
    }

    @Test
    fun testIncrementCount() {
        val fakeHaptic = FakeHapticFeedback()
        val viewModel = TasbihViewModel(hapticFeedback = fakeHaptic)

        viewModel.onIntent(TasbihUiIntent.Increment)
        assertEquals(1, viewModel.currentState.currentCount)
        assertEquals(1, viewModel.currentState.totalCount)
        assertEquals(1, fakeHaptic.clickCount)

        viewModel.onIntent(TasbihUiIntent.Increment)
        assertEquals(2, viewModel.currentState.currentCount)
        assertEquals(2, viewModel.currentState.totalCount)
        assertEquals(2, fakeHaptic.clickCount)
    }

    @Test
    fun testTargetReachedMilestone() = runTest {
        val fakeHaptic = FakeHapticFeedback()
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = TasbihViewModel(
            hapticFeedback = fakeHaptic,
            dispatcher = testDispatcher
        )

        // Set small target of 3x for test
        viewModel.onIntent(TasbihUiIntent.SetTarget(3))
        assertEquals(3, viewModel.currentState.targetCount)

        viewModel.onIntent(TasbihUiIntent.Increment) // 1
        viewModel.onIntent(TasbihUiIntent.Increment) // 2
        assertFalse(viewModel.currentState.isTargetReached)

        viewModel.onIntent(TasbihUiIntent.Increment) // 3 -> reaches target
        assertTrue(viewModel.currentState.isTargetReached)
        assertEquals(1, viewModel.currentState.lapCount)
        assertEquals(3, viewModel.currentState.currentCount)
        assertEquals(3, viewModel.currentState.totalCount)
        assertTrue(fakeHaptic.milestoneCount > 0)
    }

    @Test
    fun testDecrementCount() {
        val fakeHaptic = FakeHapticFeedback()
        val viewModel = TasbihViewModel(hapticFeedback = fakeHaptic)

        viewModel.onIntent(TasbihUiIntent.Increment)
        viewModel.onIntent(TasbihUiIntent.Increment)
        assertEquals(2, viewModel.currentState.currentCount)

        viewModel.onIntent(TasbihUiIntent.Decrement)
        assertEquals(1, viewModel.currentState.currentCount)
        assertEquals(1, viewModel.currentState.totalCount)

        viewModel.onIntent(TasbihUiIntent.Decrement)
        assertEquals(0, viewModel.currentState.currentCount)

        // Does not go negative
        viewModel.onIntent(TasbihUiIntent.Decrement)
        assertEquals(0, viewModel.currentState.currentCount)
    }

    @Test
    fun testResetCount() {
        val fakeHaptic = FakeHapticFeedback()
        val viewModel = TasbihViewModel(hapticFeedback = fakeHaptic)

        viewModel.onIntent(TasbihUiIntent.Increment)
        viewModel.onIntent(TasbihUiIntent.Increment)
        viewModel.onIntent(TasbihUiIntent.RequestReset)
        assertTrue(viewModel.currentState.showResetDialog)

        viewModel.onIntent(TasbihUiIntent.ConfirmReset)
        assertEquals(0, viewModel.currentState.currentCount)
        assertEquals(0, viewModel.currentState.lapCount)
        assertFalse(viewModel.currentState.showResetDialog)
    }

    @Test
    fun testSelectDzikirPreset() {
        val fakeHaptic = FakeHapticFeedback()
        val viewModel = TasbihViewModel(hapticFeedback = fakeHaptic)

        val shalawatPreset = viewModel.currentState.availablePresets.first { it.id == "shalawat_bani_hasyim" }
        viewModel.onIntent(TasbihUiIntent.SelectDzikir(shalawatPreset))

        assertEquals("shalawat_bani_hasyim", viewModel.currentState.selectedDzikirId)
        assertEquals("Shalawat Bani Hasyim", viewModel.currentState.selectedDzikirTitle)
        assertEquals(165, viewModel.currentState.targetCount)
        assertEquals(0, viewModel.currentState.currentCount)
    }

    @Test
    fun testToggleHapticAndSound() {
        val fakeHaptic = FakeHapticFeedback()
        val viewModel = TasbihViewModel(hapticFeedback = fakeHaptic)

        assertTrue(viewModel.currentState.isHapticEnabled)
        viewModel.onIntent(TasbihUiIntent.ToggleHaptic)
        assertFalse(viewModel.currentState.isHapticEnabled)

        assertTrue(viewModel.currentState.isSoundEnabled)
        viewModel.onIntent(TasbihUiIntent.ToggleSound)
        assertFalse(viewModel.currentState.isSoundEnabled)
    }

    @Test
    fun testSyncData() {
        val fakeHaptic = FakeHapticFeedback()
        val viewModel = TasbihViewModel(hapticFeedback = fakeHaptic)

        viewModel.onIntent(
            TasbihUiIntent.SyncData(
                count = 13,
                target = 165,
                dzikirTitle = "Dzikir Jahr"
            )
        )

        val state = viewModel.currentState
        assertEquals(13, state.currentCount)
        assertEquals(165, state.targetCount)
        assertEquals(0, state.lapCount)
        assertEquals("Dzikir Jahr", state.selectedDzikirTitle)
        assertEquals("لَا إِلَهَ إِلَّا اللَّهُ", state.selectedDzikirArabic)
        assertEquals("dzikir_nafi_itsbat", state.selectedDzikirId)
        assertFalse(state.isTargetReached)
    }
}
