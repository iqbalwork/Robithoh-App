package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.feature.waktal.ui.components.WaktalImageLoaderCache
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

@Composable
fun ZoomableImage(
    model: Any?,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onDragChange: (Float) -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    // Zoom & Pan State
    var scale by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    // Swipe Dismiss State
    val swipeOffsetY = remember { Animatable(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    // Async Image Bitmap loading state
    var bitmap by remember(model) { mutableStateOf<ImageBitmap?>(if (model is ImageBitmap) model else null) }
    var isLoading by remember(model) { mutableStateOf(model is String) }

    LaunchedEffect(model) {
        if (model is String && model.isNotBlank()) {
            isLoading = true
            bitmap = WaktalImageLoaderCache.loadImage(model)
            isLoading = false
        } else if (model is ImageBitmap) {
            bitmap = model
            isLoading = false
        } else {
            isLoading = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(model) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            panOffset = Offset.Zero
                        } else {
                            scale = 2.5f
                        }
                    }
                )
            }
            .pointerInput(model) {
                detectZoomAndSwipe(
                    onGesture = { _, pan, zoom, _, _, _ ->
                        val newScale = (scale * zoom).coerceIn(1f, 4f)
                        scale = newScale

                        if (scale > 1f) {
                            if (swipeOffsetY.value != 0f) {
                                scope.launch { swipeOffsetY.snapTo(0f) }
                                onDragChange(0f)
                            }

                            val maxPanningX = if (size.width > 0) (size.width * (scale - 1)) / 2 else 0f
                            val maxPanningY = if (size.height > 0) (size.height * (scale - 1)) / 2 else 0f
                            val newX = (panOffset.x + pan.x).coerceIn(-maxPanningX, maxPanningX)
                            val newY = (panOffset.y + pan.y).coerceIn(-maxPanningY, maxPanningY)
                            panOffset = Offset(newX, newY)
                        } else {
                            panOffset = Offset.Zero
                            val newSwipe = swipeOffsetY.value + pan.y
                            scope.launch { swipeOffsetY.snapTo(newSwipe.coerceAtLeast(0f)) }

                            val progress = if (size.height > 0) {
                                (newSwipe / size.height.toFloat()).coerceIn(0f, 1f)
                            } else 0f
                            onDragChange(progress)
                        }
                    },
                    onEnd = { velocity ->
                        if (scale == 1f) {
                            val swipeDist = swipeOffsetY.value
                            val threshold = if (size.height > 0) size.height * 0.15f else 200f
                            val isFling = velocity.y > 1000f

                            if (swipeDist > threshold || isFling) {
                                onDismiss()
                            } else {
                                scope.launch { swipeOffsetY.animateTo(0f) }
                                onDragChange(0f)
                            }
                        }
                    }
                )
            }
            .onSizeChanged { size = it }
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MerahMerdeka
                )
            }
        } else if (bitmap != null) {
            Image(
                bitmap = bitmap!!,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = panOffset.x
                        translationY = panOffset.y + swipeOffsetY.value
                    }
            )
        } else if (model is Painter) {
            Image(
                painter = model,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = panOffset.x
                        translationY = panOffset.y + swipeOffsetY.value
                    }
            )
        } else if (model is DrawableResource) {
            Image(
                painter = painterResource(model),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = panOffset.x
                        translationY = panOffset.y + swipeOffsetY.value
                    }
            )
        }
    }
}

/**
 * Custom gesture detector that tracks Zoom, Pan, and Velocity on gesture release for fling dismissal.
 */
private suspend fun PointerInputScope.detectZoomAndSwipe(
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float, timeMillis: Long, pointers: List<PointerInputChange>) -> Unit,
    onEnd: (velocity: Velocity) -> Unit
) {
    awaitEachGesture {
        val rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop
        val velocityTracker = VelocityTracker()

        awaitFirstDown(requireUnconsumed = false)

        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.fastAny { it.isConsumed }
            if (canceled) break

            val zoomChange = event.calculateZoom()
            val panChange = event.calculatePan()

            if (!pastTouchSlop) {
                zoom *= zoomChange
                pan += panChange

                val centroidSize = event.calculateCentroidSize(useCurrent = false)
                val zoomMotion = abs(1 - zoom) * centroidSize
                val panMotion = pan.getDistance()

                if (zoomMotion > touchSlop || panMotion > touchSlop) {
                    pastTouchSlop = true
                }
            }

            if (pastTouchSlop) {
                val centroid = event.calculateCentroid(useCurrent = false)
                if (zoomChange != 1f || panChange != Offset.Zero) {
                    onGesture(centroid, panChange, zoomChange, rotation, event.changes[0].uptimeMillis, event.changes)
                }

                event.changes.fastForEach {
                    if (it.changedToDownIgnoreConsumed()) {
                        velocityTracker.resetTracking()
                    }
                    velocityTracker.addPointerInputChange(it)
                    it.consume()
                }
            }
        } while (event.changes.fastAny { it.pressed })

        val velocity = velocityTracker.calculateVelocity()
        onEnd(velocity)
    }
}
