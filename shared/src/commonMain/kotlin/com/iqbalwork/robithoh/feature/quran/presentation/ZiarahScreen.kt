package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.quran.model.ZiarahSection

@Composable
fun ZiarahScreen(
    sections: List<ZiarahSection>,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            GoldCrimsonCard(
                variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "PANDUAN & ADAB ZIARAH",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmasMuda,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tata cara, adab batin, dan bacaan doa ziarah kubur umum serta ziarah maqam Waliyullah per kaifiyat TQN Sirnarasa.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = PutihBersih,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            IslamicDivider(motif = IslamicDividerMotif.RUB_EL_HIZB)
        }

        items(sections, key = { it.id }) { section ->
            ZiarahCard(section = section, isDark = isDark)
        }
    }
}

@Composable
private fun ZiarahCard(
    section: ZiarahSection,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else SlateCharcoalText,
                fontSize = 15.sp
            )
        )
        Text(
            text = section.subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isDark) DarkMuted else SlateMuted,
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (section.adabSteps.isNotEmpty()) {
            Text(
                text = "Adab & Tata Cara:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = MerahMerdeka
            )
            Spacer(modifier = Modifier.height(4.dp))
            section.adabSteps.forEach { step ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("• ", color = EmasKhidmat, fontWeight = FontWeight.Bold)
                    Text(
                        text = step,
                        fontSize = 12.sp,
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (section.arabicPrayer.isNotBlank()) {
            Text(
                text = section.arabicPrayer,
                style = RabithohTheme.typography.arabicMedium.copy(
                    fontSize = 18.sp,
                    lineHeight = 32.sp,
                    color = if (isDark) PutihBersih else SlateCharcoalText,
                    textAlign = TextAlign.Right
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = section.latinPrayer,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = EmasKhidmat,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = section.indonesianTranslation,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )
        }

        if (section.fadhilah.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                color = if (isDark) DarkSurfaceVariant else Color(0xFFF1F3F5),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Keutamaan: ${section.fadhilah}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) EmasMuda else MerahMarunGelap,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
