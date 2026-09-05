package com.iqbalwork.robithoh.core.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class JvmAnalyticsTracker : AnalyticsTracker {
    override fun logEvent(name: String, params: Map<String, Any>) {
        println("[Analytics-JVM] Event: $name, Params: $params")
    }

    override fun setUserProperty(name: String, value: String) {
        println("[Analytics-JVM] UserProperty: $name = $value")
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        println("[Analytics-JVM] ScreenView: $screenName")
    }
}

private val defaultJvmTracker = JvmAnalyticsTracker()

actual fun getAnalyticsTracker(): AnalyticsTracker = defaultJvmTracker

@Composable
actual fun rememberAnalyticsTracker(): AnalyticsTracker {
    return remember { defaultJvmTracker }
}
