package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import kotlin.test.*

class PrayerTimesCalculatorTest {

    private val calculator = PrayerTimesCalculator()

    @Test
    fun testSirnarasaPrayerScheduleCalculation() {
        val schedule = calculator.calculateSchedule(
            year = 2026,
            month = 8,
            day = 24,
            latitude = -7.1432,
            longitude = 108.2831,
            timezoneOffset = 7.0,
            locationName = "Pesantren Sirnarasa Panjalu"
        )

        assertNotNull(schedule)
        assertEquals("2026-08-24", schedule.dateFormatted)
        assertEquals("WIB", schedule.timezone)
        assertEquals("Pesantren Sirnarasa Panjalu", schedule.locationName)

        // Subuh should be in morning range (around 04:30 - 05:00 WIB)
        assertTrue(schedule.subuh.startsWith("04:") || schedule.subuh.startsWith("05:"))
        // Imsak should be ~10 mins before Subuh
        assertTrue(schedule.imsak.isNotBlank())
        // Dzuhur should be around midday (11:45 - 12:15 WIB)
        assertTrue(schedule.dzuhur.startsWith("11:") || schedule.dzuhur.startsWith("12:"))
        // Ashar should be afternoon (15:00 - 15:30 WIB)
        assertTrue(schedule.ashar.startsWith("15:"))
        // Maghrib should be dusk (17:40 - 18:15 WIB)
        assertTrue(schedule.maghrib.startsWith("17:") || schedule.maghrib.startsWith("18:"))
        // Isya should be night (18:50 - 19:30 WIB)
        assertTrue(schedule.isya.startsWith("18:") || schedule.isya.startsWith("19:"))

        // Tasawuf Schedule (Tahajjud & Waktal)
        assertTrue(schedule.tahajjud.isNotBlank())
        assertTrue(schedule.waktal.isNotBlank())
    }

    @Test
    fun testQiblaCalculationForSirnarasa() {
        val qibla = calculator.calculateQibla(
            latitude = -7.1432,
            longitude = 108.2831,
            cityName = "Pesantren Sirnarasa Panjalu"
        )

        assertNotNull(qibla)
        // From Java, Indonesia, Qibla angle is roughly 294.0° - 296.0° from True North
        assertTrue(qibla.directionDegrees in 290.0..300.0, "Qibla angle should be ~295°, was ${qibla.directionDegrees}")
        // Distance to Makkah from Java is approx 7800 - 8500 km
        assertTrue(qibla.distanceKm in 7500.0..8800.0, "Distance should be ~8000 km, was ${qibla.distanceKm}")
        assertEquals("Pesantren Sirnarasa Panjalu", qibla.cityName)
        assertTrue(qibla.compassHeading.contains("Barat Laut"))
    }

    @Test
    fun testNextPrayerCountdownComputation() {
        val schedule = calculator.calculateSchedule(
            year = 2026,
            month = 8,
            day = 24,
            latitude = -7.1432,
            longitude = 108.2831,
            timezoneOffset = 7.0
        )

        // Midday (11:00) -> Next prayer should be Dzuhur
        val countdown = calculator.computeNextPrayer(
            schedule = schedule,
            currentHour = 11,
            currentMinute = 0,
            currentSecond = 0
        )

        assertEquals("Dzuhur", countdown.nextPrayerName)
        assertTrue(countdown.totalRemainingSeconds > 0)
        assertTrue(countdown.progressFraction in 0f..1f)
    }

    @Test
    fun testPresetLocations() {
        val presets = PrayerTimesCalculator.PRESET_LOCATIONS
        assertTrue(presets.isNotEmpty())
        assertTrue(presets.any { it.name.contains("Sirnarasa") })
        assertTrue(presets.any { it.name.contains("Jakarta") })
        assertTrue(presets.any { it.name.contains("Bandung") })
        assertTrue(presets.any { it.name.contains("Suryalaya") })
    }
}
