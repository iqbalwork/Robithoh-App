package com.iqbalwork.robithoh.feature.quran.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.PageTurnState
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.PageTurnTier
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.drawPageCurl
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.rigidPageFlip
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.feature.quran.model.AyahBlock
import com.iqbalwork.robithoh.feature.quran.model.QuranPageMapping
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun MushafPageView(
    pageNumber: Int,
    pageMapping: QuranPageMapping?,
    pageImage: ImageBitmap?,
    selectedAyah: Pair<Int, Int>?,
    activeAudioAyah: Pair<Int, Int>?,
    onAyahClick: (surah: Int, ayah: Int) -> Unit,
    onBackgroundClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDoubleTap: (() -> Unit)? = null,
    onPinchOut: (() -> Unit)? = null,
    onPinchIn: (() -> Unit)? = null,
    pageTurnState: PageTurnState? = null
) {
    val isDark = RabithohTheme.colors.isDark
    val bgTheme = if (isDark) DarkCanvas else Color(0xFFFBF9F4) // Warm cream mushaf paper tint in light mode

    // Pulsing alpha for active audio recitation
    val infiniteTransition = rememberInfiniteTransition(label = "mushaf_audio_pulse")
    val audioPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.20f,
        targetValue = 0.40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "audio_alpha"
    )

    val isActivelyCurling = pageTurnState != null && pageTurnState.progress > 0.001f
    val effectiveBg = if (isActivelyCurling) Color.Transparent else bgTheme

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(effectiveBg),
        contentAlignment = Alignment.TopCenter
    ) {
        val displayW = constraints.maxWidth.toFloat()
        val displayH = constraints.maxHeight.toFloat()
        val isLandscape = displayW > displayH

        val viewportW = (pageMapping?.viewportWidth ?: 1080).toFloat()
        val viewportH = (pageMapping?.viewportHeight ?: 1745).toFloat()

        // In landscape, scale to fit the entire width so Arabic typography is large and easy to read.
        // In portrait, scale to fit both width & height so the entire page is visible.
        val scale = if (isLandscape) {
            displayW / viewportW
        } else {
            min(displayW / viewportW, displayH / viewportH)
        }
        val renderedW = viewportW * scale
        val renderedH = viewportH * scale
        val offsetX = if (isLandscape) 0f else (displayW - renderedW) / 2f
        val offsetY = if (isLandscape) 0f else (displayH - renderedH) / 2f

        val scrollState = rememberScrollState()

        // Reset scroll position when page changes
        LaunchedEffect(pageNumber) {
            if (isLandscape) {
                scrollState.scrollTo(0)
            }
        }

        // Touch and gesture container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isLandscape) Modifier.verticalScroll(scrollState)
                    else Modifier
                )
                // Pinch detection (Pinch out -> Landscape, Pinch in -> Portrait)
                .pointerInput(onPinchIn, onPinchOut) {
                    awaitEachGesture {
                        var triggered = false
                        var initialDistance = -1f
                        do {
                            val event = awaitPointerEvent()
                            val activePointers = event.changes.filter { it.pressed }
                            if (activePointers.size >= 2) {
                                val p1 = activePointers[0].position
                                val p2 = activePointers[1].position
                                val dist = hypot(p1.x - p2.x, p1.y - p2.y)
                                if (initialDistance < 0f) {
                                    initialDistance = dist
                                } else if (!triggered && initialDistance > 20f) {
                                    val ratio = dist / initialDistance
                                    if (ratio > 1.25f) {
                                        triggered = true
                                        onPinchOut?.invoke()
                                    } else if (ratio < 0.78f) {
                                        triggered = true
                                        onPinchIn?.invoke()
                                    }
                                }
                            } else {
                                initialDistance = -1f
                            }
                        } while (event.changes.any { it.pressed })
                    }
                }
                // Tap, Double tap & Long press detection
                .pointerInput(pageMapping, scale, offsetX, offsetY, isLandscape) {
                    detectTapGestures(
                        onDoubleTap = {
                            onDoubleTap?.invoke()
                        },
                        onLongPress = { tapOffset ->
                            val currentScrollY = if (isLandscape) scrollState.value else 0
                            val localX = tapOffset.x - offsetX
                            val localY = (tapOffset.y + currentScrollY) - offsetY

                            // Long press on ayah selects and highlights it
                            if (localX >= 0 && localX <= renderedW && localY >= 0 && localY <= renderedH) {
                                val unscaledX = (localX / scale).roundToInt()
                                val unscaledY = (localY / scale).roundToInt()

                                val matchedBlock = pageMapping?.data?.find { block ->
                                    unscaledY in block.top..(block.top + block.height) &&
                                    unscaledX in block.left..(block.left + block.width)
                                }

                                if (matchedBlock != null) {
                                    try {
                                        getHapticFeedback().performClick()
                                    } catch (_: Throwable) {}
                                    onAyahClick(matchedBlock.surah, matchedBlock.ayah)
                                }
                            }
                        },
                        onTap = {
                            // Tap doang hanya untuk show/hide top bar & controls
                            onBackgroundClick()
                        }
                    )
                }
        ) {
            val density = androidx.compose.ui.platform.LocalDensity.current
            val imgWidthDp = with(density) { renderedW.toDp() }
            val imgHeightDp = with(density) { renderedH.toDp() }

            Box(
                modifier = if (isLandscape) {
                    Modifier.size(imgWidthDp, imgHeightDp)
                } else {
                    Modifier.fillMaxSize()
                }
            ) {
                val isTurning = pageTurnState != null && pageTurnState.progress > 0.001f

                // -----------------------------------------------------------------
                // LAYER 3: Background Layer (Physical Paper Sheet)
                // When flat, renders the authentic mushaf paper background.
                // When transitioning (curling), paper substrate is drawn per-strip.
                // -----------------------------------------------------------------
                if (!isTurning) {
                    Box(
                        modifier = if (isLandscape) {
                            Modifier.size(imgWidthDp, imgHeightDp)
                        } else {
                            Modifier
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .size(imgWidthDp, imgHeightDp)
                        }
                            .background(bgTheme)
                    )
                }

                // -----------------------------------------------------------------
                // LAYER 2: Second Layer (Marker when Long Tap of Ayah / Audio Recitation)
                // Renders highlighter marker on paper, underneath the calligraphy ink.
                // -----------------------------------------------------------------
                val targetHighlight = if (isTurning) null else (activeAudioAyah ?: selectedAyah)
                if (targetHighlight != null && pageMapping != null) {
                    val (surah, ayah) = targetHighlight
                    val matchingBlocks = pageMapping.data.filter { it.surah == surah && it.ayah == ayah }

                    if (matchingBlocks.isNotEmpty()) {
                        val isAudioHighlight = (activeAudioAyah != null)
                        val fillColor = if (isAudioHighlight) {
                            MerahMerdeka.copy(alpha = audioPulseAlpha)
                        } else {
                            EmasKhidmat.copy(alpha = 0.32f)
                        }
                        val strokeColor = if (isAudioHighlight) {
                            MerahMerdeka.copy(alpha = 0.85f)
                        } else {
                            EmasKhidmat.copy(alpha = 0.85f)
                        }

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cornerRadius = CornerRadius(6f * scale, 6f * scale)
                            for (block in matchingBlocks) {
                                val rectX = offsetX + (block.left * scale)
                                val rectY = offsetY + (block.top * scale)
                                val rectW = block.width * scale
                                val rectH = block.height * scale

                                // Marker Fill
                                drawRoundRect(
                                    color = fillColor,
                                    topLeft = Offset(rectX, rectY),
                                    size = Size(rectW, rectH),
                                    cornerRadius = cornerRadius
                                )

                                // Marker Outline
                                drawRoundRect(
                                    color = strokeColor,
                                    topLeft = Offset(rectX, rectY),
                                    size = Size(rectW, rectH),
                                    cornerRadius = cornerRadius,
                                    style = Stroke(width = 1.5f * scale)
                                )
                            }
                        }
                    }
                }

                // -----------------------------------------------------------------
                // LAYER 1: Top Layer (Quran Image Calligraphy)
                // Renders crisp Arabic calligraphy on top of the highlighter marker.
                // -----------------------------------------------------------------
                if (pageImage != null) {
                    if (isTurning && !isLandscape) {
                        when (pageTurnState.activeTier) {
                            PageTurnTier.TIER_B_CURL -> {
                                Canvas(
                                    modifier = Modifier
                                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                        .size(imgWidthDp, imgHeightDp)
                                ) {
                                    drawPageCurl(
                                        image = pageImage,
                                        progress = pageTurnState.progress,
                                        spineSide = pageTurnState.spineSide
                                    )
                                }
                            }
                            PageTurnTier.TIER_A_RIGID -> {
                                Image(
                                    bitmap = pageImage,
                                    contentDescription = "Mushaf Halaman $pageNumber",
                                    modifier = Modifier
                                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                        .size(imgWidthDp, imgHeightDp)
                                        .background(bgTheme)
                                        .rigidPageFlip(pageTurnState.progress, pageTurnState.spineSide)
                                )
                            }
                        }
                    } else {
                        Image(
                            bitmap = pageImage,
                            contentDescription = "Mushaf Halaman $pageNumber",
                            modifier = if (isLandscape) {
                                Modifier.size(imgWidthDp, imgHeightDp)
                            } else {
                                Modifier
                                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                    .size(imgWidthDp, imgHeightDp)
                            }
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = MerahMerdeka,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Memuat Halaman $pageNumber...",
                                color = SlateMuted,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
