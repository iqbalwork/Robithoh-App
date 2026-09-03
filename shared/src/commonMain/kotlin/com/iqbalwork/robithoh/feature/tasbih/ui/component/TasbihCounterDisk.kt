package com.iqbalwork.robithoh.feature.tasbih.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import kotlinx.coroutines.launch

@Composable
fun TasbihCounterDisk(
    currentCount: Int,
    targetCount: Int,
    isMilestone: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    diskSize: Dp = 260.dp,
    isCompact: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()
    val scaleAnim = remember { Animatable(1f) }
    val glowAlphaAnim = remember { Animatable(if (isMilestone) 0.5f else 0.22f) }
    val glowScaleAnim = remember { Animatable(1f) }

    val progressFraction = if (targetCount > 0) {
        val countInLap = currentCount % targetCount
        if (countInLap == 0 && currentCount > 0) 1f  // full circle on milestone
        else (countInLap.toFloat() / targetCount.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val diskBorderColor = if (isMilestone) EmasKhidmat else MerahMerdeka.copy(alpha = 0.85f)
    val glowColor = if (isMilestone) EmasKhidmat else MerahMerdeka

    // Outer Box holding the unclipped ambient/tap glow and the inner disk
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // 1. Dynamic Outer Radial Glow (Web-like glow halo)
        Canvas(
            modifier = Modifier
                .size(diskSize + if (isCompact) 32.dp else 56.dp)
                .scale(glowScaleAnim.value)
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = glowAlphaAnim.value),
                        glowColor.copy(alpha = glowAlphaAnim.value * 0.45f),
                        Color.Transparent
                    )
                )
            )
        }

        // 2. Main Interactive Disk Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(diskSize)
                .scale(scaleAnim.value)
                .clip(CircleShape)
                .shadow(
                    elevation = if (isMilestone) 20.dp else 12.dp,
                    shape = CircleShape,
                    spotColor = glowColor,
                    ambientColor = glowColor
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF38070C),
                            Color(0xFF1E0406),
                            Color(0xFF0F0405)
                        )
                    )
                )
                .border(
                    width = if (isMilestone) 3.5.dp else 2.5.dp,
                    color = diskBorderColor,
                    shape = CircleShape
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            coroutineScope.launch {
                                launch {
                                    scaleAnim.animateTo(
                                        targetValue = 0.93f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                                launch {
                                    glowAlphaAnim.animateTo(0.85f, androidx.compose.animation.core.tween(70))
                                }
                                launch {
                                    glowScaleAnim.animateTo(1.15f, androidx.compose.animation.core.tween(70))
                                }
                            }
                            tryAwaitRelease()
                            coroutineScope.launch {
                                launch {
                                    scaleAnim.animateTo(
                                        targetValue = 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                                launch {
                                    glowAlphaAnim.animateTo(
                                        targetValue = if (isMilestone) 0.5f else 0.22f,
                                        animationSpec = androidx.compose.animation.core.tween(300)
                                    )
                                }
                                launch {
                                    glowScaleAnim.animateTo(
                                        targetValue = 1f,
                                        animationSpec = androidx.compose.animation.core.tween(300)
                                    )
                                }
                            }
                            onTap()
                        }
                    )
                }
        ) {
        // Outer decorative ring canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isCompact) 8.dp else 12.dp)
        ) {
            // Background track
            drawCircle(
                color = Color(0xFF3B0B10),
                style = Stroke(
                    width = if (isCompact) 6.dp.toPx() else 9.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
            // Progress active arc
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(MerahMerdeka, EmasKhidmat, MerahMerdeka)
                ),
                startAngle = -90f,
                sweepAngle = progressFraction * 360f,
                useCenter = false,
                style = Stroke(
                    width = if (isCompact) 7.dp.toPx() else 10.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        // Inner Counter Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = if (isMilestone) "🎉 TARGET TERCAPAI!" else "KETUK UNTUK MENGHITUNG",
                color = if (isMilestone) EmasMuda else SlateMuted,
                fontSize = if (isCompact) 8.sp else 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(if (isCompact) 2.dp else 4.dp))

            Text(
                text = "$currentCount",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = if (isCompact) 42.sp else 64.sp,
                    fontWeight = FontWeight.Black,
                    color = PutihBersih
                )
            )

            Spacer(modifier = Modifier.height(if (isCompact) 2.dp else 4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("🔥", fontSize = if (isCompact) 11.sp else 13.sp)
                Text(
                    text = "Target: ${targetCount}x",
                    color = EmasKhidmat,
                    fontSize = if (isCompact) 11.sp else 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Mini horizontal progress line
            Spacer(modifier = Modifier.height(if (isCompact) 4.dp else 8.dp))
            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .width(if (isCompact) 80.dp else 120.dp)
                    .height(if (isCompact) 4.dp else 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progressFraction)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(MerahMerdeka, EmasKhidmat, EmasMuda)
                            )
                        )
                )
            }
        }
    }
}
}

