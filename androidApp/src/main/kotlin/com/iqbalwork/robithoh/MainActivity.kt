package com.iqbalwork.robithoh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        com.iqbalwork.robithoh.core.designsystem.setGlobalAppContext(this)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val manager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as? android.app.NotificationManager
            val adzanChannel = android.app.NotificationChannel(
                "prayer_adzan_channel",
                "Waktu Sholat & Adzan",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi dan kumandang adzan saat masuk waktu sholat"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }
            val pushChannel = android.app.NotificationChannel(
                "prayer_push_channel",
                "Notifikasi Waktu Sholat",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Push notifikasi saat masuk waktu sholat"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }
            manager?.createNotificationChannels(listOf(adzanChannel, pushChannel))
        }

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