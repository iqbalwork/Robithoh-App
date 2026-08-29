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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType

import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerNotificationModePickerSheet(
    prayerType: PrayerType,
    currentMode: PrayerNotificationMode,
    onSelectMode: (PrayerNotificationMode) -> Unit,
    onDismiss: () -> Unit,
    onTestTrigger: ((PrayerNotificationMode) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onDismiss()
    }
    val isDark = RabithohTheme.colors.isDark
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val options = if (prayerType == PrayerType.IMSAK) {
        listOf(
            Triple(
                PrayerNotificationMode.PUSH_NOTIFICATION,
                "Push Notifikasi",
                "Notifikasi layar standar dengan nada dering perangkat"
            ),
            Triple(
                PrayerNotificationMode.SILENT,
                "Senyap",
                "Tidak ada notifikasi dan suara alarm"
            )
        )
    } else {
        listOf(
            Triple(
                PrayerNotificationMode.ADZAN,
                "Suara Adzan (Alarm)",
                "Lantunan adzan lengkap & notifikasi waktu sholat"
            ),
            Triple(
                PrayerNotificationMode.PUSH_NOTIFICATION,
                "Push Notifikasi",
                "Notifikasi layar standar dengan nada dering perangkat"
            ),
            Triple(
                PrayerNotificationMode.SILENT,
                "Senyap",
                "Tidak ada notifikasi dan suara alarm"
            )
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Pengaturan Notifikasi ${prayerType.label}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else Color(0xFF2D2A26),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(options, key = { it.first.id }) { (mode, title, subtitle) ->
                    val isSelected = mode == currentMode
                    val selectedBgColor = if (isDark) Color(0xFF1E382B) else Color(0xFFCBEBD0)
                    val defaultBgColor = if (isDark) Color.Transparent else Color.Transparent

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) selectedBgColor else defaultBgColor)
                            .clickable {
                                onSelectMode(mode)
                            }
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else Color(0xFF2B2B2B)
                            )
                            Text(
                                text = subtitle,
                                fontSize = 13.sp,
                                color = if (isDark) DarkMuted else Color(0xFF6B6864)
                            )
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "✓",
                                color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (onTestTrigger != null && currentMode != PrayerNotificationMode.SILENT) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        FilledTonalButton(
                            onClick = { onTestTrigger(currentMode) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (isDark) DarkSurfaceVariant else Color(0xFFEBE6DC)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "🔔 Uji Notifikasi & Suara Sekarang",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else Color(0xFF2D2A26)
                            )
                        }
                    }
                }
            }
        }
    }
}
