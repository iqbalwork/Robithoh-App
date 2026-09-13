package com.iqbalwork.robithoh.feature.waktal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkMuted
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.SlateCharcoalText
import com.iqbalwork.robithoh.core.designsystem.theme.SlateMuted
import com.iqbalwork.robithoh.feature.waktal.domain.WaktalStatus
import org.jetbrains.compose.resources.stringResource
import robithohapp.shared.generated.resources.Res
import robithohapp.shared.generated.resources.waktal_filter_all_provinces
import robithohapp.shared.generated.resources.waktal_filter_apply
import robithohapp.shared.generated.resources.waktal_filter_nearest
import robithohapp.shared.generated.resources.waktal_filter_province
import robithohapp.shared.generated.resources.waktal_filter_reset
import robithohapp.shared.generated.resources.waktal_filter_sort
import robithohapp.shared.generated.resources.waktal_filter_status
import robithohapp.shared.generated.resources.waktal_filter_subtitle
import robithohapp.shared.generated.resources.waktal_filter_title

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WaktalFilterSheet(
    selectedStatus: WaktalStatus,
    selectedProvince: String?,
    isSortByDistance: Boolean,
    availableProvinces: List<String>,
    onSelectStatus: (WaktalStatus) -> Unit,
    onSelectProvince: (String?) -> Unit,
    onToggleSortByDistance: (Boolean) -> Unit,
    onResetFilters: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val isDark = RabithohTheme.colors.isDark
    val hasActiveFilters = selectedStatus != WaktalStatus.SEMUA ||
            selectedProvince != null ||
            isSortByDistance

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) Color(0xFF1E1B1B) else Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            // Header Row: Title + Reset Button + Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.waktal_filter_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) PutihBersih else SlateCharcoalText
                    )
                    Text(
                        text = stringResource(Res.string.waktal_filter_subtitle),
                        fontSize = 12.sp,
                        color = if (isDark) DarkMuted else SlateMuted
                    )
                }

                if (hasActiveFilters) {
                    TextButton(onClick = onResetFilters) {
                        Text(
                            text = stringResource(Res.string.waktal_filter_reset),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MerahMerdeka
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = if (isDark) DarkMuted else SlateMuted
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = if (isDark) DarkBorder else Color(0xFFE2E8F0)
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Status Filter Section
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(Res.string.waktal_filter_status),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) PutihBersih else SlateCharcoalText
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        WaktalStatus.entries.forEach { status ->
                            val selected = selectedStatus == status
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectStatus(status) },
                                label = {
                                    Text(
                                        text = status.label,
                                        fontSize = 12.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MerahMerdeka,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // 2. Sort Section
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(Res.string.waktal_filter_sort),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) PutihBersih else SlateCharcoalText
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = isSortByDistance,
                            onClick = { onToggleSortByDistance(!isSortByDistance) },
                            label = {
                                Text(
                                    text = stringResource(Res.string.waktal_filter_nearest),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSortByDistance) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2563EB),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // 3. Wilayah (Provinsi) Section
                if (availableProvinces.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = stringResource(Res.string.waktal_filter_province),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) PutihBersih else SlateCharcoalText
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val isAllProvincesSelected = selectedProvince == null
                            FilterChip(
                                selected = isAllProvincesSelected,
                                onClick = { onSelectProvince(null) },
                                label = {
                                    Text(
                                        text = stringResource(Res.string.waktal_filter_all_provinces),
                                        fontSize = 12.sp,
                                        fontWeight = if (isAllProvincesSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmasKhidmat,
                                    selectedLabelColor = Color.White
                                )
                            )

                            availableProvinces.forEach { province ->
                                val selected = selectedProvince == province
                                FilterChip(
                                    selected = selected,
                                    onClick = { onSelectProvince(if (selected) null else province) },
                                    label = {
                                        Text(
                                            text = province,
                                            fontSize = 12.sp,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = EmasKhidmat,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Apply Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MerahMerdeka,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(Res.string.waktal_filter_apply),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
