package com.iqbalwork.robithoh.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent

class TasbihWidget4x1Provider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        TasbihWidgetHelper.updateAllWidgets(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            TasbihWidgetHelper.ACTION_TASBIH_INCREMENT -> {
                TasbihWidgetHelper.incrementCount(context)
            }
            TasbihWidgetHelper.ACTION_TASBIH_RESET -> {
                TasbihWidgetHelper.resetCount(context)
            }
            TasbihWidgetHelper.ACTION_UPDATE_TASBIH_WIDGET,
            Intent.ACTION_BOOT_COMPLETED -> {
                TasbihWidgetHelper.updateAllWidgets(context)
            }
        }
    }
}
