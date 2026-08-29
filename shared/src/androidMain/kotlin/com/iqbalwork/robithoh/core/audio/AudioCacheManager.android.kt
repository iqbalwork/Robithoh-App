package com.iqbalwork.robithoh.core.audio

import android.content.Context
import com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext
import java.io.File

class AndroidAudioCacheManager(
    private val contextProvider: () -> Context? = { getGlobalAppContext() }
) : AudioCacheManager {

    private fun getAudioDir(): File {
        val ctx = contextProvider()
        val baseDir = ctx?.filesDir ?: File(System.getProperty("java.io.tmpdir") ?: ".", "robithoh_audio")
        val audioDir = File(baseDir, "audio")
        if (!audioDir.exists()) {
            audioDir.mkdirs()
        }
        return audioDir
    }

    override fun isDownloaded(fileName: String): Boolean {
        val file = File(getAudioDir(), fileName)
        return file.exists() && file.length() > 0
    }

    override fun getLocalFilePath(fileName: String): String? {
        val file = File(getAudioDir(), fileName)
        return if (file.exists() && file.length() > 0) file.absolutePath else null
    }

    override fun getLocalFileUri(fileName: String): String? {
        val path = getLocalFilePath(fileName) ?: return null
        return if (path.startsWith("/")) "file://$path" else path
    }

    override fun getAudioDirectoryPath(): String {
        return getAudioDir().absolutePath
    }

    override fun saveBytes(fileName: String, bytes: ByteArray): String {
        val file = File(getAudioDir(), fileName)
        file.writeBytes(bytes)
        return file.absolutePath
    }

    override fun delete(fileName: String): Boolean {
        val file = File(getAudioDir(), fileName)
        return if (file.exists()) file.delete() else false
    }

    override fun getDownloadedBytes(fileName: String): Long {
        val file = File(getAudioDir(), fileName)
        return if (file.exists()) file.length() else 0L
    }

    override fun getTotalCacheSize(): Long {
        val dir = getAudioDir()
        if (!dir.exists() || !dir.isDirectory) return 0L
        return dir.listFiles()?.sumOf { it.length() } ?: 0L
    }

    override fun clearAllCache() {
        val dir = getAudioDir()
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.forEach { it.delete() }
        }
    }
}

actual fun createAudioCacheManager(): AudioCacheManager = AndroidAudioCacheManager()
