package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.amaliyah.model.*
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiState
import com.iqbalwork.robithoh.navigation.ScreenKey

@Composable
fun AmaliyahScreen(
    state: AmaliyahUiState,
    onIntent: (AmaliyahUiIntent) -> Unit,
    onNavigate: (ScreenKey) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    var isShowingPrayerTimesDetail by remember { mutableStateOf(false) }
    var isShowingDzikirDetail by remember { mutableStateOf(false) }

    if (isShowingPrayerTimesDetail) {
        PrayerTimesScreen(
            state = state,
            onIntent = onIntent,
            onBack = { isShowingPrayerTimesDetail = false }
        )
        return
    }

    if (isShowingDzikirDetail) {
        DzikirDetailScreen(
            state = state,
            onIntent = onIntent,
            onOpenTasbih = { target, title ->
                onNavigate(ScreenKey.Tasbih)
            },
            onBack = { isShowingDzikirDetail = false }
        )
        return
    }

    Scaffold(
        topBar = {
            IslamicHeader(
                title = "Amaliyah & Dzikir",
                subtitle = "Panduan Ibadah Harian & Bulanan TQN 38",
                arabicTitle = "الْأَعْمَالُ وَالْأَوْرَادُ",
                onBackClick = onBack
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // 3-Language Toggle Switch
            item {
                LanguageTabSwitch(
                    selectedLanguage = state.selectedLanguage,
                    onLanguageSelected = { onIntent(AmaliyahUiIntent.SelectLanguage(it)) }
                )
            }

            // Quick Prayer Times Preview Hero Banner
            item {
                val countdown = state.nextPrayerCountdown
                val schedule = state.prayerSchedule
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                    onClick = { isShowingPrayerTimesDetail = true }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "WAKTU SHOLAT & TASAWUF",
                                color = EmasMuda,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            if (countdown != null && schedule != null) {
                                Text(
                                    text = "Menuju ${countdown.nextPrayerName} (${countdown.nextPrayerTime})",
                                    color = PutihBersih,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Sisa: ${countdown.remainingHours}j ${countdown.remainingMinutes}m ${countdown.remainingSeconds}d • ${schedule.locationName}",
                                    color = PutihBersih.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Surface(
                            color = PutihBersih.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Jadwal ›",
                                color = PutihBersih,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Quick Tasbih Digital Action Card
            item {
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.GOLD_BORDER,
                    onClick = { onNavigate(ScreenKey.Tasbih) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MerahMerdeka.copy(alpha = 0.12f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📿", fontSize = 20.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Tasbih Digital Haptik",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (isDark) PutihBersih else SlateCharcoalText
                                )
                                Text(
                                    text = "Preset 33x, 100x, 165x (Dzikir Jahr TQN)",
                                    fontSize = 12.sp,
                                    color = if (isDark) DarkMuted else SlateMuted
                                )
                            }
                        }

                        Surface(
                            color = MerahMerdeka,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Buka",
                                color = PutihBersih,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Amaliyah Category Tabs
            item {
                Text(
                    text = "Kategori Amaliyah",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(AmaliyahCategory.entries) { cat ->
                        val isSelected = cat == state.selectedCategory
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MerahMerdeka else (if (isDark) DarkSurfaceVariant else Color(0xFFE9ECEF)),
                            border = if (isSelected) BorderStroke(1.dp, EmasKhidmat) else null,
                            modifier = Modifier.clickable {
                                onIntent(AmaliyahUiIntent.SelectCategory(cat))
                            }
                        ) {
                            Text(
                                text = cat.label,
                                color = if (isSelected) PutihBersih else (if (isDark) Color.LightGray else SlateCharcoalText),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }

            // Content Based on Selected Category
            when (state.selectedCategory) {
                AmaliyahCategory.DZIKIR_BA_DA_SHOLAT -> {
                    item {
                        GoldCrimsonCard(
                            variant = GoldCrimsonCardVariant.GOLD_TINTED,
                            onClick = { isShowingDzikirDetail = true }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Dzikir Ba'da Sholat Maktubah",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MerahMerdeka
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Dzikir Jahr (Nafi Itsbat 165x) & Dzikir Khofi (Ismu Dzat) lengkap dengan Hadhloroh Silsilah 38.",
                                        fontSize = 12.sp,
                                        color = if (isDark) DarkMuted else SlateMuted
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { isShowingDzikirDetail = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("Baca Lengkap", fontSize = 12.sp, color = PutihBersih)
                                }
                            }
                        }
                    }

                    // Preview of Dzikir Items
                    items(state.dzikirJahrList.take(4), key = { it.id }) { item ->
                        DzikirPreviewCard(
                            item = item,
                            selectedLanguage = state.selectedLanguage,
                            isDark = isDark,
                            isExpanded = state.expandedItemId == item.id,
                            onToggleExpand = { onIntent(AmaliyahUiIntent.ToggleExpandItem(item.id)) }
                        )
                    }
                }

                AmaliyahCategory.PRAYER_TIMES -> {
                    item {
                        PrayerTimesScreenInline(state = state, onOpenFull = { isShowingPrayerTimesDetail = true })
                    }
                }

                AmaliyahCategory.DOA_HARIAN -> {
                    items(state.dailyPrayersList, key = { it.id }) { prayer ->
                        SpecialPrayerCard(
                            prayer = prayer,
                            selectedLanguage = state.selectedLanguage,
                            isDark = isDark,
                            isExpanded = state.expandedItemId == prayer.id,
                            onToggleExpand = { onIntent(AmaliyahUiIntent.ToggleExpandItem(prayer.id)) }
                        )
                    }
                }

                AmaliyahCategory.BULAN_HIJRIYAH -> {
                    items(state.hijriyahList, key = { it.monthNumber.toString() }) { month ->
                        HijriyahMonthCard(
                            month = month,
                            isDark = isDark,
                            isExpanded = state.expandedItemId == "month_${month.monthNumber}",
                            onToggleExpand = { onIntent(AmaliyahUiIntent.ToggleExpandItem("month_${month.monthNumber}")) }
                        )
                    }
                }

                AmaliyahCategory.SHOLAT_SUNNAH -> {
                    items(state.sholatSunnahList, key = { it.id }) { sholat ->
                        SpecialPrayerCard(
                            prayer = sholat,
                            selectedLanguage = state.selectedLanguage,
                            isDark = isDark,
                            isExpanded = state.expandedItemId == sholat.id,
                            onToggleExpand = { onIntent(AmaliyahUiIntent.ToggleExpandItem(sholat.id)) }
                        )
                    }
                }
            }

            // Bottom Ornamental Motif
            item {
                IslamicDivider(
                    motif = IslamicDividerMotif.RUB_EL_HIZB,
                    color = EmasKhidmat,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun DzikirPreviewCard(
    item: DzikirItem,
    selectedLanguage: LiturgyLanguage,
    isDark: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    GoldCrimsonCard(
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        onClick = onToggleExpand
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MerahMerdeka,
                    modifier = Modifier.size(22.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("${item.number}", color = PutihBersih, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                Text(
                    text = "${item.repetitionCount}x",
                    color = MerahMerdeka,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Arabic text
        Text(
            text = item.arabicText,
            style = RabithohTheme.typography.arabicMedium.copy(
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                color = if (isDark) PutihBersih else Color(0xFF1E2022)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = item.latinText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = MerahMerdeka,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                val translation = when (selectedLanguage) {
                    LiturgyLanguage.ARABIC -> item.indonesianText
                    LiturgyLanguage.INDONESIAN -> item.indonesianText
                    LiturgyLanguage.SUNDANESE -> item.sundaneseText
                }
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 12.sp
                    )
                )
                if (item.kaifiyatNote.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kaifiyat: ${item.kaifiyatNote}",
                        fontSize = 11.sp,
                        color = EmasKhidmat
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecialPrayerCard(
    prayer: SpecialPrayer,
    selectedLanguage: LiturgyLanguage,
    isDark: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    GoldCrimsonCard(
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        onClick = onToggleExpand
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = prayer.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isDark) PutihBersih else SlateCharcoalText
                )
                if (prayer.recommendedTime.isNotBlank()) {
                    Text(
                        text = "Waktu: ${prayer.recommendedTime}",
                        fontSize = 11.sp,
                        color = EmasKhidmat
                    )
                }
            }
            Text(
                text = if (isExpanded) "▲" else "▼",
                color = MerahMerdeka,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = prayer.arabicText,
            style = RabithohTheme.typography.arabicMedium.copy(
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                color = if (isDark) PutihBersih else Color(0xFF1E2022)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = prayer.latinText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = MerahMerdeka,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                val translation = when (selectedLanguage) {
                    LiturgyLanguage.ARABIC -> prayer.indonesianText
                    LiturgyLanguage.INDONESIAN -> prayer.indonesianText
                    LiturgyLanguage.SUNDANESE -> prayer.sundaneseText
                }
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 12.sp
                    )
                )

                if (prayer.kaifiyat.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = if (isDark) DarkSurfaceVariant else Color(0xFFF7F7F8),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Tata Cara: ${prayer.kaifiyat}",
                            fontSize = 11.sp,
                            color = if (isDark) PutihBersih else SlateCharcoalText,
                            modifier = Modifier.padding(8.dp),
                            lineHeight = 15.sp
                        )
                    }
                }

                if (prayer.virtue.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Keutamaan: ${prayer.virtue}",
                        fontSize = 11.sp,
                        color = EmasKhidmat
                    )
                }
            }
        }
    }
}

@Composable
private fun HijriyahMonthCard(
    month: HijriyahAmaliyah,
    isDark: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    GoldCrimsonCard(
        variant = GoldCrimsonCardVariant.GOLD_BORDER,
        onClick = onToggleExpand
    ) {
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
                        Text("${month.monthNumber}", color = PutihBersih, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "${month.monthNumber}. ${month.monthName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isDark) PutihBersih else SlateCharcoalText
                    )
                    Text(
                        text = month.arabicName,
                        style = RabithohTheme.typography.arabicMedium.copy(
                            color = EmasKhidmat,
                            fontSize = 13.sp
                        )
                    )
                }
            }
            Text(
                text = if (isExpanded) "▲" else "▼",
                color = MerahMerdeka,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = month.virtues,
            fontSize = 12.sp,
            color = if (isDark) DarkMuted else SlateMuted,
            lineHeight = 17.sp
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Amalan yang Dianjurkan:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MerahMerdeka
                )
                Spacer(modifier = Modifier.height(4.dp))
                month.recommendedAmalan.forEach { amalan ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text("• ", color = EmasKhidmat, fontWeight = FontWeight.Bold)
                        Text(
                            text = amalan,
                            fontSize = 12.sp,
                            color = if (isDark) PutihBersih else SlateCharcoalText,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrayerTimesScreenInline(
    state: AmaliyahUiState,
    onOpenFull: () -> Unit
) {
    val schedule = state.prayerSchedule
    if (schedule != null) {
        GoldCrimsonCard(variant = GoldCrimsonCardVariant.GOLD_BORDER) {
            Text(
                text = "Jadwal Sholat Hari Ini (${schedule.locationName})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Subuh: ${schedule.subuh}", fontSize = 12.sp)
                    Text("Dzuhur: ${schedule.dzuhur}", fontSize = 12.sp)
                    Text("Ashar: ${schedule.ashar}", fontSize = 12.sp)
                }
                Column {
                    Text("Maghrib: ${schedule.maghrib}", fontSize = 12.sp)
                    Text("Isya: ${schedule.isya}", fontSize = 12.sp)
                    Text("Tahajjud: ${schedule.tahajjud}", fontSize = 12.sp, color = MerahMerdeka)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onOpenFull,
                colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buka Jadwal Lengkap & Kompas Kiblat", color = PutihBersih, fontSize = 12.sp)
            }
        }
    }
}
