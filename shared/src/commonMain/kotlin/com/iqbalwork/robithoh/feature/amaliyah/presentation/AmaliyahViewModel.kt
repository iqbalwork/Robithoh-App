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
    private val alarmScheduler: com.iqbalwork.robithoh.core.notification.PrayerAlarmScheduler? = null,
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
            is AmaliyahUiIntent.TogglePrayerLogged -> {
                val current = currentState.loggedPrayers
                val updated = if (current.contains(intent.prayerName)) {
                    current - intent.prayerName
                } else {
                    current + intent.prayerName
                }
                updateState { copy(loggedPrayers = updated) }
            }
            is AmaliyahUiIntent.OpenTasbihWithTarget -> {
                sendEffect(AmaliyahUiEffect.NavigateToTasbih(intent.targetCount, intent.dzikirTitle))
            }
            is AmaliyahUiIntent.SelectAdzanVoice -> {
                val updatedNotif = currentState.notificationSettings.copy(selectedVoiceId = intent.voiceId)
                updateState { copy(notificationSettings = updatedNotif) }
                persistAdzanVoice(voiceId = intent.voiceId, customPath = updatedNotif.customAudioPath)
                syncAlarmSchedule(currentState.prayerSchedule, updatedNotif)
            }
            is AmaliyahUiIntent.SetCustomAdzanPath -> {
                val updatedNotif = currentState.notificationSettings.copy(
                    customAudioPath = intent.path,
                    selectedVoiceId = "custom"
                )
                updateState { copy(notificationSettings = updatedNotif) }
                persistAdzanVoice(voiceId = "custom", customPath = intent.path)
                syncAlarmSchedule(currentState.prayerSchedule, updatedNotif)
            }
            is AmaliyahUiIntent.TogglePrayerNotification -> {
                val updatedNotif = currentState.notificationSettings.withToggledPrayer(intent.prayerType, intent.enabled)
                updateState { copy(notificationSettings = updatedNotif) }
                persistPrayerNotificationToggles(updatedNotif)
                syncAlarmSchedule(currentState.prayerSchedule, updatedNotif)
            }
            is AmaliyahUiIntent.SetPrayerNotificationMode -> {
                val updatedNotif = currentState.notificationSettings.withPrayerMode(intent.prayerType, intent.mode)
                updateState {
                    copy(
                        notificationSettings = updatedNotif,
                        activeNotificationModePickerPrayer = null
                    )
                }
                persistPrayerNotificationToggles(updatedNotif)
                syncAlarmSchedule(currentState.prayerSchedule, updatedNotif)
            }
            is AmaliyahUiIntent.CyclePrayerNotificationMode -> {
                val updatedNotif = currentState.notificationSettings.withCycledPrayerMode(intent.prayerType)
                updateState { copy(notificationSettings = updatedNotif) }
                persistPrayerNotificationToggles(updatedNotif)
                syncAlarmSchedule(currentState.prayerSchedule, updatedNotif)
            }
            is AmaliyahUiIntent.SetNotificationModePickerPrayer -> {
                updateState { copy(activeNotificationModePickerPrayer = intent.prayerType) }
            }
            is AmaliyahUiIntent.SetAdzanPickerSheetOpen -> {
                updateState { copy(isAdzanPickerSheetOpen = intent.isOpen) }
            }
            is AmaliyahUiIntent.SetPreviewingAdzanId -> {
                updateState { copy(previewingAdzanId = intent.voiceId) }
            }
            is AmaliyahUiIntent.TestTriggerPrayerNotification -> {
                alarmScheduler?.testTriggerNotification(
                    prayerName = intent.prayerType.label,
                    mode = intent.mode,
                    voiceId = currentState.notificationSettings.selectedVoiceId,
                    customPath = currentState.notificationSettings.customAudioPath
                )
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

                    val notifSettings = PrayerNotificationSettings(
                        subuhMode = PrayerNotificationMode.fromDbValue(settings.subuh_notif_enabled),
                        dzuhurMode = PrayerNotificationMode.fromDbValue(settings.dzuhur_notif_enabled),
                        asharMode = PrayerNotificationMode.fromDbValue(settings.ashar_notif_enabled),
                        maghribMode = PrayerNotificationMode.fromDbValue(settings.maghrib_notif_enabled),
                        isyaMode = PrayerNotificationMode.fromDbValue(settings.isya_notif_enabled),
                        imsakMode = PrayerNotificationMode.fromDbValue(settings.imsak_notif_enabled).let {
                            if (it == PrayerNotificationMode.ADZAN) PrayerNotificationMode.PUSH_NOTIFICATION else it
                        },
                        selectedVoiceId = settings.selected_adzan_voice_id,
                        customAudioPath = settings.custom_adzan_audio_path
                    )

                    withContext(Dispatchers.Main) {
                        val resolvedLocation = if (currentState.isGpsActive) currentState.selectedLocation else location
                        val resolvedIsGps = currentState.isGpsActive || isGps

                        updateState {
                            copy(
                                selectedCalculationMethod = method,
                                prayerAdjustments = adjustments,
                                selectedLocation = resolvedLocation,
                                isGpsActive = resolvedIsGps,
                                notificationSettings = notifSettings
                            )
                        }
                        recalculatePrayerTimes(
                            location = resolvedLocation,
                            method = method,
                            adjustments = adjustments
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        if (!currentState.isGpsActive) {
                            recalculatePrayerTimes(
                                location = currentState.selectedLocation,
                                method = currentState.selectedCalculationMethod,
                                adjustments = currentState.prayerAdjustments
                            )
                        }
                    }
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    if (!currentState.isGpsActive) {
                        recalculatePrayerTimes(
                            location = currentState.selectedLocation,
                            method = currentState.selectedCalculationMethod,
                            adjustments = currentState.prayerAdjustments
                        )
                    }
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
        val notif = currentState.notificationSettings
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
                    is_gps = if (isGps) 1L else 0L,
                    selected_adzan_voice_id = notif.selectedVoiceId,
                    custom_adzan_audio_path = notif.customAudioPath,
                    subuh_notif_enabled = PrayerNotificationMode.toDbValue(notif.subuhMode),
                    dzuhur_notif_enabled = PrayerNotificationMode.toDbValue(notif.dzuhurMode),
                    ashar_notif_enabled = PrayerNotificationMode.toDbValue(notif.asharMode),
                    maghrib_notif_enabled = PrayerNotificationMode.toDbValue(notif.maghribMode),
                    isya_notif_enabled = PrayerNotificationMode.toDbValue(notif.isyaMode),
                    imsak_notif_enabled = PrayerNotificationMode.toDbValue(notif.imsakMode)
                )
            } catch (_: Exception) {
                // Silently ignore persist errors
            }
        }
    }

    private fun persistAdzanVoice(voiceId: String, customPath: String?) {
        if (database == null) return
        viewModelScope.launch(dispatcher) {
            try {
                database.robithohDatabaseQueries.updateAdzanVoice(
                    selectedAdzanVoiceId = voiceId,
                    customAdzanAudioPath = customPath
                )
            } catch (_: Exception) {}
        }
    }

    private fun persistPrayerNotificationToggles(notif: PrayerNotificationSettings) {
        if (database == null) return
        viewModelScope.launch(dispatcher) {
            try {
                database.robithohDatabaseQueries.updatePrayerNotificationToggles(
                    subuh = PrayerNotificationMode.toDbValue(notif.subuhMode),
                    dzuhur = PrayerNotificationMode.toDbValue(notif.dzuhurMode),
                    ashar = PrayerNotificationMode.toDbValue(notif.asharMode),
                    maghrib = PrayerNotificationMode.toDbValue(notif.maghribMode),
                    isya = PrayerNotificationMode.toDbValue(notif.isyaMode),
                    imsak = PrayerNotificationMode.toDbValue(notif.imsakMode)
                )
            } catch (_: Exception) {}
        }
    }

    private fun syncAlarmSchedule(schedule: PrayerSchedule?, notifSettings: PrayerNotificationSettings) {
        if (schedule == null || alarmScheduler == null) return
        viewModelScope.launch(dispatcher) {
            try {
                alarmScheduler.scheduleAlarms(schedule, notifSettings)
            } catch (_: Exception) {}
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

        if (schedule != null) {
            syncAlarmSchedule(schedule, currentState.notificationSettings)
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
