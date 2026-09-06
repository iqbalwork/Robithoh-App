package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.BorderSubtle
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.ReaderTheme
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
 * Shared "text & reading settings" bottom sheet: offers the Arabic font-size
 * slider, reading background themes (Putih, Sepia, Khaki, Gelap), and optional
 * feature switches (e.g. Latin/translation visibility).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextReaderSettingsSheet(
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    onDismiss: () -> Unit,
    selectedTheme: ReaderTheme = ReaderTheme.WHITE,
    onThemeSelected: ((ReaderTheme) -> Unit)? = null,
    toggles: List<ReaderToggleOption> = emptyList()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (selectedTheme.isDark) Color(0xFF1E1A1A) else Color.White,
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
                color = if (selectedTheme.isDark) Color.White else TextCharcoal
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Font Scale Slider
            Text(
                text = "Ukuran Huruf: ${(fontScale * 100).toInt()}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selectedTheme.isDark) Color.White else TextCharcoal
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("A-", fontSize = 14.sp, color = if (selectedTheme.isDark) Color(0xFFA1A1AA) else TextMuted)
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
                Text("A+", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (selectedTheme.isDark) Color.White else TextCharcoal)
            }

            // 2. Reading Background Color Themes (Horizontal Swatches)
            if (onThemeSelected != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Warna Latar Bacaan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selectedTheme.isDark) Color.White else TextCharcoal
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ReaderTheme.entries.forEach { theme ->
                        val isSelected = theme == selectedTheme
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MerahMerdeka.copy(alpha = 0.12f) else (if (selectedTheme.isDark) Color(0xFF2A2424) else Color(0xFFF3F4F6)),
                            border = if (isSelected) BorderStroke(2.dp, MerahMerdeka) else BorderStroke(1.dp, if (selectedTheme.isDark) Color(0xFF3E3636) else Color(0xFFE5E7EB)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onThemeSelected(theme) }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(theme.swatchColor)
                                        .border(
                                            width = if (theme == ReaderTheme.WHITE) 1.dp else 0.5.dp,
                                            color = if (theme == ReaderTheme.WHITE) Color(0xFFCBD5E1) else Color(0x33000000),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (theme == ReaderTheme.DARK) Color.White else MerahMerdeka)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = theme.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MerahMerdeka else (if (selectedTheme.isDark) Color(0xFFE2E8F0) else TextCharcoal)
                                )
                            }
                        }
                    }
                }
            }

            // 3. Optional Feature Toggles (e.g. Quran Latin / Translation)
            if (toggles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = if (selectedTheme.isDark) Color(0xFF3E3636) else BorderSubtle)
                Spacer(modifier = Modifier.height(16.dp))

                toggles.forEachIndexed { i, toggle ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                toggle.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (selectedTheme.isDark) Color.White else TextCharcoal
                            )
                            Text(
                                toggle.subtitle,
                                fontSize = 12.sp,
                                color = if (selectedTheme.isDark) Color(0xFFA1A1AA) else TextMuted
                            )
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
