package com.iqbalwork.robithoh.feature.home.ui

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

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
    onOpenSheet: (String) -> Unit,
    viewModel: com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel? = null
) {
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val vm = viewModel ?: remember(database) {
        com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel(database = database)
    }
    val state by vm.uiState.collectAsState()
    val countdown = state.nextPrayerCountdown
    val schedule = state.prayerSchedule

    val menuGridItems = listOf(
        HomeGridMenuItem("dzikir", "Dzikir", "📖"),
        HomeGridMenuItem("tasbih", "Tasbih", "📿"),
        HomeGridMenuItem("khotaman", "Khotaman", "📜"),
        HomeGridMenuItem("manaqib", "Manaqib", "🏛️"),
        HomeGridMenuItem("sholat", "Sholat", "🕌"),
        HomeGridMenuItem("langgam", "Langgam", "🎵"),
        HomeGridMenuItem("tarhim", "Tarhim", "📢"),
        HomeGridMenuItem("sholawat", "Sholawat", "✨"),
        HomeGridMenuItem("wakil_talqin", "Wakil Talqin", "👥"),
        HomeGridMenuItem("doa", "Doa", "🤲"),
        HomeGridMenuItem("silsilah", "Silsilah", "🔗"),
        HomeGridMenuItem("tahlil", "Tahlil & Ziyaroh", "🌿")
    )

    val kutipanHariIni = remember { untaianMutiaraTanbih.random() }

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Header (Greeting & Notification)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Assalamu'alaikum, Sahabat",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextCharcoal
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${schedule?.dateFormatted ?: "Hari ini"} · ${schedule?.hijriDateFormatted ?: "14 Rabiul Awal 1448 H"}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Surface(
                    color = Color(0xFFFDEED2),
                    shape = CircleShape,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🔔", fontSize = 18.sp)
                    }
                }
            }
        }

        // 2. Next Prayer Hero Card (Live Calculated from adhan-kotlin)
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE8C8)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToPrayerTimes)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SHOLAT BERIKUTNYA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8C5B00),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Jadwal Lengkap ›",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MerahMerdeka
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    val prayerIcon = when (countdown?.nextPrayerName) {
                        "Subuh" -> "🌅"
                        "Dzuhur" -> "☀️"
                        "Ashar" -> "🌤️"
                        "Maghrib" -> "🌇"
                        "Isya" -> "🌙"
                        else -> "🕌"
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("$prayerIcon ", fontSize = 18.sp)
                        Text(
                            text = countdown?.nextPrayerName ?: "Subuh",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextCharcoal
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (countdown != null) {
                            "${countdown.remainingHours.toString().padStart(2, '0')}:${countdown.remainingMinutes.toString().padStart(2, '0')}:${countdown.remainingSeconds.toString().padStart(2, '0')}"
                        } else {
                            "00:00:00"
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextCharcoal,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${countdown?.nextPrayerTime ?: schedule?.subuh ?: "04:37"} · ${schedule?.locationName ?: "Panjalu, Ciamis"} (${schedule?.timezone ?: "WIB"})",
                        fontSize = 12.sp,
                        color = Color(0xFF785B28)
                    )
                }
            }
        }

        // 3. Menu Title & 12 Grid Items (4 columns)
        item {
            Text(
                text = "Menu Amaliyah",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
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
                            Box(modifier = Modifier.weight(1f)) {
                                MainGridButton(
                                    item = item,
                                    onClick = {
                                        when (item.id) {
                                            "dzikir" -> onNavigateToDocument("dzikir_tqn")
                                            "tasbih" -> onNavigateToTasbih()
                                            "khotaman" -> onNavigateToDocument("khotaman_tqn")
                                            "tarhim" -> onNavigateToDocument("tarhim_tqn")
                                            "silsilah" -> onNavigateToDocument("silsilah_tqn")
                                            "langgam" -> onNavigateToLanggam()
                                            "manaqib" -> onOpenSheet("manaqib")
                                            "sholat" -> onOpenSheet("sholat")
                                            "sholawat" -> onOpenSheet("sholawat")
                                            "tahlil" -> onOpenSheet("tahlil")
                                            "doa" -> onOpenSheet("doa")
                                            "wakil_talqin" -> onNavigateToDocument("silsilah_tqn")
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
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE5F4FD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "KUTIPAN HARI INI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFBAE6FD),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "TANBIH",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "“$kutipanHariIni”",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0F172A),
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Wasiat Pangersa Abah Sepuh · TQN Sirnarasa",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }
            }
        }

        // 6. Khidmah Maliyah Section
        item {
            Text(
                text = "Khidmah Maliyah Sirnarasa",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KhidmahLogoCard("Pesantren\nSirnarasa", "🕌", Modifier.weight(1f))
                KhidmahLogoCard("STID\nSirnarasa", "🎓", Modifier.weight(1f))
                KhidmahLogoCard("Baitul Maal\nSirnarasa", "🏛️", Modifier.weight(1f))
                KhidmahLogoCard("Baitul\nAsror", "✨", Modifier.weight(1f))
            }
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
private fun MainGridButton(
    item: HomeGridMenuItem,
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
            color = TextCharcoal,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            maxLines = 2
        )
    }
}

@Composable
private fun KhidmahLogoCard(
    title: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp)
        ) {
            Surface(
                color = PaperBackgroundLight,
                shape = CircleShape,
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(emoji, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = TextCharcoal,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
    }
}
