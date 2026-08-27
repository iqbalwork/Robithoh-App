package com.iqbalwork.robithoh

import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerCalculationMethods
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerTimeAdjustments
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PrayerTimesEngineTest {

    private val calculator = PrayerTimesCalculator()

    @Test
    fun testDefaultKemenagCalculation() {
        val schedule = calculator.calculateSchedule(
            year = 2026,
            month = 8,
            day = 24,
            latitude = -7.1432,
            longitude = 108.2831,
            timezoneOffset = 7.0,
            locationName = "Pesantren Sirnarasa Panjalu",
            method = PrayerCalculationMethods.DEFAULT,
            adjustments = PrayerTimeAdjustments()
        )

        assertNotNull(schedule)
        assertEquals("Pesantren Sirnarasa Panjalu", schedule.locationName)
        assertEquals("WIB", schedule.timezone)
        assertTrue(schedule.subuh.isNotBlank())
        assertTrue(schedule.imsak.isNotBlank())
        assertTrue(schedule.dzuhur.isNotBlank())
        assertTrue(schedule.ashar.isNotBlank())
        assertTrue(schedule.maghrib.isNotBlank())
        assertTrue(schedule.isya.isNotBlank())
        assertTrue(schedule.tahajjud.isNotBlank())
        assertTrue(schedule.waktal.isNotBlank())
        assertTrue(schedule.isyroq.isNotBlank())
        assertTrue(schedule.dhuha.isNotBlank())
    }

    @Test
    fun testInternationalCalculationMethods() {
        val sirnarasaLoc = PrayerTimesCalculator.DEFAULT_LOCATION
        val kemenag = calculator.calculateSchedule(
            year = 2026, month = 8, day = 24,
            latitude = sirnarasaLoc.latitude,
            longitude = sirnarasaLoc.longitude,
            timezoneOffset = sirnarasaLoc.timezoneOffset,
            method = PrayerCalculationMethods.findById("KEMENAG")
        )

        val mwl = calculator.calculateSchedule(
            year = 2026, month = 8, day = 24,
            latitude = sirnarasaLoc.latitude,
            longitude = sirnarasaLoc.longitude,
            timezoneOffset = sirnarasaLoc.timezoneOffset,
            method = PrayerCalculationMethods.findById("MUSLIM_WORLD_LEAGUE")
        )

        val ummAlQura = calculator.calculateSchedule(
            year = 2026, month = 8, day = 24,
            latitude = sirnarasaLoc.latitude,
            longitude = sirnarasaLoc.longitude,
            timezoneOffset = sirnarasaLoc.timezoneOffset,
            method = PrayerCalculationMethods.findById("UMM_AL_QURA")
        )

        assertNotNull(kemenag)
        assertNotNull(mwl)
        assertNotNull(ummAlQura)
        assertEquals("Kementrian Agama Indonesia", kemenag.methodName)
        assertEquals("Muslim World League", mwl.methodName)
        assertEquals("Umm al-Qura", ummAlQura.methodName)
    }

    @Test
    fun testPrayerAdjustments() {
        val sirnarasaLoc = PrayerTimesCalculator.DEFAULT_LOCATION
        val baseSchedule = calculator.calculateSchedule(
            year = 2026, month = 8, day = 24,
            latitude = sirnarasaLoc.latitude,
            longitude = sirnarasaLoc.longitude,
            timezoneOffset = sirnarasaLoc.timezoneOffset,
            adjustments = PrayerTimeAdjustments()
        )

        val adjustedSchedule = calculator.calculateSchedule(
            year = 2026, month = 8, day = 24,
            latitude = sirnarasaLoc.latitude,
            longitude = sirnarasaLoc.longitude,
            timezoneOffset = sirnarasaLoc.timezoneOffset,
            adjustments = PrayerTimeAdjustments(
                imsak = 2,
                subuh = 2,
                dzuhur = 1,
                ashar = -1,
                maghrib = 3,
                isya = 2
            )
        )

        assertNotNull(baseSchedule)
        assertNotNull(adjustedSchedule)

        fun parseMin(timeStr: String): Int {
            val parts = timeStr.split(":")
            return parts[0].toInt() * 60 + parts[1].toInt()
        }

        val baseSubuhMin = parseMin(baseSchedule.subuh)
        val adjSubuhMin = parseMin(adjustedSchedule.subuh)
        assertEquals(baseSubuhMin + 2, adjSubuhMin)

        val baseDzuhurMin = parseMin(baseSchedule.dzuhur)
        val adjDzuhurMin = parseMin(adjustedSchedule.dzuhur)
        assertEquals(baseDzuhurMin + 1, adjDzuhurMin)

        val baseAsharMin = parseMin(baseSchedule.ashar)
        val adjAsharMin = parseMin(adjustedSchedule.ashar)
        assertEquals(baseAsharMin - 1, adjAsharMin)
    }

    @Test
    fun testQiblaCalculation() {
        val qibla = calculator.calculateQibla(
            latitude = -7.1432,
            longitude = 108.2831,
            cityName = "Pesantren Sirnarasa Panjalu"
        )

        assertTrue(qibla.directionDegrees > 290.0 && qibla.directionDegrees < 300.0)
        assertTrue(qibla.distanceKm > 7000.0 && qibla.distanceKm < 9000.0)
        assertEquals("Pesantren Sirnarasa Panjalu", qibla.cityName)
    }

    @Test
    fun testCustomGpsLocationCalculation() {
        // Test custom GPS for Jakarta
        val jakartaGps = com.iqbalwork.robithoh.core.location.UserLocation(
            latitude = -6.2088,
            longitude = 106.8456,
            locationName = "Jakarta Pusat",
            timezoneOffset = 7.0,
            isGps = true
        )

        val schedule = calculator.calculateSchedule(
            year = 2026,
            month = 8,
            day = 24,
            latitude = jakartaGps.latitude,
            longitude = jakartaGps.longitude,
            timezoneOffset = jakartaGps.timezoneOffset,
            locationName = jakartaGps.locationName
        )

        assertNotNull(schedule)
        assertEquals("Jakarta Pusat", schedule.locationName)
        assertEquals("WIB", schedule.timezone)
        assertTrue(schedule.subuh.isNotBlank())
        assertTrue(schedule.dzuhur.isNotBlank())

        val qibla = calculator.calculateQibla(
            latitude = jakartaGps.latitude,
            longitude = jakartaGps.longitude,
            cityName = jakartaGps.locationName
        )
        assertTrue(qibla.directionDegrees > 290.0 && qibla.directionDegrees < 300.0)
    }

    @Test
    fun testComputeNextPrayerAt1724BeforeMaghrib() {
        val schedule = calculator.calculateSchedule(
            year = 2026,
            month = 8,
            day = 24,
            latitude = -7.1432,
            longitude = 108.2831,
            timezoneOffset = 7.0,
            locationName = "Pesantren Sirnarasa Panjalu"
        )

        // At 17:24 (before Maghrib ~17:47), next prayer must be Maghrib!
        val countdown = calculator.computeNextPrayer(
            schedule = schedule,
            currentHour = 17,
            currentMinute = 24,
            currentSecond = 0
        )

        assertEquals("Maghrib", countdown.nextPrayerName)
        assertEquals(schedule.maghrib, countdown.nextPrayerTime)
        assertTrue(countdown.totalRemainingSeconds > 0)
        assertTrue(countdown.remainingMinutes >= 20)
    }

    @Test
    fun testBandungKemenagAccuracy27August2026() {
        val schedule = calculator.calculateSchedule(
            year = 2026,
            month = 8,
            day = 27,
            latitude = -6.9175,
            longitude = 107.6191,
            timezoneOffset = 7.0,
            locationName = "Kota Bandung",
            method = PrayerCalculationMethods.findById("KEMENAG")
        )

        assertNotNull(schedule)
        println("=== BANDUNG 27 AUG 2026 ===")
        println("Imsak: ${schedule.imsak}")
        println("Subuh: ${schedule.subuh}")
        println("Syuruq: ${schedule.isyroq}")
        println("Dzuhur: ${schedule.dzuhur}")
        println("Ashar: ${schedule.ashar}")
        println("Maghrib: ${schedule.maghrib}")
        println("Isya: ${schedule.isya}")

        assertEquals("04:37", schedule.subuh)
        assertEquals("11:55", schedule.dzuhur)
        assertEquals("15:14", schedule.ashar)
        assertEquals("17:57", schedule.maghrib)
        assertEquals("19:02", schedule.isya)
        assertEquals("Kamis, 27 Agustus 2026", schedule.dateFormatted)
        assertTrue(schedule.hijriDateFormatted.isNotBlank())
    }

    @Test
    fun testDateShiftAndHumanReadableFormatting() {
        val (y, m, d) = com.iqbalwork.robithoh.core.datetime.shiftDate(2026, 8, 27, 1)
        assertEquals(2026, y)
        assertEquals(8, m)
        assertEquals(28, d)

        val dateStr = com.iqbalwork.robithoh.core.datetime.formatIndonesianDate(y, m, d)
        assertEquals("Jumat, 28 Agustus 2026", dateStr)

        val (prevY, prevM, prevD) = com.iqbalwork.robithoh.core.datetime.shiftDate(2026, 8, 27, -1)
        assertEquals(2026, prevY)
        assertEquals(8, prevM)
        assertEquals(26, prevD)

        val prevDateStr = com.iqbalwork.robithoh.core.datetime.formatIndonesianDate(prevY, prevM, prevD)
        assertEquals("Rabu, 26 Agustus 2026", prevDateStr)
    }
}
