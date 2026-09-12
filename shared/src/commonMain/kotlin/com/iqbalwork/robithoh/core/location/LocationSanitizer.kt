package com.iqbalwork.robithoh.core.location

import kotlin.math.PI
import kotlin.math.cos

/**
 * Utility for sanitizing location names and resolving raw GPS coordinates
 * to clean, human-readable Indonesian city / district names (e.g., "Kota Bandung").
 */
object LocationSanitizer {

    private data class CityCoord(
        val name: String,
        val latitude: Double,
        val longitude: Double,
    )

    private val KNOWN_CITIES = listOf(
        CityCoord("Kota Bandung", -6.9175, 107.6191),
        CityCoord("Kabupaten Ciamis", -7.3256, 108.3533),
        CityCoord("Pesantren Sirnarasa Panjalu", -7.1432, 108.2831),
        CityCoord("Pontren Suryalaya", -7.1581, 108.2169),
        CityCoord("Kota Tasikmalaya", -7.3274, 108.2207),
        CityCoord("Kabupaten Garut", -7.2278, 107.9086),
        CityCoord("Kabupaten Sumedang", -6.8582, 107.9262),
        CityCoord("Kota Bogor", -6.5950, 106.8166),
        CityCoord("Kota Depok", -6.4025, 106.7942),
        CityCoord("Kota Bekasi", -6.2383, 106.9756),
        CityCoord("Kota Tangerang", -6.1783, 106.6300),
        CityCoord("Kota Tangerang Selatan", -6.2886, 106.7179),
        CityCoord("Kota Sukabumi", -6.9277, 106.9300),
        CityCoord("Kota Cirebon", -6.7320, 108.5523),
        CityCoord("Kabupaten Kuningan", -6.9760, 108.4834),
        CityCoord("Kabupaten Majalengka", -6.8364, 108.2274),
        CityCoord("Kabupaten Indramayu", -6.3263, 108.3200),
        CityCoord("Kabupaten Subang", -6.5715, 107.7587),
        CityCoord("Kabupaten Purwakarta", -6.5569, 107.4433),
        CityCoord("Kabupaten Karawang", -6.3073, 107.3019),
        CityCoord("Jakarta Pusat", -6.2088, 106.8456),
        CityCoord("Jakarta Selatan", -6.2615, 106.8106),
        CityCoord("Jakarta Barat", -6.1683, 106.7588),
        CityCoord("Jakarta Timur", -6.2250, 106.9004),
        CityCoord("Jakarta Utara", -6.1214, 106.8827),
        CityCoord("Kota Serang", -6.1200, 106.1500),
        CityCoord("Kota Cilegon", -6.0174, 106.0538),
        CityCoord("Kota Semarang", -6.9667, 110.4167),
        CityCoord("Kota Surakarta (Solo)", -7.5755, 110.8243),
        CityCoord("Kota Yogyakarta", -7.7956, 110.3695),
        CityCoord("Kota Magelang", -7.4706, 110.2178),
        CityCoord("Kabupaten Banyumas (Purwokerto)", -7.4243, 109.2301),
        CityCoord("Kota Surabaya", -7.2575, 112.7521),
        CityCoord("Kota Malang", -7.9666, 112.6326),
        CityCoord("Kota Kediri", -7.8480, 112.0178),
        CityCoord("Kabupaten Banyuwangi", -8.2192, 114.3692),
        CityCoord("Kota Denpasar", -8.6705, 115.2126),
        CityCoord("Kota Mataram", -8.5833, 116.1167),
        CityCoord("Kota Kupang", -10.1772, 123.6070),
        CityCoord("Kota Banda Aceh", 5.5483, 95.3238),
        CityCoord("Kota Medan", 3.5952, 98.6722),
        CityCoord("Kota Padang", -0.9471, 100.4172),
        CityCoord("Kota Pekanbaru", 0.5071, 101.4478),
        CityCoord("Kota Batam", 1.1301, 104.0529),
        CityCoord("Kota Palembang", -2.9761, 104.7754),
        CityCoord("Kota Bandar Lampung", -5.4292, 105.2625),
        CityCoord("Kota Pontianak", -0.0263, 109.3425),
        CityCoord("Kota Banjarmasin", -3.3194, 114.5908),
        CityCoord("Kota Samarinda", -0.5022, 117.1536),
        CityCoord("Kota Balikpapan", -1.2379, 116.8529),
        CityCoord("Kota Makassar", -5.1477, 119.4327),
        CityCoord("Kota Manado", 1.4748, 124.8428),
        CityCoord("Kota Ambon", -3.6954, 128.1814),
        CityCoord("Kota Jayapura", -2.5489, 140.7197),
    )

    /**
     * Finds the nearest city name for given latitude and longitude coordinates.
     */
    fun findNearestCity(latitude: Double, longitude: Double): String {
        var minDistanceSq = Double.MAX_VALUE
        var nearestName = "Kota Bandung"

        for (city in KNOWN_CITIES) {
            val dLat = latitude - city.latitude
            val dLng = (longitude - city.longitude) * cos(latitude * (PI / 180.0))
            val distSq = (dLat * dLat) + (dLng * dLng)
            if (distSq < minDistanceSq) {
                minDistanceSq = distSq
                nearestName = city.name
            }
        }
        return nearestName
    }

    /**
     * Clean and format address names from Geocoder or raw strings.
     * Replaces English suffixes ("Regency" -> "Kabupaten", "City" -> "Kota")
     * and ensures raw coordinates or "Lokasi GPS" strings are turned into real city names.
     */
    fun sanitize(rawName: String?, latitude: Double? = null, longitude: Double? = null): String {
        if (rawName.isNullOrBlank()) {
            return if ((latitude != null) && (longitude != null)) {
                findNearestCity(latitude, longitude)
            } else {
                "Wilayah Anda"
            }
        }

        val trimmed = rawName.trim()

        // Check if rawName is or contains "Lokasi GPS" or raw coordinates like "(-6.87, 107.57)"
        val isGpsRaw = trimmed.contains("Lokasi GPS", ignoreCase = true) ||
                trimmed.contains("(") ||
                trimmed.matches(Regex(".*-?\\d+\\.\\d+.*"))

        if (isGpsRaw) {
            val (extractedLat, extractedLng) = if ((latitude != null) && (longitude != null)) {
                Pair(latitude, longitude)
            } else {
                extractCoordsFromString(trimmed) ?: Pair(null, null)
            }

            return if ((extractedLat != null) && (extractedLng != null)) {
                findNearestCity(extractedLat, extractedLng)
            } else {
                "Wilayah Anda"
            }
        }

        // Clean up English reverse geocoding suffixes if present
        var cleaned = trimmed
        if (cleaned.endsWith(" City", ignoreCase = true)) {
            val base = cleaned.substring(0, cleaned.length - 5).trim()
            cleaned = "Kota $base"
        } else if (cleaned.endsWith(" Regency", ignoreCase = true)) {
            val base = cleaned.substring(0, cleaned.length - 8).trim()
            cleaned = "Kabupaten $base"
        }

        return cleaned
    }

    private fun extractCoordsFromString(text: String): Pair<Double, Double>? {
        val regex = Regex("(-?\\d+\\.\\d+)\\s*,\\s*(-?\\d+\\.\\d+)")
        val match = regex.find(text) ?: return null
        val lat = match.groupValues[1].toDoubleOrNull() ?: return null
        val lng = match.groupValues[2].toDoubleOrNull() ?: return null
        return Pair(lat, lng)
    }
}
