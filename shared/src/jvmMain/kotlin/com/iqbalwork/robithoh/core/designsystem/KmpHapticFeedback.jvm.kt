package com.iqbalwork.robithoh.core.designsystem

import androidx.compose.runtime.Composable

class JvmHapticFeedback : KmpHapticFeedback {
    override fun performClick() {
        // No-op on JVM/Desktop
    }

    override fun performSuccess() {
        // No-op on JVM/Desktop
    }

    override fun performMilestone() {
        // No-op on JVM/Desktop
    }
}

@Composable
actual fun InitHapticContext() {
    // No-op on JVM
}

actual fun getHapticFeedback(): KmpHapticFeedback = JvmHapticFeedback()

