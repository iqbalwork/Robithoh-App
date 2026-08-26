package com.iqbalwork.robithoh.feature.profile.ui

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

@Composable
fun ProfileTabContent(
    onNavigateToLanggam: () -> Unit,
    onNavigateToProfilePesantren: () -> Unit
) {
    var darkModeEnabled by remember { mutableStateOf(false) }

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
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Title
        item {
            Text(
                text = "Profil & Pengaturan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
        }

        // 2. User Hero Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFFFD8DE),
                            shape = CircleShape,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "R",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MerahMerdeka
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Ikhwan TQN Sirnarasa",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextCharcoal
                            )
                            Text(
                                text = "Rabithah Ruhaniyah 100% Offline",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Level
                        Surface(
                            color = Color(0xFFEDE7F6),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("TQN 38", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5E35B1))
                                Text("Silsilah Emas", fontSize = 10.sp, color = TextMuted)
                            }
                        }

                        // Streak
                        Surface(
                            color = Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("7 Hari", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                                Text("Istiqomah", fontSize = 10.sp, color = TextMuted)
                            }
                        }

                        // XP
                        Surface(
                            color = Color(0xFFE0F7FA),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("165x", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00838F))
                                Text("Dzikir Jahr", fontSize = 10.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }

        // 3. Langgam Audio Player Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F1)),
                border = androidx.compose.foundation.BorderStroke(1.dp, MerahMerdeka.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToLanggam() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = MerahMerdeka,
                            shape = CircleShape,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🎵", fontSize = 20.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Langgam TQN Audio Player",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MerahMarunGelap
                            )
                            Text(
                                text = "11 rekaman langgam sholat & dzikir Abah Aos",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Text("▶ Buka", color = MerahMerdeka, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // 4. Group TAMPILAN
        item {
            Text(
                text = "TAMPILAN",
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFFF3E5F5),
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🌙", fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Mode gelap", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextCharcoal)
                                Text(if (darkModeEnabled) "Gelap" else "Terang", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                        Switch(
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = MerahMerdeka)
                        )
                    }

                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))

                    ProfileNavRow(icon = "🌐", iconBg = Color(0xFFE1F5FE), title = "Bahasa", subtitle = "Indonesia & Sunda")
                }
            }
        }

        // 5. Group EKOSISTEM SIRNARASA
        item {
            Text(
                text = "EKOSISTEM SIRNARASA",
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
                    ProfileNavRow(
                        icon = "🕌",
                        iconBg = Color(0xFFE8F5E9),
                        title = "Pondok Pesantren Sirnarasa",
                        subtitle = "Ciceuri, Panjalu, Ciamis, Jawa Barat",
                        onClick = onNavigateToProfilePesantren
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileNavRow(
                        icon = "🎓",
                        iconBg = Color(0xFFFFF8E1),
                        title = "STID Sirnarasa",
                        subtitle = "Sekolah Tinggi Ilmu Dakwah Sirnarasa",
                        onClick = onNavigateToProfilePesantren
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileNavRow(
                        icon = "🏛️",
                        iconBg = Color(0xFFE0F2F1),
                        title = "Baitul Maal Sirnarasa (BMS)",
                        subtitle = "Lembaga Zakat, Infaq & Shodaqoh",
                        onClick = onNavigateToProfilePesantren
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileNavRow(
                        icon = "✨",
                        iconBg = Color(0xFFFBE9E7),
                        title = "Masjid Agung Baitul Asror",
                        subtitle = "Pusat Dzikir & Manaqib Syaikh Abdul Qodir Al-Jailani",
                        onClick = onNavigateToProfilePesantren
                    )
                }
            }
        }

        // 6. Group TENTANG
        item {
            Text(
                text = "TENTANG",
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
                    ProfileNavRow(
                        icon = "📖",
                        iconBg = Color(0xFFE3F2FD),
                        title = "Tentang Aplikasi Robithoh",
                        subtitle = "Versi 2.0 · 100% Offline-First"
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
private fun ProfileNavRow(
    icon: String,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() }),
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
