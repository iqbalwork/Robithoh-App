@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.iqbalwork.robithoh.core.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSClassFromString
import platform.Foundation.NSMutableDictionary
import platform.Foundation.setValue

class IosAnalyticsTracker : AnalyticsTracker {

    override fun logEvent(name: String, params: Map<String, Any>) {
        try {
            if (NSClassFromString("FIRAnalytics") == null) return
            val dict = NSMutableDictionary()
            for ((key, value) in params) {
                dict.setValue(value.toString(), forKey = key)
            }
        } catch (_: Throwable) {
            // Guard against analytics failure
        }
    }

    override fun setUserProperty(name: String, value: String) {
        try {
            if (NSClassFromString("FIRAnalytics") == null) return
        } catch (_: Throwable) {
            // Guard
        }
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        logEvent("screen_view", mapOf(
            "screen_name" to screenName,
            "screen_class" to (screenClass ?: screenName)
        ))
    }
}

private val defaultIosTracker = IosAnalyticsTracker()

actual fun getAnalyticsTracker(): AnalyticsTracker = defaultIosTracker

@Composable
actual fun rememberAnalyticsTracker(): AnalyticsTracker {
    return remember { defaultIosTracker }
}


