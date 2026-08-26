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
    onNavigateToDocument: (String) -> Unit,
    onNavigateToSurah: (Int) -> Unit,
    onNavigateToLanggam: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToProfilePesantren: () -> Unit,
    audioPlayer: KmpAudioPlayer = remember { createAudioPlayer() }
) {
    var currentTab by remember { mutableStateOf(MainTab.HOME) }
    var activeSheet by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = activeSheet != null) {
        activeSheet = null
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
        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 76.dp)
        ) {
            when (currentTab) {
                MainTab.HOME -> {
                    HomeTabContent(
                        onNavigateToDocument = onNavigateToDocument,
                        onNavigateToLanggam = onNavigateToLanggam,
                        onNavigateToTasbih = onNavigateToTasbih,
                        onNavigateToProfile = { currentTab = MainTab.PROFIL },
                        onOpenSheet = { activeSheet = it }
                    )
                }
                MainTab.SALAT -> {
                    SalatTabContent(
                        onNavigateToDocument = onNavigateToDocument
                    )
                }
                MainTab.KITAB -> {
                    KitabTabContent(
                        onNavigateToSurah = onNavigateToSurah
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
                                    MainTab.PROFIL -> Color(0xFFFFD6E8)
                                }
                            } else Color.Transparent,
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .clickable { currentTab = tab }
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
                    activeSheet = null
                    onNavigateToDocument(docId)
                },
                onDismiss = { activeSheet = null }
            )
        }
        "sholat" -> {
            SholatModalBottomSheet(
                onItemClick = { docId ->
                    activeSheet = null
                    onNavigateToDocument(docId)
                },
                onDismiss = { activeSheet = null }
            )
        }
        "sholawat" -> {
            SholawatModalBottomSheet(
                onItemClick = { docId ->
                    activeSheet = null
                    onNavigateToDocument(docId)
                },
                onDismiss = { activeSheet = null }
            )
        }
        "tahlil" -> {
            TahlilZiyarohModalBottomSheet(
                onItemClick = { docId ->
                    activeSheet = null
                    onNavigateToDocument(docId)
                },
                onDismiss = { activeSheet = null }
            )
        }
        "doa" -> {
            DoaModalBottomSheet(
                onItemClick = { docId ->
                    activeSheet = null
                    onNavigateToDocument(docId)
                },
                onDismiss = { activeSheet = null }
            )
        }
    }
}
