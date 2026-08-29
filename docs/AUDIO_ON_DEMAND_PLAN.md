# 🎵 Rencana Implementasi: Hybrid Audio Architecture (On-Demand Langgam & Embedded Adzan)
## Strategi Optimasi Ukuran Aplikasi Robithoh App via GitHub Releases

Dokumen ini merinci arsitektur **Hybrid Audio** dengan alur **Tap-to-Download, Floating Download Widget & Auto-Play** menggunakan media penyimpanan **GitHub Releases (`iqbalwork/Robithoh-App`)**:
1. **Tetap Embedded (Bawaan Aplikasi)**: **8 Berkas Audio Adzan (~24.6 MB)** tetap dikemas di dalam bundle aplikasi agar alarm notifikasi waktu sholat 100% andal, langsung berbunyi out-of-the-box tanpa ketergantungan koneksi/unduhan.
2. **On-Demand Download via GitHub Releases**: **11 Berkas Audio Langgam & Amaliyah (~47.7 MB)** diunggah ke Release Tag GitHub (`audio-v1`).
3. **Alur Interaksi Pengguna (User Flow)**:
   * **Pertama kali tap**: Aplikasi mengunduh file dari GitHub Releases.
   * **Floating Download Progress Widget**: Menampilkan widget melayang (di atas dock navigasi bawah) yang menunjukkan judul lagu, persentase unduhan (`0% - 100%`), dan indikator MB real-time. Pengguna tetap bisa bebas pindah halaman/tab tanpa membatalkan unduhan.
   * **Selesai 100%**: File tersimpan permanen di memori lokal aplikasi dan **otomatis langsung beralih ke Mini Floating Audio Player & berputar (*Auto-Play*)**.
   * **Pemutaran selanjutnya**: Langsung memutar berkas lokal seketika (*instant offline playback*), tanpa buffering dan tanpa butuh internet.

---

## 📌 Alur Pemutaran & Floating Download Widget

```
┌─────────────────────────────────────────────────────────────┐
│                 Pengguna Tap Audio Langgam                  │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
              ┌─────────────────────────────────┐
              │ File Audio Ada di Penyimpanan?  │
              └───────────────┬─────────────────┘
                              │
              ┌───────────────┴───────────────┐
             YA                               TIDAK
              │                               │
              ▼                               ▼
  ┌───────────────────────┐       ┌─────────────────────────────────────┐
  │ Langsung Putar File   │       │ Muncul Floating Download Widget     │
  │ Lokal (Instant Play,  │       │ (Melayang di atas Dock Bar bawah)   │
  │ 100% Offline)         │       │ • "Mengunduh Bani Hasyim..."        │
  └───────────────────────┘       │ • Progress: 45% (8.5 MB / 19.0 MB)  │
                                  │ • Tombol Batal (X)                  │
                                  └──────────────────┬──────────────────┘
                                                     │
                                                     ▼
                                  ┌─────────────────────────────────────┐
                                  │ Unduhan Selesai 100%                │
                                  │ (Tersimpan Permanen di Storage HP)  │
                                  └──────────────────┬──────────────────┘
                                                     │
                                                     ▼
                                  ┌─────────────────────────────────────┐
                                  │ Smooth Transition ke                │
                                  │ MINI FLOATING AUDIO PLAYER          │
                                  │ & Otomatis Berputar (Auto-Play)     │
                                  └─────────────────────────────────────┘
```

---

## 🎨 Desain Floating Download Progress Widget

Widget ini diletakkan di `MainAppContainer` (tepat di atas *Floating Pill Dock Navigation Bar*) dan memiliki animasi *slide-in / fade-in*:

```
┌──────────────────────────────────────────────────────────────────────┐
│ [⬇️ 45%]  Mengunduh Bani Hasyim...                8.5 MB / 19.0 MB [✕]│
│ ━━━━━━━━━━━━━━━━━━━━━━━───────────────────────────────────────────── │ (Progress Line)
└──────────────────────────────────────────────────────────────────────┘
```

### Karakteristik Widget:
1. **Global & Persistent**: Tetap aktif dan berjalan meskipun pengguna berpindah dari tab Langgam ke tab Sholat, Home, atau Al-Qur'an.
2. **Indikator Real-time**: Menampilkan persentase, status ukuran bytes terunduh vs total bytes.
3. **Tombol Batal**: Tombol (X) untuk membatalkan unduhan jika pengguna ingin menunda atau berpindah jaringan.
4. **Seamless Transition**: Saat unduhan selesai 100%, widget unduhan otomatis bertransisi secara halus ke `MiniFloatingAudioBar` untuk langsung memutar lagunya.

---

## 📊 Pemetaan Berkas Audio

```
                      AUDIO ROBITHOH APP (Total 72.3 MB)
                                     │
              ┌──────────────────────┴──────────────────────┐
              ▼                                             ▼
    [TETAP EMBEDDED DI APLIKASI]                [ON-DEMAND VIA GITHUB RELEASES]
        8 Berkas Audio Adzan                     11 Berkas Langgam & Amaliyah
              ~24.6 MB                                      ~47.7 MB
 ┌──────────────────────────────┐              ┌──────────────────────────────┐
 │ • Misyari Rasyid (Std/Fajr)  │              │ • Bani Hasyim (19.0 MB)      │
 │ • Mansour Zahrani (Std/Fajr) │              │ • Irama Dzikir (14.0 MB)     │
 │ • Ahmad Al-Nafees (Std/Fajr) │              │ • Tarowih (8.5 MB)           │
 │ • Mustafa Ozcan (Std/Fajr)   │              │ • Sholat Jumat (4.4 MB)      │
 │                              │              │ • Dzikir Jahr (3.1 MB)       │
 │                              │              │ • 6x Langgam Surat (~2.4 MB) │
 └──────────────────────────────┘              └──────────────────────────────┘
```

---

## 🚀 Panduan Setup GitHub Releases Storage

Repository: `https://github.com/iqbalwork/Robithoh-App`  
Release Tag: **`audio-v1`**  
Base Download URL: `https://github.com/iqbalwork/Robithoh-App/releases/download/audio-v1/`

### 11 Berkas yang Diunggah:
1. `bani_hasyim.mp3` (19.0 MB)
2. `irama_dzikir.mp3` (14.0 MB)
3. `tarowih.mp3` (8.5 MB)
4. `sholat_jumat.mp3` (4.4 MB)
5. `dzikir_jahr.mp3` (3.1 MB)
6. `al_fatihah_ad_dhuha.mpeg` (461 KB)
7. `al_fatihah_al_fill.mpeg` (442 KB)
8. `al_fatihah_al_quraisy.mpeg` (431 KB)
9. `al_fatihah_al_kafirun.mpeg` (386 KB)
10. `al_fatihah_al_insyiroh.mpeg` (368 KB)
11. `al_fatihah_an_nasr.mpeg` (347 KB)

---

## 🛠️ Tahapan Implementasi Teknis

### Fase 1: Data & Storage Module (KMP)
1. **`AudioCacheManager` (expect/actual)**:
   * Android: `context.filesDir/audio/<fileName>`
   * iOS: `NSApplicationSupportDirectory/Audio/<fileName>`
   * Metode: `isDownloaded(fileName)`, `getLocalPath(fileName)`, `deleteFile(fileName)`
2. **`LanggamDownloader`**:
   * Unduh file per-chunk menggunakan `ktor-client`.
   * State management: `StateFlow<DownloadState>` (`Idle`, `Downloading(track, bytesDownloaded, totalBytes, progress)`, `Completed(track)`, `Error(msg)`).
   * Fitur pembatalan (*cancel job*).

### Fase 2: Floating Download Widget & UI Integration
1. **[NEW] `FloatingDownloadBar.kt`**:
   * Widget floating di atas navigation dock.
   * Mengamati `LanggamDownloader.state`.
   * Menampilkan nama track, progress bar, persentase, dan tombol batal.
2. **[MODIFY] `MainAppContainer.kt`**:
   * Menyematkan `FloatingDownloadBar` di stack layer bawah berdampingan dengan `MiniFloatingAudioBar`.
3. **[MODIFY] `LanggamScreen.kt`**:
   * Saat item di-tap: jika belum ada di lokal -> kirim intent download -> downloader aktif -> auto-play saat selesai.
   * Menampilkan badge ukuran file (misal: "19 MB") dan opsi hapus file jika sudah terunduh.

### Fase 3: Audio Player & Resource Cleanup
1. **`KmpAudioPlayer`**:
   * Memutar berkas dari path `AudioCacheManager` untuk Langgam.
   * Mempertahankan pembacaan resource bundle untuk 8 audio Adzan.
2. **Pembersihan Berkas**:
   * Hapus 11 berkas Langgam dari `shared/src/commonMain/composeResources/files/`.
   * Pertahankan 8 berkas Adzan.

---

## 🧪 Rencana Pengujian & Verifikasi

1. **Pengujian Floating Download Bar**:
   * Tap "Bani Hasyim" -> pastikan Floating Download Bar muncul di bawah layar dengan progress berjalan (0-100%).
   * Pindah tab dari Langgam ke Home / Sholat -> pastikan unduhan tidak terputus dan progress bar tetap berjalan.
2. **Pengujian Auto-Play Setelah Selesai**:
   * Begitu download selesai 100% -> Floating Download Bar berganti secara mulus menjadi Mini Floating Audio Player dan audio langsung berputar otomatis.
3. **Pengujian Offline Playback**:
   * Masuk ke mode Airplane -> tap kembali lagu yang sudah diunduh -> pastikan audio langsung berputar tanpa jeda download.
