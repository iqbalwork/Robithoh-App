# 📱 Android Glance Home Screen Widgets Architecture

This document details the 8 interactive Home Screen Widgets built into **Robithoh App** (`androidApp`).

---

## 🧩 1. The 8 Widget Variants

All widgets are located under `androidApp/src/main/kotlin/com/iqbalwork/robithoh/widget/`:

| No | Widget Name | Class Provider | Size | Description |
| :--- | :--- | :--- | :--- | :--- |
| 1 | **Jadwal Sholat Ringkas** | `PrayerWidget4x1Provider.kt` | 4x1 | Menampilkan waktu sholat aktif saat ini dan hitung mundur ke waktu sholat berikutnya. |
| 2 | **Jadwal Sholat Lengkap** | `PrayerWidget4x2Provider.kt` | 4x2 | Matriks jadwal sholat lengkap harian (Subuh, Terbit, Dzuhur, Ashar, Maghrib, Isya). |
| 3 | **Tasbih Digital Mini** | `TasbihWidget2x2Provider.kt` | 2x2 | Tombol interaktif ketuk wirid langsung dari homescreen tanpa membuka aplikasi. |
| 4 | **Tasbih Bar** | `TasbihWidget4x1Provider.kt` | 4x1 | Bilah horizontal tasbih dengan progress target wirid dan tombol hitung cepat. |
| 5 | **Mutiara Wasiat Tanbih** | `TanbihWidget4x2Provider.kt` | 4x2 | Kutipan wasiat Abah Sepuh & Abah Anom yang berganti secara berkala dengan tombol refresh. |
| 6 | **Penanda Baca Al-Qur'an** | `QuranWidget2x2Provider.kt` | 2x2 | Kartu penanda terakhir dibaca (nama surat, nomor ayat) dengan tombol pintasan buka mushaf. |
| 7 | **Al-Qur'an Bookmark Bar** | `QuranWidget4x1Provider.kt` | 4x1 | Bilah horizontal penanda baca Al-Qur'an ringkas. |
| 8 | **Akses Cepat Amaliyah** | `QuickAccessWidget4x1Provider.kt` | 4x1 | 4 tombol pintasan langsung: Jadwal Sholat, Al-Qur'an, Dzikir Harian, dan Manaqib. |

---

## 🔄 2. State Synchronization Pattern

When the user modifies state inside the main app (e.g. increments tasbih, updates bookmark in Quran reader, or modifies prayer calculation settings):
1. The app saves the updated state to the local database / SharedPreferences.
2. The app invokes the helper's update method:
   - `PrayerWidgetHelper.updateAllWidgets(context)`
   - `TasbihWidgetHelper.updateAllWidgets(context)`
   - `QuranWidgetHelper.updateAllWidgets(context)`
   - `TanbihWidgetHelper.updateAllWidgets(context)`
   - `QuickAccessWidgetHelper.updateAllWidgets(context)`
3. Each helper sends a broadcast `ACTION_APPWIDGET_UPDATE` or calls `AppWidgetManager.updateAppWidget()` to refresh the RemoteViews/Glance hierarchy immediately.

---

## 🎨 3. Design & Theming Rules
- Widgets adhere to the brand color palette (`#8B1E1E` Primary Crimson, `#D4AF37` Accent Gold, `#FAF7F2` Cream).
- Supports Android dynamic theming and high-contrast dark mode.
- Rounded corners match Android 12+ (API 31+) widget styling guidelines (`system_app_widget_background_radius`).
