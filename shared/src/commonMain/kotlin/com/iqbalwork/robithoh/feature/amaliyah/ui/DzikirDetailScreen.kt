package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.iqbalwork.robithoh.core.designsystem.rememberShareTextAction
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.core.designsystem.theme.ReaderTheme
import com.iqbalwork.robithoh.feature.amaliyah.model.DzikirItem
import com.iqbalwork.robithoh.feature.amaliyah.model.DzikirType
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiState
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent

@Composable
fun DzikirDetailScreen(
    state: AmaliyahUiState,
    onIntent: (AmaliyahUiIntent) -> Unit,
    onOpenTasbih: (count: Int, target: Int, title: String) -> Unit,
    onBack: () -> Unit,
    tasbihViewModel: com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel? = null,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val readerSettingsRepository = com.iqbalwork.robithoh.core.settings.rememberReaderSettingsRepository()
    val readerSettings by readerSettingsRepository.settings.collectAsState()
    val fontScale = readerSettings.fontScale
    val readerTheme = readerSettings.resolveTheme(isDark)
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    val currentDzikirList = if (state.activeDzikirType == DzikirType.JAHR) {
        state.dzikirJahrList
    } else {
        state.dzikirKhofiList
    }

    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val resolvedTasbihViewModel = tasbihViewModel ?: remember(database) {
        com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel(database = database)
    }
    val tasbihState by resolvedTasbihViewModel.uiState.collectAsState()
    var selectedDzikirForOptions by remember { mutableStateOf<DzikirItem?>(null) }
    val clipboardManager = LocalClipboardManager.current
    val shareAction = rememberShareTextAction()
    val hapticFeedback = remember { getHapticFeedback() }

    Scaffold(
        topBar = {
            IslamicHeader(
                title = if (state.activeDzikirType == DzikirType.JAHR) "Dzikir Jahr (165x)" else "Dzikir Khofi (Ismu Dzat)",
                subtitle = "MTQN Suryalaya Sirnarasa PPKN III Silsilah 38",
                arabicTitle = if (state.activeDzikirType == DzikirType.JAHR) "ذِكْرُ الْجَهْرِ" else "ذِكْرُ الْخَفِيِّ",
                onBackClick = onBack,
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
                }
            )
        },
        containerColor = readerTheme.backgroundColor,
        modifier = modifier
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
            // Type Switcher Tabs (Jahr vs Khofi)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isDark) DarkSurfaceVariant else Color(0xFFE9ECEF))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DzikirType.entries.forEach { type ->
                        val isSelected = type == state.activeDzikirType
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MerahMerdeka else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onIntent(AmaliyahUiIntent.SelectDzikirType(type)) }
                        ) {
                            Text(
                                text = type.label,
                                color = if (isSelected) PutihBersih else (if (isDark) Color.LightGray else Color.DarkGray),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // 3-Language Toggle
            item {
                LanguageTabSwitch(
                    selectedLanguage = state.selectedLanguage,
                    onLanguageSelected = { onIntent(AmaliyahUiIntent.SelectLanguage(it)) }
                )
            }

            // Introduction Banner Card
            item {
                GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_TINTED) {
                    Text(
                        text = if (state.activeDzikirType == DzikirType.JAHR) {
                            "Kaifiyat Dzikir Jahr Ba'da Sholat"
                        } else {
                            "Kaifiyat Dzikir Khofi & Rabithah Mursyid 38"
                        },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MerahMerdeka
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (state.activeDzikirType == DzikirType.JAHR) {
                            "Dzikir Jahr diamalkan bersuara nyaring dengan menggelengkan kepala dari lambung kanan ke kiri (lathifah qolbi) sebanyak 165 kali setelah sholat fardhu."
                        } else {
                            "Dzikir Khofi menggetarkan Ismu Dzat (ALLAH) di lathifah qolbi (bawah dada kiri) tanpa suara dan tanpa nafas, dengan rabithah kepada Syekh Mursyid Abah Aos Ra. Qs."
                        },
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else SlateMuted,
                        lineHeight = 17.sp
                    )
                }
            }

            // Dzikir Liturgical Items
            items(currentDzikirList, key = { it.id }) { item ->
                DzikirLiturgicalCard(
                    item = item,
                    selectedLanguage = state.selectedLanguage,
                    readerTheme = readerTheme,
                    fontScale = fontScale,
                    onClick = { selectedDzikirForOptions = item },
                    onOpenTasbih = { target, title ->
                        resolvedTasbihViewModel.onIntent(TasbihUiIntent.SetTarget(target))
                        resolvedTasbihViewModel.onIntent(TasbihUiIntent.SetFloatingExpanded(true))
                    }
                )
            }

            // Islamic End Decoration
            item {
                IslamicDivider(
                    motif = IslamicDividerMotif.RUB_EL_HIZB,
                    color = EmasKhidmat,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // Floating Tasbih Overlay (Expandable Floating Widget)
        com.iqbalwork.robithoh.feature.tasbih.ui.component.FloatingTasbihOverlay(
            state = tasbihState,
            onIntent = resolvedTasbihViewModel::onIntent,
            onOpenFullScreen = {
                onOpenTasbih(
                    tasbihState.currentCount,
                    tasbihState.targetCount,
                    tasbihState.selectedDzikirTitle
                )
            }
        )
    }
    }

    if (showSettingsDialog) {
        TextReaderSettingsSheet(
            fontScale = fontScale,
            onFontScaleChange = { readerSettingsRepository.updateFontScale(it) },
            selectedTheme = readerTheme,
            onThemeSelected = { readerSettingsRepository.updateTheme(it) },
            onDismiss = { showSettingsDialog = false }
        )
    }

    selectedDzikirForOptions?.let { item ->
        val translationText = when (state.selectedLanguage) {
            LiturgyLanguage.ARABIC -> item.indonesianText
            LiturgyLanguage.INDONESIAN -> item.indonesianText
            LiturgyLanguage.SUNDANESE -> item.sundaneseText
        }
        val shareText = remember(item, state.selectedLanguage) {
            buildString {
                append(item.title)
                if (item.repetitionCount > 1) append(" (${item.repetitionCount}x)")
                append("\n\n")
                append(item.arabicText)
                if (item.latinText.isNotBlank()) {
                    append("\n\n")
                    append(item.latinText)
                }
                if (translationText.isNotBlank()) {
                    append("\n\n")
                    val langBadge = if (state.selectedLanguage == LiturgyLanguage.SUNDANESE) "Basa Sunda" else "Terjemahan"
                    append("[$langBadge] $translationText")
                }
                if (item.kaifiyatNote.isNotBlank()) {
                    append("\n\nKaifiyat: ")
                    append(item.kaifiyatNote)
                }
                append("\n\n(Dzikir MTQN Suryalaya Sirnarasa PPKN III Silsilah 38)")
            }
        }

        val customOptions = buildList {
            if (item.repetitionCount > 1) {
                add(
                    ContentItemOption(
                        icon = "📿",
                        label = "Hitung dengan Tasbih (${item.repetitionCount}x)",
                        onClick = {
                            hapticFeedback.performClick()
                            resolvedTasbihViewModel.onIntent(TasbihUiIntent.SetTarget(item.repetitionCount))
                            resolvedTasbihViewModel.onIntent(TasbihUiIntent.SetFloatingExpanded(true))
                        }
                    )
                )
            }
        }

        ContentItemOptionsSheet(
            title = "${item.number}. ${item.title}",
            subtitle = if (item.repetitionCount > 1) "${item.repetitionCount}x Pengulangan" else null,
            onDismiss = { selectedDzikirForOptions = null },
            onCopy = { clipboardManager.setText(AnnotatedString(shareText)) },
            copyLabel = "Salin Teks Dzikir",
            onShare = { shareAction(shareText) },
            shareLabel = "Bagikan Dzikir",
            customOptions = customOptions
        )
    }
}

@Composable
private fun DzikirLiturgicalCard(
    item: DzikirItem,
    selectedLanguage: LiturgyLanguage,
    readerTheme: ReaderTheme = ReaderTheme.WHITE,
    fontScale: Float = 1.0f,
    onClick: () -> Unit,
    onOpenTasbih: (Int, String) -> Unit
) {
    val isDark = readerTheme.isDark
    val hapticFeedback = remember { getHapticFeedback() }
    GoldCrimsonCard(
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        customBackgroundColor = readerTheme.cardBackgroundColor,
        customBorderColor = readerTheme.cardBorderColor,
        onClick = onClick
    ) {
        // Card Top Header: Number, Title, Repetition Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MerahMerdeka,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${item.number}",
                            color = PutihBersih,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isDark) PutihBersih else SlateCharcoalText
                )
            }

            if (item.repetitionCount > 1) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmasKhidmat.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, EmasKhidmat)
                ) {
                    Text(
                        text = "${item.repetitionCount}x",
                        color = if (isDark) EmasMuda else MerahMarunGelap,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Arabic Text
        Text(
            text = item.arabicText,
            style = RabithohTheme.typography.arabicLarge.copy(
                fontSize = (21 * fontScale).sp,
                lineHeight = (36 * fontScale).sp,
                textAlign = TextAlign.End,
                color = readerTheme.arabicTextColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Latin Transliteration
        Text(
            text = item.latinText,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = readerTheme.latinTextColor,
                fontSize = (14.5 * fontScale).sp,
                lineHeight = 22.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Translation based on active switch
        val translationText = when (selectedLanguage) {
            LiturgyLanguage.ARABIC -> item.indonesianText // fallback to Indo if Arab selected for translation
            LiturgyLanguage.INDONESIAN -> item.indonesianText
            LiturgyLanguage.SUNDANESE -> item.sundaneseText
        }

        val langBadge = when (selectedLanguage) {
            LiturgyLanguage.SUNDANESE -> "Basa Sunda"
            else -> "Terjemahan"
        }

        Text(
            text = "[$langBadge] $translationText",
            style = MaterialTheme.typography.bodySmall.copy(
                color = readerTheme.translationTextColor,
                fontSize = (12 * fontScale).sp,
                lineHeight = 17.sp
            )
        )

        // Notes & Kaifiyat
        if (item.kaifiyatNote.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = if (isDark) Color(0xFF232328) else Color(0xFFF7F7F8),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Kaifiyat: ${item.kaifiyatNote}",
                    fontSize = 11.sp,
                    color = if (isDark) DarkMuted else SlateMuted,
                    modifier = Modifier.padding(8.dp),
                    lineHeight = 15.sp
                )
            }
        }

        // Quick Launch to Tasbih Button (if repetition > 1)
        if (item.repetitionCount > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = {
                    hapticFeedback.performClick()
                    onOpenTasbih(item.repetitionCount, item.title)
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, EmasKhidmat),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isDark) EmasMuda else MerahMerdeka
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "📿 Hitung dengan Tasbih (${item.repetitionCount}x)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
