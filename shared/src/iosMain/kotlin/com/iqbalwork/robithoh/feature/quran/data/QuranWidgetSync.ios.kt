@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.iqbalwork.robithoh.feature.quran.data

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSClassFromString

actual fun notifyQuranWidgetUpdate() {
    try {
        if (NSClassFromString("WidgetCenter") == null) return
    } catch (_: Throwable) {
        // Guard against WidgetCenter availability
    }
}

