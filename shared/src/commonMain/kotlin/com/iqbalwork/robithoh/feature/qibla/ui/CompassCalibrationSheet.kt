package com.iqbalwork.robithoh.feature.qibla.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Compass Calibration Bottom Sheet / Dialog presenting the figure-8 calibration gesture instruction.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompassCalibrationSheet(
    onDismiss: () -> Unit
) {
    val isDark = RabithohTheme.colors.isDark
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) Color(0xFF1E1A1A) else Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .background(Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Animated Figure-8 Calibration Vector Illustration
            Figure8CalibrationIllustration(
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )

            // Title
            Text(
                text = "Kalibrasi Kompas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else TextCharcoal,
                textAlign = TextAlign.Center
            )

            // Description
            Text(
                text = "Silahkan lakukan gerakan seperti angka 8 seperti tertera pada gambar diatas untuk meningkatkan akurasi sensor kompas perangkat Anda.",
                fontSize = 13.5.sp,
                color = if (isDark) Color(0xFFA1A1AA) else SlateMuted,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MerahMerdeka,
                    contentColor = PutihBersih
                )
            ) {
                Text(
                    text = "Ok, Saya Mengerti",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Animated Figure-8 motion graphic with hand holding smartphone and animated looping motion.
 */
@Composable
private fun Figure8CalibrationIllustration(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "figure8Animation")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val a = size.width * 0.36f // width of lemniscate lobe
        val b = size.height * 0.22f // height of lemniscate lobe

        // 1. Draw Infinity / Figure-8 Dashed Guide Track
        val path = Path()
        val steps = 120
        for (i in 0..steps) {
            val t = (i.toFloat() / steps) * 2 * PI
            // Lemniscate of Gerono: x = a * sin(t), y = b * sin(t) * cos(t) = (b/2) * sin(2t)
            // Or Lemniscate of Bernoulli:
            val scale = 2 / (3 - cos(2 * t))
            val x = (cx + a * sin(t)).toFloat()
            val y = (cy + b * sin(2 * t)).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        // Draw dotted blue/cyan motion curve
        drawPath(
            path = path,
            color = Color(0xFF0284C7).copy(alpha = 0.85f), // Blue Kiblat
            style = Stroke(
                width = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 6.dp.toPx()), 0f),
                cap = StrokeCap.Round
            )
        )

        // 2. Compute Current Moving Phone Position along the Figure-8 track
        val currentT = progress * 2 * PI
        val phoneX = (cx + a * sin(currentT)).toFloat()
        val phoneY = (cy + b * sin(2 * currentT)).toFloat()

        // Tangent angle for tilting the phone naturally along the track
        val nextT = (progress + 0.02f) * 2 * PI
        val nextX = (cx + a * sin(nextT)).toFloat()
        val nextY = (cy + b * sin(2 * nextT)).toFloat()
        val angleDeg = (atan2((nextY - phoneY).toDouble(), (nextX - phoneX).toDouble()) * (180.0 / PI)).toFloat()

        // 3. Draw Direction Arrow along the path
        val arrowT = ((progress + 0.35f) % 1.0f) * 2 * PI
        val arrowX = (cx + a * sin(arrowT)).toFloat()
        val arrowY = (cy + b * sin(2 * arrowT)).toFloat()
        drawCircle(
            color = Color(0xFF0284C7),
            radius = 4.dp.toPx(),
            center = Offset(arrowX, arrowY)
        )

        // 4. Draw Hand & Smartphone at animated position
        rotate(angleDeg * 0.4f, pivot = Offset(phoneX, phoneY)) {
            drawHandAndPhone(
                center = Offset(phoneX, phoneY),
                phoneWidth = 44.dp.toPx(),
                phoneHeight = 72.dp.toPx()
            )
        }
    }
}

/**
 * Draws stylized hand holding the smartphone device.
 */
private fun DrawScope.drawHandAndPhone(
    center: Offset,
    phoneWidth: Float,
    phoneHeight: Float
) {
    val phoneLeft = center.x - phoneWidth / 2f
    val phoneTop = center.y - phoneHeight / 2f

    // Hand Palm & Wrist (Skin tone)
    val skinColor = Color(0xFFE8B298)
    val handPath = Path().apply {
        moveTo(phoneLeft + phoneWidth * 0.2f, phoneTop + phoneHeight * 0.6f)
        cubicTo(
            phoneLeft - 20.dp.toPx(), phoneTop + phoneHeight * 0.9f,
            phoneLeft - 10.dp.toPx(), phoneTop + phoneHeight * 1.4f,
            phoneLeft + 10.dp.toPx(), phoneTop + phoneHeight * 1.5f
        )
        lineTo(phoneLeft + phoneWidth * 0.8f, phoneTop + phoneHeight * 1.5f)
        cubicTo(
            phoneLeft + phoneWidth + 10.dp.toPx(), phoneTop + phoneHeight * 1.2f,
            phoneLeft + phoneWidth * 0.9f, phoneTop + phoneHeight * 0.8f,
            phoneLeft + phoneWidth * 0.8f, phoneTop + phoneHeight * 0.6f
        )
        close()
    }
    drawPath(path = handPath, color = skinColor)

    // Smartphone Body (Dark Charcoal Bezel)
    val phoneRect = RoundRect(
        rect = androidx.compose.ui.geometry.Rect(
            left = phoneLeft,
            top = phoneTop,
            right = phoneLeft + phoneWidth,
            bottom = phoneTop + phoneHeight
        ),
        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
    )
    val phonePath = Path().apply { addRoundRect(phoneRect) }
    drawPath(path = phonePath, color = Color(0xFF262626))

    // Smartphone Screen (Dark Blue glass)
    val screenMargin = 3.dp.toPx()
    val screenRect = RoundRect(
        rect = androidx.compose.ui.geometry.Rect(
            left = phoneLeft + screenMargin,
            top = phoneTop + screenMargin * 2,
            right = phoneLeft + phoneWidth - screenMargin,
            bottom = phoneTop + phoneHeight - screenMargin * 2
        ),
        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
    )
    val screenPath = Path().apply { addRoundRect(screenRect) }
    drawPath(path = screenPath, color = Color(0xFF0F172A))

    // Smartphone screen mini compass line
    drawLine(
        color = Color(0xFF0284C7),
        start = Offset(center.x, center.y - 12.dp.toPx()),
        end = Offset(center.x, center.y + 12.dp.toPx()),
        strokeWidth = 1.5.dp.toPx(),
        cap = StrokeCap.Round
    )

    // Hand Thumb gripping the phone edge
    val thumbPath = Path().apply {
        moveTo(phoneLeft - 2.dp.toPx(), phoneTop + phoneHeight * 0.45f)
        cubicTo(
            phoneLeft + 8.dp.toPx(), phoneTop + phoneHeight * 0.45f,
            phoneLeft + 10.dp.toPx(), phoneTop + phoneHeight * 0.65f,
            phoneLeft - 2.dp.toPx(), phoneTop + phoneHeight * 0.65f
        )
        close()
    }
    drawPath(path = thumbPath, color = skinColor)
}
