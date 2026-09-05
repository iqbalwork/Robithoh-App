@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.iqbalwork.robithoh.core.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.amaliyah.model.AdzanVoices
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationSettings
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDateComponents
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationAction
import platform.UserNotifications.UNNotificationActionOptionDestructive
import platform.UserNotifications.UNNotificationActionOptionForeground
import platform.UserNotifications.UNNotificationCategory
import platform.UserNotifications.UNNotificationCategoryOptionCustomDismissAction
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter

class IosPrayerAlarmScheduler : PrayerAlarmScheduler {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    private val audioPlayer = createAudioPlayer()
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        registerNotificationCategories()
    }

    private fun registerNotificationCategories() {
        val stopAction = UNNotificationAction.actionWithIdentifier(
            identifier = "ACTION_STOP_ADZAN",
            title = "Matikan Adzan",
            options = UNNotificationActionOptionDestructive
        )
        val openAction = UNNotificationAction.actionWithIdentifier(
            identifier = "ACTION_OPEN_APP",
            title = "Buka Aplikasi",
            options = UNNotificationActionOptionForeground
        )
        val category = UNNotificationCategory.categoryWithIdentifier(
            identifier = "PRAYER_NOTIFICATION",
            actions = listOf(stopAction, openAction),
            intentIdentifiers = emptyList<Any?>(),
            options = UNNotificationCategoryOptionCustomDismissAction
        )
        notificationCenter.setNotificationCategories(setOf(category))
    }

    override fun scheduleAlarms(schedule: PrayerSchedule, settings: PrayerNotificationSettings) {
        val authOptions = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        notificationCenter.requestAuthorizationWithOptions(authOptions) { granted, _ ->
            if (granted) {
                scope.launch {
                    doScheduleAlarms(schedule, settings)
                }
            }
        }
    }

    private suspend fun doScheduleAlarms(schedule: PrayerSchedule, settings: PrayerNotificationSettings) {
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
            val preIdentifier = "prayer_pre_" + identifier.removePrefix("prayer_alarm_")
            val rawMode = settings.getPrayerMode(prayerType)
            val isImsak = prayerType == PrayerType.IMSAK
            val mode = if (isImsak && rawMode == PrayerNotificationMode.ADZAN) {
                PrayerNotificationMode.PUSH_NOTIFICATION
            } else {
                rawMode
            }
            if (mode == PrayerNotificationMode.SILENT) {
                cancelAlarm(identifier)
                cancelAlarm(preIdentifier)
                continue
            }

            val parts = timeStr.trim().split(":")
            if (parts.size < 2) continue
            val hour = parts[0].toLongOrNull() ?: continue
            val minute = parts[1].toLongOrNull() ?: continue

            val audioFileName = voiceOption.getAudioForPrayer(prayerType.label)
            val soundToUse = when (mode) {
                PrayerNotificationMode.ADZAN -> {
                    if (!isImsak) {
                        prepareNotificationSoundInLibrary(audioFileName)
                        UNNotificationSound.soundNamed(audioFileName)
                    } else {
                        UNNotificationSound.defaultSound()
                    }
                }
                PrayerNotificationMode.PUSH_NOTIFICATION -> {
                    UNNotificationSound.defaultSound()
                }
                PrayerNotificationMode.SILENT -> null
            }

            val notifTitle = if (isImsak) "Waktu Imsak" else "Waktu Sholat ${prayerType.label} Telah Tiba"
            val notifBody = if (isImsak) "Memasuki waktu Imsak untuk wilayah ${schedule.locationName}" else "Saatnya menunaikan sholat ${prayerType.label} untuk wilayah ${schedule.locationName}"

            val content = UNMutableNotificationContent().apply {
                setTitle(notifTitle)
                setBody(notifBody)
                if (soundToUse != null) {
                    setSound(soundToUse)
                }
                setCategoryIdentifier("PRAYER_NOTIFICATION")
                setUserInfo(mapOf(
                    "prayerName" to prayerType.label,
                    "locationName" to schedule.locationName,
                    "mode" to mode.id,
                    "voiceId" to settings.selectedVoiceId,
                    "voiceTitle" to voiceTitle,
                    "audioFile" to audioFileName
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

            val preReminderTime = if (settings.isPrePrayerReminderEnabled) {
                PrayerReminderTime.compute(hour.toInt(), minute.toInt())
            } else {
                null
            }
            if (preReminderTime != null) {
                val (preHour, preMinute) = preReminderTime
                val preContent = UNMutableNotificationContent().apply {
                    setTitle("10 Menit Menuju Waktu ${prayerType.label}")
                    setBody("Waktu sholat ${prayerType.label} akan tiba dalam 10 menit untuk wilayah ${schedule.locationName}")
                    setSound(UNNotificationSound.defaultSound())
                    setUserInfo(mapOf(
                        "prayerName" to prayerType.label,
                        "type" to "pre_reminder"
                    ))
                }
                val preTrigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
                    dateComponents = NSDateComponents().apply {
                        setHour(preHour.toLong())
                        setMinute(preMinute.toLong())
                        setSecond(0)
                    },
                    repeats = true
                )
                notificationCenter.addNotificationRequest(
                    UNNotificationRequest.requestWithIdentifier(
                        identifier = preIdentifier,
                        content = preContent,
                        trigger = preTrigger
                    )
                ) { _ -> }
            } else {
                cancelAlarm(preIdentifier)
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
            "prayer_alarm_isya",
            "prayer_pre_imsak",
            "prayer_pre_subuh",
            "prayer_pre_dzuhur",
            "prayer_pre_ashar",
            "prayer_pre_maghrib",
            "prayer_pre_isya"
        )
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(identifiers)
        stopActiveAdzan()
    }

    private fun cancelAlarm(identifier: String) {
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(listOf(identifier))
    }

    override fun stopActiveAdzan() {
        com.iqbalwork.robithoh.core.audio.IosAudioPlayer.stopGlobalPlayback()
        audioPlayer.stop()
    }

    override fun testTriggerNotification(
        prayerName: String,
        mode: PrayerNotificationMode,
        voiceId: String,
        customPath: String?,
        volume: Float
    ) {
        // Catatan: volume tidak relevan di iOS - notifikasi memakai system volume
        val isImsak = prayerName.equals("Imsak", ignoreCase = true)
        val effectiveMode = if (isImsak) {
            if (mode == PrayerNotificationMode.SILENT) PrayerNotificationMode.SILENT else PrayerNotificationMode.PUSH_NOTIFICATION
        } else {
            mode
        }
        if (effectiveMode == PrayerNotificationMode.SILENT) return

        scope.launch {
            val voiceOption = AdzanVoices.findById(voiceId)
            val audioFileName = voiceOption.getAudioForPrayer(prayerName)

            val soundToUse = when (effectiveMode) {
                PrayerNotificationMode.ADZAN -> {
                    prepareNotificationSoundInLibrary(audioFileName)
                    withContext(Dispatchers.Main) {
                        audioPlayer.play(
                            AudioTrack(
                                id = "test_adzan_$prayerName",
                                title = "Adzan $prayerName (Uji Coba)",
                                subtitle = voiceOption.title,
                                urlOrPath = audioFileName
                            )
                        )
                    }
                    UNNotificationSound.soundNamed(audioFileName)
                }
                PrayerNotificationMode.PUSH_NOTIFICATION -> {
                    UNNotificationSound.defaultSound()
                }
                PrayerNotificationMode.SILENT -> null
            }

            val notifTitle = if (isImsak) "Waktu Imsak (Uji Coba)" else "Waktu $prayerName (Uji Coba)"
            val notifBody = if (isImsak) "Memasuki waktu Imsak untuk wilayah Wilayah Anda" else "Waktu sholat $prayerName telah masuk."

            val content = UNMutableNotificationContent().apply {
                setTitle(notifTitle)
                setBody(notifBody)
                if (soundToUse != null) {
                    setSound(soundToUse)
                }
                setCategoryIdentifier("PRAYER_NOTIFICATION")
                setUserInfo(mapOf(
                    "prayerName" to prayerName,
                    "mode" to effectiveMode.id,
                    "voiceId" to voiceId,
                    "audioFile" to audioFileName
                ))
            }

            val request = UNNotificationRequest.requestWithIdentifier(
                identifier = "test_alarm_${prayerName.lowercase()}",
                content = content,
                trigger = null
            )
            notificationCenter.addNotificationRequest(request) { _ -> }
        }
    }

    private suspend fun prepareNotificationSoundInLibrary(audioFileName: String) {
        try {
            val fileManager = NSFileManager.defaultManager
            val libraryDir = NSSearchPathForDirectoriesInDomains(
                NSLibraryDirectory,
                NSUserDomainMask,
                true
            ).firstOrNull() as? String ?: return

            val soundsDir = "$libraryDir/Sounds"
            if (!fileManager.fileExistsAtPath(soundsDir)) {
                fileManager.createDirectoryAtPath(
                    path = soundsDir,
                    withIntermediateDirectories = true,
                    attributes = null,
                    error = null
                )
            }

            val targetPath = "$soundsDir/$audioFileName"
            if (!fileManager.fileExistsAtPath(targetPath)) {
                val resourcePath = if (audioFileName.startsWith("files/")) audioFileName else "files/$audioFileName"
                val bytes = org.jetbrains.compose.resources.ExperimentalResourceApi::class.let {
                    robithohapp.shared.generated.resources.Res.readBytes(resourcePath)
                }
                if (bytes.isNotEmpty()) {
                    val nsData = bytes.toNSData()
                    fileManager.createFileAtPath(targetPath, contents = nsData, attributes = null)
                }
            }
        } catch (_: Throwable) {}
    }

    private fun ByteArray.toNSData(): NSData {
        if (this.isEmpty()) return NSData()
        return this.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(0),
                length = this.size.toULong()
            )
        }
    }
}

actual fun createPrayerAlarmScheduler(): PrayerAlarmScheduler = IosPrayerAlarmScheduler()

@Composable
actual fun rememberPrayerAlarmScheduler(): PrayerAlarmScheduler {
    return remember { IosPrayerAlarmScheduler() }
}
