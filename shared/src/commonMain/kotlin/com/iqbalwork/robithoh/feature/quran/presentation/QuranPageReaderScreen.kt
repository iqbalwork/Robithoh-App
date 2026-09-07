package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.AyahOptionsSheet
import com.iqbalwork.robithoh.core.designsystem.component.DownloadMushafSheet
import com.iqbalwork.robithoh.core.designsystem.component.GoToSurahAyahSheet
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.component.MiniFloatingAudioBar
import com.iqbalwork.robithoh.core.designsystem.rememberShareTextAction
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.model.AudioTrack
import com.iqbalwork.robithoh.core.settings.rememberAppSettingsRepository
import com.iqbalwork.robithoh.feature.quran.data.MushafDownloadState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.iqbalwork.robithoh.core.device.rememberScreenOrientationController
import com.iqbalwork.robithoh.feature.quran.data.QuranData
import com.iqbalwork.robithoh.feature.quran.data.QuranPageLookup
import com.iqbalwork.robithoh.feature.quran.data.QuranPageManager
import com.iqbalwork.robithoh.feature.quran.model.QuranPageMapping
import com.iqbalwork.robithoh.feature.quran.presentation.component.MushafPageView
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranPageReaderScreen(
    viewModel: QuranViewModel,
    initialPageNumber: Int = 1,
    initialAyahNumber: Int? = null,
    onBackClick: () -> Unit,
    onSwitchToTextMode: (surahNumber: Int, ayahNumber: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val isDark = RabithohTheme.colors.isDark
    val coroutineScope = rememberCoroutineScope()
    val pageManager = remember { QuranPageManager() }
    val appSettingsRepo = rememberAppSettingsRepository()
    val appSettings by appSettingsRepo.settings.collectAsState()

    val orientationController = rememberScreenOrientationController()
    val handleBack = {
        orientationController.resetToDefault()
        onBackClick()
    }

    com.iqbalwork.robithoh.navigation.BackHandler {
        handleBack()
    }

    DisposableEffect(Unit) {
        onDispose {
            orientationController.resetToDefault()
        }
    }

    // Pager state (0-indexed: page 0 is Halaman 1, reversed layout for RTL Mushaf feel)
    val startPage = (initialPageNumber - 1).coerceIn(0, QuranPageLookup.TOTAL_PAGES - 1)
    val pagerState = rememberPagerState(initialPage = startPage) { QuranPageLookup.TOTAL_PAGES }
    val currentPageNumber = pagerState.currentPage + 1
    val pageMeta = remember(currentPageNumber) { QuranPageLookup.getPageMeta(currentPageNumber) }

    // Fullscreen state: starts in fullscreen as requested
    var isFullscreen by rememberSaveable { mutableStateOf(true) }

    // Dialog & sheet visibility
    var showGoToSheet by rememberSaveable { mutableStateOf(false) }
    var showDownloadSheet by rememberSaveable { mutableStateOf(false) }
    var hasCheckedDownloadPrompt by rememberSaveable { mutableStateOf(false) }
    val downloadState by pageManager.downloadState.collectAsState()

    // Selected Ayah for options sheet
    var selectedAyah by remember {
        mutableStateOf(
            if (initialAyahNumber != null) {
                Pair(pageMeta.surahNumber, initialAyahNumber)
            } else null
        )
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

    // Auto-scroll pager if active audio crosses to another page
    LaunchedEffect(activeAudioAyah) {
        if (activeAudioAyah != null) {
            val targetPage = QuranPageLookup.getPageForAyah(activeAudioAyah.first, activeAudioAyah.second)
            if (targetPage != currentPageNumber) {
                pagerState.animateScrollToPage(targetPage - 1)
            }
        }
    }

    // Prefetch neighbor pages whenever current page changes
    LaunchedEffect(currentPageNumber) {
        pageManager.prefetchPages(currentPageNumber)
    }

    // Check whether to show first-time download suggestion (only once per session)
    LaunchedEffect(Unit) {
        if (!hasCheckedDownloadPrompt) {
            hasCheckedDownloadPrompt = true
            if (!appSettings.hideMushafDownloadPrompt && !pageManager.isAllDownloaded()) {
                showDownloadSheet = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) DarkCanvas else Color(0xFFFBF9F4))
    ) {
        // Horizontal Pager for Mushaf Pages with 3D Book Turn animation
        HorizontalPager(
            state = pagerState,
            reverseLayout = true,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val pageNum = pageIndex + 1
            var pageImage by remember(pageNum) { mutableStateOf<ImageBitmap?>(null) }
            var pageMapping by remember(pageNum) { mutableStateOf<QuranPageMapping?>(null) }

            LaunchedEffect(pageNum) {
                pageImage = pageManager.loadPageImage(pageNum)
                pageMapping = pageManager.getPageMapping(pageNum)
            }

            // Calculate page offset for authentic physical book page curl effect
            val pageOffset = ((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction)
            val clampedOffset = pageOffset.coerceIn(-1f, 1f)
            val isRightPage = (pageNum % 2 == 1)

            // Menentukan apakah transisi yang sedang aktif adalah intra-spread (Slide) atau inter-spread (Wattpad Paper Curl)
            // Transisi aktif dengan halaman berikutnya (swipe forward, clampedOffset > 0):
            // - Jika halaman ganjil (kanan): ke halaman genap (kiri) -> Intra-spread (Slide)
            // - Jika halaman genap (kiri): ke halaman ganjil (kanan lembar baru) -> Inter-spread (Curl)
            // Transisi aktif dengan halaman sebelumnya (swipe backward/entering, clampedOffset < 0):
            // - Jika halaman genap (kiri): dari halaman ganjil (kanan) -> Intra-spread (Slide)
            // - Jika halaman ganjil (kanan): dari halaman genap (kiri lembar lama) -> Inter-spread (Curl, stationary underneath)
            val isSlideMode = (clampedOffset > 0f && isRightPage) || (clampedOffset < 0f && !isRightPage)
            val isCurlTurningLeaf = (clampedOffset > 0f && !isRightPage)
            val isCurlUnderneathLeaf = (clampedOffset < 0f && isRightPage)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex((QuranPageLookup.TOTAL_PAGES - pageIndex).toFloat())
                    .graphicsLayer {
                        cameraDistance = 24f * density

                        if (isSlideMode) {
                            // MODE SLIDE: Geser horizontal berdampingan murni (Intra-Spread)
                            // Biarkan HorizontalPager menggeser kedua halaman secara natural tanpa penahanan
                            translationX = 0f
                            translationY = 0f
                            rotationY = 0f
                            rotationZ = 0f
                            scaleX = 1f
                            scaleY = 1f
                            alpha = 1f
                        } else if (isCurlTurningLeaf) {
                            // MODE WATTPAD PAPER CURL: Lembaran kertas genap membalik ke kanan (poros tulang jilid)
                            val progress = clampedOffset.coerceIn(0f, 1f)

                            // Poros pada tulang jilid di sisi kanan
                            transformOrigin = TransformOrigin(1f, 0.5f)

                            // Rotasi 3D progresif: dimulai dari 0 derajat hingga terlipat -82 derajat
                            val rotationAngle = -82f * (progress * (1.2f - 0.2f * progress))
                            rotationY = rotationAngle.coerceIn(-90f, 0f)

                            // Kelenturan lengkungan kertas vertikal (kertas agak melengkung di tengah tarikan)
                            val flexFactor = sin(progress * PI.toFloat())
                            scaleY = 1f - (0.045f * flexFactor)
                            translationY = -4f * flexFactor * density

                            // Sedikit kemiringan Z alami seperti kertas ditarik dari sudut tengah
                            rotationZ = 1.2f * flexFactor

                            translationX = 0f
                            scaleX = 1f
                            alpha = 1f
                        } else if (isCurlUnderneathLeaf) {
                            // MODE LEMBARAN DI BAWAH: Halaman ganjil diam di tempat, tersingkap oleh lembaran atas
                            val progress = clampedOffset.coerceIn(-1f, 0f)

                            // Tahan posisi halaman agar tetap diam di bawah lembaran yang sedang melipat
                            translationX = -progress * size.width
                            translationY = 0f
                            rotationY = 0f
                            rotationZ = 0f

                            // Kedalaman halus saat tersingkap (sedikit scale-up saat terbuka)
                            val depthScale = 0.97f + (0.03f * (1f + progress))
                            scaleX = depthScale
                            scaleY = depthScale
                            alpha = 1f
                        } else {
                            // Halaman settled atau di luar jangkauan transisi aktif
                            translationX = 0f
                            translationY = 0f
                            rotationY = 0f
                            rotationZ = 0f
                            scaleX = 1f
                            scaleY = 1f
                            alpha = 1f
                        }
                    }
                    .drawWithContent {
                        drawContent()

                        if (isCurlTurningLeaf) {
                            val progress = clampedOffset.coerceIn(0f, 1f)
                            val flexFactor = sin(progress * PI.toFloat())

                            // 1. Garis kilau lekukan kertas bergerak (Moving Crease Highlight & Inner Shadow)
                            // Saat kertas dilipat, silinder lekukan bergerak dari sisi kiri ke sisi kanan (tulang jilid)
                            val foldX = size.width * (1f - progress)
                            val creaseWidth = (36.dp.toPx() * (1f + flexFactor * 0.5f)).coerceAtLeast(10f)

                            // Kilau putih di puncak lengkungan silinder kertas
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.14f * flexFactor),
                                        Color.White.copy(alpha = 0.32f * flexFactor),
                                        Color.Black.copy(alpha = 0.10f * flexFactor),
                                        Color.Transparent
                                    ),
                                    startX = (foldX - creaseWidth).coerceAtLeast(0f),
                                    endX = (foldX + creaseWidth).coerceAtMost(size.width)
                                ),
                                topLeft = Offset((foldX - creaseWidth).coerceAtLeast(0f), 0f),
                                size = Size(creaseWidth * 2f, size.height)
                            )

                            // 2. Ambient dimming pada kertas yang terlipat membelakangi pencahayaan
                            if (progress > 0.05f) {
                                val dimAlpha = (0.24f * progress).coerceIn(0f, 0.24f)
                                drawRect(
                                    color = Color.Black.copy(alpha = dimAlpha),
                                    size = size
                                )
                            }

                            // 3. Bayangan jatuh tajam di tepi kertas yang terangkat
                            val edgeShadowWidth = 28.dp.toPx()
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.22f * flexFactor),
                                        Color.Transparent
                                    ),
                                    startX = 0f,
                                    endX = edgeShadowWidth
                                ),
                                topLeft = Offset.Zero,
                                size = Size(edgeShadowWidth, size.height)
                            )
                        } else if (isCurlUnderneathLeaf) {
                            val progress = (-clampedOffset).coerceIn(0f, 1f) // 0 saat mulai tersingkap, 1 saat tertutup penuh
                            val revealProgress = 1f - progress // 0 saat tertutup, 1 saat terbuka penuh

                            // 1. Bayangan jatuh dari lembaran atas yang sedang melipat ke atas halaman bawah
                            val edgeX = size.width * revealProgress
                            val castShadowWidth = 42.dp.toPx()

                            if (progress > 0.01f) {
                                drawRect(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.30f * progress)
                                        ),
                                        startX = (edgeX - castShadowWidth).coerceAtLeast(0f),
                                        endX = edgeX
                                    ),
                                    topLeft = Offset((edgeX - castShadowWidth).coerceAtLeast(0f), 0f),
                                    size = Size(castShadowWidth, size.height)
                                )

                                // 2. Ambient shadow pada area yang masih tertutup lembaran atas
                                drawRect(
                                    color = Color.Black.copy(alpha = 0.12f * progress),
                                    topLeft = Offset(edgeX, 0f),
                                    size = Size((size.width - edgeX).coerceAtLeast(0f), size.height)
                                )
                            }
                        }
                    }
            ) {
                MushafPageView(
                    pageNumber = pageNum,
                    pageMapping = pageMapping,
                    pageImage = pageImage,
                    selectedAyah = if (currentPageNumber == pageNum) selectedAyah else null,
                    activeAudioAyah = if (currentPageNumber == pageNum) activeAudioAyah else null,
                    onAyahClick = { surah, ayah ->
                        selectedAyah = Pair(surah, ayah)
                    },
                    onBackgroundClick = {
                        isFullscreen = !isFullscreen
                    },
                    onDoubleTap = {
                        orientationController.toggleOrientation()
                    },
                    onPinchOut = {
                        orientationController.setLandscape()
                    },
                    onPinchIn = {
                        orientationController.setPortrait()
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // 1. Bayangan Tulang Jilid Mushaf Fisik (Spine Gutter Shadow & Crease)
                // Halaman ganjil: jilid buku di kiri; Halaman genap: jilid buku di kanan
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(32.dp)
                        .align(if (isRightPage) Alignment.CenterStart else Alignment.CenterEnd)
                        .background(
                            Brush.horizontalGradient(
                                if (isRightPage) {
                                    listOf(
                                        Color.Black.copy(alpha = 0.22f),
                                        Color.Black.copy(alpha = 0.08f),
                                        Color.Black.copy(alpha = 0.02f),
                                        Color.Transparent
                                    )
                                } else {
                                    listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.02f),
                                        Color.Black.copy(alpha = 0.08f),
                                        Color.Black.copy(alpha = 0.22f)
                                    )
                                }
                            )
                        )
                )

                // Garis lipatan jahitan jilid (Spine stitch groove) di sisi jilid
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.5.dp)
                        .align(if (isRightPage) Alignment.CenterStart else Alignment.CenterEnd)
                        .background(Color.Black.copy(alpha = 0.15f))
                )

                // Garis potong tepi luar kertas (Clean outer paper margin) di sisi berlawanan jilid
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .align(if (isRightPage) Alignment.CenterEnd else Alignment.CenterStart)
                        .background(Color.Black.copy(alpha = 0.05f))
                )
            }
        }

        // Top Bar Overlay
        AnimatedVisibility(
            visible = !isFullscreen,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                color = if (isDark) com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface.copy(alpha = 0.95f) else PutihBersih.copy(alpha = 0.95f),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                val sideText = if (currentPageNumber % 2 == 1) "Kanan" else "Kiri"
                IslamicHeader(
                    title = "Hal. $currentPageNumber ($sideText) • ${pageMeta.surahName}",
                    subtitle = "Juz ${pageMeta.juz} • ${pageMeta.ayahRangeText}",
                    arabicTitle = pageMeta.arabicSurahName,
                    onBackClick = handleBack,
                    showBottomDivider = false,
                    actions = {
                        // Switch to Text Reader button (Smart Sync: specific ayah or page start ayah)
                        IconButton(
                            onClick = {
                                val targetSurah = selectedAyah?.first ?: pageMeta.surahNumber
                                val targetAyah = selectedAyah?.second ?: pageMeta.startAyah
                                onSwitchToTextMode(targetSurah, targetAyah)
                            }
                        ) {
                            Surface(
                                color = MerahMerdeka.copy(alpha = 0.12f),
                                shape = CircleShape,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📜", fontSize = 14.sp)
                                }
                            }
                        }

                        // Go To Navigation button
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

                        // Offline Download Mushaf button
                        IconButton(onClick = { showDownloadSheet = true }) {
                            Surface(
                                color = if (pageManager.isAllDownloaded()) EmasKhidmat.copy(alpha = 0.18f) else MerahMerdeka.copy(alpha = 0.12f),
                                shape = CircleShape,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (pageManager.isAllDownloaded()) "💾" else "📥", fontSize = 14.sp)
                                }
                            }
                        }
                    }
                )
            }
        }

        // Fullscreen indicator pill (shown when fullscreen to let reader know the current page & juz)
        AnimatedVisibility(
            visible = isFullscreen,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.45f),
                shape = RoundedCornerShape(16.dp)
            ) {
                val sideText = if (currentPageNumber % 2 == 1) "Kanan" else "Kiri"
                Text(
                    text = "Hal. $currentPageNumber ($sideText) • Juz ${pageMeta.juz}",
                    color = PutihBersih,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        // Bottom Bar Overlay (Audio Player Bar if audio is active)
        AnimatedVisibility(
            visible = !isFullscreen && state.activeAudioTrack != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            MiniFloatingAudioBar(
                track = state.activeAudioTrack,
                playbackState = state.audioPlaybackState,
                currentPositionMs = state.audioPositionMs,
                durationMs = state.audioDurationMs,
                onPlayPauseClick = { viewModel.onIntent(QuranUiIntent.TogglePlayPauseAudio) },
                onBarClick = {},
                onCloseClick = { viewModel.onIntent(QuranUiIntent.StopAudio) }
            )
        }
    }

    // Go To Navigation Sheet
    if (showGoToSheet) {
        GoToSurahAyahSheet(
            surahs = state.surahs.ifEmpty { QuranData.surahs },
            initialSurahNumber = pageMeta.surahNumber,
            initialPageNumber = currentPageNumber,
            onDismiss = { showGoToSheet = false },
            onConfirm = { surahNumber, ayahNumber ->
                showGoToSheet = false
                val targetPage = QuranPageLookup.getPageForAyah(surahNumber, ayahNumber)
                selectedAyah = Pair(surahNumber, ayahNumber)
                coroutineScope.launch {
                    pagerState.animateScrollToPage(targetPage - 1)
                }
            },
            onConfirmPage = { targetPage ->
                showGoToSheet = false
                coroutineScope.launch {
                    pagerState.animateScrollToPage(targetPage - 1)
                }
            }
        )
    }

    // Download Mushaf Offline Sheet
    if (showDownloadSheet) {
        DownloadMushafSheet(
            downloadState = downloadState,
            onDismiss = { showDownloadSheet = false },
            onStartDownload = { pageManager.startFullDownload() },
            onCancelDownload = { pageManager.cancelDownload() },
            onDontShowAgainChecked = { dontShow ->
                appSettingsRepo.setHideMushafDownloadPrompt(dontShow)
            }
        )
    }

    // Ayah Options Sheet (When user taps an ayah)
    selectedAyah?.let { (surahNum, ayahNum) ->
        val surahMeta = remember(surahNum) { QuranData.surahs.find { it.number == surahNum } }
        val surahName = surahMeta?.nameLatin ?: "Surat $surahNum"
        val ayahModel = remember(surahNum, ayahNum) {
            QuranData.getAyahsForSurah(surahNum).find { it.numberInSurah == ayahNum }
        }

        val shareAction = rememberShareTextAction()
        val clipboardManager = LocalClipboardManager.current

        val isAyahLastRead = state.lastReadBookmark?.let {
            it.surahNumber == surahNum && it.ayahNumber == ayahNum
        } ?: false

        AyahOptionsSheet(
            surahName = surahName,
            ayahNumber = ayahNum,
            onDismiss = { selectedAyah = null },
            onPlayMurotal = {
                val audioUrl = ayahModel?.audioUrl ?: surahMeta?.audioUrl
                if (audioUrl != null) {
                    viewModel.onIntent(
                        QuranUiIntent.PlayAudio(
                            AudioTrack(
                                id = "ayah_${surahNum}_${ayahNum}",
                                title = "Murottal $surahName Ayat $ayahNum",
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
                        surahNumber = surahNum,
                        ayahNumber = ayahNum,
                        surahName = surahName,
                        showToast = true
                    )
                )
            },
            onShare = {
                if (ayahModel != null) {
                    val shareText = buildString {
                        append(ayahModel.textArabic)
                        append("\n\n")
                        if (ayahModel.transliterationLatin.isNotBlank()) {
                            append(ayahModel.transliterationLatin)
                            append("\n\n")
                        }
                        append(ayahModel.translationIndonesian)
                        append("\n\n")
                        append("(QS. $surahName: $ayahNum)")
                    }
                    shareAction(shareText)
                }
            },
            onCopy = {
                if (ayahModel != null) {
                    val copyText = "${ayahModel.textArabic}\n\n${ayahModel.translationIndonesian}\n\n(QS. $surahName: $ayahNum)"
                    clipboardManager.setText(AnnotatedString(copyText))
                }
            },
            isLastRead = isAyahLastRead
        )
    }
}
