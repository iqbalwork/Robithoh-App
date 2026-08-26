package com.iqbalwork.robithoh.feature.tasbih.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.core.designsystem.KmpHapticFeedback
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasbihViewModel(
    private val hapticFeedback: KmpHapticFeedback = getHapticFeedback(),
    private val database: RobithohDatabase? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : MviViewModel<TasbihUiState, TasbihUiIntent, TasbihUiEffect>(
    TasbihUiState()
) {

    init {
        loadInitialProgress(currentState.selectedDzikirId)
    }

    override fun onIntent(intent: TasbihUiIntent) {
        when (intent) {
            is TasbihUiIntent.Increment -> handleIncrement()
            is TasbihUiIntent.Decrement -> handleDecrement()
            is TasbihUiIntent.RequestReset -> updateState { copy(showResetDialog = true) }
            is TasbihUiIntent.ConfirmReset -> handleReset()
            is TasbihUiIntent.DismissResetDialog -> updateState { copy(showResetDialog = false) }
            is TasbihUiIntent.SetTarget -> {
                updateState { copy(targetCount = intent.target, isTargetReached = false) }
                saveProgress()
            }
            is TasbihUiIntent.ShowCustomTargetDialog -> updateState { copy(showCustomTargetDialog = true) }
            is TasbihUiIntent.DismissCustomTargetDialog -> updateState { copy(showCustomTargetDialog = false) }
            is TasbihUiIntent.SelectDzikir -> {
                updateState {
                    copy(
                        selectedDzikirId = intent.preset.id,
                        selectedDzikirTitle = intent.preset.title,
                        selectedDzikirArabic = intent.preset.arabic,
                        targetCount = intent.preset.defaultTarget,
                        currentCount = 0,
                        lapCount = 0,
                        isTargetReached = false
                    )
                }
                loadInitialProgress(intent.preset.id)
            }
            is TasbihUiIntent.ToggleHaptic -> updateState { copy(isHapticEnabled = !isHapticEnabled) }
            is TasbihUiIntent.ToggleSound -> updateState { copy(isSoundEnabled = !isSoundEnabled) }
            is TasbihUiIntent.DismissTargetReached -> updateState { copy(isTargetReached = false) }
            is TasbihUiIntent.LoadProgress -> loadInitialProgress(intent.dzikirId)
        }
    }

    private fun handleIncrement() {
        val nextCount = currentState.currentCount + 1
        val target = currentState.targetCount
        val reachedTarget = nextCount >= target
        val nextLap = if (reachedTarget) currentState.lapCount + 1 else currentState.lapCount
        val resetCount = if (reachedTarget) 0 else nextCount
        val total = currentState.totalCount + 1

        if (currentState.isHapticEnabled) {
            if (reachedTarget || nextCount % 33 == 0) {
                hapticFeedback.performMilestone()
                sendEffect(TasbihUiEffect.TriggerHapticMilestone)
            } else {
                hapticFeedback.performClick()
                sendEffect(TasbihUiEffect.TriggerHapticTap)
            }
        }

        if (currentState.isSoundEnabled) {
            if (reachedTarget) {
                sendEffect(TasbihUiEffect.PlayMilestoneChime)
            } else {
                sendEffect(TasbihUiEffect.PlayClickChime)
            }
        }

        if (reachedTarget) {
            sendEffect(TasbihUiEffect.ShowMilestoneToast(target, target))
        }

        updateState {
            copy(
                currentCount = resetCount,
                lapCount = nextLap,
                totalCount = total,
                isTargetReached = reachedTarget
            )
        }

        saveProgress()
    }

    private fun handleDecrement() {
        if (currentState.currentCount > 0) {
            val prevCount = currentState.currentCount - 1
            val total = (currentState.totalCount - 1).coerceAtLeast(0)
            updateState {
                copy(
                    currentCount = prevCount,
                    totalCount = total,
                    isTargetReached = false
                )
            }
            if (currentState.isHapticEnabled) {
                hapticFeedback.performClick()
            }
            saveProgress()
        }
    }

    private fun handleReset() {
        updateState {
            copy(
                currentCount = 0,
                lapCount = 0,
                isTargetReached = false,
                showResetDialog = false
            )
        }
        if (currentState.isHapticEnabled) {
            hapticFeedback.performClick()
        }
        viewModelScope.launch(dispatcher) {
            try {
                database?.robithohDatabaseQueries?.resetAmaliyahProgress(
                    lastUpdated = 20260824L,
                    id = currentState.selectedDzikirId
                )
            } catch (_: Exception) {}
        }
    }

    private fun loadInitialProgress(dzikirId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val entity = database?.robithohDatabaseQueries?.getAmaliyahProgressById(dzikirId)?.executeAsOneOrNull()
                if (entity != null) {
                    updateState {
                        copy(
                            currentCount = entity.current_count.toInt(),
                            targetCount = entity.target_count.toInt().coerceAtLeast(1)
                        )
                    }
                }
            } catch (_: Exception) {}
        }
    }

    private fun saveProgress() {
        val state = currentState
        viewModelScope.launch(dispatcher) {
            try {
                database?.robithohDatabaseQueries?.insertOrUpdateAmaliyahProgress(
                    id = state.selectedDzikirId,
                    title = state.selectedDzikirTitle,
                    current_count = state.currentCount.toLong(),
                    target_count = state.targetCount.toLong(),
                    last_updated = 20260824L,
                    is_completed = if (state.currentCount >= state.targetCount) 1L else 0L
                )
            } catch (_: Exception) {}
        }
    }
}
