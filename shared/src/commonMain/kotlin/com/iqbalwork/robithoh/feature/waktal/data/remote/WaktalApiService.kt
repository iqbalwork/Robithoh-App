package com.iqbalwork.robithoh.feature.waktal.data.remote

import com.iqbalwork.robithoh.shared.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WaktalApiService(
    private val httpClient: HttpClient,
    baseUrl: String = BuildKonfig.BASE_URL
) {
    private val apiBaseUrl: String = run {
        val trimmed = baseUrl.replace("\"", "").trimEnd('/')
        if (trimmed.endsWith("/api/v1")) trimmed else "$trimmed/api/v1"
    }

    suspend fun checkVersionManifest(): SyncManifestResponseDto {
        println("[WaktalApiService] GET $apiBaseUrl/sync/version-manifest")
        return httpClient.get("$apiBaseUrl/sync/version-manifest").body()
    }

    suspend fun fetchWaktalDelta(): BaseApiResponse<List<WakilTalqinItemDto>> {
        println("[WaktalApiService] GET $apiBaseUrl/sync/delta/waktal")
        return httpClient.get("$apiBaseUrl/sync/delta/waktal").body()
    }

    suspend fun fetchWaktalList(
        search: String? = null,
        status: String = "semua",
        provinsi: String? = null,
        kota: String? = null,
        lat: Double? = null,
        lng: Double? = null,
        page: Int = 1,
        perPage: Int = 20
    ): BaseApiResponse<List<WakilTalqinItemDto>> {
        println("[WaktalApiService] GET $apiBaseUrl/waktal (search=$search, status=$status, provinsi=$provinsi, kota=$kota, lat=$lat, lng=$lng)")
        return httpClient.get("$apiBaseUrl/waktal") {
            if (!search.isNullOrBlank()) parameter("search", search)
            parameter("status", status)
            if (!provinsi.isNullOrBlank()) parameter("provinsi", provinsi)
            if (!kota.isNullOrBlank()) parameter("kota", kota)
            if (lat != null) parameter("lat", lat)
            if (lng != null) parameter("lng", lng)
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
    }

    suspend fun fetchWaktalDetail(id: Int): BaseApiResponse<WakilTalqinItemDto> {
        println("[WaktalApiService] GET $apiBaseUrl/waktal/$id")
        return httpClient.get("$apiBaseUrl/waktal/$id").body()
    }
}
