package com.iqbalwork.robithoh.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenKey {
    @Serializable
    data object Home : ScreenKey

    @Serializable
    data object Amaliyah : ScreenKey

    @Serializable
    data object Tasbih : ScreenKey

    @Serializable
    data object ManaqibList : ScreenKey

    @Serializable
    data class ManaqibDetail(val chapterNumber: Int = 1) : ScreenKey

    @Serializable
    data object QuranList : ScreenKey

    @Serializable
    data class QuranSurah(val surahNumber: Int = 1) : ScreenKey

    @Serializable
    data object Settings : ScreenKey

    @Serializable
    data object ProfilePesantren : ScreenKey

    @Serializable
    data class DocumentReader(val documentId: String) : ScreenKey

    @Serializable
    data object Langgam : ScreenKey
}
