package com.iqbalwork.robithoh.core.designsystem.component.pageturn

/**
 * Runtime frame timing guard that monitors frame durations during active page turns.
 * Automatically downgrades from [PageTurnTier.TIER_B_CURL] to [PageTurnTier.TIER_A_RIGID]
 * if consecutive janky frames (>32ms, equivalent to dropping 2 consecutive frames at 60fps)
 * are detected on low-spec hardware.
 */
class PageTurnPerformanceGuard(
    private val jankThresholdNanos: Long = 32_000_000L,
    private val maxConsecutiveJanks: Int = 3
) {
    private var consecutiveJanks = 0

    /** Currently active rendering tier. */
    var activeTier: PageTurnTier = PageTurnTier.TIER_B_CURL
        private set

    /**
     * Records a frame draw time in nanoseconds and updates tier if consecutive janks occur.
     */
    fun recordFrame(frameDurationNanos: Long) {
        if (activeTier == PageTurnTier.TIER_A_RIGID) return

        if (frameDurationNanos > jankThresholdNanos) {
            consecutiveJanks++
            if (consecutiveJanks >= maxConsecutiveJanks) {
                activeTier = PageTurnTier.TIER_A_RIGID
            }
        } else {
            consecutiveJanks = 0
        }
    }

    /**
     * Resets guard state back to [PageTurnTier.TIER_B_CURL].
     */
    fun reset() {
        consecutiveJanks = 0
        activeTier = PageTurnTier.TIER_B_CURL
    }
}
