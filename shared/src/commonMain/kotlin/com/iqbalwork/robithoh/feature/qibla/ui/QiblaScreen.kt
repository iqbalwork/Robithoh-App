package com.iqbalwork.robithoh.feature.qibla.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import com.iqbalwork.robithoh.core.location.rememberLocationPermissionLauncher
import com.iqbalwork.robithoh.core.location.rememberLocationProvider
import com.iqbalwork.robithoh.core.sensor.rememberCompassSensor
import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Full interactive Qibla Compass Screen delegating data handling to QiblaContent.
 */
@Composable
fun QiblaScreen(
    onBack: () -> Unit,
    viewModel: AmaliyahViewModel? = null,
    modifier: Modifier = Modifier
) {
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
    val deltaAngle = ((compassState.heading - qiblaAzimuth + 540) % 360) - 180
    val isAligned = abs(deltaAngle) <= 2.5

    // Trigger subtle haptic pulse when device locks onto Qibla
    var wasAligned by remember { mutableStateOf(false) }
    LaunchedEffect(isAligned) {
        if (isAligned && !wasAligned) {
            haptic.performSuccess()
        }
        wasAligned = isAligned
    }

    QiblaContent(
        qiblaInfo = qiblaInfo,
        deviceHeading = compassState.heading,
        compassAccuracy = compassState.accuracy,
        isGpsActive = state.isGpsActive,
        isFetchingLocation = state.isFetchingLocation,
        onBack = onBack,
        onCalibrationClick = { showCalibrationSheet = true },
        onGpsClick = {
            if (locationProvider.hasLocationPermission()) {
                fetchGps()
            } else {
                requestPermissionAndFetch()
            }
        },
        modifier = modifier
    )

    // Modal Calibration Sheet
    if (showCalibrationSheet) {
        CompassCalibrationSheet(
            onDismiss = { showCalibrationSheet = false }
        )
    }
}
