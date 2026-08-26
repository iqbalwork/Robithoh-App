package com.iqbalwork.robithoh

import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class AudioPlaybackStateTest {

    private val json = Json { prettyPrint = false }

    @Test
    fun testAudioTrackModel() {
        val track = AudioTrack(
            id = "manqobah_01",
            title = "Manqobah Pertama",
            subtitle = "Kelahiran Syekh Abdul Qodir Al-Jailani r.a.",
            urlOrPath = "audio/manqobah_01.mp3",
            durationMs = 245_000L
        )

        assertEquals("manqobah_01", track.id)
        assertEquals("Manqobah Pertama", track.title)
        assertEquals(245_000L, track.durationMs)

        val serialized = json.encodeToString(track)
        val deserialized = json.decodeFromString<AudioTrack>(serialized)
        assertEquals(track, deserialized)
    }

    @Test
    fun testAudioPlaybackStateEnum() {
        val state = AudioPlaybackState.PLAYING
        val serialized = json.encodeToString(state)
        val deserialized = json.decodeFromString<AudioPlaybackState>(serialized)
        assertEquals(AudioPlaybackState.PLAYING, deserialized)
    }
}
