package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.manaqib.model.ManqobahChapter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManaqibListScreen(
    viewModel: ManaqibViewModel,
    onChapterClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val isDark = RabithohTheme.colors.isDark

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            IslamicHeader(
                title = "Kitab Manaqib TQN",
                subtitle = "Sulthonul Auliya Syekh Abdul Qodir Al-Jailani r.a.",
                arabicTitle = "الْمَنَاقِبُ الشَّرِيفَةُ",
                onBackClick = onBackClick
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Horizontal Tab Navigation
            ManaqibNavigationTabs(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.onIntent(ManaqibUiIntent.SelectTab(it)) }
            )

            // Content view based on active tab
            when (state.selectedTab) {
                ManaqibTab.CHAPTERS -> {
                    ChaptersTabContent(
                        state = state,
                        onChapterClick = { chapterNum ->
                            viewModel.onIntent(ManaqibUiIntent.SelectChapter(chapterNum))
                            onChapterClick(chapterNum)
                        },
                        onSearchChange = { viewModel.onIntent(ManaqibUiIntent.SearchChapters(it)) },
                        onLanguageSelected = { viewModel.onIntent(ManaqibUiIntent.SelectLanguage(it)) }
                    )
                }
                ManaqibTab.TANBIH -> {
                    TanbihScreen(
                        tanbih = state.tanbih,
                        selectedLanguage = state.selectedLanguage,
                        onLanguageSelected = { viewModel.onIntent(ManaqibUiIntent.SelectLanguage(it)) }
                    )
                }
                ManaqibTab.SILSILAH -> {
                    TawassulSilsilahScreen(
                        silsilahList = state.silsilahList,
                        searchQuery = state.silsilahSearchQuery,
                        onSearchChange = { viewModel.onIntent(ManaqibUiIntent.SearchSilsilah(it)) }
                    )
                }
                ManaqibTab.MC_ACARA -> {
                    McManaqibScreen(
                        programs = state.mcPrograms,
                        selectedLanguage = state.selectedLanguage,
                        onLanguageSelected = { viewModel.onIntent(ManaqibUiIntent.SelectLanguage(it)) }
                    )
                }
                ManaqibTab.KHOTAMAN -> {
                    KhotamanScreen(
                        steps = state.khotamanSteps
                    )
                }
                ManaqibTab.DOA -> {
                    DoaManaqibScreen(
                        doaList = state.doaList,
                        selectedDoa = state.selectedDoa,
                        onSelectDoa = { viewModel.onIntent(ManaqibUiIntent.SelectDoa(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ManaqibNavigationTabs(
    selectedTab: ManaqibTab,
    onTabSelected: (ManaqibTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (isDark) DarkSurface else PutihBersih)
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ManaqibTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            val bgColor = if (isSelected) {
                if (isDark) MerahMarunGelap else MerahMerdeka
            } else {
                if (isDark) DarkSurfaceVariant else Color(0xFFF1F3F5)
            }
            val textColor = if (isSelected) PutihBersih else (if (isDark) DarkMuted else SlateMuted)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ChaptersTabContent(
    state: ManaqibUiState,
    onChapterClick: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    onLanguageSelected: (LiturgyLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Language Switcher
        LanguageTabSwitch(
            selectedLanguage = state.selectedLanguage,
            onLanguageSelected = onLanguageSelected,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = state.chapterSearchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            placeholder = {
                Text("Cari bab Manqobah 1 s/d 56...", fontSize = 13.sp)
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmasKhidmat,
                unfocusedBorderColor = if (isDark) DarkBorder else SlateBorder,
                focusedContainerColor = if (isDark) DarkSurface else PutihBersih,
                unfocusedContainerColor = if (isDark) DarkSurface else PutihBersih
            )
        )

        // Chapters List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(state.chapters, key = { it.id }) { chapter ->
                ChapterItemCard(
                    chapter = chapter,
                    selectedLanguage = state.selectedLanguage,
                    onClick = { onChapterClick(chapter.chapterNumber) }
                )
            }
        }
    }
}

@Composable
private fun ChapterItemCard(
    chapter: ManqobahChapter,
    selectedLanguage: LiturgyLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    GoldCrimsonCard(
        modifier = modifier,
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chapter Number Badge
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDark) MerahMarunGelap else MerahMerdeka.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${chapter.chapterNumber}",
                    color = if (isDark) EmasMuda else MerahMerdeka,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Titles
            Column(modifier = Modifier.weight(1f)) {
                val activeTitle = chapter.titleForLanguage(selectedLanguage)
                Text(
                    text = activeTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (isDark) PutihBersih else SlateCharcoalText
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Bab ${chapter.chapterNumber} • Sulthonul Auliya",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 11.sp
                    )
                )
            }

            Text(
                text = "›",
                fontSize = 22.sp,
                color = EmasKhidmat,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
