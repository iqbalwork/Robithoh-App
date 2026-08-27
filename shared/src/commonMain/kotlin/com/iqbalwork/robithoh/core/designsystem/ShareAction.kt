package com.iqbalwork.robithoh.core.designsystem

import androidx.compose.runtime.Composable

/** Presents the platform's native share sheet for a plain-text payload (e.g. a shared ayat). */
@Composable
expect fun rememberShareTextAction(): (String) -> Unit
