package com.iqbalwork.robithoh.di

import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import com.iqbalwork.robithoh.feature.amaliyah.data.AmaliyahRepository
import com.iqbalwork.robithoh.feature.amaliyah.domain.PrayerTimesCalculator
import com.iqbalwork.robithoh.feature.amaliyah.presentation.AmaliyahViewModel
import com.iqbalwork.robithoh.feature.manaqib.data.ManaqibRepository
import com.iqbalwork.robithoh.feature.manaqib.data.ManaqibRepositoryImpl
import com.iqbalwork.robithoh.feature.manaqib.presentation.ManaqibViewModel
import com.iqbalwork.robithoh.feature.quran.data.QuranRepository
import com.iqbalwork.robithoh.feature.quran.data.QuranRepositoryImpl
import com.iqbalwork.robithoh.feature.quran.presentation.QuranViewModel
import com.iqbalwork.robithoh.feature.tasbih.presentation.TasbihViewModel
import org.koin.dsl.module

val viewModelModule = module {
    // Domain & Repositories
    single { AmaliyahRepository() }
    single { PrayerTimesCalculator() }
    single<ManaqibRepository> {
        ManaqibRepositoryImpl(
            database = getOrNull<RobithohDatabase>(),
            dispatcher = get()
        )
    }
    single<QuranRepository> {
        QuranRepositoryImpl(
            database = getOrNull<RobithohDatabase>(),
            dispatcher = get()
        )
    }

    // Feature ViewModels
    factory {
        AmaliyahViewModel(
            repository = get(),
            calculator = get()
        )
    }

    factory {
        TasbihViewModel(
            hapticFeedback = getHapticFeedback(),
            database = getOrNull<RobithohDatabase>(),
            dispatcher = get()
        )
    }

    factory {
        ManaqibViewModel(
            repository = get()
        )
    }

    factory {
        QuranViewModel(
            repository = get(),
            audioPlayer = getOrNull<KmpAudioPlayer>()
        )
    }
}
