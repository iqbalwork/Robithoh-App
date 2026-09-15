# Spesifikasi Teknis: Penghapusan Wirid Kemalaikatan dari Tasbih Digital

- **ID Dokumen**: `SPEC-DATA-TASBIH-001`
- **Status**: Draft / Ready for Implementation
- **Versi**: 1.0.0
- **Terkait ADR**: [ADR 0001: Penghapusan Wirid Kemalaikatan dari Fitur Tasbih Digital](../adr/0001-removal-wirid-kemalaikatan-from-tasbih.md)

---

## 1. Ringkasan Eksekutif (Executive Summary)
Spesifikasi ini mengatur tata cara penghapusan 7 varian Wirid Kemalaikatan (Ahad sampai Sabtu) dari fitur Tasbih Digital. Fitur Tasbih Digital setelah perubahan ini hanya akan memuat 6 preset standar. Dokumen amaliyah mandiri `WIRID_KEMALAIKATAN.md` tetap dipertahankan pada menu Buku Saku/Amaliyah.

---

## 2. Definisi Kebutuhan (Requirements)

### 2.1 Kebutuhan Fungsional (Functional Requirements)
1. **Pembaruan Dokumen Markdown (`DAFTAR_DZIKIR_TASBIH.md`)**:
   - Seluruh blok teks mulai dari `## Wirid Kemalaikatan: Ahad` hingga baris terakhir `## Wirid Kemalaikatan: Sabtu` (beserta teks Arab, jumlah target, dan penjelasannya) harus dihapus dari:
     - `Robithoh-Docs/documents/DAFTAR_DZIKIR_TASBIH.md`
     - `Robithoh App/shared/src/commonMain/composeResources/files/DAFTAR_DZIKIR_TASBIH.md`
   - Daftar dzikir tasbih yang sah setelah perubahan hanya terdiri atas 6 entri berikut:
     1. **Tahlil** (165x)
     2. **Tasbih & Tahmid** (33x)
     3. **Hauqolah** (165x)
     4. **Shalawat Munjiyat** (11x)
     5. **Istighfar** (165x)
     6. **Shalawat Bani Hasyim** (165x)

2. **Integritas Manifest Remote (`manifest.json`)**:
   - Menjalankan `scripts/generate_manifest.py` pada repository `Robithoh-Docs`.
   - Manifest baru harus merefleksikan:
     - Checksum SHA-256 baru untuk `DAFTAR_DZIKIR_TASBIH.md`.
     - File size baru untuk `DAFTAR_DZIKIR_TASBIH.md`.
     - `version` dinaikkan secara otomatis (+1).
     - Timestamp `updatedAt` terbaru dalam format ISO-8601 UTC.

3. **Konsistensi Kode Aplikasi (Codebase Consistency)**:
   - Variabel `defaultTasbihPresets` di `TasbihMvi.kt` harus disesuaikan menjadi 6 preset inti saja (menghilangkan 7 preset kemalaikatan).
   - Pemetaan di `generatePresetId` dalam `MarkdownDocumentRepository.kt` dapat dibersihkan dari pemetaan hari kemalaikatan.
   - Preservasi dokumen `WIRID_KEMALAIKATAN.md` pada daftar dokumen utama di `MarkdownDocumentRepository.kt` (jangan dihapus dari repository amaliyah).

### 2.2 Kebutuhan Non-Fungsional (Non-Functional Requirements)
1. **Zero Downtime / Zero Crash**:
   - Pembaruan dokumen tidak boleh memicu NullPointerException atau error deserialization di client yang sedang berjalan.
2. **Backward Compatibility**:
   - Jika pengguna sedang membuka sesi tasbih dengan preset kemalaikatan sebelum sync, state saat itu tetap berjalan normal. Begitu pengguna memilih dzikir lain atau restart, daftar pilihan hanya menyajikan preset yang valid.
3. **Verifiability**:
   - Semua unit test (`TasbihPresetsDocumentTest`) harus lulus (`PASSED`).

---

## 3. Struktur Perubahan Data (Data Contract & Diffs)

### 3.1 `DAFTAR_DZIKIR_TASBIH.md`
```markdown
# Daftar Wirid & Dzikir Tasbih

Koleksi wirid dan dzikir yang sering dibaca beserta keutamaan dan target bilangan dzikirnya.

---

## Tahlil
(165x)

لَا إِلٰهَ إِلَّا اللَّهُ

*Pengagungan kebesaran Allah di atas seluruh alam.*

---

## Tasbih & Tahmid
(33x)

سُبْحَانَ اللَّهِ وَبِحَمْدِهِ سُبْحَانَ اللَّهِ الْعَظِيمِ

*Dua kalimat yang ringan di lisan, berat di timbangan, dicintai ar-Rahman.*

---

## Hauqolah
(165x)

لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ

*Simpanan perbendaharaan surga dan penawar berbagai macam duka cita.*

---

## Shalawat Munjiyat
(11x)

اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ صَلَاةً تُنْجِينَا بِهَا مِنْ جَمِيعِ الْأَهْوَالِ وَالْآفَاتِ وَتَقْضِي لَنَا بِهَا جَمِيعَ الْحَاجَاتِ وَتُطَهِّرُنَا بِهَا مِنْ جَمِيعِ السَّيِّئَاتِ وَتَرْفَعُنَا بِهَا عِنْدَكَ أَعْلَى الدَّرَجَاتِ وَتُبَلِّغُنَا بِهَا أَقْصَى الْغَايَاتِ مِنْ جَمِيعِ الْخَيْرَاتِ فِي الْحَيَاةِ وَبَعْدَ الْمَمَاتِ

*Shalawat penyelamat dari mara bahaya dan permohonan segala hajat besar.*

---

## Istighfar
(165x)

أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ

*Pelebur noda dosa dan pembuka pintu ampunan Ilahi.*

---

## Shalawat Bani Hasyim
(165x)

اللَّهُمَّ صَلِّ عَلَى النَّبِيِّ الْهَاشِمِيِّ مُحَمَّدٍ وَعَلَى آلِهِ وَسَلِّمْ تَسْلِيمًا

*Shalawat kebanggaan ikhwan dan akhwat pembuka futuh dan mahabbah Rasulullah.*
```

---

## 4. Rencana Pengujian (Test Strategy)
1. **Unit Test di Client (`shared:testDebugUnitTest` / `shared:jvmTest`)**:
   - Memastikan `defaultTasbihPresets.size == 6`.
   - Memastikan parser `parseMarkdownToTasbihPresets` menghasilkan 6 preset dari dokumen baru.
2. **End-to-End OTA Simulation**:
   - Memastikan `DocumentSyncManager` berhasil membaca manifest baru, mendeteksi perbedaan hash, mengunduh file baru, dan memicu `TasbihUiIntent.ReloadPresets`.
