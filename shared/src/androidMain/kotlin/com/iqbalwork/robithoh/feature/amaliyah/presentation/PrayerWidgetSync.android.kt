package com.iqbalwork.robithoh.feature.amaliyah.presentation

import android.content.Intent
import com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext

actual fun notifyPrayerWidgetUpdate() {
    try {
        val context = getGlobalAppContext() ?: return
        val intent = Intent("com.iqbalwork.robithoh.ACTION_UPDATE_PRAYER_WIDGET").apply {
            `package` = context.packageName
        }
        context.sendBroadcast(intent)
    } catch (_: Throwable) {}
}
