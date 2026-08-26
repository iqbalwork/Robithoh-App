package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.feature.manaqib.data.ManaqibDataSeeder
import com.iqbalwork.robithoh.feature.manaqib.data.ManaqibRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ManaqibViewModel(
    private val repository: ManaqibRepository
) : MviViewModel<ManaqibUiState, ManaqibUiIntent, ManaqibUiEffect>(ManaqibUiState(isLoading = true)) {

    private val allSeedChapters = ManaqibDataSeeder.getManqobahChapters()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val tanbih = repository.getTanbih()
        val silsilah = repository.getSilsilahNodes()
        val mc = repository.getMcProgramList()
        val khotaman = repository.getKhotamanSteps()
        val doa = repository.getDoaList()

        updateState {
            copy(
                chapters = allSeedChapters,
                currentChapter = allSeedChapters.firstOrNull { it.chapterNumber == 1 } ?: allSeedChapters.firstOrNull(),
                tanbih = tanbih,
                silsilahList = silsilah,
                mcPrograms = mc,
                khotamanSteps = khotaman,
                doaList = doa,
                selectedDoa = doa.firstOrNull(),
                isLoading = false
            )
        }

        viewModelScope.launch {
            repository.seedDatabase()
        }

        repository.getAllChapters()
            .onEach { chapters ->
                if (chapters.isNotEmpty() && currentState.chapterSearchQuery.isBlank()) {
                    updateState {
                        copy(
                            chapters = chapters,
                            isLoading = false
                        )
                    }
                }
            }
            .catch { err ->
                updateState { copy(isLoading = false, errorMessage = err.message) }
            }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: ManaqibUiIntent) {
        when (intent) {
            is ManaqibUiIntent.SelectTab -> {
                updateState { copy(selectedTab = intent.tab) }
            }
            is ManaqibUiIntent.SelectLanguage -> {
                updateState { copy(selectedLanguage = intent.language) }
            }
            is ManaqibUiIntent.SearchChapters -> {
                updateState { copy(chapterSearchQuery = intent.query) }
                repository.searchChapters(intent.query)
                    .onEach { results ->
                        updateState { copy(chapters = results) }
                    }
                    .launchIn(viewModelScope)
            }
            is ManaqibUiIntent.SelectChapter -> {
                val target = intent.chapterNumber
                val chapter = allSeedChapters.find { it.chapterNumber == target }
                    ?: currentState.chapters.find { it.chapterNumber == target }
                if (chapter != null) {
                    updateState { copy(currentChapter = chapter) }
                }
                sendEffect(ManaqibUiEffect.NavigateToChapter(target))
            }
            is ManaqibUiIntent.SearchSilsilah -> {
                updateState {
                    copy(
                        silsilahSearchQuery = intent.query,
                        silsilahList = repository.searchSilsilah(intent.query)
                    )
                }
            }
            is ManaqibUiIntent.SelectDoa -> {
                val doa = repository.getDoaById(intent.doaId)
                updateState { copy(selectedDoa = doa) }
            }
            is ManaqibUiIntent.TogglePresentationMode -> {
                updateState {
                    copy(isPresentationMode = intent.enabled ?: !isPresentationMode)
                }
            }
            is ManaqibUiIntent.UpdateFontScale -> {
                val clamped = intent.scale.coerceIn(0.75f, 2.0f)
                updateState { copy(fontScale = clamped) }
            }
            is ManaqibUiIntent.ToggleHighContrast -> {
                updateState {
                    copy(isHighContrast = intent.enabled ?: !isHighContrast)
                }
            }
            is ManaqibUiIntent.NextChapter -> {
                val current = currentState.currentChapter?.chapterNumber ?: 1
                if (current < 56) {
                    val next = current + 1
                    val chapter = allSeedChapters.find { it.chapterNumber == next }
                        ?: currentState.chapters.find { it.chapterNumber == next }
                    if (chapter != null) {
                        updateState { copy(currentChapter = chapter) }
                    }
                    sendEffect(ManaqibUiEffect.NavigateToChapter(next))
                }
            }
            is ManaqibUiIntent.PreviousChapter -> {
                val current = currentState.currentChapter?.chapterNumber ?: 1
                if (current > 1) {
                    val prev = current - 1
                    val chapter = allSeedChapters.find { it.chapterNumber == prev }
                        ?: currentState.chapters.find { it.chapterNumber == prev }
                    if (chapter != null) {
                        updateState { copy(currentChapter = chapter) }
                    }
                    sendEffect(ManaqibUiEffect.NavigateToChapter(prev))
                }
            }
            is ManaqibUiIntent.RefreshData -> {
                loadInitialData()
            }
        }
    }
}
