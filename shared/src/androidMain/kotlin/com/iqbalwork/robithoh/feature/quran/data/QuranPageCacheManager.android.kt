package com.iqbalwork.robithoh.feature.quran.data

import android.content.Context
import com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext
import java.io.File

class AndroidQuranPageCacheManager(
    private val contextProvider: () -> Context? = { getGlobalAppContext() }
) : QuranPageCacheManager {

    private fun getPagesDir(): File {
        val ctx = contextProvider()
        val baseDir = ctx?.filesDir ?: File(System.getProperty("java.io.tmpdir") ?: ".", "robithoh_quran")

        // Clean legacy Madinah cache if it exists to free storage and ensure Kemenag assets are loaded
        val legacyDir = File(baseDir, "quran_pages")
        if (legacyDir.exists()) {
            try {
                legacyDir.deleteRecursively()
            } catch (_: Exception) {}
        }

        val dir = File(baseDir, "quran_pages_kemenag")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun formatFileName(pageNumber: Int): String {
        return "${pageNumber.toString().padStart(3, '0')}.png"
    }

    override fun isPageDownloaded(pageNumber: Int): Boolean {
        val file = File(getPagesDir(), formatFileName(pageNumber))
        return file.exists() && file.length() > 0
    }

    override fun getPageFilePath(pageNumber: Int): String? {
        val file = File(getPagesDir(), formatFileName(pageNumber))
        return if (file.exists() && file.length() > 0) file.absolutePath else null
    }

    override fun getPageBytes(pageNumber: Int): ByteArray? {
        val file = File(getPagesDir(), formatFileName(pageNumber))
        return if (file.exists() && file.length() > 0) {
            try {
                file.readBytes()
            } catch (_: Exception) {
                null
            }
        } else null
    }

    override fun savePageBytes(pageNumber: Int, bytes: ByteArray): String {
        val file = File(getPagesDir(), formatFileName(pageNumber))
        file.writeBytes(bytes)
        return file.absolutePath
    }

    override fun getDownloadedPageCount(): Int {
        val dir = getPagesDir()
        if (!dir.exists() || !dir.isDirectory) return 0
        return (1..QuranPageLookup.TOTAL_PAGES).count { isPageDownloaded(it) }
    }

    override fun getTotalCacheSize(): Long {
        val dir = getPagesDir()
        if (!dir.exists() || !dir.isDirectory) return 0L
        return dir.listFiles()?.sumOf { it.length() } ?: 0L
    }

    override fun clearAllCache() {
        val dir = getPagesDir()
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.forEach { it.delete() }
        }
    }

    override fun getPagesDirectoryPath(): String {
        return getPagesDir().absolutePath
    }
}

actual fun createQuranPageCacheManager(): QuranPageCacheManager {
    return AndroidQuranPageCacheManager()
}
