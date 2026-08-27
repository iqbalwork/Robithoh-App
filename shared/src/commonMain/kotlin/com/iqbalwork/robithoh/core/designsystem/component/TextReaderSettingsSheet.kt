package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.BorderSubtle
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted

/**
 * A single on/off row shown below the font-size slider in [TextReaderSettingsSheet]
 * (e.g. "show Latin transliteration", "show translation").
 */
data class ReaderToggleOption(
    val title: String,
    val subtitle: String,
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
)

/**
 * Shared "text & reading settings" bottom sheet: always has the Arabic font-size
 * slider; pass [toggles] to additionally offer on/off switches (used by the
 * Qur'an reader for Latin/translation visibility). Other readers only need the
 * font size, so they call this with an empty toggle list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextReaderSettingsSheet(
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    onDismiss: () -> Unit,
    toggles: List<ReaderToggleOption> = emptyList()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "Pengaturan Teks & Bacaan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Font Scale Slider
            Text(
                text = "Ukuran Huruf Arab: ${(fontScale * 100).toInt()}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("A-", fontSize = 14.sp, color = TextMuted)
                Slider(
                    value = fontScale,
                    onValueChange = onFontScaleChange,
                    valueRange = 0.85f..1.65f,
                    steps = 5,
                    colors = SliderDefaults.colors(
                        thumbColor = MerahMerdeka,
                        activeTrackColor = MerahMerdeka
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                )
                Text("A+", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextCharcoal)
            }

            if (toggles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(16.dp))

                toggles.forEachIndexed { i, toggle ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(toggle.title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextCharcoal)
                            Text(toggle.subtitle, fontSize = 12.sp, color = TextMuted)
                        }
                        Switch(
                            checked = toggle.checked,
                            onCheckedChange = toggle.onCheckedChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MerahMerdeka,
                                uncheckedThumbColor = Color(0xFF9CA3AF),
                                uncheckedTrackColor = Color(0xFFE5E7EB),
                                uncheckedBorderColor = Color(0xFFCBD5E1)
                            )
                        )
                    }
                    if (i != toggles.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
