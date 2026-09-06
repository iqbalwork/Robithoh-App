package com.iqbalwork.robithoh.core.analytics

import androidx.compose.runtime.Composable

/**
 * Interface pelacak analytics multiplatform (KMP).
 * Diimplementasikan oleh platform native (Firebase Analytics di Android, Stub di iOS/JVM).
 */
interface AnalyticsTracker {
    /**
     * Catat event dengan nama dan parameter kustom.
     */
    fun logEvent(name: String, params: Map<String, Any> = emptyMap())

    /**
     * Set user property yang melekat pada profil pengguna di analytics.
     */
    fun setUserProperty(name: String, value: String)

    /**
     * Catat perpindahan halaman/layar.
     */
    fun logScreenView(screenName: String, screenClass: String? = null)
}

/**
 * Helper extension untuk mencatat event dengan vararg Pairs.
 */
fun AnalyticsTracker.logEvent(name: String, vararg params: Pair<String, Any>) {
    logEvent(name, params.toMap())
}

/**
 * Factory platform untuk mendapatkan AnalyticsTracker singleton.
 */
expect fun getAnalyticsTracker(): AnalyticsTracker

/**
 * Composable helper untuk me-remember AnalyticsTracker instance di Compose UI.
 */
@Composable
expect fun rememberAnalyticsTracker(): AnalyticsTracker
