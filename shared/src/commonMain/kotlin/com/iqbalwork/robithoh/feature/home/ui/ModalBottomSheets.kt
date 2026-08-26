package com.iqbalwork.robithoh.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

data class SheetGridItem(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val isSunda: Boolean = false,
    val documentId: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManaqibModalBottomSheet(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val manaqibIdItems = listOf(
        SheetGridItem("mc_id", "MC", "🎤", false, "mc_manaqib_id"),
        SheetGridItem("quran_id", "Quran", "📖", false, "quran_list"),
        SheetGridItem("thoriqoh_id", "Thoriqoh", "📜", false, "sholawat_thoriqiyyah"),
        SheetGridItem("tanbih_id", "Tanbih", "🏛️", false, "tanbih_id"),
        SheetGridItem("tawassul_id", "Tawassul", "🤲", false, "tawassul_tqn"),
        SheetGridItem("manqobah_id", "Manqobah", "📜", false, "manqobah_id"),
        SheetGridItem("bani_id", "Bani Hasyim", "✨", false, "sholawat_bani_hasyim"),
        SheetGridItem("badriyyah_id", "Badriyyah", "🌟", false, "sholawat_badriyyah")
    )

    val manaqibSuItems = listOf(
        SheetGridItem("mc_su", "MC", "🎤", true, "mc_manaqib_su"),
        SheetGridItem("tanbih_su", "Tanbih", "🏛️", true, "tanbih_su"),
        SheetGridItem("manqobah_su", "Manqobah", "📜", true, "manqobah_su")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "Manaqib",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Manaqib Indonesia Grid (4 columns)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                manaqibIdItems.take(4).forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                manaqibIdItems.drop(4).take(4).forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Manaqib Sunda",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Manaqib Sunda Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                manaqibSuItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SholatModalBottomSheet(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val items = listOf(
        SheetGridItem("harian", "Harian", "⏰", false, "sholat_harian"),
        SheetGridItem("bulanan", "Bulanan", "📅", false, "amaliyah_muharrom"),
        SheetGridItem("tahunan", "Tahunan", "🗓️", false, "sholat_rojab"),
        SheetGridItem("safar", "Safar", "🕌", false, "sholat_tarowih")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "Sholat Sunnah & Waktu",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SholawatModalBottomSheet(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val items = listOf(
        SheetGridItem("thoriqoh", "Thoriqoh", "📜", false, "sholawat_thoriqiyyah"),
        SheetGridItem("bani", "Bani Hasyim", "✨", false, "sholawat_bani_hasyim"),
        SheetGridItem("amjad", "Amjad", "🌟", false, "amjad"),
        SheetGridItem("iqomah", "Iqomah Subuh", "🌅", false, "iqomah_subuh"),
        SheetGridItem("badriyyah", "Badriyyah", "⭐", false, "sholawat_badriyyah")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "Sholawat TQN Sirnarasa",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items.take(4).forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items.drop(4).forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TahlilZiyarohModalBottomSheet(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val items = listOf(
        SheetGridItem("tahlil", "Tahlil TQN", "📿", false, "tahlil_tqn"),
        SheetGridItem("waliyulloh", "Dziarah Wali", "🕌", false, "dziarah_waliyulloh"),
        SheetGridItem("umum", "Dziarah Umum", "🤲", false, "dziarah_umum"),
        SheetGridItem("kubur", "Dziarah Kubur", "🌿", false, "dziarah_kubur")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "Tahlil & Ziyaroh",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoaModalBottomSheet(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val items = listOf(
        SheetGridItem("istighotsah", "Istighotsah", "🤲", false, "doa_istighotsah"),
        SheetGridItem("rijalul", "Rijalul Ghoib", "✨", false, "doa_rijalul_ghoib"),
        SheetGridItem("tidur", "Sebelum Tidur", "🌙", false, "sebelum_tidur"),
        SheetGridItem("mursyid", "Salam Mursyid", "🌿", false, "salam_wali_mursyid")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "Doa & Wirid Khusus",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
            }
        }
    }
}

@Composable
private fun SheetIconCard(
    item: SheetGridItem,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(MerahMerdeka, MerahMarunGelap)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.iconEmoji,
                fontSize = 28.sp
            )

            if (item.isSunda) {
                Surface(
                    color = MerahSundaBadge,
                    shape = RoundedCornerShape(topStart = 4.dp, bottomEnd = 4.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "SUNDA",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextCharcoal,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
