package com.iqbalwork.robithoh.core.designsystem

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

actual fun getHapticFeedback(): KmpHapticFeedback = JvmHapticFeedback()
