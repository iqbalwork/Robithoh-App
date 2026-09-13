package com.iqbalwork.robithoh.feature.waktal

import com.iqbalwork.robithoh.feature.waktal.domain.HaversineDistance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HaversineDistanceTest {

    @Test
    fun testHaversineDistanceCalculationAccuracy() {
        // Bandung (-6.9175, 107.6191) to Jakarta (-6.1553, 106.7321)
        val latBandung = -6.9175
        val lonBandung = 107.6191
        val latJakarta = -6.1553
        val lonJakarta = 106.7321

        val distanceKm = HaversineDistance.calculateKm(latBandung, lonBandung, latJakarta, lonJakarta)

        // Expected distance ~128 km
        assertTrue(distanceKm in 120.0..135.0, "Distance should be ~128km but was $distanceKm")
    }

    @Test
    fun testSameLocationDistanceIsZero() {
        val lat = -7.1126
        val lon = 108.2045

        val distanceKm = HaversineDistance.calculateKm(lat, lon, lat, lon)

        assertEquals(0.0, distanceKm, 0.0001)
    }

    @Test
    fun testDistanceFormatting() {
        assertNull(HaversineDistance.formatDistance(null))
        assertNull(HaversineDistance.formatDistance(-1.0))
        assertEquals("450 m", HaversineDistance.formatDistance(0.45))
        assertEquals("14.2 km", HaversineDistance.formatDistance(14.23))
        assertEquals("128 km", HaversineDistance.formatDistance(128.4))
    }
}
