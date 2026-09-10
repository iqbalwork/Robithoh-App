package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.quran.data.QuranPageLookup
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class GoToTab(val title: String) {
    SURAH("Surat & Ayat"),
    PAGE("Halaman"),
    JUZ("Juz")
}

/**
 * Bottom sheet letting the reader jump straight to a chosen Surat & Ayat,
 * Halaman (1 - 604), or Juz (1 - 30).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoToSurahAyahSheet(
    surahs: List<SurahMeta>,
    initialSurahNumber: Int,
    initialAyahNumber: Int = 1,
    initialPageNumber: Int? = null,
    onDismiss: () -> Unit,
    onConfirm: (surahNumber: Int, ayahNumber: Int) -> Unit,
    onConfirmPage: ((pageNumber: Int) -> Unit)? = null
) {
    val isDark = RabithohTheme.colors.isDark
    val textColor = if (isDark) PutihBersih else SlateCharcoalText
    val labelColor = if (isDark) DarkMuted else SlateMuted
    val borderColor = if (isDark) DarkBorder else BorderSubtle

    var selectedTab by remember {
        mutableStateOf(if (initialPageNumber != null) GoToTab.PAGE else GoToTab.SURAH)
    }

    // Surah & Ayah state
    var selectedSurah by remember {
        mutableStateOf(surahs.find { it.number == initialSurahNumber } ?: surahs.firstOrNull())
    }
    var ayahText by remember { mutableStateOf(initialAyahNumber.coerceAtLeast(1).toString()) }
    var showSurahPicker by remember { mutableStateOf(false) }

    // Page state
    val defaultPage = initialPageNumber ?: QuranPageLookup.getPageForAyah(initialSurahNumber, initialAyahNumber)
    var pageText by remember { mutableStateOf(defaultPage.toString()) }

    // Juz state
    val defaultJuz = QuranPageLookup.getPageMeta(defaultPage).juz
    var selectedJuz by remember { mutableStateOf(defaultJuz) }

    LaunchedEffect(selectedSurah) {
        val maxAyah = selectedSurah?.numberOfAyahs ?: 1
        val current = ayahText.toIntOrNull()
        if (current != null && current > maxAyah) {
            ayahText = maxAyah.toString()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) DarkSurface else PutihBersih,
        shape = RabithohTheme.shapes.bottomSheetShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Menuju ke",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Segmented Tab Selector
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isDark) DarkSurfaceVariant else PutihAbuBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    GoToTab.entries.forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) MerahMerdeka else Color.Transparent)
                                .clickable { selectedTab = tab }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab.title,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PutihBersih else labelColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (selectedTab) {
                GoToTab.SURAH -> {
                    // Surat selection
                    Text(text = "Surat", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = labelColor)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { showSurahPicker = true })
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedSurah?.let { "${it.number}. ${it.nameLatin}" } ?: "Pilih Surat",
                                color = textColor,
                                fontSize = 15.sp
                            )
                            Text("▾", color = labelColor, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val maxAyah = selectedSurah?.numberOfAyahs ?: 1
                    Text(text = "Ayat", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = labelColor)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = ayahText,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }
                            ayahText = if (digits.isEmpty()) "" else digits.toInt().coerceIn(1, maxAyah).toString()
                        },
                        placeholder = { Text("1 - $maxAyah") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MerahMerdeka,
                            unfocusedBorderColor = borderColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                GoToTab.PAGE -> {
                    Text(text = "Nomor Halaman (1 - 604)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = labelColor)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = pageText,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }
                            pageText = if (digits.isEmpty()) "" else digits.toInt().coerceIn(1, QuranPageLookup.TOTAL_PAGES).toString()
                        },
                        placeholder = { Text("1 - 604") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MerahMerdeka,
                            unfocusedBorderColor = borderColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    val pageNum = pageText.toIntOrNull()
                    if (pageNum != null && pageNum in 1..QuranPageLookup.TOTAL_PAGES) {
                        val meta = QuranPageLookup.getPageMeta(pageNum)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ℹ️ ${meta.surahName} • ${meta.ayahRangeText} (Juz ${meta.juz})",
                            fontSize = 12.sp,
                            color = EmasKhidmat
                        )
                    }
                }

                GoToTab.JUZ -> {
                    Text(text = "Pilih Juz (1 - 30)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = labelColor)
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items((1..30).toList()) { juz ->
                            val isSelected = selectedJuz == juz
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MerahMerdeka else if (isDark) DarkSurfaceVariant else PutihAbuBackground,
                                border = BorderStroke(1.dp, if (isSelected) MerahMerdeka else borderColor),
                                modifier = Modifier
                                    .clickable { selectedJuz = juz }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$juz",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 14.sp,
                                        color = if (isSelected) PutihBersih else textColor
                                    )
                                }
                            }
                        }
                    }

                    val juzStartPage = QuranPageLookup.getPageForJuz(selectedJuz)
                    val meta = QuranPageLookup.getPageMeta(juzStartPage)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ℹ️ Awal Juz $selectedJuz: Halaman $juzStartPage (${meta.surahName})",
                        fontSize = 12.sp,
                        color = EmasKhidmat
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Batal")
                }
                Button(
                    onClick = {
                        when (selectedTab) {
                            GoToTab.SURAH -> {
                                val surah = selectedSurah ?: return@Button
                                val maxAyah = surah.numberOfAyahs
                                val ayahNumber = ayahText.toIntOrNull()?.coerceIn(1, maxAyah) ?: 1
                                if (onConfirmPage != null) {
                                    val page = QuranPageLookup.getPageForAyah(surah.number, ayahNumber)
                                    onConfirmPage(page)
                                } else {
                                    onConfirm(surah.number, ayahNumber)
                                }
                            }
                            GoToTab.PAGE -> {
                                val page = pageText.toIntOrNull()?.coerceIn(1, QuranPageLookup.TOTAL_PAGES) ?: 1
                                if (onConfirmPage != null) {
                                    onConfirmPage(page)
                                } else {
                                    val meta = QuranPageLookup.getPageMeta(page)
                                    onConfirm(meta.surahNumber, 1)
                                }
                            }
                            GoToTab.JUZ -> {
                                val page = QuranPageLookup.getPageForJuz(selectedJuz)
                                if (onConfirmPage != null) {
                                    onConfirmPage(page)
                                } else {
                                    val meta = QuranPageLookup.getPageMeta(page)
                                    onConfirm(meta.surahNumber, 1)
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pilih", color = PutihBersih, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showSurahPicker) {
        SurahPickerScreen(
            surahs = surahs,
            onDismiss = { showSurahPicker = false },
            onSelect = { surah ->
                selectedSurah = surah
                showSurahPicker = false
            }
        )
    }
}
