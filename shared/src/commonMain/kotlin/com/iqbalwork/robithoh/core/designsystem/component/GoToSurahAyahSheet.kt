package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta

/**
 * Bottom sheet letting the reader jump straight to a chosen Surat and Ayat,
 * mirroring the "Menuju Surat / Ayat" pattern from mainstream Qur'an apps.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoToSurahAyahSheet(
    surahs: List<SurahMeta>,
    initialSurahNumber: Int,
    initialAyahNumber: Int = 1,
    onDismiss: () -> Unit,
    onConfirm: (surahNumber: Int, ayahNumber: Int) -> Unit
) {
    val isDark = RabithohTheme.colors.isDark
    val textColor = if (isDark) PutihBersih else SlateCharcoalText
    val labelColor = if (isDark) DarkMuted else SlateMuted
    val borderColor = if (isDark) DarkBorder else BorderSubtle

    var selectedSurah by remember {
        mutableStateOf(surahs.find { it.number == initialSurahNumber } ?: surahs.firstOrNull())
    }
    var ayahText by remember { mutableStateOf(initialAyahNumber.coerceAtLeast(1).toString()) }
    var showSurahPicker by remember { mutableStateOf(false) }

    LaunchedEffect(selectedSurah) {
        val maxAyah = selectedSurah?.numberOfAyahs ?: 1
        val current = ayahText.toIntOrNull()
        if (current != null && current > maxAyah) {
            ayahText = maxAyah.toString()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) DarkSurface else PutihBersih,
        shape = RabithohTheme.shapes.bottomSheetShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Menuju Surat / Ayat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Surat", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = labelColor)
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSurahPicker = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedSurah?.let { "${it.number}. ${it.nameLatin}" } ?: "Pilih Surat",
                        color = textColor,
                        fontSize = 15.sp
                    )
                    Text("▾", color = labelColor, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val maxAyah = selectedSurah?.numberOfAyahs ?: 1
            Text(text = "Ayat", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = labelColor)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = ayahText,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    ayahText = if (digits.isEmpty()) "" else digits.toInt().coerceIn(1, maxAyah).toString()
                },
                placeholder = { Text("1 - $maxAyah") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MerahMerdeka,
                    unfocusedBorderColor = borderColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Batal")
                }
                Button(
                    onClick = {
                        val surah = selectedSurah ?: return@Button
                        val ayahNumber = ayahText.toIntOrNull()?.coerceIn(1, maxAyah) ?: return@Button
                        onConfirm(surah.number, ayahNumber)
                    },
                    enabled = selectedSurah != null && ayahText.toIntOrNull() != null,
                    colors = ButtonDefaults.buttonColors(containerColor = MerahMerdeka),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pilih", color = PutihBersih, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showSurahPicker) {
        SurahPickerScreen(
            surahs = surahs,
            onDismiss = { showSurahPicker = false },
            onSelect = { surah ->
                selectedSurah = surah
                showSurahPicker = false
            }
        )
    }
}
