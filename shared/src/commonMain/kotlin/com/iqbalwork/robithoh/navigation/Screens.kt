package com.iqbalwork.robithoh.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

private val MerahMerdeka = Color(0xFFCE1126)
private val EmasKhidmat = Color(0xFFD4AF37)
private val PutihAbu = Color(0xFFF8F9FA)
private val CharcoalDark = Color(0xFF1A1D20)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (ScreenKey) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Robithoh",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Aplikasi Spiritual & Amaliyah Sirnarasa",
                            fontSize = 12.sp,
                            color = EmasKhidmat
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MerahMerdeka
                )
            )
        },
        containerColor = PutihAbu
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Selamat Datang di Robithoh — Panduan Amaliyah, Dzikir Khofi/Jahr, Manqobah Syekh Abdul Qodir Al-Jailani r.a. dan Al-Qur'an Digital 114 Surah.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Text(
                "Menu Utama",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalDark
            )

            // Navigation Grid Buttons
            MenuNavigationCard(
                title = "Buku Saku Amaliyah",
                subtitle = "Tata Tertib Sholat, Kaifiyat Dzikir Khofi/Jahr, & Doa-Doa",
                badge = "TQN",
                onClick = { onNavigate(ScreenKey.Amaliyah) }
            )

            MenuNavigationCard(
                title = "Tasbih Digital Haptik",
                subtitle = "Penghitung wirid interaktif dengan getaran preset 33/100/165",
                badge = "Haptic",
                onClick = { onNavigate(ScreenKey.Tasbih) }
            )

            MenuNavigationCard(
                title = "Manqobah 1-56",
                subtitle = "MC Acara, Tanbih, Silsilah 1-38 & Manqobah 3 Bahasa",
                badge = "3 Bahasa",
                onClick = { onNavigate(ScreenKey.ManaqibList) }
            )

            MenuNavigationCard(
                title = "Al-Qur'an Digital 114 Surah",
                subtitle = "Mushaf Tajwid, Shalawat Bani Hasyim & Panduan Ziarah",
                badge = "Audio",
                onClick = { onNavigate(ScreenKey.QuranList) }
            )

            MenuNavigationCard(
                title = "Profil Pesantren Sirnarasa",
                subtitle = "Ekosistem lembaga, STID, Baitul Asror & sejarah",
                badge = "Profil",
                onClick = { onNavigate(ScreenKey.ProfilePesantren) }
            )

            MenuNavigationCard(
                title = "Pengaturan Aplikasi",
                subtitle = "Ukuran font Arab, tema warna & notifikasi",
                badge = "Settings",
                onClick = { onNavigate(ScreenKey.Settings) }
            )
        }
    }
}

@Composable
private fun MenuNavigationCard(
    title: String,
    subtitle: String,
    badge: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = CharcoalDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = MerahMerdeka.copy(alpha = 0.1f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = badge,
                    color = MerahMerdeka,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun AmaliyahScreen(
    onNavigate: (ScreenKey) -> Unit,
    onBack: () -> Unit,
    viewModel: com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel = androidx.compose.runtime.remember {
        com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel()
    }
) {
    val state by viewModel.uiState.collectAsState()
    com.iqbalwork.robithoh.feature.amaliyah.ui.AmaliyahScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onNavigate = onNavigate,
        onBack = onBack
    )
}

@Composable
fun TasbihScreen(
    onBack: () -> Unit,
    viewModel: com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel = androidx.compose.runtime.remember {
        com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel()
    }
) {
    val state by viewModel.uiState.collectAsState()
    com.iqbalwork.robithoh.feature.tasbih.ui.TasbihScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onBack = onBack
    )
}

@Composable
fun ManaqibListScreen(
    onChapterClick: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: com.iqbalwork.robithoh.feature.manaqib.presentation.ManaqibViewModel = androidx.compose.runtime.remember {
        com.iqbalwork.robithoh.feature.manaqib.presentation.ManaqibViewModel(
            com.iqbalwork.robithoh.feature.manaqib.data.ManaqibRepositoryImpl()
        )
    }
) {
    com.iqbalwork.robithoh.feature.manaqib.presentation.ManaqibListScreen(
        viewModel = viewModel,
        onChapterClick = onChapterClick,
        onBackClick = onBack
    )
}

@Composable
fun ManaqibDetailScreen(
    chapterNumber: Int,
    onBack: () -> Unit,
    viewModel: com.iqbalwork.robithoh.feature.manaqib.presentation.ManaqibViewModel = androidx.compose.runtime.remember {
        com.iqbalwork.robithoh.feature.manaqib.presentation.ManaqibViewModel(
            com.iqbalwork.robithoh.feature.manaqib.data.ManaqibRepositoryImpl()
        )
    }
) {
    com.iqbalwork.robithoh.feature.manaqib.presentation.ManaqibDetailScreen(
        viewModel = viewModel,
        chapterNumber = chapterNumber,
        onBackClick = onBack
    )
}

@Composable
fun QuranListScreen(
    onSurahClick: (Int, Int?) -> Unit,
    onBack: () -> Unit
) {
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val viewModel = androidx.compose.runtime.remember(database) {
        com.iqbalwork.robithoh.feature.quran.presentation.QuranViewModel(
            com.iqbalwork.robithoh.feature.quran.data.QuranRepositoryImpl(database)
        )
    }
    val state by viewModel.uiState.collectAsState()
    com.iqbalwork.robithoh.feature.library.ui.KitabTabContent(
        lastReadBookmark = state.lastReadBookmark,
        onNavigateToSurah = onSurahClick,
        onBack = onBack
    )
}

@Composable
fun QuranSurahScreen(
    surahNumber: Int,
    onBack: () -> Unit,
    initialAyahNumber: Int? = null
) {
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val viewModel = androidx.compose.runtime.remember(database) {
        com.iqbalwork.robithoh.feature.quran.presentation.QuranViewModel(
            com.iqbalwork.robithoh.feature.quran.data.QuranRepositoryImpl(database)
        )
    }
    com.iqbalwork.robithoh.feature.quran.presentation.QuranReaderScreen(
        viewModel = viewModel,
        surahNumber = surahNumber,
        initialAyahNumber = initialAyahNumber,
        onBackClick = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("←", color = Color.White, fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MerahMerdeka)
            )
        },
        containerColor = PutihAbu
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Robithoh v1.0.0",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CharcoalDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aplikasi Spiritual & Amaliyah TQN 38 Sirnarasa Ciceuri Panjalu Ciamis. 100% Offline-First dengan SQLDelight.",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun ProfilePesantrenScreen(onBack: () -> Unit) {
    com.iqbalwork.robithoh.feature.profile.presentation.ProfileScreen(
        onBackClick = onBack
    )
}
