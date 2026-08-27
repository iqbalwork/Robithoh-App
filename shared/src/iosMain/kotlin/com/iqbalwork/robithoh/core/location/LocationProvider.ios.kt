package com.iqbalwork.robithoh.core.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class IosLocationProvider : LocationProvider {
    override suspend fun getCurrentLocation(): UserLocation? {
        return UserLocation.DEFAULT
    }

    override fun hasLocationPermission(): Boolean {
        return true
    }
}

@Composable
actual fun rememberLocationProvider(): LocationProvider {
    return remember { IosLocationProvider() }
}

@Composable
actual fun rememberLocationPermissionLauncher(
    onPermissionResult: (granted: Boolean) -> Unit
): () -> Unit {
    return {
        onPermissionResult(true)
    }
}
