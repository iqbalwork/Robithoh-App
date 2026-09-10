# ADR-0005: System Notifications for Liturgical Document Sync Progress

*   **Status**: Accepted
*   **Date**: 2026-09-09
*   **Deciders**: Iqbal Fauzi, Antigravity AI
*   **Consulted**: Core Development Team
*   **Informed**: Engineering & QA

---

## 1. Context and Problem Statement
When Robithoh App performs background or manual sync of amaliyah liturgical texts (`DocumentSyncManager`), the application previously rendered an in-app overlay toast popup card (`DocumentSyncOverlay`) at the top of the UI screen. This in-app popup obscured the reader's view during reading sessions and created visual clutter upon app launch. Users requested moving sync progress and completion feedback into system notifications (live activity style progress bar).

## 2. Decision
We decided to replace the in-app popup toast UI with a multiplatform system notification mechanism (`DocumentSyncNotifier` expect/actual):
1. **System Progress Notification**: On Android, when sync is active, `AndroidDocumentSyncNotifier` posts an ongoing high-priority progress notification in `document_sync_channel_v2` with a progress bar and status text.
2. **Silent Background Auto-Check**: Automatic checks on app launch run silently in the background (`isManual = false`). Notifications only pop up if documents are actually being downloaded and updated (`updatedCount > 0`). If no documents changed, zero notifications pop up.
3. **Completion & Error Notification**: Upon sync completion (`Success` or `Error`), the ongoing notification updates to show the final status (e.g. "3 naskah berhasil diperbarui!") and becomes auto-dismissable.
4. **Multiplatform Abstraction**: `DocumentSyncNotifier` is abstracted as an expect/actual interface implemented across Android (`NotificationCompat`), iOS (`UNUserNotificationCenter`), and JVM targets.
5. **Clean UI**: Removed `DocumentSyncOverlay` from the root `App.kt` layout so document updates run smoothly without disturbing the user's reading experience.

## 3. Rationale
* Keeps the reading interface completely unobstructed and focused on worship.
* System notifications allow users to track background document update progress even if they switch tabs or minimize the app.
* Follows native Android notification best practices with `IMPORTANCE_LOW` for progress updates to avoid annoying sound alerts.

## 4. Consequences
* **Good**: Unobstructed UI during reading; background visibility for long syncs; native system notification integration.
* **Bad**: System notification permissions (`POST_NOTIFICATIONS`) required on Android 13+ (already requested for prayer alerts).
* **Neutral**: Sync state remains observable via `DocumentSyncManager.syncState` for any UI component that still wants to display local indicators.

## 5. References
* [DocumentSyncNotifier.kt](../../shared/src/commonMain/kotlin/com/iqbalwork/robithoh/core/notification/DocumentSyncNotifier.kt)
* [DocumentSyncManager.kt](../../shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/reader/data/sync/DocumentSyncManager.kt)
* [App.kt](../../shared/src/commonMain/kotlin/com/iqbalwork/robithoh/App.kt)
