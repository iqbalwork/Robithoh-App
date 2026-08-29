package com.iqbalwork.robithoh.feature.home.ui

import androidx.compose.foundation.BorderStroke
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

            // Manaqib Grid (4 columns)
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SholatModalBottomSheet(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val harianItems = listOf(
        SheetGridItem("harian", "Harian", "⏰", false, "sholat_harian"),
        SheetGridItem("sebelum_tidur", "Sebelum Tidur", "🛌", false, "sebelum_tidur"),
        SheetGridItem("bulanan", "Bulanan", "📅", false, "sholat_bulanan"),
        SheetGridItem("safar", "Safar", "🚗", false, "sholat_safar")
    )

    val tahunanItems = listOf(
        SheetGridItem("rojab", "Sholat Rojab", "🌙", false, "sholat_rojab"),
        SheetGridItem("nisfu_syaban", "Nisfu Sya'ban", "✨", false, "sholat_nisfu_syaban"),
        SheetGridItem("tarowih", "Tarowih", "🕌", false, "sholat_tarowih"),
        SheetGridItem("lailatul_qodar", "Lailatul Qodar", "🌟", false, "sholat_lailatul_qodar"),
        SheetGridItem("lidafil_bala", "Lidaf'il Bala", "🛡️", false, "sholat_lidafil_bala")
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
                text = "Sholat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "WAKTU & SAFAR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                harianItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SheetIconCard(item = item, onClick = { onItemClick(item.documentId) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "SHOLAT TAHUNAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                tahunanItems.take(4).forEach { item ->
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
                tahunanItems.drop(4).forEach { item ->
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

data class SheetMenuItem(
    val title: String,
    val documentId: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TahlilZiyarohModalBottomSheet(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val items = listOf(
        SheetMenuItem("Tahlil TQN", "tahlil_tqn"),
        SheetMenuItem("Ziyaroh Umum", "dziarah_umum"),
        SheetMenuItem("Ziyaroh Waliyulloh", "dziarah_waliyulloh")
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
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Tahlil & Ziyaroh",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(4.dp))

            items.forEach { item ->
                SheetMenuButton(
                    title = item.title,
                    onClick = { onItemClick(item.documentId) }
                )
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
        SheetMenuItem("Salam Wali Mursyid", "salam_wali_mursyid"),
        SheetMenuItem("Doa Rijalul Ghoib", "doa_rijalul_ghoib"),
        SheetMenuItem("Ziyaroh Rosul", "sholawat_jiyaaroh"),
        SheetMenuItem("Doa Istighotsah", "doa_istighotsah"),
        SheetMenuItem("Amaliyah Sebelum Tidur", "sebelum_tidur")
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
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Doa",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextCharcoal
            )
            Spacer(modifier = Modifier.height(4.dp))

            items.forEach { item ->
                SheetMenuButton(
                    title = item.title,
                    onClick = { onItemClick(item.documentId) }
                )
            }
        }
    }
}

@Composable
fun SheetMenuButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.5.dp, MerahMerdeka)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title.uppercase(),
                color = MerahMerdeka,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
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
