package com.iqbalwork.robithoh.feature.doa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.MerahSundaBadge
import com.iqbalwork.robithoh.core.designsystem.theme.PutihAbuBackground
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.feature.doa.presentation.DoaUiState
import com.iqbalwork.robithoh.feature.reader.model.LiturgyDocument

@Composable
fun DoaListContent(
    state: DoaUiState,
    onSearchQueryChange: (String) -> Unit,
    onDocumentClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            IslamicHeader(
                title = "Doa & Ziarah",
                subtitle = "Kumpulan Doa, Istighotsah & Adab Ziarah",
                arabicTitle = "الدُّعَاءُ",
                onBackClick = onBackClick
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Input Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Cari doa, istighotsah, ziarah...",
                            color = if (isDark) DarkMuted else SlateMuted,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Text(text = "🔍", fontSize = 16.sp)
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Text(text = "✕", fontSize = 16.sp, color = MerahMerdeka)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isDark) DarkSurface else PutihBersih,
                        unfocusedContainerColor = if (isDark) DarkSurface else PutihBersih,
                        focusedBorderColor = EmasKhidmat,
                        unfocusedBorderColor = if (isDark) DarkBorder else EmasKhidmat.copy(alpha = 0.4f),
                        focusedTextColor = if (isDark) PutihBersih else SlateCharcoalText,
                        unfocusedTextColor = if (isDark) PutihBersih else SlateCharcoalText
                    )
                )
            }

            // Results Counter & Category Label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DAFTAR BACAAN DOA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkMuted else SlateMuted,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "${state.filteredDocuments.size} Doa",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MerahMerdeka
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Document Cards List
            if (state.filteredDocuments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🤲", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Doa Tidak Ditemukan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else SlateCharcoalText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coba gunakan kata kunci pencarian yang berbeda.",
                            fontSize = 13.sp,
                            color = if (isDark) DarkMuted else SlateMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = state.filteredDocuments,
                        key = { it.id }
                    ) { doc ->
                        DoaCardItem(
                            document = doc,
                            onClick = { onDocumentClick(doc.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DoaCardItem(
    document: LiturgyDocument,
    onClick: () -> Unit
) {
    val isDark = RabithohTheme.colors.isDark

    GoldCrimsonCard(
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        onClick = onClick,
        contentPadding = PaddingValues(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Badge
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(MerahMerdeka, MerahMarunGelap)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🤲",
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Info Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = document.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (!document.languageBadge.isNullOrBlank()) {
                        Surface(
                            color = MerahSundaBadge,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = document.languageBadge,
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                if (document.subtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = document.subtitle,
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else SlateMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (document.arabicTitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = document.arabicTitle,
                        style = RabithohTheme.typography.arabicSmall,
                        color = EmasKhidmat,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Arrow Chevron
            Text(
                text = "›",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkMuted else SlateMuted
            )
        }
    }
}
