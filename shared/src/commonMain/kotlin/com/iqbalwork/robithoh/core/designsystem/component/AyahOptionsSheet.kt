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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

/**
 * Bottom sheet of quick actions for a single ayat, opened by tapping an ayah card
 * in the reader (murotal playback, bookmarking, sharing, copying).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahOptionsSheet(
    surahName: String,
    ayahNumber: Int,
    onDismiss: () -> Unit,
    onPlayMurotal: () -> Unit,
    onMarkLastRead: () -> Unit,
    onShare: () -> Unit,
    onCopy: () -> Unit,
    playMurotalEnabled: Boolean = true
) {
    val isDark = RabithohTheme.colors.isDark
    val textColor = if (isDark) PutihBersih else SlateCharcoalText
    val dividerColor = if (isDark) DarkBorder else BorderSubtle

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) DarkSurface else PutihBersih,
        shape = RabithohTheme.shapes.bottomSheetShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Surface(
                    color = MerahMerdeka,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.7f))
                ) {
                    Text(
                        text = "$surahName - Ayat $ayahNumber",
                        color = PutihBersih,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AyahOptionRow(
                icon = "▶",
                label = "Putar Murotal",
                enabled = playMurotalEnabled,
                onClick = { onDismiss(); onPlayMurotal() }
            )
            AyahOptionRow(
                icon = "🔖",
                label = "Tandai Terakhir Baca",
                onClick = { onDismiss(); onMarkLastRead() }
            )
            AyahOptionRow(
                icon = "📤",
                label = "Bagikan Ayat",
                onClick = { onDismiss(); onShare() }
            )
            AyahOptionRow(
                icon = "📋",
                label = "Salin Ayat",
                onClick = { onDismiss(); onCopy() }
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = dividerColor)
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tutup", color = textColor, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun AyahOptionRow(
    icon: String,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val isDark = RabithohTheme.colors.isDark
    val textColor = if (enabled) (if (isDark) PutihBersih else SlateCharcoalText) else (if (isDark) DarkMuted else SlateMuted)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(MerahMerdeka.copy(alpha = if (enabled) 0.12f else 0.06f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 14.sp)
        }
        Text(label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}
