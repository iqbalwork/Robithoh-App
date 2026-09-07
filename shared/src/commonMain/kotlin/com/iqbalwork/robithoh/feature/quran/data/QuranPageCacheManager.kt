package com.iqbalwork.robithoh.feature.quran.data

interface QuranPageCacheManager {
    fun isPageDownloaded(pageNumber: Int): Boolean
    fun getPageFilePath(pageNumber: Int): String?
    fun getPageBytes(pageNumber: Int): ByteArray?
    fun savePageBytes(pageNumber: Int, bytes: ByteArray): String
    fun getDownloadedPageCount(): Int
    fun getTotalCacheSize(): Long
    fun clearAllCache()
    fun getPagesDirectoryPath(): String
}

expect fun createQuranPageCacheManager(): QuranPageCacheManager
