package com.iqbalwork.robithoh.feature.waktal.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.IslamicHeader
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.DarkCanvas
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted
import com.iqbalwork.robithoh.feature.waktal.presentation.WaktalDetailUiState
import com.iqbalwork.robithoh.feature.waktal.ui.components.WaktalAvatar
import com.iqbalwork.robithoh.feature.waktal.ui.components.WaktalQuickActions
import com.iqbalwork.robithoh.feature.waktal.ui.components.WaktalStatusBadge
import org.jetbrains.compose.resources.stringResource
import robithohapp.shared.generated.resources.Res
import robithohapp.shared.generated.resources.waktal_biografi_title
import robithohapp.shared.generated.resources.waktal_detail_subtitle
import robithohapp.shared.generated.resources.waktal_domisili_title
import robithohapp.shared.generated.resources.waktal_majlis_title
import robithohapp.shared.generated.resources.waktal_no_urut

@Composable
fun WaktalDetailContent(
    state: WaktalDetailUiState,
    onDialPhone: () -> Unit,
    onOpenWhatsApp: () -> Unit,
    onOpenMap: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val item = state.waktal

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = if (isDark) DarkCanvas else PaperBackgroundLight,
        topBar = {
            IslamicHeader(
                title = item?.namaResmi ?: "Detail Profil Ulama",
                subtitle = stringResource(Res.string.waktal_detail_subtitle),
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MerahMerdeka)
            }
        } else if (item == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.errorMessage ?: "Data Wakil Talqin tidak ditemukan.",
                    color = if (isDark) Color.White else TextCharcoal,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Portrait Header & Name Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) DarkSurface else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            WaktalAvatar(
                                url = item.normalizedFotoUrl,
                                sizeDp = 80,
                                emojiSizeSp = 40
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = item.namaResmi,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else TextCharcoal
                            )

                            item.nomorUrut?.let { urut ->
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stringResource(Res.string.waktal_no_urut, urut),
                                    fontSize = 12.sp,
                                    color = MerahMerdeka,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            WaktalStatusBadge(
                                status = item.status,
                                tahunWafat = item.tahunWafat
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Quick Action Bar
                item {
                    val hasPhone = !item.nomorTelepon.isNullOrBlank()
                    val hasWa = !item.nomorWhatsapp.isNullOrBlank()
                    val hasCoords = item.latitude != null && item.longitude != null

                    if (hasPhone || hasWa || hasCoords) {
                        WaktalQuickActions(
                            hasPhone = hasPhone,
                            hasWhatsapp = hasWa,
                            hasCoordinates = hasCoords,
                            onDialPhone = onDialPhone,
                            onOpenWhatsApp = onOpenWhatsApp,
                            onOpenMap = onOpenMap
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Info Section Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) DarkSurface else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.waktal_domisili_title),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MerahMerdeka
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            InfoRow(label = "Wilayah", value = "${item.kotaKabupaten}, ${item.provinsi}")

                            item.kecamatan?.takeIf { it.isNotBlank() }?.let { kec ->
                                InfoRow(label = "Kecamatan", value = kec)
                            }

                            item.alamatLengkap?.takeIf { it.isNotBlank() }?.let { alamat ->
                                InfoRow(label = "Alamat Lengkap", value = alamat)
                            }

                            item.majlisBinaan?.takeIf { it.isNotBlank() }?.let { majlis ->
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = if (isDark) DarkBorder else Color(0xFFF1F5F9)
                                )
                                Text(
                                    text = stringResource(Res.string.waktal_majlis_title),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MerahMerdeka
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = majlis,
                                    fontSize = 14.sp,
                                    color = if (isDark) Color.White else TextCharcoal
                                )
                            }

                            item.biografiSingkat?.takeIf { it.isNotBlank() }?.let { bio ->
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = if (isDark) DarkBorder else Color(0xFFF1F5F9)
                                )
                                Text(
                                    text = stringResource(Res.string.waktal_biografi_title),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MerahMerdeka
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = bio,
                                    fontSize = 13.sp,
                                    color = if (isDark) Color.White else TextCharcoal,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    val isDark = RabithohTheme.colors.isDark
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isDark) Color(0xFFA1A1AA) else TextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDark) Color.White else TextCharcoal
        )
    }
}
