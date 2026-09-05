package com.iqbalwork.robithoh.core.notification

import androidx.compose.runtime.Composable
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationSettings
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule

interface PrayerAlarmScheduler {
    fun scheduleAlarms(schedule: PrayerSchedule, settings: PrayerNotificationSettings)
    fun cancelAllAlarms()
    fun stopActiveAdzan()
    fun testTriggerNotification(
        prayerName: String,
        mode: com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode,
        voiceId: String,
        customPath: String?,
        volume: Float
    )
}

expect fun createPrayerAlarmScheduler(): PrayerAlarmScheduler

@Composable
expect fun rememberPrayerAlarmScheduler(): PrayerAlarmScheduler
