package com.iqbalwork.robithoh.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext
import com.iqbalwork.robithoh.feature.amaliyah.model.AdzanVoices
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationSettings
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType
import java.util.Calendar

class AndroidPrayerAlarmScheduler(private val context: Context) : PrayerAlarmScheduler {

    private val alarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    }

    override fun scheduleAlarms(schedule: PrayerSchedule, settings: PrayerNotificationSettings) {
        val voiceOption = AdzanVoices.findById(settings.selectedVoiceId)
        val voiceTitle = voiceOption.title
        val customPath = settings.customAudioPath

        val prayerEntries = listOf(
            Triple(PrayerType.IMSAK, schedule.imsak, 101),
            Triple(PrayerType.SUBUH, schedule.subuh, 102),
            Triple(PrayerType.DZUHUR, schedule.dzuhur, 103),
            Triple(PrayerType.ASHAR, schedule.ashar, 104),
            Triple(PrayerType.MAGHRIB, schedule.maghrib, 105),
            Triple(PrayerType.ISYA, schedule.isya, 106)
        )

        for ((prayerType, timeStr, reqCode) in prayerEntries) {
            val rawMode = settings.getPrayerMode(prayerType)
            val mode = if (prayerType == PrayerType.IMSAK && rawMode == com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN) {
                com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.PUSH_NOTIFICATION
            } else {
                rawMode
            }
            if (mode == com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.SILENT) {
                cancelAlarm(reqCode)
                continue
            }

            val audioFile = voiceOption.getAudioForPrayer(prayerType.label)

            val triggerMillis = computeNextTriggerMillis(timeStr)
            if (triggerMillis != null) {
                val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                    putExtra(PrayerAdzanService.EXTRA_PRAYER_NAME, prayerType.label)
                    putExtra(PrayerAdzanService.EXTRA_LOCATION_NAME, schedule.locationName)
                    putExtra(PrayerAdzanService.EXTRA_AUDIO_FILE, audioFile)
                    putExtra(PrayerAdzanService.EXTRA_CUSTOM_AUDIO_PATH, customPath)
                    putExtra(PrayerAdzanService.EXTRA_VOICE_TITLE, voiceTitle)
                    putExtra(PrayerAdzanService.EXTRA_NOTIFICATION_MODE, mode.id)
                }

                val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
                val pendingIntent = PendingIntent.getBroadcast(context, reqCode, intent, flags)

                val showIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                val showPendingIntent = if (showIntent != null) {
                    PendingIntent.getActivity(
                        context,
                        reqCode + 5000,
                        showIntent,
                        flags
                    )
                } else null

                try {
                    val canScheduleExact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        alarmManager?.canScheduleExactAlarms() ?: true
                    } else true

                    if (canScheduleExact && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && showPendingIntent != null) {
                        alarmManager?.setAlarmClock(
                            AlarmManager.AlarmClockInfo(triggerMillis, showPendingIntent),
                            pendingIntent
                        )
                    } else if (canScheduleExact && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager?.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            triggerMillis,
                            pendingIntent
                        )
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager?.setAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            triggerMillis,
                            pendingIntent
                        )
                    } else {
                        alarmManager?.set(
                            AlarmManager.RTC_WAKEUP,
                            triggerMillis,
                            pendingIntent
                        )
                    }
                } catch (_: Exception) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager?.setAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                triggerMillis,
                                pendingIntent
                            )
                        } else {
                            alarmManager?.set(
                                AlarmManager.RTC_WAKEUP,
                                triggerMillis,
                                pendingIntent
                            )
                        }
                    } catch (_: Exception) {}
                }
            }
        }
    }

    override fun cancelAllAlarms() {
        val reqCodes = listOf(101, 102, 103, 104, 105, 106)
        for (code in reqCodes) {
            cancelAlarm(code)
        }
    }

    private fun cancelAlarm(reqCode: Int) {
        val intent = Intent(context, PrayerAlarmReceiver::class.java)
        val flags = PendingIntent.FLAG_NO_CREATE or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pendingIntent = PendingIntent.getBroadcast(context, reqCode, intent, flags)
        if (pendingIntent != null) {
            alarmManager?.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    override fun stopActiveAdzan() {
        val stopIntent = Intent(context, PrayerAdzanService::class.java).apply {
            action = PrayerAdzanService.ACTION_STOP_ADZAN
        }
        try {
            context.startService(stopIntent)
        } catch (_: Exception) {}
    }

    override fun testTriggerNotification(
        prayerName: String,
        mode: com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode,
        voiceId: String,
        customPath: String?
    ) {
        val effectiveMode = if (prayerName.equals("Imsak", ignoreCase = true)) {
            if (mode == com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.SILENT) {
                com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.SILENT
            } else {
                com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.PUSH_NOTIFICATION
            }
        } else {
            mode
        }

        if (effectiveMode == com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.SILENT) return

        val voiceOption = AdzanVoices.findById(voiceId)
        val audioFile = voiceOption.getAudioForPrayer(prayerName)
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            putExtra(PrayerAdzanService.EXTRA_PRAYER_NAME, prayerName)
            putExtra(PrayerAdzanService.EXTRA_LOCATION_NAME, "Uji Coba Notifikasi")
            putExtra(PrayerAdzanService.EXTRA_AUDIO_FILE, audioFile)
            putExtra(PrayerAdzanService.EXTRA_CUSTOM_AUDIO_PATH, customPath)
            putExtra(PrayerAdzanService.EXTRA_VOICE_TITLE, voiceOption.title)
            putExtra(PrayerAdzanService.EXTRA_NOTIFICATION_MODE, effectiveMode.id)
        }
        context.sendBroadcast(intent)
    }

    private fun computeNextTriggerMillis(timeStr: String): Long? {
        val parts = timeStr.trim().split(":")
        if (parts.size < 2) return null
        val hour = parts[0].toIntOrNull() ?: return null
        val minute = parts[1].toIntOrNull() ?: return null

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val diffSec = (now.timeInMillis - target.timeInMillis) / 1000
        if (diffSec in 0..59) {
            // Target is in the current minute (e.g. user testing now) -> trigger in 2 seconds
            return now.timeInMillis + 2000L
        } else if (target.before(now)) {
            // Schedule for next day if already passed today
            target.add(Calendar.DAY_OF_MONTH, 1)
        }

        return target.timeInMillis
    }

    companion object {
        fun rescheduleFromDatabase(context: Context) {
            try {
                val db = com.iqbalwork.robithoh.core.database.createDatabase(
                    com.iqbalwork.robithoh.core.database.DatabaseDriverFactory(context)
                )
                val settings = db.robithohDatabaseQueries.getPrayerSettings().executeAsOneOrNull() ?: return

                val method = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerCalculationMethods.findById(settings.method_id)
                val adjustments = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerTimeAdjustments(
                    imsak = settings.imsak_offset.toInt(),
                    subuh = settings.subuh_offset.toInt(),
                    terbit = settings.terbit_offset.toInt(),
                    dzuhur = settings.dzuhur_offset.toInt(),
                    ashar = settings.ashar_offset.toInt(),
                    maghrib = settings.maghrib_offset.toInt(),
                    isya = settings.isya_offset.toInt()
                )
                val location = if (settings.custom_lat != null && settings.custom_lng != null) {
                    com.iqbalwork.robithoh.feature.amaliyah.model.LocationPreset(
                        name = settings.custom_location_name ?: "Lokasi Tersimpan",
                        latitude = settings.custom_lat,
                        longitude = settings.custom_lng,
                        timezoneOffset = settings.custom_timezone_offset ?: 7.0,
                        province = if (settings.is_gps == 1L) "GPS" else "Manual"
                    )
                } else {
                    com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator.DEFAULT_LOCATION
                }

                val notifSettings = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationSettings(
                    subuhMode = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.fromDbValue(settings.subuh_notif_enabled),
                    dzuhurMode = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.fromDbValue(settings.dzuhur_notif_enabled),
                    asharMode = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.fromDbValue(settings.ashar_notif_enabled),
                    maghribMode = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.fromDbValue(settings.maghrib_notif_enabled),
                    isyaMode = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.fromDbValue(settings.isya_notif_enabled),
                    imsakMode = com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.fromDbValue(settings.imsak_notif_enabled).let {
                        if (it == com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN) {
                            com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.PUSH_NOTIFICATION
                        } else it
                    },
                    selectedVoiceId = settings.selected_adzan_voice_id,
                    customAudioPath = settings.custom_adzan_audio_path
                )

                val now = com.iqbalwork.robithoh.core.datetime.currentLocalDateTime()
                val schedule = com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator().calculateSchedule(
                    year = now.year,
                    month = now.month,
                    day = now.day,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timezoneOffset = location.timezoneOffset,
                    locationName = location.name,
                    method = method,
                    adjustments = adjustments
                )

                AndroidPrayerAlarmScheduler(context).scheduleAlarms(schedule, notifSettings)
            } catch (_: Throwable) {}
        }
    }
}

actual fun createPrayerAlarmScheduler(): PrayerAlarmScheduler {
    val ctx = getGlobalAppContext() ?: error("Global App Context not initialized")
    return AndroidPrayerAlarmScheduler(ctx)
}

@Composable
actual fun rememberPrayerAlarmScheduler(): PrayerAlarmScheduler {
    val context = LocalContext.current.applicationContext
    return remember(context) { AndroidPrayerAlarmScheduler(context) }
}
