package com.iqbalwork.robithoh.feature.quran.data

import androidx.compose.ui.graphics.ImageBitmap
import com.iqbalwork.robithoh.core.network.createKtorHttpClient
import com.iqbalwork.robithoh.feature.quran.model.ConsolidatedPageMappings
import com.iqbalwork.robithoh.feature.quran.model.QuranPageMapping
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.decodeToImageBitmap
import robithohapp.shared.generated.resources.Res

sealed interface MushafDownloadState {
    data object Idle : MushafDownloadState
    data class Downloading(
        val downloadedPages: Int,
        val totalPages: Int = QuranPageLookup.TOTAL_PAGES,
        val progress: Float,
        val currentFileName: String
    ) : MushafDownloadState
    data class Completed(val totalPages: Int = QuranPageLookup.TOTAL_PAGES) : MushafDownloadState
    data class Error(val errorMessage: String) : MushafDownloadState
}

class QuranPageManager(
    private val cacheManager: QuranPageCacheManager = createQuranPageCacheManager(),
    private val httpClient: HttpClient = createKtorHttpClient(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val mappingMutex = Mutex()
    private var consolidatedMappings: ConsolidatedPageMappings? = null
    private val mappingCache = mutableMapOf<Int, QuranPageMapping>()

    private val memoryImageMutex = Mutex()
    // Keep max 5 decoded pages in RAM to preserve mobile heap
    private val memoryImageCache = mutableMapOf<Int, ImageBitmap>()
    private val memoryImageOrder = mutableListOf<Int>()

    private val _downloadState = MutableStateFlow<MushafDownloadState>(MushafDownloadState.Idle)
    val downloadState: StateFlow<MushafDownloadState> = _downloadState.asStateFlow()
    private var downloadJob: Job? = null

    companion object {
        const val PRIMARY_PAGE_URL = "https://cdn.jsdelivr.net/gh/iqbalwork/Robithoh-Docs@e2a806a/quran-page"
        const val FALLBACK_PAGE_URL = "https://raw.githubusercontent.com/iqbalwork/Robithoh-Docs/e2a806a/quran-page"
    }

    private fun putMemoryImage(pageNumber: Int, bmp: ImageBitmap) {
        memoryImageCache[pageNumber] = bmp
        memoryImageOrder.remove(pageNumber)
        memoryImageOrder.add(pageNumber)
        if (memoryImageOrder.size > 5) {
            val oldest = memoryImageOrder.removeAt(0)
            memoryImageCache.remove(oldest)
        }
    }

    private suspend fun ensureConsolidatedLoaded() {
        if (consolidatedMappings != null) return
        mappingMutex.withLock {
            if (consolidatedMappings != null) return@withLock
            try {
                val bytes = Res.readBytes("files/quran_page_mappings.json")
                val text = bytes.decodeToString()
                consolidatedMappings = json.decodeFromString<ConsolidatedPageMappings>(text)
            } catch (e: Exception) {
                // If resource read fails, keep null
            }
        }
    }

    suspend fun getPageMapping(pageNumber: Int): QuranPageMapping? = withContext(dispatcher) {
        val cached = mappingCache[pageNumber]
        if (cached != null) return@withContext cached

        ensureConsolidatedLoaded()
        val mapped = consolidatedMappings?.pages?.get(pageNumber.toString())
        if (mapped != null) {
            val mapping = QuranPageMapping(
                viewportWidth = consolidatedMappings?.viewportWidth ?: 1080,
                viewportHeight = consolidatedMappings?.viewportHeight ?: 1745,
                data = mapped
            )
            mappingCache[pageNumber] = mapping
            return@withContext mapping
        }
        null
    }

    suspend fun loadPageImage(pageNumber: Int): ImageBitmap? = withContext(dispatcher) {
        memoryImageMutex.withLock {
            memoryImageCache[pageNumber]?.let { return@withContext it }
        }

        // 1. Try local disk cache
        val diskBytes = cacheManager.getPageBytes(pageNumber)
        if (diskBytes != null && diskBytes.isNotEmpty()) {
            try {
                val bmp = diskBytes.decodeToImageBitmap()
                memoryImageMutex.withLock {
                    putMemoryImage(pageNumber, bmp)
                }
                return@withContext bmp
            } catch (_: Exception) {}
        }

        // 2. Fetch from remote CDN
        val pageFormatted = pageNumber.toString().padStart(3, '0')
        val bytes = fetchRemotePageBytes(pageFormatted) ?: return@withContext null
        cacheManager.savePageBytes(pageNumber, bytes)
        try {
            val bmp = bytes.decodeToImageBitmap()
            memoryImageMutex.withLock {
                putMemoryImage(pageNumber, bmp)
            }
            bmp
        } catch (_: Exception) {
            null
        }
    }

    fun prefetchPages(currentPage: Int) {
        scope.launch {
            val prev = currentPage - 1
            val next = currentPage + 1
            if (prev >= 1) {
                launch { loadPageImage(prev) }
                launch { getPageMapping(prev) }
            }
            if (next <= QuranPageLookup.TOTAL_PAGES) {
                launch { loadPageImage(next) }
                launch { getPageMapping(next) }
            }
        }
    }

    private suspend fun fetchRemotePageBytes(pageFormatted: String): ByteArray? {
        val fileName = "$pageFormatted.png"
        val urls = listOf(
            "$PRIMARY_PAGE_URL/$fileName",
            "$FALLBACK_PAGE_URL/$fileName"
        )
        for (url in urls) {
            try {
                val response: ByteArray = httpClient.get(url).body()
                if (response.isNotEmpty()) {
                    return response
                }
            } catch (_: Exception) {
                // Try fallback
            }
        }
        return null
    }

    fun isAllDownloaded(): Boolean {
        return cacheManager.getDownloadedPageCount() >= QuranPageLookup.TOTAL_PAGES
    }

    fun getDownloadedPageCount(): Int {
        return cacheManager.getDownloadedPageCount()
    }

    fun startFullDownload() {
        if (downloadJob?.isActive == true) return

        downloadJob = scope.launch {
            val total = QuranPageLookup.TOTAL_PAGES
            var downloaded = cacheManager.getDownloadedPageCount()

            _downloadState.value = MushafDownloadState.Downloading(
                downloadedPages = downloaded,
                totalPages = total,
                progress = downloaded.toFloat() / total,
                currentFileName = ""
            )

            for (p in 1..total) {
                if (cacheManager.isPageDownloaded(p)) {
                    continue
                }

                val pageFormatted = p.toString().padStart(3, '0')
                _downloadState.value = MushafDownloadState.Downloading(
                    downloadedPages = downloaded,
                    totalPages = total,
                    progress = downloaded.toFloat() / total,
                    currentFileName = "Halaman $p ($pageFormatted.png)"
                )

                val bytes = fetchRemotePageBytes(pageFormatted)
                if (bytes != null && bytes.isNotEmpty()) {
                    cacheManager.savePageBytes(p, bytes)
                    downloaded++
                    _downloadState.value = MushafDownloadState.Downloading(
                        downloadedPages = downloaded,
                        totalPages = total,
                        progress = downloaded.toFloat() / total,
                        currentFileName = "Halaman $p selesai"
                    )
                } else {
                    _downloadState.value = MushafDownloadState.Error("Gagal mengunduh halaman $p.")
                    return@launch
                }
            }

            _downloadState.value = MushafDownloadState.Completed(total)
        }
    }

    fun cancelDownload() {
        downloadJob?.cancel()
        downloadJob = null
        _downloadState.value = MushafDownloadState.Idle
    }

    fun resetDownloadState() {
        _downloadState.value = MushafDownloadState.Idle
    }
}
