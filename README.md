# Robithoh App — Kotlin & Compose Multiplatform

Aplikasi Ibadah & Amaliyah TQN 38 Sirnarasa berbasis **Kotlin Multiplatform (KMP)** & **Compose Multiplatform (CMP)** dengan tema **Merah Putih & Emas Khidmat**, navigasi **Floating Pill Dock Bar**, serta arsitektur **100% Offline-First**.

## Struktur Proyek

* `/androidApp`: Modul aplikasi Android asli (`com.iqbalwork.robithoh`).
* `/iosApp`: Modul aplikasi iOS (SwiftUI + Shared KMP Framework).
* `/shared`: Modul bersama Compose Multiplatform:
  - `commonMain`: Logika bersama, tema M3, seluruh komponen UI, reader 45 dokumen markdown, dan repositori audio Langgam.
  - `androidMain`: Implementasi native Android (`KmpAudioPlayer` dengan MediaPlayer & cache offline, `DatabaseDriverFactory`, `KmpHapticFeedback`).
  - `iosMain`: Implementasi native iOS.

## Perintah Kompilasi & Build

```bash
# Build Android Debug APK
./gradlew :androidApp:assembleDebug

# Jalankan pengujian bersama
./gradlew :shared:allTests
```

Untuk detail dokumentasi lengkap, silakan lihat [README.md di direktori root](../README.md).