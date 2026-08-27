package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerTimeAdjustments
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerOffsetPickerSheet(
    prayerType: PrayerType,
    currentOffset: Int,
    onSelectOffset: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val isDark = RabithohTheme.colors.isDark
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) DarkSurface else PureWhite,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = if (isDark) DarkBorder else BorderSubtle
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Waktu Shalat ${prayerType.label}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) PutihBersih else TextCharcoal
                )
                IconButton(onClick = onDismiss) {
                    Text(
                        text = "✕",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else TextCharcoal
                    )
                }
            }

            HorizontalDivider(
                color = if (isDark) DarkBorder else BorderSubtle,
                thickness = 1.dp
            )

            // Offset List (-5 to +5 minutes)
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(PrayerTimeAdjustments.AVAILABLE_OFFSETS) { offset ->
                    val isSelected = offset == currentOffset
                    val label = PrayerTimeAdjustments.formatOffsetLabel(offset)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectOffset(offset)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MerahMerdeka else (if (isDark) PutihBersih else TextCharcoal)
                            )

                            if (isSelected) {
                                Text(
                                    text = "✓",
                                    color = Color(0xFF00897B),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        HorizontalDivider(
                            color = if (isDark) DarkBorder.copy(alpha = 0.4f) else BorderSubtle.copy(alpha = 0.5f),
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
