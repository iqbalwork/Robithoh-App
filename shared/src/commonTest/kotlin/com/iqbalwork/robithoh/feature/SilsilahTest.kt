package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.manaqib.data.ManaqibDataSeeder
import kotlin.test.*

class SilsilahTest {

    @Test
    fun testSilsilah111MonotonicAndNoGaps() {
        val silsilah = ManaqibDataSeeder.silsilahNodes
        assertEquals(111, silsilah.size, "Silsilah list must have exactly 111 nodes")

        val orders = silsilah.map { it.orderNumber }
        assertEquals((1..111).toList(), orders, "Order numbers must be contiguous from 1 to 111")

        for (node in silsilah) {
            assertTrue(node.name.isNotBlank(), "Node ${node.orderNumber} must have non-blank name")
            assertTrue(node.title.isNotBlank(), "Node ${node.orderNumber} must have non-blank title")
            assertTrue(node.locationOrEpithet.isNotBlank(), "Node ${node.orderNumber} must have non-blank location/epithet")
        }
    }

    @Test
    fun testKeySilsilahFigures() {
        val silsilah = ManaqibDataSeeder.silsilahNodes

        // #1 Rasulullah SAW
        val node1 = silsilah[0]
        assertEquals(1, node1.orderNumber)
        assertTrue(node1.name.contains("Muhammad"))
        assertTrue(node1.arabicName.contains("مُحَمَّدٌ"))

        // #2 Ali bin Abi Thalib
        val node2 = silsilah[1]
        assertEquals(2, node2.orderNumber)
        assertTrue(node2.name.contains("Ali"))

        // #17 Syekh Abdul Qodir Al-Jailani
        val node17 = silsilah.find { it.orderNumber == 17 }
        assertNotNull(node17)
        assertTrue(node17.name.contains("Abdul Qodir Al-Jailani"))
        assertTrue(node17.title.contains("Sulthonul Auliya"))

        // #32 Syekh Ahmad Khotib Sambas
        val node32 = silsilah.find { it.orderNumber == 32 }
        assertNotNull(node32)
        assertTrue(node32.name.contains("Ahmad Khotib Sambas"))

        // #34 Abah Sepuh (Syekh Abdullah Mubarok)
        val node34 = silsilah.find { it.orderNumber == 34 }
        assertNotNull(node34)
        assertTrue(node34.name.contains("Abdullah Mubarok") || node34.name.contains("Abah Sepuh"))

        // #35 Abah Anom (Syekh Ahmad Shohibulwafa Tajul Arifin)
        val node35 = silsilah.find { it.orderNumber == 35 }
        assertNotNull(node35)
        assertTrue(node35.name.contains("Ahmad Shohibulwafa") || node35.name.contains("Abah Anom"))

        // #36 Abah Aos
        val node36 = silsilah.find { it.orderNumber == 36 }
        assertNotNull(node36)
        assertTrue(node36.name.contains("Abdul Gaos") || node36.name.contains("Abah Aos"))

        // #111 Abah Aos
        val node111 = silsilah.find { it.orderNumber == 111 }
        assertNotNull(node111)
        assertTrue(node111.name.contains("Abdul Gaos") || node111.name.contains("Abah Aos"))
    }
}
