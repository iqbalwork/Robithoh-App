package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack

/**
 * MiniFloatingAudioBar: Persistent bottom playback controller.
 * Displays track details, progress bar, play/pause controls, and full player expand trigger.
 */
@Composable
fun MiniFloatingAudioBar(
    track: AudioTrack?,
    playbackState: AudioPlaybackState,
    currentPositionMs: Long,
    durationMs: Long,
    onPlayPauseClick: () -> Unit,
    onBarClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCloseClick: (() -> Unit)? = null
) {
    val isVisible = track != null && playbackState != AudioPlaybackState.IDLE

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        if (track == null) return@AnimatedVisibility

        val isDark = RabithohTheme.colors.isDark
        val isPlaying = playbackState == AudioPlaybackState.PLAYING
        val isBuffering = playbackState == AudioPlaybackState.BUFFERING

        val progress = if (durationMs > 0) {
            (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
        } else 0f

        val barBg = if (isDark) DarkSurface else PutihBersih
        val border = BorderStroke(1.dp, EmasKhidmat.copy(alpha = if (isDark) 0.6f else 0.4f))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onBarClick),
            shape = RoundedCornerShape(16.dp),
            color = barBg,
            border = border,
            shadowElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Mini Progress Line
                if (isBuffering) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp),
                        color = EmasKhidmat,
                        trackColor = Color.Transparent
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .background(if (isDark) DarkBorder else Color(0xFFE4E4E7))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = progress)
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(MerahMerdeka, EmasKhidmat)
                                    )
                                )
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Audio Icon / Disc badge
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(MerahMarunGelap, MerahMerdeka)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♫",
                            color = EmasMuda,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Title & Subtitle / Timestamp
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = track.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) PutihBersih else SlateCharcoalText
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        val sub = if (track.subtitle.isNotBlank()) track.subtitle else "Rabithoh Audio"
                        val timeStr = formatMs(currentPositionMs) + " / " + formatMs(durationMs)
                        Text(
                            text = "$sub • $timeStr",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isDark) DarkMuted else SlateMuted,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Play/Pause Action Button
                    IconButton(
                        onClick = onPlayPauseClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (isPlaying) MerahMerdeka else EmasKhidmat
                            )
                    ) {
                        Text(
                            text = if (isPlaying) "❚❚" else "▶",
                            color = PutihBersih,
                            fontSize = if (isPlaying) 12.sp else 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (onCloseClick != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onCloseClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text(
                                text = "✕",
                                color = if (isDark) DarkMuted else SlateMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    if (ms <= 0L) return "0:00"
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}
