package com.iqbalwork.robithoh.feature.quran.data

import com.iqbalwork.robithoh.core.network.createKtorHttpClient
import com.iqbalwork.robithoh.feature.quran.model.ChapterAudio
import com.iqbalwork.robithoh.feature.quran.model.QariList
import com.iqbalwork.robithoh.feature.quran.model.QariOption
import com.iqbalwork.robithoh.feature.quran.model.VerseTiming
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
private data class QdcAudioFilesResponse(
    @SerialName("audio_files")
    val audioFiles: List<QdcAudioFileItem> = emptyList()
)

@Serializable
private data class QdcAudioFileItem(
    val id: Int? = null,
    @SerialName("chapter_id")
    val chapterId: Int,
    @SerialName("audio_url")
    val audioUrl: String,
    val duration: Long = 0L,
    @SerialName("verse_timings")
    val verseTimings: List<VerseTiming> = emptyList()
)

interface QuranAudioRepository {
    val selectedQari: StateFlow<QariOption>
    fun setSelectedQari(qari: QariOption)
    suspend fun getChapterAudio(qari: QariOption, chapterNumber: Int): Result<ChapterAudio>
    fun getAyahAudioUrl(qari: QariOption, surahNumber: Int, ayahNumber: Int): String
    fun getCachedChapterAudio(qariId: Int, chapterNumber: Int): ChapterAudio?
}

class QuranAudioRepositoryImpl(
    private val httpClient: HttpClient = createKtorHttpClient()
) : QuranAudioRepository {

    private val _selectedQari = MutableStateFlow(QariList.default)
    override val selectedQari: StateFlow<QariOption> = _selectedQari.asStateFlow()

    private val chapterCache = mutableMapOf<Pair<Int, Int>, ChapterAudio>()

    override fun setSelectedQari(qari: QariOption) {
        _selectedQari.value = qari
    }

    override fun getCachedChapterAudio(qariId: Int, chapterNumber: Int): ChapterAudio? {
        return chapterCache[Pair(qariId, chapterNumber)]
    }

    override suspend fun getChapterAudio(qari: QariOption, chapterNumber: Int): Result<ChapterAudio> {
        val cacheKey = Pair(qari.id, chapterNumber)
        chapterCache[cacheKey]?.let { return Result.success(it) }

        return try {
            val response: QdcAudioFilesResponse = httpClient.get(
                "https://api.qurancdn.com/api/qdc/audio/reciters/${qari.id}/audio_files?chapter=$chapterNumber&segments=true"
            ).body()

            val item = response.audioFiles.firstOrNull()
            if (item != null && item.audioUrl.isNotBlank()) {
                val chapterAudio = ChapterAudio(
                    chapterId = item.chapterId,
                    audioUrl = item.audioUrl,
                    durationMs = item.duration,
                    verseTimings = item.verseTimings
                )
                chapterCache[cacheKey] = chapterAudio
                Result.success(chapterAudio)
            } else {
                // Fallback direct URL if response item missing
                val fallbackAudio = buildFallbackChapterAudio(qari, chapterNumber)
                chapterCache[cacheKey] = fallbackAudio
                Result.success(fallbackAudio)
            }
        } catch (e: Exception) {
            // Fallback direct URL on network timeout or parsing issues
            val fallbackAudio = buildFallbackChapterAudio(qari, chapterNumber)
            chapterCache[cacheKey] = fallbackAudio
            Result.success(fallbackAudio)
        }
    }

    private fun buildFallbackChapterAudio(qari: QariOption, chapterNumber: Int): ChapterAudio {
        return ChapterAudio(
            chapterId = chapterNumber,
            audioUrl = "https://download.quranicaudio.com/qdc/${qari.slug}/murattal/$chapterNumber.mp3",
            durationMs = 0L,
            verseTimings = emptyList()
        )
    }

    override fun getAyahAudioUrl(qari: QariOption, surahNumber: Int, ayahNumber: Int): String {
        val surahPadded = surahNumber.toString().padStart(3, '0')
        val ayahPadded = ayahNumber.toString().padStart(3, '0')
        return "https://verses.quran.com/${qari.subfolder}/mp3/$surahPadded$ayahPadded.mp3"
    }
}
