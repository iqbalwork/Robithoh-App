# Implementation Plan: Direktori Wakil Talqin (Waktal) & Offline-First Sync Engine

* **Feature**: Direktori Resmi Ulama Wakil Talqin (Waktal) TQN Suryalaya - Sirnarasa
* **Target Version**: `1.2.0` (Code `10`)
* **ADR Reference**: [ADR-0008: Waktal (Wakil Talqin) Directory & Offline-First Sync Architecture](../adr/0008-waktal-directory-and-offline-sync.md)
* **Spec Reference**: [0002-waktal-directory-and-sync-engine.md](../spec/0002-waktal-directory-and-sync-engine.md)
* **CMS Contract**: `/Users/iqbalfauzi/Personal/App/Robithoh-CMS/docs/API_CONTRACT_WAKTAL.md`
* **Date**: 2026-09-13

---

## 1. Scope and Prerequisites

### In Scope
- **Offline-First SQLite Storage**:
  - `WakilTalqinEntity` & `WaktalSyncManifestEntity` dalam `RobithohDatabase.sq`.
  - Inisialisasi awal (initial seed) dari berkas JSON ter-bundel (`waktal_seed.json`) agar aplikasi 100% berfungsi offline saat baru diinstal (Core Rule 1).
- **Sinkronisasi Remote CMS**:
  - Endpoint manifest versi (`/api/v1/sync/version-manifest`) dan download snapshot delta (`/api/v1/sync/delta/waktal`).
  - Batch replace transactional ingestion ke SQLite.
- **Kalkulasi Jarak On-Device (Haversine)**:
  - Perhitungan jarak ulama terhadap posisi GPS pengguna menggunakan formula Spherical Haversine lokal murni Kotlin.
  - Sorting jarak terdekat tanpa memerlukan panggilan API jaringan.
- **Presentasi MVI (Compose Multiplatform)**:
  - `WaktalListScreen` & `WaktalListContent`: Search bar, filter status (`Semua`, `Aktif`, `Almarhum`), filter provinsi, toggle urutkan terdekat, kartu ulama.
  - `WaktalDetailScreen` & `WaktalDetailContent`: Header foto portrait, badge status, info majlis & alamat, biografi singkat, kartu aksi kontak.
- **Aksi Cepat Eksternal**:
  - Panggilan telepon langsung (`tel:{nomor}`).
  - Chat resmi WhatsApp (`https://wa.me/{nomor}`).
  - Peta lokasi navigasi (`geo:{lat},{lng}`).
- **Integrasi Navigasi & Home**:
  - `ScreenKey.WaktalList` dan `ScreenKey.WaktalDetail(val id: Int)` dalam Navigation3.
  - Tombol menu grid "Wakil Talqin" pada `HomeTabContent`.

### Out of Scope (Deferred)
- Input catatan pribadi atau bookmark favorit per ulama (Fase 2).
- Tampilan peta interaktif langsung dengan native Map SDK (menggunakan external map intent).

---

## 2. Technical Architecture

```
                               ┌──────────────────────────────────────────────┐
                               │             Robithoh CMS Backend             │
                               │   - GET /api/v1/sync/version-manifest        │
                               │   - GET /api/v1/sync/delta/waktal            │
                               └──────────────────────┬───────────────────────┘
                                                      │ (Ktor HTTP Client)
                                                      ▼
 ┌───────────────────────────┐         ┌──────────────────────────────┐
 │  waktal_seed.json         │         │      WaktalSyncManager       │
 │  (Pre-bundled Assets)     ├────────►│  (Version checking & delta)  │
 └───────────────────────────┘         └──────────────┬───────────────┘
                                                      │ (Batch Transaction)
                                                      ▼
                                       ┌──────────────────────────────┐
                                       │     SQLite Local Database    │
                                       │    - WakilTalqinEntity       │
                                       │    - WaktalSyncManifest      │
                                       └──────────────┬───────────────┘
                                                      │ (Reactive Queries)
                                                      ▼
 ┌───────────────────────────┐         ┌──────────────────────────────┐
 │     LocationProvider      ├────────►│       WaktalRepository       │
 │   (Multiplatform GPS)     │         │  (Local search & Haversine)  │
 └───────────────────────────┘         └──────────────┬───────────────┘
                                                      │
                                                      ▼
                                       ┌──────────────────────────────┐
                                       │       WaktalViewModel        │
                                       │ (StateFlow<WaktalListUiState>)│
                                       └──────────────┬───────────────┘
                                                      │
                                                      ▼
                                       ┌──────────────────────────────┐
                                       │       WaktalListScreen       │
                                       │       WaktalListContent      │
                                       └──────────────────────────────┘
```

---

## 3. Implementation Tasks Sequence

### Phase 1: Database Schema & Pre-bundled Seed Data
- [x] Modifikasi `shared/src/commonMain/sqldelight/com/iqbalwork/robithoh/core/database/RobithohDatabase.sq`:
  - Tambahkan tabel `WakilTalqinEntity` dan `WaktalSyncManifestEntity`.
  - Tambahkan indeks: `waktal_status_idx`, `waktal_provinsi_idx`, `waktal_kota_idx`, `waktal_nama_idx`.
  - Tambahkan query: `getAllWakilTalqin`, `getWakilTalqinById`, `searchWakilTalqin`, `getDistinctProvinces`, `insertOrReplaceWakilTalqin`, `getWaktalSyncManifest`, `updateWaktalSyncManifest`, `deleteAllWakilTalqin`.
- [x] Ekstrak dan buat berkas seed baseline `shared/src/commonMain/composeResources/files/waktal_seed.json`:
  - Format JSON array snapshot data awal Wakil Talqin (399 record dari Robithoh-CMS).
- [x] Update baseline SHA-256 pada `.agents/rules/sacred_texts_hashes.json` menggunakan:
  ```bash
  bash .agents/tools/check_sacred_texts.sh --update
  ```

### Phase 2: Remote DTOs & Ktor API Client
- [x] Buat `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/waktal/data/remote/WaktalDto.kt`:
  - `@Serializable BaseApiResponse<T>`
  - `@Serializable PaginationMeta`
  - `@Serializable WakilTalqinItemDto`
  - `@Serializable SyncManifestResponseDto`
- [x] Buat `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/waktal/data/remote/WaktalApiService.kt`:
  - Method `checkVersionManifest()`
  - Method `fetchWaktalDelta()`

### Phase 3: Domain Logic, Proximity Engine & Repository
- [x] Buat `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/waktal/domain/HaversineDistance.kt`:
  - Algoritma perhitungan jarak `calculateKm(lat1, lon1, lat2, lon2): Double`.
  - Helper formatting jarak `formatDistance(distanceKm: Double?): String?`.
- [x] Buat `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/waktal/domain/WakilTalqin.kt`:
  - Domain class immutable `WakilTalqin` & enum `WaktalStatus`.
  - Mapper function dari DTO / SQLDelight entity ke domain model.
- [x] Buat `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/waktal/data/WaktalRepository.kt`:
  - Loading data awal dari `waktal_seed.json` jika database SQLite masih kosong.
  - Query get list dengan filter `status`, `provinsi`, dan `query`.
  - Query get detail ulama berdasarkan `id`.
  - Query daftar provinsi unik untuk filter dropdown.
- [x] Buat `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/waktal/data/sync/WaktalSyncManager.kt`:
  - State machine `WaktalSyncState` (`Idle`, `Checking`, `Syncing`, `Success`, `Error`).
  - Evaluasi manifest versi server vs lokal.
  - Transactional batch replace ke SQLite saat update versi tersedia.

### Phase 4: MVI Presentation & UI Components
- [x] Buat kontrak MVI di `feature/waktal/presentation/`:
  - `WaktalListMvi.kt` (`WaktalListUiState`, `WaktalListUiIntent`, `WaktalListUiEffect`).
  - `WaktalDetailMvi.kt` (`WaktalDetailUiState`, `WaktalDetailUiIntent`, `WaktalDetailUiEffect`).
- [x] Buat ViewModel:
  - `WaktalViewModel.kt`: Mengelola flow state pencarian, filter status/wilayah, pengurutan jarak GPS, dan sinkronisasi manual.
  - `WaktalDetailViewModel.kt`: Mengambil detail profil dan meluncurkan intent panggilan/pesan/peta.
- [x] Buat komponen UI modular di `feature/waktal/ui/components/`:
  - `WaktalCard.kt`: Kartu ringkasan ulama dengan foto portrait/placeholder, nama resmi, badge status, domisili, dan jarak KM.
  - `WaktalStatusBadge.kt`: Badge visual status `Aktif` (hijau) atau `Almarhum` (abu-abu/marun) disertai tahun wafat.
  - `WaktalQuickActions.kt`: Baris tombol aksi cepat (Telepon, WhatsApp, Peta).
- [x] Buat Screen & Content Composable (strict MVI separation & SwipeToRefresh):
  - `WaktalListScreen.kt` & `WaktalListContent.kt` dengan `PullToRefreshBox` dan menghapus ikon refresh di top app bar.
  - `WaktalDetailScreen.kt` & `WaktalDetailContent.kt`.

### Phase 5: Navigation & Home Grid Integration
- [x] Modifikasi `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/navigation/ScreenKey.kt`:
  - Tambahkan `data object WaktalList : ScreenKey`
  - Tambahkan `data class WaktalDetail(val id: Int) : ScreenKey`
- [x] Modifikasi `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/navigation/Screens.kt` & `App.kt`:
  - Tambahkan entri rute Navigation3 untuk `ScreenKey.WaktalList` dan `ScreenKey.WaktalDetail`.
- [x] Modifikasi `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/home/ui/HomeTabContent.kt`:
  - Tambahkan tombol menu grid **"Wakil Talqin"** (`id = "waktal"`, emoji `"👳‍♂️"`, title `"Wakil Talqin"`).
  - Sambungkan aksi navigasi menu ke `ScreenKey.WaktalList`.

### Phase 6: Automated Testing & Verification Pipeline
- [x] Buat unit tests di `shared/src/commonTest/kotlin/`:
  - `com.iqbalwork.robithoh.feature.waktal.WaktalDomainTest`: Validasi akurasi jarak dan formatting.
- [x] Jalankan verifikasi otomatis:
  ```bash
  ./gradlew :shared:testDebugUnitTest
  bash .agents/tools/check_mvi_architecture.sh
  bash .agents/tools/check_hardcoded_strings.sh
  bash .agents/tools/check_sacred_texts.sh --check
  ```
- [x] Sinkronisasi dokumentasi audit di `.agents/changelog/AI_CHANGELOG.md` dan `.agents/memory/STATE.md`.
