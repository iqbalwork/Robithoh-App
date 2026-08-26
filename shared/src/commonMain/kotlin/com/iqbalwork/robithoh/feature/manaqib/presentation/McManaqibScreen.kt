package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.manaqib.model.McProgramItem

@Composable
fun McManaqibScreen(
    programs: List<McProgramItem>,
    selectedLanguage: LiturgyLanguage,
    onLanguageSelected: (LiturgyLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val availableLanguages = listOf(LiturgyLanguage.INDONESIAN, LiturgyLanguage.SUNDANESE)
    val activeLang = if (selectedLanguage == LiturgyLanguage.ARABIC) LiturgyLanguage.INDONESIAN else selectedLanguage

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        item {
            LanguageTabSwitch(
                selectedLanguage = activeLang,
                onLanguageSelected = onLanguageSelected,
                languages = availableLanguages,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            GoldCrimsonCard(
                variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = if (activeLang == LiturgyLanguage.SUNDANESE) "Runtuyan Acara Manaqib (Basa Sunda)" else "Susunan Acara Manaqib (Bahasa Indonesia)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmasMuda
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Panduan lengkap protokol / pembawa acara pembacaan Kitab Manaqib TQN.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = PutihBersih,
                        fontSize = 12.sp
                    )
                )
            }
        }

        items(programs, key = { it.stepNumber }) { item ->
            McProgramCard(item = item, language = activeLang)
        }
    }
}

@Composable
private fun McProgramCard(
    item: McProgramItem,
    language: LiturgyLanguage,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val title = if (language == LiturgyLanguage.SUNDANESE) item.titleSu else item.titleId
    val protocol = if (language == LiturgyLanguage.SUNDANESE) item.protocolSu else item.protocolId

    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        contentPadding = PaddingValues(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Step Number Badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isDark) MerahMarunGelap else MerahMerdeka),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${item.stepNumber}",
                    color = PutihBersih,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        fontSize = 14.sp
                    )
                )

                Text(
                    text = "Petugas: ${item.officerRole}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = EmasKhidmat,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                if (item.arabicIntro.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.arabicIntro,
                        style = RabithohTheme.typography.arabicSmall.copy(
                            color = if (isDark) EmasMuda else MerahMarunGelap,
                            fontSize = 13.sp,
                            lineHeight = 22.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = protocol,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}
