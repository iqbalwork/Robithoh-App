package com.iqbalwork.robithoh.feature.manaqib.data

import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.feature.manaqib.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface ManaqibRepository {
    fun getAllChapters(): Flow<List<ManqobahChapter>>
    suspend fun getChapter(chapterNumber: Int): ManqobahChapter?
    fun searchChapters(query: String): Flow<List<ManqobahChapter>>
    suspend fun seedDatabase()
    fun getTanbih(): TanbihContent
    fun getMcProgramList(): List<McProgramItem>
    fun getSilsilahNodes(): List<SilsilahNode>
    fun searchSilsilah(query: String): List<SilsilahNode>
    fun getKhotamanSteps(): List<KhotamanStep>
    fun getDoaList(): List<DoaSpiritualItem>
    fun getDoaById(id: String): DoaSpiritualItem?
}

class ManaqibRepositoryImpl(
    private val database: RobithohDatabase? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ManaqibRepository {

    private val staticChapters = ManaqibDataSeeder.getManqobahChapters()

    override fun getAllChapters(): Flow<List<ManqobahChapter>> = flow {
        val db = database
        if (db != null) {
            try {
                val dbEntities = db.robithohDatabaseQueries.getAllManqobah().executeAsList()
                if (dbEntities.isNotEmpty()) {
                    emit(dbEntities.map { it.toDomainModel() })
                    return@flow
                }
            } catch (_: Exception) {
                // fallback to static memory seed
            }
        }
        emit(staticChapters)
    }.flowOn(dispatcher)

    override suspend fun getChapter(chapterNumber: Int): ManqobahChapter? = withContext(dispatcher) {
        val db = database
        if (db != null) {
            try {
                val entity = db.robithohDatabaseQueries.getManqobahByChapter(chapterNumber.toLong()).executeAsOneOrNull()
                if (entity != null) {
                    return@withContext entity.toDomainModel()
                }
            } catch (_: Exception) {
                // fallback
            }
        }
        staticChapters.find { it.chapterNumber == chapterNumber }
    }

    override fun searchChapters(query: String): Flow<List<ManqobahChapter>> = flow {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            emit(staticChapters)
            return@flow
        }
        val db = database
        if (db != null) {
            try {
                val dbResults = db.robithohDatabaseQueries.searchManqobah(trimmed).executeAsList()
                if (dbResults.isNotEmpty()) {
                    emit(dbResults.map { it.toDomainModel() })
                    return@flow
                }
            } catch (_: Exception) {
                // fallback
            }
        }
        val filtered = staticChapters.filter {
            it.titleIndonesian.contains(trimmed, ignoreCase = true) ||
            it.titleSundanese.contains(trimmed, ignoreCase = true) ||
            it.contentIndonesian.contains(trimmed, ignoreCase = true) ||
            it.contentSundanese.contains(trimmed, ignoreCase = true) ||
            it.chapterNumber.toString() == trimmed
        }
        emit(filtered)
    }.flowOn(dispatcher)

    override suspend fun seedDatabase(): Unit = withContext(dispatcher) {
        val db = database ?: return@withContext
        try {
            val count = db.robithohDatabaseQueries.getAllManqobah().executeAsList().size
            if (count < 56) {
                staticChapters.forEach { ch ->
                    db.robithohDatabaseQueries.insertManqobah(
                        id = ch.id,
                        chapter_number = ch.chapterNumber.toLong(),
                        title_arabic = ch.titleArabic,
                        title_indonesian = ch.titleIndonesian,
                        title_sundanese = ch.titleSundanese,
                        content_arabic = ch.contentArabic,
                        content_indonesian = ch.contentIndonesian,
                        content_sundanese = ch.contentSundanese,
                        audio_path = ch.audioPath
                    )
                }
            }
        } catch (_: Exception) {
            // ignore
        }
    }

    override fun getTanbih(): TanbihContent = ManaqibDataSeeder.tanbihData

    override fun getMcProgramList(): List<McProgramItem> = ManaqibDataSeeder.mcProgramList

    override fun getSilsilahNodes(): List<SilsilahNode> = ManaqibDataSeeder.silsilahNodes

    override fun searchSilsilah(query: String): List<SilsilahNode> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return ManaqibDataSeeder.silsilahNodes
        return ManaqibDataSeeder.silsilahNodes.filter {
            it.name.contains(trimmed, ignoreCase = true) ||
            it.title.contains(trimmed, ignoreCase = true) ||
            it.locationOrEpithet.contains(trimmed, ignoreCase = true) ||
            it.orderNumber.toString() == trimmed
        }
    }

    override fun getKhotamanSteps(): List<KhotamanStep> = ManaqibDataSeeder.khotamanSteps

    override fun getDoaList(): List<DoaSpiritualItem> = listOf(
        ManaqibDataSeeder.doaManaqobah,
        ManaqibDataSeeder.doaRijalulGhoib,
        ManaqibDataSeeder.doaAshabulKahfi
    )

    override fun getDoaById(id: String): DoaSpiritualItem? = getDoaList().find { it.id == id }
}

private fun com.iqbalwork.robithoh.core.database.ManqobahEntity.toDomainModel(): ManqobahChapter {
    return ManqobahChapter(
        id = this.id,
        chapterNumber = this.chapter_number.toInt(),
        titleArabic = this.title_arabic,
        titleIndonesian = this.title_indonesian,
        titleSundanese = this.title_sundanese,
        contentArabic = this.content_arabic,
        contentIndonesian = this.content_indonesian,
        contentSundanese = this.content_sundanese,
        audioPath = this.audio_path
    )
}
