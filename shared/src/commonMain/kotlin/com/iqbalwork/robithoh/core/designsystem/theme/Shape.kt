package com.iqbalwork.robithoh.core.designsystem.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

// =========================================================================
// 1. MATERIAL 3 SHAPES
// =========================================================================

val RabithohM3Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

// =========================================================================
// 2. ISLAMIC GEOMETRIC & ARCH SHAPES
// =========================================================================

/**
 * Islamic pointed arch shape (Mihrab / Qubbah arch).
 * Curves upward from left and right to meet at a gentle peak at top center.
 */
class IslamicArchShape(
    private val archHeightRatio: Float = 0.25f,
    private val bottomCornerRadius: Dp = 12.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width
        val height = size.height
        val archH = height * archHeightRatio.coerceIn(0.1f, 0.5f)
        val bottomR = with(density) { bottomCornerRadius.toPx() }

        val path = Path().apply {
            // Start at bottom-left corner
            moveTo(0f, height - bottomR)
            quadraticTo(0f, height, bottomR, height)

            // Bottom edge to bottom-right
            lineTo(width - bottomR, height)
            quadraticTo(width, height, width, height - bottomR)

            // Right edge up to spring line of arch
            lineTo(width, archH)

            // Arch curve right-to-peak: Cubic bezier with subtle ogival inflection
            cubicTo(
                width, archH * 0.4f,
                width * 0.65f, 0f,
                width * 0.5f, 0f
            )

            // Arch curve peak-to-left: Symmetrical
            cubicTo(
                width * 0.35f, 0f,
                0f, archH * 0.4f,
                0f, archH
            )

            // Left edge down to bottom-left
            close()
        }

        return Outline.Generic(path)
    }
}

/**
 * Islamic 8-Pointed Star / Octagonal Cartouche Shape
 */
class IslamicEightPointStarShape(
    private val cornerInset: Dp = 8.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val inset = with(density) { cornerInset.toPx() }
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(inset, 0f)
            lineTo(w - inset, 0f)
            lineTo(w, inset)
            lineTo(w, h - inset)
            lineTo(w - inset, h)
            lineTo(inset, h)
            lineTo(0f, h - inset)
            lineTo(0f, inset)
            close()
        }
        return Outline.Generic(path)
    }
}

// =========================================================================
// 3. CUSTOM RABITHOH SHAPES EXTENSION
// =========================================================================

@Immutable
data class RabithohShapes(
    val cardRounded: CornerBasedShape = RoundedCornerShape(16.dp),
    val cardSubtle: CornerBasedShape = RoundedCornerShape(12.dp),
    val cardCutCorner: CornerBasedShape = CutCornerShape(12.dp),
    val chipShape: CornerBasedShape = RoundedCornerShape(20.dp),
    val buttonShape: CornerBasedShape = RoundedCornerShape(12.dp),
    val tasbihBeadShape: CornerBasedShape = RoundedCornerShape(50),
    val dialogShape: CornerBasedShape = RoundedCornerShape(20.dp),
    val bottomSheetShape: CornerBasedShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    val islamicArch: Shape = IslamicArchShape(),
    val islamicStar: Shape = IslamicEightPointStarShape()
)

val LocalRabithohShapes = staticCompositionLocalOf { RabithohShapes() }
