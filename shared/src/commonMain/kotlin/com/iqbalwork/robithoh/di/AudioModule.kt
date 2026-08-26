package com.iqbalwork.robithoh.di

import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.designsystem.KmpHapticFeedback
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import org.koin.dsl.module

val audioModule = module {
    single<KmpAudioPlayer> { createAudioPlayer() }
    single<KmpHapticFeedback> { getHapticFeedback() }
}
