package com.iqbalwork.robithoh.feature.quran.model

import androidx.compose.ui.geometry.Rect
import kotlinx.serialization.Serializable

@Serializable
data class QuranPageMapping(
    val viewportWidth: Int = 2600,
    val viewportHeight: Int = 4206,
    val data: List<AyahBlock>
)

@Serializable
data class ConsolidatedPageMappings(
    val viewportWidth: Int = 2600,
    val viewportHeight: Int = 4206,
    val pages: Map<String, List<AyahBlock>>
)

@Serializable
data class AyahBlock(
    val surah: Int,
    val ayah: Int,
    val blok: Int,
    val top: Int,
    val left: Int,
    val width: Int,
    val height: Int
)

data class ScaledAyahBlock(
    val surah: Int,
    val ayah: Int,
    val blok: Int,
    val rect: Rect
)

data class PageMeta(
    val pageNumber: Int,
    val juz: Int,
    val surahName: String,
    val surahNumber: Int,
    val arabicSurahName: String = "",
    val ayahRangeText: String = ""
)
