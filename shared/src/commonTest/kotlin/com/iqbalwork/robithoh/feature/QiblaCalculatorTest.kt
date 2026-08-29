package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QiblaCalculatorTest {

    private val calculator = PrayerTimesCalculator()

    @Test
    fun testSirnarasaPanjaluQiblaCalculation() {
        // Sirnarasa Panjalu: -7.1432, 108.2831
        val qibla = calculator.calculateQibla(
            latitude = -7.1432,
            longitude = 108.2831,
            cityName = "Pesantren Sirnarasa Panjalu"
        )

        assertTrue(qibla.directionDegrees in 294.0..296.0, "Expected ~295 deg, got ${qibla.directionDegrees}")
        assertTrue(qibla.distanceKm > 7000.0 && qibla.distanceKm < 9000.0, "Expected ~8000 km, got ${qibla.distanceKm}")
        assertEquals("Barat Laut (WNW)", qibla.compassHeading)
        assertEquals("Pesantren Sirnarasa Panjalu", qibla.cityName)
    }

    @Test
    fun testJakartaQiblaCalculation() {
        // Jakarta Pusat: -6.2088, 106.8456
        val qibla = calculator.calculateQibla(
            latitude = -6.2088,
            longitude = 106.8456,
            cityName = "Jakarta Pusat"
        )

        assertTrue(qibla.directionDegrees in 294.0..296.0, "Expected ~295 deg, got ${qibla.directionDegrees}")
        assertTrue(qibla.distanceKm > 7000.0 && qibla.distanceKm < 9000.0, "Distance check failed: ${qibla.distanceKm}")
    }
}
