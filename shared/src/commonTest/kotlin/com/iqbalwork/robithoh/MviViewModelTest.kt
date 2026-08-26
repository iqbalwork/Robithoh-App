package com.iqbalwork.robithoh

import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.core.presentation.UiEffect
import com.iqbalwork.robithoh.core.presentation.UiIntent
import com.iqbalwork.robithoh.core.presentation.UiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

data class TestState(val count: Int = 0) : UiState
sealed interface TestIntent : UiIntent {
    data object Increment : TestIntent
    data class SetCount(val value: Int) : TestIntent
    data class TriggerToast(val message: String) : TestIntent
}
sealed interface TestEffect : UiEffect {
    data class ShowToast(val message: String) : TestEffect
}

class TestViewModel : MviViewModel<TestState, TestIntent, TestEffect>(TestState()) {
    override fun onIntent(intent: TestIntent) {
        when (intent) {
            is TestIntent.Increment -> updateState { copy(count = count + 1) }
            is TestIntent.SetCount -> updateState { copy(count = intent.value) }
            is TestIntent.TriggerToast -> sendEffect(TestEffect.ShowToast(intent.message))
        }
    }
}

class MviViewModelTest {

    @Test
    fun testInitialState() {
        val viewModel = TestViewModel()
        assertEquals(0, viewModel.uiState.value.count)
        assertEquals(0, viewModel.currentState.count)
    }

    @Test
    fun testStateUpdateOnIntent() {
        val viewModel = TestViewModel()
        viewModel.onIntent(TestIntent.Increment)
        assertEquals(1, viewModel.uiState.value.count)

        viewModel.onIntent(TestIntent.SetCount(33))
        assertEquals(33, viewModel.uiState.value.count)
    }

    @Test
    fun testEffectEmission() = runTest {
        val viewModel = TestViewModel()
        viewModel.onIntent(TestIntent.TriggerToast("Subhanallah"))
        val effect = viewModel.effect.first()
        assertEquals(TestEffect.ShowToast("Subhanallah"), effect)
    }
}
