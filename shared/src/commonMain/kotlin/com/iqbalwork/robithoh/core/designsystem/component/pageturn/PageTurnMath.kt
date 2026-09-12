package com.iqbalwork.robithoh.core.designsystem.component.pageturn

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Geometric output for a single point or strip coordinate projected along the virtual cylinder.
 *
 * @property screenX The resulting X-coordinate on screen.
 * @property shadeFactor Ambient lighting factor cos(theta), ranging from 1.0 (flat facing user)
 *                        to 0.0 (perpendicular at apex) to -1.0 (inverted back-face).
 * @property isBackFace True if the point is rotated past 90 degrees (theta > pi/2).
 * @property verticalScale Perspective vertical scale factor (applies ~1.5% vertical taper).
 */
data class ProjectedStrip(
    val screenX: Float,
    val shadeFactor: Float,
    val isBackFace: Boolean,
    val verticalScale: Float
)

/**
 * Pure Kotlin mathematical engine for calculating cylinder curl geometry.
 * Completely free of Compose UI dependencies for deterministic unit testing.
 */
object PageTurnMath {

    /** Overlap in pixels between adjacent strips to prevent 1px raster seam bleed. */
    const val SEAM_OVERLAP_PX = 0.5f

    /** Maximum vertical perspective taper ratio (1.5%). */
    const val MAX_VERTICAL_TAPER = 0.015f

    /** Initial curvature radius as fraction of page width (18%). */
    const val INITIAL_RADIUS_FRACTION = 0.18f

    /** Minimum allowable cylinder radius in pixels to prevent division by zero. */
    const val MIN_RADIUS_PX = 1.0f

    /**
     * Determines whether a transition represents turning a physical paper leaf across spreads
     * (requiring 3D book curl) versus shifting within the same open two-page spread (requiring slide).
     *
     * In a physical mushaf:
     * - Spread 1: Page 1 (odd/right) and Page 2 (even/left) -> floorPage = 0 -> slide within spread.
     * - Turning from Page 2 to Page 3 flips the leaf -> floorPage = 1 -> curl.
     * - Spread 2: Page 3 (odd/right) and Page 4 (even/left) -> floorPage = 2 -> slide within spread.
     * - Turning from Page 4 to Page 5 flips the leaf -> floorPage = 3 -> curl.
     *
     * Mathematically:
     * - floorPage % 2 == 1 (and >= 1): Leaf turn across spreads (2 <-> 3, 4 <-> 5, 6 <-> 7, ...) -> true (CURL)
     * - floorPage % 2 == 0: Intra-spread slide (1 <-> 2, 3 <-> 4, 5 <-> 6, ...) -> false (SLIDE)
     */
    fun isSpreadTurn(floorPage: Int): Boolean {
        return floorPage >= 1 && (floorPage % 2 == 1)
    }

    /**
     * Calculates the position on the X-axis where the paper begins to lift from the flat plane.
     * When progress = 0.0, the fold is at the outer margin (width).
     * When progress = 1.0, the fold has travelled all the way to the spine (0.0).
     */
    fun foldPosition(width: Float, progress: Float): Float {
        val p = progress.coerceIn(0f, 1f)
        return width * (1f - p)
    }

    /**
     * Curvature radius of the virtual cylinder.
     * Decays linearly with progress towards near-zero to prevent any residual curled lobe
     * at progress = 1.0.
     */
    fun curlRadius(width: Float, progress: Float): Float {
        val p = progress.coerceIn(0f, 1f)
        return max(width * INITIAL_RADIUS_FRACTION * (1f - p), MIN_RADIUS_PX)
    }

    /**
     * Calculates the number of vertical strips to slice the page into.
     * Adaptive based on viewport width: ~1 strip per 12 pixels, clamped between 24 and 64.
     */
    fun stripCountFor(width: Float): Int {
        return (width / 12f).toInt().coerceIn(24, 64)
    }

    /**
     * Projects a 1D source coordinate [x] onto the cylinder curl geometry.
     *
     * @param x Source X-coordinate in page space (0.0 .. width).
     * @param width Viewport width in pixels.
     * @param progress Turn progress normalized (0.0 .. 1.0).
     * @param spineSide Location of the page binding. Default is [SpineSide.LEFT].
     */
    fun project(
        x: Float,
        width: Float,
        progress: Float,
        spineSide: SpineSide = SpineSide.LEFT
    ): ProjectedStrip {
        if (width <= 0f) {
            return ProjectedStrip(x, 1f, false, 1f)
        }

        // Handle RTL / Spine on Right by mirroring coordinate space
        if (spineSide == SpineSide.RIGHT) {
            val mirroredX = width - x
            val proj = projectLeftSpine(mirroredX, width, progress)
            return proj.copy(screenX = width - proj.screenX)
        }

        return projectLeftSpine(x, width, progress)
    }

    private fun projectLeftSpine(
        x: Float,
        width: Float,
        progress: Float
    ): ProjectedStrip {
        val p = progress.coerceIn(0f, 1f)
        val f = foldPosition(width, p)
        val r = curlRadius(width, p)
        val piFloat = PI.toFloat()
        val halfPi = piFloat / 2f
        val cylinderCircumferenceHalf = piFloat * r

        return when {
            // Zone 1: Flat unturned page section
            x <= f -> {
                ProjectedStrip(
                    screenX = x,
                    shadeFactor = 1.0f,
                    isBackFace = false,
                    verticalScale = 1.0f
                )
            }

            // Zone 2: Cylindrical wrapping bend
            x <= f + cylinderCircumferenceHalf -> {
                val arc = x - f
                val theta = min(arc / r, piFloat)
                val screenX = f + r * sin(theta)
                val shade = cos(theta)
                val isBack = theta > (halfPi + 0.001f)
                val taper = 1.0f - (MAX_VERTICAL_TAPER * (1.0f - shade) / 2.0f)

                ProjectedStrip(
                    screenX = screenX,
                    shadeFactor = shade,
                    isBackFace = isBack,
                    verticalScale = taper
                )
            }

            // Zone 3: Flat inverted back-face section extending toward spine
            else -> {
                val overshoot = (x - f) - cylinderCircumferenceHalf
                val screenX = f - overshoot
                val taper = 1.0f - MAX_VERTICAL_TAPER

                ProjectedStrip(
                    screenX = screenX,
                    shadeFactor = -1.0f,
                    isBackFace = true,
                    verticalScale = taper
                )
            }
        }
    }
}
