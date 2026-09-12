package com.iqbalwork.robithoh.core.designsystem.component.pageturn

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PageTurnMathTest {

    private val testWidth = 1000f

    @Test
    fun testFoldPositionProgression() {
        // At progress 0.0, fold is at the right edge
        assertEquals(testWidth, PageTurnMath.foldPosition(testWidth, 0.0f))

        // At progress 0.5, fold is at the midpoint
        assertEquals(500f, PageTurnMath.foldPosition(testWidth, 0.5f))

        // At progress 1.0, fold is at the spine (0.0)
        assertEquals(0f, PageTurnMath.foldPosition(testWidth, 1.0f))

        // Clamping check
        assertEquals(testWidth, PageTurnMath.foldPosition(testWidth, -0.2f))
        assertEquals(0f, PageTurnMath.foldPosition(testWidth, 1.5f))
    }

    @Test
    fun testCurlRadiusDecay() {
        val initialRadius = PageTurnMath.curlRadius(testWidth, 0.0f)
        assertEquals(testWidth * PageTurnMath.INITIAL_RADIUS_FRACTION, initialRadius)

        val halfRadius = PageTurnMath.curlRadius(testWidth, 0.5f)
        assertEquals(initialRadius / 2f, halfRadius)

        // At progress 1.0, radius should decay to MIN_RADIUS_PX, preventing residual curved lobe
        val finalRadius = PageTurnMath.curlRadius(testWidth, 1.0f)
        assertEquals(PageTurnMath.MIN_RADIUS_PX, finalRadius)
    }

    @Test
    fun testStripCountAdaptiveClamping() {
        // Very small width clamps to 24
        assertEquals(24, PageTurnMath.stripCountFor(100f))

        // Typical phone width (e.g. 480px / 12 = 40)
        assertEquals(40, PageTurnMath.stripCountFor(480f))

        // Large tablet width (e.g. 1200px / 12 = 100 -> clamped to 64)
        assertEquals(64, PageTurnMath.stripCountFor(1200f))
    }

    @Test
    fun testFlatZoneProjection() {
        val progress = 0.3f
        val fold = PageTurnMath.foldPosition(testWidth, progress) // 700f

        // Sample points before the fold
        val p1 = PageTurnMath.project(0f, testWidth, progress, SpineSide.LEFT)
        assertEquals(0f, p1.screenX)
        assertEquals(1.0f, p1.shadeFactor)
        assertFalse(p1.isBackFace)
        assertEquals(1.0f, p1.verticalScale)

        val p2 = PageTurnMath.project(fold - 10f, testWidth, progress, SpineSide.LEFT)
        assertEquals(fold - 10f, p2.screenX)
        assertEquals(1.0f, p2.shadeFactor)
        assertFalse(p2.isBackFace)
    }

    @Test
    fun testCylinderWrapAndApex() {
        val progress = 0.5f
        val fold = PageTurnMath.foldPosition(testWidth, progress) // 500f
        val radius = PageTurnMath.curlRadius(testWidth, progress) // 90f

        // Apex occurs when arc = (pi / 2) * radius
        val apexArc = (kotlin.math.PI.toFloat() / 2f) * radius
        val apexX = fold + apexArc
        val apexProj = PageTurnMath.project(apexX, testWidth, progress, SpineSide.LEFT)

        // At apex, screenX should be fold + radius
        assertTrue(abs((fold + radius) - apexProj.screenX) < 0.5f)
        // Cos(pi/2) is 0.0
        assertTrue(abs(apexProj.shadeFactor) < 0.01f)
        // Apex is boundary of back-face
        assertFalse(apexProj.isBackFace)
    }

    @Test
    fun testBackFaceDetection() {
        val progress = 0.5f
        val fold = PageTurnMath.foldPosition(testWidth, progress)
        val radius = PageTurnMath.curlRadius(testWidth, progress)

        // Point well past apex into the cylinder bend
        val pastApexArc = (kotlin.math.PI.toFloat() * 0.75f) * radius
        val proj = PageTurnMath.project(fold + pastApexArc, testWidth, progress, SpineSide.LEFT)

        assertTrue(proj.isBackFace)
        assertTrue(proj.shadeFactor < 0f)
        assertTrue(proj.verticalScale < 1.0f)
    }

    @Test
    fun testRtlSymmetry() {
        val progress = 0.4f

        // Left spine fold starts at testWidth and moves left
        // Right spine fold starts at 0 and moves right
        val leftSpineFold = PageTurnMath.foldPosition(testWidth, progress) // 600f

        // An unturned point on LEFT spine at x = 200 should mirror a point on RIGHT spine at x = 800
        val leftProj = PageTurnMath.project(200f, testWidth, progress, SpineSide.LEFT)
        val rightProj = PageTurnMath.project(800f, testWidth, progress, SpineSide.RIGHT)

        assertEquals(200f, leftProj.screenX)
        assertEquals(800f, rightProj.screenX)
        assertEquals(leftProj.shadeFactor, rightProj.shadeFactor)
        assertEquals(leftProj.isBackFace, rightProj.isBackFace)

        // Test curled section symmetry
        val leftCurled = PageTurnMath.project(leftSpineFold + 50f, testWidth, progress, SpineSide.LEFT)
        val rightCurled = PageTurnMath.project((testWidth - leftSpineFold) - 50f, testWidth, progress, SpineSide.RIGHT)

        // The mirrored screen coordinate should satisfy: rightCurled.screenX == testWidth - leftCurled.screenX
        assertTrue(
            abs(rightCurled.screenX - (testWidth - leftCurled.screenX)) < 0.1f,
            "Expected right screenX ${testWidth - leftCurled.screenX} but was ${rightCurled.screenX}"
        )
        assertEquals(leftCurled.shadeFactor, rightCurled.shadeFactor)
        assertEquals(leftCurled.isBackFace, rightCurled.isBackFace)
    }

    @Test
    fun testSpreadTurnVersusIntraSpreadSlide() {
        // Intra-spread pairs (Odd <-> Even): 1 <-> 2, 3 <-> 4, 5 <-> 6 -> SLIDE (isSpreadTurn == false)
        assertFalse(PageTurnMath.isSpreadTurn(0), "Pages 1 <-> 2 must slide")
        assertFalse(PageTurnMath.isSpreadTurn(2), "Pages 3 <-> 4 must slide")
        assertFalse(PageTurnMath.isSpreadTurn(4), "Pages 5 <-> 6 must slide")
        assertFalse(PageTurnMath.isSpreadTurn(6), "Pages 7 <-> 8 must slide")

        // Inter-spread pairs (Even <-> Odd): 2 <-> 3, 4 <-> 5, 6 <-> 7 -> CURL (isSpreadTurn == true)
        assertTrue(PageTurnMath.isSpreadTurn(1), "Pages 2 <-> 3 must curl")
        assertTrue(PageTurnMath.isSpreadTurn(3), "Pages 4 <-> 5 must curl")
        assertTrue(PageTurnMath.isSpreadTurn(5), "Pages 6 <-> 7 must curl")
        assertTrue(PageTurnMath.isSpreadTurn(7), "Pages 8 <-> 9 must curl")
    }
}
