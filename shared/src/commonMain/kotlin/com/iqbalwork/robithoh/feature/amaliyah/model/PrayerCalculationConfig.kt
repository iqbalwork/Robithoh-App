package com.iqbalwork.robithoh.feature.amaliyah.model

import com.batoulapps.adhan2.CalculationParameters
import com.batoulapps.adhan2.Madhab
import com.batoulapps.adhan2.PrayerAdjustments
import kotlinx.serialization.Serializable

@Serializable
enum class PrayerType(val label: String) {
    IMSAK("Imsak"),
    SUBUH("Subuh"),
    TERBIT("Terbit"),
    DZUHUR("Dzuhur"),
    ASHAR("Ashar"),
    MAGHRIB("Maghrib"),
    ISYA("Isya")
}

@Serializable
data class PrayerCalculationMethodItem(
    val id: String,
    val name: String,
    val description: String,
    val fajrAngle: Double = 18.0,
    val ishaAngle: Double = 18.0,
    val ishaInterval: Int = 0,
    val defaultFajrAdj: Int = 0,
    val defaultSunriseAdj: Int = 0,
    val defaultDhuhrAdj: Int = 0,
    val defaultAsrAdj: Int = 0,
    val defaultMaghribAdj: Int = 0,
    val defaultIshaAdj: Int = 0
) {
    fun toCalculationParameters(madhab: Madhab = Madhab.SHAFI): CalculationParameters {
        val methodAdj = PrayerAdjustments(
            fajr = defaultFajrAdj,
            sunrise = defaultSunriseAdj,
            dhuhr = defaultDhuhrAdj,
            asr = defaultAsrAdj,
            maghrib = defaultMaghribAdj,
            isha = defaultIshaAdj
        )
        return if (ishaInterval > 0) {
            CalculationParameters(
                fajrAngle = fajrAngle,
                ishaInterval = ishaInterval,
                madhab = madhab,
                methodAdjustments = methodAdj
            )
        } else {
            CalculationParameters(
                fajrAngle = fajrAngle,
                ishaAngle = ishaAngle,
                madhab = madhab,
                methodAdjustments = methodAdj
            )
        }
    }
}

object PrayerCalculationMethods {
    val ALL_METHODS = listOf(
        PrayerCalculationMethodItem(
            id = "KEMENAG",
            name = "Kementrian Agama Indonesia",
            description = "Subuh 20° - Isya 18° (Standar Ihtiyati Kemenag RI)",
            fajrAngle = 20.0,
            ishaAngle = 18.0,
            defaultFajrAdj = 2,
            defaultSunriseAdj = -2,
            defaultDhuhrAdj = 4,
            defaultAsrAdj = 3,
            defaultMaghribAdj = 7,
            defaultIshaAdj = 2
        ),
        PrayerCalculationMethodItem(
            id = "SOUTH_EAST_ASIA",
            name = "South East Asia",
            description = "Subuh 20° - Isya 18°",
            fajrAngle = 20.0,
            ishaAngle = 18.0,
            defaultFajrAdj = 2,
            defaultDhuhrAdj = 2,
            defaultAsrAdj = 2,
            defaultMaghribAdj = 3,
            defaultIshaAdj = 2
        ),
        PrayerCalculationMethodItem(
            id = "JAKIM",
            name = "Jabatan Kemajuan Islam Malaysia (JAKIM)",
            description = "Subuh 20° - Isya 18°",
            fajrAngle = 20.0,
            ishaAngle = 18.0,
            defaultFajrAdj = 2,
            defaultDhuhrAdj = 1,
            defaultAsrAdj = 2,
            defaultMaghribAdj = 2,
            defaultIshaAdj = 2
        ),
        PrayerCalculationMethodItem(
            id = "UMM_AL_QURA",
            name = "Umm al-Qura",
            description = "Subuh 18.5° - Isya 90 menit setelah maghrib",
            fajrAngle = 18.5,
            ishaInterval = 90
        ),
        PrayerCalculationMethodItem(
            id = "MUSLIM_WORLD_LEAGUE",
            name = "Muslim World League",
            description = "Subuh 18° - Isya 17°",
            fajrAngle = 18.0,
            ishaAngle = 17.0
        ),
        PrayerCalculationMethodItem(
            id = "MUIS",
            name = "Majlis Ugama Islam Singapura",
            description = "Subuh 20° - Isya 18°",
            fajrAngle = 20.0,
            ishaAngle = 18.0,
            defaultDhuhrAdj = 1
        ),
        PrayerCalculationMethodItem(
            id = "KARACHI",
            name = "University of Islamic Sciences",
            description = "Subuh 18° - Isya 18°",
            fajrAngle = 18.0,
            ishaAngle = 18.0
        ),
        PrayerCalculationMethodItem(
            id = "KHEU_BRUNEI",
            name = "Kementerian Hal Ehwal Ugama",
            description = "Subuh 20° - Isya 18°",
            fajrAngle = 20.0,
            ishaAngle = 18.0,
            defaultFajrAdj = 2,
            defaultDhuhrAdj = 2,
            defaultAsrAdj = 2,
            defaultMaghribAdj = 3,
            defaultIshaAdj = 2
        ),
        PrayerCalculationMethodItem(
            id = "NORTH_AMERICA",
            name = "Islamic Society of North America",
            description = "Subuh 15° - Isya 15°",
            fajrAngle = 15.0,
            ishaAngle = 15.0
        ),
        PrayerCalculationMethodItem(
            id = "TURKEY",
            name = "Diyanet İşleri Başkanlığı",
            description = "Subuh 18° - Isya 17°",
            fajrAngle = 18.0,
            ishaAngle = 17.0
        ),
        PrayerCalculationMethodItem(
            id = "EGYPTIAN",
            name = "Egyptian General Authority",
            description = "Subuh 19.5° - Isya 17.5°",
            fajrAngle = 19.5,
            ishaAngle = 17.5
        ),
        PrayerCalculationMethodItem(
            id = "UAE",
            name = "United Arab Emirates",
            description = "Subuh 19.5° - Isya 90 menit setelah maghrib",
            fajrAngle = 19.5,
            ishaInterval = 90
        ),
        PrayerCalculationMethodItem(
            id = "QATAR",
            name = "Qatar",
            description = "Subuh 19.5° - Isya 90 menit setelah maghrib",
            fajrAngle = 19.5,
            ishaInterval = 90
        ),
        PrayerCalculationMethodItem(
            id = "KUWAIT",
            name = "Kuwait",
            description = "Subuh 18° - Isya 17.5°",
            fajrAngle = 18.0,
            ishaAngle = 17.5
        )
    )

    val DEFAULT = ALL_METHODS[0]

    fun findById(id: String): PrayerCalculationMethodItem {
        return ALL_METHODS.find { it.id.equals(id, ignoreCase = true) } ?: DEFAULT
    }
}

@Serializable
data class PrayerTimeAdjustments(
    val imsak: Int = 0,
    val subuh: Int = 0,
    val terbit: Int = 0,
    val dzuhur: Int = 0,
    val ashar: Int = 0,
    val maghrib: Int = 0,
    val isya: Int = 0
) {
    fun getOffset(prayerType: PrayerType): Int = when (prayerType) {
        PrayerType.IMSAK -> imsak
        PrayerType.SUBUH -> subuh
        PrayerType.TERBIT -> terbit
        PrayerType.DZUHUR -> dzuhur
        PrayerType.ASHAR -> ashar
        PrayerType.MAGHRIB -> maghrib
        PrayerType.ISYA -> isya
    }

    fun withOffset(prayerType: PrayerType, offset: Int): PrayerTimeAdjustments = when (prayerType) {
        PrayerType.IMSAK -> copy(imsak = offset)
        PrayerType.SUBUH -> copy(subuh = offset)
        PrayerType.TERBIT -> copy(terbit = offset)
        PrayerType.DZUHUR -> copy(dzuhur = offset)
        PrayerType.ASHAR -> copy(ashar = offset)
        PrayerType.MAGHRIB -> copy(maghrib = offset)
        PrayerType.ISYA -> copy(isya = offset)
    }

    fun getOffsetLabel(prayerType: PrayerType): String {
        val offset = getOffset(prayerType)
        return formatOffsetLabel(offset)
    }

    fun toAdhanPrayerAdjustments(): PrayerAdjustments {
        return PrayerAdjustments(
            fajr = subuh,
            sunrise = terbit,
            dhuhr = dzuhur,
            asr = ashar,
            maghrib = maghrib,
            isha = isya
        )
    }

    companion object {
        fun formatOffsetLabel(offset: Int): String {
            return when {
                offset == 0 -> "Standar"
                offset > 0 -> "+$offset Menit"
                else -> "$offset Menit"
            }
        }

        val AVAILABLE_OFFSETS = listOf(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5)
    }
}
