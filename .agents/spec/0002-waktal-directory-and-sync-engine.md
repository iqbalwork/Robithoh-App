---
title: "Technical Specification: Waktal (Wakil Talqin) Directory & Offline-First Sync Engine"
version: 1.0.0
date_created: 2026-09-13
owner: "Robithoh App Engineering"
tags: ["waktal", "sync", "sqldelight", "ktor", "offline-first", "mvi", "gps-proximity", "compose-multiplatform"]
---

# Technical Specification: Waktal (Wakil Talqin) Directory & Offline-First Sync Engine

This specification defines the functional, data, architectural, and mathematical requirements for the **Wakil Talqin (Waktal)** directory module and its background synchronization engine in **Robithoh App**, implementing the CMS REST contract specified in `Robithoh-CMS/docs/API_CONTRACT_WAKTAL.md`.

---

## 1. Purpose & Scope

### 1.1 Purpose
To provide ikhwan and akhwat of Tarekat Qodiriyyah Naqsyabandiyyah (TQN) Pondok Pesantren Suryalaya - Sirnarasa with a fast, comprehensive, 100% offline-first directory of authorized ulama Wakil Talqin. The feature allows users to discover local and historical Wakil Talqin, calculate proximity distance using on-device GPS, filter by status and territory, inspect biographic profiles, and initiate direct pastoral contact (phone call, WhatsApp, and navigation map coordinates).

### 1.2 Scope
*   **In Scope**:
    *   Pre-bundled offline SQLite database storage (`WakilTalqinEntity` via SQLDelight).
    *   Initial seed snapshot ingestion (`waktal_seed.json`) ensuring zero network latency on first launch.
    *   Remote synchronization with Robithoh CMS (`/api/v1/sync/version-manifest` and `/api/v1/sync/delta/waktal`).
    *   Client-side Haversine GPS proximity sorting based on device location from `LocationProvider`.
    *   MVI Directory Screen (`WaktalListScreen` / `WaktalListContent`) with real-time multi-token search, status filter chips (`Semua`, `Aktif`, `Almarhum`), and province dropdown.
    *   MVI Profile Detail Screen (`WaktalDetailScreen` / `WaktalDetailContent`) with photo portrait, titles, biography, majlis binaan, and external intent launchers (`tel:`, `https://wa.me/`, `geo:`).
    *   Navigation3 integration (`ScreenKey.WaktalList` and `ScreenKey.WaktalDetail`).
*   **Target Platforms**: Android (API 24+) and iOS (iOS 15+) via Compose Multiplatform.
*   **Out of Scope for Initial Release**:
    *   User-submitted notes or personal bookmarking of Wakil Talqin (planned for Phase 2).
    *   Interactive map clustering view (MapLibre/Google Maps native SDK embed; direct external map intent launch is used instead).

---

## 2. Definitions & Domain Models

### 2.1 Domain Models
```kotlin
enum class WaktalStatus(val raw: String, val label: String) {
    SEMUA("semua", "Semua"),
    AKTIF("aktif", "Aktif"),
    WAFAT("wafat", "Almarhum");

    companion object {
        fun fromRaw(raw: String): WaktalStatus =
            entries.firstOrNull { it.raw.equals(raw, ignoreCase = true) } ?: SEMUA
    }
}

data class WakilTalqin(
    val id: Int,
    val nomorUrut: Int?,
    val namaLengkap: String,
    val namaResmi: String,
    val gelarDepan: String?,
    val gelarBelakang: String?,
    val status: WaktalStatus,
    val tahunWafat: Int?,
    val nomorTelepon: String?,
    val nomorWhatsapp: String?,
    val fotoUrl: String?,
    val negara: String = "Indonesia",
    val provinsi: String,
    val kotaKabupaten: String,
    val kecamatan: String?,
    val alamatLengkap: String?,
    val latitude: Double?,
    val longitude: Double?,
    val distanceKm: Double? = null,
    val majlisBinaan: String?,
    val biografiSingkat: String?
) {
    val isAlmarhum: Boolean get() = status == WaktalStatus.WAFAT
    val dialPhoneUri: String? get() = nomorTelepon?.takeIf { it.isNotBlank() }?.let { "tel:$it" }
    val whatsappUrl: String? get() = nomorWhatsapp?.takeIf { it.isNotBlank() }?.let { "https://wa.me/$it" }
    val mapIntentUri: String? get() = if (latitude != null && longitude != null) {
        "geo:$latitude,$longitude?q=$latitude,$longitude(${namaLengkap.replace(" ", "+")})"
    } else null
}
```

---

## 3. Requirements & Constraints

### 3.1 Functional Requirements
*   **REQ-001 (100% Offline Capability)**: The app MUST function out-of-the-box with pre-bundled baseline seed data without requiring an initial network connection.
*   **REQ-002 (Multi-Param Search)**: The search bar MUST filter against `nama_lengkap`, `nama_resmi`, `majlis_binaan`, `kota_kabupaten`, and `provinsi` using case-insensitive substring matching.
*   **REQ-003 (Status Filter)**: Users MUST be able to toggle between `Semua`, `Aktif` (masih sugeng), and `Almarhum` (`wafat`).
*   **REQ-004 (Territory Filter)**: Users MUST be able to filter by `provinsi` and `kota_kabupaten`.
*   **REQ-005 (Proximity Sorting)**: When GPS coordinates are available, users MUST have an option to sort Wakil Talqin by distance from nearest to farthest.
*   **REQ-006 (Haversine Accuracy)**: Distance calculations MUST run on-device using the spherical Haversine formula and be formatted as `X.X km` (or `X m` if $< 1\text{ km}$).
*   **REQ-007 (Detail Profile)**: Selecting a card MUST navigate to `WaktalDetailScreen`, displaying the ulama's full title, photo portrait, status chip (with death year if deceased), majlis binaan, full address, bio, and quick-action bar.
*   **REQ-008 (Quick Actions)**:
    *   Tapping **Hubungi Telepon** MUST trigger the platform dialer via `tel:{nomor}`.
    *   Tapping **Kirim WhatsApp** MUST open WhatsApp chat via `https://wa.me/{nomor}`.
    *   Tapping **Petunjuk Arah / Peta** MUST trigger the native map application (Google Maps / Apple Maps) using `geo:{lat},{lng}`.
    *   If a field is null/blank, the corresponding button MUST be disabled or cleanly hidden.
*   **REQ-009 (Sync Engine)**: When network connectivity is active, the app MUST query `GET /api/v1/sync/version-manifest`. If server `version_code` > local `version_code`, the app downloads `GET /api/v1/sync/delta/waktal` and atomically updates the local SQLite database.
*   **REQ-010 (Swipe-to-Refresh Gesture)**: The directory screen MUST support native Swipe-to-Refresh (via Compose Material3 `PullToRefreshBox`) to trigger synchronization on demand, replacing top app bar action icons.
*   **REQ-011 (Pagination & Virtualization)**: The directory list MUST utilize Compose `LazyColumn` with key-based item identity (`key = { it.id }`) for stutter-free scrolling.
*   **REQ-012 (Home Integration)**: A prominent entry button MUST be added to `HomeTabContent` grid menus to open the Waktal directory.

### 3.2 Non-Functional & Architecture Constraints
*   **CON-001 (Core Rule 1 - Offline First)**: Under no circumstance shall the UI block or fail if the network is unavailable. Local SQLite is the single source of truth (SSOT).
*   **CON-002 (Core Rule 2 - MVI UI Separation)**: Stateful logic belongs exclusively in `WaktalViewModel` / `WaktalDetailViewModel`. Presentation composables (`WaktalListContent`, `WaktalDetailContent`) must be pure and stateless.
*   **CON-003 (Core Rule 3 - No Hardcoded Strings)**: All user-facing strings must reside in multiplatform string resources (`Res.string.*`).
*   **CON-004 (Core Rule 9 - No Inline FQNs)**: Import all packages at file head; avoid inline FQNs.
*   **PERF-001 (Instant Filtering)**: In-memory and SQLite queries must resolve in $< 16\text{ ms}$ for 500 items.
*   **PERF-002 (Atomic Transactions)**: Sync ingestion of 400+ records must occur in a single SQLDelight transaction `database.transaction { ... }` taking $< 50\text{ ms}$.

---

## 4. Database Schema & SQLDelight Queries

The following entities and queries are specified for inclusion in `RobithohDatabase.sq`:

```sql
-- Wakil Talqin Entity (Directory of Ulama Wakil Talqin TQN)
CREATE TABLE WakilTalqinEntity (
    id INTEGER NOT NULL PRIMARY KEY,
    nomor_urut INTEGER,
    nama_lengkap TEXT NOT NULL,
    nama_resmi TEXT NOT NULL,
    gelar_depan TEXT,
    gelar_belakang TEXT,
    status TEXT NOT NULL, -- 'aktif' | 'wafat'
    status_label TEXT NOT NULL, -- 'Aktif' | 'Almarhum'
    tahun_wafat INTEGER,
    nomor_telepon TEXT,
    nomor_whatsapp TEXT,
    foto_url TEXT,
    negara TEXT NOT NULL DEFAULT 'Indonesia',
    provinsi TEXT NOT NULL,
    kota_kabupaten TEXT NOT NULL,
    kecamatan TEXT,
    alamat_lengkap TEXT,
    latitude REAL,
    longitude REAL,
    majlis_binaan TEXT,
    biografi_singkat TEXT,
    updated_at INTEGER NOT NULL
);

CREATE INDEX waktal_status_idx ON WakilTalqinEntity(status);
CREATE INDEX waktal_provinsi_idx ON WakilTalqinEntity(provinsi);
CREATE INDEX waktal_kota_idx ON WakilTalqinEntity(kota_kabupaten);
CREATE INDEX waktal_nama_idx ON WakilTalqinEntity(nama_lengkap);

-- Waktal Sync Manifest Entity
CREATE TABLE WaktalSyncManifestEntity (
    id INTEGER NOT NULL PRIMARY KEY DEFAULT 1,
    version_code INTEGER NOT NULL DEFAULT 1,
    checksum TEXT,
    last_synced_at INTEGER NOT NULL
);

-- Queries
getAllWakilTalqin:
SELECT * FROM WakilTalqinEntity ORDER BY nomor_urut ASC, id ASC;

getWakilTalqinById:
SELECT * FROM WakilTalqinEntity WHERE id = :id LIMIT 1;

searchWakilTalqin:
SELECT * FROM WakilTalqinEntity
WHERE (:status = 'semua' OR status = :status)
  AND (:provinsi IS NULL OR provinsi = :provinsi)
  AND (
    :query = '' OR
    nama_lengkap LIKE '%' || :query || '%' OR
    nama_resmi LIKE '%' || :query || '%' OR
    majlis_binaan LIKE '%' || :query || '%' OR
    kota_kabupaten LIKE '%' || :query || '%' OR
    provinsi LIKE '%' || :query || '%'
  )
ORDER BY nomor_urut ASC, id ASC;

getDistinctProvinces:
SELECT DISTINCT provinsi FROM WakilTalqinEntity WHERE provinsi IS NOT NULL AND provinsi != '' ORDER BY provinsi ASC;

insertOrReplaceWakilTalqin:
INSERT OR REPLACE INTO WakilTalqinEntity(
    id, nomor_urut, nama_lengkap, nama_resmi, gelar_depan, gelar_belakang,
    status, status_label, tahun_wafat, nomor_telepon, nomor_whatsapp,
    foto_url, negara, provinsi, kota_kabupaten, kecamatan, alamat_lengkap,
    latitude, longitude, majlis_binaan, biografi_singkat, updated_at
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

getWaktalSyncManifest:
SELECT * FROM WaktalSyncManifestEntity WHERE id = 1 LIMIT 1;

updateWaktalSyncManifest:
INSERT OR REPLACE INTO WaktalSyncManifestEntity(id, version_code, checksum, last_synced_at)
VALUES (1, :versionCode, :checksum, :lastSyncedAt);

deleteAllWakilTalqin:
DELETE FROM WakilTalqinEntity;
```

---

## 5. Mathematical & Algorithmic Specification

### 5.1 Haversine Distance Formula
Given user position $(\phi_1, \lambda_1)$ and ulama location $(\phi_2, \lambda_2)$ in decimal degrees, distance $d$ in kilometers is computed as:

$$\Delta\phi = \frac{(\phi_2 - \phi_1) \cdot \pi}{180}, \quad \Delta\lambda = \frac{(\lambda_2 - \lambda_1) \cdot \pi}{180}$$

$$a = \sin^2\left(\frac{\Delta\phi}{2}\right) + \cos\left(\frac{\phi_1 \cdot \pi}{180}\right) \cdot \cos\left(\frac{\phi_2 \cdot \pi}{180}\right) \cdot \sin^2\left(\frac{\Delta\lambda}{2}\right)$$

$$c = 2 \cdot \text{atan2}\left(\sqrt{a}, \sqrt{1-a}\right)$$

$$d = R \cdot c$$

where mean Earth radius $R = 6371.0088\text{ km}$.

```kotlin
object HaversineDistance {
    private const val EARTH_RADIUS_KM = 6371.0088

    fun calculateKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = (lat2 - lat1) * (kotlin.math.PI / 180.0)
        val dLon = (lon2 - lon1) * (kotlin.math.PI / 180.0)
        val rLat1 = lat1 * (kotlin.math.PI / 180.0)
        val rLat2 = lat2 * (kotlin.math.PI / 180.0)

        val a = kotlin.math.sin(dLat / 2.0).let { it * it } +
            kotlin.math.cos(rLat1) * kotlin.math.cos(rLat2) *
            kotlin.math.sin(dLon / 2.0).let { it * it }
        val c = 2.0 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1.0 - a))
        return EARTH_RADIUS_KM * c
    }
}
```

### 5.2 Distance Presentation Formatting
* If $d < 1.0\text{ km}$: format as `"$(round(d * 1000).toInt()) m"` (e.g. `450 m`).
* If $1.0 \le d < 100.0\text{ km}$: format with 1 decimal place (e.g. `14.2 km`).
* If $d \ge 100.0\text{ km}$: format as integer (e.g. `128 km`).
* If coordinates are missing (`latitude == null || longitude == null`): return `null`.

---

## 6. Remote Synchronization Engine

### 6.1 Sync Protocol Sequence
```
┌───────────────────────────┐          ┌───────────────────────────┐
│     Robithoh App (KMP)    │          │     Robithoh CMS API      │
└─────────────┬─────────────┘          └─────────────┬─────────────┘
              │                                      │
              │ 1. GET /api/v1/sync/version-manifest │
              ├─────────────────────────────────────►│
              │                                      │
              │ 2. Response { waktal: { v: 4 } }     │
              │◄─────────────────────────────────────┤
              │                                      │
              ├─ Check: Local v(3) < Remote v(4)     │
              │                                      │
              │ 3. GET /api/v1/sync/delta/waktal     │
              ├─────────────────────────────────────►│
              │                                      │
              │ 4. Response: Full Snapshot Array     │
              │◄─────────────────────────────────────┤
              │                                      │
              ├─ 5. SQLite batch transaction         │
              ├─ 6. Save new local version_code = 4  │
              │                                      │
```

### 6.2 State Machine (`WaktalSyncState`)
```kotlin
sealed interface WaktalSyncState {
    data object Idle : WaktalSyncState
    data class Checking(val message: String) : WaktalSyncState
    data class Syncing(val current: Int, val total: Int) : WaktalSyncState
    data class Success(val updatedCount: Int) : WaktalSyncState
    data class Error(val message: String) : WaktalSyncState
}
```

---

## 7. MVI Presentation Contract

### 7.1 Waktal List Contract
```kotlin
data class WaktalListUiState(
    val searchQuery: String = "",
    val selectedStatus: WaktalStatus = WaktalStatus.SEMUA,
    val selectedProvince: String? = null,
    val isSortByDistance: Boolean = false,
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
    val availableProvinces: List<String> = emptyList(),
    val items: List<WakilTalqin> = emptyList(),
    val filteredItems: List<WakilTalqin> = emptyList(),
    val isLoading: Boolean = false,
    val syncState: WaktalSyncState = WaktalSyncState.Idle
) : UiState

sealed interface WaktalListUiIntent : UiIntent {
    data class UpdateSearchQuery(val query: String) : WaktalListUiIntent
    data class SelectStatus(val status: WaktalStatus) : WaktalListUiIntent
    data class SelectProvince(val province: String?) : WaktalListUiIntent
    data class ToggleSortByDistance(val enabled: Boolean) : WaktalListUiIntent
    data class SetUserLocation(val lat: Double, val lng: Double) : WaktalListUiIntent
    data class SelectWaktal(val id: Int) : WaktalListUiIntent
    data object TriggerSync : WaktalListUiIntent
}

sealed interface WaktalListUiEffect : UiEffect {
    data class NavigateToDetail(val id: Int) : WaktalListUiEffect
    data class ShowSnackbar(val message: String) : WaktalListUiEffect
}
```

### 7.2 Waktal Detail Contract
```kotlin
data class WaktalDetailUiState(
    val waktal: WakilTalqin? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) : UiState

sealed interface WaktalDetailUiIntent : UiIntent {
    data class LoadDetail(val id: Int) : WaktalDetailUiIntent
    data object DialPhone : WaktalDetailUiIntent
    data object OpenWhatsApp : WaktalDetailUiIntent
    data object OpenMap : WaktalDetailUiIntent
}

sealed interface WaktalDetailUiEffect : UiEffect {
    data class LaunchUri(val uri: String) : WaktalDetailUiEffect
    data class ShowToast(val message: String) : WaktalDetailUiEffect
}
```

---

## 8. Verification & Test Matrix

| ID | Test Scenario | Expected Outcome | Component |
|---|---|---|---|
| **TEST-001** | Database initial seed from bundled JSON | 398+ records inserted, `getAllWakilTalqin` returns full list offline. | `WaktalRepositoryTest` |
| **TEST-002** | Search filter matching name | Query `"Sholeh"` returns KH M Sholeh Mukhtar. | `WaktalRepositoryTest` |
| **TEST-003** | Status filter (`aktif` vs `wafat`) | Filter `WAFAT` returns only records where `status == "wafat"`. | `WaktalViewModelTest` |
| **TEST-004** | Province filter | Selecting `"Jawa Barat"` returns only records located in West Java. | `WaktalViewModelTest` |
| **TEST-005** | Haversine distance accuracy | Distance between Bandung (-6.9175, 107.6191) and Jakarta (-6.1553, 106.7321) is $128 \pm 2\text{ km}$. | `HaversineDistanceTest` |
| **TEST-006** | Distance sort order | Nearest ulama appears first in `filteredItems` when `isSortByDistance = true`. | `WaktalViewModelTest` |
| **TEST-007** | Sync version matching | If remote version == local version, no delta download is requested. | `WaktalSyncManagerTest` |
| **TEST-008** | Sync delta update | If remote version > local version, updates are written transactionally. | `WaktalSyncManagerTest` |
| **TEST-009** | Detail intent generation | Phone uri matches `tel:0813...`, WhatsApp uri matches `https://wa.me/628...`. | `WaktalDetailViewModelTest` |
| **TEST-010** | Architecture compliance | Run `.agents/tools/check_mvi_architecture.sh` to ensure Screen/Content separation. | Linter CI |
