package com.iqbalwork.robithoh.core.settings

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppSettingsRepositoryTest {

    @Test
    fun testDefaultSettings() {
        val testDispatcher = StandardTestDispatcher()
        val testScope = TestScope(testDispatcher)
        val repository = AppSettingsRepository(
            database = null,
            dispatcher = testDispatcher,
            coroutineScope = testScope
        )

        assertFalse(repository.settings.value.hasCompletedOnboarding)
        assertFalse(repository.settings.value.hasSeenReaderSpotlight)
        assertFalse(repository.settings.value.hasSeenPrayerSpotlight)
        assertFalse(repository.settings.value.hasSeenQuranSpotlight)
    }

    @Test
    fun testSetReaderSpotlightSeen() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)
        val repository = AppSettingsRepository(
            database = null,
            dispatcher = testDispatcher,
            coroutineScope = testScope
        )

        assertFalse(repository.settings.value.hasSeenReaderSpotlight)

        repository.setReaderSpotlightSeen(true)
        assertTrue(repository.settings.value.hasSeenReaderSpotlight)

        repository.setReaderSpotlightSeen(false)
        assertFalse(repository.settings.value.hasSeenReaderSpotlight)
    }

    @Test
    fun testSetPrayerSpotlightSeen() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)
        val repository = AppSettingsRepository(
            database = null,
            dispatcher = testDispatcher,
            coroutineScope = testScope
        )

        assertFalse(repository.settings.value.hasSeenPrayerSpotlight)

        repository.setPrayerSpotlightSeen(true)
        assertTrue(repository.settings.value.hasSeenPrayerSpotlight)

        repository.setPrayerSpotlightSeen(false)
        assertFalse(repository.settings.value.hasSeenPrayerSpotlight)
    }

    @Test
    fun testSetQuranSpotlightSeen() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)
        val repository = AppSettingsRepository(
            database = null,
            dispatcher = testDispatcher,
            coroutineScope = testScope
        )

        assertFalse(repository.settings.value.hasSeenQuranSpotlight)

        repository.setQuranSpotlightSeen(true)
        assertTrue(repository.settings.value.hasSeenQuranSpotlight)

        repository.setQuranSpotlightSeen(false)
        assertFalse(repository.settings.value.hasSeenQuranSpotlight)
    }

    @Test
    fun testSetOnboardingCompleted() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)
        val repository = AppSettingsRepository(
            database = null,
            dispatcher = testDispatcher,
            coroutineScope = testScope
        )

        assertFalse(repository.settings.value.hasCompletedOnboarding)

        repository.setOnboardingCompleted(true)
        assertTrue(repository.settings.value.hasCompletedOnboarding)
    }
}
