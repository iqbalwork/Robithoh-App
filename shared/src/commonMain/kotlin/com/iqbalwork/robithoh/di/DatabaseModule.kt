package com.iqbalwork.robithoh.di

import com.iqbalwork.robithoh.core.database.DatabaseDriverFactory
import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.core.database.createDatabase
import org.koin.dsl.module

val databaseModule = module {
    single<RobithohDatabase> {
        val driverFactory = get<DatabaseDriverFactory>()
        createDatabase(driverFactory)
    }
}
