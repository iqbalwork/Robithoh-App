package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iqbalwork.robithoh.core.designsystem.theme.*

enum class GoldCrimsonCardVariant {
    /** Clean background with subtle gold border */
    GOLD_BORDER,
    /** Clean background with subtle crimson border */
    CRIMSON_BORDER,
    /** Hero banner card with deep crimson gradient background and gold border */
    CRIMSON_HERO,
    /** Soft gold tinted card */
    GOLD_TINTED,
    /** Minimal surface card with subtle neutral outline */
    SURFACE_CLEAN
}

/**
 * GoldCrimsonCard: Premium container with subtle gold/crimson border,
 * smooth elevation, and optional click interaction.
 */
@Composable
fun GoldCrimsonCard(
    modifier: Modifier = Modifier,
    variant: GoldCrimsonCardVariant = GoldCrimsonCardVariant.GOLD_BORDER,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 2.dp,
    customBackgroundColor: Color? = null,
    customBorderColor: Color? = null,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = RabithohTheme.colors.isDark

    val (cardBg, borderStroke) = when (variant) {
        GoldCrimsonCardVariant.GOLD_BORDER -> {
            val bg = if (isDark) DarkSurface else PutihBersih
            val border = BorderStroke(1.dp, EmasKhidmat.copy(alpha = if (isDark) 0.5f else 0.4f))
            bg to border
        }
        GoldCrimsonCardVariant.CRIMSON_BORDER -> {
            val bg = if (isDark) DarkSurface else PutihBersih
            val border = BorderStroke(1.dp, MerahMerdeka.copy(alpha = if (isDark) 0.4f else 0.3f))
            bg to border
        }
        GoldCrimsonCardVariant.CRIMSON_HERO -> {
            val bg = Color.Transparent // handled by gradient brush
            val border = BorderStroke(1.5.dp, EmasKhidmat.copy(alpha = 0.7f))
            bg to border
        }
        GoldCrimsonCardVariant.GOLD_TINTED -> {
            val bg = if (isDark) Color(0xFF221F18) else Color(0xFFFFFDF5)
            val border = BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.5f))
            bg to border
        }
        GoldCrimsonCardVariant.SURFACE_CLEAN -> {
            val bg = if (isDark) DarkSurface else PutihBersih
            val border = BorderStroke(1.dp, if (isDark) DarkBorder else SlateBorder)
            bg to border
        }
    }

    val finalCardBg = customBackgroundColor ?: cardBg
    val finalBorder = if (customBorderColor != null) BorderStroke(1.dp, customBorderColor) else borderStroke

    if (variant == GoldCrimsonCardVariant.CRIMSON_HERO) {
        val heroGradient = Brush.linearGradient(
            colors = listOf(
                MerahMarunGelap,
                MerahMerdeka,
                MerahMarunGelap
            )
        )
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
            shape = shape,
            shadowElevation = elevation,
            border = finalBorder,
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(heroGradient)
                    .padding(contentPadding)
            ) {
                Column(content = content)
            }
        }
    } else {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
            shape = shape,
            border = finalBorder,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            colors = CardDefaults.cardColors(
                containerColor = finalCardBg,
                contentColor = if (isDark) PutihBersih else SlateCharcoalText
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content
            )
        }
    }
}
