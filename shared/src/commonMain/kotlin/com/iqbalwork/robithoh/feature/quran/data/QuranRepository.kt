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

    override fun getLastReadBookmark(): Flow<QuranBookmark?> = flow {
        val db = database
        if (db != null) {
            try {
                val entity = db.robithohDatabaseQueries.getLastReadBookmark("quran").executeAsOneOrNull()
                if (entity != null) {
                    emit(
                        QuranBookmark(
                            id = entity.id,
                            surahNumber = entity.page_or_surah.toInt(),
                            ayahNumber = entity.verse_or_section.toInt(),
                            surahName = entity.title,
                            timestamp = entity.updated_at
                        )
                    )
                    return@flow
                }
            } catch (_: Exception) {
                // fallback
            }
        }
        // Default initial bookmark: Al-Fatihah ayat 1
        emit(QuranBookmark(id = 1L, surahNumber = 1, ayahNumber = 1, surahName = "Al-Fatihah", timestamp = 0L))
    }.flowOn(dispatcher)

    override suspend fun saveLastRead(surahNumber: Int, ayahNumber: Int, surahName: String): Unit = withContext(dispatcher) {
        val db = database ?: return@withContext
        try {
            val now = 1771800000000L // current epoch timestamp
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
        } catch (_: Exception) {
            // ignore
        }
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
