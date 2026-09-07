package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.BorderSubtle
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihAbuBackground
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.feature.quran.data.MushafDownloadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadMushafSheet(
    downloadState: MushafDownloadState,
    onDismiss: () -> Unit,
    onStartDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onDontShowAgainChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    var dontShowAgain by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = {
            if (downloadState !is MushafDownloadState.Downloading) {
                onDismiss()
            }
        },
        containerColor = if (isDark) DarkSurface else PutihBersih,
        shape = RabithohTheme.shapes.bottomSheetShape,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Unduh Seluruh Halaman Mushaf",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else SlateCharcoalText
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Card item
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isDark) DarkSurfaceVariant else PutihAbuBackground,
                border = BorderStroke(1.dp, if (isDark) DarkBorder else BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MerahMerdeka.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📖", fontSize = 22.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mushaf Madinah 15 Baris",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = if (isDark) PutihBersih else SlateCharcoalText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ukuran file: ~117 MB (604 Halaman)",
                            fontSize = 13.sp,
                            color = if (isDark) DarkMuted else SlateMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Unduh 604 halaman mushaf agar Anda dapat membaca Al-Qur'an secara luring (offline) tanpa kuota internet dengan transisi antar halaman yang sangat cepat.",
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = if (isDark) DarkMuted else SlateMuted
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (downloadState) {
                is MushafDownloadState.Downloading -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Mengunduh: ${downloadState.downloadedPages} / ${downloadState.totalPages}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) PutihBersih else SlateCharcoalText
                            )
                            Text(
                                text = "${(downloadState.progress * 100).toInt()}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MerahMerdeka
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { downloadState.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MerahMerdeka,
                            trackColor = if (isDark) DarkBorder else BorderSubtle,
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedButton(
                            onClick = onCancelDownload,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Batalkan Unduhan", color = MerahMerdeka)
                        }
                    }
                }

                is MushafDownloadState.Completed -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✅ Seluruh mushaf berhasil diunduh!",
                            fontWeight = FontWeight.SemiBold,
                            color = EmasKhidmat,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Selesai", color = PutihBersih, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                is MushafDownloadState.Error -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = downloadState.errorMessage,
                            color = MerahMerdeka,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Tutup")
                            }
                            Button(
                                onClick = onStartDownload,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Coba Lagi", color = PutihBersih, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                MushafDownloadState.Idle -> {
                    // Checkbox "Jangan tampilkan lagi"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                dontShowAgain = !dontShowAgain
                                onDontShowAgainChecked(dontShowAgain)
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = dontShowAgain,
                            onCheckedChange = {
                                dontShowAgain = it
                                onDontShowAgainChecked(it)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MerahMerdeka,
                                checkmarkColor = PutihBersih
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Jangan tampilkan lagi",
                            fontSize = 14.sp,
                            color = if (isDark) PutihBersih else SlateCharcoalText
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Lain Kali")
                        }
                        Button(
                            onClick = onStartDownload,
                            colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Unduh Sekarang", color = PutihBersih, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}
