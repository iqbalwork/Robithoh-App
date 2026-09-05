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
            is TasbihUiIntent.ToggleFloatingExpand -> updateState { copy(isFloatingExpanded = !isFloatingExpanded) }
            is TasbihUiIntent.SetFloatingExpanded -> updateState { copy(isFloatingExpanded = intent.expanded) }
            is TasbihUiIntent.SetFloatingVisible -> updateState { copy(isFloatingVisible = intent.visible) }
            is TasbihUiIntent.SyncData -> handleSyncData(intent)
        }
    }

    private fun handleSyncData(intent: TasbihUiIntent.SyncData) {
        val target = intent.target ?: currentState.targetCount
        val lap = if (target > 0) intent.count / target else currentState.lapCount
        val matchedPreset = currentState.availablePresets.find {
            intent.dzikirTitle != null && (
                it.title.equals(intent.dzikirTitle, ignoreCase = true) ||
                intent.dzikirTitle.contains(it.title, ignoreCase = true) ||
                it.title.contains(intent.dzikirTitle, ignoreCase = true)
            )
        }
        val title = matchedPreset?.title ?: intent.dzikirTitle ?: currentState.selectedDzikirTitle
        val arabic = matchedPreset?.arabic ?: currentState.selectedDzikirArabic
        val dzikirId = matchedPreset?.id ?: currentState.selectedDzikirId

        updateState {
            copy(
                currentCount = intent.count,
                targetCount = target,
                lapCount = lap,
                selectedDzikirTitle = title,
                selectedDzikirArabic = arabic,
                selectedDzikirId = dzikirId,
                isTargetReached = target > 0 && intent.count >= target && (intent.count % target == 0)
            )
        }
        saveProgress()
    }

    private fun handleIncrement() {
        val nextCount = currentState.currentCount + 1
        val target = currentState.targetCount
        val reachedMilestone = target > 0 && nextCount % target == 0
        val nextLap = if (reachedMilestone) currentState.lapCount + 1 else currentState.lapCount

        if (currentState.isHapticEnabled) {
            if (reachedMilestone || nextCount % 33 == 0) {
                hapticFeedback.performMilestone()
                sendEffect(TasbihUiEffect.TriggerHapticMilestone)
            } else {
                hapticFeedback.performClick()
                sendEffect(TasbihUiEffect.TriggerHapticTap)
            }
        }

        if (currentState.isSoundEnabled) {
            if (reachedMilestone) {
                sendEffect(TasbihUiEffect.PlayMilestoneChime)
            } else {
                sendEffect(TasbihUiEffect.PlayClickChime)
            }
        }

        if (reachedMilestone) {
            sendEffect(TasbihUiEffect.ShowMilestoneToast(target, target))
        }

        updateState {
            copy(
                currentCount = nextCount,
                lapCount = nextLap,
                totalCount = totalCount + 1,
                isTargetReached = reachedMilestone
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
                notifyTasbihWidgetUpdate()
            } catch (_: Exception) {}
        }
    }

    private fun loadInitialProgress(dzikirId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                var entity = database?.robithohDatabaseQueries?.getAmaliyahProgressById(dzikirId)?.executeAsOneOrNull()
                if (entity == null && dzikirId == "dzikir_jahr") {
                    entity = database?.robithohDatabaseQueries?.getAmaliyahProgressById("dzikir_nafi_itsbat")?.executeAsOneOrNull()
                }
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
                notifyTasbihWidgetUpdate()
            } catch (_: Exception) {}
        }
    }
}
