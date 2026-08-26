package com.iqbalwork.robithoh.feature.manaqib.presentation

import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.core.presentation.UiEffect
import com.iqbalwork.robithoh.core.presentation.UiIntent
import com.iqbalwork.robithoh.core.presentation.UiState
import com.iqbalwork.robithoh.feature.manaqib.model.*

enum class ManaqibTab(val label: String) {
    CHAPTERS("56 Manqobah"),
    TANBIH("Wasiat Tanbih"),
    SILSILAH("Silsilah 1-38"),
    MC_ACARA("Susunan MC"),
    KHOTAMAN("Khotaman TQN"),
    DOA("Doa-Doa")
}

data class ManaqibUiState(
    val selectedTab: ManaqibTab = ManaqibTab.CHAPTERS,
    val selectedLanguage: LiturgyLanguage = LiturgyLanguage.INDONESIAN,
    val chapters: List<ManqobahChapter> = emptyList(),
    val currentChapter: ManqobahChapter? = null,
    val chapterSearchQuery: String = "",
    val silsilahList: List<SilsilahNode> = emptyList(),
    val silsilahSearchQuery: String = "",
    val tanbih: TanbihContent? = null,
    val mcPrograms: List<McProgramItem> = emptyList(),
    val khotamanSteps: List<KhotamanStep> = emptyList(),
    val doaList: List<DoaSpiritualItem> = emptyList(),
    val selectedDoa: DoaSpiritualItem? = null,
    val isPresentationMode: Boolean = false,
    val fontScale: Float = 1.0f, // 0.8f to 1.8f for senior readers
    val isHighContrast: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState

sealed interface ManaqibUiIntent : UiIntent {
    data class SelectTab(val tab: ManaqibTab) : ManaqibUiIntent
    data class SelectLanguage(val language: LiturgyLanguage) : ManaqibUiIntent
    data class SearchChapters(val query: String) : ManaqibUiIntent
    data class SelectChapter(val chapterNumber: Int) : ManaqibUiIntent
    data class SearchSilsilah(val query: String) : ManaqibUiIntent
    data class SelectDoa(val doaId: String) : ManaqibUiIntent
    data class TogglePresentationMode(val enabled: Boolean? = null) : ManaqibUiIntent
    data class UpdateFontScale(val scale: Float) : ManaqibUiIntent
    data class ToggleHighContrast(val enabled: Boolean? = null) : ManaqibUiIntent
    data object NextChapter : ManaqibUiIntent
    data object PreviousChapter : ManaqibUiIntent
    data object RefreshData : ManaqibUiIntent
}

sealed interface ManaqibUiEffect : UiEffect {
    data class ShowToast(val message: String) : ManaqibUiEffect
    data class NavigateToChapter(val chapterNumber: Int) : ManaqibUiEffect
}
