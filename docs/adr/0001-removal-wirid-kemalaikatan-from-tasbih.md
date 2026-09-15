# ADR 0001: Penghapusan Wirid Kemalaikatan dari Fitur Tasbih Digital

- **Status**: Accepted
- **Tanggal**: 2026-09-15
- **Pengambil Keputusan**: Engineering & Content Team Robithoh
- **Domain**: Tasbih Digital, Remote Document Sync, Content Management

---

## 1. Konteks & Masalah (Context & Problem Statement)

Wirid Kemalaikatan (7 varian: Ahad s/d Sabtu) saat ini tercantum di dalam file dokumen `DAFTAR_DZIKIR_TASBIH.md` dan dijadikan sebagai preset cepat pada fitur **Tasbih Digital**.

Berdasarkan arahan konten terbaru, Wirid Kemalaikatan akan dihapus dari daftar wirid/dzikir pada fitur Tasbih Digital, namun tetap dipertahankan sebagai dokumen amaliyah mandiri (`WIRID_KEMALAIKATAN.md`). 

Tantangannya adalah bagaimana mengeksekusi perubahan ini secara instan ke pengguna akhir **tanpa mewajibkan rilis update aplikasi (APK/AAB/IPA)** melalui Google Play Store / App Store, namun tetap menjaga kebersihan kode dan integritas unit testing pada repository aplikasi.

---

## 2. Pilihan Solusi yang Dipertimbangkan (Options Considered)

### Opsi A: Update Melalui Rilis Aplikasi Baru Saja (Store Release Only)
- Mengubah `defaultTasbihPresets` di kode Kotlin dan memperbarui file bundled asset di dalam APK.
- **Kekurangan**: Membutuhkan proses build, testing, rilis ke store, menunggu review Google Play/App Store, dan pengguna baru akan mendapatkan perubahan setelah mereka meng-update aplikasinya secara manual/otomatis.

### Opsi B: Update Dokumen Remote Saja Tanpa Memperbarui Kode App
- Hanya mengedit `DAFTAR_DZIKIR_TASBIH.md` di repo `Robithoh-Docs` dan generate manifest baru.
- **Kekurangan**: Pengguna aktif langsung ter-update via OTA, namun pada fresh install (offline) atau fallback jika load gagal, item kemalaikatan masih muncul dari hardcoded `defaultTasbihPresets`. Unit test di CI juga berisiko tidak sinkron.

### Opsi C: Strategi Dual-Phase (OTA Remote Sync + In-Repo Codebase Alignment) [DIPILIH]
- **Fase 1 (Zero App Update / Instan)**: Perbarui `DAFTAR_DZIKIR_TASBIH.md` di repositori `Robithoh-Docs`, jalankan `generate_manifest.py`, lalu push ke remote `main`. Aplikasi aktif pengguna akan mengunduh dokumen secara OTA melalui `DocumentSyncManager` saat aplikasi dibuka, langsung merefleksikan daftar dzikir terbaru tanpa update aplikasi.
- **Fase 2 (In-Repo Code & Asset Alignment)**: Perbarui bundled asset dan sesuaikan `defaultTasbihPresets` di `TasbihMvi.kt` serta unit test di `TasbihPresetsDocumentTest.kt`. Perubahan ini akan masuk ke rilis aplikasi versi reguler berikutnya sebagai default baseline yang bersih.

---

## 3. Keputusan (Decision Outcome)

Kami memutuskan untuk mengadopsi **Opsi C (Strategi Dual-Phase)**:

1. **Jalur Utama Distribusi (Zero-Update Rollout)**:
   Memanfaatkan infrastruktur **Dynamic Document Sync (OTA)** yang sudah ada pada `DocumentSyncManager` dan `Robithoh-Docs`. Aplikasi mengandalkan hash SHA-256 pada `manifest.json`. Ketika hash `DAFTAR_DZIKIR_TASBIH.md` berubah:
   - Client mendeteksi perubahan hash saat startup (`syncDocuments()`).
   - Client mengunduh dokumen markdown terbaru dan menyimpannya di SQLite `cached_documents`.
   - `App.kt` memicu intent `TasbihUiIntent.ReloadPresets`.
   - `TasbihViewModel` mem-parsing ulang preset dari dokumen ter-update.

2. **Lingkup Dokumen Amaliyah Mandiri**:
   Dokumen `WIRID_KEMALAIKATAN.md` **TIDAK DIHAPUS** dari amaliyah/buku saku. Dokumen tersebut tetap tersedia bagi ikhwan/akhwat yang membacanya sebagai amaliyah mingguan. Yang dihapus secara eksklusif hanyalah peruntukannya di Tasbih Digital.

---

## 4. Konsekuensi & Mitigasi (Consequences & Trade-offs)

### Positif:
- **Instant Rollout**: Pengguna aktif langsung merasakan perubahan tanpa perlu menunggu review rilis Play Store.
- **Zero Downtime / Zero Crash**: Parser `parseMarkdownToTasbihPresets` bersifat dinamis berbasis heading markdown, sehingga pengurangan item tidak menyebabkan NPE/crash.
- **Backward Compatibility**: Pengguna yang saat ini sedang berada di tengah sesi tasbih dengan preset kemalaikatan tetap dapat menyelesaikan sesinya tanpa gangguan; preset tersebut hanya tidak lagi muncul di menu pilihan baru.

### Negatif & Mitigasi:
- **Pengguna Baru (Fresh Install Offline)**: Jika pengguna baru menginstall aplikasi versi saat ini dan membukanya tanpa internet sama sekali, preset awal masih memuat 13 item sampai koneksi internet pertama kali didapat.
  - *Mitigasi*: Eksekusi Fase 2 pada repository aplikasi (`defaultTasbihPresets` dan bundled asset diupdate) sehingga rilis APK berikutnya sudah bersih sejak instalasi pertama.
