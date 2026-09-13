package com.iqbalwork.robithoh.feature.waktal.domain

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

object HaversineDistance {
    private const val EARTH_RADIUS_KM = 6371.0088

    fun calculateKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = (lat2 - lat1) * (PI / 180.0)
        val dLon = (lon2 - lon1) * (PI / 180.0)
        val rLat1 = lat1 * (PI / 180.0)
        val rLat2 = lat2 * (PI / 180.0)

        val a = sin(dLat / 2.0).let { it * it } +
            cos(rLat1) * cos(rLat2) *
            sin(dLon / 2.0).let { it * it }
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        return EARTH_RADIUS_KM * c
    }

    fun formatDistance(distanceKm: Double?): String? {
        if (distanceKm == null || distanceKm < 0.0) return null
        return when {
            distanceKm < 1.0 -> "${round(distanceKm * 1000.0).toInt()} m"
            distanceKm < 100.0 -> {
                val rounded = round(distanceKm * 10.0) / 10.0
                "$rounded km"
            }
            else -> "${round(distanceKm).toInt()} km"
        }
    }
}
