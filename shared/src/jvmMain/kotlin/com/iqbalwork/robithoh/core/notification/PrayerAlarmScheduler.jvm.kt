package com.iqbalwork.robithoh.core.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationSettings
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule

class JvmPrayerAlarmScheduler : PrayerAlarmScheduler {
    override fun scheduleAlarms(schedule: PrayerSchedule, settings: PrayerNotificationSettings) {
        // Desktop / JVM stub
    }

    override fun cancelAllAlarms() {
        // Desktop / JVM stub
    }

    override fun stopActiveAdzan() {
        // Desktop / JVM stub
    }

    override fun testTriggerNotification(
        prayerName: String,
        mode: com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode,
        voiceId: String,
        customPath: String?
    ) {
        // Desktop / JVM stub
    }
}

actual fun createPrayerAlarmScheduler(): PrayerAlarmScheduler = JvmPrayerAlarmScheduler()

@Composable
actual fun rememberPrayerAlarmScheduler(): PrayerAlarmScheduler {
    return remember { JvmPrayerAlarmScheduler() }
}
