package com.iqbalwork.robithoh.feature.langgam.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.langgam.data.LanggamItem
import com.iqbalwork.robithoh.feature.langgam.data.LanggamRepository
import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanggamScreen(
    onBack: () -> Unit,
    audioPlayer: KmpAudioPlayer = remember { createAudioPlayer() }
) {
    BackHandler {
        onBack()
    }
    val currentTrack by audioPlayer.currentTrack.collectAsState()
    val playbackState by audioPlayer.playbackState.collectAsState()
    val currentPositionMs by audioPlayer.currentPositionMs.collectAsState()
    val durationMs by audioPlayer.durationMs.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Langgam TQN Sirnarasa",
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

                    LanggamTrackCard(
                        item = item,
                        isCurrent = isCurrent,
                        isPlaying = isPlaying,
                        onClick = {
                            if (isCurrent) {
                                if (isPlaying) audioPlayer.pause() else audioPlayer.resume()
                            } else {
                                audioPlayer.play(item.toAudioTrack())
                            }
                        }
                    )
                }
            }

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
    onClick: () -> Unit
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
                HorizontalDivider(color = BorderSubtle.copy(alpha = 0.6f))
            }

            Spacer(modifier = Modifier.width(10.dp))

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
    }
}

private fun formatTime(ms: Long): String {
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
}
