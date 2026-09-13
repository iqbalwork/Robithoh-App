package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Global singleton state manager to control full-screen image viewer overlay.
 */
object ImageViewerManager {
    var activeImage by mutableStateOf<Any?>(null)
        private set

    val isVisible: Boolean
        get() = activeImage != null

    fun show(model: Any?) {
        if (model != null) {
            activeImage = model
        }
    }

    fun hide() {
        activeImage = null
    }
}
