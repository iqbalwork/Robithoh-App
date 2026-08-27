package com.iqbalwork.robithoh.feature.reader.data

import com.iqbalwork.robithoh.feature.reader.model.LiturgyDocument
import com.iqbalwork.robithoh.feature.reader.model.LiturgyVerse
import com.iqbalwork.robithoh.feature.reader.model.ParsedDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import robithohapp.shared.generated.resources.Res

class MarkdownDocumentRepository {

    val allDocuments: List<LiturgyDocument> = listOf(
        // Dzikir & Khotaman
        LiturgyDocument(
            id = "dzikir_tqn",
            title = "Dzikir Harian",
            subtitle = "",
            category = "Dzikir & Khotaman",
            fileName = "DZIKIR_TQN.md",
            arabicTitle = "الذِّكْرُ بَعْدَ الصَّلَاةِ",
            iconName = "dzikir"
        ),
        LiturgyDocument(
            id = "khotaman_tqn",
            title = "Khotaman TQN Sirnarasa",
            subtitle = "Amaliyah Khotaman",
            category = "Dzikir & Khotaman",
            fileName = "KHOTAMAN_TQN.md",
            arabicTitle = "",
            iconName = "khotaman"
        ),
        LiturgyDocument(
            id = "tarhim_tqn",
            title = "Tarhim TQN",
            subtitle = "Bacaan tarhim menjelang adzan shubuh & sholat",
            category = "Dzikir & Khotaman",
            fileName = "TARHIM_TQN.md",
            arabicTitle = "التَّرْحِيمُ",
            iconName = "tarhim"
        ),
        LiturgyDocument(
            id = "silsilah_tqn",
            title = "Silsilah 38 Guru Mursyid",
            subtitle = "Mata rantai emas kemursyidan TQN dari Rasulullah SAW",
            category = "Dzikir & Khotaman",
            fileName = "SILSILAH_TQN.md",
            arabicTitle = "السِّلْسِلَةُ الشَّرِيفَةُ",
            iconName = "silsilah"
        ),
        LiturgyDocument(
            id = "tahlil_tqn",
            title = "Tahlil TQN",
            subtitle = "Panduan bacaan tahlil & hadhloroh arwah",
            category = "Dzikir & Khotaman",
            fileName = "TAHLIL_TQN.md",
            arabicTitle = "التَّهْلِيلُ",
            iconName = "tahlil"
        ),

        // Manaqib Indonesia
        LiturgyDocument(
            id = "mc_manaqib_id",
            title = "MC Manaqib",
            subtitle = "Susunan acara & protokol MC Manaqib TQN",
            category = "Manaqib",
            fileName = "MC_MANAQIB_INDONESIA.md",
            arabicTitle = "",
            languageBadge = "INDONESIA",
            iconName = "mc",
            isSingleDocumentView = true,
            alternateLanguageDocId = "mc_manaqib_su"
        ),
        LiturgyDocument(
            id = "tanbih_id",
            title = "Tanbih Guru Mursyid",
            subtitle = "Wasiat luhur Syaikh Abdullah Mubarok bin Nur Muhammad",
            category = "Manaqib",
            fileName = "TANBIH_INDONESIA.md",
            arabicTitle = "",
            languageBadge = "INDONESIA",
            iconName = "tanbih",
            isSingleDocumentView = true,
            alternateLanguageDocId = "tanbih_su"
        ),
        LiturgyDocument(
            id = "tawassul_tqn",
            title = "Tawassul TQN Sirnarasa",
            subtitle = "Hadhloroh tawasul auliya & masyayikh",
            category = "Manaqib",
            fileName = "TAWASSUL_TQN.md",
            arabicTitle = "التَّوَسُّلُ",
            iconName = "tawassul"
        ),
        LiturgyDocument(
            id = "manqobah_id",
            title = "Manqobah",
            subtitle = "Kisah kemuliaan Syaikh Abdul Qodir Al-Jailani r.a.",
            category = "Manaqib",
            fileName = "MANQOBAH_INDONESIA.md",
            arabicTitle = "",
            languageBadge = "INDONESIA",
            iconName = "manqobah",
            isSingleDocumentView = true,
            alternateLanguageDocId = "manqobah_su"
        ),
        LiturgyDocument(
            id = "doa_manqobah_id",
            title = "Doa Manqobah (Indonesia)",
            subtitle = "Doa penutup manaqib",
            category = "Manaqib",
            fileName = "DOA_MANQOBAH_INDONESIA.md",
            arabicTitle = "دُعَاءُ الْمَنَاقِبِ",
            iconName = "doa",
            alternateLanguageDocId = "doa_manqobah_su"
        ),

        // Manaqib Sunda
        LiturgyDocument(
            id = "mc_manaqib_su",
            title = "MC Manaqib (Basa Sunda)",
            subtitle = "Runtuyan acara & protokol MC Manaqib TQN",
            category = "Manaqib Sunda",
            fileName = "MC_MANAQIB_SUNDA.md",
            arabicTitle = "",
            languageBadge = "SUNDA",
            iconName = "mc_sunda",
            isSingleDocumentView = true,
            alternateLanguageDocId = "mc_manaqib_id"
        ),
        LiturgyDocument(
            id = "tanbih_su",
            title = "Tanbih (Basa Sunda)",
            subtitle = "Wasiat Pangersa Abah Sepuh dina basa Sunda",
            category = "Manaqib Sunda",
            fileName = "TANBIH_SUNDA.md",
            arabicTitle = "",
            languageBadge = "SUNDA",
            iconName = "tanbih_sunda",
            isSingleDocumentView = true,
            alternateLanguageDocId = "tanbih_id"
        ),
        LiturgyDocument(
            id = "manqobah_su",
            title = "Manqobah (Basa Sunda)",
            subtitle = "Riwayat karomah Syaikh Abdul Qodir Al-Jailani dina basa Sunda",
            category = "Manaqib Sunda",
            fileName = "MANQOBAH_SUNDA.md",
            arabicTitle = "",
            languageBadge = "SUNDA",
            iconName = "manqobah_sunda",
            isSingleDocumentView = true,
            alternateLanguageDocId = "manqobah_id"
        ),
        LiturgyDocument(
            id = "doa_manqobah_su",
            title = "Doa Manqobah (Basa Sunda)",
            subtitle = "Mugi berkah karomah Guru Mursyid",
            category = "Manaqib Sunda",
            fileName = "DOA_MANQOBAH_SUNDA.md",
            arabicTitle = "دُعَاءُ الْمَنَاقِبِ",
            languageBadge = "SUNDA",
            iconName = "doa",
            alternateLanguageDocId = "doa_manqobah_id"
        ),

        // Sholat Sunnah
        LiturgyDocument(
            id = "sholat_harian",
            title = "Sholat Harian",
            subtitle = "Amaliyah sholat fardhu & sunnah Guru Mursyid",
            category = "Sholat",
            fileName = "SHOLAT_HARIAN.md",
            arabicTitle = "",
            iconName = "harian",
            isSingleDocumentView = true
        ),
        LiturgyDocument(
            id = "sholat_tarowih",
            title = "Sholat Tarowih & Witir",
            subtitle = "Kaifiyat sholat tarowih & witir TQN",
            category = "Sholat",
            fileName = "SHOLAT_TAROWIH.md",
            arabicTitle = "صَلَاةُ التَّرَاوِيحِ",
            iconName = "tarowih"
        ),
        LiturgyDocument(
            id = "sholat_rojab",
            title = "Sholat Sunnah Bulan Rojab",
            subtitle = "Amaliyah malam 1, 15 & akhir Rojab",
            category = "Sholat",
            fileName = "SHOLAT_ROJAB.md",
            arabicTitle = "صَلَاةُ رَجَبٍ",
            iconName = "rojab"
        ),
        LiturgyDocument(
            id = "sholat_nisfu_syaban",
            title = "Sholat Nisfu Sya'ban",
            subtitle = "Tata cara sholat malam nisfu Sya'ban & doanya",
            category = "Sholat",
            fileName = "SHOLAT_NISFU_SYABAN.md",
            arabicTitle = "صَلَاةُ نِصْفِ شَعْبَانَ",
            iconName = "syaban"
        ),
        LiturgyDocument(
            id = "sholat_lailatul_qodar",
            title = "Sholat Lailatul Qodar",
            subtitle = "Sholat malam kemuliaan di bulan Romadhon",
            category = "Sholat",
            fileName = "SHOLAT_LAILATUL_QODAR.md",
            arabicTitle = "صَلَاةُ لَيْلَةِ الْقَدْرِ",
            iconName = "romadhon"
        ),
        LiturgyDocument(
            id = "sholat_lidafil_bala",
            title = "Sholat Lidaf'il Bala (Rebo Wekasan)",
            subtitle = "Sholat tolak bala hari Rabu terakhir bulan Shofar",
            category = "Sholat",
            fileName = "SHOLAT_LIDAFIL_BALA.md",
            arabicTitle = "صَلَاةُ لِدَفْعِ الْبَلَاءِ",
            iconName = "shofar_icon"
        ),

        // Sholawat
        LiturgyDocument(
            id = "sholawat_thoriqiyyah",
            title = "Sholawat Thoriqiyyah",
            subtitle = "Sholawat pembuka ruhani TQN",
            category = "Sholawat",
            fileName = "SHOLAWAT_THORIQIYYAH.md",
            arabicTitle = "الصَّلَاةُ الطَّرِيقِيَّةُ",
            iconName = "thoriqoh"
        ),
        LiturgyDocument(
            id = "sholawat_bani_hasyim",
            title = "Sholawat Bani Hasyim",
            subtitle = "Sholawat ketenangan & benteng qolbu",
            category = "Sholawat",
            fileName = "SHOLAWAT_BANI_HASYIM.md",
            arabicTitle = "صَلَوَاتُ بَنِي هَاشِمٍ",
            iconName = "bani",
            isSingleDocumentView = true
        ),
        LiturgyDocument(
            id = "amjad",
            title = "Sholawat Amjad",
            subtitle = "Sholawat keagungan & kemuliaan",
            category = "Sholawat",
            fileName = "AMJAD.md",
            arabicTitle = "صَلَاةُ أَمْجَدْ",
            iconName = "amjad"
        ),
        LiturgyDocument(
            id = "iqomah_subuh",
            title = "Iqomah Shubuh & Tarhim",
            subtitle = "Lafadz iqomah & pujian shubuh",
            category = "Sholawat",
            fileName = "IQOMAH_SUBUH.md",
            arabicTitle = "إِقَامَةُ الصُّبْحِ",
            iconName = "subuh"
        ),
        LiturgyDocument(
            id = "sholawat_badriyyah",
            title = "Sholawat Badriyyah",
            subtitle = "Tawassul ahli Badar",
            category = "Sholawat",
            fileName = "SHOLAWAT_BADRIYYAH.md",
            arabicTitle = "صَلَاةُ الْبَدْرِيَّةِ",
            iconName = "badriyah",
            isSingleDocumentView = true
        ),
        LiturgyDocument(
            id = "sholawat_jiyaaroh",
            title = "Sholawat Jiyaaroh ke Rosululloh",
            subtitle = "Sholawat salam ziarah Madinah Al-Munawwarah",
            category = "Sholawat",
            fileName = "SHOLAWAT_JIYAAROH_KE_ROSULULLOH.md",
            arabicTitle = "صَلَوَاتُ الزِّيَارَةِ",
            iconName = "sholawat"
        ),

        // Doa & Ziarah
        LiturgyDocument(
            id = "doa_istighotsah",
            title = "Doa Istighotsah",
            subtitle = "Memohon pertolongan agung kepada Allah SWT",
            category = "Doa & Ziarah",
            fileName = "DOA_ISTIGHOTSAH.md",
            arabicTitle = "دُعَاءُ الإِسْتِغَاثَةِ",
            iconName = "doa"
        ),
        LiturgyDocument(
            id = "doa_rijalul_ghoib",
            title = "Doa Rijalul Ghoib",
            subtitle = "Salam & doa kepada para hamba pilihan Allah",
            category = "Doa & Ziarah",
            fileName = "DOA_RIJALUL_GHOIB.md",
            arabicTitle = "دُعَاءُ رِجَالِ الْغَيْبِ",
            iconName = "doa"
        ),
        LiturgyDocument(
            id = "sebelum_tidur",
            title = "Amaliyah Sebelum Tidur",
            subtitle = "Wirid, ayat pelindung & dzikir khofi tidur",
            category = "Doa & Ziarah",
            fileName = "SEBELUM_TIDUR.md",
            arabicTitle = "أَدْعِيَةُ النَّوْمِ",
            iconName = "doa"
        ),
        LiturgyDocument(
            id = "salam_wali_mursyid",
            title = "Salam Kepada Wali Mursyid",
            subtitle = "Adab salam & rabithah ruhaniyah",
            category = "Doa & Ziarah",
            fileName = "SALAM_KEPADA_WALI_MURSYID.md",
            arabicTitle = "السَّلَامُ عَلَى الْمُرْشِدِ",
            iconName = "doa"
        ),
        LiturgyDocument(
            id = "dziarah_waliyulloh",
            title = "Dziarah Waliyulloh",
            subtitle = "Panduan & tata cara ziarah maqom para wali",
            category = "Doa & Ziarah",
            fileName = "DZIARAH_WALIYULLOH.md",
            arabicTitle = "زِيَارَةُ أَوْلِيَاءِ اللَّهِ",
            iconName = "dziarah_waliyulloh"
        ),
        LiturgyDocument(
            id = "dziarah_umum",
            title = "Dziarah Kubur Umum",
            subtitle = "Doa & adab ziarah kubur muslimin",
            category = "Doa & Ziarah",
            fileName = "DZIARAH_UMUM.md",
            arabicTitle = "زِيَارَةُ الْقُبُورِ الْعَامَّةِ",
            iconName = "dziarah_umum"
        ),
        LiturgyDocument(
            id = "dziarah_kubur",
            title = "Panduan Ziarah Lengkap",
            subtitle = "Adab, bacaan & doa ziarah makam",
            category = "Doa & Ziarah",
            fileName = "DZIARAH_KUBUR.md",
            arabicTitle = "كِتَابُ الزِّيَارَةِ",
            iconName = "dziarah_umum"
        ),

        // Amaliyah 12 Bulan Hijriyah
        LiturgyDocument(id = "amaliyah_muharrom", title = "Amaliyah Bulan Muharrom", subtitle = "Awal tahun baru Islam & Asyura 10 Muharrom", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_MUHARROM.md", arabicTitle = "عَمَلِيَّةُ الْمُحَرَّمِ", iconName = "muharrom"),
        LiturgyDocument(id = "amaliyah_shofar", title = "Amaliyah Bulan Shofar", subtitle = "Amaliyah & doa bulan Shofar", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_SHOFAR.md", arabicTitle = "عَمَلِيَّةُ صَفَرٍ", iconName = "shofar"),
        LiturgyDocument(id = "amaliyah_robiulawal", title = "Amaliyah Bulan Robi'ul Awal", subtitle = "Bulan Maulid Nabi Muhammad SAW", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_ROBIULAWAL.md", arabicTitle = "عَمَلِيَّةُ رَبِيعِ الْأَوَّلِ", iconName = "robiulawal"),
        LiturgyDocument(id = "amaliyah_robiutstsani", title = "Amaliyah Bulan Robi'uts Tsani", subtitle = "Bulan Haul Agung Syaikh Abdul Qodir Al-Jailani", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_ROBIUTSTSANI.md", arabicTitle = "عَمَلِيَّةُ رَبِيعِ الثَّانِي", iconName = "robiutstsani"),
        LiturgyDocument(id = "amaliyah_jumadilula", title = "Amaliyah Bulan Jumadil Ula", subtitle = "Wirid & sholat sunnah Jumadil Ula", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_JUMADILULA.md", arabicTitle = "عَمَلِيَّةُ جُمَادَى الْأُولَى", iconName = "jumadilula"),
        LiturgyDocument(id = "amaliyah_jumaditsaniyah", title = "Amaliyah Bulan Jumadits Tsaniyah", subtitle = "Wirid & amaliyah Jumadits Tsaniyah", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_JUMADITSTSANIYAH.md", arabicTitle = "عَمَلِيَّةُ جُمَادَى الثَّانِيَةِ", iconName = "jumaditstsaniyah"),
        LiturgyDocument(id = "amaliyah_rojab", title = "Amaliyah Bulan Rojab", subtitle = "Bulan Isra Mi'raj & puasa sunnah", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_ROJAB.md", arabicTitle = "عَمَلِيَّةُ رَجَبٍ", iconName = "rojab"),
        LiturgyDocument(id = "amaliyah_syaban", title = "Amaliyah Bulan Sya'ban", subtitle = "Bulan Ruwah & persiapan Romadhon", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_SYABAN.md", arabicTitle = "عَمَلِيَّةُ شَعْبَانَ", iconName = "syaban"),
        LiturgyDocument(id = "amaliyah_romadhon", title = "Amaliyah Bulan Romadhon", subtitle = "Bulan Puasa, Tarowih, Tadarus & Lailatul Qodar", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_ROMADHON.md", arabicTitle = "عَمَلِيَّةُ رَمَضَانَ", iconName = "romadhon"),
        LiturgyDocument(id = "amaliyah_syawal", title = "Amaliyah Bulan Syawal", subtitle = "Idul Fitri & puasa sunnah 6 hari Syawal", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_SYAWAL.md", arabicTitle = "عَمَلِيَّةُ شَوَّالٍ", iconName = "syawal"),
        LiturgyDocument(id = "amaliyah_dzulqodah", title = "Amaliyah Bulan Dzulqo'dah", subtitle = "Bulan Hapit & persiapan haji", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_DZULQODAH.md", arabicTitle = "عَمَلِيَّةُ ذِي الْقَعْدَةِ", iconName = "dzulqodah"),
        LiturgyDocument(id = "amaliyah_dzulhijjah", title = "Amaliyah Bulan Dzulhijjah", subtitle = "Bulan Haji, Idul Adha & Hari Tasyriq", category = "12 Bulan Hijriyah", fileName = "AMALIYAH_DZULHIJJAH.md", arabicTitle = "عَمَلِيَّةُ ذِي الْحِجَّةِ", iconName = "dzulhijjah")
    )

    private val documentCache = mutableMapOf<String, ParsedDocument>()

    fun getCachedDocument(id: String): ParsedDocument? {
        return documentCache[id]
    }

    fun getDocumentById(id: String): LiturgyDocument? {
        return allDocuments.find { it.id.equals(id, ignoreCase = true) }
    }

    fun getDocumentsByCategory(category: String): List<LiturgyDocument> {
        return allDocuments.filter { it.category.equals(category, ignoreCase = true) }
    }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun loadDocumentContent(document: LiturgyDocument): ParsedDocument = withContext(Dispatchers.Default) {
        documentCache[document.id]?.let { return@withContext it }

        val rawText = try {
            val bytes = Res.readBytes("files/${document.fileName}")
            bytes.decodeToString()
        } catch (e: Exception) {
            "# ${document.title}\n\n${document.subtitle}\n\n*Konten dokumen sedang dimuat dari arsip lokal.*"
        }
        val verses = if (document.isSingleDocumentView) {
            emptyList()
        } else {
            parseMarkdownToVerses(rawText)
        }
        val parsed = ParsedDocument(info = document, rawContent = rawText, verses = verses)
        documentCache[document.id] = parsed

        // Asynchronously preload alternate language document if available
        document.alternateLanguageDocId?.let { altId ->
            if (!documentCache.containsKey(altId)) {
                getDocumentById(altId)?.let { altDoc ->
                    try {
                        val altBytes = Res.readBytes("files/${altDoc.fileName}")
                        val altText = altBytes.decodeToString()
                        val altVerses = if (altDoc.isSingleDocumentView) emptyList() else parseMarkdownToVerses(altText)
                        documentCache[altId] = ParsedDocument(info = altDoc, rawContent = altText, verses = altVerses)
                    } catch (_: Exception) {}
                }
            }
        }

        parsed
    }

    private fun parseMarkdownToVerses(rawMarkdown: String): List<LiturgyVerse> {
        val verses = mutableListOf<LiturgyVerse>()
        val blocks = rawMarkdown.replace("\r\n", "\n").split(Regex("\n\n+"))

        var currentTitle = ""
        // Whether currentTitle came from the document's own "# Title" H1
        // (already shown in the header card) rather than an in-body section
        // label — an H1-only title should never become its own card.
        var titleFromDocHeading = false
        var currentArabic = StringBuilder()
        var currentLatin = StringBuilder()
        var currentTranslation = StringBuilder()
        var currentNote = StringBuilder()
        var currentRepeatCount = 1
        var index = 1

        fun extractRepeatCount(text: String): Int {
            val match = Regex("""\((?:x|\s*)*([0-9]+|[٠-٩]+)(?:x|\s*)*\)""", RegexOption.IGNORE_CASE).find(text)
                ?: Regex("""\b([0-9]+)x\b""", RegexOption.IGNORE_CASE).find(text)
            if (match != null) {
                val numStr = match.groupValues[1]
                val westernNum = numStr.map { c ->
                    when (c) {
                        '٠' -> '0'; '١' -> '1'; '٢' -> '2'; '٣' -> '3'; '٤' -> '4'
                        '٥' -> '5'; '٦' -> '6'; '٧' -> '7'; '٨' -> '8'; '٩' -> '9'
                        else -> c
                    }
                }.joinToString("")
                return westernNum.toIntOrNull() ?: 1
            }
            return 1
        }

        fun flushVerse(allowTitleOnly: Boolean = false) {
            val ar = currentArabic.toString().trim()
            val lt = currentLatin.toString().trim()
            val tr = currentTranslation.toString().trim()
            val nt = currentNote.toString().trim()

            val cleanTitle = if (currentTitle.equals("KHOTAMAN", ignoreCase = true) ||
                currentTitle.equals("DZIKIR", ignoreCase = true) ||
                currentTitle.equals("Dzikir Harian", ignoreCase = true) ||
                currentTitle.equals("Khotaman TQN", ignoreCase = true)
            ) "" else currentTitle

            val hasContent = ar.isNotEmpty() || lt.isNotEmpty() || tr.isNotEmpty() || nt.isNotEmpty()
            val hasTitleOnly = allowTitleOnly && !titleFromDocHeading && cleanTitle.isNotEmpty()

            if (hasContent || hasTitleOnly) {
                verses.add(
                    LiturgyVerse(
                        index = index++,
                        title = cleanTitle,
                        arabic = ar,
                        latin = lt,
                        translation = tr,
                        note = nt,
                        repeatCount = currentRepeatCount
                    )
                )
                currentArabic.clear()
                currentLatin.clear()
                currentTranslation.clear()
                currentNote.clear()
                currentRepeatCount = 1
                currentTitle = ""
                titleFromDocHeading = false
            }
        }

        fun isTransliterationLike(text: String): Boolean {
            val lower = text.lowercase()
            val translitKeywords = listOf(
                "bismillaah", "alhamdu", "allooh", "alloh", "laa ", "ilaaha", "illallooh", "sholli", "shollallohu",
                "sayyidina", "sayyidinaa", "musthofa", "musthofaa", "hadrotin", "astaghfirulloh", "astaghfirullooh",
                "ilaahii", "anta ", "subhaanallooh", "wa ‘alaa", "wa 'alaa", "wa aalihii", "ashhabihii",
                "dzurriyyaat", "birohmatika", "yaa arhamar", "innallooha", "yusholluu", "tsumma ilaa",
                "khushuushon", "qoddasalloohu", "qaddasalloohu", "radiyalloohu", "rodlialloohu", "karromalloohu",
                "robbanaa", "robbii", "shobran", "yassir", "angzilnaa", "afrigh", "a'udzu", "al-fatihah",
                "al faatihah", "alfatihah", "laṭīfum", "yarzuqu", "bainakum", "shohhan", "saddaw", "kaf-hha",
                "mungzalam", "bainana"
            )
            return translitKeywords.any { lower.contains(it) } || text.contains("‘") || text.contains("’") || (lower.contains("aa") && lower.contains("ii")) || (lower.contains("uu") && lower.contains("ii"))
        }

        var inTawajuhSection = false

        for (rawBlock in blocks) {
            val trimmed = rawBlock.trim()
            if (trimmed.isEmpty() || trimmed == "---" || trimmed == "***" || trimmed == "۞۞۞") {
                continue
            }

            // Headings
            if (trimmed.startsWith("#")) {
                flushVerse()
                val heading = trimmed.trimStart('#').trim()
                currentTitle = if (heading.equals("DZIKIR", ignoreCase = true) ||
                    heading.equals("Dzikir Harian", ignoreCase = true) ||
                    heading.equals("KHOTAMAN", ignoreCase = true) ||
                    heading.equals("Khotaman", ignoreCase = true) ||
                    heading.equals("Khotaman TQN", ignoreCase = true)
                ) "" else heading
                titleFromDocHeading = true
                inTawajuhSection = false
                continue
            }

            // Arabic text detection
            val arabicCharCount = trimmed.count { c -> c in '\u0600'..'\u06FF' || c in '\u0750'..'\u077F' || c in '\u08A0'..'\u08FF' }
            val isArabic = arabicCharCount >= 3 && (arabicCharCount.toFloat() / trimmed.length.toFloat()) > 0.20

            val isParentheticalTitle = trimmed.startsWith("(") && trimmed.endsWith(")") && trimmed.length < 150
            val isAllUpperSection = trimmed.all { it.isUpperCase() || it.isWhitespace() || it == '-' || it == '(' || it == ')' } && trimmed.length > 4 && !isTransliterationLike(trimmed)
            // "Hadoroh kesatu/kedua/..." (Tahlil) — a short heading line that
            // introduces the block that follows it, not a translation of the
            // block before it.
            val isHadorohTitle = Regex("""^Hadoroh\s+ke\S+$""", RegexOption.IGNORE_CASE).matches(trimmed)

            if (isAllUpperSection || isParentheticalTitle || isHadorohTitle) {
                flushVerse(allowTitleOnly = true)
                val rawTitle = trimmed.removeSurrounding("(").removeSurrounding(")").trim()
                currentTitle = if (rawTitle.equals("KHOTAMAN", ignoreCase = true) ||
                    rawTitle.equals("DZIKIR", ignoreCase = true)
                ) "" else rawTitle
                titleFromDocHeading = false
                inTawajuhSection = false
                val count = extractRepeatCount(trimmed)
                if (count > 1) {
                    currentRepeatCount = count
                }
                continue
            }

            // Detect if entering Tawajuh & Asy-Syura block
            if (trimmed.contains("Tawajuh", ignoreCase = true) || trimmed.contains("Kedua mata terpejam", ignoreCase = true)) {
                inTawajuhSection = true
            }

            if (isArabic) {
                if (!inTawajuhSection && (currentLatin.isNotEmpty() || currentTranslation.isNotEmpty())) {
                    flushVerse()
                }

                val count = extractRepeatCount(trimmed)
                if (count > currentRepeatCount) {
                    currentRepeatCount = count
                }

                if (currentArabic.isNotEmpty()) {
                    currentArabic.append("\n\n")
                }
                currentArabic.append(trimmed)
            } else {
                val isExplicitTranslation = trimmed.startsWith("Artinya:", ignoreCase = true) ||
                                           trimmed.startsWith("Aku memohon", ignoreCase = true) ||
                                           trimmed.startsWith("Yaa Alloh semoga", ignoreCase = true) ||
                                           trimmed.startsWith("Yaa Alloh limpahkanlah", ignoreCase = true) ||
                                           trimmed.startsWith("Tuhanku Engkaulah", ignoreCase = true) ||
                                           trimmed.startsWith("Tiada Tuhan", ignoreCase = true) ||
                                           trimmed.startsWith("Dengan menyebut", ignoreCase = true) ||
                                           trimmed.startsWith("Dengan nama", ignoreCase = true) ||
                                           trimmed.startsWith("Semoga", ignoreCase = true) ||
                                           trimmed.startsWith("Ya Tuhanku", ignoreCase = true) ||
                                           trimmed.startsWith("Wahai", ignoreCase = true) ||
                                           trimmed.startsWith("Ya Alloh", ignoreCase = true)

                val isInstruction = trimmed.startsWith("Kemudian", ignoreCase = true) ||
                                    trimmed.startsWith("Adapun", ignoreCase = true) ||
                                    trimmed.startsWith("Selanjutnya", ignoreCase = true) ||
                                    trimmed.startsWith("Catatan:", ignoreCase = true) ||
                                    trimmed.startsWith("Faedah:", ignoreCase = true) ||
                                    trimmed.startsWith("Keutamaan:", ignoreCase = true) ||
                                    trimmed.startsWith("Do’a dilaksanakan", ignoreCase = true) ||
                                    trimmed.startsWith("Doa dilaksanakan", ignoreCase = true) ||
                                    trimmed.startsWith("–", ignoreCase = true) ||
                                    trimmed.startsWith("- ", ignoreCase = true) ||
                                    trimmed.startsWith("Dilanjutkan", ignoreCase = true) ||
                                    trimmed.startsWith("Asy Syura", ignoreCase = true) ||
                                    trimmed.startsWith("sebanyak", ignoreCase = true) ||
                                    inTawajuhSection

                if (isTransliterationLike(trimmed) && !isExplicitTranslation) {
                    val count = extractRepeatCount(trimmed)
                    if (count > currentRepeatCount) {
                        currentRepeatCount = count
                    }
                    if (currentLatin.isNotEmpty()) currentLatin.append("\n\n")
                    currentLatin.append(trimmed.removeSurrounding("*").removeSurrounding("\""))
                } else if (isInstruction) {
                    if (currentNote.isNotEmpty()) currentNote.append("\n\n")
                    currentNote.append(trimmed)
                } else {
                    if (currentTranslation.isNotEmpty()) currentTranslation.append("\n\n")
                    currentTranslation.append(trimmed)
                }
            }
        }
        flushVerse()

        if (verses.isEmpty() && rawMarkdown.isNotBlank()) {
            verses.add(
                LiturgyVerse(
                    index = 1,
                    title = "Isi Dokumen",
                    translation = rawMarkdown
                )
            )
        }

        return verses
    }
}
