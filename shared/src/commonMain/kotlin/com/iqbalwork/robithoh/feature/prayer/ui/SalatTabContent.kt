package com.iqbalwork.robithoh.feature.prayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import kotlinx.coroutines.launch

data class PrayerScheduleItem(
    val name: String,
    val time: String,
    val isMandatory: Boolean,
    val isCurrent: Boolean = false,
    val isPast: Boolean = false,
    val initialDone: Boolean = false
)

@Composable
fun SalatTabContent(
    onNavigateToDocument: (String) -> Unit,
    onNavigateToCalculationMethods: () -> Unit = {},
    onNavigateToPrayerAdjustments: () -> Unit = {},
    viewModel: AmaliyahViewModel? = null
) {
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val vm = viewModel ?: remember(database) {
        AmaliyahViewModel(database = database)
    }
    val state by vm.uiState.collectAsState()
    val schedule = state.prayerSchedule
    val countdown = state.nextPrayerCountdown

    var pinnedScheduleSwitch by remember { mutableStateOf(true) }

    val nextName = countdown?.nextPrayerName ?: "Subuh"

    val prayerList = if (schedule != null) {
        listOf(
            PrayerScheduleItem("Imsak", schedule.imsak, isMandatory = false, isCurrent = nextName == "Imsak"),
            PrayerScheduleItem("Subuh", schedule.subuh, isMandatory = true, isCurrent = nextName == "Subuh"),
            PrayerScheduleItem("Syuruq", schedule.isyroq, isMandatory = false, isCurrent = nextName == "Syuruq" || nextName == "Isyroq"),
            PrayerScheduleItem("Dzuhur", schedule.dzuhur, isMandatory = true, isCurrent = nextName == "Dzuhur"),
            PrayerScheduleItem("Ashar", schedule.ashar, isMandatory = true, isCurrent = nextName == "Ashar"),
            PrayerScheduleItem("Maghrib", schedule.maghrib, isMandatory = true, isCurrent = nextName == "Maghrib"),
            PrayerScheduleItem("Isya", schedule.isya, isMandatory = true, isCurrent = nextName == "Isya")
        )
    } else {
        listOf(
            PrayerScheduleItem("Imsak", "04:28", isMandatory = false),
            PrayerScheduleItem("Subuh", "04:38", isMandatory = true, isCurrent = true),
            PrayerScheduleItem("Syuruq", "05:52", isMandatory = false),
            PrayerScheduleItem("Dzuhur", "11:54", isMandatory = true),
            PrayerScheduleItem("Ashar", "15:14", isMandatory = true),
            PrayerScheduleItem("Maghrib", "17:52", isMandatory = true),
            PrayerScheduleItem("Isya", "19:02", isMandatory = true)
        )
    }

    val scope = rememberCoroutineScope()
    val locationProvider = com.iqbalwork.robithoh.core.location.rememberLocationProvider()

    val fetchGps = {
        scope.launch {
            vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.SetFetchingLocation(true))
            val loc = locationProvider.getCurrentLocation()
            if (loc != null) {
                vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.SetGpsLocation(loc))
            } else {
                vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.SetLocationError("Gagal mendeteksi lokasi GPS."))
            }
        }
    }

    val requestPermissionAndFetch = com.iqbalwork.robithoh.core.location.rememberLocationPermissionLauncher { granted ->
        if (granted) {
            fetchGps()
        } else {
            vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.SetLocationError("Izin lokasi tidak diberikan."))
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBackgroundLight),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Sholat & Waktu",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextCharcoal
                    )
                    Text(
                        text = "Jadwal sholat wajib & sunnah TQN",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Surface(
                    color = if (state.isGpsActive) Color(0xFFDDF5E6) else Color(0xFFF0F0F0),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.clickable {
                        if (locationProvider.hasLocationPermission()) {
                            fetchGps()
                        } else {
                            requestPermissionAndFetch()
                        }
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(if (state.isFetchingLocation) "⏳" else "📍", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (state.isFetchingLocation) "Mencari GPS..." else (schedule?.locationName ?: "Panjalu / Ciamis"),
                            color = if (state.isGpsActive) Color(0xFF1E824C) else TextCharcoal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // 2. Date Navigation Bar
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.ChangeDateOffset(-1)) }) {
                        Text("‹", fontSize = 24.sp, color = TextCharcoal)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = schedule?.dateFormatted ?: "Hari ini",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextCharcoal
                        )
                        val relativeLabel = when (state.selectedDateOffsetDays) {
                            0 -> "Hari ini"
                            -1 -> "Kemarin"
                            1 -> "Besok"
                            else -> if (state.selectedDateOffsetDays > 0) "+${state.selectedDateOffsetDays} hari" else "${state.selectedDateOffsetDays} hari"
                        }
                        Text(
                            text = "${schedule?.hijriDateFormatted ?: "14 Rabiul Awal 1448 H"} · $relativeLabel",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    IconButton(onClick = { vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.ChangeDateOffset(1)) }) {
                        Text("›", fontSize = 24.sp, color = TextCharcoal)
                    }
                }
            }
        }

        // 3. Section Header
        item {
            Text(
                text = "JADWAL SHOLAT (${schedule?.methodName ?: state.selectedCalculationMethod.name})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        // 4. Prayer Items
        items(prayerList.size) { i ->
            val p = prayerList[i]

            if (p.isCurrent) {
                // Highlighted Card for Current / Next Prayer
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE8C4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEA580C))
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = p.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TextCharcoal
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = p.time,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = TextCharcoal
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Surface(
                                    color = Color(0xFFFCE1B6),
                                    shape = CircleShape,
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("🔔", fontSize = 14.sp)
                                    }
                                }
                            }
                        }

                        if (countdown != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { countdown.progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = MerahMerdeka,
                                trackColor = Color(0xFFE2C99D)
                            )
                        }
                    }
                }
            } else {
                // Regular Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (p.isMandatory) Color.White else Color(0xFFF6F3EE)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (p.isMandatory) 1.dp else 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = p.name,
                            fontWeight = if (p.isMandatory) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 15.sp,
                            color = if (p.isMandatory) TextCharcoal else TextMuted
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = p.time,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (p.isMandatory) TextCharcoal else TextMuted
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Surface(
                                color = if (p.isMandatory) Color(0xFFE2F3E7) else Color.Transparent,
                                shape = CircleShape,
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (p.name == "Subuh") "🔊" else "🔔", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Pengaturan Sholat Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "PENGATURAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingItemRow(
                        icon = "🔔",
                        iconBg = Color(0xFFFFECEF),
                        title = "Suara adzan",
                        subtitle = "Misyari Rasyid (bawaan)"
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📍", fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Jadwal tersemat", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextCharcoal)
                                Text("Sholat berikutnya di notifikasi", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                        Switch(
                            checked = pinnedScheduleSwitch,
                            onCheckedChange = { pinnedScheduleSwitch = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = MerahMerdeka)
                        )
                    }
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    SettingItemRow(
                        icon = "⏰",
                        iconBg = Color(0xFFE3F2FD),
                        title = "Metode perhitungan",
                        subtitle = state.selectedCalculationMethod.name,
                        onClick = onNavigateToCalculationMethods
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    SettingItemRow(
                        icon = "⏱️",
                        iconBg = Color(0xFFE0F7FA),
                        title = "Koreksi waktu sholat",
                        subtitle = "Subuh: ${state.prayerAdjustments.getOffsetLabel(PrayerType.SUBUH)}, Dzuhur: ${state.prayerAdjustments.getOffsetLabel(PrayerType.DZUHUR)}, Ashar: ${state.prayerAdjustments.getOffsetLabel(PrayerType.ASHAR)}",
                        onClick = onNavigateToPrayerAdjustments
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
private fun SettingItemRow(
    icon: String,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                color = iconBg,
                shape = CircleShape,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextCharcoal)
                Text(subtitle, fontSize = 11.sp, color = TextMuted)
            }
        }
        Text("›", fontSize = 18.sp, color = TextMuted)
    }
}
