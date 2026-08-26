package com.iqbalwork.robithoh.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Navigation 3 NavDisplay implementation for Compose Multiplatform.
 * Displays the active top route from the backstack with smooth directional motion.
 */
@Composable
fun <T : Any> NavDisplay(
    backstack: List<T>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    entryProvider: @Composable (key: T) -> Unit
) {
    val currentKey = backstack.lastOrNull() ?: return

    AnimatedContent(
        targetState = currentKey,
        transitionSpec = {
            fadeIn(animationSpec = tween(220)) +
                slideInHorizontally(animationSpec = tween(260)) { width -> width / 4 } togetherWith
                fadeOut(animationSpec = tween(180)) +
                slideOutHorizontally(animationSpec = tween(220)) { width -> -width / 4 }
        },
        label = "Nav3DisplayAnimation",
        modifier = modifier
    ) { key ->
        Box(modifier = Modifier.fillMaxSize()) {
            entryProvider(key)
        }
    }
}
