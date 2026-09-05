package com.iqbalwork.robithoh.feature.quran.data

import android.content.Intent
import com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext

actual fun notifyQuranWidgetUpdate() {
    try {
        val context = getGlobalAppContext() ?: return
        val intent = Intent("com.iqbalwork.robithoh.ACTION_UPDATE_QURAN_WIDGET").apply {
            `package` = context.packageName
        }
        context.sendBroadcast(intent)
    } catch (_: Throwable) {}
}
