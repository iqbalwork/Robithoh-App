package com.iqbalwork.robithoh.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent

class TanbihWidget4x2Provider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        TanbihWidgetHelper.updateAllWidgets(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            TanbihWidgetHelper.ACTION_NEXT_TANBIH_QUOTE -> {
                TanbihWidgetHelper.nextQuote(context)
            }
            TanbihWidgetHelper.ACTION_UPDATE_TANBIH_WIDGET,
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> {
                TanbihWidgetHelper.updateAllWidgets(context)
            }
        }
    }
}
