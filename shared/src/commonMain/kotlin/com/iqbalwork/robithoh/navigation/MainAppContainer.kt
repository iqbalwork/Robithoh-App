package com.iqbalwork.robithoh.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.designsystem.component.MiniFloatingAudioBar
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.core.model.AudioPlaybackState
import com.iqbalwork.robithoh.feature.home.ui.*
import com.iqbalwork.robithoh.feature.library.ui.KitabTabContent
import com.iqbalwork.robithoh.feature.prayer.ui.SalatTabContent
import com.iqbalwork.robithoh.feature.profile.ui.ProfileTabContent

enum class MainTab(val title: String, val icon: String) {
    HOME("Home", "🏠"),
    SALAT("Sholat", "🕌"),
    KITAB("Al Quran", "📖"),
    PROFIL("Profil", "👤")
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
    audioPlayer: KmpAudioPlayer = remember { createAudioPlayer() }
) {
    // currentTab / activeSheet are hoisted to App() (the composable root that
    // NavDisplay never disposes) so they survive being navigated away from and
    // back to — MainAppContainer itself gets torn down and rebuilt by NavDisplay
    // whenever the backstack top changes away from ScreenKey.Home.
    BackHandler(enabled = activeSheet != null) {
        onSheetChange(null)
    }

    val currentTrack by audioPlayer.currentTrack.collectAsState()
    val playbackState by audioPlayer.playbackState.collectAsState()
    val currentPositionMs by audioPlayer.currentPositionMs.collectAsState()
    val durationMs by audioPlayer.durationMs.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBackgroundLight)
    ) {
        // Tab Content — fills the whole screen; each tab's own list scrolls
        // beneath the floating nav dock, which stays translucent so content
        // remains visible through it (see nav Surface below).
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
                        onOpenSheet = { onSheetChange(it) }
                    )
                }
                MainTab.SALAT -> {
                    SalatTabContent(
                        onNavigateToDocument = onNavigateToDocument
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
                MainTab.PROFIL -> {
                    ProfileTabContent(
                        onNavigateToLanggam = onNavigateToLanggam,
                        onNavigateToProfilePesantren = onNavigateToProfilePesantren
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

            // Modern Floating Pill Dock Navigation Bar — the pill itself
            // stays solid; only the area outside its rounded shape is
            // transparent, so content scrolling behind the dock is visible
            // around it without washing out the dock's own legibility.
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
                                    MainTab.PROFIL -> Color(0xFFFFD6E8)
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
    // Note: onItemClick intentionally does NOT clear activeSheet — the sheet
    // stays "open" in saved state while the destination screen is on top of
    // the backstack, so it reappears automatically when the user navigates back.
    when (activeSheet) {
        "manaqib" -> {
            ManaqibModalBottomSheet(
                onItemClick = { docId -> onNavigateToDocument(docId) },
                onDismiss = { onSheetChange(null) }
            )
        }
        "sholat" -> {
            SholatModalBottomSheet(
                onItemClick = { docId -> onNavigateToDocument(docId) },
                onDismiss = { onSheetChange(null) }
            )
        }
        "sholawat" -> {
            SholawatModalBottomSheet(
                onItemClick = { docId -> onNavigateToDocument(docId) },
                onDismiss = { onSheetChange(null) }
            )
        }
        "tahlil" -> {
            TahlilZiyarohModalBottomSheet(
                onItemClick = { docId -> onNavigateToDocument(docId) },
                onDismiss = { onSheetChange(null) }
            )
        }
        "doa" -> {
            DoaModalBottomSheet(
                onItemClick = { docId -> onNavigateToDocument(docId) },
                onDismiss = { onSheetChange(null) }
            )
        }
    }
}
