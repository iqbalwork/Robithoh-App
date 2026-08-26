package com.iqbalwork.robithoh.feature.tasbih.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihDzikirPreset
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(
    state: TasbihUiState,
    onIntent: (TasbihUiIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val coroutineScope = rememberCoroutineScope()
    val scaleAnim = remember { Animatable(1f) }
    var showDzikirSelectorSheet by remember { mutableStateOf(false) }
    var customTargetInput by remember { mutableStateOf(state.targetCount.toString()) }

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
            // Top Section: Selected Dzikir Card & Presets
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Active Dzikir Banner
                GoldCrimsonCard(
                    variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                    onClick = { showDzikirSelectorSheet = true },
                    contentPadding = PaddingValues(12.dp)
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

                // Preset Targets Row (33x, 100x, 165x, Custom)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf(33, 100, 165)
                    presets.forEach { target ->
                        val isSelected = state.targetCount == target
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MerahMerdeka else (if (isDark) DarkSurfaceVariant else Color(0xFFE9ECEF)),
                            border = if (isSelected) BorderStroke(1.dp, EmasKhidmat) else null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onIntent(TasbihUiIntent.SetTarget(target)) }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "${target}x",
                                    color = if (isSelected) PutihBersih else (if (isDark) PutihBersih else SlateCharcoalText),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = if (target == 165) "TQN" else "Preset",
                                    color = if (isSelected) EmasMuda else (if (isDark) DarkMuted else SlateMuted),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }

                    // Custom Target Button
                    val isCustom = state.targetCount !in presets
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
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = if (isCustom) "${state.targetCount}x" else "Kustom",
                                color = if (isCustom) PutihBersih else (if (isDark) PutihBersih else SlateCharcoalText),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Bebas",
                                color = if (isCustom) EmasMuda else (if (isDark) DarkMuted else SlateMuted),
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Center Interactive Spring-Animated Tap Surface
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(270.dp)
                    .scale(scaleAnim.value)
                    .clip(CircleShape)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = if (isDark) listOf(
                                MerahMarunGelap,
                                Color(0xFF280306),
                                DarkSurface
                            ) else listOf(
                                Color(0xFFFFF0F0),
                                Color(0xFFFFE5E5),
                                PutihBersih
                            )
                        )
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                coroutineScope.launch {
                                    scaleAnim.animateTo(
                                        targetValue = 0.92f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                                tryAwaitRelease()
                                coroutineScope.launch {
                                    scaleAnim.animateTo(
                                        targetValue = 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                                onIntent(TasbihUiIntent.Increment)
                            }
                        )
                    }
            ) {
                // Circular Progress Arc Canvas
                val progressFraction = (state.currentCount.toFloat() / state.targetCount.toFloat()).coerceIn(0f, 1f)
                val primaryColor = MerahMerdeka
                val goldColor = EmasKhidmat
                val ringBgColor = if (isDark) Color(0xFF3A1215) else Color(0xFFFFD4D8)

                Canvas(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                    // Background track ring
                    drawCircle(
                        color = ringBgColor,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                    // Progress arc
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(primaryColor, goldColor, primaryColor)
                        ),
                        startAngle = -90f,
                        sweepAngle = progressFraction * 360f,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Center Count & Subtitle Display
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${state.currentCount}",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 62.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isDark) PutihBersih else MerahMarunGelap
                        )
                    )
                    Text(
                        text = "dari ${state.targetCount}x",
                        color = if (isDark) EmasMuda else EmasKhidmat,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = MerahMerdeka.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Putaran ke-${state.lapCount + 1}",
                            color = MerahMerdeka,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ketuk untuk hitung",
                        color = if (isDark) DarkMuted else SlateMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Actions Section: Total count, Decrement (-1), Reset button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Total Wirid Sesi Ini: ${state.totalCount} kali",
                    fontSize = 13.sp,
                    color = if (isDark) PutihBersih else SlateCharcoalText,
                    fontWeight = FontWeight.Medium
                )

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
                        val count = customTargetInput.toIntOrNull() ?: 33
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
