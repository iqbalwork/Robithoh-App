package com.iqbalwork.robithoh.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.designsystem.component.MiniFloatingAudioBar
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.location.rememberLocationPermissionLauncher
import com.iqbalwork.robithoh.core.location.rememberLocationProvider
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import com.iqbalwork.robithoh.feature.home.ui.DoaModalBottomSheet
import com.iqbalwork.robithoh.feature.home.ui.HomeTabContent
import com.iqbalwork.robithoh.feature.home.ui.ManaqibModalBottomSheet
import com.iqbalwork.robithoh.feature.home.ui.SholatModalBottomSheet
import com.iqbalwork.robithoh.feature.home.ui.SholawatModalBottomSheet
import com.iqbalwork.robithoh.feature.home.ui.TahlilZiyarohModalBottomSheet
import com.iqbalwork.robithoh.feature.library.ui.KitabTabContent
import com.iqbalwork.robithoh.feature.prayer.ui.SalatTabContent
import com.iqbalwork.robithoh.feature.profile.ui.SettingsTabContent
import kotlinx.coroutines.launch

enum class MainTab(val title: String, val icon: String) {
    HOME("Beranda", "🏠"),
    SALAT("Sholat", "🕌"),
    KITAB("Al Quran", "📖"),
    PENGATURAN("Pengaturan", "⚙️")
}

@Composable
fun MainAppContainer(
    currentTab: MainTab,
    onTabChange: (MainTab) -> Unit,
    activeSheet: String?,
    onSheetChange: (String?) -> Unit,
    onNavigateToDocument: (String) -> Unit,
    onNavigateToSurah: (Int, Int?) -> Unit,
    onNavigateToLanggam: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToProfilePesantren: () -> Unit,
    onNavigateToCalculationMethods: () -> Unit = {},
    onNavigateToPrayerAdjustments: () -> Unit = {},
    amaliyahViewModel: AmaliyahViewModel,
    audioPlayer: KmpAudioPlayer = remember { createAudioPlayer() },
    audioDownloader: com.iqbalwork.robithoh.core.audio.AudioDownloader = remember { com.iqbalwork.robithoh.core.audio.createAudioDownloader() },
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {}
) {
    val currentTrack by audioPlayer.currentTrack.collectAsState()
    val playbackState by audioPlayer.playbackState.collectAsState()
    val currentPositionMs by audioPlayer.currentPositionMs.collectAsState()
    val durationMs by audioPlayer.durationMs.collectAsState()
    val downloadState by audioDownloader.downloadState.collectAsState()

    val scope = rememberCoroutineScope()
    val locationProvider = rememberLocationProvider()

    val fetchGps = {
        scope.launch {
            amaliyahViewModel.onIntent(AmaliyahUiIntent.SetFetchingLocation(true))
            val loc = locationProvider.getCurrentLocation()
            if (loc != null) {
                amaliyahViewModel.onIntent(AmaliyahUiIntent.SetGpsLocation(loc))
            } else {
                amaliyahViewModel.onIntent(AmaliyahUiIntent.SetLocationError("Gagal mendeteksi lokasi GPS."))
            }
        }
    }

    val requestPermissionAndFetch = rememberLocationPermissionLauncher { granted ->
        if (granted) {
            fetchGps()
        }
    }

    // Auto-fetch GPS or request location on first launch and whenever entering HOME tab
    LaunchedEffect(currentTab) {
        if (currentTab == MainTab.HOME) {
            if (locationProvider.hasLocationPermission()) {
                fetchGps()
            } else {
                requestPermissionAndFetch()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBackgroundLight)
    ) {
        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (currentTab) {
                MainTab.HOME -> {
                    HomeTabContent(
                        onNavigateToDocument = onNavigateToDocument,
                        onNavigateToLanggam = onNavigateToLanggam,
                        onNavigateToTasbih = onNavigateToTasbih,
                        onNavigateToPrayerTimes = { onTabChange(MainTab.SALAT) },
                        onOpenSheet = { onSheetChange(it) },
                        viewModel = amaliyahViewModel
                    )
                }
                MainTab.SALAT -> {
                    SalatTabContent(
                        onNavigateToDocument = onNavigateToDocument,
                        onNavigateToCalculationMethods = onNavigateToCalculationMethods,
                        onNavigateToPrayerAdjustments = onNavigateToPrayerAdjustments,
                        viewModel = amaliyahViewModel
                    )
                }
                MainTab.KITAB -> {
                    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
                    val quranViewModel = remember(database) {
                        com.iqbalwork.robithoh.feature.quran.presentation.QuranViewModel(
                            com.iqbalwork.robithoh.feature.quran.data.QuranRepositoryImpl(database)
                        )
                    }
                    val quranState by quranViewModel.uiState.collectAsState()
                    KitabTabContent(
                        onNavigateToSurah = onNavigateToSurah,
                        lastReadBookmark = quranState.lastReadBookmark
                    )
                }
                MainTab.PENGATURAN -> {
                    SettingsTabContent(
                        isDarkMode = isDarkMode,
                        onDarkModeChange = onDarkModeChange
                    )
                }
            }
        }

        // Floating Audio Bar (if playing) + Floating Navigation Dock Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            com.iqbalwork.robithoh.core.designsystem.component.FloatingDownloadBar(
                downloadState = downloadState,
                onCancelClick = { trackId -> audioDownloader.cancelDownload(trackId) }
            )

            MiniFloatingAudioBar(
                track = currentTrack,
                playbackState = playbackState,
                currentPositionMs = currentPositionMs,
                durationMs = durationMs,
                onPlayPauseClick = {
                    if (playbackState == AudioPlaybackState.PLAYING) {
                        audioPlayer.pause()
                    } else {
                        audioPlayer.resume()
                    }
                },
                onBarClick = onNavigateToLanggam,
                onCloseClick = { audioPlayer.stop() }
            )

            // Modern Floating Pill Dock Navigation Bar
            Surface(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .border(2.dp, Color(0xFF2C2523), RoundedCornerShape(32.dp)),
                color = Color.White,
                shape = RoundedCornerShape(32.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    MainTab.entries.forEach { tab ->
                        val isSelected = currentTab == tab

                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = if (isSelected) {
                                when (tab) {
                                    MainTab.HOME -> Color(0xFFFFE5D0)
                                    MainTab.SALAT -> Color(0xFFD0EDFF)
                                    MainTab.KITAB -> Color(0xFFD5F5E3)
                                    MainTab.PENGATURAN -> Color(0xFFEDE7F6)
                                }
                            } else Color.Transparent,
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .clickable { onTabChange(tab) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(tab.icon, fontSize = 18.sp)
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = tab.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextCharcoal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheets
    when (activeSheet) {
        "manaqib" -> {
            ManaqibModalBottomSheet(
                onItemClick = { docId ->
                    onNavigateToDocument(docId)
                },
                onDismiss = { onSheetChange(null) }
            )
        }
        "sholat" -> {
            SholatModalBottomSheet(
                onItemClick = { docId ->
                    onNavigateToDocument(docId)
                },
                onDismiss = { onSheetChange(null) }
            )
        }
        "sholawat" -> {
            SholawatModalBottomSheet(
                onItemClick = { docId ->
                    onNavigateToDocument(docId)
                },
                onDismiss = { onSheetChange(null) }
            )
        }
        "tahlil" -> {
            TahlilZiyarohModalBottomSheet(
                onItemClick = { docId ->
                    onNavigateToDocument(docId)
                },
                onDismiss = { onSheetChange(null) }
            )
        }
        "doa" -> {
            DoaModalBottomSheet(
                onItemClick = { docId ->
                    onNavigateToDocument(docId)
                },
                onDismiss = { onSheetChange(null) }
            )
        }
    }
}
