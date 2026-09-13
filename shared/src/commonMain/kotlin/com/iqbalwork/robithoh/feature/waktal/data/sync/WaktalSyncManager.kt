package com.iqbalwork.robithoh.feature.waktal.data.sync

import com.iqbalwork.robithoh.feature.waktal.data.WaktalRepository
import com.iqbalwork.robithoh.feature.waktal.data.remote.WakilTalqinItemDto
import com.iqbalwork.robithoh.feature.waktal.data.remote.WaktalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

sealed interface WaktalSyncState {
    data object Idle : WaktalSyncState
    data class Checking(val message: String = "Memeriksa versi direktori Waktal...") : WaktalSyncState
    data class Syncing(val current: Int = 0, val total: Int = 0) : WaktalSyncState
    data class Success(val updatedCount: Int) : WaktalSyncState
    data class Error(val message: String) : WaktalSyncState
}

class WaktalSyncManager(
    private val apiService: WaktalApiService,
    private val repository: WaktalRepository
) {
    private val _syncState = MutableStateFlow<WaktalSyncState>(WaktalSyncState.Idle)
    val syncState: StateFlow<WaktalSyncState> = _syncState.asStateFlow()

    suspend fun syncWaktalDirectory(force: Boolean = false): Result<Int> = withContext(Dispatchers.Default) {
        _syncState.value = WaktalSyncState.Checking()
        try {
            var remoteVersion = 1
            var checksum: String? = null

            try {
                val manifest = apiService.checkVersionManifest()
                val remoteWaktal = manifest.entities["waktal"]
                remoteVersion = remoteWaktal?.versionCode ?: 1
                checksum = remoteWaktal?.checksum
            } catch (e: Exception) {
                println("[WaktalSyncManager] Manifest check error: ${e.message}")
            }

            val localVersion = repository.getLocalVersionCode()

            if (force || remoteVersion > localVersion) {
                _syncState.value = WaktalSyncState.Syncing(0, 0)
                var items: List<WakilTalqinItemDto> = emptyList()

                try {
                    val deltaResponse = apiService.fetchWaktalDelta()
                    items = deltaResponse.data ?: emptyList()
                } catch (e: Exception) {
                    println("[WaktalSyncManager] Delta fetch error: ${e.message}, trying list API...")
                    try {
                        val listResponse = apiService.fetchWaktalList(perPage = 100)
                        items = listResponse.data ?: emptyList()
                    } catch (e2: Exception) {
                        println("[WaktalSyncManager] List API fetch error: ${e2.message}")
                    }
                }

                if (items.isNotEmpty()) {
                    repository.saveWaktalSnapshot(
                        items = items,
                        versionCode = if (remoteVersion > localVersion) remoteVersion else localVersion + 1,
                        checksum = checksum
                    )
                    _syncState.value = WaktalSyncState.Success(items.size)
                    Result.success(items.size)
                } else {
                    _syncState.value = WaktalSyncState.Success(0)
                    Result.success(0)
                }
            } else {
                _syncState.value = WaktalSyncState.Success(0)
                Result.success(0)
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Gagal memperbarui data Waktal"
            _syncState.value = WaktalSyncState.Error(errorMessage)
            Result.failure(e)
        }
    }
}
