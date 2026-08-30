package com.iqbalwork.robithoh.feature.amaliyah.model

import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import kotlinx.serialization.Serializable

@Serializable
enum class AmaliyahCategory(val label: String, val subtitle: String) {
    DZIKIR_BA_DA_SHOLAT("Dzikir Ba'da Sholat", "Lafadz Dzikir Jahr & Khofi MTQN Suryalaya Sirnarasa PPKN III Silsilah 38"),
    PRAYER_TIMES("Jadwal Sholat & Tasawuf", "Waktu Sholat, Tahajjud, Waktal & Arah Kiblat"),
    DOA_HARIAN("Doa & Wirid Harian", "Sebelum Tidur, Tarhim, Salam Wali Mursyid, Istighotsah"),
    BULAN_HIJRIYAH("12 Bulan Hijriyah", "Panduan amalan bulanan & sholat sunnah istimewa"),
    SHOLAT_SUNNAH("Sholat Sunnah Khusus", "Sholat Rajab, Nisfu Sya'ban, Tarawih & Witir, Rebo Wekasan")
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
    val hijriDateFormatted: String = "",
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
    val locationName: String,
    val methodName: String = "Kementrian Agama Indonesia"
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

@Serializable
data class AdzanVoiceOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val audioFileName: String,
    val fajrAudioFileName: String = audioFileName,
    val isBuiltIn: Boolean = true,
    val isCustom: Boolean = false
) {
    fun getAudioForPrayer(prayerName: String): String {
        val cleanName = prayerName.trim().lowercase()
        return if (cleanName == "subuh" || cleanName == "fajr") {
            fajrAudioFileName
        } else {
            audioFileName
        }
    }
}

object AdzanVoices {
    val MISYARI_RASYID = AdzanVoiceOption(
        id = "misyari_rasyid",
        title = "Misyari Rasyid Al-Afasi",
        subtitle = "Lantunan adzan merdu & syahdu",
        audioFileName = "adzan_misyari_rasyid.mp3",
        fajrAudioFileName = "adzan_misyari_rasyid_fajr.mp3",
        isBuiltIn = true
    )

    val AHMAD_AL_NAFEES = AdzanVoiceOption(
        id = "ahmad_al_nafees",
        title = "Ahmad al-Nafees",
        subtitle = "Lantunan adzan khas Kuwait",
        audioFileName = "adzan_ahmad_al_nafees.mp3",
        fajrAudioFileName = "adzan_ahmad_al_nafees_fajr.mp3",
        isBuiltIn = true
    )

    val MANSOUR_AL_ZAHRANI = AdzanVoiceOption(
        id = "mansour_al_zahrani",
        title = "Mansour Al-Zahrani",
        subtitle = "Lantunan adzan khas jazirah Arab",
        audioFileName = "adzan_mansour_al_zahrani.mp3",
        fajrAudioFileName = "adzan_mansour_al_zahrani_fajr.mp3",
        isBuiltIn = true
    )

    val HAFIZ_MUSTAFA_OZCAN = AdzanVoiceOption(
        id = "hafiz_mustafa_ozcan",
        title = "Hafiz Mustafa Özcan",
        subtitle = "Lantunan adzan langgam Turki / Utsmani",
        audioFileName = "adzan_hafiz_mustafa_ozcan.mp3",
        fajrAudioFileName = "adzan_hafiz_mustafa_ozcan_fajr.mp3",
        isBuiltIn = true
    )

    val ALL: List<AdzanVoiceOption> = listOf(
        MISYARI_RASYID,
        AHMAD_AL_NAFEES,
        MANSOUR_AL_ZAHRANI,
        HAFIZ_MUSTAFA_OZCAN
    )

    fun findById(id: String): AdzanVoiceOption {
        return ALL.find { it.id == id } ?: MISYARI_RASYID
    }
}

@Serializable
enum class PrayerNotificationMode(
    val id: String,
    val title: String,
    val description: String,
    val icon: String
) {
    ADZAN(
        id = "adzan",
        title = "Adzan (Alarm)",
        description = "Memutar lantunan suara adzan lengkap saat waktu sholat tiba",
        icon = "🔔"
    ),
    PUSH_NOTIFICATION(
        id = "push",
        title = "Push Notifikasi",
        description = "Notifikasi pop-up layar dengan getaran dan nada singkat",
        icon = "💬"
    ),
    SILENT(
        id = "silent",
        title = "Senyap (Off)",
        description = "Tidak ada notifikasi dan suara alarm",
        icon = "🔕"
    );

    fun nextMode(): PrayerNotificationMode = when (this) {
        ADZAN -> PUSH_NOTIFICATION
        PUSH_NOTIFICATION -> SILENT
        SILENT -> ADZAN
    }

    companion object {
        fun fromId(id: String?): PrayerNotificationMode {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: ADZAN
        }

        fun fromDbValue(value: Long): PrayerNotificationMode = when (value) {
            2L -> ADZAN
            1L -> PUSH_NOTIFICATION
            else -> SILENT
        }

        fun toDbValue(mode: PrayerNotificationMode): Long = when (mode) {
            ADZAN -> 2L
            PUSH_NOTIFICATION -> 1L
            SILENT -> 0L
        }
    }
}

@Serializable
data class PrayerNotificationSettings(
    val subuhMode: PrayerNotificationMode = PrayerNotificationMode.ADZAN,
    val dzuhurMode: PrayerNotificationMode = PrayerNotificationMode.ADZAN,
    val asharMode: PrayerNotificationMode = PrayerNotificationMode.ADZAN,
    val maghribMode: PrayerNotificationMode = PrayerNotificationMode.ADZAN,
    val isyaMode: PrayerNotificationMode = PrayerNotificationMode.ADZAN,
    val imsakMode: PrayerNotificationMode = PrayerNotificationMode.PUSH_NOTIFICATION,
    val isPrePrayerReminderEnabled: Boolean = true,
    val selectedVoiceId: String = "misyari_rasyid",
    val customAudioPath: String? = null
) {
    val isSubuhEnabled: Boolean get() = subuhMode != PrayerNotificationMode.SILENT
    val isDzuhurEnabled: Boolean get() = dzuhurMode != PrayerNotificationMode.SILENT
    val isAsharEnabled: Boolean get() = asharMode != PrayerNotificationMode.SILENT
    val isMaghribEnabled: Boolean get() = maghribMode != PrayerNotificationMode.SILENT
    val isIsyaEnabled: Boolean get() = isyaMode != PrayerNotificationMode.SILENT
    val isImsakEnabled: Boolean get() = imsakMode != PrayerNotificationMode.SILENT

    fun isPrayerEnabled(type: PrayerType): Boolean = when (type) {
        PrayerType.SUBUH -> isSubuhEnabled
        PrayerType.DZUHUR -> isDzuhurEnabled
        PrayerType.ASHAR -> isAsharEnabled
        PrayerType.MAGHRIB -> isMaghribEnabled
        PrayerType.ISYA -> isIsyaEnabled
        PrayerType.IMSAK -> isImsakEnabled
        PrayerType.TERBIT -> false
    }

    fun getPrayerMode(type: PrayerType): PrayerNotificationMode = when (type) {
        PrayerType.SUBUH -> subuhMode
        PrayerType.DZUHUR -> dzuhurMode
        PrayerType.ASHAR -> asharMode
        PrayerType.MAGHRIB -> maghribMode
        PrayerType.ISYA -> isyaMode
        PrayerType.IMSAK -> imsakMode
        PrayerType.TERBIT -> PrayerNotificationMode.SILENT
    }

    fun withPrayerMode(type: PrayerType, mode: PrayerNotificationMode): PrayerNotificationSettings = when (type) {
        PrayerType.SUBUH -> copy(subuhMode = mode)
        PrayerType.DZUHUR -> copy(dzuhurMode = mode)
        PrayerType.ASHAR -> copy(asharMode = mode)
        PrayerType.MAGHRIB -> copy(maghribMode = mode)
        PrayerType.ISYA -> copy(isyaMode = mode)
        PrayerType.IMSAK -> copy(imsakMode = if (mode == PrayerNotificationMode.ADZAN) PrayerNotificationMode.PUSH_NOTIFICATION else mode)
        PrayerType.TERBIT -> this
    }

    fun withCycledPrayerMode(type: PrayerType): PrayerNotificationSettings {
        val current = getPrayerMode(type)
        val next = if (type == PrayerType.IMSAK) {
            if (current == PrayerNotificationMode.PUSH_NOTIFICATION) PrayerNotificationMode.SILENT else PrayerNotificationMode.PUSH_NOTIFICATION
        } else {
            current.nextMode()
        }
        return withPrayerMode(type, next)
    }

    fun withToggledPrayer(type: PrayerType, enabled: Boolean): PrayerNotificationSettings {
        val mode = if (enabled) {
            if (type == PrayerType.IMSAK) PrayerNotificationMode.PUSH_NOTIFICATION else PrayerNotificationMode.ADZAN
        } else {
            PrayerNotificationMode.SILENT
        }
        return withPrayerMode(type, mode)
    }

    fun withPrePrayerReminder(enabled: Boolean): PrayerNotificationSettings {
        return copy(isPrePrayerReminderEnabled = enabled)
    }
}
