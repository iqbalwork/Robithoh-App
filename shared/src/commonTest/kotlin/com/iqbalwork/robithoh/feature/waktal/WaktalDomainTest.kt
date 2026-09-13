package com.iqbalwork.robithoh.feature.waktal

import com.iqbalwork.robithoh.feature.waktal.domain.WakilTalqin
import com.iqbalwork.robithoh.feature.waktal.domain.WaktalStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WaktalDomainTest {

    @Test
    fun testWaktalStatusParsing() {
        assertEquals(WaktalStatus.AKTIF, WaktalStatus.fromRaw("aktif"))
        assertEquals(WaktalStatus.WAFAT, WaktalStatus.fromRaw("wafat"))
        assertEquals(WaktalStatus.SEMUA, WaktalStatus.fromRaw("semua"))
        assertEquals(WaktalStatus.SEMUA, WaktalStatus.fromRaw("unknown"))
    }

    @Test
    fun testWakilTalqinModelPropertiesAndIntentUris() {
        val waktalAktif = WakilTalqin(
            id = 1,
            nomorUrut = 1,
            namaLengkap = "KH Baban Ahmad Jihad",
            namaResmi = "KH Baban Ahmad Jihad",
            gelarDepan = "KH",
            gelarBelakang = null,
            status = WaktalStatus.AKTIF,
            tahunWafat = null,
            nomorTelepon = "081234567890",
            nomorWhatsapp = "6281234567890",
            fotoUrl = null,
            negara = "Indonesia",
            provinsi = "Jawa Barat",
            kotaKabupaten = "Tasikmalaya",
            kecamatan = "Pageurageung",
            alamatLengkap = "PP Suryalaya",
            latitude = -7.1126,
            longitude = 108.2045,
            distanceKm = 12.5,
            majlisBinaan = "PP Suryalaya",
            biografiSingkat = "Pimpinan PP Suryalaya"
        )

        assertFalse(waktalAktif.isAlmarhum)
        assertEquals("tel:081234567890", waktalAktif.dialPhoneUri)
        assertEquals("https://wa.me/6281234567890", waktalAktif.whatsappUrl)
        assertEquals("12.5 km", waktalAktif.formattedDistance)
        assertTrue(waktalAktif.mapIntentUri?.startsWith("geo:-7.1126,108.2045") == true)
    }

    @Test
    fun testWakilTalqinAlmarhumProperties() {
        val waktalWafat = WakilTalqin(
            id = 2,
            nomorUrut = 2,
            namaLengkap = "M Sholeh Mukhtar",
            namaResmi = "(ALM) KH M Sholeh Mukhtar",
            gelarDepan = "KH",
            gelarBelakang = null,
            status = WaktalStatus.WAFAT,
            tahunWafat = 2020,
            nomorTelepon = null,
            nomorWhatsapp = null,
            fotoUrl = null,
            provinsi = "DKI Jakarta",
            kotaKabupaten = "Jakarta Barat",
            kecamatan = null,
            alamatLengkap = null,
            latitude = null,
            longitude = null,
            majlisBinaan = null,
            biografiSingkat = null
        )

        assertTrue(waktalWafat.isAlmarhum)
        assertNull(waktalWafat.dialPhoneUri)
        assertNull(waktalWafat.whatsappUrl)
        assertNull(waktalWafat.mapIntentUri)
    }

    @Test
    fun testNormalizedFotoUrl() {
        fun createWaktal(fotoUrl: String?) = WakilTalqin(
            id = 1, nomorUrut = 1, namaLengkap = "Test", namaResmi = "Test",
            gelarDepan = null, gelarBelakang = null, status = WaktalStatus.AKTIF,
            tahunWafat = null, nomorTelepon = null, nomorWhatsapp = null,
            fotoUrl = fotoUrl, provinsi = "Jabar", kotaKabupaten = "Bandung",
            kecamatan = null, alamatLengkap = null, latitude = null, longitude = null,
            majlisBinaan = null, biografiSingkat = null
        )

        assertNull(createWaktal(null).normalizedFotoUrl)
        assertNull(createWaktal("  ").normalizedFotoUrl)

        val expectedBase = "http://192.168.101.7:8000"

        assertEquals(
            "$expectedBase/storage/waktal/1-KH-M-Sholeh-Mukhtar-Hujatul-Arifin.png",
            createWaktal("http://localhost:8000/storage/waktal/1-KH-M-Sholeh-Mukhtar-Hujatul-Arifin.png").normalizedFotoUrl
        )

        assertEquals(
            "$expectedBase/storage/waktal/1-KH-M-Sholeh-Mukhtar-Hujatul-Arifin.png",
            createWaktal("http://127.0.0.1:8000/storage/waktal/1-KH-M-Sholeh-Mukhtar-Hujatul-Arifin.png").normalizedFotoUrl
        )

        assertEquals(
            "$expectedBase/storage/waktal/1-KH-M-Sholeh-Mukhtar-Hujatul-Arifin.png",
            createWaktal("https://api.robithoh.id/storage/waktal/1-KH-M-Sholeh-Mukhtar-Hujatul-Arifin.png").normalizedFotoUrl
        )

        assertEquals(
            "$expectedBase/storage/waktal/1-KH-M-Sholeh-Mukhtar-Hujatul-Arifin.png",
            createWaktal("/storage/waktal/1-KH-M-Sholeh-Mukhtar-Hujatul-Arifin.png").normalizedFotoUrl
        )
    }
}
