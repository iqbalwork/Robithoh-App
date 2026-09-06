package com.iqbalwork.robithoh.di

import com.iqbalwork.robithoh.core.network.createKtorHttpClient
import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncManager
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
}
