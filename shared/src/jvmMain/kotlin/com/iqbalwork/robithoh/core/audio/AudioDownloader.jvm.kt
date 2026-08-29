package com.iqbalwork.robithoh.core.audio

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class JvmAudioDownloader(
    private val cacheManager: AudioCacheManager
) : AudioDownloader {

    private val _downloadState = MutableStateFlow<DownloadProgressState>(DownloadProgressState.Idle)
    override val downloadState: StateFlow<DownloadProgressState> = _downloadState.asStateFlow()

    private var activeJob: Job? = null
    private var activeTrackId: String? = null

    override suspend fun downloadAudio(
        trackId: String,
        title: String,
        remoteUrl: String,
        fileName: String,
        expectedSizeBytes: Long
    ): Result<String> = withContext(Dispatchers.IO) {
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

        val audioDir = File(cacheManager.getAudioDirectoryPath())
        if (!audioDir.exists()) {
            audioDir.mkdirs()
        }
        val tempFile = File(audioDir, "$fileName.tmp")
        val finalFile = File(audioDir, fileName)

        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        try {
            var currentUrl = remoteUrl
            var redirectCount = 0
            val maxRedirects = 6

            while (redirectCount < maxRedirects) {
                val urlObj = URL(currentUrl)
                val conn = urlObj.openConnection() as HttpURLConnection
                conn.instanceFollowRedirects = true
                conn.connectTimeout = 15000
                conn.readTimeout = 30000
                conn.setRequestProperty("User-Agent", "RobithohApp/1.0")

                val status = conn.responseCode
                if (status == HttpURLConnection.HTTP_MOVED_TEMP ||
                    status == HttpURLConnection.HTTP_MOVED_PERM ||
                    status == HttpURLConnection.HTTP_SEE_OTHER ||
                    status == 307 || status == 308
                ) {
                    val newUrl = conn.getHeaderField("Location")
                    conn.disconnect()
                    if (newUrl.isNullOrBlank()) {
                        throw IllegalStateException("Redirect location was empty")
                    }
                    currentUrl = newUrl
                    redirectCount++
                } else if (status == HttpURLConnection.HTTP_OK) {
                    connection = conn
                    break
                } else {
                    conn.disconnect()
                    throw IllegalStateException("Server returned HTTP $status")
                }
            }

            val conn = connection ?: throw IllegalStateException("Failed to connect after redirects")
            val contentLength = conn.contentLengthLong.let { if (it > 0) it else expectedSizeBytes }

            inputStream = conn.inputStream
            outputStream = FileOutputStream(tempFile)

            val buffer = ByteArray(8 * 1024)
            var bytesRead: Int
            var totalBytesDownloaded = 0L
            var lastReportTime = 0L

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                if (!coroutineContext.isActive) {
                    throw CancellationException("Download cancelled")
                }
                outputStream.write(buffer, 0, bytesRead)
                totalBytesDownloaded += bytesRead

                val now = System.currentTimeMillis()
                if (now - lastReportTime > 150 || totalBytesDownloaded == contentLength) {
                    lastReportTime = now
                    val progress = if (contentLength > 0) {
                        (totalBytesDownloaded.toFloat() / contentLength.toFloat()).coerceIn(0f, 1f)
                    } else 0f

                    _downloadState.value = DownloadProgressState.Downloading(
                        trackId = trackId,
                        fileName = fileName,
                        title = title,
                        bytesDownloaded = totalBytesDownloaded,
                        totalBytes = contentLength,
                        progress = progress
                    )
                }
            }

            outputStream.flush()
            outputStream.close()
            outputStream = null
            inputStream.close()
            inputStream = null
            conn.disconnect()

            if (tempFile.exists()) {
                if (finalFile.exists()) finalFile.delete()
                val renamed = tempFile.renameTo(finalFile)
                if (!renamed) {
                    tempFile.copyTo(finalFile, overwrite = true)
                    tempFile.delete()
                }
            }

            val resultPath = finalFile.absolutePath
            _downloadState.value = DownloadProgressState.Completed(trackId, fileName, resultPath)
            Result.success(resultPath)
        } catch (e: CancellationException) {
            if (tempFile.exists()) tempFile.delete()
            _downloadState.value = DownloadProgressState.Idle
            Result.failure(e)
        } catch (e: Exception) {
            if (tempFile.exists()) tempFile.delete()
            val errorMsg = e.message ?: "Gagal mengunduh audio"
            _downloadState.value = DownloadProgressState.Error(trackId, fileName, errorMsg)
            Result.failure(e)
        } finally {
            try { outputStream?.close() } catch (_: Exception) {}
            try { inputStream?.close() } catch (_: Exception) {}
            try { connection?.disconnect() } catch (_: Exception) {}
        }
    }

    override fun cancelDownload(trackId: String?) {
        if (trackId == null || activeTrackId == trackId) {
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
    JvmAudioDownloader(cacheManager)
