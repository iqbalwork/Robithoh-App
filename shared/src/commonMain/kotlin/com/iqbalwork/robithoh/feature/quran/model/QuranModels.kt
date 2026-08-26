package com.iqbalwork.robithoh.feature.quran.model

import kotlinx.serialization.Serializable

enum class RevelationType(val label: String) {
    MAKKIYAH("Makkiyah"),
    MADANIYAH("Madaniyah")
}

@Serializable
data class SurahMeta(
    val number: Int,
    val nameLatin: String,
    val nameArabic: String,
    val englishNameTranslation: String,
    val indonesianMeaning: String,
    val numberOfAyahs: Int,
    val revelationType: RevelationType,
    val audioUrl: String? = null
)

@Serializable
data class Ayah(
    val numberInSurah: Int,
    val surahNumber: Int,
    val textArabic: String,
    val transliterationLatin: String = "",
    val translationIndonesian: String,
    val translationSundanese: String = "",
    val audioUrl: String? = null
)

@Serializable
data class QuranBookmark(
    val id: Long = 0L,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val timestamp: Long = 0L
)

@Serializable
data class ShalawatModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val arabicText: String,
    val latinText: String,
    val indonesianTranslation: String,
    val sundaneseTranslation: String = "",
    val virtue: String,
    val audioPath: String? = null
)

@Serializable
data class ZiarahSection(
    val id: String,
    val title: String,
    val subtitle: String,
    val adabSteps: List<String>,
    val arabicPrayer: String,
    val latinPrayer: String,
    val indonesianTranslation: String,
    val fadhilah: String
)
