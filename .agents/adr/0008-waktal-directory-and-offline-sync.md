# ADR-0008: Waktal (Wakil Talqin) Directory & Offline-First Sync Architecture

*   **Status**: Accepted
*   **Date**: 2026-09-13
*   **Deciders**: Antigravity, Iqbal Fauzi
*   **Consulted**: Core Mobile Team, Robithoh Backend & CMS Team
*   **Informed**: QA Team, TQN Liturgy Custodians

---

## 1. Context and Problem Statement

Majlis Pembina Tarbiyyah Islamiyyah (MPTI) dan Tarekat Qodiriyyah Naqsyabandiyyah (TQN) Pondok Pesantren Suryalaya - Sirnarasa memiliki direktori resmi ulama **Wakil Talqin (Waktal)**—tokoh-tokoh yang diberikan amanah dan mandat khusus oleh Pangersa Abah Sepuh dan Pangersa Abah Anom untuk membimbing pengamalan dzikir talqin. Direktori ini mencakup 399 ulama (baik yang masih sugeng/aktif maupun yang sudah wafat/almarhum) yang telah lengkap disinkronkan dari Robithoh-CMS.

Aplikasi Robithoh memerlukan modul direktori Wakil Talqin agar ikhwan/akhwat dapat:
1. Menemukan ulama Wakil Talqin di sekitar domisili mereka (berdasarkan jarak terdekat / GPS).
2. Melakukan pencarian multi-parameter (nama, gelar, majlis binaan, provinsi, kabupaten/kota).
3. Memfilter berdasarkan status keaktifan (`aktif` / `wafat`) dan wilayah.
4. Melihat profil lengkap, biografi singkat, riwayat penugasan, foto portrait, serta kontak resmi.
5. Melakukan aksi cepat: panggilan telepon langsung (`tel:`), perpesanan WhatsApp resmi (`https://wa.me/`), serta navigasi lokasi sekretariat/majlis via Google Maps / Apple Maps.
6. Melakukan pembaruan data secara fleksibel via gestur **Swipe-to-Refresh** (Pull-to-Refresh) langsung pada layar direktori Waktal.

### Constraints & Challenges
* **Core Rule 1 (100% Offline-First Architecture)**: Robithoh App tidak boleh bergantung pada koneksi internet untuk membaca direktori. Pengguna di daerah pelosok atau saat offline harus tetap bisa mencari dan membaca profil Waktal lengkap beserta penghitungan jarak terdekat.
* **Dynamic CMS Updates**: Data Wakil Talqin dapat bertambah atau diperbarui oleh pengurus melalui Robithoh CMS (`API_CONTRACT_WAKTAL.md`).
* **Performance**: Pencarian teks dan penghitungan jarak 399 data harus instan tanpa freeze (frame budget < 16.6 ms).

---

## 2. Decision

Kami memutuskan untuk mengimplementasikan modul Wakil Talqin dengan arsitektur **Hybrid Offline-First Sync** dan **Compose Multiplatform MVI**:

### 2.1 100% Pre-Bundled Offline Baseline + SQLite Storage
1. Data awal 399 records dikemas dalam berkas JSON seed pre-bundled (`waktal_seed.json`) di `composeResources` diekstrak dari Robithoh-CMS (`wakil_talqins_seed.json`).
2. Pada instalasi pertama (first run) atau saat tabel lokal kosong, database SQLite lokal (`RobithohDatabase.sq` via CashApp SQLDelight) otomatis melakukan seeding ke dalam tabel `WakilTalqinEntity`.
3. Seluruh pencarian, pemfilteran provinsi/kota, dan sorting dilakukan langsung terhadap basis data SQLite lokal atau in-memory filtered flow, memastikan ketersediaan 100% tanpa jaringan.

### 2.2 Remote Synchronization Engine (`WaktalSyncManager`) & Swipe-to-Refresh UI
1. Sinkronisasi data menggunakan endpoint CMS sesuai `API_CONTRACT_WAKTAL.md`:
   - `GET /api/v1/sync/version-manifest`: Memeriksa apakah `version_code` server lebih baru daripada `version_code` lokal di `WaktalSyncManifestEntity`.
   - `GET /api/v1/sync/delta/waktal`: Jika ada pembaruan versi, mengunduh full snapshot data Waktal terbaru.
2. Proses pembaruan ke SQLite dijalankan dalam database transaction tunggal (`batch insert/replace`) untuk menjamin integritas data (ACID).
3. Siklus sinkronisasi dipicu secara alami via gestur **Swipe-to-Refresh** (`PullToRefreshBox`) pada halaman direktori Waktal, menggantikan tombol ikon refresh pada top app bar untuk tampilan antarmuka yang lebih bersih (*clean UI*).

### 2.3 On-Device Proximity Calculation (Client-Side Haversine)
1. Perhitungan jarak ulama terhadap posisi perangkat pengguna dilakukan secara lokal di perangkat menggunakan formula **Spherical Haversine**.
2. Koordinat pengguna diambil dari abstraksi multiplatform `LocationProvider` yang sudah ada di Robithoh App.
3. Sorting jarak terdekat dapat dinikmati pengguna secara offline tanpa perlu memanggil endpoint server yang membutuhkan koordinat GPS.

### 2.4 MVI Presentation & Screen/Content Separation
1. Mengikuti konvensi ketat Robithoh MVI:
   - `WaktalListScreen` (stateful, ViewModel-connected) $\leftrightarrow$ `WaktalListContent` (stateless, pure previewable UI).
   - `WaktalDetailScreen` $\leftrightarrow$ `WaktalDetailContent`.
2. Sub-komponen visual (Card ulama, Filter chips, Status badge, Quick action buttons) diletakkan pada sub-package `feature/waktal/ui/components/`.
3. Seluruh teks antarmuka menggunakan resource multiplatform (`Res.string.*`) tanpa string hardcoded.

### 2.5 Image Caching & Fallback Placeholder
1. Menampilkan foto portrait ulama dari URL CDN dengan placeholder offline fallback (vektor siluet kaligrafi / logo Robithoh resmi) jika perangkat sedang offline atau URL gambar belum ter-cache.

### 2.6 Native External Actions
1. Panggilan Telepon: Membuka dialer via platform intent uri `tel:{nomor}`.
2. WhatsApp: Membuka aplikasi WhatsApp via link universal `https://wa.me/{nomor_wa}`.
3. Peta Navigasi: Membuka Google Maps / Apple Maps dengan koordinat `geo:{lat},{lng}?q={lat},{lng}({nama})`.

---

## 3. Rationale

1. **Mematuhi Core Rule 1**: Robithoh App adalah aplikasi ibadah dan panduan thoriqoh yang harus selalu dapat diandalkan dalam kondisi tanpa sinyal. Menyimpan seluruh katalog di SQLite lokal adalah keharusan mutlak.
2. **Efisiensi Jaringan & Hemat Kuota**: Dengan version manifest checking, aplikasi hanya mengunduh data jika benar-benar ada revisi dari pusat CMS. Ukuran data 400 record JSON terkompresi hanya ~40-60 KB, sangat ringan.
3. **Kemandirian Perhitungan Jarak**: Menghitung jarak via Haversine di client jauh lebih cepat (< 2 ms untuk 400 item) dibanding round-trip network request ke backend.
4. **Konsistensi Arsitektur**: Struktur `WaktalSyncManager` selaras dengan `DocumentSyncManager` yang sudah sukses menangani sinkronisasi 48 naskah liturgi amaliyah di aplikasi.

---

## 4. Consequences

### Good
* **Instan & Responsif**: Pengguna dapat mencari nama ulama dan menyaring wilayah seketika (0 ms latency).
* **Zero Downtime**: Gangguan server CMS atau ketiadaan kuota pengguna tidak menghentikan fungsi direktori.
* **Navigasi Terarah**: Mempermudah jamaah menghubungi atau mendatangi madrasah/zawiyah Wakil Talqin terdekat saat safar atau ziarah.
* **Type-Safe**: Seluruh entitas dan DTO terverifikasi type-safe melalui Kotlinx Serialization dan SQLDelight.

### Bad
* **Ukuran APK/IPA**: Ada sedikit penambahan ukuran bundle app (~120 KB untuk seed data JSON dan ikon).
* **Gambar Butuh Jaringan Awal**: Foto portrait ulama resolusi tinggi tidak dapat dibundel seluruhnya ke dalam offline asset (keterbatasan ukuran binary app), sehingga memerlukan koneksi internet sekali untuk caching pertama.

### Neutral
* Perubahan skema pada `RobithohDatabase.sq` memerlukan update baseline SHA-256 pada `.agents/rules/sacred_texts_hashes.json` melalui skrip `.agents/tools/check_sacred_texts.sh --update`.

---

## 5. References

* Kontrak API CMS: `/Users/iqbalfauzi/Personal/App/Robithoh-CMS/docs/API_CONTRACT_WAKTAL.md`
* Spesifikasi Teknis: [`.agents/spec/0002-waktal-directory-and-sync-engine.md`](../spec/0002-waktal-directory-and-sync-engine.md)
* [ADR-0001: 100% Offline-First Architecture via SQLDelight & Bundled Markdown](0001-offline-first-sqlite-and-markdown.md)
* [ADR-0002: Compose Multiplatform & MVI Presentation Architecture](0002-cmp-mvi-presentation-architecture.md)
* [ADR-0005: System Notifications for Liturgical Document Sync Progress](0005-document-sync-notifications.md)
