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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.iqbalwork.robithoh.core.designsystem.component.ScrollToTopButton
import com.iqbalwork.robithoh.core.designsystem.component.shouldShowScrollToTop
import com.iqbalwork.robithoh.core.presentation.rememberPersistedLazyListState
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.ReaderTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateBorder
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.feature.quran.model.Ayah
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta
import com.iqbalwork.robithoh.feature.quran.ui.QariPickerSheet
import com.iqbalwork.robithoh.feature.quran.ui.QuranAudioBottomBar
import com.iqbalwork.robithoh.navigation.BackHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    viewModel: QuranViewModel,
    surahNumber: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialAyahNumber: Int? = null,
    onSwitchToMushafMode: ((pageNumber: Int) -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()
    val isDark = RabithohTheme.colors.isDark
    val listState = rememberPersistedLazyListState("quran_reader_${state.currentSurah?.number ?: surahNumber}")
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

    // Parse active audio ayah from track ID (e.g. "ayah_2_142")
    val activeAudioAyah = remember(state.activeAudioTrack) {
        val id = state.activeAudioTrack?.id ?: return@remember null
        val parts = id.split("_")
        if (parts.size >= 3 && parts[0] == "ayah") {
            val s = parts[1].toIntOrNull()
            val a = parts[2].toIntOrNull()
            if (s != null && a != null) Pair(s, a) else null
        } else null
    }

    val currentActiveAyahNumber = activeAudioAyah?.let { (surahNum, ayahNum) ->
        if (surahNum == currentSurahNumber) ayahNum else null
    } ?: state.activeAyahNumber

    val isAudioActive = state.audioPlaybackState == AudioPlaybackState.PLAYING ||
            state.audioPlaybackState == AudioPlaybackState.BUFFERING

    // Auto-scroll to currently reciting ayah when audio is playing or buffering
    LaunchedEffect(currentActiveAyahNumber, isAudioActive) {
        val target = currentActiveAyahNumber
        if (target != null && isAudioActive && state.currentAyahs.isNotEmpty()) {
            val index = state.currentAyahs.indexOfFirst { it.numberInSurah == target }
            if (index >= 0) {
                listState.animateScrollToItem((index + ayahListOffset).coerceAtLeast(0))
            }
        }
    }

    // Menandai terakhir dibaca untuk item paling atas dieksekusi ketika onDestroy (onDispose) page-nya,
    // bukan ketika halaman dibuka atau ketika pengguna melakukan scroll.
    val currentSurahForDispose by rememberUpdatedState(surah)
    val currentAyahsForDispose by rememberUpdatedState(state.currentAyahs)

    DisposableEffect(Unit) {
        onDispose {
            val surahToSave = currentSurahForDispose
            val ayahsToSave = currentAyahsForDispose
            if (ayahsToSave.isNotEmpty() && surahToSave != null) {
                val rawIndex = listState.firstVisibleItemIndex - ayahListOffset
                val safeIndex = rawIndex.coerceIn(0, ayahsToSave.lastIndex)
                val visibleAyah = ayahsToSave.getOrNull(safeIndex)
                if (visibleAyah != null) {
                    val current = state.lastReadBookmark
                    if (current == null || current.surahNumber != visibleAyah.surahNumber || current.ayahNumber != visibleAyah.numberInSurah) {
                        viewModel.onIntent(
                            QuranUiIntent.SaveBookmark(
                                surahNumber = visibleAyah.surahNumber,
                                ayahNumber = visibleAyah.numberInSurah,
                                surahName = surahToSave.nameLatin,
                                showToast = false
                            )
                        )
                    }
                }
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

    // Auto-switch surah if auto-play advances to the next surah
    LaunchedEffect(state.activeAudioTrack) {
        val trackId = state.activeAudioTrack?.id ?: return@LaunchedEffect
        if (trackId.startsWith("ayah_")) {
            val parts = trackId.split("_")
            val playingSurah = parts.getOrNull(1)?.toIntOrNull()
            val playingAyah = parts.getOrNull(2)?.toIntOrNull()
            if (playingSurah != null && playingSurah != currentSurahNumber) {
                jumpTo(playingSurah, playingAyah ?: 1)
            }
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
            ),
            SpotlightStep(
                id = "quran_mushaf",
                title = "Beralih ke Mode Mushaf",
                description = "Ketuk tombol 📖 ini untuk membaca Al-Qur'an dalam tampilan halaman mushaf digital—persis seperti kitab fisik, lengkap dengan navigasi halaman dan download offline.",
                shapeType = SpotlightShapeType.CIRCLE,
                padding = 6.dp
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
                    if (onSwitchToMushafMode != null) {
                        IconButton(
                            onClick = {
                                val visibleAyah = listState.layoutInfo.visibleItemsInfo
                                    .mapNotNull { itemInfo ->
                                        val keyStr = itemInfo.key as? String ?: return@mapNotNull null
                                        val parts = keyStr.split("_")
                                        if (parts.size == 2) {
                                            val sNum = parts[0].toIntOrNull()
                                            val aNum = parts[1].toIntOrNull()
                                            if (sNum != null && aNum != null) aNum else null
                                        } else null
                                    }
                                    .firstOrNull() ?: 1

                                val targetPage = com.iqbalwork.robithoh.feature.quran.data.QuranPageLookup.getPageForAyah(currentSurahNumber, visibleAyah)
                                onSwitchToMushafMode(targetPage)
                            },
                            modifier = Modifier.spotlightAnchor(quranSpotlightState, "quran_mushaf")
                        ) {
                            Surface(
                                color = MerahMerdeka.copy(alpha = 0.12f),
                                shape = CircleShape,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📖", fontSize = 14.sp)
                                }
                            }
                        }
                    }
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
        containerColor = readerTheme.backgroundColor
    ) { paddingValues ->
        val navBarBottomInset = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(
                    top = 12.dp,
                    bottom = 90.dp + if (navBarBottomInset > 20.dp) navBarBottomInset + 4.dp else 12.dp
                )
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
                    val isPlaying = currentActiveAyahNumber == ayah.numberInSurah && isAudioActive

                    AyahItemCard(
                        ayah = ayah,
                        surahName = surah?.nameLatin ?: "Surah $currentSurahNumber",
                        fontScale = fontScale,
                        showLatin = showLatin,
                        showTranslation = showTranslation,
                        isLastRead = isLastRead,
                        isPlaying = isPlaying,
                        readerTheme = readerTheme,
                        onClick = { selectedAyahForOptions = ayah },
                        modifier = Modifier.let { mod ->
                            if (isFirstAyah) mod.spotlightAnchor(quranSpotlightState, "quran_verse_action")
                            else mod
                        }
                    )
                }
            }

            // Floating Audio Bottom Bar Dock
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = if (navBarBottomInset > 20.dp) navBarBottomInset + 4.dp else 12.dp
                    )
            ) {
                QuranAudioBottomBar(
                    selectedQari = state.selectedQari,
                    playbackState = state.audioPlaybackState,
                    isAudioLoading = state.isAudioLoading,
                    repeatMode = state.audioRepeatMode,
                    currentPositionMs = state.audioPositionMs,
                    durationMs = state.audioDurationMs,
                    activeAyahNumber = currentActiveAyahNumber,
                    surahName = surah?.nameLatin,
                    onPlayPauseClick = {
                        if (state.audioPlaybackState == AudioPlaybackState.PLAYING ||
                            state.audioPlaybackState == AudioPlaybackState.PAUSED
                        ) {
                            viewModel.onIntent(QuranUiIntent.TogglePlayPauseAudio)
                        } else {
                            val startAyah = currentActiveAyahNumber ?: 1
                            viewModel.onIntent(QuranUiIntent.PlayAyahAudio(currentSurahNumber, startAyah))
                        }
                    },
                    onToggleRepeatMode = { viewModel.onIntent(QuranUiIntent.ToggleAudioRepeatMode) },
                    onQariClick = { viewModel.onIntent(QuranUiIntent.SetQariPickerVisible(true)) },
                    customBackgroundColor = readerTheme.backgroundColor
                )
            }

            ScrollToTopButton(
                visible = listState.shouldShowScrollToTop(),
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = if (state.audioPlaybackState != AudioPlaybackState.IDLE) 120.dp else 24.dp,
                        end = 16.dp
                    )
            )
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
            },
            onConfirmPage = { targetPage ->
                showGoToSheet = false
                if (onSwitchToMushafMode != null) {
                    onSwitchToMushafMode(targetPage)
                } else {
                    val meta = com.iqbalwork.robithoh.feature.quran.data.QuranPageLookup.getPageMeta(targetPage)
                    jumpTo(meta.surahNumber, 1)
                }
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
                viewModel.onIntent(
                    QuranUiIntent.PlayAyahAudio(
                        surahNumber = ayah.surahNumber,
                        ayahNumber = ayah.numberInSurah
                    )
                )
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
            playMurotalEnabled = true,
            isLastRead = isAyahLastRead
        )
    }

    if (state.isQariPickerVisible) {
        QariPickerSheet(
            selectedQari = state.selectedQari,
            availableQaris = state.availableQaris,
            onSelectQari = { viewModel.onIntent(QuranUiIntent.SelectQari(it)) },
            onDismiss = { viewModel.onIntent(QuranUiIntent.SetQariPickerVisible(false)) }
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

    // reverseLayout: surah 1 ada di kanan (RTL). Scroll-target di-mirror: index 0 = kanan.
    // Dengan reverseLayout, animateScrollToItem(n) akan scroll ke posisi yang benar.
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
            reverseLayout = true,
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
    isPlaying: Boolean = false,
    readerTheme: ReaderTheme = ReaderTheme.WHITE,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = readerTheme.isDark
    val cardBg = when {
        isPlaying -> if (isDark) EmasKhidmat.copy(alpha = 0.18f) else Color(0xFFFFFBEB)
        isLastRead -> readerTheme.lastReadCardBackgroundColor
        else -> readerTheme.cardBackgroundColor
    }
    val cardBorder = when {
        isPlaying -> EmasKhidmat
        isLastRead -> readerTheme.lastReadCardBorderColor
        else -> readerTheme.cardBorderColor
    }

    GoldCrimsonCard(
        modifier = modifier,
        variant = if (isPlaying || isLastRead) GoldCrimsonCardVariant.GOLD_BORDER else GoldCrimsonCardVariant.SURFACE_CLEAN,
        customBackgroundColor = cardBg,
        customBorderColor = cardBorder,
        customBorderWidth = if (isPlaying || isLastRead) 1.5.dp else 1.dp,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick
    ) {
        // Top Header in Ayah Card (Ayah Number & Last Read / Playing Marker)
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
                        when {
                            isPlaying -> EmasKhidmat
                            isLastRead -> MerahMerdeka
                            else -> if (isDark) DarkSurfaceVariant else Color(0xFFF3F4F6)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${ayah.numberInSurah}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (isPlaying || isLastRead) PutihBersih else MerahMerdeka
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isPlaying) {
                    Surface(
                        color = EmasKhidmat,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("🔊", fontSize = 10.sp)
                            Text(
                                text = "Murottal",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PutihBersih
                            )
                        }
                    }
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
