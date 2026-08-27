package com.iqbalwork.robithoh.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberShareTextAction(): (String) -> Unit {
    return remember { { _: String -> /* Sharing is not supported on desktop */ } }
}
