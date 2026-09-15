# Implementation Plan: Penghapusan Wirid Kemalaikatan dari Tasbih Digital

- **Terkait Spec**: [SPEC-DATA-TASBIH-001](../specs/spec-data-removal-wirid-kemalaikatan-tasbih.md)
- **Terkait ADR**: [ADR 0001](../adr/0001-removal-wirid-kemalaikatan-from-tasbih.md)
- **Estimasi Kompleksitas**: Rendah (Zero-Risk)

---

## Tahapan Eksekusi

### Fase 1: Pembaruan Remote Dokumen (OTA - Zero App Update)
Target: Perubahan langsung terasa di perangkat pengguna tanpa rilis store.

- [ ] **Langkah 1.1**: Edit `Robithoh-Docs/documents/DAFTAR_DZIKIR_TASBIH.md`
  - Hapus bagian `## Wirid Kemalaikatan: Ahad` sampai dengan baris terakhir `## Wirid Kemalaikatan: Sabtu` (baris 61-121).
- [ ] **Langkah 1.2**: Generate Ulang Manifest Remote
  - Jalankan script `python3 scripts/generate_manifest.py` di direktori `Robithoh-Docs`.
  - Verifikasi perubahan di `Robithoh-Docs/manifest.json` (hash baru & version increment).
- [ ] **Langkah 1.3**: Deploy ke Remote
  - Commit dan push perubahan di `Robithoh-Docs` ke branch `main`.
  - Verifikasi file mentah terakses di `https://raw.githubusercontent.com/iqbalwork/Robithoh-Docs/main/manifest.json`.

---

### Fase 2: Penyelarasan Codebase Aplikasi (Robithoh App)
Target: Integritas kode, fresh install baseline, dan kelulusan unit testing.

- [ ] **Langkah 2.1**: Salin Dokumen Terbaru ke Bundled Resources
  - Salin `DAFTAR_DZIKIR_TASBIH.md` terbaru ke `Robithoh App/shared/src/commonMain/composeResources/files/DAFTAR_DZIKIR_TASBIH.md`.
  - Salin `manifest.json` terbaru ke `Robithoh App/shared/src/commonMain/composeResources/files/manifest.json`.
- [ ] **Langkah 2.2**: Bersihkan `defaultTasbihPresets` di `TasbihMvi.kt`
  - Hapus 7 preset `wirid_kemalaikatan_*` dari list `defaultTasbihPresets` di `com/iqbalwork/robithoh/feature/tasbih/presentation/TasbihMvi.kt`.
- [ ] **Langkah 2.3**: Bersihkan Mapping `generatePresetId` di `MarkdownDocumentRepository.kt`
  - Hapus baris pemetaan `ahad`, `senin`, `selasa`, `rabu`, `kamis`, `jum'at`/`jumat`, `sabtu` di `generatePresetId()` jika sudah tidak digunakan.
- [ ] **Langkah 2.4**: Perbarui Unit Test di `TasbihPresetsDocumentTest.kt`
  - Ubah assertion `testDefaultTasbihPresets_containsThirteenItems()` menjadi `testDefaultTasbihPresets_containsSixItems()`.
  - Hapus assertion checking untuk `wirid_kemalaikatan_*`.
  - Jalankan `./gradlew :shared:jvmTest` untuk memastikan seluruh tes hijau (PASS).

---

## Verifikasi & Kriteria Keberhasilan (Acceptance Criteria)
1. `DAFTAR_DZIKIR_TASBIH.md` hanya berisi 6 preset dzikir utama.
2. `manifest.json` memiliki versi ter-increment dengan hash dokumen yang valid.
3. Unit test di `Robithoh App/shared` lulus tanpa error.
4. Fitur amaliyah `WIRID_KEMALAIKATAN.md` tetap utuh dan dapat dibuka secara terpisah di menu buku saku/amaliyah.
