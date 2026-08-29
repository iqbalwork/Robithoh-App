package com.iqbalwork.robithoh.feature.tasbih.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCard
import com.iqbalwork.robithoh.core.designsystem.component.GoldCrimsonCardVariant
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurfaceVariant
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihAbuBackground
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateBorder
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiState
import com.iqbalwork.robithoh.feature.tasbih.ui.component.TasbihCounterDisk
import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(
    state: TasbihUiState,
    onIntent: (TasbihUiIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    var showDzikirSelectorSheet by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (showDzikirSelectorSheet) {
            showDzikirSelectorSheet = false
        } else {
            onBack()
        }
    }
    var customTargetInput by rememberSaveable { mutableStateOf(state.targetCount.toString()) }

    val isMilestone = state.currentCount > 0 && state.currentCount % state.targetCount == 0
    val progressPercent = if (state.targetCount > 0) {
        ((state.currentCount.toFloat() / state.targetCount.toFloat()) * 100).toInt().coerceIn(0, 100)
    } else 0

    Scaffold(
        topBar = {
            IslamicHeader(
                title = "Tasbih Digital Haptik",
                subtitle = "Penghitung Dzikir Interaktif",
                arabicTitle = "الْمِسْبَحَةُ الرَّقْمِيَّةُ",
                onBackClick = onBack,
                actions = {
                    // Sound Toggle Icon Button
                    IconButton(onClick = { onIntent(TasbihUiIntent.ToggleSound) }) {
                        Text(
                            text = if (state.isSoundEnabled) "🔔" else "🔕",
                            fontSize = 18.sp
                        )
                    }
                    // Haptic Toggle Icon Button
                    IconButton(onClick = { onIntent(TasbihUiIntent.ToggleHaptic) }) {
                        Text(
                            text = if (state.isHapticEnabled) "📳" else "📴",
                            fontSize = 18.sp
                        )
                    }
                }
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Selected Dzikir Card & Presets (165x & Kustom)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Active Dzikir Banner
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                    onClick = { showDzikirSelectorSheet = true },
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "BACAAN DZIKIR AKTIF",
                                color = EmasMuda,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = state.selectedDzikirTitle,
                                color = PutihBersih,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.selectedDzikirArabic,
                                style = RabithohTheme.typography.arabicMedium.copy(
                                    color = PutihBersih,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Surface(
                            color = PutihBersih.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Ganti ›",
                                color = PutihBersih,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Preset Targets Row: 165x (TQN) and Kustom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val is165 = state.targetCount == 165
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (is165) MerahMerdeka else (if (isDark) DarkSurfaceVariant else Color(0xFFE9ECEF)),
                        border = if (is165) BorderStroke(1.dp, EmasKhidmat) else null,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onIntent(TasbihUiIntent.SetTarget(165)) }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = "165x",
                                color = if (is165) PutihBersih else (if (isDark) PutihBersih else SlateCharcoalText),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "TQN PP Suryalaya Sirnarasa 38",
                                color = if (is165) EmasMuda else (if (isDark) DarkMuted else SlateMuted),
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Custom Target Button
                    val isCustom = state.targetCount != 165
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCustom) MerahMerdeka else (if (isDark) DarkSurfaceVariant else Color(0xFFE9ECEF)),
                        border = if (isCustom) BorderStroke(1.dp, EmasKhidmat) else null,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                customTargetInput = state.targetCount.toString()
                                onIntent(TasbihUiIntent.ShowCustomTargetDialog)
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = if (isCustom) "${state.targetCount}x" else "Kustom",
                                color = if (isCustom) PutihBersih else (if (isDark) PutihBersih else SlateCharcoalText),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isCustom) "Aktif" else "Ubah Target Bebas",
                                color = if (isCustom) EmasMuda else (if (isDark) DarkMuted else SlateMuted),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Center Interactive Counter Disk
            TasbihCounterDisk(
                currentCount = state.currentCount,
                targetCount = state.targetCount,
                isMilestone = isMilestone,
                onTap = { onIntent(TasbihUiIntent.Increment) },
                diskSize = 270.dp,
                isCompact = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Actions & Stats Section: Putaran, Kemajuan, Decrement (-1), Reset
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Stats Card
                Surface(
                    color = if (isDark) DarkSurface else Color.White,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, if (isDark) DarkBorder else Color(0xFFE2E8F0)),
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Putaran Selesai", fontSize = 11.sp, color = if (isDark) DarkMuted else SlateMuted)
                            Text(
                                text = "${state.lapCount}x",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isDark) PutihBersih else TextCharcoal
                            )
                        }

                        VerticalDivider(
                            modifier = Modifier.height(28.dp),
                            color = if (isDark) DarkBorder else Color(0xFFE2E8F0)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Kemajuan", fontSize = 11.sp, color = if (isDark) DarkMuted else SlateMuted)
                            Text(
                                text = "$progressPercent%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = EmasKhidmat
                            )
                        }

                        VerticalDivider(
                            modifier = Modifier.height(28.dp),
                            color = if (isDark) DarkBorder else Color(0xFFE2E8F0)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Sesi", fontSize = 11.sp, color = if (isDark) DarkMuted else SlateMuted)
                            Text(
                                text = "${state.totalCount}x",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MerahMerdeka
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Decrement Button (-1)
                    OutlinedButton(
                        onClick = { onIntent(TasbihUiIntent.Decrement) },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isDark) DarkBorder else SlateBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Kurang (-1)", color = if (isDark) PutihBersih else SlateCharcoalText, fontSize = 13.sp)
                    }

                    // Reset Button
                    Button(
                        onClick = { onIntent(TasbihUiIntent.RequestReset) },
                        colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Ulangi (Reset)", color = PutihBersih, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }

    // Reset Confirmation Dialog
    if (state.showResetDialog) {
        AlertDialog(
            onDismissRequest = { onIntent(TasbihUiIntent.DismissResetDialog) },
            title = { Text("Reset Hitungan Tasbih?", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin mengulang hitungan kembali ke angka 0?") },
            confirmButton = {
                Button(
                    onClick = { onIntent(TasbihUiIntent.ConfirmReset) },
                    colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka)
                ) {
                    Text("Ya, Reset", color = PutihBersih)
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(TasbihUiIntent.DismissResetDialog) }) {
                    Text("Batal")
                }
            }
        )
    }

    // Custom Target Input Dialog
    if (state.showCustomTargetDialog) {
        AlertDialog(
            onDismissRequest = { onIntent(TasbihUiIntent.DismissCustomTargetDialog) },
            title = { Text("Target Hitungan Kustom", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Masukkan jumlah target hitungan dzikir yang Anda inginkan:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customTargetInput,
                        onValueChange = { customTargetInput = it.filter { ch -> ch.isDigit() } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val count = customTargetInput.toIntOrNull() ?: 165
                        onIntent(TasbihUiIntent.SetTarget(count.coerceAtLeast(1)))
                        onIntent(TasbihUiIntent.DismissCustomTargetDialog)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka)
                ) {
                    Text("Simpan", color = PutihBersih)
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(TasbihUiIntent.DismissCustomTargetDialog) }) {
                    Text("Batal")
                }
            }
        )
    }

    // Milestone Reached Dialog
    if (state.isTargetReached) {
        AlertDialog(
            onDismissRequest = { onIntent(TasbihUiIntent.DismissTargetReached) },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Alhamdulillah! ✦", fontWeight = FontWeight.Bold, color = MerahMerdeka)
                }
            },
            text = {
                Text(
                    "Target ${state.targetCount}x dzikir '${state.selectedDzikirTitle}' telah selesai tercapai.\n\nLanjutkan putaran ke-${state.lapCount + 1}?"
                )
            },
            confirmButton = {
                Button(
                    onClick = { onIntent(TasbihUiIntent.DismissTargetReached) },
                    colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka)
                ) {
                    Text("Lanjutkan", color = PutihBersih)
                }
            }
        )
    }

    // Dzikir Selector Bottom Sheet / Dialog
    if (showDzikirSelectorSheet) {
        ModalBottomSheet(
            onDismissRequest = { showDzikirSelectorSheet = false },
            containerColor = if (isDark) DarkSurface else PutihBersih
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Pilih Lafadz Dzikir & Wirid",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText
                    )
                )
                state.availablePresets.forEach { preset ->
                    val isSelected = preset.id == state.selectedDzikirId
                    GoldCrimsonCard(
                        variant = if (isSelected) GoldCrimsonCardVariant.CRIMSON_BORDER else GoldCrimsonCardVariant.GOLD_BORDER,
                        onClick = {
                            onIntent(TasbihUiIntent.SelectDzikir(preset))
                            showDzikirSelectorSheet = false
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = preset.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) MerahMerdeka else (if (isDark) PutihBersih else SlateCharcoalText)
                                )
                                Text(
                                    text = preset.arabic,
                                    style = RabithohTheme.typography.arabicMedium.copy(
                                        color = EmasKhidmat,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = preset.virtue,
                                    fontSize = 11.sp,
                                    color = if (isDark) DarkMuted else SlateMuted
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MerahMerdeka else (if (isDark) DarkSurfaceVariant else Color(0xFFE9ECEF))
                            ) {
                                Text(
                                    text = "${preset.defaultTarget}x",
                                    color = if (isSelected) PutihBersih else SlateCharcoalText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
