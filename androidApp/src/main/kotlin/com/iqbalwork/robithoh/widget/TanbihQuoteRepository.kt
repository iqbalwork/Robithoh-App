package com.iqbalwork.robithoh.widget

import android.content.Context
import java.util.Calendar

data class TanbihQuote(
    val id: Int,
    val category: String,
    val quote: String,
    val author: String,
    val reference: String
)

object TanbihQuoteRepository {

    private const val PREFS_NAME = "robithoh_tanbih_widget_prefs"
    private const val KEY_QUOTE_INDEX = "current_quote_index"
    private const val KEY_LAST_DAY_OF_YEAR = "last_day_of_year"

    val quotes: List<TanbihQuote> = listOf(
        TanbihQuote(
            id = 1,
            category = "Adab Luhur",
            quote = "Ka saluhureun urang, boh lahirna boh batinna kedah tumut sarta ngajenan, ka sasama hirup sarta ka sahandapeun urang kedah mikawelas asih.",
            author = "Pangersa Guru Agung Syekh Abdullah Mubarok r.a.",
            reference = "Wasiat Tanbih • Butir Ke-1"
        ),
        TanbihQuote(
            id = 2,
            category = "Ukhuwah Islamiyah",
            quote = "Terhadap sesama ahli agama, harus saling berkasih-sayangan, jangan ada rasa dengki dan iri hati, laksana satu tubuh yang saling menguatkan.",
            author = "Pangersa Guru Agung Syekh Abdullah Mubarok r.a.",
            reference = "Wasiat Tanbih • Butir Ke-2"
        ),
        TanbihQuote(
            id = 3,
            category = "Kerukunan Hidup",
            quote = "Terhadap orang yang berbeda keyakinan, harus senantiasa hidup rukun, damai dan saling menghargai, sebab sekalian manusia itu sama-sama ciptaan Allah Ta'ala.",
            author = "Pangersa Guru Agung Syekh Abdullah Mubarok r.a.",
            reference = "Wasiat Tanbih • Butir Ke-3"
        ),
        TanbihQuote(
            id = 4,
            category = "Bela Negara",
            quote = "Terhadap Pemerintah Republik Indonesia yang sah, harus senantiasa patuh dan taat pada aturan hukum yang berlaku demi kejayaan Nusa, Bangsa, dan Agama.",
            author = "Pangersa Guru Agung Syekh Abdullah Mubarok r.a.",
            reference = "Wasiat Tanbih • Butir Ke-4"
        ),
        TanbihQuote(
            id = 5,
            category = "Kepedulian Sosial",
            quote = "Terhadap fakir miskin dan anak yatim, harus senantiasa menyayangi serta memberi pertolongan semampunya dengan hati yang ikhlas, jangan menelantarkannya.",
            author = "Pangersa Guru Agung Syekh Abdullah Mubarok r.a.",
            reference = "Wasiat Tanbih • Butir Ke-5"
        ),
        TanbihQuote(
            id = 6,
            category = "Dzikirullah",
            quote = "Mayengkeun dzikirullah siang dan malam, agar kalbu senantiasa bercahaya dengan nur Ilahi serta dijauhkan dari segala kegelapan nafsu.",
            author = "Pangersa Abah Anom r.a.",
            reference = "Intisari Talqin Dzikir TQN"
        ),
        TanbihQuote(
            id = 7,
            category = "Hikmah Manqobah",
            quote = "Aku meneliti seluruh amal kebajikan, maka tidak kutemukan amal yang lebih mulia daripada memberi makan orang yang lapar dan melapangkan kesusahan sesama.",
            author = "Sulthanul Auliya Syekh Abdul Qodir Al-Jailani q.s.",
            reference = "Kitab Manqobah Syekh Abdul Qodir"
        ),
        TanbihQuote(
            id = 8,
            category = "Tawadhu & Ikhlas",
            quote = "Budi pekerti yang luhur adalah manakala engkau tidak memandang dirimu lebih mulia daripada orang lain dan senantiasa berprasangka baik kepada hamba Allah.",
            author = "Pangersa Guru Agung Suryalaya Sirnarasa",
            reference = "Mutiara Akhlakul Karimah"
        ),
        TanbihQuote(
            id = 9,
            category = "Ketenangan Batin",
            quote = "Orang yang senantiasa hidup hatinya dengan dzikirullah akan dikaruniai ketenangan batin dalam menghadapi segala gelombang ujian duniawi.",
            author = "Pangersa Abah Aos M.Q.S.",
            reference = "Kalam Mursyid Sirnarasa"
        ),
        TanbihQuote(
            id = 10,
            category = "Syukur & Ridho",
            quote = "Janganlah engkau menuntut balasan atas amalmu, melainkan bersyukurlah karena Allah telah memberimu taufiq dan hidayah untuk dapat taat kepada-Nya.",
            author = "Hikmah Thoriqoh Qodiriyyah Naqsyabandiyyah",
            reference = "Risalah Tasawuf TQN"
        )
    )

    fun getCurrentQuote(context: Context): TanbihQuote {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastDay = prefs.getInt(KEY_LAST_DAY_OF_YEAR, -1)

        val currentIndex = if (lastDay != today) {
            val autoIndex = today % quotes.size
            prefs.edit()
                .putInt(KEY_LAST_DAY_OF_YEAR, today)
                .putInt(KEY_QUOTE_INDEX, autoIndex)
                .apply()
            autoIndex
        } else {
            prefs.getInt(KEY_QUOTE_INDEX, today % quotes.size)
        }

        return quotes[currentIndex.coerceIn(0, quotes.size - 1)]
    }

    fun getNextQuote(context: Context): TanbihQuote {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentIndex = prefs.getInt(KEY_QUOTE_INDEX, 0)
        val nextIndex = (currentIndex + 1) % quotes.size

        prefs.edit()
            .putInt(KEY_QUOTE_INDEX, nextIndex)
            .apply()

        return quotes[nextIndex]
    }
}
