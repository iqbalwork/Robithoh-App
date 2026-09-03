package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.ContentItemOptionsSheet
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.component.IslamicDivider
import com.iqbalwork.robithoh.core.designsystem.component.IslamicDividerMotif
import com.iqbalwork.robithoh.core.designsystem.rememberShareTextAction
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.feature.manaqib.model.KhotamanStep

@Composable
fun KhotamanScreen(
    steps: List<KhotamanStep>,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    var selectedStepForOptions by remember { mutableStateOf<KhotamanStep?>(null) }
    val clipboardManager = LocalClipboardManager.current
    val shareAction = rememberShareTextAction()

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
                    text = "Amalan Khotaman mingguan/dua mingguan ikhwan TQN PP Suryalaya Sirnarasa untuk pensucian jiwa dan peningkatan derajat ma'rifat.",
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
            KhotamanStepCard(
                step = step,
                onClick = { selectedStepForOptions = step }
            )
        }
    }

    selectedStepForOptions?.let { step ->
        val shareText = remember(step) {
            buildString {
                append("Langkah ${step.stepNumber}: ${step.title}")
                if (step.repeatCount.isNotBlank()) append(" (${step.repeatCount})")
                append("\n\n")
                append(step.arabicText)
                if (step.latinText.isNotBlank()) {
                    append("\n\n")
                    append(step.latinText)
                }
                if (step.translation.isNotBlank()) {
                    append("\n\n[Terjemahan]\n")
                    append(step.translation)
                }
                if (step.instructions.isNotBlank()) {
                    append("\n\nKaifiyat: ")
                    append(step.instructions)
                }
                append("\n\n(Panduan Khotaman TQN Pondok Pesantren Sirnarasa)")
            }
        }

        ContentItemOptionsSheet(
            title = "Langkah ${step.stepNumber}",
            subtitle = step.title,
            onDismiss = { selectedStepForOptions = null },
            onCopy = { clipboardManager.setText(AnnotatedString(shareText)) },
            copyLabel = "Salin Bacaan Khotaman",
            onShare = { shareAction(shareText) },
            shareLabel = "Bagikan Bacaan Khotaman"
        )
    }
}

@Composable
private fun KhotamanStepCard(
    step: KhotamanStep,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick
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
                color = if (isDark) PutihBersih else TextCharcoal,
                fontWeight = FontWeight.Medium,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = step.translation,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isDark) PutihBersih.copy(alpha = 0.95f) else TextCharcoal,
                fontSize = 12.5.sp,
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
