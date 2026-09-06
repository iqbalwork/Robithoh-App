package com.iqbalwork.robithoh.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import androidx.compose.foundation.BorderStroke
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel

data class HomeGridMenuItem(
    val id: String,
    val title: String,
    val emoji: String,
    val badge: String? = null
)

// "Untaian Mutiara" — pearls of wisdom from the Tanbih of Pangersa Guru Almarhum (Abah Sepuh)
private val untaianMutiaraTanbih = listOf(
    "Jangan benci kepada ulama yang sezaman.",
    "Jangan menyalahkan ajaran orang lain.",
    "Jangan meneliti murid orang lain.",
    "Jangan berubah sikap meskipun disakiti orang lain.",
    "Mesti menyayangi orang yang membencimu."
)

@Composable
fun HomeTabContent(
    onNavigateToDocument: (String) -> Unit,
    onNavigateToLanggam: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToPrayerTimes: () -> Unit = {},
    onNavigateToQibla: () -> Unit = {},
    onOpenSheet: (String) -> Unit,
    viewModel: AmaliyahViewModel? = null
) {
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val vm = viewModel ?: remember(database) {
        AmaliyahViewModel(database = database)
    }
    val state by vm.uiState.collectAsState()
    val countdown = state.nextPrayerCountdown
    val schedule = state.prayerSchedule

    val isDark = RabithohTheme.colors.isDark

    val menuGridItems = listOf(
        HomeGridMenuItem("dzikir", "Dzikir", "📖"),
        HomeGridMenuItem("tasbih", "Tasbih Digital", "📿"),
        HomeGridMenuItem("khotaman", "Khotaman", "📜"),
        HomeGridMenuItem("manaqib", "Manaqib", "🏛️"),
        HomeGridMenuItem("sholat", "Sholat", "🕌"),
        HomeGridMenuItem("kiblat", "Arah Kiblat", "🧭"),
        HomeGridMenuItem("langgam", "Langgam", "🎵"),
        HomeGridMenuItem("tarhim", "Tarhim", "📢"),
        HomeGridMenuItem("sholawat", "Sholawat", "✨"),
        HomeGridMenuItem("doa", "Doa", "🤲"),
        HomeGridMenuItem("silsilah", "Silsilah", "🔗"),
        HomeGridMenuItem("tahlil", "Tahlil & Ziyaroh", "🌿")
    )

    val kutipanHariIni = remember { untaianMutiaraTanbih.random() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkCanvas else PaperBackgroundLight),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Top Header (Greeting)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Assalamu'alaikum",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else TextCharcoal
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${schedule?.dateFormatted ?: "Hari ini"} · ${schedule?.hijriDateFormatted ?: "14 Rabiul Awal 1448 H"}",
                        fontSize = 11.5.sp,
                        color = if (isDark) DarkMuted else TextMuted,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }

        // 2. Next Prayer Hero Card (Live Calculated from adhan-kotlin)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color(0xFFFEE8C8)),
                border = if (isDark) BorderStroke(1.dp, DarkBorder) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToPrayerTimes)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SHOLAT BERIKUTNYA",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) EmasMuda else Color(0xFF8C5B00),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Jadwal ›",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MerahMerdeka
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    val prayerIcon = when (countdown?.nextPrayerName) {
                        "Subuh" -> "🌅"
                        "Dzuhur" -> "☀️"
                        "Ashar" -> "🌤️"
                        "Maghrib" -> "🌇"
                        "Isya" -> "🌙"
                        else -> "🕌"
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("$prayerIcon ", fontSize = 16.sp)
                        Text(
                            text = countdown?.nextPrayerName ?: "Subuh",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else TextCharcoal
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (countdown != null) {
                            "${countdown.remainingHours.toString().padStart(2, '0')}:${countdown.remainingMinutes.toString().padStart(2, '0')}:${countdown.remainingSeconds.toString().padStart(2, '0')}"
                        } else {
                            "00:00:00"
                        },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) PutihBersih else TextCharcoal,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${countdown?.nextPrayerTime ?: schedule?.subuh ?: "04:37"} · ${if (state.isFetchingLocation) "Mencari lokasi..." else (schedule?.locationName ?: "Panjalu, Ciamis")} (${schedule?.timezone ?: "WIB"})",
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        color = if (isDark) DarkMuted else Color(0xFF785B28)
                    )
                }
            }
        }

        // 3. Menu Title & 12 Grid Items (4 columns)
        item {
            Text(
                text = "Menu Amaliyah",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) PutihBersih else TextCharcoal
            )
        }

        // Grid Rows
        for (i in menuGridItems.indices step 4) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (j in 0..3) {
                        val index = i + j
                        if (index < menuGridItems.size) {
                            val item = menuGridItems[index]
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                MainGridButton(
                                    item = item,
                                    isDark = isDark,
                                    onClick = {
                                        when (item.id) {
                                            "dzikir" -> onNavigateToDocument("dzikir_tqn")
                                            "tasbih" -> onNavigateToTasbih()
                                            "khotaman" -> onNavigateToDocument("khotaman_tqn")
                                            "tarhim" -> onNavigateToDocument("tarhim_tqn")
                                            "silsilah" -> onNavigateToDocument("silsilah_tqn")
                                            "kiblat" -> onNavigateToQibla()
                                            "langgam" -> onNavigateToLanggam()
                                            "manaqib" -> onOpenSheet("manaqib")
                                            "sholat" -> onOpenSheet("sholat")
                                            "sholawat" -> onOpenSheet("sholawat")
                                            "tahlil" -> onOpenSheet("tahlil")
                                            "doa" -> onOpenSheet("doa")
                                            else -> onNavigateToDocument("dzikir_tqn")
                                        }
                                    }
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // 5. Kutipan Hari Ini / Tanbih
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color(0xFFE5F4FD)),
                border = if (isDark) BorderStroke(1.dp, DarkBorder) else null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "KUTIPAN HARI INI",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) EmasMuda else Color(0xFF0284C7),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (isDark) DarkSurfaceVariant else Color(0xFFBAE6FD),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "TANBIH",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) EmasMuda else Color(0xFF0369A1),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "“$kutipanHariIni”",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) PutihBersih else Color(0xFF0F172A),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Wasiat Pangersa Abah Sepuh · MTQN Suryalaya Sirnarasa PPKN III",
                        fontSize = 10.5.sp,
                        color = if (isDark) DarkMuted else Color(0xFF475569)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MainGridButton(
    item: HomeGridMenuItem,
    isDark: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(MerahMerdeka, MerahMarunGelap)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.emoji,
                fontSize = 28.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDark) PutihBersih else TextCharcoal,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            maxLines = 1,
            softWrap = false,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

