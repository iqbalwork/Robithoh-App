# ADR-0004: 8 Android Home Screen Widgets via Glance & AppWidgetProvider

*   **Status**: Accepted
*   **Date**: 2026-09-08
*   **Deciders**: Iqbal Fauzi, Antigravity AI
*   **Consulted**: Core Development Team
*   **Informed**: Android Engineering & QA

---

## 1. Context and Problem Statement
Users need fast, glanceable access to prayer schedules, daily wirid tasbih, Quran bookmarks, and Tanbih inspirational quotes directly from their Android launcher without launching the full application.

## 2. Decision
We decided to implement **8 Android Home Screen Widget variants** under `androidApp/src/main/kotlin/com/iqbalwork/robithoh/widget/`:
1. `PrayerWidget4x1Provider` & `PrayerWidget4x2Provider`: Prayer schedule & next countdown.
2. `TasbihWidget2x2Provider` & `TasbihWidget4x1Provider`: Quick wirid counter with direct home screen tapping.
3. `TanbihWidget4x2Provider`: Rotating Tanbih quotes from Abah Sepuh & Abah Anom.
4. `QuranWidget2x2Provider` & `QuranWidget4x1Provider`: Last read bookmark cards & direct jump action.
5. `QuickAccessWidget4x1Provider`: 4-button quick launch bar for primary features.

### Synchronization Pattern:
App operations trigger corresponding `*WidgetHelper.updateAllWidgets(context)` methods that broadcast `ACTION_APPWIDGET_UPDATE` to ensure widget displays remain reactive to in-app changes.

## 3. Rationale
* Significantly increases user engagement and utility throughout daily prayer intervals.
* Modular helper classes isolate widget layout building from main app UI logic.

## 4. Consequences
* **Good**: Deep Android system integration, high daily active utility.
* **Bad**: Widgets are Android-specific; iOS Home Screen Widgets (WidgetKit) must be implemented separately in `iosApp` in the future.
* **Neutral**: Battery usage is kept negligible by updating widgets only on state mutation and prayer time transitions.

## 5. References
* [WIDGET_ARCHITECTURE.md](../references/WIDGET_ARCHITECTURE.md)
