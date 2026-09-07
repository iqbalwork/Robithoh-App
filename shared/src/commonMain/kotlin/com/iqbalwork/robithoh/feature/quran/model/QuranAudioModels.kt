package com.iqbalwork.robithoh.feature.quran.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QariOption(
    val id: Int,
    val name: String,
    val style: String = "Murattal",
    val slug: String,
    val subfolder: String
)

@Serializable
data class VerseTiming(
    @SerialName("verse_key")
    val verseKey: String,
    @SerialName("timestamp_from")
    val timestampFromMs: Long,
    @SerialName("timestamp_to")
    val timestampToMs: Long,
    @SerialName("duration")
    val durationMs: Long
) {
    val surahNumber: Int
        get() = verseKey.split(":").firstOrNull()?.toIntOrNull() ?: 1

    val ayahNumber: Int
        get() = verseKey.split(":").getOrNull(1)?.toIntOrNull() ?: 1
}

@Serializable
data class ChapterAudio(
    val chapterId: Int,
    val audioUrl: String,
    val durationMs: Long,
    val verseTimings: List<VerseTiming> = emptyList()
)

object QariList {
    val default = QariOption(
        id = 7,
        name = "Mishari Rashid al-`Afasy",
        style = "Murattal",
        slug = "mishari_al_afasy",
        subfolder = "Alafasy"
    )

    val popular: List<QariOption> = listOf(
        default,
        QariOption(
            id = 3,
            name = "Abdur-Rahman as-Sudais",
            style = "Murattal",
            slug = "abdurrahmaan_as_sudais",
            subfolder = "Abdurrahmaan_As-Sudais"
        ),
        QariOption(
            id = 4,
            name = "Abu Bakr al-Shatri",
            style = "Murattal",
            slug = "abu_bakr_ash-shaatree",
            subfolder = "Abu_Bakr_Ash-Shaatree"
        ),
        QariOption(
            id = 6,
            name = "Mahmoud Khalil Al-Husary",
            style = "Murattal",
            slug = "mahmood_khaleel_al-husaree",
            subfolder = "Husary"
        ),
        QariOption(
            id = 9,
            name = "Mohamed Siddiq al-Minshawi",
            style = "Murattal",
            slug = "muhammad_siddeeq_al-minshaawee",
            subfolder = "Minshawy_Murattal"
        ),
        QariOption(
            id = 2,
            name = "AbdulBaset AbdulSamad",
            style = "Murattal",
            slug = "abdul_baasit_murattal",
            subfolder = "Abdul_Basit_Murattal"
        ),
        QariOption(
            id = 10,
            name = "Sa'ud ash-Shuraim",
            style = "Murattal",
            slug = "sa_ood_ash-shuraym",
            subfolder = "Saood_ash-Shuraym"
        ),
        QariOption(
            id = 97,
            name = "Yasser Ad Dussary",
            style = "Murattal",
            slug = "yasser_ad-dussary",
            subfolder = "Yasser_Ad-Dussary"
        )
    )
}
