package com.iqbalwork.robithoh.core.analytics

/**
 * Konstanta nama event standar untuk aplikasi Robithoh.
 */
object AnalyticsEvents {
    // Habit & Dzikir Harian
    const val DZIKIR_SESSION_COMPLETED = "dzikir_session_completed"
    const val TASBIH_TARGET_REACHED = "tasbih_target_reached"
    const val TASBIH_RESET = "tasbih_reset"
    const val TASBIH_SESSION_SUMMARY = "tasbih_session_summary"
    const val DOA_VIEWED = "doa_viewed"

    // Manaqib & Khotaman Majlis
    const val MANAQIB_DETAIL_OPENED = "manaqib_detail_opened"
    const val MANAQIB_LANGUAGE_CHANGED = "manaqib_language_changed"
    const val PRESENTATION_MODE_TOGGLED = "presentation_mode_toggled"
    const val KHOTAMAN_GUIDE_OPENED = "khotaman_guide_opened"

    // Audio & Langgam Shalawat
    const val AUDIO_PLAYBACK_STARTED = "audio_playback_started"
    const val AUDIO_PLAYBACK_COMPLETED = "audio_playback_completed"
    const val AUDIO_PLAYBACK_PAUSED = "audio_playback_paused"

    // Al-Qur'an & Penjelajahan
    const val QURAN_SURAH_OPENED = "quran_surah_opened"
    const val QURAN_BOOKMARK_SAVED = "quran_bookmark_saved"
    const val CONTENT_SEARCH_PERFORMED = "content_search_performed"

    // Widgets & System Sholat
    const val WIDGET_SHORTCUT_CLICKED = "widget_shortcut_clicked"
    const val PRAYER_ALARM_SETTING_CHANGED = "prayer_alarm_setting_changed"
}

/**
 * Konstanta parameter event analytics terstandarisasi.
 */
object AnalyticsParams {
    const val DZIKIR_TYPE = "dzikir_type"
    const val TIME_SLOT = "time_slot"
    const val DURATION_SECONDS = "duration_seconds"
    const val TARGET_COUNT = "target_count"
    const val TOTAL_TAPS = "total_taps"
    const val IS_VIBRATION_ENABLED = "is_vibration_enabled"
    const val DOA_ID = "doa_id"
    const val DOA_TITLE = "doa_title"

    const val MANQOBAH_NUMBER = "manqobah_number"
    const val MANQOBAH_TITLE = "manqobah_title"
    const val SELECTED_LANGUAGE = "selected_language"
    const val PREVIOUS_LANGUAGE = "previous_language"
    const val IS_ENABLED = "is_enabled"
    const val SECTION = "section"

    const val AUDIO_ID = "audio_id"
    const val AUDIO_TITLE = "audio_title"
    const val CATEGORY = "category"
    const val TOTAL_DURATION_SEC = "total_duration_sec"

    const val SURAH_NUMBER = "surah_number"
    const val SURAH_NAME = "surah_name"
    const val AYAH_NUMBER = "ayah_number"
    const val SOURCE = "source"
    const val SEARCH_QUERY = "search_query"
    const val RESULTS_FOUND = "results_found"

    const val WIDGET_TYPE = "widget_type"
    const val TARGET_DESTINATION = "target_destination"
    const val PRAYER_NAME = "prayer_name"
    const val ALARM_MODE = "alarm_mode"
}

/**
 * User Properties untuk segmentasi pengguna di Firebase & GA4.
 */
object UserProperties {
    const val PREFERRED_LANGUAGE = "preferred_translation_language"
    const val APP_THEME = "app_theme"
    const val ARABIC_FONT_SCALE = "arabic_font_scale"
    const val PRAYER_NOTIFICATION_ENABLED = "prayer_notification_enabled"
}
