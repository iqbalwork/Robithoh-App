package com.iqbalwork.robithoh.core.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class IosAnalyticsTracker : AnalyticsTracker {
    override fun logEvent(name: String, params: Map<String, Any>) {
        // iOS analytics implementation / stub
    }

    override fun setUserProperty(name: String, value: String) {
        // iOS analytics implementation / stub
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        // iOS analytics implementation / stub
    }
}

private val defaultIosTracker = IosAnalyticsTracker()

actual fun getAnalyticsTracker(): AnalyticsTracker = defaultIosTracker

@Composable
actual fun rememberAnalyticsTracker(): AnalyticsTracker {
    return remember { defaultIosTracker }
}
