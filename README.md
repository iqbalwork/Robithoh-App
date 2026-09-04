# 📿 Robithoh App — Panduan Ibadah & Amaliyah TQN 38 Sirnarasa

<div align="center">

![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-2.4.10-7F52FF?logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.11.1-4285F4?logo=jetpackcompose&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-3DDC84?logo=android&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-MVI%20%7C%20Offline--First-blueviolet)
![Database](https://img.shields.io/badge/Database-SQLDelight%202.0.2-brightgreen)
![DI](https://img.shields.io/badge/DI-Koin%204.0-E24A4A)
![License](https://img.shields.io/badge/License-Proprietary-red)

**Aplikasi Pendamping Ibadah & Amaliyah Tarekat Qodiriyah Naqsyabandiyah (TQN) PP. Sirnarasa Ciamis (Silsilah ke-38)**  
Dibangun menggunakan **Kotlin Multiplatform (KMP)** & **Compose Multiplatform (CMP)** untuk **Android** dan **iOS** dengan arsitektur **100% Offline-First**.

</div>

---

## 📌 Daftar Isi
- [✨ Fitur Utama](#-fitur-utama)
- [📱 Tampilan & Desain](#-tampilan--desain)
- [🏗️ Arsitektur & Tech Stack](#️-arsitektur--tech-stack)
- [📂 Struktur Proyek](#-struktur-proyek)
- [🚀 Memulai & Menjalankan Proyek](#-memulai--menjalankan-proyek)
  - [Prasyarat](#prasyarat)
  - [Kompilasi Android](#kompilasi-android)
  - [Kompilasi iOS](#kompilasi-ios)
  - [Menjalankan Unit Test](#menjalankan-unit-test)
- [📖 Katalog Liturgi & Amaliyah](#-katalog-liturgi--amaliyah)
- [⚙️ Pengaturan & Kustomisasi Sholat](#️-pengaturan--kustomisasi-sholat)
- [🤝 Kontribusi & Pedoman](#-kontribusi--pedoman)

---

## ✨ Fitur Utama

### 1. 📖 Al-Qur'an 30 Juz & Pembaca Kitab
* **Teks Lengkap 30 Juz Offline**: Pembaca mushaf lengkap tanpa perlu koneksi internet.
* **Navigasi & Pencarian Cepat**: Pilih surat, loncat ke nomor ayat tertentu, serta opsi salin dan bagikan ayat.
* **Penanda Terakhir Dibaca (Bookmark)**: Tersimpan otomatis di basis data lokal SQLDelight.

### 2. 📜 Manaqib & Silsilah MTQN Suryalaya Sirnarasa PPKN
* **Bilingual Manaqib**: Teks Manqobah Syaikh Abdul Qodir Al-Jailani r.a. tersedia dalam **Bahasa Indonesia** dan **Basa Sunda**.
* **Protokoler MC Manaqib**: Panduan susunan acara MC Manaqib (Indonesia & Sunda).
* **Tanbih Guru Mursyid**: Wasiat luhur Syaikh Abdullah Mubarok bin Nur Muhammad (Abah Sepuh) & Syaikh Ahmad Shohibulwafa Tajul Arifin (Abah Anom) dalam dua bahasa.
* **Tawassul & Silsilah 38 Mursyid**: Mata rantai emas kemursyidan TQN bersambung dari Baginda Nabi Muhammad SAW hingga Guru Mursyid ke-38 Pangersa Abah Aos.
* **Doa Manaqib**: Doa penutup amaliyah manaqib lengkap.

### 3. 🕌 Jadwal Sholat & Notifikasi Adzan Presisi
* **Kalkulasi Astronomis KMP (Adhan Engine)**: Perhitungan waktu sholat akurat berbasis koordinat GPS perangkat maupun pencarian lokasi manual.
* **Metode Hisab Lengkap**: Mendukung standar **Kemenag RI**, Muslim World League (MWL), ISNA, Umm Al-Qura, Egyptian General Authority of Survey, University of Islamic Sciences Karachi, Shia Ithna-Ashari, dan Institute of Geophysics Tehran.
* **Koreksi Menit Manual (Ihtiyat)**: Pengaturan offset menit per waktu sholat (Imsak, Subuh, Terbit, Dzuhur, Ashar, Maghrib, Isya).
* **Pilihan Muadzin & Audio Adzan Khusus**:
  * Syaikh Misyari Rasyid Al-Afasy (Standard & Subuh)
  * Syaikh Mansour Al-Zahrani (Standard & Subuh)
  * Hafiz Mustafa Ozcan (Standard & Subuh)
  * Ahmad Al-Nafees (Standard & Subuh)
* **Penjadwalan Alarm Mandiri**: Background alarm scheduler asli pada Android (`AlarmManager` + Foreground Service) dan iOS Local Notification.

### 4. 📿 Tasbih Digital Interaktif
* **Respon Haptik & Audio**: Getaran taktil presisi (*Haptic Feedback*) pada setiap ketukan dan saat target tercapai.
* **Pilihan Target Fleksibel**: Hitungan 33, 99, 165 (standar dzikir harian TQN), 1000, atau tanpa batas (*uncounted*).
* **Floating Tasbih Overlay**: Widget tasbih melayang yang dapat digunakan bersamaan saat membaca dokumen atau amaliyah.

### 5. 🎵 Langgam TQN & Pemutar Audio Multiplatform
* **Koleksi Audio Resmi**: Langgam Al-Fatihah Ad-Dhuha s/d An-Nasr, Dzikir Jahr, Dzikir Khofi/Irama Dzikir, Sholawat Bani Hasyim, Sholat Jumat, dan Sholat Tarawih.
* **Mini Floating Audio Bar**: Player audio melayang di bagian bawah layar dengan kontrol play/pause, seek timeline bar, dan info lagu yang tetap aktif saat berpindah-pindah layar.
* **100% Asset Lokal**: Berkas audio langsung dimuat dari resource bundle aplikasi (tanpa streaming/kuota internet).

### 6. 📑 Katalog 45+ Amaliyah & Sholat Sunnah
* Dzikir Ba'da Sholat & Khotaman TQN.
* Panduan Sholat: Sholat Harian, Bulanan, Tahunan, Sholat Safar (Jamak & Qashar), Tarawih & Witir, Sholat Rajab, Nisfu Sya'ban, Lailatul Qadar, dan Sholat Lidaf'il Bala (Rebo Wekasan).
* Sholawat Thoriqiyyah, Sholawat Bani Hasyim, Sholawat Amjad, Sholawat Badriyyah, Sholawat Jiyaaroh ke Rasulullah SAW, dan Iqomah Subuh.
* Amaliyah 12 Bulan Hijriyah (Muharram hingga Dzulhijjah).
* Panduan Ziarah: Ziarah Waliyulloh, Ziarah Kubur Umum, dan Adab Ziarah Maqom.

### 7. 🏫 Profil Pondok Pesantren Sirnarasa
* Profil Pesantren Sirnarasa Ciamis, Jawa Barat.
* Sejarah tarekat dan biografi Guru Mursyid Silsilah ke-38 Syaikh Muhammad Abdul Gaos Saefulloh Maslul r.a. (Pangersa Abah Aos).

---

## 📱 Tampilan & Desain

* **Nuansa Khidmat Merah Putih & Emas**:
  * *Primary Crimson*: `#8B1E1E` / `#A11E22` (Merah Marun Tradisi)
  * *Accent Gold*: `#D4AF37` / `#C5A059` (Emas Khidmat)
  * *Paper Cream Background*: `#FAF7F2` (Latar ramah mata pembaca)
  * *Charcoal Text*: `#1C1917` (Keterbacaan tinggi)
* **Floating Pill Dock Bar**: Navigasi bawah modern mengambang dengan indikator tab aktif (Home, Sholat, Kitab Al-Qur'an, Profil).
* **Tipografi Mushaf & Kaligrafi**: Font Arab Scheherazade dioptimalkan untuk harakat yang jelas dan tipografi teks Latin modern (*Plus Jakarta Sans*).

---

## 🏗️ Arsitektur & Tech Stack

Aplikasi ini menggunakan pola arsitektur **Clean Architecture + MVI (Model-View-Intent)** berbasis Unidirectional Data Flow (UDF).

```
┌─────────────────────────────────────────────────────────────┐
│                    Shared UI Layer                          │
│        (Compose Multiplatform 1.11.1 / Material 3)          │
└──────────────────────────────┬──────────────────────────────┘
                               │ UI State / UI Intent
┌──────────────────────────────▼──────────────────────────────┐
│                  Presentation Layer (MVI)                   │
│   (AmaliyahViewModel, QuranViewModel, TasbihViewModel)      │
└──────────────────────────────┬──────────────────────────────┘
                               │ Flow / Suspend functions
┌──────────────────────────────▼──────────────────────────────┐
│                    Data & Domain Layer                      │
│     (AmaliyahRepo, QuranRepo, LanggamRepo, MarkdownRepo)    │
└──────────────────────────────┬──────────────────────────────┘
                               │
       ┌───────────────────────┴───────────────────────┐
       ▼                                               ▼
┌──────────────────────────────┐     ┌────────────────────────────────┐
│   Local Database (SQLDelight)│     │  Platform Services (Expect)    │
│   • Bookmarks & Last Read    │     │  • KmpAudioPlayer              │
│   • Prayer Configurations    │     │  • PrayerAlarmScheduler        │
│   • Amaliyah Counter History │     │  • LocationProvider            │
│   • Manqobah FTS & Entities  │     │  • KmpHapticFeedback           │
└──────────────────────────────┘     └────────────────────────────────┘
```

### Rincian Teknologi & Dependensi

| Komponen | Pustaka / Teknologi | Versi | Deskripsi |
|---|---|---|---|
| **Language** | Kotlin Multiplatform | `2.4.10` | Bahasa utama lintas platform |
| **UI Framework** | Compose Multiplatform | `1.11.1` | Deklaratif UI bersama untuk Android & iOS |
| **Design System** | Material 3 Multiplatform | `1.11.0-alpha07` | Sistem komponen UI & tema dinamis |
| **Dependency Injection** | Koin | `4.0.2` | DI terpusat (`koin-core`, `koin-compose`) |
| **Database** | CashApp SQLDelight | `2.0.2` | SQLite type-safe code generator & driver |
| **Prayer Times** | BatoulApps Adhan 2 | `0.0.7` | Perhitungan astronomi waktu sholat KMP |
| **Concurrency** | Kotlinx Coroutines | `1.10.1` | Pemrosesan asinkron & Reactive Flow |
| **Serialization** | Kotlinx Serialization | `1.8.0` | Parsing JSON & struktur data |
| **Date / Time** | Kotlinx Datetime | `0.6.1` | Manajemen waktu dan tanggal kalender |
| **Android Audio Engine** | AndroidX Media3 / MediaPlayer | `1.5.1` | Native playback audio & foreground service |
| **iOS Audio Engine** | AVFoundation / AVPlayer | *Native* | Native playback audio iOS |

---

## 📂 Struktur Proyek

```text
Robithoh-App/
├── androidApp/                        # Modul Aplikasi Android Native
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/iqbalwork/robithoh/MainActivity.kt
├── iosApp/                            # Modul Aplikasi iOS Native (SwiftUI)
│   ├── iosApp.xcodeproj/
│   └── iosApp/
│       ├── ContentView.swift          # Jembatan ComposeView via UIViewControllerRepresentable
│       └── iOSApp.swift               # Entry point SwiftUI App
├── shared/                            # Modul KMP & Compose Multiplatform
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/                # Kode Bersama (100% Shared UI & Logic)
│       │   ├── composeResources/      # Asset gambar, font kaligrafi, file Markdown, audio MP3
│       │   │   ├── drawable/
│       │   │   ├── files/             # 45+ Dokumen amaliyah .md & audio adzan/langgam
│       │   │   └── font/
│       │   ├── kotlin/com/iqbalwork/robithoh/
│       │   │   ├── App.kt             # Root composable & state hoisting
│       │   │   ├── core/              # Komponen inti (Audio, Database, Theme, Location, Haptic)
│       │   │   │   ├── audio/         # Interface KmpAudioPlayer expect/actual
│       │   │   │   ├── database/      # Helper & Driver SQLDelight
│       │   │   │   ├── designsystem/  # Tema warna, Card, Header, Font, Divider
│       │   │   │   ├── location/      # Expect/actual GPS provider
│       │   │   │   └── notification/  # Interface PrayerAlarmScheduler
│       │   │   ├── di/                # Modul Koin (AppModule, ViewModelModule, AudioModule)
│       │   │   ├── feature/           # Fitur modular
│       │   │   │   ├── amaliyah/      # Kalkulasi sholat, amaliyah MVI & views
│       │   │   │   ├── home/          # Tab Dashboard utama & Modal Bottom Sheets
│       │   │   │   ├── langgam/       # Audio player Langgam TQN & daftar track
│       │   │   │   ├── library/       # Katalog kitab & dokumen amaliyah
│       │   │   │   ├── manaqib/       # Pembaca Manaqib Indonesia/Sunda & MVI
│       │   │   │   ├── prayer/        # Layar tab Sholat & navigasi adzan
│       │   │   │   ├── profile/       # Profil Pesantren Sirnarasa & Settings
│       │   │   │   ├── quran/         # Pembaca Al-Qur'an 30 Juz & bookmark
│       │   │   │   ├── reader/        # Markdown Parser & Generic Document Reader
│       │   │   │   └── tasbih/        # Layar & widget tasbih digital
│       │   │   └── navigation/        # BackHandler, Dock Bar, NavDisplay, ScreenKey
│       │   └── sqldelight/            # Skema RobithohDatabase.sq
│       ├── androidMain/               # Implementasi platform Android (actual)
│       └── iosMain/                   # Implementasi platform iOS (actual)
├── gradle/
│   └── libs.versions.toml             # Version Catalog Gradle terpusat
└── settings.gradle.kts
```

---

## 🚀 Memulai & Menjalankan Proyek

### Prasyarat
1. **JDK 17** atau **JDK 21** terpasang pada sistem.
2. **Android Studio** (Ladybug / Koala / versi terbaru dengan plugin Kotlin Multiplatform).
3. **Android SDK** (API Level 34+ / Compile SDK 37).
4. *(Khusus iOS)* **macOS** dengan **Xcode 15+** dan CocoaPods / Swift Package Manager terkonfigurasi.

### Kompilasi Android

Untuk melakukan build berkas APK Android:

```bash
# Build Debug APK
./gradlew :androidApp:assembleDebug

# Jalankan langsung pada perangkat / emulator Android yang terhubung
./gradlew :androidApp:installDebug
```

Berkas APK akan dihasilkan di folder `androidApp/build/outputs/apk/debug/`.

### Kompilasi iOS

1. Buka folder `iosApp` di Xcode:
   ```bash
   open iosApp/iosApp.xcodeproj
   ```
2. Pilih target perangkat atau simulator (misal: *iPhone 16 Pro*).
3. Klik **Run** (`Cmd + R`) di Xcode.

Atau jalankan melalui task Gradle:
```bash
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```

### Menjalankan Unit Test

Untuk menjalankan seluruh rangkaian pengujian unit bersama:

```bash
./gradlew :shared:allTests
```

---

## 📖 Katalog Liturgi & Amaliyah

Aplikasi ini menyertakan lebih dari **45 dokumen panduan lengkap** yang diparsing secara instan secara offline:

<details>
<summary><b>1. Dzikir & Khotaman</b></summary>

* Dzikir Ba'da Sholat TQN (`DZIKIR_TQN.md`)
* Amaliyah Khotaman MTQN Suryalaya Sirnarasa PPKN (`KHOTAMAN_TQN.md`)
* Tarhim TQN Menjelang Sholat (`TARHIM_TQN.md`)
* Silsilah 38 Guru Mursyid TQN (`SILSILAH_TQN.md`)
* Tahlil TQN & Hadhloroh Arwah (`TAHLIL_TQN.md`)
</details>

<details>
<summary><b>2. Manaqib Syaikh Abdul Qodir Al-Jailani r.a.</b></summary>

* Susunan Acara MC Manaqib — Bahasa Indonesia & Basa Sunda
* Tanbih Guru Mursyid — Bahasa Indonesia & Basa Sunda
* Tawassul Lengkap MTQN Suryalaya Sirnarasa PPKN
* 40+ Pasal Manqobah — Bahasa Indonesia & Basa Sunda
* Doa Manqobah — Bahasa Indonesia & Basa Sunda
</details>

<details>
<summary><b>3. Sholat Sunnah & Waktu-Waktu Khusus</b></summary>

* Sholat Harian Guru Mursyid (Tahajjud, Hajat, Dhuha, Awwabin, Taubat, dll.)
* Sholat Bulanan TQN
* Sholat Tahunan
* Sholat Safar (Panduan Jamak & Qashar Perjalanan)
* Sholat Tarawih & Witir Kaifiyat TQN
* Sholat Sunnah Bulan Rajab (Malam 1, 15, dan Akhir Rajab)
* Sholat Nisfu Sya'ban & Doa
* Sholat Lailatul Qadar
* Sholat Lidaf'il Bala (Rebo Wekasan Bulan Shofar)
</details>

<details>
<summary><b>4. Sholawat & Doa Khusus</b></summary>

* Sholawat Thoriqiyyah
* Sholawat Bani Hasyim
* Sholawat Amjad
* Sholawat Badriyyah
* Sholawat Jiyaaroh ke Rasulullah SAW
* Iqomah Shubuh & Tarhiman
* Salam Kepada Guru Mursyid (Adab Rabithah)
* Doa Rijalul Ghaib
* Doa Istighotsah
* Wirid & Amaliyah Sebelum Tidur
</details>

<details>
<summary><b>5. Amaliyah 12 Bulan Hijriyah</b></summary>

* Amaliyah Muharram (Asyura & Awal Tahun)
* Amaliyah Shofar
* Amaliyah Rabi'ul Awwal (Maulid Nabi SAW)
* Amaliyah Rabi'uts Tsani (Haul Syaikh Abdul Qodir Al-Jailani)
* Amaliyah Jumadil Ula & Jumadits Tsaniyah
* Amaliyah Rajab & Sya'ban
* Amaliyah Ramadhan (Tadarus & Lailatul Qadar)
* Amaliyah Syawal (Puasa 6 Hari)
* Amaliyah Dzulqa'dah & Dzulhijjah (Haji & Qurban)
</details>

---

## ⚙️ Pengaturan & Kustomisasi Sholat

Pengguna memiliki fleksibilitas penuh dalam menentukan konfigurasi sholat melalui basis data `PrayerSettingsEntity`:
1. **Pilihan Metode Hisab**: Penyesuaian sudut Subuh & Isya sesuai regulasi setempat.
2. **Penyesuaian Madzhab**: Perhitungan waktu Ashar (Madzhab Syafi'i/Maliki/Hambali dengan bayangan 1x vs Madzhab Hanafi dengan bayangan 2x).
3. **Koreksi Ihtiyat**: Slider/picker penambahan atau pengurangan menit (-10 s/d +10 menit) untuk masing-masing waktu sholat.
4. **Adzan Toggle**: Pilihan bunyi adzan aktif/nonaktif independen per waktu sholat (misal: aktifkan hanya saat Subuh & Maghrib).

---

## 🤝 Kontribusi & Pedoman

1. Buat branch baru untuk setiap fitur atau perbaikan bug:
   ```bash
   git checkout -b feature/nama-fitur
   ```
2. Pastikan kode mengikuti konvensi penamaan Kotlin & Compose Multiplatform.
3. Selalu periksa kompilasi kedua platform (`androidApp` & `iosApp`) sebelum membuat Pull Request.
4. Lakukan commit dengan pesan yang deskriptif:
   ```bash
   git commit -m "feat(amaliyah): tambah fitur pencarian doa manaqib"
   ```

---

<div align="center">

Dibuat dengan khidmat untuk ikhwan/akhwat **TQN Pondok Pesantren Sirnarasa Ciamis**.  
*Semoga membawa berkah dan manfaat bagi kemaslahatan umat.*

</div>