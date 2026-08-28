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
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? android.os.PowerManager
        val wakeLock = powerManager?.newWakeLock(
            android.os.PowerManager.PARTIAL_WAKE_LOCK,
            "robithoh:PrayerAlarmWakeLock"
        )
        try {
            wakeLock?.acquire(10_000L) // 10 seconds timeout
        } catch (_: Exception) {}

        val prayerName = intent.getStringExtra(PrayerAdzanService.EXTRA_PRAYER_NAME) ?: "Sholat"
        val locationName = intent.getStringExtra(PrayerAdzanService.EXTRA_LOCATION_NAME) ?: "Wilayah Anda"
        val audioFile = intent.getStringExtra(PrayerAdzanService.EXTRA_AUDIO_FILE) ?: "adzan_misyari_rasyid.mp3"
        val customPath = intent.getStringExtra(PrayerAdzanService.EXTRA_CUSTOM_AUDIO_PATH)
        val voiceTitle = intent.getStringExtra(PrayerAdzanService.EXTRA_VOICE_TITLE) ?: "Adzan"
        val mode = intent.getStringExtra(PrayerAdzanService.EXTRA_NOTIFICATION_MODE) ?: "adzan"

        if (mode.equals("push", ignoreCase = true)) {
            showPushNotification(context, prayerName, locationName)
        } else {
            val serviceIntent = Intent(context, PrayerAdzanService::class.java).apply {
                action = PrayerAdzanService.ACTION_PLAY_ADZAN
                putExtra(PrayerAdzanService.EXTRA_PRAYER_NAME, prayerName)
                putExtra(PrayerAdzanService.EXTRA_LOCATION_NAME, locationName)
                putExtra(PrayerAdzanService.EXTRA_AUDIO_FILE, audioFile)
                putExtra(PrayerAdzanService.EXTRA_CUSTOM_AUDIO_PATH, customPath)
                putExtra(PrayerAdzanService.EXTRA_VOICE_TITLE, voiceTitle)
            }

            try {
                ContextCompat.startForegroundService(context, serviceIntent)
            } catch (_: Exception) {
                try {
                    context.startService(serviceIntent)
                } catch (_: Exception) {}
            }
        }
    }

    private fun showPushNotification(context: Context, prayerName: String, locationName: String) {
        val channelId = "prayer_push_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi Waktu Sholat",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Push notifikasi saat masuk waktu sholat"
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

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .apply {
                if (largeIconBitmap != null) {
                    setLargeIcon(largeIconBitmap)
                }
            }
            .setContentTitle("Waktu Sholat $prayerName Telah Tiba")
            .setContentText("Saatnya menunaikan sholat $prayerName untuk wilayah $locationName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager?.notify(prayerName.hashCode(), notification)
    }
}
