package com.iqbalwork.robithoh.core.datetime

import java.util.Calendar

actual fun currentEpochSeconds(): Long = System.currentTimeMillis() / 1000L

actual fun currentLocalDateTime(): DateTimeParts {
    val cal = Calendar.getInstance()
    return DateTimeParts(
        year = cal.get(Calendar.YEAR),
        month = cal.get(Calendar.MONTH) + 1,
        day = cal.get(Calendar.DAY_OF_MONTH),
        hour = cal.get(Calendar.HOUR_OF_DAY),
        minute = cal.get(Calendar.MINUTE),
        second = cal.get(Calendar.SECOND)
    )
}
