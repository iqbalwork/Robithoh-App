package com.iqbalwork.robithoh.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.iqbalwork.robithoh.MainActivity
import com.iqbalwork.robithoh.R

object QuickAccessWidgetHelper {

    private fun getPendingIntentFlags(isMutable: Boolean = false): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isMutable) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

    private fun createNavPendingIntent(context: Context, destination: String, requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("NAVIGATE_TO", destination)
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            getPendingIntentFlags(isMutable = false)
        )
    }

    fun buildRemoteViews4x1(context: Context): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_quick_access_4x1)

        // 1. Amaliyah
        val amaliyahPendingIntent = createNavPendingIntent(context, "AMALIYAH", 7001)
        views.setOnClickPendingIntent(R.id.btn_quick_amaliyah, amaliyahPendingIntent)

        // 2. Tasbih
        val tasbihPendingIntent = createNavPendingIntent(context, "TASBIH", 7002)
        views.setOnClickPendingIntent(R.id.btn_quick_tasbih, tasbihPendingIntent)

        // 3. Manqobah
        val manaqibPendingIntent = createNavPendingIntent(context, "MANAQIB", 7003)
        views.setOnClickPendingIntent(R.id.btn_quick_manaqib, manaqibPendingIntent)

        // 4. Al-Qur'an
        val quranPendingIntent = createNavPendingIntent(context, "QURAN", 7004)
        views.setOnClickPendingIntent(R.id.btn_quick_quran, quranPendingIntent)

        return views
    }

    fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
        val provider = ComponentName(context, QuickAccessWidget4x1Provider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(provider)
        if (ids.isNotEmpty()) {
            val views = buildRemoteViews4x1(context)
            appWidgetManager.updateAppWidget(ids, views)
        }
    }
}
