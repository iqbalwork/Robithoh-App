package com.iqbalwork.robithoh.core.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DatabaseDriverFactory): RobithohDatabase {
    val driver = driverFactory.createDriver()
    try {
        driver.execute(
            identifier = null,
            sql = "CREATE TABLE IF NOT EXISTS ReaderSettingsEntity (id INTEGER NOT NULL PRIMARY KEY DEFAULT 1, font_scale REAL NOT NULL DEFAULT 1.0, theme_id TEXT NOT NULL DEFAULT 'system');",
            parameters = 0
        )
        driver.execute(
            identifier = null,
            sql = "CREATE TABLE IF NOT EXISTS AppSettingsEntity (id INTEGER NOT NULL PRIMARY KEY DEFAULT 1, has_completed_onboarding INTEGER NOT NULL DEFAULT 0, has_seen_reader_spotlight INTEGER NOT NULL DEFAULT 0, has_seen_prayer_spotlight INTEGER NOT NULL DEFAULT 0, has_seen_quran_spotlight INTEGER NOT NULL DEFAULT 0);",
            parameters = 0
        )
        try {
            driver.execute(null, "ALTER TABLE AppSettingsEntity ADD COLUMN has_seen_prayer_spotlight INTEGER NOT NULL DEFAULT 0;", 0)
        } catch (_: Exception) {}
        try {
            driver.execute(null, "ALTER TABLE AppSettingsEntity ADD COLUMN has_seen_quran_spotlight INTEGER NOT NULL DEFAULT 0;", 0)
        } catch (_: Exception) {}
    } catch (_: Exception) {
        // Ignore if already created or managed
    }
    return RobithohDatabase(driver)
}
