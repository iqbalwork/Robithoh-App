package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.quran.model.Ayah
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta
import com.iqbalwork.robithoh.navigation.BackHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    viewModel: QuranViewModel,
    surahNumber: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialAyahNumber: Int? = null
) {
    val state by viewModel.uiState.collectAsState()
    val isDark = RabithohTheme.colors.isDark
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Load the surah this screen was entered with. Switching surahs afterwards (tab strip,
    // "Menuju Surat/Ayat") happens in place via jumpTo() below — it never re-enters this
    // effect or touches the nav backstack, so it behaves like a ViewPager rather than pushing
    // a new "page" per surah.
    LaunchedEffect(Unit) {
        if (state.currentSurah?.number != surahNumber) {
            viewModel.onIntent(QuranUiIntent.SelectSurah(surahNumber))
        }
    }

    val surah = state.currentSurah
    val currentSurahNumber = surah?.number ?: surahNumber
    val fontScale = state.fontScale
    var showLatin by rememberSaveable { mutableStateOf(true) }
    var showTranslation by rememberSaveable { mutableStateOf(true) }
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    var showGoToSheet by rememberSaveable { mutableStateOf(false) }
    var selectedAyahForOptions by remember { mutableStateOf<Ayah?>(null) }
    var pendingScrollAyah by rememberSaveable { mutableStateOf(initialAyahNumber) }

    BackHandler {
        if (showSettingsDialog) {
            showSettingsDialog = false
        } else if (showGoToSheet) {
            showGoToSheet = false
        } else if (selectedAyahForOptions != null) {
            selectedAyahForOptions = null
        } else {
            onBackClick()
        }
    }

    // Header items ahead of the ayah list: hero banner + divider, plus Basmalah unless At-Taubah.
    val ayahListOffset = if (currentSurahNumber != 9) 3 else 2

    // Fires on first load and every in-place surah switch (state.currentAyahs changes each
    // time), so a pending scroll target set by jumpTo() is applied once the new ayahs arrive.
    LaunchedEffect(state.currentAyahs) {
        val target = pendingScrollAyah
        if (target != null && state.currentAyahs.isNotEmpty()) {
            val index = state.currentAyahs.indexOfFirst { it.numberInSurah == target }
            listState.scrollToItem(if (index >= 0) index + ayahListOffset else 0)
            pendingScrollAyah = null
        }
    }

    // Switches to a surah/ayat without navigating: same surah just scrolls, a different
    // surah loads via the ViewModel and the effect above scrolls once its ayahs are ready.
    fun jumpTo(targetSurahNumber: Int, targetAyahNumber: Int) {
        if (targetSurahNumber == currentSurahNumber) {
            val index = state.currentAyahs.indexOfFirst { it.numberInSurah == targetAyahNumber }
            coroutineScope.launch {
                listState.animateScrollToItem(if (index >= 0) index + ayahListOffset else 0)
            }
        } else {
            pendingScrollAyah = targetAyahNumber
            viewModel.onIntent(QuranUiIntent.SelectSurah(targetSurahNumber))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
          Column {
            IslamicHeader(
                title = surah?.nameLatin ?: "Surah $currentSurahNumber",
                subtitle = "${surah?.indonesianMeaning ?: ""} • ${surah?.numberOfAyahs ?: 0} Ayat",
                arabicTitle = surah?.nameArabic,
                onBackClick = onBackClick,
                showBottomDivider = false,
                actions = {
                    IconButton(onClick = { showGoToSheet = true }) {
                        Surface(
                            color = MerahMerdeka.copy(alpha = 0.12f),
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🧭", fontSize = 14.sp)
                            }
                        }
                    }
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Surface(
                            color = MerahMerdeka.copy(alpha = 0.12f),
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("A±", color = MerahMerdeka, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
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
            SurahTabStrip(
                surahs = state.surahs,
                currentSurahNumber = currentSurahNumber,
                onSurahSelected = { targetSurahNumber -> jumpTo(targetSurahNumber, 1) }
            )
          }
        },
        bottomBar = {
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
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground
    ) { paddingValues ->
        LazyColumn(
            state = listState,
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
                    if (surah?.revelationType != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Surface(
                                color = Color.White.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = surah.revelationType.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EmasMuda,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Basmalah (kecuali At-Taubah)
            if (currentSurahNumber != 9) {
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
                    surahName = surah?.nameLatin ?: "Surah $currentSurahNumber",
                    fontScale = fontScale,
                    showLatin = showLatin,
                    showTranslation = showTranslation,
                    onClick = { selectedAyahForOptions = ayah },
                    onBookmark = {
                        viewModel.onIntent(
                            QuranUiIntent.SaveBookmark(
                                surahNumber = ayah.surahNumber,
                                ayahNumber = ayah.numberInSurah,
                                surahName = surah?.nameLatin ?: "Surah $currentSurahNumber"
                            )
                        )
                    }
                )
            }
        }
    }

    if (showSettingsDialog) {
        TextReaderSettingsSheet(
            fontScale = fontScale,
            onFontScaleChange = { viewModel.onIntent(QuranUiIntent.UpdateFontScale(it)) },
            onDismiss = { showSettingsDialog = false },
            toggles = listOf(
                ReaderToggleOption(
                    title = "Teks Latin (Transliterasi)",
                    subtitle = "Panduan lafal bagi pemula",
                    checked = showLatin,
                    onCheckedChange = { showLatin = it }
                ),
                ReaderToggleOption(
                    title = "Terjemahan",
                    subtitle = "Mengetahui makna & kandungan",
                    checked = showTranslation,
                    onCheckedChange = { showTranslation = it }
                )
            )
        )
    }

    if (showGoToSheet) {
        GoToSurahAyahSheet(
            surahs = state.surahs,
            initialSurahNumber = currentSurahNumber,
            initialAyahNumber = 1,
            onDismiss = { showGoToSheet = false },
            onConfirm = { targetSurahNumber, targetAyahNumber ->
                showGoToSheet = false
                jumpTo(targetSurahNumber, targetAyahNumber)
            }
        )
    }

    selectedAyahForOptions?.let { ayah ->
        val currentSurahName = surah?.nameLatin ?: "Surah $currentSurahNumber"
        val shareText = remember(ayah) {
            buildString {
                append(ayah.textArabic)
                append("\n\n")
                if (ayah.transliterationLatin.isNotBlank()) {
                    append(ayah.transliterationLatin)
                    append("\n\n")
                }
                append(ayah.translationIndonesian)
                append("\n\n")
                append("(QS. $currentSurahName: ${ayah.numberInSurah})")
            }
        }
        val shareAction = rememberShareTextAction()
        val clipboardManager = LocalClipboardManager.current

        AyahOptionsSheet(
            surahName = currentSurahName,
            ayahNumber = ayah.numberInSurah,
            onDismiss = { selectedAyahForOptions = null },
            onPlayMurotal = {
                val audioUrl = ayah.audioUrl ?: surah?.audioUrl
                if (audioUrl != null) {
                    viewModel.onIntent(
                        QuranUiIntent.PlayAudio(
                            AudioTrack(
                                id = "ayah_${ayah.surahNumber}_${ayah.numberInSurah}",
                                title = "Murottal $currentSurahName Ayat ${ayah.numberInSurah}",
                                subtitle = "Al-Qur'an 30 Juz",
                                urlOrPath = audioUrl
                            )
                        )
                    )
                }
            },
            onMarkLastRead = {
                viewModel.onIntent(
                    QuranUiIntent.SaveBookmark(
                        surahNumber = ayah.surahNumber,
                        ayahNumber = ayah.numberInSurah,
                        surahName = currentSurahName
                    )
                )
            },
            onShare = { shareAction(shareText) },
            onCopy = { clipboardManager.setText(AnnotatedString(shareText)) },
            playMurotalEnabled = ayah.audioUrl != null || surah?.audioUrl != null
        )
    }
}

/** Horizontal scrollable strip of surah tabs, for quickly switching surah while reading. */
@Composable
private fun SurahTabStrip(
    surahs: List<SurahMeta>,
    currentSurahNumber: Int,
    onSurahSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (surahs.isEmpty()) return

    val isDark = RabithohTheme.colors.isDark
    val listState = rememberLazyListState()
    val currentIndex = remember(surahs, currentSurahNumber) {
        surahs.indexOfFirst { it.number == currentSurahNumber }
    }

    LaunchedEffect(currentIndex) {
        if (currentIndex >= 0) {
            listState.animateScrollToItem(maxOf(0, currentIndex - 1))
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (isDark) DarkSurface else PutihBersih
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(surahs, key = { it.number }) { tabSurah ->
                val isSelected = tabSurah.number == currentSurahNumber
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) MerahMerdeka else (if (isDark) DarkSurfaceVariant else Color(0xFFF1F3F5)),
                    border = if (isSelected) BorderStroke(1.dp, EmasKhidmat) else null,
                    modifier = Modifier.clickable { onSurahSelected(tabSurah.number) }
                ) {
                    Text(
                        text = "${tabSurah.number}. ${tabSurah.nameLatin}",
                        color = if (isSelected) PutihBersih else (if (isDark) DarkMuted else SlateMuted),
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AyahItemCard(
    ayah: Ayah,
    surahName: String,
    fontScale: Float,
    showLatin: Boolean,
    showTranslation: Boolean,
    onClick: () -> Unit,
    onBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.SURFACE_CLEAN,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick
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

        if (showLatin && ayah.transliterationLatin.isNotBlank()) {
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

        if (showTranslation) {
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
}
