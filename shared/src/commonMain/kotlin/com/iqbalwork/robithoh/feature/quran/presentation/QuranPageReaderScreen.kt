package com.iqbalwork.robithoh.feature.quran.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.PageTurnMath
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.PageTurnPerformanceGuard
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.PageTurnState
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.PageTurnStyle
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.SpineSide
import com.iqbalwork.robithoh.core.designsystem.component.pageturn.TurnDirection
import com.iqbalwork.robithoh.navigation.BackHandler
import com.iqbalwork.robithoh.core.designsystem.component.AyahOptionsSheet
import com.iqbalwork.robithoh.core.designsystem.component.DownloadMushafSheet
import com.iqbalwork.robithoh.core.designsystem.component.GoToSurahAyahSheet
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.component.SpotlightOverlay
import com.iqbalwork.robithoh.core.designsystem.component.SpotlightShapeType
import com.iqbalwork.robithoh.core.designsystem.component.SpotlightStep
import com.iqbalwork.robithoh.core.designsystem.component.rememberSpotlightState
import com.iqbalwork.robithoh.core.designsystem.component.spotlightAnchor
import com.iqbalwork.robithoh.core.designsystem.rememberShareTextAction
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.device.rememberScreenOrientationController
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.core.settings.rememberAppSettingsRepository
import com.iqbalwork.robithoh.feature.quran.data.QuranData
import com.iqbalwork.robithoh.feature.quran.data.QuranPageLookup
import com.iqbalwork.robithoh.feature.quran.data.QuranPageManager
import com.iqbalwork.robithoh.feature.quran.model.QuranPageMapping
import com.iqbalwork.robithoh.feature.quran.presentation.component.MushafPageView
import com.iqbalwork.robithoh.feature.quran.ui.QariPickerSheet
import com.iqbalwork.robithoh.feature.quran.ui.QuranAudioBottomBar
import kotlinx.coroutines.launch
import kotlin.math.floor

// Threshold (dp) di mana mode dual-page diaktifkan.
// 600dp = Window size Medium: mencakup foldable terbuka (portrait) & tablet.
private val DUAL_PAGE_WIDTH_THRESHOLD = 600.dp

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
    // Memastikan halaman Mushaf Quran Page selalu tampil dalam mode terang (light mode)
    RabithohTheme(darkTheme = false) {
        val state by viewModel.uiState.collectAsState()
    val isDark = RabithohTheme.colors.isDark
    val coroutineScope = rememberCoroutineScope()
    val pageManager = remember { QuranPageManager() }
    val appSettingsRepo = rememberAppSettingsRepository()
    val appSettings by appSettingsRepo.settings.collectAsState()

    // Fullscreen state: starts in fullscreen as requested
    var isFullscreen by rememberSaveable { mutableStateOf(true) }

    // Spotlight: 3 langkah panduan mode Mushaf
    val mushafSpotlightSteps = remember {
        listOf(
            SpotlightStep(
                id = "mushaf_goto",
                title = "Navigasi Cepat Antar Halaman",
                description = "Lompat langsung ke halaman, surah, atau ayat tertentu dengan presisi—tanpa perlu menggeser satu per satu.",
                shapeType = SpotlightShapeType.CIRCLE,
                padding = 6.dp
            ),
            SpotlightStep(
                id = "mushaf_switch",
                title = "Beralih ke Mode Teks",
                description = "Pindah ke tampilan teks ayat per ayat dengan terjemahan Indonesia & transliterasi Latin, murottal, dan penanda bookmark.",
                shapeType = SpotlightShapeType.CIRCLE,
                padding = 6.dp
            ),
            SpotlightStep(
                id = "mushaf_download",
                title = "Download Mushaf Offline",
                description = "Unduh seluruh halaman mushaf ke perangkat agar dapat dibaca lancar tanpa koneksi internet kapan pun dan di mana pun.",
                shapeType = SpotlightShapeType.CIRCLE,
                padding = 6.dp
            )
        )
    }

    val mushafSpotlightState = rememberSpotlightState(
        steps = mushafSpotlightSteps,
        onComplete = {
            appSettingsRepo.setQuranPageSpotlightSeen(true)
        }
    )

    // Trigger spotlight saat toolbar terlihat (tidak fullscreen) dan belum pernah ditampilkan
    LaunchedEffect(appSettings.hasSeenQuranPageSpotlight, isFullscreen) {
        if (!appSettings.hasSeenQuranPageSpotlight && !isFullscreen && !mushafSpotlightState.isVisible) {
            mushafSpotlightState.start()
        }
    }

    val orientationController = rememberScreenOrientationController()
    val handleBack = {
        orientationController.resetToDefault()
        onBackClick()
    }

    BackHandler {
        handleBack()
    }

    // Page Turn state & performance guard
    var pageTurnStyle by rememberSaveable { mutableStateOf(PageTurnStyle.CURL) }
    val performanceGuard = remember { PageTurnPerformanceGuard() }

    DisposableEffect(Unit) {
        onDispose {
            orientationController.resetToDefault()
        }
    }

    // Dialog & sheet visibility
    var showGoToSheet by rememberSaveable { mutableStateOf(false) }
    var showDownloadSheet by rememberSaveable { mutableStateOf(false) }
    var hasCheckedDownloadPrompt by rememberSaveable { mutableStateOf(false) }
    val downloadState by pageManager.downloadState.collectAsState()

    // Selected Ayah for options sheet
    var selectedAyah by remember { mutableStateOf<Pair<Int, Int>?>(null) }

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

    // Check whether to show first-time download suggestion (only once per session)
    LaunchedEffect(Unit) {
        if (!hasCheckedDownloadPrompt) {
            hasCheckedDownloadPrompt = true
            if (!appSettings.hideMushafDownloadPrompt && !pageManager.isAllDownloaded()) {
                showDownloadSheet = true
            }
        }
    }

    // Lacak halaman terakhir yang dilihat agar folding / unfolding transisi mulus
    var lastViewedPage by rememberSaveable { mutableStateOf(initialPageNumber) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isDualPage = maxWidth >= DUAL_PAGE_WIDTH_THRESHOLD
        val totalPages = QuranPageLookup.TOTAL_PAGES

        val initialIndex = remember(isDualPage) {
            if (isDualPage) {
                ((lastViewedPage - 1) / 2).coerceIn(0, (totalPages + 1) / 2 - 1)
            } else {
                (lastViewedPage - 1).coerceIn(0, totalPages - 1)
            }
        }
        val pagerPageCount = if (isDualPage) (totalPages + 1) / 2 else totalPages
        val pagerState = rememberPagerState(initialPage = initialIndex) { pagerPageCount }

        // Lacak halaman awal sebelum gesture scroll dimulai agar arah curl tidak terbalik di tengah drag
        var gestureStartPage by remember { mutableStateOf(initialIndex) }
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.isScrollInProgress }
                .collect { inProgress ->
                    if (!inProgress) {
                        gestureStartPage = pagerState.currentPage
                    }
                }
        }

        // currentPageNumber = nomor halaman ganjil (kanan) yang sedang aktif
        val currentPageNumber = if (isDualPage) {
            pagerState.currentPage * 2 + 1
        } else {
            pagerState.currentPage + 1
        }
        val pageMeta = remember(currentPageNumber) { QuranPageLookup.getPageMeta(currentPageNumber) }

        // Inisialisasi selectedAyah jika initialAyahNumber diberikan
        LaunchedEffect(Unit) {
            if (initialAyahNumber != null) {
                selectedAyah = Pair(pageMeta.surahNumber, initialAyahNumber)
            }
        }

        // Sinkronisasi lastViewedPage saat user swipe pager
        LaunchedEffect(pagerState.currentPage, isDualPage) {
            lastViewedPage = if (isDualPage) {
                pagerState.currentPage * 2 + 1
            } else {
                pagerState.currentPage + 1
            }
        }

        // Auto-scroll pager jika active audio berganti ke halaman lain
        LaunchedEffect(activeAudioAyah) {
            if (activeAudioAyah != null) {
                val targetPage = QuranPageLookup.getPageForAyah(activeAudioAyah.first, activeAudioAyah.second)
                val targetIndex = if (isDualPage) (targetPage - 1) / 2 else targetPage - 1
                if (targetIndex != pagerState.currentPage) {
                    pagerState.animateScrollToPage(targetIndex)
                }
            }
        }

        // Prefetch halaman tetangga
        LaunchedEffect(currentPageNumber) {
            pageManager.prefetchPages(currentPageNumber)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDark) DarkCanvas else Color(0xFFFBF9F4))
        ) {
            HorizontalPager(
                state = pagerState,
                reverseLayout = true,
                beyondViewportPageCount = 1,
                modifier = Modifier.fillMaxSize()
            ) { pagerIndex ->
                if (isDualPage) {
                    // DUAL-PAGE MODE (Foldable Unfolded / Tablet)
                    val rightPageNum = pagerIndex * 2 + 1
                    val leftPageNum = pagerIndex * 2 + 2
                    val hasLeftPage = leftPageNum <= totalPages

                    var rightImage by remember(rightPageNum) { mutableStateOf<ImageBitmap?>(null) }
                    var leftImage by remember(leftPageNum) { mutableStateOf<ImageBitmap?>(null) }
                    var rightMapping by remember(rightPageNum) { mutableStateOf<QuranPageMapping?>(null) }
                    var leftMapping by remember(leftPageNum) { mutableStateOf<QuranPageMapping?>(null) }

                    LaunchedEffect(rightPageNum) {
                        rightImage = pageManager.loadPageImage(rightPageNum)
                        rightMapping = pageManager.getPageMapping(rightPageNum)
                    }
                    LaunchedEffect(leftPageNum) {
                        if (hasLeftPage) {
                            leftImage = pageManager.loadPageImage(leftPageNum)
                            leftMapping = pageManager.getPageMapping(leftPageNum)
                        }
                    }

                    val isCurrentSpread = (pagerIndex == pagerState.currentPage)

                    // Continuous Coordinate Spread Tracking for Dual-Page (Foldable / Tablet)
                    val currentPos = pagerState.currentPage + pagerState.currentPageOffsetFraction
                    val floorSpread = floor(currentPos.toDouble()).toInt().coerceIn(0, pagerPageCount - 1)
                    val curlProgress = (currentPos - floorSpread).coerceIn(0f, 1f)
                    val isTransitioning = (curlProgress > 0.001f && curlProgress < 0.999f)
                    val isCurlEligible = (pageTurnStyle == PageTurnStyle.CURL)

                    val isTopCurlingSpread = isCurlEligible && isTransitioning && (pagerIndex == floorSpread)
                    val isUnderlyingSpread = isCurlEligible && isTransitioning && (pagerIndex == floorSpread + 1)
                    val shouldNeutralize = isTopCurlingSpread || isUnderlyingSpread

                    val leftPageTurnState = if (isTopCurlingSpread) {
                        PageTurnState(
                            progress = curlProgress,
                            spineSide = SpineSide.RIGHT,
                            direction = if (pagerState.currentPageOffsetFraction < 0f) TurnDirection.BACKWARD else TurnDirection.FORWARD,
                            activeTier = performanceGuard.activeTier
                        )
                    } else null

                    val rightPageAlpha = if (isTopCurlingSpread) {
                        if (curlProgress <= 0.4f) 1f else (1f - ((curlProgress - 0.4f) / 0.6f)).coerceIn(0f, 1f)
                    } else 1f

                    val spreadZIndex = when {
                        isTopCurlingSpread -> 1f
                        isUnderlyingSpread -> 0f
                        else -> 0f
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(spreadZIndex)
                            .then(
                                if (shouldNeutralize) {
                                    Modifier.graphicsLayer {
                                        translationX = ((pagerIndex - pagerState.currentPage) - pagerState.currentPageOffsetFraction) * size.width
                                    }
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            // Halaman Kiri (Genap)
                            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                                if (hasLeftPage) {
                                    MushafPageView(
                                        pageNumber = leftPageNum,
                                        pageMapping = leftMapping,
                                        pageImage = leftImage,
                                        selectedAyah = if (isCurrentSpread) selectedAyah else null,
                                        activeAudioAyah = if (isCurrentSpread) activeAudioAyah else null,
                                        onAyahClick = { surah, ayah -> selectedAyah = Pair(surah, ayah) },
                                        onBackgroundClick = { isFullscreen = !isFullscreen },
                                        onDoubleTap = { orientationController.toggleOrientation() },
                                        onPinchOut = { orientationController.setLandscape() },
                                        onPinchIn = { orientationController.setPortrait() },
                                        pageTurnState = leftPageTurnState,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    if (!isTopCurlingSpread || curlProgress <= 0.05f) {
                                        SpineEdgeShadow(side = SpineSide.RIGHT)
                                    }
                                }
                            }

                            // Garis Jilid Tengah (Spine)
                            SpineCenterDivider(isDark = isDark)

                            // Halaman Kanan (Ganjil)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .graphicsLayer { alpha = rightPageAlpha }
                            ) {
                                MushafPageView(
                                    pageNumber = rightPageNum,
                                    pageMapping = rightMapping,
                                    pageImage = rightImage,
                                    selectedAyah = if (isCurrentSpread) selectedAyah else null,
                                    activeAudioAyah = if (isCurrentSpread) activeAudioAyah else null,
                                    onAyahClick = { surah, ayah -> selectedAyah = Pair(surah, ayah) },
                                    onBackgroundClick = { isFullscreen = !isFullscreen },
                                    onDoubleTap = { orientationController.toggleOrientation() },
                                    onPinchOut = { orientationController.setLandscape() },
                                    onPinchIn = { orientationController.setPortrait() },
                                    modifier = Modifier.fillMaxSize()
                                )

                                SpineEdgeShadow(side = SpineSide.LEFT)
                            }
                        }
                    }
                } else {
                    // SINGLE-PAGE MODE (Phone)
                    val pageNum = pagerIndex + 1
                    var pageImage by remember(pageNum) { mutableStateOf<ImageBitmap?>(null) }
                    var pageMapping by remember(pageNum) { mutableStateOf<QuranPageMapping?>(null) }

                    LaunchedEffect(pageNum) {
                        pageImage = pageManager.loadPageImage(pageNum)
                        pageMapping = pageManager.getPageMapping(pageNum)
                    }

                    val isRightPage = (pageNum % 2 == 1)

                    // Page Turn & Curl Calculation via Continuous Coordinate Math
                    val currentPos = pagerState.currentPage + pagerState.currentPageOffsetFraction
                    val floorPage = floor(currentPos.toDouble()).toInt().coerceIn(0, totalPages - 1)
                    val rawProgress = (currentPos - floorPage).coerceIn(0f, 1f)
                    val isTransitioning = (rawProgress > 0.001f && rawProgress < 0.999f)

                    // In a physical mushaf:
                    // - Intra-spread transitions (1 <-> 2, 3 <-> 4, 5 <-> 6, ...) slide across the open spread.
                    // - Inter-spread transitions (2 <-> 3, 4 <-> 5, 6 <-> 7, ...) flip the physical paper leaf (3D curl).
                    val isSpreadTurn = PageTurnMath.isSpreadTurn(floorPage)
                    val isCurlEligible = (pageTurnStyle == PageTurnStyle.CURL && !isDualPage && isSpreadTurn)

                    // Symmetrical Direction Tracking:
                    // Determine which page originated the transition:
                    // - Forward (e.g. Page 4 to 5): gestureStartPage <= floorPage (Page 4).
                    //   Page 4 curls towards SpineSide.RIGHT (rawProgress 0 -> 1), Page 5 is revealed underneath.
                    // - Backward (e.g. Page 5 to 4): gestureStartPage > floorPage (Page 5).
                    //   Page 5 curls towards SpineSide.LEFT (progress 1 - rawProgress: 0 -> 1), Page 4 is revealed underneath!
                    val isCurlingFloorPage = (gestureStartPage <= floorPage)

                    val curlingPageIndex = if (isCurlingFloorPage) floorPage else (floorPage + 1)
                    val underlyingPageIndex = if (isCurlingFloorPage) (floorPage + 1) else floorPage
                    val curlProgress = if (isCurlingFloorPage) rawProgress else (1f - rawProgress)

                    val pageSpineSide = if (isRightPage) SpineSide.LEFT else SpineSide.RIGHT

                    val isTopCurlingLeaf = isCurlEligible && isTransitioning && (pagerIndex == curlingPageIndex)
                    val isUnderlyingLeaf = isCurlEligible && isTransitioning && (pagerIndex == underlyingPageIndex)
                    val shouldNeutralize = isTopCurlingLeaf || isUnderlyingLeaf

                    val pageTurnState = if (isTopCurlingLeaf) {
                        PageTurnState(
                            progress = curlProgress,
                            spineSide = pageSpineSide,
                            direction = if (isCurlingFloorPage) TurnDirection.FORWARD else TurnDirection.BACKWARD,
                            activeTier = performanceGuard.activeTier
                        )
                    } else null

                    val itemZIndex = when {
                        isTopCurlingLeaf -> 1f
                        isUnderlyingLeaf -> 0f
                        else -> 0f
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(itemZIndex)
                            .then(
                                if (shouldNeutralize) {
                                    Modifier.graphicsLayer {
                                        // Counteract default horizontal slide translation so pages stay pinned at (0, 0)
                                        translationX = ((pagerIndex - pagerState.currentPage) - pagerState.currentPageOffsetFraction) * size.width
                                    }
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        MushafPageView(
                            pageNumber = pageNum,
                            pageMapping = pageMapping,
                            pageImage = pageImage,
                            selectedAyah = if (currentPageNumber == pageNum) selectedAyah else null,
                            activeAudioAyah = if (currentPageNumber == pageNum) activeAudioAyah else null,
                            onAyahClick = { surah, ayah -> selectedAyah = Pair(surah, ayah) },
                            onBackgroundClick = { isFullscreen = !isFullscreen },
                            onDoubleTap = { orientationController.toggleOrientation() },
                            onPinchOut = { orientationController.setLandscape() },
                            onPinchIn = { orientationController.setPortrait() },
                            pageTurnState = pageTurnState,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Bayangan Tulang Jilid Mushaf Fisik (hanya ditampilkan saat flat atau pada halaman di bawah)
                        if (!isTopCurlingLeaf || curlProgress <= 0.05f) {
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

                            // Garis lipatan jahitan jilid (Spine stitch groove)
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.5.dp)
                                    .align(if (isRightPage) Alignment.CenterStart else Alignment.CenterEnd)
                                    .background(Color.Black.copy(alpha = 0.15f))
                            )

                            // Garis potong tepi luar kertas
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                                    .align(if (isRightPage) Alignment.CenterEnd else Alignment.CenterStart)
                                    .background(Color.Black.copy(alpha = 0.05f))
                            )
                        }
                    }
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
                    color = if (isDark) DarkSurface.copy(alpha = 0.95f) else PutihBersih.copy(alpha = 0.95f),
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IslamicHeader(
                        title = pageMeta.surahName,
                        arabicTitle = pageMeta.arabicSurahName,
                        onBackClick = handleBack,
                        showBottomDivider = false,
                        actions = {
                            IconButton(
                                onClick = {
                                    val targetSurah = selectedAyah?.first ?: pageMeta.surahNumber
                                    val targetAyah = selectedAyah?.second ?: pageMeta.startAyah
                                    onSwitchToTextMode(targetSurah, targetAyah)
                                },
                                modifier = Modifier.spotlightAnchor(mushafSpotlightState, "mushaf_switch")
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

                            IconButton(
                                onClick = { showGoToSheet = true },
                                modifier = Modifier.spotlightAnchor(mushafSpotlightState, "mushaf_goto")
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
                                onClick = { showDownloadSheet = true },
                                modifier = Modifier.spotlightAnchor(mushafSpotlightState, "mushaf_download")
                            ) {
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

                            IconButton(
                                onClick = {
                                    pageTurnStyle = when (pageTurnStyle) {
                                        PageTurnStyle.CURL -> PageTurnStyle.SLIDE
                                        PageTurnStyle.SLIDE -> PageTurnStyle.NONE
                                        PageTurnStyle.NONE -> PageTurnStyle.CURL
                                    }
                                }
                            ) {
                                Surface(
                                    color = MerahMerdeka.copy(alpha = 0.12f),
                                    shape = CircleShape,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            when (pageTurnStyle) {
                                                PageTurnStyle.CURL -> "📖"
                                                PageTurnStyle.SLIDE -> "↔️"
                                                PageTurnStyle.NONE -> "⚡"
                                            },
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Fullscreen indicator pill
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
                    val pillText = if (isDualPage) {
                        val rightPage = pagerState.currentPage * 2 + 1
                        val leftPage = (rightPage + 1).coerceAtMost(totalPages)
                        "Hal. $rightPage–$leftPage • Juz ${pageMeta.juz}"
                    } else {
                        val sideText = if (currentPageNumber % 2 == 1) "Kanan" else "Kiri"
                        "Hal. $currentPageNumber ($sideText) • Juz ${pageMeta.juz}"
                    }
                    Text(
                        text = pillText,
                        color = PutihBersih,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            val navBarBottomInset = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

            // Bottom Bar Overlay (Floating Audio Bar)
            AnimatedVisibility(
                visible = !isFullscreen,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
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
                    activeAyahNumber = activeAudioAyah?.second ?: selectedAyah?.second,
                    surahName = pageMeta.surahName,
                    onPlayPauseClick = {
                        if (state.audioPlaybackState == AudioPlaybackState.PLAYING ||
                            state.audioPlaybackState == AudioPlaybackState.PAUSED
                        ) {
                            viewModel.onIntent(QuranUiIntent.TogglePlayPauseAudio)
                        } else {
                            val targetSurah = selectedAyah?.first ?: pageMeta.surahNumber
                            val targetAyah = selectedAyah?.second ?: pageMeta.startAyah
                            viewModel.onIntent(QuranUiIntent.PlayAyahAudio(targetSurah, targetAyah))
                        }
                    },
                    onToggleRepeatMode = { viewModel.onIntent(QuranUiIntent.ToggleAudioRepeatMode) },
                    onQariClick = { viewModel.onIntent(QuranUiIntent.SetQariPickerVisible(true)) }
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
                    val targetIndex = if (isDualPage) (targetPage - 1) / 2 else targetPage - 1
                    selectedAyah = Pair(surahNumber, ayahNumber)
                    lastViewedPage = targetPage
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(targetIndex)
                    }
                },
                onConfirmPage = { targetPage ->
                    showGoToSheet = false
                    val targetIndex = if (isDualPage) (targetPage - 1) / 2 else targetPage - 1
                    lastViewedPage = targetPage
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(targetIndex)
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

        // Ayah Options Sheet
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
                    viewModel.onIntent(
                        QuranUiIntent.PlayAyahAudio(
                            surahNumber = surahNum,
                            ayahNumber = ayahNum
                        )
                    )
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

    // Spotlight overlay for Mushaf mode tutorial
    SpotlightOverlay(state = mushafSpotlightState)

    // Qari Picker Bottom Sheet
    if (state.isQariPickerVisible) {
        QariPickerSheet(
            selectedQari = state.selectedQari,
            availableQaris = state.availableQaris,
            onSelectQari = { qari ->
                viewModel.onIntent(QuranUiIntent.SelectQari(qari))
                viewModel.onIntent(QuranUiIntent.SetQariPickerVisible(false))
            },
            onDismiss = {
                viewModel.onIntent(QuranUiIntent.SetQariPickerVisible(false))
            }
        )
    }
    }
}

// ── Helper: sisi bayangan jilid ──────────────────────────────────────────────

@Composable
private fun BoxScope.SpineEdgeShadow(side: SpineSide, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(28.dp)
            .align(if (side == SpineSide.LEFT) Alignment.CenterStart else Alignment.CenterEnd)
            .background(
                Brush.horizontalGradient(
                    when (side) {
                        SpineSide.LEFT -> listOf(
                            Color.Black.copy(alpha = 0.20f),
                            Color.Black.copy(alpha = 0.07f),
                            Color.Black.copy(alpha = 0.01f),
                            Color.Transparent
                        )
                        SpineSide.RIGHT -> listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.01f),
                            Color.Black.copy(alpha = 0.07f),
                            Color.Black.copy(alpha = 0.20f)
                        )
                    }
                )
            )
    )
}

/** Garis jilid tengah antara dua halaman dalam dual-page mode. */
@Composable
private fun SpineCenterDivider(isDark: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(3.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Black.copy(alpha = if (isDark) 0.35f else 0.18f),
                        Color.Black.copy(alpha = if (isDark) 0.50f else 0.28f),
                        Color.Black.copy(alpha = if (isDark) 0.35f else 0.18f)
                    )
                )
            )
    )
}
