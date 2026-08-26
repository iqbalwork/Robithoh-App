package com.iqbalwork.robithoh.feature.manaqib.presentation

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
import com.iqbalwork.robithoh.feature.manaqib.model.SilsilahNode

@Composable
fun TawassulSilsilahScreen(
    silsilahList: List<SilsilahNode>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
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
        // Tawassul Card
        item {
            GoldCrimsonCard(
                variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "TAWASSUL TQN 38",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = EmasMuda,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "إِلَى حَضْرَةِ النَّبِيِّ الْمُصْطَفَى سَيِّدِنَا وَمَوْلَانَا مُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ، وَعَلَى آلِهِ وَأَصْحَابِهِ وَأَزْوَاجِهِ وَذُرِّيَّاتِهِ وَأَهْلِ بَيْتِهِ الْكِرَامِ، وَإِلَى أَرْوَاحِ جَمِيعِ سِلْسِلَةِ السَّادَاتِ الْقَادِرِيَّةِ وَالنَّقْشَبَنْدِيَّةِ خُصُوصًا سُلْطَانَ الْأَوْلِيَاءِ سَيِّدَنَا الشَّيْخَ عَبْدَ الْقَادِرِ الْجَيْلَانِيَّ وَسَيِّدَنَا الشَّيْخَ عَبْدَ اللَّهِ مُبَارَكْ وَسَيِّدَنَا الشَّيْخَ أَحْمَدَ صَاحِبَ الْوَفَاءِ تَاجَ الْعَارِفِينَ وَسَيِّدَنَا الشَّيْخَ مُحَمَّدَ عَبْدَ الْغَوْثِ سَيْفَ اللَّهِ مَسْلُولْ رَضِيَ اللَّهُ عَنْهُمْ أَجْمَعِينَ... الْفَاتِحَة",
                    style = RabithohTheme.typography.arabicMedium.copy(
                        color = PutihBersih,
                        fontSize = 15.sp,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            IslamicDivider(motif = IslamicDividerMotif.RUB_EL_HIZB)
        }

        // Search Silsilah
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Cari Silsilah TQN 1 s/d 38 (Nama / Urutan / Gelar)...", fontSize = 13.sp)
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmasKhidmat,
                    unfocusedBorderColor = if (isDark) DarkBorder else SlateBorder,
                    focusedContainerColor = if (isDark) DarkSurface else PutihBersih,
                    unfocusedContainerColor = if (isDark) DarkSurface else PutihBersih
                )
            )
        }

        item {
            Text(
                text = "Rantai Emas Silsilah TQN 1 s/d 38 (${silsilahList.size} Mursyid)",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) EmasMuda else MerahMarunGelap
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Silsilah Items
        items(silsilahList, key = { it.orderNumber }) { node ->
            SilsilahNodeCard(node = node)
        }
    }
}

@Composable
private fun SilsilahNodeCard(
    node: SilsilahNode,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val isHighlighted = node.orderNumber in listOf(1, 2, 11, 17, 32, 33, 34, 35, 36, 37, 38)

    val cardVariant = if (isHighlighted) {
        GoldCrimsonCardVariant.GOLD_TINTED
    } else {
        GoldCrimsonCardVariant.SURFACE_CLEAN
    }

    GoldCrimsonCard(
        modifier = modifier,
        variant = cardVariant,
        contentPadding = PaddingValues(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Order Badge
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isHighlighted) {
                            if (isDark) MerahMarunGelap else MerahMerdeka
                        } else {
                            if (isDark) DarkSurfaceVariant else Color(0xFFE4E4E7)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${node.orderNumber}",
                    color = if (isHighlighted) PutihBersih else (if (isDark) DarkMuted else SlateMuted),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                if (node.arabicName.isNotBlank()) {
                    Text(
                        text = node.arabicName,
                        style = RabithohTheme.typography.arabicSmall.copy(
                            color = EmasKhidmat,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Text(
                    text = node.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        fontSize = 14.sp
                    )
                )

                Text(
                    text = "${node.title} • ${node.locationOrEpithet}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 12.sp
                    )
                )

                if (node.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = node.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isDark) Color(0xFFB0B0B8) else Color(0xFF4B5563),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }
        }
    }
}
