package com.iqbalwork.robithoh.feature.reader.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncManager
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncState
import kotlinx.coroutines.delay

@Composable
fun DocumentSyncOverlay(
    syncManager: DocumentSyncManager,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    val syncState by syncManager.syncState.collectAsState()
    var isDismissedManually by remember { mutableStateOf(false) }

    // Reset manual dismiss when new sync begins
    LaunchedEffect(syncState) {
        if (syncState is DocumentSyncState.Checking || syncState is DocumentSyncState.Syncing) {
            isDismissedManually = false
        }
    }

    // Auto-dismiss for Success / Error after a delay
    var showSuccessToast by remember { mutableStateOf(false) }
    var showErrorToast by remember { mutableStateOf(false) }

    LaunchedEffect(syncState) {
        when (syncState) {
            is DocumentSyncState.Success -> {
                showSuccessToast = true
                delay(3500)
                showSuccessToast = false
            }
            is DocumentSyncState.Error -> {
                showErrorToast = true
                delay(6000)
                showErrorToast = false
            }
            else -> {
                showSuccessToast = false
                showErrorToast = false
            }
        }
    }

    val isVisible = !isDismissedManually && (
        syncState is DocumentSyncState.Checking ||
        syncState is DocumentSyncState.Syncing ||
        (syncState is DocumentSyncState.Success && showSuccessToast) ||
        (syncState is DocumentSyncState.Error && showErrorToast)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 44.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                tonalElevation = 8.dp,
                shadowElevation = 10.dp,
                border = BorderStroke(
                    1.dp,
                    when (syncState) {
                        is DocumentSyncState.Error -> MerahMerdeka.copy(alpha = 0.6f)
                        is DocumentSyncState.Success -> Color(0xFF10B981).copy(alpha = 0.6f)
                        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    }
                ),
                modifier = Modifier.widthIn(min = 260.dp, max = 380.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (val state = syncState) {
                        is DocumentSyncState.Checking -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = MerahMerdeka
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = state.message,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        is DocumentSyncState.Syncing -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MerahMerdeka
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Memperbarui naskah (${state.current}/${state.total})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = "${(state.progress * 100).toInt()}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MerahMerdeka
                                )
                            }
                            if (state.currentFileName != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = state.currentFileName,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { state.progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp),
                                color = MerahMerdeka,
                                trackColor = MerahMerdeka.copy(alpha = 0.2f)
                            )
                        }

                        is DocumentSyncState.Success -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "✓",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (state.updatedCount > 0) {
                                        "${state.updatedCount} naskah berhasil diperbarui!"
                                    } else {
                                        "Naskah sudah versi terbaru."
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        is DocumentSyncState.Error -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "⚠️",
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Gagal memperbarui: ${state.message}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MerahMerdeka,
                                        maxLines = 2
                                    )
                                }
                                TextButton(
                                    onClick = onRetry,
                                    modifier = Modifier.padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "Coba Lagi",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MerahMerdeka
                                    )
                                }
                            }
                        }

                        DocumentSyncState.Idle -> {}
                    }
                }
            }
        }
    }
}
