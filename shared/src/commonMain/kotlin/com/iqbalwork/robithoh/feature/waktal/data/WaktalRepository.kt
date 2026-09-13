package com.iqbalwork.robithoh.feature.waktal.data

import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.core.database.WakilTalqinEntity
import com.iqbalwork.robithoh.feature.waktal.data.remote.WakilTalqinItemDto
import com.iqbalwork.robithoh.feature.waktal.domain.WakilTalqin
import com.iqbalwork.robithoh.feature.waktal.domain.WaktalStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import robithohapp.shared.generated.resources.Res

class WaktalRepository(
    private val database: RobithohDatabase?,
    private val json: Json = Json { ignoreUnknownKeys = true; isLenient = true }
) {
    @OptIn(ExperimentalResourceApi::class)
    suspend fun ensureSeedLoaded() = withContext(Dispatchers.Default) {
        val db = database ?: return@withContext
        val currentItems = db.robithohDatabaseQueries.getAllWakilTalqin().executeAsList()
        if (currentItems.isNotEmpty()) return@withContext

        try {
            val bytes = Res.readBytes("files/waktal_seed.json")
            val seedText = bytes.decodeToString()
            val seedItems = json.decodeFromString<List<WakilTalqinItemDto>>(seedText)

            db.transaction {
                val now = 1740000000L
                seedItems.forEach { item ->
                    db.robithohDatabaseQueries.insertOrReplaceWakilTalqin(
                        id = item.id.toLong(),
                        nomor_urut = item.nomorUrut?.toLong(),
                        nama_lengkap = item.namaLengkap,
                        nama_resmi = item.namaResmi,
                        gelar_depan = item.gelarDepan,
                        gelar_belakang = item.gelarBelakang,
                        status = item.status,
                        status_label = item.statusLabel,
                        tahun_wafat = item.tahunWafat?.toLong(),
                        nomor_telepon = item.nomorTelepon,
                        nomor_whatsapp = item.nomorWhatsapp,
                        foto_url = item.fotoUrl,
                        negara = item.negara,
                        provinsi = item.provinsi,
                        kota_kabupaten = item.kotaKabupaten,
                        kecamatan = item.kecamatan,
                        alamat_lengkap = item.alamatLengkap,
                        latitude = item.latitude,
                        longitude = item.longitude,
                        majlis_binaan = item.majlisBinaan,
                        biografi_singkat = item.biografiSingkat,
                        updated_at = if (item.updatedAt > 0) item.updatedAt else now
                    )
                }
                db.robithohDatabaseQueries.updateWaktalSyncManifest(
                    versionCode = 1,
                    checksum = "seed-v1",
                    lastSyncedAt = now
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getWakilTalqinList(
        query: String = "",
        status: WaktalStatus = WaktalStatus.SEMUA,
        provinsi: String? = null
    ): List<WakilTalqin> = withContext(Dispatchers.Default) {
        val db = database ?: return@withContext emptyList()
        ensureSeedLoaded()

        val entities = db.robithohDatabaseQueries.searchWakilTalqin(
            status = status.raw,
            provinsi = provinsi,
            query = query.trim()
        ).executeAsList()

        entities.map { it.toDomain() }
    }

    suspend fun getWakilTalqinById(id: Int): WakilTalqin? = withContext(Dispatchers.Default) {
        val db = database ?: return@withContext null
        ensureSeedLoaded()

        db.robithohDatabaseQueries.getWakilTalqinById(id.toLong())
            .executeAsOneOrNull()
            ?.toDomain()
    }

    suspend fun getDistinctProvinces(): List<String> = withContext(Dispatchers.Default) {
        val db = database ?: return@withContext emptyList()
        ensureSeedLoaded()

        db.robithohDatabaseQueries.getDistinctProvinces().executeAsList()
    }

    suspend fun saveWaktalSnapshot(
        items: List<WakilTalqinItemDto>,
        versionCode: Int,
        checksum: String?
    ) = withContext(Dispatchers.Default) {
        val db = database ?: return@withContext
        val now = 1740000000L

        db.transaction {
            items.forEach { item ->
                db.robithohDatabaseQueries.insertOrReplaceWakilTalqin(
                    id = item.id.toLong(),
                    nomor_urut = item.nomorUrut?.toLong(),
                    nama_lengkap = item.namaLengkap,
                    nama_resmi = item.namaResmi,
                    gelar_depan = item.gelarDepan,
                    gelar_belakang = item.gelarBelakang,
                    status = item.status,
                    status_label = item.statusLabel,
                    tahun_wafat = item.tahunWafat?.toLong(),
                    nomor_telepon = item.nomorTelepon,
                    nomor_whatsapp = item.nomorWhatsapp,
                    foto_url = item.fotoUrl,
                    negara = item.negara,
                    provinsi = item.provinsi,
                    kota_kabupaten = item.kotaKabupaten,
                    kecamatan = item.kecamatan,
                    alamat_lengkap = item.alamatLengkap,
                    latitude = item.latitude,
                    longitude = item.longitude,
                    majlis_binaan = item.majlisBinaan,
                    biografi_singkat = item.biografiSingkat,
                    updated_at = if (item.updatedAt > 0) item.updatedAt else now
                )
            }
            db.robithohDatabaseQueries.updateWaktalSyncManifest(
                versionCode = versionCode.toLong(),
                checksum = checksum,
                lastSyncedAt = now
            )
        }
    }

    suspend fun getLocalVersionCode(): Int = withContext(Dispatchers.Default) {
        val db = database ?: return@withContext 1
        db.robithohDatabaseQueries.getWaktalSyncManifest().executeAsOneOrNull()?.version_code?.toInt() ?: 1
    }

    private fun WakilTalqinEntity.toDomain(): WakilTalqin {
        return WakilTalqin(
            id = id.toInt(),
            nomorUrut = nomor_urut?.toInt(),
            namaLengkap = nama_lengkap,
            namaResmi = nama_resmi,
            gelarDepan = gelar_depan,
            gelarBelakang = gelar_belakang,
            status = WaktalStatus.fromRaw(status),
            tahunWafat = tahun_wafat?.toInt(),
            nomorTelepon = nomor_telepon,
            nomorWhatsapp = nomor_whatsapp,
            fotoUrl = foto_url,
            negara = negara,
            provinsi = provinsi,
            kotaKabupaten = kota_kabupaten,
            kecamatan = kecamatan,
            alamatLengkap = alamat_lengkap,
            latitude = latitude,
            longitude = longitude,
            majlisBinaan = majlis_binaan,
            biografiSingkat = biografi_singkat
        )
    }
}
