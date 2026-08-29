package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.amaliyah.model.AdzanVoiceOption
import com.iqbalwork.robithoh.feature.amaliyah.model.AdzanVoices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdzanVoicePickerSheet(
    selectedVoiceId: String,
    onSelectVoice: (String) -> Unit,
    onDismiss: () -> Unit,
    onUploadCustomAudio: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Audio player dedicated for previewing adzan sounds
    val previewPlayer = remember { createAudioPlayer() }
    val playbackState by previewPlayer.playbackState.collectAsState()
    val currentTrack by previewPlayer.currentTrack.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            previewPlayer.release()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            previewPlayer.stop()
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = if (isDark) DarkSurface else Color(0xFFFCF8F2),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isDark) DarkMuted else Color(0xFFDCD6CD))
            )
        },
        modifier = modifier
    ) {
        var previewSubuhVersion by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Pilih Suara Adzan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else Color(0xFF2D2A26),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Segmented Preview Mode Switcher
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isDark) Color(0xFF1E2620) else Color(0xFFEBE6DC),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    val activeBg = if (isDark) Color(0xFF2C4A38) else Color.White
                    val inactiveBg = Color.Transparent
                    val activeTextColor = if (isDark) Color(0xFF98E2AF) else Color(0xFF1B4D2E)
                    val inactiveTextColor = if (isDark) DarkMuted else Color(0xFF6B6864)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!previewSubuhVersion) activeBg else inactiveBg)
                            .clickable {
                                previewSubuhVersion = false
                                previewPlayer.stop()
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Adzan Biasa",
                            fontSize = 13.sp,
                            fontWeight = if (!previewSubuhVersion) FontWeight.Bold else FontWeight.Medium,
                            color = if (!previewSubuhVersion) activeTextColor else inactiveTextColor
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (previewSubuhVersion) activeBg else inactiveBg)
                            .clickable {
                                previewSubuhVersion = true
                                previewPlayer.stop()
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Adzan Subuh",
                            fontSize = 13.sp,
                            fontWeight = if (previewSubuhVersion) FontWeight.Bold else FontWeight.Medium,
                            color = if (previewSubuhVersion) activeTextColor else inactiveTextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(AdzanVoices.ALL, key = { it.id }) { voice ->
                    val isSelected = voice.id == selectedVoiceId
                    val isThisPlaying = currentTrack?.id == "${voice.id}_${if (previewSubuhVersion) "subuh" else "reg"}" && playbackState == AudioPlaybackState.PLAYING

                    val targetAudio = if (previewSubuhVersion) voice.fajrAudioFileName else voice.audioFileName

                    AdzanVoiceRowItem(
                        voice = voice,
                        isSelected = isSelected,
                        isPlaying = isThisPlaying,
                        isDark = isDark,
                        previewModeSubtitle = if (previewSubuhVersion) "Versi Subuh (Khairum Minan Naum)" else "Versi Dzuhur, Ashar, Maghrib, Isya",
                        onSelect = {
                            onSelectVoice(voice.id)
                        },
                        onTogglePlay = {
                            if (isThisPlaying) {
                                previewPlayer.stop()
                            } else {
                                previewPlayer.play(
                                    AudioTrack(
                                        id = "${voice.id}_${if (previewSubuhVersion) "subuh" else "reg"}",
                                        title = "${voice.title} (${if (previewSubuhVersion) "Subuh" else "Biasa"})",
                                        subtitle = voice.subtitle,
                                        urlOrPath = targetAudio
                                    )
                                )
                            }
                        }
                    )
                }

                item {
                    // Upload custom audio item
                    UploadCustomAudioRowItem(
                        isDark = isDark,
                        onClick = {
                            previewPlayer.stop()
                            onUploadCustomAudio()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdzanVoiceRowItem(
    voice: AdzanVoiceOption,
    isSelected: Boolean,
    isPlaying: Boolean,
    isDark: Boolean,
    previewModeSubtitle: String? = null,
    onSelect: () -> Unit,
    onTogglePlay: () -> Unit
) {
    val selectedBgColor = if (isDark) Color(0xFF1E382B) else Color(0xFFCBEBD0)
    val defaultBgColor = Color.Transparent
    val playButtonBgColor = if (isSelected) {
        if (isDark) Color(0xFF2C5E43) else Color.White
    } else {
        if (isDark) Color(0xFF2C4A38) else Color(0xFFCBEBD0)
    }

    val playIconColor = if (isDark) Color(0xFF98E2AF) else Color(0xFF2E7D32)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) selectedBgColor else defaultBgColor)
            .clickable(onClick = onSelect)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = voice.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else Color(0xFF2B2B2B)
            )
            Text(
                text = voice.subtitle,
                fontSize = 13.sp,
                color = if (isDark) DarkMuted else Color(0xFF6B6864)
            )
            if (previewModeSubtitle != null) {
                Text(
                    text = "• $previewModeSubtitle",
                    fontSize = 11.sp,
                    color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (isSelected) {
            Text(
                text = "✓",
                color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 12.dp)
            )
        }

        // Play / Pause Circle Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(playButtonBgColor)
                .clickable(onClick = onTogglePlay),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isPlaying) "⏸" else "▶",
                color = playIconColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun UploadCustomAudioRowItem(
    isDark: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = "Unggah Audio Kustom",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else Color(0xFF2B2B2B)
            )
            Text(
                text = "Pilih berkas rekaman adzan dari perangkat Anda",
                fontSize = 13.sp,
                color = if (isDark) DarkMuted else Color(0xFF6B6864)
            )
        }
    }
}
