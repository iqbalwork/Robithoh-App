package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.component.MiniFloatingAudioBar
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihAbuBackground
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateBorder
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta
import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranListScreen(
    viewModel: QuranViewModel,
    onSurahClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }
    val state by viewModel.uiState.collectAsState()
    val isDark = RabithohTheme.colors.isDark

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            IslamicHeader(
                title = "Al-Qur'an Digital",
                subtitle = "114 Surah, Shalawat & Panduan Ziarah",
                arabicTitle = "الْقُرْآنُ الْكَرِيمُ",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            // Persistent Mini Floating Audio Player
            MiniFloatingAudioBar(
                track = state.activeAudioTrack,
                playbackState = state.audioPlaybackState,
                currentPositionMs = state.audioPositionMs,
                durationMs = state.audioDurationMs,
                onPlayPauseClick = { viewModel.onIntent(QuranUiIntent.TogglePlayPauseAudio) },
                onBarClick = {},
                onCloseClick = { viewModel.onIntent(QuranUiIntent.StopAudio) }
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Horizontal Navigation Tabs
            QuranNavigationTabs(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.onIntent(QuranUiIntent.SelectTab(it)) }
            )

            when (state.selectedTab) {
                QuranTab.SURAHS -> {
                    SurahsTabContent(
                        state = state,
                        onSurahClick = { surahNumber ->
                            viewModel.onIntent(QuranUiIntent.SelectSurah(surahNumber))
                            onSurahClick(surahNumber)
                        },
                        onSearchChange = { viewModel.onIntent(QuranUiIntent.SearchSurahs(it)) }
                    )
                }
                QuranTab.SHALAWAT -> {
                    ShalawatScreen(
                        shalawatList = state.shalawatList,
                        onPlayAudio = { audioPath: String?, title: String ->
                            viewModel.onIntent(
                                QuranUiIntent.PlayAudio(
                                    AudioTrack(
                                        id = "shalawat_$audioPath",
                                        title = title,
                                        subtitle = "Lantunan Shalawat TQN",
                                        urlOrPath = audioPath ?: "bani_hasyim.mp3"
                                    )
                                )
                            )
                        }
                    )
                }
                QuranTab.ZIARAH -> {
                    ZiarahScreen(
                        sections = state.ziarahSections
                    )
                }
                QuranTab.BOOKMARKS -> {
                    BookmarksTabContent(
                        state = state,
                        onSurahClick = { surahNumber ->
                            viewModel.onIntent(QuranUiIntent.SelectSurah(surahNumber))
                            onSurahClick(surahNumber)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuranNavigationTabs(
    selectedTab: QuranTab,
    onTabSelected: (QuranTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (isDark) DarkSurface else PutihBersih)
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuranTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            val bgColor = if (isSelected) {
                if (isDark) MerahMarunGelap else MerahMerdeka
            } else {
                if (isDark) DarkSurfaceVariant else Color(0xFFF1F3F5)
            }
            val textColor = if (isSelected) PutihBersih else (if (isDark) DarkMuted else SlateMuted)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SurahsTabContent(
    state: QuranUiState,
    onSurahClick: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // Last Read Hero Card
        if (state.lastReadBookmark != null) {
            item {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                    onClick = { onSurahClick(state.lastReadBookmark.surahNumber) },
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Terakhir Dibaca",
                                fontSize = 12.sp,
                                color = EmasMuda,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Surah ${state.lastReadBookmark.surahName}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PutihBersih,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = "Ayat ke-${state.lastReadBookmark.ayahNumber}",
                                fontSize = 12.sp,
                                color = PutihBersih.copy(alpha = 0.85f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(EmasKhidmat),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("▶", color = PutihBersih, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Cari 114 Surah (cth. Yasin, Al-Mulk)...",
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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

        // Surah Items List
        items(state.surahs, key = { it.number }) { surah ->
            SurahItemCard(surah = surah, onClick = { onSurahClick(surah.number) })
        }
    }
}

@Composable
private fun SurahItemCard(
    surah: SurahMeta,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        onClick = onClick,
        contentPadding = PaddingValues(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Surah Number Diamond / Badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDark) DarkSurfaceVariant else Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${surah.number}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MerahMerdeka
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Latin Name & Indonesian Meaning
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.nameLatin,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "${surah.indonesianMeaning} • ${surah.numberOfAyahs} Ayat • ${surah.revelationType.label}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 11.sp
                    )
                )
            }

            // Arabic Name
            Text(
                text = surah.nameArabic,
                style = RabithohTheme.typography.arabicMedium.copy(
                    color = EmasKhidmat,
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Composable
private fun BookmarksTabContent(
    state: QuranUiState,
    onSurahClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            GoldCrimsonCard(
                variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "DAFTAR PENANDA & TERAKHIR DIBACA",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmasMuda
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Akses cepat ke posisi ayat Al-Qur'an yang terakhir Anda baca dan pelajari.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = PutihBersih,
                        fontSize = 12.sp
                    )
                )
            }
        }

        if (state.bookmarks.isEmpty() && state.lastReadBookmark == null) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada riwayat penanda bacaan.",
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            val allList = (listOfNotNull(state.lastReadBookmark) + state.bookmarks).distinctBy { it.surahNumber to it.ayahNumber }

            items(allList, key = { "${it.surahNumber}_${it.ayahNumber}" }) { bookmark ->
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.GOLD_BORDER,
                    onClick = { onSurahClick(bookmark.surahNumber) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Surah ${bookmark.surahName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isDark) PutihBersih else SlateCharcoalText
                            )
                            Text(
                                text = "Ayat ke-${bookmark.ayahNumber}",
                                fontSize = 12.sp,
                                color = EmasKhidmat
                            )
                        }

                        Text(
                            text = "Lanjutkan ›",
                            color = MerahMerdeka,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
