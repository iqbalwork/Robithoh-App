package com.iqbalwork.robithoh.core.audio

import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JvmAudioPlayer : KmpAudioPlayer {
    private val _currentTrack = MutableStateFlow<AudioTrack?>(null)
    override val currentTrack: StateFlow<AudioTrack?> = _currentTrack.asStateFlow()

    private val _playbackState = MutableStateFlow(AudioPlaybackState.IDLE)
    override val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    override val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    override val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    override fun play(track: AudioTrack) {
        _currentTrack.value = track
        _playbackState.value = AudioPlaybackState.PLAYING
        _durationMs.value = track.durationMs
    }

    override fun pause() {
        if (_playbackState.value == AudioPlaybackState.PLAYING) {
            _playbackState.value = AudioPlaybackState.PAUSED
        }
    }

    override fun resume() {
        if (_playbackState.value == AudioPlaybackState.PAUSED) {
            _playbackState.value = AudioPlaybackState.PLAYING
        }
    }

    override fun seekTo(positionMs: Long) {
        _currentPositionMs.value = positionMs.coerceIn(0L, _durationMs.value)
    }

    override fun stop() {
        _playbackState.value = AudioPlaybackState.IDLE
        _currentPositionMs.value = 0L
    }

    override fun release() {
        stop()
        _currentTrack.value = null
    }
}

actual fun createAudioPlayer(): KmpAudioPlayer = JvmAudioPlayer()
