package com.iqbalwork.robithoh.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.appVersionName
import com.iqbalwork.robithoh.getPlatform
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted

@Composable
fun ProfileTabContent(
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {},
    onNavigateToAboutApp: () -> Unit = {},
    onCheckForUpdates: () -> Unit = {},
    onOpenPlayStore: () -> Unit = {}
) {
    SettingsTabContent(
        isDarkMode = isDarkMode,
        onDarkModeChange = onDarkModeChange,
        onNavigateToAboutApp = onNavigateToAboutApp,
        onCheckForUpdates = onCheckForUpdates,
        onOpenPlayStore = onOpenPlayStore
    )
}

@Composable
fun SettingsTabContent(
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {},
    onNavigateToAboutApp: () -> Unit = {},
    onCheckForUpdates: () -> Unit = {},
    onOpenPlayStore: () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    val isDark = RabithohTheme.colors.isDark || isDarkMode
    val isAndroid = getPlatform().name.startsWith("Android")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkCanvas else PaperBackgroundLight),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
            bottom = 120.dp
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
                onClick = { onDarkModeChange(!isDarkMode) },
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

        // 3. Section BANTUAN & MASUKAN
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
                    subtitle = "Hubungi via WhatsApp (+62 878-2288-2668)",
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

        // 4. Section INFORMASI & TENTANG APLIKASI
        item {
            Text(
                text = "INFORMASI",
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
                Column {
                    SettingsActionRow(
                        icon = "📖",
                        iconBg = if (isDark) Color(0xFF4A151D) else Color(0xFFFFEBEE),
                        title = "Tentang Aplikasi",
                        subtitle = "Robithoh v1.0.0 · Roudloh Merah Putih MTQN Suryalaya Sirnarasa PPKN III",
                        isDark = isDark,
                        onClick = onNavigateToAboutApp
                    )

                    HorizontalDivider(
                        color = if (isDark) Color(0xFF2E2727) else Color(0xFFF1F5F9),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    SettingsActionRow(
                        icon = "🌐",
                        iconBg = if (isDark) Color(0xFF382C1B) else Color(0xFFFFF8E1),
                        title = "Website Resmi Robithoh",
                        subtitle = "Portal web & informasi rilis multiplatform",
                        isDark = isDark,
                        onClick = {
                            try {
                                uriHandler.openUri("https://iqbalwork.github.io/Robithoh-Landing/")
                            } catch (_: Exception) {
                            }
                        }
                    )

                    if (isAndroid) {
                        HorizontalDivider(
                            color = if (isDark) Color(0xFF2E2727) else Color(0xFFF1F5F9),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        SettingsActionRow(
                            icon = "🔄",
                            iconBg = if (isDark) Color(0xFF1E3A5F) else Color(0xFFD0EDFF),
                            title = "Periksa Pembaruan",
                            subtitle = "Cek versi terbaru dari Google Play",
                            isDark = isDark,
                            onClick = onCheckForUpdates
                        )
                    }

                    HorizontalDivider(
                        color = if (isDark) Color(0xFF2E2727) else Color(0xFFF1F5F9),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = if (isDark) Color(0xFF2A2424) else Color(0xFFF5EFE6),
                                shape = CircleShape,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📱", fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Versi Aplikasi",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) PutihBersih else TextCharcoal
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isDark) Color(0xFF382C1B) else Color(0xFFFFF8E1)
                        ) {
                            Text(
                                text = appVersionName(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmasKhidmat,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    if (isAndroid) {
                        HorizontalDivider(
                            color = if (isDark) Color(0xFF2E2727) else Color(0xFFF1F5F9),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        SettingsActionRow(
                            icon = "⭐",
                            iconBg = if (isDark) Color(0xFF5C4A1E) else Color(0xFFFFF3CD),
                            title = "Beri Rating",
                            subtitle = "Dukung Robithoh dengan ulasan di Play Store",
                            isDark = isDark,
                            onClick = onOpenPlayStore
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
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
