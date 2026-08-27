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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
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

        // 1. Try to get best last known location immediately
        var bestLocation: Location? = null
        val providers = locationManager.getProviders(true)
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }

        if (bestLocation != null) {
            return locationToUserLocation(bestLocation)
        }

        // 2. If last known is null, request a single fresh location update
        return suspendCancellableCoroutine { continuation ->
            val listener = object : android.location.LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationManager.removeUpdates(this)
                    if (continuation.isActive) {
                        continuation.resume(locationToUserLocation(location))
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            val provider = when {
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                else -> LocationManager.PASSIVE_PROVIDER
            }

            try {
                locationManager.requestSingleUpdate(provider, listener, null)
                continuation.invokeOnCancellation {
                    locationManager.removeUpdates(listener)
                }
            } catch (e: Exception) {
                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }
        }
    }

    private fun locationToUserLocation(location: Location): UserLocation {
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

        return UserLocation(
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
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}
