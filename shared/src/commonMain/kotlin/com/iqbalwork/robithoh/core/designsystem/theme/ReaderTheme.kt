package com.iqbalwork.robithoh.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Standard reading background themes designed for optimum typography contrast (WCAG compliant)
 * and eye comfort across various lighting conditions.
 */
enum class ReaderTheme(
    val id: String,
    val label: String,
    val swatchColor: Color,
    val backgroundColor: Color,
    val surfaceColor: Color,
    val cardBackgroundColor: Color,
    val cardBorderColor: Color,
    val arabicTextColor: Color,
    val primaryTextColor: Color,
    val secondaryTextColor: Color,
    val isDark: Boolean
) {
    WHITE(
        id = "white",
        label = "Putih",
        swatchColor = Color(0xFFFFFFFF),
        backgroundColor = Color(0xFFF8F9FA),
        surfaceColor = Color(0xFFFFFFFF),
        cardBackgroundColor = Color(0xFFFFFFFF),
        cardBorderColor = Color(0xFFE5E7EB),
        arabicTextColor = Color(0xFF1A1D20),
        primaryTextColor = Color(0xFF1E2124),
        secondaryTextColor = Color(0xFF64748B),
        isDark = false
    ),
    SEPIA(
        id = "sepia",
        label = "Sepia",
        swatchColor = Color(0xFFFBF0D9),
        backgroundColor = Color(0xFFFBF0D9),
        surfaceColor = Color(0xFFF4ECD8),
        cardBackgroundColor = Color(0xFFF4ECD8),
        cardBorderColor = Color(0xFFE6D7BA),
        arabicTextColor = Color(0xFF2C2523),
        primaryTextColor = Color(0xFF3E342F),
        secondaryTextColor = Color(0xFF7C6E65),
        isDark = false
    ),
    KHAKI(
        id = "khaki",
        label = "Khaki",
        swatchColor = Color(0xFFF5F2E8),
        backgroundColor = Color(0xFFF5F2E8),
        surfaceColor = Color(0xFFEDE8DB),
        cardBackgroundColor = Color(0xFFEDE8DB),
        cardBorderColor = Color(0xFFDFD7C4),
        arabicTextColor = Color(0xFF1E2022),
        primaryTextColor = Color(0xFF2C2523),
        secondaryTextColor = Color(0xFF6B655B),
        isDark = false
    ),
    DARK(
        id = "dark",
        label = "Gelap",
        swatchColor = Color(0xFF1E1A1A),
        backgroundColor = Color(0xFF141212),
        surfaceColor = Color(0xFF1E1A1A),
        cardBackgroundColor = Color(0xFF1E1A1A),
        cardBorderColor = Color(0xFF3E3636),
        arabicTextColor = Color(0xFFF8FAFC),
        primaryTextColor = Color(0xFFF1F5F9),
        secondaryTextColor = Color(0xFFA1A1AA),
        isDark = true
    );

    companion object {
        fun fromId(id: String?): ReaderTheme {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: WHITE
        }
    }
}
