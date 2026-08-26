package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

enum class LiturgyLanguage(val label: String, val nativeLabel: String) {
    ARABIC("Arab", "العربية"),
    INDONESIAN("Indonesia", "Terjemahan"),
    SUNDANESE("Sunda", "Basa Sunda")
}

/**
 * LanguageTabSwitch: 3-way animated toggle between Arab, Terjemahan Indonesia, and Basa Sunda.
 * Used across Dzikir, Manqobah, Tanbih, and Tawassul screens.
 */
@Composable
fun LanguageTabSwitch(
    selectedLanguage: LiturgyLanguage,
    onLanguageSelected: (LiturgyLanguage) -> Unit,
    modifier: Modifier = Modifier,
    languages: List<LiturgyLanguage> = LiturgyLanguage.entries
) {
    val isDark = RabithohTheme.colors.isDark
    val containerBg = if (isDark) DarkSurfaceVariant else Color(0xFFF1F3F5)
    val containerBorder = if (isDark) DarkBorder else SlateBorder

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(containerBg)
            .border(1.dp, containerBorder, RoundedCornerShape(24.dp))
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            languages.forEach { lang ->
                val isSelected = lang == selectedLanguage

                val tabBgColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        if (isDark) MerahMarunGelap else MerahMerdeka
                    } else {
                        Color.Transparent
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "tabBg"
                )

                val tabTextColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        PutihBersih
                    } else {
                        if (isDark) DarkMuted else SlateMuted
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "tabText"
                )

                val borderStrokeColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        EmasKhidmat.copy(alpha = 0.8f)
                    } else {
                        Color.Transparent
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "tabBorder"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(tabBgColor)
                        .border(
                            width = if (isSelected) 1.dp else 0.dp,
                            color = borderStrokeColor,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onLanguageSelected(lang) }
                        )
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = lang.label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = tabTextColor,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                        Text(
                            text = lang.nativeLabel,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSelected) EmasMuda else (if (isDark) Color(0xFF71717A) else Color(0xFFA1A1AA)),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }
}
