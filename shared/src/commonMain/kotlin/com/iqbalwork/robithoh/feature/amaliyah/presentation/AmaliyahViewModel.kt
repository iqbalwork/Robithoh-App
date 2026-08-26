package com.iqbalwork.robithoh.feature.amaliyah.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.feature.amaliyah.data.AmaliyahRepository
import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.model.AmaliyahCategory
import com.iqbalwork.robithoh.feature.amaliyah.model.DzikirType
import com.iqbalwork.robithoh.feature.amaliyah.model.LocationPreset
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AmaliyahViewModel(
    private val repository: AmaliyahRepository = AmaliyahRepository(),
    private val calculator: PrayerTimesCalculator = PrayerTimesCalculator()
) : MviViewModel<AmaliyahUiState, AmaliyahUiIntent, AmaliyahUiEffect>(
    AmaliyahUiState()
) {

    init {
        loadData()
        recalculatePrayerTimes(currentState.selectedLocation)
        startCountdownTicker()
    }

    override fun onIntent(intent: AmaliyahUiIntent) {
        when (intent) {
            is AmaliyahUiIntent.SelectLanguage -> {
                updateState { copy(selectedLanguage = intent.language) }
            }
            is AmaliyahUiIntent.SelectCategory -> {
                updateState { copy(selectedCategory = intent.category) }
            }
            is AmaliyahUiIntent.SelectDzikirType -> {
                updateState { copy(activeDzikirType = intent.dzikirType) }
            }
            is AmaliyahUiIntent.SelectLocation -> {
                updateState { copy(selectedLocation = intent.location) }
                recalculatePrayerTimes(intent.location)
            }
            is AmaliyahUiIntent.ToggleExpandItem -> {
                updateState {
                    copy(expandedItemId = if (expandedItemId == intent.itemId) null else intent.itemId)
                }
            }
            is AmaliyahUiIntent.TickCountdown -> {
                val schedule = currentState.prayerSchedule
                if (schedule != null) {
                    val countdown = calculator.computeNextPrayer(
                        schedule = schedule,
                        currentHour = intent.hour,
                        currentMinute = intent.minute,
                        currentSecond = intent.second
                    )
                    updateState { copy(nextPrayerCountdown = countdown) }
                }
            }
            is AmaliyahUiIntent.RefreshSchedule -> {
                recalculatePrayerTimes(currentState.selectedLocation)
            }
            is AmaliyahUiIntent.OpenTasbihWithTarget -> {
                sendEffect(AmaliyahUiEffect.NavigateToTasbih(intent.targetCount, intent.dzikirTitle))
            }
        }
    }

    private fun loadData() {
        val jahr = repository.getDzikirJahrList()
        val khofi = repository.getDzikirKhofiList()
        val daily = repository.getDailyPrayersList()
        val hijriyah = repository.get12BulanHijriyahList()
        val sunnah = repository.getSholatSunnahList()

        updateState {
            copy(
                dzikirJahrList = jahr,
                dzikirKhofiList = khofi,
                dailyPrayersList = daily,
                hijriyahList = hijriyah,
                sholatSunnahList = sunnah
            )
        }
    }

    private fun recalculatePrayerTimes(location: LocationPreset) {
        val schedule = calculator.calculateSchedule(
            year = 2026,
            month = 8,
            day = 24,
            latitude = location.latitude,
            longitude = location.longitude,
            timezoneOffset = location.timezoneOffset,
            locationName = location.name
        )

        val qibla = calculator.calculateQibla(
            latitude = location.latitude,
            longitude = location.longitude,
            cityName = location.name
        )

        val countdown = calculator.computeNextPrayer(
            schedule = schedule,
            currentHour = 18,
            currentMinute = 30,
            currentSecond = 0
        )

        updateState {
            copy(
                prayerSchedule = schedule,
                qiblaInfo = qibla,
                nextPrayerCountdown = countdown
            )
        }
    }

    private fun startCountdownTicker() {
        viewModelScope.launch {
            var secondOffset = 0
            while (isActive) {
                val schedule = currentState.prayerSchedule
                if (schedule != null) {
                    val h = (18 + (30 + secondOffset / 60) / 60) % 24
                    val m = (30 + secondOffset / 60) % 60
                    val s = secondOffset % 60

                    val countdown = calculator.computeNextPrayer(
                        schedule = schedule,
                        currentHour = h,
                        currentMinute = m,
                        currentSecond = s
                    )
                    updateState { copy(nextPrayerCountdown = countdown) }
                }
                delay(1000)
                secondOffset++
            }
        }
    }
}
