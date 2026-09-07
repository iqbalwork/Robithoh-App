@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.iqbalwork.robithoh.feature.quran.data

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
import platform.Foundation.dataWithContentsOfFile
import platform.posix.memcpy

class IosQuranPageCacheManager : QuranPageCacheManager {

    private val fileManager = NSFileManager.defaultManager

    private fun getPagesDir(): String {
        val appSupportDir = NSSearchPathForDirectoriesInDomains(
            NSApplicationSupportDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() as? String ?: NSTemporaryDirectory()

        val pagesDir = "$appSupportDir/QuranPages"
        if (!fileManager.fileExistsAtPath(pagesDir)) {
            fileManager.createDirectoryAtPath(
                path = pagesDir,
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
        }
        return pagesDir
    }

    private fun formatFileName(pageNumber: Int): String {
        return "${pageNumber.toString().padStart(3, '0')}.png"
    }

    override fun isPageDownloaded(pageNumber: Int): Boolean {
        val path = "${getPagesDir()}/${formatFileName(pageNumber)}"
        if (!fileManager.fileExistsAtPath(path)) return false
        val attrs = fileManager.attributesOfItemAtPath(path, error = null)
        val size = (attrs?.get(NSFileSize) as? NSNumber)?.longValue ?: 0L
        return size > 0
    }

    override fun getPageFilePath(pageNumber: Int): String? {
        val path = "${getPagesDir()}/${formatFileName(pageNumber)}"
        return if (isPageDownloaded(pageNumber)) path else null
    }

    override fun getPageBytes(pageNumber: Int): ByteArray? {
        val path = "${getPagesDir()}/${formatFileName(pageNumber)}"
        if (!fileManager.fileExistsAtPath(path)) return null
        val data = NSData.dataWithContentsOfFile(path) ?: return null
        return data.toByteArray()
    }

    override fun savePageBytes(pageNumber: Int, bytes: ByteArray): String {
        val path = "${getPagesDir()}/${formatFileName(pageNumber)}"
        val data = bytes.toNSData()
        fileManager.createFileAtPath(path, contents = data, attributes = null)
        return path
    }

    override fun getDownloadedPageCount(): Int {
        return (1..QuranPageLookup.TOTAL_PAGES).count { isPageDownloaded(it) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getTotalCacheSize(): Long {
        val dir = getPagesDir()
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
        val dir = getPagesDir()
        val files = fileManager.contentsOfDirectoryAtPath(dir, error = null) as? List<String> ?: return
        for (f in files) {
            fileManager.removeItemAtPath("$dir/$f", error = null)
        }
    }

    override fun getPagesDirectoryPath(): String = getPagesDir()

    private fun ByteArray.toNSData(): NSData {
        if (this.isEmpty()) return NSData()
        return this.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(0),
                length = this.size.toULong()
            )
        }
    }

    private fun NSData.toByteArray(): ByteArray {
        val size = length.toInt()
        val byteArray = ByteArray(size)
        if (size > 0) {
            byteArray.usePinned { pinned ->
                memcpy(pinned.addressOf(0), bytes, length)
            }
        }
        return byteArray
    }
}

actual fun createQuranPageCacheManager(): QuranPageCacheManager = IosQuranPageCacheManager()
