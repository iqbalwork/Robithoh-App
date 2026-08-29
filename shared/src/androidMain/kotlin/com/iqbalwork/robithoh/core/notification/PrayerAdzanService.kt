package com.iqbalwork.robithoh.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PrayerAdzanService : Service() {

    companion object {
        const val CHANNEL_ID = "prayer_adzan_channel"
        const val CHANNEL_NAME = "Waktu Sholat & Adzan"
        const val NOTIFICATION_ID = 1001

        const val ACTION_PLAY_ADZAN = "com.iqbalwork.robithoh.ACTION_PLAY_ADZAN"
        const val ACTION_STOP_ADZAN = "com.iqbalwork.robithoh.ACTION_STOP_ADZAN"

        const val EXTRA_PRAYER_NAME = "extra_prayer_name"
        const val EXTRA_LOCATION_NAME = "extra_location_name"
        const val EXTRA_AUDIO_FILE = "extra_audio_file"
        const val EXTRA_CUSTOM_AUDIO_PATH = "extra_custom_audio_path"
        const val EXTRA_VOICE_TITLE = "extra_voice_title"
        const val EXTRA_NOTIFICATION_MODE = "extra_notification_mode"
        const val EXTRA_IS_PRE_REMINDER = "extra_is_pre_reminder"

        private var isPlayingAdzan = false
        fun isAdzanPlaying(): Boolean = isPlayingAdzan
    }

    private var mediaPlayer: MediaPlayer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            stopSelf()
            return START_NOT_STICKY
        }

        when (intent.action) {
            ACTION_STOP_ADZAN -> {
                stopAdzanAudio()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                } else {
                    @Suppress("DEPRECATION")
                    stopForeground(true)
                }
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                manager?.cancel(NOTIFICATION_ID)
                stopSelf()
            }
            ACTION_PLAY_ADZAN -> {
                val prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME) ?: "Sholat"
                val locationName = intent.getStringExtra(EXTRA_LOCATION_NAME) ?: "Wilayah Anda"
                val audioFile = intent.getStringExtra(EXTRA_AUDIO_FILE) ?: "adzan_misyari_rasyid.mp3"
                val customPath = intent.getStringExtra(EXTRA_CUSTOM_AUDIO_PATH)
                val voiceTitle = intent.getStringExtra(EXTRA_VOICE_TITLE) ?: "Adzan"

                val notification = buildNotification(prayerName, locationName, voiceTitle)
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startForeground(
                            NOTIFICATION_ID,
                            notification,
                            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                        )
                    } else {
                        startForeground(NOTIFICATION_ID, notification)
                    }
                    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                    manager?.notify(NOTIFICATION_ID, notification)
                } catch (_: Exception) {
                    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                    manager?.notify(NOTIFICATION_ID, notification)
                }

                playAdzanAudio(audioFile, customPath)
            }
            else -> {
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    private fun playAdzanAudio(audioFileName: String, customPath: String?) {
        stopAdzanAudio()
        isPlayingAdzan = true

        serviceScope.launch(Dispatchers.IO) {
            try {
                val resolvedPath = if (!customPath.isNullOrBlank() && File(customPath).exists()) {
                    customPath
                } else {
                    val bytes = try {
                        robithohapp.shared.generated.resources.Res.readBytes("files/$audioFileName")
                    } catch (_: Exception) {
                        null
                    }
                    if (bytes != null) {
                        val temp = File.createTempFile("adzan_", ".mp3", cacheDir).apply {
                            deleteOnExit()
                            writeBytes(bytes)
                        }
                        temp.absolutePath
                    } else {
                        null
                    }
                }

                withContext(Dispatchers.Main) {
                    if (resolvedPath != null) {
                        val player = MediaPlayer().apply {
                            setWakeMode(applicationContext, android.os.PowerManager.PARTIAL_WAKE_LOCK)
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_ALARM)
                                    .build()
                            )
                            setDataSource(resolvedPath)
                            setOnCompletionListener {
                                isPlayingAdzan = false
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    stopForeground(STOP_FOREGROUND_REMOVE)
                                } else {
                                    @Suppress("DEPRECATION")
                                    stopForeground(true)
                                }
                                stopSelf()
                            }
                            setOnErrorListener { _, _, _ ->
                                isPlayingAdzan = false
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    stopForeground(STOP_FOREGROUND_REMOVE)
                                } else {
                                    @Suppress("DEPRECATION")
                                    stopForeground(true)
                                }
                                stopSelf()
                                true
                            }
                            prepareAsync()
                            setOnPreparedListener { mp ->
                                mp.setVolume(1.0f, 1.0f)
                                mp.start()
                            }
                        }
                        mediaPlayer = player
                    } else {
                        isPlayingAdzan = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            stopForeground(STOP_FOREGROUND_REMOVE)
                        } else {
                            @Suppress("DEPRECATION")
                            stopForeground(true)
                        }
                        stopSelf()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isPlayingAdzan = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                    } else {
                        @Suppress("DEPRECATION")
                        stopForeground(true)
                    }
                    stopSelf()
                }
            }
        }
    }

    private fun stopAdzanAudio() {
        isPlayingAdzan = false
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) {
                    player.stop()
                }
                player.reset()
                player.release()
            } catch (_: Exception) {}
        }
        mediaPlayer = null
    }

    private fun buildNotification(prayerName: String, locationName: String, voiceTitle: String): Notification {
        val stopIntent = Intent(this, PrayerAdzanService::class.java).apply {
            action = ACTION_STOP_ADZAN
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            2001,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        // Delete intent when user swipes away / dismisses notification
        val deleteIntent = Intent(this, PrayerAdzanService::class.java).apply {
            action = ACTION_STOP_ADZAN
        }
        val deletePendingIntent = PendingIntent.getService(
            this,
            2002,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        // Launch app intent
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        val contentPendingIntent = if (launchIntent != null) {
            PendingIntent.getActivity(
                this,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )
        } else null

        val customIconId = resources.getIdentifier("ic_stat_prayer", "drawable", packageName)
        val iconRes = if (customIconId != 0) customIconId else android.R.drawable.ic_lock_idle_alarm
        val launcherIconId = resources.getIdentifier("ic_launcher", "mipmap", packageName)
        val largeIconBitmap = if (launcherIconId != 0) {
            try {
                android.graphics.BitmapFactory.decodeResource(resources, launcherIconId)
            } catch (_: Exception) {
                null
            }
        } else null

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(iconRes)
            .apply {
                if (largeIconBitmap != null) {
                    setLargeIcon(largeIconBitmap)
                }
            }
            .setContentTitle("Waktu Sholat $prayerName Telah Tiba")
            .setContentText("Saatnya menunaikan sholat $prayerName ($locationName)")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Lantunan Adzan: $voiceTitle\nSaatnya menunaikan ibadah sholat $prayerName untuk wilayah $locationName."))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
            .setOngoing(false)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Matikan Adzan", stopPendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi dan kumandang adzan saat masuk waktu sholat"
                setSound(null, null) // Audio handled explicitly by MediaPlayer in service
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        stopAdzanAudio()
        serviceScope.cancel()
        super.onDestroy()
    }
}
