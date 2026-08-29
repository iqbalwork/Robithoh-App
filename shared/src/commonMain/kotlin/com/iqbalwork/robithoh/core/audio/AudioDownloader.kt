package com.iqbalwork.robithoh.core.audio

import kotlinx.coroutines.flow.StateFlow

sealed interface DownloadProgressState {
    data object Idle : DownloadProgressState

    data class Downloading(
        val trackId: String,
        val fileName: String,
        val title: String,
        val bytesDownloaded: Long,
        val totalBytes: Long,
        val progress: Float // 0.0f .. 1.0f
    ) : DownloadProgressState

    data class Completed(
        val trackId: String,
        val fileName: String,
        val localFilePath: String
    ) : DownloadProgressState

    data class Error(
        val trackId: String,
        val fileName: String,
        val errorMessage: String
    ) : DownloadProgressState
}

interface AudioDownloader {
    val downloadState: StateFlow<DownloadProgressState>

    suspend fun downloadAudio(
        trackId: String,
        title: String,
        remoteUrl: String,
        fileName: String,
        expectedSizeBytes: Long = 0L
    ): Result<String>

    fun cancelDownload(trackId: String? = null)
    fun resetState()
}

expect fun createAudioDownloader(cacheManager: AudioCacheManager = createAudioCacheManager()): AudioDownloader
