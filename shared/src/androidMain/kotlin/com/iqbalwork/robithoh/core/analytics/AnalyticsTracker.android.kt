package com.iqbalwork.robithoh.core.analytics

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.FirebaseAnalytics
import com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext

class AndroidFirebaseAnalyticsTracker(
    private val contextProvider: () -> Context?
) : AnalyticsTracker {

    private val firebaseAnalytics: FirebaseAnalytics?
        get() = contextProvider()?.let { FirebaseAnalytics.getInstance(it) }

    override fun logEvent(name: String, params: Map<String, Any>) {
        try {
            val bundle = Bundle().apply {
                for ((key, value) in params) {
                    when (value) {
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        is Long -> putLong(key, value)
                        is Double -> putDouble(key, value)
                        is Float -> putDouble(key, value.toDouble())
                        is Boolean -> putBoolean(key, value)
                        else -> putString(key, value.toString())
                    }
                }
            }
            firebaseAnalytics?.logEvent(name, bundle)
        } catch (_: Throwable) {
            // Guard against analytics crash
        }
    }

    override fun setUserProperty(name: String, value: String) {
        try {
            firebaseAnalytics?.setUserProperty(name, value)
        } catch (_: Throwable) {
            // Guard
        }
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        try {
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass ?: screenName)
            }
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
        } catch (_: Throwable) {
            // Guard
        }
    }
}

private val defaultAndroidTracker by lazy {
    AndroidFirebaseAnalyticsTracker { getGlobalAppContext() }
}

actual fun getAnalyticsTracker(): AnalyticsTracker = defaultAndroidTracker

@Composable
actual fun rememberAnalyticsTracker(): AnalyticsTracker {
    val context = LocalContext.current.applicationContext
    return remember(context) {
        AndroidFirebaseAnalyticsTracker { context }
    }
}
