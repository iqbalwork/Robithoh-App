package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.iqbalwork.robithoh.core.audio.DownloadProgressState
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted

@Composable
fun FloatingDownloadBar(
    downloadState: DownloadProgressState,
    onCancelClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDownloading = downloadState is DownloadProgressState.Downloading
    val isError = downloadState is DownloadProgressState.Error

    AnimatedVisibility(
        visible = isDownloading || isError,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        val isDark = RabithohTheme.colors.isDark
        val barBg = if (isDark) DarkSurface else PutihBersih
        val border = BorderStroke(1.dp, if (isError) MerahMerdeka else EmasKhidmat.copy(alpha = if (isDark) 0.6f else 0.4f))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            color = barBg,
            border = border,
            shadowElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Progress Bar Line
                if (isDownloading) {
                    val state = downloadState as DownloadProgressState.Downloading
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(if (isDark) DarkBorder else Color(0xFFE4E4E7))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = state.progress.coerceIn(0.01f, 1f))
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
                    // Icon Box
                    Surface(
                        color = if (isError) MerahMerdeka.copy(alpha = 0.12f) else EmasKhidmat.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isError) "⚠️" else "⬇️",
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Text Info
                    Column(modifier = Modifier.weight(1f)) {
                        when (downloadState) {
                            is DownloadProgressState.Downloading -> {
                                val percent = (downloadState.progress * 100).toInt()
                                val downloadedMb = downloadState.bytesDownloaded / (1024.0 * 1024.0)
                                val totalMb = downloadState.totalBytes / (1024.0 * 1024.0)
                                val sizeText = if (downloadState.totalBytes > 0) {
                                    "${formatMb(downloadedMb)} / ${formatMb(totalMb)} MB"
                                } else {
                                    "${formatMb(downloadedMb)} MB"
                                }

                                Text(
                                    text = "Mengunduh ${downloadState.title}...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isDark) PutihBersih else TextCharcoal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "$percent%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MerahMerdeka
                                    )
                                    Text(
                                        text = sizeText,
                                        fontSize = 11.sp,
                                        color = if (isDark) DarkMuted else TextMuted
                                    )
                                }
                            }
                            is DownloadProgressState.Error -> {
                                Text(
                                    text = "Gagal mengunduh audio",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MerahMerdeka,
                                    maxLines = 1
                                )
                                Text(
                                    text = downloadState.errorMessage,
                                    fontSize = 11.sp,
                                    color = if (isDark) DarkMuted else TextMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            else -> {}
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Cancel / Dismiss button
                    IconButton(
                        onClick = {
                            val trackId = when (downloadState) {
                                is DownloadProgressState.Downloading -> downloadState.trackId
                                is DownloadProgressState.Error -> downloadState.trackId
                                else -> ""
                            }
                            onCancelClick(trackId)
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Text(
                            text = "✕",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkMuted else TextMuted
                        )
                    }
                }
            }
        }
    }
}

private fun formatMb(value: Double): String {
    val rounded = (value * 10).toInt() / 10.0
    return if (rounded == rounded.toLong().toDouble()) {
        "${rounded.toLong()}"
    } else {
        "$rounded"
    }
}
