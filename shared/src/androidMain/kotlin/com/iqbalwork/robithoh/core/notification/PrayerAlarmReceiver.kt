package com.iqbalwork.robithoh.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED
        ) {
            AndroidPrayerAlarmScheduler.rescheduleFromDatabase(context)
            return
        }

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? android.os.PowerManager
        val wakeLock = powerManager?.newWakeLock(
            android.os.PowerManager.PARTIAL_WAKE_LOCK,
            "robithoh:PrayerAlarmWakeLock"
        )
        try {
            wakeLock?.acquire(15_000L) // 15 seconds timeout
        } catch (_: Exception) {}

        val isPreReminder = intent.getBooleanExtra(PrayerAdzanService.EXTRA_IS_PRE_REMINDER, false)
        val prayerName = intent.getStringExtra(PrayerAdzanService.EXTRA_PRAYER_NAME) ?: "Sholat"
        val locationName = intent.getStringExtra(PrayerAdzanService.EXTRA_LOCATION_NAME) ?: "Wilayah Anda"

        if (isPreReminder) {
            showPrePrayerReminderNotification(context, prayerName, locationName)
            return
        }

        val audioFile = intent.getStringExtra(PrayerAdzanService.EXTRA_AUDIO_FILE) ?: "adzan_misyari_rasyid.mp3"
        val customPath = intent.getStringExtra(PrayerAdzanService.EXTRA_CUSTOM_AUDIO_PATH)
        val voiceTitle = intent.getStringExtra(PrayerAdzanService.EXTRA_VOICE_TITLE) ?: "Adzan"
        val mode = intent.getStringExtra(PrayerAdzanService.EXTRA_NOTIFICATION_MODE) ?: "adzan"

        val isTest = intent.getBooleanExtra(PrayerAdzanService.EXTRA_IS_TEST, false)

        val isImsak = prayerName.equals("Imsak", ignoreCase = true)
        if (isImsak || mode.equals("push", ignoreCase = true)) {
            showPushNotification(context, prayerName, locationName)
        } else {
            val serviceIntent = Intent(context, PrayerAdzanService::class.java).apply {
                this.action = PrayerAdzanService.ACTION_PLAY_ADZAN
                putExtra(PrayerAdzanService.EXTRA_PRAYER_NAME, prayerName)
                putExtra(PrayerAdzanService.EXTRA_LOCATION_NAME, locationName)
                putExtra(PrayerAdzanService.EXTRA_AUDIO_FILE, audioFile)
                putExtra(PrayerAdzanService.EXTRA_CUSTOM_AUDIO_PATH, customPath)
                putExtra(PrayerAdzanService.EXTRA_VOICE_TITLE, voiceTitle)
            }

            var serviceStarted = false
            try {
                ContextCompat.startForegroundService(context, serviceIntent)
                serviceStarted = true
            } catch (_: Throwable) {
                try {
                    context.startService(serviceIntent)
                    serviceStarted = true
                } catch (_: Throwable) {
                    serviceStarted = false
                }
            }

            // CRITICAL FALLBACK: If starting foreground service failed or was blocked by Android,
            // immediately show push notification so the user never misses the prayer notification!
            if (!serviceStarted) {
                showPushNotification(context, prayerName, locationName)
            }
        }

        // Reschedule future prayer alarms to keep the alarm schedule refreshed (only for real alarms)
        if (!isTest) {
            try {
                AndroidPrayerAlarmScheduler.rescheduleFromDatabase(context)
            } catch (_: Throwable) {}
        }
    }

    private fun showPrePrayerReminderNotification(context: Context, prayerName: String, locationName: String) {
        val channelId = "pre_prayer_reminder_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pengingat 10 Menit Sebelum Sholat",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi pengingat 10 menit sebelum waktu sholat tiba"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 150, 250)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }
            notificationManager?.createNotificationChannel(channel)
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = if (launchIntent != null) {
            PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )
        } else null

        val customIconId = context.resources.getIdentifier("ic_stat_prayer", "drawable", context.packageName)
        val iconRes = if (customIconId != 0) customIconId else android.R.drawable.ic_lock_idle_alarm
        val launcherIconId = context.resources.getIdentifier("ic_launcher", "mipmap", context.packageName)
        val largeIconBitmap = if (launcherIconId != 0) {
            try {
                android.graphics.BitmapFactory.decodeResource(context.resources, launcherIconId)
            } catch (_: Exception) {
                null
            }
        } else null

        val notifTitle = "10 Menit Menuju Waktu $prayerName"
        val notifText = "Waktu sholat $prayerName akan tiba dalam 10 menit untuk wilayah $locationName"

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .apply {
                if (largeIconBitmap != null) {
                    setLargeIcon(largeIconBitmap)
                }
            }
            .setContentTitle(notifTitle)
            .setContentText(notifText)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Persiapan menunaikan ibadah sholat $prayerName. Waktu sholat akan tiba dalam 10 menit untuk wilayah $locationName."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager?.notify((prayerName.hashCode() + 5000), notification)
    }

    private fun showPushNotification(context: Context, prayerName: String, locationName: String) {
        val channelId = "prayer_push_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi Waktu Sholat & Imsak",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Push notifikasi saat masuk waktu sholat & imsak"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }
            notificationManager?.createNotificationChannel(channel)
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = if (launchIntent != null) {
            PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )
        } else null

        val customIconId = context.resources.getIdentifier("ic_stat_prayer", "drawable", context.packageName)
        val iconRes = if (customIconId != 0) customIconId else android.R.drawable.ic_lock_idle_alarm
        val launcherIconId = context.resources.getIdentifier("ic_launcher", "mipmap", context.packageName)
        val largeIconBitmap = if (launcherIconId != 0) {
            try {
                android.graphics.BitmapFactory.decodeResource(context.resources, launcherIconId)
            } catch (_: Exception) {
                null
            }
        } else null

        val isImsak = prayerName.equals("Imsak", ignoreCase = true)
        val notifTitle = if (isImsak) "Waktu Imsak" else "Waktu Sholat $prayerName Telah Tiba"
        val notifText = if (isImsak) "Memasuki waktu Imsak untuk wilayah $locationName" else "Saatnya menunaikan sholat $prayerName untuk wilayah $locationName"

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .apply {
                if (largeIconBitmap != null) {
                    setLargeIcon(largeIconBitmap)
                }
            }
            .setContentTitle(notifTitle)
            .setContentText(notifText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notifText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager?.notify(prayerName.hashCode(), notification)
    }
}
