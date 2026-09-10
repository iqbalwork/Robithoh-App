package com.iqbalwork.robithoh.feature.tasbih.presentation

import com.iqbalwork.robithoh.core.presentation.UiEffect
import com.iqbalwork.robithoh.core.presentation.UiIntent
import com.iqbalwork.robithoh.core.presentation.UiState
import kotlinx.serialization.Serializable

@Serializable
data class TasbihDzikirPreset(
    val id: String,
    val title: String,
    val arabic: String,
    val defaultTarget: Int,
    val virtue: String
)

data class TasbihUiState(
    val currentCount: Int = 0,
    val targetCount: Int = 165,
    val lapCount: Int = 0,
    val totalCount: Int = 0,
    val selectedDzikirId: String = "dzikir_jahr",
    val selectedDzikirTitle: String = "Dzikir Jahr",
    val selectedDzikirArabic: String = "لَا إِلَهَ إِلَّا اللَّهُ",
    val isHapticEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val isTargetReached: Boolean = false,
    val showResetDialog: Boolean = false,
    val showCustomTargetDialog: Boolean = false,
    val isFloatingExpanded: Boolean = false,
    val isFloatingVisible: Boolean = true,
    val scaleFactor: Float = 1.0f,
    val availablePresets: List<TasbihDzikirPreset> = defaultTasbihPresets
) : UiState

val defaultTasbihPresets: List<TasbihDzikirPreset> = listOf(
    TasbihDzikirPreset(
        id = "tahlil_tqn",
        title = "Tahlil",
        arabic = "لَا إِلٰهَ إِلَّا اللَّهُ",
        defaultTarget = 165,
        virtue = "Pengagungan kebesaran Allah di atas seluruh alam."
    ),
    TasbihDzikirPreset(
        id = "tasbih_tahmid",
        title = "Tasbih & Tahmid",
        arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
        defaultTarget = 33,
        virtue = "Kalimat yang ringan di lisan namun berat di timbangan mizan."
    ),
    TasbihDzikirPreset(
        id = "hauqolah",
        title = "Hauqolah",
        arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
        defaultTarget = 100,
        virtue = "Simpanan perbendaharaan surga dan penolak 99 pintu kesusahan."
    ),
    TasbihDzikirPreset(
        id = "shalawat_munjiyat",
        title = "Shalawat Munjiyat",
        arabic = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ صَلَاةً تُنْجِينَا بِهَا مِنْ جَمِيعِ الْأَهْوَالِ وَالْآفَاتِ",
        defaultTarget = 100,
        virtue = "Penyelamat dari segala marabahaya dan pemenuh segala hajat."
    ),
    TasbihDzikirPreset(
        id = "istighfar_tqn",
        title = "Istighfar",
        arabic = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ",
        defaultTarget = 165,
        virtue = "Pelebur noda dosa dan pembuka pintu ampunan Ilahi."
    ),
    TasbihDzikirPreset(
        id = "shalawat_bani_hasyim",
        title = "Shalawat Bani Hasyim",
        arabic = "اللَّهُمَّ صَلِّ عَلَى النَّبِيِّ الْهَاشِمِيِّ مُحَمَّدٍ وَعَلَى آلِهِ وَسَلِّمْ تَسْلِيمًا",
        defaultTarget = 165,
        virtue = "Shalawat kebanggaan ikhwan dan akhwat pembuka futuh dan mahabbah Rasulullah."
    ),
    TasbihDzikirPreset(
        id = "wirid_kemalaikatan_ahad",
        title = "Wirid Kemalaikatan: Ahad",
        arabic = "حَيٌّ قَيُّومٌ",
        defaultTarget = 222,
        virtue = "HAYYUN QOYYUUM (222x) • Malaikat Syamsayaa Yayil • Setiap Sabtu malam bagi lahir hari Ahad."
    ),
    TasbihDzikirPreset(
        id = "wirid_kemalaikatan_senin",
        title = "Wirid Kemalaikatan: Senin",
        arabic = "رَحْمٰنُ الرَّحِيمُ",
        defaultTarget = 333,
        virtue = "ROHMAANUR ROHIIM (333x) • Malaikat Karmayaa Yayil • Setiap Minggu malam bagi lahir hari Senin."
    ),
    TasbihDzikirPreset(
        id = "wirid_kemalaikatan_selasa",
        title = "Wirid Kemalaikatan: Selasa",
        arabic = "مَلِكٌ قُدُّوسٌ",
        defaultTarget = 444,
        virtue = "MALIKUN QUDDUUSUN (444x) • Malaikat Ruuhayaa Yayil • Setiap Senin malam bagi lahir hari Selasa."
    ),
    TasbihDzikirPreset(
        id = "wirid_kemalaikatan_rabu",
        title = "Wirid Kemalaikatan: Rabu",
        arabic = "كَبِيرٌ مُتَعَالٌ",
        defaultTarget = 555,
        virtue = "KABIIRUN MUTA'AL (555x) • Malaikat Ruuqoyaa Yayil • Setiap Selasa malam bagi lahir hari Rabu."
    ),
    TasbihDzikirPreset(
        id = "wirid_kemalaikatan_kamis",
        title = "Wirid Kemalaikatan: Kamis",
        arabic = "فَتَّاحٌ رَزَّاقٌ",
        defaultTarget = 666,
        virtue = "PATTAAHUN ROZZAKUN (666x) • Malaikat Miikayaa Yayil • Setiap Rabu malam bagi lahir hari Kamis."
    ),
    TasbihDzikirPreset(
        id = "wirid_kemalaikatan_jumat",
        title = "Wirid Kemalaikatan: Jum'at",
        arabic = "شَدِيدٌ ذُو قُوَّةٍ",
        defaultTarget = 777,
        virtue = "SYADIIDUN DZUUQUWWATIN (777x) • Malaikat Jabroyaa Yayil • Setiap Kamis malam bagi lahir hari Jum'at."
    ),
    TasbihDzikirPreset(
        id = "wirid_kemalaikatan_sabtu",
        title = "Wirid Kemalaikatan: Sabtu",
        arabic = "قَوِيٌّ قَدِيرٌ",
        defaultTarget = 888,
        virtue = "QOWIYYUN QODIIRUN (888x) • Malaikat Lathofa Yaa Yayil • Setiap Jum'at malam bagi lahir hari Sabtu."
    )
)

sealed interface TasbihUiIntent : UiIntent {
    data object Increment : TasbihUiIntent
    data object Decrement : TasbihUiIntent
    data object RequestReset : TasbihUiIntent
    data object ConfirmReset : TasbihUiIntent
    data object DismissResetDialog : TasbihUiIntent
    data class SetTarget(val target: Int) : TasbihUiIntent
    data object ShowCustomTargetDialog : TasbihUiIntent
    data object DismissCustomTargetDialog : TasbihUiIntent
    data class SelectDzikir(val preset: TasbihDzikirPreset) : TasbihUiIntent
    data object ToggleHaptic : TasbihUiIntent
    data object ToggleSound : TasbihUiIntent
    data object DismissTargetReached : TasbihUiIntent
    data class LoadProgress(val dzikirId: String) : TasbihUiIntent
    data object ToggleFloatingExpand : TasbihUiIntent
    data class SetFloatingExpanded(val expanded: Boolean) : TasbihUiIntent
    data class SetFloatingVisible(val visible: Boolean) : TasbihUiIntent
    data object ReloadPresets : TasbihUiIntent
    data class SyncData(
        val count: Int,
        val target: Int? = null,
        val dzikirTitle: String? = null
    ) : TasbihUiIntent
}

sealed interface TasbihUiEffect : UiEffect {
    data object TriggerHapticTap : TasbihUiEffect
    data object TriggerHapticMilestone : TasbihUiEffect
    data object PlayClickChime : TasbihUiEffect
    data object PlayMilestoneChime : TasbihUiEffect
    data class ShowMilestoneToast(val count: Int, val target: Int) : TasbihUiEffect
}
