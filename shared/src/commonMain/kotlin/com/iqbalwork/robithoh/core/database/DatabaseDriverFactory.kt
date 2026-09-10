package com.iqbalwork.robithoh.core.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun SqlDriver.addColumnIfNotExists(
    tableName: String,
    columnName: String,
    columnDefinition: String
) {
    try {
        val cursor = executeQuery(
            identifier = null,
            sql = "PRAGMA table_info($tableName);",
            mapper = { cursor ->
                var found = false
                while (cursor.next().value) {
                    val name = cursor.getString(1)
                    if (name == columnName) {
                        found = true
                        break
                    }
                }
                QueryResult.Value(found)
            },
            parameters = 0
        )
        val columnExists = cursor.value
        if (!columnExists) {
            execute(
                identifier = null,
                sql = "ALTER TABLE $tableName ADD COLUMN $columnName $columnDefinition;",
                parameters = 0
            )
        }
    } catch (_: Throwable) {
        try {
            execute(
                identifier = null,
                sql = "ALTER TABLE $tableName ADD COLUMN $columnName $columnDefinition;",
                parameters = 0
            )
        } catch (_: Throwable) {}
    }
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
            sql = """
                CREATE TABLE IF NOT EXISTS AppSettingsEntity (
                    id INTEGER NOT NULL PRIMARY KEY DEFAULT 1,
                    has_completed_onboarding INTEGER NOT NULL DEFAULT 0,
                    has_seen_reader_spotlight INTEGER NOT NULL DEFAULT 0,
                    has_seen_prayer_spotlight INTEGER NOT NULL DEFAULT 0,
                    has_seen_quran_spotlight INTEGER NOT NULL DEFAULT 0,
                    has_seen_quran_page_spotlight INTEGER NOT NULL DEFAULT 0
                );
            """.trimIndent(),
            parameters = 0
        )
        driver.addColumnIfNotExists("AppSettingsEntity", "has_seen_prayer_spotlight", "INTEGER NOT NULL DEFAULT 0")
        driver.addColumnIfNotExists("AppSettingsEntity", "has_seen_quran_spotlight", "INTEGER NOT NULL DEFAULT 0")
        driver.addColumnIfNotExists("AppSettingsEntity", "has_seen_quran_page_spotlight", "INTEGER NOT NULL DEFAULT 0")
    } catch (_: Throwable) {
        // Ignore if already created or managed
    }
    return RobithohDatabase(driver)
}
