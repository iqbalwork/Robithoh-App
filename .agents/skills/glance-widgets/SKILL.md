---
name: glance-widgets
description: Architecture and development guidelines for the 8 Android Glance Home Screen Widgets in Robithoh App. Covers widget providers, layouts, state synchronization, and background updates.
---

# Android Glance Home Screen Widgets

Robithoh App includes 8 interactive Android launcher widgets located under:
`androidApp/src/main/kotlin/com/iqbalwork/robithoh/widget/`

---

## 1. The 8 Widget Providers

1. `PrayerWidget4x1Provider.kt`: Compact prayer time & countdown.
2. `PrayerWidget4x2Provider.kt`: Full daily prayer timetable.
3. `TasbihWidget2x2Provider.kt`: Interactive digital tasbih counter.
4. `TasbihWidget4x1Provider.kt`: Horizontal tasbih progress bar.
5. `TanbihWidget4x2Provider.kt`: Rotating Tanbih quotes from Abah Sepuh & Abah Anom.
6. `QuranWidget2x2Provider.kt`: Last-read Quran card with quick jump action.
7. `QuranWidget4x1Provider.kt`: Horizontal Quran bookmark bar.
8. `QuickAccessWidget4x1Provider.kt`: 4-button quick launch bar.

---

## 2. Triggering Updates from Main App

Whenever data that affects a widget is mutated inside the main app (e.g. tasbih tap, bookmark update, prayer calculation change):
Always call the corresponding helper's update method:

```kotlin
// Examples:
PrayerWidgetHelper.updateAllWidgets(context)
TasbihWidgetHelper.updateAllWidgets(context)
QuranWidgetHelper.updateAllWidgets(context)
TanbihWidgetHelper.updateAllWidgets(context)
QuickAccessWidgetHelper.updateAllWidgets(context)
```

Each helper builds the latest RemoteViews or Glance state and notifies the Android `AppWidgetManager`.

---

## 3. Styling & Android 12+ Guidelines

- Respect Android system rounded corners (`@android:dimen/system_app_widget_background_radius`).
- Support both Light and Dark modes with high legibility.
- Keep RemoteViews layout hierarchies shallow for optimal launcher performance.
