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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

@Composable
fun ProfileTabContent(
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {}
) {
    SettingsTabContent(
        isDarkMode = isDarkMode,
        onDarkModeChange = onDarkModeChange
    )
}

@Composable
fun SettingsTabContent(
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    val isDark = RabithohTheme.colors.isDark || isDarkMode

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkCanvas else PaperBackgroundLight),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header Title
        item {
            Column {
                Text(
                    text = "Pengaturan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) PutihBersih else TextCharcoal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Preferensi tampilan & informasi aplikasi",
                    fontSize = 13.sp,
                    color = if (isDark) DarkMuted else TextMuted
                )
            }
        }

        // 2. Section TAMPILAN (Mode Gelap)
        item {
            Text(
                text = "TAMPILAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkMuted else TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = if (isDark) Color(0xFF3B2D54) else Color(0xFFF3E5F5),
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🌙", fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Mode Gelap",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) PutihBersih else TextCharcoal
                            )
                            Text(
                                text = if (isDarkMode) "Gelap (Dark theme aktif)" else "Terang (Light theme aktif)",
                                fontSize = 11.sp,
                                color = if (isDark) DarkMuted else TextMuted
                            )
                        }
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onDarkModeChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MerahMerdeka,
                            uncheckedThumbColor = Color(0xFF9CA3AF),
                            uncheckedTrackColor = Color(0xFFE5E7EB),
                            uncheckedBorderColor = Color(0xFFCBD5E1)
                        )
                    )
                }
            }
        }

        // 3. Section BANTUAN & MASUKAN (Masukan dan Saran)
        item {
            Text(
                text = "BANTUAN & MASUKAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkMuted else TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsActionRow(
                    icon = "💬",
                    iconBg = if (isDark) Color(0xFF1B382B) else Color(0xFFE8F5E9),
                    title = "Masukan & Saran",
                    subtitle = "Hubungi via WhatsApp (0878-2288-2668)",
                    isDark = isDark,
                    onClick = {
                        try {
                            uriHandler.openUri("https://wa.me/6287822882668?text=Assalamu%27alaikum%20saya%20ingin%20memberikan%20masukan%20dan%20saran%20untuk%20aplikasi%20Robithoh%3A%0A")
                        } catch (_: Exception) {
                        }
                    }
                )
            }
        }

        // 4. Section TENTANG APLIKASI
        item {
            Text(
                text = "TENTANG APLIKASI",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkMuted else TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = if (isDark) Color(0xFF4A151D) else Color(0xFFFFEBEE),
                            shape = CircleShape,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("📖", fontSize = 20.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Robithoh",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else MerahMarunGelap
                            )
                            Text(
                                text = "Versi 1.0.0 · 100% Offline-First",
                                fontSize = 11.sp,
                                color = if (isDark) DarkMuted else TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aplikasi Spiritual & Panduan Amaliyah Harian Ikhwan/Akhwat TQN (Thoriqoh Qodiriyyah Naqsyabandiyyah) Suryalaya - Sirnarasa Ciceuri Panjalu Ciamis.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = if (isDark) PutihBersih.copy(alpha = 0.85f) else TextCharcoal
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDark) Color(0xFF2A2424) else Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = "✨ 100% Offline",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) PutihBersih else TextCharcoal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDark) Color(0xFF2A2424) else Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = "🕌 Sirnarasa",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) PutihBersih else TextCharcoal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsActionRow(
    icon: String,
    iconBg: Color,
    title: String,
    subtitle: String,
    isDark: Boolean,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = iconBg,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) PutihBersih else TextCharcoal
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (isDark) DarkMuted else TextMuted
                )
            }
        }
        Text("›", fontSize = 20.sp, color = if (isDark) DarkMuted else TextMuted)
    }
}
