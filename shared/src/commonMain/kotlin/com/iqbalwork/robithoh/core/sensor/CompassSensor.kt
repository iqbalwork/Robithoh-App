package com.iqbalwork.robithoh.core.sensor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Sensor compass accuracy status according to Android SensorManager / iOS CoreLocation.
 */
enum class CompassAccuracy {
    HIGH,
    MEDIUM,
    LOW,
    UNRELIABLE,
    UNKNOWN
}

/**
 * Compass state emitting live azimuth heading, sensor status, and accuracy.
 */
@Immutable
data class CompassState(
    val heading: Float = 0f,
    val accuracy: CompassAccuracy = CompassAccuracy.UNKNOWN,
    val isAvailable: Boolean = true,
    val errorMessage: String? = null
)

/**
 * Composable multiplatform hook to observe the device's compass heading in degrees (0° - 360°).
 */
@Composable
expect fun rememberCompassSensor(): CompassState
