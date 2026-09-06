package com.iqbalwork.robithoh.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single<CoroutineDispatcher> { Dispatchers.Default }
    single<com.iqbalwork.robithoh.core.analytics.AnalyticsTracker> { com.iqbalwork.robithoh.core.analytics.getAnalyticsTracker() }
}

fun appModules(): List<Module> = listOf(
    appModule,
    databaseModule,
    audioModule,
    networkModule,
    viewModelModule
)

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModules())
}

/**
 * Convenience entry point for iOS Swift initialization.
 */
fun initKoinIos() = initKoin {}
