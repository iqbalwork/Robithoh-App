package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka

enum class IslamicDividerMotif(val symbol: String) {
    CRESCENT_STAR("☪"),
    RUB_EL_HIZB("۞"),
    ARABESQUE_DIAMOND("❖"),
    FLORAL_KNOT("✦"),
    CLEAN_LINE("")
}

/**
 * IslamicDivider: Ornamental divider featuring gradient gold/crimson lines
 * with a center Islamic motif (Crescent & Star, Rub el Hizb, or Arabesque Diamond).
 */
@Composable
fun IslamicDivider(
    modifier: Modifier = Modifier,
    motif: IslamicDividerMotif = IslamicDividerMotif.RUB_EL_HIZB,
    color: Color = EmasKhidmat,
    accentColor: Color = MerahMerdeka,
    thickness: Dp = 1.dp,
    verticalPadding: Dp = 12.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Left gradient line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(thickness)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            color.copy(alpha = 0.3f),
                            color.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        if (motif != IslamicDividerMotif.CLEAN_LINE) {
            Spacer(modifier = Modifier.width(8.dp))

            // Center Ornamental Motif
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = motif.symbol,
                    color = color,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        // Right gradient line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(thickness)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            color.copy(alpha = 0.8f),
                            color.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
