package com.iqbalwork.robithoh.feature.amaliyah.domain

import com.iqbalwork.robithoh.feature.amaliyah.model.LocationPreset
import com.iqbalwork.robithoh.feature.amaliyah.model.NextPrayerCountdown
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule
import com.iqbalwork.robithoh.feature.amaliyah.model.QiblaInfo
import kotlin.math.*

/**
 * Offline Astronomical Prayer Times & Tasawuf Schedule Calculator Engine.
 * Implements high-precision solar geometry calculations (declination, equation of time,
 * hour angle) per Kemenag / Islamic astronomical standards, plus TQN Sirnarasa Tasawuf
 * specific schedules (Waktu Malam / Tahajjud, Isyroq, Waktal).
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
     * Calculate daily prayer schedule for a specific date and geographic coordinate.
     */
    fun calculateSchedule(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double,
        timezoneOffset: Double,
        locationName: String = "Sirnarasa Panjalu"
    ): PrayerSchedule {
        val julianDay = getJulianDay(year, month, day)
        val d = julianDay - 2451545.0

        // Solar coordinates
        val meanAnomaly = fixAngle(357.529 + 0.98560028 * d)
        val meanLongitude = fixAngle(280.459 + 0.98564736 * d)
        val apparentLongitude = fixAngle(
            meanLongitude + 1.915 * sin(degToRad(meanAnomaly)) + 0.020 * sin(degToRad(2 * meanAnomaly))
        )
        val obliquity = 23.439 - 0.00000036 * d
        val solarDeclination = radToDeg(
            asin(sin(degToRad(obliquity)) * sin(degToRad(apparentLongitude)))
        )

        // Equation of Time (EoT) in minutes
        val y = tan(degToRad(obliquity) / 2.0).pow(2.0)
        val ecc = 0.01671
        val eqTimeMinutes = 4.0 * radToDeg(
            y * sin(2.0 * degToRad(meanLongitude))
                    - 2.0 * ecc * sin(degToRad(meanAnomaly))
                    + 4.0 * ecc * y * sin(degToRad(meanAnomaly)) * cos(2.0 * degToRad(meanLongitude))
                    - 0.5 * y * y * sin(4.0 * degToRad(meanLongitude))
                    - 1.25 * ecc * ecc * sin(2.0 * degToRad(meanAnomaly))
        )

        // Solar Transit (Dzuhur standard) in decimal hours
        val solarTransit = 12.0 + timezoneOffset - (longitude / 15.0) - (eqTimeMinutes / 60.0)
        val ihtiyatHours = 2.0 / 60.0 // 2 minutes ihtiyat (safety buffer)

        // Fajr / Shubuh angle: -20.0 degrees (Kemenag standard)
        val fajrHourAngle = computeHourAngle(-20.0, latitude, solarDeclination)
        val rawFajr = solarTransit - fajrHourAngle
        val fajrTime = rawFajr + ihtiyatHours
        val imsakTime = fajrTime - (10.0 / 60.0) // 10 minutes before Fajr

        // Sunrise (Syuruq): -0.833 degrees
        val sunriseHourAngle = computeHourAngle(-0.833, latitude, solarDeclination)
        val sunriseTime = solarTransit - sunriseHourAngle
        val isyroqTime = sunriseTime + (15.0 / 60.0) // 15 mins after sunrise
        val dhuhaTime = sunriseTime + (25.0 / 60.0)  // 25 mins after sunrise

        // Dzuhur
        val dzuhurTime = solarTransit + ihtiyatHours

        // Asr: Shadow angle formula for Shafi'i
        val asrAngle = computeAsrAltitude(latitude, solarDeclination)
        val asrHourAngle = computeHourAngle(asrAngle, latitude, solarDeclination)
        val asrTime = solarTransit + asrHourAngle + ihtiyatHours

        // Maghrib: -0.833 degrees
        val maghribTime = solarTransit + sunriseHourAngle + ihtiyatHours

        // Isha: -18.0 degrees (Kemenag standard)
        val ishaHourAngle = computeHourAngle(-18.0, latitude, solarDeclination)
        val ishaTime = solarTransit + ishaHourAngle + ihtiyatHours

        // Tasawuf Schedule: Waktu Malam / Tahajjud (1/3 akhir malam)
        val nightDuration = ((fajrTime + 24.0) - maghribTime) % 24.0
        val tahajjudStart = (maghribTime + (2.0 / 3.0) * nightDuration) % 24.0

        // Waktal (Waktu Talqin / Wirid Khusus TQN Sirnarasa)
        // Prime meditation slot is 02:30 or 1 hour before Fajr
        val waktalTime = (fajrTime - 1.5 + 24.0) % 24.0

        val tzLabel = when (timezoneOffset.toInt()) {
            7 -> "WIB"
            8 -> "WITA"
            9 -> "WIT"
            else -> "UTC+${timezoneOffset.toInt()}"
        }

        return PrayerSchedule(
            dateFormatted = "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}",
            imsak = formatDecimalHours(imsakTime),
            subuh = formatDecimalHours(fajrTime),
            isyroq = formatDecimalHours(isyroqTime),
            dhuha = formatDecimalHours(dhuhaTime),
            dzuhur = formatDecimalHours(dzuhurTime),
            ashar = formatDecimalHours(asrTime),
            maghrib = formatDecimalHours(maghribTime),
            isya = formatDecimalHours(ishaTime),
            tahajjud = formatDecimalHours(tahajjudStart),
            waktal = formatDecimalHours(waktalTime),
            timezone = tzLabel,
            locationName = locationName
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
            "Tahajjud" to parseTimeToSeconds(schedule.tahajjud),
            "Waktal" to parseTimeToSeconds(schedule.waktal),
            "Imsak" to parseTimeToSeconds(schedule.imsak),
            "Subuh" to parseTimeToSeconds(schedule.subuh),
            "Isyroq" to parseTimeToSeconds(schedule.isyroq),
            "Dhuha" to parseTimeToSeconds(schedule.dhuha),
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
            // Next is tomorrow's first prayer
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
            "Tahajjud" -> schedule.tahajjud
            "Waktal" -> schedule.waktal
            "Imsak" -> schedule.imsak
            "Subuh" -> schedule.subuh
            "Isyroq" -> schedule.isyroq
            "Dhuha" -> schedule.dhuha
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

    private fun parseTimeToSeconds(timeStr: String): Long {
        val parts = timeStr.split(":")
        if (parts.size < 2) return 0L
        val h = parts[0].trim().toLongOrNull() ?: 0L
        val m = parts[1].trim().toLongOrNull() ?: 0L
        return h * 3600L + m * 60L
    }

    private fun computeHourAngle(altitudeDeg: Double, latitudeDeg: Double, declinationDeg: Double): Double {
        val latRad = degToRad(latitudeDeg)
        val decRad = degToRad(declinationDeg)
        val altRad = degToRad(altitudeDeg)

        val numerator = sin(altRad) - sin(latRad) * sin(decRad)
        val denominator = cos(latRad) * cos(decRad)
        val cosH = (numerator / denominator).coerceIn(-1.0, 1.0)
        return radToDeg(acos(cosH)) / 15.0
    }

    private fun computeAsrAltitude(latitudeDeg: Double, declinationDeg: Double): Double {
        val delta = abs(latitudeDeg - declinationDeg)
        val shadowRatio = 1.0 + tan(degToRad(delta))
        return radToDeg(atan(1.0 / shadowRatio))
    }

    private fun getJulianDay(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2.0 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun fixAngle(deg: Double): Double {
        val res = deg % 360.0
        return if (res < 0.0) res + 360.0 else res
    }

    private fun degToRad(deg: Double): Double = deg * (PI / 180.0)
    private fun radToDeg(rad: Double): Double = rad * (180.0 / PI)

    private fun formatDecimalHours(hours: Double): String {
        val normalized = (hours % 24.0 + 24.0) % 24.0
        val h = normalized.toInt()
        val totalMinutes = (normalized - h) * 60.0
        val m = totalMinutes.roundToInt()
        val finalH = if (m == 60) (h + 1) % 24 else h
        val finalM = if (m == 60) 0 else m
        return "${finalH.toString().padStart(2, '0')}:${finalM.toString().padStart(2, '0')}"
    }
}
