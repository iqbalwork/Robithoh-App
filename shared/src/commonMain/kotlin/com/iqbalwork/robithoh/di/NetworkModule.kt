package com.iqbalwork.robithoh.di

import com.iqbalwork.robithoh.core.network.createKtorHttpClient
import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncManager
import com.iqbalwork.robithoh.feature.waktal.data.WaktalRepository
import com.iqbalwork.robithoh.feature.waktal.data.remote.WaktalApiService
import com.iqbalwork.robithoh.feature.waktal.data.sync.WaktalSyncManager
import org.koin.dsl.module

val networkModule = module {
    single { createKtorHttpClient() }
    single { MarkdownDocumentRepository(database = getOrNull()) }
    single {
        DocumentSyncManager(
            httpClient = get(),
            database = get(),
            repository = get()
        )
    }
    single { WaktalApiService(httpClient = get()) }
    single { WaktalRepository(database = getOrNull()) }
    single {
        WaktalSyncManager(
            apiService = get(),
            repository = get()
        )
    }
}

