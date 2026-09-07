package com.iqbalwork.robithoh.core.device

import androidx.compose.runtime.Composable

interface ScreenOrientationController {
    fun setLandscape()
    fun setPortrait()
    fun toggleOrientation()
    fun resetToDefault()
}

@Composable
expect fun rememberScreenOrientationController(): ScreenOrientationController
