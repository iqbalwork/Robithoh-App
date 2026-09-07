package com.iqbalwork.robithoh.core.device

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class JvmScreenOrientationController : ScreenOrientationController {
    override fun setLandscape() {}
    override fun setPortrait() {}
    override fun toggleOrientation() {}
    override fun resetToDefault() {}
}

@Composable
actual fun rememberScreenOrientationController(): ScreenOrientationController {
    return remember { JvmScreenOrientationController() }
}
