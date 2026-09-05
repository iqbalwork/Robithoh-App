package com.iqbalwork.robithoh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import com.iqbalwork.robithoh.navigation.BackHandler
import com.iqbalwork.robithoh.navigation.MainAppContainer
import com.iqbalwork.robithoh.navigation.MainTab
import com.iqbalwork.robithoh.navigation.PrayerAdjustmentsScreen
import com.iqbalwork.robithoh.navigation.PrayerCalculationMethodScreen
import com.iqbalwork.robithoh.navigation.ProfilePesantrenScreen
import com.iqbalwork.robithoh.navigation.QuranListScreen
import com.iqbalwork.robithoh.navigation.QuranSurahScreen
import com.iqbalwork.robithoh.navigation.ScreenKey
import com.iqbalwork.robithoh.navigation.ScreenKeyListSaver
import com.iqbalwork.robithoh.navigation.SettingsScreen
import com.iqbalwork.robithoh.navigation.TasbihScreen

@Composable
fun App(
    initialDestination: String? = null,
    initialSurahNumber: Int = 1,
    initialAyahNumber: Int = 1,
    widgetNavTarget: com.iqbalwork.robithoh.navigation.WidgetNavTarget? = null
) {
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
        val tasbihViewModel: com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel = viewModel {
            com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel(database = database)
        }
        val sharedCacheManager = remember { com.iqbalwork.robithoh.core.audio.createAudioCacheManager() }
        val sharedDownloader = remember { com.iqbalwork.robithoh.core.audio.createAudioDownloader(sharedCacheManager) }
        val sharedAudioPlayer = remember { com.iqbalwork.robithoh.core.audio.createAudioPlayer() }
        val documentRepository = remember(database) {
            com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository(database = database)
        }
        val documentSyncManager = remember(database, documentRepository) {
            com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncManager(
                httpClient = com.iqbalwork.robithoh.core.network.createKtorHttpClient(),
                database = database,
                repository = documentRepository
            )
        }

        LaunchedEffect(Unit) {
            documentSyncManager.syncDocuments()
        }

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

        fun routeDestination(dest: String, surahNum: Int = initialSurahNumber, ayahNum: Int = initialAyahNumber) {
            when (dest) {
                "AMALIYAH" -> {
                    backstack.add(ScreenKey.DocumentReader("dzikir_tqn"))
                }
                "TASBIH" -> {
                    if (backstack.lastOrNull() !is ScreenKey.Tasbih) {
                        backstack.add(ScreenKey.Tasbih())
                    }
                }
                "MANAQIB" -> {
                    backstack.add(ScreenKey.DocumentReader("manqobah_id"))
                }
                "TANBIH" -> {
                    backstack.add(ScreenKey.DocumentReader("tanbih_id"))
                }
                "QURAN" -> {
                    if (backstack.lastOrNull() != ScreenKey.QuranList) {
                        backstack.add(ScreenKey.QuranList)
                    }
                }
                "QURAN_SURAH" -> {
                    backstack.add(ScreenKey.QuranSurah(surahNum, ayahNum))
                }
                "PRAYER" -> {
                    homeTab = MainTab.SALAT
                    while (backstack.size > 1) {
                        backstack.removeAt(backstack.lastIndex)
                    }
                }
            }
        }

        LaunchedEffect(widgetNavTarget) {
            if (widgetNavTarget != null && !backstack.contains(ScreenKey.Splash)) {
                routeDestination(widgetNavTarget.destination, widgetNavTarget.surahNumber, widgetNavTarget.ayahNumber)
            }
        }

        LaunchedEffect(initialDestination) {
            if (initialDestination != null && widgetNavTarget == null && !backstack.contains(ScreenKey.Splash)) {
                routeDestination(initialDestination, initialSurahNumber, initialAyahNumber)
            }
        }

        val entries = entryProvider<NavKey> {
            entry<ScreenKey.Splash> { _ ->
                SplashScreen(
                    onSplashFinished = {
                        backstack.clear()
                        backstack.add(ScreenKey.Home)
                        if (widgetNavTarget != null) {
                            routeDestination(widgetNavTarget.destination, widgetNavTarget.surahNumber, widgetNavTarget.ayahNumber)
                        } else if (initialDestination != null) {
                            routeDestination(initialDestination, initialSurahNumber, initialAyahNumber)
                        }
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
                    onNavigateToTasbih = { backstack.add(ScreenKey.Tasbih()) },
                    onNavigateToProfilePesantren = { backstack.add(ScreenKey.ProfilePesantren) },
                    onNavigateToCalculationMethods = { backstack.add(ScreenKey.PrayerCalculationMethods) },
                    onNavigateToPrayerAdjustments = { backstack.add(ScreenKey.PrayerAdjustments) },
                    onNavigateToQibla = { backstack.add(ScreenKey.Qibla) },
                    amaliyahViewModel = amaliyahViewModel,
                    audioPlayer = sharedAudioPlayer,
                    audioDownloader = sharedDownloader,
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { isDarkMode = it }
                )
            }
            entry<ScreenKey.DocumentReader> { key ->
                com.iqbalwork.robithoh.feature.reader.ui.GenericDocumentReaderScreen(
                    documentId = key.documentId,
                    tasbihViewModel = tasbihViewModel,
                    repository = documentRepository,
                    onNavigateToTasbih = { count, target, title ->
                        backstack.add(
                            ScreenKey.Tasbih(
                                initialCount = count,
                                targetCount = target,
                                dzikirTitle = title
                            )
                        )
                    },
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.Langgam> { _ ->
                com.iqbalwork.robithoh.feature.langgam.ui.LanggamScreen(
                    audioPlayer = sharedAudioPlayer,
                    cacheManager = sharedCacheManager,
                    audioDownloader = sharedDownloader,
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.Tasbih> { key ->
                LaunchedEffect(key) {
                    if (key.initialCount != null) {
                        tasbihViewModel.onIntent(
                            com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent.SyncData(
                                count = key.initialCount,
                                target = key.targetCount,
                                dzikirTitle = key.dzikirTitle
                            )
                        )
                    }
                }
                TasbihScreen(
                    onBack = onBackAction,
                    viewModel = tasbihViewModel
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
            entry<ScreenKey.Qibla> { _ ->
                com.iqbalwork.robithoh.feature.qibla.ui.QiblaScreen(
                    onBack = onBackAction,
                    viewModel = amaliyahViewModel
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
