package com.iqbalwork.robithoh.core.model

import kotlinx.serialization.Serializable

@Serializable
data class AudioTrack(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val urlOrPath: String,
    val durationMs: Long = 0L,
    val artworkUrl: String? = null
)
