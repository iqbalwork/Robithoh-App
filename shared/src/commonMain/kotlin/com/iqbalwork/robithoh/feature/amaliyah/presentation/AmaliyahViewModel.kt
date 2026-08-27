package com.iqbalwork.robithoh.feature.amaliyah.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.core.datetime.currentLocalDateTime
import com.iqbalwork.robithoh.core.designsystem.component.LiturgyLanguage
import com.iqbalwork.robithoh.core.location.UserLocation
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.feature.amaliyah.data.AmaliyahRepository
import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AmaliyahViewModel(
    private val repository: AmaliyahRepository = AmaliyahRepository(),
    private val calculator: PrayerTimesCalculator = PrayerTimesCalculator(),
    private val database: RobithohDatabase? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : MviViewModel<AmaliyahUiState, AmaliyahUiIntent, AmaliyahUiEffect>(
    AmaliyahUiState()
) {

    init {
        loadData()
        loadPersistedPrayerSettings()
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
                updateState {
                    copy(
                        selectedLocation = intent.location,
                        isGpsActive = false,
                        isFetchingLocation = false,
                        locationErrorMessage = null
                    )
                }
                persistSettings(
                    method = currentState.selectedCalculationMethod,
                    adjustments = currentState.prayerAdjustments,
                    location = intent.location,
                    isGps = false
                )
                recalculatePrayerTimes(
                    location = intent.location,
                    method = currentState.selectedCalculationMethod,
                    adjustments = currentState.prayerAdjustments
                )
            }
            is AmaliyahUiIntent.SetGpsLocation -> {
                val loc = LocationPreset(
                    name = intent.location.locationName,
                    latitude = intent.location.latitude,
                    longitude = intent.location.longitude,
                    timezoneOffset = intent.location.timezoneOffset,
                    province = "GPS"
                )
                updateState {
                    copy(
                        selectedLocation = loc,
                        isGpsActive = true,
                        isFetchingLocation = false,
                        locationErrorMessage = null
                    )
                }
                persistSettings(
                    method = currentState.selectedCalculationMethod,
                    adjustments = currentState.prayerAdjustments,
                    location = loc,
                    isGps = true
                )
                recalculatePrayerTimes(
                    location = loc,
                    method = currentState.selectedCalculationMethod,
                    adjustments = currentState.prayerAdjustments
                )
            }
            is AmaliyahUiIntent.SetFetchingLocation -> {
                updateState { copy(isFetchingLocation = intent.isFetching) }
            }
            is AmaliyahUiIntent.SetLocationError -> {
                updateState {
                    copy(
                        isFetchingLocation = false,
                        locationErrorMessage = intent.message
                    )
                }
            }
            is AmaliyahUiIntent.SelectCalculationMethod -> {
                updateState { copy(selectedCalculationMethod = intent.method) }
                persistSettings(
                    method = intent.method,
                    adjustments = currentState.prayerAdjustments,
                    location = currentState.selectedLocation,
                    isGps = currentState.isGpsActive
                )
                recalculatePrayerTimes(
                    location = currentState.selectedLocation,
                    method = intent.method,
                    adjustments = currentState.prayerAdjustments
                )
            }
            is AmaliyahUiIntent.UpdatePrayerAdjustment -> {
                val updatedAdjustments = currentState.prayerAdjustments.withOffset(
                    prayerType = intent.prayerType,
                    offset = intent.offsetMinutes
                )
                updateState {
                    copy(
                        prayerAdjustments = updatedAdjustments,
                        activeAdjustmentPrayerType = null
                    )
                }
                persistSettings(
                    method = currentState.selectedCalculationMethod,
                    adjustments = updatedAdjustments,
                    location = currentState.selectedLocation,
                    isGps = currentState.isGpsActive
                )
                recalculatePrayerTimes(
                    location = currentState.selectedLocation,
                    method = currentState.selectedCalculationMethod,
                    adjustments = updatedAdjustments
                )
            }
            is AmaliyahUiIntent.OpenAdjustmentPicker -> {
                updateState { copy(activeAdjustmentPrayerType = intent.prayerType) }
            }
            is AmaliyahUiIntent.CloseAdjustmentPicker -> {
                updateState { copy(activeAdjustmentPrayerType = null) }
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
                recalculatePrayerTimes(
                    location = currentState.selectedLocation,
                    method = currentState.selectedCalculationMethod,
                    adjustments = currentState.prayerAdjustments
                )
            }
            is AmaliyahUiIntent.ChangeDateOffset -> {
                val newOffset = currentState.selectedDateOffsetDays + intent.deltaDays
                recalculatePrayerTimes(dateOffsetDays = newOffset)
            }
            is AmaliyahUiIntent.ResetDateOffset -> {
                recalculatePrayerTimes(dateOffsetDays = 0)
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

    private fun loadPersistedPrayerSettings() {
        if (database == null) {
            recalculatePrayerTimes(
                location = currentState.selectedLocation,
                method = currentState.selectedCalculationMethod,
                adjustments = currentState.prayerAdjustments
            )
            return
        }

        viewModelScope.launch(dispatcher) {
            try {
                val settings = database.robithohDatabaseQueries.getPrayerSettings().executeAsOneOrNull()
                if (settings != null) {
                    val method = PrayerCalculationMethods.findById(settings.method_id)
                    val adjustments = PrayerTimeAdjustments(
                        imsak = settings.imsak_offset.toInt(),
                        subuh = settings.subuh_offset.toInt(),
                        terbit = settings.terbit_offset.toInt(),
                        dzuhur = settings.dzuhur_offset.toInt(),
                        ashar = settings.ashar_offset.toInt(),
                        maghrib = settings.maghrib_offset.toInt(),
                        isya = settings.isya_offset.toInt()
                    )
                    val location = if (settings.custom_lat != null && settings.custom_lng != null) {
                        LocationPreset(
                            name = settings.custom_location_name ?: "Lokasi Tersimpan",
                            latitude = settings.custom_lat,
                            longitude = settings.custom_lng,
                            timezoneOffset = settings.custom_timezone_offset ?: 7.0,
                            province = if (settings.is_gps == 1L) "GPS" else "Manual"
                        )
                    } else {
                        currentState.selectedLocation
                    }
                    val isGps = settings.is_gps == 1L

                    withContext(Dispatchers.Main) {
                        updateState {
                            copy(
                                selectedCalculationMethod = method,
                                prayerAdjustments = adjustments,
                                selectedLocation = location,
                                isGpsActive = isGps
                            )
                        }
                        recalculatePrayerTimes(
                            location = location,
                            method = method,
                            adjustments = adjustments
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        recalculatePrayerTimes(
                            location = currentState.selectedLocation,
                            method = currentState.selectedCalculationMethod,
                            adjustments = currentState.prayerAdjustments
                        )
                    }
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    recalculatePrayerTimes(
                        location = currentState.selectedLocation,
                        method = currentState.selectedCalculationMethod,
                        adjustments = currentState.prayerAdjustments
                    )
                }
            }
        }
    }

    private fun persistSettings(
        method: PrayerCalculationMethodItem,
        adjustments: PrayerTimeAdjustments,
        location: LocationPreset = currentState.selectedLocation,
        isGps: Boolean = currentState.isGpsActive
    ) {
        if (database == null) return
        viewModelScope.launch(dispatcher) {
            try {
                database.robithohDatabaseQueries.insertOrUpdatePrayerSettings(
                    method_id = method.id,
                    imsak_offset = adjustments.imsak.toLong(),
                    subuh_offset = adjustments.subuh.toLong(),
                    terbit_offset = adjustments.terbit.toLong(),
                    dzuhur_offset = adjustments.dzuhur.toLong(),
                    ashar_offset = adjustments.ashar.toLong(),
                    maghrib_offset = adjustments.maghrib.toLong(),
                    isya_offset = adjustments.isya.toLong(),
                    madhab = "SHAFI",
                    custom_lat = location.latitude,
                    custom_lng = location.longitude,
                    custom_location_name = location.name,
                    custom_timezone_offset = location.timezoneOffset,
                    is_gps = if (isGps) 1L else 0L
                )
            } catch (_: Exception) {
                // Silently ignore persist errors
            }
        }
    }

    private fun recalculatePrayerTimes(
        location: LocationPreset = currentState.selectedLocation,
        method: PrayerCalculationMethodItem = currentState.selectedCalculationMethod,
        adjustments: PrayerTimeAdjustments = currentState.prayerAdjustments,
        dateOffsetDays: Int = currentState.selectedDateOffsetDays
    ) {
        val now = currentLocalDateTime()
        val (calcYear, calcMonth, calcDay) = com.iqbalwork.robithoh.core.datetime.shiftDate(
            now.year, now.month, now.day, dateOffsetDays
        )

        val schedule = calculator.calculateSchedule(
            year = calcYear,
            month = calcMonth,
            day = calcDay,
            latitude = location.latitude,
            longitude = location.longitude,
            timezoneOffset = location.timezoneOffset,
            locationName = location.name,
            method = method,
            adjustments = adjustments
        )

        val qibla = calculator.calculateQibla(
            latitude = location.latitude,
            longitude = location.longitude,
            cityName = location.name
        )

        val countdown = calculator.computeNextPrayer(
            schedule = schedule,
            currentHour = now.hour,
            currentMinute = now.minute,
            currentSecond = now.second
        )

        updateState {
            copy(
                prayerSchedule = schedule,
                qiblaInfo = qibla,
                nextPrayerCountdown = countdown,
                selectedDateOffsetDays = dateOffsetDays
            )
        }
    }

    private fun startCountdownTicker() {
        viewModelScope.launch {
            while (isActive) {
                val schedule = currentState.prayerSchedule
                if (schedule != null) {
                    val now = currentLocalDateTime()
                    val countdown = calculator.computeNextPrayer(
                        schedule = schedule,
                        currentHour = now.hour,
                        currentMinute = now.minute,
                        currentSecond = now.second
                    )
                    updateState { copy(nextPrayerCountdown = countdown) }
                }
                delay(1000)
            }
        }
    }
}
