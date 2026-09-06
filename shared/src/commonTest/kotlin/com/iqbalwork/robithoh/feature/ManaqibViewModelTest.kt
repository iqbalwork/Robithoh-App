package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.feature.manaqib.data.ManaqibRepositoryImpl
import com.iqbalwork.robithoh.feature.manaqib.presentation.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class ManaqibViewModelTest {

    private lateinit var repository: ManaqibRepositoryImpl
    private lateinit var viewModel: ManaqibViewModel

    @BeforeTest
    fun setup() {
        repository = ManaqibRepositoryImpl()
        viewModel = ManaqibViewModel(repository)
    }

    @Test
    fun testInitialState() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(ManaqibTab.CHAPTERS, state.selectedTab)
        assertEquals(LiturgyLanguage.INDONESIAN, state.selectedLanguage)
        assertNotNull(state.tanbih)
        assertEquals(38, state.silsilahList.size)
        assertEquals(7, state.mcPrograms.size)
        assertEquals(3, state.doaList.size)
    }

    @Test
    fun testSelectTabIntent() = runTest {
        viewModel.onIntent(ManaqibUiIntent.SelectTab(ManaqibTab.TANBIH))
        assertEquals(ManaqibTab.TANBIH, viewModel.uiState.value.selectedTab)

        viewModel.onIntent(ManaqibUiIntent.SelectTab(ManaqibTab.SILSILAH))
        assertEquals(ManaqibTab.SILSILAH, viewModel.uiState.value.selectedTab)
    }

    @Test
    fun testSelectLanguageIntent() = runTest {
        viewModel.onIntent(ManaqibUiIntent.SelectLanguage(LiturgyLanguage.SUNDANESE))
        assertEquals(LiturgyLanguage.SUNDANESE, viewModel.uiState.value.selectedLanguage)

        viewModel.onIntent(ManaqibUiIntent.SelectLanguage(LiturgyLanguage.ARABIC))
        assertEquals(LiturgyLanguage.ARABIC, viewModel.uiState.value.selectedLanguage)
    }

    @Test
    fun testPresentationModeAndFontScale() = runTest {
        viewModel.onIntent(ManaqibUiIntent.TogglePresentationMode(true))
        assertTrue(viewModel.uiState.value.isPresentationMode)

        viewModel.onIntent(ManaqibUiIntent.UpdateFontScale(1.5f))
        assertEquals(1.5f, viewModel.uiState.value.fontScale)

        // Test boundary clamping
        viewModel.onIntent(ManaqibUiIntent.UpdateFontScale(3.0f))
        assertEquals(2.0f, viewModel.uiState.value.fontScale)

        viewModel.onIntent(ManaqibUiIntent.ToggleHighContrast(true))
        assertTrue(viewModel.uiState.value.isHighContrast)
    }

    @Test
    fun testChapterNavigation() = runTest {
        viewModel.onIntent(ManaqibUiIntent.SelectChapter(10))
        assertEquals(10, viewModel.uiState.value.currentChapter?.chapterNumber)

        viewModel.onIntent(ManaqibUiIntent.NextChapter)
        assertEquals(11, viewModel.uiState.value.currentChapter?.chapterNumber)

        viewModel.onIntent(ManaqibUiIntent.PreviousChapter)
        assertEquals(10, viewModel.uiState.value.currentChapter?.chapterNumber)
    }

    @Test
    fun testSearchSilsilah() = runTest {
        viewModel.onIntent(ManaqibUiIntent.SearchSilsilah("Abdul Qodir"))
        assertTrue(viewModel.uiState.value.silsilahList.isNotEmpty())
        assertTrue(viewModel.uiState.value.silsilahList.any { it.orderNumber == 17 })

        viewModel.onIntent(ManaqibUiIntent.SearchSilsilah(""))
        assertEquals(38, viewModel.uiState.value.silsilahList.size)
    }
}
