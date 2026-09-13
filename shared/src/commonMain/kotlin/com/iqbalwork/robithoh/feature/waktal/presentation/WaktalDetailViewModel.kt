package com.iqbalwork.robithoh.feature.waktal.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.feature.waktal.data.WaktalRepository
import kotlinx.coroutines.launch

class WaktalDetailViewModel(
    private val repository: WaktalRepository
) : MviViewModel<WaktalDetailUiState, WaktalDetailUiIntent, WaktalDetailUiEffect>(
    initialState = WaktalDetailUiState()
) {

    override fun onIntent(intent: WaktalDetailUiIntent) {
        when (intent) {
            is WaktalDetailUiIntent.LoadDetail -> loadDetail(intent.id)
            WaktalDetailUiIntent.DialPhone -> handleDialPhone()
            WaktalDetailUiIntent.OpenWhatsApp -> handleOpenWhatsApp()
            WaktalDetailUiIntent.OpenMap -> handleOpenMap()
        }
    }

    private fun loadDetail(id: Int) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            val item = repository.getWakilTalqinById(id)
            if (item != null) {
                updateState { copy(waktal = item, isLoading = false) }
            } else {
                updateState {
                    copy(
                        isLoading = false,
                        errorMessage = "Data Wakil Talqin tidak ditemukan."
                    )
                }
            }
        }
    }

    private fun handleDialPhone() {
        val uri = currentState.waktal?.dialPhoneUri
        if (!uri.isNullOrBlank()) {
            sendEffect(WaktalDetailUiEffect.LaunchUri(uri))
        } else {
            sendEffect(WaktalDetailUiEffect.ShowToast("Nomor telepon tidak tersedia."))
        }
    }

    private fun handleOpenWhatsApp() {
        val uri = currentState.waktal?.whatsappUrl
        if (!uri.isNullOrBlank()) {
            sendEffect(WaktalDetailUiEffect.LaunchUri(uri))
        } else {
            sendEffect(WaktalDetailUiEffect.ShowToast("Nomor WhatsApp tidak tersedia."))
        }
    }

    private fun handleOpenMap() {
        val uri = currentState.waktal?.mapIntentUri
        if (!uri.isNullOrBlank()) {
            sendEffect(WaktalDetailUiEffect.LaunchUri(uri))
        } else {
            sendEffect(WaktalDetailUiEffect.ShowToast("Koordinat lokasi tidak tersedia."))
        }
    }
}
