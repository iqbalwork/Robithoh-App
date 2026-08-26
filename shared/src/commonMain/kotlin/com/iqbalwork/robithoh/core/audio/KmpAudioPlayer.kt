package com.iqbalwork.robithoh.core.audio

import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import kotlinx.coroutines.flow.StateFlow

/**
 * Multiplatform audio player interface with reactive playback state, position, and track flows.
 */
interface KmpAudioPlayer {
    val currentTrack: StateFlow<AudioTrack?>
    val playbackState: StateFlow<AudioPlaybackState>
    val currentPositionMs: StateFlow<Long>
    val durationMs: StateFlow<Long>

    fun play(track: AudioTrack)
    fun pause()
    fun resume()
    fun seekTo(positionMs: Long)
    fun stop()
    fun release()
}

expect fun createAudioPlayer(): KmpAudioPlayer
