package com.iqbalwork.robithoh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.iqbalwork.robithoh.core.audio.createAudioCacheManager
import com.iqbalwork.robithoh.core.audio.createAudioDownloader
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.database.rememberRobithohDatabase
import com.iqbalwork.robithoh.core.designsystem.InitHapticContext
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.network.createKtorHttpClient
import com.iqbalwork.robithoh.core.notification.rememberPrayerAlarmScheduler
import com.iqbalwork.robithoh.core.presentation.LocalScrollPositionStore
import com.iqbalwork.robithoh.core.presentation.ScrollPositionStore
import com.iqbalwork.robithoh.core.settings.rememberAppSettingsRepository
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import com.iqbalwork.robithoh.feature.doa.ui.DoaListScreen
import com.iqbalwork.robithoh.feature.langgam.ui.LanggamScreen
import com.iqbalwork.robithoh.feature.onboarding.OnboardingScreen
import com.iqbalwork.robithoh.feature.qibla.ui.QiblaScreen
import com.iqbalwork.robithoh.core.notification.rememberDocumentSyncNotifier
import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncManager
import com.iqbalwork.robithoh.feature.reader.data.sync.DocumentSyncState
import com.iqbalwork.robithoh.feature.reader.ui.GenericDocumentReaderScreen
import com.iqbalwork.robithoh.feature.splash.SplashScreen
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel
import com.iqbalwork.robithoh.navigation.BackHandler
import com.iqbalwork.robithoh.navigation.MainAppContainer
import com.iqbalwork.robithoh.navigation.MainTab
import com.iqbalwork.robithoh.navigation.PrayerAdjustmentsScreen
import com.iqbalwork.robithoh.navigation.PrayerCalculationMethodScreen
import com.iqbalwork.robithoh.navigation.ProfilePesantrenScreen
import com.iqbalwork.robithoh.navigation.QuranListScreen
import com.iqbalwork.robithoh.navigation.QuranPageReaderScreen
import com.iqbalwork.robithoh.navigation.QuranSurahScreen
import com.iqbalwork.robithoh.navigation.ScreenKey
import com.iqbalwork.robithoh.navigation.ScreenKeyListSaver
import com.iqbalwork.robithoh.navigation.SettingsScreen
import com.iqbalwork.robithoh.navigation.TasbihScreen
import com.iqbalwork.robithoh.navigation.WidgetNavTarget

@Composable
fun App(
    initialDestination: String? = null,
    initialSurahNumber: Int = 1,
    initialAyahNumber: Int = 1,
    widgetNavTarget: WidgetNavTarget? = null,
    onCheckForUpdates: () -> Unit = {},
    onOpenPlayStore: () -> Unit = {}
) {
    var isDarkMode by rememberSaveable { mutableStateOf(false) }

    RabithohTheme(darkTheme = isDarkMode) {
        InitHapticContext()
        val scrollPositionStore = rememberSaveable(saver = ScrollPositionStore.Saver) {
            ScrollPositionStore()
        }
        val backstack = rememberSaveable(saver = ScreenKeyListSaver) {
            mutableStateListOf<NavKey>(ScreenKey.Splash)
        }

        val database = rememberRobithohDatabase()
        val appSettingsRepository = rememberAppSettingsRepository()
        val appSettings by appSettingsRepository.settings.collectAsState()
        // Tracks whether AppSettings has been loaded from DB at least once.
        // Without this, the 2.2s splash may finish before the async DB read completes,
        // causing hasCompletedOnboarding to read as false even for returning users.
        var isSettingsLoaded by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(appSettings) {
            if (!isSettingsLoaded) isSettingsLoaded = true
        }
        val alarmScheduler = rememberPrayerAlarmScheduler()
        val amaliyahViewModel: AmaliyahViewModel = viewModel {
            AmaliyahViewModel(
                database = database,
                alarmScheduler = alarmScheduler
            )
        }
        val documentRepository = remember(database) {
            MarkdownDocumentRepository(database = database)
        }
        val tasbihViewModel: TasbihViewModel = viewModel {
            TasbihViewModel(
                database = database,
                repository = documentRepository
            )
        }
        val sharedCacheManager = remember { createAudioCacheManager() }
        val sharedDownloader = remember { createAudioDownloader(sharedCacheManager) }
        val sharedAudioPlayer = remember { createAudioPlayer() }
        val documentSyncNotifier = rememberDocumentSyncNotifier()
        val documentSyncManager = remember(database, documentRepository, documentSyncNotifier) {
            DocumentSyncManager(
                httpClient = createKtorHttpClient(),
                database = database,
                repository = documentRepository,
                notifier = documentSyncNotifier
            )
        }

        LaunchedEffect(documentSyncManager) {
            documentSyncManager.syncDocuments()
        }

        LaunchedEffect(documentSyncManager) {
            documentSyncManager.syncState.collect { state ->
                if (state is DocumentSyncState.Success && state.updatedCount > 0) {
                    tasbihViewModel.onIntent(TasbihUiIntent.ReloadPresets)
                }
            }
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
                // splashDone is set to true when the 2.2s animation finishes.
                // We delay routing until BOTH splash animation is done AND settings are loaded from DB.
                var splashDone by rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(splashDone, isSettingsLoaded) {
                    if (splashDone && isSettingsLoaded) {
                        backstack.clear()
                        if (!appSettings.hasCompletedOnboarding) {
                            backstack.add(ScreenKey.Onboarding)
                        } else {
                            backstack.add(ScreenKey.Home)
                            if (widgetNavTarget != null) {
                                routeDestination(widgetNavTarget.destination, widgetNavTarget.surahNumber, widgetNavTarget.ayahNumber)
                            } else if (initialDestination != null) {
                                routeDestination(initialDestination, initialSurahNumber, initialAyahNumber)
                            }
                        }
                    }
                }

                SplashScreen(
                    onSplashFinished = { splashDone = true }
                )
            }
            entry<ScreenKey.Onboarding> { _ ->
                OnboardingScreen(
                    onComplete = {
                        appSettingsRepository.setOnboardingCompleted(true)
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
                        } else if (docId == "doa_list") {
                            backstack.add(ScreenKey.DoaList)
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
                    onDarkModeChange = { isDarkMode = it },
                    onCheckForUpdates = onCheckForUpdates,
                    onOpenPlayStore = onOpenPlayStore
                )
            }
            entry<ScreenKey.DocumentReader> { key ->
                GenericDocumentReaderScreen(
                    documentId = key.documentId,
                    tasbihViewModel = tasbihViewModel,
                    repository = documentRepository,
                    syncManager = documentSyncManager,
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
            entry<ScreenKey.DoaList> { _ ->
                DoaListScreen(
                    onDocumentClick = { docId ->
                        backstack.add(ScreenKey.DocumentReader(docId))
                    },
                    onBackClick = onBackAction
                )
            }
            entry<ScreenKey.Langgam> { _ ->
                LanggamScreen(
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
                            TasbihUiIntent.SyncData(
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
                    onMushafClick = { pageNumber ->
                        backstack.add(ScreenKey.QuranPageReader(pageNumber))
                    },
                    audioPlayer = sharedAudioPlayer,
                    onBack = onBackAction
                )
            }
            entry<ScreenKey.QuranSurah> { key ->
                QuranSurahScreen(
                    surahNumber = key.surahNumber,
                    initialAyahNumber = key.ayahNumber,
                    audioPlayer = sharedAudioPlayer,
                    onBack = onBackAction,
                    onSwitchToMushafMode = { pageNumber ->
                        if (backstack.isNotEmpty()) {
                            backstack[backstack.lastIndex] = ScreenKey.QuranPageReader(pageNumber)
                        } else {
                            backstack.add(ScreenKey.QuranPageReader(pageNumber))
                        }
                    }
                )
            }
            entry<ScreenKey.QuranPageReader> { key ->
                QuranPageReaderScreen(
                    pageNumber = key.pageNumber,
                    initialAyahNumber = key.targetAyahNumber,
                    audioPlayer = sharedAudioPlayer,
                    onBack = onBackAction,
                    onSwitchToTextMode = { surahNumber, ayahNumber ->
                        if (backstack.isNotEmpty()) {
                            backstack[backstack.lastIndex] = ScreenKey.QuranSurah(surahNumber, ayahNumber)
                        } else {
                            backstack.add(ScreenKey.QuranSurah(surahNumber, ayahNumber))
                        }
                    }
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
                QiblaScreen(
                    onBack = onBackAction,
                    viewModel = amaliyahViewModel
                )
            }
        }

        CompositionLocalProvider(LocalScrollPositionStore provides scrollPositionStore) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
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
    }
}
