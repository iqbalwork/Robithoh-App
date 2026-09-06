package com.iqbalwork.robithoh.core.settings

import com.iqbalwork.robithoh.core.designsystem.theme.ReaderTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ReaderSettingsRepositoryTest {

    @Test
    fun testDefaultSettings() {
        val testDispatcher = StandardTestDispatcher()
        val testScope = TestScope(testDispatcher)
        val repository = ReaderSettingsRepository(
            database = null,
            dispatcher = testDispatcher,
            coroutineScope = testScope
        )

        assertEquals(1.0f, repository.settings.value.fontScale)
        assertEquals(ReaderSettings.SYSTEM_THEME_ID, repository.settings.value.themeId)
        assertEquals(ReaderTheme.WHITE, repository.settings.value.resolveTheme(isDark = false))
        assertEquals(ReaderTheme.DARK, repository.settings.value.resolveTheme(isDark = true))
    }

    @Test
    fun testUpdateFontScale() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)
        val repository = ReaderSettingsRepository(
            database = null,
            dispatcher = testDispatcher,
            coroutineScope = testScope
        )

        repository.updateFontScale(1.3f)
        assertEquals(1.3f, repository.settings.value.fontScale)

        // Test upper clamp
        repository.updateFontScale(2.5f)
        assertEquals(2.0f, repository.settings.value.fontScale)

        // Test lower clamp
        repository.updateFontScale(0.5f)
        assertEquals(0.75f, repository.settings.value.fontScale)
    }

    @Test
    fun testUpdateTheme() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)
        val repository = ReaderSettingsRepository(
            database = null,
            dispatcher = testDispatcher,
            coroutineScope = testScope
        )

        repository.updateTheme(ReaderTheme.SEPIA)
        assertEquals("sepia", repository.settings.value.themeId)
        assertEquals(ReaderTheme.SEPIA, repository.settings.value.resolveTheme(isDark = false))
        assertEquals(ReaderTheme.SEPIA, repository.settings.value.resolveTheme(isDark = true))

        repository.updateTheme(ReaderTheme.KHAKI)
        assertEquals(ReaderTheme.KHAKI, repository.settings.value.resolveTheme(isDark = false))

        repository.updateTheme(ReaderTheme.DARK)
        assertEquals(ReaderTheme.DARK, repository.settings.value.resolveTheme(isDark = false))
    }
}
