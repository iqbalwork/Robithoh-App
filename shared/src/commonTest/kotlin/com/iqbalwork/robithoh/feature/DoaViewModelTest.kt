package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.doa.presentation.DoaUiEffect
import com.iqbalwork.robithoh.feature.doa.presentation.DoaUiIntent
import com.iqbalwork.robithoh.feature.doa.presentation.DoaViewModel
import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DoaViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MarkdownDocumentRepository
    private lateinit var viewModel: DoaViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = MarkdownDocumentRepository()
        viewModel = DoaViewModel(repository = repository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialStateLoadsNewExtractedDoa() = runTest {
        val state = viewModel.uiState.first()
        val docIds = state.documents.map { it.id }

        assertTrue(docIds.contains("doa_turun_hujan"), "Doa turun hujan must be loaded")
        assertTrue(docIds.contains("doa_sebelum_makan"), "Doa sebelum makan must be loaded")
        assertTrue(docIds.contains("doa_minum_air_zamzam"), "Doa minum air zamzam must be loaded")
        assertTrue(docIds.contains("doa_keluar_rumah"), "Doa keluar rumah must be loaded")
        assertTrue(docIds.contains("doa_masuk_rumah"), "Doa masuk rumah must be loaded")
        assertTrue(docIds.contains("wirid_kemalaikatan"), "Wirid kemalaikatan must be loaded")
    }

    @Test
    fun testSearchFilteringForExtractedDoa() = runTest {
        // Search "hujan"
        viewModel.onIntent(DoaUiIntent.SearchDoa("hujan"))
        var filtered = viewModel.uiState.value.filteredDocuments
        assertEquals(1, filtered.size)
        assertEquals("doa_turun_hujan", filtered.first().id)

        // Search "makan"
        viewModel.onIntent(DoaUiIntent.SearchDoa("makan"))
        filtered = viewModel.uiState.value.filteredDocuments
        assertEquals(1, filtered.size)
        assertEquals("doa_sebelum_makan", filtered.first().id)

        // Search "zamzam"
        viewModel.onIntent(DoaUiIntent.SearchDoa("zamzam"))
        filtered = viewModel.uiState.value.filteredDocuments
        assertEquals(1, filtered.size)
        assertEquals("doa_minum_air_zamzam", filtered.first().id)

        // Search "malaikat"
        viewModel.onIntent(DoaUiIntent.SearchDoa("malaikat"))
        filtered = viewModel.uiState.value.filteredDocuments
        assertEquals(1, filtered.size)
        assertEquals("wirid_kemalaikatan", filtered.first().id)

        // Search "rumah" (should match both keluar rumah & masuk rumah)
        viewModel.onIntent(DoaUiIntent.SearchDoa("rumah"))
        filtered = viewModel.uiState.value.filteredDocuments
        val filteredIds = filtered.map { it.id }
        assertTrue(filteredIds.contains("doa_keluar_rumah"))
        assertTrue(filteredIds.contains("doa_masuk_rumah"))
    }

    @Test
    fun testSelectDocumentEmitsNavigationEffect() = runTest {
        viewModel.onIntent(DoaUiIntent.SelectDocument("doa_turun_hujan"))
        testDispatcher.scheduler.advanceUntilIdle()
        val receivedEffect = viewModel.effect.first()
        assertEquals(DoaUiEffect.NavigateToDocument("doa_turun_hujan"), receivedEffect)
    }
}
