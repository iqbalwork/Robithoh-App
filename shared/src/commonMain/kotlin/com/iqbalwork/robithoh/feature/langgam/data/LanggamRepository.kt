package com.iqbalwork.robithoh.feature.langgam.data

import com.iqbalwork.robithoh.core.model.AudioTrack

data class LanggamItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val fileName: String,
    val drawableName: String,
    val arabicCalligraphyText: String = ""
) {
    fun toAudioTrack(): AudioTrack = AudioTrack(
        id = id,
        title = title,
        subtitle = subtitle,
        urlOrPath = fileName,
        artworkUrl = drawableName
    )
}

object LanggamRepository {
    val langgamList: List<LanggamItem> = listOf(
        LanggamItem(
            id = "langgam_tarowih",
            title = "Tarowih",
            subtitle = "Abah Aos",
            fileName = "tarowih.mp3",
            drawableName = "tarowih",
            arabicCalligraphyText = "تَرَاوِيح"
        ),
        LanggamItem(
            id = "langgam_irama_dzikir",
            title = "Irama Dzikir",
            subtitle = "Abah Aos",
            fileName = "irama_dzikir.mp3",
            drawableName = "dzikir_jahr",
            arabicCalligraphyText = "الذِّكْر"
        ),
        LanggamItem(
            id = "langgam_dzikir_jahr",
            title = "Dzikir Jahr",
            subtitle = "Abah Aos Berjama'ah",
            fileName = "dzikir_jahr.mp3",
            drawableName = "dzikir_jahr",
            arabicCalligraphyText = "الذِّكْر"
        ),
        LanggamItem(
            id = "langgam_bani_hasyim",
            title = "Bani Hasyim",
            subtitle = "Abah Aos",
            fileName = "bani_hasyim.mp3",
            drawableName = "bani_hasyim",
            arabicCalligraphyText = "الصَّلَوَات"
        ),
        LanggamItem(
            id = "langgam_fatihah_dhuha",
            title = "Al Fatihah + Ad-dhuha",
            subtitle = "Subuh Rakaat 1",
            fileName = "al_fatihah_ad_dhuha.mpeg",
            drawableName = "al_fatihah_ad_dhuha",
            arabicCalligraphyText = "الضُّحَى"
        ),
        LanggamItem(
            id = "langgam_fatihah_insyiroh",
            title = "Al-fatihah + Al-insyiroh",
            subtitle = "Subuh Rakaat 2",
            fileName = "al_fatihah_al_insyiroh.mpeg",
            drawableName = "al_fatihah_al_insyiroh",
            arabicCalligraphyText = "الشَّرْح"
        ),
        LanggamItem(
            id = "langgam_fatihah_kafirun",
            title = "Al-fatihah + Al-kafirun",
            subtitle = "Maghrib Rakaat 1",
            fileName = "al_fatihah_al_kafirun.mpeg",
            drawableName = "al_fatihah_al_kafirun",
            arabicCalligraphyText = "الْكَافِرُون"
        ),
        LanggamItem(
            id = "langgam_fatihah_quraisy",
            title = "Al-fatihah + Al-quraisy",
            subtitle = "Maghrib Rakaat 2",
            fileName = "al_fatihah_al_quraisy.mpeg",
            drawableName = "al_fatihah_al_quraisy",
            arabicCalligraphyText = "قُرَيْش"
        ),
        LanggamItem(
            id = "langgam_fatihah_fil",
            title = "Al-fatihah + Al-fil",
            subtitle = "Isya Rakaat 1",
            fileName = "al_fatihah_al_fill.mpeg",
            drawableName = "al_fatihah_al_fill",
            arabicCalligraphyText = "الْفِيل"
        ),
        LanggamItem(
            id = "langgam_fatihah_nasr",
            title = "Al-fatihah + An-nasr",
            subtitle = "Isya Rakaat 2",
            fileName = "al_fatihah_an_nasr.mpeg",
            drawableName = "al_fatihah_an_nasr",
            arabicCalligraphyText = "النَّصْر"
        ),
        LanggamItem(
            id = "langgam_sholat_jumat",
            title = "Sholat Jumat",
            subtitle = "Langgam TQN Sirnarasa",
            fileName = "sholat_jumat.mp3",
            drawableName = "sholat_jumat",
            arabicCalligraphyText = "الْجُمُعَة"
        )
    )
}
