package com.iqbalwork.robithoh.navigation

data class WidgetNavTarget(
    val destination: String,
    val surahNumber: Int = 1,
    val ayahNumber: Int = 1,
    val timestamp: Long = 0L
)
