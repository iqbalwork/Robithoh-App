package com.iqbalwork.robithoh.feature.library.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.quran.data.QuranData
import com.iqbalwork.robithoh.feature.quran.model.QuranBookmark
import com.iqbalwork.robithoh.feature.quran.model.RevelationType

@Composable
fun KitabTabContent(
    onNavigateToSurah: (Int, Int?) -> Unit,
    lastReadBookmark: QuranBookmark? = null,
    onBack: (() -> Unit)? = null
) {
    var searchQuery by remember { mutableStateOf("") }

    val allSurahs = remember { QuranData.surahs }

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allSurahs
        } else {
            val query = searchQuery.trim().lowercase()
            allSurahs.filter { surah ->
                surah.nameLatin.lowercase().contains(query) ||
                surah.indonesianMeaning.lowercase().contains(query) ||
                surah.nameArabic.contains(query) ||
                surah.number.toString() == query
            }
        }
    }

    val pastelColors = listOf(
        Color(0xFFFFE8D6),
        Color(0xFFE2F0D9),
        Color(0xFFD9E8F5),
        Color(0xFFFBE4E6),
        Color(0xFFF3E5F5),
        Color(0xFFFFF9C4)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBackgroundLight),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onBack != null) {
                        Surface(
                            color = MerahMerdeka,
                            shape = CircleShape,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .clickable { onBack() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                // Drawn rather than a text glyph — the "←" character's
                                // font metrics render it visibly off-center in a circle.
                                Canvas(modifier = Modifier.size(16.dp)) {
                                    val strokeWidthPx = 2.4.dp.toPx()
                                    val path = Path().apply {
                                        moveTo(size.width * 0.62f, size.height * 0.12f)
                                        lineTo(size.width * 0.18f, size.height * 0.5f)
                                        lineTo(size.width * 0.62f, size.height * 0.88f)
                                    }
                                    drawPath(
                                        path = path,
                                        color = Color.White,
                                        style = Stroke(
                                            width = strokeWidthPx,
                                            cap = StrokeCap.Round,
                                            join = StrokeJoin.Round
                                        )
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column {
                        Text(
                            text = "Al-Qur'an",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextCharcoal
                        )
                        Text(
                            text = "114 Surah · Mushaf Digital",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                Surface(
                    color = Color(0xFFDDF5E6),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "الْقُرْآنُ الْكَرِيمُ",
                        color = Color(0xFF1E824C),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // 2. Terakhir Dibaca (last read)
        if (lastReadBookmark != null) {
            item {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                    contentPadding = PaddingValues(16.dp),
                    onClick = { onNavigateToSurah(lastReadBookmark.surahNumber, lastReadBookmark.ayahNumber) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TERAKHIR DIBACA",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmasMuda,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lastReadBookmark.surahName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = PutihBersih
                            )
                            Text(
                                text = "Ayat ${lastReadBookmark.ayahNumber}",
                                fontSize = 13.sp,
                                color = EmasMuda
                            )
                        }
                        Surface(
                            color = Color.White.copy(alpha = 0.18f),
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("▶", color = PutihBersih, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 3. Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari 114 Surah (cth: Yasin, Al-Mulk, Al-Kahf)...", fontSize = 13.sp, color = TextMuted) },
                leadingIcon = { Text("🔍", fontSize = 14.sp) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MerahMerdeka,
                    unfocusedBorderColor = BorderSubtle
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 4. Section Title
        item {
            Text(
                text = "DAFTAR SURAH (${filteredSurahs.size})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        // 5. List 114 Surahs
        items(filteredSurahs.size, key = { filteredSurahs[it].number }) { index ->
            val surah = filteredSurahs[index]
            val badgeColor = pastelColors[(surah.number - 1) % pastelColors.size]

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSurah(surah.number, null) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Number Badge
                    Surface(
                        color = badgeColor,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${surah.number}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextCharcoal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = surah.nameLatin,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextCharcoal
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (surah.revelationType == RevelationType.MAKKIYAH) Color(0xFFFFF3E0) else Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = surah.revelationType.label,
                                    color = if (surah.revelationType == RevelationType.MAKKIYAH) Color(0xFFE65100) else Color(0xFF2E7D32),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${surah.indonesianMeaning} • ${surah.numberOfAyahs} Ayat",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = surah.nameArabic,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MerahMarunGelap,
                        textAlign = TextAlign.Right
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
