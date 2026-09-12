# 🧠 Robithoh App - Agent Active State & Context Memory

> **Instructions for AI Agents**: Read this file at the start of every session to inherit context from previous sessions. Update this document before finishing your task to prepare context for future sessions.

---

## 📌 Current Status

* **Project Version**: `1.2.0` (Code `10`)
* **Current Focus**: Android Splash Screen ANR & Main Thread Blocking Fix, iOS Compass Sensor & True North Location Setup.
* **Codebase Health**:
  - `shared`: Clean architecture, explicit import convention enforced, 125 passing unit test suites.
  - `androidApp`: Main thread blocking widget/alarm updates moved to Dispatchers.IO, Geocoder guarded, InAppUpdateManager & InAppReviewManager lifecycle fixed, versionCode 10, 8 Glance widgets implemented.
  - `iosApp`: CoreLocation True North declination setup enabled with LocationWhenInUse usage description in Info.plist.
  - `E2E`: Full Maestro test suite in `.maestro/` (10 flows verified).
  - `Linters & Tools`: `verify_build.sh`, `check_mvi_architecture.sh`, `check_sacred_texts.sh`, `check_markdown_assets.sh` verified.

---

## 🎯 Completed Roadmap

- [x] **Agent Harness Tahap 1 (Foundation)**:
  - [x] Master `AGENTS.md` and `CLAUDE.md`.
  - [x] Security and asset protection rules (`rules/security-rules.md`).
  - [x] Technical references (`PROJECT_SETUP.md`, `AI_WORKFLOW.md`, `CODE_CONVENTIONS.md`).
  - [x] Memory & Changelog scaffolding (`memory/STATE.md`, `changelog/AI_CHANGELOG.md`).
  - [x] Fast automated verification script (`tools/verify_build.sh`).
- [x] **Agent Harness Tahap 2 (Domain Knowledge & Baseline ADRs)**:
  - [x] `references/OFFLINE_FIRST.md` (SQLDelight schema, 45+ Markdown docs, bundled audio).
  - [x] `references/AUDIO_ENGINE.md` (KmpAudioPlayer, Media3 Android vs AVPlayer iOS, floating player bar).
  - [x] `references/PRAYER_TIME_ENGINE.md` (Adhan 2 astronomical engine, Ihtiyat, AlarmManager, volume slider).
  - [x] `references/WIDGET_ARCHITECTURE.md` (8 Android Glance widgets & broadcast update synchronization).
  - [x] `references/TESTING_AND_QA.md` (Unit test Coroutines rules & Maestro 10-flow suite).
  - [x] ADR Template & Baseline ADRs (`ADR-0001` s/d `ADR-0004`).
- [x] **Agent Harness Tahap 3 (Custom Skills & Linters)**:
  - [x] Custom modular skills:
    - [x] `skills/robithoh-mvi/SKILL.md`
    - [x] `skills/sqldelight-database/SKILL.md`
    - [x] `skills/glance-widgets/SKILL.md`
    - [x] `skills/maestro-e2e/SKILL.md`
    - [x] `skills/kmp-audio-engine/SKILL.md`
    - [x] `skills/adr-authoring/SKILL.md`
  - [x] Automation tools & linters:
    - [x] `tools/check_hardcoded_strings.sh` (ratchet scanner for hardcoded UI strings, baseline ratcheted to 690).
    - [x] `tools/check_markdown_assets.sh` (validates all 48 pre-bundled Markdown docs).
    - [x] `tools/install_hooks.sh` (installs `.git/hooks/pre-commit` verification pipeline).
    - [x] `tools/run_maestro.sh` (Maestro UI test runner).
- [x] **Agent Harness Tahap 4 (Loop Engineering System)**:
  - [x] `rules/loop-protocol.md`: Closed-loop autonomous execution cycle protocol.
  - [x] `tools/check_mvi_architecture.sh`: MVI architecture & pure stateless Content linter (scanned & verified).
  - [x] `tools/check_sacred_texts.sh` & `rules/sacred_texts_hashes.json`: SHA-256 integrity guard for 49 sacred liturgical files & .sq schemas.
  - [x] `tools/extract_build_errors.py`: Compiler error & test failure diagnostic parser for self-correction loop.
  - [x] `.git/hooks/pre-commit`: Updated pre-commit hook executing 4 automated quality linters before every commit.

---

## ⚠️ Known Notes & Watchouts

1. **Keystore Security**: Keystore files (`keystore.jks`, `keystore.properties`) are strictly protected and gitignored. Do not touch or print in logs.
2. **Offline-First Contract**: All liturgy texts, Quran data, and audio recitations must stay local within the app bundle. Never introduce remote network dependencies for liturgy data.
3. **Arabic Text Rendering**: Ensure `lineHeight` in Compose is sufficiently large (≥ 32.sp) to prevent clipping of vowel marks (harakat/tajwid).
4. **Coroutine Dispatcher in ViewModel Tests**: Always configure `Dispatchers.setMain(testDispatcher)` in `@BeforeTest` and `Dispatchers.resetMain()` in `@AfterTest` when testing ViewModels with `viewModelScope`.
