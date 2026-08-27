package com.iqbalwork.robithoh.core.designsystem.component

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta

/**
 * Full-screen searchable surat picker, opened from the "Menuju Surat / Ayat" sheet
 * so choosing among all 114 surah doesn't rely on scrolling a small dropdown.
 */
@Composable
fun SurahPickerScreen(
    surahs: List<SurahMeta>,
    onDismiss: () -> Unit,
    onSelect: (SurahMeta) -> Unit
) {
    val isDark = RabithohTheme.colors.isDark
    val textColor = if (isDark) PutihBersih else SlateCharcoalText
    val mutedColor = if (isDark) DarkMuted else SlateMuted
    val dividerColor = if (isDark) DarkBorder else BorderSubtle

    var query by remember { mutableStateOf("") }
    val filteredSurahs = remember(surahs, query) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            surahs
        } else {
            surahs.filter { surah ->
                surah.nameLatin.contains(trimmed, ignoreCase = true) ||
                surah.indonesianMeaning.contains(trimmed, ignoreCase = true) ||
                surah.nameArabic.contains(trimmed) ||
                surah.number.toString() == trimmed
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (isDark) DarkCanvas else PutihBersih
        ) {
            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Text("✕", color = MerahMerdeka, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Surat", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = textColor)
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Cari surat", color = mutedColor) },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MerahMerdeka,
                            unfocusedBorderColor = dividerColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = dividerColor)

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredSurahs, key = { it.number }) { surah ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(surah) }
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = "${surah.number}. ${surah.nameLatin}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = textColor
                            )
                            Text(
                                text = surah.indonesianMeaning,
                                fontSize = 12.sp,
                                color = mutedColor
                            )
                        }
                        HorizontalDivider(color = dividerColor.copy(alpha = 0.6f))
                    }
                }
            }
        }
    }
}
