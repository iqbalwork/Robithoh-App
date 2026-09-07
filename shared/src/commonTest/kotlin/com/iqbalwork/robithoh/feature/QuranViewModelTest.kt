package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.quran.data.QuranRepositoryImpl
import com.iqbalwork.robithoh.feature.quran.presentation.QuranTab
import com.iqbalwork.robithoh.feature.quran.presentation.QuranUiIntent
import com.iqbalwork.robithoh.feature.quran.presentation.QuranViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class QuranViewModelTest {

    private lateinit var repository: QuranRepositoryImpl
    private lateinit var viewModel: QuranViewModel

    @BeforeTest
    fun setup() {
        repository = QuranRepositoryImpl()
        viewModel = QuranViewModel(repository)
    }

    @Test
    fun testInitialState() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(QuranTab.SURAHS, state.selectedTab)
        assertEquals(114, state.surahs.size)
        assertTrue(state.shalawatList.isNotEmpty())
        assertTrue(state.ziarahSections.isNotEmpty())
    }

    @Test
    fun testSelectTab() = runTest {
        viewModel.onIntent(QuranUiIntent.SelectTab(QuranTab.SHALAWAT))
        assertEquals(QuranTab.SHALAWAT, viewModel.uiState.value.selectedTab)

        viewModel.onIntent(QuranUiIntent.SelectTab(QuranTab.ZIARAH))
        assertEquals(QuranTab.ZIARAH, viewModel.uiState.value.selectedTab)

        viewModel.onIntent(QuranUiIntent.SelectTab(QuranTab.BOOKMARKS))
        assertEquals(QuranTab.BOOKMARKS, viewModel.uiState.value.selectedTab)
    }

    @Test
    fun testSelectSurahAndLoadAyahs() = runTest {
        viewModel.onIntent(QuranUiIntent.SelectSurah(1))
        assertEquals(1, viewModel.uiState.value.currentSurah?.number)
        assertEquals(7, viewModel.uiState.value.currentAyahs.size)

        viewModel.onIntent(QuranUiIntent.SelectSurah(114))
        assertEquals(114, viewModel.uiState.value.currentSurah?.number)
        assertEquals(6, viewModel.uiState.value.currentAyahs.size)
    }

    @Test
    fun testFontScaling() = runTest {
        viewModel.onIntent(QuranUiIntent.UpdateFontScale(1.3f))
        assertEquals(1.3f, viewModel.uiState.value.fontScale)

        viewModel.onIntent(QuranUiIntent.UpdateFontScale(0.5f))
        assertEquals(0.75f, viewModel.uiState.value.fontScale)
    }

    @Test
    fun testSearchSurahs() = runTest {
        viewModel.onIntent(QuranUiIntent.SearchSurahs("Ikhlas"))
        val filtered = viewModel.uiState.value.surahs
        assertEquals(1, filtered.size)
        assertEquals(112, filtered[0].number)
    }

    @Test
    fun testSaveBookmarkUpdatesLastReadState() = runTest {
        viewModel.onIntent(
            QuranUiIntent.SaveBookmark(
                surahNumber = 36,
                ayahNumber = 58,
                surahName = "Yasin"
            )
        )
        val bookmark = viewModel.uiState.value.lastReadBookmark
        assertNotNull(bookmark)
        assertEquals(36, bookmark.surahNumber)
        assertEquals(58, bookmark.ayahNumber)
        assertEquals("Yasin", bookmark.surahName)
    }

    @Test
    fun testDefaultQariAndPickerState() = runTest {
        val state = viewModel.uiState.value
        assertEquals("Mishari Rashid al-`Afasy", state.selectedQari.name)
        assertTrue(state.availableQaris.size >= 8)
        assertEquals(false, state.isQariPickerVisible)

        viewModel.onIntent(QuranUiIntent.SetQariPickerVisible(true))
        assertEquals(true, viewModel.uiState.value.isQariPickerVisible)

        viewModel.onIntent(QuranUiIntent.SetQariPickerVisible(false))
        assertEquals(false, viewModel.uiState.value.isQariPickerVisible)
    }

    @Test
    fun testSelectQari() = runTest {
        val sudais = viewModel.uiState.value.availableQaris.first { it.id == 3 }
        viewModel.onIntent(QuranUiIntent.SelectQari(sudais))
        assertEquals(3, viewModel.uiState.value.selectedQari.id)
        assertEquals("Abdur-Rahman as-Sudais", viewModel.uiState.value.selectedQari.name)
    }

    @Test
    fun testPlaySurahAudioPlaysVerseByVerse() = runTest {
        viewModel.onIntent(QuranUiIntent.PlaySurahAudio(surahNumber = 1, startAyahNumber = 1))
        val track = viewModel.uiState.value.activeAudioTrack
        assertNotNull(track)
        assertEquals("ayah_1_1", track.id)
        assertEquals(1, viewModel.uiState.value.activeAyahNumber)
    }

    @Test
    fun testAudioRepeatModeCycling() = runTest {
        assertEquals(com.iqbalwork.robithoh.feature.quran.presentation.QuranAudioRepeatMode.REPEAT_SURAH, viewModel.uiState.value.audioRepeatMode)

        viewModel.onIntent(QuranUiIntent.ToggleAudioRepeatMode)
        assertEquals(com.iqbalwork.robithoh.feature.quran.presentation.QuranAudioRepeatMode.REPEAT_AYAH, viewModel.uiState.value.audioRepeatMode)

        viewModel.onIntent(QuranUiIntent.ToggleAudioRepeatMode)
        assertEquals(com.iqbalwork.robithoh.feature.quran.presentation.QuranAudioRepeatMode.OFF, viewModel.uiState.value.audioRepeatMode)

        viewModel.onIntent(QuranUiIntent.ToggleAudioRepeatMode)
        assertEquals(com.iqbalwork.robithoh.feature.quran.presentation.QuranAudioRepeatMode.REPEAT_SURAH, viewModel.uiState.value.audioRepeatMode)
    }

    @Test
    fun testAutoPlayNextAyahOnCompletion() = runTest {
        val testPlayer = TestAudioPlayer()
        val vm = QuranViewModel(repository, audioPlayer = testPlayer)
        vm.onIntent(QuranUiIntent.PlayAyahAudio(surahNumber = 1, ayahNumber = 1))
        advanceUntilIdle()
        assertEquals("ayah_1_1", vm.uiState.value.activeAudioTrack?.id)
        assertEquals(1, vm.uiState.value.activeAyahNumber)

        // Complete track 1
        testPlayer._playbackState.value = com.iqbalwork.robithoh.core.model.AudioPlaybackState.COMPLETED
        advanceUntilIdle()

        // Next track should automatically be ayah 2
        assertEquals("ayah_1_2", vm.uiState.value.activeAudioTrack?.id)
        assertEquals(2, vm.uiState.value.activeAyahNumber)
    }

    @Test
    fun testAutoPlayNextSurahOnCompletionOfLastAyah() = runTest {
        val testPlayer = TestAudioPlayer()
        val vm = QuranViewModel(repository, audioPlayer = testPlayer)
        // Surah 1 has 7 ayahs
        vm.onIntent(QuranUiIntent.PlayAyahAudio(surahNumber = 1, ayahNumber = 7))
        advanceUntilIdle()
        assertEquals("ayah_1_7", vm.uiState.value.activeAudioTrack?.id)
        assertEquals(7, vm.uiState.value.activeAyahNumber)

        // Complete ayah 7
        testPlayer._playbackState.value = com.iqbalwork.robithoh.core.model.AudioPlaybackState.COMPLETED
        advanceUntilIdle()

        // Should advance to Surah 2 Ayah 1
        assertEquals("ayah_2_1", vm.uiState.value.activeAudioTrack?.id)
        assertEquals(1, vm.uiState.value.activeAyahNumber)
        assertEquals(2, vm.uiState.value.currentSurah?.number)
    }

    @Test
    fun testRepeatAyahModeRepeatsSameAyah() = runTest {
        val testPlayer = TestAudioPlayer()
        val vm = QuranViewModel(repository, audioPlayer = testPlayer)
        vm.onIntent(QuranUiIntent.SetAudioRepeatMode(com.iqbalwork.robithoh.feature.quran.presentation.QuranAudioRepeatMode.REPEAT_AYAH))
        vm.onIntent(QuranUiIntent.PlayAyahAudio(surahNumber = 1, ayahNumber = 3))
        advanceUntilIdle()

        testPlayer._playbackState.value = com.iqbalwork.robithoh.core.model.AudioPlaybackState.COMPLETED
        advanceUntilIdle()

        assertEquals("ayah_1_3", vm.uiState.value.activeAudioTrack?.id)
        assertEquals(3, vm.uiState.value.activeAyahNumber)
    }
}

private class TestAudioPlayer : com.iqbalwork.robithoh.core.audio.KmpAudioPlayer {
    val _currentTrack = kotlinx.coroutines.flow.MutableStateFlow<com.iqbalwork.robithoh.core.model.AudioTrack?>(null)
    override val currentTrack: kotlinx.coroutines.flow.StateFlow<com.iqbalwork.robithoh.core.model.AudioTrack?> = _currentTrack

    val _playbackState = kotlinx.coroutines.flow.MutableStateFlow(com.iqbalwork.robithoh.core.model.AudioPlaybackState.IDLE)
    override val playbackState: kotlinx.coroutines.flow.StateFlow<com.iqbalwork.robithoh.core.model.AudioPlaybackState> = _playbackState

    val _currentPositionMs = kotlinx.coroutines.flow.MutableStateFlow(0L)
    override val currentPositionMs: kotlinx.coroutines.flow.StateFlow<Long> = _currentPositionMs

    val _durationMs = kotlinx.coroutines.flow.MutableStateFlow(0L)
    override val durationMs: kotlinx.coroutines.flow.StateFlow<Long> = _durationMs

    override fun play(track: com.iqbalwork.robithoh.core.model.AudioTrack) {
        _currentTrack.value = track
        _playbackState.value = com.iqbalwork.robithoh.core.model.AudioPlaybackState.PLAYING
    }

    override fun pause() {
        _playbackState.value = com.iqbalwork.robithoh.core.model.AudioPlaybackState.PAUSED
    }

    override fun resume() {
        _playbackState.value = com.iqbalwork.robithoh.core.model.AudioPlaybackState.PLAYING
    }

    override fun seekTo(positionMs: Long) {
        _currentPositionMs.value = positionMs
    }

    override fun stop() {
        _currentTrack.value = null
        _playbackState.value = com.iqbalwork.robithoh.core.model.AudioPlaybackState.IDLE
    }

    override fun release() {
        stop()
    }
}

