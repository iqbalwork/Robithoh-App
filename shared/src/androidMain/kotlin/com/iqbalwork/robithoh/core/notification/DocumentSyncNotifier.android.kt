package com.iqbalwork.robithoh.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncState

class AndroidDocumentSyncNotifier(private val context: Context) : DocumentSyncNotifier {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    companion object {
        const val CHANNEL_ID = "document_sync_channel_v2"
        const val NOTIFICATION_ID = 2001
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pembaruan Naskah",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi status pembaruan naskah amaliyah"
                setShowBadge(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun getPendingIntent(): PendingIntent? {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        } ?: return null

        return PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun notifySyncState(state: DocumentSyncState, isManual: Boolean) {
        val pendingIntent = getPendingIntent()
        val iconRes = getNotificationIcon()

        when (state) {
            DocumentSyncState.Idle -> {
                // Tidak menampilkan notifikasi saat idle
            }
            is DocumentSyncState.Checking -> {
                if (!isManual) return // Cek otomatis di latar belakang berjalan senyap

                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(iconRes)
                    .setContentTitle("Pembaruan Naskah")
                    .setContentText(state.message)
                    .setProgress(0, 0, true)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                if (pendingIntent != null) {
                    builder.setContentIntent(pendingIntent)
                }

                notificationManager?.notify(NOTIFICATION_ID, builder.build())
            }
            is DocumentSyncState.Syncing -> {
                val progressPercent = (state.progress * 100).toInt()
                val subText = if (state.currentFileName.isNotBlank()) {
                    "Mengunduh ${state.currentFileName}"
                } else {
                    "Mengunduh naskah..."
                }

                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(iconRes)
                    .setContentTitle("Memperbarui Naskah (${state.current}/${state.total}) - $progressPercent%")
                    .setContentText(subText)
                    .setProgress(state.total, state.current, false)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                if (pendingIntent != null) {
                    builder.setContentIntent(pendingIntent)
                }

                notificationManager?.notify(NOTIFICATION_ID, builder.build())
            }
            is DocumentSyncState.Success -> {
                if (state.updatedCount == 0 && !isManual) {
                    // Cek otomatis dan tidak ada naskah baru yang diperbarui -> senyap
                    return
                }

                val message = if (state.updatedCount > 0) {
                    "${state.updatedCount} naskah berhasil diperbarui!"
                } else {
                    "Naskah amaliyah sudah versi terbaru."
                }

                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(iconRes)
                    .setContentTitle("Pembaruan Naskah Selesai")
                    .setContentText(message)
                    .setProgress(0, 0, false)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)

                if (pendingIntent != null) {
                    builder.setContentIntent(pendingIntent)
                }

                notificationManager?.notify(NOTIFICATION_ID, builder.build())
            }
            is DocumentSyncState.Error -> {
                if (!isManual) return // Cek otomatis yang gagal koneksi tidak mengganggu pengguna

                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(iconRes)
                    .setContentTitle("Gagal Memperbarui Naskah")
                    .setContentText("Koneksi bermasalah: ${state.message}")
                    .setProgress(0, 0, false)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)

                if (pendingIntent != null) {
                    builder.setContentIntent(pendingIntent)
                }

                notificationManager?.notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    override fun dismiss() {
        notificationManager?.cancel(NOTIFICATION_ID)
    }

    private fun getNotificationIcon(): Int {
        val customIconId = context.resources.getIdentifier("ic_stat_prayer", "drawable", context.packageName)
        return if (customIconId != 0) customIconId else context.applicationInfo.icon
    }
}

actual fun createDocumentSyncNotifier(): DocumentSyncNotifier {
    val ctx = getGlobalAppContext() ?: error("Global App Context not initialized")
    return AndroidDocumentSyncNotifier(ctx)
}

@Composable
actual fun rememberDocumentSyncNotifier(): DocumentSyncNotifier {
    val context = LocalContext.current.applicationContext
    return remember(context) { AndroidDocumentSyncNotifier(context) }
}
