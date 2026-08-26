package com.iqbalwork.robithoh.feature.profile.data

import com.iqbalwork.robithoh.feature.profile.model.InstitutionItem
import com.iqbalwork.robithoh.feature.profile.model.PesantrenProfile

object ProfileData {

    val sirnarasaProfile = PesantrenProfile(
        name = "Pondok Pesantren Sirnarasa",
        location = "Ciceuri, Panjalu, Kabupaten Ciamis, Jawa Barat",
        tagline = "Pusat Peradaban Dzikir TQN Silsilah 38 & Tasawuf Terpadu",
        historyText = """
Pondok Pesantren Sirnarasa didirikan dan dibina langsung oleh Pangersa Guru Agung Syekh Muhammad Abdul Gaos Saefulloh Maslul (Pangersa Abah Aos Ra. Qs.) di Dusun Ciceuri, Desa Ciomas, Kecamatan Panjalu, Kabupaten Ciamis, Jawa Barat.

Pesantren ini berakar kokoh pada ajaran Thoriqoh Qodiriyyah Naqsyabandiyyah (TQN) Pondok Pesantren Suryalaya yang diwariskan oleh Syekh Abdullah Mubarok bin Nur Muhammad (Abah Sepuh) dan Syekh Ahmad Shohibulwafa Tajul Arifin (Abah Anom). Sirnarasa menjadi episentrum pengembangan peradaban dzikirullah, keilmuan tasawuf, pendidikan tinggi Islam, perekonomian syariah berbasis pesantren, dan pemberdayaan umat lintas nusantara hingga mancanegara.
        """.trimIndent(),
        mursyidName = "Syekh Muhammad Abdul Gaos Saefulloh Maslul (Pangersa Abah Aos) Ra. Qs.",
        mursyidTitle = "Mursyid Thoriqoh Qodiriyyah Naqsyabandiyyah Silsilah ke-38",
        mursyidBiography = """
Pangersa Abah Aos Ra. Qs. adalah Mursyid Agung TQN penerus amanah kemursyidan dari Pangersa Abah Anom r.a. Beliau membimbing jutaan ikhwan/akhwat dalam melanggengkan dzikir jahr dan khofi, menegakkan sholat sunnah dan amaliyah malam, menyebarkan Kitab Manaqib Syekh Abdul Qodir Al-Jailani r.a., serta mengawal tegaknya nilai-nilai luhur Tanbih dalam kehidupan berbangsa dan bernegara.
        """.trimIndent(),
        institutions = listOf(
            InstitutionItem(
                id = "stid",
                name = "STID Sirnarasa (Sekolah Tinggi Ilmu Dakwah)",
                acronym = "STID Sirnarasa",
                description = "Perguruan tinggi Islam pencetak sarjana dakwah dan komunikasi Islam berakhlak tasawuf modern.",
                roleCategory = "Pendidikan Tinggi",
                logoDrawable = "logo_sirnarasa.png"
            ),
            InstitutionItem(
                id = "baitul_asror",
                name = "Baitul Asror",
                acronym = "Baitul Asror",
                description = "Pusat pembinaan spiritual, suluk, dan markaz dzikir terpadu ikhwan TQN Sirnarasa.",
                roleCategory = "Spiritual & Dzikir",
                logoDrawable = "logo_baitul_asror.png"
            ),
            InstitutionItem(
                id = "hipda",
                name = "HIPDA (Himpunan Pemuda & Pelajar Sirnarasa)",
                acronym = "HIPDA",
                description = "Wadah kaderisasi pemuda, pelajar, dan santri dalam kepemimpinan dan pengabdian umat.",
                roleCategory = "Kepemudaan",
                logoDrawable = "hipda.png"
            ),
            InstitutionItem(
                id = "bms",
                name = "BMS (Baitul Maal Sirnarasa)",
                acronym = "BMS",
                description = "Lembaga pengelola zakat, infaq, shadaqah, dan wakaf produktif untuk kemaslahatan umat.",
                roleCategory = "Filantropi & Keuangan",
                logoDrawable = "bms.png"
            ),
            InstitutionItem(
                id = "sri",
                name = "SRI (Sirnarasa Research Institute)",
                acronym = "SRI",
                description = "Lembaga riset tasawuf, manuskrip keislaman, dan kajian sains spiritual terapan.",
                roleCategory = "Riset & Manuskrip",
                logoDrawable = "kra.png"
            ),
            InstitutionItem(
                id = "trensmart",
                name = "Trensmart",
                acronym = "Trensmart",
                description = "Platform digital dan jejaring kewirausahaan santri untuk kemandirian ekonomi pesantren.",
                roleCategory = "Ekonomi Santri",
                logoDrawable = "amjad.png"
            ),
            InstitutionItem(
                id = "kra",
                name = "KRA (Koperasi Rakyat Amanah)",
                acronym = "KRA",
                description = "Unit koperasi syariah penyedia kebutuhan harian warga pesantren dan jamaah majlis.",
                roleCategory = "Koperasi Syariah",
                logoDrawable = "kra.png"
            ),
            InstitutionItem(
                id = "amjad",
                name = "Amjad (Yayasan & Media Dakwah)",
                acronym = "Amjad",
                description = "Lembaga media penyiaran dakwah, publikasi kitab manaqib, dan siaran multimedia majlis.",
                roleCategory = "Media & Publikasi",
                logoDrawable = "amjad.png"
            )
        )
    )
}
