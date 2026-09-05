package com.iqbalwork.robithoh.feature.tasbih.presentation

import android.content.Intent
import com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext

actual fun notifyTasbihWidgetUpdate() {
    try {
        val context = getGlobalAppContext() ?: return
        val intent = Intent("com.iqbalwork.robithoh.ACTION_UPDATE_TASBIH_WIDGET").apply {
            `package` = context.packageName
        }
        context.sendBroadcast(intent)
    } catch (_: Throwable) {}
}
