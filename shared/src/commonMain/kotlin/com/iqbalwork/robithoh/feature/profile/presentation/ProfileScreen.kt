package com.iqbalwork.robithoh.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.*
import com.iqbalwork.robithoh.core.designsystem.theme.*
import com.iqbalwork.robithoh.appVersionName
import com.iqbalwork.robithoh.navigation.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }
    val uriHandler = LocalUriHandler.current
    val isDark = RabithohTheme.colors.isDark

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            IslamicHeader(
                title = "Tentang Aplikasi",
                subtitle = "Roudloh Merah Putih MTQN Suryalaya Sirnarasa PPKN III",
                arabicTitle = "عَنْ التَّطْبِيقِ",
                onBackClick = onBackClick
            )
        },
        containerColor = if (isDark) DarkCanvas else PutihAbuBackground
    ) { paddingValues ->
        SelectionContainer {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
            ) {
                // 1. Hero Card: Robithoh App
                item {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.CRIMSON_HERO,
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        Text(
                            text = "ROBITHOH",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PutihBersih,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Aplikasi Ibadah & Amaliyah Roudloh Merah Putih MTQN Suryalaya Sirnarasa PPKN III",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = EmasMuda,
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Dirancang dengan arsitektur 100% Offline-First, bebas iklan, dan tanpa pelacakan data demi kekhusyukan beribadah seluruh ikhwan dan akhwat.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PutihBersih.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                color = Color.Black.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "✨ 100% Offline",
                                    color = PutihBersih,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color.Black.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "🔒 Bebas Iklan",
                                    color = PutihBersih,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color.Black.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "📱 Multiplatform",
                                    color = PutihBersih,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.35f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        uriHandler.openUri("https://iqbalwork.github.io/Robithoh-Landing/")
                                    } catch (_: Exception) {
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🌐 Website Resmi Portal Web",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmasMuda
                                )
                                Text(
                                    text = "Buka Portal ›",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmasMuda
                                )
                            }
                        }
                    }
                }

                // 2. Penghormatan Khusus: Pangersa Guru Mursyid
                item {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.GOLD_TINTED,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = if (isDark) Color(0xFF422E1A) else Color(0xFFFFF3CD),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Penghormatan Khusus",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmasKhidmat,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Text(
                                text = "Mursyid Ke-38",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) EmasMuda else MerahMarunGelap
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Syaikh Muhammad Abdul Gaos Saefulloh Maslul Ra. Qs.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) PutihBersih else SlateCharcoalText,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = "Pangersa Abah Aos — Guru Mursyid Roudloh Merah Putih MTQN Suryalaya Sirnarasa PPKN III (Silsilah Ke-38)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = EmasKhidmat,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "\"Rasa syukur dan terima kasih tak terhingga kami haturkan kepada Pangersa Guru Mursyid Syaikh Muhammad Abdul Gaos Saefulloh Maslul Ra. Qs. (Abah Aos) atas limpahan karomah, doa, restu, dan bimbingan ruhani yang senantiasa menaungi para murid.\"",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isDark) PutihBersih.copy(alpha = 0.9f) else SlateCharcoalText,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                fontStyle = FontStyle.Italic
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Aplikasi Robithoh ini didedikasikan secara tulus sebagai washilah kemudahan mengamalkan Dzikir, Kitab Manaqib, dan seluruh Amaliyah Roudloh Merah Putih MTQN Suryalaya Sirnarasa PPKN III bagi ikhwan dan akhwat di seluruh penjuru dunia.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isDark) DarkMuted else SlateMuted,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                            color = if (isDark) Color(0xFF382C22) else Color(0xFFF0E5D3)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        uriHandler.openUri("https://www.instagram.com/abahaos38/")
                                    } catch (_: Exception) {
                                    }
                                }
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📸 Instagram Resmi Pangersa Abah Aos",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) EmasKhidmat else Color(0xFFC13584)
                            )
                            Text(
                                text = "@abahaos38 ›",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) EmasKhidmat else Color(0xFFC13584)
                            )
                        }
                    }
                }

                item {
                    IslamicDivider(motif = IslamicDividerMotif.RUB_EL_HIZB)
                }

                // 3. Pembimbing & Penasihat Title
                item {
                    Text(
                        text = "PEMBIMBING & PENASIHAT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkMuted else TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                // 3a. Eyang Epi Ruhiat Ganefi
                item {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.GOLD_BORDER,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Eyang Epi Ruhiat Ganefi",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) PutihBersih else SlateCharcoalText,
                                        fontSize = 14.sp
                                    )
                                )
                                Text(
                                    text = "Pembimbing Padepokan Talangraga · Wakil Talqin Abah Aos",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EmasKhidmat,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isDark) Color(0xFF382C1B) else Color(0xFFFFF8E1)
                            ) {
                                Text(
                                    text = "Pembimbing & Penasihat",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmasKhidmat,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pembimbing Roudhoh Padepokan Talangraga Ponpes Suryalaya Sirnarasa yang bertempat di Indihiang (Tasikmalaya), sekaligus Wakil Talqin Pangersa Abah Aos. Beliau senantiasa memberikan arahan, restu, dan bimbingan amaliyah agar seluruh kaifiyat dzikir dalam aplikasi Robithoh senantiasa terjaga kemurniannya dan muttashil dengan tuntunan Guru Mursyid.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isDark) DarkMuted else SlateMuted,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }

                // 3b. Dr. Eko Yulianto
                item {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.GOLD_BORDER,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Dr. Eko Yulianto",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) PutihBersih else SlateCharcoalText,
                                        fontSize = 14.sp
                                    )
                                )
                                Text(
                                    text = "Wakil Talqin Pangersa Abah Aos · Didikan Eyang Epi",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EmasKhidmat,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isDark) Color(0xFF382C1B) else Color(0xFFFFF8E1)
                            ) {
                                Text(
                                    text = "Pembimbing & Penasihat",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmasKhidmat,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Wakil Talqin Pangersa Abah Aos yang juga merupakan bagian dari didikan Eyang Epi Ruhiat Ganefi di Padepokan Talangraga Indihiang. Beliau bertindak sebagai pembimbing dan penasihat yang memverifikasi keaslian naskah Kitab Manaqib 1–56, teks sanad Tawassul Silsilah, susunan MC Manaqib, serta rujukan liturgi amaliyah dalam aplikasi Robithoh.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isDark) DarkMuted else SlateMuted,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }

                // 4. Pengembang Aplikasi Title
                item {
                    Text(
                        text = "PENGEMBANG APLIKASI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkMuted else TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                // 4a. Iqbal Fauzi Card
                item {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.SURFACE_CLEAN,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Iqbal Fauzi",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) PutihBersih else SlateCharcoalText,
                                        fontSize = 14.sp
                                    )
                                )
                                Text(
                                    text = "Creator & Lead Software Engineer · Didikan Eyang Epi",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isDark) Color(0xFFFF8A80) else MerahMerdeka,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isDark) Color(0xFF4A151D) else Color(0xFFFFEBEE)
                            ) {
                                Text(
                                    text = "Pengembang",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFFFF8A80) else MerahMerdeka,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Software engineer yang juga merupakan bagian dari didikan Eyang Epi Ruhiat Ganefi di Padepokan Talangraga Indihiang. Merancang arsitektur, mengembangkan, dan memelihara aplikasi Robithoh secara multiplatform (Kotlin Multiplatform & Compose Multiplatform) sebagai dedikasi teknologi untuk kemaslahatan ikhwan dan akhwat.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isDark) DarkMuted else SlateMuted,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                            color = if (isDark) Color(0xFF2E2727) else Color(0xFFF1F5F9)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        uriHandler.openUri("https://iqbalwork.github.io/")
                                    } catch (_: Exception) {
                                    }
                                }
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌐 Portofolio Pengembang",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) EmasKhidmat else MerahMerdeka
                            )
                            Text(
                                text = "iqbalwork.github.io ›",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) EmasKhidmat else MerahMerdeka
                            )
                        }
                    }
                }

                // 5. Informasi Aplikasi
                item {
                    Text(
                        text = "INFORMASI APLIKASI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkMuted else TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                item {
                    GoldCrimsonCard(
                        variant = GoldCrimsonCardVariant.SURFACE_CLEAN,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Versi Aplikasi",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) PutihBersih else SlateCharcoalText,
                                    fontSize = 14.sp
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isDark) Color(0xFF382C1B) else Color(0xFFFFF8E1)
                            ) {
                                Text(
                                    text = appVersionName(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmasKhidmat,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
