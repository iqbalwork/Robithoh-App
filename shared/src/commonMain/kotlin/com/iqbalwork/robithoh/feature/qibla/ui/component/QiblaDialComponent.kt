package com.iqbalwork.robithoh.feature.qibla.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.EmasTua
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Interactive, high-precision Islamic Qibla Compass Dial.
 * Renders an 8-petaled Islamic mandala rosette, 360-degree precision ring, cardinal directions,
 * North Pointer, and Kaaba Pointer needle with gold-accented styling.
 */
@Composable
fun QiblaDialComponent(
    deviceHeading: Float,
    qiblaAzimuth: Double,
    isAligned: Boolean,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val textMeasurer = rememberTextMeasurer()

    // Smooth angle animation
    val animatedHeading by animateFloatAsState(
        targetValue = deviceHeading,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.85f),
        label = "compassHeading"
    )

    Box(
        modifier = modifier
            .padding(16.dp)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f

            // 1. Draw Islamic Mandala Outer Petal Rosette
            drawIslamicMandalaPetals(
                center = center,
                radius = radius,
                isDark = isDark,
                isAligned = isAligned
            )

            // 2. Draw Main Outer Dial Body
            val dialRadius = radius * 0.82f
            val dialBgColor = if (isDark) Color(0xFF262121) else Color(0xFF383533)
            val innerRingColor = if (isDark) Color(0xFF1E1A1A) else Color(0xFF2B2826)

            // Outer dial drop shadow ring
            drawCircle(
                color = if (isAligned) EmasKhidmat.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.25f),
                radius = dialRadius + 4.dp.toPx(),
                center = center
            )

            // Outer dial border (Gold / Crimson)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        dialBgColor,
                        if (isAligned) EmasKhidmat else Color(0xFF4A4542)
                    ),
                    center = center,
                    radius = dialRadius
                ),
                radius = dialRadius,
                center = center
            )

            // Inner dark dial face
            drawCircle(
                color = innerRingColor,
                radius = dialRadius * 0.94f,
                center = center
            )

            // 3. Rotating Compass Dial Elements (Ticks, Numbers, Cardinals)
            // Rotates with -animatedHeading so North always stays aligned with physical world
            rotate(-animatedHeading, pivot = center) {
                // Draw 360 Degree Ticks & Numbers
                drawCompassTicksAndNumbers(
                    center = center,
                    radius = dialRadius * 0.88f,
                    isDark = isDark,
                    textMeasurer = textMeasurer
                )

                // Draw Cardinal Directions: U, T, S, B
                drawCardinalLetters(
                    center = center,
                    radius = dialRadius * 0.68f,
                    textMeasurer = textMeasurer
                )

                // Draw Ka'bah Needle pointing to qiblaAzimuth
                drawKaabaNeedle(
                    center = center,
                    radius = dialRadius * 0.80f,
                    azimuth = qiblaAzimuth.toFloat(),
                    isAligned = isAligned
                )
            }

            // 4. Fixed Device Top Pointer (Heading line at 0 degrees relative to phone screen)
            drawDeviceNorthPointer(
                center = center,
                radius = dialRadius * 0.82f,
                isAligned = isAligned
            )

            // 5. Center Pivot Cap (Gold & Crimson Jewel)
            drawCenterPivot(
                center = center,
                isAligned = isAligned
            )
        }
    }
}

/**
 * Draws 8-pointed Islamic geometric mandala rosette around the compass ring.
 */
private fun DrawScope.drawIslamicMandalaPetals(
    center: Offset,
    radius: Float,
    isDark: Boolean,
    isAligned: Boolean
) {
    val petalCount = 8
    val outerRadius = radius * 0.98f
    val innerRadius = radius * 0.80f
    val petalColor = if (isAligned) {
        EmasKhidmat.copy(alpha = 0.25f)
    } else {
        if (isDark) Color(0xFF332929).copy(alpha = 0.6f) else Color(0xFFE8DFD8).copy(alpha = 0.85f)
    }
    val petalStrokeColor = if (isAligned) EmasKhidmat.copy(alpha = 0.6f) else Color(0xFFD4C8BE).copy(alpha = 0.5f)

    val path = Path()
    for (i in 0 until petalCount) {
        val angleDeg = i * (360f / petalCount)
        val rad1 = (angleDeg - 22.5) * (PI / 180.0)
        val radTip = angleDeg * (PI / 180.0)
        val rad2 = (angleDeg + 22.5) * (PI / 180.0)

        val pStart = Offset(
            (center.x + innerRadius * cos(rad1)).toFloat(),
            (center.y + innerRadius * sin(rad1)).toFloat()
        )
        val pTip = Offset(
            (center.x + outerRadius * cos(radTip)).toFloat(),
            (center.y + outerRadius * sin(radTip)).toFloat()
        )
        val pEnd = Offset(
            (center.x + innerRadius * cos(rad2)).toFloat(),
            (center.y + innerRadius * sin(rad2)).toFloat()
        )

        if (i == 0) {
            path.moveTo(pStart.x, pStart.y)
        } else {
            path.lineTo(pStart.x, pStart.y)
        }
        path.quadraticTo(pTip.x, pTip.y, pEnd.x, pEnd.y)
    }
    path.close()

    drawPath(path = path, color = petalColor, style = Fill)
    drawPath(path = path, color = petalStrokeColor, style = Stroke(width = 1.5.dp.toPx()))
}

/**
 * Draws precision degree tick marks (every 2 degrees, prominent every 20 degrees with numbers).
 */
private fun DrawScope.drawCompassTicksAndNumbers(
    center: Offset,
    radius: Float,
    isDark: Boolean,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val tickColor = Color.White.copy(alpha = 0.85f)
    val subtleTickColor = Color.White.copy(alpha = 0.35f)

    for (deg in 0 until 360 step 2) {
        val isMajor20 = (deg % 20 == 0)
        val isMedium10 = (deg % 10 == 0)

        val tickLength = when {
            isMajor20 -> 10.dp.toPx()
            isMedium10 -> 6.dp.toPx()
            else -> 3.dp.toPx()
        }

        val strokeWidth = when {
            isMajor20 -> 1.8.dp.toPx()
            isMedium10 -> 1.2.dp.toPx()
            else -> 0.8.dp.toPx()
        }

        val rad = (deg - 90) * (PI / 180.0)
        val startX = (center.x + (radius - tickLength) * cos(rad)).toFloat()
        val startY = (center.y + (radius - tickLength) * sin(rad)).toFloat()
        val endX = (center.x + radius * cos(rad)).toFloat()
        val endY = (center.y + radius * sin(rad)).toFloat()

        drawLine(
            color = if (isMajor20) tickColor else subtleTickColor,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Draw degree text numbers every 20 degrees
        if (isMajor20) {
            val textRad = (deg - 90) * (PI / 180.0)
            val textDist = radius - tickLength - 9.dp.toPx()
            val textX = (center.x + textDist * cos(textRad)).toFloat()
            val textY = (center.y + textDist * sin(textRad)).toFloat()

            val textLayout = textMeasurer.measure(
                text = AnnotatedString(deg.toString()),
                style = TextStyle(
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    textX - textLayout.size.width / 2f,
                    textY - textLayout.size.height / 2f
                )
            )
        }
    }
}

/**
 * Draws Cardinal Direction Letters: U (Utara/North), T (Timur/East), S (Selatan/South), B (Barat/West).
 */
private fun DrawScope.drawCardinalLetters(
    center: Offset,
    radius: Float,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val cardinals = listOf(
        Pair("U", 0f),   // Utara (North)
        Pair("T", 90f),  // Timur (East)
        Pair("S", 180f), // Selatan (South)
        Pair("B", 270f)  // Barat (West)
    )

    cardinals.forEach { (label, angle) ->
        val rad = (angle - 90) * (PI / 180.0)
        val letterDist = radius * 0.95f
        val posX = (center.x + letterDist * cos(rad)).toFloat()
        val posY = (center.y + letterDist * sin(rad)).toFloat()

        val isNorth = (label == "U")
        val textColor = if (isNorth) MerahMerdeka else Color.White

        val textLayout = textMeasurer.measure(
            text = AnnotatedString(label),
            style = TextStyle(
                color = textColor,
                fontSize = if (isNorth) 17.sp else 15.sp,
                fontWeight = FontWeight.Bold
            )
        )

        drawText(
            textLayoutResult = textLayout,
            topLeft = Offset(
                posX - textLayout.size.width / 2f,
                posY - textLayout.size.height / 2f
            )
        )
    }
}

/**
 * Draws the Kaaba pointer needle on the compass face pointing to the exact Qibla Azimuth.
 */
private fun DrawScope.drawKaabaNeedle(
    center: Offset,
    radius: Float,
    azimuth: Float,
    isAligned: Boolean
) {
    rotate(azimuth, pivot = center) {
        val needleLength = radius * 0.85f
        val needleWidth = 6.dp.toPx()

        // 1. Golden Needle Arm
        val needlePath = Path().apply {
            moveTo(center.x - needleWidth / 2f, center.y)
            lineTo(center.x, center.y - needleLength)
            lineTo(center.x + needleWidth / 2f, center.y)
            close()
        }

        // Needle glow when aligned
        if (isAligned) {
            drawPath(
                path = needlePath,
                brush = Brush.radialGradient(
                    colors = listOf(EmasMuda, EmasKhidmat),
                    center = center
                )
            )
        } else {
            drawPath(
                path = needlePath,
                brush = Brush.linearGradient(
                    colors = listOf(EmasKhidmat, EmasTua),
                    start = Offset(center.x, center.y),
                    end = Offset(center.x, center.y - needleLength)
                )
            )
        }

        // 2. Kaaba Miniature Badge at Needle Tip
        val kaabaCenter = Offset(center.x, center.y - needleLength)
        val badgeRadius = 14.dp.toPx()

        // Golden Badge Ring
        drawCircle(
            color = if (isAligned) EmasMuda else EmasKhidmat,
            radius = badgeRadius,
            center = kaabaCenter
        )
        drawCircle(
            color = Color(0xFF1E1A1A),
            radius = badgeRadius - 2.dp.toPx(),
            center = kaabaCenter
        )

        // Draw Ka'bah Icon inside badge
        val kaabaSize = badgeRadius * 1.1f
        val kaabaLeft = kaabaCenter.x - kaabaSize / 2f
        val kaabaTop = kaabaCenter.y - kaabaSize / 2f

        // Kaaba Cube (Dark charcoal)
        drawRect(
            color = Color(0xFF111111),
            topLeft = Offset(kaabaLeft, kaabaTop),
            size = Size(kaabaSize, kaabaSize)
        )

        // Gold Kiswah Band near top
        drawRect(
            color = EmasKhidmat,
            topLeft = Offset(kaabaLeft, kaabaTop + kaabaSize * 0.22f),
            size = Size(kaabaSize, kaabaSize * 0.16f)
        )

        // Golden Door (Bab al-Kaaba)
        drawRect(
            color = EmasMuda,
            topLeft = Offset(kaabaLeft + kaabaSize * 0.58f, kaabaTop + kaabaSize * 0.45f),
            size = Size(kaabaSize * 0.25f, kaabaSize * 0.45f)
        )
    }
}

/**
 * Draws the fixed Device Heading pointer (Top Needle) that indicates where the phone is currently aimed.
 */
private fun DrawScope.drawDeviceNorthPointer(
    center: Offset,
    radius: Float,
    isAligned: Boolean
) {
    val pointerLength = radius * 0.72f
    val pointerWidth = 5.dp.toPx()

    val path = Path().apply {
        moveTo(center.x - pointerWidth / 2f, center.y)
        lineTo(center.x, center.y - pointerLength)
        lineTo(center.x + pointerWidth / 2f, center.y)
        close()
    }

    val pointerColor = if (isAligned) Color(0xFF10B981) else Color(0xFF0D9488) // Emerald / Teal

    drawPath(
        path = path,
        color = pointerColor,
        style = Fill
    )

    // Subtle pointer outline
    drawPath(
        path = path,
        color = Color.White.copy(alpha = 0.5f),
        style = Stroke(width = 1.dp.toPx())
    )
}

/**
 * Draws the central pivot jewel of the compass.
 */
private fun DrawScope.drawCenterPivot(
    center: Offset,
    isAligned: Boolean
) {
    // Outer golden bevel
    drawCircle(
        color = if (isAligned) EmasMuda else EmasKhidmat,
        radius = 9.dp.toPx(),
        center = center
    )

    // Inner Crimson / Marun core
    drawCircle(
        color = if (isAligned) Color(0xFF10B981) else MerahMarunGelap,
        radius = 5.5.dp.toPx(),
        center = center
    )

    // Top highlight reflection
    drawCircle(
        color = Color.White.copy(alpha = 0.7f),
        radius = 2.dp.toPx(),
        center = Offset(center.x - 1.5.dp.toPx(), center.y - 1.5.dp.toPx())
    )
}
