package com.iqbalwork.robithoh.feature.amaliyah.model

import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import kotlinx.serialization.Serializable

@Serializable
enum class AmaliyahCategory(val label: String, val subtitle: String) {
    DZIKIR_BA_DA_SHOLAT("Dzikir Ba'da Sholat", "Lafadz Dzikir Jahr & Khofi TQN 38"),
    PRAYER_TIMES("Jadwal Sholat & Tasawuf", "Waktu Sholat, Tahajjud, Waktal & Arah Kiblat"),
    DOA_HARIAN("Doa & Wirid Harian", "Sebelum Tidur, Tarhim, Salam Wali Mursyid, Istighotsah"),
    BULAN_HIJRIYAH("12 Bulan Hijriyah", "Panduan amalan bulanan & sholat sunnah istimewa"),
    SHOLAT_SUNNAH("Sholat Sunnah Khusus", "Sholat Rajab, Nisfu Sya'ban, Tarawih TQN, Rebo Wekasan")
}

@Serializable
enum class DzikirType(val label: String, val subtitle: String) {
    JAHR("Dzikir Jahr", "Dzikir bersuara nyaring (165x)"),
    KHOFI("Dzikir Khofi", "Dzikir di dalam hati (Lathifah Qolbi)")
}

@Serializable
data class DzikirItem(
    val id: String,
    val number: Int,
    val title: String,
    val arabicText: String,
    val latinText: String,
    val indonesianText: String,
    val sundaneseText: String,
    val repetitionCount: Int = 1,
    val category: String = "Dzikir",
    val kaifiyatNote: String = "",
    val audioPath: String? = null
) {
    fun getTextForLanguage(language: LiturgyLanguage): String = when (language) {
        LiturgyLanguage.ARABIC -> arabicText
        LiturgyLanguage.INDONESIAN -> indonesianText
        LiturgyLanguage.SUNDANESE -> sundaneseText
    }
}

@Serializable
data class SpecialPrayer(
    val id: String,
    val title: String,
    val arabicTitle: String,
    val category: String,
    val arabicText: String,
    val latinText: String,
    val indonesianText: String,
    val sundaneseText: String,
    val kaifiyat: String,
    val virtue: String = "",
    val recommendedTime: String = "",
    val rakaatCount: Int = 0
) {
    fun getTextForLanguage(language: LiturgyLanguage): String = when (language) {
        LiturgyLanguage.ARABIC -> arabicText
        LiturgyLanguage.INDONESIAN -> indonesianText
        LiturgyLanguage.SUNDANESE -> sundaneseText
    }
}

@Serializable
data class HijriyahAmaliyah(
    val monthNumber: Int,
    val monthName: String,
    val arabicName: String,
    val virtues: String,
    val recommendedAmalan: List<String>,
    val specialPrayers: List<SpecialPrayer> = emptyList()
)

@Serializable
data class PrayerSchedule(
    val dateFormatted: String,
    val imsak: String,
    val subuh: String,
    val isyroq: String,
    val dhuha: String,
    val dzuhur: String,
    val ashar: String,
    val maghrib: String,
    val isya: String,
    val tahajjud: String,
    val waktal: String,
    val timezone: String,
    val locationName: String
)

@Serializable
data class NextPrayerCountdown(
    val nextPrayerName: String,
    val nextPrayerTime: String,
    val remainingHours: Long,
    val remainingMinutes: Long,
    val remainingSeconds: Long,
    val totalRemainingSeconds: Long,
    val progressFraction: Float,
    val isPrayerTimeNow: Boolean
)

@Serializable
data class QiblaInfo(
    val directionDegrees: Double,
    val distanceKm: Double,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val compassHeading: String
)

@Serializable
data class LocationPreset(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffset: Double,
    val province: String
)
