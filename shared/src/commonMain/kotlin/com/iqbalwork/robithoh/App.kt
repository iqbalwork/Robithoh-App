package com.iqbalwork.robithoh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.navigation.*

@Composable
fun App() {
    RabithohTheme {
        com.iqbalwork.robithoh.core.designsystem.InitHapticContext()
        val backstack = rememberSaveable(saver = ScreenKeyListSaver) {
            mutableStateListOf<ScreenKey>(ScreenKey.Home)
        }

        val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
        val alarmScheduler = com.iqbalwork.robithoh.core.notification.rememberPrayerAlarmScheduler()
        val amaliyahViewModel: com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel = viewModel {
            com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel(
                database = database,
                alarmScheduler = alarmScheduler
            )
        }
        val sharedAudioPlayer = remember { com.iqbalwork.robithoh.core.audio.createAudioPlayer() }

        // Hoisted here (App() is the true root — never disposed by NavDisplay)
        // so the Home tab/sheet selection survives navigating away and back and configuration changes
        var homeTab by rememberSaveable { mutableStateOf(MainTab.HOME) }
        var homeActiveSheet by rememberSaveable { mutableStateOf<String?>(null) }

        BackHandler(enabled = backstack.size > 1) {
            if (backstack.size > 1) {
                backstack.removeAt(backstack.lastIndex)
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavDisplay(
                backstack = backstack,
                onBack = {
                    if (backstack.size > 1) {
                        backstack.removeAt(backstack.lastIndex)
                    }
                },
                entryProvider = { key ->
                    when (key) {
                        is ScreenKey.Home -> {
                            MainAppContainer(
                                currentTab = homeTab,
                                onTabChange = { homeTab = it },
                                activeSheet = homeActiveSheet,
                                onSheetChange = { homeActiveSheet = it },
                                onNavigateToDocument = { docId ->
                                    if (docId == "quran_list") {
                                        backstack.add(ScreenKey.QuranList)
                                    } else {
                                        backstack.add(ScreenKey.DocumentReader(docId))
                                    }
                                },
                                onNavigateToSurah = { surahNumber, ayahNumber ->
                                    backstack.add(ScreenKey.QuranSurah(surahNumber, ayahNumber))
                                },
                                onNavigateToLanggam = { backstack.add(ScreenKey.Langgam) },
                                onNavigateToTasbih = { backstack.add(ScreenKey.Tasbih) },
                                onNavigateToProfilePesantren = { backstack.add(ScreenKey.ProfilePesantren) },
                                onNavigateToCalculationMethods = { backstack.add(ScreenKey.PrayerCalculationMethods) },
                                onNavigateToPrayerAdjustments = { backstack.add(ScreenKey.PrayerAdjustments) },
                                amaliyahViewModel = amaliyahViewModel,
                                audioPlayer = sharedAudioPlayer
                            )
                        }
                        is ScreenKey.DocumentReader -> {
                            com.iqbalwork.robithoh.feature.reader.ui.GenericDocumentReaderScreen(
                                documentId = key.documentId,
                                onNavigateToTasbih = { backstack.add(ScreenKey.Tasbih) },
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.Langgam -> {
                            com.iqbalwork.robithoh.feature.langgam.ui.LanggamScreen(
                                audioPlayer = sharedAudioPlayer,
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.Amaliyah -> {
                            AmaliyahScreen(
                                onNavigate = { destination -> backstack.add(destination) },
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                },
                                viewModel = amaliyahViewModel
                            )
                        }
                        is ScreenKey.Tasbih -> {
                            TasbihScreen(
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.ManaqibList -> {
                            ManaqibListScreen(
                                onChapterClick = { chapterNumber ->
                                    backstack.add(ScreenKey.ManaqibDetail(chapterNumber))
                                },
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.ManaqibDetail -> {
                            ManaqibDetailScreen(
                                chapterNumber = key.chapterNumber,
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.QuranList -> {
                            QuranListScreen(
                                onSurahClick = { surahNumber, ayahNumber ->
                                    backstack.add(ScreenKey.QuranSurah(surahNumber, ayahNumber))
                                },
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.QuranSurah -> {
                            QuranSurahScreen(
                                surahNumber = key.surahNumber,
                                initialAyahNumber = key.ayahNumber,
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.Settings -> {
                            SettingsScreen(
                                onNavigateToCalculationMethods = {
                                    backstack.add(ScreenKey.PrayerCalculationMethods)
                                },
                                onNavigateToPrayerAdjustments = {
                                    backstack.add(ScreenKey.PrayerAdjustments)
                                },
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.PrayerCalculationMethods -> {
                            PrayerCalculationMethodScreen(
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                },
                                viewModel = amaliyahViewModel
                            )
                        }
                        is ScreenKey.PrayerAdjustments -> {
                            PrayerAdjustmentsScreen(
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                },
                                viewModel = amaliyahViewModel
                            )
                        }
                        is ScreenKey.ProfilePesantren -> {
                            ProfilePesantrenScreen(
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}