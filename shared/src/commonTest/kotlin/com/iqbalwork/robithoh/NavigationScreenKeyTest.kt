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
}
