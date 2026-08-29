# 📢 Dokumen Spesifikasi & Rencana Implementasi: Fitur Maklumat (Pengumuman Resmi Pesantren)
## Arsitektur Dynamic REST API & Offline-First Client untuk Robithoh App (KMP & CMP)

Dokumen ini merinci rancangan teknis, spesifikasi kontrak REST API, skema basis data lokal, dan alur UI/UX untuk **Fitur Maklumat** pada **Robithoh App**. Fitur ini memungkinkan aplikasi menampilkan maklumat/pengumuman berkala dari Pesantren Pusat (seperti maklumat PPKN, dawuh Guru Mursyid, amaliyah khusus, dan instruksi ibadah) secara dinamis, terurut dari yang terbaru (*latest first*), dengan dukungan penuh mode luring (*Offline-First*).

---

## 📌 Daftar Isi
1. [🎯 Tujuan & Ruang Lingkup](#1--tujuan--ruang-lingkup)
2. [🌐 Spesifikasi Kontrak REST API](#2--spesifikasi-kontrak-rest-api)
   - [Model & Schema Data](#model--schema-data)
   - [Contoh JSON Response (Sesuai Screenshot)](#contoh-json-response-sesuai-screenshot)
   - [Daftar Endpoint API](#daftar-endpoint-api)
3. [🏗️ Arsitektur Data Client (Offline-First KMP)](#3-️-arsitektur-data-client-offline-first-kmp)
   - [Alur Sinkronisasi Data (Network-Bound Resource)](#alur-sinkronisasi-data-network-bound-resource)
   - [Skema Basis Data SQLDelight](#skema-basis-data-sqldelight)
4. [🎨 Desain UI / UX & Flow Navigasi](#4--desain-ui--ux--flow-navigasi)
   - [Titik Masuk (Home Grid & Banner)](#a-titik-masuk-home-grid--banner)
   - [Halaman Daftar Maklumat (List Screen)](#b-halaman-daftar-maklumat-list-screen)
   - [Halaman Baca Maklumat (Detail Screen)](#c-halaman-baca-maklumat-detail-screen)
5. [🛠️ Struktur Kode & Komponen Project](#5-️-struktur-kode--komponen-project)
6. [🚀 Tahapan Rencana Kerja (Implementation Roadmap)](#6--tahapan-rencana-kerja-implementation-roadmap)

---

## 1. 🎯 Tujuan & Ruang Lingkup

1. **Pembaruan Dinamis Tanpa Rilis Ulang APK/IPA**: Admin pesantren dapat menerbitkan maklumat baru melalui backend/API, dan aplikasi pengguna akan otomatis menerimanya saat terhubung ke internet.
2. **Ketahanan Offline (100% Offline-First)**: Maklumat yang pernah diunduh tersimpan ke basis data lokal SQLDelight sehingga pengguna tetap dapat membaca teks maklumat dan amalan doa kapan saja tanpa sinyal.
3. **Tipografi Khidmat Sesuai Karakteristik Amaliyah**:
   - Teks Arab menggunakan font khas (*Scheherazade / font mushaf*) lengkap dengan harakat.
   - Transliterasi Latin yang presisi (*Allohumma ahyinī miskīnan...*).
   - Petunjuk waktu dan amalan yang terstruktur.
4. **Fitur Berbagi Cepat (Fast Sharing)**: Memudahkan jamaah menyebarkan maklumat resmi ke grup WhatsApp/media sosial dengan format teks rapi beserta atribusi resmi.

---

## 2. 🌐 Spesifikasi Kontrak REST API

### Model & Schema Data

| Field | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id` | `String` | Identifier unik (e.g. `"maklumat-20260725-ppkn3"`). |
| `title` | `String` | Judul utama maklumat. |
| `timestamp` | `Long` | Waktu publikasi dalam Epoch Milliseconds (kunci utama sorting *descending*). |
| `date_masehi` | `String` | Format tanggal Masehi yang ramah dibaca (e.g. `"Sabtu, 25 Juli 2026"`). |
| `date_hijri` | `String` | Format tanggal Hijriyah (e.g. `"10 Romadhon / 6 Shofar 1448 H"`). |
| `category` | `String` | Kategori maklumat (e.g. `"PPKN"`, `"Amaliyah"`, `"Dawuh Mursyid"`, `"Umum"`). |
| `sender` | `String` | Pengirim / Penandatangan (e.g. `"AL-AMIIN, Pembantu Khusus PPKN III"`). |
| `summary` | `String` | Ringkasan pendek (1–2 kalimat) untuk preview di halaman daftar. |
| `content_markdown` | `String` | Isi maklumat lengkap dalam format Markdown (mendukung teks tebal, miring, paragraf, list). |
| `doa_block` | `Object?` | *(Opsional)* Blok doa khusus jika maklumat memuat amaliyah bacaan doa. |
| ↳ `doa_block.arabic` | `String` | Teks doa dalam Bahasa Arab bersyakal lengkap. |
| ↳ `doa_block.latin` | `String` | Transliterasi teks Latin doa. |
| ↳ `doa_block.translation` | `String?` | Arti / terjemahan doa (jika ada). |
| ↳ `doa_block.instruction` | `String?` | Petunjuk waktu pengamalan (e.g. `"Dibaca tiap habis sholat..."`). |
| `is_pinned` | `Boolean` | `true` jika maklumat darurat/krusial yang harus tetap di posisi teratas. |
| `is_urgent` | `Boolean` | `true` untuk menandai badge peringatan merah di UI. |
| `share_url` | `String?` | *(Opsional)* Tautan web resmi jika ada lampiran/portal. |

---

### Contoh JSON Response (Sesuai Screenshot)

#### `GET /api/v1/maklumat`
```json
{
  "status": "success",
  "data": [
    {
      "id": "maklumat-20260725-ppkn3",
      "title": "MAKLUMAT Penghulu Pesantren KETAHANAN NASIONAL III",
      "timestamp": 1784955600000,
      "date_masehi": "Sabtu, 25 Juli 2026",
      "date_hijri": "10 Romadhon / 6 Shofar 1448 H",
      "category": "PPKN",
      "sender": "AL-AMIIN, Pembantu Khusus PPKN III",
      "summary": "Maklumat berita penting dari Panglima Besar Perang Dunia Ketiga untuk membaca do'a khusus tiap habis sholat.",
      "content_markdown": "السلام عليكم ورحمة الله وبركاته\n\nBerikut ini **MAKLUMAT** berita penting dari Panglima Besar Perang Dunia Ketiga untuk para Pecinta Kesucian Jiwa di seluruh dunia,\n\nMulai tadi pukul 1, 2, 3 dan 4 sebagai bukti ke dunia kita pemenang dalam Perang Dunia Ketiga sekarang kita mesti membaca do'a:\n\nDemikian MAKLUMAT PPKN III disampaikan untuk menjadi pedoman.\n\nSalam Khidmah,\n**AL-AMIIN, Pembantu Khusus PPKN III**",
      "doa_block": {
        "arabic": "اَللّٰهُمَّ اَحْيِنِيْ مِسْكِيْنًا وَاَمِتْنِيْ مِسْكِيْنًا وَاحْشُرْنِيْ فِيْ زُمْرَةِ الْمَسَاكِيْنِ",
        "latin": "Allohumma aḥyinī miskīnan wa amitnī miskīnan waḥshurnī fī zumratil-masākīn.",
        "translation": "Ya Allah, hidupkanlah aku dalam keadaan miskin, matikanlah aku dalam keadaan miskin, dan kumpulkanlah aku bersama golongan orang-orang miskin.",
        "instruction": "Dibaca tiap habis sholat, disambung dari do'a sebelumnya: اجرا عظيما..."
      },
      "is_pinned": true,
      "is_urgent": false,
      "share_url": "https://facebook.com/..."
    }
  ],
  "meta": {
    "total": 1,
    "page": 1,
    "last_updated": "2026-08-29T12:20:00Z"
  }
}
```

---

### Daftar Endpoint API

1. **`GET /api/v1/maklumat`**
   - **Query Parameters**:
     - `page`: nomor halaman (default: `1`)
     - `limit`: jumlah data per request (default: `20`)
     - `category`: filter kategori (opsional)
   - **Response**: Mengembalikan daftar maklumat terurut *timestamp DESC* (dengan item `is_pinned: true` diprioritaskan di atas).
2. **`GET /api/v1/maklumat/{id}`**
   - **Response**: Mengembalikan detail lengkap 1 maklumat tertentu.

---

## 3. 🏗️ Arsitektur Data Client (Offline-First KMP)

### Alur Sinkronisasi Data (Network-Bound Resource)

```
┌──────────────────────────────────────────────────────────────┐
│                    Pengguna Buka Menu Maklumat               │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│  1. Load data dari SQLDelight Lokal (Instant Render di UI)   │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│  2. Background Request ke REST API (`GET /api/v1/maklumat`)  │
└──────────────────────────────┬───────────────────────────────┘
                               │
               ┌───────────────┴───────────────┐
             SUKSES                          GAGAL / OFFLINE
               │                               │
               ▼                               ▼
┌─────────────────────────────┐  ┌─────────────────────────────┐
│ 3. Simpan / Update data baru│  │ Tampilkan toast / snackbar  │
│    ke SQLDelight            │  │ "Mode Offline - Menggunakan │
│ 4. Emit Flow ke UI          │  │ data cache tersimpan"       │
└─────────────────────────────┘  └─────────────────────────────┘
```

### Skema Basis Data SQLDelight

Tambahkan file tabel baru di `shared/src/commonMain/sqldelight/.../MaklumatEntity.sq`:

```sql
CREATE TABLE MaklumatEntity (
    id TEXT PRIMARY KEY NOT NULL,
    title TEXT NOT NULL,
    timestamp INTEGER NOT NULL,
    dateMasehi TEXT NOT NULL,
    dateHijri TEXT NOT NULL,
    category TEXT NOT NULL,
    sender TEXT NOT NULL,
    summary TEXT NOT NULL,
    contentMarkdown TEXT NOT NULL,
    arabicText TEXT,
    latinText TEXT,
    translation TEXT,
    instruction TEXT,
    isPinned INTEGER AS Boolean NOT NULL DEFAULT 0,
    isUrgent INTEGER AS Boolean NOT NULL DEFAULT 0,
    shareUrl TEXT,
    isRead INTEGER AS Boolean NOT NULL DEFAULT 0,
    cachedAt INTEGER NOT NULL
);

-- Queries
selectAllMaklumat:
SELECT * FROM MaklumatEntity
ORDER BY isPinned DESC, timestamp DESC;

selectMaklumatById:
SELECT * FROM MaklumatEntity
WHERE id = :id;

insertOrReplaceMaklumat:
INSERT OR REPLACE INTO MaklumatEntity(
    id, title, timestamp, dateMasehi, dateHijri, category, sender,
    summary, contentMarkdown, arabicText, latinText, translation,
    instruction, isPinned, isUrgent, shareUrl, isRead, cachedAt
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

markAsRead:
UPDATE MaklumatEntity SET isRead = 1 WHERE id = :id;
```

---

## 4. 🎨 Desain UI / UX & Flow Navigasi

### A. Titik Masuk (Home Grid & Banner)
1. **Home Grid Menu**:
   - Menambahkan ikon menu **"Maklumat"** (Ikon: 📢 atau 📜) pada grid utama beranda (`HomeTabContent.kt`).
   - Terdapat badge titik merah (*indicator dot*) jika terdapat maklumat baru yang belum dibaca (`isRead == false`).
2. **Top Announcement Banner (Beranda)**:
   - Jika ada maklumat berstatus `is_pinned: true` atau diterbitkan dalam 3 hari terakhir, tampilkan card ringkas di atas jadwal sholat / mutiara tanbih.

### B. Halaman Daftar Maklumat (List Screen)
```
┌─────────────────────────────────────────────────────────────┐
│ ←  Maklumat & Pengumuman                          [🔍] [🔄] │
├─────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────────────────────────┐ │
│ │ 📌 PINNED • PPKN             Sabtu, 25 Jul 2026 / 10 Rom │ │
│ │ MAKLUMAT Penghulu Pesantren KETAHANAN NASIONAL III       │ │
│ │                                                         │ │
│ │ Berikut ini MAKLUMAT berita penting dari Panglima Besar │ │
│ │ Perang Dunia Ketiga untuk membaca do'a khusus...        │ │
│ │                                                         │ │
│ │ Dari: AL-AMIIN, Pembantu Khusus PPKN III    [Baca Detail]│ │
│ └─────────────────────────────────────────────────────────┘ │
│                                                             │
│ ┌─────────────────────────────────────────────────────────┐ │
│ │ AMALIYAH                     Kamis, 15 Jan 2026 / 25 Raj │ │
│ │ Maklumat Pelaksanaan Manaqib Kubro Bulan Rajab          │ │
│ │ ...                                                     │ │
│ └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### C. Halaman Baca Maklumat (Detail Screen)
* **Header**: Judul maklumat, tanggal Masehi & Hijriah, pengirim resmi.
* **Badges**: Kategori & status maklumat.
* **Isi Maklumat (Body)**:
  * Teks pengantar dan penutup dirender rapi.
  * **Kotak Khusus Doa / Amalan**: Card dengan background khusus bernuansa khidmat (`PaperBackgroundLight` & aksen emas/marun):
    * Teks Arab berukuran besar dan terbaca jelas (Font *Scheherazade*).
    * Transliterasi Latin miring (*italic*).
    * Arti/terjemahan doa.
    * Petunjuk pengamalan (misal: *"Dibaca tiap habis sholat..."*).
* **Bottom Action Bar**:
  * Tombol **Bagikan (Share ke WhatsApp)**: Mengenerate teks ringkas + format lengkap siap kirim.
  * Tombol **Salin Teks (Copy)**.
  * Pengatur ukuran teks (*Font Size - / +*).

---

## 5. 🛠️ Struktur Kode & Komponen Project

Komponen baru akan ditempatkan di dalam modul `shared` dengan struktur Clean Architecture & MVI:

```
shared/src/commonMain/kotlin/com/iqbalwork/robithoh/
├── feature/
│   └── maklumat/
│       ├── data/
│       │   ├── dto/
│       │   │   └── MaklumatDto.kt         # Model serialisasi Ktor / kotlinx.serialization
│       │   ├── remote/
│       │   │   └── MaklumatApiClient.kt   # Interface & Ktor client implementation
│       │   └── repository/
│       │       └── MaklumatRepositoryImpl.kt
│       ├── domain/
│       │   ├── model/
│       │   │   └── Maklumat.kt            # Clean Domain Model
│       │   └── repository/
│       │       └── MaklumatRepository.kt  # Interface domain repository
│       ├── presentation/
│       │   ├── MaklumatListState.kt       # UI State (Loading, Success, Error, Cache)
│       │   ├── MaklumatDetailState.kt
│       │   └── MaklumatViewModel.kt       # MVI ViewModel
│       └── ui/
│           ├── MaklumatListScreen.kt      # Screen daftar dengan Pull-to-Refresh
│           ├── MaklumatDetailScreen.kt    # Screen baca detail & amalan doa
│           └── components/
│               ├── MaklumatCardItem.kt
│               └── DoaCalloutCard.kt
├── navigation/
│   └── ScreenKey.kt                       # Tambah ScreenKey.MaklumatList & MaklumatDetail
```

---

## 6. 🚀 Tahapan Rencana Kerja (Implementation Roadmap)

### Fase 1: Persiapan API Backend
1. Menentukan hosting endpoint API (bisa menggunakan Go/Node.js REST API, Supabase, Cloudflare Workers, Firebase, atau static JSON host).
2. Menyusun endpoint `GET /api/v1/maklumat` sesuai skema JSON di dokumen ini.

### Fase 2: Integrasi Client Data & Database
1. Menambahkan dependencies Ktor Client (`ktor-client-core`, `ktor-client-content-negotiation`, `ktor-serialization-kotlinx-json`, engine Android & Darwin) jika belum ada.
2. Membuat tabel `MaklumatEntity.sq` pada SQLDelight.
3. Mengimplementasikan `MaklumatRepository` dengan strategi *Network-Bound Resource* (Cache first -> Network fetch -> Update cache).

### Fase 3: Pembuatan UI & Komponen
1. Membangun `MaklumatListScreen` dengan dukungan search, filter kategori, badge status, dan pull-to-refresh.
2. Membangun `MaklumatDetailScreen` lengkap dengan `DoaCalloutCard`, font switcher, dan fungsi bagikan (*share text*).

### Fase 4: Integrasi Navigasi & Home Screen
1. Menambahkan item menu `Maklumat` di `HomeTabContent.kt` dan mendaftarkan rute navigasi di `App.kt`.
2. Menghubungkan badge notifikasi maklumat baru di beranda.

### Fase 5: Pengujian & Rilis
1. Verifikasi integrasi API online dan uji keandalan saat kondisi perangkat luring (*airplane mode*).
2. Validasi render teks Arab dan transliterasi pada berbagai ukuran layar Android & iOS.
