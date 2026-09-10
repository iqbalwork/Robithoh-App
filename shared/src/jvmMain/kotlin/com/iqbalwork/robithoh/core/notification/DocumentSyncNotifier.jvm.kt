package com.iqbalwork.robithoh.core.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncState

class JvmDocumentSyncNotifier : DocumentSyncNotifier {
    override fun notifySyncState(state: DocumentSyncState, isManual: Boolean) {
        // Desktop / JVM stub
    }

    override fun dismiss() {
        // Desktop / JVM stub
    }
}

actual fun createDocumentSyncNotifier(): DocumentSyncNotifier = JvmDocumentSyncNotifier()

@Composable
actual fun rememberDocumentSyncNotifier(): DocumentSyncNotifier {
    return remember { JvmDocumentSyncNotifier() }
}
