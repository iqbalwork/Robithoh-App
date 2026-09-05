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
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.iqbalwork.robithoh.core.designsystem.setGlobalAppContext
import com.iqbalwork.robithoh.core.notification.AndroidPrayerAlarmScheduler
import com.iqbalwork.robithoh.navigation.WidgetNavTarget
import com.iqbalwork.robithoh.update.InAppUpdateManager

class MainActivity : ComponentActivity() {
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Result handled */ }

    private lateinit var inAppUpdateManager: InAppUpdateManager
    private val widgetNavState = mutableStateOf<WidgetNavTarget?>(null)

    private fun handleWidgetNavigation(intent: Intent?) {
        val dest = intent?.getStringExtra("NAVIGATE_TO") ?: return
        val surah = intent.getIntExtra("SURAH_NUMBER", 1)
        val ayah = intent.getIntExtra("AYAH_NUMBER", 1)
        widgetNavState.value = WidgetNavTarget(
            destination = dest,
            surahNumber = surah,
            ayahNumber = ayah,
            timestamp = System.currentTimeMillis()
        )
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleWidgetNavigation(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setGlobalAppContext(this)

        inAppUpdateManager = InAppUpdateManager(this)
        inAppUpdateManager.checkForUpdates(preferImmediate = false)

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
            com.iqbalwork.robithoh.widget.PrayerWidgetHelper.updateAllWidgets(this)
            com.iqbalwork.robithoh.widget.TasbihWidgetHelper.updateAllWidgets(this)
            com.iqbalwork.robithoh.widget.TanbihWidgetHelper.updateAllWidgets(this)
            com.iqbalwork.robithoh.widget.QuranWidgetHelper.updateAllWidgets(this)
            com.iqbalwork.robithoh.widget.QuickAccessWidgetHelper.updateAllWidgets(this)
        } catch (_: Throwable) {}

        handleWidgetNavigation(intent)

        val navDestination = intent?.getStringExtra("NAVIGATE_TO")
        val surahNum = intent?.getIntExtra("SURAH_NUMBER", 1) ?: 1
        val ayahNum = intent?.getIntExtra("AYAH_NUMBER", 1) ?: 1

        setContent {
            val navTarget by widgetNavState
            App(
                initialDestination = navDestination,
                initialSurahNumber = surahNum,
                initialAyahNumber = ayahNum,
                widgetNavTarget = navTarget
            )
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            com.iqbalwork.robithoh.widget.PrayerWidgetHelper.updateAllWidgets(this)
            com.iqbalwork.robithoh.widget.TasbihWidgetHelper.updateAllWidgets(this)
            com.iqbalwork.robithoh.widget.TanbihWidgetHelper.updateAllWidgets(this)
            com.iqbalwork.robithoh.widget.QuranWidgetHelper.updateAllWidgets(this)
            com.iqbalwork.robithoh.widget.QuickAccessWidgetHelper.updateAllWidgets(this)
        } catch (_: Throwable) {}
        if (::inAppUpdateManager.isInitialized) {
            inAppUpdateManager.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::inAppUpdateManager.isInitialized) {
            inAppUpdateManager.onDestroy()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
