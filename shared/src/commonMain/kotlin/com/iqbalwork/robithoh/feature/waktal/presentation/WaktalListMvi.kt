package com.iqbalwork.robithoh.feature.waktal.presentation

import com.iqbalwork.robithoh.core.presentation.UiEffect
import com.iqbalwork.robithoh.core.presentation.UiIntent
import com.iqbalwork.robithoh.core.presentation.UiState
import com.iqbalwork.robithoh.feature.waktal.data.sync.WaktalSyncState
import com.iqbalwork.robithoh.feature.waktal.domain.WakilTalqin
import com.iqbalwork.robithoh.feature.waktal.domain.WaktalStatus

data class WaktalListUiState(
    val searchQuery: String = "",
    val selectedStatus: WaktalStatus = WaktalStatus.SEMUA,
    val selectedProvince: String? = null,
    val isSortByDistance: Boolean = false,
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
    val availableProvinces: List<String> = emptyList(),
    val items: List<WakilTalqin> = emptyList(),
    val filteredItems: List<WakilTalqin> = emptyList(),
    val isLoading: Boolean = false,
    val syncState: WaktalSyncState = WaktalSyncState.Idle
) : UiState

sealed interface WaktalListUiIntent : UiIntent {
    data class UpdateSearchQuery(val query: String) : WaktalListUiIntent
    data class SelectStatus(val status: WaktalStatus) : WaktalListUiIntent
    data class SelectProvince(val province: String?) : WaktalListUiIntent
    data class ToggleSortByDistance(val enabled: Boolean) : WaktalListUiIntent
    data class SetUserLocation(val lat: Double, val lng: Double) : WaktalListUiIntent
    data class SelectWaktal(val id: Int) : WaktalListUiIntent
    data object TriggerSync : WaktalListUiIntent
    data object ResetFilters : WaktalListUiIntent
}

sealed interface WaktalListUiEffect : UiEffect {
    data class NavigateToDetail(val id: Int) : WaktalListUiEffect
    data class ShowSnackbar(val message: String) : WaktalListUiEffect
}
