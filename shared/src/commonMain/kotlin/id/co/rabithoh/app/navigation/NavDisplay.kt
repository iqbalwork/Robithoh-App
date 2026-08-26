package id.co.rabithoh.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T : Any> NavDisplay(
    backstack: List<T>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    entryProvider: @Composable (key: T) -> Unit
) {
    com.iqbalwork.robithoh.navigation.NavDisplay(
        backstack = backstack,
        onBack = onBack,
        modifier = modifier,
        entryProvider = entryProvider
    )
}
