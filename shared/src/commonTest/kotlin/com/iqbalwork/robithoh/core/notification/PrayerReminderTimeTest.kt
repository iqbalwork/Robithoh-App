package com.iqbalwork.robithoh.core.notification

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PrayerReminderTimeTest {

    @Test
    fun subtractsTenMinutesNormally() {
        assertEquals(4 to 23, PrayerReminderTime.compute(4, 33))
        assertEquals(11 to 50, PrayerReminderTime.compute(12, 0))
        assertEquals(5 to 55, PrayerReminderTime.compute(6, 5))
        assertEquals(3 to 50, PrayerReminderTime.compute(4, 0))
    }

    @Test
    fun returnsNullWhenCrossingMidnight() {
        // Semantik Android: pre-trigger yang jatuh ke hari sebelumnya dibatalkan
        assertNull(PrayerReminderTime.compute(0, 5))
        assertNull(PrayerReminderTime.compute(0, 0))
    }

    @Test
    fun supportsCustomMinutesBefore() {
        assertEquals(4 to 23, PrayerReminderTime.compute(4, 33, minutesBefore = 10))
        assertEquals(4 to 3, PrayerReminderTime.compute(4, 33, minutesBefore = 30))
        assertNull(PrayerReminderTime.compute(0, 5, minutesBefore = 10))
    }
}
