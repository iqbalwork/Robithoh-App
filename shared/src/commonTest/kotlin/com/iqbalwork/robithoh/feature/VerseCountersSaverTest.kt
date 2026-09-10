package com.iqbalwork.robithoh.feature

import androidx.compose.runtime.saveable.SaverScope
import com.iqbalwork.robithoh.feature.reader.ui.VerseCountersSaver
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VerseCountersSaverTest {

    private val dummyScope = object : SaverScope {
        override fun canBeSaved(value: Any): Boolean = true
    }

    @Test
    fun testSaveAndRestoreCounters() {
        val original = mapOf(1 to 3, 5 to 10, 12 to 165)
        val saved = with(VerseCountersSaver) { dummyScope.save(original) }
        val restored = VerseCountersSaver.restore(saved!!)

        assertEquals(original, restored)
    }

    @Test
    fun testSaveAndRestoreEmptyMap() {
        val original = emptyMap<Int, Int>()
        val saved = with(VerseCountersSaver) { dummyScope.save(original) }
        val restored = VerseCountersSaver.restore(saved!!)

        assertTrue(restored?.isEmpty() == true)
    }

    @Test
    fun testRestoreWithCorruptOrInvalidData() {
        val invalidSaved = listOf("invalid", "a:b", "10:", ":20", "4:12")
        val restored = VerseCountersSaver.restore(invalidSaved)

        assertEquals(mapOf(4 to 12), restored)
    }
}
