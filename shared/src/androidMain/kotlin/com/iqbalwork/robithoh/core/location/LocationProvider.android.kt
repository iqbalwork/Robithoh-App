package com.iqbalwork.robithoh.core.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.resume

class AndroidLocationProvider(private val context: Context) : LocationProvider {

    override fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineLocation || coarseLocation
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): UserLocation? {
        if (!hasLocationPermission()) return null

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return null

        // 1. Try to get best last known location immediately from all available providers
        var bestLocation: Location? = null
        val providers = locationManager.getProviders(true)
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }

        // 2. Request a fresh location update with timeout (6s)
        val freshLocation = withTimeoutOrNull(6000L) {
            suspendCancellableCoroutine<Location?> { continuation ->
                val listener = object : android.location.LocationListener {
                    override fun onLocationChanged(location: Location) {
                        try {
                            locationManager.removeUpdates(this)
                        } catch (_: Exception) {}
                        if (continuation.isActive) {
                            continuation.resume(location)
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                val mainLooper = Looper.getMainLooper()
                var requestedAny = false

                // Try NETWORK_PROVIDER first for rapid indoor/wifi fix
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    try {
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, mainLooper)
                        requestedAny = true
                    } catch (_: Exception) {}
                }

                // Also listen to GPS_PROVIDER for high accuracy
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try {
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, mainLooper)
                        requestedAny = true
                    } catch (_: Exception) {}
                }

                if (!requestedAny && locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                    try {
                        locationManager.requestSingleUpdate(LocationManager.PASSIVE_PROVIDER, listener, mainLooper)
                        requestedAny = true
                    } catch (_: Exception) {}
                }

                if (!requestedAny) {
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }

                continuation.invokeOnCancellation {
                    try {
                        locationManager.removeUpdates(listener)
                    } catch (_: Exception) {}
                }
            }
        }

        val resolvedLocation = freshLocation ?: bestLocation ?: return null
        return locationToUserLocation(resolvedLocation)
    }

    private suspend fun locationToUserLocation(location: Location): UserLocation = withContext(Dispatchers.IO) {
        val lat = location.latitude
        val lng = location.longitude
        val tzOffset = TimeZone.getDefault().rawOffset.toDouble() / 3600000.0

        val cityName = try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                addr.subAdminArea ?: addr.locality ?: addr.adminArea ?: "Lokasi GPS"
            } else {
                "Lokasi GPS (${formatCoord(lat)}, ${formatCoord(lng)})"
            }
        } catch (e: Exception) {
            "Lokasi GPS (${formatCoord(lat)}, ${formatCoord(lng)})"
        }

        UserLocation(
            latitude = lat,
            longitude = lng,
            locationName = cityName,
            timezoneOffset = tzOffset,
            isGps = true
        )
    }

    private fun formatCoord(value: Double): String {
        return ((value * 100).toInt() / 100.0).toString()
    }
}

@Composable
actual fun rememberLocationProvider(): LocationProvider {
    val context = LocalContext.current
    return remember(context) { AndroidLocationProvider(context) }
}

@Composable
actual fun rememberLocationPermissionLauncher(
    onPermissionResult: (granted: Boolean) -> Unit
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        onPermissionResult(granted)
    }

    return {
        val perms = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            perms.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        launcher.launch(perms.toTypedArray())
    }
}
