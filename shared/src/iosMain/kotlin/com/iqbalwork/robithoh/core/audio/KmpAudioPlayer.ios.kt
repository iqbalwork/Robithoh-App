@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.iqbalwork.robithoh.core.audio

import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.currentItem
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.seekToTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSURL

class IosAudioPlayer : KmpAudioPlayer {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var avPlayer: AVPlayer? = null
    private var progressJob: Job? = null

    private val _currentTrack = MutableStateFlow<AudioTrack?>(null)
    override val currentTrack: StateFlow<AudioTrack?> = _currentTrack.asStateFlow()

    private val _playbackState = MutableStateFlow(AudioPlaybackState.IDLE)
    override val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    override val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    override val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    override fun play(track: AudioTrack) {
        stop()
        _currentTrack.value = track
        _playbackState.value = AudioPlaybackState.BUFFERING

        try {
            val url = NSURL.URLWithString(track.urlOrPath)
            if (url != null) {
                val item = AVPlayerItem(uRL = url)
                val player = AVPlayer(playerItem = item)
                avPlayer = player
                player.play()
                _playbackState.value = AudioPlaybackState.PLAYING
                startProgressTracker()
            } else {
                _playbackState.value = AudioPlaybackState.ERROR
            }
        } catch (e: Exception) {
            _playbackState.value = AudioPlaybackState.ERROR
        }
    }

    override fun pause() {
        avPlayer?.pause()
        _playbackState.value = AudioPlaybackState.PAUSED
        stopProgressTracker()
    }

    override fun resume() {
        avPlayer?.let { player ->
            player.play()
            _playbackState.value = AudioPlaybackState.PLAYING
            startProgressTracker()
        }
    }

    override fun seekTo(positionMs: Long) {
        avPlayer?.let { player ->
            val seconds = positionMs / 1000.0
            val time = CMTimeMakeWithSeconds(seconds, 1000)
            player.seekToTime(time)
            _currentPositionMs.value = positionMs
        }
    }

    override fun stop() {
        stopProgressTracker()
        avPlayer?.pause()
        avPlayer = null
        _playbackState.value = AudioPlaybackState.IDLE
        _currentPositionMs.value = 0L
    }

    override fun release() {
        stop()
        _currentTrack.value = null
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = scope.launch {
            while (isActive) {
                avPlayer?.let { player ->
                    val currentSec = CMTimeGetSeconds(player.currentTime())
                    if (!currentSec.isNaN()) {
                        _currentPositionMs.value = (currentSec * 1000).toLong()
                    }
                    val item = player.currentItem()
                    if (item != null) {
                        val durationSec = CMTimeGetSeconds(item.duration())
                        if (!durationSec.isNaN()) {
                            _durationMs.value = (durationSec * 1000).toLong()
                        }
                    }
                }
                delay(250)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }
}

actual fun createAudioPlayer(): KmpAudioPlayer = IosAudioPlayer()
