package com.iqbalwork.robithoh.core.datetime

data class DateTimeParts(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
)

expect fun currentEpochSeconds(): Long
expect fun currentLocalDateTime(): DateTimeParts

/**
 * Formats Gregorian date to human-readable Indonesian string (e.g. "Kamis, 27 Agustus 2026")
 */
fun formatIndonesianDate(year: Int, month: Int, day: Int): String {
    val dayName = getDayOfWeekIndonesian(year, month, day)
    val monthName = when (month) {
        1 -> "Januari"
        2 -> "Februari"
        3 -> "Maret"
        4 -> "April"
        5 -> "Mei"
        6 -> "Juni"
        7 -> "Juli"
        8 -> "Agustus"
        9 -> "September"
        10 -> "Oktober"
        11 -> "November"
        12 -> "Desember"
        else -> "Januari"
    }
    return "$dayName, $day $monthName $year"
}

fun getDayOfWeekIndonesian(year: Int, month: Int, day: Int): String {
    val t = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
    var y = year
    if (month < 3) y -= 1
    val dayOfWeek = (y + y / 4 - y / 100 + y / 400 + t[month - 1] + day) % 7
    return when (dayOfWeek) {
        0 -> "Minggu"
        1 -> "Senin"
        2 -> "Selasa"
        3 -> "Rabu"
        4 -> "Kamis"
        5 -> "Jumat"
        6 -> "Sabtu"
        else -> "Senin"
    }
}

/**
 * Shifts a Gregorian date by a delta number of days (+/-).
 */
fun shiftDate(year: Int, month: Int, day: Int, offsetDays: Int): Triple<Int, Int, Int> {
    if (offsetDays == 0) return Triple(year, month, day)
    val a = (14 - month) / 12
    val y = year + 4800 - a
    val m = month + 12 * a - 3
    var jdn = day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045
    jdn += offsetDays

    val l = jdn + 68569
    val n = (4 * l) / 146097
    val l2 = l - (146097 * n + 3) / 4
    val i = (4000 * (l2 + 1)) / 1461001
    val l3 = l2 - (1461 * i) / 4 + 31
    val j = (80 * l3) / 2447
    val newDay = l3 - (2447 * j) / 80
    val l4 = j / 11
    val newMonth = j + 2 - (12 * l4)
    val newYear = 100 * (n - 49) + i + l4

    return Triple(newYear, newMonth, newDay)
}

/**
 * Computes estimated Hijri Date string for a Gregorian date (e.g. "14 Rabiul Awal 1448 H")
 */
fun getHijriDateFormatted(year: Int, month: Int, day: Int): String {
    val a = (14 - month) / 12
    val y = year + 4800 - a
    val m = month + 12 * a - 3
    val jd = day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045

    val l = jd - 1948440 + 10632
    val n = ((l - 1) / 10631)
    val l1 = l - 10631 * n + 354
    val j = ((10985 - l1) / 5316) * ((50 * l1) / 17719) + (l1 / 5670) * ((43 * l1) / 15238)
    val l2 = l1 - ((30 - j) / 15) * ((17719 * j) / 50) - (j / 16) * ((15238 * j) / 43) + 29
    val hijriMonth = (24 * l2) / 709
    val hijriDay = l2 - (709 * hijriMonth) / 24
    val hijriYear = 30 * n + j - 30

    val monthName = when (hijriMonth) {
        1 -> "Muharram"
        2 -> "Safar"
        3 -> "Rabiul Awal"
        4 -> "Rabiul Akhir"
        5 -> "Jumadil Awal"
        6 -> "Jumadil Akhir"
        7 -> "Rajab"
        8 -> "Sya'ban"
        9 -> "Ramadhan"
        10 -> "Syawal"
        11 -> "Dzulqa'dah"
        12 -> "Dzulhijjah"
        else -> "Rabiul Awal"
    }
    return "$hijriDay $monthName $hijriYear H"
}
