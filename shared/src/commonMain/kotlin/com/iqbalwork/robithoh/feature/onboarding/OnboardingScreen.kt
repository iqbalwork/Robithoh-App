package com.iqbalwork.robithoh.feature.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.location.rememberLocationPermissionLauncher
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 6 })

    // Permission launcher for Location & Post-Notifications (Android & iOS)
    val requestPermission = rememberLocationPermissionLauncher { _ ->
        // Proceed to main screen regardless of permission grant/deny
        onComplete()
    }

    OnboardingContent(
        pagerState = pagerState,
        onSkipClick = onComplete,
        onNextClick = {
            if (pagerState.currentPage < 5) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        },
        onGrantPermissionClick = {
            requestPermission()
        },
        onSkipPermissionClick = onComplete,
        modifier = modifier
    )
}
