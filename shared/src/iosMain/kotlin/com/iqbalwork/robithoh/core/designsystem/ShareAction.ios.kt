package com.iqbalwork.robithoh.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@Composable
actual fun rememberShareTextAction(): (String) -> Unit {
    return remember {
        { text: String ->
            val activityController = UIActivityViewController(
                activityItems = listOf(text),
                applicationActivities = null
            )
            var topController = UIApplication.sharedApplication.keyWindow?.rootViewController
            while (topController?.presentedViewController != null) {
                topController = topController.presentedViewController
            }
            topController?.presentViewController(activityController, animated = true, completion = null)
        }
    }
}
