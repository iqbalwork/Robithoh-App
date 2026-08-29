package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.core.audio.createAudioCacheManager
import com.iqbalwork.robithoh.feature.langgam.data.LanggamRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AudioOnDemandTest {

    @Test
    fun testLanggamCatalogHas11OnDemandTracks() {
        val tracks = LanggamRepository.langgamList
        assertEquals(11, tracks.size)

        for (track in tracks) {
            assertTrue(track.remoteUrl.startsWith(LanggamRepository.GITHUB_AUDIO_BASE_URL))
            assertTrue(track.sizeBytes > 0)
            assertTrue(track.sizeLabel.isNotEmpty())
            assertTrue(track.fileName.endsWith(".mp3") || track.fileName.endsWith(".mpeg"))
        }
    }

    @Test
    fun testLanggamFindByIdAndFileName() {
        val baniHasyim = LanggamRepository.findById("langgam_bani_hasyim")
        assertNotNull(baniHasyim)
        assertEquals("Bani Hasyim", baniHasyim.title)
        assertEquals("bani_hasyim.mp3", baniHasyim.fileName)
        assertEquals("19.0 MB", baniHasyim.sizeLabel)

        val tarowih = LanggamRepository.findByFileName("tarowih.mp3")
        assertNotNull(tarowih)
        assertEquals("Tarowih", tarowih.title)
    }

    @Test
    fun testAudioCacheManagerSaveAndDelete() {
        val cacheManager = createAudioCacheManager()
        val testFileName = "test_audio_track.mp3"
        val sampleData = "SAMPLE_AUDIO_BYTES".encodeToByteArray()

        // Clean before
        cacheManager.delete(testFileName)
        assertFalse(cacheManager.isDownloaded(testFileName))

        // Save
        val savedPath = cacheManager.saveBytes(testFileName, sampleData)
        assertNotNull(savedPath)
        assertTrue(cacheManager.isDownloaded(testFileName))
        assertEquals(sampleData.size.toLong(), cacheManager.getDownloadedBytes(testFileName))

        val localUri = cacheManager.getLocalFileUri(testFileName)
        assertNotNull(localUri)

        // Delete
        val deleted = cacheManager.delete(testFileName)
        assertTrue(deleted)
        assertFalse(cacheManager.isDownloaded(testFileName))
    }
}
