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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier

/**
 * Navigation 3 NavDisplay implementation for Compose Multiplatform.
 * Displays the active top route from the backstack with smooth directional motion.
 *
 * Each entry's content is wrapped in a [androidx.compose.runtime.saveable.SaveableStateHolder]
 * keyed by its route, so `rememberSaveable` state inside a screen (scroll/lazy list
 * position, form input, etc.) survives being covered by another screen and later
 * returned to — otherwise AnimatedContent fully disposes the covered entry's
 * composition and every remembered value resets.
 */
@Composable
fun <T : Any> NavDisplay(
    backstack: List<T>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    entryProvider: @Composable (key: T) -> Unit
) {
    val currentKey = backstack.lastOrNull() ?: return
    val saveableStateHolder = rememberSaveableStateHolder()

    // SaveableStateProvider's key must be Bundle-storable on Android (String,
    // Int, Parcelable, ...) — ScreenKey (a plain data class/object) isn't, so
    // we key on its toString() instead, which is unique per distinct route
    // value (e.g. "Home", "QuranSurah(surahNumber=2)").
    val stateKeyOf: (T) -> String = { it.toString() }

    // Release retained state for entries popped off the backstack for good,
    // so it doesn't accumulate unboundedly over a long session.
    val previousBackstack = remember { mutableListOf<T>() }
    SideEffect {
        previousBackstack.filter { it !in backstack }
            .forEach { saveableStateHolder.removeState(stateKeyOf(it)) }
        previousBackstack.clear()
        previousBackstack.addAll(backstack)
    }

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
            saveableStateHolder.SaveableStateProvider(stateKeyOf(key)) {
                entryProvider(key)
            }
        }
    }
}
