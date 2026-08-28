@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.iqbalwork.robithoh.core.audio

import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.AVFAudio.*
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
import platform.Foundation.*

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

    init {
        try {
            val session = AVAudioSession.sharedInstance()
            session.setCategory(AVAudioSessionCategoryPlayback, error = null)
            session.setActive(true, error = null)
        } catch (_: Throwable) {}
    }

    override fun play(track: AudioTrack) {
        stop()
        _currentTrack.value = track
        _playbackState.value = AudioPlaybackState.BUFFERING

        scope.launch(Dispatchers.Default) {
            try {
                val urlOrPath = track.urlOrPath
                val targetUrl: NSURL? = if (urlOrPath.startsWith("http://") || urlOrPath.startsWith("https://")) {
                    NSURL.URLWithString(urlOrPath)
                } else if (urlOrPath.startsWith("file://")) {
                    NSURL.URLWithString(urlOrPath)
                } else {
                    val fileManager = NSFileManager.defaultManager
                    if (fileManager.fileExistsAtPath(urlOrPath)) {
                        NSURL.fileURLWithPath(urlOrPath)
                    } else {
                        // Extract from Compose Multiplatform resources
                        val bytes = org.jetbrains.compose.resources.ExperimentalResourceApi::class.let {
                            robithohapp.shared.generated.resources.Res.readBytes("files/$urlOrPath")
                        }
                        val tempDir = NSTemporaryDirectory()
                        val sanitized = urlOrPath.replace("/", "_").replace("\\", "_")
                        val tempFilePath = "$tempDir/audio_kmp_$sanitized"

                        val nsData = bytes.toNSData()
                        fileManager.createFileAtPath(tempFilePath, contents = nsData, attributes = null)
                        NSURL.fileURLWithPath(tempFilePath)
                    }
                }

                withContext(Dispatchers.Main) {
                    if (targetUrl != null) {
                        val item = AVPlayerItem(uRL = targetUrl)
                        val player = AVPlayer(playerItem = item)
                        avPlayer = player
                        player.play()
                        _playbackState.value = AudioPlaybackState.PLAYING
                        startProgressTracker()
                    } else {
                        _playbackState.value = AudioPlaybackState.ERROR
                    }
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    _playbackState.value = AudioPlaybackState.ERROR
                }
            }
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

    private fun ByteArray.toNSData(): NSData = this.usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = this.size.toULong()
        )
    }
}

actual fun createAudioPlayer(): KmpAudioPlayer = IosAudioPlayer()
