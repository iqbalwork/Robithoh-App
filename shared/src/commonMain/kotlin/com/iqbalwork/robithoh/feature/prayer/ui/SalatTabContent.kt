package com.iqbalwork.robithoh.feature.prayer.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

data class PrayerScheduleItem(
    val name: String,
    val time: String,
    val isMandatory: Boolean,
    val isCurrent: Boolean = false,
    val isPast: Boolean = false,
    val initialDone: Boolean = false
)

@Composable
fun SalatTabContent(
    onNavigateToDocument: (String) -> Unit
) {
    var pinnedScheduleSwitch by remember { mutableStateOf(true) }

    val prayerList = listOf(
        PrayerScheduleItem("Imsak", "04:28", isMandatory = false, isPast = true),
        PrayerScheduleItem("Subuh", "04:38", isMandatory = true, isPast = true, initialDone = true),
        PrayerScheduleItem("Syuruq", "05:52", isMandatory = false, isPast = true),
        PrayerScheduleItem("Dzuhur", "11:54", isMandatory = true, isPast = true, initialDone = true),
        PrayerScheduleItem("Ashar", "15:14", isMandatory = true, isPast = true, initialDone = true),
        PrayerScheduleItem("Maghrib", "17:52", isMandatory = true, isPast = true, initialDone = true),
        PrayerScheduleItem("Isya", "19:02", isMandatory = true, isCurrent = true, initialDone = false)
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
                Column {
                    Text(
                        text = "Sholat & Waktu",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextCharcoal
                    )
                    Text(
                        text = "Jadwal sholat wajib & sunnah TQN",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Surface(
                    color = Color(0xFFDDF5E6),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("📍", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Panjalu / Ciamis",
                            color = Color(0xFF1E824C),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // 2. Date Navigation Bar
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Text("‹", fontSize = 24.sp, color = TextMuted)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Senin, 24 Agustus 2026",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextCharcoal
                        )
                        Text(
                            text = "11 Rabiul Awal 1448 H · Hari ini",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    IconButton(onClick = {}) {
                        Text("›", fontSize = 24.sp, color = TextMuted)
                    }
                }
            }
        }

        // 3. Section Header
        item {
            Text(
                text = "JADWAL SHOLAT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        // 4. Prayer Items
        items(prayerList.size) { i ->
            val p = prayerList[i]

            if (p.isCurrent) {
                // Highlighted Card for Current Prayer
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE8C4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEA580C))
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = p.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TextCharcoal
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = p.time,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = TextCharcoal
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Surface(
                                    color = Color(0xFFFCE1B6),
                                    shape = CircleShape,
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("🔔", fontSize = 14.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { 0.45f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MerahMerdeka,
                            trackColor = Color(0xFFE2C99D)
                        )
                    }
                }
            } else {
                // Regular Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (p.isMandatory) Color.White else Color(0xFFF6F3EE)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (p.isMandatory) 1.dp else 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = p.name,
                            fontWeight = if (p.isMandatory) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 15.sp,
                            color = if (p.isMandatory) TextCharcoal else TextMuted
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = p.time,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (p.isMandatory) TextCharcoal else TextMuted
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Surface(
                                color = if (p.isMandatory) Color(0xFFE2F3E7) else Color.Transparent,
                                shape = CircleShape,
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(if (p.name == "Subuh") "🔊" else "🔔", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Pengaturan Sholat Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "PENGATURAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingItemRow(
                        icon = "🔔",
                        iconBg = Color(0xFFFFECEF),
                        title = "Suara adzan",
                        subtitle = "Misyari Rasyid (bawaan)"
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📍", fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Jadwal tersemat", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextCharcoal)
                                Text("Sholat berikutnya di notifikasi", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                        Switch(
                            checked = pinnedScheduleSwitch,
                            onCheckedChange = { pinnedScheduleSwitch = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = MerahMerdeka)
                        )
                    }
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    SettingItemRow(
                        icon = "⏰",
                        iconBg = Color(0xFFE3F2FD),
                        title = "Metode perhitungan",
                        subtitle = "Kemenag RI"
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    SettingItemRow(
                        icon = "⏱️",
                        iconBg = Color(0xFFE0F7FA),
                        title = "Koreksi waktu sholat",
                        subtitle = "+2 menit (Ihtiyath)"
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
private fun SettingItemRow(
    icon: String,
    iconBg: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = iconBg,
                shape = CircleShape,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextCharcoal)
                Text(subtitle, fontSize = 11.sp, color = TextMuted)
            }
        }
        Text("›", fontSize = 18.sp, color = TextMuted)
    }
}
