package com.iqbalwork.robithoh.core.location

import kotlin.test.Test
import kotlin.test.assertEquals

class LocationSanitizerTest {

    @Test
    fun testSanitizeRawGpsWithCoordsInString() {
        val result = LocationSanitizer.sanitize("Lokasi GPS (-6.87, 107.57)")
        assertEquals("Kota Bandung", result)
    }

    @Test
    fun testSanitizeRawGpsWithSuppliedCoords() {
        val result = LocationSanitizer.sanitize("Lokasi GPS", -6.87, 107.57)
        assertEquals("Kota Bandung", result)
    }

    @Test
    fun testSanitizeRawGpsWithoutCoords() {
        val result = LocationSanitizer.sanitize("Lokasi GPS")
        assertEquals("Wilayah Anda", result)
    }

    @Test
    fun testSanitizeEnglishCitySuffix() {
        val result = LocationSanitizer.sanitize("Bandung City")
        assertEquals("Kota Bandung", result)
    }

    @Test
    fun testSanitizeEnglishRegencySuffix() {
        val result = LocationSanitizer.sanitize("Bandung Regency")
        assertEquals("Kabupaten Bandung", result)
    }

    @Test
    fun testSanitizeNormalIndonesianCity() {
        assertEquals("Kota Bandung", LocationSanitizer.sanitize("Kota Bandung"))
        assertEquals("Kabupaten Ciamis", LocationSanitizer.sanitize("Kabupaten Ciamis"))
    }

    @Test
    fun testFindNearestCityForBandungCoords() {
        val city = LocationSanitizer.findNearestCity(-6.87, 107.57)
        assertEquals("Kota Bandung", city)
    }

    @Test
    fun testFindNearestCityForCiamisCoords() {
        val city = LocationSanitizer.findNearestCity(-7.32, 108.35)
        assertEquals("Kabupaten Ciamis", city)
    }

    @Test
    fun testNullOrBlankName() {
        assertEquals("Wilayah Anda", LocationSanitizer.sanitize(null))
        assertEquals("Wilayah Anda", LocationSanitizer.sanitize("   "))
        assertEquals("Kota Bandung", LocationSanitizer.sanitize(null, -6.87, 107.57))
    }
}
