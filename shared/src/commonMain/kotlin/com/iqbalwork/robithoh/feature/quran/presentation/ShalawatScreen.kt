package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.iqbalwork.robithoh.feature.quran.model.ShalawatModel

@Composable
fun ShalawatScreen(
    shalawatList: List<ShalawatModel>,
    onPlayAudio: (String?, String) -> Unit,
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
                    text = "KUMPULAN SHALAWAT TQN",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmasMuda,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lantunan Shalawat Bani Hasyim, Shalawat Badriyah & Salam Ziarah Rasulullah ﷺ penenteram kalbu dan penyambung tali robithoh.",
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

        items(shalawatList, key = { it.id }) { item ->
            ShalawatCard(item = item, onPlayAudio = { onPlayAudio(item.audioPath, item.title) })
        }
    }
}

@Composable
private fun ShalawatCard(
    item: ShalawatModel,
    onPlayAudio: () -> Unit,
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 11.sp
                    )
                )
            }

            if (item.audioPath != null) {
                IconButton(
                    onClick = onPlayAudio,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MerahMerdeka)
                ) {
                    Text("▶", color = PutihBersih, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Arabic
        Text(
            text = item.arabicText,
            style = RabithohTheme.typography.arabicMedium.copy(
                fontSize = 20.sp,
                lineHeight = 36.sp,
                color = if (isDark) PutihBersih else SlateCharcoalText,
                textAlign = TextAlign.Right
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.latinText,
            style = MaterialTheme.typography.bodySmall.copy(
                color = EmasKhidmat,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = item.indonesianTranslation,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        )

        if (item.sundaneseTranslation.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Basa Sunda: ${item.sundaneseTranslation}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (isDark) DarkMuted else SlateMuted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            color = if (isDark) DarkSurfaceVariant else Color(0xFFF1F3F5),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Fadhilah: ${item.virtue}",
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
