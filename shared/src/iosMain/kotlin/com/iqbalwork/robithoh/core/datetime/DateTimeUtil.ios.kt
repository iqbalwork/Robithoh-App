package com.iqbalwork.robithoh.core.datetime

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
actual fun currentEpochSeconds(): Long = NSDate().timeIntervalSince1970.toLong()

@OptIn(ExperimentalForeignApi::class)
actual fun currentLocalDateTime(): DateTimeParts {
    val date = NSDate()
    val calendar = NSCalendar.currentCalendar
    val components = calendar.components(
        NSCalendarUnitYear or
        NSCalendarUnitMonth or
        NSCalendarUnitDay or
        NSCalendarUnitHour or
        NSCalendarUnitMinute or
        NSCalendarUnitSecond,
        fromDate = date
    )
    return DateTimeParts(
        year = components.year.toInt(),
        month = components.month.toInt(),
        day = components.day.toInt(),
        hour = components.hour.toInt(),
        minute = components.minute.toInt(),
        second = components.second.toInt()
    )
}
