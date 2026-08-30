# 🧪 Robithoh App — Maestro Automation Testing & Play Store Assets Capture

Automasi pengujian menyeluruh (*End-to-End Test Suite*) untuk **Robithoh App** menggunakan **Maestro CLI**, dirancang untuk memvalidasi seluruh alur fungsionalitas aplikasi sekaligus mengambil tangkapan layar (*high-res screenshots*) dari setiap halaman, modal bottom sheet, dan dialog untuk dijadikan **Assets / Screenshots di Google Play Store**.

---

## 📁 Struktur Direktori

```text
.maestro/
├── full_suite.yaml                  # Master test suite yang mengeksekusi semua flow
├── README.md                        # Panduan dan dokumentasi test
├── screenshots/                     # Output tangkapan layar untuk Play Store
├── output_video/                    # Output rekaman video demo MP4
├── flows/
│   ├── 01_splash_and_home.yaml      # Beranda (Header, Next Prayer Hero, Grid Menu, Tanbih)
│   ├── 02_modal_bottom_sheets.yaml  # 5 Bottom Sheets (Manaqib, Sholat, Sholawat, Tahlil, Doa)
│   ├── 03_salat_prayer_times.yaml   # Tab Sholat, Checklist Sholat, Date Nav, Notification & Adzan Picker
│   ├── 04_prayer_settings.yaml      # Layar Metode Perhitungan & Koreksi Waktu Sholat (Adjustments)
│   ├── 05_qibla_compass.yaml        # Kompas Arah Kiblat & Sheet Panduan Kalibrasi Angka 8
│   ├── 06_tasbih_digital.yaml       # Tasbih Digital, Disk Counter, Sheet Dzikir & Dialog Reset/Kustom
│   ├── 07_langgam_player.yaml       # Pemutar Audio Langgam TQN & Mini Floating Bar
│   ├── 08_quran_and_reader.yaml     # Mushaf 114 Surah, Search Yasin, Surah Reader & Sheet Settings/Jump
│   ├── 09_document_reader.yaml      # Pembaca Amaliyah Dzikir & Tanbih (Bilingual Indonesia & Sunda)
│   └── 10_settings_and_profile.yaml # Tab Pengaturan, Toggle Dark Mode & Profil Pesantren
└── scripts/
    ├── run_test.sh                  # Eksekusi full test suite & simpan screenshot
    └── record_video.sh              # Eksekusi full test suite & rekam video MP4
```

---

## 🚀 Cara Menjalankan

### 1. Prasyarat
- **Maestro CLI** telah terpasang di sistem (`maestro --version`).
- Emulator Android sedang aktif atau HP fisik Android terhubung dengan mode **USB Debugging** aktif (`adb devices`).
- Aplikasi Robithoh telah terinstal di perangkat (`com.iqbalwork.robithoh`).

---

### 2. Menjalankan Semua Test & Mengambil Screenshot (Play Store Assets)

Jalankan perintah berikut dari root project:

```bash
# Opsi 1: Menggunakan helper script
./.maestro/scripts/run_test.sh

# Opsi 2: Menggunakan maestro CLI langsung
maestro test .maestro/full_suite.yaml
```

Semua screenshot otomatis tersimpan di folder `.maestro/screenshots/`.

---

### 3. Merekam Video Pengujian / Demo Aplikasi (.mp4)

Maestro menyediakan fitur perekaman layar bawaan dengan format:
`maestro record --local <flowFile> [<outputFile>]`

```bash
# Opsi 1: Menggunakan helper script
./.maestro/scripts/record_video.sh

# Opsi 2: Menggunakan maestro record langsung
maestro record --local .maestro/full_suite.yaml .maestro/output_video/robithoh_demo.mp4
```

---

### 4. Menjalankan Flow Tertentu Secara Terpisah

Jika Anda ingin menguji atau mengambil screenshot satu fitur tertentu saja:

```bash
# Contoh: Hanya menguji Kompas Kiblat & Kalibrasi
maestro test .maestro/flows/05_qibla_compass.yaml

# Contoh: Hanya menguji Tasbih Digital & Dialogs
maestro test .maestro/flows/06_tasbih_digital.yaml

# Contoh: Hanya menguji Al-Qur'an 114 Surah
maestro test .maestro/flows/08_quran_and_reader.yaml
```

---

## 📸 Daftar Tangkapan Layar (Play Store Assets Catalog)

| No | File Screenshot | Halaman / Komponen | Fitur yang Ditampilkan |
|---|---|---|---|
| 01 | `01_home_screen.png` | **Beranda Utama** | Dashboard, Waktu Sholat Terdekat, Grid 12 Menu Amaliyah |
| 02 | `02_home_screen_scrolled.png` | **Beranda (Bawah)** | Kartu Untaian Mutiara & Tanbih Abah Sepuh |
| 03 | `03_sheet_manaqib.png` | **Modal Bottom Sheet** | Menu Manaqib (MC, Tanbih, Tawassul, Manqobah, Bani Hasyim) |
| 04 | `04_sheet_sholat.png` | **Modal Bottom Sheet** | Menu Sholat Waktu & Safar + Sholat Sunnah Tahunan |
| 05 | `05_sheet_sholawat.png` | **Modal Bottom Sheet** | Koleksi Sholawat TQN PP Suryalaya Sirnarasa |
| 06 | `06_sheet_tahlil_ziyaroh.png` | **Modal Bottom Sheet** | Tahlil TQN & Ziyaroh Umum / Waliyulloh |
| 07 | `07_sheet_doa.png` | **Modal Bottom Sheet** | Doa Istighotsah, Rijalul Ghoib, Salam Wali Mursyid |
| 08 | `08_salat_tab.png` | **Tab Sholat** | Jadwal Sholat 7 Waktu, Azimuth Kiblat & Checklist Sholat |
| 09 | `09_salat_next_day.png` | **Tab Sholat** | Navigasi Jadwal Sholat Hari Berikutnya |
| 10 | `10_salat_settings_section.png` | **Tab Sholat (Pengaturan)** | Pengaturan Suara Adzan, Pengingat & Koreksi Menit |
| 11 | `11_sheet_adzan_voice_picker.png` | **Bottom Sheet** | Pemilih Audio Muadzin (Misyari, Mansour, Al-Nafees, dll) |
| 12 | `12_sheet_notification_mode_picker.png` | **Bottom Sheet** | Pemilih Mode Notifikasi (Adzan, Push Notif, Mute) |
| 13 | `13_prayer_calculation_methods.png` | **Layar Pengaturan** | Metode Perhitungan (Kemenag RI, MWL, Umm Al-Qura, dll) |
| 14 | `14_prayer_adjustments.png` | **Layar Pengaturan** | Penyesuaian / Koreksi Waktu Sholat Manual (Ihtiyat) |
| 15 | `15_prayer_offset_picker_sheet.png` | **Bottom Sheet** | Slider / Picker Offset Menit Per Waktu Sholat |
| 16 | `16_qibla_compass.png` | **Kompas Arah Kiblat** | Dial Kompas Interaktif, Derajat Azimuth & Kunci Kiblat |
| 17 | `17_compass_calibration_guide.png` | **Bottom Sheet** | Panduan Animasi Kalibrasi Sensor Gerakan Angka 8 |
| 18 | `18_tasbih_digital_screen.png` | **Tasbih Digital** | Piringan Ketuk Haptik, Preset Target 165x & Status Sesi |
| 19 | `19_tasbih_dzikir_selector_sheet.png` | **Bottom Sheet** | Pemilih Bacaan Wirid & Kalimah Thoyyibah |
| 20 | `20_tasbih_counter_tapped.png` | **Tasbih Digital** | Interaksi Ketukan Counter & Progress Percentage |
| 21 | `21_tasbih_reset_dialog.png` | **Alert Dialog** | Dialog Konfirmasi Ulangi / Reset Hitungan |
| 22 | `22_tasbih_custom_target_dialog.png` | **Alert Dialog** | Dialog Input Target Wirid Kustom Bebas |
| 23 | `23_langgam_track_list.png` | **Langgam Audio Player** | Daftar Rekaman Audio Langgam Resmi TQN PP Suryalaya Sirnarasa |
| 24 | `24_langgam_playing.png` | **Langgam Audio Player** | Player Bar dengan Kontrol Seek Timeline & Play/Pause |
| 25 | `25_home_with_floating_player.png` | **Beranda** | Mini Floating Audio Bar Aktif di Bawah Layar |
| 26 | `26_quran_surah_list.png` | **Al-Qur'an Digital** | Daftar 114 Surah Lengkap dengan Nomor & Asal Surah |
| 27 | `27_quran_search_yasin.png` | **Al-Qur'an Digital** | Pencarian Instan Surah Berdasarkan Nama & Latin |
| 28 | `28_quran_surah_reader.png` | **Pembaca Mushaf** | Teks Arab Scheherazade, Terjemahan & Penanda Ayat |
| 29 | `29_quran_reader_settings_sheet.png` | **Bottom Sheet** | Pengaturan Ukuran Huruf Arab, Font & Tampilan Terjemah |
| 30 | `30_quran_jump_to_ayah_sheet.png` | **Bottom Sheet** | Lompat Cepat ke Nomor Surah & Nomor Ayat Tertentu |
| 31 | `31_reader_dzikir_tqn.png` | **Pembaca Liturgi** | Teks Amaliyah Dzikir Ba'da Sholat |
| 32 | `32_reader_typography_settings_sheet.png` | **Bottom Sheet** | Pengaturan Tipografi Pembaca Dokumen Amaliyah |
| 33 | `33_reader_tanbih_indonesia.png` | **Pembaca Tanbih** | Wasiat Guru Mursyid (Bahasa Indonesia) |
| 34 | `34_reader_tanbih_sunda.png` | **Pembaca Tanbih** | Wasiat Guru Mursyid (Basa Sunda) |
| 35 | `35_settings_light_mode.png` | **Pengaturan (Light)** | Menu Pengaturan Aplikasi Tema Terang (*Cream Paper*) |
| 36 | `36_settings_dark_mode.png` | **Pengaturan (Dark)** | Tampilan Tema Gelap (*Dark Charcoal & Crimson*) |
| 37 | `37_settings_about_section.png` | **Pengaturan (Info)** | Versi Aplikasi, Bantuan & Informasi Pesantren |
