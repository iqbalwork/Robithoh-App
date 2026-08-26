package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.quran.model.Ayah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    viewModel: QuranViewModel,
    surahNumber: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val isDark = RabithohTheme.colors.isDark

    LaunchedEffect(surahNumber) {
        if (state.currentSurah?.number != surahNumber) {
            viewModel.onIntent(QuranUiIntent.SelectSurah(surahNumber))
        }
    }

    val surah = state.currentSurah
    val fontScale = state.fontScale

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            IslamicHeader(
                title = surah?.nameLatin ?: "Surah $surahNumber",
                subtitle = "${surah?.indonesianMeaning ?: ""} • ${surah?.numberOfAyahs ?: 0} Ayat",
                arabicTitle = surah?.nameArabic,
                onBackClick = onBackClick,
                actions = {
                    if (surah?.audioUrl != null) {
                        IconButton(
                            onClick = {
                                viewModel.onIntent(
                                    QuranUiIntent.PlayAudio(
                                        AudioTrack(
                                            id = "surah_${surah.number}",
                                            title = "Murottal Surah ${surah.nameLatin}",
                                            subtitle = "Al-Qur'an 30 Juz",
                                            urlOrPath = surah.audioUrl
                                        )
                                    )
                                )
                            }
                        ) {
                            Text("▶", color = EmasKhidmat, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        },
        bottomBar = {
            Column {
                // Persistent Mini Audio Bar
                MiniFloatingAudioBar(
                    track = state.activeAudioTrack,
                    playbackState = state.audioPlaybackState,
                    currentPositionMs = state.audioPositionMs,
                    durationMs = state.audioDurationMs,
                    onPlayPauseClick = { viewModel.onIntent(QuranUiIntent.TogglePlayPauseAudio) },
                    onBarClick = {},
                    onCloseClick = { viewModel.onIntent(QuranUiIntent.StopAudio) }
                )

                // Quick font scale controller
                Surface(
                    color = if (isDark) DarkSurface else PutihBersih,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Ukuran Huruf:", fontSize = 12.sp, color = if (isDark) DarkMuted else SlateMuted)
                            Button(
                                onClick = { viewModel.onIntent(QuranUiIntent.UpdateFontScale(fontScale - 0.15f)) },
                                modifier = Modifier.size(32.dp),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) DarkSurfaceVariant else Color(0xFFE4E4E7)
                                )
                            ) {
                                Text("A-", fontSize = 12.sp, color = if (isDark) PutihBersih else SlateCharcoalText)
                            }
                            Text(
                                "${(fontScale * 100).toInt()}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmasKhidmat
                            )
                            Button(
                                onClick = { viewModel.onIntent(QuranUiIntent.UpdateFontScale(fontScale + 0.15f)) },
                                modifier = Modifier.size(32.dp),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) DarkSurfaceVariant else Color(0xFFE4E4E7)
                                )
                            ) {
                                Text("A+", fontSize = 12.sp, color = if (isDark) PutihBersih else SlateCharcoalText)
                            }
                        }

                        Text(
                            text = "${surah?.revelationType?.label ?: ""}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MerahMerdeka
                        )
                    }
                }
            }
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
        ) {
            // Surah Header Banner
            item {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = surah?.nameArabic ?: "",
                        style = RabithohTheme.typography.arabicLarge.copy(
                            color = EmasMuda,
                            fontSize = 26.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Surah ${surah?.nameLatin ?: ""} • ${surah?.indonesianMeaning ?: ""}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = PutihBersih,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Basmalah (kecuali At-Taubah)
            if (surahNumber != 9) {
                item {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.GOLD_BORDER,
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            style = RabithohTheme.typography.arabicMedium.copy(
                                color = if (isDark) EmasMuda else MerahMarunGelap,
                                fontSize = (22 * fontScale).sp,
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

            // Ayahs list
            items(state.currentAyahs, key = { "${it.surahNumber}_${it.numberInSurah}" }) { ayah ->
                AyahItemCard(
                    ayah = ayah,
                    surahName = surah?.nameLatin ?: "Surah $surahNumber",
                    fontScale = fontScale,
                    onBookmark = {
                        viewModel.onIntent(
                            QuranUiIntent.SaveBookmark(
                                surahNumber = ayah.surahNumber,
                                ayahNumber = ayah.numberInSurah,
                                surahName = surah?.nameLatin ?: "Surah $surahNumber"
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AyahItemCard(
    ayah: Ayah,
    surahName: String,
    fontScale: Float,
    onBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.SURFACE_CLEAN,
        contentPadding = PaddingValues(16.dp)
    ) {
        // Top Toolbar in Ayah Card (Ayah Number & Bookmark button)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (isDark) DarkSurfaceVariant else Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${ayah.numberInSurah}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MerahMerdeka
                )
            }

            TextButton(
                onClick = onBookmark,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "🔖 Tandai Bacaan",
                    fontSize = 11.sp,
                    color = EmasKhidmat,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Arabic text
        Text(
            text = ayah.textArabic,
            style = RabithohTheme.typography.arabicLarge.copy(
                fontSize = (24 * fontScale).sp,
                lineHeight = (44 * fontScale).sp,
                color = if (isDark) PutihBersih else SlateCharcoalText,
                textAlign = TextAlign.Right
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (ayah.transliterationLatin.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = ayah.transliterationLatin,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = EmasKhidmat,
                    fontSize = (13 * fontScale).sp,
                    lineHeight = (18 * fontScale).sp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Indonesian Translation
        Text(
            text = ayah.translationIndonesian,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText,
                fontSize = (13 * fontScale).sp,
                lineHeight = (20 * fontScale).sp
            )
        )

        if (ayah.translationSundanese.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Basa Sunda: ${ayah.translationSundanese}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (isDark) DarkMuted else SlateMuted,
                    fontSize = (12 * fontScale).sp,
                    lineHeight = (18 * fontScale).sp
                )
            )
        }
    }
}
