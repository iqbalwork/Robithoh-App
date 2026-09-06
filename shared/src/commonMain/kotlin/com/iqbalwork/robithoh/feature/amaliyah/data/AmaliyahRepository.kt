package com.iqbalwork.robithoh.feature.amaliyah.data

import com.iqbalwork.robithoh.feature.amaliyah.model.DzikirItem
import com.iqbalwork.robithoh.feature.amaliyah.model.HijriyahAmaliyah
import com.iqbalwork.robithoh.feature.amaliyah.model.SpecialPrayer

/**
 * Authentic Data Repository for MTQN Suryalaya Sirnarasa PPKN III 38 Amaliyah,
 * Dzikir Harian Ba'da Sholat (Jahr & Khofi), Daily Special Prayers,
 * 12 Bulan Hijriyah, and Sholat Sunnah with 3-language liturgical text.
 */
class AmaliyahRepository {

    fun getDzikirJahrList(): List<DzikirItem> = listOf(
        DzikirItem(
            id = "dzikir_harian_1",
            number = 1,
            title = "Hadhrot / Tawassul Pembuka",
            arabicText = "اِلٰى حَضْرَةِ النَّبِيِّ الْمُصْطَفٰى مُحَمَّدٍ صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمَ وَعَلٰى اٰلِهٖ وَاَصْحَابِهٖ وَاَزْوَاجِهٖ وَذُرِّيّٰتِهٖ وَاَهْلِ بَيْتِهٖ وَلِمَنْ دَخَلَ فِي بَيْتِهٖ اَجْمَعِيْنَ كُلُّ شَيْئ ٍ لِلّٰهِ لَهُمُ الْفَاتِحَةُ",
            latinText = "Ilaa hadrotin nabiyyil musthofaa Muhammadin Shollalloohu ‘Alaihi Wa sallam wa ‘alaa Aalihii wa Ashhabihii wa Azwaajihii wa Dzurriyyaatihii wa Ahli Baitihii wa Liman dakhola fii Baitihii ajma ‘iin, kullu syai’in lillahi lahum, Al-Fatihah",
            indonesianText = "Yaa اللّه semoga disampaikan pahala bacaan fatihah ini kehadapan Nabi Besar Muhammad SAW dan kepada keluarga, sahabat, istri, anak cucu dan ahli baitnya. Segala sesuatu hanya milik اللّه, untuk mereka (kami) hadiahkan, Al-Faatihah",
            sundaneseText = "Khatur ka payuneun Kangjeng Nabi Muhammad SAW sakulawargi, para sahabatna, garwana, turunanana, ahli baitna, sareng sing saha bae anu lebet ka bumina sadayana, Al-Faatihah",
            repetitionCount = 1,
            category = "Tawassul",
            kaifiyatNote = "Dibaca dengan khusyuk menghadapkan ruhani kepada Baginda Nabi SAW."
        ),
        DzikirItem(
            id = "dzikir_harian_2",
            number = 2,
            title = "Istighfar Pembuka (3x)",
            arabicText = "أَسْتَغْفِرُ اللّٰهَ الْغَفُوْرَ الرَّحِيْمَ",
            latinText = "Astaghfirulloohal Ghofuuror Rohiim (3x)",
            indonesianText = "Aku memohon ampunan kepada اللّه yang Maha Pengampun dan Maha Penyayang.",
            sundaneseText = "Abdi neda panghampura ka Gusti اللّه Nu Maha Ngahapunten tur Maha Asih.",
            repetitionCount = 3,
            category = "Istighfar",
            kaifiyatNote = "Dibaca 3x dengan merendahkan hati memohon ampunan."
        ),
        DzikirItem(
            id = "dzikir_harian_3",
            number = 3,
            title = "Sholawat Pembuka (3x)",
            arabicText = "اَللّٰهُمَّ صَلِّ عَلٰى سَيِّدِنَا مُحَمَّدٍ وَعَلٰى اٰلِهٖ وَصَحْبِهٖ وَسَلِّمْ",
            latinText = "Alloohumma sholli ‘alaa Sayyidinaa Muhammadiw wa ‘alaa aalihii Wa shohbihii wa sallim (3x)",
            indonesianText = "Yaa اللّه limpahkanlah rahmat-Mu kepada sayyidina Muhammad SAW dan kepada keluarga, dan para sahabatnya, serta limpahkanlah keselamatan baginya.",
            sundaneseText = "Nun Gusti اللّه mugi ngalungsurkeun rahmat ka Gusti junjungan abdi sadaya Kangjeng Nabi Muhammad SAW miwah kulawargana, para sahabatna, sareng salam karahayuan.",
            repetitionCount = 3,
            category = "Sholawat",
            kaifiyatNote = "Dibaca 3x menyambung mahabbah kepada Rasulullah SAW."
        ),
        DzikirItem(
            id = "dzikir_harian_4",
            number = 4,
            title = "Munajat Ilahi Anta Maqshudi",
            arabicText = "اِلَهِي اَنْتَ مَقْصُودِيْ وَرِضاَكَ مَطْلُوبِي اَعْطِنِيْ مَحَبَتَكَ وَمَعْرِفَتَكَ وَاَعْطِنِيْ مَعَكَ اَبَدَا",
            latinText = "Ilaahii Anta maqshuudii wa ridhoo-Ka mathluubii, a’thinii Mahabbata-Ka wa Ma’rifata-Ka wa ‘athinii ma’aka abada.",
            indonesianText = "Tuhanku Engkaulah yang menjadi maksudku dan keridhoan-Mu yang menjadi pintaku. Berikanlah kepadaku kecintaan dan ma’rifat kepada-Mu dan berikan aku selamanya bersamaMu.",
            sundaneseText = "Nun Nun Gusti Pangeran abdi, Gusti anu janten tujuan abdi sareng karidhoan Gusti anu dipilari ku abdi, mugi Gusti maparin katresnan Gusti sareng ma'rifat ka Gusti sarta lungsurkeun abdi salamina sareng Gusti.",
            repetitionCount = 1,
            category = "Munajat",
            kaifiyatNote = "Niat ikhlas memurnikan tujuan ibadah hanya kepada اللّه SWT semata."
        ),
        DzikirItem(
            id = "dzikir_harian_5",
            number = 5,
            title = "Tahlil Pembuka (3x)",
            arabicText = "لَا إِلٰهَ إِلَّا اللّٰهُ",
            latinText = "Laa Ilaaha Illallooh (3x)",
            indonesianText = "Tiada Tuhan selain اللّه (3x)",
            sundaneseText = "Teu aya deui Pangeran lian ti Gusti اللّه (3x)",
            repetitionCount = 3,
            category = "Tahlil",
            kaifiyatNote = "Dibaca 3x dengan irama dzikir jahr."
        ),
        DzikirItem(
            id = "dzikir_harian_6",
            number = 6,
            title = "Dzikir Jahr (165x)",
            arabicText = "لَا إِلٰهَ إِلَّا اللّٰهُ",
            latinText = "LAA ILAAHA ILLALLOOH",
            indonesianText = "Kemudian dilanjutkan dengan Zikir sekurang-kurangnya 165x. Lebih banyak lebih baik dan Zikir diakhiri pada hitungan bilangan ganjil.",
            sundaneseText = "Lajeng diteraskeun dzikir sakirang-kirangna 165x. Langkung seueur langkung sae tur dipungkas dina etangan ganjil.",
            repetitionCount = 165,
            category = "Dzikir Jahr Inti",
            kaifiyatNote = "Dzikir Jahr diucapkan bersuara dengan irama: memalingkan kepala dari lambung kanan kemudian dihunjamkan ke arah lathifah qolbi (dada kiri bawah). Standar hitungan 165x."
        ),
        DzikirItem(
            id = "dzikir_harian_7",
            number = 7,
            title = "Penutup Dzikir Jahr",
            arabicText = "سَيِّدُنَا مُحَمَّدٌ رَسُوْلُ اللّٰهِ صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمَ",
            latinText = "Sayyidunaa Muhammadur Rosuululloohi Shollalloohu ‘Alaihi wa Sallam",
            indonesianText = "Sayyidina Muhammad Rosulnya اللّه, semoga اللّه melimpahkan Rahmat dan keselamatan atas beliau.",
            sundaneseText = "Sayyidina Muhammad Rosululloh, mugi اللّه ngalimpahkeun Rahmat sareng kasalametan ka anjeunna.",
            repetitionCount = 1,
            category = "Penutup Dzikir",
            kaifiyatNote = "Dibaca dengan khusyuk sebagai penutup dzikir jahr."
        ),
        DzikirItem(
            id = "dzikir_harian_8",
            number = 8,
            title = "Doa Ba'da Dzikir (Munjiyat, Bai'at & Doa Miskin)",
            arabicText = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ\nاَللّٰهُمَّ صَلِّ عَلٰى سَيِّدِنَا مُحَمَّدٍ وَعَلٰى أٰلِ سَيِّدِنَا مُحَمَّدٍ صَلَاةً تُنْجِيْنَا بِهَا مِنْ جَمِيْعِ الْأَهْوَالِ وَالْأٰفَاتِ وَتَقْضِي لَنَا بِهَا جَمِيْعَ الْحَاجَاتِ وَتُطَهِّرُنَا بِهَا مِنْ جَمِيْعِ السَّيِّأٰتِ وَتَرْفَعُنَا بِهَا عِنْدَكَ أَعْلَى الدَّرَجَاتِ وَتُبَلِّغُنَا بِهَا أَقْصَى الْغَايَاتِ مِنْ جَمِيْعِ الْـخَيْرَاتِ فِي الْحَيَاةِ وَبَعْدَ الْمَمَاتِ، إِنَّ الَّذِينَ يُبَايِعُوْنَكَ إِنَّمَا يُبَايِعُوْنَ اللّٰهَ يَدُ اللّٰهِ فَوْقَ أَيْدِيْهِمْ فَمَنْ نَكَثَ فَإِنَّمَا يَنْكُثُ عَلٰى نَفْسِهٖ وَمَنْ اَوْفٰى بِمَا عَاهَد عَلَيْهُ اللّٰهَ فَسَيُؤْتِيْهِ أَجْرًا عَظِيْمًا\nٱللَّٰهُمَّ أَحْيِنِي مِسْكِينًا وَأَمِتْنِي مِسْكِينًا وَاحْشُرْنِي فِي زُمْرَةِ ٱلْمَسَاكِينِ",
            latinText = "Bismillaahirrohmaanirrohiim. Allohumma sholli ‘alaa Sayyidinaa Muhammad wa ‘alaa Aali Sayyidinaa Muhammad, sholaatan tunjiinaa bihaa min jamii’il ahwaali wal aafaat, wa taqdhiilanaa bihaa jamii’al haajaat, wa tuthohhirunaa bihaa min jamii’is sayyi’aat, wa tarfa’unaa bihaa ‘indaka a’lad darojaat, wa tuballighunaa bihaa aqshol ghooyaat min jamii’il khoiroot fil hayaati wa ba’dal mamaat. Innal ladziina yubaayi‘uunaka innamaa yubaayi ‘uunalloha, yadulloohi fauqo aidiihim, faman nakatsa fa’innamaa yankutsu ‘alaa nafsih, wa man aufaa bimaa ‘aahada ‘alaihullooha fasayu’tiihi ajron ‘azhiimaa. Allohumma ahyinii miskiinan, wa amitnii miskiinan, wahsyurnii fii jumrotil masaakiin",
            indonesianText = "Dengan menyebut nama اللّه yang Maha Pengasih lagi Maha Penyayang. Yaa اللّه limpahkan rahmat-Mu kepada Nabi Muhammad SAW dan keluarganya dengan rahmat yang akan menyelamatkan kami dari semua marabahaya dan mengabulkan bagi kami semua keperluan kami, dan membersihkan kami dari segala kesalahan, dan mengangkat kami ke derajat yang tinggi serta menyampaikan kami ke puncak sejak masih hidup sampai meninggal dunia. Sesungguhnya orang-orang yang berjanji setia kepada-Mu, itu sebenarnya mereka berjanji kepada اللّه. ‘Tangan اللّه’ di atas tangan-tangan (kekuasaan) mereka, maka barangsiapa melanggar janjinya, niscaya akibat dari melanggar janji itu akan menimpa dirinya sendiri, dan barangsiapa yang menepati janjinya kepada اللّه maka اللّه akan memberinya pahala yang besar. Ya اللّه, hidupkanlah aku dalam keadaan miskin, matikanlah aku dalam keadaan miskin, dan kumpulkanlah aku bersama rombongan orang-orang miskin (HR. Ibnu Majah: permohonan sifat tawadhu, khusyuk dan kerendahan hati).",
            sundaneseText = "Kalayan nyebat jenengan Gusti اللّه Nu Maha Welas tur Maha Asih. Nun Gusti mugi ngalimpahkeun rahmat kasalametan, ngabersihkeun sagala dosa, ngangkat darajat luhur, sarta nempatkeun abdi sadaya dina golongan jalma-jalma anu tawadhu tur dipikacinta ku Mantenna.",
            repetitionCount = 1,
            category = "Doa Ba'da Dzikir",
            kaifiyatNote = "Doa Sholawat Munjiyat, ikrar bai'at thariqah, serta doa ketawadhuan yang diajarkan Rasulullah SAW."
        ),
        DzikirItem(
            id = "dzikir_harian_9",
            number = 9,
            title = "Hadhrot Rasulullah SAW (Kedua)",
            arabicText = "اِلٰى حَضْرَةِ النَّبِيِّ الْمُصْطَفٰى مُحَمَّدٍ صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمَ وَعَلٰى اٰلِهٖ وَاَصْحَابِهٖ وَاَزْوَاجِهٖ وَذُرِّيّٰتِهٖ وَاَهْلِ بَيْتِهٖ وَلِمَنْ دَخَلَ فِي بَيْتِهٖ اَجْمَعِيْنَ كُلُّ شَيْئٍ لِلّٰهِ لَهُمُ الْفَاتِحَةُ",
            latinText = "Ilaa hadrotin nabiyyil musthofaa Muhammadin Shollalloohu ‘Alaihi Wa sallam wa ‘alaa Aalihii wa Ashhabihii wa Azwaajihii wa Dzurriyyaatihii wa Ahli Baitihii wa Liman dakhola fii Baitihii ajma ‘iin, kullu syai’in lillahi lahum, Al-Fatihah",
            indonesianText = "Semoga disampaikan kepada junjungan kami Nabi Muhammad SAW, semoga اللّه melimpahkan rahmat dan keselamatan kepanya, kepada keluarganya, para sahabatnya, istrinya, keturunannya serta ahli baitnya. Segala sesuatu hanya milik اللّه, untuk mereka (kami) hadiahkan, Al-Fatihah",
            sundaneseText = "Khatur ka payuneun Kangjeng Nabi Muhammad SAW sakulawargi, para sahabatna, garwana, turunanana, ahli baitna sadayana, Al-Faatihah",
            repetitionCount = 1,
            category = "Tawassul",
            kaifiyatNote = "Membaca Al-Fatihah kedua untuk Baginda Nabi SAW."
        ),
        DzikirItem(
            id = "dzikir_harian_10",
            number = 10,
            title = "Tawassul Silsilah MTQN Suryalaya Sirnarasa PPKN III",
            arabicText = "ثُمَّ اِلٰى أَهْلِ السِّلْسِلَةِ الْقَادِرِيَّةِ النَّقْشَبَنْدِيَّةِ مَعْهَدِ سُرْيَالَيَا سِرْناَ رَاسَا وَجَمِيْعِ أَهْلِ الطُّرُقِ خُصُوْصًا اِلٰى حَضْرَةِ سُلْطَانِ الْأَوْلِيَاءِ غَوْثِ الْأَعْظَمِ قُطْبِ الْعَالَمِيْنَ السَّيِّدِ الشَّيْخِ عَبْدِ الْقَادِرِ الْجَيْلَانِي قَدَّسَ اللّٰهُ سِرَّهُ وَالسَّيِّدِ الشَّيْخِ أبِي الْقَاسِمِ جُنَيْدِ الْبَغْدَادِي وَالسَّيِّدِ الشَّيْخِ أَحْمَدَ خَاطِبِ ابْنِ عَبْدِ الْغَفَّارِ السَّمْبَاسِيِّ وَالسَّيِّدِ الشَّيْخِ طَلْحَةَ كَالِي سَافُو السِرْبَوْنِي وَالسَّيِّدِ الشَّيْخِ عَبْدِ اللّٰهِ مُبَارَكِ بْنِ نُوْرِ مُحَمَّدٍ وَشَيْخِنَا الْمُكَرَّمِ الشَّيْخِ أَحْمَدَ صَاحِبِ الْوَفٰى تَاجِ الْعَارِفِيْنَ وَشَيْخِنَا الْمُكَرَّمِ الشَّيْخِ مُحَمَّدْ عَبْدُ الْغَوْثِ سَيْفُ اللّٰهِ مَسْلُوْلُ قَدَّسَ اللّٰهُ سِرَّهمُ وَأُصُوْلِهِمْ وَفُرُوْعِهِمْ وَاَهْلِ سِلْسِلَتِهِمْ وَالْأۤخِذِيْنَ عَنْهُمْ كُلُّ شَيْئ ٍ لِلّٰهِ لَهُمُ الْفَاتِحَةُ",
            latinText = "Tsumma ilaa Ahli Silsilatil Qoodiriyyah Naqsyabandiyyah MTQN Suryalaya Sirnarasa PPKN III wa jamii’i ahlith thuruqi khushuushon ilaa Hadroti shulthoonil auliyaa’I ghoutsil a’zhom quthbil ‘aalamiin, As Sayyidisy Syaikh Muhyiddin ‘Abdul Qoodir Al Jailaani Qoddasalloohu Sirroh wa Sayyidisy Syaikh Abil Qoosim Junaidil Baghdaadiy Wa Sayyidisy Syaikh Ahmad Khootib Syambas ibni ‘Abdil Ghoffaar Wa Sayyidisy Syaikh Tholhah Kalisapu Cirebon wa hadhroti Syaikh ‘Abdulloh Mubarook bin Nur Muhammad wa Sayyidisy Syaikh Ahmad Shoohibul Wafaa Taajul ‘Aarifin wa syaikhinal mukarrom Syaikh Muhammad Abdul Gaos Saefulloh Maslul Qaddasalloohu Sirrohum wa ushuulihim Wa furuu‘ihim wa ahli silsilaatihim wal aakhidziina ‘anhum, kullu syai’in lillahi lahum, Al-Fatihah",
            indonesianText = "Semoga Engkau sampaikan kepada para silsilah Thoriqot Qoodiriyyah Naqsabandiyyah MTQN Suryalaya Sirnarasa PPKN III dan kepada semua ahli thoriqot terutama kepada pimpinan para wali penolong agama اللّه Syaikh Abdul Qodir al Jailani. Semoga اللّه melimpahkan kesucian kepada maqomnya dan kepada Syaikh Abdul Qosim Junaidi al Baghdadi serta Syaikh Ahmad Khotib as Syambasyi Abdil Ghoffar dan Syaikh Tholhah bin Tholabuddin dan Syaikh Abdulloh Mubarok bin Nur Muhammad serta Syaikh Ahmad Shohibul Wafa Tajul’Arifin, dan Syaikh Muhammad Abdul Gaos Saefulloh Maslul Qaddasalloohu Sirrohum semoga اللّه melimpahkan keridhoan kepada mereka dan kepada leluhurnya, kepada anak turunnya dan ahli keluarga silsilah serta semua yang mengambil berkah dari mereka. Segala sesuatu hanya milik اللّه, untuk mereka (kami) hadiahkan, Al-Fatihah",
            sundaneseText = "Lajeng khatur ka para ahli Silsilah MTQN Suryalaya Sirnarasa PPKN III, Syekh Abdul Qodir Al-Jailani Qs., Syekh Junaid Al-Baghdadi, Syekh Ahmad Khatib Sambas, Syekh Tolhah Kalisapu, Syekh Abdullah Mubarok (Abah Sepuh), Syekh Ahmad Shohibulwafa Tajul Arifin (Abah Anom), miwah Guru Mursyid urang sadaya Syekh Muhammad Abdul Gaos Saefulloh Maslul (Abah Aos Ra. Qs. 38), Al-Faatihah.",
            repetitionCount = 1,
            category = "Silsilah",
            kaifiyatNote = "Menyambungkan rabithah qolbiyyah kepada Silsilah Emas MTQN Suryalaya Sirnarasa PPKN III 38."
        ),
        DzikirItem(
            id = "dzikir_harian_11",
            number = 11,
            title = "Hadiah Fatihah untuk Orang Tua & Kaum Muslimin",
            arabicText = "ثُمَّ اِلٰى أَرْوَاحِ أٰبَائِنَا وَأُمَّهَاتِنَا وَلِكَافَّةِ الْمُسْلِمِيْنَ وَالْمُسْلِمَاتِ وَالْمُؤْمِنِيْنَ وَالْمُؤْمِنَاتِ .والمحسنين والمحسنات.الْأَحْيَاءِ مِنْهُمْ وَالْأَمْوَاتِ كُلُّ شَيْئ ٍ لِلّٰهِ لَهُمُ الْفَاتِحَةُ",
            latinText = "Tsumma ilaa arwaahi Aaba’inaa wa Ummahaatinaa wa likaaffatil Muslimiina wal Muslimaat wal Mu’miniina wal Mu’minaat wal muhsiniina wal muhsinaati al Ahyaa’i minhum wal amwaat, kullu syai’in lillaahi lahum, Al-Fatihah",
            indonesianText = "Selanjutnya semoga اللّه menyampaikan kepada bapak-bapak kami dan ibu-ibu kami dan kepada semua muslimin dan muslimat, mukminin dan mukminat, yang masih hidup maupun yang telah meninggal dunia. Segala sesuatu hanya milik اللّه, untuk mereka (kami) hadiahkan, Al-Fatihah",
            sundaneseText = "Lajeng khatur ka arwah sepuh urang sadaya, ibu rama urang, ka sakumna muslimin muslimat, mukminin mukminat, anu masih keneh jumeneng atanapi parantos ngantunkeun, Al-Faatihah.",
            repetitionCount = 1,
            category = "Tawassul",
            kaifiyatNote = "Mendoakan keselamatan dan barokah bagi kedua orang tua dan umat Islam."
        ),
        DzikirItem(
            id = "dzikir_harian_12",
            number = 12,
            title = "Istighfar Taubat (3x)",
            arabicText = "اَسْتَغْفِرُ اللّٰهَ رَبِّي مِنْ كُلِّ ذَنْبٍ وَاَتُوْبُ اِلَيْهِ",
            latinText = "Astaghfirullooha Robbii min kulli dzanbin wa atuubu ilaihi (3x)",
            indonesianText = "Aku memohon ampunan kepada اللّه Tuhanku dari segala dosa dan aku bertaubat kepada-Nya.",
            sundaneseText = "Abdi neda panghampura ka Gusti اللّه Pangeran abdi tina saniskara dosa tur abdi tobat ka Mantenna.",
            repetitionCount = 3,
            category = "Istighfar",
            kaifiyatNote = "Dibaca 3x dengan ketundukan jiwa."
        ),
        DzikirItem(
            id = "dzikir_harian_13",
            number = 13,
            title = "Sholawat Ibrahimiyah",
            arabicText = "اَللّٰهُمَّ صَلِّ عَلٰى سَيِّدِنَا مُحَمَّدٍ وَعَلٰى أٰلِ سَيِّدِنَا مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى سَيِّدِنَا اِبْرَاهِيْمَ وَعَلٰى أٰلِ اِبْرَاهِيْمَ وَبَارِكْ عَلٰى سَيِّدِنَا مُحَمَّدٍ وَعَلٰى أٰلِ سَيِّدِنَا مُحَمَّدٍ كَمَا بَارَكْتَ عَلٰى سَيِّدِنَا اِبْرَاهِيْمَ وَعَلٰى أٰلِ اِبْرَاهِيْمَ فِي الْعَالَمِيْنَ إِنَّكَ حَمِيْدٌ مَجِيْدٌ",
            latinText = "Alloohumma sholli ‘alaa sayyidinaa Muhammad wa ‘alaa Aali Sayyidinaa Muhammad, kamaa shollaita ‘alaa sayyidinaa Ibroohim wa ‘alaa Aali sayyidinaa Ibroohim, wa baarik ‘alaa sayyidinaa Muhammad Wa ‘alaa Aali sayyidinaa Muhammad, kamaa baarokta ‘alaa sayyidinaa Ibroohim wa ‘alaa Aali sayyidinaa Ibroohim, fil ‘aalamiina innaka Hamiidum Majiid.",
            indonesianText = "Yaa اللّه Semoga Engkau melimpahkan rahmat kepada Nabi Muhammad dan keluarganya seperti halnya Engkau melimpahkan rahmat kepada Nabi Ibrahim dan keluarganya dan semoga Engkau melimpahkan barokah kepada Nabi Muhammad dan keluarganya seperti halnya Engkau melimpahkan barokah kepada Nabi Ibrahim dan keluarganya di seluruh alam. Engkau Maha Terpuji lagi Maha Agung.",
            sundaneseText = "Nun Gusti اللّه mugi ngalimpahkeun rahmat sareng kabarokahan ka Kangjeng Nabi Muhammad miwah kulawargana sakumaha Gusti parantos maparin rahmat ka Kangjeng Nabi Ibrahim miwah kulawargana di sakuliah alam. Saestuna Gusti Maha Pinuji tur Maha Agung.",
            repetitionCount = 1,
            category = "Sholawat Ibrahimiyah",
            kaifiyatNote = "Dibaca dengan penuh khidmat sebelum memasuki sesi Tawajuh Dzikir Khofi."
        )
    )

    fun getDzikirKhofiList(): List<DzikirItem> = listOf(
        DzikirItem(
            id = "dzikir_khofi_tawajuh",
            number = 1,
            title = "Tata Cara Tawajuh (129 Detik / 2 Menit 9 Detik)",
            arabicText = "اللَّهُ • اللَّهُ • اللَّهُ (فِي الْقَلْبِ)",
            latinText = "ALLOH... ALLOH... ALLOH... (Dzikir Khofi di dalam Lathifah Qolbi)",
            indonesianText = "Selanjutnya Tawajuh Selama 129 Detik ( 2 menit 9 detik ) dengan cara:\n– Kedua mata terpejam\n– Bibir dirapatkan\n– Lidah dilipatkan ke langit-langit\n– Gigi dirapatkan tidak bergerak\n– Menahan nafas sekuatnya\n– Kepala ditundukkan ke sebelah kiri\n– Hati tanpa berhenti ber- Dzikir Khofi sekuatnya.",
            sundaneseText = "Tawajuh salami 129 detik (2 menit 9 detik) kalayan tata cara: panon peureum, lambey dirapetkeun, letah ditilepkeun ka lalangit, waos dirapetkeun teu usik, nahan napas sakuatna, mastaka ditundukkeun ka kenca, manah teu kendat dzikir khofi sakuatna.",
            repetitionCount = 129,
            category = "Tawajuh Khofi",
            kaifiyatNote = "Tawajuh 129 detik dengan 7 adab dzikir khofi dan menyambungkan rabithah kepada Guru Mursyid 38."
        ),
        DzikirItem(
            id = "dzikir_khofi_asy_syura",
            number = 2,
            title = "Ayat Asy-Syura: 19 (38x)",
            arabicText = "اَللّٰهُ لَطِيْفٌۢ بِعِبَادِهٖ يَرْزُقُ مَنْ يَّشَاۤءُۚ وَهُوَ الْقَوِيُّ الْعَزِيْزُࣖ",
            latinText = "Allāhu laṭīfum bi’ibādihī yarzuqu may yasyā’, wa huwal-qawiyyul-‘azīz (38x)",
            indonesianText = "اللّه Mahalembut terhadap hamba-hamba-Nya. Dia memberi rezeki kepada siapa yang Dia kehendaki. Dia Mahakuat lagi Mahaperkasa. (Dibaca 38x)",
            sundaneseText = "Gusti اللّه Maha Lembut ka para hamba-Na. Mantenna maparin rezeki ka sing saha anu dipikersa ku Mantenna. Mantenna Maha Kiat tur Maha Gagah Perkasa. (Dimaos 38x)",
            repetitionCount = 38,
            category = "Ayat Pilihan 38x",
            kaifiyatNote = "Dibaca 38x ba'da Tawajuh Dzikir Khofi."
        )
    )

    fun getDailyPrayersList(): List<SpecialPrayer> = listOf(
        SpecialPrayer(
            id = "doa_subuh_maghrib_persatuan",
            title = "Doa Persatuan & Kesatuan (Subuh & Maghrib)",
            arabicTitle = "دعاء جمع الشمل والوحدة",
            category = "Doa Khusus Subuh & Maghrib",
            arabicText = "عَسَى اللّٰهُ أَن يَجْعَلَ بَيْنَكُمْ وبَيْنَ الَّذِيْنَ عَادَيْتُم مِّنْهُم مَّوَدَّةً وَاللّٰهُ قَدِيْرٌ وَاللّٰهُ غَفُوْرٌ رَّحِيْمٌ، رَبِّ إِنِّي ظَلَمْتُ نَفْسِي فَاغْفِرْ لِي ذَنْبِيْ، لَا إِلٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            latinText = "‘Asalloohu ayyaj’ala bainakum wa bainal ladziina ‘adaitum minhhum mawadataw walloohhu qodiiruw walloohhu ghofuurur rohiimu robbi innii zholamtu nafsii faghfirlii dzambii laa ilaahha illa angta subhaanaka innii kungtu minazh zhoolimiina (3x).",
            indonesianText = "Semoga اللّه menumbuhkan kasih sayang di antara kalian dan termasuk pada orang-orang yang memusuhi kalian. اللّه Maha Kuasa dan Maha Pengampun lagi Maha Penyayang. Tiada Tuhan selain Engkau. Maha Suci Engkau, sesungguhnya aku adalah orang yang menganiaya kepada diriku sendiri. (3x)",
            sundaneseText = "Mugia Gusti اللّه numwuhkeun rasa nyaah di antawis aranjeun sareng jalma-jalma anu mikangewa ka aranjeun. اللّه Maha Kawasa tur Maha Asih...",
            kaifiyat = "Doa untuk menggalang persatuan dan kesatuan baik lingkungan keluarga, masyarakat maupun negara. Dibaca 3x khusus ba'da Subuh dan Maghrib.",
            virtue = "Mengharmoniskan hubungan antar sesama dan meredam permusuhan.",
            recommendedTime = "Ba'da Sholat Shubuh & Maghrib"
        ),
        SpecialPrayer(
            id = "doa_subuh_maghrib_benteng",
            title = "Doa Benteng dari Gangguan Musuh (Subuh & Maghrib)",
            arabicTitle = "دعاء الحصن والاعتصام",
            category = "Doa Khusus Subuh & Maghrib",
            arabicText = "اَللّٰهُمَّ صَحًّا صَحًّا صَحًّا وَحًّا بَحًّا حٰمۤ لَا يُنْصَرُوْنَ ، وَجَعَلْنَا مِنْ بَيْنِ أَيْدِيهِمْ سَدًّا وَّمِنْ خَلْفِهِمْ سَدًّا فَأَغْشَيْنَاهُمْ فَهُمْ لَا يُبْصِرُوْنَ ، كۤهٰيٰعۤصۤ حٰمۤ عۤسۤقۤ لَا يُصَدَّعُوْنَ عَنْهَا وَلَا يُنْزِفُوْنَ ، يَا رَبُّ يَا رَبُّ يَا رَبُّ وَلَاحَوْلَ وَلَاقُوَّةَ إِلَّا بِاللّٰهِ الْعَلِيِّ الْعَظِيْم",
            latinText = "Alloohhumma shohhan-shohhan-shohhan wa han bahan haa-mim laa yungshoruuna wa ja’alnaa mim baini aydiihhim saddaw wa min kholfihhim saddang fa-aghsyainaahhum fahhum laa yubshiruuna kaf-hha-ya-‘ain-shod-ha-mim-‘ain-sin-qof laa yushodda’uuna ‘anhhaa wa laa yungzifuuna yaa robbu-yaa robbu-yaa robbu wa laa haula wa laa quwwata illa billaahhil ‘aliyyil ‘azhiimi (3x).",
            indonesianText = "Ya اللّه, sehatkanlah-sehatkanlah-sehatkanlah, tuluskanlah ha-mim orang yang zholim tidak akan mendapat pertolongan. Dan Kami adakan di hadapan mereka dinding, dan di belakang mereka dinding. Dan Kami tutup mata mereka, sehingga mereka tidak melihat, kaf-hha-ya-‘ain-shod-ha-mim-‘ain-sin-qof, tiadalah mereka mematahkan dari pada-Nya, dan tidaklah mereka bisa mengeluarkan. Ya Rob-Ya Rob-Ya Rob, tiada daya dan upaya kecuali dari اللّه Yang Maha Luhur dan Maha Agung. (3x)",
            sundaneseText = "Nun Gusti اللّه mugi maparin kasalametan, kasehatan, sareng panyalindungan tina sagala rereged gangguan lahir batin...",
            kaifiyat = "Doa sebagai benteng dari gangguan musuh baik dari dalam dan luar, serta berserah diri total hanya pada اللّه. Dibaca 3x ba'da Subuh dan Maghrib.",
            virtue = "Benteng perlindungan kokoh dari segala makar dan marabahaya musuh.",
            recommendedTime = "Ba'da Sholat Shubuh & Maghrib"
        ),
        SpecialPrayer(
            id = "doa_subuh_maghrib_penangkal_bala",
            title = "Doa Penangkal Penyakit & Bala / Asy-Syafii",
            arabicTitle = "دعاء الشفاء ودفع البلاء",
            category = "Doa Khusus Subuh & Maghrib",
            arabicText = "بِسْمِ اللّٰهِ الشَّافِي بِسْمِ اللّٰهِ الْكَافِي بِسْمِ اللّٰهِ الْمَعَافِي بِسْمِ اللّٰهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            latinText = "Bismillaahisy syaafii bismillaahhil kaafii bismillaahhil ma’aafii bismillaahhil ladzii laa yadlurru ma’asmihhii syai-um fiil ardli wa laa fiis samaa-i wa hhuwas samii’ul ‘aliimu (3x).",
            indonesianText = "Dengan Nama اللّه Yang Maha Menyembuhkan, dengan Nama اللّه Yang Maha Mencukupi, dengan Nama اللّه Yang Maha Menyehatkan, dengan Nama اللّه yang melalui Nama-Nya segala sesuatu yang ada di bumi dan di langit tidak membahayakan. Dan Dia-lah Yang Maha Mengetahui. (3x)",
            sundaneseText = "Kalayan Jenengan اللّه Nu Maha Nyageurkeun, Maha Nyukupan, Maha Nga'afiatkeun, anu ku Jenengan-Na teu aya bahaya naon wae di bumi sareng di langit...",
            kaifiyat = "Doa untuk penangkal dari berbagai penyakit / bala lahir bathin. Dibaca 3x ba'da Subuh dan Maghrib.",
            virtue = "Pencegah segala wabah penyakit dan marabahaya.",
            recommendedTime = "Ba'da Sholat Shubuh & Maghrib"
        ),
        SpecialPrayer(
            id = "doa_subuh_maghrib_keberkahan_posisi",
            title = "Doa Keberkahan Posisi & Profesi",
            arabicTitle = "دعاء البركة في المنزلة",
            category = "Doa Khusus Subuh & Maghrib",
            arabicText = "رَبَّنَا أَنْزِلْنَا مُنْزَلًا مُّبَارَكًا وَّأَنْتَ خَيْرُ الْمُنْزِلِيْنَ",
            latinText = "Robbanaa angzilnaa mungzalam mubaarokaw wa angta khoirul mungziliina (3x).",
            indonesianText = "Ya Tuhanku, tempatkanlah aku pada tempat yang penuh berkah. Dan Engkaulah Dzat yang sebaik-baiknya memberikan tempat. (3x)",
            sundaneseText = "Nun Gusti Pangeran abdi sadaya, mugi nempatkeun abdi dina tempat anu pinuh ku kabarokahan...",
            kaifiyat = "Doa untuk keberkahan dalam posisi dan profesi yang di ridhoi اللّه. Dibaca 3x.",
            virtue = "Meraih kemuliaan dan keberkahan dalam pekerjaan dan kedudukan hidup.",
            recommendedTime = "Ba'da Sholat Shubuh & Maghrib"
        ),
        SpecialPrayer(
            id = "doa_subuh_maghrib_pintu_hidayah",
            title = "Doa Pembuka Pintu Hidayah",
            arabicTitle = "دعاء فتح أبواب الهداية",
            category = "Doa Khusus Subuh & Maghrib",
            arabicText = "رَبَّنَا افْتَحْ بَيْنَنَا وَبَيْنَ قَوْمِنَا بِالْحَقِّ وَأَنْتَ خَيْرُ الْفَاتِحيْنَ",
            latinText = "Robbanaftah bainana wabaina qouminaa bil haqqi wa anta khoirul faatihiin (3x).",
            indonesianText = "Ya Tuhanku, bukakanlah di antara kami dan di antara kaum kami kebenaran. Dan hanya Engkaulah sebaik-baik pembukanya. (3x)",
            sundaneseText = "Nun Gusti Pangeran abdi, mugi muka lawang bebeneran di antawis abdi sadaya sareng kaum abdi sadaya...",
            kaifiyat = "Doa untuk membukakan pintu hidayah urusan dunia maupun akhirat. Dibaca 3x.",
            virtue = "Membuka solusi persoalan dan jalan hidayah kebenaran.",
            recommendedTime = "Ba'da Sholat Shubuh & Maghrib"
        ),
        SpecialPrayer(
            id = "doa_subuh_maghrib_kesabaran",
            title = "Doa Kesabaran & Pertolongan",
            arabicTitle = "دعاء الصبر والنصر",
            category = "Doa Khusus Subuh & Maghrib",
            arabicText = "رَبَّنَاۤ أَفْرِغْ عَلَيْنَا صَبْرًا وَثَبِّتْ أَقْدَامَنَا وَانْصُرْنَا عَلَى القَوْمِ الْكَافِرِينَ",
            latinText = "Rabbana afrigh ‘alaina shabran wa tsabbit aqdamana wanshurna ‘alal qoumil kafirin (3x).",
            indonesianText = "Ya Tuhanku, limpahkanlah kesabaran atas diri kami, kokohkanlah pendirian kami serta tolonglah kami dalam mengalahkan orang-orang kafir. (3x)",
            sundaneseText = "Nun Gusti Pangeran abdi sadaya, kucurkeun kasabaran dina diri abdi sadaya, pancegkeun pendirian abdi sadaya...",
            kaifiyat = "Doa agar diberi kesabaran hati dan pertolongan dari orang-orang kafir. Dibaca 3x.",
            virtue = "Meneguhkan ketabahan iman dan kemenangan batin.",
            recommendedTime = "Ba'da Sholat Shubuh & Maghrib"
        ),
        SpecialPrayer(
            id = "doa_subuh_maghrib_masker_anti_virus",
            title = "Doa Masker Anti Segala Virus",
            arabicTitle = "دعاء الوقاية من الأوبئة",
            category = "Doa Khusus Subuh & Maghrib",
            arabicText = "اَللَّهُمَّ اِنِّى اَعُوْذُ بِكَ مِنْ شَرِّ نَفْسِيْ وَمِنْ شَرِّ كُلِّ دَابَّةٍ اَنْتَ أخِذٌ بِناَصِيَتِهَا اِنَّ رَبِّي عَلَى صِرَاطٍ مُسْتَقِيْمْ",
            latinText = "Allohumma inni a’udzu bika min syarri nafsiy wamin syarri kulli daaabbatin anta aakhidzun bina shiyatihaa inna robbii ‘alla shiroothin mustaqiem",
            indonesianText = "Ya اللّه, sesungguhnya aku berlindung kepada-Mu dari keburukan diriku sendiri dan dari keburukan setiap makhluk melata yang Engkau genggam ubun-ubunnya. Sesungguhnya Tuhanku berada di atas jalan yang lurus.",
            sundaneseText = "Nun Gusti اللّه, saestuna abdi nyalindung ka Gusti tina kajahatan hawa nafsu abdi sareng kajahatan sakur makhluq...",
            kaifiyat = "Doa masker batin anti segala virus dan penyakit. Dilaksanakan 1x setelah Sholat Maghrib dan 1x setelah Sholat Shubuh.",
            virtue = "Tameng perlindungan biologis dan spiritual.",
            recommendedTime = "1x Ba'da Sholat Maghrib & 1x Ba'da Sholat Shubuh"
        ),
        SpecialPrayer(
            id = "doa_subuh_maghrib_robbi_yassir",
            title = "Doa Kemudahan & Kesempurnaan Amal (Robbii Yassir)",
            arabicTitle = "دعاء التيسير والتمام",
            category = "Doa Khusus Subuh & Maghrib",
            arabicText = "رَبِّ يَسِّرْ لَنَا وَلَا تُعَسِّرْ عَلَيْنَا رَبِّي تَمِّمْ لَنَا بِالـْخَيْرِ أَعْمَالَنَا",
            latinText = "Robbii yassir lanaa, walaa tu’assir ‘alainaa, Robbii tammim lanaa bikhairi a’malana (3x)",
            indonesianText = "Ya Tuhanku, mudahkanlah segala sesuatu bagi kami, janganlah Kau persulit atas kami. Yaa اللّه Tuhanku, berikanlah kesempurnaan atas kami dengan segala kebaikan atas perbuatan kami. (3x)",
            sundaneseText = "Nun Gusti Pangeran abdi, mugi ngagampilkeun sagala rupi perkawis ka abdi sadaya, tong disulitkeun, sarta sampurnakeun sagala amal kasaean abdi sadaya.",
            kaifiyat = "Doa agar dimudahkan dan disempurnakan اللّه Azza Wa Jalla segala urusan amal dan ibadah. Dibaca 3x.",
            virtue = "Pembuka kemudahan urusan dunia dan akhirat.",
            recommendedTime = "Ba'da Sholat Shubuh & Maghrib"
        ),
        SpecialPrayer(
            id = "doa_sebelum_tidur",
            title = "Doa & Wirid Sebelum Tidur",
            arabicTitle = "دعاء قبل النوم",
            category = "Harian",
            arabicText = "بِاسْمِكَ اللَّهُمَّ أَحْيَا وَبِاسْمِكَ أَمُوتُ • أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
            latinText = "Bismika Allahumma ahyaa wa bismika amuut. A'uudzu bikalimaatillaahit taammaati min syarri maa khalaq.",
            indonesianText = "Dengan nama-Mu ya اللّه aku hidup dan dengan nama-Mu aku mati. Aku berlindung dengan kalimat-kalimat اللّه yang sempurna dari kejahatan apa yang Dia ciptakan.",
            sundaneseText = "Kalayan jenengan Gusti nun اللّه abdi hirup tur kalayan jenengan Gusti abdi maot. Abdi nyalindung kalayan kalimah-kalimah اللّه anu sampurna tina kajahatan saniskara makhluq.",
            kaifiyat = "Amaliyah sebelum tidur: Berwudhu, membaca Ayat Kursi, Surat Al-Ikhlas, Al-Falaq, An-Nas 3x ditiupkan ke telapak tangan lalu diusapkan ke seluruh tubuh, membaca doa tidur dan menyandarkan rabithah kepada Guru Mursyid sambil meneruskan Dzikir Khofi hingga tertidur.",
            virtue = "Menjaga ruhani senantiasa tersambung dengan Allah SWT dan terpelihara dari gangguan syaitan saat terlelap.",
            recommendedTime = "Sebelum tidur malam"
        ),
        SpecialPrayer(
            id = "tarhim_subuh",
            title = "Tarhim Fajar",
            arabicTitle = "الترحيم قبل الصبح",
            category = "Tarhim",
            arabicText = "الصَّلَاةُ وَالسَّلَامُ عَلَيْكَ، يَا إِمَامَ الْمُجَاهِدِينَ، يَا رَسُولَ اللَّهِ. الصَّلَاةُ وَالسَّلَامُ عَلَيْكَ، يَا نَاصِرَ الْهُدَى، يَا خَيْرَ خَلْقِ اللَّهِ. الصَّلَاةُ وَالسَّلَامُ عَلَيْكَ، يَا مَنْ أَسْرَى بِكَ الْمُهَيْمِنُ لَيْلًا، نِلْتَ مَا نِلْتَ وَالْأَنَامُ نِيَامٌ.",
            latinText = "Ash-shalaatu was salaamu 'alaik, yaa imaamal mujaahidiin, yaa Rasuulallaah. Ash-shalaatu was salaamu 'alaik, yaa naashiral hudaa, yaa khaira khalqillaah. Ash-shalaatu was salaamu 'alaik, yaa man asraa bikal muhaiminu lailan, nilta maa nilta wal anaamu niyaam.",
            indonesianText = "Shalawat dan salam semoga tercurah kepadamu, wahai pemimpin para pejuang, wahai Rasulullah. Shalawat dan salam semoga tercurah kepadamu wahai penolong petunjuk, wahai sebaik-baik makhluk اللّه. Shalawat dan salam semoga tercurah kepadamu wahai sosok yang diperjalankan oleh اللّه di waktu malam, engkau meraih derajat agung tatkala seluruh manusia terlelap.",
            sundaneseText = "Shalawat miwah salam mugi lungsur ka Gusti panutan, Nun pamingpin para pejuang, Nun Rasulullah...",
            kaifiyat = "Dilantunkan menjelang adzan Shubuh untuk membangunkan jiwa dan menyambut fajar shodiq.",
            virtue = "Menenangkan hati dan menggetarkan kecintaan kepada Baginda Rasulullah SAW.",
            recommendedTime = "15-30 menit sebelum Adzan Shubuh"
        ),
        SpecialPrayer(
            id = "salam_wali_mursyid",
            title = "Salam Kepada Wali Mursyid",
            arabicTitle = "السلام على الولي المرشد",
            category = "Tawassul Khusus",
            arabicText = "اَلسَّلَامُ عَلَيْكَ يَا مَالِكَ الزَّمَانِ، وَ يَا إِمَامَ الْمَكَانِ، وَ يَا قَائِمَ بِأَمْرِ الرَّحْمَانِ، وَ يَا وَارِثَ الْكِتَابِ، وَ يَا نَائِبَ رَسُوْلِ اللهِ صَلَّى اللهُ عَلَيْهِ وَ سَلَّمَ، يَا مَنْ مِنَ السَّمَاءِ وَ الْأَرْضِ عَائِدَتُهُ، يَا مَنْ أَهْلُ وَقْتِهِ كُلُّهُمْ عَائِلَتُهُ، يَا مَنْ يَنزِلُ الْغَيْثُ بِدَعْوَتِهِ، وَ يُدَرُّ الضَّرْعُ بِبَرَكَتِهِ، وَ رَحْمَةُ اللهِ وَ بَرَكَاتُهُ، الْفَاتِحَة.",
            latinText = "Assalaamu 'alaika yaa maalikaz zamaan, wa yaa imaamal makaan, wa yaa qooimu biamrir rohmaan, wa yaa waaritsal kitaab, wa yaa naaiba Rosuulillaahi SAW, yaa man minassamaa-i wal ardhi 'aa-idatuh, yaa man ahlu waqtihii kulluhum 'aa-ilatuh, yaa man yanzilul ghoitsu bida'watih, wa yadirrudh-dhor'u bibarokatih, wa rohmatulloohi wa barokaatuh, Al-Faatihah...",
            indonesianText = "Salam untukmu wahai penguasa zaman, pemimpin wilayah, penegak ketentuan Ar-Rahman, pewaris kitab, wakil Rasulullah SAW, yang selalu pergi pulang antara bumi dan langit, yang orang-orang sezamannya adalah keluarganya, yang diturunkan pertolongan karena doanya, yang dikucurkan limpah susu karena keberkahannya, beserta rahmat اللّه dan keberkahan-Nya, Al-Fatihah.",
            sundaneseText = "Kasalametan mugi tetep ka salira nun pangersa pamingpin jaman, pamingpin wilayah, nu ngadegkeun parentah Gusti Nu Maha Welas, ahli waris Al-Kitab, wakil Kangjeng Rasulullah SAW, nu lungsur-unggah antara bumi sareng langit, nu sakur jalma di jaman ieu sadayana janten kulawargana, nu lungsur pitulung karana du'ana, nu ngocor limpas susuna karana kabarokahanana, miwah rahmat اللّه sareng barokah-Na, Al-Fatihah.",
            kaifiyat = "Dibaca ketika hendak memulai amalan penting, tawassul, atau saat memasuki majlis ziarah dan rabithah qalbi.",
            virtue = "Memperkuat ikatan batin (Rabithah) dengan Guru Mursyid penghubung sanad 38 kepada Rasulullah SAW.",
            recommendedTime = "Setiap saat / Ba'da Sholat & Manaqib"
        ),
        SpecialPrayer(
            id = "doa_rijalul_ghoib",
            title = "Doa Rijalul Ghoib",
            arabicTitle = "دعاء رجال الغيب",
            category = "Tawassul Khusus",
            arabicText = "بِسْمِ اللهِ الرَّحْمَنِ الرَّحِيمِ، اَلسَّلَامُ عَلَيْكُمْ يَارِجَالَ الْغَيْبِ، اَلسَّلَامُ عَلَيْكُمْ يَا أَيُّهَا الْأَرْوَاحُ الْمُقَدَّسَةُ، يَا نُقَبَا يَا نُجَبَا يَا رُقَبَا يَا بُدَلَا، يَا أَوْتَادَ الْأَرْضِ أَوْتَادٌ أَرْبَعَةٌ، يَا إِمَامَانِ يَا قُطْبُ يَا فَرْدُ يَا أُمَنَاءُ، اَغِيْثُوْنِيْ بِغَوْثَةٍ وَانْظُرُوْنِيْ بِنَظْرَةٍ وَارْحَمُوْنِيْ بِرَحْمَةٍ، وَحَصِّلُوْا مُرَادِيْ وَمَقَاصِدِيْ، وَقُوْمُوْا عَلَى قَضَاءِ حَوَائِجِيْ عِنْدَ نَبِيِّنَا مُحَمَّدٍ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ، سَلَّمَكُمُ اللهُ تَعَالَى فِي الدُّنْيَا وَالْآخِرَةِ، اَللَّهُمَّ صَلِّ عَلَى نَبِيِّ الْخِضْرِ عَلَيْهِ السَّلَامُ، اَلْفَاتِحَةُ...",
            latinText = "Bismillaahirrohmaanirrohiim. Assalaamu'alaikum yaa rijaalal ghoib. Assalaamu'alaikum yaa ayyuhal arwaahul muqoddasah. Yaa nuqobaa, yaa nujabaa, yaa ruqobaa, yaa budalaa, yaa autadal ardhi, autaadun arba'ah, yaa imaamaani, yaa quthbu yaa fardu yaa umanaa, Aghiitsuunii bighoutsatin, wandhuruunii binadhrotin, warhamuunii birohmatin, wahasshiluu muroodii wamaqooshidii waquumuu 'alaa qodhooi hawaaijii 'inda nabiyyinaa Muhammadin SAW, Sallamakumulloohu ta'aalaa fiddunyaa wal aakhiroh, Alloohumma sholli 'alannabiyyil Khidhir 'alaihissalaam, Alfaatihah...",
            indonesianText = "Keselamatan atasmu wahai Rijalul Ghoib, keselamatan atasmu wahai segenap arwah yang suci, wahai segenap Wali Nuqabaa, Wali Nujabaa, Wali Ruqabaa, Wali Abdal, wahai para Wali Paku Alam dari empat penjuru angin, wahai Wali Imam, Wali Qutub, Wali Tunggal, Wali Pengaman Dunia. Tolonglah aku dengan pertolonganmu, lihatlah aku dengan penglihatanmu, kasihilah aku dengan rahmatmu, kabulkanlah keinginan dan maksud kami, dan dukunglah aku dalam menunaikan hajatku atas seidzin Nabi Muhammad SAW. Selamatkanlah kami ya اللّه di dunia dan di akhirat, semoga اللّه menambah rahmat-Nya atas Nabi Khidr AS, Al-Fatihah.",
            sundaneseText = "Kasalametan mugi tetep ka salira nun Rijalul Ghoib, kasalametan ka sakumna arwah anu saruci, nun Wali Nuqaba, Nujaba, Ruqaba, Abdal, paku bumi opat madhab, nun Wali Imam, Qutub, Tunggal, Pangreksa Dunya...",
            kaifiyat = "Dibaca saat bertawassul kepada para Rijalul Ghoib, saat mempunyai hajat penting, atau ketika berziarah ke maqam auliya.",
            virtue = "Memohon pertolongan barokah karomah Rijalullah dan para wali kekasih Allah SWT.",
            recommendedTime = "Setiap saat / Ba'da Sholat & Tawassul"
        ),
        SpecialPrayer(
            id = "sholawat_jiyaaroh",
            title = "Sholawat Jiyaaroh Ke Rosululloh",
            arabicTitle = "صلاة الزيارة إلى رسول الله",
            category = "Doa & Ziarah",
            arabicText = "بِسْمِ اللهِ الرَّحْمَنِ الرَّحِيمِ، اَلصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا رَسُوْلَ اللهِ ، اَلصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا حَبِيْبَ اللهِ ، اَلصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا كَرِيْمَ اللهِ ، اَلصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا سَيِّدَ الْكَوْنَيْنِ ، اَلصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا سَيِّدَ الثَّقَلَيْنِ ، اَلصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا أَحْمَدُ ، اَلصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا طٰهٰ ، اَلصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا يس ، وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، ... اَلْفَاتِحَة",
            latinText = "Assholatu Wassalamualaika Yaa Rosuulalloh, Assholatu Wassalamualaika Yaa Habiballoh, Assholatu Wassalamualaika Yaa Karimalloh, Assholatu Wassalamualaika Yaa Sayyidal Kaunaini, Assholatu Wassalamualaika Yaa Sayyidas Syakolaini, Assholatu Wassalamualaika Yaa Ahmadu, Assholatu Wassalamualaika Yaa Tooha, Assholatu Wassalamualaika Yaa Yaasiin Warohmatullohi Wabarokatuh, Al-Faatihah",
            indonesianText = "Shalawat dan salam semoga tercurah kepadamu wahai Rasulullah, wahai Kekasih اللّه, wahai yang Mulia di sisi اللّه, wahai Pemimpin dua alam, wahai Pemimpin bangsa manusia dan jin, wahai Ahmad, wahai Thaha, wahai Yasin, beserta rahmat اللّه dan keberkahan-Nya, Al-Fatihah.",
            sundaneseText = "Shalawat sinareng salam mugi tetep ka Gusti panutan Nun Rasulullah SAW, Nun kakasih اللّه...",
            kaifiyat = "Dibaca saat berziarah ke Maqam Baginda Rasulullah SAW di Madinah Al-Munawwarah atau saat bersholawat dan rabithah rindu kepada Rasulullah SAW.",
            virtue = "Mendekatkan jiwa dan menyambungkan ruhaniyah kecintaan kepada Baginda Rasulullah SAW.",
            recommendedTime = "Setiap saat / Ba'da Sholat & Ziarah"
        ),
        SpecialPrayer(
            id = "doa_istighotsah",
            title = "Doa Istighotsah",
            arabicTitle = "دُعَاءُ الْاِسْتِغَاثَةِ",
            category = "Doa & Ziarah",
            arabicText = "بِسْمِ اللهِ الرَّحْمَنِ الرَّحِيمِ، يَا شَيْخَ مُحْيِ الدِّيْنِ يَا سَيِّدَ مُحْيِ الدِّيْنِ . يَا مَوْلَانَا مُحْيِ الدِّيْنِ يَا مَخْدُوْمَ مُحْيِ الدِّيْنِ . يَا خَوَاجَه مُحْيِ الدِّيْنِ يَا شَاهْ مُحْيِ الدِّيْنِ، يَا دَرْوِيْشَ مُحْيِ الدِّيْنِ يَا قُطْبَ مُحْيِ الدِّيْنِ . يَا سُلْطَانَ مُحْيِ الدِّيْنِ يَا غَوْثَ مُحْيِ الدِّيْنِ . يَا سَيِّدَ السَّادَاتِ مُحْيِ الدِّيْنِ عَبْدَ الْقَادِرِ . يَا عُبَيْدَ اللهِ أَغِثْنَا بِإِذْنِ اللهِ ، وَيَا شَيْخَ الثَّقَلَيْنِ . أَغِثْنَا وَ امْدِدْنَا فِي قَضَاءِ حَوَائِجِنَا خَيْرِي الدُّنيَا وَالآ خِرَة . وارزقنا هما يافاعل كُلِّ خَيْرٍ ويا هاديا الى كلِّ خيرٍ . ويا دا لاً على كلِّ خيرٍ ويا اهل الخير . ويا خالق الخير ويااهل الخيرات . ويا خالق الخير ويااهل الخيرات . انت الله والله انت الله لا اله الا انت . اللّهم لك الكلّ وبك الكلّ ومنك الكلّ واليك الكلّ . وانت الكلّ وكلُّ الكلِّ برحمتك ياأرحم الراحمينَ . وصلى الله على سيدنا محمد وعلى آله واصحابه . وعلى سائر الانبياء والمرسلين وعلى آلهم واصحابهم . اجمعين والحمد لله رب العالمين ... اَلْفَاتِحَة",
            latinText = "Yaa man yablughu limuriidihi 'indal istighootsati walau kaana fil masyriqi farosuka masruujun wa saifuka masluulun wa romhuka man shuubun wa qowsuka mautuurun wa sahmuka shoo-ibun wa rikaabuka 'aalin Aghisnii fii qodhoo-i hawaa-iji khoiro yiddunyaa wal aakhiroti ........ (sebut hajatnya). Al-Faatihah",
            indonesianText = "Wahai orang yang sampai kepada muridnya ketika permohonan bantuan disampaikan, meski ia berada jauh ditimur kudamu telah siap dengan pelananya, pedangmu telah terhunus, tombakmu siap dilemparkan, busurmu telah ditarik, anak panahmu siap membidik sasaran, dan tungganganmu amat tinggi. Tolonglah kami dalam memenuhi segala hajat yang baik urusan dunia dan akhirat.",
            sundaneseText = "Nun pangersa anu dugi ka muridna nalika panyuhunkeun pitulung ditepikeun...",
            kaifiyat = "Dibaca saat munajat istighotsah memohon pertolongan dan kelancaran hajat dunia akhirat.",
            virtue = "Washilah agung memohon pertolongan dan inayah Allah SWT melalui karomah Syaikh Abdul Qadir Al-Jailani.",
            recommendedTime = "Setiap saat / Malam Jumat / Majlis Istighotsah"
        ),
        SpecialPrayer(
            id = "doa_rajab_syaban",
            title = "Doa Bulan Rajab & Nisfu Sya'ban",
            arabicTitle = "دعاء شهر رجب وشعبان",
            category = "Bulanan",
            arabicText = "اللَّهُمَّ بَارِكْ لَنَا فِي رَجَبَ وَشَعْبَانَ وَبَلِّغْنَا رَمَضَانَ وَسَلِّمْنَا لِرَمَضَانَ وَسَلِّمْ رَمَضَانَ لَنَا وَتَسَلَّمْهُ مِنَّا مُتَقَبَّلًا",
            latinText = "Allaahumma baarik lanaa fii Rajaba wa Sya'baana wa ballighnaa Ramadhaana, wa sallimnaa li Ramadhaana wa sallim Ramadhaana lanaa wa tasallamhu minnaa mutaqabbalaa.",
            indonesianText = "Ya اللّه, berkahilah kami di bulan Rajab dan Sya'ban, dan sampaikanlah usia kami pada bulan Ramadhan, selamatkanlah kami untuk Ramadhan, selamatkanlah Ramadhan untuk kami, dan terimalah amalan Ramadhan dari kami.",
            sundaneseText = "Nun Gusti اللّه, mugi ngalimpahkeun kabarokahan ka abdi sadaya dina sasih Rajab sareng Sya'ban...",
            kaifiyat = "Dibaca setiap hari sejak awal masuk bulan Rajab hingga akhir Sya'ban.",
            virtue = "Meraih berkah kesucian Rajab, kemuliaan Sya'ban, dan kesiapan ruhani menyambut Ramadhan.",
            recommendedTime = "Sepanjang bulan Rajab & Sya'ban"
        )
    )

    fun get12BulanHijriyahList(): List<HijriyahAmaliyah> = listOf(
        HijriyahAmaliyah(
            monthNumber = 1,
            monthName = "Muharram",
            arabicName = "مُحَرَّم",
            virtues = "Bulan pembuka tahun Hijriyah, salah satu dari 4 bulan haram yang mulia. Di dalamnya terdapat hari Asyura (10 Muharram) yang penuh berkah dan ampunan.",
            recommendedAmalan = listOf(
                "Puasa Sunnah Tasu'a (9 Muharram) dan Asyura (10 Muharram).",
                "Menyantuni dan mengusap kepala anak yatim pada 10 Muharram.",
                "Memperbanyak sedekah dan melapangkan nafkah untuk keluarga.",
                "Membaca Doa Awal & Akhir Tahun serta Dzikir Khofi."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 2,
            monthName = "Shafar",
            arabicName = "صَفَر",
            virtues = "Bulan kedua Hijriyah. MTQN Suryalaya Sirnarasa PPKN III mengajarkan penguatan amaliyah tolak bala (Li Daf'il Bala') pada Rebo Wekasan (Rabu terakhir bulan Shafar).",
            recommendedAmalan = listOf(
                "Sholat Sunnah Li Daf'il Bala' 4 rakaat 2 salam pada Rebo Wekasan.",
                "Membaca Surat Yasin dan doa permohonan keselamatan dari segala marabahaya.",
                "Memperbanyak sedekah tolak bala dan shalawat munjiyat.",
                "Memperteguh Dzikir Jahr 165x ba'da sholat."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 3,
            monthName = "Rabi'ul Awwal",
            arabicName = "رَبِيعُ الْأَوَّل",
            virtues = "Bulan kelahiran Baginda Nabi Agung Muhammad SAW (Maulidur Rasul). Bulan bertabur shalawat dan rasa syukur atas rahmat terbesar alam semesta.",
            recommendedAmalan = listOf(
                "Mengikuti majlis pembacaan Maulid Nabi (Barzanji, Diba'i, Simthuddurar).",
                "Memperbanyak membaca Shalawat Bani Hasyim dan Shalawat Badriyah.",
                "Mempelajari Sirah Nabawiyyah dan meneladani akhlak Rasulullah SAW.",
                "Menyelenggarakan tasyakuran dan berbagi hidangan keberkahan."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 4,
            monthName = "Rabi'ul Akhir",
            arabicName = "رَبِيعُ الْآخِر",
            virtues = "Bulan Haul Agung Sulthonul Auliya Syekh Abdul Qodir Al-Jailani r.a. (11 Rabi'ul Akhir). Bulan puncak khidmat Manaqib MTQN Suryalaya Sirnarasa PPKN III.",
            recommendedAmalan = listOf(
                "Menghadiri atau menyelenggarakan Khidmat Amaliah Manaqib Syekh Abdul Qodir Al-Jailani.",
                "Mengkhatamkan pembacaan Tanbih, Tawassul, Manqobah, dan Sholawat.",
                "Memperbanyak sedekah manaqib dan menjamu tamu ikhwan.",
                "Meningkatkan Dzikir Jahr dan Dzikir Khofi."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 5,
            monthName = "Jumadil Ula",
            arabicName = "جُمَادَى الْأُولَى",
            virtues = "Bulan kelima Hijriyah. Momentum muhasabah diri, keteguhan menuntut ilmu agama, dan istiqomah di majlis dzikir.",
            recommendedAmalan = listOf(
                "Istiqomah Dzikir Ba'da Sholat (Jahr 165x & Khofi).",
                "Membaca Al-Qur'an dan memperbanyak sholat sunnah rawatib.",
                "Menjaga kerukunan dan silaturahmi antar ikhwan thoriqoh.",
                "Menghadiri pengajian kitab dan majlis ta'lim."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 6,
            monthName = "Jumadil Akhir",
            arabicName = "جُمَادَى الْآخِرَة",
            virtues = "Bulan keenam Hijriyah. Penguatan amalan dzikir harian dan pembersihan hati menyongsong bulan-bulan haram berikutnya.",
            recommendedAmalan = listOf(
                "Memperbanyak istighfar dan taubat nasuha.",
                "Meningkatkan frekuensi Dzikir Khofi saat beraktivitas sehari-hari.",
                "Sholat Tahajjud dan sholat sunnah Taubat di sepertiga malam.",
                "Sedekah subuh istiqomah."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 7,
            monthName = "Rajab",
            arabicName = "رَجَب",
            virtues = "Bulan Rajab, bulannya Allah Ta'ala. Salah satu bulan haram yang agung, peristiwa Isra Mi'raj (27 Rajab), dan anjuran sholat sunnah Rajab.",
            recommendedAmalan = listOf(
                "Membaca Doa Masuk Bulan Rajab & Sya'ban setiap hari.",
                "Sholat Sunnah Rajab khusus (malam 1, malam Jumat pertama, pertengahan, dan akhir Rajab).",
                "Puasa sunnah Rajab (terutama tanggal 1, 27, dan Ayyamul Bidh).",
                "Memperbanyak membaca Sayyidul Istighfar dan Shalawat."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 8,
            monthName = "Sya'ban",
            arabicName = "شَعْبَان",
            virtues = "Bulan Sya'ban, bulannya Rasulullah SAW. Bulan pelaporan amal, bertabur ampunan pada Malam Nisfu Sya'ban (15 Sya'ban).",
            recommendedAmalan = listOf(
                "Sholat Sunnah Nisfu Sya'ban dan membaca Surat Yasin 3x setelah Maghrib.",
                "Memperbanyak puasa sunnah di bulan Sya'ban sebagaimana kebiasaan Rasulullah SAW.",
                "Membaca doa Nisfu Sya'ban untuk panjang umur barakah, keluasan rezeki, dan husnul khotimah.",
                "Memperbanyak shalawat atas Nabi SAW."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 9,
            monthName = "Ramadhan",
            arabicName = "رَمَضَان",
            virtues = "Sayyidus Syuhur (Penghulu segala bulan). Bulan diturunkannya Al-Qur'an, kewajiban puasa, Sholat Tarawih, Nuzulul Qur'an, dan Malam Lailatul Qadar.",
            recommendedAmalan = listOf(
                "Menunaikan ibadah Puasa Ramadhan dengan menjaga lisan dan hati.",
                "Sholat Tarawih 20 Rakaat dan Witir 3 Rakaat berjamaah dengan amaliyah MTQN Suryalaya Sirnarasa PPKN III.",
                "Tadarus dan khataman Al-Qur'an.",
                "I'tikaf dan Sholat Lailatul Qadar pada 10 malam terakhir.",
                "Membayar Zakat Fitrah dan memperbanyak infaq sedekah."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 10,
            monthName = "Syawwal",
            arabicName = "شَوَّال",
            virtues = "Bulan kemenangan Hari Raya Idul Fitri (1 Syawwal) dan keutamaan Puasa Sunnah 6 hari Syawwal yang pahalanya setara berpuasa setahun penuh.",
            recommendedAmalan = listOf(
                "Sholat Idul Fitri dan saling bermaaf-maafan.",
                "Puasa Sunnah 6 hari di bulan Syawwal.",
                "Menyambung silaturahmi ke Guru Mursyid, orang tua, kerabat, dan sahabat.",
                "Mempertahankan kualitas ibadah Ramadhan."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 11,
            monthName = "Dzulqa'dah",
            arabicName = "ذُو الْقَعْدَة",
            virtues = "Bulan kesebelas Hijriyah, salah satu dari 4 bulan haram yang mulia. Bulan ketenangan, persiapan ibadah haji, dan pengendalian hawa nafsu.",
            recommendedAmalan = listOf(
                "Puasa sunnah Senin-Kamis dan Ayyamul Bidh (13, 14, 15 Dzulqa'dah).",
                "Memperbanyak Dzikir Jahr 165x dan Dzikir Khofi terus-menerus.",
                "Menghindari pertikaian, maksiat, dan menjaga kesucian hati.",
                "Mendoakan para jamaah calon haji."
            )
        ),
        HijriyahAmaliyah(
            monthNumber = 12,
            monthName = "Dzulhijjah",
            arabicName = "ذُو الْحِجَّة",
            virtues = "Bulan puncak ibadah Haji, 10 hari pertama yang paling dicintai Allah, Hari Arafah (9 Dzulhijjah), Sholat Idul Adha (10 Dzulhijjah), dan Hari Tasyrik.",
            recommendedAmalan = listOf(
                "Puasa Sunnah 9 hari pertama Dzulhijjah (khususnya Puasa Tarwiyah dan Arafah).",
                "Melaksanakan Ibadah Qurban bagi yang mampu.",
                "Sholat Hari Raya Idul Adha berjamaah.",
                "Membaca Takbir pada hari-hari Tasyrik (11, 12, 13 Dzulhijjah).",
                "Membaca Doa Akhir Tahun Hijriyah pada sore hari akhir Dzulhijjah."
            )
        )
    )

    fun getSholatSunnahList(): List<SpecialPrayer> = listOf(
        SpecialPrayer(
            id = "sholat_rajab",
            title = "Sholat Sunnah Bulan Rojab",
            arabicTitle = "صَلَاةُ رَجَبٍ الْمَخْصُوْصَةُ",
            category = "Sholat Sunnah",
            arabicText = "أُصَلِّي سُنَّةَ شَهْرِ رَجَبَ رَكْعَتَيْنِ مُسْتَقْبِلَ الْقِبْلَةِ (مَأْمُوْمًا / إِمَامًا) لِلّٰهِ تَعَالَى",
            latinText = "Ushallii sunnatan syahri Rajaba rak'ataini mustaqbilal qiblati (ma'muuman / imaaman) lillaahi Ta'aalaa.",
            indonesianText = "Aku niat sholat sunnah bulan Rajab dua rakaat menghadap kiblat (sebagai makmum/imam) karena اللّه Ta'ala.",
            sundaneseText = "Niat abdi sholat sunnah sasih Rajab dua rakaat mayun ka kiblat (ma'mum/imam) karana اللّه Ta'ala.",
            kaifiyat = "Jumlah keseluruhan 42 rakaat dilaksanakan ba'da ba'diyah Maghrib:\n1. Tgl 1 Rajab: 10 rakaat (5 salam), tiap rakaat ba'da Al-Fatihah baca Al-Ikhlas 3x & Al-Kafirun 3x. Ba'da salam baca doa: Laa ilaaha illalloohu wahdahu laa syariikalah...\n2. Malam Jumat Pertama: 12 rakaat (6 salam, diawali puasa siang harinya), tiap rakaat ba'da Al-Fatihah baca Al-Qadr 3x & Al-Ikhlas 12x. Ba'da sholat baca Sholawat Nabi Ummi 70x, Sujud Tasbih 70x, Duduk Istighfar 70x, Sujud lagi Tasbih 70x.\n3. Tgl 15 Rajab: 10 rakaat (5 salam), baca Al-Ikhlas 3x & Al-Kafirun 3x. Ba'da salam baca doa tauhid.\n4. Tgl 30 Rajab (Akhir Bulan): 10 rakaat (5 salam), baca Al-Ikhlas 3x & Al-Kafirun 3x. Ba'da salam baca doa penutup.",
            virtue = "Melebur dosa, memperoleh ampunan dan keselamatan dunia-akhirat, serta dicatat dalam golongan hamba yang dicintai Allah SWT.",
            recommendedTime = "Antara Maghrib dan Isya (ba'da ba'diyah Maghrib) di bulan Rajab",
            rakaatCount = 42
        ),
        SpecialPrayer(
            id = "sholat_nisfu_syaban",
            title = "Sholat Sunnah Nisfu Sya'ban",
            arabicTitle = "صلاة ليلة نصف شعبان",
            category = "Sholat Sunnah",
            arabicText = "أُصَلِّي سُنَّةَ نِصْفِ شَعْبَانَ رَكْعَتَيْنِ لِلَّهِ تَعَالَى",
            latinText = "Ushallii sunnata nisfi Sya'baana rak'ataini lillaahi Ta'aalaa.",
            indonesianText = "Aku niat sholat sunnah Nisfu Sya'ban dua rakaat karena اللّه Ta'ala.",
            sundaneseText = "Niat abdi sholat sunnah Nisfu Sya'ban dua rakaat karana اللّه Ta'ala.",
            kaifiyat = "Dikerjakan pada malam ke-15 Sya'ban. Setiap rakaat setelah Al-Fatihah membaca Surat Al-Ikhlas 10x atau membaca Surat Yasin 3x diselingi doa Nisfu Sya'ban setelah sholat.",
            virtue = "Mendapat ampunan Allah SWT, ditetapkan takdir yang baik, dan dijauhkan dari segala marabahaya.",
            recommendedTime = "Malam 15 Sya'ban (ba'da Maghrib)",
            rakaatCount = 2
        ),
        SpecialPrayer(
            id = "sholat_tarawih_tqn",
            title = "Sholat Tarawih 20 Rakaat & Witir",
            arabicTitle = "صلاة التراويح والوتر",
            category = "Sholat Sunnah",
            arabicText = "أُصَلِّي سُنَّةَ التَّرَاوِيحِ رَكْعَتَيْنِ (إِمَامًا / مَأْمُومًا) لِلَّهِ تَعَالَى",
            latinText = "Ushallii sunnatat taraawiihi rak'ataini (imaaman / ma'muuman) lillaahi Ta'aalaa.",
            indonesianText = "Aku niat sholat sunnah Tarawih dua rakaat (sebagai imam/makmum) karena اللّه Ta'ala.",
            sundaneseText = "Niat abdi sholat sunnah Tarawih dua rakaat (imam/makmum) karana اللّه Ta'ala.",
            kaifiyat = "Dikerjakan 20 rakaat (10 salam) tiap 2 rakaat salam, diselingi shalawat dan doa tarawih tiap 4 rakaat. Dilanjutkan Sholat Witir 3 rakaat (2 rakaat salam + 1 rakaat salam) dan membaca doa witir serta Dzikir.",
            virtue = "Menghidupkan malam Ramadhan, melebur dosa-dosa yang telah lalu, dan meraih ridha Allah.",
            recommendedTime = "Malam hari di bulan Ramadhan ba'da Isya",
            rakaatCount = 20
        ),
        SpecialPrayer(
            id = "sholat_lailatul_qadar",
            title = "Sholat Sunnah Lailatul Qadar",
            arabicTitle = "صلاة ليلة القدر",
            category = "Sholat Sunnah",
            arabicText = "أُصَلِّي سُنَّةَ لَيْلَةِ الْقَدْرِ رَكْعَتَيْنِ لِلَّهِ تَعَالَى",
            latinText = "Ushallii sunnata lailatil qadri rak'ataini lillaahi Ta'aalaa.",
            indonesianText = "Aku niat sholat sunnah Lailatul Qadar dua rakaat karena اللّه Ta'ala.",
            sundaneseText = "Niat abdi sholat sunnah Lailatul Qadar dua rakaat karana اللّه Ta'ala.",
            kaifiyat = "Dilaksanakan pada 10 malam terakhir Ramadhan (terutama malam-malam ganjil). Tiap rakaat ba'da Al-Fatihah membaca Surat At-Takatsur 1x dan Al-Ikhlas 3x (atau Al-Qadr 1x dan Al-Ikhlas 3x). Ba'da sholat memperbanyak doa: Allahumma innaka 'afuwwun tuhibbul 'afwa fa'fu 'annii.",
            virtue = "Meraih kemuliaan malam yang lebih utama daripada seribu bulan.",
            recommendedTime = "10 malam terakhir Ramadhan sepertiga malam",
            rakaatCount = 2
        ),
        SpecialPrayer(
            id = "sholat_lidafil_bala",
            title = "Sholat Sunnah Li Daf'il Bala' (Rebo Wekasan)",
            arabicTitle = "صلاة لدفع البلاء",
            category = "Sholat Sunnah",
            arabicText = "أُصَلِّي سُنَّةً لِدَفْعِ الْبَلَاءِ رَكْعَتَيْنِ لِلَّهِ تَعَالَى",
            latinText = "Ushallii sunnatan li daf'il balaa-i rak'ataini lillaahi Ta'aalaa.",
            indonesianText = "Aku niat sholat sunnah tolak bala dua rakaat karena اللّه Ta'ala.",
            sundaneseText = "Niat abdi sholat sunnah tolak bala dua rakaat karana اللّه Ta'ala.",
            kaifiyat = "Dilaksanakan pada hari Rabu terakhir bulan Shafar pada waktu Dhuha sebanyak 4 rakaat (2 kali salam). Setiap rakaat setelah Al-Fatihah membaca:\n1. Surat Al-Kautsar 17x\n2. Surat Al-Ikhlas 5x\n3. Surat Al-Falaq 1x\n4. Surat An-Nas 1x\nSetelah salam membaca Doa Tolak Bala / Doa Rebo Wekasan.",
            virtue = "Memohon perlindungan Allah SWT dari 320.000 marabahaya dan bala yang diturunkan pada akhir bulan Shafar.",
            recommendedTime = "Rabu terakhir bulan Shafar waktu Dhuha",
            rakaatCount = 4
        )
    )
}
