package com.iqbalwork.robithoh.core.location

import kotlinx.serialization.Serializable

@Serializable
data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val timezoneOffset: Double = 7.0,
    val isGps: Boolean = false
) {
    companion object {
        val DEFAULT = UserLocation(
            latitude = -7.1432,
            longitude = 108.2831,
            locationName = "Pesantren Sirnarasa Panjalu",
            timezoneOffset = 7.0,
            isGps = false
        )
    }
}
