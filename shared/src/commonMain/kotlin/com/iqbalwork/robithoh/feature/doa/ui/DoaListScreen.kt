package com.iqbalwork.robithoh.feature.doa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iqbalwork.robithoh.core.database.rememberRobithohDatabase
import com.iqbalwork.robithoh.feature.doa.presentation.DoaUiEffect
import com.iqbalwork.robithoh.feature.doa.presentation.DoaUiIntent
import com.iqbalwork.robithoh.feature.doa.presentation.DoaViewModel
import com.iqbalwork.robithoh.navigation.BackHandler
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DoaListScreen(
    onDocumentClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DoaViewModel? = null
) {
    BackHandler {
        onBackClick()
    }

    val database = rememberRobithohDatabase()
    val vm = viewModel ?: viewModel {
        DoaViewModel(database = database)
    }

    val state by vm.uiState.collectAsState()

    LaunchedEffect(vm) {
        vm.effect.collectLatest { effect ->
            when (effect) {
                is DoaUiEffect.NavigateToDocument -> onDocumentClick(effect.documentId)
            }
        }
    }

    DoaListContent(
        state = state,
        onSearchQueryChange = { query ->
            vm.onIntent(DoaUiIntent.SearchDoa(query))
        },
        onDocumentClick = { documentId ->
            vm.onIntent(DoaUiIntent.SelectDocument(documentId))
        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}
