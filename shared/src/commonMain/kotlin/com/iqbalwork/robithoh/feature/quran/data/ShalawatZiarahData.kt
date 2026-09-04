package com.iqbalwork.robithoh.feature.quran.data

import com.iqbalwork.robithoh.feature.quran.model.ShalawatModel
import com.iqbalwork.robithoh.feature.quran.model.ZiarahSection

object ShalawatZiarahData {

    val shalawatList: List<ShalawatModel> = listOf(
        ShalawatModel(
            id = "bani_hasyim",
            title = "Shalawat Bani Hasyim",
            subtitle = "Shalawat Agung Pengikat Ruhani (Robithoh) Mursyid TQN",
            arabicText = """
اللَّهُمَّ صَلِّ عَلَى النَّبِيِّ الْهَاشِمِيِّ مُحَمَّدٍ وَعَلَى آلِهِ وَسَلِّمْ تَسْلِيمًا
            """.trimIndent(),
            latinText = "Allahumma sholli 'alan-nabiyyil haasyimiyyi Muhammadin wa 'alaa aalihii wa sallim tasliimaa.",
            indonesianTranslation = "Ya Allah, limpahkanlah shalawat dan salam yang sempurna kepada Nabi keturunan Bani Hasyim, yakni Baginda Nabi Muhammad ﷺ, beserta keluarga beliau.",
            sundaneseTranslation = "Nun Gusti Allah, mugia maparin shalawat sinareng kasalametan anu sampurna ka Kangjeng Nabi teureuh Bani Hasyim, nyaeta Gusti Kanjeng Nabi Muhammad ﷺ miwah kulawargina.",
            virtue = "Shalawat resmi amalan khas ikhwan MTQN Suryalaya Sirnarasa PPKN yang senantiasa dilantunkan seusai sholat fardhu, manaqib, dan khotaman untuk menyambungkan frekuensi nurani kepada Rasulullah ﷺ dan Guru Mursyid.",
            audioPath = "bani_hasyim.mp3"
        ),
        ShalawatModel(
            id = "badriyah",
            title = "Shalawat Badriyah",
            subtitle = "Tawassul Karomah Para Pejuang Perang Badar",
            arabicText = """
صَلَاةُ اللَّهِ سَلَامُ اللَّهِ • عَلَى طٰهَ رَسُولِ اللَّهِ
صَلَاةُ اللَّهِ سَلَامُ اللَّهِ • عَلَى يٰسٓ حَبِيبِ اللَّهِ
تَوَسَّلْنَا بِبِسْمِ اللَّهِ • وَبِالْهَادِي رَسُولِ اللَّهِ
وَكُلِّ مُجَاهِدٍ لِلَّهِ • بِأَهْلِ الْبَدْرِ يَا أَللَّهُ
            """.trimIndent(),
            latinText = "Sholaatullaah salaamullaah, 'alaa Thoohaa Rosuulillaah. Sholaatullaah salaamullaah, 'alaa Yaasiin Habiibillaah. Tawassalnaa bibismillaah, wa bil-Haadii Rosuulillaah, wa kulli mujaahidin lillaah, bi-ahlil badri yaa Allaah.",
            indonesianTranslation = "Rahmat dan keselamatan Allah semoga tercurah kepada Thaha (Nabi Muhammad ﷺ) utusan Allah. Rahmat dan keselamatan Allah semoga tercurah kepada Yasin kekasih Allah. Kami bertawassul dengan Bismillah, dengan petunjuk Rasulullah, dan dengan setiap pejuang di jalan Allah, berkat para pejuang Perang Badar ya Allah.",
            sundaneseTranslation = "Rahmat sareng kasalametan Gusti mugia ngocor ka Kangjeng Nabi panutan alam, kakasih Gusti. Simkuring tawassul kalayan asma Allah sareng Rasulullah sarta para pajuang Perang Badar.",
            virtue = "Menolak segala bala bencana, menghalau marabahaya, mendatangkan keberkahan rezeki, dan memberikan ketenteraman dalam keluarga dan masyarakat.",
            audioPath = "badriyah.png"
        ),
        ShalawatModel(
            id = "ziarah_rasul",
            title = "Shalawat & Salam Ziarah Rasulullah ﷺ",
            subtitle = "Adab Menghaturkan Salam di Hadapan Makam Mulia Raudhah Madinah",
            arabicText = """
السَّلَامُ عَلَيْكَ يَا رَسُولَ اللَّهِ، السَّلَامُ عَلَيْكَ يَا نَبِيَّ اللَّهِ، السَّلَامُ عَلَيْكَ يَا خِيَرَةَ اللَّهِ مِنْ خَلْقِهِ، السَّلَامُ عَلَيْكَ يَا حَبِيبَ اللَّهِ، السَّلَامُ عَلَيْكَ يَا سَيِّدَ الْمُرْسَلِينَ وَخَاتَمَ النَّبِيِّينَ، جَزَاكَ اللَّهُ عَنَّا أَفْضَلَ مَا جَزَى نَبِيًّا عَنْ أُمَّتِهِ.
            """.trimIndent(),
            latinText = "Assalaamu 'alaika yaa Rosuulallaah, Assalaamu 'alaika yaa Nabiyyallaah, Assalaamu 'alaika yaa khiyaratallaahi min kholqih, Assalaamu 'alaika yaa Habiiballaah, Assalaamu 'alaika yaa Sayyidal Mursaliin wa Khootaman Nabiyyiin...",
            indonesianTranslation = "Salam sejahtera bagimu wahai Rasulullah, salam sejahtera bagimu wahai Nabi Allah, salam sejahtera bagimu wahai sebaik-baik ciptaan Allah, salam bagimu wahai kekasih Allah, salam bagimu wahai pemimpin para Rasul dan penutup para Nabi. Semoga Allah membalas kebaikanmu untuk kami dengan balasan termulia yang pernah diberikan kepada seorang Nabi bagi umatnya.",
            sundaneseTranslation = "Kasalametan mugia ngocor ka salira he Rasulullah, he Nabi Allah, he kakasih Gusti, he pamingpin para Rasul. Mugia Allah maparin ganjaran anu pangutamana ka salira.",
            virtue = "Menghadirkan rasa kedekatan rohani dengan Baginda Nabi Muhammad ﷺ serta mendapatkan syafa'at beliau di yaumil qiyamah.",
            audioPath = null
        )
    )

    val ziarahSections: List<ZiarahSection> = listOf(
        ZiarahSection(
            id = "ziarah_umum",
            title = "Panduan Ziarah Kubur Umum",
            subtitle = "Adab & Tata Cara Berziarah ke Makam Kaum Muslimin",
            adabSteps = listOf(
                "Mengikhlaskan niat semata-mata untuk mengingat kematian (dzikrul maut) dan mendoakan ahli kubur.",
                "Berwudhu dan menjaga kesucian lahir batin sebelum memasuki area pemakaman.",
                "Mengucapkan salam kepada ahli kubur saat memasuki pintu gerbang makam.",
                "Menghadap ke arah kiblat atau wajah ahli kubur tanpa menginjak atau menduduki gundukan makam.",
                "Membaca Surat Al-Fatihah, Ayat Kursi, Surat Yasin/Al-Ikhlas/Al-Falaq/An-Nas, dan menghadiahkan pahalanya bagi ahli kubur."
            ),
            arabicPrayer = """
السَّلَامُ عَلَيْكُمْ دَارَ قَوْمٍ مُؤْمِنِينَ، وَإِنَّا إِنْ شَاءَ اللَّهُ بِكُمْ لَاحِقُونَ، أَنْتُمْ لَنَا فَرَطٌ وَنَحْنُ لَكُمْ تَبَعٌ، نَسْأَلُ اللَّهَ لَنَا وَلَكُمُ الْعَافِيَةَ. اللَّهُمَّ اغْفِرْ لَهُمْ وَارْحَمْهُمْ وَعَافِهِمْ وَاعْفُ عَنْهُمْ.
            """.trimIndent(),
            latinPrayer = "Assalaamu 'alaikum daaro qowmim mu'miniin, wa innaa in syaa-allaahu bikum laahiquun. Antum lanaa farathun wa nahnu lakum taba'un. Nas-alullaaha lanaa wa lakumul 'aafiyah. Allahummaghfir lahum warhamhum wa 'aafihim wa'fu 'anhum.",
            indonesianTranslation = "Semoga keselamatan tercurah kepadamu wahai penghuni tempat kaum mukminin. Dan sesungguhnya kami, insya Allah, akan menyusul kalian. Kalian telah mendahului kami dan kami akan mengikuti kalian. Kami memohon keselamatan kepada Allah untuk kami dan untuk kalian. Ya Allah ampunilah mereka, rahmatilah mereka, dan maafkanlah kesalahan mereka.",
            fadhilah = "Mengingatkan manusia akan hakikat kampung akhirat serta mengalirkan pahala doa kepada arwah orang beriman."
        ),
        ZiarahSection(
            id = "ziarah_waliyullah",
            title = "Panduan Ziarah Makam Waliyullah",
            subtitle = "Tata Krama & Doa Tawassul saat Berziarah ke Makam Para Kekasih Allah",
            adabSteps = listOf(
                "Menjaga rasa khusyuk, tawadhu, dan ta'dhim (hormat) kepada kesucian wali yang dikunjungi.",
                "Meluruskan aqidah bahwa yang memberi manfaat dan madharat hanyalah Allah SWT semata, dan wali menjadi wasilah/tawassul barokah.",
                "Membaca Tawassul TQN, Surat Al-Fatihah, Dzikir Tahlil 165x, dan Shalawat Bani Hasyim.",
                "Menghindari perbuatan syirik seperti meminta langsung kepada kuburan, melainkan memohon kepada Allah berkat karomah sang wali.",
                "Menjaga ketertiban, kebersihan, dan kesopanan di lingkungan maqam."
            ),
            arabicPrayer = """
بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
السَّلَامُ عَلَيْكُمْ يَا وَلِيَّ اللَّهِ، السَّلَامُ عَلَيْكُمْ يَا صَفِيَّ اللَّهِ، السَّلَامُ عَلَيْكُمْ يَا حَبِيبَ اللَّهِ. جِئْنَاكَ زَائِرِينَ، وَبِفَضْلِ اللَّهِ مُتَوَسِّلِينَ، وَلِحُرْمَتِكَ عَارِفِينَ، فَاشْفَعْ لَنَا عِنْدَ رَبِّكَ فِي قَضَاءِ حَوَائِجِنَا وَمَغْفِرَةِ ذُنُوبِنَا وَصَلَاحِ قُلُوبِنَا بِإِذْنِ اللَّهِ تَعَالَى.
            """.trimIndent(),
            latinPrayer = "Bismillaahir-rohmaanir-rohiim. Assalaamu 'alaika yaa waliyyallaah, Assalaamu 'alaika yaa shofiyyallaah, Assalaamu 'alaika yaa habiiballaah. Ji'naaka zaa-iriina, wa bifadhlillaahi mutawassiliina, wa lihurmatika 'aarifiina, fasyfa' lanaa 'inda robbika fii qodhoo-i hawaa-ijinaa wa maghfiroti dzunuubinaa wa sholaahi quluubinaa bi-idznillaahi Ta'aalaa.",
            indonesianTranslation = "Dengan nama Allah Yang Maha Pengasih lagi Maha Penyayang. Salam sejahtera atasmu wahai kekasih Allah, wahai hamba pilihan Allah. Kami datang kepadamu berziarah, bertawassul dengan karunia Allah, dan memuliakan derajatmu. Maka mohonkanlah syafa'at/pertolongan doa bagi kami di sisi Tuhanmu agar hajat kami dikabulkan, dosa kami diampuni, dan hati kami disucikan dengan izin Allah Ta'ala.",
            fadhilah = "Mendapatkan limpahan nur berkah karomah para waliyullah, kemudahan dalam mengamalkan ketaatan, dan keteguhan rohani dalam menempuh thoriqoh."
        )
    )
}
