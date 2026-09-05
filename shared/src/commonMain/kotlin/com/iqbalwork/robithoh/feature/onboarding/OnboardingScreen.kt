package com.iqbalwork.robithoh.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.core.location.rememberLocationPermissionLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import robithohapp.shared.generated.resources.Res
import robithohapp.shared.generated.resources.ic_app_launcher

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 5 })

    // Permission launcher for Location & Post-Notifications (Android & iOS)
    val requestPermission = rememberLocationPermissionLauncher { _ ->
        // Proceed to main screen regardless of permission grant/deny
        onComplete()
    }

    val backgroundColor = if (isDark) DarkCanvas else PureWhite

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar: Skip Button (aligned to right, badge removed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Skip Button (Visible on slides 0, 1, 2, 3)
                AnimatedVisibility(
                    visible = pagerState.currentPage < 4,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    TextButton(
                        onClick = onComplete,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Lewati",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) DarkMuted else TextMuted
                        )
                    }
                }
            }

            // Pager Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> OnboardingSlideWelcome(isDark = isDark)
                    1 -> OnboardingSlideOfflineAmaliyah(isDark = isDark)
                    2 -> OnboardingSlideReadingComfort(isDark = isDark)
                    3 -> OnboardingSlideWidget(isDark = isDark)
                    4 -> OnboardingSlidePermissions(
                        isDark = isDark,
                        onGrantClicked = {
                            requestPermission()
                        },
                        onSkipPermissionClicked = onComplete
                    )
                }
            }

            // Bottom Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dots Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateDpAsState(targetValue = if (isSelected) 24.dp else 8.dp)
                        val color = if (isSelected) {
                            MerahMerdeka
                        } else {
                            if (isDark) Color.White.copy(alpha = 0.2f) else TextMuted.copy(alpha = 0.25f)
                        }

                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(RoundedCornerShape(4.dp))
                                .background(color)
                        )
                    }
                }

                // Next Button (Slide 0-3)
                if (pagerState.currentPage < 4) {
                    Button(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MerahMerdeka,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(100.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Lanjut",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "→", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingSlideWelcome(isDark: Boolean) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Launcher Icon with Clean Container
        Box(
            modifier = Modifier
                .size(130.dp)
                .shadow(elevation = 8.dp, shape = CircleShape, spotColor = EmasKhidmat.copy(alpha = 0.4f))
                .clip(CircleShape)
                .background(if (isDark) DarkSurface else PureWhite)
                .border(2.dp, EmasKhidmat.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_app_launcher),
                contentDescription = "Robithoh Logo",
                modifier = Modifier.size(90.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "رابطة • Robithoh",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = EmasKhidmat,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Selamat Datang di Robithoh",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else TextCharcoal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Sahabat setia amaliyah ibadah harian, dzikir, dan khidmah spiritual Anda. Menjaga pertalian batin dan keistiqomahan dengan tenang dan khidmat.",
            fontSize = 14.sp,
            color = if (isDark) DarkMuted else TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun OnboardingSlideOfflineAmaliyah(isDark: Boolean) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Feature Preview Card
        GoldCrimsonCard(
            modifier = Modifier.fillMaxWidth(),
            variant = GoldCrimsonCardVariant.GOLD_BORDER,
            contentPadding = PaddingValues(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MerahMerdeka.copy(alpha = 0.12f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("📿", fontSize = 22.sp)
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Dzikir & Khotaman TQN",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else TextCharcoal
                    )
                    Text(
                        text = "Dzikir Ba'da Sholat, Khotaman, Manaqib & Doa Lengkap",
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            shape = RoundedCornerShape(100.dp),
            color = Color(0xFF1B5E20).copy(alpha = if (isDark) 0.3f else 0.1f),
            border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.3f))
        ) {
            Text(
                text = "⚡ 100% OFFLINE-FIRST",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color(0xFF81C784) else Color(0xFF1B5E20),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Amaliyah Lengkap Kapan Saja",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else TextCharcoal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Seluruh naskah amaliyah dan dzikir tersimpan aman di perangkat. Anda dapat mengamalkannya di mana pun tanpa bergantung pada jaringan internet.",
            fontSize = 14.sp,
            color = if (isDark) DarkMuted else TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun OnboardingSlideReadingComfort(isDark: Boolean) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Reading Customization Mockup
        GoldCrimsonCard(
            modifier = Modifier.fillMaxWidth(),
            variant = GoldCrimsonCardVariant.GOLD_BORDER,
            contentPadding = PaddingValues(18.dp)
        ) {
            // Arabic Text Preview
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDark) Color.White else TextCharcoal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Simulated Slider Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("A-", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isDark) DarkBorder else BorderSubtle)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.6f)
                            .background(MerahMerdeka)
                    )
                }
                Text("A+", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MerahMerdeka)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Simulated Theme Swatches
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThemeColorBubble(color = Color(0xFFFBF8F3), label = "Krem", isSelected = true)
                Spacer(modifier = Modifier.width(16.dp))
                ThemeColorBubble(color = Color(0xFFFFFFFF), label = "Putih", isSelected = false)
                Spacer(modifier = Modifier.width(16.dp))
                ThemeColorBubble(color = Color(0xFF1E1A1A), label = "Gelap", isSelected = false)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            shape = RoundedCornerShape(100.dp),
            color = EmasKhidmat.copy(alpha = if (isDark) 0.25f else 0.15f),
            border = BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.4f))
        ) {
            Text(
                text = "👓 RAMAH SEMUA USIA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) EmasMuda else EmasTua,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Membaca Nyaman & Ramah Mata",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else TextCharcoal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Sesuaikan ukuran tulisan Arab, terjemahan, dan warna latar bacaan agar mata tidak lelah—nyaman dibaca oleh ikhwan & akhwat segala usia.",
            fontSize = 14.sp,
            color = if (isDark) DarkMuted else TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun ThemeColorBubble(color: Color, label: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) MerahMerdeka else Color.Gray.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, color = TextMuted)
    }
}

@Composable
private fun OnboardingSlideWidget(isDark: Boolean) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Widget Preview Card (Simulating a modern home screen widget)
        GoldCrimsonCard(
            modifier = Modifier.fillMaxWidth(),
            variant = GoldCrimsonCardVariant.GOLD_BORDER,
            contentPadding = PaddingValues(16.dp)
        ) {
            // Widget Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕌", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ROBITHOH WIDGET",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmasKhidmat,
                        letterSpacing = 1.sp
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isDark) DarkSurfaceVariant else Color(0xFFF0ECE4)
                ) {
                    Text(
                        text = "Lokasi Presisi",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) DarkMuted else TextMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Active Prayer Hero
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Menuju Sholat",
                        fontSize = 11.sp,
                        color = if (isDark) DarkMuted else TextMuted
                    )
                    Text(
                        text = "Maghrib • 18:02",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else TextCharcoal
                    )
                }

                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = MerahMerdeka.copy(alpha = if (isDark) 0.25f else 0.12f),
                    border = BorderStroke(1.dp, MerahMerdeka.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "⏳ 23 Menit lagi",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) MerahPrimaryDark else MerahMarunGelap,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = if (isDark) DarkBorder else BorderSubtle, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Mini Prayer Times Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniPrayerTimeItem("Subuh", "04:38", false, isDark)
                MiniPrayerTimeItem("Dzuhur", "11:58", false, isDark)
                MiniPrayerTimeItem("Ashar", "15:15", false, isDark)
                MiniPrayerTimeItem("Maghrib", "18:02", true, isDark)
                MiniPrayerTimeItem("Isya", "19:11", false, isDark)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Widget Quick Tap Shortcuts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDark) DarkSurfaceVariant else Color(0xFFF7F4EE),
                    border = BorderStroke(0.5.dp, if (isDark) DarkBorder else BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("📿", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Dzikir Harian",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else TextCharcoal
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDark) DarkSurfaceVariant else Color(0xFFF7F4EE),
                    border = BorderStroke(0.5.dp, if (isDark) DarkBorder else BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("📖", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Baca Terakhir",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else TextCharcoal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tip Card
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isDark) DarkSurfaceVariant.copy(alpha = 0.6f) else Color(0xFFF5EFE6),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💡", fontSize = 13.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tekan lama Home Screen ➔ Widget ➔ Robithoh",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) DarkMuted else TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = RoundedCornerShape(100.dp),
            color = MerahMerdeka.copy(alpha = if (isDark) 0.25f else 0.12f),
            border = BorderStroke(1.dp, MerahMerdeka.copy(alpha = 0.3f))
        ) {
            Text(
                text = "📱 WIDGET LAYAR UTAMA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) MerahPrimaryDark else MerahMarunGelap,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Akses Cepat Lewat Widget",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else TextCharcoal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Pantau jadwal sholat, sisa hitung mundur waktu adzan, dan langsung lanjutkan wirid atau bacaan amaliyah langsung dari layar depan ponsel Anda.",
            fontSize = 14.sp,
            color = if (isDark) DarkMuted else TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun MiniPrayerTimeItem(
    name: String,
    time: String,
    isActive: Boolean,
    isDark: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            fontSize = 10.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) MerahMerdeka else if (isDark) DarkMuted else TextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = time,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) MerahMerdeka else if (isDark) Color.White else TextCharcoal
        )
    }
}

@Composable
private fun OnboardingSlidePermissions(
    isDark: Boolean,
    onGrantClicked: () -> Unit,
    onSkipPermissionClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Optimalkan Waktu Ibadah",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else TextCharcoal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Aktifkan akses berikut agar Robithoh dapat menghitung jadwal sholat dan membunyikan pengingat adzan secara tepat:",
            fontSize = 13.sp,
            color = if (isDark) DarkMuted else TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Permission Card 1: Lokasi
        GoldCrimsonCard(
            modifier = Modifier.fillMaxWidth(),
            variant = GoldCrimsonCardVariant.GOLD_BORDER,
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MerahMerdeka.copy(alpha = 0.12f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("📍", fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Akses Lokasi (GPS)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else TextCharcoal
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = EmasKhidmat.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Waktu Sholat",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) EmasMuda else EmasTua,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Untuk menghitung waktu sholat presisi dan penunjuk arah kiblat sesuai tempat Anda berada.",
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else TextMuted,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Permission Card 2: Notifikasi
        GoldCrimsonCard(
            modifier = Modifier.fillMaxWidth(),
            variant = GoldCrimsonCardVariant.GOLD_BORDER,
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmasKhidmat.copy(alpha = 0.15f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🔔", fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Notifikasi Adzan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else TextCharcoal
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MerahMerdeka.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Pengingat",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) MerahPrimaryDark else MerahMarunGelap,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Memberikan pengingat adzan masuknya waktu sholat fardhu agar amaliyah tepat waktu.",
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else TextMuted,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Primary Button: Izinkan & Mulai
        Button(
            onClick = onGrantClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = MerahMerdeka,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Izinkan & Mulai Amaliyah",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Secondary Text Button: Nanti Saja
        TextButton(
            onClick = onSkipPermissionClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Atur Nanti Saja",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDark) DarkMuted else TextMuted
            )
        }
    }
}
