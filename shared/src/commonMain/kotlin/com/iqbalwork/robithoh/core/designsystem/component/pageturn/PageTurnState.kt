package com.iqbalwork.robithoh.core.designsystem.component.pageturn

/**
 * Indicates which side the page binding (spine) is located.
 * In RTL Arabic mushaf, odd pages bind on the left and even pages bind on the right.
 */
enum class SpineSide {
    LEFT,
    RIGHT
}

/**
 * Direction of page transition.
 */
enum class TurnDirection {
    FORWARD,
    BACKWARD
}

/**
 * Rendering tier for the page turn engine.
 * - [TIER_B_CURL]: 24-64 vertical canvas strips with cylindrical projection, lighting, and shadow.
 * - [TIER_A_RIGID]: Hardware-accelerated 3D flip fallback via graphicsLayer.
 */
enum class PageTurnTier {
    TIER_B_CURL,
    TIER_A_RIGID
}

/**
 * Encapsulates the dynamic state of a turning page.
 *
 * @property progress Normalized turn progress from 0.0 (unturned/flat) to 1.0 (turn complete/flat).
 * @property spineSide The bound edge of the page.
 * @property direction Direction of movement (FORWARD or BACKWARD).
 * @property activeTier Active renderer tier (TIER_B_CURL or TIER_A_RIGID).
 */
data class PageTurnState(
    val progress: Float,
    val spineSide: SpineSide,
    val direction: TurnDirection = TurnDirection.FORWARD,
    val activeTier: PageTurnTier = PageTurnTier.TIER_B_CURL
)
