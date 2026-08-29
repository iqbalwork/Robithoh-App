package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.manaqib.model.TanbihContent

@Composable
fun TanbihScreen(
    tanbih: TanbihContent?,
    selectedLanguage: LiturgyLanguage,
    onLanguageSelected: (LiturgyLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val scrollState = rememberScrollState()

    val availableLanguages = listOf(LiturgyLanguage.INDONESIAN, LiturgyLanguage.SUNDANESE)
    val activeLang = if (selectedLanguage == LiturgyLanguage.ARABIC) LiturgyLanguage.INDONESIAN else selectedLanguage

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        LanguageTabSwitch(
            selectedLanguage = activeLang,
            onLanguageSelected = onLanguageSelected,
            languages = availableLanguages,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (tanbih != null) {
            SelectionContainer {
                Column {
                    // Header Hero
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                Text(
                    text = tanbih.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmasMuda,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = tanbih.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = PutihBersih,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Opening Arabic
            GoldCrimsonCard(
                variant = GoldCrimsonCardVariant.GOLD_BORDER,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = tanbih.openingArabic,
                    style = RabithohTheme.typography.arabicMedium.copy(
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        lineHeight = 32.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            IslamicDivider(motif = IslamicDividerMotif.RUB_EL_HIZB)

            Spacer(modifier = Modifier.height(16.dp))

            // Body Wasiat
            GoldCrimsonCard(
                variant = GoldCrimsonCardVariant.SURFACE_CLEAN,
                contentPadding = PaddingValues(20.dp)
            ) {
                val text = if (activeLang == LiturgyLanguage.SUNDANESE) tanbih.sundaneseText else tanbih.indonesianText
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 24.sp,
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Closing Arabic
            GoldCrimsonCard(
                variant = GoldCrimsonCardVariant.GOLD_BORDER,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = tanbih.closingArabic,
                    style = RabithohTheme.typography.arabicMedium.copy(
                        color = if (isDark) EmasMuda else MerahMarunGelap,
                        lineHeight = 30.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
