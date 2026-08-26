package id.co.rabithoh.app.di

import org.koin.dsl.KoinAppDeclaration

val appModule = com.iqbalwork.robithoh.di.appModule
val databaseModule = com.iqbalwork.robithoh.di.databaseModule
val audioModule = com.iqbalwork.robithoh.di.audioModule
val viewModelModule = com.iqbalwork.robithoh.di.viewModelModule

fun appModules() = com.iqbalwork.robithoh.di.appModules()
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = com.iqbalwork.robithoh.di.initKoin(appDeclaration)
fun initKoinIos() = com.iqbalwork.robithoh.di.initKoinIos()
