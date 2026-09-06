package com.iqbalwork.robithoh.feature.reader.data.sync

import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import robithohapp.shared.generated.resources.Res

sealed interface DocumentSyncState {
    data object Idle : DocumentSyncState
    data object Syncing : DocumentSyncState
    data class Success(val updatedCount: Int) : DocumentSyncState
    data class Error(val message: String) : DocumentSyncState
}

class DocumentSyncManager(
    private val httpClient: HttpClient,
    private val database: RobithohDatabase,
    private val repository: MarkdownDocumentRepository,
    private val json: Json = Json { ignoreUnknownKeys = true; isLenient = true }
) {
    private val _syncState = MutableStateFlow<DocumentSyncState>(DocumentSyncState.Idle)
    val syncState: StateFlow<DocumentSyncState> = _syncState.asStateFlow()

    companion object {
        const val PRIMARY_MANIFEST_URL = "https://cdn.jsdelivr.net/gh/iqbalwork/Robithoh-Docs@main/manifest.json"
        const val FALLBACK_MANIFEST_URL = "https://raw.githubusercontent.com/iqbalwork/Robithoh-Docs/main/manifest.json"
        const val PAGES_MANIFEST_URL = "https://iqbalwork.github.io/Robithoh-Docs/manifest.json"
    }

    @OptIn(ExperimentalResourceApi::class, ExperimentalTime::class)
    suspend fun syncDocuments(force: Boolean = false): Result<Int> = withContext(Dispatchers.Default) {
        if (_syncState.value is DocumentSyncState.Syncing) {
            return@withContext Result.success(0)
        }
        _syncState.value = DocumentSyncState.Syncing

        try {
            // 1. Pastikan baseline hash tersimpan di database jika database masih kosong
            seedBaselineIfEmpty()

            // 2. Ambil manifest terbaru dari cloud
            val remoteManifest = fetchRemoteManifest()
                ?: throw IllegalStateException("Gagal memuat manifest dokumen dari cloud.")

            // 3. Ambil hash dokumen yang saat ini tersimpan di basis data lokal
            val cachedHashes = database.robithohDatabaseQueries
                .getAllCachedDocumentHashes()
                .executeAsList()
                .associate { it.file_name to it.sha256 }

            var updatedCount = 0
            val now = Clock.System.now().toEpochMilliseconds()

            for (remoteDoc in remoteManifest.documents) {
                val currentHash = cachedHashes[remoteDoc.fileName]
                val needsUpdate = force || currentHash == null || currentHash != remoteDoc.sha256

                if (needsUpdate) {
                    val content = downloadDocumentContent(remoteDoc)
                    if (content != null) {
                        val docId = repository.allDocuments
                            .firstOrNull { it.fileName.equals(remoteDoc.fileName, ignoreCase = true) }
                            ?.id ?: remoteDoc.fileName.removeSuffix(".md").lowercase()

                        database.robithohDatabaseQueries.insertOrUpdateCachedDocument(
                            id = docId,
                            file_name = remoteDoc.fileName,
                            sha256 = remoteDoc.sha256,
                            content = content,
                            updated_at = now
                        )
                        repository.invalidateCache(remoteDoc.fileName)
                        updatedCount++
                    }
                }
            }

            _syncState.value = DocumentSyncState.Success(updatedCount)
            Result.success(updatedCount)
        } catch (e: Exception) {
            _syncState.value = DocumentSyncState.Error(e.message ?: "Unknown sync error")
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalResourceApi::class, ExperimentalTime::class)
    private suspend fun seedBaselineIfEmpty() {
        val count = database.robithohDatabaseQueries.getAllCachedDocumentHashes().executeAsList().size
        if (count > 0) return

        try {
            val baselineBytes = Res.readBytes("files/manifest.json")
            val baselineManifest = json.decodeFromString<DocumentManifest>(baselineBytes.decodeToString())
            val now = Clock.System.now().toEpochMilliseconds()

            database.transaction {
                for (doc in baselineManifest.documents) {
                    val docId = repository.allDocuments
                        .firstOrNull { it.fileName.equals(doc.fileName, ignoreCase = true) }
                        ?.id ?: doc.fileName.removeSuffix(".md").lowercase()

                    database.robithohDatabaseQueries.insertOrUpdateCachedDocument(
                        id = docId,
                        file_name = doc.fileName,
                        sha256 = doc.sha256,
                        content = "", // Kosong menandakan dokumen memakai naskah bawaan APK (bundled)
                        updated_at = now
                    )
                }
            }
        } catch (_: Exception) {
            // Jika manifest baseline gagal dibaca, sync akan mengunduh sesuai kebutuhan
        }
    }

    private suspend fun fetchRemoteManifest(): DocumentManifest? {
        val urls = listOf(PRIMARY_MANIFEST_URL, FALLBACK_MANIFEST_URL, PAGES_MANIFEST_URL)
        for (url in urls) {
            try {
                val responseText = httpClient.get(url).bodyAsText()
                if (responseText.isNotBlank()) {
                    return json.decodeFromString<DocumentManifest>(responseText)
                }
            } catch (_: Exception) {
                // Coba URL cadangan
            }
        }
        return null
    }

    private suspend fun downloadDocumentContent(doc: ManifestDocumentItem): String? {
        val urls = listOfNotNull(doc.url, doc.rawUrl, doc.pagesUrl)
        for (url in urls) {
            if (url.isBlank()) continue
            try {
                val text = httpClient.get(url).bodyAsText()
                if (text.isNotBlank()) {
                    return text
                }
            } catch (_: Exception) {
                // Coba URL cadangan
            }
        }
        return null
    }
}
