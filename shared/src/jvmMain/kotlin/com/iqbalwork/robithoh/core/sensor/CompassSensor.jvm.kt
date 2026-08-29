package com.iqbalwork.robithoh.core.sensor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberCompassSensor(): CompassState {
    return remember {
        CompassState(
            heading = 0f,
            accuracy = CompassAccuracy.HIGH,
            isAvailable = true,
            errorMessage = null
        )
    }
}
