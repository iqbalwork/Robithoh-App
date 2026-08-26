package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.manaqib.model.KhotamanStep

@Composable
fun KhotamanScreen(
    steps: List<KhotamanStep>,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        item {
            GoldCrimsonCard(
                variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "PANDUAN KHOTAMAN TQN",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmasMuda,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Amalan Khotaman mingguan/dua mingguan ikhwan TQN Sirnarasa untuk pensucian jiwa dan peningkatan derajat ma'rifat.",
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

        items(steps, key = { it.stepNumber }) { step ->
            KhotamanStepCard(step = step)
        }
    }
}

@Composable
private fun KhotamanStepCard(
    step: KhotamanStep,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isDark) MerahMarunGelap else MerahMerdeka),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${step.stepNumber}",
                        color = PutihBersih,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        fontSize = 14.sp
                    )
                )
            }

            Surface(
                color = MerahMerdeka.copy(alpha = 0.12f),
                shape = CircleShape
            ) {
                Text(
                    text = step.repeatCount,
                    color = MerahMerdeka,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Arabic text
        Text(
            text = step.arabicText,
            style = RabithohTheme.typography.arabicMedium.copy(
                fontSize = 18.sp,
                lineHeight = 32.sp,
                color = if (isDark) PutihBersih else SlateCharcoalText,
                textAlign = TextAlign.Right
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = step.latinText,
            style = MaterialTheme.typography.bodySmall.copy(
                color = EmasKhidmat,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = step.translation,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isDark) PutihBersih.copy(alpha = 0.85f) else SlateCharcoalText,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        )

        if (step.instructions.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = if (isDark) DarkSurfaceVariant else Color(0xFFF1F3F5),
                shape = CircleShape
            ) {
                Text(
                    text = "Kaifiyat: ${step.instructions}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}
