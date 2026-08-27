package com.iqbalwork.robithoh.core.location

import androidx.compose.runtime.Composable

interface LocationProvider {
    suspend fun getCurrentLocation(): UserLocation?
    fun hasLocationPermission(): Boolean
}

@Composable
expect fun rememberLocationProvider(): LocationProvider

@Composable
expect fun rememberLocationPermissionLauncher(
    onPermissionResult: (granted: Boolean) -> Unit
): () -> Unit
