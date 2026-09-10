package com.iqbalwork.robithoh.feature.quran.ui

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateBorder
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.quran.model.QariOption
import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QariPickerSheet(
    selectedQari: QariOption,
    availableQaris: List<QariOption>,
    onSelectQari: (QariOption) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onDismiss()
    }
    val isDark = RabithohTheme.colors.isDark
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Dedicated preview player so previewing sample audio doesn't interfere with main audio state
    val previewPlayer = remember { createAudioPlayer() }
    val playbackState by previewPlayer.playbackState.collectAsState()
    val currentTrack by previewPlayer.currentTrack.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            previewPlayer.stop()
            previewPlayer.release()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            previewPlayer.stop()
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = if (isDark) DarkSurface else PutihBersih,
        contentColor = if (isDark) PutihBersih else SlateCharcoalText,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Pilih Qari Murottal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else SlateCharcoalText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Audio bersumber dari API Quran.com & Quran Foundation CDN",
                fontSize = 12.sp,
                color = if (isDark) DarkMuted else SlateMuted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(availableQaris, key = { it.id }) { qari ->
                    val isSelected = qari.id == selectedQari.id
                    val previewTrackId = "preview_qari_${qari.id}"
                    val isPreviewPlaying = currentTrack?.id == previewTrackId &&
                            (playbackState == AudioPlaybackState.PLAYING || playbackState == AudioPlaybackState.BUFFERING)

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) {
                            if (isDark) MerahMerdeka.copy(alpha = 0.22f) else MerahMerdeka.copy(alpha = 0.08f)
                        } else {
                            if (isDark) DarkSurfaceVariant else Color(0xFFF8F9FA)
                        },
                        border = if (isSelected) {
                            androidx.compose.foundation.BorderStroke(1.5.dp, EmasKhidmat)
                        } else {
                            androidx.compose.foundation.BorderStroke(1.dp, if (isDark) DarkBorder else SlateBorder)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                previewPlayer.stop()
                                onSelectQari(qari)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icon / Status indicator
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) EmasKhidmat else (if (isDark) DarkSurface else Color(0xFFE5E7EB))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Text("✓", color = PutihBersih, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                } else {
                                    Text(
                                        text = "${qari.id}",
                                        color = if (isDark) DarkMuted else SlateMuted,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = qari.name,
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) {
                                        if (isDark) EmasMuda else MerahMerdeka
                                    } else {
                                        if (isDark) PutihBersih else SlateCharcoalText
                                    }
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Riwayat Hafs • ${qari.style}",
                                    fontSize = 12.sp,
                                    color = if (isDark) DarkMuted else SlateMuted
                                )
                            }

                            // Preview sample button (plays Al-Fatihah sample: 1.mp3)
                            Surface(
                                shape = CircleShape,
                                color = if (isPreviewPlaying) MerahMerdeka else (if (isDark) DarkSurface else PutihBersih),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isPreviewPlaying) EmasKhidmat else (if (isDark) DarkBorder else SlateBorder)
                                ),
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        if (isPreviewPlaying) {
                                            previewPlayer.stop()
                                        } else {
                                            previewPlayer.play(
                                                AudioTrack(
                                                    id = previewTrackId,
                                                    title = "Sampel ${qari.name}",
                                                    subtitle = "Al-Fatihah",
                                                    urlOrPath = "https://download.quranicaudio.com/qdc/${qari.slug}/murattal/1.mp3"
                                                )
                                            )
                                        }
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (isPreviewPlaying) "■" else "▶",
                                        fontSize = 13.sp,
                                        color = if (isPreviewPlaying) PutihBersih else EmasKhidmat,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
