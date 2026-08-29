package com.iqbalwork.robithoh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.feature.splash.SplashScreen
import com.iqbalwork.robithoh.navigation.*

@Composable
fun App() {
    var isDarkMode by rememberSaveable { mutableStateOf(false) }

    RabithohTheme(darkTheme = isDarkMode) {
        com.iqbalwork.robithoh.core.designsystem.InitHapticContext()
        val backstack = rememberSaveable(saver = ScreenKeyListSaver) {
            mutableStateListOf<NavKey>(ScreenKey.Splash)
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

        val onBackAction: () -> Unit = {
            if (backstack.size > 1) {
                backstack.removeAt(backstack.lastIndex)
            }
        }

        BackHandler(enabled = backstack.size > 1) {
            onBackAction()
        }

        val entries = entryProvider<NavKey> {
            entry<ScreenKey.Splash> { _ ->
                SplashScreen(
                    onSplashFinished = {
                        backstack.clear()
                        backstack.add(ScreenKey.Home)
                    }
                )
            }
            entry<ScreenKey.Home> { _ ->
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
                    audioPlayer = sharedAudioPlayer,
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { isDarkMode = it }
                )
            }
            entry<ScreenKey.DocumentReader> { key ->
                com.iqbalwork.robithoh.feature.reader.ui.GenericDocumentReaderScreen(
                    documentId = key.documentId,
                    onNavigateToTasbih = { backstack.add(ScreenKey.Tasbih) },
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.Langgam> { _ ->
                com.iqbalwork.robithoh.feature.langgam.ui.LanggamScreen(
                    audioPlayer = sharedAudioPlayer,
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.Amaliyah> { _ ->
                AmaliyahScreen(
                    onNavigate = { destination -> backstack.add(destination) },
                    onBack = onBackAction,
                    viewModel = amaliyahViewModel
                )
            }
            entry<ScreenKey.Tasbih> { _ ->
                TasbihScreen(
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.ManaqibList> { _ ->
                ManaqibListScreen(
                    onChapterClick = { chapterNumber ->
                        backstack.add(ScreenKey.ManaqibDetail(chapterNumber))
                    },
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.ManaqibDetail> { key ->
                ManaqibDetailScreen(
                    chapterNumber = key.chapterNumber,
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.QuranList> { _ ->
                QuranListScreen(
                    onSurahClick = { surahNumber, ayahNumber ->
                        backstack.add(ScreenKey.QuranSurah(surahNumber, ayahNumber))
                    },
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.QuranSurah> { key ->
                QuranSurahScreen(
                    surahNumber = key.surahNumber,
                    initialAyahNumber = key.ayahNumber,
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.Settings> { _ ->
                SettingsScreen(
                    onNavigateToCalculationMethods = {
                        backstack.add(ScreenKey.PrayerCalculationMethods)
                    },
                    onNavigateToPrayerAdjustments = {
                        backstack.add(ScreenKey.PrayerAdjustments)
                    },
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.PrayerCalculationMethods> { _ ->
                PrayerCalculationMethodScreen(
                    onBack = onBackAction,
                    viewModel = amaliyahViewModel
                )
            }
            entry<ScreenKey.PrayerAdjustments> { _ ->
                PrayerAdjustmentsScreen(
                    onBack = onBackAction,
                    viewModel = amaliyahViewModel
                )
            }
            entry<ScreenKey.ProfilePesantren> { _ ->
                ProfilePesantrenScreen(
                    onBack = onBackAction
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavDisplay(
                backStack = backstack,
                onBack = onBackAction,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
                    rememberViewModelStoreNavEntryDecorator<NavKey>()
                ),
                entryProvider = entries
            )
        }
    }
}