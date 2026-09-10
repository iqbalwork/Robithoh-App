# ADR-0001: 100% Offline-First Architecture via SQLDelight & Bundled Markdown

*   **Status**: Accepted
*   **Date**: 2026-09-08
*   **Deciders**: Iqbal Fauzi, Antigravity AI
*   **Consulted**: Core Development Team
*   **Informed**: Engineering & QA

---

## 1. Context and Problem Statement
Robithoh App is a liturgical and devotional guide (*panduan ibadah dan amaliyah*) used by worshippers in diverse environments, including remote retreat centres (*khalwat/suluk*), basements, and travels without cellular signals. Relying on remote servers or CDNs for prayer texts, Quran surahs, or amaliyah leads to network failure, latency, and broken user experiences during worship.

## 2. Decision
We decided to adopt a strict **100% Offline-First Architecture**:
1. All structured operational data (bookmarks, Manaqib chapters, prayer settings, tasbih history, spotlight guide states) is persisted locally via **CashApp SQLDelight 2.0.2** generating type-safe SQLite access.
2. All 45+ liturgical guide texts are bundled directly into the application package as Markdown files in `composeResources/files/`.
3. All essential audio files and Arabic fonts are pre-bundled in the application binary.
4. No external network request is required or allowed for core app functionality.

## 3. Rationale
* Instant load time (0ms network latency).
* 100% reliability regardless of airplane mode, rural locations, or server outages.
* Respect for user privacy—no tracking or transmission of personal wirid and prayer habits.

## 4. Consequences
* **Good**: Guaranteed availability anywhere; zero server maintenance costs for core content.
* **Bad**: App binary size includes bundled assets (~50MB+ including audio assets).
* **Neutral**: Content updates to liturgical texts require app updates or local cache sync.

## 5. References
* [OFFLINE_FIRST.md](../references/OFFLINE_FIRST.md)
* [RobithohDatabase.sq](../../shared/src/commonMain/sqldelight/com/iqbalwork/robithoh/core/database/RobithohDatabase.sq)
