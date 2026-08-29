package com.iqbalwork.robithoh.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
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
import com.iqbalwork.robithoh.feature.profile.data.ProfileData
import com.iqbalwork.robithoh.feature.profile.model.InstitutionItem
import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }
    val isDark = RabithohTheme.colors.isDark
    val profile = ProfileData.sirnarasaProfile

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            IslamicHeader(
                title = "Profil Pesantren",
                subtitle = "Pondok Pesantren Sirnarasa Ciamis",
                arabicTitle = "مَعْهَدُ سِرْنَا رَاسَا",
                onBackClick = onBackClick
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground
    ) { paddingValues ->
        SelectionContainer {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
            ) {
            // Hero Card
            item {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                    contentPadding = PaddingValues(20.dp)
                ) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PutihBersih,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = profile.tagline,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = EmasMuda,
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = Color.Black.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "📍 ${profile.location}",
                            color = PutihBersih,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Guru Mursyid Card
            item {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.GOLD_TINTED,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = "Pangersa Guru Agung Mursyid TQN 38",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = profile.mursyidName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else SlateCharcoalText,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = profile.mursyidTitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = EmasKhidmat,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = profile.mursyidBiography,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            item {
                IslamicDivider(motif = IslamicDividerMotif.RUB_EL_HIZB)
            }

            // Sejarah & Visi
            item {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.SURFACE_CLEAN,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = "Sejarah & Pilar Dakwah",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else SlateCharcoalText,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = profile.historyText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            // Affiliate Institutions Title
            item {
                Text(
                    text = "Ekosistem & Lembaga Afiliasi Sirnarasa",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Affiliate items
            items(profile.institutions, key = { it.id }) { inst ->
                InstitutionCard(item = inst)
            }
        }
    }
}
}

@Composable
private fun InstitutionCard(
    item: InstitutionItem,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        contentPadding = PaddingValues(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isDark) MerahMarunGelap else MerahMerdeka),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.acronym.take(3),
                    color = PutihBersih,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else SlateCharcoalText,
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        color = MerahMerdeka.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item.roleCategory,
                            color = MerahMerdeka,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}
