package com.iqbalwork.robithoh.core.device

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

class AndroidScreenOrientationController(private val activity: Activity?) : ScreenOrientationController {
    override fun setLandscape() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    override fun setPortrait() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
    }

    override fun toggleOrientation() {
        val current = activity?.resources?.configuration?.orientation
        if (current == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            setPortrait()
        } else {
            setLandscape()
        }
    }

    override fun resetToDefault() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}

@Composable
actual fun rememberScreenOrientationController(): ScreenOrientationController {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    return remember(activity) { AndroidScreenOrientationController(activity) }
}
