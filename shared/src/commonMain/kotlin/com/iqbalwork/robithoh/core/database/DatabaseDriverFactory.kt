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
    } catch (_: Exception) {
        // Ignore if already created or managed
    }
    return RobithohDatabase(driver)
}
