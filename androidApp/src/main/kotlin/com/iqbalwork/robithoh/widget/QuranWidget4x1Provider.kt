package com.iqbalwork.robithoh.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent

class QuranWidget4x1Provider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val data = QuranWidgetHelper.loadLastReadData(context)
        for (appWidgetId in appWidgetIds) {
            val views = QuranWidgetHelper.buildRemoteViews4x1(context, data)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action ?: return
        if (action == QuranWidgetHelper.ACTION_UPDATE_QURAN_WIDGET ||
            action == Intent.ACTION_BOOT_COMPLETED
        ) {
            QuranWidgetHelper.updateAllWidgets(context)
        }
    }
}
