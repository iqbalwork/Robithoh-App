package com.iqbalwork.robithoh.feature.qibla.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.location.rememberLocationPermissionLauncher
import com.iqbalwork.robithoh.core.location.rememberLocationProvider
import com.iqbalwork.robithoh.core.sensor.CompassAccuracy
import com.iqbalwork.robithoh.core.sensor.rememberCompassSensor
import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import com.iqbalwork.robithoh.feature.qibla.ui.component.QiblaDialComponent
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Full interactive Qibla Compass Screen styled in Robithoh's Merah Putih & Emas Khidmat theme.
 */
@Composable
fun QiblaScreen(
    onBack: () -> Unit,
    viewModel: AmaliyahViewModel? = null,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val vm = viewModel ?: remember(database) {
        AmaliyahViewModel(database = database)
    }

    val state by vm.uiState.collectAsState()
    val compassState = rememberCompassSensor()
    val haptic = getHapticFeedback()

    var showCalibrationSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val locationProvider = rememberLocationProvider()

    val fetchGps = {
        scope.launch {
            vm.onIntent(AmaliyahUiIntent.SetFetchingLocation(true))
            val loc = locationProvider.getCurrentLocation()
            if (loc != null) {
                vm.onIntent(AmaliyahUiIntent.SetGpsLocation(loc))
            } else {
                vm.onIntent(AmaliyahUiIntent.SetLocationError("Gagal mendeteksi lokasi GPS."))
            }
        }
    }

    val requestPermissionAndFetch = rememberLocationPermissionLauncher { granted ->
        if (granted) {
            fetchGps()
        } else {
            vm.onIntent(AmaliyahUiIntent.SetLocationError("Izin lokasi tidak diberikan."))
        }
    }

    // Active Qibla info from coordinates
    val qiblaInfo = remember(state.qiblaInfo, state.selectedLocation) {
        state.qiblaInfo ?: PrayerTimesCalculator().calculateQibla(
            latitude = state.selectedLocation.latitude,
            longitude = state.selectedLocation.longitude,
            cityName = state.selectedLocation.name
        )
    }

    val qiblaAzimuth = qiblaInfo.directionDegrees

    // Calculate angular deviation between phone heading and Qibla azimuth (-180 to +180)
    val deltaAngle = remember(compassState.heading, qiblaAzimuth) {
        ((compassState.heading - qiblaAzimuth + 540) % 360) - 180
    }
    val isAligned = abs(deltaAngle) <= 2.5

    // Trigger subtle haptic pulse when device locks onto Qibla
    var wasAligned by remember { mutableStateOf(false) }
    LaunchedEffect(isAligned) {
        if (isAligned && !wasAligned) {
            haptic.performSuccess()
        }
        wasAligned = isAligned
    }

    Scaffold(
        topBar = {
            IslamicHeader(
                title = "Arah Kiblat",
                subtitle = "Penunjuk arah Ka'bah Al-Mukarromah",
                arabicTitle = "الْقِبْلَةُ",
                onBackClick = onBack,
                actions = {
                    // Calibration Info Button
                    Surface(
                        shape = CircleShape,
                        color = if (isDark) DarkSurfaceVariant else Color(0xFFFFF0F2),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(34.dp)
                            .clickable { showCalibrationSheet = true }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "ⓘ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MerahMerdeka
                            )
                        }
                    }
                }
            )
        },
        containerColor = if (isDark) DarkCanvas else PaperBackgroundLight,
        modifier = modifier
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Background Islamic Arabesque Geometric Pattern
            IslamicArabesqueBackground(
                modifier = Modifier.fillMaxSize(),
                isDark = isDark
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Alignment Status Pill Banner
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = when {
                        isAligned -> Color(0xFF1E824C)
                        abs(deltaAngle) <= 15 -> Color(0xFFD97706)
                        else -> if (isDark) DarkSurfaceVariant else Color.White
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isAligned) EmasKhidmat else (if (isDark) Color(0xFF3E3636) else Color(0xFFE2D9CF))
                    ),
                    shadowElevation = if (isAligned) 4.dp else 1.dp,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = when {
                                isAligned -> "✨"
                                deltaAngle > 0 -> "↺"
                                else -> "↻"
                            },
                            fontSize = 14.sp,
                            color = if (isAligned) EmasMuda else MerahMerdeka
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when {
                                isAligned -> "Tepat Menghadap Kiblat"
                                deltaAngle > 0 -> "Putar ${abs(deltaAngle).roundToInt()}° ke kiri"
                                else -> "Putar ${abs(deltaAngle).roundToInt()}° ke kanan"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAligned || abs(deltaAngle) <= 15) PutihBersih else (if (isDark) PutihBersih else SlateCharcoalText)
                        )
                    }
                }

                // 2. Interactive Compass Dial
                QiblaDialComponent(
                    deviceHeading = compassState.heading,
                    qiblaAzimuth = qiblaAzimuth,
                    isAligned = isAligned,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 4.dp)
                )

                // 3. Qibla Degree & Compass Heading Readout
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Qiblat",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText
                    )
                    Text(
                        text = "${qiblaAzimuth}°",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAligned) Color(0xFF1E824C) else MerahMerdeka
                    )
                    Text(
                        text = qiblaInfo.compassHeading,
                        fontSize = 13.sp,
                        color = if (isDark) DarkMuted else SlateMuted
                    )
                }

                // 4. Information Details Card
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.GOLD_BORDER,
                    elevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Location & GPS Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Lokasi Referensi:",
                                    fontSize = 11.sp,
                                    color = if (isDark) DarkMuted else SlateMuted
                                )
                                Text(
                                    text = qiblaInfo.cityName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) PutihBersih else SlateCharcoalText
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (state.isGpsActive) Color(0xFFDDF5E6) else (if (isDark) DarkSurfaceVariant else Color(0xFFF0F0F0)),
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
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(if (state.isFetchingLocation) "⏳" else "📍", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (state.isFetchingLocation) "Mencari..." else (if (state.isGpsActive) "GPS Aktif" else "Pakai GPS"),
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (state.isGpsActive) Color(0xFF1E824C) else MerahMerdeka
                                    )
                                }
                            }
                        }

                        androidx.compose.material3.HorizontalDivider(
                            color = if (isDark) Color(0xFF3E3636) else Color(0xFFEBE5DF)
                        )

                        // Distance to Kaaba Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Jarak ke Ka'bah Al-Mukarromah:",
                                fontSize = 12.sp,
                                color = if (isDark) DarkMuted else SlateMuted
                            )
                            Text(
                                text = "${qiblaInfo.distanceKm} km",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmasKhidmat
                            )
                        }

                        // Sensor Accuracy Status Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Akurasi Sensor Kompas:",
                                fontSize = 12.sp,
                                color = if (isDark) DarkMuted else SlateMuted
                            )

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = when (compassState.accuracy) {
                                    CompassAccuracy.HIGH -> Color(0xFFE8F5E9)
                                    CompassAccuracy.MEDIUM -> Color(0xFFFFF8E1)
                                    CompassAccuracy.LOW, CompassAccuracy.UNRELIABLE -> Color(0xFFFFEBEE)
                                    CompassAccuracy.UNKNOWN -> Color(0xFFF5F5F5)
                                },
                                modifier = Modifier.clickable { showCalibrationSheet = true }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    val (label, textColor) = when (compassState.accuracy) {
                                        CompassAccuracy.HIGH -> Pair("Tinggi (Akurat)", Color(0xFF2E7D32))
                                        CompassAccuracy.MEDIUM -> Pair("Sedang", Color(0xFFF57F17))
                                        CompassAccuracy.LOW, CompassAccuracy.UNRELIABLE -> Pair("Perlu Kalibrasi ⓘ", MerahMerdeka)
                                        CompassAccuracy.UNKNOWN -> Pair("Kalibrasi ⓘ", SlateMuted)
                                    }
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Modal Calibration Sheet
    if (showCalibrationSheet) {
        CompassCalibrationSheet(
            onDismiss = { showCalibrationSheet = false }
        )
    }
}

/**
 * Elegant Islamic Arabesque geometric background wallpaper.
 */
@Composable
private fun IslamicArabesqueBackground(
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    val patternColor = if (isDark) {
        Color(0xFF231D1D).copy(alpha = 0.4f)
    } else {
        Color(0xFFEFE8DE).copy(alpha = 0.6f)
    }

    Canvas(modifier = modifier) {
        val step = 70.dp.toPx()
        val numX = (size.width / step).toInt() + 2
        val numY = (size.height / step).toInt() + 2

        for (ix in 0..numX) {
            for (iy in 0..numY) {
                val cx = ix * step
                val cy = iy * step

                // Draw 8-pointed star geometry
                val starRadius = step * 0.30f
                val starPath = Path()
                for (p in 0 until 16) {
                    val r = if (p % 2 == 0) starRadius else starRadius * 0.5f
                    val angle = p * (PI / 8.0)
                    val px = (cx + r * cos(angle)).toFloat()
                    val py = (cy + r * sin(angle)).toFloat()
                    if (p == 0) starPath.moveTo(px, py) else starPath.lineTo(px, py)
                }
                starPath.close()

                drawPath(
                    path = starPath,
                    color = patternColor,
                    style = Stroke(width = 0.8.dp.toPx())
                )
            }
        }
    }
}
