package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme

/**
 * ScrollToTopButton: Floating action button that smoothly scrolls back to the top of a page.
 */
@Composable
fun ScrollToTopButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Ke Atas"
) {
    val haptic = getHapticFeedback()

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        val isDark = RabithohTheme.colors.isDark

        Surface(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    haptic.performClick()
                    onClick()
                },
            shape = CircleShape,
            color = MerahMerdeka,
            border = BorderStroke(1.5.dp, EmasKhidmat.copy(alpha = if (isDark) 0.8f else 0.6f)),
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            listOf(MerahMerdeka, MerahMarunGelap)
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "▲",
                    color = EmasMuda,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    color = EmasMuda,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Extension function to determine if ScrollToTopButton should be visible for LazyListState.
 */
fun LazyListState.shouldShowScrollToTop(minItemIndex: Int = 0, minOffset: Int = 150): Boolean {
    return firstVisibleItemIndex > minItemIndex || (firstVisibleItemIndex == minItemIndex && firstVisibleItemScrollOffset > minOffset)
}

/**
 * Extension function to determine if ScrollToTopButton should be visible for ScrollState.
 */
fun ScrollState.shouldShowScrollToTop(minScrollValue: Int = 300): Boolean {
    return value > minScrollValue
}
