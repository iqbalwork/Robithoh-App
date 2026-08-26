package com.iqbalwork.robithoh.feature.manaqib.model

import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import kotlinx.serialization.Serializable

@Serializable
data class ManqobahChapter(
    val id: Long,
    val chapterNumber: Int,
    val titleArabic: String,
    val titleIndonesian: String,
    val titleSundanese: String,
    val contentArabic: String,
    val contentIndonesian: String,
    val contentSundanese: String,
    val audioPath: String? = null
) {
    fun titleForLanguage(language: LiturgyLanguage): String = when (language) {
        LiturgyLanguage.ARABIC -> titleArabic
        LiturgyLanguage.INDONESIAN -> titleIndonesian
        LiturgyLanguage.SUNDANESE -> titleSundanese
    }

    fun contentForLanguage(language: LiturgyLanguage): String = when (language) {
        LiturgyLanguage.ARABIC -> contentArabic
        LiturgyLanguage.INDONESIAN -> contentIndonesian
        LiturgyLanguage.SUNDANESE -> contentSundanese
    }
}

@Serializable
data class SilsilahNode(
    val orderNumber: Int,
    val name: String,
    val title: String,
    val locationOrEpithet: String,
    val arabicName: String = "",
    val description: String = ""
)

@Serializable
data class TanbihContent(
    val title: String,
    val subtitle: String,
    val openingArabic: String,
    val indonesianText: String,
    val sundaneseText: String,
    val closingArabic: String
)

@Serializable
data class McProgramItem(
    val stepNumber: Int,
    val titleId: String,
    val titleSu: String,
    val arabicIntro: String,
    val protocolId: String,
    val protocolSu: String,
    val officerRole: String
)

@Serializable
data class DoaSpiritualItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val arabicText: String,
    val latinText: String,
    val indonesianTranslation: String,
    val sundaneseTranslation: String,
    val fadhilah: String
)

@Serializable
data class KhotamanStep(
    val stepNumber: Int,
    val title: String,
    val repeatCount: String,
    val arabicText: String,
    val latinText: String,
    val translation: String,
    val instructions: String
)
