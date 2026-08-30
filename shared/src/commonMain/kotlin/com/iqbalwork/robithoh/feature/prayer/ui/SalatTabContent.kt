package com.iqbalwork.robithoh.feature.prayer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.BorderSubtle
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted
import com.iqbalwork.robithoh.feature.amaliyah.model.AdzanVoices
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import com.iqbalwork.robithoh.feature.amaliyah.ui.AdzanVoicePickerSheet
import com.iqbalwork.robithoh.feature.amaliyah.ui.PrayerNotificationModePickerSheet
import kotlinx.coroutines.launch

data class PrayerTrackingItem(
    val name: String,
    val time: String,
    val isMandatory: Boolean,
    val isCurrent: Boolean,
    val isPast: Boolean,
    val statusSubtitle: String,
    val progressFraction: Float? = null
)

private fun buildPrayerTrackingList(
    schedule: com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule?,
    selectedDateOffsetDays: Int,
    currentHour: Int,
    currentMinute: Int,
    currentSecond: Int
): List<PrayerTrackingItem> {
    if (schedule == null) {
        return listOf(
            PrayerTrackingItem("Imsak", "04:27", isMandatory = false, isCurrent = false, isPast = false, statusSubtitle = "Akan datang"),
            PrayerTrackingItem("Subuh", "04:37", isMandatory = true, isCurrent = true, isPast = false, statusSubtitle = "Sedang berlangsung", progressFraction = 0.5f),
            PrayerTrackingItem("Syuruq", "06:06", isMandatory = false, isCurrent = false, isPast = false, statusSubtitle = "Akan datang"),
            PrayerTrackingItem("Dzuhur", "11:55", isMandatory = true, isCurrent = false, isPast = false, statusSubtitle = "Akan datang"),
            PrayerTrackingItem("Ashar", "15:14", isMandatory = true, isCurrent = false, isPast = false, statusSubtitle = "Akan datang"),
            PrayerTrackingItem("Maghrib", "17:57", isMandatory = true, isCurrent = false, isPast = false, statusSubtitle = "Akan datang"),
            PrayerTrackingItem("Isya", "19:02", isMandatory = true, isCurrent = false, isPast = false, statusSubtitle = "Akan datang")
        )
    }

    fun parseToSec(timeStr: String): Long {
        val parts = timeStr.split(":")
        if (parts.size < 2) return 0L
        val h = parts[0].trim().toLongOrNull() ?: 0L
        val m = parts[1].trim().toLongOrNull() ?: 0L
        return h * 3600L + m * 60L
    }

    fun formatDuration(seconds: Long): String {
        val hrs = seconds / 3600L
        val mins = (seconds % 3600L) / 60L
        return when {
            hrs > 0 && mins > 0 -> "$hrs jam $mins m lagi"
            hrs > 0 -> "$hrs jam lagi"
            mins > 0 -> "$mins m lagi"
            else -> "< 1 m lagi"
        }
    }

    val currentSec = currentHour * 3600L + currentMinute * 60L + currentSecond

    val rawEntries = listOf(
        Triple("Imsak", schedule.imsak, false),
        Triple("Subuh", schedule.subuh, true),
        Triple("Syuruq", schedule.isyroq, false),
        Triple("Dzuhur", schedule.dzuhur, true),
        Triple("Ashar", schedule.ashar, true),
        Triple("Maghrib", schedule.maghrib, true),
        Triple("Isya", schedule.isya, true)
    )

    val timesSec = rawEntries.map { parseToSec(it.second) }

    return rawEntries.mapIndexed { index, (name, timeStr, isMandatory) ->
        val startSec = timesSec[index]
        val endSec = if (index < timesSec.size - 1) {
            timesSec[index + 1]
        } else {
            timesSec[0] + 86400L // Next day Imsak
        }

        if (selectedDateOffsetDays != 0) {
            val status = if (selectedDateOffsetDays < 0) "Selesai" else "Akan datang"
            PrayerTrackingItem(
                name = name,
                time = timeStr,
                isMandatory = isMandatory,
                isCurrent = false,
                isPast = selectedDateOffsetDays < 0,
                statusSubtitle = status,
                progressFraction = null
            )
        } else {
            val isActive = if (endSec > startSec) {
                currentSec in startSec until endSec
            } else {
                currentSec >= startSec || currentSec < (endSec % 86400L)
            }

            val isPast = if (endSec > startSec) {
                currentSec >= endSec
            } else {
                currentSec in (endSec % 86400L) until startSec
            }

            val isFuture = if (endSec > startSec) {
                currentSec < startSec
            } else {
                false
            }

            if (isActive) {
                val windowDuration = (endSec - startSec).coerceAtLeast(1L)
                val elapsed = if (currentSec >= startSec) {
                    currentSec - startSec
                } else {
                    currentSec + 86400L - startSec
                }
                val remainingSec = (windowDuration - elapsed).coerceAtLeast(0L)
                val progress = (elapsed.toFloat() / windowDuration.toFloat()).coerceIn(0f, 1f)

                PrayerTrackingItem(
                    name = name,
                    time = timeStr,
                    isMandatory = isMandatory,
                    isCurrent = true,
                    isPast = false,
                    statusSubtitle = "Sedang berlangsung · ${formatDuration(remainingSec)}",
                    progressFraction = progress
                )
            } else if (isFuture) {
                val remainingUntilStart = (startSec - currentSec).coerceAtLeast(0L)
                PrayerTrackingItem(
                    name = name,
                    time = timeStr,
                    isMandatory = isMandatory,
                    isCurrent = false,
                    isPast = false,
                    statusSubtitle = formatDuration(remainingUntilStart),
                    progressFraction = null
                )
            } else {
                PrayerTrackingItem(
                    name = name,
                    time = timeStr,
                    isMandatory = isMandatory,
                    isCurrent = false,
                    isPast = true,
                    statusSubtitle = "Waktu telah lewat",
                    progressFraction = null
                )
            }
        }
    }
}

@Composable
fun SalatTabContent(
    onNavigateToDocument: (String) -> Unit,
    onNavigateToCalculationMethods: () -> Unit = {},
    onNavigateToPrayerAdjustments: () -> Unit = {},
    onNavigateToQibla: () -> Unit = {},
    viewModel: AmaliyahViewModel? = null
) {
    val isDark = RabithohTheme.colors.isDark
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val vm = viewModel ?: remember(database) {
        AmaliyahViewModel(database = database)
    }
    val state by vm.uiState.collectAsState()
    val schedule = state.prayerSchedule
    val countdown = state.nextPrayerCountdown

    val now = com.iqbalwork.robithoh.core.datetime.currentLocalDateTime()
    val prayerList = remember(schedule, countdown, state.selectedDateOffsetDays, now.minute, now.second) {
        buildPrayerTrackingList(
            schedule = schedule,
            selectedDateOffsetDays = state.selectedDateOffsetDays,
            currentHour = now.hour,
            currentMinute = now.minute,
            currentSecond = now.second
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
            .background(if (isDark) DarkCanvas else PaperBackgroundLight),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
            bottom = 120.dp
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
                        text = "Jadwal Sholat",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else TextCharcoal
                    )
                    Text(
                        text = "Jadwal sholat wajib & sunnah TQN",
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else TextMuted
                    )
                }

                Surface(
                    color = if (isDark) DarkSurfaceVariant else (if (state.isGpsActive) Color(0xFFDDF5E6) else Color(0xFFF0F0F0)),
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
                            color = if (isDark) (if (state.isGpsActive) Color(0xFF86EFAC) else PutihBersih) else (if (state.isGpsActive) Color(0xFF1E824C) else TextCharcoal),
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
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color.White),
                border = if (isDark) BorderStroke(1.dp, DarkBorder) else null,
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
                        Text("‹", fontSize = 24.sp, color = if (isDark) PutihBersih else TextCharcoal)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = schedule?.dateFormatted ?: "Hari ini",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else TextCharcoal
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
                            color = if (isDark) DarkMuted else TextMuted
                        )
                    }
                    IconButton(onClick = { vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.ChangeDateOffset(1)) }) {
                        Text("›", fontSize = 24.sp, color = if (isDark) PutihBersih else TextCharcoal)
                    }
                }
            }
        }

        // 2.5 Quick Access Kompas Kiblat Card
        item {
            val qibla = state.qiblaInfo ?: com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator().calculateQibla(
                state.selectedLocation.latitude,
                state.selectedLocation.longitude,
                state.selectedLocation.name
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color.White),
                border = if (isDark) BorderStroke(1.dp, DarkBorder) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToQibla)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isDark) DarkSurfaceVariant else Color(0xFFFFF0F2),
                            border = BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.6f)),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🧭", fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Arah Kiblat",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else TextCharcoal
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Azimuth: ${qibla.directionDegrees}° · ${qibla.compassHeading}",
                                fontSize = 11.5.sp,
                                color = MerahMerdeka,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MerahMerdeka.copy(alpha = 0.15f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Buka Kompas",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MerahMerdeka,
                                maxLines = 1,
                                softWrap = false
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "›",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MerahMerdeka
                            )
                        }
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
                color = if (isDark) DarkMuted else TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        // 4. Prayer Tracking Items
        items(prayerList.size) { i ->
            val p = prayerList[i]
            val isLogged = state.loggedPrayers.contains(p.name)

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        p.isCurrent -> if (isDark) Color(0xFF2C241B) else Color(0xFFFDE8C4)
                        p.isPast -> if (isDark) DarkSurfaceVariant.copy(alpha = 0.6f) else Color(0xFFF8F6F2)
                        else -> if (isDark) DarkSurface else Color.White
                    }
                ),
                border = if (isDark) BorderStroke(1.dp, DarkBorder) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = if (p.isCurrent) 2.dp else (if (p.isMandatory) 1.dp else 0.dp)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left: Checkbox + Name & Subtitle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Checkbox (tap to log)
                            Surface(
                                shape = RoundedCornerShape(7.dp),
                                color = if (isLogged) Color(0xFF1E824C) else Color.Transparent,
                                border = if (!isLogged) androidx.compose.foundation.BorderStroke(1.5.dp, if (isDark) DarkBorder else Color(0xFFC7BAA7)) else null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.TogglePrayerLogged(p.name))
                                    }
                            ) {
                                if (isLogged) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (p.isCurrent) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFEA580C))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = p.name,
                                        fontWeight = if (p.isCurrent || p.isMandatory) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = if (p.isPast) (if (isDark) DarkMuted else TextMuted) else (if (isDark) PutihBersih else TextCharcoal)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = p.statusSubtitle,
                                    fontSize = 11.sp,
                                    fontWeight = if (p.isCurrent) FontWeight.Medium else FontWeight.Normal,
                                    color = if (p.isCurrent) (if (isDark) EmasMuda else Color(0xFF8C5B00)) else (if (isDark) DarkMuted else TextMuted)
                                )
                            }
                        }

                        // Right: Time + Bell
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = p.time,
                                fontWeight = FontWeight.Bold,
                                fontSize = if (p.isCurrent) 17.sp else 16.sp,
                                color = if (p.isPast) (if (isDark) DarkMuted else TextMuted) else (if (isDark) PutihBersih else TextCharcoal)
                            )
                            val prayerType = when (p.name) {
                                "Imsak" -> PrayerType.IMSAK
                                "Subuh" -> PrayerType.SUBUH
                                "Dzuhur" -> PrayerType.DZUHUR
                                "Ashar" -> PrayerType.ASHAR
                                "Maghrib" -> PrayerType.MAGHRIB
                                "Isya" -> PrayerType.ISYA
                                else -> null
                            }
                            val notifMode = prayerType?.let { state.notificationSettings.getPrayerMode(it) }

                            if (notifMode != null && prayerType != null) {
                                Spacer(modifier = Modifier.width(10.dp))
                                val bgModeColor = if (isDark) DarkSurfaceVariant else when (notifMode) {
                                    PrayerNotificationMode.ADZAN -> if (p.isCurrent) Color(0xFFFCE1B6) else Color(0xFFE2F3E7)
                                    PrayerNotificationMode.PUSH_NOTIFICATION -> Color(0xFFE1F5FE)
                                    PrayerNotificationMode.SILENT -> Color(0xFFF0F0F0)
                                }

                                Surface(
                                    color = bgModeColor,
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clickable {
                                            vm.onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(prayerType))
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(notifMode.icon, fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Progress Bar for Active Prayer Window
                    if (p.isCurrent && p.progressFraction != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { p.progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = Color(0xFF2C2A29),
                            trackColor = Color(0xFFE2C99D)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Ketuk baris untuk mencatat status · ketuk ikon untuk mengatur notifikasi",
                fontSize = 11.5.sp,
                color = if (isDark) DarkMuted else TextMuted,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        // 5. Pengaturan Sholat Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "PENGATURAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkMuted else TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        item {
            val notif = state.notificationSettings
            val selectedVoice = remember(notif.selectedVoiceId) {
                AdzanVoices.findById(notif.selectedVoiceId)
            }
            val customPath = notif.customAudioPath
            val voiceSubtitle = if (notif.selectedVoiceId == "custom" && !customPath.isNullOrBlank()) {
                "Audio Kustom (${customPath.substringAfterLast("/")})"
            } else {
                "${selectedVoice.title} (${if (selectedVoice.isBuiltIn) "bawaan" else "kustom"})"
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color.White),
                border = if (isDark) BorderStroke(1.dp, DarkBorder) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingItemRow(
                        icon = "🔔",
                        iconBg = if (isDark) DarkSurfaceVariant else Color(0xFFFFECEF),
                        title = "Suara adzan",
                        subtitle = voiceSubtitle,
                        isDark = isDark,
                        onClick = {
                            vm.onIntent(AmaliyahUiIntent.SetAdzanPickerSheetOpen(true))
                        }
                    )
                    HorizontalDivider(color = if (isDark) DarkBorder else BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                color = if (isDark) DarkSurfaceVariant else Color(0xFFFFF3E0),
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("⏱️", fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Pengingat sebelum sholat",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDark) PutihBersih else TextCharcoal
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Notifikasi 10 menit sebelum waktu tiba",
                                    fontSize = 11.5.sp,
                                    color = if (isDark) DarkMuted else TextMuted
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = state.notificationSettings.isPrePrayerReminderEnabled,
                            onCheckedChange = { vm.onIntent(AmaliyahUiIntent.TogglePrePrayerReminder(it)) },
                            colors = SwitchDefaults.colors(checkedTrackColor = MerahMerdeka)
                        )
                    }
                    HorizontalDivider(color = if (isDark) DarkBorder else BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    SettingItemRow(
                        icon = "⏰",
                        iconBg = if (isDark) DarkSurfaceVariant else Color(0xFFE3F2FD),
                        title = "Metode perhitungan",
                        subtitle = state.selectedCalculationMethod.name,
                        isDark = isDark,
                        onClick = onNavigateToCalculationMethods
                    )
                    HorizontalDivider(color = if (isDark) DarkBorder else BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    SettingItemRow(
                        icon = "⏱️",
                        iconBg = if (isDark) DarkSurfaceVariant else Color(0xFFE0F7FA),
                        title = "Koreksi waktu sholat",
                        subtitle = "Subuh: ${state.prayerAdjustments.getOffsetLabel(PrayerType.SUBUH)}, Dzuhur: ${state.prayerAdjustments.getOffsetLabel(PrayerType.DZUHUR)}, Ashar: ${state.prayerAdjustments.getOffsetLabel(PrayerType.ASHAR)}",
                        isDark = isDark,
                        onClick = onNavigateToPrayerAdjustments
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }

    if (state.isAdzanPickerSheetOpen) {
        AdzanVoicePickerSheet(
            selectedVoiceId = state.notificationSettings.selectedVoiceId,
            onSelectVoice = { voiceId ->
                vm.onIntent(AmaliyahUiIntent.SelectAdzanVoice(voiceId))
            },
            onDismiss = {
                vm.onIntent(AmaliyahUiIntent.SetAdzanPickerSheetOpen(false))
            }
        )
    }

    state.activeNotificationModePickerPrayer?.let { activePrayer ->
        val currentMode = state.notificationSettings.getPrayerMode(activePrayer)
        PrayerNotificationModePickerSheet(
            prayerType = activePrayer,
            currentMode = currentMode,
            onSelectMode = { mode ->
                vm.onIntent(AmaliyahUiIntent.SetPrayerNotificationMode(activePrayer, mode))
            },
            onTestTrigger = { mode ->
                vm.onIntent(AmaliyahUiIntent.TestTriggerPrayerNotification(activePrayer, mode))
            },
            onDismiss = {
                vm.onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(null))
            }
        )
    }
}

@Composable
private fun SettingItemRow(
    icon: String,
    iconBg: Color,
    title: String,
    subtitle: String,
    isDark: Boolean = false,
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
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = if (isDark) PutihBersih else TextCharcoal)
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, fontSize = 11.5.sp, color = if (isDark) DarkMuted else TextMuted)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("›", fontSize = 18.sp, color = if (isDark) DarkMuted else TextMuted)
    }
}
