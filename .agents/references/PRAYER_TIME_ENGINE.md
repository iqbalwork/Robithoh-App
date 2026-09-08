# 🕌 Prayer Time Calculation Engine & Adhan Scheduler

This document details the astronomical prayer time calculation and notification system in **Robithoh App**.

---

## 🧭 1. Astronomical Calculation Engine (Adhan 2 KMP)

Robithoh App uses **BatoulApps Adhan 2 (0.0.7)** for pure Kotlin Multiplatform astronomical calculation.

### Supported Calculation Methods (`PrayerCalculationMethods`):
1. **Kemenag RI (Standard Indonesia)**: Subuh 20.0°, Isya 18.0° (Official standard of Indonesian Ministry of Religious Affairs).
2. **Muslim World League (MWL)**: Fajr 18.0°, Isha 17.0°.
3. **ISNA (Islamic Society of North America)**: Fajr 15.0°, Isha 15.0°.
4. **Umm Al-Qura (Makkah)**: Fajr 18.5°, Isha 90 min after Maghrib.
5. **Egyptian General Authority of Survey**: Fajr 19.5°, Isha 17.5°.
6. **University of Islamic Sciences, Karachi**: Fajr 18.0°, Isha 18.0°.
7. **Shia Ithna-Ashari / Leva Institute, Qum**: Fajr 16.0°, Isha 14.0°.
8. **Institute of Geophysics, University of Tehran**: Fajr 17.7°, Isha 14.0°.

### Asr Jurisprudence (Madhab):
- `SHAFI` (Standard: Shafi'i, Maliki, Hanbali) — Shadow length = object length + noon shadow.
- `HANAFI` — Shadow length = 2x object length + noon shadow.

---

## ⏱️ 2. Manual Ihtiyat Offsets (Koreksi Menit)

To ensure synchronization with local mosque timetables, users can configure minute offsets for each prayer time via `PrayerSettingsEntity`:
- `imsak_offset`: Minutes added to Imsak.
- `subuh_offset`: Minutes added to Subuh.
- `terbit_offset`: Minutes added to Sunrise/Syuruq.
- `dzuhur_offset`: Minutes added to Dzuhur.
- `ashar_offset`: Minutes added to Ashar.
- `maghrib_offset`: Minutes added to Maghrib.
- `isya_offset`: Minutes added to Isya.

---

## 🔔 3. Android Notification & Alarm Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                 PrayerAlarmScheduler (Android)              │
│        Schedules alarms via Android AlarmManager            │
└──────────────────────────────┬──────────────────────────────┘
                               │ Exact Alarm triggers at prayer time
┌──────────────────────────────▼──────────────────────────────┐
│                    PrayerAlarmReceiver                      │
│             Catches AlarmManager Broadcast                  │
└──────────────────────────────┬──────────────────────────────┘
                               │ Starts Foreground Service
┌──────────────────────────────▼──────────────────────────────┐
│                    PrayerAdzanService                       │
│  • Plays chosen Adzan audio (Misyari, Mansour, Mustafa, etc)│
│  • Shows High-Priority Heads-Up Notification                │
│  • Honors independent adzan_volume (0.0 to 1.0)             │
│  • Provides "Mute / Matikan Suara" action button            │
└─────────────────────────────────────────────────────────────┘
```

### Key Safety Rules:
1. **Independent Volume Slider**: The Adzan volume can be set from 0% (Silent / Mute) to 100% without disabling the visual notification.
2. **Doze Mode Immunity**: Uses `AlarmManager.setExactAndAllowWhileIdle()` so alarms ring reliably on Android 12+ (API 31+) power-saving modes.
3. **Auto Reschedule on Boot**: Alarms are rescheduled automatically when `BOOT_COMPLETED` is received.
