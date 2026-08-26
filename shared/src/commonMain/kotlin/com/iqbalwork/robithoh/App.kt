package com.iqbalwork.robithoh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.navigation.*

@Composable
fun App() {
    RabithohTheme {
        val backstack = remember { mutableStateListOf<ScreenKey>(ScreenKey.Home) }

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
                                onNavigateToDocument = { docId ->
                                    if (docId == "quran_list") {
                                        backstack.add(ScreenKey.QuranList)
                                    } else {
                                        backstack.add(ScreenKey.DocumentReader(docId))
                                    }
                                },
                                onNavigateToSurah = { surahNumber ->
                                    backstack.add(ScreenKey.QuranSurah(surahNumber))
                                },
                                onNavigateToLanggam = { backstack.add(ScreenKey.Langgam) },
                                onNavigateToTasbih = { backstack.add(ScreenKey.Tasbih) },
                                onNavigateToProfilePesantren = { backstack.add(ScreenKey.ProfilePesantren) }
                            )
                        }
                        is ScreenKey.DocumentReader -> {
                            com.iqbalwork.robithoh.feature.reader.ui.GenericDocumentReaderScreen(
                                documentId = key.documentId,
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.Langgam -> {
                            com.iqbalwork.robithoh.feature.langgam.ui.LanggamScreen(
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
                                }
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
                                onSurahClick = { surahNumber ->
                                    backstack.add(ScreenKey.QuranSurah(surahNumber))
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
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                        is ScreenKey.Settings -> {
                            SettingsScreen(
                                onBack = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
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