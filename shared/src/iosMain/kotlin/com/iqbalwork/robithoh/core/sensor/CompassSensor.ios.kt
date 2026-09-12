@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.iqbalwork.robithoh.core.sensor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import platform.CoreLocation.CLDeviceOrientationPortrait
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.Foundation.NSError
import platform.darwin.NSObject

private class IosCompassDelegate(
    private val onHeadingUpdate: (heading: Float, accuracy: CompassAccuracy) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
        // Use trueHeading (True North) when location services provide magnetic declination,
        // otherwise fall back to magneticHeading (Magnetic North).
        val headingValue = if (didUpdateHeading.trueHeading >= 0.0) {
            didUpdateHeading.trueHeading
        } else {
            didUpdateHeading.magneticHeading
        }

        if (headingValue >= 0.0) {
            val accuracy = when {
                didUpdateHeading.headingAccuracy < 0.0 -> CompassAccuracy.UNRELIABLE
                didUpdateHeading.headingAccuracy <= 15.0 -> CompassAccuracy.HIGH
                didUpdateHeading.headingAccuracy <= 35.0 -> CompassAccuracy.MEDIUM
                else -> CompassAccuracy.LOW
            }

            onHeadingUpdate(headingValue.toFloat(), accuracy)
        }
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        // Location fix acquired, enabling CoreLocation to compute True North magnetic declination
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        // Ignore location errors for compass heading
    }

    override fun locationManagerShouldDisplayHeadingCalibration(manager: CLLocationManager): Boolean {
        return true
    }
}

@Composable
actual fun rememberCompassSensor(): CompassState {
    var compassState by remember { mutableStateOf(CompassState()) }

    val locationManager = remember { CLLocationManager() }
    val delegate = remember {
        IosCompassDelegate { heading, accuracy ->
            compassState = CompassState(
                heading = heading,
                accuracy = accuracy,
                isAvailable = true,
                errorMessage = null
            )
        }
    }

    DisposableEffect(locationManager, delegate) {
        if (!CLLocationManager.headingAvailable()) {
            compassState = CompassState(
                isAvailable = false,
                errorMessage = "Sensor kompas tidak didukung pada perangkat ini."
            )
            return@DisposableEffect onDispose {}
        }

        locationManager.delegate = delegate
        locationManager.headingFilter = 0.5 // Update every 0.5 degree
        locationManager.headingOrientation = CLDeviceOrientationPortrait

        // Request location permission if not determined so trueHeading (True North) can be calculated
        val status = locationManager.authorizationStatus
        if (status == kCLAuthorizationStatusNotDetermined) {
            locationManager.requestWhenInUseAuthorization()
        }

        // Request location fix if authorized so CoreLocation can compute magnetic declination for trueHeading
        if (status == kCLAuthorizationStatusAuthorizedWhenInUse || status == kCLAuthorizationStatusAuthorizedAlways) {
            locationManager.requestLocation()
        }

        locationManager.startUpdatingHeading()

        onDispose {
            locationManager.stopUpdatingHeading()
            locationManager.delegate = null
        }
    }

    return compassState
}
