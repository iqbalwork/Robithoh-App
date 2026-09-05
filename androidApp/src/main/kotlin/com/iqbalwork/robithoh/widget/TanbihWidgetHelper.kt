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

object TanbihWidgetHelper {

    const val ACTION_NEXT_TANBIH_QUOTE = "com.iqbalwork.robithoh.ACTION_NEXT_TANBIH_QUOTE"
    const val ACTION_UPDATE_TANBIH_WIDGET = "com.iqbalwork.robithoh.ACTION_UPDATE_TANBIH_WIDGET"

    /**
     * Membangun RemoteViews untuk Widget Mutiara Tanbih 4x2
     */
    fun buildRemoteViews(context: Context, quote: TanbihQuote): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_tanbih_quote_4x2)

        views.setTextViewText(R.id.tv_quote_category, quote.category)
        views.setTextViewText(R.id.tv_quote_text, "“${quote.quote}”")

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

        // Intent Ganti Kutipan (↻)
        val nextIntent = Intent(context, TanbihWidget4x2Provider::class.java).apply {
            action = ACTION_NEXT_TANBIH_QUOTE
        }
        val nextPendingIntent = PendingIntent.getBroadcast(context, 3001, nextIntent, flags)
        views.setOnClickPendingIntent(R.id.btn_next_quote, nextPendingIntent)

        // Intent Buka Naskah Lengkap di Aplikasi
        val openIntent = Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("NAVIGATE_TO", "TANBIH")
        }
        val openPendingIntent = PendingIntent.getActivity(context, 3002, openIntent, flags)
        views.setOnClickPendingIntent(R.id.widget_root, openPendingIntent)

        return views
    }

    /**
     * Memperbarui seluruh widget Tanbih yang terpasang
     */
    fun updateAllWidgets(context: Context, quote: TanbihQuote? = null) {
        val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
        val currentQuote = quote ?: TanbihQuoteRepository.getCurrentQuote(context)

        val component = ComponentName(context, TanbihWidget4x2Provider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(component)
        if (ids != null && ids.isNotEmpty()) {
            val views = buildRemoteViews(context, currentQuote)
            appWidgetManager.updateAppWidget(ids, views)
        }
    }

    /**
     * Mengganti kutipan ke mutiara hikmah berikutnya secara manual
     */
    fun nextQuote(context: Context) {
        val next = TanbihQuoteRepository.getNextQuote(context)
        updateAllWidgets(context, next)
    }
}
