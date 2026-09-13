package com.iqbalwork.robithoh.feature.waktal.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iqbalwork.robithoh.core.database.rememberRobithohDatabase
import com.iqbalwork.robithoh.core.network.createKtorHttpClient
import com.iqbalwork.robithoh.feature.waktal.data.WaktalRepository
import com.iqbalwork.robithoh.feature.waktal.data.remote.WaktalApiService
import com.iqbalwork.robithoh.feature.waktal.data.sync.WaktalSyncManager
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalListUiEffect
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalListUiIntent
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalViewModel
import com.iqbalwork.robithoh.navigation.BackHandler
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WaktalListScreen(
    onWaktalClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WaktalViewModel? = null
) {
    BackHandler {
        onBackClick()
    }

    val database = rememberRobithohDatabase()
    val vm = viewModel ?: viewModel {
        val repository = WaktalRepository(database = database)
        val apiService = WaktalApiService(httpClient = createKtorHttpClient())
        val syncManager = WaktalSyncManager(apiService = apiService, repository = repository)
        WaktalViewModel(repository = repository, syncManager = syncManager)
    }

    val state by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(vm) {
        vm.effect.collectLatest { effect ->
            when (effect) {
                is WaktalListUiEffect.NavigateToDetail -> onWaktalClick(effect.id)
                is WaktalListUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    WaktalListContent(
        state = state,
        onSearchQueryChange = { query ->
            vm.onIntent(WaktalListUiIntent.UpdateSearchQuery(query))
        },
        onSelectStatus = { status ->
            vm.onIntent(WaktalListUiIntent.SelectStatus(status))
        },
        onSelectProvince = { province ->
            vm.onIntent(WaktalListUiIntent.SelectProvince(province))
        },
        onToggleSortByDistance = { enabled ->
            vm.onIntent(WaktalListUiIntent.ToggleSortByDistance(enabled))
        },
        onWaktalClick = { id ->
            vm.onIntent(WaktalListUiIntent.SelectWaktal(id))
        },
        onTriggerSync = {
            vm.onIntent(WaktalListUiIntent.TriggerSync)
        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}
