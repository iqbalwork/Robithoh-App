package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentManifest
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DocumentSyncTest {

    @Test
    fun testParseDocumentManifest() {
        val sampleJson = """
            {
              "version": 2,
              "updatedAt": "2026-09-04T07:00:00Z",
              "totalDocuments": 2,
              "documents": [
                {
                  "fileName": "DZIKIR_TQN.md",
                  "sha256": "abc123hash",
                  "size": 18240,
                  "url": "https://cdn.jsdelivr.net/gh/iqbalwork/Robithoh-Docs@main/documents/DZIKIR_TQN.md",
                  "rawUrl": "https://raw.githubusercontent.com/iqbalwork/Robithoh-Docs/main/documents/DZIKIR_TQN.md",
                  "pagesUrl": "https://iqbalwork.github.io/Robithoh-Docs/documents/DZIKIR_TQN.md"
                },
                {
                  "fileName": "KHOTAMAN_TQN.md",
                  "sha256": "def456hash",
                  "size": 25000,
                  "url": "https://cdn.jsdelivr.net/gh/iqbalwork/Robithoh-Docs@main/documents/KHOTAMAN_TQN.md"
                }
              ]
            }
        """.trimIndent()

        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        val manifest = json.decodeFromString<DocumentManifest>(sampleJson)

        assertEquals(2, manifest.version)
        assertEquals(2, manifest.totalDocuments)
        assertEquals(2, manifest.documents.size)

        val firstDoc = manifest.documents[0]
        assertEquals("DZIKIR_TQN.md", firstDoc.fileName)
        assertEquals("abc123hash", firstDoc.sha256)
        assertEquals(18240L, firstDoc.size)
        assertTrue(firstDoc.url.contains("DZIKIR_TQN.md"))
        assertEquals("https://iqbalwork.github.io/Robithoh-Docs/documents/DZIKIR_TQN.md", firstDoc.pagesUrl)
    }

    @Test
    fun testMarkdownDocumentRepositoryLookupAndCache() = runTest {
        val repository = MarkdownDocumentRepository(database = null)

        val doc = repository.getDocumentById("dzikir_tqn")
        assertNotNull(doc)
        assertEquals("DZIKIR_TQN.md", doc.fileName)

        val parsed = repository.loadDocumentContent(doc)
        assertNotNull(parsed)
        assertTrue(parsed.rawContent.isNotEmpty())

        val cached = repository.getCachedDocument("dzikir_tqn")
        assertNotNull(cached)
        assertEquals(parsed.rawContent, cached.rawContent)

        // Test invalidation
        repository.invalidateCache("DZIKIR_TQN.md")
        val invalidated = repository.getCachedDocument("dzikir_tqn")
        assertEquals(null, invalidated)
    }

    @Test
    fun testWiridKemalaikatanParsingAndCounts() = runTest {
        val repository = MarkdownDocumentRepository(database = null)
        val doc = repository.getDocumentById("wirid_kemalaikatan")
        assertNotNull(doc)

        val sampleContent = """
            # Wirid Kemalaikatan

            Maklumat Guru Agung Hadrotus Syeikh ABAH AOS Ra Qs ~ Senin, 5 Juni 2023

            ---

            ## Hari Lahir Ahad (Setiap Sabtu Malam)

            حَيٌّ قَيُّومٌ

            شَمْسَيَايِيْل

            HAYYUN QOYYUUM (222x)

            Malaikat SYAMSAYAA YAYIL ... Al-Fatihah

            Catatan: Dibaca setiap Sabtu malam bagi yang lahir pada hari Ahad.

            ---

            ## Hari Lahir Senin (Setiap Minggu Malam)

            رَحْمٰنُ الرَّحِيمُ

            كَرْمَيَايِيْل

            ROHMAANUR ROHIIM (333x)

            Malaikat KARMAYAA YAYIL ... Al-Fatihah

            Catatan: Dibaca setiap Minggu malam bagi yang lahir pada hari Senin.

            ---

            ## Hari Lahir Selasa (Setiap Senin Malam)

            مَلِكٌ قُدُّوسٌ

            رُوْحَيَايِيْل

            MALIKUN QUDDUUSUN (444x)

            Malaikat RUUHAYAA YAYIL ... Al-Fatihah

            Catatan: Dibaca setiap Senin malam bagi yang lahir pada hari Selasa.

            ---

            ## Hari Lahir Rabu (Setiap Selasa Malam)

            كَبِيرٌ مُتَعَالٌ

            رُوْقَيَايِيْل

            KABIIRUN MUTA'AL (555x)

            Malaikat RUUQOYAA YAYIL ... Al-Fatihah

            Catatan: Dibaca setiap Selasa malam bagi yang lahir pada hari Rabu.

            ---

            ## Hari Lahir Kamis (Setiap Rabu Malam)

            فَتَّاحٌ رَزَّاقٌ

            مِيْكَيَايِيْل

            PATTAAHUN ROZZAKUN (666x)

            Malaikat MIIKAYAA YAYIL ... Al-Fatihah

            Catatan: Dibaca setiap Rabu malam bagi yang lahir pada hari Kamis.

            ---

            ## Hari Lahir Jum'at (Setiap Kamis Malam)

            شَدِيدٌ ذُو قُوَّةٍ

            جَبْرَيَايِيْل

            SYADIIDUN DZUUQUWWATIN (777x)

            Malaikat JABROYAA YAYIL ... Al-Fatihah

            Catatan: Dibaca setiap Kamis malam bagi yang lahir pada hari Jum'at.

            ---

            ## Hari Lahir Sabtu (Setiap Jum'at Malam)

            قَوِيٌّ قَدِيرٌ

            لَطْفَيَايِيْل

            QOWIYYUN QODIIRUN (888x)

            Malaikat LATHOFA YAA YAYIL ... Al-Fatihah

            Catatan: Dibaca setiap Jum'at malam bagi yang lahir pada hari Sabtu.
        """.trimIndent()

        val parsed = repository.loadDocumentContent(doc)
        assertNotNull(parsed)

        val versesToTest = if (parsed.verses.any { it.title.contains("Hari Lahir", ignoreCase = true) }) {
            parsed.verses
        } else {
            repository.parseMarkdownToVerses(sampleContent)
        }

        val dayVerses = versesToTest.filter { verse ->
            verse.title.contains("Hari Lahir", ignoreCase = true)
        }
        assertEquals(7, dayVerses.size)

        val expectedCounts = listOf(222, 333, 444, 555, 666, 777, 888)
        val expectedTitles = listOf("Ahad", "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu")

        for (i in 0 until 7) {
            val verse = dayVerses[i]
            assertTrue(verse.title.contains(expectedTitles[i]), "Verse $i title should contain ${expectedTitles[i]} but was ${verse.title}")
            assertEquals(expectedCounts[i], verse.repeatCount, "Verse $i repeatCount should be ${expectedCounts[i]}")
            assertTrue(verse.arabic.isNotEmpty(), "Verse $i arabic should not be empty")
            assertTrue(verse.latin.isNotEmpty(), "Verse $i latin should not be empty")
            assertTrue(verse.latin.contains(expectedCounts[i].toString()), "Verse $i latin should contain count")
        }
    }
}
