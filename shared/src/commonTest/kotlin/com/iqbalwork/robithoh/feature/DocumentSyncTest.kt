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
}
