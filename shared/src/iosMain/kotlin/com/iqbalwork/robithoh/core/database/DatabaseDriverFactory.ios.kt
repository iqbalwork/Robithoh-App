package com.iqbalwork.robithoh.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = RobithohDatabase.Schema,
            name = "robithoh.db"
        )
    }
}
