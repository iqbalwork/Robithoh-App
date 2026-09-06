package com.iqbalwork.robithoh.feature.amaliyah.domain

import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Madhab
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.data.DateComponents
import com.iqbalwork.robithoh.feature.amaliyah.model.LocationPreset
import com.iqbalwork.robithoh.feature.amaliyah.model.NextPrayerCountdown
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerCalculationMethodItem
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerCalculationMethods
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerTimeAdjustments
import com.iqbalwork.robithoh.feature.amaliyah.model.QiblaInfo
import kotlinx.datetime.Instant
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Offline Astronomical Prayer Times & Tasawuf Schedule Calculator Engine.
 * Powered by adhan-kotlin (com.batoulapps.adhan:adhan2) for high-precision
 * international calculation methods and manual prayer adjustments, combined with
 * MTQN Suryalaya Sirnarasa PPKN III Tasawuf specific schedules (Tahajjud, Waktal, Isyroq, Dhuha).
 */
class PrayerTimesCalculator {

    companion object {
        const val KAABA_LATITUDE = 21.422487
        const val KAABA_LONGITUDE = 39.826206
        const val EARTH_RADIUS_KM = 6371.0

        val PRESET_LOCATIONS = listOf(
            LocationPreset(
                name = "Pesantren Sirnarasa Panjalu",
                latitude = -7.1432,
                longitude = 108.2831,
                timezoneOffset = 7.0,
                province = "Ciamis, Jawa Barat"
            ),
            LocationPreset(
                name = "Pontren Suryalaya",
                latitude = -7.1581,
                longitude = 108.2169,
                timezoneOffset = 7.0,
                province = "Tasikmalaya, Jawa Barat"
            ),
            LocationPreset(
                name = "Jakarta Pusat",
                latitude = -6.2088,
                longitude = 106.8456,
                timezoneOffset = 7.0,
                province = "DKI Jakarta"
            ),
            LocationPreset(
                name = "Bandung",
                latitude = -6.9175,
                longitude = 107.6191,
                timezoneOffset = 7.0,
                province = "Jawa Barat"
            ),
            LocationPreset(
                name = "Ciamis",
                latitude = -7.3256,
                longitude = 108.3533,
                timezoneOffset = 7.0,
                province = "Jawa Barat"
            ),
            LocationPreset(
                name = "Tasikmalaya",
                latitude = -7.3274,
                longitude = 108.2207,
                timezoneOffset = 7.0,
                province = "Jawa Barat"
            ),
            LocationPreset(
                name = "Yogyakarta",
                latitude = -7.7956,
                longitude = 110.3695,
                timezoneOffset = 7.0,
                province = "DI Yogyakarta"
            ),
            LocationPreset(
                name = "Surabaya",
                latitude = -7.2575,
                longitude = 112.7521,
                timezoneOffset = 7.0,
                province = "Jawa Timur"
            ),
            LocationPreset(
                name = "Medan",
                latitude = 3.5952,
                longitude = 98.6722,
                timezoneOffset = 7.0,
                province = "Sumatera Utara"
            ),
            LocationPreset(
                name = "Makassar",
                latitude = -5.1477,
                longitude = 119.4327,
                timezoneOffset = 8.0,
                province = "Sulawesi Selatan"
            )
        )

        val DEFAULT_LOCATION = PRESET_LOCATIONS[0]
    }

    /**
     * Calculate daily prayer schedule for a specific date, coordinates, calculation method, and adjustments.
     */
    fun calculateSchedule(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double,
        timezoneOffset: Double,
        locationName: String = "Sirnarasa Panjalu",
        method: PrayerCalculationMethodItem = PrayerCalculationMethods.DEFAULT,
        adjustments: PrayerTimeAdjustments = PrayerTimeAdjustments(),
        madhab: Madhab = Madhab.SHAFI
    ): PrayerSchedule {
        val coordinates = Coordinates(latitude, longitude)
        val dateComponents = DateComponents(year, month, day)
        val calculationParameters = method.toCalculationParameters(madhab).copy(
            prayerAdjustments = adjustments.toAdhanPrayerAdjustments()
        )

        val prayerTimes = PrayerTimes(coordinates, dateComponents, calculationParameters)

        // Formatted prayer strings
        val subuhFormatted = formatInstantToLocalTime(prayerTimes.fajr, timezoneOffset)
        val terbitFormatted = formatInstantToLocalTime(prayerTimes.sunrise, timezoneOffset)
        val dzuhurFormatted = formatInstantToLocalTime(prayerTimes.dhuhr, timezoneOffset)
        val asharFormatted = formatInstantToLocalTime(prayerTimes.asr, timezoneOffset)
        val maghribFormatted = formatInstantToLocalTime(prayerTimes.maghrib, timezoneOffset)
        val isyaFormatted = formatInstantToLocalTime(prayerTimes.isha, timezoneOffset)

        // Imsak: 10 minutes before Fajr + user imsak adjustment
        val imsakEpoch = (prayerTimes.fajr.epochSeconds - 600) + (adjustments.imsak * 60)
        val imsakFormatted = formatEpochToLocalTime(imsakEpoch, timezoneOffset)

        // Isyroq: 15 minutes after Sunrise
        val isyroqEpoch = prayerTimes.sunrise.epochSeconds + (15 * 60)
        val isyroqFormatted = formatEpochToLocalTime(isyroqEpoch, timezoneOffset)

        // Dhuha: 25 minutes after Sunrise
        val dhuhaEpoch = prayerTimes.sunrise.epochSeconds + (25 * 60)
        val dhuhaFormatted = formatEpochToLocalTime(dhuhaEpoch, timezoneOffset)

        // Tasawuf Schedule: Waktu Malam / Tahajjud (1/3 akhir malam)
        val nightSeconds = (prayerTimes.fajr.epochSeconds + 86400 - prayerTimes.maghrib.epochSeconds) % 86400
        val tahajjudEpoch = prayerTimes.maghrib.epochSeconds + ((2.0 / 3.0) * nightSeconds).toLong()
        val tahajjudFormatted = formatEpochToLocalTime(tahajjudEpoch, timezoneOffset)

        // Waktal (Wirid Khusus MTQN Suryalaya Sirnarasa PPKN III: 1.5 jam sebelum Subuh)
        val waktalEpoch = prayerTimes.fajr.epochSeconds - (90 * 60)
        val waktalFormatted = formatEpochToLocalTime(waktalEpoch, timezoneOffset)

        val tzLabel = when (timezoneOffset.toInt()) {
            7 -> "WIB"
            8 -> "WITA"
            9 -> "WIT"
            else -> "UTC+${timezoneOffset.toInt()}"
        }

        val indonesianDate = com.iqbalwork.robithoh.core.datetime.formatIndonesianDate(year, month, day)
        val hijriDate = com.iqbalwork.robithoh.core.datetime.getHijriDateFormatted(year, month, day)

        return PrayerSchedule(
            dateFormatted = indonesianDate,
            hijriDateFormatted = hijriDate,
            imsak = imsakFormatted,
            subuh = subuhFormatted,
            isyroq = isyroqFormatted,
            dhuha = dhuhaFormatted,
            dzuhur = dzuhurFormatted,
            ashar = asharFormatted,
            maghrib = maghribFormatted,
            isya = isyaFormatted,
            tahajjud = tahajjudFormatted,
            waktal = waktalFormatted,
            timezone = tzLabel,
            locationName = locationName,
            methodName = method.name
        )
    }

    /**
     * Compute Great Circle Qibla Direction (Azimuth degrees clockwise from True North)
     * and distance to Kaaba in kilometers.
     */
    fun calculateQibla(
        latitude: Double,
        longitude: Double,
        cityName: String = "Sirnarasa Panjalu"
    ): QiblaInfo {
        val userLatRad = degToRad(latitude)
        val kaabaLatRad = degToRad(KAABA_LATITUDE)
        val deltaLonRad = degToRad(KAABA_LONGITUDE - longitude)

        val y = sin(deltaLonRad)
        val x = cos(userLatRad) * tan(kaabaLatRad) - sin(userLatRad) * cos(deltaLonRad)
        var qiblaDegrees = radToDeg(atan2(y, x))
        qiblaDegrees = (qiblaDegrees + 360.0) % 360.0

        // Distance via Haversine
        val deltaLatRad = degToRad(KAABA_LATITUDE - latitude)
        val a = sin(deltaLatRad / 2.0).pow(2.0) +
                cos(userLatRad) * cos(kaabaLatRad) * sin(deltaLonRad / 2.0).pow(2.0)
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        val distanceKm = EARTH_RADIUS_KM * c

        val heading = when {
            qiblaDegrees in 280.0..305.0 -> "Barat Laut (WNW)"
            qiblaDegrees in 260.0..280.0 -> "Barat (W)"
            qiblaDegrees in 305.0..340.0 -> "Barat Laut (NW)"
            else -> "${qiblaDegrees.roundToInt()}°"
        }

        return QiblaInfo(
            directionDegrees = ((qiblaDegrees * 100.0).roundToInt() / 100.0),
            distanceKm = ((distanceKm * 10.0).roundToInt() / 10.0),
            cityName = cityName,
            latitude = latitude,
            longitude = longitude,
            compassHeading = heading
        )
    }

    /**
     * Compute next prayer countdown given current hour, minute, second and daily schedule.
     */
    fun computeNextPrayer(
        schedule: PrayerSchedule,
        currentHour: Int,
        currentMinute: Int,
        currentSecond: Int
    ): NextPrayerCountdown {
        val currentTotalSeconds = currentHour * 3600L + currentMinute * 60L + currentSecond

        val prayerEntries = listOf(
            "Subuh" to parseTimeToSeconds(schedule.subuh),
            "Dzuhur" to parseTimeToSeconds(schedule.dzuhur),
            "Ashar" to parseTimeToSeconds(schedule.ashar),
            "Maghrib" to parseTimeToSeconds(schedule.maghrib),
            "Isya" to parseTimeToSeconds(schedule.isya)
        ).sortedBy { it.second }

        var nextName = prayerEntries.first().first
        var nextTimeSec = prayerEntries.first().second
        var prevTimeSec = prayerEntries.last().second

        var found = false
        for (i in prayerEntries.indices) {
            val (name, timeSec) = prayerEntries[i]
            if (timeSec > currentTotalSeconds) {
                nextName = name
                nextTimeSec = timeSec
                prevTimeSec = if (i > 0) prayerEntries[i - 1].second else prayerEntries.last().second
                found = true
                break
            }
        }

        val remainingSec = if (found) {
            nextTimeSec - currentTotalSeconds
        } else {
            val first = prayerEntries.first()
            nextName = first.first
            nextTimeSec = first.second
            (86400L - currentTotalSeconds) + nextTimeSec
        }

        val totalInterval = if (found && prevTimeSec < nextTimeSec) {
            nextTimeSec - prevTimeSec
        } else {
            86400L - prevTimeSec + nextTimeSec
        }.coerceAtLeast(1L)

        val elapsed = (totalInterval - remainingSec).coerceAtLeast(0L)
        val progress = (elapsed.toFloat() / totalInterval.toFloat()).coerceIn(0f, 1f)

        val remHours = remainingSec / 3600L
        val remMinutes = (remainingSec % 3600L) / 60L
        val remSeconds = remainingSec % 60L

        val formattedNextTime = when (nextName) {
            "Subuh" -> schedule.subuh
            "Dzuhur" -> schedule.dzuhur
            "Ashar" -> schedule.ashar
            "Maghrib" -> schedule.maghrib
            "Isya" -> schedule.isya
            else -> schedule.subuh
        }

        return NextPrayerCountdown(
            nextPrayerName = nextName,
            nextPrayerTime = formattedNextTime,
            remainingHours = remHours,
            remainingMinutes = remMinutes,
            remainingSeconds = remSeconds,
            totalRemainingSeconds = remainingSec,
            progressFraction = progress,
            isPrayerTimeNow = remainingSec <= 60L
        )
    }

    private fun formatInstantToLocalTime(instant: Instant, timezoneOffset: Double): String {
        return formatEpochToLocalTime(instant.epochSeconds, timezoneOffset)
    }

    private fun formatEpochToLocalTime(epochSeconds: Long, timezoneOffset: Double): String {
        val offsetSeconds = (timezoneOffset * 3600.0).roundToLong()
        val localSeconds = epochSeconds + offsetSeconds
        val secondsInDay = ((localSeconds % 86400L) + 86400L) % 86400L
        val hour = (secondsInDay / 3600L).toInt()
        val minute = ((secondsInDay % 3600L) / 60L).toInt()
        return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
    }

    private fun parseTimeToSeconds(timeStr: String): Long {
        val parts = timeStr.split(":")
        if (parts.size < 2) return 0L
        val h = parts[0].trim().toLongOrNull() ?: 0L
        val m = parts[1].trim().toLongOrNull() ?: 0L
        return h * 3600L + m * 60L
    }

    private fun degToRad(deg: Double): Double = deg * (PI / 180.0)
    private fun radToDeg(rad: Double): Double = rad * (180.0 / PI)
}
