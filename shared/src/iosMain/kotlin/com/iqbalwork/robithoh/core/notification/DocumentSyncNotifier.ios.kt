package com.iqbalwork.robithoh.core.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncState
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

class IosDocumentSyncNotifier : DocumentSyncNotifier {
    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    override fun notifySyncState(state: DocumentSyncState, isManual: Boolean) {
        when (state) {
            is DocumentSyncState.Success -> {
                if (state.updatedCount == 0 && !isManual) return

                val content = UNMutableNotificationContent().apply {
                    setTitle("Pembaruan Naskah Selesai")
                    setBody(
                        if (state.updatedCount > 0) {
                            "${state.updatedCount} naskah berhasil diperbarui!"
                        } else {
                            "Naskah amaliyah sudah versi terbaru."
                        }
                    )
                }
                val request = UNNotificationRequest.requestWithIdentifier(
                    identifier = "doc_sync_success",
                    content = content,
                    trigger = null
                )
                notificationCenter.addNotificationRequest(request, null)
            }
            is DocumentSyncState.Error -> {
                if (!isManual) return

                val content = UNMutableNotificationContent().apply {
                    setTitle("Pembaruan Naskah Gagal")
                    setBody("Gagal memperbarui: ${state.message}")
                }
                val request = UNNotificationRequest.requestWithIdentifier(
                    identifier = "doc_sync_error",
                    content = content,
                    trigger = null
                )
                notificationCenter.addNotificationRequest(request, null)
            }
            else -> {}
        }
    }

    override fun dismiss() {
        notificationCenter.removeDeliveredNotificationsWithIdentifiers(listOf("doc_sync_success", "doc_sync_error"))
    }
}

actual fun createDocumentSyncNotifier(): DocumentSyncNotifier = IosDocumentSyncNotifier()

@Composable
actual fun rememberDocumentSyncNotifier(): DocumentSyncNotifier {
    return remember { IosDocumentSyncNotifier() }
}
