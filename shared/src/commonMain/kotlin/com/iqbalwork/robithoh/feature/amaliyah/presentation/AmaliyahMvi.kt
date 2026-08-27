package com.iqbalwork.robithoh.feature.amaliyah.presentation

import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.core.presentation.UiEffect
import com.iqbalwork.robithoh.core.presentation.UiIntent
import com.iqbalwork.robithoh.core.presentation.UiState
import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.model.*

data class AmaliyahUiState(
    val selectedLanguage: LiturgyLanguage = LiturgyLanguage.ARABIC,
    val selectedCategory: AmaliyahCategory = AmaliyahCategory.DZIKIR_BA_DA_SHOLAT,
    val activeDzikirType: DzikirType = DzikirType.JAHR,
    val dzikirJahrList: List<DzikirItem> = emptyList(),
    val dzikirKhofiList: List<DzikirItem> = emptyList(),
    val dailyPrayersList: List<SpecialPrayer> = emptyList(),
    val hijriyahList: List<HijriyahAmaliyah> = emptyList(),
    val sholatSunnahList: List<SpecialPrayer> = emptyList(),
    val prayerSchedule: PrayerSchedule? = null,
    val nextPrayerCountdown: NextPrayerCountdown? = null,
    val qiblaInfo: QiblaInfo? = null,
    val locationPresets: List<LocationPreset> = PrayerTimesCalculator.PRESET_LOCATIONS,
    val selectedLocation: LocationPreset = PrayerTimesCalculator.DEFAULT_LOCATION,
    val selectedCalculationMethod: PrayerCalculationMethodItem = PrayerCalculationMethods.DEFAULT,
    val prayerAdjustments: PrayerTimeAdjustments = PrayerTimeAdjustments(),
    val availableCalculationMethods: List<PrayerCalculationMethodItem> = PrayerCalculationMethods.ALL_METHODS,
    val activeAdjustmentPrayerType: PrayerType? = null,
    val isGpsActive: Boolean = false,
    val isFetchingLocation: Boolean = false,
    val locationErrorMessage: String? = null,
    val expandedItemId: String? = null,
    val selectedDateOffsetDays: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState

sealed interface AmaliyahUiIntent : UiIntent {
    data class SelectLanguage(val language: LiturgyLanguage) : AmaliyahUiIntent
    data class SelectCategory(val category: AmaliyahCategory) : AmaliyahUiIntent
    data class SelectDzikirType(val dzikirType: DzikirType) : AmaliyahUiIntent
    data class SelectLocation(val location: LocationPreset) : AmaliyahUiIntent
    data class SetGpsLocation(val location: com.iqbalwork.robithoh.core.location.UserLocation) : AmaliyahUiIntent
    data class SetFetchingLocation(val isFetching: Boolean) : AmaliyahUiIntent
    data class SetLocationError(val message: String?) : AmaliyahUiIntent
    data class SelectCalculationMethod(val method: PrayerCalculationMethodItem) : AmaliyahUiIntent
    data class UpdatePrayerAdjustment(val prayerType: PrayerType, val offsetMinutes: Int) : AmaliyahUiIntent
    data class OpenAdjustmentPicker(val prayerType: PrayerType) : AmaliyahUiIntent
    data object CloseAdjustmentPicker : AmaliyahUiIntent
    data class ToggleExpandItem(val itemId: String) : AmaliyahUiIntent
    data class TickCountdown(val hour: Int, val minute: Int, val second: Int) : AmaliyahUiIntent
    data object RefreshSchedule : AmaliyahUiIntent
    data class ChangeDateOffset(val deltaDays: Int) : AmaliyahUiIntent
    data object ResetDateOffset : AmaliyahUiIntent
    data class OpenTasbihWithTarget(val targetCount: Int, val dzikirTitle: String) : AmaliyahUiIntent
}

sealed interface AmaliyahUiEffect : UiEffect {
    data class NavigateToTasbih(val targetCount: Int, val dzikirTitle: String) : AmaliyahUiEffect
    data class ShowMessage(val message: String) : AmaliyahUiEffect
}
