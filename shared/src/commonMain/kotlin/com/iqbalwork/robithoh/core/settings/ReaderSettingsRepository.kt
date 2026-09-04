package com.iqbalwork.robithoh.core.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.core.database.rememberRobithohDatabase
import com.iqbalwork.robithoh.core.designsystem.theme.ReaderTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Model representing the reader settings for font size and reading background color.
 */
data class ReaderSettings(
    val fontScale: Float = 1.0f,
    val themeId: String = SYSTEM_THEME_ID
) {
    /**
     * Resolves the effective [ReaderTheme].
     * If set to "system", adapts to the active app [isDark] theme.
     * Otherwise returns the user's explicitly selected theme.
     */
    fun resolveTheme(isDark: Boolean): ReaderTheme {
        return if (themeId == SYSTEM_THEME_ID) {
            if (isDark) ReaderTheme.DARK else ReaderTheme.WHITE
        } else {
            ReaderTheme.fromId(themeId)
        }
    }

    companion object {
        const val SYSTEM_THEME_ID = "system"
    }
}

/**
 * Repository providing reactive and persistent reader settings across all reading screens
 * (Quran, Manaqib, Dzikir, and Generic Documents).
 */
class ReaderSettingsRepository(
    private val database: RobithohDatabase? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
) {
    private val _settings = MutableStateFlow(ReaderSettings())
    val settings: StateFlow<ReaderSettings> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val db = database ?: return
        coroutineScope.launch(dispatcher) {
            try {
                val entity = db.robithohDatabaseQueries.getReaderSettings().executeAsOneOrNull()
                if (entity != null) {
                    _settings.value = ReaderSettings(
                        fontScale = entity.font_scale.toFloat(),
                        themeId = entity.theme_id
                    )
                }
            } catch (_: Exception) {
                // Table not ready or query failed, keep defaults
            }
        }
    }

    fun updateFontScale(scale: Float) {
        val clamped = scale.coerceIn(0.75f, 2.0f)
        _settings.value = _settings.value.copy(fontScale = clamped)
        persist()
    }

    fun updateTheme(theme: ReaderTheme) {
        _settings.value = _settings.value.copy(themeId = theme.id)
        persist()
    }

    fun updateSettings(scale: Float, theme: ReaderTheme) {
        val clamped = scale.coerceIn(0.75f, 2.0f)
        _settings.value = ReaderSettings(fontScale = clamped, themeId = theme.id)
        persist()
    }

    private fun persist() {
        val db = database ?: return
        val current = _settings.value
        coroutineScope.launch(dispatcher) {
            try {
                db.robithohDatabaseQueries.insertOrUpdateReaderSettings(
                    fontScale = current.fontScale.toDouble(),
                    themeId = current.themeId
                )
            } catch (_: Exception) {
                // Ignore failure on persist
            }
        }
    }
}

/**
 * Singleton holder so that all reader screens across the app share the active session state.
 */
object ReaderSettingsManager {
    private var instance: ReaderSettingsRepository? = null

    fun getInstance(database: RobithohDatabase? = null): ReaderSettingsRepository {
        val existing = instance
        if (existing != null) {
            return existing
        }
        val created = ReaderSettingsRepository(database = database)
        instance = created
        return created
    }
}

/**
 * Composable helper to inject or remember the shared [ReaderSettingsRepository].
 */
@Composable
fun rememberReaderSettingsRepository(): ReaderSettingsRepository {
    val database = rememberRobithohDatabase()
    return remember(database) {
        ReaderSettingsManager.getInstance(database)
    }
}
