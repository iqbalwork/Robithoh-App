package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.rememberShareTextAction
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.manaqib.model.DoaSpiritualItem

@Composable
fun DoaManaqibScreen(
    doaList: List<DoaSpiritualItem>,
    selectedDoa: DoaSpiritualItem?,
    onSelectDoa: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    var isOptionsSheetOpen by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val shareAction = rememberShareTextAction()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Quick Selector Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                doaList.forEach { doa ->
                    val isSelected = doa.id == selectedDoa?.id
                    val bgColor = if (isSelected) {
                        if (isDark) MerahMarunGelap else MerahMerdeka
                    } else {
                        if (isDark) DarkSurfaceVariant else Color(0xFFF1F3F5)
                    }
                    val textColor = if (isSelected) PutihBersih else (if (isDark) DarkMuted else SlateMuted)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .clickable { onSelectDoa(doa.id) }
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = doa.title.replace("Doa ", "").replace("Salam & ", ""),
                            color = textColor,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Active Doa Display
        if (selectedDoa != null) {
            item {
                SelectionContainer {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                        contentPadding = PaddingValues(16.dp),
                        onClick = { isOptionsSheetOpen = true }
                    ) {
                        Text(
                            text = selectedDoa.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmasMuda,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedDoa.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PutihBersih,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                IslamicDivider(motif = IslamicDividerMotif.RUB_EL_HIZB)
            }

            // Arabic text
            item {
                SelectionContainer {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.SURFACE_CLEAN,
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        Text(
                            text = selectedDoa.arabicText,
                            style = RabithohTheme.typography.arabicLarge.copy(
                                fontSize = 22.sp,
                                lineHeight = 42.sp,
                                color = if (isDark) PutihBersih else SlateCharcoalText,
                                textAlign = TextAlign.Right
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Latin & Translation
            item {
                SelectionContainer {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.GOLD_BORDER,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(
                            text = "Transliterasi Latin",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDark) EmasMuda else MerahMarunGelap
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedDoa.latinText,
                            fontSize = 13.5.sp,
                            lineHeight = 20.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = if (isDark) PutihBersih else TextCharcoal
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Terjemahan Bahasa Indonesia",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MerahMerdeka
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedDoa.indonesianTranslation,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = if (isDark) PutihBersih.copy(alpha = 0.95f) else TextCharcoal
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Terjemahan Basa Sunda",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDark) EmasMuda else MerahMarunGelap
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedDoa.sundaneseTranslation,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = if (isDark) PutihBersih.copy(alpha = 0.95f) else TextCharcoal
                        )
                    }
                }
            }

            // Fadhilah
            item {
                SelectionContainer {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.GOLD_TINTED,
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Text(
                            text = "Kautamaan & Fadhilah",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDark) EmasMuda else MerahMarunGelap
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedDoa.fadhilah,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = if (isDark) PutihBersih else SlateCharcoalText
                        )
                    }
                }
            }
        }
    }

    if (isOptionsSheetOpen && selectedDoa != null) {
        val shareText = remember(selectedDoa) {
            buildString {
                append("${selectedDoa.title} - ${selectedDoa.subtitle}")
                append("\n\n")
                append(selectedDoa.arabicText)
                if (selectedDoa.latinText.isNotBlank()) {
                    append("\n\n")
                    append(selectedDoa.latinText)
                }
                if (selectedDoa.indonesianTranslation.isNotBlank()) {
                    append("\n\n[Terjemahan]\n")
                    append(selectedDoa.indonesianTranslation)
                }
                if (selectedDoa.sundaneseTranslation.isNotBlank()) {
                    append("\n\n[Basa Sunda]\n")
                    append(selectedDoa.sundaneseTranslation)
                }
                if (selectedDoa.fadhilah.isNotBlank()) {
                    append("\n\nKeutamaan: ")
                    append(selectedDoa.fadhilah)
                }
                append("\n\n(Doa & Khidmah Manaqib TQN Pondok Pesantren Sirnarasa)")
            }
        }

        ContentItemOptionsSheet(
            title = selectedDoa.title,
            subtitle = selectedDoa.subtitle,
            onDismiss = { isOptionsSheetOpen = false },
            onCopy = { clipboardManager.setText(AnnotatedString(shareText)) },
            copyLabel = "Salin Doa",
            onShare = { shareAction(shareText) },
            shareLabel = "Bagikan Doa"
        )
    }
}
