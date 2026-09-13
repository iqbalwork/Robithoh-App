package com.iqbalwork.robithoh.core.network

import com.iqbalwork.robithoh.shared.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createKtorHttpClient(
    enableLogging: Boolean = BuildKonfig.IS_DEBUG
): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
            })
        }

        if (enableLogging) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
            }
        }
    }
}
