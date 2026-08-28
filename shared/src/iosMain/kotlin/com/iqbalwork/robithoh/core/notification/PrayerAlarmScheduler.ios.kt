package com.iqbalwork.robithoh.core.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.feature.amaliyah.model.AdzanVoices
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationSettings
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType
import platform.Foundation.NSDateComponents
import platform.UserNotifications.*

class IosPrayerAlarmScheduler : PrayerAlarmScheduler {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    private val audioPlayer = createAudioPlayer()

    override fun scheduleAlarms(schedule: PrayerSchedule, settings: PrayerNotificationSettings) {
        val authOptions = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        notificationCenter.requestAuthorizationWithOptions(authOptions) { granted, _ ->
            if (granted) {
                doScheduleAlarms(schedule, settings)
            }
        }
    }

    private fun doScheduleAlarms(schedule: PrayerSchedule, settings: PrayerNotificationSettings) {
        val voiceOption = AdzanVoices.findById(settings.selectedVoiceId)
        val voiceTitle = voiceOption.title

        val prayerEntries = listOf(
            Triple(PrayerType.IMSAK, schedule.imsak, "prayer_alarm_imsak"),
            Triple(PrayerType.SUBUH, schedule.subuh, "prayer_alarm_subuh"),
            Triple(PrayerType.DZUHUR, schedule.dzuhur, "prayer_alarm_dzuhur"),
            Triple(PrayerType.ASHAR, schedule.ashar, "prayer_alarm_ashar"),
            Triple(PrayerType.MAGHRIB, schedule.maghrib, "prayer_alarm_maghrib"),
            Triple(PrayerType.ISYA, schedule.isya, "prayer_alarm_isya")
        )

        for ((prayerType, timeStr, identifier) in prayerEntries) {
            val mode = settings.getPrayerMode(prayerType)
            if (mode == PrayerNotificationMode.SILENT) {
                cancelAlarm(identifier)
                continue
            }

            val parts = timeStr.trim().split(":")
            if (parts.size < 2) continue
            val hour = parts[0].toLongOrNull() ?: continue
            val minute = parts[1].toLongOrNull() ?: continue

            val content = UNMutableNotificationContent().apply {
                setTitle("Waktu Sholat ${prayerType.label} Telah Tiba")
                setBody("Saatnya menunaikan sholat ${prayerType.label} untuk wilayah ${schedule.locationName}")
                setSound(UNNotificationSound.defaultSound())
                setUserInfo(mapOf(
                    "prayerName" to prayerType.label,
                    "locationName" to schedule.locationName,
                    "mode" to mode.id,
                    "voiceId" to settings.selectedVoiceId,
                    "voiceTitle" to voiceTitle
                ))
            }

            val dateComponents = NSDateComponents().apply {
                setHour(hour)
                setMinute(minute)
                setSecond(0)
            }

            val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
                dateComponents = dateComponents,
                repeats = true
            )

            val request = UNNotificationRequest.requestWithIdentifier(
                identifier = identifier,
                content = content,
                trigger = trigger
            )

            notificationCenter.addNotificationRequest(request) { _ ->
                // Registered
            }
        }
    }

    override fun cancelAllAlarms() {
        val identifiers = listOf(
            "prayer_alarm_imsak",
            "prayer_alarm_subuh",
            "prayer_alarm_dzuhur",
            "prayer_alarm_ashar",
            "prayer_alarm_maghrib",
            "prayer_alarm_isya"
        )
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(identifiers)
        stopActiveAdzan()
    }

    private fun cancelAlarm(identifier: String) {
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(listOf(identifier))
    }

    override fun stopActiveAdzan() {
        audioPlayer.stop()
    }

    override fun testTriggerNotification(
        prayerName: String,
        mode: PrayerNotificationMode,
        voiceId: String,
        customPath: String?
    ) {
        if (mode == PrayerNotificationMode.SILENT) return
        val content = UNMutableNotificationContent().apply {
            setTitle("Waktu $prayerName (Uji Coba)")
            setBody("Waktu sholat $prayerName telah masuk.")
            setSound(UNNotificationSound.defaultSound())
        }
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = "test_alarm_${prayerName.lowercase()}",
            content = content,
            trigger = null
        )
        notificationCenter.addNotificationRequest(request) { _ -> }
    }
}

actual fun createPrayerAlarmScheduler(): PrayerAlarmScheduler = IosPrayerAlarmScheduler()

@Composable
actual fun rememberPrayerAlarmScheduler(): PrayerAlarmScheduler {
    return remember { IosPrayerAlarmScheduler() }
}
