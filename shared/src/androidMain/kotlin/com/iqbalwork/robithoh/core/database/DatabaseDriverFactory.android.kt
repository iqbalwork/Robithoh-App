package com.iqbalwork.robithoh.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        val driver = AndroidSqliteDriver(
            schema = RobithohDatabase.Schema,
            context = context,
            name = "robithoh.db"
        )
        ensureTablesExist(driver)
        return driver
    }

    private fun ensureTablesExist(driver: SqlDriver) {
        try {
            driver.execute(
                null,
                """
                CREATE TABLE IF NOT EXISTS PrayerSettingsEntity (
                    id INTEGER NOT NULL PRIMARY KEY DEFAULT 1,
                    method_id TEXT NOT NULL DEFAULT 'KEMENAG',
                    imsak_offset INTEGER NOT NULL DEFAULT 0,
                    subuh_offset INTEGER NOT NULL DEFAULT 0,
                    terbit_offset INTEGER NOT NULL DEFAULT 0,
                    dzuhur_offset INTEGER NOT NULL DEFAULT 0,
                    ashar_offset INTEGER NOT NULL DEFAULT 0,
                    maghrib_offset INTEGER NOT NULL DEFAULT 0,
                    isya_offset INTEGER NOT NULL DEFAULT 0,
                    madhab TEXT NOT NULL DEFAULT 'SHAFI',
                    custom_lat REAL,
                    custom_lng REAL,
                    custom_location_name TEXT,
                    custom_timezone_offset REAL,
                    is_gps INTEGER NOT NULL DEFAULT 0,
                    selected_adzan_voice_id TEXT NOT NULL DEFAULT 'misyari_rasyid',
                    custom_adzan_audio_path TEXT,
                    adzan_volume REAL NOT NULL DEFAULT 1.0,
                    subuh_notif_enabled INTEGER NOT NULL DEFAULT 1,
                    dzuhur_notif_enabled INTEGER NOT NULL DEFAULT 1,
                    ashar_notif_enabled INTEGER NOT NULL DEFAULT 1,
                    maghrib_notif_enabled INTEGER NOT NULL DEFAULT 1,
                    isya_notif_enabled INTEGER NOT NULL DEFAULT 1,
                    imsak_notif_enabled INTEGER NOT NULL DEFAULT 0,
                    pre_reminder_enabled INTEGER NOT NULL DEFAULT 1
                );
                """.trimIndent(),
                0
            )
            try {
                driver.execute(
                    null,
                    "ALTER TABLE PrayerSettingsEntity ADD COLUMN adzan_volume REAL NOT NULL DEFAULT 1.0;",
                    0
                )
            } catch (_: Throwable) {} // kolom sudah ada di instalasi lama -> abaikan
            try {
                driver.execute(
                    null,
                    "ALTER TABLE PrayerSettingsEntity ADD COLUMN pre_reminder_enabled INTEGER NOT NULL DEFAULT 1;",
                    0
                )
            } catch (_: Throwable) {} // kolom sudah ada di instalasi lama -> abaikan
            driver.execute(
                null,
                """
                CREATE TABLE IF NOT EXISTS CachedDocumentEntity (
                    id TEXT NOT NULL PRIMARY KEY,
                    file_name TEXT NOT NULL,
                    sha256 TEXT NOT NULL,
                    content TEXT NOT NULL,
                    updated_at INTEGER NOT NULL
                );
                """.trimIndent(),
                0
            )
            driver.execute(
                null,
                "CREATE INDEX IF NOT EXISTS cached_doc_filename_idx ON CachedDocumentEntity(file_name);",
                0
            )
        } catch (_: Throwable) {}
    }
}
