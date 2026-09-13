package com.iqbalwork.robithoh.feature.waktal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted
import com.iqbalwork.robithoh.feature.waktal.data.sync.WaktalSyncState
import com.iqbalwork.robithoh.feature.waktal.domain.WaktalStatus
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalListUiState
import com.iqbalwork.robithoh.feature.waktal.ui.components.WaktalCard

@Composable
fun WaktalListContent(
    state: WaktalListUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectStatus: (WaktalStatus) -> Unit,
    onSelectProvince: (String?) -> Unit,
    onToggleSortByDistance: (Boolean) -> Unit,
    onWaktalClick: (Int) -> Unit,
    onTriggerSync: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = if (isDark) DarkCanvas else PaperBackgroundLight,
        topBar = {
            IslamicHeader(
                title = "Direktori Wakil Talqin",
                subtitle = "Ulama Waktal TQN Suryalaya - Sirnarasa",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = onTriggerSync) {
                        Text(text = "🔄", fontSize = 18.sp)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search TextField
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = {
                    Text(
                        text = "Cari nama, kota, majlis...",
                        color = if (isDark) Color(0xFFA1A1AA) else TextMuted,
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MerahMerdeka,
                    unfocusedBorderColor = if (isDark) DarkBorder else Color(0xFFE2E8F0),
                    focusedContainerColor = if (isDark) DarkSurface else Color.White,
                    unfocusedContainerColor = if (isDark) DarkSurface else Color.White
                )
            )

            // Status Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(WaktalStatus.entries) { status ->
                    val selected = state.selectedStatus == status
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectStatus(status) },
                        label = {
                            Text(
                                text = status.label,
                                fontSize = 13.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MerahMerdeka,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                item {
                    val isSortSelected = state.isSortByDistance
                    FilterChip(
                        selected = isSortSelected,
                        onClick = { onToggleSortByDistance(!isSortSelected) },
                        label = {
                            Text(
                                text = "📍 Jarak Terdekat",
                                fontSize = 13.sp,
                                fontWeight = if (isSortSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF2563EB),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Province Chips (if available)
            if (state.availableProvinces.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        val isAllSelected = state.selectedProvince == null
                        FilterChip(
                            selected = isAllSelected,
                            onClick = { onSelectProvince(null) },
                            label = { Text("Semua Wilayah", fontSize = 12.sp) }
                        )
                    }

                    items(state.availableProvinces) { province ->
                        val selected = state.selectedProvince == province
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectProvince(if (selected) null else province) },
                            label = { Text(province, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // Sync Banner info
            when (val sync = state.syncState) {
                is WaktalSyncState.Checking -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFEF3C7))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = sync.message,
                            fontSize = 12.sp,
                            color = Color(0xFF92400E),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                is WaktalSyncState.Syncing -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFDBEAFE))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Mengunduh pembaruan data Waktal...",
                            fontSize = 12.sp,
                            color = Color(0xFF1E40AF),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                else -> {}
            }

            // Items List
            if (state.filteredItems.isEmpty() && !state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "👳‍♂️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Data Wakil Talqin tidak ditemukan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else TextCharcoal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coba ubah kata kunci pencarian atau filter wilayah Anda.",
                            fontSize = 13.sp,
                            color = if (isDark) Color(0xFFA1A1AA) else TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.filteredItems,
                        key = { it.id }
                    ) { item ->
                        WaktalCard(
                            item = item,
                            onClick = { onWaktalClick(item.id) }
                        )
                    }
                }
            }
        }
    }
}
