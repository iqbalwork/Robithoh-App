package com.iqbalwork.robithoh.feature.tasbih.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiIntent
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihUiState

@Composable
fun FloatingTasbihOverlay(
    state: TasbihUiState,
    onIntent: (TasbihUiIntent) -> Unit,
    onOpenFullScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!state.isFloatingVisible) return

    var customTargetInput by remember(state.targetCount) { mutableStateOf(state.targetCount.toString()) }
    val progressPercent = if (state.targetCount > 0) {
        val countInLap = state.currentCount % state.targetCount
        val fraction = if (countInLap == 0 && state.currentCount > 0) 1f
                       else countInLap.toFloat() / state.targetCount.toFloat()
        (fraction * 100).toInt().coerceIn(0, 100)
    } else 0

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedContent(
            targetState = state.isFloatingExpanded,
            transitionSpec = {
                (fadeIn(spring()) + scaleIn(spring()))
                    .togetherWith(fadeOut(spring()) + scaleOut(spring()))
            },
            label = "FloatingTasbihAnimation"
        ) { expanded ->
            if (!expanded) {
                // Collapsed State: Floating Action Pill / Button
                Surface(
                    onClick = { onIntent(TasbihUiIntent.SetFloatingExpanded(true)) },
                    shape = RoundedCornerShape(28.dp),
                    color = MerahMarunGelap,
                    border = BorderStroke(1.5.dp, EmasKhidmat),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text("📿", fontSize = 18.sp)
                        Column {
                            Text(
                                text = "Tasbih",
                                color = EmasMuda,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "${state.currentCount} / ${state.targetCount}x",
                                color = PutihBersih,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Surface(
                            color = EmasKhidmat.copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("▲", color = EmasMuda, fontSize = 10.sp)
                            }
                        }
                    }
                }
            } else {
                // Expanded State: Interactive Floating Panel
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF140305).copy(alpha = 0.96f),
                    border = BorderStroke(1.5.dp, EmasKhidmat.copy(alpha = 0.8f)),
                    shadowElevation = 16.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Top Header: Title, Controls (Sound, Haptic, Fullscreen, Minimize)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("📿", fontSize = 16.sp)
                                Text(
                                    text = "Tasbih Digital",
                                    color = PutihBersih,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Sound toggle
                                IconButton(
                                    onClick = { onIntent(TasbihUiIntent.ToggleSound) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text(
                                        text = if (state.isSoundEnabled) "🔔" else "🔕",
                                        fontSize = 14.sp
                                    )
                                }

                                // Haptic toggle
                                IconButton(
                                    onClick = { onIntent(TasbihUiIntent.ToggleHaptic) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text(
                                        text = if (state.isHapticEnabled) "📳" else "📴",
                                        fontSize = 14.sp
                                    )
                                }

                                // Fullscreen button
                                IconButton(
                                    onClick = onOpenFullScreen,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("⛶", color = EmasMuda, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }

                                // Minimize button
                                IconButton(
                                    onClick = { onIntent(TasbihUiIntent.SetFloatingExpanded(false)) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("✕", color = PutihBersih.copy(alpha = 0.8f), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Target Selector Row: Preset 165x and Kustom
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val is165 = state.targetCount == 165
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (is165) MerahMerdeka else Color(0xFF240609),
                                border = BorderStroke(1.dp, if (is165) EmasKhidmat else Color(0xFF3D1015)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onIntent(TasbihUiIntent.SetTarget(165)) }
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "165x",
                                        color = if (is165) PutihBersih else Color.LightGray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "165x",
                                        color = if (is165) EmasMuda else DarkMuted,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            val isCustom = state.targetCount != 165
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isCustom) MerahMerdeka else Color(0xFF240609),
                                border = BorderStroke(1.dp, if (isCustom) EmasKhidmat else Color(0xFF3D1015)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        customTargetInput = state.targetCount.toString()
                                        onIntent(TasbihUiIntent.ShowCustomTargetDialog)
                                    }
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (isCustom) "${state.targetCount}x" else "Kustom",
                                        color = if (isCustom) PutihBersih else Color.LightGray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "✏️",
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        // Main Animated Tap Disk (Compact Mode)
                        TasbihCounterDisk(
                            currentCount = state.currentCount,
                            targetCount = state.targetCount,
                            isMilestone = state.isTargetReached,
                            onTap = { onIntent(TasbihUiIntent.Increment) },
                            diskSize = 185.dp,
                            isCompact = true
                        )

                        // Stats & Action Buttons (Putaran, Kemajuan, Decrement, Reset)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Putaran: ${state.lapCount}x",
                                    color = SlateMuted,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Kemajuan: $progressPercent%",
                                    color = EmasKhidmat,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                // Decrement button (-1)
                                OutlinedButton(
                                    onClick = { onIntent(TasbihUiIntent.Decrement) },
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFF4A151B)),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray)
                                ) {
                                    Text("-1", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                // Reset button
                                Button(
                                    onClick = { onIntent(TasbihUiIntent.RequestReset) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("Reset", fontSize = 11.sp, color = PutihBersih, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
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
                    Text("Masukkan target hitungan dzikir yang Anda inginkan (misal: 165, 300, 500):")
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
}
