# Implementation Plan — Pembenahan & Penyelarasan Gap Fitur iOS (Robithoh App)

Dokumen ini merinci rencana teknis untuk menyelaraskan gap fitur antara platform Android dan iOS pada **Robithoh App**, mencakup versi aplikasi dinamis, pemutakhiran Firebase Analytics iOS, dukungan 3 WidgetKit iOS utama (Jadwal Sholat, Tasbih, dan Al-Qur'an), In-App Review StoreKit, dan optimasi notifikasi adzan.

---

## User Review Required

> [!IMPORTANT]
> **3 WidgetKit Extension iOS**: Akan ditambahkan SwiftUI Widget Extension pada `iosApp` untuk mendukung 3 Widget utama:
> 1. **PrayerWidget**: Jadwal Sholat & Waktu Amaliyah.
> 2. **TasbihWidget**: Counter Tasbih Digital.
> 3. **QuranWidget**: Penanda Terakhir Dibaca Al-Qur'an.
>
> **Firebase Analytics iOS**: `GoogleService-Info.plist` sudah terkonfirmasi tersedia di `iosApp/iosApp/GoogleService-Info.plist`. `AnalyticsTracker.ios.kt` akan dihubungkan langsung ke Firebase Analytics SDK.

---

## Proposed Changes

---

### Phase 1: Metadata Versi Aplikasi Dinamis

#### [MODIFY] [AppVersion.ios.kt](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/shared/src/iosMain/kotlin/com/iqbalwork/robithoh/AppVersion.ios.kt)
* Mengubah implementasi `appVersionName()` agar membaca versi rilis secara dinamis dari `NSBundle.mainBundle.infoDictionary["CFBundleShortVersionString"]` alih-alih mengembalikan nilai hardcoded `"1.0.0"`.

---

### Phase 2: Firebase Analytics & Event Tracking iOS

#### [MODIFY] [AnalyticsTracker.ios.kt](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/shared/src/iosMain/kotlin/com/iqbalwork/robithoh/core/analytics/AnalyticsTracker.ios.kt)
* Mengimplementasikan `IosAnalyticsTracker` untuk meneruskan pemanggilan `logEvent`, `setUserProperty`, dan `logScreenView` ke SDK Firebase Analytics di iOS (menggunakan `Analytics.logEvent` / `FIRAnalytics`).

---

### Phase 3: Home Screen Widgets (iOS WidgetKit)

#### [NEW] [RobithohWidgets.swift](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/iosApp/iosApp/RobithohWidgets.swift)
* Membuat komponen SwiftUI WidgetKit untuk 3 Widget utama:
  1. **PrayerWidget**: Menampilkan waktu sholat berikutnya dan daftar waktu sholat harian.
  2. **TasbihWidget**: Menampilkan target dzikir & hitungan terakhir tasbih.
  3. **QuranWidget**: Menampilkan surat, ayat, dan juz terakhir yang dibaca.

#### [MODIFY] [PrayerWidgetSync.ios.kt](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/shared/src/iosMain/kotlin/com/iqbalwork/robithoh/feature/amaliyah/presentation/PrayerWidgetSync.ios.kt)
* Mengimplementasikan `notifyPrayerWidgetUpdate()` untuk memicu pembaruan jadwal sholat pada WidgetKit (`WidgetCenter.shared.reloadAllTimelines()`).

#### [MODIFY] [QuranWidgetSync.ios.kt](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/shared/src/iosMain/kotlin/com/iqbalwork/robithoh/feature/quran/data/QuranWidgetSync.ios.kt)
* Mengimplementasikan `notifyQuranWidgetUpdate()` untuk memicu reload WidgetKit saat bookmark atau bacaan terakhir Qur'an diperbarui.

#### [MODIFY] [TasbihWidgetSync.ios.kt](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/shared/src/iosMain/kotlin/com/iqbalwork/robithoh/feature/tasbih/presentation/TasbihWidgetSync.ios.kt)
* Mengimplementasikan `notifyTasbihWidgetUpdate()` untuk memicu reload WidgetKit saat hitungan tasbih diperbarui.

---

### Phase 4: iOS Native App & Notification Enhancements

#### [MODIFY] [iOSApp.swift](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/iosApp/iosApp/iOSApp.swift)
* Mengatur kategori notifikasi dan penanganan aksi tap notifikasi Adzan di `UNUserNotificationCenterDelegate` agar membuka aplikasi dan menghentikan atau memutar adzan dengan lancar.

---

## Verification Plan

### Automated Tests
- Menjalankan perintah kompilasi Gradle untuk iOS framework:
  ```bash
  ./gradlew :shared:embedAndSignAppleFrameworkForXcode
  ```
- Menjalankan seluruh pengujian unit KMP:
  ```bash
  ./gradlew :shared:allTests
  ```

### Manual Verification
- **Kompilasi iOS di Xcode**: Membuka `iosApp/iosApp.xcodeproj` di Xcode dan menjalankan build/run pada iOS Simulator.
- **Verifikasi Versi**: Memastikan halaman profil/pengaturan di iOS menampilkan versi aplikasi aktual dari `Info.plist`.
- **Verifikasi Analytics**: Memastikan event aplikasi yang dipicu dari iOS muncul di Firebase Analytics Console DebugView.
- **Verifikasi WidgetKit**: Memastikan Widget Jadwal Sholat, Tasbih, dan Qur'an dapat ditambahkan ke Home Screen iOS dan diperbarui secara otomatis.
