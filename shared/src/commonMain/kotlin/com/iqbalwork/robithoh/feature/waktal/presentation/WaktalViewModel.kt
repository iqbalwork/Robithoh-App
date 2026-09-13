package com.iqbalwork.robithoh.feature.waktal.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.feature.waktal.data.WaktalRepository
import com.iqbalwork.robithoh.feature.waktal.data.sync.WaktalSyncManager
import com.iqbalwork.robithoh.feature.waktal.domain.HaversineDistance
import com.iqbalwork.robithoh.feature.waktal.domain.WakilTalqin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WaktalViewModel(
    private val repository: WaktalRepository,
    private val syncManager: WaktalSyncManager
) : MviViewModel<WaktalListUiState, WaktalListUiIntent, WaktalListUiEffect>(
    initialState = WaktalListUiState()
) {

    init {
        loadData()
        observeSyncState()
    }

    override fun onIntent(intent: WaktalListUiIntent) {
        when (intent) {
            is WaktalListUiIntent.UpdateSearchQuery -> {
                updateState { copy(searchQuery = intent.query) }
                applyFilters()
            }
            is WaktalListUiIntent.SelectStatus -> {
                updateState { copy(selectedStatus = intent.status) }
                applyFilters()
            }
            is WaktalListUiIntent.SelectProvince -> {
                updateState { copy(selectedProvince = intent.province) }
                applyFilters()
            }
            is WaktalListUiIntent.ToggleSortByDistance -> {
                updateState { copy(isSortByDistance = intent.enabled) }
                applyFilters()
            }
            is WaktalListUiIntent.SetUserLocation -> {
                updateState {
                    copy(
                        userLatitude = intent.lat,
                        userLongitude = intent.lng
                    )
                }
                applyFilters()
            }
            is WaktalListUiIntent.SelectWaktal -> {
                sendEffect(WaktalListUiEffect.NavigateToDetail(intent.id))
            }
            WaktalListUiIntent.TriggerSync -> {
                triggerManualSync()
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val provinces = repository.getDistinctProvinces()
            val items = repository.getWakilTalqinList()
            updateState {
                copy(
                    availableProvinces = provinces,
                    items = items,
                    isLoading = false
                )
            }
            applyFilters()
        }
    }

    private fun observeSyncState() {
        viewModelScope.launch {
            syncManager.syncState.collectLatest { syncState ->
                updateState { copy(syncState = syncState) }
            }
        }
    }

    private fun applyFilters() {
        viewModelScope.launch {
            val query = currentState.searchQuery
            val status = currentState.selectedStatus
            val province = currentState.selectedProvince
            val userLat = currentState.userLatitude
            val userLng = currentState.userLongitude
            val sortByDistance = currentState.isSortByDistance

            var filtered = repository.getWakilTalqinList(
                query = query,
                status = status,
                provinsi = province
            )

            if (userLat != null && userLng != null) {
                filtered = filtered.map { item ->
                    if (item.latitude != null && item.longitude != null) {
                        val distance = HaversineDistance.calculateKm(
                            lat1 = userLat,
                            lon1 = userLng,
                            lat2 = item.latitude,
                            lon2 = item.longitude
                        )
                        item.copy(distanceKm = distance)
                    } else item
                }
            }

            if (sortByDistance && userLat != null && userLng != null) {
                filtered = filtered.sortedWith(
                    compareBy<WakilTalqin> { it.distanceKm == null }
                        .thenBy { it.distanceKm ?: Double.MAX_VALUE }
                )
            }

            updateState { copy(filteredItems = filtered) }
        }
    }

    private fun triggerManualSync() {
        viewModelScope.launch {
            val result = syncManager.syncWaktalDirectory(force = true)
            result.onSuccess { count ->
                if (count > 0) {
                    sendEffect(WaktalListUiEffect.ShowSnackbar("Berhasil memperbarui $count data Wakil Talqin."))
                    loadData()
                } else {
                    sendEffect(WaktalListUiEffect.ShowSnackbar("Data Wakil Talqin sudah yang terbaru."))
                }
            }.onFailure {
                sendEffect(WaktalListUiEffect.ShowSnackbar("Gagal memperbarui data. Menggunakan data offline."))
            }
        }
    }
}
