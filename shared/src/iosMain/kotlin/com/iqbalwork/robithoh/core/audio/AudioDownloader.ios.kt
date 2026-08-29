@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.iqbalwork.robithoh.core.audio

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.Foundation.NSFileManager
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSURLSessionConfiguration
import platform.Foundation.NSURLSessionDownloadTask
import platform.Foundation.downloadTaskWithRequest
import platform.Foundation.setValue

class IosAudioDownloader(
    private val cacheManager: AudioCacheManager
) : AudioDownloader {

    private val _downloadState = MutableStateFlow<DownloadProgressState>(DownloadProgressState.Idle)
    override val downloadState: StateFlow<DownloadProgressState> = _downloadState.asStateFlow()

    private var activeJob: Job? = null
    private var activeTrackId: String? = null
    private var activeTask: NSURLSessionDownloadTask? = null

    override suspend fun downloadAudio(
        trackId: String,
        title: String,
        remoteUrl: String,
        fileName: String,
        expectedSizeBytes: Long
    ): Result<String> = withContext(Dispatchers.Default) {
        if (cacheManager.isDownloaded(fileName)) {
            val localPath = cacheManager.getLocalFilePath(fileName)!!
            _downloadState.value = DownloadProgressState.Completed(trackId, fileName, localPath)
            return@withContext Result.success(localPath)
        }

        activeTrackId = trackId
        _downloadState.value = DownloadProgressState.Downloading(
            trackId = trackId,
            fileName = fileName,
            title = title,
            bytesDownloaded = 0L,
            totalBytes = expectedSizeBytes,
            progress = 0f
        )

        val nsUrl = NSURL.URLWithString(remoteUrl)
        if (nsUrl == null) {
            val err = "Invalid URL: $remoteUrl"
            _downloadState.value = DownloadProgressState.Error(trackId, fileName, err)
            return@withContext Result.failure(IllegalArgumentException(err))
        }

        val fileManager = NSFileManager.defaultManager
        val targetPath = "${cacheManager.getAudioDirectoryPath()}/$fileName"
        val targetUrl = NSURL.fileURLWithPath(targetPath)

        try {
            val request = NSMutableURLRequest.requestWithURL(nsUrl).apply {
                setValue("RobithohApp/1.0", forHTTPHeaderField = "User-Agent")
                setTimeoutInterval(45.0)
            }

            val completer = CompletableDeferred<Result<String>>()

            val sessionConfig = NSURLSessionConfiguration.defaultSessionConfiguration
            val session = NSURLSession.sessionWithConfiguration(sessionConfig)

            val task = session.downloadTaskWithRequest(request) { location, response, error ->
                if (error != null) {
                    val msg = error.localizedDescription ?: "Download error"
                    completer.complete(Result.failure(Exception(msg)))
                    return@downloadTaskWithRequest
                }

                val httpResponse = response as? NSHTTPURLResponse
                val statusCode = httpResponse?.statusCode?.toInt() ?: 0
                if (statusCode !in 200..299) {
                    completer.complete(Result.failure(Exception("Server returned HTTP $statusCode")))
                    return@downloadTaskWithRequest
                }

                if (location != null) {
                    try {
                        if (fileManager.fileExistsAtPath(targetPath)) {
                            fileManager.removeItemAtPath(targetPath, error = null)
                        }
                        fileManager.moveItemAtURL(location, targetUrl, error = null)
                        completer.complete(Result.success(targetPath))
                    } catch (e: Throwable) {
                        completer.complete(Result.failure(Exception(e.message ?: "Failed to save file")))
                    }
                } else {
                    completer.complete(Result.failure(Exception("Downloaded file location was null")))
                }
            }

            activeTask = task
            task.resume()

            val progressJob = launch {
                while (task.state == platform.Foundation.NSURLSessionTaskStateRunning && isActive) {
                    val countOfBytesReceived = task.countOfBytesReceived
                    val countOfBytesExpected = if (task.countOfBytesExpectedToReceive > 0) {
                        task.countOfBytesExpectedToReceive
                    } else expectedSizeBytes

                    val progress = if (countOfBytesExpected > 0) {
                        (countOfBytesReceived.toFloat() / countOfBytesExpected.toFloat()).coerceIn(0f, 1f)
                    } else 0f

                    _downloadState.value = DownloadProgressState.Downloading(
                        trackId = trackId,
                        fileName = fileName,
                        title = title,
                        bytesDownloaded = countOfBytesReceived,
                        totalBytes = countOfBytesExpected,
                        progress = progress
                    )
                    delay(200)
                }
            }

            val result = completer.await()
            progressJob.cancel()

            if (result.isSuccess) {
                _downloadState.value = DownloadProgressState.Completed(trackId, fileName, targetPath)
            } else {
                _downloadState.value = DownloadProgressState.Error(
                    trackId,
                    fileName,
                    result.exceptionOrNull()?.message ?: "Gagal mengunduh audio"
                )
            }

            result
        } catch (e: CancellationException) {
            activeTask?.cancel()
            _downloadState.value = DownloadProgressState.Idle
            Result.failure(e)
        } catch (e: Exception) {
            val err = e.message ?: "Gagal mengunduh audio"
            _downloadState.value = DownloadProgressState.Error(trackId, fileName, err)
            Result.failure(e)
        } finally {
            activeTask = null
            activeTrackId = null
        }
    }

    override fun cancelDownload(trackId: String?) {
        if (trackId == null || activeTrackId == trackId) {
            activeTask?.cancel()
            activeTask = null
            activeJob?.cancel()
            _downloadState.value = DownloadProgressState.Idle
            activeTrackId = null
        }
    }

    override fun resetState() {
        _downloadState.value = DownloadProgressState.Idle
        activeTrackId = null
    }
}

actual fun createAudioDownloader(cacheManager: AudioCacheManager): AudioDownloader =
    IosAudioDownloader(cacheManager)
