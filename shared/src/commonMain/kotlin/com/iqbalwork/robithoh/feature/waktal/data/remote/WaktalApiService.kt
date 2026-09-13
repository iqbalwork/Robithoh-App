package com.iqbalwork.robithoh.feature.waktal.data.remote

import com.iqbalwork.robithoh.shared.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WaktalApiService(
    private val httpClient: HttpClient,
    private val baseUrl: String = BuildKonfig.BASE_URL
) {
    suspend fun checkVersionManifest(): SyncManifestResponseDto {
        return httpClient.get("$baseUrl/sync/version-manifest").body()
    }

    suspend fun fetchWaktalDelta(): BaseApiResponse<List<WakilTalqinItemDto>> {
        return httpClient.get("$baseUrl/sync/delta/waktal").body()
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
        return httpClient.get("$baseUrl/waktal") {
            parameter("search", search)
            parameter("status", status)
            parameter("provinsi", provinsi)
            parameter("kota", kota)
            parameter("lat", lat)
            parameter("lng", lng)
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
    }

    suspend fun fetchWaktalDetail(id: Int): BaseApiResponse<WakilTalqinItemDto> {
        return httpClient.get("$baseUrl/waktal/$id").body()
    }
}
