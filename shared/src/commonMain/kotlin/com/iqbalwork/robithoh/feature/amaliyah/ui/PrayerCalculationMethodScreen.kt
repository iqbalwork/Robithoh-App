package com.iqbalwork.robithoh.feature.amaliyah.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerCalculationMethodItem
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerCalculationMethods
import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerCalculationMethodScreen(
    selectedMethod: PrayerCalculationMethodItem,
    onSelectMethod: (PrayerCalculationMethodItem) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBack()
    }
    val isDark = RabithohTheme.colors.isDark
    val methods = PrayerCalculationMethods.ALL_METHODS

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perhitungan Waktu Salat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isDark) PutihBersih else TextCharcoal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "✕",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) PutihBersih else TextCharcoal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) DarkSurface else PureWhite
                )
            )
        },
        containerColor = if (isDark) DarkCanvas else PureWhite,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(methods) { method ->
                val isSelected = method.id.equals(selectedMethod.id, ignoreCase = true)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectMethod(method)
                            onBack()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = method.name,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) MerahMerdeka else (if (isDark) PutihBersih else TextCharcoal)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = method.description,
                                fontSize = 12.sp,
                                color = if (isDark) DarkMuted else TextMuted
                            )
                        }

                        if (isSelected) {
                            Text(
                                text = "✓",
                                color = MerahMerdeka,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    HorizontalDivider(
                        color = if (isDark) DarkBorder.copy(alpha = 0.5f) else BorderSubtle.copy(alpha = 0.6f),
                        thickness = 0.75.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}
