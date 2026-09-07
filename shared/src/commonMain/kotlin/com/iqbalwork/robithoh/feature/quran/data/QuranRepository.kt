package com.iqbalwork.robithoh.feature.quran.data

import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.feature.quran.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface QuranRepository {
    fun getAllSurahs(): Flow<List<SurahMeta>>
    suspend fun getSurah(surahNumber: Int): SurahMeta?
    fun searchSurahs(query: String): Flow<List<SurahMeta>>
    fun getAyahs(surahNumber: Int): Flow<List<Ayah>>
    fun getLastReadBookmark(): Flow<QuranBookmark?>
    suspend fun saveLastRead(surahNumber: Int, ayahNumber: Int, surahName: String)
    fun getAllBookmarks(): Flow<List<QuranBookmark>>
    fun getShalawatList(): List<ShalawatModel>
    fun getZiarahSections(): List<ZiarahSection>
}

class QuranRepositoryImpl(
    private val database: RobithohDatabase? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : QuranRepository {

    private val allSurahs = QuranData.surahs

    override fun getAllSurahs(): Flow<List<SurahMeta>> = flow {
        emit(allSurahs)
    }.flowOn(dispatcher)

    override suspend fun getSurah(surahNumber: Int): SurahMeta? = withContext(dispatcher) {
        allSurahs.find { it.number == surahNumber }
    }

    override fun searchSurahs(query: String): Flow<List<SurahMeta>> = flow {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            emit(allSurahs)
            return@flow
        }
        val filtered = allSurahs.filter {
            it.nameLatin.contains(trimmed, ignoreCase = true) ||
            it.indonesianMeaning.contains(trimmed, ignoreCase = true) ||
            it.nameArabic.contains(trimmed) ||
            it.number.toString() == trimmed
        }
        emit(filtered)
    }.flowOn(dispatcher)

    override fun getAyahs(surahNumber: Int): Flow<List<Ayah>> = flow {
        val ayahs = QuranData.getAyahsForSurah(surahNumber)
        emit(ayahs)
    }.flowOn(dispatcher)

    companion object {
        private val _lastReadBookmarkFlow = kotlinx.coroutines.flow.MutableSharedFlow<QuranBookmark>(
            replay = 1,
            onBufferOverflow = kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
        )
    }

    override fun getLastReadBookmark(): Flow<QuranBookmark?> = flow {
        val initialBookmark = queryDbLastRead() ?: QuranBookmark(
            id = 1L,
            surahNumber = 1,
            ayahNumber = 1,
            surahName = "Al-Fatihah",
            timestamp = 0L
        )
        emit(initialBookmark)
        _lastReadBookmarkFlow.collect { bookmark ->
            emit(bookmark)
        }
    }.flowOn(dispatcher)

    private fun queryDbLastRead(): QuranBookmark? {
        val db = database ?: return null
        return try {
            val entity = db.robithohDatabaseQueries.getLastReadBookmark("quran").executeAsOneOrNull()
            entity?.let {
                QuranBookmark(
                    id = it.id,
                    surahNumber = it.page_or_surah.toInt(),
                    ayahNumber = it.verse_or_section.toInt(),
                    surahName = it.title,
                    timestamp = it.updated_at
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun saveLastRead(surahNumber: Int, ayahNumber: Int, surahName: String): Unit = withContext(dispatcher) {
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val bookmark = QuranBookmark(
            id = 1L,
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            surahName = surahName,
            timestamp = now
        )
        val db = database
        if (db != null) {
            try {
                db.robithohDatabaseQueries.insertOrUpdateBookmark(
                    id = 1L,
                    item_type = "quran",
                    item_id = "surah_$surahNumber",
                    title = surahName,
                    subtitle = "Ayat $ayahNumber",
                    page_or_surah = surahNumber.toLong(),
                    verse_or_section = ayahNumber.toLong(),
                    created_at = now,
                    updated_at = now
                )
                notifyQuranWidgetUpdate()
            } catch (_: Exception) {
                // ignore
            }
        }
        _lastReadBookmarkFlow.emit(bookmark)
    }

    override fun getAllBookmarks(): Flow<List<QuranBookmark>> = flow {
        val db = database
        if (db != null) {
            try {
                val list = db.robithohDatabaseQueries.getBookmarksByType("quran").executeAsList()
                emit(
                    list.map {
                        QuranBookmark(
                            id = it.id,
                            surahNumber = it.page_or_surah.toInt(),
                            ayahNumber = it.verse_or_section.toInt(),
                            surahName = it.title,
                            timestamp = it.updated_at
                        )
                    }
                )
                return@flow
            } catch (_: Exception) {
                // fallback
            }
        }
        emit(emptyList())
    }.flowOn(dispatcher)

    override fun getShalawatList(): List<ShalawatModel> = ShalawatZiarahData.shalawatList

    override fun getZiarahSections(): List<ZiarahSection> = ShalawatZiarahData.ziarahSections
}
