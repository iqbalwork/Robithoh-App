package com.iqbalwork.robithoh.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val MerahMerdeka = Color(0xFFCE1126)
private val PutihAbu = Color(0xFFF8F9FA)
private val CharcoalDark = Color(0xFF1A1D20)

@Composable
fun PrayerCalculationMethodScreen(
    onBack: () -> Unit,
    viewModel: com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel? = null
) {
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val vm = viewModel ?: viewModel {
        com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel(database = database)
    }
    val state by vm.uiState.collectAsState()

    com.iqbalwork.robithoh.feature.amaliyah.ui.PrayerCalculationMethodScreen(
        selectedMethod = state.selectedCalculationMethod,
        onSelectMethod = { method ->
            vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.SelectCalculationMethod(method))
        },
        onBack = onBack
    )
}

@Composable
fun PrayerAdjustmentsScreen(
    onBack: () -> Unit,
    viewModel: com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel? = null
) {
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val vm = viewModel ?: viewModel {
        com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel(database = database)
    }
    val state by vm.uiState.collectAsState()

    com.iqbalwork.robithoh.feature.amaliyah.ui.PrayerAdjustmentsScreen(
        schedule = state.prayerSchedule,
        adjustments = state.prayerAdjustments,
        activePrayerTypeForSheet = state.activeAdjustmentPrayerType,
        onOpenPicker = { type ->
            vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.OpenAdjustmentPicker(type))
        },
        onSelectOffset = { type, offset ->
            vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.UpdatePrayerAdjustment(type, offset))
        },
        onClosePicker = {
            vm.onIntent(com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent.CloseAdjustmentPicker)
        },
        onBack = onBack
    )
}

@Composable
fun TasbihScreen(
    onBack: () -> Unit,
    viewModel: com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel = viewModel {
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
fun QuranListScreen(
    onSurahClick: (Int, Int?) -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val viewModel: com.iqbalwork.robithoh.feature.quran.presentation.QuranViewModel = viewModel {
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
    BackHandler {
        onBack()
    }
    val database = com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()
    val viewModel: com.iqbalwork.robithoh.feature.quran.presentation.QuranViewModel = viewModel(key = "quran_surah_$surahNumber") {
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
fun SettingsScreen(
    onNavigateToCalculationMethods: () -> Unit = {},
    onNavigateToPrayerAdjustments: () -> Unit = {},
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Waktu Salat Settings Group
            Text(
                text = "Pengaturan Waktu Salat",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalDark
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateToCalculationMethods)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Metode Perhitungan",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = CharcoalDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Pilih standar lembaga (Kemenag RI, JAKIM, MWL, Umm al-Qura, dll)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Text(text = "›", fontSize = 20.sp, color = Color.Gray)
                    }

                    HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateToPrayerAdjustments)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Penyesuaian Waktu Salat",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = CharcoalDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Koreksi manual menit per waktu sholat (Imsak, Subuh, Dzuhur, dll)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Text(text = "›", fontSize = 20.sp, color = Color.Gray)
                    }
                }
            }

            // App Info Card
            Text(
                text = "Tentang Aplikasi",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalDark
            )

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
                        text = "Aplikasi Ibadah & Amaliyah MTQN Suryalaya Sirnarasa PPKN III Modern Multiplatform. 100% Offline-First dengan SQLDelight & adhan-kotlin.",
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
