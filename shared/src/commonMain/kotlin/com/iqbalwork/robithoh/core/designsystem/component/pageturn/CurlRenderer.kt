package com.iqbalwork.robithoh.core.designsystem.component.pageturn

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Tier B Renderer: Renders a realistic cylindrical paper book curl by slicing an [ImageBitmap]
 * into adaptive vertical strips, projecting their geometry via [PageTurnMath], and applying
 * dynamic shadows, back-face dimming, and specular ridge lighting.
 */
object CurlRenderer {

    /**
     * Draws the curled page onto the [DrawScope].
     *
     * @param image The pre-rendered mushaf page bitmap.
     * @param progress Normalized page turn progress from 0.0 (unturned) to 1.0 (turn complete).
     * @param spineSide Which edge the page is bound to ([SpineSide.LEFT] or [SpineSide.RIGHT]).
     * @param paperColor Background physical paper color filling the full page.
     * @param imageRect Bounding rectangle of the Quran image within the full page canvas.
     * @param stripCount Number of vertical strips. Defaults to adaptive count from [PageTurnMath.stripCountFor].
     */
    fun draw(
        drawScope: DrawScope,
        image: ImageBitmap,
        progress: Float,
        spineSide: SpineSide = SpineSide.LEFT,
        paperColor: Color = Color(0xFFFBF9F4),
        imageRect: Rect? = null,
        stripCount: Int = PageTurnMath.stripCountFor(drawScope.size.width)
    ) {
        val p = progress.coerceIn(0f, 1f)
        val canvasW = drawScope.size.width
        val canvasH = drawScope.size.height
        if (canvasW <= 0f || canvasH <= 0f) return

        val imgRect = imageRect ?: Rect(0f, 0f, canvasW, canvasH)

        // At rest, draw flat paper substrate and image directly with zero slicing overhead
        if (p <= 0.001f) {
            drawScope.drawRect(
                color = paperColor,
                topLeft = Offset.Zero,
                size = Size(canvasW, canvasH)
            )
            drawScope.drawImage(
                image = image,
                dstOffset = IntOffset(imgRect.left.roundToInt(), imgRect.top.roundToInt()),
                dstSize = IntSize(imgRect.width.roundToInt(), imgRect.height.roundToInt())
            )
            return
        }

        val fold = PageTurnMath.foldPosition(canvasW, p)
        val radius = PageTurnMath.curlRadius(canvasW, p)

        // 1. Dynamic Cast Shadow onto underlying page
        drawCastShadow(drawScope, canvasW, canvasH, fold, radius, p, spineSide)

        // 2. Vertical strip slicing and cylindrical projection
        val stripW = canvasW / stripCount

        // Determine drawing order for painter's algorithm:
        // SpineSide.LEFT: Flat part is at 0..fold (low index), curl folds over towards 0 (high index).
        // Iterate ascending so curled top-layer strips draw on top of flat ones.
        // SpineSide.RIGHT: Flat part is at fold..canvasW (high index), curl folds over towards canvasW (low index).
        // Iterate descending so curled top-layer strips draw on top of flat ones.
        val indices = if (spineSide == SpineSide.LEFT) {
            0 until stripCount
        } else {
            (stripCount - 1) downTo 0
        }

        for (i in indices) {
            val x0 = i * stripW
            val x1 = (i + 1) * stripW
            val xMid = (x0 + x1) / 2f

            val proj0 = PageTurnMath.project(x0, canvasW, p, spineSide)
            val proj1 = PageTurnMath.project(x1, canvasW, p, spineSide)
            val projMid = PageTurnMath.project(xMid, canvasW, p, spineSide)

            val screenLeft = min(proj0.screenX, proj1.screenX)
            val screenRight = max(proj0.screenX, proj1.screenX) + PageTurnMath.SEAM_OVERLAP_PX
            val stripDestW = screenRight - screenLeft

            val scaleY = projMid.verticalScale
            val stripDestH = canvasH * scaleY
            val stripTop = (canvasH - stripDestH) / 2f

            // Layer 3: Physical paper substrate for each strip across full page height
            drawScope.drawRect(
                color = paperColor,
                topLeft = Offset(screenLeft, stripTop),
                size = Size(stripDestW, stripDestH)
            )

            // Layer 1: Quran Image Calligraphy slice (if strip overlaps imageRect)
            val overlapX0 = max(x0, imgRect.left)
            val overlapX1 = min(x1, imgRect.right)
            if (overlapX1 > overlapX0 && imgRect.width > 0f && imgRect.height > 0f) {
                val srcX = (((overlapX0 - imgRect.left) / imgRect.width) * image.width)
                    .roundToInt().coerceIn(0, image.width - 1)
                val srcXEnd = (((overlapX1 - imgRect.left) / imgRect.width) * image.width)
                    .roundToInt().coerceIn(srcX + 1, image.width)
                val srcWidth = srcXEnd - srcX

                val fracX0 = (overlapX0 - x0) / stripW
                val fracX1 = (overlapX1 - x0) / stripW
                val imgSliceLeft = screenLeft + fracX0 * stripDestW
                val imgSliceW = (fracX1 - fracX0) * stripDestW

                val fracY0 = imgRect.top / canvasH
                val fracH = imgRect.height / canvasH
                val imgSliceTop = stripTop + fracY0 * stripDestH
                val imgSliceH = fracH * stripDestH

                drawScope.drawImage(
                    image = image,
                    srcOffset = IntOffset(srcX, 0),
                    srcSize = IntSize(srcWidth, image.height),
                    dstOffset = IntOffset(imgSliceLeft.roundToInt(), imgSliceTop.roundToInt()),
                    dstSize = IntSize(max(1, imgSliceW.roundToInt()), max(1, imgSliceH.roundToInt()))
                )
            }

            // Lighting & Shading pass
            if (projMid.isBackFace) {
                // Dimmed translucent back face past 90 degrees
                drawScope.drawRect(
                    color = Color.Black.copy(alpha = 0.45f),
                    topLeft = Offset(screenLeft, stripTop),
                    size = Size(stripDestW, stripDestH)
                )
            } else if (projMid.shadeFactor < 0.99f) {
                // Ambient curvature shadow on front face
                val shadowAlpha = (1f - max(0f, projMid.shadeFactor)) * 0.28f
                if (shadowAlpha > 0.01f) {
                    drawScope.drawRect(
                        color = Color.Black.copy(alpha = shadowAlpha),
                        topLeft = Offset(screenLeft, stripTop),
                        size = Size(stripDestW, stripDestH)
                    )
                }
            }
        }

        // 3. Specular highlight band along the apex of the curl ridge
        drawSpecularHighlight(drawScope, canvasW, canvasH, fold, radius, p, spineSide)
    }

    private fun drawCastShadow(
        drawScope: DrawScope,
        canvasW: Float,
        canvasH: Float,
        fold: Float,
        radius: Float,
        progress: Float,
        spineSide: SpineSide
    ) {
        val shadowAlpha = 0.32f * (1f - progress)
        if (shadowAlpha <= 0.01f) return

        val shadowWidth = radius * 1.8f
        if (spineSide == SpineSide.LEFT) {
            val startX = fold
            val endX = min(canvasW, fold + shadowWidth)
            if (endX > startX) {
                drawScope.drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = shadowAlpha),
                            Color.Black.copy(alpha = shadowAlpha * 0.35f),
                            Color.Transparent
                        ),
                        startX = startX,
                        endX = endX
                    ),
                    topLeft = Offset(startX, 0f),
                    size = Size(endX - startX, canvasH)
                )
            }
        } else {
            val startX = max(0f, (canvasW - fold) - shadowWidth)
            val endX = canvasW - fold
            if (endX > startX) {
                drawScope.drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = shadowAlpha * 0.35f),
                            Color.Black.copy(alpha = shadowAlpha)
                        ),
                        startX = startX,
                        endX = endX
                    ),
                    topLeft = Offset(startX, 0f),
                    size = Size(endX - startX, canvasH)
                )
            }
        }
    }

    private fun drawSpecularHighlight(
        drawScope: DrawScope,
        canvasW: Float,
        canvasH: Float,
        fold: Float,
        radius: Float,
        progress: Float,
        spineSide: SpineSide
    ) {
        if (progress <= 0.02f || progress >= 0.98f) return

        val apexX = if (spineSide == SpineSide.LEFT) {
            fold + radius
        } else {
            (canvasW - fold) - radius
        }
        val highlightHalfW = radius * 0.28f
        val startX = (apexX - highlightHalfW).coerceAtLeast(0f)
        val endX = (apexX + highlightHalfW).coerceAtMost(canvasW)
        if (endX <= startX) return

        val highlightAlpha = 0.28f * (1f - progress * 0.5f)
        drawScope.drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0f),
                    Color.White.copy(alpha = highlightAlpha),
                    Color.White.copy(alpha = 0f)
                ),
                startX = startX,
                endX = endX
            ),
            topLeft = Offset(startX, 0f),
            size = Size(endX - startX, canvasH)
        )
    }
}

/**
 * Extension function on [DrawScope] providing direct access to [CurlRenderer.draw].
 */
fun DrawScope.drawPageCurl(
    image: ImageBitmap,
    progress: Float,
    spineSide: SpineSide = SpineSide.LEFT,
    paperColor: Color = Color(0xFFFBF9F4),
    imageRect: Rect? = null,
    stripCount: Int = PageTurnMath.stripCountFor(size.width)
) {
    CurlRenderer.draw(
        drawScope = this,
        image = image,
        progress = progress,
        spineSide = spineSide,
        paperColor = paperColor,
        imageRect = imageRect,
        stripCount = stripCount
    )
}
