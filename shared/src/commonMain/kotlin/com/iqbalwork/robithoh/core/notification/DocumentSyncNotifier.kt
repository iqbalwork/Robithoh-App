package com.iqbalwork.robithoh.core.notification

import androidx.compose.runtime.Composable
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncState

interface DocumentSyncNotifier {
    fun notifySyncState(state: DocumentSyncState, isManual: Boolean = false)
    fun dismiss()
}

expect fun createDocumentSyncNotifier(): DocumentSyncNotifier

@Composable
expect fun rememberDocumentSyncNotifier(): DocumentSyncNotifier
