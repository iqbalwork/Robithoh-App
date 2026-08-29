@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.iqbalwork.robithoh.core.audio

import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryOptionMixWithOthers
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionModeDefault
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.seekToTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.darwin.NSObjectProtocol
import robithohapp.shared.generated.resources.Res

class IosAudioPlayer(
    private val cacheManager: AudioCacheManager = createAudioCacheManager()
) : KmpAudioPlayer {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var avPlayer: AVPlayer? = null
    private var progressJob: Job? = null
    private var endObserver: NSObjectProtocol? = null

    companion object {
        private var globalActivePlayer: AVPlayer? = null
        private var globalEndObserver: NSObjectProtocol? = null
        private var globalActiveInstance: IosAudioPlayer? = null

        fun stopGlobalPlayback() {
            globalEndObserver?.let {
                NSNotificationCenter.defaultCenter.removeObserver(it)
                globalEndObserver = null
            }
            globalActivePlayer?.pause()
            globalActivePlayer = null
            globalActiveInstance?.stopProgressTracker()
            globalActiveInstance?._currentTrack?.value = null
            globalActiveInstance?._playbackState?.value = AudioPlaybackState.IDLE
            globalActiveInstance?._currentPositionMs?.value = 0L
            globalActiveInstance?._durationMs?.value = 0L
            globalActiveInstance = null

            try {
                AVAudioSession.sharedInstance().setActive(
                    active = false,
                    withOptions = platform.AVFAudio.AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation,
                    error = null
                )
            } catch (_: Throwable) {}
        }
    }

    private val _currentTrack = MutableStateFlow<AudioTrack?>(null)
    override val currentTrack: StateFlow<AudioTrack?> = _currentTrack.asStateFlow()

    private val _playbackState = MutableStateFlow(AudioPlaybackState.IDLE)
    override val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    override val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    override val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    init {
        configureAudioSession()
    }

    private fun configureAudioSession() {
        try {
            val session = AVAudioSession.sharedInstance()
            session.setCategory(
                category = AVAudioSessionCategoryPlayback,
                mode = AVAudioSessionModeDefault,
                options = 0u,
                error = null
            )
            session.setActive(true, error = null)
        } catch (_: Throwable) {}
    }

    override fun play(track: AudioTrack) {
        stop()
        _currentTrack.value = track
        _playbackState.value = AudioPlaybackState.BUFFERING

        scope.launch(Dispatchers.Default) {
            val fileUrl = resolveUrlOrPath(track.urlOrPath)
            withContext(Dispatchers.Main) {
                if (fileUrl == null) {
                    _playbackState.value = AudioPlaybackState.ERROR
                    return@withContext
                }

                try {
                    val session = AVAudioSession.sharedInstance()
                    session.setCategory(
                        category = AVAudioSessionCategoryPlayback,
                        mode = AVAudioSessionModeDefault,
                        options = AVAudioSessionCategoryOptionMixWithOthers,
                        error = null
                    )
                    session.setActive(true, error = null)
                } catch (_: Throwable) {}

                val playerItem = AVPlayerItem(uRL = fileUrl)
                val player = AVPlayer(playerItem = playerItem)

                globalActivePlayer?.pause()
                globalActivePlayer = player
                globalActiveInstance = this@IosAudioPlayer

                endObserver = NSNotificationCenter.defaultCenter.addObserverForName(
                    name = AVPlayerItemDidPlayToEndTimeNotification,
                    `object` = playerItem,
                    queue = NSOperationQueue.mainQueue
                ) { _ ->
                    _playbackState.value = AudioPlaybackState.COMPLETED
                    _currentPositionMs.value = _durationMs.value
                    stopProgressTracker()
                }
                globalEndObserver = endObserver

                avPlayer = player

                val duration = playerItem.duration()
                val durationSeconds = CMTimeGetSeconds(duration)
                if (!durationSeconds.isNaN() && durationSeconds > 0) {
                    _durationMs.value = (durationSeconds * 1000).toLong()
                } else if (track.durationMs > 0) {
                    _durationMs.value = track.durationMs
                }

                player.play()
                _playbackState.value = AudioPlaybackState.PLAYING
                startProgressTracker()
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
        endObserver?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
            endObserver = null
        }
        val p = avPlayer
        avPlayer?.pause()
        avPlayer = null
        if (globalActivePlayer == p) {
            globalActivePlayer = null
            globalEndObserver = null
            globalActiveInstance = null
        }
        _currentTrack.value = null
        _playbackState.value = AudioPlaybackState.IDLE
        _currentPositionMs.value = 0L
        _durationMs.value = 0L
    }

    override fun release() {
        stop()
    }

    private suspend fun resolveUrlOrPath(urlOrPath: String): NSURL? {
        if (urlOrPath.startsWith("http://") || urlOrPath.startsWith("https://")) {
            return NSURL.URLWithString(urlOrPath)
        }
        if (urlOrPath.startsWith("file://")) {
            return NSURL.URLWithString(urlOrPath)
        }

        val fileManager = NSFileManager.defaultManager
        if (fileManager.fileExistsAtPath(urlOrPath)) {
            return NSURL.fileURLWithPath(urlOrPath)
        }

        val cachedPath = cacheManager.getLocalFilePath(urlOrPath)
        if (cachedPath != null && fileManager.fileExistsAtPath(cachedPath)) {
            return NSURL.fileURLWithPath(cachedPath)
        }

        // Cache directory for extracted audio resources (e.g. embedded adzan)
        val cachesDir = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() as? String ?: NSTemporaryDirectory()

        val sanitized = urlOrPath.replace("/", "_").replace("\\", "_")
        val extension = if (sanitized.contains(".")) "" else ".mp3"
        val cachedFilePath = "$cachesDir/audio_kmp_$sanitized$extension"

        if (fileManager.fileExistsAtPath(cachedFilePath)) {
            return NSURL.fileURLWithPath(cachedFilePath)
        }

        return try {
            val resourcePath = if (urlOrPath.startsWith("files/")) urlOrPath else "files/$urlOrPath"
            val bytes = ExperimentalResourceApi::class.let {
                Res.readBytes(resourcePath)
            }
            if (bytes.isNotEmpty()) {
                val nsData = bytes.toNSData()
                fileManager.createFileAtPath(cachedFilePath, contents = nsData, attributes = null)
                NSURL.fileURLWithPath(cachedFilePath)
            } else {
                null
            }
        } catch (_: Throwable) {
            null
        }
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = scope.launch {
            while (isActive) {
                avPlayer?.let { player ->
                    val currentSec = CMTimeGetSeconds(player.currentTime())
                    if (!currentSec.isNaN() && currentSec >= 0) {
                        _currentPositionMs.value = (currentSec * 1000).toLong()
                    }
                    val item = player.currentItem()
                    if (item != null) {
                        val durationSec = CMTimeGetSeconds(item.duration())
                        if (!durationSec.isNaN() && durationSec > 0) {
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

    private fun ByteArray.toNSData(): NSData {
        if (this.isEmpty()) return NSData()
        return this.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(0),
                length = this.size.toULong()
            )
        }
    }
}

actual fun createAudioPlayer(): KmpAudioPlayer = IosAudioPlayer()
