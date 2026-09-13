package com.iqbalwork.robithoh.feature.waktal.presentation

import com.iqbalwork.robithoh.core.presentation.UiEffect
import com.iqbalwork.robithoh.core.presentation.UiIntent
import com.iqbalwork.robithoh.core.presentation.UiState
import com.iqbalwork.robithoh.feature.waktal.domain.WakilTalqin

data class WaktalDetailUiState(
    val waktal: WakilTalqin? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) : UiState

sealed interface WaktalDetailUiIntent : UiIntent {
    data class LoadDetail(val id: Int) : WaktalDetailUiIntent
    data object DialPhone : WaktalDetailUiIntent
    data object OpenWhatsApp : WaktalDetailUiIntent
    data object OpenMap : WaktalDetailUiIntent
}

sealed interface WaktalDetailUiEffect : UiEffect {
    data class LaunchUri(val uri: String) : WaktalDetailUiEffect
    data class ShowToast(val message: String) : WaktalDetailUiEffect
}
