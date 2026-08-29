@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.iqbalwork.robithoh.core.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.*
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.secondsFromGMT
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IosLocationProvider : LocationProvider {
    private val locationManager = CLLocationManager()

    override fun hasLocationPermission(): Boolean {
        val status = locationManager.authorizationStatus
        return status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                status == kCLAuthorizationStatusAuthorizedAlways
    }

    override suspend fun getCurrentLocation(): UserLocation? {
        if (!hasLocationPermission()) {
            val loc = locationManager.location
            if (loc != null) {
                return geocodeLocation(loc)
            }
            return null
        }

        val lastLoc = locationManager.location
        if (lastLoc != null) {
            return geocodeLocation(lastLoc)
        }

        return suspendCancellableCoroutine { continuation ->
            var resumed = false
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                    if (resumed) return
                    resumed = true
                    manager.stopUpdatingLocation()
                    manager.delegate = null
                    val last = didUpdateLocations.lastOrNull() as? CLLocation
                    if (last != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val userLoc = geocodeLocation(last)
                            if (continuation.isActive) {
                                continuation.resume(userLoc)
                            }
                        }
                    } else {
                        if (continuation.isActive) continuation.resume(null)
                    }
                }

                override fun locationManager(manager: CLLocationManager, didFailWithError: platform.Foundation.NSError) {
                    if (resumed) return
                    resumed = true
                    manager.stopUpdatingLocation()
                    manager.delegate = null
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }
            }

            locationManager.delegate = delegate
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.requestLocation()

            continuation.invokeOnCancellation {
                locationManager.stopUpdatingLocation()
                locationManager.delegate = null
            }
        }
    }

    private suspend fun geocodeLocation(location: CLLocation): UserLocation {
        val lat = location.coordinate.useContents { latitude }
        val lng = location.coordinate.useContents { longitude }
        val tzSeconds = NSTimeZone.localTimeZone.secondsFromGMT.toDouble()
        val tzOffset = tzSeconds / 3600.0

        val cityName = suspendCancellableCoroutine<String> { continuation ->
            val geocoder = CLGeocoder()
            geocoder.reverseGeocodeLocation(location) { placemarks, _ ->
                if (placemarks != null && placemarks.isNotEmpty()) {
                    val placemark = placemarks.first() as? CLPlacemark
                    val name = placemark?.locality
                        ?: placemark?.subAdministrativeArea
                        ?: placemark?.administrativeArea
                        ?: "Lokasi GPS"
                    continuation.resume(name)
                } else {
                    continuation.resume("Lokasi GPS (${((lat * 100).toInt() / 100.0)}, ${((lng * 100).toInt() / 100.0)})")
                }
            }
        }

        return UserLocation(
            latitude = lat,
            longitude = lng,
            locationName = cityName,
            timezoneOffset = tzOffset,
            isGps = true
        )
    }
}

private class IosLocationPermissionDelegate(
    private val onPermissionResult: (granted: Boolean) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {
    private var manager: CLLocationManager? = null
    private var isRequesting = false

    fun requestPermission() {
        val mgr = manager ?: CLLocationManager().also {
            manager = it
            it.delegate = this
        }
        val status = mgr.authorizationStatus
        if (status == kCLAuthorizationStatusAuthorizedWhenInUse ||
            status == kCLAuthorizationStatusAuthorizedAlways
        ) {
            onPermissionResult(true)
            return
        } else if (status == kCLAuthorizationStatusDenied ||
            status == kCLAuthorizationStatusRestricted
        ) {
            onPermissionResult(false)
            return
        }
        isRequesting = true
        mgr.requestWhenInUseAuthorization()
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        if (!isRequesting) return
        val status = manager.authorizationStatus
        if (status != kCLAuthorizationStatusNotDetermined) {
            isRequesting = false
            val granted = status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                    status == kCLAuthorizationStatusAuthorizedAlways
            onPermissionResult(granted)
        }
    }

    override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
        if (!isRequesting) return
        if (didChangeAuthorizationStatus != kCLAuthorizationStatusNotDetermined) {
            isRequesting = false
            val granted = didChangeAuthorizationStatus == kCLAuthorizationStatusAuthorizedWhenInUse ||
                    didChangeAuthorizationStatus == kCLAuthorizationStatusAuthorizedAlways
            onPermissionResult(granted)
        }
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
    val delegate = remember(onPermissionResult) {
        IosLocationPermissionDelegate(onPermissionResult)
    }
    return remember(delegate) {
        { delegate.requestPermission() }
    }
}
