package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihAbuBackground
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.location.rememberLocationPermissionLauncher
import com.iqbalwork.robithoh.core.location.rememberLocationProvider
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiState
import kotlinx.coroutines.launch

@Composable
fun PrayerTimesScreen(
    state: AmaliyahUiState,
    onIntent: (AmaliyahUiIntent) -> Unit,
    onNavigateToMethods: () -> Unit = {},
    onNavigateToAdjustments: () -> Unit = {},
    onNavigateToQibla: () -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val schedule = state.prayerSchedule
    val countdown = state.nextPrayerCountdown
    val qibla = state.qiblaInfo
    val scope = rememberCoroutineScope()
    val locationProvider = rememberLocationProvider()

    val fetchGps = {
        scope.launch {
            onIntent(AmaliyahUiIntent.SetFetchingLocation(true))
            val loc = locationProvider.getCurrentLocation()
            if (loc != null) {
                onIntent(AmaliyahUiIntent.SetGpsLocation(loc))
            } else {
                onIntent(AmaliyahUiIntent.SetLocationError("Gagal mendeteksi lokasi GPS."))
            }
        }
    }

    val requestPermissionAndFetch = rememberLocationPermissionLauncher { granted ->
        if (granted) {
            fetchGps()
        } else {
            onIntent(AmaliyahUiIntent.SetLocationError("Izin lokasi tidak diberikan."))
        }
    }

    Scaffold(
        topBar = {
            IslamicHeader(
                title = "Jadwal Sholat & Tasawuf",
                subtitle = "Jadwal luring, Tahajjud, Waktal & Arah Kiblat",
                arabicTitle = "مَوَاقِيتُ الصَّلَاةِ",
                onBackClick = onBack
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location Presets & GPS Picker
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pilih Lokasi Wilayah:",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else SlateCharcoalText
                        )
                    )
                    if (state.isFetchingLocation) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                strokeWidth = 2.dp,
                                color = MerahMerdeka
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Mencari GPS...",
                                fontSize = 11.sp,
                                color = MerahMerdeka
                            )
                        }
                    }
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 1. GPS Location Button
                    item {
                        val isGpsActive = state.isGpsActive
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isGpsActive) Color(0xFF1E824C) else (if (isDark) DarkSurfaceVariant else Color(0xFFE8F5E9)),
                            border = BorderStroke(1.dp, if (isGpsActive) EmasKhidmat else Color(0xFF81C784)),
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
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("📍", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isGpsActive) "GPS: ${state.selectedLocation.name}" else "GPS Lokasi Saya",
                                    color = if (isGpsActive) PutihBersih else Color(0xFF1E824C),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // 2. Preset Locations
                    items(state.locationPresets) { loc ->
                        val isSelected = !state.isGpsActive && loc.name == state.selectedLocation.name
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MerahMerdeka else (if (isDark) DarkSurfaceVariant else Color(0xFFE9ECEF)),
                            border = if (isSelected) BorderStroke(1.dp, EmasKhidmat) else null,
                            modifier = Modifier.clickable {
                                onIntent(AmaliyahUiIntent.SelectLocation(loc))
                            }
                        ) {
                            Text(
                                text = loc.name,
                                color = if (isSelected) PutihBersih else (if (isDark) Color.LightGray else SlateCharcoalText),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Hero Next Prayer Countdown Card
            if (countdown != null && schedule != null) {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Menuju Waktu Berikutnya:",
                                color = EmasMuda,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = countdown.nextPrayerName,
                                color = PutihBersih,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${countdown.nextPrayerTime} ${schedule.timezone}",
                                color = PutihBersih.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                        }

                        // Countdown Timer Box
                        Surface(
                            color = Color.Black.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.6f))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "${countdown.remainingHours.toString().padStart(2, '0')}:${countdown.remainingMinutes.toString().padStart(2, '0')}:${countdown.remainingSeconds.toString().padStart(2, '0')}",
                                    color = EmasMuda,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "sisa waktu",
                                    color = PutihBersih.copy(alpha = 0.7f),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { countdown.progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = EmasKhidmat,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lokasi: ${state.selectedLocation.name}",
                            color = PutihBersih.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = state.selectedCalculationMethod.name,
                            color = EmasMuda,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Calculation Method & Adjustments Quick Settings Card
            GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_BORDER) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pengaturan Perhitungan & Notifikasi",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else SlateCharcoalText
                        )
                    )
                    Text("⚙️", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 1. Calculation Method Setting Tile
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDark) DarkSurfaceVariant else Color(0xFFF7F7F8),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToMethods)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Metode Perhitungan",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else SlateCharcoalText
                            )
                            Text(
                                text = state.selectedCalculationMethod.name,
                                fontSize = 11.sp,
                                color = MerahMerdeka
                            )
                        }
                        Text(
                            text = "Ubah ›",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmasKhidmat
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 2. Prayer Time Adjustments Tile
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDark) DarkSurfaceVariant else Color(0xFFF7F7F8),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToAdjustments)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Penyesuaian Waktu Salat",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else SlateCharcoalText
                            )
                            Text(
                                text = "Koreksi manual Imsak, Subuh, Dzuhur, Ashar, Maghrib, Isya (+/- menit)",
                                fontSize = 11.sp,
                                color = if (isDark) DarkMuted else SlateMuted
                            )
                        }
                        Text(
                            text = "Koreksi ›",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmasKhidmat
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 3. Adzan Voice & Notification Setting Tile
                val selectedVoice = com.iqbalwork.robithoh.feature.amaliyah.model.AdzanVoices.findById(state.notificationSettings.selectedVoiceId)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDark) DarkSurfaceVariant else Color(0xFFF7F7F8),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onIntent(AmaliyahUiIntent.SetAdzanPickerSheetOpen(true)) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Pilihan Suara Adzan & Notifikasi",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else SlateCharcoalText
                            )
                            Text(
                                text = "Muadzin: ${selectedVoice.title}",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "Pilih ›",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmasKhidmat
                        )
                    }
                }
            }

            // Daily 5 Prayer Schedule Card
            if (schedule != null) {
                val notif = state.notificationSettings
                GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_BORDER) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jadwal Sholat Fardhu 5 Waktu",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else SlateCharcoalText
                            )
                        )
                        Text(
                            text = "Ketuk 🔔 utk ubah mode",
                            fontSize = 11.sp,
                            color = if (isDark) DarkMuted else SlateMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    PrayerTimeRowItem(
                        label = "Imsak",
                        time = schedule.imsak,
                        timezone = schedule.timezone,
                        isActive = false,
                        isDark = isDark,
                        prayerType = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.IMSAK,
                        notificationMode = notif.imsakMode,
                        onCycleNotificationMode = { onIntent(AmaliyahUiIntent.CyclePrayerNotificationMode(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.IMSAK)) },
                        onOpenModePicker = { onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.IMSAK)) }
                    )
                    PrayerTimeRowItem(
                        label = "Subuh",
                        time = schedule.subuh,
                        timezone = schedule.timezone,
                        isActive = countdown?.nextPrayerName == "Subuh",
                        isDark = isDark,
                        prayerType = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.SUBUH,
                        notificationMode = notif.subuhMode,
                        onCycleNotificationMode = { onIntent(AmaliyahUiIntent.CyclePrayerNotificationMode(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.SUBUH)) },
                        onOpenModePicker = { onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.SUBUH)) }
                    )
                    PrayerTimeRowItem(
                        label = "Syuruq (Terbit)",
                        time = schedule.isyroq,
                        timezone = schedule.timezone,
                        isActive = false,
                        isDark = isDark
                    )
                    PrayerTimeRowItem(
                        label = "Dhuha",
                        time = schedule.dhuha,
                        timezone = schedule.timezone,
                        isActive = countdown?.nextPrayerName == "Dhuha",
                        isDark = isDark
                    )
                    PrayerTimeRowItem(
                        label = "Dzuhur",
                        time = schedule.dzuhur,
                        timezone = schedule.timezone,
                        isActive = countdown?.nextPrayerName == "Dzuhur",
                        isDark = isDark,
                        prayerType = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.DZUHUR,
                        notificationMode = notif.dzuhurMode,
                        onCycleNotificationMode = { onIntent(AmaliyahUiIntent.CyclePrayerNotificationMode(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.DZUHUR)) },
                        onOpenModePicker = { onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.DZUHUR)) }
                    )
                    PrayerTimeRowItem(
                        label = "Ashar",
                        time = schedule.ashar,
                        timezone = schedule.timezone,
                        isActive = countdown?.nextPrayerName == "Ashar",
                        isDark = isDark,
                        prayerType = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.ASHAR,
                        notificationMode = notif.asharMode,
                        onCycleNotificationMode = { onIntent(AmaliyahUiIntent.CyclePrayerNotificationMode(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.ASHAR)) },
                        onOpenModePicker = { onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.ASHAR)) }
                    )
                    PrayerTimeRowItem(
                        label = "Maghrib",
                        time = schedule.maghrib,
                        timezone = schedule.timezone,
                        isActive = countdown?.nextPrayerName == "Maghrib",
                        isDark = isDark,
                        prayerType = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.MAGHRIB,
                        notificationMode = notif.maghribMode,
                        onCycleNotificationMode = { onIntent(AmaliyahUiIntent.CyclePrayerNotificationMode(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.MAGHRIB)) },
                        onOpenModePicker = { onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.MAGHRIB)) }
                    )
                    PrayerTimeRowItem(
                        label = "Isya",
                        time = schedule.isya,
                        timezone = schedule.timezone,
                        isActive = countdown?.nextPrayerName == "Isya",
                        isDark = isDark,
                        prayerType = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.ISYA,
                        notificationMode = notif.isyaMode,
                        onCycleNotificationMode = { onIntent(AmaliyahUiIntent.CyclePrayerNotificationMode(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.ISYA)) },
                        onOpenModePicker = { onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType.ISYA)) }
                    )
                }

                // Tasawuf Schedule Card
                GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_TINTED) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Jadwal Amaliyah Tasawuf",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MerahMerdeka
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("✦", color = EmasKhidmat, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Waktu hening utama untuk Dzikir Khofi, sholat malam & munajat Sirnarasa:",
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else SlateMuted
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    PrayerTimeRowItem("Waktu Malam (Tahajjud)", schedule.tahajjud, schedule.timezone, countdown?.nextPrayerName == "Tahajjud", isDark, isHighlight = true)
                    PrayerTimeRowItem("Waktal (Wirid Khusus)", schedule.waktal, schedule.timezone, countdown?.nextPrayerName == "Waktal", isDark, isHighlight = true)
                    PrayerTimeRowItem("Isyroq (Awal Dhuha)", schedule.isyroq, schedule.timezone, false, isDark)
                }
            }

            // Qibla Direction Card
            if (qibla != null) {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.GOLD_BORDER,
                    modifier = Modifier.clickable(onClick = onNavigateToQibla)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Arah Kiblat (Ka'bah Al-Mukarromah)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) PutihBersih else SlateCharcoalText
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Kompas ›",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmasKhidmat
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Azimuth: ${qibla.directionDegrees}° (${qibla.compassHeading})",
                                color = MerahMerdeka,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Jarak ke Ka'bah: ${qibla.distanceKm} km · Ketuk untuk buka kompas",
                                color = if (isDark) DarkMuted else SlateMuted,
                                fontSize = 12.sp
                            )
                        }

                        // Compass Graphic Icon
                        Surface(
                            modifier = Modifier.size(54.dp),
                            shape = CircleShape,
                            color = MerahMerdeka.copy(alpha = 0.1f),
                            border = BorderStroke(1.5.dp, EmasKhidmat)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🧭", fontSize = 24.sp)
                            }
                        }
                    }
                }
            }
        }

        if (state.isAdzanPickerSheetOpen) {
            AdzanVoicePickerSheet(
                selectedVoiceId = state.notificationSettings.selectedVoiceId,
                onSelectVoice = { voiceId ->
                    onIntent(AmaliyahUiIntent.SelectAdzanVoice(voiceId))
                },
                onDismiss = {
                    onIntent(AmaliyahUiIntent.SetAdzanPickerSheetOpen(false))
                }
            )
        }

        state.activeNotificationModePickerPrayer?.let { activePrayer ->
            val currentMode = state.notificationSettings.getPrayerMode(activePrayer)
            PrayerNotificationModePickerSheet(
                prayerType = activePrayer,
                currentMode = currentMode,
                onSelectMode = { mode ->
                    onIntent(AmaliyahUiIntent.SetPrayerNotificationMode(activePrayer, mode))
                },
                onDismiss = {
                    onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(null))
                }
            )
        }
    }
}

@Composable
private fun PrayerTimeRowItem(
    label: String,
    time: String,
    timezone: String,
    isActive: Boolean,
    isDark: Boolean,
    isHighlight: Boolean = false,
    prayerType: com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType? = null,
    notificationMode: com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode? = null,
    onCycleNotificationMode: (() -> Unit)? = null,
    onOpenModePicker: (() -> Unit)? = null
) {
    Surface(
        color = when {
            isActive -> MerahMerdeka.copy(alpha = 0.12f)
            isHighlight -> EmasKhidmat.copy(alpha = 0.08f)
            else -> Color.Transparent
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(MerahMerdeka)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = if (isActive || isHighlight) FontWeight.Bold else FontWeight.Normal,
                    color = if (isActive) MerahMerdeka else (if (isDark) PutihBersih else SlateCharcoalText)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$time $timezone",
                    fontSize = 13.sp,
                    fontWeight = if (isActive || isHighlight) FontWeight.Bold else FontWeight.Medium,
                    color = if (isActive) MerahMerdeka else (if (isHighlight) EmasKhidmat else (if (isDark) Color.LightGray else Color.DarkGray))
                )

                if (notificationMode != null && onCycleNotificationMode != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    val bgModeColor = when (notificationMode) {
                        com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN -> {
                            if (isDark) Color(0xFF1E382B) else Color(0xFFE8F5E9)
                        }
                        com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.PUSH_NOTIFICATION -> {
                            if (isDark) Color(0xFF263238) else Color(0xFFE1F5FE)
                        }
                        com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.SILENT -> {
                            if (isDark) DarkSurfaceVariant else Color(0xFFF0F0F0)
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = bgModeColor,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                if (onOpenModePicker != null) {
                                    onOpenModePicker()
                                } else {
                                    onCycleNotificationMode()
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = notificationMode.icon,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
