package com.iqbalwork.robithoh.feature.quran.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.BorderSubtle
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.MerahPrimaryDark
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.feature.quran.model.QariOption
import com.iqbalwork.robithoh.feature.quran.presentation.QuranAudioRepeatMode

/**
 * QuranAudioBottomBar: Persistent bottom floating audio bar.
 * Matches clean Quran reader design with:
 * - Circular outlined Play / Pause button (or loading spinner)
 * - Loop / repeat mode toggle button
 * - Equalizer / settings button
 * - Vertical divider
 * - Clickable selected Qari name
 * - Chevron right '>' to open Qari selection sheet
 */
@Composable
fun QuranAudioBottomBar(
    selectedQari: QariOption,
    playbackState: AudioPlaybackState,
    isAudioLoading: Boolean,
    repeatMode: QuranAudioRepeatMode,
    onPlayPauseClick: () -> Unit,
    onQariClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeAyahNumber: Int? = null,
    surahName: String? = null,
    onToggleRepeatMode: () -> Unit = {},
    onSettingsClick: () -> Unit = onQariClick,
    currentPositionMs: Long = 0L,
    durationMs: Long = 0L,
    customBackgroundColor: Color = Color.Unspecified
) {
    val isDark = RabithohTheme.colors.isDark
    val isPlaying = playbackState == AudioPlaybackState.PLAYING
    val isBuffering = isAudioLoading || playbackState == AudioPlaybackState.BUFFERING
    val audioAccent = if (isDark) MerahPrimaryDark else MerahMerdeka

    val barBg = if (customBackgroundColor != Color.Unspecified) {
        customBackgroundColor
    } else {
        if (isDark) DarkSurface else PutihBersih
    }

    val dockBorderColor = if (isDark) DarkBorder else Color(0xFF2C2523)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = barBg),
        border = BorderStroke(2.dp, dockBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Slim progress indicator line when audio is active
            if (isBuffering) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp),
                    color = audioAccent,
                    trackColor = Color.Transparent
                )
            } else if (durationMs > 0 && playbackState != AudioPlaybackState.IDLE) {
                val progress = (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp)
                        .background(if (isDark) DarkBorder else Color(0xFFE4E4E7))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progress)
                            .height(2.5.dp)
                            .background(audioAccent)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(1.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Play / Pause Circular Outlined Button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .border(
                            width = 1.6.dp,
                            color = audioAccent,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable(onClick = onPlayPauseClick),
                    contentAlignment = Alignment.Center
                ) {
                    if (isBuffering) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = audioAccent
                        )
                    } else if (isPlaying) {
                        Text(
                            text = "❚❚",
                            color = audioAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "▶",
                            color = audioAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 2. Loop / Repeat Mode Button
                val repeatColor = when (repeatMode) {
                    QuranAudioRepeatMode.REPEAT_SURAH -> audioAccent
                    QuranAudioRepeatMode.REPEAT_AYAH -> EmasKhidmat
                    QuranAudioRepeatMode.OFF -> if (isDark) DarkMuted else SlateMuted
                }

                IconButton(
                    onClick = onToggleRepeatMode,
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "⟳",
                            color = repeatColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (repeatMode == QuranAudioRepeatMode.REPEAT_AYAH) {
                            Text(
                                text = "1",
                                color = repeatColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 1.dp, end = 1.dp)
                            )
                        }
                    }
                }

                // 3. Audio Settings / Equalizer Button
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.size(34.dp)
                ) {
                    Text(
                        text = "🎛️",
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // 4. Vertical Divider
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp)
                        .background(if (isDark) DarkBorder else BorderSubtle)
                )

                Spacer(modifier = Modifier.width(10.dp))

                // 5. Clickable Qari Reciter Info + Chevron
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onQariClick)
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (activeAyahNumber != null) {
                            Text(
                                text = if (surahName != null) "$surahName • Ayat $activeAyahNumber" else "Ayat $activeAyahNumber",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = audioAccent,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = selectedQari.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) PutihBersih else SlateCharcoalText,
                                fontSize = if (activeAyahNumber != null) 12.5.sp else 13.5.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "›",
                        color = audioAccent,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
