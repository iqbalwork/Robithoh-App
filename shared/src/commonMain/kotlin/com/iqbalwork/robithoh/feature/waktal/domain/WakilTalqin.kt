package com.iqbalwork.robithoh.feature.waktal.domain

import com.iqbalwork.robithoh.shared.BuildKonfig

enum class WaktalStatus(val raw: String, val label: String) {
    SEMUA("semua", "Semua"),
    AKTIF("aktif", "Aktif"),
    WAFAT("wafat", "Almarhum");

    companion object {
        fun fromRaw(raw: String): WaktalStatus =
            entries.firstOrNull { it.raw.equals(raw, ignoreCase = true) } ?: SEMUA
    }
}

data class WakilTalqin(
    val id: Int,
    val nomorUrut: Int?,
    val namaLengkap: String,
    val namaResmi: String,
    val gelarDepan: String?,
    val gelarBelakang: String?,
    val status: WaktalStatus,
    val tahunWafat: Int?,
    val nomorTelepon: String?,
    val nomorWhatsapp: String?,
    val fotoUrl: String?,
    val negara: String = "Indonesia",
    val provinsi: String,
    val kotaKabupaten: String,
    val kecamatan: String?,
    val alamatLengkap: String?,
    val latitude: Double?,
    val longitude: Double?,
    val distanceKm: Double? = null,
    val majlisBinaan: String?,
    val biografiSingkat: String?
) {
    val isAlmarhum: Boolean get() = status == WaktalStatus.WAFAT
    val dialPhoneUri: String? get() = nomorTelepon?.takeIf { it.isNotBlank() }?.let { "tel:$it" }
    val whatsappUrl: String? get() = nomorWhatsapp?.takeIf { it.isNotBlank() }?.let { "https://wa.me/$it" }
    val formattedDistance: String? get() = HaversineDistance.formatDistance(distanceKm)
    val mapIntentUri: String? get() = if (latitude != null && longitude != null) {
        "geo:$latitude,$longitude?q=$latitude,$longitude(${namaLengkap.replace(" ", "+")})"
    } else null

    val normalizedFotoUrl: String? get() {
        val raw = fotoUrl?.trim() ?: return null
        if (raw.isBlank()) return null

        val baseUrl = BuildKonfig.BASE_URL.replace("\"", "").trimEnd('/')

        val normalizedPath = when {
            raw.contains("localhost") || raw.contains("127.0.0.1") -> {
                val pathAfterHost = raw.substringAfter("localhost").substringAfter("127.0.0.1")
                val pathWithoutPort = pathAfterHost.replaceFirst(Regex("^:\\d+"), "")
                pathWithoutPort.trimStart('/')
            }
            raw.contains("/storage/") -> {
                "storage/" + raw.substringAfter("/storage/")
            }
            raw.startsWith("https://api.robithoh.id/") || raw.startsWith("http://api.robithoh.id/") -> {
                raw.removePrefix("https://api.robithoh.id/").removePrefix("http://api.robithoh.id/").trimStart('/')
            }
            raw.startsWith("https://api.robithoh.com/") || raw.startsWith("http://api.robithoh.com/") -> {
                raw.removePrefix("https://api.robithoh.com/").removePrefix("http://api.robithoh.com/").trimStart('/')
            }
            raw.startsWith("/") -> {
                raw.trimStart('/')
            }
            !raw.startsWith("http://") && !raw.startsWith("https://") -> {
                raw.trimStart('/')
            }
            else -> null
        }

        return if (normalizedPath != null) "$baseUrl/$normalizedPath" else raw
    }
}
