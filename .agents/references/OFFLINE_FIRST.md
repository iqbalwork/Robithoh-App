# 📴 Offline-First Architecture & Data Strategy

This document details the 100% offline-first architecture of **Robithoh App**.

---

## 🎯 Core Philosophy: Zero-Network Dependency

Robithoh App is designed as a sanctuary of worship (*panduan amaliyah*) that worshippers can rely upon anywhere—inside mountain retreats (*khalwat/suluk*), during travels, inside basements, or in areas with zero cellular connectivity.

Therefore:
1. **Core Liturgy Content**: Never relies on remote APIs, CDNs, or cloud databases.
2. **Zero Loading Spinners for Text**: All amaliyah documents, Quranic verses, and prayer guides render immediately from local storage.
3. **Local Assets**: All liturgical audio, Arabic fonts, and vector graphics are pre-bundled inside the application binary.

---

## 💾 1. SQLDelight Database (`RobithohDatabase.sq`)

The local persistence layer is powered by **CashApp SQLDelight 2.0.2** generating type-safe Kotlin APIs over SQLite.

### Database Tables:

| Table | Purpose | Key Columns |
| :--- | :--- | :--- |
| `ManqobahEntity` | 56 chapters of Manaqib Syaikh Abdul Qodir Al-Jailani r.a. | `chapter_number`, `title_arabic`, `title_indonesian`, `title_sundanese`, `content_arabic`, `content_indonesian`, `content_sundanese`, `audio_path` |
| `BookmarkEntity` | User bookmarks & last-read markers across Quran and Kitab | `item_type`, `item_id`, `title`, `page_or_surah`, `verse_or_section`, `updated_at` |
| `AmaliyahProgressEntity` | Persistent counters for daily wirid & tasbih | `id`, `title`, `current_count`, `target_count`, `is_completed` |
| `PrayerSettingsEntity` | Persistent calculation settings, offsets, and notification modes | `method_id`, `imsak_offset`..`isya_offset`, `madhab`, `custom_lat`, `custom_lng`, `selected_adzan_voice_id`, `adzan_volume` |
| `ReaderSettingsEntity` | Global document viewer typography & theme preference | `font_scale` (0.85 - 1.65), `theme_id` (`system`, `cream`, `white`, `dark`) |
| `AppSettingsEntity` | Onboarding completion flag & interactive spotlight guideline states | `has_completed_onboarding`, `has_seen_reader_spotlight`, `has_seen_prayer_spotlight`, `has_seen_quran_spotlight` |
| `CachedDocumentEntity` | Offline cached Markdown content with SHA-256 integrity hash | `file_name`, `sha256`, `content`, `updated_at` |

---

## 📜 2. Pre-Bundled Liturgy Documents (Markdown)

Over 45+ liturgical and devotional texts are stored in `shared/src/commonMain/composeResources/files/`:
- Dzikir Ba'da Sholat & Khotaman TQN.
- Panduan Sholat Sunnah (Harian, Bulanan, Tahunan, Safar, Rajab, Nisfu Sya'ban, Lailatul Qadar, Lidaf'il Bala).
- Sholawat Thoriqiyyah, Bani Hasyim, Badriyyah, Jiyaaroh, dll.
- Panduan Ziarah & Adab Maqom.
- Sejarah & Profil Pesantren Sirnarasa & Abah Aos.

### Access Pattern:
Repository (`AmaliyahRepository` / `MarkdownRepository`) loads documents via `Res.readBytes("files/<filename>.md")`, caches them in memory, and parses headings, Arabic verses, Latin transliterations, and Indonesian/Sundanese translations.

---

## 🎵 3. Pre-Bundled Audio recitations

All core liturgical audios are stored in `shared/src/commonMain/composeResources/files/`:
- `audio/langgam_fatihah_*.mp3`
- `audio/dzikir_jahr.mp3`
- `audio/dzikir_khofi.mp3`
- `audio/sholawat_bani_hasyim.mp3`
- `audio/adzan_*.mp3`

These tracks are played directly through `KmpAudioPlayer` without network bandwidth consumption.
