package com.iqbalwork.robithoh.feature.reader.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iqbalwork.robithoh.core.designsystem.component.ContentItemOption
import com.iqbalwork.robithoh.core.designsystem.component.ContentItemOptionsSheet
import com.iqbalwork.robithoh.core.designsystem.component.TextReaderSettingsSheet
import com.iqbalwork.robithoh.core.designsystem.rememberShareTextAction
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.GoldContainerLight
import com.iqbalwork.robithoh.core.designsystem.theme.HijauKhasRobithoh
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted
import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import com.iqbalwork.robithoh.feature.reader.model.LiturgyDocument
import com.iqbalwork.robithoh.feature.reader.model.LiturgyVerse
import com.iqbalwork.robithoh.navigation.BackHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericDocumentReaderScreen(
    documentId: String,
    onBack: () -> Unit,
    onNavigateToTasbih: (() -> Unit)? = null,
    repository: MarkdownDocumentRepository = remember { MarkdownDocumentRepository() }
) {
    BackHandler {
        onBack()
    }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val initialCachedDoc = remember(documentId) { repository.getCachedDocument(documentId) }
    var currentDocId by rememberSaveable(documentId) { mutableStateOf(documentId) }
    var parsedDoc by remember { mutableStateOf(initialCachedDoc) }
    var isLoading by remember { mutableStateOf(parsedDoc == null) }
    var fontScale by rememberSaveable { mutableStateOf(1.0f) }
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    val docInfo = remember(currentDocId) { repository.getDocumentById(currentDocId) }
    val isDzikirDoc = currentDocId.contains("dzikir", ignoreCase = true) ||
        docInfo?.id?.contains("dzikir", ignoreCase = true) == true

    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val tasbihViewModel: com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel = viewModel(key = "tasbih_reader_vm") {
        com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel(database = database)
    }
    val tasbihState by tasbihViewModel.uiState.collectAsState()
    var selectedVerseForOptions by remember { mutableStateOf<LiturgyVerse?>(null) }
    val clipboardManager = LocalClipboardManager.current
    val shareAction = rememberShareTextAction()

    LaunchedEffect(currentDocId) {
        val cached = repository.getCachedDocument(currentDocId)
        if (cached != null) {
            parsedDoc = cached
            isLoading = false
        } else {
            if (parsedDoc == null) {
                isLoading = true
            }
            if (docInfo != null) {
                parsedDoc = repository.loadDocumentContent(docInfo)
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = docInfo?.title ?: "Bacaan Amaliyah",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("A±", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MerahMerdeka
                )
            )
        },
        containerColor = PaperBackgroundLight
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MerahMerdeka)
                }
            } else {
                val doc = parsedDoc
                if (doc != null) {
                    val sections = remember(doc.info.id, doc.rawContent) {
                        parseDocumentSections(doc.info.id, doc.rawContent)
                    }
                    val isTahunanPager = doc.info.id.startsWith("sholat_tahunan") && sections.size > 1
                    val hasShortcuts = sections.size > 1
                    val isDoaDoc = doc.info.category.equals("Doa & Ziarah", ignoreCase = true) ||
                        doc.info.category.equals("Doa", ignoreCase = true) ||
                        doc.info.category.equals("Sholawat", ignoreCase = true) ||
                        doc.info.id.contains("doa", ignoreCase = true) ||
                        doc.info.id.contains("salam", ignoreCase = true) ||
                        doc.info.id.contains("istighotsah", ignoreCase = true) ||
                        doc.info.id.contains("sholawat", ignoreCase = true) ||
                        doc.info.id.contains("tahlil", ignoreCase = true)

                    val tahunanPagerState = if (isTahunanPager) {
                        rememberPagerState(initialPage = 0) { sections.size }
                    } else null

                    val currentVisibleSectionIndex by remember {
                        derivedStateOf {
                            if (tahunanPagerState != null) {
                                tahunanPagerState.currentPage
                            } else {
                                val firstVisible = listState.firstVisibleItemIndex
                                if (firstVisible in sections.indices) firstVisible else 0
                            }
                        }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Top Controls Header (Persistent Language Switch + Shortcuts)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PaperBackgroundLight)
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // 1. Language Switcher if alternate language is available
                            if (doc.info.alternateLanguageDocId != null) {
                                ReaderLanguageTabSwitch(
                                    currentDoc = doc.info,
                                    onSwitchDoc = { newDocId ->
                                        val cached = repository.getCachedDocument(newDocId)
                                        if (cached != null) {
                                            parsedDoc = cached
                                        }
                                        currentDocId = newDocId
                                    }
                                )
                            }

                            // 2. Section Shortcut Bar if multiple sections exist
                            if (hasShortcuts) {
                                DocumentSectionShortcutRow(
                                    docId = doc.info.id,
                                    sections = sections,
                                    selectedSectionIndex = currentVisibleSectionIndex,
                                    onSelectSection = { idx ->
                                        coroutineScope.launch {
                                            if (tahunanPagerState != null) {
                                                tahunanPagerState.animateScrollToPage(idx)
                                            } else {
                                                listState.animateScrollToItem(idx)
                                            }
                                        }
                                    }
                                )
                            }
                        }

                        // Content Area
                        if (isTahunanPager && tahunanPagerState != null) {
                            // Horizontal ViewPager for Sholat Tahunan
                            HorizontalPager(
                                state = tahunanPagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { pageIdx ->
                                val sec = sections[pageIdx]
                                val pageScrollState = rememberLazyListState()
                                LazyColumn(
                                    state = pageScrollState,
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    item(key = sec.id) {
                                        SingleContinuousDocumentCard(
                                            rawContent = sec.content,
                                            fontScale = fontScale,
                                            isForceCentered = isDoaDoc
                                        )
                                    }
                                    item(key = "bottom_spacer_${sec.id}") {
                                        Spacer(modifier = Modifier.height(80.dp))
                                    }
                                }
                            }
                        } else {
                            // Standard Continuous Reading List
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                if (doc.info.isSingleDocumentView) {
                                    if (hasShortcuts) {
                                        itemsIndexed(sections, key = { _, sec -> sec.id }) { _, sec ->
                                            SingleContinuousDocumentCard(
                                                rawContent = sec.content,
                                                fontScale = fontScale,
                                                isForceCentered = isDoaDoc
                                            )
                                        }
                                    } else {
                                        item(key = "single_doc") {
                                            SingleContinuousDocumentCard(
                                                rawContent = doc.rawContent,
                                                fontScale = fontScale,
                                                isForceCentered = isDoaDoc
                                            )
                                        }
                                    }
                                } else {
                                    // Verses List
                                    items(doc.verses, key = { it.index }) { verse ->
                                        VerseReadingCard(
                                            verse = verse,
                                            fontScale = fontScale,
                                            isCentered = isDoaDoc,
                                            onClick = { selectedVerseForOptions = verse }
                                        )
                                    }
                                }

                                item(key = "bottom_spacer") {
                                    Spacer(modifier = Modifier.height(80.dp))
                                }
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Dokumen tidak ditemukan.", color = TextMuted)
                    }
                }
            }

            if (isDzikirDoc) {
                com.iqbalwork.robithoh.feature.tasbih.ui.component.FloatingTasbihOverlay(
                    state = tasbihState,
                    onIntent = tasbihViewModel::onIntent,
                    onOpenFullScreen = { onNavigateToTasbih?.invoke() }
                )
            }
        }
    }

    if (showSettingsDialog) {
        TextReaderSettingsSheet(
            fontScale = fontScale,
            onFontScaleChange = { fontScale = it },
            onDismiss = { showSettingsDialog = false }
        )
    }

    selectedVerseForOptions?.let { verse ->
        val shareText = remember(verse) {
            buildString {
                if (verse.title.isNotBlank()) {
                    append(verse.title)
                    if (verse.repeatCount > 1) append(" (${verse.repeatCount}x)")
                    append("\n\n")
                }
                if (verse.arabic.isNotBlank()) {
                    append(verse.arabic)
                    append("\n\n")
                }
                if (verse.latin.isNotBlank()) {
                    append(verse.latin.replace("**", "").replace("*", ""))
                    append("\n\n")
                }
                if (verse.translation.isNotBlank()) {
                    append("[Terjemahan]\n")
                    append(verse.translation.replace("**", "").replace("*", ""))
                    append("\n\n")
                }
                if (verse.note.isNotBlank()) {
                    append("Catatan: ")
                    append(verse.note.replace("**", "").replace("*", ""))
                    append("\n\n")
                }
                append("(${docInfo?.title ?: "Amaliyah TQN PP Suryalaya Sirnarasa"})")
            }
        }

        val customOptions = buildList {
            if (verse.repeatCount > 1) {
                add(
                    ContentItemOption(
                        icon = "📿",
                        label = "Hitung dengan Tasbih (${verse.repeatCount}x)",
                        onClick = {
                            tasbihViewModel.onIntent(com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent.SetTarget(verse.repeatCount))
                            tasbihViewModel.onIntent(com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent.SetFloatingExpanded(true))
                        }
                    )
                )
            }
        }

        ContentItemOptionsSheet(
            title = if (verse.title.isNotBlank()) verse.title else "${docInfo?.title ?: "Bacaan"} #${verse.index}",
            subtitle = if (verse.repeatCount > 1) "${verse.repeatCount}x Pengulangan" else null,
            onDismiss = { selectedVerseForOptions = null },
            onCopy = { clipboardManager.setText(AnnotatedString(shareText)) },
            copyLabel = "Salin Teks Bacaan",
            onShare = { shareAction(shareText) },
            shareLabel = "Bagikan Bacaan",
            customOptions = customOptions
        )
    }
}

@Composable
private fun DocumentHeaderCard(info: LiturgyDocument) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (info.arabicTitle.isNotEmpty()) {
                Text(
                    text = info.arabicTitle,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextCharcoal,
                    textAlign = TextAlign.Center,
                    lineHeight = 38.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            Text(
                text = info.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal,
                textAlign = TextAlign.Center
            )
            if (info.subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = info.subtitle,
                    fontSize = 13.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
            if (info.languageBadge != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MerahMerdeka.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = info.languageBadge,
                        color = MerahMerdeka,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReaderLanguageTabSwitch(
    currentDoc: LiturgyDocument,
    onSwitchDoc: (String) -> Unit
) {
    val isSunda = currentDoc.languageBadge?.equals("SUNDA", ignoreCase = true) == true || currentDoc.id.endsWith("_su")

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val idSelected = !isSunda
            Surface(
                color = if (idSelected) MerahMerdeka else Color.Transparent,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (!idSelected && currentDoc.alternateLanguageDocId != null) {
                            onSwitchDoc(currentDoc.alternateLanguageDocId)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🇮🇩 Bahasa Indonesia",
                        color = if (idSelected) Color.White else TextCharcoal,
                        fontWeight = if (idSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }

            Surface(
                color = if (isSunda) MerahMerdeka else Color.Transparent,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (isSunda.not() && currentDoc.alternateLanguageDocId != null) {
                            onSwitchDoc(currentDoc.alternateLanguageDocId)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🏴 Basa Sunda",
                        color = if (isSunda) Color.White else TextCharcoal,
                        fontWeight = if (isSunda) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SingleContinuousDocumentCard(
    rawContent: String,
    fontScale: Float,
    isForceCentered: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
            // Check if there is a Catatan / Catetan section
            val noteMarkerIndex = rawContent.indexOf("### Catat")
            val mainContent = if (noteMarkerIndex != -1) rawContent.substring(0, noteMarkerIndex).trim() else rawContent
            val noteContent = if (noteMarkerIndex != -1) rawContent.substring(noteMarkerIndex).trim() else null

            val cleanBlocks = mainContent.split("\n\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() && !it.startsWith("# ") }

            fun isIslamicMonth(text: String): Boolean {
                val clean = text.removePrefix("###")
                    .replace("’", "")
                    .replace("'", "")
                    .replace("-", "")
                    .replace(" ", "")
                    .trim()
                    .uppercase()
                return clean in setOf(
                    "MUHARROM", "SHOFAR", "SHOOFAR",
                    "ROBIULAWAL", "ROBIULAWWAL",
                    "ROBIUTSTANI", "ROBIUSTSTANI", "ROBIUTSTSANI", "ROBIUTSANI", "ROBIUSTSANI",
                    "JUMADILULA", "JUMADILAWAL", "JUMADILAWWAL",
                    "JUMADISTTSANIYAH", "JUMADITSTSANIYAH", "JUMADISTSANIYAH", "JUMADITSANIYAH", "JUMADILAKHIR",
                    "ROJAB", "RAJAB",
                    "SYABAN", "ROMADHON", "RAMADHAN", "RAMADHON",
                    "SYAWAL", "SYAWWAL",
                    "DZULQODAH", "DZULQOIDAH",
                    "DZULHIJJAH", "ZULHIJJAH",
                    "DOAMANQOBAH"
                )
            }

            for (block in cleanBlocks) {
                val lines = block.lines().map { it.trim() }.filter { it.isNotEmpty() }
                if (lines.isEmpty()) continue

                if (block == "---" || block.contains("۞۞۞")) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "۞   ۞   ۞",
                            fontSize = 16.sp,
                            color = EmasKhidmat,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    continue
                }

                val firstLine = lines.first()
                val isH4Header = firstLine.startsWith("#### ")
                val isH3Header = firstLine.startsWith("### ")
                val isH2Header = firstLine.startsWith("## ")
                val isHeader = isH4Header || isH3Header || isH2Header
                val remainingLines = if (isHeader) lines.drop(1) else lines

                if (isH2Header) {
                    // ## WAKTU ... section header → styled badge
                    val headerText = firstLine.removePrefix("## ").trim()
                    val waktuIcon = when {
                        headerText.contains("MALAM", ignoreCase = true) -> "🌙"
                        headerText.contains("SHUBUH", ignoreCase = true) || headerText.contains("SUBUH", ignoreCase = true) -> "🌅"
                        headerText.contains("ISYROQ", ignoreCase = true) || headerText.contains("ISYRAQ", ignoreCase = true) -> "☀️"
                        headerText.contains("DHUHA", ignoreCase = true) || headerText.contains("DUHA", ignoreCase = true) -> "🌤️"
                        headerText.contains("DZUHUR", ignoreCase = true) || headerText.contains("ZUHUR", ignoreCase = true) -> "☀️"
                        headerText.contains("ASHAR", ignoreCase = true) || headerText.contains("ASAR", ignoreCase = true) -> "🌇"
                        headerText.contains("MAGHRIB", ignoreCase = true) -> "🌆"
                        headerText.contains("ISYA", ignoreCase = true) -> "🌌"
                        else -> "🕌"
                    }
                    Surface(
                        color = MerahMarunGelap,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 6.dp)
                    ) {
                        Text(
                            text = "$waktuIcon  $headerText",
                            fontSize = (14 * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                } else if (isH4Header) {
                    val headerText = firstLine.removePrefix("#### ").trim()
                    val icon = when {
                        headerText.contains("Tanggal 1", ignoreCase = true) || headerText.contains("1.", ignoreCase = true) -> "1️⃣"
                        headerText.contains("Jumat", ignoreCase = true) || headerText.contains("Jum’at", ignoreCase = true) -> "🕌"
                        headerText.contains("15", ignoreCase = true) -> "🌕"
                        headerText.contains("30", ignoreCase = true) || headerText.contains("Akhir", ignoreCase = true) -> "🔚"
                        headerText.contains("Wirid", ignoreCase = true) -> "📿"
                        headerText.contains("Doa", ignoreCase = true) || headerText.contains("Do’a", ignoreCase = true) -> "🤲"
                        else -> "🗓️"
                    }
                    Surface(
                        color = MerahMarunGelap.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MerahMerdeka.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = icon, fontSize = (18 * fontScale).sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = headerText,
                                fontSize = (16 * fontScale).sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MerahMarunGelap
                            )
                        }
                    }
                } else if (isH3Header) {
                    val headerText = firstLine.removePrefix("### ").trim()
                    val isDateHeading = headerText.contains("Tanggal", ignoreCase = true) ||
                        headerText.contains("Jum’at", ignoreCase = true) ||
                        headerText.contains("Jumat", ignoreCase = true) ||
                        headerText.contains("Wirid", ignoreCase = true) ||
                        headerText.contains("Do’a", ignoreCase = true) ||
                        headerText.contains("Doa", ignoreCase = true)

                    if (isDateHeading) {
                        val icon = when {
                            headerText.contains("Tanggal 1", ignoreCase = true) || headerText.contains("1.", ignoreCase = true) -> "1️⃣"
                            headerText.contains("Jumat", ignoreCase = true) || headerText.contains("Jum’at", ignoreCase = true) -> "🕌"
                            headerText.contains("15", ignoreCase = true) -> "🌕"
                            headerText.contains("30", ignoreCase = true) || headerText.contains("Akhir", ignoreCase = true) -> "🔚"
                            headerText.contains("Wirid", ignoreCase = true) -> "📿"
                            headerText.contains("Doa", ignoreCase = true) || headerText.contains("Do’a", ignoreCase = true) -> "🤲"
                            else -> "🗓️"
                        }
                        Surface(
                            color = MerahMarunGelap.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, MerahMerdeka.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = icon, fontSize = (18 * fontScale).sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = headerText,
                                    fontSize = (16 * fontScale).sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MerahMarunGelap
                                )
                            }
                        }
                    } else if (isIslamicMonth(headerText)) {
                        val monthName = headerText.removePrefix("Ke-").removePrefix("Ka-").trim()
                        Surface(
                            color = MerahMarunGelap,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                        ) {
                            Text(
                                text = "📅  $monthName",
                                fontSize = (13 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = headerText,
                            fontSize = (15 * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = MerahMarunGelap
                        )
                    }
                }

                if (remainingLines.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        for (line in remainingLines) {
                            val trimmedLine = line.trim()
                            if (trimmedLine.isEmpty()) continue

                            if (trimmedLine == "---" || trimmedLine.contains("۞۞۞")) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "۞   ۞   ۞",
                                        fontSize = 15.sp,
                                        color = EmasKhidmat,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                continue
                            }

                            val arabicCharCount = trimmedLine.count { c -> c in '\u0600'..'\u06FF' || c in '\u0750'..'\u077F' || c in '\u08A0'..'\u08FF' }
                            val isArabic = arabicCharCount >= 3 && (arabicCharCount.toFloat() / trimmedLine.length.toFloat()) > 0.20
                            val isCentered = isArabic || trimmedLine.startsWith("*“Ilaa") || trimmedLine.startsWith("*Assalamu") || trimmedLine.startsWith("*Bismillaah") || trimmedLine.startsWith("*(Pangersa") || trimmedLine.startsWith("*Bikaromati") || trimmedLine.startsWith("PATAPAN") || trimmedLine.startsWith("Wasiat ini") || trimmedLine.startsWith("Ieu wasiat") || trimmedLine.startsWith("ttd") || trimmedLine.startsWith("ditawis") || trimmedLine.startsWith("**(") || trimmedLine.startsWith("Alloohumman tsur", ignoreCase = true) || trimmedLine.startsWith("YAA IMAMAL", ignoreCase = true) || trimmedLine.startsWith("WA YAA", ignoreCase = true)

                            val isMonthHeader = isIslamicMonth(trimmedLine)

                            val isManqobahHeading = trimmedLine.startsWith("Manqobah Ke-", ignoreCase = true) ||
                                trimmedLine.startsWith("Manqodah Ke-", ignoreCase = true) ||
                                trimmedLine.startsWith("MANQOBAH KA", ignoreCase = true) ||
                                trimmedLine.startsWith("MUQODIMAH", ignoreCase = true)

                            val matchNumbered = Regex("^(\\d+)\\.\\s*(.*)").find(trimmedLine)

                            if (isArabic) {
                                Text(
                                    text = trimmedLine.replace("**", "").replace("*", "").trim(),
                                    fontSize = (22 * fontScale).sp,
                                    lineHeight = (38 * fontScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextCharcoal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                )
                            } else if (isMonthHeader) {
                                val monthName = trimmedLine.removePrefix("###").removePrefix("Ke-").removePrefix("Ka-").trim()
                                Surface(
                                    color = MerahMarunGelap,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                                ) {
                                    Text(
                                        text = "📅  $monthName",
                                        fontSize = (13 * fontScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                    )
                                }
                            } else if (isManqobahHeading) {
                                Surface(
                                    color = GoldContainerLight.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.35f)),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = trimmedLine.replace("**", "").replace("*", "").trim(),
                                        fontSize = (14 * fontScale).sp,
                                        lineHeight = (21 * fontScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MerahMarunGelap,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                    )
                                }
                            } else if (trimmedLine.startsWith("#### ") || (trimmedLine.startsWith("### ") && !isMonthHeader)) {
                                val headerText = trimmedLine.removePrefix("#### ").removePrefix("### ").trim()
                                val icon = when {
                                    headerText.contains("Tanggal 1", ignoreCase = true) || headerText.contains("1.", ignoreCase = true) -> "1️⃣"
                                    headerText.contains("Jumat", ignoreCase = true) || headerText.contains("Jum’at", ignoreCase = true) -> "🕌"
                                    headerText.contains("15", ignoreCase = true) -> "🌕"
                                    headerText.contains("30", ignoreCase = true) || headerText.contains("Akhir", ignoreCase = true) -> "🔚"
                                    headerText.contains("Wirid", ignoreCase = true) -> "📿"
                                    headerText.contains("Doa", ignoreCase = true) || headerText.contains("Do’a", ignoreCase = true) -> "🤲"
                                    else -> "🗓️"
                                }
                                Surface(
                                    color = MerahMarunGelap.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MerahMerdeka.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = icon, fontSize = (18 * fontScale).sp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = headerText,
                                            fontSize = (16 * fontScale).sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MerahMarunGelap
                                        )
                                    }
                                }
                            } else if (matchNumbered != null) {
                                val num = matchNumbered.groupValues[1]
                                val rawText = matchNumbered.groupValues[2].trim()
                                val isNumberedBold = rawText.startsWith("**") || rawText.contains(":**")
                                val text = rawText.replace("**", "").replace("*", "").replace("\\", "").trim()
                                val isNumberedLatinArabic = text.startsWith("Usholli", ignoreCase = true) ||
                                    text.startsWith("Usholii", ignoreCase = true) ||
                                    text.startsWith("Alloohumma", ignoreCase = true) ||
                                    text.startsWith("Allohumma", ignoreCase = true) ||
                                    text.startsWith("Astaghfir", ignoreCase = true) ||
                                    text.startsWith("Azamtu", ignoreCase = true) ||
                                    text.startsWith("Qolbi", ignoreCase = true) ||
                                    text.startsWith("Qolbī", ignoreCase = true)
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Surface(
                                        color = MerahMerdeka.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = num,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MerahMerdeka
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = text,
                                        fontSize = if (isNumberedLatinArabic) (14.5f * fontScale).sp else (14 * fontScale).sp,
                                        lineHeight = if (isNumberedLatinArabic) (23 * fontScale).sp else (22 * fontScale).sp,
                                        color = TextCharcoal,
                                        fontWeight = if (isNumberedBold || isNumberedLatinArabic) FontWeight.Bold else FontWeight.Normal,
                                        fontStyle = if (isNumberedLatinArabic) FontStyle.Italic else FontStyle.Normal,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            } else if (trimmedLine.startsWith("- ")) {
                                // Bullet list item
                                val bulletText = trimmedLine.removePrefix("- ").trim()
                                val isBulletBold = bulletText.startsWith("**") || bulletText.contains(":**")
                                val cleanBullet = bulletText.replace("**", "").replace("*", "").replace("\\", "").trim()
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "•",
                                        fontSize = (14 * fontScale).sp,
                                        color = MerahMerdeka,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 2.dp, end = 8.dp)
                                    )
                                    Text(
                                        text = cleanBullet,
                                        fontSize = (14 * fontScale).sp,
                                        lineHeight = (22 * fontScale).sp,
                                        color = TextCharcoal,
                                        fontWeight = if (isBulletBold) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            } else {
                                val isBoldLabel = trimmedLine.startsWith("**") && trimmedLine.endsWith(":**")
                                val isBold = isBoldLabel || (trimmedLine.startsWith("**") && trimmedLine.endsWith("**")) || (trimmedLine.all { it.isUpperCase() || it.isWhitespace() || it == '-' || it == '(' || it == ')' || it == '.' || it == ':' || it == '\'' } && trimmedLine.length > 5 && !trimmedLine.contains("THORIIQOH"))
                                val isGreeting = trimmedLine.startsWith("Assalamualaikum", ignoreCase = true) || trimmedLine.startsWith("Wassalamu", ignoreCase = true)
                                val isItalic = (trimmedLine.startsWith("*") && trimmedLine.endsWith("*")) || (trimmedLine.startsWith("\u201C") && trimmedLine.endsWith("\u201D")) || trimmedLine.startsWith("Alloohumman tsur", ignoreCase = true)

                                val cleanText = trimmedLine.replace("**", "").replace("*", "").replace("\\", "").trim()

                                val isTranslation = (isItalic || cleanText.startsWith("Artinya", ignoreCase = true) || cleanText.startsWith("Sengaja", ignoreCase = true)) && (
                                    cleanText.startsWith("Sengaja", ignoreCase = true) ||
                                    cleanText.startsWith("Aku memohon", ignoreCase = true) ||
                                    cleanText.startsWith("Dengan menyebut", ignoreCase = true) ||
                                    cleanText.startsWith("Yaa اللّه", ignoreCase = true) ||
                                    cleanText.startsWith("Ya اللّه", ignoreCase = true) ||
                                    cleanText.startsWith("Tuhanku", ignoreCase = true) ||
                                    cleanText.startsWith("Artinya", ignoreCase = true) ||
                                    cleanText.startsWith("Katakan", ignoreCase = true) ||
                                    cleanText.startsWith("Semoga", ignoreCase = true) ||
                                    cleanText.startsWith("Khatur", ignoreCase = true) ||
                                    cleanText.startsWith("Abdi", ignoreCase = true) ||
                                    cleanText.startsWith("Nun Gusti", ignoreCase = true) ||
                                    cleanText.startsWith("Tiada daya", ignoreCase = true) ||
                                    cleanText.startsWith("Tiada Tuhan", ignoreCase = true) ||
                                    cleanText.startsWith("Segala puji", ignoreCase = true) ||
                                    cleanText.startsWith("Dia-lah", ignoreCase = true) ||
                                    cleanText.startsWith("Kalayan", ignoreCase = true) ||
                                    cleanText.startsWith("Dan Kami", ignoreCase = true) ||
                                    cleanText.startsWith("Maka apabila", ignoreCase = true) ||
                                    cleanText.startsWith("Bukankah", ignoreCase = true) ||
                                    cleanText.startsWith("Dan hanya", ignoreCase = true) ||
                                    cleanText.startsWith("Wahai orang", ignoreCase = true)
                                )

                                val isLatinArabic = !isGreeting && !isTranslation && (
                                    trimmedLine.startsWith("***") ||
                                    (isItalic && !isBoldLabel) ||
                                    cleanText.startsWith("Usholli", ignoreCase = true) ||
                                    cleanText.startsWith("Usholii", ignoreCase = true) ||
                                    cleanText.startsWith("Ilaa had", ignoreCase = true) ||
                                    cleanText.startsWith("Astaghfir", ignoreCase = true) ||
                                    cleanText.startsWith("Alloohumma", ignoreCase = true) ||
                                    cleanText.startsWith("Allohumma", ignoreCase = true) ||
                                    cleanText.startsWith("Laa ilaaha", ignoreCase = true) ||
                                    cleanText.startsWith("Subhaanallooh", ignoreCase = true) ||
                                    cleanText.startsWith("Subhanalloh", ignoreCase = true) ||
                                    cleanText.startsWith("Hasbunallooh", ignoreCase = true) ||
                                    cleanText.startsWith("Bismillaah", ignoreCase = true) ||
                                    cleanText.startsWith("Qul huwal", ignoreCase = true) ||
                                    cleanText.startsWith("Qul a'uudzu", ignoreCase = true) ||
                                    cleanText.startsWith("Qul A-", ignoreCase = true) ||
                                    cleanText.startsWith("Qul A‘", ignoreCase = true) ||
                                    cleanText.startsWith("In-naa a'thoi", ignoreCase = true) ||
                                    cleanText.startsWith("Innaa a'thoi", ignoreCase = true) ||
                                    cleanText.startsWith("Robbighfir", ignoreCase = true) ||
                                    cleanText.startsWith("Robbi", ignoreCase = true) ||
                                    cleanText.startsWith("Robbanaa", ignoreCase = true) ||
                                    cleanText.startsWith("Subbuhun", ignoreCase = true) ||
                                    cleanText.startsWith("Washollalloohu", ignoreCase = true) ||
                                    cleanText.startsWith("Walhamdulillaahi", ignoreCase = true) ||
                                    cleanText.startsWith("Tawakkaltu", ignoreCase = true) ||
                                    cleanText.startsWith("Wa'tashomtu", ignoreCase = true) ||
                                    cleanText.startsWith("Wa'tasoamtu", ignoreCase = true) ||
                                    cleanText.startsWith("Azamtu", ignoreCase = true) ||
                                    cleanText.startsWith("Qolbii", ignoreCase = true) ||
                                    cleanText.startsWith("Qolbi", ignoreCase = true) ||
                                    cleanText.startsWith("Tsumma ilaa", ignoreCase = true) ||
                                    cleanText.startsWith("Sayyidunaa", ignoreCase = true)
                                )

                                Text(
                                    text = cleanText,
                                    fontSize = when {
                                        isLatinArabic -> (14.5f * fontScale).sp
                                        isTranslation -> (13.5f * fontScale).sp
                                        else -> (14 * fontScale).sp
                                    },
                                    lineHeight = when {
                                        isLatinArabic -> (23 * fontScale).sp
                                        else -> (22 * fontScale).sp
                                    },
                                    fontWeight = when {
                                        isLatinArabic || isBold -> FontWeight.Bold
                                        isGreeting -> FontWeight.SemiBold
                                        else -> FontWeight.Normal
                                    },
                                    fontStyle = when {
                                        isLatinArabic || isTranslation || isItalic -> FontStyle.Italic
                                        else -> FontStyle.Normal
                                    },
                                    color = when {
                                        isTranslation -> TextMuted
                                        else -> TextCharcoal
                                    },
                                    textAlign = if (isCentered || isForceCentered) TextAlign.Center else TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            if (noteContent != null) {
                Surface(
                    color = GoldContainerLight.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val isSundaNote = noteContent.contains("Catetan", ignoreCase = true)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📌", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isSundaNote) "Catetan Penting Pangersa Abah" else "Catatan Penting Pangersa Abah",
                                fontWeight = FontWeight.Bold,
                                fontSize = (14 * fontScale).sp,
                                color = MerahMarunGelap
                            )
                        }
                        val cleanNote = noteContent.lines()
                            .filterNot { it.trim().startsWith("###") || it.trim() == "---" }
                            .joinToString("\n")
                            .trim()

                        Text(
                            text = cleanNote.replace("*", "").replace("\\", "").trim(),
                            fontSize = (13 * fontScale).sp,
                            lineHeight = (21 * fontScale).sp,
                            color = TextCharcoal,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}
}

@Composable
private fun VerseReadingCard(
    verse: LiturgyVerse,
    fontScale: Float,
    isCentered: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    var countProgress by remember(verse.index) { mutableStateOf(0) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Repetition counter (index badge intentionally removed)
                if (verse.repeatCount > 1) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (countProgress >= verse.repeatCount) HijauKhasRobithoh.copy(alpha = 0.15f) else MerahMerdeka.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.clickable {
                                countProgress = (countProgress + 1) % (verse.repeatCount + 1)
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (countProgress >= verse.repeatCount) "✓ Selesai ${verse.repeatCount}x" else "Hitung: $countProgress / ${verse.repeatCount}x",
                                    color = if (countProgress >= verse.repeatCount) HijauKhasRobithoh else MerahMerdeka,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                if (verse.title.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = verse.title,
                        fontSize = (15 * fontScale).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MerahMarunGelap,
                        textAlign = if (isCentered) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Arabic Text
                if (verse.arabic.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = verse.arabic,
                        fontSize = (24 * fontScale).sp,
                        lineHeight = (48 * fontScale).sp,
                        fontWeight = FontWeight.Normal,
                        color = TextCharcoal,
                        textAlign = if (isCentered) TextAlign.Center else TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Latin Transliteration
                if (verse.latin.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = parseMarkdownFormatting(verse.latin),
                        fontSize = (14 * fontScale).sp,
                        lineHeight = (22 * fontScale).sp,
                        color = Color(0xFF64748B),
                        fontStyle = FontStyle.Italic,
                        textAlign = if (isCentered) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Translation
                if (verse.translation.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = parseMarkdownFormatting(verse.translation),
                        fontSize = (14 * fontScale).sp,
                        lineHeight = (22 * fontScale).sp,
                        color = TextCharcoal,
                        textAlign = if (isCentered) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Notes / Fadhilah
                if (verse.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = PaperBackgroundLight,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = parseMarkdownFormatting(verse.note),
                            fontSize = (12 * fontScale).sp,
                            lineHeight = (18 * fontScale).sp,
                            color = Color(0xFF475569),
                            textAlign = if (isCentered) TextAlign.Center else TextAlign.Start,
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun parseMarkdownFormatting(text: String): AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0
        val tokenRegex = Regex("""(\*\*(.+?)\*\*)|(__(.+?)__)|(\*(.+?)\*)|(_(.+?)_)""")
        val matches = tokenRegex.findAll(text)

        for (match in matches) {
            val start = match.range.first
            val end = match.range.last + 1
            if (start > cursor) {
                append(text.substring(cursor, start))
            }
            when {
                match.groups[1] != null -> {
                    val content = match.groups[2]?.value ?: ""
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(content)
                    }
                }
                match.groups[3] != null -> {
                    val content = match.groups[4]?.value ?: ""
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(content)
                    }
                }
                match.groups[5] != null -> {
                    val content = match.groups[6]?.value ?: ""
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(content)
                    }
                }
                match.groups[7] != null -> {
                    val content = match.groups[8]?.value ?: ""
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(content)
                    }
                }
            }
            cursor = end
        }
        if (cursor < text.length) {
            append(text.substring(cursor))
        }
    }
}

data class ReaderDocumentSection(
    val id: String,
    val title: String?,
    val content: String
)

fun parseDocumentSections(docId: String, rawContent: String): List<ReaderDocumentSection> {
    if (docId.startsWith("manqobah")) {
        val monthRegex = Regex("""(?m)^###\s+(MUHARROM|SHOOFAR|SHOFAR|ROBI'?UL\s+AWAL|ROBI'?UTS?\s+TSANI|JUMADIL\s+ULA|JUMADITS?\s+TSANIYAH|ROJAB|SYA'?BAN|ROMADHON|SYAWAL|DZULQO'?DAH|DZULHIJJAH|DOA\s+MANQOBAH)""", RegexOption.IGNORE_CASE)
        val matches = monthRegex.findAll(rawContent).toList()
        if (matches.isEmpty()) {
            return listOf(ReaderDocumentSection(id = "full", title = null, content = rawContent))
        }

        val sections = mutableListOf<ReaderDocumentSection>()
        val firstMatch = matches.first()
        if (firstMatch.range.first > 0) {
            val introText = rawContent.substring(0, firstMatch.range.first).trim()
            if (introText.isNotEmpty()) {
                sections.add(ReaderDocumentSection(id = "muqodimah", title = "Muqodimah", content = introText))
            }
        }

        for (i in matches.indices) {
            val match = matches[i]
            val header = match.groupValues[1].trim()
            val start = match.range.first
            val end = if (i + 1 < matches.size) matches[i + 1].range.first else rawContent.length
            val sectionContent = rawContent.substring(start, end).trim()
            sections.add(ReaderDocumentSection(id = "month_$i", title = header, content = sectionContent))
        }
        return sections
    } else if (docId.startsWith("sholat_harian")) {
        val waktuRegex = Regex("""(?m)^##\s+(WAKTU\s+[A-Z’']+|SEBELUM\s+TIDUR|HENDAK\s+TIDUR|WAKTU\s+SEBELUM\s+TIDUR)""", RegexOption.IGNORE_CASE)
        val matches = waktuRegex.findAll(rawContent).toList()
        if (matches.isEmpty()) {
            return listOf(ReaderDocumentSection(id = "full", title = null, content = rawContent))
        }

        val sections = mutableListOf<ReaderDocumentSection>()
        val firstMatch = matches.first()
        if (firstMatch.range.first > 0) {
            val introText = rawContent.substring(0, firstMatch.range.first).trim()
            if (introText.isNotEmpty()) {
                sections.add(ReaderDocumentSection(id = "ringkasan", title = "Ringkasan", content = introText))
            }
        }

        for (i in matches.indices) {
            val match = matches[i]
            val header = match.groupValues[1].trim()
            val start = match.range.first
            val end = if (i + 1 < matches.size) matches[i + 1].range.first else rawContent.length
            val sectionContent = rawContent.substring(start, end).trim()
            sections.add(ReaderDocumentSection(id = "waktu_$i", title = header, content = sectionContent))
        }
        return sections
    } else if (docId.startsWith("sholat_tahunan")) {
        val annualRegex = Regex("""(?m)^###?\s+(SHOLAT\s+[^\n]+|\d+\.\s+[^\n]+)""", RegexOption.IGNORE_CASE)
        val matches = annualRegex.findAll(rawContent).toList()
        if (matches.isEmpty()) {
            return listOf(ReaderDocumentSection(id = "full", title = null, content = rawContent))
        }

        val sections = mutableListOf<ReaderDocumentSection>()
        for (i in matches.indices) {
            val match = matches[i]
            val header = match.groupValues[1].trim()
            val start = match.range.first
            val end = if (i + 1 < matches.size) matches[i + 1].range.first else rawContent.length
            val sectionContent = rawContent.substring(start, end).trim()
            sections.add(ReaderDocumentSection(id = "tahunan_$i", title = header, content = sectionContent))
        }
        return sections
    } else if (docId.startsWith("sholat_bulanan")) {
        return listOf(ReaderDocumentSection(id = "full", title = null, content = rawContent))
    } else if (docId.startsWith("sholat_rojab") || docId.startsWith("sholat_rajab")) {
        val headerRegex = Regex("""(?m)^###\s+([^\n]+)""")
        val matches = headerRegex.findAll(rawContent).toList()
        if (matches.isEmpty()) {
            return listOf(ReaderDocumentSection(id = "full", title = null, content = rawContent))
        }

        val sections = mutableListOf<ReaderDocumentSection>()
        for (i in matches.indices) {
            val match = matches[i]
            val header = match.groupValues[1].trim()
            val start = match.range.first
            val end = if (i + 1 < matches.size) matches[i + 1].range.first else rawContent.length
            val sectionContent = rawContent.substring(start, end).trim()
            sections.add(ReaderDocumentSection(id = "rojab_$i", title = header, content = sectionContent))
        }
        return sections
    } else if (docId.startsWith("sholat_safar")) {
        val headerRegex = Regex("""(?m)^###\s+([^\n]+)""")
        val matches = headerRegex.findAll(rawContent).toList()
        if (matches.isEmpty()) {
            return listOf(ReaderDocumentSection(id = "full", title = null, content = rawContent))
        }

        val sections = mutableListOf<ReaderDocumentSection>()
        val firstMatch = matches.first()
        if (firstMatch.range.first > 0) {
            val introText = rawContent.substring(0, firstMatch.range.first).trim()
            if (introText.isNotEmpty()) {
                sections.add(ReaderDocumentSection(id = "intro", title = "Pengantar", content = introText))
            }
        }

        for (i in matches.indices) {
            val match = matches[i]
            val header = match.groupValues[1].trim()
            val start = match.range.first
            val end = if (i + 1 < matches.size) matches[i + 1].range.first else rawContent.length
            val sectionContent = rawContent.substring(start, end).trim()
            sections.add(ReaderDocumentSection(id = "safar_$i", title = header, content = sectionContent))
        }
        return sections
    }

    return listOf(ReaderDocumentSection(id = "full", title = null, content = rawContent))
}

@Composable
fun DocumentSectionShortcutRow(
    docId: String,
    sections: List<ReaderDocumentSection>,
    selectedSectionIndex: Int,
    onSelectSection: (Int) -> Unit
) {
    val shortcutRowState = rememberLazyListState()

    LaunchedEffect(selectedSectionIndex) {
        if (selectedSectionIndex in sections.indices) {
            shortcutRowState.animateScrollToItem(selectedSectionIndex)
        }
    }

    val shortcutHeader = when {
        docId.startsWith("sholat_harian") -> "PILIH WAKTU SHOLAT :"
        docId.startsWith("sholat_tahunan") -> "PILIH SHOLAT TAHUNAN :"
        docId.startsWith("sholat_rojab") || docId.startsWith("sholat_rajab") -> "PILIH PANDUAN ROJAB :"
        docId.startsWith("sholat_bulanan") -> "PILIH AMALIYAH BULANAN :"
        docId.startsWith("sholat_safar") -> "PILIH PANDUAN SAFAR :"
        docId.startsWith("manqobah") -> "PILIH BULAN ISLAM :"
        else -> "PILIH BAGIAN :"
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            text = shortcutHeader,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
        )
        LazyRow(
            state = shortcutRowState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(sections) { idx, sec ->
                val isSelected = idx == selectedSectionIndex
                val cleanTitle = sec.title?.uppercase()
                    ?.replace("’", "")
                    ?.replace("'", "")
                    ?.replace(" ", "") ?: ""

                val (label, icon) = when {
                    // Sholat Rojab
                    docId.startsWith("sholat_rojab") || docId.startsWith("sholat_rajab") -> when {
                        cleanTitle.contains("MAKLUMAT") -> "Maklumat" to "📜"
                        cleanTitle.contains("SHOLATROJAB") -> "Ringkasan" to "🌙"
                        cleanTitle.contains("15") -> "Tgl 15 Rojab" to "🌕"
                        cleanTitle.contains("30") || cleanTitle.contains("AKHIR") -> "Akhir Rojab" to "🔚"
                        cleanTitle.contains("JUMAT") -> "Malam Jum'at 1" to "🕌"
                        cleanTitle.contains("TANGGAL1") || cleanTitle.contains("1.TANGGAL") -> "Tgl 1 Rojab" to "1️⃣"
                        cleanTitle.contains("WIRID") -> "Wirid Maghrib" to "📿"
                        cleanTitle.contains("DOA") -> "Doa Rojab" to "🤲"
                        else -> (sec.title ?: "Bagian ${idx + 1}") to "🌙"
                    }

                    // Sholat Tahunan
                    docId.startsWith("sholat_tahunan") -> when {
                        cleanTitle.contains("ROJAB") -> "Sholat Rojab" to "🌙"
                        cleanTitle.contains("NISFU") || cleanTitle.contains("SYABAN") || cleanTitle.contains("SABAN") -> "Sholat Nisfu Sa'ban" to "✨"
                        cleanTitle.contains("TAROWIH") || cleanTitle.contains("TARAWIH") -> "Sholat Tarowih" to "🕌"
                        cleanTitle.contains("LIDAF") -> "Sholat Lidaf'il bala" to "🛡️"
                        cleanTitle.contains("LAILATUL") -> "Sholat Lailatul qodar" to "🌟"
                        else -> (sec.title ?: "Sholat ${idx + 1}") to "🕌"
                    }

                    // Sholat Harian
                    docId.startsWith("sholat_harian") -> when {
                        cleanTitle == "RINGKASAN" -> "Ringkasan" to "📋"
                        cleanTitle == "PENGANTAR" -> "Pengantar" to "📜"
                        cleanTitle.contains("MALAM") -> "Malam" to "🌙"
                        cleanTitle.contains("SHUBUH") || cleanTitle.contains("SUBUH") -> "Shubuh" to "🌅"
                        cleanTitle.contains("ISYROQ") || cleanTitle.contains("ISYRAQ") -> "Isyroq" to "☀️"
                        cleanTitle.contains("DHUHA") || cleanTitle.contains("DUHA") -> "Dhuha" to "🌤️"
                        cleanTitle.contains("DZUHUR") || cleanTitle.contains("ZUHUR") -> "Dzuhur" to "☀️"
                        cleanTitle.contains("ASHAR") || cleanTitle.contains("ASAR") -> "Ashar" to "🌇"
                        cleanTitle.contains("MAGHRIB") -> "Maghrib" to "🌆"
                        cleanTitle.contains("ISYA") -> "Isya" to "🌌"
                        cleanTitle.contains("SEBELUM") || cleanTitle.contains("TIDUR") -> "Sebelum Tidur" to "🛌"
                        else -> (sec.title ?: "Waktu ${idx + 1}") to "⏰"
                    }

                    // Sholat Safar
                    docId.startsWith("sholat_safar") -> when {
                        cleanTitle.contains("DZUHUR") -> "Dzuhur & Ashar" to "☀️"
                        cleanTitle.contains("MAGHRIB") -> "Maghrib & Isya" to "🌆"
                        cleanTitle.contains("CATATAN") -> "Catatan Safar" to "📝"
                        else -> (sec.title ?: "Panduan ${idx + 1}") to "🚗"
                    }

                    // Sholat Bulanan
                    docId.startsWith("sholat_bulanan") -> "Sholat Lailatul Qodar" to "🌟"

                    // Manqobah
                    docId.startsWith("manqobah") -> when {
                        cleanTitle == "MUQODIMAH" -> "Muqodimah" to "📜"
                        cleanTitle == "MUHARROM" -> "Muharrom" to "📅"
                        cleanTitle in setOf("SHOOFAR", "SHOFAR") -> "Shoofar" to "📅"
                        cleanTitle in setOf("ROBIULAWAL", "ROBIULAWWAL") -> "Robi'ul Awal" to "📅"
                        cleanTitle.startsWith("ROBIUT") || cleanTitle.startsWith("ROBIUST") -> "Robi'uts Tsani" to "📅"
                        cleanTitle.startsWith("JUMADILU") -> "Jumadil Ula" to "📅"
                        cleanTitle.startsWith("JUMADIT") || cleanTitle.startsWith("JUMADIST") -> "Jumadits Tsaniyah" to "📅"
                        cleanTitle in setOf("ROJAB", "RAJAB") -> "Rojab" to "📅"
                        cleanTitle.startsWith("SYABAN") -> "Sya'ban" to "📅"
                        cleanTitle.startsWith("ROMADHON") || cleanTitle.startsWith("RAMADHAN") -> "Romadhon" to "📅"
                        cleanTitle.startsWith("SYAWAL") || cleanTitle.startsWith("SYAWWAL") -> "Syawal" to "📅"
                        cleanTitle.startsWith("DZULQO") -> "Dzulqo'dah" to "📅"
                        cleanTitle.startsWith("DZULHIJ") || cleanTitle.startsWith("ZULHIJ") -> "Dzulhijjah" to "📅"
                        cleanTitle.startsWith("DOAMAN") -> "Doa Manqobah" to "🤲"
                        else -> (sec.title ?: "Bulan ${idx + 1}") to "📅"
                    }

                    else -> (sec.title ?: "Bagian ${idx + 1}") to "📌"
                }

                Surface(
                    color = if (isSelected) MerahMerdeka else Color.White,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) MerahMerdeka else MerahMerdeka.copy(alpha = 0.25f)
                    ),
                    shadowElevation = if (isSelected) 2.dp else 0.5.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectSection(idx) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = icon,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isSelected) Color.White else MerahMarunGelap
                        )
                    }
                }
            }
        }
    }
}
