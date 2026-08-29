@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.iqbalwork.robithoh.core.audio

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create

class IosAudioCacheManager : AudioCacheManager {

    private val fileManager = NSFileManager.defaultManager

    private fun getAudioDir(): String {
        val appSupportDir = NSSearchPathForDirectoriesInDomains(
            NSApplicationSupportDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() as? String ?: NSTemporaryDirectory()

        val audioDir = "$appSupportDir/Audio"
        if (!fileManager.fileExistsAtPath(audioDir)) {
            fileManager.createDirectoryAtPath(
                path = audioDir,
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
        }
        return audioDir
    }

    override fun isDownloaded(fileName: String): Boolean {
        val path = "${getAudioDir()}/$fileName"
        if (!fileManager.fileExistsAtPath(path)) return false
        val attrs = fileManager.attributesOfItemAtPath(path, error = null)
        val size = (attrs?.get(NSFileSize) as? NSNumber)?.longValue ?: 0L
        return size > 0
    }

    override fun getLocalFilePath(fileName: String): String? {
        val path = "${getAudioDir()}/$fileName"
        return if (isDownloaded(fileName)) path else null
    }

    override fun getLocalFileUri(fileName: String): String? {
        val path = getLocalFilePath(fileName) ?: return null
        return if (path.startsWith("/")) "file://$path" else path
    }

    override fun getAudioDirectoryPath(): String = getAudioDir()

    override fun saveBytes(fileName: String, bytes: ByteArray): String {
        val path = "${getAudioDir()}/$fileName"
        val data = bytes.toNSData()
        fileManager.createFileAtPath(path, contents = data, attributes = null)
        return path
    }

    override fun delete(fileName: String): Boolean {
        val path = "${getAudioDir()}/$fileName"
        return if (fileManager.fileExistsAtPath(path)) {
            fileManager.removeItemAtPath(path, error = null)
        } else false
    }

    override fun getDownloadedBytes(fileName: String): Long {
        val path = "${getAudioDir()}/$fileName"
        if (!fileManager.fileExistsAtPath(path)) return 0L
        val attrs = fileManager.attributesOfItemAtPath(path, error = null)
        return (attrs?.get(NSFileSize) as? NSNumber)?.longValue ?: 0L
    }

    @Suppress("UNCHECKED_CAST")
    override fun getTotalCacheSize(): Long {
        val dir = getAudioDir()
        val files = fileManager.contentsOfDirectoryAtPath(dir, error = null) as? List<String> ?: return 0L
        var total = 0L
        for (f in files) {
            val path = "$dir/$f"
            val attrs = fileManager.attributesOfItemAtPath(path, error = null)
            val size = (attrs?.get(NSFileSize) as? NSNumber)?.longValue ?: 0L
            total += size
        }
        return total
    }

    @Suppress("UNCHECKED_CAST")
    override fun clearAllCache() {
        val dir = getAudioDir()
        val files = fileManager.contentsOfDirectoryAtPath(dir, error = null) as? List<String> ?: return
        for (f in files) {
            fileManager.removeItemAtPath("$dir/$f", error = null)
        }
    }

    private fun ByteArray.toNSData(): NSData {
        if (this.isEmpty()) return NSData()
        return this.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(0),
                length = this.size.toULong()
            )
        }
    }
}

actual fun createAudioCacheManager(): AudioCacheManager = IosAudioCacheManager()
