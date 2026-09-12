package com.iqbalwork.robithoh.core.designsystem.component.pageturn

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Tier A Fallback: Hardware-accelerated 3D page flip using [Modifier.graphicsLayer].
 * Pinned to the spine edge with perspective camera distance.
 *
 * @param progress Normalized turn progress from 0.0 (flat unturned) to 1.0 (turn complete).
 * @param spineSide Location of the page binding/hinge ([SpineSide.LEFT] or [SpineSide.RIGHT]).
 */
fun Modifier.rigidPageFlip(
    progress: Float,
    spineSide: SpineSide
): Modifier {
    val p = progress.coerceIn(0f, 1f)
    if (p <= 0.001f) return this

    val angle = when (spineSide) {
        SpineSide.LEFT -> -180f * p
        SpineSide.RIGHT -> 180f * p
    }

    val origin = when (spineSide) {
        SpineSide.LEFT -> TransformOrigin(0f, 0.5f)
        SpineSide.RIGHT -> TransformOrigin(1f, 0.5f)
    }

    return this.graphicsLayer {
        rotationY = angle
        cameraDistance = 16f * density
        transformOrigin = origin
    }
}
