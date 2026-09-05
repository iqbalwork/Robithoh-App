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
import com.iqbalwork.robithoh.core.database.DatabaseDriverFactory
import com.iqbalwork.robithoh.core.database.createDatabase
import com.iqbalwork.robithoh.feature.quran.data.QuranData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object QuranWidgetHelper {

    const val ACTION_UPDATE_QURAN_WIDGET = "com.iqbalwork.robithoh.ACTION_UPDATE_QURAN_WIDGET"

    data class LastReadData(
        val surahNumber: Int,
        val ayahNumber: Int,
        val surahName: String,
        val surahArabic: String,
        val lastUpdatedFormatted: String
    )

    fun loadLastReadData(context: Context): LastReadData {
        return try {
            val driver = DatabaseDriverFactory(context)
            val db = createDatabase(driver)
            val bookmark = db.robithohDatabaseQueries.getLastReadBookmark("quran").executeAsOneOrNull()

            if (bookmark != null) {
                val surahNum = bookmark.page_or_surah.toInt().coerceAtLeast(1)
                val ayahNum = bookmark.verse_or_section.toInt().coerceAtLeast(1)
                val surahMeta = QuranData.surahs.find { it.number == surahNum }
                val surahName = if (bookmark.title.isNotBlank()) bookmark.title else (surahMeta?.nameLatin ?: "Al-Fatihah")
                val surahArabic = surahMeta?.nameArabic ?: "الفاتحة"

                val dateStr = if (bookmark.updated_at > 0) {
                    try {
                        val sdf = SimpleDateFormat("d MMM", Locale("id", "ID"))
                        sdf.format(Date(bookmark.updated_at))
                    } catch (_: Exception) {
                        ""
                    }
                } else ""

                LastReadData(
                    surahNumber = surahNum,
                    ayahNumber = ayahNum,
                    surahName = surahName,
                    surahArabic = surahArabic,
                    lastUpdatedFormatted = dateStr
                )
            } else {
                LastReadData(
                    surahNumber = 1,
                    ayahNumber = 1,
                    surahName = "Al-Fatihah",
                    surahArabic = "الفاتحة",
                    lastUpdatedFormatted = ""
                )
            }
        } catch (_: Throwable) {
            LastReadData(
                surahNumber = 1,
                ayahNumber = 1,
                surahName = "Al-Fatihah",
                surahArabic = "الفاتحة",
                lastUpdatedFormatted = ""
            )
        }
    }

    private fun getPendingIntentFlags(isMutable: Boolean = false): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isMutable) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

    private fun createOpenQuranPendingIntent(context: Context, surahNumber: Int, ayahNumber: Int, requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("NAVIGATE_TO", "QURAN_SURAH")
            putExtra("SURAH_NUMBER", surahNumber)
            putExtra("AYAH_NUMBER", ayahNumber)
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            getPendingIntentFlags(isMutable = false)
        )
    }

    fun buildRemoteViews2x2(context: Context, data: LastReadData): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_quran_last_read_2x2)

        views.setTextViewText(R.id.tv_surah_name, data.surahName)
        views.setTextViewText(R.id.tv_surah_arabic, data.surahArabic)
        views.setTextViewText(R.id.tv_ayah_badge, "Ayat ${data.ayahNumber}")

        if (data.lastUpdatedFormatted.isNotBlank()) {
            views.setViewVisibility(R.id.tv_quran_date, android.view.View.VISIBLE)
            views.setTextViewText(R.id.tv_quran_date, data.lastUpdatedFormatted)
        } else {
            views.setViewVisibility(R.id.tv_quran_date, android.view.View.GONE)
        }

        val pendingIntent = createOpenQuranPendingIntent(context, data.surahNumber, data.ayahNumber, 6001)
        views.setOnClickPendingIntent(R.id.btn_quran_resume, pendingIntent)
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        return views
    }

    fun buildRemoteViews4x1(context: Context, data: LastReadData): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_quran_last_read_4x1)

        views.setTextViewText(R.id.tv_surah_name, data.surahName)
        views.setTextViewText(R.id.tv_ayah_badge, "Ayat ${data.ayahNumber}")

        val pendingIntent = createOpenQuranPendingIntent(context, data.surahNumber, data.ayahNumber, 6002)
        views.setOnClickPendingIntent(R.id.btn_quran_resume, pendingIntent)
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        return views
    }

    fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
        val data = loadLastReadData(context)

        // Update 2x2
        val provider2x2 = ComponentName(context, QuranWidget2x2Provider::class.java)
        val ids2x2 = appWidgetManager.getAppWidgetIds(provider2x2)
        if (ids2x2.isNotEmpty()) {
            val views2x2 = buildRemoteViews2x2(context, data)
            appWidgetManager.updateAppWidget(ids2x2, views2x2)
        }

        // Update 4x1
        val provider4x1 = ComponentName(context, QuranWidget4x1Provider::class.java)
        val ids4x1 = appWidgetManager.getAppWidgetIds(provider4x1)
        if (ids4x1.isNotEmpty()) {
            val views4x1 = buildRemoteViews4x1(context, data)
            appWidgetManager.updateAppWidget(ids4x1, views4x1)
        }
    }
}
