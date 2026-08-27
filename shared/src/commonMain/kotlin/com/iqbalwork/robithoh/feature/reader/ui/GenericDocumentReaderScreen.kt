package com.iqbalwork.robithoh.feature.reader.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.TextReaderSettingsSheet
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import com.iqbalwork.robithoh.feature.reader.model.LiturgyDocument
import com.iqbalwork.robithoh.feature.reader.model.LiturgyVerse
import com.iqbalwork.robithoh.feature.reader.model.ParsedDocument
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericDocumentReaderScreen(
    documentId: String,
    onBack: () -> Unit,
    repository: MarkdownDocumentRepository = remember { MarkdownDocumentRepository() }
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val initialCachedDoc = remember(documentId) { repository.getCachedDocument(documentId) }
    var currentDocId by remember(documentId) { mutableStateOf(documentId) }
    var parsedDoc by remember { mutableStateOf<ParsedDocument?>(initialCachedDoc) }
    var isLoading by remember { mutableStateOf(parsedDoc == null) }
    var fontScale by remember { mutableStateOf(1.0f) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val docInfo = remember(currentDocId) { repository.getDocumentById(currentDocId) }

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
                    val hasShortcuts = sections.size > 1

                    val currentVisibleSectionIndex by remember {
                        derivedStateOf {
                            val firstVisible = listState.firstVisibleItemIndex
                            if (firstVisible in sections.indices) firstVisible else 0
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
                                            listState.animateScrollToItem(idx)
                                        }
                                    }
                                )
                            }
                        }

                        // Reading List
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
                                            fontScale = fontScale
                                        )
                                    }
                                } else {
                                    item(key = "single_doc") {
                                        SingleContinuousDocumentCard(
                                            rawContent = doc.rawContent,
                                            fontScale = fontScale
                                        )
                                    }
                                }
                            } else {
                                // Verses List
                                items(doc.verses, key = { it.index }) { verse ->
                                    VerseReadingCard(
                                        verse = verse,
                                        fontScale = fontScale
                                    )
                                }
                            }

                            item(key = "bottom_spacer") {
                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Dokumen tidak ditemukan.", color = TextMuted)
                    }
                }
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
                    color = MerahMarunGelap,
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
    fontScale: Float
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
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
                val isH2Header = firstLine.startsWith("## ")
                val isHeader = firstLine.startsWith("### ") || isH2Header
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
                } else if (isHeader) {
                    val headerText = firstLine.removePrefix("### ").trim()
                    if (isIslamicMonth(headerText)) {
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
                    if (remainingLines.any { it.matches(Regex("^\\d+\\..*")) }) {
                        // Numbered List
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (line in remainingLines) {
                                val match = Regex("^(\\d+)\\.\\s*(.*)").find(line)
                                if (match != null) {
                                    val num = match.groupValues[1]
                                    val rawText = match.groupValues[2].trim()
                                    val isSubBold = rawText.startsWith("**") && rawText.contains(":**")
                                    val text = rawText.replace("**", "").replace("*", "").trim()
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
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
                                            fontSize = (14 * fontScale).sp,
                                            lineHeight = (22 * fontScale).sp,
                                            color = TextCharcoal,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                } else {
                                    Text(
                                        text = line,
                                        fontSize = (14 * fontScale).sp,
                                        lineHeight = (22 * fontScale).sp,
                                        color = TextCharcoal
                                    )
                                }
                            }
                        }
                    } else {
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

                                if (isArabic) {
                                    Text(
                                        text = trimmedLine,
                                        fontSize = (22 * fontScale).sp,
                                        lineHeight = (38 * fontScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MerahMarunGelap,
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
                                } else if (trimmedLine.startsWith("- ")) {
                                    // Bullet list item
                                    val bulletText = trimmedLine.removePrefix("- ").trim()
                                    val cleanBullet = bulletText.replace("**", "").replace("*", "").trim()
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
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                } else {
                                    val isBoldLabel = trimmedLine.startsWith("**") && trimmedLine.endsWith(":**")
                                    val isBold = isBoldLabel || (trimmedLine.startsWith("**") && trimmedLine.endsWith("**")) || (trimmedLine.all { it.isUpperCase() || it.isWhitespace() || it == '-' || it == '(' || it == ')' || it == '.' || it == ':' || it == '\'' } && trimmedLine.length > 5 && !trimmedLine.contains("THORIIQOH"))
                                    val isGreeting = trimmedLine.startsWith("Assalamualaikum", ignoreCase = true) || trimmedLine.startsWith("Wassalamu", ignoreCase = true)
                                    val isItalic = (trimmedLine.startsWith("*") && trimmedLine.endsWith("*")) || (trimmedLine.startsWith("\u201C") && trimmedLine.endsWith("\u201D")) || trimmedLine.startsWith("Alloohumman tsur", ignoreCase = true)

                                    val cleanText = trimmedLine.replace("**", "").replace("*", "").replace("\\", "").trim()

                                    Text(
                                        text = cleanText,
                                        fontSize = (14 * fontScale).sp,
                                        lineHeight = (22 * fontScale).sp,
                                        fontWeight = when {
                                            isBold -> FontWeight.Bold
                                            isGreeting -> FontWeight.SemiBold
                                            else -> FontWeight.Normal
                                        },
                                        fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                                        color = when {
                                            isGreeting || isBold -> MerahMarunGelap
                                            isItalic -> TextMuted
                                            else -> TextCharcoal
                                        },
                                        textAlign = if (isCentered) TextAlign.Center else TextAlign.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
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

@Composable
private fun VerseReadingCard(
    verse: LiturgyVerse,
    fontScale: Float
) {
    var countProgress by remember(verse.index) { mutableStateOf(0) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
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
                    color = MerahMarunGelap
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
                    textAlign = TextAlign.Right,
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
                    fontStyle = FontStyle.Italic
                )
            }

            // Translation
            if (verse.translation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = parseMarkdownFormatting(verse.translation),
                    fontSize = (14 * fontScale).sp,
                    lineHeight = (22 * fontScale).sp,
                    color = TextCharcoal
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
                        modifier = Modifier.padding(10.dp)
                    )
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
        val waktuRegex = Regex("""(?m)^##\s+(WAKTU\s+[A-Z’']+|SEBELUM\s+TIDUR)""", RegexOption.IGNORE_CASE)
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
                    // Sholat Harian
                    cleanTitle == "RINGKASAN" -> "Ringkasan" to "📋"
                    cleanTitle.contains("MALAM") -> "Malam" to "🌙"
                    cleanTitle.contains("SHUBUH") || cleanTitle.contains("SUBUH") -> "Shubuh" to "🌅"
                    cleanTitle.contains("ISYROQ") || cleanTitle.contains("ISYRAQ") -> "Isyroq" to "☀️"
                    cleanTitle.contains("DHUHA") || cleanTitle.contains("DUHA") -> "Dhuha" to "🌤️"
                    cleanTitle.contains("DZUHUR") || cleanTitle.contains("ZUHUR") -> "Dzuhur" to "☀️"
                    cleanTitle.contains("ASHAR") || cleanTitle.contains("ASAR") -> "Ashar" to "🌇"
                    cleanTitle.contains("MAGHRIB") -> "Maghrib" to "🌆"
                    cleanTitle.contains("ISYA") -> "Isya" to "🌌"

                    // Manqobah
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
