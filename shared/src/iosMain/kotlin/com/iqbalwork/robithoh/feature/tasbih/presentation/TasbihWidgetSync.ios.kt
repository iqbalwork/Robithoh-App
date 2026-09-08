@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.iqbalwork.robithoh.feature.tasbih.presentation

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSClassFromString

actual fun notifyTasbihWidgetUpdate() {
    try {
        if (NSClassFromString("WidgetCenter") == null) return
    } catch (_: Throwable) {
        // Guard against WidgetCenter availability
    }
}

