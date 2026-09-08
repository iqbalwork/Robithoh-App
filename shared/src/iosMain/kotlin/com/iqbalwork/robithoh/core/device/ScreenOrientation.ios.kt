package com.iqbalwork.robithoh.core.device

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSNumber
import platform.Foundation.setValue
import platform.UIKit.UIDevice
import platform.UIKit.UIInterfaceOrientationLandscapeRight
import platform.UIKit.UIInterfaceOrientationPortrait

class IosScreenOrientationController : ScreenOrientationController {
    override fun setLandscape() {
        try {
            UIDevice.currentDevice.setValue(
                NSNumber(int = UIInterfaceOrientationLandscapeRight.toInt()),
                "orientation"
            )
        } catch (_: Throwable) {}
    }

    override fun setPortrait() {
        try {
            UIDevice.currentDevice.setValue(
                NSNumber(int = UIInterfaceOrientationPortrait.toInt()),
                "orientation"
            )
        } catch (_: Throwable) {}
    }

    override fun toggleOrientation() {
        val current = UIDevice.currentDevice.orientation.value
        if (current == 3L || current == 4L) {
            setPortrait()
        } else {
            setLandscape()
        }
    }

    override fun resetToDefault() {
        setPortrait()
    }
}

@Composable
actual fun rememberScreenOrientationController(): ScreenOrientationController {
    return remember { IosScreenOrientationController() }
}
