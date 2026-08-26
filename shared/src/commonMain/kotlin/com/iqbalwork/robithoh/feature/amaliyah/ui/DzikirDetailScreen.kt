package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.amaliyah.model.DzikirItem
import com.iqbalwork.robithoh.feature.amaliyah.model.DzikirType
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiState

@Composable
fun DzikirDetailScreen(
    state: AmaliyahUiState,
    onIntent: (AmaliyahUiIntent) -> Unit,
    onOpenTasbih: (Int, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val currentDzikirList = if (state.activeDzikirType == DzikirType.JAHR) {
        state.dzikirJahrList
    } else {
        state.dzikirKhofiList
    }

    Scaffold(
        topBar = {
            IslamicHeader(
                title = if (state.activeDzikirType == DzikirType.JAHR) "Dzikir Jahr (165x)" else "Dzikir Khofi (Ismu Dzat)",
                subtitle = "TQN Pondok Pesantren Sirnarasa Silsilah 38",
                arabicTitle = if (state.activeDzikirType == DzikirType.JAHR) "ذِكْرُ الْجَهْرِ" else "ذِكْرُ الْخَفِيِّ",
                onBackClick = onBack
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Type Switcher Tabs (Jahr vs Khofi)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isDark) DarkSurfaceVariant else Color(0xFFE9ECEF))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DzikirType.entries.forEach { type ->
                        val isSelected = type == state.activeDzikirType
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MerahMerdeka else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onIntent(AmaliyahUiIntent.SelectDzikirType(type)) }
                        ) {
                            Text(
                                text = type.label,
                                color = if (isSelected) PutihBersih else (if (isDark) Color.LightGray else Color.DarkGray),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // 3-Language Toggle
            item {
                LanguageTabSwitch(
                    selectedLanguage = state.selectedLanguage,
                    onLanguageSelected = { onIntent(AmaliyahUiIntent.SelectLanguage(it)) }
                )
            }

            // Introduction Banner Card
            item {
                GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_TINTED) {
                    Text(
                        text = if (state.activeDzikirType == DzikirType.JAHR) {
                            "Kaifiyat Dzikir Jahr Ba'da Sholat Maktubah"
                        } else {
                            "Kaifiyat Dzikir Khofi & Rabithah Mursyid 38"
                        },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MerahMerdeka
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (state.activeDzikirType == DzikirType.JAHR) {
                            "Dzikir Jahr TQN diamalkan bersuara nyaring dengan menggelengkan kepala dari lambung kanan ke kiri (lathifah qolbi) sebanyak 165 kali setelah sholat fardhu."
                        } else {
                            "Dzikir Khofi menggetarkan Ismu Dzat (ALLAH) di lathifah qolbi (bawah dada kiri) tanpa suara dan tanpa nafas, dengan rabithah kepada Syekh Mursyid Abah Aos Ra. Qs."
                        },
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else SlateMuted,
                        lineHeight = 17.sp
                    )
                }
            }

            // Dzikir Liturgical Items
            items(currentDzikirList, key = { it.id }) { item ->
                DzikirLiturgicalCard(
                    item = item,
                    selectedLanguage = state.selectedLanguage,
                    isDark = isDark,
                    onOpenTasbih = onOpenTasbih
                )
            }

            // Islamic End Decoration
            item {
                IslamicDivider(
                    motif = IslamicDividerMotif.RUB_EL_HIZB,
                    color = EmasKhidmat,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun DzikirLiturgicalCard(
    item: DzikirItem,
    selectedLanguage: LiturgyLanguage,
    isDark: Boolean,
    onOpenTasbih: (Int, String) -> Unit
) {
    GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_BORDER) {
        // Card Top Header: Number, Title, Repetition Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MerahMerdeka,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${item.number}",
                            color = PutihBersih,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isDark) PutihBersih else SlateCharcoalText
                )
            }

            if (item.repetitionCount > 1) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmasKhidmat.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, EmasKhidmat)
                ) {
                    Text(
                        text = "${item.repetitionCount}x",
                        color = if (isDark) EmasMuda else MerahMarunGelap,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Arabic Text
        Text(
            text = item.arabicText,
            style = RabithohTheme.typography.arabicLarge.copy(
                fontSize = 21.sp,
                lineHeight = 36.sp,
                textAlign = TextAlign.End,
                color = if (isDark) PutihBersih else Color(0xFF1E2022)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Latin Transliteration
        Text(
            text = item.latinText,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontStyle = FontStyle.Italic,
                color = MerahMerdeka,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Translation based on active switch
        val translationText = when (selectedLanguage) {
            LiturgyLanguage.ARABIC -> item.indonesianText // fallback to Indo if Arab selected for translation
            LiturgyLanguage.INDONESIAN -> item.indonesianText
            LiturgyLanguage.SUNDANESE -> item.sundaneseText
        }

        val langBadge = when (selectedLanguage) {
            LiturgyLanguage.SUNDANESE -> "Basa Sunda"
            else -> "Terjemahan"
        }

        Text(
            text = "[$langBadge] $translationText",
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isDark) Color(0xFFB0B0B8) else Color(0xFF4A4A52),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        )

        // Notes & Kaifiyat
        if (item.kaifiyatNote.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = if (isDark) Color(0xFF232328) else Color(0xFFF7F7F8),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Kaifiyat: ${item.kaifiyatNote}",
                    fontSize = 11.sp,
                    color = if (isDark) DarkMuted else SlateMuted,
                    modifier = Modifier.padding(8.dp),
                    lineHeight = 15.sp
                )
            }
        }

        // Quick Launch to Tasbih Button (if repetition > 1)
        if (item.repetitionCount > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = { onOpenTasbih(item.repetitionCount, item.title) },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, EmasKhidmat),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isDark) EmasMuda else MerahMerdeka
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "📿 Hitung dengan Tasbih (${item.repetitionCount}x)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
