package com.iqbalwork.robithoh.feature.quran.presentation

import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.core.presentation.UiEffect
import com.iqbalwork.robithoh.core.presentation.UiIntent
import com.iqbalwork.robithoh.core.presentation.UiState
import com.iqbalwork.robithoh.feature.quran.model.Ayah
import com.iqbalwork.robithoh.feature.quran.model.QuranBookmark
import com.iqbalwork.robithoh.feature.quran.model.ShalawatModel
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta
import com.iqbalwork.robithoh.feature.quran.model.ZiarahSection

import com.iqbalwork.robithoh.feature.quran.model.ChapterAudio
import com.iqbalwork.robithoh.feature.quran.model.QariList
import com.iqbalwork.robithoh.feature.quran.model.QariOption

enum class QuranTab(val label: String) {
    SURAHS("114 Surah"),
    SHALAWAT("Koleksi Shalawat"),
    ZIARAH("Panduan Ziarah"),
    BOOKMARKS("Terakhir Dibaca")
}

enum class QuranAudioRepeatMode {
    REPEAT_SURAH, // Auto-play next ayah in surah
    REPEAT_AYAH,  // Repeat current ayah
    OFF           // Stop after current ayah finishes
}

data class QuranUiState(
    val selectedTab: QuranTab = QuranTab.SURAHS,
    val surahs: List<SurahMeta> = emptyList(),
    val searchQuery: String = "",
    val currentSurah: SurahMeta? = null,
    val currentAyahs: List<Ayah> = emptyList(),
    val lastReadBookmark: QuranBookmark? = null,
    val bookmarks: List<QuranBookmark> = emptyList(),
    val shalawatList: List<ShalawatModel> = emptyList(),
    val ziarahSections: List<ZiarahSection> = emptyList(),
    val fontScale: Float = 1.0f, // 0.8f to 2.0f
    val isTajwidColorEnabled: Boolean = true,
    val activeAudioTrack: AudioTrack? = null,
    val audioPlaybackState: AudioPlaybackState = AudioPlaybackState.IDLE,
    val audioPositionMs: Long = 0L,
    val audioDurationMs: Long = 0L,
    val selectedQari: QariOption = QariList.default,
    val availableQaris: List<QariOption> = QariList.popular,
    val activeAyahNumber: Int? = null,
    val activeChapterAudio: ChapterAudio? = null,
    val audioRepeatMode: QuranAudioRepeatMode = QuranAudioRepeatMode.REPEAT_SURAH,
    val isQariPickerVisible: Boolean = false,
    val isAudioLoading: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState

sealed interface QuranUiIntent : UiIntent {
    data class SelectTab(val tab: QuranTab) : QuranUiIntent
    data class SearchSurahs(val query: String) : QuranUiIntent
    data class SelectSurah(val surahNumber: Int) : QuranUiIntent
    data class SaveBookmark(
        val surahNumber: Int,
        val ayahNumber: Int,
        val surahName: String,
        val showToast: Boolean = false
    ) : QuranUiIntent
    data class UpdateFontScale(val scale: Float) : QuranUiIntent
    data class ToggleTajwidColors(val enabled: Boolean? = null) : QuranUiIntent
    data class PlayAudio(val track: AudioTrack) : QuranUiIntent
    data class PlaySurahAudio(val surahNumber: Int, val startAyahNumber: Int = 1) : QuranUiIntent
    data class PlayAyahAudio(val surahNumber: Int, val ayahNumber: Int) : QuranUiIntent
    data class SeekToAyah(val ayahNumber: Int) : QuranUiIntent
    data class SelectQari(val qari: QariOption) : QuranUiIntent
    data class SetQariPickerVisible(val visible: Boolean) : QuranUiIntent
    data object ToggleAudioRepeatMode : QuranUiIntent
    data class SetAudioRepeatMode(val mode: QuranAudioRepeatMode) : QuranUiIntent
    data object TogglePlayPauseAudio : QuranUiIntent
    data object StopAudio : QuranUiIntent
    data object RefreshData : QuranUiIntent
}

sealed interface QuranUiEffect : UiEffect {
    data class ShowToast(val message: String) : QuranUiEffect
    data class NavigateToSurah(val surahNumber: Int) : QuranUiEffect
}

