package com.iqbalwork.robithoh.feature.manaqib.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.LanguageTabSwitch
import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.core.designsystem.theme.BorderSubtle
import com.iqbalwork.robithoh.core.designsystem.theme.DarkSurface
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.EmasMuda
import com.iqbalwork.robithoh.core.designsystem.theme.GoldContainerLight
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PutihBersih
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.TextMuted
import com.iqbalwork.robithoh.feature.manaqib.model.McProgramItem

@Composable
fun McManaqibScreen(
    programs: List<McProgramItem>,
    selectedLanguage: LiturgyLanguage,
    onLanguageSelected: (LiturgyLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark
    val availableLanguages = listOf(LiturgyLanguage.INDONESIAN, LiturgyLanguage.SUNDANESE)
    val activeLang = if (selectedLanguage == LiturgyLanguage.ARABIC) LiturgyLanguage.INDONESIAN else selectedLanguage

    val isSunda = activeLang == LiturgyLanguage.SUNDANESE

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        item {
            LanguageTabSwitch(
                selectedLanguage = activeLang,
                onLanguageSelected = onLanguageSelected,
                languages = availableLanguages,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        item {
            SelectionContainer {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isDark) DarkSurface else Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header / Title
                    Text(
                        text = if (isSunda) "MC MANAQIB (BASA SUNDA)" else "MC MANAQIB (BAHASA INDONESIA)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    )

                    // Pembuka / Salam & Basmalah
                    Text(
                        text = if (isSunda) {
                            "Assalamualaikum warohmatulloh wabarokatuh. Bismillahirrohmanirrohim Alhamdulillahi robbil alamin Washsholatu wassalaamu ala syamsil mursalin wa qomarin nabiyyin wa sidroti muntahal ‘arifin sayidina Muhammadin wa ala alihi washohbihi ajma’in (ammaa ba’ad)."
                        } else {
                            "Assalamualaikum warohmatulloh wabarokatuh. Bismillahirrohmanirrohim Alhamdulillahi robbil alamin Washsholatu wassalaamu ala syamsil mursalin wa qomarin nabiyyin wa sidroti muntahal ‘arifin sayidina Muhammadin wa ala alihi washohbihi ajma’in amma ba’ad.."
                        },
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) PutihBersih else TextCharcoal
                    )

                    // 9 Kemenangan Intro
                    Text(
                        text = if (isSunda) {
                            "Pangajian anti gempa manaqib ieu dina raraga ngarayakeun 9 kamenangan :"
                        } else {
                            "Pengajian Anti Gempa, manaqiban kali ini dalam rangka merayakan 9 kemenangan :"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    )

                    // 9 Kemenangan Items
                    val kemenanganList = if (isSunda) {
                        listOf(
                            "La’alakum tattaquun",
                            "Nuzulul Qur’an",
                            "Lailatur Qodar",
                            "Idul Fitri",
                            "Minal ‘aa idiin wal faa iziin",
                            "Maulid sareng Maulud Nabi Muhammad SAW",
                            "Kajayaan Agama sareng Nagara (Kabinet Merah Putih)",
                            "Katahanan Nasional NKRI (Kabinet Merah Putih)",
                            "Peradaban Dunia (Kabinet Merah Putih)"
                        )
                    } else {
                        listOf(
                            "La’alakum Tattaqun",
                            "Nuzuulul Qur’an",
                            "Lailatul Qodar",
                            "I’dul fithri",
                            "Minal A’idin Wal Faizin",
                            "Maulid dan Maulud Nabi Muhammad SAW",
                            "Kejayaan Agama dan Negara (Kabinet Merah Putih)",
                            "Ketahanan Nasional NKRI (Kabinet Merah Putih)",
                            "Peradaban Dunia (Kabinet Merah putih)"
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        kemenanganList.forEachIndexed { index, itemText ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    color = if (isDark) MerahMarunGelap else MerahMerdeka.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDark) EmasMuda else MerahMerdeka
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = itemText,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    color = if (isDark) PutihBersih else TextCharcoal,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = if (isDark) BorderSubtle.copy(alpha = 0.3f) else BorderSubtle)

                    // Runtuyan Acara Header
                    Text(
                        text = if (isSunda) "Kalayan Acara Runtuyan Salajengna :" else "Dengan Acara Sebagai Berikut :",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    )

                    // Section A: Khidmat Amaliyah
                    Text(
                        text = if (isSunda) "A. Kalayan acara Khidmat ‘Amaliyah" else "A. Khidmat ‘Amaliyah",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    )

                    val amaliyahList = if (isSunda) {
                        listOf(
                            "Ngaoskeun ayat Al-Qur’an ku kersana : ……………………..",
                            "Ngaoskeun sholawat thoriqiyyah ku kersana : ……………………..",
                            "Ngaoskeun tanbih sareng teks pancasila ku kersana : ……………………..",
                            "Ngaoskeun tawassul ku kersana : ……………………..",
                            "Ngaoskeun Manqobah ku kersana : …………………….."
                        )
                    } else {
                        listOf(
                            "Pembacaan ayat Al-Qur’an oleh : ……………………..",
                            "Pembacaan Sholawat Thoriqiyyah oleh : ……………………..",
                            "Pembacaan Tanbih dengan Pancasila oleh : ……………………..",
                            "Pembacaan Tawassul oleh : ……………………..",
                            "Pembacaan Manqobah oleh : …………………….."
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        amaliyahList.forEachIndexed { index, itemText ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    color = if (isDark) MerahMarunGelap else MerahMerdeka.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDark) EmasMuda else MerahMerdeka
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = itemText,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    color = if (isDark) PutihBersih else TextCharcoal,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Section B
                    if (!isSunda) {
                        Text(
                            text = "B. (Jika ada) Sambutan oleh : ……………………..",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) PutihBersih else TextCharcoal
                        )
                    }

                    // Section B (Sunda) / Section C (Indo) Tabaruk
                    Text(
                        text = if (isSunda) "B. Tabaruk Kitab Fadhoilussyuhuur ku kersana : …………………….. (upami aya)" else "C. (Jika ada) Tabaruk Kitab Fadhoilussyuhuur disampaikan oleh : ……………………..",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) PutihBersih else TextCharcoal
                    )

                    // Section C (Sunda) / Section D (Indo) Khidmat Ilmiyyah
                    Text(
                        text = if (isSunda) "C. Khidmat ‘Ilmiyyah disanggakeun ku :" else "D. Khidmat ‘Ilmiyyah disampaikan oleh :",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "1. ……………………..",
                            "2. ……………………..",
                            "3. Pangersa Abah Aos"
                        ).forEach {
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                fontWeight = if (it.contains("Pangersa Abah")) FontWeight.Bold else FontWeight.Normal,
                                color = if (it.contains("Pangersa Abah")) (if (isDark) EmasMuda else MerahMarunGelap) else (if (isDark) PutihBersih else TextCharcoal),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }

                    // Section E Penutup
                    Text(
                        text = if (isSunda) "E. Ditutup ku aosan Bani Hasyim (3x) sareng Sholawat Badriyyah" else "E. Sholawat Bani Hasyim (3x) dilanjutkan dengan Sholawat Badriyyah",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    )

                    Text(
                        text = if (isSunda) "Kanggo para petugas disanggakeun sacara estafet." else "Kepada para petugas dipersilahkan secara estafet.",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = if (isDark) PutihBersih.copy(alpha = 0.8f) else TextMuted
                    )

                    Text(
                        text = if (isSunda) "Wassalamu’alaikum warohmatulloh wabarokatuh." else "Wassalamu’alaikum warohmatulloh wabarokatuh.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) EmasMuda else MerahMarunGelap
                    )

                    // Catatan Box
                    Surface(
                        color = if (isDark) MerahMarunGelap.copy(alpha = 0.3f) else GoldContainerLight.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, if (isDark) EmasMuda.copy(alpha = 0.3f) else EmasKhidmat.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📌", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isSunda) "Catetan Penting Pangersa Abah" else "Catatan Penting Pangersa Abah",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isDark) EmasMuda else MerahMarunGelap
                                )
                            }
                            Text(
                                text = if (isSunda) {
                                    """#UlahDirobahDitambahUlahDikurangan
#SapertosAnuParantosDimaklumatkeunKuPangersaAbah

Upami shohibul hajat hoyong disebat hajatna anu seueur…. Dugikeun wae ku MC sateuacan dibuka acara manaqib.

Dawuhan Pangersa Abah : Nyebatkeun sacara lisan hiji-hiji hajat urang, hartosna ngadikte اللّه, nganggep اللّه teu uninga kana hajat urang. Parantos…. Cekap dina manah wae اللّه parantos MAHA UNINGA. Sanaos teu disebat oge اللّه Maha Uninga.

* Upami aya sambutan, sateuacan dibuka acara atanapi saparantos do’a manaqib sateuacan khidmah ‘ilmiyyah… Sareng maksimal 5 menit wae *"""
                                } else {
                                    """#JanganDirobahDitambahJanganDikurang
#SepertiYangSudahDiMaklumatkanOlehPangersaAbah

Jika shohibul hajat ingin disebut hajatnya yang banyak…. Disampaikan saja oleh MC sebelum dibuka acara manaqib.

Kata Pangersa Abah : Menyebut secara lisan satu persatu hajat kita, berarti mendikte اللّه, menganggap اللّه tidak tahu hajat kita. Sudah…. Cukup di hati saja اللّه sudah MAHA TAHU. Bahkan tidak disebut pun اللّه Maha Tahu.

* Jika ada sambutan, sebelum dibuka acara atau setelah do’a manaqib sebelum khidmah ‘ilmiyyah… Dan maksimal 5 menit saja *"""
                                },
                                fontSize = 13.sp,
                                lineHeight = 21.sp,
                                color = if (isDark) PutihBersih.copy(alpha = 0.9f) else TextCharcoal,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}
}
