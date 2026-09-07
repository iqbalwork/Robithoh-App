package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.robithoh.core.analytics.AnalyticsEvents
import com.iqbalwork.robithoh.core.analytics.AnalyticsParams
import com.iqbalwork.robithoh.core.analytics.AnalyticsTracker
import com.iqbalwork.robithoh.core.analytics.getAnalyticsTracker
import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.feature.quran.data.QuranAudioRepository
import com.iqbalwork.robithoh.feature.quran.data.QuranAudioRepositoryImpl
import com.iqbalwork.robithoh.feature.quran.data.QuranData
import com.iqbalwork.robithoh.feature.quran.data.QuranRepository
import com.iqbalwork.robithoh.feature.quran.model.ChapterAudio
import com.iqbalwork.robithoh.feature.quran.model.QariOption
import com.iqbalwork.robithoh.feature.quran.model.QuranBookmark
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class QuranViewModel(
    private val repository: QuranRepository,
    private val audioPlayer: KmpAudioPlayer? = null,
    private val audioRepository: QuranAudioRepository = QuranAudioRepositoryImpl(),
    private val analyticsTracker: AnalyticsTracker = getAnalyticsTracker()
) : MviViewModel<QuranUiState, QuranUiIntent, QuranUiEffect>(QuranUiState(isLoading = true)) {

    init {
        loadInitialData()
        observeAudio()
    }

    private fun loadInitialData() {
        val shalawat = repository.getShalawatList()
        val ziarah = repository.getZiarahSections()
        val initialSurahs = QuranData.surahs
        val firstSurah = initialSurahs.firstOrNull()

        updateState {
            copy(
                surahs = initialSurahs,
                currentSurah = firstSurah,
                currentAyahs = QuranData.getAyahsForSurah(firstSurah?.number ?: 1),
                shalawatList = shalawat,
                ziarahSections = ziarah,
                isLoading = false
            )
        }

        repository.getAllSurahs()
            .onEach { surahs ->
                if (surahs.isNotEmpty() && currentState.searchQuery.isBlank()) {
                    updateState {
                        copy(
                            surahs = surahs,
                            isLoading = false
                        )
                    }
                }
            }
            .catch { err ->
                updateState { copy(isLoading = false, errorMessage = err.message) }
            }
            .launchIn(viewModelScope)

        repository.getLastReadBookmark()
            .onEach { bookmark ->
                updateState { copy(lastReadBookmark = bookmark) }
            }
            .launchIn(viewModelScope)

        repository.getAllBookmarks()
            .onEach { bookmarks ->
                updateState { copy(bookmarks = bookmarks) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeAudio() {
        val player = audioPlayer ?: return

        player.currentTrack
            .onEach { track -> updateState { copy(activeAudioTrack = track) } }
            .launchIn(viewModelScope)

        var previousPlaybackState = AudioPlaybackState.IDLE
        player.playbackState
            .onEach { state ->
                val prev = previousPlaybackState
                previousPlaybackState = state
                updateState {
                    copy(
                        audioPlaybackState = state,
                        isAudioLoading = state == AudioPlaybackState.BUFFERING,
                        activeAyahNumber = if (state == AudioPlaybackState.IDLE && player.currentTrack.value == null) null else activeAyahNumber
                    )
                }
                if (state == AudioPlaybackState.COMPLETED && prev != AudioPlaybackState.COMPLETED) {
                    handleTrackCompleted()
                }
            }
            .launchIn(viewModelScope)

        player.currentPositionMs
            .onEach { pos ->
                val track = player.currentTrack.value ?: currentState.activeAudioTrack
                val isCurrentSurahPlaying = track?.id?.startsWith("surah_") == true
                val activeAyah = if (isCurrentSurahPlaying) {
                    currentState.activeChapterAudio?.verseTimings?.find {
                        pos in it.timestampFromMs..it.timestampToMs
                    }?.ayahNumber
                } else if (track?.id?.startsWith("ayah_") == true) {
                    val parts = track.id.split("_")
                    parts.getOrNull(2)?.toIntOrNull()
                } else null

                updateState {
                    copy(
                        audioPositionMs = pos,
                        activeAyahNumber = activeAyah ?: this.activeAyahNumber
                    )
                }
            }
            .launchIn(viewModelScope)

        player.durationMs
            .onEach { dur -> updateState { copy(audioDurationMs = dur) } }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: QuranUiIntent) {
        when (intent) {
            is QuranUiIntent.SelectTab -> {
                updateState { copy(selectedTab = intent.tab) }
            }
            is QuranUiIntent.SearchSurahs -> {
                val trimmed = intent.query.trim()
                val filtered = if (trimmed.isEmpty()) {
                    QuranData.surahs
                } else {
                    QuranData.surahs.filter {
                        it.nameLatin.contains(trimmed, ignoreCase = true) ||
                        it.indonesianMeaning.contains(trimmed, ignoreCase = true) ||
                        it.nameArabic.contains(trimmed) ||
                        it.number.toString() == trimmed
                    }
                }
                updateState {
                    copy(
                        searchQuery = intent.query,
                        surahs = filtered
                    )
                }
            }
            is QuranUiIntent.SelectSurah -> {
                val surah = QuranData.surahs.find { it.number == intent.surahNumber }
                val ayahs = QuranData.getAyahsForSurah(intent.surahNumber)
                analyticsTracker.logEvent(
                    AnalyticsEvents.QURAN_SURAH_OPENED,
                    mapOf(
                        AnalyticsParams.SURAH_NUMBER to intent.surahNumber,
                        AnalyticsParams.SURAH_NAME to (surah?.nameLatin ?: "Surah ${intent.surahNumber}")
                    )
                )
                updateState {
                    copy(
                        currentSurah = surah ?: currentSurah,
                        currentAyahs = ayahs
                    )
                }
                viewModelScope.launch {
                    sendEffect(QuranUiEffect.NavigateToSurah(intent.surahNumber))
                }
            }
            is QuranUiIntent.SaveBookmark -> {
                analyticsTracker.logEvent(
                    AnalyticsEvents.QURAN_BOOKMARK_SAVED,
                    mapOf(
                        AnalyticsParams.SURAH_NUMBER to intent.surahNumber,
                        AnalyticsParams.SURAH_NAME to intent.surahName,
                        AnalyticsParams.AYAH_NUMBER to intent.ayahNumber
                    )
                )
                updateState {
                    copy(
                        lastReadBookmark = QuranBookmark(
                            id = lastReadBookmark?.id ?: 1L,
                            surahNumber = intent.surahNumber,
                            ayahNumber = intent.ayahNumber,
                            surahName = intent.surahName,
                            timestamp = 0L
                        )
                    )
                }
                viewModelScope.launch {
                    kotlinx.coroutines.withContext(NonCancellable) {
                        repository.saveLastRead(intent.surahNumber, intent.ayahNumber, intent.surahName)
                    }
                    if (intent.showToast) {
                        sendEffect(QuranUiEffect.ShowToast("Disimpan ke Terakhir Dibaca: ${intent.surahName} ayat ${intent.ayahNumber}"))
                    }
                }
            }
            is QuranUiIntent.UpdateFontScale -> {
                val clamped = intent.scale.coerceIn(0.75f, 2.0f)
                updateState { copy(fontScale = clamped) }
            }
            is QuranUiIntent.ToggleTajwidColors -> {
                updateState {
                    copy(isTajwidColorEnabled = intent.enabled ?: !isTajwidColorEnabled)
                }
            }
            is QuranUiIntent.SelectQari -> {
                audioRepository.setSelectedQari(intent.qari)
                updateState { copy(selectedQari = intent.qari) }
                val currentTrack = currentState.activeAudioTrack
                if (currentTrack != null && currentTrack.id.startsWith("ayah_")) {
                    val parts = currentTrack.id.split("_")
                    val surahNumber = parts.getOrNull(1)?.toIntOrNull()
                    val ayahNumber = parts.getOrNull(2)?.toIntOrNull()
                    if (surahNumber != null && ayahNumber != null) {
                        onIntent(QuranUiIntent.PlayAyahAudio(surahNumber, ayahNumber))
                    }
                } else if (currentTrack != null && currentTrack.id.startsWith("surah_")) {
                    val surahNumber = currentTrack.id.removePrefix("surah_").toIntOrNull()
                    if (surahNumber != null) {
                        onIntent(QuranUiIntent.PlayAyahAudio(surahNumber, currentState.activeAyahNumber ?: 1))
                    }
                }
            }
            is QuranUiIntent.SetQariPickerVisible -> {
                updateState { copy(isQariPickerVisible = intent.visible) }
            }
            is QuranUiIntent.ToggleAudioRepeatMode -> {
                val nextMode = when (currentState.audioRepeatMode) {
                    QuranAudioRepeatMode.REPEAT_SURAH -> QuranAudioRepeatMode.REPEAT_AYAH
                    QuranAudioRepeatMode.REPEAT_AYAH -> QuranAudioRepeatMode.OFF
                    QuranAudioRepeatMode.OFF -> QuranAudioRepeatMode.REPEAT_SURAH
                }
                updateState { copy(audioRepeatMode = nextMode) }
            }
            is QuranUiIntent.SetAudioRepeatMode -> {
                updateState { copy(audioRepeatMode = intent.mode) }
            }
            is QuranUiIntent.PlaySurahAudio -> {
                // Play verse-by-verse starting from startAyahNumber instead of downloading entire surah
                onIntent(QuranUiIntent.PlayAyahAudio(intent.surahNumber, intent.startAyahNumber))
            }
            is QuranUiIntent.PlayAyahAudio -> {
                val surah = currentState.surahs.find { it.number == intent.surahNumber }
                    ?: QuranData.surahs.find { it.number == intent.surahNumber }
                val surahName = surah?.nameLatin ?: "Surah ${intent.surahNumber}"
                val ayahs = if (currentState.currentSurah?.number != intent.surahNumber) {
                    QuranData.getAyahsForSurah(intent.surahNumber)
                } else {
                    currentState.currentAyahs
                }
                val qari = currentState.selectedQari

                val ayahUrl = audioRepository.getAyahAudioUrl(qari, intent.surahNumber, intent.ayahNumber)
                val track = AudioTrack(
                    id = "ayah_${intent.surahNumber}_${intent.ayahNumber}",
                    title = "Surah $surahName Ayat ${intent.ayahNumber}",
                    subtitle = "Lantunan ${qari.name}",
                    urlOrPath = ayahUrl
                )
                updateState {
                    copy(
                        currentSurah = if (currentSurah?.number != intent.surahNumber) surah ?: currentSurah else currentSurah,
                        currentAyahs = if (currentSurah?.number != intent.surahNumber) ayahs else currentAyahs,
                        isAudioLoading = true,
                        activeAyahNumber = intent.ayahNumber,
                        activeAudioTrack = track
                    )
                }
                onIntent(QuranUiIntent.PlayAudio(track))
            }
            is QuranUiIntent.SeekToAyah -> {
                val activeAudio = currentState.activeChapterAudio
                if (activeAudio != null) {
                    val timing = activeAudio.verseTimings.find { it.ayahNumber == intent.ayahNumber }
                    if (timing != null) {
                        audioPlayer?.seekTo(timing.timestampFromMs)
                    }
                }
            }
            is QuranUiIntent.PlayAudio -> {
                updateState { copy(activeAudioTrack = intent.track) }
                analyticsTracker.logEvent(
                    AnalyticsEvents.AUDIO_PLAYBACK_STARTED,
                    mapOf(
                        AnalyticsParams.AUDIO_ID to intent.track.id,
                        AnalyticsParams.AUDIO_TITLE to intent.track.title
                    )
                )
                audioPlayer?.play(intent.track)
            }
            is QuranUiIntent.TogglePlayPauseAudio -> {
                val player = audioPlayer ?: return
                if (currentState.audioPlaybackState == AudioPlaybackState.PLAYING) {
                    player.pause()
                } else {
                    player.resume()
                }
            }
            is QuranUiIntent.StopAudio -> {
                audioPlayer?.stop()
            }
            is QuranUiIntent.RefreshData -> {
                loadInitialData()
            }
        }
    }

    private fun handleTrackCompleted() {
        val track = audioPlayer?.currentTrack?.value ?: currentState.activeAudioTrack ?: return
        if (track.id.startsWith("ayah_")) {
            val parts = track.id.split("_")
            val surah = parts.getOrNull(1)?.toIntOrNull() ?: return
            val ayah = parts.getOrNull(2)?.toIntOrNull() ?: return

            when (currentState.audioRepeatMode) {
                QuranAudioRepeatMode.REPEAT_AYAH -> {
                    onIntent(QuranUiIntent.PlayAyahAudio(surah, ayah))
                }
                QuranAudioRepeatMode.REPEAT_SURAH -> {
                    val surahMeta = QuranData.surahs.find { it.number == surah }
                    val totalAyahs = surahMeta?.numberOfAyahs ?: 0
                    if (ayah < totalAyahs) {
                        onIntent(QuranUiIntent.PlayAyahAudio(surah, ayah + 1))
                    } else if (surah < 114) {
                        onIntent(QuranUiIntent.PlayAyahAudio(surah + 1, 1))
                    } else {
                        updateState { copy(activeAyahNumber = null) }
                    }
                }
                QuranAudioRepeatMode.OFF -> {
                    updateState { copy(activeAyahNumber = null) }
                }
            }
        }
    }
}
