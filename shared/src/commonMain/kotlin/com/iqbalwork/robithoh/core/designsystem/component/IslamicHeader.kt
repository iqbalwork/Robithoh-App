package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

/**
 * IslamicHeader: Top app bar featuring subtle gold ornamental borders,
 * title, optional Arabic calligraphy or subtitle, back navigation, and action buttons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    arabicTitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    showBottomDivider: Boolean = true,
    elevation: Dp = 2.dp,
    containerColor: Color = Color.Unspecified
) {
    val isDark = RabithohTheme.colors.isDark
    val actualBg = if (containerColor != Color.Unspecified) {
        containerColor
    } else {
        if (isDark) DarkSurface else PutihBersih
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = actualBg,
        shadowElevation = elevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else SlateCharcoalText
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!subtitle.isNullOrBlank()) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isDark) DarkMuted else SlateMuted,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Text(
                                text = "‹",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MerahMerdeka,
                                    fontSize = 32.sp
                                )
                            )
                        }
                    }
                },
                actions = {
                    if (!arabicTitle.isNullOrBlank()) {
                        Text(
                            text = arabicTitle,
                            style = RabithohTheme.typography.arabicMedium.copy(
                                color = EmasKhidmat,
                                fontSize = 18.sp,
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    actions()
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = if (isDark) PutihBersih else SlateCharcoalText,
                    navigationIconContentColor = MerahMerdeka,
                    actionIconContentColor = EmasKhidmat
                )
            )

            if (showBottomDivider) {
                // Subtle Gold-Crimson Gradient line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.5.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    EmasKhidmat.copy(alpha = 0.6f),
                                    MerahMerdeka.copy(alpha = 0.4f),
                                    EmasKhidmat.copy(alpha = 0.6f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}
