package com.iqbalwork.robithoh.widget

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.iqbalwork.robithoh.MainActivity
import com.iqbalwork.robithoh.R
import com.iqbalwork.robithoh.core.database.DatabaseDriverFactory
import com.iqbalwork.robithoh.core.database.createDatabase
import com.iqbalwork.robithoh.core.datetime.currentLocalDateTime
import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.model.LocationPreset
import com.iqbalwork.robithoh.feature.amaliyah.model.NextPrayerCountdown
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerCalculationMethods
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerSchedule
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerTimeAdjustments
import java.util.Calendar
import java.util.Locale

object PrayerWidgetHelper {

    const val ACTION_UPDATE_PRAYER_WIDGET = "com.iqbalwork.robithoh.ACTION_UPDATE_PRAYER_WIDGET"
    private const val ALARM_REQUEST_CODE = 9001

    data class PrayerWidgetData(
        val schedule: PrayerSchedule,
        val countdown: NextPrayerCountdown
    )

    /**
     * Mengambil konfigurasi jadwal sholat dari database lokal dan menghitung waktu sholat serta countdown.
     */
    fun loadPrayerData(context: Context): PrayerWidgetData {
        return try {
            val driver = DatabaseDriverFactory(context)
            val db = createDatabase(driver)
            val settings = db.robithohDatabaseQueries.getPrayerSettings().executeAsOneOrNull()

            val method = if (settings != null) {
                PrayerCalculationMethods.findById(settings.method_id)
            } else {
                PrayerCalculationMethods.DEFAULT
            }

            val adjustments = if (settings != null) {
                PrayerTimeAdjustments(
                    imsak = settings.imsak_offset.toInt(),
                    subuh = settings.subuh_offset.toInt(),
                    terbit = settings.terbit_offset.toInt(),
                    dzuhur = settings.dzuhur_offset.toInt(),
                    ashar = settings.ashar_offset.toInt(),
                    maghrib = settings.maghrib_offset.toInt(),
                    isya = settings.isya_offset.toInt()
                )
            } else {
                PrayerTimeAdjustments()
            }

            var customLat = settings?.custom_lat
            var customLng = settings?.custom_lng
            var customLocName = settings?.custom_location_name
            val isGps = settings?.is_gps == 1L || settings == null

            // Prioritaskan lokasi GPS terkini jika mode GPS aktif atau lokasi belum ditentukan
            if (isGps) {
                val hasFineLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                val hasCoarseLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                if (hasFineLoc || hasCoarseLoc) {
                    try {
                        val locManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                        val lastGps = locManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        val lastNetwork = locManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        val bestLoc = when {
                            lastGps != null && lastNetwork != null -> if (lastGps.time >= lastNetwork.time) lastGps else lastNetwork
                            lastGps != null -> lastGps
                            else -> lastNetwork
                        }
                        if (bestLoc != null) {
                            customLat = bestLoc.latitude
                            customLng = bestLoc.longitude
                            try {
                                val geocoder = Geocoder(context, Locale.getDefault())
                                val addresses = geocoder.getFromLocation(bestLoc.latitude, bestLoc.longitude, 1)
                                if (!addresses.isNullOrEmpty()) {
                                    val addr = addresses[0]
                                    customLocName = addr.subAdminArea ?: addr.locality ?: addr.adminArea ?: customLocName ?: "Lokasi GPS"
                                }
                            } catch (_: Throwable) {}
                        }
                    } catch (_: Throwable) {}
                }
            }

            val location = if (customLat != null && customLng != null) {
                LocationPreset(
                    name = customLocName ?: "Lokasi Tersimpan",
                    latitude = customLat,
                    longitude = customLng,
                    timezoneOffset = settings?.custom_timezone_offset ?: 7.0,
                    province = if (isGps) "GPS" else "Manual"
                )
            } else {
                PrayerTimesCalculator.DEFAULT_LOCATION
            }

            val now = currentLocalDateTime()
            val calculator = PrayerTimesCalculator()
            val schedule = calculator.calculateSchedule(
                year = now.year,
                month = now.month,
                day = now.day,
                latitude = location.latitude,
                longitude = location.longitude,
                timezoneOffset = location.timezoneOffset,
                locationName = location.name,
                method = method,
                adjustments = adjustments
            )

            val countdown = calculator.computeNextPrayer(
                schedule = schedule,
                currentHour = now.hour,
                currentMinute = now.minute,
                currentSecond = now.second
            )

            PrayerWidgetData(schedule, countdown)
        } catch (t: Throwable) {
            val now = currentLocalDateTime()
            val calculator = PrayerTimesCalculator()
            val defaultLoc = PrayerTimesCalculator.DEFAULT_LOCATION
            val schedule = calculator.calculateSchedule(
                year = now.year,
                month = now.month,
                day = now.day,
                latitude = defaultLoc.latitude,
                longitude = defaultLoc.longitude,
                timezoneOffset = defaultLoc.timezoneOffset,
                locationName = defaultLoc.name
            )
            val countdown = calculator.computeNextPrayer(
                schedule = schedule,
                currentHour = now.hour,
                currentMinute = now.minute,
                currentSecond = now.second
            )
            PrayerWidgetData(schedule, countdown)
        }
    }

    /**
     * Membangun RemoteViews untuk Widget 4x1 (Compact Bar)
     */
    fun buildRemoteViews4x1(context: Context, data: PrayerWidgetData): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_prayer_times_4x1)
        val schedule = data.schedule
        val countdown = data.countdown

        // Teks countdown
        val countdownText = if (countdown.isPrayerTimeNow) {
            "Saatnya Sholat!"
        } else if (countdown.remainingHours > 0) {
            "${countdown.remainingHours}j ${countdown.remainingMinutes}m lagi"
        } else {
            "${countdown.remainingMinutes} mnt lagi"
        }

        views.setTextViewText(R.id.tv_prayer_name, countdown.nextPrayerName)
        views.setTextViewText(R.id.tv_prayer_time, "${countdown.nextPrayerTime} ${schedule.timezone}")
        views.setTextViewText(R.id.tv_countdown, countdownText)
        views.setTextViewText(R.id.tv_location, schedule.locationName)
        views.setTextViewText(R.id.tv_date_hijri_short, if (schedule.hijriDateFormatted.isNotEmpty()) schedule.hijriDateFormatted else "Amaliyah")

        // Intent untuk membuka aplikasi saat widget di-klik
        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("NAVIGATE_TO", "PRAYER")
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pendingIntent = PendingIntent.getActivity(context, 1001, launchIntent, flags)
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        return views
    }

    /**
     * Membangun RemoteViews untuk Widget 4x2 (Full Schedule Card)
     */
    fun buildRemoteViews4x2(context: Context, data: PrayerWidgetData): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_prayer_times_4x2)
        val schedule = data.schedule
        val countdown = data.countdown

        // Header info
        views.setTextViewText(R.id.tv_location, schedule.locationName)
        val headerDate = if (schedule.hijriDateFormatted.isNotEmpty()) {
            "${schedule.dateFormatted} • ${schedule.hijriDateFormatted}"
        } else {
            schedule.dateFormatted
        }
        views.setTextViewText(R.id.tv_date_hijri, headerDate)

        // Highlight countdown
        val countdownText = if (countdown.isPrayerTimeNow) {
            "Saatnya Sholat!"
        } else if (countdown.remainingHours > 0) {
            "${countdown.remainingHours}j ${countdown.remainingMinutes}m lagi"
        } else {
            "${countdown.remainingMinutes} mnt lagi"
        }
        views.setTextViewText(R.id.tv_next_prayer_name, countdown.nextPrayerName)
        views.setTextViewText(R.id.tv_next_prayer_time, "${countdown.nextPrayerTime} ${schedule.timezone}")
        views.setTextViewText(R.id.tv_countdown, countdownText)

        // 5 Fardhu Prayer Times
        views.setTextViewText(R.id.time_subuh, schedule.subuh)
        views.setTextViewText(R.id.time_dzuhur, schedule.dzuhur)
        views.setTextViewText(R.id.time_ashar, schedule.ashar)
        views.setTextViewText(R.id.time_maghrib, schedule.maghrib)
        views.setTextViewText(R.id.time_isya, schedule.isya)

        // Reset highlight kartu
        val prayers = listOf(
            Triple("Subuh", R.id.card_subuh, R.id.label_subuh),
            Triple("Dzuhur", R.id.card_dzuhur, R.id.label_dzuhur),
            Triple("Ashar", R.id.card_ashar, R.id.label_ashar),
            Triple("Maghrib", R.id.card_maghrib, R.id.label_maghrib),
            Triple("Isya", R.id.card_isya, R.id.label_isya)
        )

        for ((name, cardId, labelId) in prayers) {
            if (name.equals(countdown.nextPrayerName, ignoreCase = true)) {
                views.setInt(cardId, "setBackgroundResource", R.drawable.bg_widget_active_prayer)
                views.setTextColor(labelId, 0xFFD4AF37.toInt())
            } else {
                views.setInt(cardId, "setBackgroundResource", R.drawable.bg_widget_inactive_prayer)
                views.setTextColor(labelId, 0xFFB0BEC5.toInt())
            }
        }

        // Amaliyah times
        views.setTextViewText(R.id.time_imsak, schedule.imsak)
        views.setTextViewText(R.id.time_dhuha, schedule.dhuha)
        views.setTextViewText(R.id.time_tahajjud, schedule.tahajjud)

        // Intent untuk membuka aplikasi saat widget di-klik
        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("NAVIGATE_TO", "PRAYER")
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pendingIntent = PendingIntent.getActivity(context, 1002, launchIntent, flags)
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        return views
    }

    /**
     * Memperbarui seluruh widget aktif (baik 4x1 maupun 4x2) dan menjadwalkan alarm refresh berikutnya.
     */
    fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
        val data = loadPrayerData(context)

        // Update 4x1
        val component4x1 = ComponentName(context, PrayerWidget4x1Provider::class.java)
        val ids4x1 = appWidgetManager.getAppWidgetIds(component4x1)
        if (ids4x1 != null && ids4x1.isNotEmpty()) {
            val views4x1 = buildRemoteViews4x1(context, data)
            appWidgetManager.updateAppWidget(ids4x1, views4x1)
        }

        // Update 4x2
        val component4x2 = ComponentName(context, PrayerWidget4x2Provider::class.java)
        val ids4x2 = appWidgetManager.getAppWidgetIds(component4x2)
        if (ids4x2 != null && ids4x2.isNotEmpty()) {
            val views4x2 = buildRemoteViews4x2(context, data)
            appWidgetManager.updateAppWidget(ids4x2, views4x2)
        }

        // Jadwalkan alarm update waktu sholat berikutnya
        scheduleNextUpdateAlarm(context, data.countdown)
    }

    /**
     * Menjadwalkan alarm tepat saat waktu sholat berikutnya tiba atau 30 menit ke depan (mana yang lebih dulu).
     */
    private fun scheduleNextUpdateAlarm(context: Context, countdown: NextPrayerCountdown) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, PrayerWidget4x1Provider::class.java).apply {
                action = ACTION_UPDATE_PRAYER_WIDGET
            }
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, flags)

            // Hitung trigger time: sisa detik sholat berikutnya, atau maksimal 30 menit
            val delaySeconds = if (countdown.totalRemainingSeconds in 1..1800) {
                countdown.totalRemainingSeconds + 2 // +2s agar melewati menit sholat
            } else {
                1800L // 30 menit
            }

            val triggerMillis = System.currentTimeMillis() + (delaySeconds * 1000L)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC, triggerMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC, triggerMillis, pendingIntent)
            }
        } catch (_: Throwable) {}
    }
}
