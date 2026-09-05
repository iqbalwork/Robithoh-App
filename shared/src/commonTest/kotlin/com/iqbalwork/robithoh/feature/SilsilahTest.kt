package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.manaqib.data.ManaqibDataSeeder
import kotlin.test.*

class SilsilahTest {

    @Test
    fun testSilsilah38MonotonicAndNoGaps() {
        val silsilah = ManaqibDataSeeder.silsilahNodes
        assertEquals(38, silsilah.size, "Silsilah list must have exactly 38 nodes")

        val orders = silsilah.map { it.orderNumber }
        assertEquals((1..38).toList(), orders, "Order numbers must be contiguous from 1 to 38")

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

        // #36 Abah Sepuh (Syekh Abdullah Mubarok)
        val node36 = silsilah.find { it.orderNumber == 36 }
        assertNotNull(node36)
        assertTrue(node36.name.contains("Abdullah Mubarok") || node36.name.contains("Abah Sepuh"))

        // #37 Abah Anom (Syekh Ahmad Shohibulwafa Tajul Arifin)
        val node37 = silsilah.find { it.orderNumber == 37 }
        assertNotNull(node37)
        assertTrue(node37.name.contains("Ahmad Shohibulwafa") || node37.name.contains("Abah Anom"))

        // #38 Abah Aos (Pangersa Abdul Gaos Saefulloh Maslul)
        val node38 = silsilah.find { it.orderNumber == 38 }
        assertNotNull(node38)
        assertTrue(node38.name.contains("Abdul Gaos") || node38.name.contains("Abah Aos"))
    }
}
