package com.iqbalwork.robithoh.feature.waktal.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseApiResponse<T>(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: T? = null,
    @SerialName("meta") val meta: PaginationMetaDto? = null
)

@Serializable
data class PaginationMetaDto(
    @SerialName("current_page") val currentPage: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("total") val total: Int
)

@Serializable
data class WakilTalqinItemDto(
    @SerialName("id") val id: Int,
    @SerialName("nomor_urut") val nomorUrut: Int? = null,
    @SerialName("nama_lengkap") val namaLengkap: String,
    @SerialName("nama_resmi") val namaResmi: String,
    @SerialName("gelar_depan") val gelarDepan: String? = null,
    @SerialName("gelar_belakang") val gelarBelakang: String? = null,
    @SerialName("status") val status: String,
    @SerialName("status_label") val statusLabel: String,
    @SerialName("tahun_wafat") val tahunWafat: Int? = null,
    @SerialName("nomor_telepon") val nomorTelepon: String? = null,
    @SerialName("nomor_whatsapp") val nomorWhatsapp: String? = null,
    @SerialName("foto_url") val fotoUrl: String? = null,
    @SerialName("negara") val negara: String = "Indonesia",
    @SerialName("provinsi") val provinsi: String,
    @SerialName("kota_kabupaten") val kotaKabupaten: String,
    @SerialName("kecamatan") val kecamatan: String? = null,
    @SerialName("alamat_lengkap") val alamatLengkap: String? = null,
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("distance_km") val distanceKm: Double? = null,
    @SerialName("majlis_binaan") val majlisBinaan: String? = null,
    @SerialName("biografi_singkat") val biografiSingkat: String? = null,
    @SerialName("updated_at") val updatedAt: Long = 0L
)

@Serializable
data class SyncManifestResponseDto(
    @SerialName("entities") val entities: Map<String, EntityVersionDto> = emptyMap()
)

@Serializable
data class EntityVersionDto(
    @SerialName("version_code") val versionCode: Int,
    @SerialName("checksum") val checksum: String? = null
)
