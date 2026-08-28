package com.iqbalwork.robithoh

import com.iqbalwork.robithoh.navigation.ScreenKey
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NavigationScreenKeyTest {

    private val json = Json { prettyPrint = false }

    @Test
    fun testScreenKeySerialization() {
        val home: ScreenKey = ScreenKey.Home
        val amaliyah: ScreenKey = ScreenKey.Amaliyah
        val tasbih: ScreenKey = ScreenKey.Tasbih
        val manaqibDetail: ScreenKey = ScreenKey.ManaqibDetail(chapterNumber = 7)
        val quranList: ScreenKey = ScreenKey.QuranList
        val quranSurah: ScreenKey = ScreenKey.QuranSurah(surahNumber = 36)
        val settings: ScreenKey = ScreenKey.Settings
        val profile: ScreenKey = ScreenKey.ProfilePesantren

        val serializedManaqib = json.encodeToString(manaqibDetail)
        val deserializedManaqib = json.decodeFromString<ScreenKey>(serializedManaqib)

        assertEquals(manaqibDetail, deserializedManaqib)
        assertTrue(deserializedManaqib is ScreenKey.ManaqibDetail)
        assertEquals(7, (deserializedManaqib as ScreenKey.ManaqibDetail).chapterNumber)

        val serializedSurah = json.encodeToString(quranSurah)
        val deserializedSurah = json.decodeFromString<ScreenKey>(serializedSurah)
        assertEquals(36, (deserializedSurah as ScreenKey.QuranSurah).surahNumber)
    }

    @Test
    fun testScreenKeyListSaverPreservesBackstack() {
        val originalBackstack = androidx.compose.runtime.mutableStateListOf<ScreenKey>(
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

