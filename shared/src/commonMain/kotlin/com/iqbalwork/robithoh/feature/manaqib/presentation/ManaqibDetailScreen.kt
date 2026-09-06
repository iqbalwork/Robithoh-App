package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.core.designsystem.theme.ReaderTheme
import androidx.compose.runtime.saveable.rememberSaveable
import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManaqibDetailScreen(
    viewModel: ManaqibViewModel,
    chapterNumber: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(chapterNumber) {
        if (state.currentChapter?.chapterNumber != chapterNumber) {
            viewModel.onIntent(ManaqibUiIntent.SelectChapter(chapterNumber))
        }
    }

    val chapter = state.currentChapter
    val isPresentation = state.isPresentationMode
    val readerSettingsRepository = com.iqbalwork.robithoh.core.settings.rememberReaderSettingsRepository()
    val readerSettings by readerSettingsRepository.settings.collectAsState()
    val isHighContrast = state.isHighContrast
    val isDark = RabithohTheme.colors.isDark || isHighContrast
    val fontScale = readerSettings.fontScale
    val readerTheme = readerSettings.resolveTheme(isDark)
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(readerSettings.fontScale) {
        if (state.fontScale != readerSettings.fontScale) {
            viewModel.onIntent(ManaqibUiIntent.UpdateFontScale(readerSettings.fontScale))
        }
    }

    val backgroundColor = if (isHighContrast) {
        Color(0xFF0A0A0C)
    } else if (isDark) {
        DarkCanvas
    } else {
        PutihAbuBackground
    }

    val textColor = if (isHighContrast || isDark) PutihBersih else SlateCharcoalText

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (!isPresentation) {
                IslamicHeader(
                    title = "Manqobah ke-$chapterNumber",
                    subtitle = "Kitab Manaqib Sulthonul Auliya",
                    arabicTitle = "الْمَنْقَبَةُ $chapterNumber",
                    onBackClick = onBackClick,
                    actions = {
                        IconButton(onClick = { showSettingsDialog = true }) {
                            Surface(
                                color = MerahMerdeka.copy(alpha = 0.12f),
                                shape = CircleShape,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("A±", color = MerahMerdeka, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        IconButton(
                            onClick = { viewModel.onIntent(ManaqibUiIntent.TogglePresentationMode(true)) }
                        ) {
                            Text("⛶", color = EmasKhidmat, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        },
        bottomBar = {
            // Presentation mode floating bar or Navigation controls
            Surface(
                color = if (isDark) DarkSurface else PutihBersih,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.navigationBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    // Presentation Mode Quick Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Font scaling buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Font:", fontSize = 12.sp, color = if (isDark) DarkMuted else SlateMuted)
                            Button(
                                onClick = {
                                    val newScale = fontScale - 0.15f
                                    readerSettingsRepository.updateFontScale(newScale)
                                    viewModel.onIntent(ManaqibUiIntent.UpdateFontScale(newScale))
                                },
                                modifier = Modifier.size(32.dp),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) DarkSurfaceVariant else Color(0xFFE4E4E7)
                                )
                            ) {
                                Text("A-", fontSize = 12.sp, color = if (isDark) PutihBersih else SlateCharcoalText)
                            }
                            Text(
                                "${(fontScale * 100).toInt()}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmasKhidmat
                            )
                            Button(
                                onClick = {
                                    val newScale = fontScale + 0.15f
                                    readerSettingsRepository.updateFontScale(newScale)
                                    viewModel.onIntent(ManaqibUiIntent.UpdateFontScale(newScale))
                                },
                                modifier = Modifier.size(32.dp),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) DarkSurfaceVariant else Color(0xFFE4E4E7)
                                )
                            ) {
                                Text("A+", fontSize = 12.sp, color = if (isDark) PutihBersih else SlateCharcoalText)
                            }
                        }

                        // High Contrast Toggle
                        TextButton(
                            onClick = { viewModel.onIntent(ManaqibUiIntent.ToggleHighContrast()) },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text(
                                if (isHighContrast) "☀ Normal" else "◐ Kontras",
                                fontSize = 12.sp,
                                color = EmasKhidmat,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Exit Presentation Mode if active
                        if (isPresentation) {
                            Button(
                                onClick = { viewModel.onIntent(ManaqibUiIntent.TogglePresentationMode(false)) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Tutup ⛶", fontSize = 12.sp, color = PutihBersih)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Previous / Next Chapter navigation buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.onIntent(ManaqibUiIntent.PreviousChapter) },
                            enabled = chapterNumber > 1,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) DarkSurfaceVariant else Color(0xFFE4E4E7),
                                disabledContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("‹ Bab Sebelumnya", fontSize = 13.sp, color = if (chapterNumber > 1) MerahMerdeka else Color.Gray)
                        }

                        Text(
                            "$chapterNumber / 56",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmasKhidmat
                        )

                        Button(
                            onClick = { viewModel.onIntent(ManaqibUiIntent.NextChapter) },
                            enabled = chapterNumber < 56,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) DarkSurfaceVariant else Color(0xFFE4E4E7),
                                disabledContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Bab Selanjutnya ›", fontSize = 13.sp, color = if (chapterNumber < 56) MerahMerdeka else Color.Gray)
                        }
                    }
                }
            }
        },
        containerColor = if (isHighContrast) Color(0xFF0A0A0C) else readerTheme.backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Language selector
            LanguageTabSwitch(
                selectedLanguage = state.selectedLanguage,
                onLanguageSelected = { viewModel.onIntent(ManaqibUiIntent.SelectLanguage(it)) },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (chapter != null) {
                SelectionContainer {
                    Column {
                        // Header Banner Card
                        GoldCrimsonCard(
                            variant = if (isHighContrast) GoldCrimsonCardVariant.GOLD_BORDER else GoldCrimsonCardVariant.CRIMSON_HERO,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(
                                text = "الْمَنْقَبَةُ ${chapter.chapterNumber}",
                                style = RabithohTheme.typography.arabicLarge.copy(
                                    color = EmasMuda,
                                    fontSize = (22 * fontScale).sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = chapter.titleForLanguage(state.selectedLanguage),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PutihBersih,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = (16 * fontScale).sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        IslamicDivider(motif = IslamicDividerMotif.RUB_EL_HIZB)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Reading Content Body
                        GoldCrimsonCard(
                            variant = GoldCrimsonCardVariant.SURFACE_CLEAN,
                            customBackgroundColor = if (isHighContrast) null else readerTheme.cardBackgroundColor,
                            customBorderColor = if (isHighContrast) null else readerTheme.cardBorderColor,
                            contentPadding = PaddingValues(20.dp)
                        ) {
                            when (state.selectedLanguage) {
                                LiturgyLanguage.ARABIC -> {
                                    Text(
                                        text = chapter.contentArabic,
                                        style = RabithohTheme.typography.arabicLarge.copy(
                                            fontSize = (24 * fontScale).sp,
                                            lineHeight = (44 * fontScale).sp,
                                            color = if (isHighContrast) PutihBersih else readerTheme.arabicTextColor,
                                            textAlign = TextAlign.Right
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                LiturgyLanguage.INDONESIAN -> {
                                    Text(
                                        text = chapter.contentIndonesian,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = (16 * fontScale).sp,
                                            lineHeight = (26 * fontScale).sp,
                                            color = if (isHighContrast) PutihBersih else readerTheme.translationTextColor
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                LiturgyLanguage.SUNDANESE -> {
                                    Text(
                                        text = chapter.contentSundanese,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = (16 * fontScale).sp,
                                            lineHeight = (26 * fontScale).sp,
                                            color = if (isHighContrast) PutihBersih else readerTheme.translationTextColor
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Fadhilah Card
                        GoldCrimsonCard(
                            variant = GoldCrimsonCardVariant.GOLD_TINTED,
                            contentPadding = PaddingValues(14.dp)
                        ) {
                            Text(
                                text = "Fadhilah Pembacaan Manaqib",
                                fontWeight = FontWeight.Bold,
                                fontSize = (14 * fontScale).sp,
                                color = if (isDark) EmasMuda else MerahMarunGelap
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Mendatangkan rahmat dan barokah, melapangkan rizki, menenteramkan kalbu, serta mempererat tali ikatan rohani (robithoh) dengan Guru Mursyid.",
                                fontSize = (12 * fontScale).sp,
                                lineHeight = (18 * fontScale).sp,
                                color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MerahMerdeka)
                }
            }
        }
    }

    if (showSettingsDialog) {
        TextReaderSettingsSheet(
            fontScale = fontScale,
            onFontScaleChange = {
                readerSettingsRepository.updateFontScale(it)
                viewModel.onIntent(ManaqibUiIntent.UpdateFontScale(it))
            },
            selectedTheme = readerTheme,
            onThemeSelected = { readerSettingsRepository.updateTheme(it) },
            onDismiss = { showSettingsDialog = false }
        )
    }
}
