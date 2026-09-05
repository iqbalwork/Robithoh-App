package com.iqbalwork.robithoh.core.analytics

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeAnalyticsTracker : AnalyticsTracker {
    val loggedEvents = mutableListOf<Pair<String, Map<String, Any>>>()
    val userProperties = mutableMapOf<String, String>()
    val screenViews = mutableListOf<Pair<String, String?>>()

    override fun logEvent(name: String, params: Map<String, Any>) {
        loggedEvents.add(name to params)
    }

    override fun setUserProperty(name: String, value: String) {
        userProperties[name] = value
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        screenViews.add(screenName to screenClass)
    }
}

class AnalyticsTrackerTest {

    @Test
    fun testLogEventWithVarargs() {
        val tracker = FakeAnalyticsTracker()
        tracker.logEvent(
            AnalyticsEvents.TASBIH_TARGET_REACHED,
            AnalyticsParams.TARGET_COUNT to 33,
            AnalyticsParams.TOTAL_TAPS to 33,
            AnalyticsParams.IS_VIBRATION_ENABLED to true
        )

        assertEquals(1, tracker.loggedEvents.size)
        val (eventName, params) = tracker.loggedEvents.first()
        assertEquals(AnalyticsEvents.TASBIH_TARGET_REACHED, eventName)
        assertEquals(33, params[AnalyticsParams.TARGET_COUNT])
        assertEquals(33, params[AnalyticsParams.TOTAL_TAPS])
        assertEquals(true, params[AnalyticsParams.IS_VIBRATION_ENABLED])
    }

    @Test
    fun testSetUserProperty() {
        val tracker = FakeAnalyticsTracker()
        tracker.setUserProperty(UserProperties.PREFERRED_LANGUAGE, "sunda")
        tracker.setUserProperty(UserProperties.APP_THEME, "dark")

        assertEquals("sunda", tracker.userProperties[UserProperties.PREFERRED_LANGUAGE])
        assertEquals("dark", tracker.userProperties[UserProperties.APP_THEME])
    }

    @Test
    fun testLogScreenView() {
        val tracker = FakeAnalyticsTracker()
        tracker.logScreenView("ManaqibDetailScreen", "ManaqibActivity")

        assertEquals(1, tracker.screenViews.size)
        assertEquals("ManaqibDetailScreen", tracker.screenViews.first().first)
        assertEquals("ManaqibActivity", tracker.screenViews.first().second)
    }

    @Test
    fun testTasbihTargetReachedTracking() {
        val tracker = FakeAnalyticsTracker()
        val viewModel = com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel(
            analyticsTracker = tracker
        )

        viewModel.onIntent(com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent.SetTarget(3))

        // Tap 3 times to reach target 3
        viewModel.onIntent(com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent.Increment)
        viewModel.onIntent(com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent.Increment)
        viewModel.onIntent(com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent.Increment)

        val targetEvents = tracker.loggedEvents.filter { it.first == AnalyticsEvents.TASBIH_TARGET_REACHED }
        assertEquals(1, targetEvents.size)
        assertEquals(3, targetEvents.first().second[AnalyticsParams.TARGET_COUNT])
        assertEquals(3, targetEvents.first().second[AnalyticsParams.TOTAL_TAPS])
    }
}
