package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
        SurahPickerContent(
            surahs = filteredSurahs,
            query = query,
            onQueryChange = { query = it },
            onDismiss = onDismiss,
            onSelect = onSelect
        )
    }
}
