package com.iqbalwork.robithoh.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    data class QuranSurah(val surahNumber: Int = 1, val ayahNumber: Int? = null) : ScreenKey

    @Serializable
    data object Settings : ScreenKey

    @Serializable
    data object ProfilePesantren : ScreenKey

    @Serializable
    data class DocumentReader(val documentId: String) : ScreenKey

    @Serializable
    data object Langgam : ScreenKey

    @Serializable
    data object PrayerCalculationMethods : ScreenKey

    @Serializable
    data object PrayerAdjustments : ScreenKey
}

/**
 * Saver for SnapshotStateList<ScreenKey> so Navigation backstack survives
 * Activity recreation (screen rotation, folding/unfolding foldable devices, etc.)
 */
val ScreenKeyListSaver: Saver<SnapshotStateList<ScreenKey>, Any> = listSaver(
    save = { stateList ->
        stateList.map { Json.encodeToString<ScreenKey>(it) }
    },
    restore = { savedList ->
        val restored = mutableStateListOf<ScreenKey>()
        savedList.forEach { jsonStr ->
            try {
                restored.add(Json.decodeFromString<ScreenKey>(jsonStr))
            } catch (_: Exception) {}
        }
        if (restored.isEmpty()) {
            restored.add(ScreenKey.Home)
        }
        restored
    }
)
