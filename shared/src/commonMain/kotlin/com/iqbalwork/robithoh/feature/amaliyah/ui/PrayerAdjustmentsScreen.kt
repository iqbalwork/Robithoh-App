package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerTimeAdjustments
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType

data class PrayerAdjustmentRowItem(
    val type: PrayerType,
    val icon: String,
    val name: String,
    val time: String,
    val offsetLabel: String,
    val offsetMinutes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerAdjustmentsScreen(
    schedule: PrayerSchedule?,
    adjustments: PrayerTimeAdjustments,
    activePrayerTypeForSheet: PrayerType?,
    onOpenPicker: (PrayerType) -> Unit,
    onSelectOffset: (PrayerType, Int) -> Unit,
    onClosePicker: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    val items = listOf(
        PrayerAdjustmentRowItem(
            type = PrayerType.IMSAK,
            icon = "⛅",
            name = "Imsak",
            time = schedule?.imsak ?: "--:--",
            offsetLabel = adjustments.getOffsetLabel(PrayerType.IMSAK),
            offsetMinutes = adjustments.imsak
        ),
        PrayerAdjustmentRowItem(
            type = PrayerType.SUBUH,
            icon = "⛅",
            name = "Subuh",
            time = schedule?.subuh ?: "--:--",
            offsetLabel = adjustments.getOffsetLabel(PrayerType.SUBUH),
            offsetMinutes = adjustments.subuh
        ),
        PrayerAdjustmentRowItem(
            type = PrayerType.TERBIT,
            icon = "🌅",
            name = "Terbit",
            time = schedule?.isyroq ?: "--:--",
            offsetLabel = adjustments.getOffsetLabel(PrayerType.TERBIT),
            offsetMinutes = adjustments.terbit
        ),
        PrayerAdjustmentRowItem(
            type = PrayerType.DZUHUR,
            icon = "☀️",
            name = "Dzuhur",
            time = schedule?.dzuhur ?: "--:--",
            offsetLabel = adjustments.getOffsetLabel(PrayerType.DZUHUR),
            offsetMinutes = adjustments.dzuhur
        ),
        PrayerAdjustmentRowItem(
            type = PrayerType.ASHAR,
            icon = "🌤️",
            name = "Ashar",
            time = schedule?.ashar ?: "--:--",
            offsetLabel = adjustments.getOffsetLabel(PrayerType.ASHAR),
            offsetMinutes = adjustments.ashar
        ),
        PrayerAdjustmentRowItem(
            type = PrayerType.MAGHRIB,
            icon = "🌇",
            name = "Maghrib",
            time = schedule?.maghrib ?: "--:--",
            offsetLabel = adjustments.getOffsetLabel(PrayerType.MAGHRIB),
            offsetMinutes = adjustments.maghrib
        ),
        PrayerAdjustmentRowItem(
            type = PrayerType.ISYA,
            icon = "🌙",
            name = "Isya",
            time = schedule?.isya ?: "--:--",
            offsetLabel = adjustments.getOffsetLabel(PrayerType.ISYA),
            offsetMinutes = adjustments.isya
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Penyesuaian Waktu Salat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isDark) PutihBersih else TextCharcoal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else TextCharcoal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) DarkSurface else PureWhite
                )
            )
        },
        containerColor = if (isDark) DarkCanvas else PureWhite,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(items) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenPicker(item.type) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Icon + Name + Time
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.icon,
                                fontSize = 22.sp,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            Column {
                                Text(
                                    text = item.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDark) PutihBersih else TextCharcoal
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.time,
                                    fontSize = 12.sp,
                                    color = if (isDark) DarkMuted else TextMuted
                                )
                            }
                        }

                        // Right: Offset Label + Chevron
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.offsetLabel,
                                fontSize = 14.sp,
                                color = if (item.offsetMinutes != 0) MerahMerdeka else (if (isDark) DarkMuted else TextMuted),
                                fontWeight = if (item.offsetMinutes != 0) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "›",
                                fontSize = 20.sp,
                                color = if (isDark) DarkMuted else TextMuted
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

    // Modal Bottom Sheet when a prayer time item is selected
    if (activePrayerTypeForSheet != null) {
        val currentOffset = adjustments.getOffset(activePrayerTypeForSheet)
        PrayerOffsetPickerSheet(
            prayerType = activePrayerTypeForSheet,
            currentOffset = currentOffset,
            onSelectOffset = { offset ->
                onSelectOffset(activePrayerTypeForSheet, offset)
            },
            onDismiss = onClosePicker
        )
    }
}
