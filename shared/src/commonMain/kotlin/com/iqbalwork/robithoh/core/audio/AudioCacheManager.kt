package com.iqbalwork.robithoh.core.audio

/**
 * Interface for managing locally cached/downloaded audio assets across platforms.
 */
interface AudioCacheManager {
    /**
     * Returns true if the file exists locally in the cache directory and is not empty.
     */
    fun isDownloaded(fileName: String): Boolean

    /**
     * Returns the absolute file path if downloaded, or null.
     */
    fun getLocalFilePath(fileName: String): String?

    /**
     * Returns a valid URI (file:// or path) for media player consumption if downloaded, or null.
     */
    fun getLocalFileUri(fileName: String): String?

    /**
     * Returns the base directory path where audio files are cached.
     */
    fun getAudioDirectoryPath(): String

    /**
     * Saves raw bytes to a cached file with [fileName] and returns the absolute path.
     */
    fun saveBytes(fileName: String, bytes: ByteArray): String

    /**
     * Deletes a cached audio file by [fileName]. Returns true if deleted.
     */
    fun delete(fileName: String): Boolean

    /**
     * Returns the size of the cached file in bytes, or 0 if not present.
     */
    fun getDownloadedBytes(fileName: String): Long

    /**
     * Returns the total sum of bytes of all cached audio files.
     */
    fun getTotalCacheSize(): Long

    /**
     * Deletes all cached audio files in the audio cache directory.
     */
    fun clearAllCache()
}

expect fun createAudioCacheManager(): AudioCacheManager
