package com.iqbalwork.robithoh.feature.waktal.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iqbalwork.robithoh.core.database.rememberRobithohDatabase
import com.iqbalwork.robithoh.feature.waktal.data.WaktalRepository
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalDetailUiEffect
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalDetailUiIntent
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalDetailViewModel
import com.iqbalwork.robithoh.navigation.BackHandler
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WaktalDetailScreen(
    id: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WaktalDetailViewModel? = null
) {
    BackHandler {
        onBackClick()
    }

    val database = rememberRobithohDatabase()
    val vm = viewModel ?: viewModel {
        val repository = WaktalRepository(database = database)
        WaktalDetailViewModel(repository = repository)
    }

    val uriHandler = LocalUriHandler.current
    val state by vm.uiState.collectAsState()

    LaunchedEffect(id) {
        vm.onIntent(WaktalDetailUiIntent.LoadDetail(id))
    }

    LaunchedEffect(vm) {
        vm.effect.collectLatest { effect ->
            when (effect) {
                is WaktalDetailUiEffect.LaunchUri -> {
                    try {
                        uriHandler.openUri(effect.uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                is WaktalDetailUiEffect.ShowToast -> {
                    // Handled gracefully in UI
                }
            }
        }
    }

    WaktalDetailContent(
        state = state,
        onDialPhone = { vm.onIntent(WaktalDetailUiIntent.DialPhone) },
        onOpenWhatsApp = { vm.onIntent(WaktalDetailUiIntent.OpenWhatsApp) },
        onOpenMap = { vm.onIntent(WaktalDetailUiIntent.OpenMap) },
        onBackClick = onBackClick,
        modifier = modifier
    )
}
