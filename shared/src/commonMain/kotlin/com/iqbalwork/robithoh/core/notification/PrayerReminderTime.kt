package com.iqbalwork.robithoh.core.notification

/**
 * Hitung waktu pengingat (default 10 menit sebelum) untuk jadwal harian repeating.
 * Mengembalikan null bila hasil jatuh ke hari sebelumnya — semantik Android:
 * pre-trigger yang melewati tengah malam dibatalkan (tidak dijadwalkan).
 */
object PrayerReminderTime {
    const val DEFAULT_MINUTES_BEFORE = 10

    fun compute(hour: Int, minute: Int, minutesBefore: Int = DEFAULT_MINUTES_BEFORE): Pair<Int, Int>? {
        val total = hour * 60 + minute - minutesBefore
        if (total < 0) return null
        return (total / 60) to (total % 60)
    }
}
