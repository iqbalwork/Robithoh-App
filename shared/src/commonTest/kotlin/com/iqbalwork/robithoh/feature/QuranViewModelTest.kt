package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.quran.data.QuranRepositoryImpl
import com.iqbalwork.robithoh.feature.quran.presentation.QuranTab
import com.iqbalwork.robithoh.feature.quran.presentation.QuranUiIntent
import com.iqbalwork.robithoh.feature.quran.presentation.QuranViewModel
import kotlinx.coroutines.flow.first
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
}
