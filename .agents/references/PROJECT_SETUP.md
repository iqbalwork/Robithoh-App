# 🛠️ Robithoh App - Project Setup & Architecture

This document serves as the architectural overview and setup guide for AI agents working on **Robithoh App**.

---

## 📌 Tech Stack & Versions

| Component | Technology | Version | Description |
| :--- | :--- | :--- | :--- |
| **Language** | Kotlin Multiplatform | `2.4.10` | Cross-platform core language |
| **UI Framework** | Compose Multiplatform | `1.11.1` | Shared declarative UI for Android & iOS |
| **Design System** | Material 3 Multiplatform | `1.11.0-alpha07` | Dynamic theming & M3 components |
| **Dependency Injection** | Koin | `4.0.2` | Dependency injection (`koin-core`, `koin-compose`) |
| **Database** | CashApp SQLDelight | `2.0.2` | SQLite type-safe code generator & driver |
| **Prayer Calculations** | BatoulApps Adhan 2 | `0.0.7` | Multiplatform astronomical prayer engine |
| **Audio Engine (Android)** | AndroidX Media3 / MediaPlayer | `1.5.1` | Background audio & media notification |
| **Audio Engine (iOS)** | AVFoundation / AVPlayer | *Native* | iOS audio session & background playback |
| **Serialization** | Kotlinx Serialization | `1.8.0` | JSON parsing & data state serializability |
| **Date & Time** | Kotlinx Datetime | `0.6.1` | Cross-platform date and time arithmetic |
| **E2E Testing** | Maestro | Latest | Cross-platform declarative UI flow testing |

---

## 📂 Project Module Structure

```text
Robithoh-App/
├── androidApp/                        # Native Android Application module
│   ├── build.gradle.kts               # Android Application build config & flavors
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/iqbalwork/robithoh/
│           ├── MainActivity.kt        # Main Android Activity
│           ├── RobithohApplication.kt # Application class, DI init
│           └── widget/                # 8 Glance Home Screen Widget implementations
├── iosApp/                            # Native iOS Application module (Xcode / SwiftUI)
│   ├── iosApp.xcodeproj/
│   └── iosApp/
│       ├── ContentView.swift          # Bridge hosting ComposeUIViewController
│       └── iOSApp.swift               # SwiftUI App entry point
├── shared/                            # Kotlin Multiplatform & Compose Shared Core
│   ├── build.gradle.kts               # Dependencies, source sets, SQLDelight config
│   └── src/
│       ├── commonMain/                # 100% Shared Business Logic & UI
│       │   ├── composeResources/      # Assets: fonts, images, liturgy Markdown, audio MP3s
│       │   └── kotlin/com/iqbalwork/robithoh/
│       │       ├── App.kt             # Root Composable & top-level navigation
│       │       ├── core/              # Foundational subsystems (Audio, Database, Theme, Location)
│       │       ├── di/                # Koin Modules (AppModule, ViewModelModule, AudioModule)
│       │       └── feature/           # Domain features (amaliyah, manaqib, prayer, quran, tasbih)
│       ├── androidMain/               # Android-specific implementations (expect/actual)
│       ├── iosMain/                   # iOS-specific implementations (expect/actual)
│       └── commonTest/                # Shared unit tests (30+ test suites)
```

---

## 🏗️ Build Variants & Flavors

The Android module utilizes two product flavors:
1. `staging`: Application ID suffix `.dev`, used for internal testing & debug distribution.
2. `production`: Production release application ID `com.iqbalwork.robithoh`.

Common build tasks:
```bash
# Compile and test shared KMP code
./gradlew :shared:allTests

# Assemble Android staging debug APK
./gradlew :androidApp:assembleStagingDebug

# Assemble Android production release APK (Requires signing key)
./gradlew :androidApp:assembleProductionRelease
```
