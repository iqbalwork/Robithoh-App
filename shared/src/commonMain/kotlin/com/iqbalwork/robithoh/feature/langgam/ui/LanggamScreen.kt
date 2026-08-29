package com.iqbalwork.robithoh.feature.langgam.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.audio.AudioCacheManager
import com.iqbalwork.robithoh.core.audio.AudioDownloader
import com.iqbalwork.robithoh.core.audio.DownloadProgressState
import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.audio.createAudioCacheManager
import com.iqbalwork.robithoh.core.audio.createAudioDownloader
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.designsystem.component.FloatingDownloadBar
import com.iqbalwork.robithoh.core.designsystem.theme.BorderSubtle
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.feature.langgam.data.LanggamItem
import com.iqbalwork.robithoh.feature.langgam.data.LanggamRepository
import com.iqbalwork.robithoh.navigation.BackHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanggamScreen(
    onBack: () -> Unit,
    audioPlayer: KmpAudioPlayer = remember { createAudioPlayer() },
    cacheManager: AudioCacheManager = remember { createAudioCacheManager() },
    audioDownloader: AudioDownloader = remember { createAudioDownloader(cacheManager) }
) {
    BackHandler {
        onBack()
    }

    val scope = rememberCoroutineScope()
    val currentTrack by audioPlayer.currentTrack.collectAsState()
    val playbackState by audioPlayer.playbackState.collectAsState()
    val currentPositionMs by audioPlayer.currentPositionMs.collectAsState()
    val durationMs by audioPlayer.durationMs.collectAsState()
    val downloadState by audioDownloader.downloadState.collectAsState()

    var cacheVersion by remember { mutableStateOf(0) }

    // When download completes, trigger cache refresh and auto-play
    LaunchedEffect(downloadState) {
        if (downloadState is DownloadProgressState.Completed) {
            val completed = downloadState as DownloadProgressState.Completed
            cacheVersion++
            val item = LanggamRepository.findById(completed.trackId)
            if (item != null) {
                audioPlayer.play(item.toAudioTrack(completed.localFilePath))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Langgam TQN PP Suryalaya Sirnarasa",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MerahMerdeka
                )
            )
        },
        containerColor = PaperBackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = MerahMerdeka.copy(alpha = 0.1f),
                                shape = CircleShape,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🎵", fontSize = 20.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Langgam & Irama Tilawah TQN",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextCharcoal
                                )
                                Text(
                                    "Bimbingan langgam bacaan sholat & dzikir Pangersa Abah Aos Ra. Qs.",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                items(LanggamRepository.langgamList, key = { it.id }) { item ->
                    val isCurrent = currentTrack?.id == item.id
                    val isPlaying = isCurrent && playbackState == AudioPlaybackState.PLAYING
                    val isCurrentDownloading = downloadState is DownloadProgressState.Downloading &&
                            (downloadState as DownloadProgressState.Downloading).trackId == item.id
                    val downloadProgress = if (isCurrentDownloading) {
                        (downloadState as DownloadProgressState.Downloading).progress
                    } else 0f

                    // Check if file is downloaded (re-evaluated on cacheVersion change)
                    val isDownloaded = remember(item.fileName, cacheVersion) {
                        cacheManager.isDownloaded(item.fileName)
                    }

                    LanggamTrackCard(
                        item = item,
                        isCurrent = isCurrent,
                        isPlaying = isPlaying,
                        isDownloaded = isDownloaded,
                        isDownloading = isCurrentDownloading,
                        downloadProgress = downloadProgress,
                        onClick = {
                            if (isCurrent) {
                                if (isPlaying) audioPlayer.pause() else audioPlayer.resume()
                            } else {
                                if (isDownloaded) {
                                    val path = cacheManager.getLocalFilePath(item.fileName)
                                    audioPlayer.play(item.toAudioTrack(path))
                                } else {
                                    scope.launch {
                                        val result = audioDownloader.downloadAudio(
                                            trackId = item.id,
                                            title = item.title,
                                            remoteUrl = item.remoteUrl,
                                            fileName = item.fileName,
                                            expectedSizeBytes = item.sizeBytes
                                        )
                                        if (result.isSuccess) {
                                            cacheVersion++
                                            val localPath = result.getOrNull()
                                            audioPlayer.play(item.toAudioTrack(localPath))
                                        }
                                    }
                                }
                            }
                        },
                        onDeleteClick = {
                            cacheManager.delete(item.fileName)
                            cacheVersion++
                            if (isCurrent) {
                                audioPlayer.stop()
                            }
                        }
                    )
                }
            }

            // Floating Download Progress Bar (if downloading)
            FloatingDownloadBar(
                downloadState = downloadState,
                onCancelClick = { trackId -> audioDownloader.cancelDownload(trackId) }
            )

            // Bottom Audio Player Bar
            AnimatedVisibility(
                visible = currentTrack != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentTrack?.title ?: "",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextCharcoal,
                                    maxLines = 1
                                )
                                Text(
                                    text = currentTrack?.subtitle ?: "",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        if (playbackState == AudioPlaybackState.PLAYING) {
                                            audioPlayer.pause()
                                        } else {
                                            audioPlayer.resume()
                                        }
                                    }
                                ) {
                                    Surface(
                                        color = MerahMerdeka,
                                        shape = CircleShape,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = if (playbackState == AudioPlaybackState.PLAYING) "⏸" else "▶",
                                                color = Color.White,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }

                                IconButton(onClick = { audioPlayer.stop() }) {
                                    Text("✕", fontSize = 16.sp, color = TextMuted)
                                }
                            }
                        }

                        if (durationMs > 0L) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Slider(
                                value = (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f),
                                onValueChange = { progress ->
                                    audioPlayer.seekTo((progress * durationMs).toLong())
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = MerahMerdeka,
                                    activeTrackColor = MerahMerdeka
                                ),
                                modifier = Modifier.fillMaxWidth().height(20.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(formatTime(currentPositionMs), fontSize = 11.sp, color = TextMuted)
                                Text(formatTime(durationMs), fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LanggamTrackCard(
    item: LanggamItem,
    isCurrent: Boolean,
    isPlaying: Boolean,
    isDownloaded: Boolean,
    isDownloading: Boolean,
    downloadProgress: Float,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) Color(0xFFFFF1F1) else Color.White
        ),
        border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, MerahMerdeka.copy(alpha = 0.4f)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calligraphy / Artwork Box
            Surface(
                color = if (isCurrent) MerahMerdeka.copy(alpha = 0.1f) else PaperBackgroundLight,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier
                    .width(76.dp)
                    .height(56.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.arabicCalligraphyText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCurrent) MerahMerdeka else MerahMarunGelap,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isCurrent) MerahMerdeka else TextCharcoal
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Size & Download Status Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (isDownloading) {
                        Text(
                            text = "Mengunduh ${(downloadProgress * 100).toInt()}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MerahMerdeka
                        )
                    } else if (isDownloaded) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = "✓ Offline • ${item.sizeLabel}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF3F4F6)
                        ) {
                            Text(
                                text = "☁️ ${item.sizeLabel}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextMuted,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (isDownloading) {
                CircularProgressIndicator(
                    progress = { downloadProgress },
                    modifier = Modifier.size(32.dp),
                    color = MerahMerdeka,
                    strokeWidth = 3.dp
                )
            } else if (isDownloaded) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("🗑️", fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Surface(
                        color = if (isPlaying) MerahMerdeka else PaperBackgroundLight,
                        shape = CircleShape,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isPlaying) "⏸" else "▶",
                                color = if (isPlaying) Color.White else TextCharcoal,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                Surface(
                    color = PaperBackgroundLight,
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "⬇️",
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
}
