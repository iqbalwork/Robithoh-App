# ADR-0009: Multiplatform Build Configuration via BuildKonfig

*   **Status**: Accepted
*   **Date**: 2026-09-13
*   **Deciders**: Iqbal Fauzi
*   **Consulted**: Core Mobile Team
*   **Informed**: QA Team

---

## 1. Context and Problem Statement

Robithoh App adalah aplikasi Kotlin Multiplatform (KMP) yang mendukung berbagai target platform (Android, iOS, JVM). Aplikasi membutuhkan konfigurasi lingkungan (environment variables / build parameters) yang fleksibel untuk membedakan lingkungan pengembangan lokal/staging dan produksi, seperti:
1. `BASE_URL`: URL API server untuk sinkronisasi data Waktal dan dokumen.
2. `ENVIRONMENT`: Identifier lingkungan (`stagingDebug`, `stagingRelease`, `productionDebug`, `productionRelease`).
3. `IS_DEBUG`: Flag boolean penanda mode debug vs release.

Sebelumnya, nilai `baseUrl` di-hardcode di dalam kode Kotlin shared (`WaktalApiService.kt`). Penggunaan `BuildConfig` bawaan AGP hanya berlaku di layer Android (`:androidApp`), sehingga tidak dapat diakses langsung secara type-safe dari kode `commonMain` pada modul `:shared`.

---

## 2. Decision

Kami memutuskan untuk mengintegrasikan pustaka **BuildKonfig** (`com.codingfeline.buildkonfig`) versi `0.22.0` pada modul `:shared`:

### 2.1 Variasi Lingkungan (Flavors & Build Types)
Kami mengonfigurasi 4 varian lingkungan utama:
1. **`stagingDebug`**: `BASE_URL = "https://staging-api.robithoh.com"`, `ENVIRONMENT = "stagingDebug"`, `IS_DEBUG = true`.
2. **`stagingRelease`**: `BASE_URL = "https://staging-api.robithoh.com"`, `ENVIRONMENT = "stagingRelease"`, `IS_DEBUG = false`.
3. **`productionDebug`**: `BASE_URL = "https://api.robithoh.com"`, `ENVIRONMENT = "productionDebug"`, `IS_DEBUG = true`.
4. **`productionRelease`**: `BASE_URL = "https://api.robithoh.com"`, `ENVIRONMENT = "productionRelease"`, `IS_DEBUG = false`.

### 2.2 Default Fallback untuk Local Development
Untuk pengembangan lokal dan target non-flavored (seperti iOS / JVM), `defaultConfigs` diatur menggunakan profil **stagingDebug** (`https://staging-api.robithoh.com`).

### 2.3 Penggunaan dalam Codebase Shared
Konstanta hasil generate BuildKonfig diakses secara type-safe melalui object `com.iqbalwork.robithoh.shared.BuildKonfig` pada modul `shared` (misalnya pada `WaktalApiService.kt`).

---

## 3. Rationale

1. **Type-Safe Multiplatform Access**: BuildKonfig menghasilkan object Kotlin yang langsung tersedia di `commonMain`, sehingga tidak membutuhkan pattern `expect/actual` manual untuk membaca konfigurasi build.
2. **Harmonisasi Flavors Android & Multiplatform**: Nama-nama flavor (`stagingDebug`, `stagingRelease`, `productionDebug`, `productionRelease`) sinkron dengan `productFlavors` & `buildTypes` pada `:androidApp`.
3. **Pemberlakuan Offline-First / Local Staging**: Penggunaan staging URL secara default pada lingkungan lokal memastikan server produksi aman dari pengujian lokal.

---

## 4. Consequences

### Good
* **Tanpa Hardcoded URL**: URL endpoint API dan flag konfigurasi lingkungan dikendalikan sepenuhnya dari `shared/build.gradle.kts` dan version catalog `libs.versions.toml`.
* **Zero Boilerplate**: Menggantikan kebutuhan pembuatan bridge `expect/actual` manual untuk BuildConfig.

### Bad
* BuildKonfig menambah satu Gradle plugin (`com.codingfeline.buildkonfig`) dan satu task generate (`generateBuildKonfig`) di pipeline build `:shared`.

### Neutral
* Setiap penambahan field konfigurasi baru di masa depan cukup ditambahkan ke block `buildkonfig { ... }` pada `shared/build.gradle.kts`.

---

## 5. References

* Repository BuildKonfig: [https://github.com/yshrsmz/BuildKonfig](https://github.com/yshrsmz/BuildKonfig)
* Konfigurasi Gradle Shared: [shared/build.gradle.kts](../../shared/build.gradle.kts)
