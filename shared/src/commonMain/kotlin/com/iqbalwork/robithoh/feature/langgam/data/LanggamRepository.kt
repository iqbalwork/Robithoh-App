package com.iqbalwork.robithoh.feature.langgam.data

import com.iqbalwork.robithoh.core.model.AudioTrack

data class LanggamItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val fileName: String,
    val remoteUrl: String,
    val sizeBytes: Long,
    val sizeLabel: String,
    val drawableName: String,
    val arabicCalligraphyText: String = ""
) {
    fun toAudioTrack(resolvedPath: String? = null): AudioTrack = AudioTrack(
        id = id,
        title = title,
        subtitle = subtitle,
        urlOrPath = resolvedPath ?: fileName,
        artworkUrl = drawableName
    )
}

object LanggamRepository {
    const val GITHUB_AUDIO_BASE_URL = "https://github.com/iqbalwork/Robithoh-App/releases/download/audio-v1"

    val langgamList: List<LanggamItem> = listOf(
        LanggamItem(
            id = "langgam_tarowih",
            title = "Tarowih",
            subtitle = "Abah Aos",
            fileName = "tarowih.mp3",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/tarowih.mp3",
            sizeBytes = 8_938_867L,
            sizeLabel = "8.5 MB",
            drawableName = "tarowih",
            arabicCalligraphyText = "تَرَاوِيح"
        ),
        LanggamItem(
            id = "langgam_irama_dzikir",
            title = "Irama Dzikir",
            subtitle = "Abah Aos",
            fileName = "irama_dzikir.mp3",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/irama_dzikir.mp3",
            sizeBytes = 14_554_523L,
            sizeLabel = "14.0 MB",
            drawableName = "dzikir_jahr",
            arabicCalligraphyText = "الذِّكْر"
        ),
        LanggamItem(
            id = "langgam_dzikir_jahr",
            title = "Dzikir Jahr",
            subtitle = "Abah Aos Berjama'ah",
            fileName = "dzikir_jahr.mp3",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/dzikir_jahr.mp3",
            sizeBytes = 3_253_360L,
            sizeLabel = "3.1 MB",
            drawableName = "dzikir_jahr",
            arabicCalligraphyText = "الذِّكْر"
        ),
        LanggamItem(
            id = "langgam_bani_hasyim",
            title = "Bani Hasyim",
            subtitle = "Abah Aos",
            fileName = "bani_hasyim.mp3",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/bani_hasyim.mp3",
            sizeBytes = 19_681_324L,
            sizeLabel = "19.0 MB",
            drawableName = "bani_hasyim",
            arabicCalligraphyText = "الصَّلَوَات"
        ),
        LanggamItem(
            id = "langgam_fatihah_dhuha",
            title = "Al Fatihah + Ad-dhuha",
            subtitle = "Subuh Rakaat 1",
            fileName = "al_fatihah_ad_dhuha.mpeg",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/al_fatihah_ad_dhuha.mpeg",
            sizeBytes = 471_975L,
            sizeLabel = "461 KB",
            drawableName = "al_fatihah_ad_dhuha",
            arabicCalligraphyText = "الضُّحَى"
        ),
        LanggamItem(
            id = "langgam_fatihah_insyiroh",
            title = "Al-fatihah + Al-insyiroh",
            subtitle = "Subuh Rakaat 2",
            fileName = "al_fatihah_al_insyiroh.mpeg",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/al_fatihah_al_insyiroh.mpeg",
            sizeBytes = 376_971L,
            sizeLabel = "368 KB",
            drawableName = "al_fatihah_al_insyiroh",
            arabicCalligraphyText = "الشَّرْح"
        ),
        LanggamItem(
            id = "langgam_fatihah_kafirun",
            title = "Al-fatihah + Al-kafirun",
            subtitle = "Maghrib Rakaat 1",
            fileName = "al_fatihah_al_kafirun.mpeg",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/al_fatihah_al_kafirun.mpeg",
            sizeBytes = 395_343L,
            sizeLabel = "386 KB",
            drawableName = "al_fatihah_al_kafirun",
            arabicCalligraphyText = "الْكَافِرُون"
        ),
        LanggamItem(
            id = "langgam_fatihah_quraisy",
            title = "Al-fatihah + Al-quraisy",
            subtitle = "Maghrib Rakaat 2",
            fileName = "al_fatihah_al_quraisy.mpeg",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/al_fatihah_al_quraisy.mpeg",
            sizeBytes = 441_336L,
            sizeLabel = "431 KB",
            drawableName = "al_fatihah_al_quraisy",
            arabicCalligraphyText = "قُرَيْش"
        ),
        LanggamItem(
            id = "langgam_fatihah_fil",
            title = "Al-fatihah + Al-fil",
            subtitle = "Isya Rakaat 1",
            fileName = "al_fatihah_al_fill.mpeg",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/al_fatihah_al_fill.mpeg",
            sizeBytes = 452_439L,
            sizeLabel = "442 KB",
            drawableName = "al_fatihah_al_fill",
            arabicCalligraphyText = "الْفِيل"
        ),
        LanggamItem(
            id = "langgam_fatihah_nasr",
            title = "Al-fatihah + An-nasr",
            subtitle = "Isya Rakaat 2",
            fileName = "al_fatihah_an_nasr.mpeg",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/al_fatihah_an_nasr.mpeg",
            sizeBytes = 355_443L,
            sizeLabel = "347 KB",
            drawableName = "al_fatihah_an_nasr",
            arabicCalligraphyText = "النَّصْر"
        ),
        LanggamItem(
            id = "langgam_sholat_jumat",
            title = "Sholat Jumat",
            subtitle = "Langgam TQN PP Suryalaya Sirnarasa",
            fileName = "sholat_jumat.mp3",
            remoteUrl = "$GITHUB_AUDIO_BASE_URL/sholat_jumat.mp3",
            sizeBytes = 4_581_805L,
            sizeLabel = "4.4 MB",
            drawableName = "sholat_jumat",
            arabicCalligraphyText = "الْجُمُعَة"
        )
    )

    fun findById(id: String): LanggamItem? = langgamList.find { it.id == id }

    fun findByFileName(fileName: String): LanggamItem? = langgamList.find { it.fileName == fileName }
}
