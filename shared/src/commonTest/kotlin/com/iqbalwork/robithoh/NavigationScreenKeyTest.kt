package com.iqbalwork.robithoh

import com.iqbalwork.robithoh.navigation.ScreenKey
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NavigationScreenKeyTest {

    private val json = Json { prettyPrint = false }

    @Test
    fun testScreenKeySerialization() {
        val docReader: ScreenKey = ScreenKey.DocumentReader("dzikir_tqn")
        val langgam: ScreenKey = ScreenKey.Langgam
        val tasbih: ScreenKey = ScreenKey.Tasbih()
        val tasbihWithData: ScreenKey = ScreenKey.Tasbih(initialCount = 13, targetCount = 165, dzikirTitle = "Dzikir Jahr")
        val quranList: ScreenKey = ScreenKey.QuranList
        val quranSurah: ScreenKey = ScreenKey.QuranSurah(surahNumber = 36)
        val settings: ScreenKey = ScreenKey.Settings
        val profile: ScreenKey = ScreenKey.ProfilePesantren
        val qibla: ScreenKey = ScreenKey.Qibla

        val serializedTasbih = json.encodeToString(tasbihWithData)
        val deserializedTasbih = json.decodeFromString<ScreenKey>(serializedTasbih)
        assertEquals(tasbihWithData, deserializedTasbih)
        assertTrue(deserializedTasbih is ScreenKey.Tasbih)
        assertEquals(13, (deserializedTasbih as ScreenKey.Tasbih).initialCount)
        assertEquals(165, (deserializedTasbih as ScreenKey.Tasbih).targetCount)
        assertEquals("Dzikir Jahr", (deserializedTasbih as ScreenKey.Tasbih).dzikirTitle)

        val serializedDoc = json.encodeToString(docReader)
        val deserializedDoc = json.decodeFromString<ScreenKey>(serializedDoc)
        assertEquals(docReader, deserializedDoc)
        assertTrue(deserializedDoc is ScreenKey.DocumentReader)
        assertEquals("dzikir_tqn", (deserializedDoc as ScreenKey.DocumentReader).documentId)

        val serializedSurah = json.encodeToString(quranSurah)
        val deserializedSurah = json.decodeFromString<ScreenKey>(serializedSurah)
        assertEquals(36, (deserializedSurah as ScreenKey.QuranSurah).surahNumber)

        val serializedQibla = json.encodeToString(qibla)
        val deserializedQibla = json.decodeFromString<ScreenKey>(serializedQibla)
        assertEquals(ScreenKey.Qibla, deserializedQibla)
    }

    @Test
    fun testScreenKeyListSaverPreservesBackstack() {
        val originalBackstack = androidx.compose.runtime.mutableStateListOf<androidx.navigation3.runtime.NavKey>(
            ScreenKey.Home,
            ScreenKey.QuranList,
            ScreenKey.QuranSurah(surahNumber = 18, ayahNumber = 40),
            ScreenKey.DocumentReader(documentId = "dzikir_tqn")
        )

        // Save state (Simulate configuration change / activity save instance)
        val saved = with(com.iqbalwork.robithoh.navigation.ScreenKeyListSaver) {
            androidx.compose.runtime.saveable.SaverScope { true }.save(originalBackstack)
        }

        // Restore state (Simulate activity recreate)
        @Suppress("UNCHECKED_CAST")
        val restored = com.iqbalwork.robithoh.navigation.ScreenKeyListSaver.restore(saved as Any)

        assertEquals(4, restored?.size)
        assertEquals(ScreenKey.Home, restored?.get(0))
        assertEquals(ScreenKey.QuranList, restored?.get(1))
        assertEquals(ScreenKey.QuranSurah(surahNumber = 18, ayahNumber = 40), restored?.get(2))
        assertEquals(ScreenKey.DocumentReader(documentId = "dzikir_tqn"), restored?.get(3))
    }
}

