package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import com.iqbalwork.robithoh.core.designsystem.component.AyahOptionsSheet
import com.iqbalwork.robithoh.core.designsystem.component.GoToSurahAyahSheet
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.component.IslamicDivider
import com.iqbalwork.robithoh.core.designsystem.component.IslamicDividerMotif
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.component.MiniFloatingAudioBar
import com.iqbalwork.robithoh.core.designsystem.component.ReaderToggleOption
import com.iqbalwork.robithoh.core.designsystem.component.SpotlightOverlay
import com.iqbalwork.robithoh.core.designsystem.component.SpotlightShapeType
import com.iqbalwork.robithoh.core.designsystem.component.SpotlightStep
import com.iqbalwork.robithoh.core.designsystem.component.rememberSpotlightState
import com.iqbalwork.robithoh.core.designsystem.component.spotlightAnchor
import com.iqbalwork.robithoh.core.designsystem.component.TextReaderSettingsSheet
import com.iqbalwork.robithoh.core.designsystem.rememberShareTextAction
import com.iqbalwork.robithoh.core.settings.rememberAppSettingsRepository
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
import com.iqbalwork.robithoh.core.designsystem.theme.ReaderTheme
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.quran.model.Ayah
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta
import com.iqbalwork.robithoh.navigation.BackHandler
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
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
    val readerSettingsRepository = com.iqbalwork.robithoh.core.settings.rememberReaderSettingsRepository()
    val readerSettings by readerSettingsRepository.settings.collectAsState()
    val fontScale = readerSettings.fontScale
    val readerTheme = readerSettings.resolveTheme(isDark)

    LaunchedEffect(readerSettings.fontScale) {
        if (state.fontScale != readerSettings.fontScale) {
            viewModel.onIntent(QuranUiIntent.UpdateFontScale(readerSettings.fontScale))
        }
    }
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

    // Header items ahead of the ayah list: hero banner + divider, plus Basmalah unless Al-Fatihah or At-Taubah.
    val hasBasmalahHeader = currentSurahNumber != 1 && currentSurahNumber != 9
    val ayahListOffset = if (hasBasmalahHeader) 3 else 2

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

    // Automatically tracks and marks the topmost visible ayah on screen as "Terakhir Dibaca"
    LaunchedEffect(state.currentAyahs, surah, pendingScrollAyah) {
        if (state.currentAyahs.isEmpty() || surah == null) return@LaunchedEffect
        if (pendingScrollAyah != null) return@LaunchedEffect

        snapshotFlow {
            val rawIndex = listState.firstVisibleItemIndex - ayahListOffset
            val safeIndex = rawIndex.coerceIn(0, state.currentAyahs.lastIndex)
            state.currentAyahs.getOrNull(safeIndex)
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { visibleAyah ->
                val current = state.lastReadBookmark
                if (current == null || current.surahNumber != visibleAyah.surahNumber || current.ayahNumber != visibleAyah.numberInSurah) {
                    viewModel.onIntent(
                        QuranUiIntent.SaveBookmark(
                            surahNumber = visibleAyah.surahNumber,
                            ayahNumber = visibleAyah.numberInSurah,
                            surahName = surah.nameLatin,
                            showToast = false
                        )
                    )
                }
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

    val appSettingsRepository = rememberAppSettingsRepository()
    val appSettings by appSettingsRepository.settings.collectAsState()

    val quranSpotlightSteps = remember {
        listOf(
            SpotlightStep(
                id = "quran_goto",
                title = "Pindah Surat & Ayat Cepat",
                description = "Gunakan tombol kompas untuk melompat langsung ke surat atau nomor ayat tertentu tanpa perlu menggulir panjang.",
                shapeType = SpotlightShapeType.CIRCLE,
                padding = 6.dp
            ),
            SpotlightStep(
                id = "quran_settings",
                title = "Pengaturan Tampilan Al-Qur'an",
                description = "Ubah ukuran huruf Arab, sembunyikan atau tampilkan teks Latin dan terjemahan, serta pilih tema warna bacaan yang nyaman di mata.",
                shapeType = SpotlightShapeType.CIRCLE,
                padding = 6.dp
            ),
            SpotlightStep(
                id = "quran_verse_action",
                title = "Salin, Bagikan & Murottal Ayat",
                description = "Ketuk pada ayat mana pun untuk membuka menu cepat: dengarkan lantunan murottal, salin teks ayat, bagikan ke kerabat, atau tandai sebagai terakhir dibaca.",
                shapeType = SpotlightShapeType.ROUNDED_RECT,
                cornerRadius = 16.dp,
                padding = 4.dp
            )
        )
    }

    val quranSpotlightState = rememberSpotlightState(
        steps = quranSpotlightSteps,
        onComplete = {
            appSettingsRepository.setQuranSpotlightSeen(true)
        }
    )

    LaunchedEffect(appSettings.hasSeenQuranSpotlight, state.currentAyahs) {
        if (!appSettings.hasSeenQuranSpotlight && state.currentAyahs.isNotEmpty() && !quranSpotlightState.isVisible) {
            quranSpotlightState.start()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
          Column {
            IslamicHeader(
                title = surah?.nameLatin ?: "Surah $currentSurahNumber",
                subtitle = "${surah?.indonesianMeaning ?: ""} • ${surah?.numberOfAyahs ?: 0} Ayat",
                arabicTitle = surah?.nameArabic,
                onBackClick = onBackClick,
                showBottomDivider = false,
                actions = {
                    IconButton(
                        onClick = { showGoToSheet = true },
                        modifier = Modifier.spotlightAnchor(quranSpotlightState, "quran_goto")
                    ) {
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
                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier.spotlightAnchor(quranSpotlightState, "quran_settings")
                    ) {
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
                readerTheme = readerTheme,
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
        containerColor = readerTheme.backgroundColor
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

            // Basmalah (kecuali Al-Fatihah dan At-Taubah)
            if (hasBasmalahHeader) {
                item {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.GOLD_BORDER,
                        customBackgroundColor = readerTheme.cardBackgroundColor,
                        customBorderColor = readerTheme.cardBorderColor,
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            style = RabithohTheme.typography.arabicMedium.copy(
                                color = readerTheme.arabicTextColor,
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
                val isLastRead = state.lastReadBookmark?.let {
                    it.surahNumber == ayah.surahNumber && it.ayahNumber == ayah.numberInSurah
                } ?: false
                val isFirstAyah = state.currentAyahs.firstOrNull()?.let {
                    it.surahNumber == ayah.surahNumber && it.numberInSurah == ayah.numberInSurah
                } ?: false

                AyahItemCard(
                    ayah = ayah,
                    surahName = surah?.nameLatin ?: "Surah $currentSurahNumber",
                    fontScale = fontScale,
                    showLatin = showLatin,
                    showTranslation = showTranslation,
                    isLastRead = isLastRead,
                    readerTheme = readerTheme,
                    onClick = { selectedAyahForOptions = ayah },
                    modifier = Modifier.let { mod ->
                        if (isFirstAyah) mod.spotlightAnchor(quranSpotlightState, "quran_verse_action")
                        else mod
                    }
                )
            }
        }
    }

    if (showSettingsDialog) {
        TextReaderSettingsSheet(
            fontScale = fontScale,
            onFontScaleChange = {
                readerSettingsRepository.updateFontScale(it)
                viewModel.onIntent(QuranUiIntent.UpdateFontScale(it))
            },
            selectedTheme = readerTheme,
            onThemeSelected = { readerSettingsRepository.updateTheme(it) },
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

        val isAyahLastRead = state.lastReadBookmark?.let {
            it.surahNumber == ayah.surahNumber && it.ayahNumber == ayah.numberInSurah
        } ?: false

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
                        surahName = currentSurahName,
                        showToast = true
                    )
                )
            },
            onShare = { shareAction(shareText) },
            onCopy = { clipboardManager.setText(AnnotatedString(shareText)) },
            playMurotalEnabled = ayah.audioUrl != null || surah?.audioUrl != null,
            isLastRead = isAyahLastRead
        )
    }

    SpotlightOverlay(state = quranSpotlightState)
  }
}

/** Horizontal scrollable strip of surah tabs, for quickly switching surah while reading. */
@Composable
private fun SurahTabStrip(
    surahs: List<SurahMeta>,
    currentSurahNumber: Int,
    readerTheme: ReaderTheme = ReaderTheme.WHITE,
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
        color = readerTheme.surfaceColor
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
                val isStripDark = readerTheme.isDark
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) MerahMerdeka else (if (isStripDark) DarkSurfaceVariant else Color(0xFFF1F3F5)),
                    border = if (isSelected) BorderStroke(1.dp, EmasKhidmat) else (if (isStripDark) BorderStroke(1.dp, DarkBorder) else null),
                    modifier = Modifier.clickable { onSurahSelected(tabSurah.number) }
                ) {
                    Text(
                        text = "${tabSurah.number}. ${tabSurah.nameLatin}",
                        color = if (isSelected) PutihBersih else (if (isStripDark) DarkMuted else SlateMuted),
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
    isLastRead: Boolean = false,
    readerTheme: ReaderTheme = ReaderTheme.WHITE,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = readerTheme.isDark
    val cardBg = if (isLastRead) readerTheme.lastReadCardBackgroundColor else readerTheme.cardBackgroundColor
    val cardBorder = if (isLastRead) readerTheme.lastReadCardBorderColor else readerTheme.cardBorderColor

    GoldCrimsonCard(
        modifier = modifier,
        variant = if (isLastRead) GoldCrimsonCardVariant.GOLD_BORDER else GoldCrimsonCardVariant.SURFACE_CLEAN,
        customBackgroundColor = cardBg,
        customBorderColor = cardBorder,
        customBorderWidth = if (isLastRead) 1.5.dp else 1.dp,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick
    ) {
        // Top Header in Ayah Card (Ayah Number & Last Read Marker)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (isLastRead) MerahMerdeka
                        else (if (isDark) DarkSurfaceVariant else Color(0xFFF3F4F6))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${ayah.numberInSurah}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (isLastRead) PutihBersih else MerahMerdeka
                )
            }

            if (isLastRead) {
                Surface(
                    color = if (isDark) MerahMerdeka.copy(alpha = 0.2f) else Color(0xFFFFF1F2),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isDark) MerahMerdeka.copy(alpha = 0.5f) else MerahMerdeka.copy(alpha = 0.6f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("🔖", fontSize = 11.sp)
                        Text(
                            text = "Terakhir Dibaca",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color(0xFFFCA5A5) else MerahMerdeka
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Arabic text
        Text(
            text = ayah.textArabic,
            style = RabithohTheme.typography.arabicLarge.copy(
                fontSize = (24 * fontScale).sp,
                lineHeight = (44 * fontScale).sp,
                color = readerTheme.arabicTextColor,
                textAlign = TextAlign.Right
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (showLatin && ayah.transliterationLatin.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = ayah.transliterationLatin,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = readerTheme.latinTextColor,
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
                    color = readerTheme.translationTextColor,
                    fontSize = (13 * fontScale).sp,
                    lineHeight = (20 * fontScale).sp
                )
            )

            if (ayah.translationSundanese.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Basa Sunda: ${ayah.translationSundanese}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = readerTheme.translationTextColor,
                        fontSize = (12 * fontScale).sp,
                        lineHeight = (18 * fontScale).sp
                    )
                )
            }
        }
    }
}
