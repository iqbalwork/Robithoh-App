package com.iqbalwork.robithoh.core.audio

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
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

class AndroidAudioPlayer : KmpAudioPlayer {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var mediaPlayer: MediaPlayer? = null
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

        scope.launch(Dispatchers.IO) {
            try {
                val resolvedPath = if (track.urlOrPath.startsWith("http://") || track.urlOrPath.startsWith("https://") || java.io.File(track.urlOrPath).exists()) {
                    track.urlOrPath
                } else {
                    val bytes = org.jetbrains.compose.resources.ExperimentalResourceApi::class.let {
                        robithohapp.shared.generated.resources.Res.readBytes("files/${track.urlOrPath}")
                    }
                    val temp = java.io.File.createTempFile("audio_kmp_", ".mp3").apply {
                        deleteOnExit()
                        writeBytes(bytes)
                    }
                    temp.absolutePath
                }

                withContext(Dispatchers.Main) {
                    val player = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        setDataSource(resolvedPath)
                        setOnPreparedListener { mp ->
                            _playbackState.value = AudioPlaybackState.PLAYING
                            _durationMs.value = mp.duration.toLong()
                            mp.start()
                            startProgressTracker()
                        }
                        setOnCompletionListener {
                            _playbackState.value = AudioPlaybackState.COMPLETED
                            _currentPositionMs.value = _durationMs.value
                            stopProgressTracker()
                        }
                        setOnErrorListener { _, _, _ ->
                            _playbackState.value = AudioPlaybackState.ERROR
                            stopProgressTracker()
                            true
                        }
                        prepareAsync()
                    }
                    mediaPlayer = player
                }
            } catch (e: Exception) {
                _playbackState.value = AudioPlaybackState.ERROR
            }
        }
    }

    override fun pause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                _playbackState.value = AudioPlaybackState.PAUSED
                stopProgressTracker()
            }
        }
    }

    override fun resume() {
        mediaPlayer?.let { player ->
            if (!player.isPlaying && _playbackState.value == AudioPlaybackState.PAUSED) {
                player.start()
                _playbackState.value = AudioPlaybackState.PLAYING
                startProgressTracker()
            }
        }
    }

    override fun seekTo(positionMs: Long) {
        mediaPlayer?.let { player ->
            player.seekTo(positionMs.toInt())
            _currentPositionMs.value = positionMs
        }
    }

    override fun stop() {
        stopProgressTracker()
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) {
                    player.stop()
                }
                player.reset()
                player.release()
            } catch (_: Exception) {}
        }
        mediaPlayer = null
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
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        _currentPositionMs.value = player.currentPosition.toLong()
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

actual fun createAudioPlayer(): KmpAudioPlayer = AndroidAudioPlayer()
