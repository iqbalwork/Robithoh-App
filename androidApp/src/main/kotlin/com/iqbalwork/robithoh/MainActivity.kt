package com.iqbalwork.robithoh

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iqbalwork.robithoh.core.designsystem.setGlobalAppContext
import com.iqbalwork.robithoh.core.notification.AndroidPrayerAlarmScheduler

class MainActivity : ComponentActivity() {
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Result handled */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setGlobalAppContext(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
            val adzanChannel = NotificationChannel(
                "prayer_adzan_channel",
                "Waktu Sholat & Adzan",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi dan kumandang adzan saat masuk waktu sholat"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }
            val pushChannel = NotificationChannel(
                "prayer_push_channel",
                "Notifikasi Waktu Sholat & Imsak",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Push notifikasi saat masuk waktu sholat & imsak"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }
            manager?.createNotificationChannels(listOf(adzanChannel, pushChannel))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Ensure alarm schedule is active from database
        try {
            AndroidPrayerAlarmScheduler.rescheduleFromDatabase(this)
        } catch (_: Throwable) {}

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
