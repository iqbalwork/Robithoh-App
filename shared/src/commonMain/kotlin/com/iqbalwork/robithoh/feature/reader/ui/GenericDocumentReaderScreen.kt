package com.iqbalwork.robithoh.feature.reader.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    var parsedDoc by remember { mutableStateOf<ParsedDocument?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var fontScale by remember { mutableStateOf(1.0f) }
    var showLatin by remember { mutableStateOf(true) }
    var showTranslation by remember { mutableStateOf(true) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val docInfo = remember(documentId) { repository.getDocumentById(documentId) }

    LaunchedEffect(documentId) {
        isLoading = true
        if (docInfo != null) {
            parsedDoc = repository.loadDocumentContent(docInfo)
        }
        isLoading = false
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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header Banner
                        item {
                            DocumentHeaderCard(doc.info)
                        }

                        // Verses List
                        items(doc.verses, key = { it.index }) { verse ->
                            VerseReadingCard(
                                verse = verse,
                                fontScale = fontScale,
                                showLatin = showLatin,
                                showTranslation = showTranslation
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
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
        ReaderSettingsModalBottomSheet(
            fontScale = fontScale,
            onFontScaleChange = { fontScale = it },
            showLatin = showLatin,
            onShowLatinChange = { showLatin = it },
            showTranslation = showTranslation,
            onShowTranslationChange = { showTranslation = it },
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
private fun VerseReadingCard(
    verse: LiturgyVerse,
    fontScale: Float,
    showLatin: Boolean,
    showTranslation: Boolean
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
            // Verse Header: Index & Repetition counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = GoldContainerLight,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "#${verse.index}",
                        color = GoldOnContainerLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (verse.repeatCount > 1) {
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
            if (showLatin && verse.latin.isNotEmpty()) {
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
            if (showTranslation && verse.translation.isNotEmpty()) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReaderSettingsModalBottomSheet(
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    showLatin: Boolean,
    onShowLatinChange: (Boolean) -> Unit,
    showTranslation: Boolean,
    onShowTranslationChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "Pengaturan Teks & Bacaan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Font Scale Slider
            Text(
                text = "Ukuran Huruf Arab: ${(fontScale * 100).toInt()}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("A-", fontSize = 14.sp, color = TextMuted)
                Slider(
                    value = fontScale,
                    onValueChange = onFontScaleChange,
                    valueRange = 0.85f..1.65f,
                    steps = 5,
                    colors = SliderDefaults.colors(
                        thumbColor = MerahMerdeka,
                        activeTrackColor = MerahMerdeka
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                )
                Text("A+", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextCharcoal)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BorderSubtle)
            Spacer(modifier = Modifier.height(16.dp))

            // Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Teks Latin (Transliterasi)", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextCharcoal)
                    Text("Panduan lafal bagi pemula", fontSize = 12.sp, color = TextMuted)
                }
                Switch(
                    checked = showLatin,
                    onCheckedChange = onShowLatinChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MerahMerdeka
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Terjemahan (Indonesia / Sunda)", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextCharcoal)
                    Text("Mengetahui makna & kandungan", fontSize = 12.sp, color = TextMuted)
                }
                Switch(
                    checked = showTranslation,
                    onCheckedChange = onShowTranslationChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MerahMerdeka
                    )
                )
            }
        }
    }
}
