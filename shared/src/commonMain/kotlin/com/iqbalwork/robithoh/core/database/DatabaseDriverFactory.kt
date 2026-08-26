package com.iqbalwork.robithoh.core.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DatabaseDriverFactory): RobithohDatabase {
    val driver = driverFactory.createDriver()
    return RobithohDatabase(driver)
}
