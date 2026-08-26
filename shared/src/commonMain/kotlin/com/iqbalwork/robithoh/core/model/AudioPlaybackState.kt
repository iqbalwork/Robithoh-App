package com.iqbalwork.robithoh.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class AudioPlaybackState {
    IDLE,
    BUFFERING,
    PLAYING,
    PAUSED,
    COMPLETED,
    ERROR
}
