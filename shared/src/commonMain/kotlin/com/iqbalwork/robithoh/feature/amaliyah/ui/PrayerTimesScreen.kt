package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.amaliyah.model.LocationPreset
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiState

@Composable
fun PrayerTimesScreen(
    state: AmaliyahUiState,
    onIntent: (AmaliyahUiIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val schedule = state.prayerSchedule
    val countdown = state.nextPrayerCountdown
    val qibla = state.qiblaInfo

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
            // Location Presets Picker
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Pilih Lokasi Wilayah:",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText
                    )
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.locationPresets) { loc ->
                        val isSelected = loc.name == state.selectedLocation.name
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
                    Text(
                        text = "Lokasi: ${state.selectedLocation.name} (${state.selectedLocation.province})",
                        color = PutihBersih.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }

            // Daily 5 Prayer Schedule Card
            if (schedule != null) {
                GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_BORDER) {
                    Text(
                        text = "Jadwal Sholat Fardhu 5 Waktu",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else SlateCharcoalText
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    PrayerTimeRowItem("Imsak", schedule.imsak, schedule.timezone, false, isDark)
                    PrayerTimeRowItem("Subuh", schedule.subuh, schedule.timezone, countdown?.nextPrayerName == "Subuh", isDark)
                    PrayerTimeRowItem("Syuruq (Terbit)", schedule.isyroq, schedule.timezone, false, isDark)
                    PrayerTimeRowItem("Dhuha", schedule.dhuha, schedule.timezone, countdown?.nextPrayerName == "Dhuha", isDark)
                    PrayerTimeRowItem("Dzuhur", schedule.dzuhur, schedule.timezone, countdown?.nextPrayerName == "Dzuhur", isDark)
                    PrayerTimeRowItem("Ashar", schedule.ashar, schedule.timezone, countdown?.nextPrayerName == "Ashar", isDark)
                    PrayerTimeRowItem("Maghrib", schedule.maghrib, schedule.timezone, countdown?.nextPrayerName == "Maghrib", isDark)
                    PrayerTimeRowItem("Isya", schedule.isya, schedule.timezone, countdown?.nextPrayerName == "Isya", isDark)
                }

                // Tasawuf Schedule Card
                GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_TINTED) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Jadwal Amaliyah Tasawuf TQN",
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
                    PrayerTimeRowItem("Waktal (Wirid Khusus TQN)", schedule.waktal, schedule.timezone, countdown?.nextPrayerName == "Waktal", isDark, isHighlight = true)
                    PrayerTimeRowItem("Isyroq (Awal Dhuha)", schedule.isyroq, schedule.timezone, false, isDark)
                }
            }

            // Qibla Direction Card
            if (qibla != null) {
                GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_BORDER) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Arah Kiblat (Ka'bah Al-Mukarromah)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) PutihBersih else SlateCharcoalText
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Azimuth: ${qibla.directionDegrees}° (${qibla.compassHeading})",
                                color = MerahMerdeka,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Jarak ke Ka'bah: ${qibla.distanceKm} km",
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
                                Text("🕋", fontSize = 24.sp)
                            }
                        }
                    }
                }
            }
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
    isHighlight: Boolean = false
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
            Text(
                text = "$time $timezone",
                fontSize = 13.sp,
                fontWeight = if (isActive || isHighlight) FontWeight.Bold else FontWeight.Medium,
                color = if (isActive) MerahMerdeka else (if (isHighlight) EmasKhidmat else (if (isDark) Color.LightGray else Color.DarkGray))
            )
        }
    }
}
