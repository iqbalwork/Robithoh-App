package com.iqbalwork.robithoh.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.RemoteViews
import com.iqbalwork.robithoh.MainActivity
import com.iqbalwork.robithoh.R
import com.iqbalwork.robithoh.core.database.DatabaseDriverFactory
import com.iqbalwork.robithoh.core.database.createDatabase

object TasbihWidgetHelper {

    const val ACTION_TASBIH_INCREMENT = "com.iqbalwork.robithoh.ACTION_TASBIH_INCREMENT"
    const val ACTION_TASBIH_RESET = "com.iqbalwork.robithoh.ACTION_TASBIH_RESET"
    const val ACTION_UPDATE_TASBIH_WIDGET = "com.iqbalwork.robithoh.ACTION_UPDATE_TASBIH_WIDGET"

    const val DEFAULT_DZIKIR_ID = "dzikir_jahr"
    const val DEFAULT_DZIKIR_TITLE = "Dzikir Jahr"
    const val DEFAULT_DZIKIR_ARABIC = "لَا إِلَهَ إِلَّا اللَّهُ"
    const val DEFAULT_TARGET = 165

    data class TasbihWidgetData(
        val id: String,
        val title: String,
        val arabic: String,
        val currentCount: Int,
        val targetCount: Int,
        val lapCount: Int,
        val isCompleted: Boolean
    )

    fun getArabicTextForDzikir(dzikirId: String): String {
        return when (dzikirId) {
            "dzikir_jahr", "dzikir_nafi_itsbat" -> "لَا إِلَهَ إِلَّا اللَّهُ"
            "tasbih_subhanallah" -> "سُبْحَانَ اللَّهِ"
            "tahmid_alhamdulillah" -> "الْحَمْدُ لِلَّهِ"
            "takbir_allahuakbar" -> "اللَّهُ أَكْبَرُ"
            "istighfar_tqn" -> "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ"
            "shalawat_bani_hasyim" -> "اللَّهُمَّ صَلِّ عَلَى النَّبِيِّ الْهَاشِمِيِّ"
            else -> "لَا إِلَهَ إِلَّا اللَّهُ"
        }
    }

    /**
     * Membaca progres amaliyah dzikir dari database SQLite
     */
    fun loadTasbihData(context: Context): TasbihWidgetData {
        return try {
            val driver = DatabaseDriverFactory(context)
            val db = createDatabase(driver)
            val entity = db.robithohDatabaseQueries.getAmaliyahProgressById(DEFAULT_DZIKIR_ID).executeAsOneOrNull()
                ?: db.robithohDatabaseQueries.getAmaliyahProgressById("dzikir_nafi_itsbat").executeAsOneOrNull()

            if (entity != null) {
                val current = entity.current_count.toInt()
                val target = entity.target_count.toInt().coerceAtLeast(1)
                val lap = (current / target) + 1
                // Gunakan nama ringkas 'Dzikir Jahr' khusus untuk widget home screen
                val displayTitle = if (entity.id == DEFAULT_DZIKIR_ID || entity.id == "dzikir_nafi_itsbat") DEFAULT_DZIKIR_TITLE else entity.title
                TasbihWidgetData(
                    id = entity.id,
                    title = displayTitle,
                    arabic = getArabicTextForDzikir(entity.id),
                    currentCount = current,
                    targetCount = target,
                    lapCount = lap,
                    isCompleted = entity.is_completed == 1L
                )
            } else {
                TasbihWidgetData(
                    id = DEFAULT_DZIKIR_ID,
                    title = DEFAULT_DZIKIR_TITLE,
                    arabic = DEFAULT_DZIKIR_ARABIC,
                    currentCount = 0,
                    targetCount = DEFAULT_TARGET,
                    lapCount = 1,
                    isCompleted = false
                )
            }
        } catch (_: Throwable) {
            TasbihWidgetData(
                id = DEFAULT_DZIKIR_ID,
                title = DEFAULT_DZIKIR_TITLE,
                arabic = DEFAULT_DZIKIR_ARABIC,
                currentCount = 0,
                targetCount = DEFAULT_TARGET,
                lapCount = 1,
                isCompleted = false
            )
        }
    }

    /**
     * Menambah hitungan dzikir (+1) langsung di database dan memperbarui widget
     */
    fun incrementCount(context: Context) {
        try {
            val data = loadTasbihData(context)
            val driver = DatabaseDriverFactory(context)
            val db = createDatabase(driver)
            val newCount = data.currentCount + 1
            val isCompleted = if (newCount >= data.targetCount) 1L else 0L

            db.robithohDatabaseQueries.insertOrUpdateAmaliyahProgress(
                id = data.id,
                title = data.title,
                current_count = newCount.toLong(),
                target_count = data.targetCount.toLong(),
                last_updated = 20260904L,
                is_completed = isCompleted
            )

            // Trigger haptic vibration on device
            triggerHapticFeedback(context, isMilestone = (newCount % data.targetCount == 0 || newCount % 33 == 0))
        } catch (_: Throwable) {}

        updateAllWidgets(context)
    }

    /**
     * Mereset hitungan dzikir kembali ke 0
     */
    fun resetCount(context: Context) {
        try {
            val data = loadTasbihData(context)
            val driver = DatabaseDriverFactory(context)
            val db = createDatabase(driver)
            db.robithohDatabaseQueries.resetAmaliyahProgress(
                lastUpdated = 20260904L,
                id = data.id
            )
            triggerHapticFeedback(context, isMilestone = false)
        } catch (_: Throwable) {}

        updateAllWidgets(context)
    }

    private fun triggerHapticFeedback(context: Context, isMilestone: Boolean) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            } ?: return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = if (isMilestone) {
                    VibrationEffect.createWaveform(longArrayOf(0, 40, 60, 80), intArrayOf(0, 180, 0, 255), -1)
                } else {
                    VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE)
                }
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(if (isMilestone) 80 else 25)
            }
        } catch (_: Throwable) {}
    }

    /**
     * Membangun RemoteViews untuk Widget Tasbih 2x2
     */
    fun buildRemoteViews2x2(context: Context, data: TasbihWidgetData): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_tasbih_2x2)

        views.setTextViewText(R.id.tv_tasbih_title, data.title)
        views.setTextViewText(R.id.tv_tasbih_arabic, data.arabic)
        views.setTextViewText(R.id.tv_tasbih_count, data.currentCount.toString())
        views.setTextViewText(R.id.tv_tasbih_target, "Target: ${data.targetCount}")
        views.setTextViewText(R.id.tv_tasbih_lap, "Putaran ${data.lapCount}")

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

        // Intent Increment (+ TAP)
        val incIntent = Intent(context, TasbihWidget2x2Provider::class.java).apply {
            action = ACTION_TASBIH_INCREMENT
        }
        val incPendingIntent = PendingIntent.getBroadcast(context, 2001, incIntent, flags)
        views.setOnClickPendingIntent(R.id.btn_tasbih_increment, incPendingIntent)

        // Intent Reset (↺)
        val resetIntent = Intent(context, TasbihWidget2x2Provider::class.java).apply {
            action = ACTION_TASBIH_RESET
        }
        val resetPendingIntent = PendingIntent.getBroadcast(context, 2002, resetIntent, flags)
        views.setOnClickPendingIntent(R.id.btn_tasbih_reset, resetPendingIntent)

        // Intent Buka App Fullscreen (↗)
        val openIntent = Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("NAVIGATE_TO", "TASBIH")
        }
        val openPendingIntent = PendingIntent.getActivity(context, 2003, openIntent, flags)
        views.setOnClickPendingIntent(R.id.btn_tasbih_open, openPendingIntent)
        views.setOnClickPendingIntent(R.id.widget_root, openPendingIntent)

        return views
    }

    /**
     * Membangun RemoteViews untuk Widget Tasbih 4x1
     */
    fun buildRemoteViews4x1(context: Context, data: TasbihWidgetData): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_tasbih_4x1)

        views.setTextViewText(R.id.tv_tasbih_title, data.title)
        views.setTextViewText(R.id.tv_tasbih_arabic, data.arabic)
        views.setTextViewText(R.id.tv_tasbih_count, data.currentCount.toString())
        views.setTextViewText(R.id.tv_tasbih_target, "/ ${data.targetCount}")
        views.setTextViewText(R.id.tv_tasbih_lap, "Putaran ${data.lapCount}")

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

        // Intent Increment (+1)
        val incIntent = Intent(context, TasbihWidget4x1Provider::class.java).apply {
            action = ACTION_TASBIH_INCREMENT
        }
        val incPendingIntent = PendingIntent.getBroadcast(context, 2101, incIntent, flags)
        views.setOnClickPendingIntent(R.id.btn_tasbih_increment, incPendingIntent)

        // Intent Reset (↺)
        val resetIntent = Intent(context, TasbihWidget4x1Provider::class.java).apply {
            action = ACTION_TASBIH_RESET
        }
        val resetPendingIntent = PendingIntent.getBroadcast(context, 2102, resetIntent, flags)
        views.setOnClickPendingIntent(R.id.btn_tasbih_reset, resetPendingIntent)

        // Intent Buka App (↗)
        val openIntent = Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("NAVIGATE_TO", "TASBIH")
        }
        val openPendingIntent = PendingIntent.getActivity(context, 2103, openIntent, flags)
        views.setOnClickPendingIntent(R.id.btn_tasbih_open, openPendingIntent)
        views.setOnClickPendingIntent(R.id.widget_root, openPendingIntent)

        return views
    }

    /**
     * Memperbarui seluruh widget tasbih (2x2 dan 4x1)
     */
    fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
        val data = loadTasbihData(context)

        // Update 2x2
        val component2x2 = ComponentName(context, TasbihWidget2x2Provider::class.java)
        val ids2x2 = appWidgetManager.getAppWidgetIds(component2x2)
        if (ids2x2 != null && ids2x2.isNotEmpty()) {
            val views2x2 = buildRemoteViews2x2(context, data)
            appWidgetManager.updateAppWidget(ids2x2, views2x2)
        }

        // Update 4x1
        val component4x1 = ComponentName(context, TasbihWidget4x1Provider::class.java)
        val ids4x1 = appWidgetManager.getAppWidgetIds(component4x1)
        if (ids4x1 != null && ids4x1.isNotEmpty()) {
            val views4x1 = buildRemoteViews4x1(context, data)
            appWidgetManager.updateAppWidget(ids4x1, views4x1)
        }
    }
}
