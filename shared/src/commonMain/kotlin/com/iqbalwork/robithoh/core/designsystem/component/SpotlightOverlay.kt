package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted

/**
 * Shape of the spotlight cutout for a given target.
 */
enum class SpotlightShapeType {
    ROUNDED_RECT,
    CIRCLE
}

/**
 * Metadata for a single spotlight tutorial step.
 */
data class SpotlightStep(
    val id: String,
    val title: String,
    val description: String,
    val shapeType: SpotlightShapeType = SpotlightShapeType.ROUNDED_RECT,
    val cornerRadius: Dp = 16.dp,
    val padding: Dp = 8.dp
)

/**
 * Holds active state, target bounds, and navigation controls for the spotlight tour.
 */
@Stable
class SpotlightState(
    val steps: List<SpotlightStep>,
    private val onComplete: () -> Unit = {}
) {
    var isVisible by mutableStateOf(false)
    var currentStepIndex by mutableStateOf(0)
    val targets = mutableStateMapOf<String, Rect>()

    val currentStep: SpotlightStep?
        get() = steps.getOrNull(currentStepIndex)

    val currentTargetRect: Rect?
        get() = currentStep?.let { targets[it.id] }

    val hasNext: Boolean
        get() = currentStepIndex < steps.size - 1

    fun next() {
        if (hasNext) {
            currentStepIndex++
        } else {
            dismiss()
        }
    }

    fun previous() {
        if (currentStepIndex > 0) {
            currentStepIndex--
        }
    }

    fun dismiss() {
        isVisible = false
        onComplete()
    }

    fun start() {
        if (steps.isNotEmpty()) {
            currentStepIndex = 0
            isVisible = true
        }
    }

    fun registerTarget(stepId: String, rect: Rect) {
        if (targets[stepId] != rect) {
            targets[stepId] = rect
        }
    }
}

@Composable
fun rememberSpotlightState(
    steps: List<SpotlightStep>,
    onComplete: () -> Unit = {}
): SpotlightState {
    return remember(steps) {
        SpotlightState(steps = steps, onComplete = onComplete)
    }
}

/**
 * Modifier extension to register an anchor target for a specific spotlight step.
 */
fun Modifier.spotlightAnchor(
    state: SpotlightState,
    stepId: String
): Modifier = this.onGloballyPositioned { coordinates: LayoutCoordinates ->
    if (coordinates.isAttached) {
        val rect = coordinates.boundsInRoot()
        state.registerTarget(stepId, rect)
    }
}

/**
 * Fullscreen Spotlight Overlay component.
 * Renders a dark scrim with a cutout hole highlighting the target element,
 * decorated with a golden glow ring and an interactive coach mark card.
 */
@Composable
fun SpotlightOverlay(
    state: SpotlightState,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.isVisible && state.currentStep != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val currentStep = state.currentStep ?: return@AnimatedVisibility
        val targetRect = state.currentTargetRect
        val isDark = RabithohTheme.colors.isDark
        val density = LocalDensity.current

        // Smooth animations for moving the spotlight hole between elements
        val animatedLeft by animateFloatAsState(
            targetValue = targetRect?.left ?: 0f,
            animationSpec = spring(dampingRatio = 0.85f, stiffness = 400f),
            label = "spotlight_left"
        )
        val animatedTop by animateFloatAsState(
            targetValue = targetRect?.top ?: 0f,
            animationSpec = spring(dampingRatio = 0.85f, stiffness = 400f),
            label = "spotlight_top"
        )
        val animatedRight by animateFloatAsState(
            targetValue = targetRect?.right ?: 0f,
            animationSpec = spring(dampingRatio = 0.85f, stiffness = 400f),
            label = "spotlight_right"
        )
        val animatedBottom by animateFloatAsState(
            targetValue = targetRect?.bottom ?: 0f,
            animationSpec = spring(dampingRatio = 0.85f, stiffness = 400f),
            label = "spotlight_bottom"
        )

        val activeRect = remember(animatedLeft, animatedTop, animatedRight, animatedBottom, targetRect) {
            if (targetRect == null) null
            else Rect(animatedLeft, animatedTop, animatedRight, animatedBottom)
        }

        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // Swallow background taps to prevent accidental triggers
                }
        ) {
            val screenHeight = maxHeight

            // 1. Cutout Scrim Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
            ) {
                // Background dark scrim
                drawRect(color = Color.Black.copy(alpha = 0.74f))

                if (activeRect != null && activeRect.width > 0f && activeRect.height > 0f) {
                    val paddingPx = with(density) { currentStep.padding.toPx() }
                    val expanded = Rect(
                        left = activeRect.left - paddingPx,
                        top = activeRect.top - paddingPx,
                        right = activeRect.right + paddingPx,
                        bottom = activeRect.bottom + paddingPx
                    )

                    when (currentStep.shapeType) {
                        SpotlightShapeType.CIRCLE -> {
                            val radius = maxOf(expanded.width, expanded.height) / 2f
                            val center = expanded.center
                            drawCircle(
                                color = Color.Transparent,
                                radius = radius,
                                center = center,
                                blendMode = BlendMode.Clear
                            )
                            drawCircle(
                                color = EmasKhidmat,
                                radius = radius,
                                center = center,
                                style = Stroke(width = 2.5.dp.toPx())
                            )
                        }
                        SpotlightShapeType.ROUNDED_RECT -> {
                            val cornerPx = with(density) { currentStep.cornerRadius.toPx() }
                            drawRoundRect(
                                color = Color.Transparent,
                                topLeft = expanded.topLeft,
                                size = expanded.size,
                                cornerRadius = CornerRadius(cornerPx, cornerPx),
                                blendMode = BlendMode.Clear
                            )
                            drawRoundRect(
                                color = EmasKhidmat,
                                topLeft = expanded.topLeft,
                                size = expanded.size,
                                cornerRadius = CornerRadius(cornerPx, cornerPx),
                                style = Stroke(width = 2.5.dp.toPx())
                            )
                        }
                    }
                }
            }

            // 2. Coach Mark Tooltip Card
            // Decide whether to place card above or below target
            val targetCenterY = activeRect?.center?.y ?: 0f
            val targetBottom = activeRect?.bottom ?: 0f
            val targetTop = activeRect?.top ?: 0f
            val screenHeightPx = with(density) { screenHeight.toPx() }

            val placeBelow = targetCenterY < (screenHeightPx * 0.55f)

            val cardPaddingPx = with(density) { 16.dp.toPx() }
            val estimatedCardTopPx = if (placeBelow) {
                (targetBottom + with(density) { currentStep.padding.toPx() } + cardPaddingPx)
                    .coerceAtMost(screenHeightPx - with(density) { 260.dp.toPx() })
            } else {
                (targetTop - with(density) { currentStep.padding.toPx() } - with(density) { 230.dp.toPx() })
                    .coerceAtLeast(with(density) { 40.dp.toPx() })
            }

            val cardTopDp = with(density) { estimatedCardTopPx.toDp() }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = cardTopDp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) DarkSurface else Color.White
                    ),
                    border = BorderStroke(1.5.dp, EmasKhidmat.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Header Row: Step badge & Counter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = MerahMerdeka.copy(alpha = if (isDark) 0.25f else 0.12f),
                                border = BorderStroke(1.dp, MerahMerdeka.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "LANGKAH ${state.currentStepIndex + 1} DARI ${state.steps.size}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) EmasMuda else MerahMarunGelap,
                                    letterSpacing = 0.8.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            // Dot Indicators
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                state.steps.indices.forEach { index ->
                                    val isCurrent = index == state.currentStepIndex
                                    Box(
                                        modifier = Modifier
                                            .size(if (isCurrent) 8.dp else 6.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isCurrent) MerahMerdeka
                                                else (if (isDark) Color(0xFF4A4040) else Color(0xFFD1D5DB))
                                            )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Title
                        Text(
                            text = currentStep.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else TextCharcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Description
                        Text(
                            text = currentStep.description,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = if (isDark) Color(0xFFD1D5DB) else TextMuted
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Actions: Skip / Previous / Next
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { state.dismiss() },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Lewati",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDark) Color(0xFFA1A1AA) else Color(0xFF6B7280)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (state.currentStepIndex > 0) {
                                    OutlinedButton(
                                        onClick = { state.previous() },
                                        shape = RoundedCornerShape(100.dp),
                                        border = BorderStroke(1.dp, if (isDark) Color(0xFF4A4040) else Color(0xFFE5E7EB)),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "Sebelumnya",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isDark) PutihBersih else TextCharcoal
                                        )
                                    }
                                }

                                Button(
                                    onClick = { state.next() },
                                    shape = RoundedCornerShape(100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MerahMerdeka,
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (state.hasNext) "Lanjut" else "Mulai Membaca",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
