package com.iqbalwork.robithoh.feature.tasbih.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiState
import com.iqbalwork.robithoh.navigation.BackHandler

@Composable
fun TasbihScreen(
    state: TasbihUiState,
    onIntent: (TasbihUiIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBack()
    }

    TasbihContent(
        state = state,
        onIntent = onIntent,
        onBack = onBack,
        modifier = modifier
    )
}
