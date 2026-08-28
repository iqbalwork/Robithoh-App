package com.iqbalwork.robithoh.feature

import com.iqbalwork.robithoh.feature.amaliyah.model.AdzanVoices
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationSettings
import com.iqbalwork.robithoh.feature.amaliyah.model.PrayerType
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahUiIntent
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdzanFeatureTest {

    @Test
    fun testAdzanVoicesListCompleteness() {
        val voices = AdzanVoices.ALL
        assertEquals(4, voices.size)

        val misyari = AdzanVoices.findById("misyari_rasyid")
        assertEquals("Misyari Rasyid Al-Afasi", misyari.title)
        assertEquals("adzan_misyari_rasyid.mp3", misyari.audioFileName)
        assertEquals("adzan_misyari_rasyid_fajr.mp3", misyari.fajrAudioFileName)
        assertEquals("adzan_misyari_rasyid_fajr.mp3", misyari.getAudioForPrayer("Subuh"))
        assertEquals("adzan_misyari_rasyid.mp3", misyari.getAudioForPrayer("Dzuhur"))
        assertEquals("adzan_misyari_rasyid.mp3", misyari.getAudioForPrayer("Isya"))

        val nafees = AdzanVoices.findById("ahmad_al_nafees")
        assertEquals("Ahmad al-Nafees", nafees.title)
        assertEquals("adzan_ahmad_al_nafees.mp3", nafees.audioFileName)
        assertEquals("adzan_ahmad_al_nafees_fajr.mp3", nafees.fajrAudioFileName)

        val ozcan = AdzanVoices.findById("hafiz_mustafa_ozcan")
        assertEquals("Hafiz Mustafa Özcan", ozcan.title)
        assertEquals("adzan_hafiz_mustafa_ozcan.mp3", ozcan.audioFileName)
        assertEquals("adzan_hafiz_mustafa_ozcan_fajr.mp3", ozcan.fajrAudioFileName)

        val zahrani = AdzanVoices.findById("mansour_al_zahrani")
        assertEquals("Mansour Al-Zahrani", zahrani.title)
        assertEquals("adzan_mansour_al_zahrani.mp3", zahrani.audioFileName)
        assertEquals("adzan_mansour_al_zahrani_fajr.mp3", zahrani.fajrAudioFileName)
        assertEquals("adzan_mansour_al_zahrani_fajr.mp3", zahrani.getAudioForPrayer("Subuh"))
        assertEquals("adzan_mansour_al_zahrani.mp3", zahrani.getAudioForPrayer("Ashar"))
    }

    @Test
    fun testPrayerNotificationSettingsTogglesAndModes() {
        var settings = PrayerNotificationSettings()
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN, settings.subuhMode)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN, settings.dzuhurMode)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN, settings.asharMode)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN, settings.maghribMode)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN, settings.isyaMode)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.PUSH_NOTIFICATION, settings.imsakMode)

        // Test cycle
        settings = settings.withCycledPrayerMode(PrayerType.SUBUH)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.PUSH_NOTIFICATION, settings.subuhMode)
        assertTrue(settings.isSubuhEnabled)

        settings = settings.withCycledPrayerMode(PrayerType.SUBUH)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.SILENT, settings.subuhMode)
        assertFalse(settings.isSubuhEnabled)

        settings = settings.withCycledPrayerMode(PrayerType.SUBUH)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN, settings.subuhMode)
        assertTrue(settings.isSubuhEnabled)

        // Test explicit mode change
        settings = settings.withPrayerMode(PrayerType.MAGHRIB, com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.PUSH_NOTIFICATION)
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.PUSH_NOTIFICATION, settings.maghribMode)
    }

    @Test
    fun testViewModelAdzanVoiceAndModeSelection() {
        val viewModel = AmaliyahViewModel()
        assertEquals("misyari_rasyid", viewModel.currentState.notificationSettings.selectedVoiceId)

        viewModel.onIntent(AmaliyahUiIntent.SelectAdzanVoice("ahmad_al_nafees"))
        assertEquals("ahmad_al_nafees", viewModel.currentState.notificationSettings.selectedVoiceId)

        // Set mode intent
        viewModel.onIntent(AmaliyahUiIntent.SetPrayerNotificationMode(PrayerType.MAGHRIB, com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.SILENT))
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.SILENT, viewModel.currentState.notificationSettings.maghribMode)
        assertFalse(viewModel.currentState.notificationSettings.isMaghribEnabled)

        // Cycle mode intent
        viewModel.onIntent(AmaliyahUiIntent.CyclePrayerNotificationMode(PrayerType.MAGHRIB))
        assertEquals(com.iqbalwork.robithoh.feature.amaliyah.model.PrayerNotificationMode.ADZAN, viewModel.currentState.notificationSettings.maghribMode)
        assertTrue(viewModel.currentState.notificationSettings.isMaghribEnabled)

        viewModel.onIntent(AmaliyahUiIntent.SetNotificationModePickerPrayer(PrayerType.SUBUH))
        assertEquals(PrayerType.SUBUH, viewModel.currentState.activeNotificationModePickerPrayer)

        viewModel.onIntent(AmaliyahUiIntent.SetAdzanPickerSheetOpen(true))
        assertTrue(viewModel.currentState.isAdzanPickerSheetOpen)
    }
}
