package com.iqbalwork.robithoh.feature.waktal.domain

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
}
