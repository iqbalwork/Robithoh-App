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
    val targetCount: Int = 33,
    val lapCount: Int = 0,
    val totalCount: Int = 0,
    val selectedDzikirId: String = "dzikir_nafi_itsbat",
    val selectedDzikirTitle: String = "Dzikir Nafi Itsbat (TQN)",
    val selectedDzikirArabic: String = "لَا إِلَهَ إِلَّا اللَّهُ",
    val isHapticEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val isTargetReached: Boolean = false,
    val showResetDialog: Boolean = false,
    val showCustomTargetDialog: Boolean = false,
    val scaleFactor: Float = 1.0f,
    val availablePresets: List<TasbihDzikirPreset> = listOf(
        TasbihDzikirPreset(
            id = "dzikir_nafi_itsbat",
            title = "Dzikir Nafi Itsbat (TQN)",
            arabic = "لَا إِلَهَ إِلَّا اللَّهُ",
            defaultTarget = 165,
            virtue = "Dzikir Jahr standar TQN Sirnarasa 38 ba'da sholat maktubah."
        ),
        TasbihDzikirPreset(
            id = "tasbih_subhanallah",
            title = "Tasbih (Subhanallah)",
            arabic = "سُبْحَانَ اللَّهِ",
            defaultTarget = 33,
            virtue = "Penyucian Dzat Allah SWT dari segala kekurangan."
        ),
        TasbihDzikirPreset(
            id = "tahmid_alhamdulillah",
            title = "Tahmid (Alhamdulillah)",
            arabic = "الْحَمْدُ لِلَّهِ",
            defaultTarget = 33,
            virtue = "Pujian syukur atas limpahan nikmat dan karunia."
        ),
        TasbihDzikirPreset(
            id = "takbir_allahuakbar",
            title = "Takbir (Allahu Akbar)",
            arabic = "اللَّهُ أَكْبَرُ",
            defaultTarget = 33,
            virtue = "Pengagungan kebesaran Allah di atas seluruh alam."
        ),
        TasbihDzikirPreset(
            id = "istighfar_tqn",
            title = "Istighfar",
            arabic = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ",
            defaultTarget = 100,
            virtue = "Pelebur noda dosa dan pembuka pintu ampunan Ilahi."
        ),
        TasbihDzikirPreset(
            id = "shalawat_bani_hasyim",
            title = "Shalawat Bani Hasyim",
            arabic = "اللَّهُمَّ صَلِّ عَلَى النَّبِيِّ الْهَاشِمِيِّ مُحَمَّدٍ وَعَلَى آلِهِ وَسَلِّمْ تَسْلِيمًا",
            defaultTarget = 100,
            virtue = "Shalawat kebanggaan ikhwan TQN pembuka futuh dan mahabbah Rasulullah."
        )
    )
) : UiState

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
}

sealed interface TasbihUiEffect : UiEffect {
    data object TriggerHapticTap : TasbihUiEffect
    data object TriggerHapticMilestone : TasbihUiEffect
    data object PlayClickChime : TasbihUiEffect
    data object PlayMilestoneChime : TasbihUiEffect
    data class ShowMilestoneToast(val count: Int, val target: Int) : TasbihUiEffect
}
