package com.iqbalwork.robithoh.core.designsystem

/**
 * Multiplatform haptic feedback engine for tactile user interactions (tasbih counter, buttons, milestones).
 */
interface KmpHapticFeedback {
    /** Light tap feedback for standard button clicks or tasbih increment */
    fun performClick()

    /** Success pattern feedback for completion of a prayer / sub-routine */
    fun performSuccess()

    /** Strong tactile milestone feedback for major milestones (33x, 100x, 165x) */
    fun performMilestone()
}

expect fun getHapticFeedback(): KmpHapticFeedback
