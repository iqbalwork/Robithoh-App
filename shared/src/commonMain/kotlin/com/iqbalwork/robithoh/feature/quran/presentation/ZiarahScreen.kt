package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
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
import com.iqbalwork.robithoh.feature.quran.model.ZiarahSection

@Composable
fun ZiarahScreen(
    sections: List<ZiarahSection>,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    var selectedSectionForOptions by remember { mutableStateOf<ZiarahSection?>(null) }
    val clipboardManager = LocalClipboardManager.current
    val shareAction = rememberShareTextAction()

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
                    text = "Tata cara, adab batin, dan bacaan doa ziarah kubur umum serta ziarah maqam Waliyullah per kaifiyat TQN PP Suryalaya Sirnarasa.",
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
            ZiarahCard(
                section = section,
                isDark = isDark,
                onClick = { selectedSectionForOptions = section }
            )
        }
    }

    selectedSectionForOptions?.let { section ->
        val shareText = remember(section) {
            buildString {
                append("${section.title} - ${section.subtitle}")
                if (section.adabSteps.isNotEmpty()) {
                    append("\n\nAdab & Tata Cara:\n")
                    section.adabSteps.forEach { step ->
                        append("• $step\n")
                    }
                }
                if (section.arabicPrayer.isNotBlank()) {
                    append("\n")
                    append(section.arabicPrayer)
                    if (section.latinPrayer.isNotBlank()) {
                        append("\n\n")
                        append(section.latinPrayer)
                    }
                    if (section.indonesianTranslation.isNotBlank()) {
                        append("\n\n[Terjemahan]\n")
                        append(section.indonesianTranslation)
                    }
                }
                if (section.fadhilah.isNotBlank()) {
                    append("\n\nKeutamaan: ")
                    append(section.fadhilah)
                }
                append("\n\n(Panduan & Adab Ziarah TQN Pondok Pesantren Sirnarasa)")
            }
        }

        ContentItemOptionsSheet(
            title = section.title,
            subtitle = section.subtitle,
            onDismiss = { selectedSectionForOptions = null },
            onCopy = { clipboardManager.setText(AnnotatedString(shareText)) },
            copyLabel = "Salin Panduan Ziarah",
            onShare = { shareAction(shareText) },
            shareLabel = "Bagikan Panduan Ziarah"
        )
    }
}

@Composable
private fun ZiarahCard(
    section: ZiarahSection,
    isDark: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick
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
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = if (isDark) PutihBersih else TextCharcoal,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
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
