package com.iqbalwork.robithoh.feature.quran.data

import java.io.File

class JvmQuranPageCacheManager : QuranPageCacheManager {

    private fun getPagesDir(): File {
        val userHome = System.getProperty("user.home") ?: "."
        val baseDir = File(userHome, ".robithoh")
        val dir = File(baseDir, "quran_pages")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun getDevPagesDir(): File? {
        val devDir = File("/home/iqbalf/Projects/Robithoh/quran-page")
        return if (devDir.exists() && devDir.isDirectory) devDir else null
    }

    private fun formatFileName(pageNumber: Int): String {
        return "${pageNumber.toString().padStart(3, '0')}.png"
    }

    override fun isPageDownloaded(pageNumber: Int): Boolean {
        val file = File(getPagesDir(), formatFileName(pageNumber))
        if (file.exists() && file.length() > 0) return true
        val devFile = getDevPagesDir()?.let { File(it, formatFileName(pageNumber)) }
        return devFile != null && devFile.exists() && devFile.length() > 0
    }

    override fun getPageFilePath(pageNumber: Int): String? {
        val file = File(getPagesDir(), formatFileName(pageNumber))
        if (file.exists() && file.length() > 0) return file.absolutePath
        val devFile = getDevPagesDir()?.let { File(it, formatFileName(pageNumber)) }
        return if (devFile != null && devFile.exists() && devFile.length() > 0) devFile.absolutePath else null
    }

    override fun getPageBytes(pageNumber: Int): ByteArray? {
        val path = getPageFilePath(pageNumber) ?: return null
        return try {
            File(path).readBytes()
        } catch (_: Exception) {
            null
        }
    }

    override fun savePageBytes(pageNumber: Int, bytes: ByteArray): String {
        val file = File(getPagesDir(), formatFileName(pageNumber))
        file.writeBytes(bytes)
        return file.absolutePath
    }

    override fun getDownloadedPageCount(): Int {
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
    return JvmQuranPageCacheManager()
}
