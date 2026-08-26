package com.iqbalwork.robithoh.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * Main Rabithoh Material 3 Design System Theme.
 * Provides Merah Putih & Emas Khidmat color palettes, Islamic typography tokens,
 * custom geometric shapes, and scalable liturgy settings.
 */
@Composable
fun RabithohTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val rabithohColors = if (darkTheme) DarkRabithohColors else LightRabithohColors
    val m3Typography = getRabithohM3Typography()
    val customTypography = getRabithohCustomTypography().withScale(fontScale)
    val rabithohShapes = RabithohShapes()

    CompositionLocalProvider(
        LocalRabithohColors provides rabithohColors,
        LocalRabithohTypography provides customTypography,
        LocalRabithohShapes provides rabithohShapes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = m3Typography,
            shapes = RabithohM3Shapes,
            content = content
        )
    }
}

/**
 * Unified Accessor for Rabithoh Theme tokens
 */
object RabithohTheme {
    val colors: RabithohColors
        @Composable
        @ReadOnlyComposable
        get() = LocalRabithohColors.current

    val typography: RabithohTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalRabithohTypography.current

    val shapes: RabithohShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalRabithohShapes.current

    val m3Colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val m3Typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val m3Shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes
}
