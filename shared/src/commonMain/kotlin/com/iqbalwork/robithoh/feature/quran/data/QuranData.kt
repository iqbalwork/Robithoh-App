package com.iqbalwork.robithoh.feature.quran.data

import com.iqbalwork.robithoh.feature.quran.model.Ayah
import com.iqbalwork.robithoh.feature.quran.model.RevelationType
import com.iqbalwork.robithoh.feature.quran.model.SurahMeta

object QuranData {

    val surahs: List<SurahMeta> = listOf(
        SurahMeta(1, "Al-Fatihah", "الفاتحة", "The Opening", "Pembukaan", 7, RevelationType.MAKKIYAH, "al_fatihah_ad_dhuha.mpeg"),
        SurahMeta(2, "Al-Baqarah", "البقرة", "The Cow", "Sapi Betina", 286, RevelationType.MADANIYAH),
        SurahMeta(3, "Ali 'Imran", "آل عمران", "The Family of Imran", "Keluarga Imran", 200, RevelationType.MADANIYAH),
        SurahMeta(4, "An-Nisa'", "النساء", "The Women", "Wanita", 176, RevelationType.MADANIYAH),
        SurahMeta(5, "Al-Ma'idah", "المائدة", "The Table Spread", "Jamuan Hidangan", 120, RevelationType.MADANIYAH),
        SurahMeta(6, "Al-An'am", "الأنعام", "The Cattle", "Binatang Ternak", 165, RevelationType.MAKKIYAH),
        SurahMeta(7, "Al-A'raf", "الأعراف", "The Heights", "Tempat yang Tertinggi", 206, RevelationType.MAKKIYAH),
        SurahMeta(8, "Al-Anfal", "الأنفال", "The Spoils of War", "Harta Rampasan Perang", 75, RevelationType.MADANIYAH),
        SurahMeta(9, "At-Taubah", "التوبة", "The Repentance", "Pengampunan", 129, RevelationType.MADANIYAH),
        SurahMeta(10, "Yunus", "يونس", "Jonah", "Nabi Yunus", 109, RevelationType.MAKKIYAH),
        SurahMeta(11, "Hud", "هود", "Hud", "Nabi Hud", 123, RevelationType.MAKKIYAH),
        SurahMeta(12, "Yusuf", "يوسف", "Joseph", "Nabi Yusuf", 111, RevelationType.MAKKIYAH),
        SurahMeta(13, "Ar-Ra'd", "الرعد", "The Thunder", "Guruh", 43, RevelationType.MADANIYAH),
        SurahMeta(14, "Ibrahim", "إبراهيم", "Abraham", "Nabi Ibrahim", 52, RevelationType.MAKKIYAH),
        SurahMeta(15, "Al-Hijr", "الحجر", "The Rocky Tract", "Negeri Kaum Tsamud", 99, RevelationType.MAKKIYAH),
        SurahMeta(16, "An-Nahl", "النحل", "The Bee", "Lebah", 128, RevelationType.MAKKIYAH),
        SurahMeta(17, "Al-Isra'", "الإسراء", "The Night Journey", "Perjalanan Malam", 111, RevelationType.MAKKIYAH),
        SurahMeta(18, "Al-Kahf", "الكهف", "The Cave", "Gua", 110, RevelationType.MAKKIYAH),
        SurahMeta(19, "Maryam", "مريم", "Mary", "Siti Maryam", 98, RevelationType.MAKKIYAH),
        SurahMeta(20, "Thaha", "طه", "Ta-Ha", "Thaha", 135, RevelationType.MAKKIYAH),
        SurahMeta(21, "Al-Anbiya'", "الأنبياء", "The Prophets", "Nabi-Nabi", 112, RevelationType.MAKKIYAH),
        SurahMeta(22, "Al-Hajj", "الحج", "The Pilgrimage", "Haji", 78, RevelationType.MADANIYAH),
        SurahMeta(23, "Al-Mu'minun", "المؤمنون", "The Believers", "Orang-Orang Mukmin", 118, RevelationType.MAKKIYAH),
        SurahMeta(24, "An-Nur", "النور", "The Light", "Cahaya", 64, RevelationType.MADANIYAH),
        SurahMeta(25, "Al-Furqan", "الفرقان", "The Criterion", "Pembeda", 77, RevelationType.MAKKIYAH),
        SurahMeta(26, "Asy-Syu'ara'", "الشعراء", "The Poets", "Penyair", 227, RevelationType.MAKKIYAH),
        SurahMeta(27, "An-Naml", "النمل", "The Ant", "Semut", 93, RevelationType.MAKKIYAH),
        SurahMeta(28, "Al-Qashash", "القصص", "The Stories", "Kisah-Kisah", 88, RevelationType.MAKKIYAH),
        SurahMeta(29, "Al-'Ankabut", "العنكبوت", "The Spider", "Laba-Laba", 69, RevelationType.MAKKIYAH),
        SurahMeta(30, "Ar-Rum", "الروم", "The Romans", "Bangsa Romawi", 60, RevelationType.MAKKIYAH),
        SurahMeta(31, "Luqman", "لقمان", "Luqman", "Keluarga Luqman", 34, RevelationType.MAKKIYAH),
        SurahMeta(32, "As-Sajdah", "السجدة", "The Prostration", "Sujud", 30, RevelationType.MAKKIYAH),
        SurahMeta(33, "Al-Ahzab", "الأحزاب", "The Combined Forces", "Golongan yang Bersekutu", 73, RevelationType.MADANIYAH),
        SurahMeta(34, "Saba'", "سبأ", "Sheba", "Kaum Saba'", 54, RevelationType.MAKKIYAH),
        SurahMeta(35, "Fathir", "فاطر", "The Originator", "Pencipta", 45, RevelationType.MAKKIYAH),
        SurahMeta(36, "Yasin", "يس", "Ya-Sin", "Yasin", 83, RevelationType.MAKKIYAH),
        SurahMeta(37, "Ash-Shaffat", "الصافات", "Those Who Set The Ranks", "Barisan-Barisan", 182, RevelationType.MAKKIYAH),
        SurahMeta(38, "Shad", "ص", "The Letter Shad", "Shad", 88, RevelationType.MAKKIYAH),
        SurahMeta(39, "Az-Zumar", "الزمر", "The Troops", "Rombongan", 75, RevelationType.MAKKIYAH),
        SurahMeta(40, "Ghafir", "غافر", "The Forgiver", "Yang Mengampuni", 85, RevelationType.MAKKIYAH),
        SurahMeta(41, "Fushshilat", "فصلت", "Explained in Detail", "Yang Dijelaskan", 54, RevelationType.MAKKIYAH),
        SurahMeta(42, "Asy-Syura", "الشورى", "The Consultation", "Musyawarah", 53, RevelationType.MAKKIYAH),
        SurahMeta(43, "Az-Zukhruf", "الزخرف", "The Ornaments of Gold", "Perhiasan", 89, RevelationType.MAKKIYAH),
        SurahMeta(44, "Ad-Dukhan", "الدخان", "The Smoke", "Kabut", 59, RevelationType.MAKKIYAH),
        SurahMeta(45, "Al-Jatsiyah", "الجاثية", "The Crouching", "Yang Berlutut", 37, RevelationType.MAKKIYAH),
        SurahMeta(46, "Al-Ahqaf", "الأحقاف", "The Wind-Curved Sandhills", "Bukit-Bukit Pasir", 35, RevelationType.MAKKIYAH),
        SurahMeta(47, "Muhammad", "محمد", "Muhammad", "Nabi Muhammad", 38, RevelationType.MADANIYAH),
        SurahMeta(48, "Al-Fath", "الفتح", "The Victory", "Kemenangan", 29, RevelationType.MADANIYAH),
        SurahMeta(49, "Al-Hujurat", "الحجرات", "The Rooms", "Kamar-Kamar", 18, RevelationType.MADANIYAH),
        SurahMeta(50, "Qaf", "ق", "The Letter Qaf", "Qaf", 45, RevelationType.MAKKIYAH),
        SurahMeta(51, "Adz-Dzariyat", "الذاريات", "The Winnowing Winds", "Angin yang Menerbangkan", 60, RevelationType.MAKKIYAH),
        SurahMeta(52, "Ath-Thur", "الطور", "The Mount", "Bukit Thur", 49, RevelationType.MAKKIYAH),
        SurahMeta(53, "An-Najm", "النجم", "The Star", "Bintang", 62, RevelationType.MAKKIYAH),
        SurahMeta(54, "Al-Qamar", "القمر", "The Moon", "Bulan", 55, RevelationType.MAKKIYAH),
        SurahMeta(55, "Ar-Rahman", "الرحمن", "The Beneficent", "Yang Maha Pemurah", 78, RevelationType.MADANIYAH),
        SurahMeta(56, "Al-Waqi'ah", "الواقعة", "The Inevitable", "Hari Kiamat", 96, RevelationType.MAKKIYAH),
        SurahMeta(57, "Al-Hadid", "الحديد", "The Iron", "Besi", 29, RevelationType.MADANIYAH),
        SurahMeta(58, "Al-Mujadilah", "المجادلة", "The Pleading Woman", "Wanita yang Mengajukan Gugatan", 22, RevelationType.MADANIYAH),
        SurahMeta(59, "Al-Hasyr", "الحشر", "The Exile", "Pengusiran", 24, RevelationType.MADANIYAH),
        SurahMeta(60, "Al-Mumtahanah", "الممتحنة", "She That Is To Be Examined", "Wanita yang Diuji", 13, RevelationType.MADANIYAH),
        SurahMeta(61, "Ash-Shaff", "الصف", "The Ranks", "Barisan", 14, RevelationType.MADANIYAH),
        SurahMeta(62, "Al-Jumu'ah", "الجمعة", "The Congregation (Friday)", "Hari Jum'at", 11, RevelationType.MADANIYAH),
        SurahMeta(63, "Al-Munafiqun", "المنافقون", "The Hypocrites", "Orang-Orang Munafik", 11, RevelationType.MADANIYAH),
        SurahMeta(64, "At-Taghabun", "التغابن", "The Mutual Disillusion", "Hari Ditampakkan Kesalahan", 18, RevelationType.MADANIYAH),
        SurahMeta(65, "Ath-Thalaq", "الطلاق", "The Divorce", "Talak", 12, RevelationType.MADANIYAH),
        SurahMeta(66, "At-Tahrim", "التحريم", "The Prohibition", "Mengharamkan", 12, RevelationType.MADANIYAH),
        SurahMeta(67, "Al-Mulk", "الملك", "The Sovereignty", "Kerajaan", 30, RevelationType.MAKKIYAH),
        SurahMeta(68, "Al-Qalam", "القلم", "The Pen", "Pena", 52, RevelationType.MAKKIYAH),
        SurahMeta(69, "Al-Haqqah", "الحاقة", "The Reality", "Hari Kiamat yang Benar", 52, RevelationType.MAKKIYAH),
        SurahMeta(70, "Al-Ma'arij", "المعارج", "The Ascending Stairways", "Tempat Naik", 44, RevelationType.MAKKIYAH),
        SurahMeta(71, "Nuh", "نوح", "Noah", "Nabi Nuh", 28, RevelationType.MAKKIYAH),
        SurahMeta(72, "Al-Jinn", "الجن", "The Jinn", "Jin", 28, RevelationType.MAKKIYAH),
        SurahMeta(73, "Al-Muzzammil", "المزمل", "The Enshrouded One", "Orang yang Berselimut", 20, RevelationType.MAKKIYAH),
        SurahMeta(74, "Al-Muddatstsir", "المدثر", "The Cloaked One", "Orang yang Berkemul", 56, RevelationType.MAKKIYAH),
        SurahMeta(75, "Al-Qiyamah", "القيامة", "The Resurrection", "Hari Kiamat", 40, RevelationType.MAKKIYAH),
        SurahMeta(76, "Al-Insan", "الإنسان", "The Man", "Manusia", 31, RevelationType.MADANIYAH),
        SurahMeta(77, "Al-Mursalat", "المرسلات", "The Emissaries", "Malaikat yang Diutus", 50, RevelationType.MAKKIYAH),
        SurahMeta(78, "An-Naba'", "النبأ", "The Tidings", "Berita Besar", 40, RevelationType.MAKKIYAH),
        SurahMeta(79, "An-Nazi'at", "النازعات", "Those Who Drag Forth", "Malaikat yang Mencabut", 46, RevelationType.MAKKIYAH),
        SurahMeta(80, "'Abasa", "عبس", "He Frowned", "Ia Bermuka Masam", 42, RevelationType.MAKKIYAH),
        SurahMeta(81, "At-Takwir", "التكوير", "The Overthrowing", "Menggulung", 29, RevelationType.MAKKIYAH),
        SurahMeta(82, "Al-Infithar", "الانفطار", "The Cleaving", "Terbelah", 19, RevelationType.MAKKIYAH),
        SurahMeta(83, "Al-Muthaffifin", "المطففين", "The Defrauding", "Orang-Orang Curang", 36, RevelationType.MAKKIYAH),
        SurahMeta(84, "Al-Insyiqaq", "الانشقاق", "The Splitting Open", "Terbelah", 25, RevelationType.MAKKIYAH),
        SurahMeta(85, "Al-Buruj", "البروج", "The Mansions of the Stars", "Gugusan Bintang", 22, RevelationType.MAKKIYAH),
        SurahMeta(86, "Ath-Thariq", "الطارق", "The Morning Star", "Yang Datang di Malam Hari", 17, RevelationType.MAKKIYAH),
        SurahMeta(87, "Al-A'la", "الأعلى", "The Most High", "Yang Paling Tinggi", 19, RevelationType.MAKKIYAH),
        SurahMeta(88, "Al-Ghasyiyah", "الغاشية", "The Overwhelming", "Hari Pembalasan", 26, RevelationType.MAKKIYAH),
        SurahMeta(89, "Al-Fajr", "الفجر", "The Dawn", "Fajar", 30, RevelationType.MAKKIYAH),
        SurahMeta(90, "Al-Balad", "البلد", "The City", "Negeri", 20, RevelationType.MAKKIYAH),
        SurahMeta(91, "Asy-Syams", "الشمس", "The Sun", "Matahari", 15, RevelationType.MAKKIYAH),
        SurahMeta(92, "Al-Lail", "الليل", "The Night", "Malam", 21, RevelationType.MAKKIYAH),
        SurahMeta(93, "Ad-Dhuha", "الضحى", "The Morning Hours", "Waktu Dhuha", 11, RevelationType.MAKKIYAH, "al_fatihah_ad_dhuha.mpeg"),
        SurahMeta(94, "Asy-Syarh", "الشرح", "The Relief", "Kelapangan", 8, RevelationType.MAKKIYAH, "al_fatihah_al_insyiroh.mpeg"),
        SurahMeta(95, "At-Tin", "التين", "The Fig", "Buah Tin", 8, RevelationType.MAKKIYAH),
        SurahMeta(96, "Al-'Alaq", "العلق", "The Clot", "Segumpal Darah", 19, RevelationType.MAKKIYAH),
        SurahMeta(97, "Al-Qadr", "القدر", "The Power", "Kemuliaan (Malam Qadar)", 5, RevelationType.MAKKIYAH),
        SurahMeta(98, "Al-Bayyinah", "البينة", "The Clear Proof", "Bukti Nyata", 8, RevelationType.MADANIYAH),
        SurahMeta(99, "Az-Zalzalah", "الزلزلة", "The Earthquake", "Kegoncangan", 8, RevelationType.MADANIYAH),
        SurahMeta(100, "Al-'Adiyat", "العاديات", "The Courser", "Kuda Perang yang Berlari Kencang", 11, RevelationType.MAKKIYAH),
        SurahMeta(101, "Al-Qari'ah", "القارعة", "The Calamity", "Hari Kiamat", 11, RevelationType.MAKKIYAH),
        SurahMeta(102, "At-Takatsur", "التكاثر", "The Rivalry in World Increase", "Bermegah-Megahan", 8, RevelationType.MAKKIYAH),
        SurahMeta(103, "Al-'Ashr", "العصر", "The Declining Day", "Masa / Waktu", 3, RevelationType.MAKKIYAH),
        SurahMeta(104, "Al-Humazah", "الهمزة", "The Traducer", "Pengumpat", 9, RevelationType.MAKKIYAH),
        SurahMeta(105, "Al-Fil", "الفيل", "The Elephant", "Gajah", 5, RevelationType.MAKKIYAH, "al_fatihah_al_fill.mpeg"),
        SurahMeta(106, "Quraisy", "قريش", "Quraysh", "Suku Quraisy", 4, RevelationType.MAKKIYAH, "al_fatihah_al_quraisy.mpeg"),
        SurahMeta(107, "Al-Ma'un", "الماعون", "The Small Kindnesses", "Barang yang Berguna", 7, RevelationType.MAKKIYAH),
        SurahMeta(108, "Al-Kautsar", "الكوثر", "The Abundance", "Nikmat yang Banyak", 3, RevelationType.MAKKIYAH),
        SurahMeta(109, "Al-Kafirun", "الكافرون", "The Disbelievers", "Orang-Orang Kafir", 6, RevelationType.MAKKIYAH, "al_fatihah_al_kafirun.mpeg"),
        SurahMeta(110, "An-Nasr", "النصر", "The Divine Support", "Pertolongan", 3, RevelationType.MADANIYAH, "al_fatihah_an_nasr.mpeg"),
        SurahMeta(111, "Al-Lahab", "اللهب", "The Palm Fiber", "Gejolak Api", 5, RevelationType.MAKKIYAH),
        SurahMeta(112, "Al-Ikhlas", "الإخلاص", "The Sincerity", "Kemurnian Keesaan Allah", 4, RevelationType.MAKKIYAH),
        SurahMeta(113, "Al-Falaq", "الفلق", "The Daybreak", "Waktu Subuh", 5, RevelationType.MAKKIYAH),
        SurahMeta(114, "An-Nas", "الناس", "Mankind", "Manusia", 6, RevelationType.MAKKIYAH)
    )

    fun getAyahsForSurah(surahNumber: Int): List<Ayah> {
        return when (surahNumber) {
            1 -> listOf(
                Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillaahir-rohmaanir-rohiim", "Dengan nama Allah Yang Maha Pengasih lagi Maha Penyayang.", "Kalayan asma Allah Nu Maha Welas tur Asih."),
                Ayah(2, 1, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "Alhamdulillaahi robbil 'aalamiin", "Segala puji bagi Allah, Tuhan semesta alam.", "Sadaya puji kagungan Allah Nu Murbeng Alam."),
                Ayah(3, 1, "الرَّحْمَٰنِ الرَّحِيمِ", "Ar-rohmaanir-rohiim", "Yang Maha Pengasih, Maha Penyayang.", "Nu Maha Welas tur Maha Asih."),
                Ayah(4, 1, "مَالِكِ يَوْمِ الدِّينِ", "Maaliki yawmid-diin", "Pemilik hari pembalasan.", "Nu Ngawasa dinten balesan."),
                Ayah(5, 1, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyaaka na'budu wa iyyaaka nasta'iin", "Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami memohon pertolongan.", "Mung ka Gusti simkuring ibadah, sareng mung ka Gusti simkuring nyuhunkeun pitulung."),
                Ayah(6, 1, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Ihdinash-shiroothol mustaqiim", "Tunjukilah kami jalan yang lurus,", "Mugia Gusti maparin pituduh jalan anu lempeng,"),
                Ayah(7, 1, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "Shirootholladziina an'amta 'alaihim ghoiril maghdhuubi 'alaihim waladh-dhoolliin", "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya; bukan (jalan) mereka yang dimurkai, dan bukan (pula jalan) mereka yang sesat.", "Jalan jalmi-jalmi anu parantos dipaparin nikmat ku Gusti, sanes jalmi nu kenging bendu sareng sanes jalmi nu sasar.")
            )
            36 -> getSampleYasinAyahs()
            67 -> getSampleMulkAyahs()
            93 -> listOf(
                Ayah(1, 93, "وَالضُّحَىٰ", "Wadh-dhuhaa", "Demi waktu dhuha (ketika matahari naik sepenggalah),", "Demi wanci dhuha,"),
                Ayah(2, 93, "وَاللَّيْلِ إِذَا سَجَىٰ", "Wal-layli idzaa sajaa", "dan demi malam apabila telah sunyi,", "sareng demi peuting nalika geus jempling,"),
                Ayah(3, 93, "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلَىٰ", "Maa wadda'aka robbuka wa maa qolaa", "Tuhanmu tiada meninggalkanmu dan tiada (pula) membencimu,", "Pangeran hidep henteu ninggalkeun hidep sarta henteu mikangewa,"),
                Ayah(4, 93, "وَلَلْآخِرَةُ خَيْرٌ لَّكَ مِنَ الْأُولَىٰ", "Wa lal-aakhirotu khoirul laka minal-uulaa", "dan sesungguhnya akhir itu lebih baik bagimu dari permulaan.", "sarta saestuna akhirat leuwih hade pikeun hidep batan dunya."),
                Ayah(5, 93, "وَلَسَوْفَ يُعْطِيكَ رَبُّكَ فَتَرْضَىٰ", "Wa lasawfa yu'thiika robbuka fatardhaa", "Dan kelak Tuhanmu pasti memberikan karunia-Nya kepadamu, lalu (hati)mu menjadi puas.", "Sarta jaga Pangeran hidep bakal maparin karunia-Na sahingga hidep ngarasa sugema."),
                Ayah(6, 93, "أَلَمْ يَجِدْكَ يَتِيمًا فَآوَىٰ", "Alam yajidka yatiiman fa-aawaa", "Bukankah Dia mendapatimu sebagai seorang yatim, lalu Dia melindungimu?", "Naha Anjeunna henteu mendakan hidep yatim tuluy ngaping?"),
                Ayah(7, 93, "وَوَجَدَكَ ضَالًّا فَهَدَىٰ", "Wa wajadaka dhoollan fahadaa", "Dan Dia mendapatimu sebagai seorang yang bingung, lalu Dia memberikan petunjuk?", "Sarta mendakan hidep bingung tuluy maparin pituduh?"),
                Ayah(8, 93, "وَوَجَدَكَ عَائِلًا فَأَغْنَىٰ", "Wa wajadaka 'aa-ilan fa-aghnaa", "Dan Dia mendapatimu sebagai seorang yang kekurangan, lalu Dia memberikan kecukupan?", "Sarta mendakan hidep kakurangan tuluy nyukupan?"),
                Ayah(9, 93, "فَأَمَّا الْيَتِيمَ فَلَا تَقْهَرْ", "Fa-ammal yatiima falaa taqhar", "Maka terhadap anak yatim janganlah engkau berlaku sewenang-wenang.", "Ku kituna, ka budak yatim hidep ulah sawenang-wenang."),
                Ayah(10, 93, "وَأَمَّا السَّائِلَ فَلَا تَنْهَرْ", "Wa-ammas saa-ila falaa tanhar", "Dan terhadap orang yang meminta-minta janganlah engkau menghardiknya.", "Sarta ka jalma nu menta-menta hidep ulah nundung."),
                Ayah(11, 93, "وَأَمَّا بِنِعْمَةِ رَبِّكَ فَحَدِّثْ", "Wa-ammaa bini'mati robbika fahaddits", "Dan terhadap nikmat Tuhanmu hendaklah engkau nyatakan (dengan bersyukur).", "Sarta perkara nikmat Pangeran hidep, pek ucapkeun kalayan rasa syukur.")
            )
            94 -> listOf(
                Ayah(1, 94, "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "Alam nasyroh laka shodrok", "Bukankah Kami telah melapangkan dadamu (wahai Muhammad)?", "Naha Kami henteu ngalapangkeun dada hidep?"),
                Ayah(2, 94, "وَوَضَعْنَا عَنكَ وِزْرَكَ", "Wa wadho'naa 'anka wizrok", "dan Kami telah menurunkan bebanmu darimu,", "Sarta Kami geus ngaleupaskeun beban hidep,"),
                Ayah(3, 94, "الَّذِي أَنقَضَ ظَهْرَكَ", "Alladzii anqodho dhohrok", "yang memberatkan punggungmu,", "anu ngabeungbeuratan tonggong hidep,"),
                Ayah(4, 94, "وَرَفَعْنَا لَكَ ذِكْرَكَ", "Wa rofa'naa laka dzikrok", "dan Kami tinggikan sebutan (nama)mu bagimu.", "Sarta Kami ngaluhurkeun ngaran hidep."),
                Ayah(5, 94, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا", "Fa-inna ma'al 'usri yusroo", "Maka sesungguhnya bersama kesulitan ada kemudahan,", "Sabab saestuna satukangeun kasulitan aya kagampangan,"),
                Ayah(6, 94, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "Inna ma'al 'usri yusroo", "sesungguhnya bersama kesulitan ada kemudahan.", "Saestuna satukangeun kasulitan aya kagampangan."),
                Ayah(7, 94, "فَإِذَا فَرَغْتَ فَانصَبْ", "Fa-idzaa faroghta fanshob", "Maka apabila engkau telah selesai (dari suatu urusan), tetaplah bekerja keras (untuk urusan yang lain),", "Ku kituna lamun hidep geus rengse tina hiji urusan, pek gawekeun urusan sejenna,"),
                Ayah(8, 94, "وَإِلَىٰ رَبِّكَ فَارْغَب", "Wa ilaa robbika farghob", "dan hanya kepada Tuhanmulah engkau berharap.", "Sarta ngan ka Pangeran hidep bae hidep kudu miharep.")
            )
            108 -> listOf(
                Ayah(1, 108, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "Innaa a'thoynaakal-kautsar", "Sungguh, Kami telah memberimu (Muhammad) nikmat yang banyak.", "Saestuna Kami geus maparin ka hidep nikmat anu kacida lobana."),
                Ayah(2, 108, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "Fasholli lirobbika wanhar", "Maka laksanakanlah sholat karena Tuhanmu, dan berkurbanlah.", "Ku kituna pek sholat karana Pangeran hidep sarta meuncit kurban."),
                Ayah(3, 108, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "Inna syaani-aka huwal-abtar", "Sungguh, orang yang membencimu dialah yang terputus (dari rahmat Allah).", "Saestuna jalma nu ngewa ka hidep nyaeta jalma nu pegat tina rahmat.")
            )
            112 -> listOf(
                Ayah(1, 112, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Qul huwalloohu ahad", "Katakanlah (Muhammad), 'Dialah Allah, Yang Maha Esa.'", "Ucapkeun: Anjeunna nyaeta Allah Nu Maha Tunggal."),
                Ayah(2, 112, "اللَّهُ الصَّمَدُ", "Alloohush-shomad", "Allah tempat meminta segala sesuatu.", "Allah tempat muntang sakabeh makhluk."),
                Ayah(3, 112, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "Lam yalid wa lam yuulad", "(Allah) tidak beranak dan tidak pula diperanakkan,", "Anjeunna henteu kagungan putra sarta henteu dibabarkeun,"),
                Ayah(4, 112, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Wa lam yakul lahuu kufuwan ahad", "dan tidak ada sesuatu yang setara dengan Dia.", "Sarta henteu aya hiji oge anu sarua jeung Anjeunna.")
            )
            113 -> listOf(
                Ayah(1, 113, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Qul a'uudzu birobbil-falaq", "Katakanlah, 'Aku berlindung kepada Tuhan yang menguasai subuh (fajar),'", "Ucapkeun: Kuring nyalindung ka Pangeran nu ngawasa fajar,"),
                Ayah(2, 113, "مِن شَرِّ مَا خَلَقَ", "Min syarri maa kholaq", "dari kejahatan (makhluk yang) Dia ciptakan,", "tina kagorengan naon-naon anu diciptakeun ku Anjeunna,"),
                Ayah(3, 113, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "Wa min syarri ghoosiqin idzaa waqob", "dan dari kejahatan malam apabila telah gelap gulita,", "sareng tina kajahatan peuting nalika poek mongkleng,"),
                Ayah(4, 113, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "Wa min syarrin-naffaatsaati fil-'uqod", "dan dari kejahatan (perempuan-perempuan) penyihir yang meniup pada buhul-buhul (talinya),", "sareng tina kajahatan tukang sihir nu niupkeun kana tali-tali,"),
                Ayah(5, 113, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "Wa min syarri haasidin idzaa hasad", "dan dari kejahatan orang yang dengki apabila dia dengki.'", "sareng tina kajahatan jalma nu dengki nalika anjeunna dengki.")
            )
            114 -> listOf(
                Ayah(1, 114, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Qul a'uudzu birobbin-naas", "Katakanlah, 'Aku berlindung kepada Tuhannya manusia,'", "Ucapkeun: Kuring nyalindung ka Pangeran manusa,"),
                Ayah(2, 114, "مَلِكِ النَّاسِ", "Malikin-naas", "Raja manusia,", "Rajana manusa,"),
                Ayah(3, 114, "إِلَٰهِ النَّاسِ", "Ilaahin-naas", "Sembahan manusia,", "Pangeran sesembahan manusa,"),
                Ayah(4, 114, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "Min syarril-waswaasil-khonnaas", "dari kejahatan (bisikan) setan yang bersembunyi,", "tina kajahatan gogoda syetan anu nyumput,"),
                Ayah(5, 114, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Alladzii yuwaswisu fii shuduurin-naas", "yang membisikkan (kejahatan) ke dalam dada manusia,", "anu ngagoda kana jero dada manusa,"),
                Ayah(6, 114, "مِنَ الْجِنَّةِ وَالنَّاسِ", "Minal-jinnati wan-naas", "dari (golongan) jin dan manusia.", "ti golongan jin sareng manusa.")
            )
            else -> generateGenericAyahs(surahNumber)
        }
    }

    private fun getSampleYasinAyahs(): List<Ayah> = listOf(
        Ayah(1, 36, "يس", "Yaa-Siiin", "Yaa Sin.", "Yaa Sin."),
        Ayah(2, 36, "وَالْقُرْآنِ الْحَكِيمِ", "Wal-Qur-aanil-hakiim", "Demi Al-Qur'an yang penuh hikmah,", "Demi Al-Qur'an anu pinuh ku hikmah,"),
        Ayah(3, 36, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "Innaka laminal-mursaliin", "sungguh, engkau (Muhammad) adalah salah seorang dari rasul-rasul,", "saestuna hidep teh salah saurang ti para Rasul,"),
        Ayah(4, 36, "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ", "'Alaa shiroothim mustaqiim", "(yang berada) di atas jalan yang lurus,", "dina jalan anu lempeng,"),
        Ayah(5, 36, "تَنزِيلَ الْعَزِيزِ الرَّحِيمِ", "Tanziilal-'aziizir-rohiim", "(sebagai wahyu) yang diturunkan oleh (Allah) Yang Maha Perkasa, Maha Penyayang,", "diturunkeun ku Nu Maha Gagah tur Maha Asih,")
    )

    private fun getSampleMulkAyahs(): List<Ayah> = listOf(
        Ayah(1, 67, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Tabaarokalladzii biyadihil-mulku wa huwa 'alaa kulli syay-in qodiir", "Maha Suci Allah yang di tangan-Nya lah segala kerajaan, dan Dia Maha Kuasa atas segala sesuatu.", "Maha Suci Allah anu dina kakawasaan-Na sagala karajaan, sarta Anjeunna Maha Kawasa kana sagala rupa."),
        Ayah(2, 67, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا", "Alladzii kholaqol-mawta wal-hayaata liyabluwakum ayyukum ahsanu 'amalaa", "Yang menciptakan mati dan hidup, untuk menguji kamu, siapa di antara kamu yang lebih baik amalnya.", "Anu nyiptakeun pati jeung hirup pikeun nguji aranjeun saha nu panghadena amalna.")
    )

    private fun generateGenericAyahs(surahNumber: Int): List<Ayah> {
        val meta = surahs.find { it.number == surahNumber } ?: return emptyList()
        val count = minOf(meta.numberOfAyahs, 10)
        return (1..count).map { ayahNum ->
            Ayah(
                numberInSurah = ayahNum,
                surahNumber = surahNumber,
                textArabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • إِنَّ هَٰذَا الْقُرْآنَ يَهْدِي لِلَّتِي هِيَ أَقْوَمُ ($ayahNum)",
                transliterationLatin = "Ayat ke-$ayahNum dari Surah ${meta.nameLatin}",
                translationIndonesian = "Ayat ke-$ayahNum: Firman Allah SWT dalam Surah ${meta.nameLatin} (${meta.indonesianMeaning}) yang menjadi petunjuk bagi orang-orang yang bertaqwa.",
                translationSundanese = "Ayat ka-$ayahNum: Dawuhan Gusti Allah SWT dina Surat ${meta.nameLatin} anu janten pituduh pikeun jalma nu taqwa."
            )
        }
    }
}
