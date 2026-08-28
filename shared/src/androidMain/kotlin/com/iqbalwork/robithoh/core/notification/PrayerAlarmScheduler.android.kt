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
            val mode = settings.getPrayerMode(prayerType)
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && showPendingIntent != null) {
                        alarmManager?.setAlarmClock(
                            AlarmManager.AlarmClockInfo(triggerMillis, showPendingIntent),
                            pendingIntent
                        )
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager?.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            triggerMillis,
                            pendingIntent
                        )
                    } else {
                        alarmManager?.setExact(
                            AlarmManager.RTC_WAKEUP,
                            triggerMillis,
                            pendingIntent
                        )
                    }
                } catch (_: Exception) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager?.setExactAndAllowWhileIdle(
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
        val voiceOption = AdzanVoices.findById(voiceId)
        val audioFile = voiceOption.getAudioForPrayer(prayerName)
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            putExtra(PrayerAdzanService.EXTRA_PRAYER_NAME, prayerName)
            putExtra(PrayerAdzanService.EXTRA_LOCATION_NAME, "Uji Coba Notifikasi")
            putExtra(PrayerAdzanService.EXTRA_AUDIO_FILE, audioFile)
            putExtra(PrayerAdzanService.EXTRA_CUSTOM_AUDIO_PATH, customPath)
            putExtra(PrayerAdzanService.EXTRA_VOICE_TITLE, voiceOption.title)
            putExtra(PrayerAdzanService.EXTRA_NOTIFICATION_MODE, mode.id)
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
