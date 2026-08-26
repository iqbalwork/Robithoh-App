package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.feature.quran.data.QuranData
import com.iqbalwork.robithoh.feature.quran.data.QuranRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class QuranViewModel(
    private val repository: QuranRepository,
    private val audioPlayer: KmpAudioPlayer? = null
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

        player.playbackState
            .onEach { state -> updateState { copy(audioPlaybackState = state) } }
            .launchIn(viewModelScope)

        player.currentPositionMs
            .onEach { pos -> updateState { copy(audioPositionMs = pos) } }
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
                viewModelScope.launch {
                    repository.saveLastRead(intent.surahNumber, intent.ayahNumber, intent.surahName)
                    sendEffect(QuranUiEffect.ShowToast("Disimpan ke Terakhir Dibaca: ${intent.surahName} ayat ${intent.ayahNumber}"))
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
            is QuranUiIntent.PlayAudio -> {
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
}
