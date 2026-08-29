package com.iqbalwork.robithoh.di

import com.iqbalwork.robithoh.core.audio.AudioCacheManager
import com.iqbalwork.robithoh.core.audio.AudioDownloader
import com.iqbalwork.robithoh.core.audio.KmpAudioPlayer
import com.iqbalwork.robithoh.core.audio.createAudioCacheManager
import com.iqbalwork.robithoh.core.audio.createAudioDownloader
import com.iqbalwork.robithoh.core.audio.createAudioPlayer
import com.iqbalwork.robithoh.core.designsystem.KmpHapticFeedback
import com.iqbalwork.robithoh.core.designsystem.getHapticFeedback
import org.koin.dsl.module

val audioModule = module {
    single<AudioCacheManager> { createAudioCacheManager() }
    single<AudioDownloader> { createAudioDownloader(get()) }
    single<KmpAudioPlayer> { createAudioPlayer() }
    single<KmpHapticFeedback> { getHapticFeedback() }
}
