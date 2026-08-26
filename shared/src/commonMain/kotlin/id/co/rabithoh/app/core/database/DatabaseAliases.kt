package id.co.rabithoh.app.core.database

typealias DatabaseDriverFactory = com.iqbalwork.robithoh.core.database.DatabaseDriverFactory
typealias RobithohDatabase = com.iqbalwork.robithoh.core.database.RobithohDatabase
typealias ManqobahEntity = com.iqbalwork.robithoh.core.database.ManqobahEntity
typealias BookmarkEntity = com.iqbalwork.robithoh.core.database.BookmarkEntity
typealias AmaliyahProgressEntity = com.iqbalwork.robithoh.core.database.AmaliyahProgressEntity

fun createDatabase(driverFactory: DatabaseDriverFactory): RobithohDatabase {
    return com.iqbalwork.robithoh.core.database.createDatabase(driverFactory)
}
