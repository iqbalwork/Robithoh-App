package com.iqbalwork.robithoh.core.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.core.database.rememberRobithohDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Model representing global app status flags.
 */
data class AppSettings(
    val hasCompletedOnboarding: Boolean = false,
    val hasSeenReaderSpotlight: Boolean = false,
    val hasSeenPrayerSpotlight: Boolean = false,
    val hasSeenQuranSpotlight: Boolean = false
)

/**
 * Repository providing reactive and persistent app flags across screens.
 */
class AppSettingsRepository(
    private val database: RobithohDatabase? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
) {
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val db = database ?: return
        coroutineScope.launch(dispatcher) {
            try {
                val entity = db.robithohDatabaseQueries.getAppSettings().executeAsOneOrNull()
                if (entity != null) {
                    _settings.value = AppSettings(
                        hasCompletedOnboarding = entity.has_completed_onboarding == 1L,
                        hasSeenReaderSpotlight = entity.has_seen_reader_spotlight == 1L,
                        hasSeenPrayerSpotlight = entity.has_seen_prayer_spotlight == 1L,
                        hasSeenQuranSpotlight = entity.has_seen_quran_spotlight == 1L
                    )
                }
            } catch (_: Exception) {
                // Table not ready or query failed, keep defaults
            }
        }
    }

    fun setOnboardingCompleted(completed: Boolean = true) {
        _settings.value = _settings.value.copy(hasCompletedOnboarding = completed)
        persist()
    }

    fun setReaderSpotlightSeen(seen: Boolean = true) {
        _settings.value = _settings.value.copy(hasSeenReaderSpotlight = seen)
        persist()
    }

    fun setPrayerSpotlightSeen(seen: Boolean = true) {
        _settings.value = _settings.value.copy(hasSeenPrayerSpotlight = seen)
        persist()
    }

    fun setQuranSpotlightSeen(seen: Boolean = true) {
        _settings.value = _settings.value.copy(hasSeenQuranSpotlight = seen)
        persist()
    }

    private fun persist() {
        val db = database ?: return
        val current = _settings.value
        coroutineScope.launch(dispatcher) {
            try {
                db.robithohDatabaseQueries.insertOrUpdateAppSettings(
                    hasCompletedOnboarding = if (current.hasCompletedOnboarding) 1L else 0L,
                    hasSeenReaderSpotlight = if (current.hasSeenReaderSpotlight) 1L else 0L,
                    hasSeenPrayerSpotlight = if (current.hasSeenPrayerSpotlight) 1L else 0L,
                    hasSeenQuranSpotlight = if (current.hasSeenQuranSpotlight) 1L else 0L
                )
            } catch (_: Exception) {
                // Ignore persistence failure
            }
        }
    }
}

object AppSettingsManager {
    private var instance: AppSettingsRepository? = null

    fun getInstance(database: RobithohDatabase? = null): AppSettingsRepository {
        val existing = instance
        if (existing != null) {
            return existing
        }
        val created = AppSettingsRepository(database = database)
        instance = created
        return created
    }
}

@Composable
fun rememberAppSettingsRepository(): AppSettingsRepository {
    val database = rememberRobithohDatabase()
    return remember(database) {
        AppSettingsManager.getInstance(database)
    }
}
