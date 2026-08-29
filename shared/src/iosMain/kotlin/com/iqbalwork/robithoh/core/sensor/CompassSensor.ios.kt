@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.iqbalwork.robithoh.core.sensor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private class IosCompassDelegate(
    private val onHeadingUpdate: (heading: Float, accuracy: CompassAccuracy) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {

    private var smoothedSin = 0.0
    private var smoothedCos = 1.0
    private val alpha = 0.15

    override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
        val rawHeading = if (didUpdateHeading.trueHeading >= 0.0) {
            didUpdateHeading.trueHeading
        } else {
            didUpdateHeading.magneticHeading
        }

        if (rawHeading >= 0.0) {
            val rad = rawHeading * (PI / 180.0)
            smoothedSin = smoothedSin * (1.0 - alpha) + sin(rad) * alpha
            smoothedCos = smoothedCos * (1.0 - alpha) + cos(rad) * alpha

            val smoothedHeading = ((atan2(smoothedSin, smoothedCos) * (180.0 / PI)) + 360.0) % 360.0

            val accuracy = when {
                didUpdateHeading.headingAccuracy < 0.0 -> CompassAccuracy.UNRELIABLE
                didUpdateHeading.headingAccuracy <= 15.0 -> CompassAccuracy.HIGH
                didUpdateHeading.headingAccuracy <= 35.0 -> CompassAccuracy.MEDIUM
                else -> CompassAccuracy.LOW
            }

            onHeadingUpdate(smoothedHeading.toFloat(), accuracy)
        }
    }

    override fun locationManagerShouldDisplayHeadingCalibration(manager: CLLocationManager): Boolean {
        return true
    }
}

@Composable
actual fun rememberCompassSensor(): CompassState {
    var compassState by remember { mutableStateOf(CompassState()) }

    DisposableEffect(Unit) {
        if (!CLLocationManager.headingAvailable()) {
            compassState = CompassState(
                isAvailable = false,
                errorMessage = "Sensor kompas tidak didukung pada perangkat ini."
            )
            return@DisposableEffect onDispose {}
        }

        val locationManager = CLLocationManager()
        val delegate = IosCompassDelegate { heading, accuracy ->
            compassState = CompassState(
                heading = heading,
                accuracy = accuracy,
                isAvailable = true,
                errorMessage = null
            )
        }

        locationManager.delegate = delegate
        locationManager.headingFilter = 0.5 // Update every 0.5 degree
        locationManager.startUpdatingHeading()

        onDispose {
            locationManager.stopUpdatingHeading()
            locationManager.delegate = null
        }
    }

    return compassState
}
