---
name: sqldelight-database
description: Guidelines for managing the SQLite database using CashApp SQLDelight in Robithoh App. Covers schema definitions, writing .sq queries, reactive Flow queries, and safe schema migrations.
---

# SQLDelight Database Architecture

Robithoh App uses **CashApp SQLDelight 2.0.2** for all local structured persistence.

---

## 1. Schema & Query Location

All database tables and queries reside in:
`shared/src/commonMain/sqldelight/com/iqbalwork/robithoh/core/database/RobithohDatabase.sq`

### Existing Entity Tables:
- `ManqobahEntity`: 56 chapters of Manaqib with Arabic, Indonesian, and Sundanese texts.
- `BookmarkEntity`: Quran and amaliyah user bookmarks.
- `AmaliyahProgressEntity`: Persistent wirid and tasbih counters.
- `PrayerSettingsEntity`: Calculation parameters, offsets, and notification modes.
- `ReaderSettingsEntity`: Global font scale and theme selection.
- `AppSettingsEntity`: Onboarding and spotlight guideline flags.
- `CachedDocumentEntity`: Offline cached Markdown content.

---

## 2. Writing Queries in `.sq`

Format:
```sql
-- Query Name:
getQueryName:
SELECT * FROM TableName WHERE column = :param;

-- Insert or Update:
insertOrUpdateItem:
INSERT OR REPLACE INTO TableName(col1, col2) VALUES (?, ?);
```

---

## 3. Reactive Flows in Repositories

Convert SQLDelight queries to Coroutine Flows using SQLDelight's coroutines extensions:

```kotlin
// In Repository implementation:
fun getBookmarksFlow(): Flow<List<BookmarkEntity>> {
    return queries.getAllBookmarks()
        .asFlow()
        .mapToList(Dispatchers.Default)
}

fun getSettingsFlow(): Flow<PrayerSettingsEntity?> {
    return queries.getPrayerSettings()
        .asFlow()
        .mapToOneOrNull(Dispatchers.Default)
}
```

---

## 4. Schema Migrations (`.sqm`)

- When modifying existing tables or columns, **DO NOT** modify the `.sq` file destructively without creating an `.sqm` migration file in the migrations folder (e.g. `1.sqm`).
- Test that existing user installs will upgrade smoothly without database corruption.
