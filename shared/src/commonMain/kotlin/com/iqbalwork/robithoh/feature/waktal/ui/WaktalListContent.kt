package com.iqbalwork.robithoh.feature.waktal.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted
import com.iqbalwork.robithoh.feature.waktal.data.sync.WaktalSyncState
import com.iqbalwork.robithoh.feature.waktal.domain.WakilTalqin
import com.iqbalwork.robithoh.feature.waktal.domain.WaktalStatus
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalListUiState
import com.iqbalwork.robithoh.feature.waktal.ui.components.WaktalCard
import com.iqbalwork.robithoh.feature.waktal.ui.components.WaktalFilterSheet
import org.jetbrains.compose.resources.stringResource
import robithohapp.shared.generated.resources.Res
import robithohapp.shared.generated.resources.waktal_empty_subtitle
import robithohapp.shared.generated.resources.waktal_empty_title
import robithohapp.shared.generated.resources.waktal_header_title
import robithohapp.shared.generated.resources.waktal_search_placeholder
import robithohapp.shared.generated.resources.waktal_sync_downloading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaktalListContent(
    state: WaktalListUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectStatus: (WaktalStatus) -> Unit,
    onSelectProvince: (String?) -> Unit,
    onToggleSortByDistance: (Boolean) -> Unit,
    onResetFilters: () -> Unit,
    onWaktalClick: (Int) -> Unit,
    onTriggerSync: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val isRefreshing = state.syncState is WaktalSyncState.Syncing || state.syncState is WaktalSyncState.Checking
    var showFilterSheet by remember { mutableStateOf(false) }

    val activeFilterCount = (if (state.selectedStatus != WaktalStatus.SEMUA) 1 else 0) +
            (if (state.selectedProvince != null) 1 else 0) +
            (if (state.isSortByDistance) 1 else 0)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = if (isDark) DarkCanvas else PaperBackgroundLight,
        topBar = {
            IslamicHeader(
                title = stringResource(Res.string.waktal_header_title),
                subtitle = "",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onTriggerSync,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Search Input Field & Filter Button Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.waktal_search_placeholder),
                                color = if (isDark) Color(0xFFA1A1AA) else TextMuted,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Cari",
                                tint = if (isDark) Color(0xFFA1A1AA) else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (state.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Hapus Pencarian",
                                        tint = MerahMerdeka,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MerahMerdeka,
                            unfocusedBorderColor = if (isDark) DarkBorder else Color(0xFFE2E8F0),
                            focusedContainerColor = if (isDark) DarkSurface else Color.White,
                            unfocusedContainerColor = if (isDark) DarkSurface else Color.White,
                            focusedTextColor = if (isDark) Color.White else TextCharcoal,
                            unfocusedTextColor = if (isDark) Color.White else TextCharcoal
                        )
                    )

                    // Filter IconButton beside Search Input Field
                    Box(
                        modifier = Modifier.size(54.dp)
                    ) {
                        OutlinedIconButton(
                            onClick = { showFilterSheet = true },
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                1.dp,
                                if (activeFilterCount > 0) MerahMerdeka else if (isDark) DarkBorder else Color(0xFFE2E8F0)
                            ),
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                containerColor = if (activeFilterCount > 0) MerahMerdeka else if (isDark) DarkSurface else Color.White,
                                contentColor = if (activeFilterCount > 0) Color.White else if (isDark) Color.White else TextCharcoal
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Filter",
                                tint = if (activeFilterCount > 0) Color.White else if (isDark) Color.White else TextCharcoal,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        if (activeFilterCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(2.dp)
                                    .size(16.dp)
                                    .background(EmasKhidmat, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activeFilterCount.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Active Filters Chip Strip (if any filter is active)
                if (activeFilterCount > 0) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (state.selectedStatus != WaktalStatus.SEMUA) {
                            item {
                                FilterChip(
                                    selected = true,
                                    onClick = { onSelectStatus(WaktalStatus.SEMUA) },
                                    label = { Text("Status: ${state.selectedStatus.label} ✕", fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MerahMerdeka,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        if (state.isSortByDistance) {
                            item {
                                FilterChip(
                                    selected = true,
                                    onClick = { onToggleSortByDistance(false) },
                                    label = { Text("📍 Jarak Terdekat ✕", fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF2563EB),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        if (state.selectedProvince != null) {
                            item {
                                FilterChip(
                                    selected = true,
                                    onClick = { onSelectProvince(null) },
                                    label = { Text("Wilayah: ${state.selectedProvince} ✕", fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = EmasKhidmat,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        item {
                            FilterChip(
                                selected = false,
                                onClick = onResetFilters,
                                label = { Text("Reset Semua", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MerahMerdeka) }
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
                                text = stringResource(Res.string.waktal_sync_downloading),
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
                                text = stringResource(Res.string.waktal_empty_title),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else TextCharcoal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(Res.string.waktal_empty_subtitle),
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
                        itemsIndexed(
                            items = state.filteredItems,
                            key = { _, item -> item.id }
                        ) { index, item ->
                            WaktalCard(
                                item = item,
                                index = index,
                                onClick = { onWaktalClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        WaktalFilterSheet(
            selectedStatus = state.selectedStatus,
            selectedProvince = state.selectedProvince,
            isSortByDistance = state.isSortByDistance,
            availableProvinces = state.availableProvinces,
            onSelectStatus = onSelectStatus,
            onSelectProvince = onSelectProvince,
            onToggleSortByDistance = onToggleSortByDistance,
            onResetFilters = onResetFilters,
            onDismiss = { showFilterSheet = false }
        )
    }
}

@Preview
@Composable
private fun WaktalListContentPreview() {
    val sampleItems = listOf(
        WakilTalqin(
            id = 1,
            nomorUrut = 1,
            namaLengkap = "KH Baban Ahmad Jihad",
            namaResmi = "KH Baban Ahmad Jihad",
            gelarDepan = "KH",
            gelarBelakang = null,
            status = WaktalStatus.AKTIF,
            tahunWafat = null,
            nomorTelepon = "081234567890",
            nomorWhatsapp = "6281234567890",
            fotoUrl = null,
            negara = "Indonesia",
            provinsi = "Jawa Barat",
            kotaKabupaten = "Tasikmalaya",
            kecamatan = "Pageurageung",
            alamatLengkap = "PP Suryalaya",
            latitude = -7.1126,
            longitude = 108.2045,
            distanceKm = 12.5,
            majlisBinaan = "PP Suryalaya",
            biografiSingkat = "Pimpinan PP Suryalaya"
        ),
        WakilTalqin(
            id = 2,
            nomorUrut = 2,
            namaLengkap = "M Sholeh Mukhtar",
            namaResmi = "(ALM) KH M Sholeh Mukhtar",
            gelarDepan = "KH",
            gelarBelakang = null,
            status = WaktalStatus.WAFAT,
            tahunWafat = 2020,
            nomorTelepon = null,
            nomorWhatsapp = null,
            fotoUrl = null,
            provinsi = "DKI Jakarta",
            kotaKabupaten = "Jakarta Barat",
            kecamatan = null,
            alamatLengkap = null,
            latitude = null,
            longitude = null,
            majlisBinaan = null,
            biografiSingkat = null
        )
    )

    val sampleState = WaktalListUiState(
        searchQuery = "",
        selectedStatus = WaktalStatus.SEMUA,
        selectedProvince = null,
        isSortByDistance = false,
        availableProvinces = listOf("Jawa Barat", "DKI Jakarta", "Jawa Tengah"),
        items = sampleItems,
        filteredItems = sampleItems,
        isLoading = false,
        syncState = WaktalSyncState.Idle
    )

    RabithohTheme {
        WaktalListContent(
            state = sampleState,
            onSearchQueryChange = {},
            onSelectStatus = {},
            onSelectProvince = {},
            onToggleSortByDistance = {},
            onResetFilters = {},
            onWaktalClick = {},
            onTriggerSync = {},
            onBackClick = {}
        )
    }
}
