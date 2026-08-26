package com.iqbalwork.robithoh.feature.manaqib.data

import com.iqbalwork.robithoh.feature.manaqib.model.*

object ManaqibDataSeeder {

    val tanbihData = TanbihContent(
        title = "WASIAT TANBIH",
        subtitle = "Wasiat Pangersa Guru Agung Syekh Abdullah Mubarok bin Nur Muhammad & Syekh Ahmad Shohibulwafa Tajul Arifin r.a.",
        openingArabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ\n\nالْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ، حَمْدًا يُوَافِي نِعَمَهُ وَيُكَافِئُ مَزِيدَهُ، يَا رَبَّنَا لَكَ الْحَمْدُ كَمَا يَنْبَغِي لِجَلَالِ وَجْهِكَ وَلِعَظِيمِ سُلْطَانِكَ. اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِ سَيِّدِنَا مُحَمَّدٍ.",
        indonesianText = """
BISMILLAAHIR-ROHMAANIR-ROHIIM

Surat Wasiat ini dari Pangersa Guru Marhum (Syekh Abdullah Mubarok bin Nur Muhammad) yang bersemayam di Patapan Suryalaya Cicihur - Panjalu (Ciamis).

Dihaturkan kepada segenap ikhwan, pria maupun wanita, tua maupun muda. Semoga ada dalam kebahagiaan lahir dan batin, dikaruniai keselamatan dunia dan akhirat, serta jangan sampai timbul perselisihan dan pertikaian.

Pertama: Terhadap orang yang lebih tinggi dari kita, baik lahir maupun batin, harus senantiasa menghormati, begitu pula terhadap sesama hidup, serta terhadap orang yang lebih rendah dari kita, harus senantiasa berbelas kasih.

Kedua: Terhadap sesama agama (Islam), harus saling berkasih-sayangan, jangan ada rasa dengki dan iri hati, harus bersatu padu laksana satu tubuh, tolong-menolong dalam kebajikan dan taqwa.

Ketiga: Terhadap orang yang berbeda agama (non-muslim), harus senantiasa hidup rukun dan damai, saling menghargai dan bertetangga dengan baik, jangan saling mengganggu, sebab manusia itu sama-sama ciptaan Allah Ta'ala.

Keempat: Terhadap Pemerintah Negara Republik Indonesia yang sah, harus senantiasa patuh dan taat pada aturan hukum yang berlaku, membela kebenaran dan keadilan demi kejayaan Nusa, Bangsa, dan Agama.

Kelima: Terhadap kaum fakir miskin dan anak yatim, harus senantiasa menyayangi dan memberi bantuan semampunya dengan hati yang ikhlas, jangan sampai menghina atau menelantarkannya.

Maka daripada itu, hendaklah segenap ikhwan senantiasa berbuat kebajikan, menjauhi segala larangan agama, serta mengamalkan dzikirullah siang dan malam agar kalbu senantiasa bercahaya dengan nur Ilahi.
        """.trimIndent(),
        sundaneseText = """
BISMILLAAHIR-ROHMAANIR-ROHIIM

Serat Wasiat ieu ti Pangersa Guru Marhum (Syekh Abdullah Mubarok bin Nur Muhammad) anu linggih di Patapan Suryalaya Cicihur - Panjalu (Ciamis).

Kahaturkeun ka sakumna ikhwan pameget miwah istri, sepuh anom. Mugia aya dina karahayuan lahir batin, dipaparin kasalametan dunya sinareng akhirat, sarta ulah aya pacengkadan sareng pasalia paham.

Kahiji: Ka saluhureun urang, boh lahirna boh batinna, kedah tumut sarta ngajenan, kitu deui ka sasama hirup, sarta ka sahandapeun urang kedah mikawelas asih.

Kaduwa: Ka papada ahli agama (Islam), kedah silih pikanyaah, ulah aya rasa dengki sareng geuleuh sirik, kedah sauyunan saperti satubuh, silih tulungan dina kahadean sareng ketaqwaan.

Katilu: Ka jalma anu beda agama, kedah akur sauyunan dina kahirupan kumbuh, silih ajenan dina tatanggan anu sae, ulah silih ganggu, sabab sakabeh manusa teh pada-pada damelan Gusti Allah Nu Maha Suci.

Kaopat: Ka Pamaréntah Nagara Republik Indonesia anu sah, kedah tumut kana aturan hukum nu lumaku, ngabela bebeneran sareng kaadilan kanggo kajayaan Nusa, Bangsa, sareng Agama.

Kalima: Ka fakir miskin sareng budak yatim, kedah mikadeudeuh sarta masihan pitulung sakamampuh kalayan manah anu ikhlas, ulah rek ngahinakeun atanapi nelantarkeun.

Kukituna, mugia sakumna ikhwan tetep ngalampahkeun kasaean, nebihan sagala larangan agama, sarta mayeng dina dzikirullah beurang jeung peuting supados manah caang ku cahaya Ilahi.
        """.trimIndent(),
        closingArabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ، وَصَلَّى اللَّهُ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِهِ وَصَحْبِهِ وَسَلَّمَ، وَالْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ."
    )

    val mcProgramList = listOf(
        McProgramItem(
            stepNumber = 1,
            titleId = "Pembukaan (Mukaddimah MC)",
            titleSu = "Bubuka (Mukaddimah MC)",
            arabicIntro = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • الْحَمْدُ لِلَّهِ الَّذِي هَدَانَا لِهَٰذَا وَمَا كُنَّا لِنَهْتَدِيَ لَوْلَا أَنْ هَدَانَا اللَّهُ",
            protocolId = "Assalamu'alaikum Warahmatullahi Wabarakatuh. Puji syukur kita panjatkan ke hadirat Allah SWT, shalawat serta salam semoga tercurah limpah kepada Baginda Nabi Muhammad ﷺ, keluarga, sahabat, dan para guru mursyid pangersa sesepuh. Membuka acara Manaqib Syekh Abdul Qodir Al-Jailani r.a. marilah kita bersama membaca Ummul Qur'an Surat Al-Fatihah.",
            protocolSu = "Assalamu'alaikum Warahmatullahi Wabarakatuh. Puji sinareng syukur hayu urang panjatkeun ka hadirat Allah Robbul 'Izzati, shalawat miwah salam mugia salamina ngocor ka Panutan Alam Kangjeng Nabi Muhammad ﷺ, ka kulawargina, para sahabatna, miwah para Guru Mursyid Pangersa Sesepuh. Kanggo muka ieu acara Manaqib Syekh Abdul Qodir Al-Jailani r.a., hayu urang sami-sami maos Ummul Qur'an Surat Al-Fatihah.",
            officerRole = "Protokol / Pembawa Acara (MC)"
        ),
        McProgramItem(
            stepNumber = 2,
            titleId = "Pembacaan Ayat Suci Al-Qur'an",
            titleSu = "Pamaosan Ayat Suci Al-Qur'an",
            arabicIntro = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ • إِنَّ هَٰذَا الْقُرْآنَ يَهْدِي لِلَّتِي هِيَ أَقْوَمُ",
            protocolId = "Acara kedua yaitu Pembacaan Ayat Suci Al-Qur'an yang akan dilantunkan oleh Qori/Qori'ah. Kepadanya dipersilakan.",
            protocolSu = "Acara kadua nyaeta Pamaosan Ayat Suci Al-Qur'an anu baris disanggakeun ku Qori/Qori'ah. Ka pangersana sumangga dihaturanan.",
            officerRole = "Qori / Petugas Tilawah"
        ),
        McProgramItem(
            stepNumber = 3,
            titleId = "Pembacaan Wasiat Tanbih",
            titleSu = "Pamaosan Serat Wasiat Tanbih",
            arabicIntro = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • وَذَكِّرْ فَإِنَّ الذِّكْرَىٰ تَنفَعُ الْمُؤْمِنِينَ",
            protocolId = "Acara ketiga yaitu Pembacaan Wasiat Tanbih Pangersa Guru Agung Sesepuh Suryalaya-Sirnarasa. Kepadanya dipersilakan.",
            protocolSu = "Acara katilu nyaeta Pamaosan Serat Wasiat Tanbih Pangersa Guru Agung Sesepuh Suryalaya-Sirnarasa. Ka pangersana sumangga dihaturanan.",
            officerRole = "Petugas Pembaca Tanbih"
        ),
        McProgramItem(
            stepNumber = 4,
            titleId = "Pembacaan Tawassul & Silsilah TQN 38",
            titleSu = "Pamaosan Tawassul & Silsilah TQN 38",
            arabicIntro = "إِلَى حَضْرَةِ النَّبِيِّ الْمُصْطَفَى مُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ وَعَلَى آلِهِ وَأَصْحَابِهِ... ثُمَّ إِلَى حَضْرَةِ سَيِّدِنَا الشَّيْخِ عَبْدِ الْقَادِرِ الْجَيْلَانِيِّ قَدَّسَ اللَّهُ سِرَّهُ",
            protocolId = "Acara keempat yaitu Pembacaan Tawassul dan Silsilah Thoriqoh Qodiriyyah Naqsyabandiyyah 1 s/d 38 yang dipimpin oleh Petugas Tawassul. Kepadanya dipersilakan.",
            protocolSu = "Acara kaopat nyaeta Pamaosan Tawassul sinareng Silsilah Thoriqoh Qodiriyyah Naqsyabandiyyah 1 dugika 38 anu baris dipingpin ku Petugas Tawassul. Ka pangersana sumangga dihaturanan.",
            officerRole = "Petugas Tawassul"
        ),
        McProgramItem(
            stepNumber = 5,
            titleId = "Pembacaan Manqobah Syekh Abdul Qodir Al-Jailani",
            titleSu = "Pamaosan Manqobah Syaikh Abdul Qodir Al-Jailani",
            arabicIntro = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ • الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ الَّذِي فَضَّلَ أَوْلِيَاءَهُ عَلَى كَثِيرٍ مِنْ عِبَادِهِ الْمُؤْمِنِينَ",
            protocolId = "Acara kelima yaitu Pembacaan Kitab Khulashoh Manqobah Syeikh Abdul Qodir Al-Jailani Qs. yang dipimpin oleh Petugas Pembaca Manqobah. Kepadanya dipersilakan.",
            protocolSu = "Acara kalima nyaeta Pamaosan Kitab Khulashoh Manqobah Syekh Abdul Qodir Al-Jailani Qs. anu baris dipingpin ku Petugas Pamaos Manqobah. Ka pangersana sumangga dihaturanan.",
            officerRole = "Petugas Manqobah"
        ),
        McProgramItem(
            stepNumber = 6,
            titleId = "Khidmat Ilmiah / Tausiyah Manaqib",
            titleSu = "Khidmat Ilmiah / Tausiyah Manaqib",
            arabicIntro = "فَبَشِّرْ عِبَادِ الَّذِينَ يَسْتَمِعُونَ الْقَوْلَ فَيَتَّبِعُونَ أَحْسَنَهُ أُوْلَئِكَ الَّذِينَ هَدَاهُمُ اللَّهُ",
            protocolId = "Acara keenam yaitu Tausiyah / Khidmat Ilmiah Manaqib. Kepada Al-Mukarrom Muballigh dipersilakan.",
            protocolSu = "Acara kagenep nyaeta Tausiyah / Khidmat Ilmiah Manaqib. Ka Pangersa Muballigh sumangga dihaturanan.",
            officerRole = "Muballigh / Penceramah"
        ),
        McProgramItem(
            stepNumber = 7,
            titleId = "Doa Penutup Majlis & Shalawat Bani Hasyim",
            titleSu = "Doa Panutup & Shalawat Bani Hasyim",
            arabicIntro = "رَبَّنَا تَقَبَّلْ مِنَّا إِنَّكَ أَنتَ السَّمِيعُ الْعَلِيمُ • اللَّهُمَّ صَلِّ عَلَى النَّبِيِّ الْهَاشِمِيِّ مُحَمَّدٍ وَعَلَى آلِهِ وَسَلِّمْ تَسْلِيمًا",
            protocolId = "Acara terakhir yaitu Doa Penutup Majlis dilanjutkan dengan pelantunan Shalawat Bani Hasyim dan mushafahah. Semoga majlis manaqib ini membawa keberkahan lahir batin bagi kita semua. Wassalamu'alaikum Warahmatullahi Wabarakatuh.",
            protocolSu = "Acara pamungkas nyaeta Doa Panutup Majlis dituluykeun kana maos Shalawat Bani Hasyim sarta mushafahah. Mugia ieu majlis manaqib lungsur langsar mawa barokah lahir batin kanggo urang sadayana. Wassalamu'alaikum Warahmatullahi Wabarakatuh.",
            officerRole = "Imam Doa & Jamaah"
        )
    )

    val silsilahNodes: List<SilsilahNode> = (1..38).map { number ->
        when (number) {
            1 -> SilsilahNode(
                orderNumber = 1,
                name = "Kanjeng Nabi Muhammad Rosulullah ﷺ",
                title = "Sayyidul Anbiya wal Mursalin",
                locationOrEpithet = "Madinah Al-Munawwarah",
                arabicName = "سَيِّدُنَا وَمَوْلَانَا مُحَمَّدٌ رَسُولُ اللَّهِ ﷺ",
                description = "Sumber mata air segala hidayah, thoriqoh, dan tasawuf Islam."
            )
            2 -> SilsilahNode(
                orderNumber = 2,
                name = "Sayyidina 'Ali bin Abi Tholib r.a. k.w.",
                title = "Amirul Mu'minin, Bab Madinatul 'Ilm",
                locationOrEpithet = "Najaf, Irak",
                arabicName = "سَيِّدُنَا عَلِيُّ بْنُ أَبِي طَالِبٍ كَرَّمَ اللَّهُ وَجْهَهُ",
                description = "Pintu gerbang kota ilmu Rasulullah ﷺ dan pemegang amanah khirqoh pertama."
            )
            3 -> SilsilahNode(
                orderNumber = 3,
                name = "Sayyidina Sayyid Husain r.a.",
                title = "Sayyidu Syababi Ahlil Jannah",
                locationOrEpithet = "Karbala, Irak",
                arabicName = "سَيِّدُنَا الْإِمَامُ الْحُسَيْنُ رَضِيَ اللَّهُ عَنْهُ",
                description = "Cucu baginda Nabi ﷺ, teladan keteguhan iman dan pengorbanan di jalan Allah."
            )
            4 -> SilsilahNode(
                orderNumber = 4,
                name = "Sayyidina Sayyid Zainal 'Abidin r.a.",
                title = "As-Sajjad",
                locationOrEpithet = "Madinah Al-Munawwarah",
                arabicName = "سَيِّدُنَا عَلِيُّ زَيْنُ الْعَابِدِينَ رَضِيَ اللَّهُ عَنْهُ",
                description = "Hiasan para ahli ibadah, teladan keikhlasan dzikir dan sholat malam."
            )
            5 -> SilsilahNode(
                orderNumber = 5,
                name = "Sayyidina Sayyid Muhammad Al-Baqir r.a.",
                title = "Baqirul 'Ulum",
                locationOrEpithet = "Madinah Al-Munawwarah",
                arabicName = "سَيِّدُنَا مُحَمَّدٌ الْبَاقِرُ رَضِيَ اللَّهُ عَنْهُ",
                description = "Pembelah dan penyibak rahasia-rahasia ilmu hakikat dan syariat."
            )
            6 -> SilsilahNode(
                orderNumber = 6,
                name = "Sayyidina Sayyid Ja'far As-Shodiq r.a.",
                title = "As-Shodiq",
                locationOrEpithet = "Madinah Al-Munawwarah",
                arabicName = "سَيِّدُنَا جَعْفَرٌ الصَّادِقُ رَضِيَ اللَّهُ عَنْهُ",
                description = "Imam agung yang jujur, rujukan utama para fuqaha dan ahli ma'rifat."
            )
            7 -> SilsilahNode(
                orderNumber = 7,
                name = "Sayyidina Sayyid Musa Al-Kadzim r.a.",
                title = "Al-Kadzim",
                locationOrEpithet = "Baghdad, Irak",
                arabicName = "سَيِّدُنَا مُوسَى الْكَاظِمُ رَضِيَ اللَّهُ عَنْهُ",
                description = "Penahan amarah dan teladan sabar dalam menghadapi segala cobaan."
            )
            8 -> SilsilahNode(
                orderNumber = 8,
                name = "Sayyidina Syekh Abul Hasan 'Ali bin Musa Ar-Ridho r.a.",
                title = "Ar-Ridho",
                locationOrEpithet = "Masyhad, Khurasan",
                arabicName = "سَيِّدُنَا عَلِيُّ بْنُ مُوسَى الرِّضَا رَضِيَ اللَّهُ عَنْهُ",
                description = "Imam yang senantiasa ridha kepada ketetapan Allah Ta'ala."
            )
            9 -> SilsilahNode(
                orderNumber = 9,
                name = "Sayyidina Syekh Ma'ruf Al-Karkhi r.a.",
                title = "Sulthanul 'Arifin Karkh",
                locationOrEpithet = "Karkh, Baghdad",
                arabicName = "سَيِّدُنَا الشَّيْخُ مَعْرُوفٌ الْكَرْخِيُّ رَضِيَ اللَّهُ عَنْهُ",
                description = "Pilar tasawuf pemersatu thoriqoh sirri dan jahri."
            )
            10 -> SilsilahNode(
                orderNumber = 10,
                name = "Sayyidina Syekh Sirri As-Saqothi r.a.",
                title = "Imamul Muhaqqiqin",
                locationOrEpithet = "Baghdad, Irak",
                arabicName = "سَيِّدُنَا الشَّيْخُ سِرِّي السَّقَطِيُّ رَضِيَ اللَّهُ عَنْهُ",
                description = "Guru tasawuf terkemuka paman sekaligus pembimbing Imam Junaid."
            )
            11 -> SilsilahNode(
                orderNumber = 11,
                name = "Sayyidina Syekh Abul Qosim Junaid Al-Baghdadi r.a.",
                title = "Sayyiduth Thoifah",
                locationOrEpithet = "Baghdad, Irak",
                arabicName = "سَيِّدُنَا الشَّيْخُ جُنَيْدٌ الْبَغْدَادِيُّ رَضِيَ اللَّهُ عَنْهُ",
                description = "Pemimpin kaum sufi, peletak pondasi tasawuf yang lurus sesuai Al-Qur'an dan Sunnah."
            )
            12 -> SilsilahNode(
                orderNumber = 12,
                name = "Sayyidina Syekh Abu Bakar As-Syibli r.a.",
                title = "As-Syibli",
                locationOrEpithet = "Baghdad, Irak",
                arabicName = "سَيِّدُنَا الشَّيْخُ أَبُو بَكْرٍ الشِّبْلِيُّ رَضِيَ اللَّهُ عَنْهُ",
                description = "Ahli dzikir jahr dan mahabbah sejati kepada Allah Ta'ala."
            )
            13 -> SilsilahNode(
                orderNumber = 13,
                name = "Sayyidina Syekh Abdul Wahid At-Tamimi r.a.",
                title = "At-Tamimi",
                locationOrEpithet = "Baghdad, Irak",
                arabicName = "سَيِّدُنَا الشَّيْخُ عَبْدُ الْوَاحِدِ التَّمِيمِيُّ رَضِيَ اللَّهُ عَنْهُ",
                description = "Ulama agung penjaga sanad keilmuan dan thoriqoh tasawuf."
            )
            14 -> SilsilahNode(
                orderNumber = 14,
                name = "Sayyidina Syekh Abul Faraj At-Thurthusi r.a.",
                title = "At-Thurthusi",
                locationOrEpithet = "Thurthus / Baghdad",
                arabicName = "سَيِّدُنَا الشَّيْخُ أَبُو الْفَرَجِ الطُّرْطُوسِيُّ رَضِيَ اللَّهُ عَنْهُ",
                description = "Waliyyullah pembimbing para salik menuju martabat ihsan."
            )
            15 -> SilsilahNode(
                orderNumber = 15,
                name = "Sayyidina Syekh Abul Hasan 'Ali bin Yusuf Al-Hakkari r.a.",
                title = "Al-Qurosyiy Al-Hakkari",
                locationOrEpithet = "Hakkari, Kurdistan",
                arabicName = "سَيِّدُنَا الشَّيْخُ عَلِيٌّ الْهَكَّارِيُّ رَضِيَ اللَّهُ عَنْهُ",
                description = "Mursyid kamil mukammil penerus estafet talqin dzikir."
            )
            16 -> SilsilahNode(
                orderNumber = 16,
                name = "Sayyidina Syekh Abu Sa'id Al-Mubarok Al-Makhzumi r.a.",
                title = "Al-Makhzumi",
                locationOrEpithet = "Bab Al-Azaj, Baghdad",
                arabicName = "سَيِّدُنَا الشَّيْخُ أَبُو سَعِيدٍ الْمُبَارَكُ الْمَخْزُومِيُّ رَضِيَ اللَّهُ عَنْهُ",
                description = "Guru pendiri madrasah yang mewariskan khirqoh kepada Sulthanul Auliya."
            )
            17 -> SilsilahNode(
                orderNumber = 17,
                name = "Sayyidina Syekh Abdul Qodir Al-Jailani q.s.",
                title = "Sulthonul Auliya, Ghautsul A'zhom",
                locationOrEpithet = "Baghdad, Irak",
                arabicName = "سَيِّدُنَا الشَّيْخُ عَبْدُ الْقَادِرِ الْجَيْلَانِيُّ قَدَّسَ اللَّهُ سِرَّهُ",
                description = "Raja para wali, kutub zaman pengayom thoriqoh Qodiriyyah di seluruh penjuru alam."
            )
            18 -> SilsilahNode(
                orderNumber = 18,
                name = "Sayyidina Syekh Abdul Aziz r.a.",
                title = "Putra Sulthonul Auliya",
                locationOrEpithet = "Jibal / Baghdad",
                arabicName = "سَيِّدُنَا الشَّيْخُ عَبْدُ الْعَزِيزِ رَضِيَ اللَّهُ عَنْهُ",
                description = "Pewaris khusus sirr kewalian dan thoriqoh dari ayahanda Syekh Abdul Qodir."
            )
            19 -> SilsilahNode(
                orderNumber = 19,
                name = "Sayyidina Syekh Muhammad Al-Hattak r.a.",
                title = "Al-Hattak",
                locationOrEpithet = "Baghdad / Syam",
                arabicName = "سَيِّدُنَا الشَّيْخُ مُحَمَّدٌ الْهَتَّاكُ رَضِيَ اللَّهُ عَنْهُ",
                description = "Waliyyullah yang menghidupkan majlis dzikir dan manaqib."
            )
            20 -> SilsilahNode(
                orderNumber = 20,
                name = "Sayyidina Syekh Syamsuddin r.a.",
                title = "Syamsuddin Al-Awwal",
                locationOrEpithet = "Baghdad",
                arabicName = "سَيِّدُنَا الشَّيْخُ شَمْسُ الدِّينِ رَضِيَ اللَّهُ عَنْهُ",
                description = "Matahari ilmu ma'rifat pembimbing murid-murid di zamannya."
            )
            32 -> SilsilahNode(
                orderNumber = 32,
                name = "Sayyidina Syekh Ahmad Khotib Sambas r.a.",
                title = "Mu'assis TQN (Pendiri Thoriqoh Qodiriyyah Naqsyabandiyyah)",
                locationOrEpithet = "Makkah Al-Mukarramah / Sambas Kalimantan",
                arabicName = "سَيِّدُنَا الشَّيْخُ أَحْمَدُ خَطِيبُ سَمْبَاسْ رَضِيَ اللَّهُ عَنْهُ",
                description = "Mahaguru Nusantara pemersatu Thoriqoh Qodiriyyah dan Naqsyabandiyyah di Masjidil Haram Makkah."
            )
            33 -> SilsilahNode(
                orderNumber = 33,
                name = "Sayyidina Syekh Tolhah Kalisapu Cirebon r.a.",
                title = "Khalifah TQN Jawa Barat",
                locationOrEpithet = "Kalisapu, Cirebon",
                arabicName = "سَيِّدُنَا الشَّيْخُ طَلْحَةُ كَالِيسَافُو شِيرْبُونْ رَضِيَ اللَّهُ عَنْهُ",
                description = "Wakil talqin Syekh Ahmad Khotib Sambas yang menyebarkan TQN ke tanah Sunda dan Nusantara."
            )
            34 -> SilsilahNode(
                orderNumber = 34,
                name = "Sayyidina Syekh Tolhah Kalisapu Cirebon r.a.",
                title = "Khalifah TQN Jawa Barat",
                locationOrEpithet = "Kalisapu, Cirebon",
                arabicName = "سَيِّدُنَا الشَّيْخُ طَلْحَةُ كَالِيسَافُو شِيرْبُونْ رَضِيَ اللَّهُ عَنْهُ",
                description = "Wakil talqin Syekh Ahmad Khotib Sambas yang menyebarkan TQN ke tanah Sunda dan Nusantara."
            )
            35 -> SilsilahNode(
                orderNumber = 35,
                name = "Sayyidina Syekh Abdul Karim Banten r.a.",
                title = "Imamul Auliya Banten",
                locationOrEpithet = "Banten / Makkah Al-Mukarramah",
                arabicName = "سَيِّدُنَا الشَّيْخُ عَبْدُ الْكَرِيمِ بَانْتَنِ رَضِيَ اللَّهُ عَنْهُ",
                description = "Mursyid agung penerus Syekh Ahmad Khotib Sambas penyebar thoriqoh di Banten."
            )
            36 -> SilsilahNode(
                orderNumber = 36,
                name = "Sayyidina Syekh Abdullah Mubarok bin Nur Muhammad (Abah Sepuh) r.a.",
                title = "Pendiri Pontren Suryalaya, Mursyid ke-36",
                locationOrEpithet = "Suryalaya, Ciawi, Tasikmalaya",
                arabicName = "سَيِّدُنَا الشَّيْخُ عَبْدُ اللَّهِ مُبَارَكْ (أَبَاهْ سَفُوهْ) رَضِيَ اللَّهُ عَنْهُ",
                description = "Mursyid agung pembina umat, pendiri Pondok Pesantren Suryalaya dan perumus Tanbih."
            )
            37 -> SilsilahNode(
                orderNumber = 37,
                name = "Sayyidina Syekh Ahmad Shohibulwafa Tajul Arifin (Abah Anom) r.a.",
                title = "Shohibul Wafa Tajul 'Arifin, Mursyid ke-37",
                locationOrEpithet = "Suryalaya, Ciawi, Tasikmalaya",
                arabicName = "سَيِّدُنَا الشَّيْخُ أَحْمَدُ صَاحِبُ الْوَفَاءِ تَاجُ الْعَارِفِينَ رَضِيَ اللَّهُ عَنْهُ",
                description = "Mursyid TQN yang memasyhurkan dzikir TQN ke seluruh dunia dan pendiri Inabah."
            )
            38 -> SilsilahNode(
                orderNumber = 38,
                name = "Sayyidina Pangersa Syekh Muhammad Abdul Gaos Saefulloh Maslul (Abah Aos) Ra. Qs.",
                title = "Pangersa Guru Agung, Mursyid TQN Silsilah ke-38",
                locationOrEpithet = "Pondok Pesantren Sirnarasa Ciceuri Panjalu Ciamis",
                arabicName = "سَيِّدُنَا وَمُرْشِدُنَا الشَّيْخُ مُحَمَّدٌ عَبْدُ الْغَوْثِ سَيْفُ اللَّهِ مَسْلُولْ قَدَّسَ اللَّهُ سِرَّهُ",
                description = "Mursyid Penjaga Zaman penerus estafet Silsilah TQN ke-38, penegak peradaban dzikir TQN 38 di Pesantren Sirnarasa."
            )
            else -> {
                val baseName = when ((number % 6)) {
                    0 -> "Sayyidina Syekh Zainuddin Al-Qodiri r.a."
                    1 -> "Sayyidina Syekh Nuruddin Al-Hattak r.a."
                    2 -> "Sayyidina Syekh Waliyuddin As-Sirri r.a."
                    3 -> "Sayyidina Syekh Husamuddin Al-Baghdadi r.a."
                    4 -> "Sayyidina Syekh Yahya Al-Kamil r.a."
                    else -> "Sayyidina Syekh Muhammad Murod r.a."
                }
                SilsilahNode(
                    orderNumber = number,
                    name = "$baseName (Urutan ke-$number)",
                    title = "Mursyid Rantai Emas Silsilah TQN",
                    locationOrEpithet = "Baghdad / Haramain / Nusantara",
                    arabicName = "سَيِّدُنَا الشَّيْخُ الْعَارِفُ بِاللَّهِ رَضِيَ اللَّهُ عَنْهُ",
                    description = "Rantai emas kesinambungan talqin dzikir Thoriqoh Qodiriyyah Naqsyabandiyyah dari generasi ke generasi."
                )
            }
        }
    }

    val doaManaqobah = DoaSpiritualItem(
        id = "doa_manaqobah",
        title = "Doa Manaqobah Syeikh Abdul Qodir Al-Jailani",
        subtitle = "Dibaca seusai pembacaan bab Kitab Manqobah 1 s/d 56",
        arabicText = """
بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ، حَمْدَ الشَّاكِرِينَ، حَمْدَ النَّاعِمِينَ، حَمْدًا يُوَافِي نِعَمَهُ وَيُكَافِئُ مَزِيدَهُ. يَا رَبَّنَا لَكَ الْحَمْدُ كَمَا يَنْبَغِي لِجَلَالِ وَجْهِكَ الْكَرِيمِ وَلِعَظِيمِ سُلْطَانِكَ.

اللَّهُمَّ صَلِّ وَسَلِّمْ وَبَارِكْ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِ سَيِّدِنَا مُحَمَّدٍ، صَلَاةً تُنَجِّينَا بِهَا مِنْ جَمِيعِ الْأَهْوَالِ وَالْآفَاتِ، وَتَقْضِي لَنَا بِهَا جَمِيعَ الْحَاجَاتِ، وَتُطَهِّرُنَا بِهَا مِنْ جَمِيعِ السَّيِّئَاتِ، وَتَرْفَعُنَا بِهَا عِنْدَكَ أَعْلَى الدَّرَجَاتِ، وَتُبَلِّغُنَا بِهَا أَقْصَى الْغَايَاتِ مِنْ جَمِيعِ الْخَيْرَاتِ فِي الْحَيَاةِ وَبَعْدَ الْمَمَاتِ.

اللَّهُمَّ بِحُرْمَةِ وَبَرَكَةِ سَيِّدِنَا وَمَوْلَانَا سُلْطَانِ الْأَوْلِيَاءِ وَقُدْوَةِ الْأَصْفِيَاءِ الشَّيْخِ عَبْدِ الْقَادِرِ الْجَيْلَانِيِّ قَدَّسَ اللَّهُ سِرَّهُ الْعَزِيزَ، وَبِكَرَامَةِ مَنَاقِبِهِ الشَّرِيفَةِ، أَنْ تَغْفِرَ لَنَا ذُنُوبَنَا وَلِوَالِدَيْنَا وَلِمَشَايِخِنَا وَلِإِخْوَانِنَا جَمِيعًا. اللَّهُمَّ افْتَحْ لَنَا فُتُوحَ الْعَارِفِينَ، وَوَفِّقْنَا لِمَا تُحِبُّ وَتَرْضَى، وَارْزُقْنَا حَلَاوَةَ ذِكْرِكَ وَمَحَبَّتِكَ، وَاحْفَظْنَا وَبِلَادَنَا مِنْ كُلِّ بَلَاءٍ وَفِتْنَةٍ يَا أَرْحَمَ الرَّاحِمِينَ.
        """.trimIndent(),
        latinText = "Bismillaahir-rohmaanir-rohiim. Alhamdulillaahi robbil 'aalamiin... Allahumma sholli 'alaa sayyidinaa Muhammadin wa 'alaa aali sayyidinaa Muhammad...",
        indonesianTranslation = "Dengan nama Allah Yang Maha Pengasih lagi Maha Penyayang. Segala puji bagi Allah Tuhan semesta alam. Ya Allah limpahkanlah shalawat dan salam kepada junjungan kami Nabi Muhammad ﷺ. Ya Allah dengan kemuliaan dan keberkahan Sulthanul Auliya Syekh Abdul Qodir Al-Jailani r.a., ampunilah dosa kami, kedua orang tua kami, guru-guru mursyid kami, dan segenap ikhwan. Bukakanlah bagi kami pintu ma'rifat kaum arifin dan selamatkanlah kami dari segala marabahaya.",
        sundaneseTranslation = "Kalayan asma Allah Nu Maha Welas tur Maha Asih. Sadaya puji kagungan Allah Nu Murbeng Alam. Ya Allah mugia maparin shalawat sareng kasalametan ka Panutan Alam Kangjeng Nabi Muhammad ﷺ. Ya Allah kalayan karomah sareng barokah Sulthonul Auliya Syekh Abdul Qodir Al-Jailani r.a., mugi ngahapunten samudaya dosa simkuring sadaya, sepuh simkuring, para guru mursyid, miwah sadaya ikhwan.",
        fadhilah = "Mendatangkan ketenangan batin, keberkahan rizki, dikabulkannya hajat kebaikan, dan perlindungan dari bala musibah."
    )

    val doaRijalulGhoib = DoaSpiritualItem(
        id = "doa_rijalul_ghoib",
        title = "Salam & Doa Rijalul Ghoib",
        subtitle = "Tawassul kepada para hamba Allah yang ghaib pengatur keselamatan alam",
        arabicText = """
السَّلَامُ عَلَيْكُمْ يَا رِجَالَ الْغَيْبِ، السَّلَامُ عَلَيْكُمْ يَا أَيُّهَا الْأَرْوَاحُ الْمُقَدَّسَةُ، السَّلَامُ عَلَيْكُمْ يَا نُقَبَاءُ، السَّلَامُ عَلَيْكُمْ يَا نُجَبَاءُ، السَّلَامُ عَلَيْكُمْ يَا رُقَبَاءُ، السَّلَامُ عَلَيْكُمْ يَا بُدَلَاءُ، السَّلَامُ عَلَيْكُمْ يَا أَوْتَادُ، السَّلَامُ عَلَيْكُمْ يَا أَخْيَارُ، السَّلَامُ عَلَيْكُمْ يَا عُمَدُ، السَّلَامُ عَلَيْكُمْ يَا غَوْثُ، أَغِيثُونَا بِغَوْثَةٍ وَانْظُرُونَا بِنَظْرَةٍ يَا رِجَالَ اللَّهِ، بِإِذْنِ اللَّهِ وَبِرَحْمَةِ اللَّهِ وَبِحُرْمَةِ رَسُولِ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ.
        """.trimIndent(),
        latinText = "Assalaamu 'alaikum yaa rijaalal ghoib, Assalaamu 'alaikum yaa ayyuhal arwaahul muqoddasah, Assalaamu 'alaikum yaa nuqobaa-u, yaa nujabaa-u, yaa ruqobaa-u, yaa budalaa-u, yaa awtaadu, yaa akhyaaru, yaa 'umadu, yaa ghoutsu. Aghiitsuunaa bighoutsatin wandhuruunaa binadhrotin yaa rijaalallaah...",
        indonesianTranslation = "Keselamatan semoga tercurah atasmu wahai para Rijalul Ghaib, wahai ruh-ruh yang disucikan, wahai Nuqaba, Nujaba, Ruqaba, Budala, Awtad, Akhyar, 'Umad, dan Ghauts. Tolonglah kami dengan pertolongan dan pandanglah kami dengan pandangan kasih sayang wahai hamba-hamba Allah, dengan izin Allah, rahmat Allah, dan kemuliaan Rasulullah ﷺ.",
        sundaneseTranslation = "Kasalametan mugia ngocor ka salira he para Rijalul Ghaib, he arwah anu disucikeun, he para wali Nuqaba, Nujaba, Ruqaba, Budala, Awtad, Akhyar, 'Umad, sinareng Ghauts. Mugia salira maparin panangtayungan sareng paneuteup welas asih kalayan widi Gusti Allah sareng kamulyaan Rasulullah ﷺ.",
        fadhilah = "Mendapat pertolongan spiritual saat menghadapi kesulitan berat dan menyingkirkan gangguan makhluk halus serta mara bahaya."
    )

    val doaAshabulKahfi = DoaSpiritualItem(
        id = "doa_ashabul_kahfi",
        title = "Doa & Isim Ashabul Kahfi",
        subtitle = "Tawassul karomah 7 Pemuda Ashabul Kahfi & Qithmir penjaga keteguhan iman",
        arabicText = """
بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
مَكْسَلْمِينَا، تَمْلِيخَا، مَرْتُوسْ، نَيْنُوسْ، سَارِبُولِسْ، ذُو نَوَانِسْ، فَلْيَسْتَطْيُوسْ، وَكَلْبُهُمْ قِطْمِيرْ.
رَبَّنَا آتِنَا مِن لَّدُنكَ رَحْمَةً وَهَيِّئْ لَنَا مِنْ أَمْرِنَا رَشَدًا. اللَّهُمَّ احْفَظْنَا مِنَ الْفِتَنِ مَا ظَهَرَ مِنْهَا وَمَا بَطَنَ، وَثَبِّتْ قُلُوبَنَا عَلَى دِينِكَ وَطَاعَتِكَ يَا مُقَلِّبَ الْقُلُوبِ.
        """.trimIndent(),
        latinText = "Bismillaahir-rohmaanir-rohiim. Maksalmiinaa, Tamliikhaa, Martuus, Naynuus, Saaribuulis, Dzuu Nawaanis, Falyastathyuus, wa kalbuhum Qithmiir. Robbanaa aatinaa mil ladunka rohmataw wa hayyi' lanaa min amrinaa rosyadaa...",
        indonesianTranslation = "Dengan nama Allah Yang Maha Pengasih lagi Maha Penyayang. (Dengan berkah) Maksalmina, Tamlikha, Martus, Nainus, Saribulis, Dzu Nawanis, Falyastatyus, dan anjing mereka Qithmir. Wahai Tuhan kami, berikanlah rahmat kepada kami dari sisi-Mu dan sempurnakanlah petunjuk yang lurus bagi kami dalam urusan kami.",
        sundaneseTranslation = "Kalayan asma Allah Nu Maha Welas tur Asih. Kalayan barokah para wali Ashabul Kahfi (Maksalmina, Tamlikha, Martus, Nainus, Saribulis, Dzu Nawanis, Falyastatyus, sareng anjingna Qithmir). Nun Gusti Pangeran simkuring sadaya, mugia maparin rohmat ti Gusti sarta ngalempengkeun sagala urusan kalayan pituduh anu sae.",
        fadhilah = "Perlindungan rumah dari pencurian, kebakaran, serta keteguhan hati dari fitnah akhir zaman."
    )

    val khotamanSteps = listOf(
        KhotamanStep(
            stepNumber = 1,
            title = "Hadhrah & Al-Fatihah Pembuka Khotaman",
            repeatCount = "7x Fatihah",
            arabicText = "إِلَى حَضْرَةِ النَّبِيِّ الْمُصْطَفَى مُحَمَّدٍ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ ... ثُمَّ إِلَى أَرْوَاحِ آبَائِهِ وَأُمَّهَاتِهِ ... ثُمَّ إِلَى أَهْلِ السِّلْسِلَةِ الْقَادِرِيَّةِ النَّقْشَبَنْدِيَّةِ ...",
            latinText = "Ilaa hadrotin nabiyyil musthofaa Muhammadin SAW ... Tsumma ilaa arwaahi aabaaihii ... Tsumma ilaa Ahlis Silsilatil Qoodiriyyah Naqsyabandiyyah ...",
            translation = "Menghadiahkan bacaan Surat Al-Fatihah kepada Baginda Nabi ﷺ, para Nabi, Malaikat, Sahabat Khulafaur Rasyidin, Imam Mujtahid, dan Silsilah Guru Mursyid TQN Suryalaya Sirnarasa.",
            instructions = "Duduk bertawajjuh menghadap kiblat, memejamkan mata dan mengarahkan segenap rasa ke lathifah qolbi."
        ),
        KhotamanStep(
            stepNumber = 2,
            title = "Membaca Shalawat Ummi",
            repeatCount = "100x",
            arabicText = "اَللّٰهُمَّ صَلِّ عَلٰى سَيِّدِنَا مُحـَمَّدِنِالنَّبِيِّ الْاُمِّيِّ وَعَلٰى اٰلِهٖ وَصَحْبِهٖ وَسَلِّمْ",
            latinText = "Alloohumma sholli ‘alaa Sayyidinaa Muhammadinin Nabiyyil Ummiyyi, wa ’alaa aalihii wa shohbihii wa sallim.",
            translation = "Yaa Allah, semoga Engkau melimpahkan rahmat kepada junjungan kami sekalian Nabi Muhammad yang ummi dan kepada keluarganya semoga melimpah keselamatan.",
            instructions = "Melantunkan shalawat dengan penuh mahabbah dan kerinduan kepada Rasulullah ﷺ."
        ),
        KhotamanStep(
            stepNumber = 3,
            title = "Membaca Surat Alam Nasyrah",
            repeatCount = "80x",
            arabicText = "أَلَمۡ نَشۡرَحۡ لَكَ صَدۡرَكَ • وَوَضَعۡنَا عَنكَ وِزۡرَكَ • ٱلَّذِيٓ أَنقَضَ ظَهۡرَكَ • وَرَفَعۡنَا لَكَ ذِكۡرَكَ • فَإِنَّ مَعَ ٱلۡعُسۡرِ يُسۡرًا • إِنَّ مَعَ ٱلۡعُسۡرِ يُسۡرٗا • فَإِذَا فَرَغۡتَ فَٱنصَبۡ • وَإِلَىٰ رَبِّكَ فَٱرۡغَب",
            latinText = "Alam nasyroh laka shodrok, wa wadho’naa ‘anka wizrok, alladzii anqodho zhohrok, wa rofa’naa laka dzikrok, fa inna ma’al ‘usri yusroo, inna ma’al ‘usri yusroo, fa idzaa faroghta fanshob, wa ilaa robbika farghob.",
            translation = "Bukankah Kami telah melapangkan dadamu? Dan Kami telah menghilangkan daripadamu bebanmu yang memberatkan punggungmu. Dan Kami tinggikan bagimu sebutan (nama)mu...",
            instructions = "Merenungkan kelapangan dada dan kemudahan setelah kesulitan."
        ),
        KhotamanStep(
            stepNumber = 4,
            title = "Membaca Surat Al-Ikhlas",
            repeatCount = "500x",
            arabicText = "قُلۡ هُوَ ٱللَّهُ أَحَدٌ • ٱللَّهُ ٱلصَّمَدُ • لَمۡ يَلِدۡ وَلَمۡ يُولَدۡ • وَلَمۡ يَكُن لَّهُۥ كُفُوًا أَحَدُ",
            latinText = "Qul huwalloohu ahad, Alloohush Shomad, Lam yalid wa lam yuulad, Wa lam yakul lahuu kufuwan ahad.",
            translation = "Katakanlah (Ya Muhammad) Allah itu Esa. Allah adalah Tuhan yang bergantung kepada-Nya segala sesuatu. Dia tiada beranak dan tiada pula diperanakkan. Dan tidak ada seorangpun yang setara dengan Dia.",
            instructions = "Menghadirkan tauhid murni dan kemurnian keesaan Allah SWT di dalam qolbu."
        ),
        KhotamanStep(
            stepNumber = 5,
            title = "Istighotsah 7 Asmaul Husna Khotaman",
            repeatCount = "7 x 100x",
            arabicText = "اَللّٰهُمَّ يَا قَاضِيَ الْحَاجَاتِ • اَللّٰهُمَّ يَا كَافِيَ الْمُهِمَّاتِ • اَللّٰهُمَّ يَا دَافِعَ الْبَلِيَّاتِ • اَللّٰهُمَّ يَا رَافِعَ الدَّرَجَاتِ • اَللّٰهُمَّ يَا شَافِيَ الْأَمْرَاضِ • اَللّٰهُمَّ يَا مُجِيْبَ الدَّعَوَاتِ • اَللّٰهُمَّ يَا أَرْحَمَ الرَّاحِمِيْنَ",
            latinText = "Alloohumma Yaa Qoodhiyal haajaat (100x), Yaa Kaafiyal muhimmaat (100x), Yaa Daafi’al baliyyaat (100x), Yaa Roofi’ad darojaat (100x), Yaa Syaafiyal amroodh (100x), Yaa Mujiibad da’awaat (100x), Yaa Arhamar roohimiin (100x).",
            translation = "Ya Allah Yang Maha Memenuhi segala kebutuhan, Mencukupi urusan penting, Menolak bala, Mengangkat derajat, Menyembuhkan penyakit, Mengabulkan doa, Maha Pengasih lagi Penyayang.",
            instructions = "Didahului Hadhrah Fatihah ke hadapan Syekh Ahmad Baqir r.a."
        ),
        KhotamanStep(
            stepNumber = 6,
            title = "Membaca Hauqolah",
            repeatCount = "100x",
            arabicText = "لَاحَوْلَ وَلَاقُوَّةَ إِلَّا بِاللّٰهِ الْعَلِيِّ الْعَظِيْمِ",
            latinText = "Laa haula wa laa quwwata illaa billaahil ‘Aliyyil ‘Azhiim.",
            translation = "Tiada daya dan kekuatan melainkan dengan pertolongan Allah Yang Maha Luhur dan Maha Agung.",
            instructions = "Didahului Hadhrah Fatihah ke hadapan Syaikh Khowajikan r.a. dan Shalawat Ummi 100x."
        ),
        KhotamanStep(
            stepNumber = 7,
            title = "Membaca Surat Al-Falaq",
            repeatCount = "1x",
            arabicText = "قُلۡ أَعُوذُ بِرَبِّ ٱلۡفَلَقِ • مِن شَرِّ مَا خَلَقَ • وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ • وَمِن شَرِّ ٱلنَّفَّٰثَٰتِ فِي ٱلۡعُقَدِ • وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            latinText = "Qul a’uudzu birobbil falaq, Min syarri maa kholaq, Wa min syarri ghoosiqin idzaa waqob, Wa min syarrin naffaatsaati fil ‘uqod, Wa min syarri haasidin idzaa hasad.",
            translation = "Katakanlah: Aku berlindung kepada Tuhan yang menguasai subuh, dari kejahatan makhluk-Nya...",
            instructions = "Didahului Hadhrah Fatihah ke hadapan Imam Robbani r.a."
        ),
        KhotamanStep(
            stepNumber = 8,
            title = "Membaca Istighfar Khotaman",
            repeatCount = "100x",
            arabicText = "اَسْتَغْفِرُ اللّٰهَ الْعَظِيْمَ الَّذِي لَآ إِلٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّوْمُ وَاَتُوْبُ إِلَيْهِ",
            latinText = "Astaghfirulloohal ‘Azhiim alladzii laa ilaaha illaa huwal Hayyul Qoyyuumu wa atuubu ilaih.",
            translation = "Aku memohon ampun kepada Allah Yang Maha Agung, Yang tiada Tuhan selain Ia Yang Maha Menghidupkan dan Maha Berdiri serta aku bertaubat kepada-Nya.",
            instructions = "Merenungi segala dosa dan kelalaian agar disucikan oleh ampunan Allah SWT."
        ),
        KhotamanStep(
            stepNumber = 9,
            title = "Membaca Surat An-Naas",
            repeatCount = "1x",
            arabicText = "قُلۡ أَعُوذُ بِرَبِّ ٱلنَّاسِ • مَلِكِ ٱلنَّاسِ • إِلَٰهِ ٱلنَّاسِ • مِن شَرِّ ٱلۡوَسۡوَاسِ ٱلۡخَنَّاسِ • ٱلَّذِي يُوَسۡوِسُ فِي صُدُورِ ٱلنَّاسِ • مِنَ ٱلۡجِنَّةِ وَٱلنَّاسِ",
            latinText = "Qul a’uudzu birobbin naas, Malikin naas, Ilaahin naas, Min syarril waswaasil khonnaas, Alladzii yuwaswisu fii shuduurin naas, Minal jinnati wan naas.",
            translation = "Katakanlah: Aku berlindung kepada Tuhan manusia, Raja manusia, Sembahan manusia, dari kejahatan bisikan syetan yang bersembunyi...",
            instructions = "Benteng perlindungan diri dari godaan was-was jin dan manusia."
        ),
        KhotamanStep(
            stepNumber = 10,
            title = "Membaca Hasbunallah wa Ni'mal Wakil",
            repeatCount = "500x",
            arabicText = "حَسْبُنَا اللّٰهُ وَنِعْمَ الْوَكِيْلُ",
            latinText = "Hasbunalloohu wa ni’mal wakiil.",
            translation = "Allah Zat yang memberi kecukupan kepada kami dan sebaik-baiknya wakil.",
            instructions = "Didahului Hadhrah Fatihah ke hadapan Sayyidina Muzhohhir r.a."
        ),
        KhotamanStep(
            stepNumber = 11,
            title = "Membaca Ni'mal Maula wa Ni'man Nashiir",
            repeatCount = "500x",
            arabicText = "نِعْمَ الْمَوْلٰى وَنِعْمَ النَّصِيْرُ",
            latinText = "Ni’mal maulaa wa ni’man Nashiir.",
            translation = "Dialah sebaik-baiknya pengurus dan sebaik-baiknya pemberi pertolongan.",
            instructions = "Didahului Hadhrah Fatihah ke hadapan Sulthonul Auliya Syekh Abdul Qodir Al-Jailani q.s."
        ),
        KhotamanStep(
            stepNumber = 12,
            title = "Membaca Yaa Khofiyyal Luthfi",
            repeatCount = "500x",
            arabicText = "يَاخَفِيَّ اللُّطْفِ أَدْرِكْنِي بِلُطْفِكَ الـْخَفِيِّ",
            latinText = "Yaa Khoofiyal luthfi, adriknii biluthfi-Kal khofiyy.",
            translation = "Wahai Zat Yang Maha Lembut rahasia kelembutan-Nya, tolonglah dan pertemukanlah daku dengan kelembutan-Mu yang samar.",
            instructions = "Didahului Hadhrah Fatihah ke hadapan Guru Agung Abah Sepuh, Abah Anom, dan Abah Aos q.s."
        ),
        KhotamanStep(
            stepNumber = 13,
            title = "Membaca Doa Nabi Yunus (Istighotsah Qolbu)",
            repeatCount = "500x",
            arabicText = "لَا إِلٰهَ إِلَّا أَنْتَ سُبْحَانَكَ اِنِّي كُنْتُ مِنَ الظَّالِمِيْنَ",
            latinText = "Laa ilaaha illa Anta Subhaanaka innii kuntu minazh zhoolimiin.",
            translation = "Tiada Tuhan yang berhak disembah selain Engkau Yang Maha Suci, sesungguhnya aku ini termasuk orang-orang yang berbuat zalim.",
            instructions = "Didahului Hadhrah Fatihah ke hadapan Imam Khawajah An-Naqsyabandi r.a."
        ),
        KhotamanStep(
            stepNumber = 14,
            title = "Tawajjuh & Dzikir Tahlil Maqshudi",
            repeatCount = "3x",
            arabicText = "اِلَهِي اَنْتَ مَقْصُودِيْ وَرِضاَكَ مَطْلُوبِي اَعْطِنِيْ مَحَبَتَكَ وَمَعْرِفَتَكَ وَاَعْطِنِيْ مَعَكَ اَبَدَا",
            latinText = "Ilaahii Anta maqshuudii wa Ridhoo-Ka mathluubii (3x) A’thinii mahabbata-Ka wa ma’rifata-Ka wa ‘athinii ma’aka abada.",
            translation = "Tuhanku Engkaulah yang menjadi maksudku dan keridhoan-Mu yang menjadi pintaku. Berikanlah kepadaku kecintaan dan ma’rifat kepada-Mu dan kekalkanlah aku bersama-Mu selamanya.",
            instructions = "Didahului Hadhrah Fatihah ke hadapan Sayyidina Ma'shum r.a. lalu memejamkan mata dalam suasana Tawajjuh ruhani."
        ),
        KhotamanStep(
            stepNumber = 15,
            title = "Dzikir Lathifah Yaa Lathiif",
            repeatCount = "16.641x",
            arabicText = "يَا لَطِيْفُ",
            latinText = "Yaa Lathiif",
            translation = "Wahai Zat Yang Maha Lembut.",
            instructions = "Dzikir sirr lathifah secara khusyuk dan hening menggetarkan lathifah-lathifah batin."
        ),
        KhotamanStep(
            stepNumber = 16,
            title = "Membaca Doa Khotaman TQN",
            repeatCount = "1x",
            arabicText = "ياَ مَنْ وَسِعَ لُطْفُهُ أَهْلَ السَّمٰوَاتِ وَاْلأَرْضِ نَسْأَلُكَ بِخَفِيِّ خَفِيِّ لُطْفِكَ الـْخَفِيِّ أَنْ تُخْفِيْنَا فِي خَفِيِّ خَفِيِّ لُطْفِكَ الـْخَفِيِّ ...",
            latinText = "Yaa lathiif (3x). Yaa Man wasi’a lathfuhuu ahlas samaawaati wal ardh, nas’aluka bi khofiyyi khofiyyi luthfi-Kal khofiyyi...",
            translation = "Wahai Zat yang luas kelembutan-Nya bagi penghuni langit dan bumi, kami memohon perlindungan di dalam rahasia kelembutan-Mu...",
            instructions = "Dibaca dengan penuh penghayatan dan ketundukan hati memohon barokah bagi seluruh umat."
        ),
        KhotamanStep(
            stepNumber = 17,
            title = "Doa Persatuan, Benteng & Tolak Bala",
            repeatCount = "3x per Doa",
            arabicText = "عَسَى اللّٰهُ أَن يَجْعَلَ بَيْنَكُمْ ... • اَللّٰهُمَّ صَحًّا صَحًّا ... • بِسْمِ اللّٰهِ الشَّافِي بِسْمِ اللّٰهِ الْكَافِي ...",
            latinText = "‘Asalloohu ayyaj’ala bainakum ... Alloohhumma shohhan-shohhan ... Bismillaahisy syaafii bismillaahhil kaafii ...",
            translation = "Rangkaian doa persatuan keluarga dan bangsa, benteng dari tipu daya musuh, serta penangkal bala lahir dan batin.",
            instructions = "Dibaca sesudah selesai doa khotaman maupun dzikir harian."
        ),
        KhotamanStep(
            stepNumber = 18,
            title = "Doa Keberkahan & Penyempurna Amal",
            repeatCount = "3x",
            arabicText = "رَبَّنَا أَنْزِلْنَا مُنْزَلًا مُّبَارَكًا ... • رَبِّ يَسِّرْ لَنَا وَلَا تُعَسِّرْ عَلَيْنَا رَبِّي تَمِّمْ لَنَا بِالـْخَيْرِ أَعْمَالَنَا • ۞۞۞",
            latinText = "Robbanaa angzilnaa mungzalam mubaarokaw wa angta khoirul mungziliina ... Robbii yassir lanaa, walaa tu’assir ‘alainaa, Robbii tammim lanaa bikhairi a’malana (3x).",
            translation = "Ya Tuhanku, tempatkanlah kami pada tempat yang penuh berkah. Mudahkanlah urusan kami dan sempurnakanlah segala amal kebajikan kami.",
            instructions = "Penutup majlis khotaman diakhiri dengan Al-Fatihah dan salam kemursyidan."
        )
    )

    fun getManqobahChapters(): List<ManqobahChapter> = (1..56).map { chapterNum ->
        generateChapter(chapterNum)
    }

    private fun generateChapter(number: Int): ManqobahChapter {
        val titleId = when (number) {
            1 -> "Manqobah ke-1: Nasab dan Asal-Usul Keturunan Syekh Abdul Qodir Al-Jailani r.a."
            2 -> "Manqobah ke-2: Kesucian dan Keajaiban Waktu Mengandung dan Melahirkan"
            3 -> "Manqobah ke-3: Keadaan Syekh pada Masa Kanak-Kanak dan Masa Kecil"
            4 -> "Manqobah ke-4: Semangat Menuntut Ilmu dan Adab Sopan Santun kepada Guru"
            5 -> "Manqobah ke-5: Riyadhah dan Mujahadah di Padang Pasir Iraq"
            6 -> "Manqobah ke-6: Ketundukan dan Ta'dhim kepada Syekh Hammad Ad-Dabbas"
            7 -> "Manqobah ke-7: Ditulisnya Nama Syekh di Lauhil Mahfudz sebagai Sayyidul Auliya"
            8 -> "Manqobah ke-8: Kesabaran Menghadapi Cobaan Kelaparan dan Kepayahan"
            9 -> "Manqobah ke-9: Perintah Rasulullah ﷺ untuk Mengajar dan Berkhutbah di Majlis"
            10 -> "Manqobah ke-10: Keagungan Khutbah dan Pengaruh Nasihat Ruhani di Baghdad"
            11 -> "Manqobah ke-11: Karomah Menghentikan Turunnya Hujan dan Salju yang Mengganggu Jamaah"
            12 -> "Manqobah ke-12: Menolong Pedagang yang Kehilangan Seluruh Hartanya"
            13 -> "Manqobah ke-13: Mengobati Wabah Penyakit Tha'un di Kota Baghdad"
            14 -> "Manqobah ke-14: Pohon Kurma Kering yang Berbuah Kembali atas Izin Allah"
            15 -> "Manqobah ke-15: Menyelamatkan Murid yang Mengalami Kesulitan di Alam Kubur"
            16 -> "Manqobah ke-16: Menghidupkan Burung Pipit yang Telah Mati"
            17 -> "Manqobah ke-17: Karomah Berjalan di Atas Air Sungai Tigris (Dajlah)"
            18 -> "Manqobah ke-18: Menghancurkan Tipu Daya Iblis yang Mengaku sebagai Tuhan"
            19 -> "Manqobah ke-19: Merubah Perampok dan Pencuri Menjadi Wali Abdal"
            20 -> "Manqobah ke-20: Memberi Makanan kepada Musafir yang Kelaparan di Tengah Hutan"
            21 -> "Manqobah ke-21: Menundukkan Raja Jin yang Hendak Mengganggu Penduduk"
            22 -> "Manqobah ke-22: Menyadarkan Orang yang Meragukan Kewalian Beliau"
            23 -> "Manqobah ke-23: Menahan Kapal yang Nyaris Tenggelam di Lautan Luas"
            24 -> "Manqobah ke-24: Karomah Sapi Qurban yang Bersaksi di Hari Kiamat"
            25 -> "Manqobah ke-25: Menyingkap Isi Hati para Ulama dan Fuqaha Baghdad"
            26 -> "Manqobah ke-26: Mendengar Dzikir Seluruh Bebatuan dan Tumbuhan"
            27 -> "Manqobah ke-27: Mengobati Orang yang Terkena Gangguan Jin dan Sihir"
            28 -> "Manqobah ke-28: Kemuliaan Khirqoh Sufi yang Dipakaikan oleh Para Mursyid"
            29 -> "Manqobah ke-29: Kedermawanan dan Belas Kasih kepada Fakir Miskin"
            30 -> "Manqobah ke-30: Mengusir Kawanan Begal dan Perompak dengan Doa"
            31 -> "Manqobah ke-31: Sikap Tawadhu dan Kerendahan Hati di Hadapan Allah"
            32 -> "Manqobah ke-32: Keutamaan Menyebut Nama Syekh Saat Menghadapi Kesusahan"
            33 -> "Manqobah ke-33: Kesaksian Para Wali pada Zamannya atas Kedudukan Sulthonul Auliya"
            34 -> "Manqobah ke-34: Ucapan Beliau 'Kedua Telapak Kakiku Berada di Atas Pundak Setiap Wali'"
            35 -> "Manqobah ke-35: Ketaatan Pasukan Malaikat dan Ruh Suci di Majlis Pengajian"
            36 -> "Manqobah ke-36: Melemparkan Terompah untuk Menyelamatkan Murid dari Serangan Penjahat"
            37 -> "Manqobah ke-37: Pohon Kayu yang Tunduk dan Memberi Salam kepada Syekh"
            38 -> "Manqobah ke-38: Menghidupkan Tulang-Belulang Ayam Menjadi Ayam Hidup"
            39 -> "Manqobah ke-39: Membebaskan Orang Mukmin dari Belenggu Penjara dan Kesempitan"
            40 -> "Manqobah ke-40: Menghibur Hati Murid yang Sedang Dilanda Kesedihan Mendalam"
            41 -> "Manqobah ke-41: Mengubah Air Sumur yang Pahit Menjadi Tawar dan Manis"
            42 -> "Manqobah ke-42: Perlindungan Beliau kepada Para Murid dari Siksa Neraka"
            43 -> "Manqobah ke-43: Memperlihatkan Panorama Syurga kepada Murid yang Ikhlas"
            44 -> "Manqobah ke-44: Keutamaan Wirid Dzikir Tarekat Qodiriyyah"
            45 -> "Manqobah ke-45: Bimbingan Karomah kepada Para Santri Pembelajar Hadits dan Fiqih"
            46 -> "Manqobah ke-46: Doa Beliau untuk Keamanan dan Ketentraman Negeri"
            47 -> "Manqobah ke-47: Membongkar Niat Jahat Orang Munafik yang Hendak Menguji"
            48 -> "Manqobah ke-48: Menerima Anugerah Cahaya dan Karunia Ruhani dari Langit"
            49 -> "Manqobah ke-49: Kasih Sayang Beliau kepada Seluruh Murid Hingga Hari Kiamat"
            50 -> "Manqobah ke-50: Pertemuan dan Kehadiran Nabi Khidhir a.s. di Majlis Beliau"
            51 -> "Manqobah ke-51: Menolak Bala dan Marabahaya yang Mengancam Kota Baghdad"
            52 -> "Manqobah ke-52: Karomah Ketika Menunaikan Ibadah Haji ke Baitullah Makkah"
            53 -> "Manqobah ke-53: Pengakuan Sayyid Ahmad Ar-Rifa'i r.a. atas Kewalian Syekh"
            54 -> "Manqobah ke-54: Wasiat Terakhir Beliau kepada Putra-Putranya dan Seluruh Murid"
            55 -> "Manqobah ke-55: Wafatnya Sulthonul Auliya Syekh Abdul Qodir Al-Jailani r.a."
            56 -> "Manqobah ke-56: Fadhilah dan Keberkahan Membaca Kitab Manaqib Syekh Abdul Qodir"
            else -> "Manqobah ke-$number: Karomah Sulthonul Auliya Syekh Abdul Qodir Al-Jailani r.a."
        }

        val titleSu = when (number) {
            1 -> "Manqobah ka-1: Nasab Turunan Kangjeng Syekh Abdul Qodir Al-Jailani r.a."
            2 -> "Manqobah ka-2: Kasucian jeung Kaajaiban dina Wanci Ngandung jeung Babar"
            3 -> "Manqobah ka-3: Kaayaan Kangjeng Syekh dina Wanci Murangkalih"
            4 -> "Manqobah ka-4: Kautamaan Nyiar Elmu sareng Tatakrama ka Guru"
            5 -> "Manqobah ka-5: Riyadhah sareng Mujahadah di Gurun Sahara Iraq"
            6 -> "Manqobah ka-6: Kasatiaan sareng Ta'dhim ka Syekh Hammad Ad-Dabbas"
            7 -> "Manqobah ka-7: Diukir Jenengan Kangjeng Syekh dina Lauhil Mahfudz"
            8 -> "Manqobah ka-8: Kasobaran Nyanghareupan Rupa-Rupa Cocoba sareng Kalaparan"
            9 -> "Manqobah ka-9: Pituduh ti Rasulullah ﷺ kanggo Wawarah sareng Khutbah di Majlis"
            10 -> "Manqobah ka-10: Kaagungan Pangaruh Khutbah sareng Nasehatna di Baghdad"
            11 -> "Manqobah ka-11: Nyegah lungsurna Hujan sareng Salju nu Ganggu Jamaah"
            12 -> "Manqobah ka-12: Maparin Pitulung ka Padagang nu Kaleungitan Harta"
            13 -> "Manqobah ka-13: Nyageurkeun Panyakit Tha'un di Kota Baghdad"
            14 -> "Manqobah ka-14: Tangkal Kurma Garing anu Buahan Deui ku Widi Allah"
            15 -> "Manqobah ka-15: Nyalametkeun Murid tina Kasulitan di Jero Kubur"
            16 -> "Manqobah ka-16: Ngahirupkeun Manuk Piit anu Parantos Paeh"
            17 -> "Manqobah ka-17: Karomah Mapah di Luhureun Cai Walungan Dajlah (Tigris)"
            18 -> "Manqobah ka-18: Ngagagalkeun Tipu Daya Iblis anu Ngaku-Ngaku Pangeran"
            19 -> "Manqobah ka-19: Ngarobah Begal sareng Bangsat Janten Wali Abdal"
            20 -> "Manqobah ka-20: Ngintunkeun Dahareun ka nu Kalaparan di Tengah Leuweung"
            21 -> "Manqobah ka-21: Nundukkeun Raja Jin anu Bade Ngaganggu Masarakat"
            22 -> "Manqobah ka-22: Nyadarkeun Jalmi anu Cangcaya kana Kawalian Anjeunna"
            23 -> "Manqobah ka-23: Nahan Parahu anu Meh Tilelep di Lautan ku Dampal Panangan"
            24 -> "Manqobah ka-24: Karomah Sapi Qurban anu Janten Saksi dina Poe Kiamat"
            25 -> "Manqobah ka-25: Ngabongkar Eusi Manah para Ulama sareng Ahli Fiqih di Baghdad"
            26 -> "Manqobah ka-26: Ngadangu Dzikir Sakumna Batu-Batu sareng Tatangkalan"
            27 -> "Manqobah ka-27: Ngubaran Jalma nu Katerap Gangguan Jin sareng Sihir"
            28 -> "Manqobah ka-28: Kamulyaan Khirqoh Tasawuf ti para Guru Mursyid"
            29 -> "Manqobah ka-29: Kedermawanan sareng Welas Asih ka Kaom Fakir Miskin"
            30 -> "Manqobah ka-30: Nyingkirkeun Gorong-Gorong sareng Begal ku Sabab Doa"
            31 -> "Manqobah ka-31: Sikep Tawadhu sareng Handap Asor ka Gusti Allah"
            32 -> "Manqobah ka-32: Kautamaan Nyebat Jenengan Kangjeng Syekh dina Mangsa Ripuh"
            33 -> "Manqobah ka-33: Kasaksian Para Wali dina Zamanna kana Kalungguhan Sulthonul Auliya"
            34 -> "Manqobah ka-34: Pangandika 'Dampal Sukuku Aya dina Punduk Sakabeh Wali'"
            35 -> "Manqobah ka-35: Kasatiaan Pasukan Malaikat sareng Ruh Suci dina Majlis Pengajian"
            36 -> "Manqobah ka-36: Ngalungkeun Gamparan kanggo Nyalametkeun Murid ti Begal"
            37 -> "Manqobah ka-37: Tangkal Kai anu Nyondong sarta Uluk Salam ka Kangjeng Syekh"
            38 -> "Manqobah ka-38: Ngahirupkeun Tulang Hayam anu Parantos Dituang Jadi Hayam Hirup"
            39 -> "Manqobah ka-39: Ngabebaskeun Jalma Mukmin tina Belenggu Panjara Dunya & Akhirat"
            40 -> "Manqobah ka-40: Ngupahan Manah Murid anu Keur Katerap Kasusahan Rohani"
            41 -> "Manqobah ka-41: Ngarobah Cai Sumur anu Pait Janten Cai Herang tur Amis"
            42 -> "Manqobah ka-42: Panyalindungan Kangjeng Syekh ka Murid-Muridna ti Siksa Naraka"
            43 -> "Manqobah ka-43: Nembongkeun Endahna Sawarga ka Murid anu Ikhlas"
            44 -> "Manqobah ka-44: Kautamaan Wirid Dzikir Tarekat Qodiriyyah"
            45 -> "Manqobah ka-45: Pituduh Karomah ka Para Santri nu Diajar Hadits sareng Fiqih"
            46 -> "Manqobah ka-46: Doa Kangjeng Syekh kanggo Katentreman sareng Karaharjaan Nagara"
            47 -> "Manqobah ka-47: Ngabongkar Niat Buruk Jalmi Munafik nu Bade Nguji"
            48 -> "Manqobah ka-48: Nampi Cahaya Karunia Rohani ti Langit"
            49 -> "Manqobah ka-49: Kadeudeuh Kangjeng Syekh ka Sadaya Murid dugika Poe Kiamat"
            50 -> "Manqobah ka-50: Pasamoan sareng Kasumpingan Nabi Khidhir a.s. di Majlis"
            51 -> "Manqobah ka-51: Nulak Bala sareng Bahaya anu Bade Nimpa Kota Baghdad"
            52 -> "Manqobah ka-52: Karomah dina Mangsa Munggah Haji ka Baitullah Makkah"
            53 -> "Manqobah ka-53: Pangangken Sayyid Ahmad Ar-Rifa'i r.a. kana Kalungguhan Kangjeng Syekh"
            54 -> "Manqobah ka-54: Wasiat Pamungkas Kangjeng Syekh ka Putra-Putrana sareng Murid-Muridna"
            55 -> "Manqobah ka-55: Pupusna Sulthonul Auliya Syekh Abdul Qodir Al-Jailani r.a."
            56 -> "Manqobah ka-56: Fadhilah sareng Barokah Maos Kitab Manaqib Kangjeng Syekh"
            else -> "Manqobah ka-$number: Karomah Sulthonul Auliya Syekh Abdul Qodir Al-Jailani r.a."
        }

        val titleAr = "الْمَنْقَبَةُ السَّادِسَةُ وَالْخَمْسُونَ ($number): مَنَاقِبُ سُلْطَانِ الْأَوْلِيَاءِ سَيِّدِي الشَّيْخِ عَبْدِ الْقَادِرِ الْجَيْلَانِيِّ قَدَّسَ اللَّهُ سِرَّهُ"

        val contentAr = """
بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
اللَّهُمَّ انْشُرْ نَفَحَاتِ الرِّضْوَانِ عَلَيْهِ، وَأَمِدَّنَا بِالْأَسْرَارِ الَّتِي أَوْدَعْتَهَا لَدَيْهِ، وَأَعِدْ عَلَيْنَا مِنْ بَرَكَاتِهِ وَأَنْوَارِهِ وَفُيُوضَاتِهِ فِي الدِّينِ وَالدُّنْيَا وَالْآخِرَةِ.

كَانَ رَضِيَ اللَّهُ عَنْهُ وَأَرْضَاهُ قُطْبَ دَائِرَةِ الْوُجُودِ، وَغَوْثَ الْبَرَايَا عَلَى الْإِطْلَاقِ، سَيِّدَنَا الشَّيْخَ عَبْدَ الْقَادِرِ الْجَيْلَانِيَّ قَدَّسَ اللَّهُ سِرَّهُ الْعَزِيزَ، إِمَامَ الْعَارِفِينَ وَشَمْسَ السَّالِكِينَ، كَثِيرَ التَّضَرُّعِ وَالِابْتِهَالِ إِلَى اللَّهِ تَعَالَى، دَائِمَ الذِّكْرِ لَا يَفْتُرُ لِسَانُهُ عَنْ لَا إِلَهَ إِلَّا اللَّهُ، نَاصِرًا لِلسُّنَّةِ الْمُطَهَّرَةِ، قَامِعًا لِلْبِدْعَةِ وَالضَّلَالَةِ، مُحِبًّا لِلْفُقَرَاءِ وَالْمَسَاكِينِ، رَحِيمًا بِالْمُؤْمِنِينَ.

فَمَنْ تَمَسَّكَ بِحَبْلِهِ وَدَاوَمَ عَلَى قِرَاءَةِ مَنَاقِبِهِ الشَّرِيفَةِ نَالَ الْفَوْزَ وَالْفَلَاحَ، وَأُفِيضَتْ عَلَيْهِ أَنْوَارُ الْمَعْرِفَةِ وَالْيَقِينِ، وَحَفِظَهُ اللَّهُ مِنْ كُلِّ مَكْرُوهٍ وَبَلَاءٍ بِفَضْلِ اللَّهِ وَبَرَكَةِ أَوْلِيَائِهِ الصَّالِحِينَ.
        """.trimIndent()

        val contentId = """
BISMILLAAHIR-ROHMAANIR-ROHIIM
Allahumma shalli wa sallim wa baarik 'ala sayyidina Muhammadin wa 'ala aalihi wa shahbihi ajma'iin.

Diceritakan dalam kitab Manaqib Uqudul Jumaan: Bahwasanya Sulthanul Auliya Syekh Abdul Qodir Al-Jailani Qaddasallahu Sirrahul 'Aziz adalah seorang wali quthub yang dianugerahi Allah SWT kemuliaan derajat ruhani, keluasan ilmu lahir dan batin, serta ribuan karomah nyata yang menuntun umat kepada jalan tauhid dan mahabbah kepada Allah dan Rasul-Nya.

Dalam Manqobah ke-$number ini, dinyatakan bagaimana keluhuran budi pekerti, kesungguhan ibadah, keteguhan dzikir, dan pertolongan beliau kepada para penuntut ilmu dan murid-muridnya senantiasa mengalir sebagai bukti nyata kebenaran risalah Islam dan karunia kewalian.

Barangsiapa yang membaca atau mendengarkan manaqib ini dengan hati yang ikhlas, khusyuk, serta bertawassul kepada Allah SWT melalui kemuliaan beliau, niscaya Allah akan melapangkan kesempitan hidupnya, mengampuni dosanya, menerangi hatinya dengan nur dzikirullah, dan memberkahi keluarganya lahir maupun batin.
        """.trimIndent()

        val contentSu = """
BISMILLAAHIR-ROHMAANIR-ROHIIM
Allahumma sholli wa sallim wa barik 'ala sayyidina Muhammadin wa 'ala aalihi wa shohbihi ajma'in.

Kacarios dina Kitab Manaqib Uqudul Jumaan: Wirehna Kangjeng Sulthonul Auliya Syekh Abdul Qodir Al-Jailani Qaddasallahu Sirrahul 'Aziz nyaeta wali quthub agung anu dipaparin darajat luhur ku Gusti Allah SWT, jembar dina elmu lahir miwah batin, sarta kasinugrahan ku rebu-rebu karomah anu janten pituduh pikeun sakumna umat nuju kana jalan tauhid sareng mahabbah ka Allah sareng Rasul-Na.

Dina Manqobah ka-$number ieu, ditetelakeun kumaha luhurna budi pekerti, kateguhan ibadah, mayengna dzikirullah, sareng kadeudeuh Kangjeng Syekh ka para santri sareng murid-muridna anu teu kendat ngocor janten bukti nyata kamulyaan para kekasih Allah.

Sing saha jalma anu maos atanapi ngadangukeun ieu manaqib kalayan manah anu beresih, ikhlas, sarta tawassul ka Gusti Allah ku karomah Kangjeng Syekh, tangtos Allah SWT baris ngalungsurkeun katentreman, ngahapunten samudaya kalepatan, nyaangan manah ku cahaya dzikir, sarta maparin kaberkahan dunya rawuh akhirat.
        """.trimIndent()

        return ManqobahChapter(
            id = number.toLong(),
            chapterNumber = number,
            titleArabic = titleAr,
            titleIndonesian = titleId,
            titleSundanese = titleSu,
            contentArabic = contentAr,
            contentIndonesian = contentId,
            contentSundanese = contentSu,
            audioPath = "doamanqobah.png"
        )
    }
}
