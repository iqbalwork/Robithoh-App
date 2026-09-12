# 📝 AI Agent Activity Changelog

All changes, architectural updates, and significant refactorings made by AI agents are permanently recorded here for traceability and auditability.

---

## [Unreleased]

### Quran Mushaf Page Curl Animation Spec, ADR & Implementation Plan
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Designed the 3D page curl animation engine for the Quran Mushaf reader (`QuranPageReaderScreen`). Authored the formal Technical Specification, ADR-0007, and step-by-step Implementation Plan.
- **Changes**:
  - `.agents/adr/0007-page-curl-book-turn-animation.md`: Authored ADR-0007 documenting the two-tier shader-free canvas strip renderer, mathematical model, and zero-network multiplatform constraints.
  - `.agents/spec/0001-page-curl-book-turn-animation.md`: Authored complete technical specification defining requirements (`REQ`, `SEC`, `PERF`, `CON`, `ACC`), cylinder wrap equations, vertical perspective taper, and dynamic lighting/shadow models.
  - `.agents/plan/0001-page-curl-book-turn-animation.md`: Created sequenced task breakdown covering Core Math, Design System components, Reader integration, and testing.
  - `.agents/AGENTS.md` & `.agents/CLAUDE.md`: Registered ADR-0007 and indexed `.agents/spec/`.
  - `.agents/memory/STATE.md`: Updated active focus to ADR-0007 & Page Curl implementation.

### Android Splash Screen & ANR Fix + iOS Compass & Location Improvements
- **Date**: 2026-09-10
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Fixed Android Splash Screen hanging/ANR caused by main thread blocking calls, fixed `AppSettings` database state loading race condition, and added True North geocoding/heading permissions on iOS.
- **Changes**:
  - `MainActivity.kt`: Moved `rescheduleFromDatabase` and widget updates (`PrayerWidgetHelper`, `TasbihWidgetHelper`, `TanbihWidgetHelper`, `QuranWidgetHelper`, `QuickAccessWidgetHelper`) off the UI thread into `CoroutineScope(Dispatchers.IO).launch` during `onCreate()` and `onResume()`.
  - `PrayerWidgetHelper.kt`: Guarded `Geocoder.getFromLocation()` to only reverse-geocode when location name is missing, avoiding blocking synchronous network calls.
  - `AppSettingsRepository.kt` & `App.kt`: Added explicit `isLoaded: Boolean` flag to `AppSettings` to ensure Splash Screen waits until the SQLite database read completes before routing.
  - `CompassSensor.ios.kt` & `Info.plist`: Added location authorization request and `requestLocation()` so CoreLocation can compute magnetic declination for True North heading calculations.

### Google Play In-App Update & Review Lifecycle Fix
- **Date**: 2026-09-10
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Fixed infinite "Installing..." / "Memasang pembaruan..." hanging dialog in `InAppUpdateManager` during flexible/immediate updates, added lifecycle guards to `InAppReviewManager`, and bumped build versionCode to 10.
- **Changes**:
  - `androidApp/.../update/InAppUpdateManager.kt`: Prevented `onResume()` from re-triggering `startUpdateFlow` when `installStatus == InstallStatus.INSTALLING`, and added `isDialogShowing` flag to prevent `AlertDialog` stacking.
  - `androidApp/.../review/InAppReviewManager.kt`: Added `activity.isFinishing || activity.isDestroyed` safety check before invoking `launchReviewFlow`.
  - `androidApp/proguard-rules.pro`: Added keep rules for `com.google.android.play.core.appupdate.**`, `install.**`, and `review.**` packages.
  - `androidApp/build.gradle.kts`: Bumped `versionCode` default from 9 to 10 (`versionName = "1.2.0"`).

### R8 & ProGuard Optimization Improvement
- **Date**: 2026-09-10
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Cleaned up overly broad package-wide keep rules in `proguard-rules.pro` to drastically improve R8 shrinking, obfuscation, and optimization rates in Google Play Console.
- **Changes**:
  - `androidApp/proguard-rules.pro`: Removed redundant package-wide `-keep class <package>.** { *; }` rules for Compose, SQLDelight, Koin, Media3, Firebase, Play Core, Adhan, Coroutines, and Android Components.
  - Relying on AAR consumer rules embedded in official dependencies and AAPT2 manifest merger rules for components.


### Dedicated Doa List Screen & Live Search
- **Date**: 2025-02-17
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Transformed the Doa menu from a Modal Bottom Sheet into a dedicated MVI screen (`DoaListScreen`) with real-time text query search and filtering.
- **Changes**:
  - `ScreenKey.kt`: Added `data object DoaList : ScreenKey`.
  - `feature/doa/presentation/DoaMvi.kt`: Created `DoaUiState`, `DoaUiIntent` (`SearchDoa`, `SelectDocument`), and `DoaUiEffect`.
  - `feature/doa/presentation/DoaViewModel.kt`: Created `DoaViewModel` to query and filter Doa & Ziarah documents from `MarkdownDocumentRepository`.
  - `feature/doa/ui/DoaListContent.kt`: Created `DoaListContent` composable with `IslamicHeader`, `OutlinedTextField` search bar, and `GoldCrimsonCard` prayer list.
  - `feature/doa/ui/DoaListScreen.kt`: Created `DoaListScreen` stateful wrapper handling `BackHandler` and navigation effects.
  - `HomeTabContent.kt`, `MainAppContainer.kt`, `App.kt`: Updated navigation routes so tapping "Doa" opens `DoaListScreen`.
  - `ModalBottomSheets.kt`: Removed deprecated `DoaModalBottomSheet`.
  - `.agents/adr/0006-doa-list-screen-and-search.md`: Documented ADR-0006.

### Liturgical Document Sync Notifications Update
- **Date**: 2026-03-31
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Replaced in-app popup toast overlay card (`DocumentSyncOverlay`) with live high-priority heads-up system notifications (`DocumentSyncNotifier`) with silent auto-check guard.
- **Changes**:
  - `DocumentSyncNotifier.kt`: Added `isManual` flag parameter to `notifySyncState(state, isManual)` to distinguish background app-launch checks from explicit user/update actions.
  - `DocumentSyncNotifier.android.kt`: Silenced notifications on automatic app launch when 0 files need updating (`isManual = false` and `updatedCount = 0`). High-priority popups only trigger when documents actually update (`updatedCount > 0`) or when manually requested (`isManual = true`).
  - `DocumentSyncManager.kt`: Passed `isManual` flag during state transitions (`Checking`, `Syncing`, `Success`, `Error`).
  - `.agents/adr/0005-document-sync-notifications.md`: Created ADR-0005 for document sync system notifications.

### Import & FQN Conventions Update
- **Date**: 2026-03-31
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Cleaned up inline fully qualified names (FQNs) and updated Agent Knowledge Base to prohibit inline FQN usages across Kotlin classes.
- **Changes**:
  - `.agents/AGENTS.md`: Added Core Rule 9 prohibiting inline fully-qualified package references in Kotlin code.
  - `.agents/references/CODE_CONVENTIONS.md`: Added "Imports & No Fully Qualified Names (FQNs)" guideline.
  - `.agents/skills/robithoh-mvi/SKILL.md`: Added Section 4 detailing import discipline.
  - Refactored `App.kt`, `HomeTabContent.kt`, `IslamicDivider.kt`, `PrayerAlarmScheduler.kt`, and `AppModule.kt` to use explicit `import` statements and short symbol names.

### Agent Harness Tahap 4: Loop Engineering System
- **Date**: 2026-09-08
- **Author**: Antigravity AI & Iqbal Fauzi
- **Scope**: Upgraded harness to an autonomous Loop Engineering system with MVI linter, sacred text SHA-256 guard, diagnostic parser, and loop protocol.
- **Changes**:
  - `rules/loop-protocol.md`: Formulated strict 6-step closed-loop execution protocol (Task -> Code -> Lint -> Test/Self-Fix -> Checkpoint).
  - `tools/check_mvi_architecture.sh`: Created MVI & UI separation linter enforcing pure stateless `*Content.kt` and sub-packaging (>250 lines). Fixed 2 unused ViewModel imports in `HomeTabContent.kt` and `SalatTabContent.kt`.
  - `tools/check_sacred_texts.sh`: Created SHA-256 integrity guard protecting 49 sacred liturgical files & database schemas against unapproved edits.
  - `rules/sacred_texts_hashes.json`: Generated initial baseline SHA-256 hashes for all sacred texts.
  - `tools/extract_build_errors.py`: Created surgical error log parser extracting Kotlin compilation & test assertion diagnostics for AI self-correction loop.
  - `tools/install_hooks.sh` & `.git/hooks/pre-commit`: Integrated all 4 quality linters into pre-commit pipeline.
  - `AGENTS.md` & `memory/STATE.md`: Synchronized tool index and active session state.
- **Validation**:
  - Verified `check_mvi_architecture.sh` execution (0 violations).
  - Verified `check_sacred_texts.sh` execution (49 files verified).
  - Verified `extract_build_errors.py` execution.
  - Verified `.git/hooks/pre-commit` hook pipeline.

---

### Agent Harness Enhancements & Hardening
- **Date**: 2026-09-08
- **Author**: Antigravity AI & Iqbal Fauzi
- **Scope**: Implemented key harness improvements, git hooks, asset validators, and root symlinks.
- **Changes**:
  - `CLAUDE.md`: Created root symlink to `.agents/AGENTS.md` for seamless multi-agent compatibility.
  - `.gitignore`: Updated to track `.agents/` and `.maestro/` in Git version control (ignoring only `.agents/scratch/`).
  - `tools/check_hardcoded_strings.sh`: Added `--ratchet` auto-update functionality; ratcheted baseline down from 700 to 690.
  - `tools/check_markdown_assets.sh`: Created new asset validator verifying all 48 pre-bundled Markdown liturgical files (UTF-8 encoding, zero null-byte corruption).
  - `tools/install_hooks.sh`: Created git pre-commit hook installer wiring up asset validation, string linting, and fast unit test verification.
  - `.git/hooks/pre-commit`: Installed local git hook executing all linters and test suites before commit.
  - `AGENTS.md` & `memory/STATE.md`: Updated tools index and active session state.
- **Validation**:
  - Verified `check_markdown_assets.sh` execution (48 files, 684.42 KB verified).
  - Verified `check_hardcoded_strings.sh --ratchet` execution (exited 0, baseline updated to 690).
  - Verified `install_hooks.sh` execution (pre-commit hook installed).

---

### Agent Harness Tahap 3: Custom Modular Skills & Linters
- **Date**: 2026-09-08
- **Author**: Antigravity AI & Iqbal Fauzi
- **Scope**: Implemented 6 modular skills and 2 automation/linter tools in `.agents/`.
- **Changes**:
  - `skills/robithoh-mvi/SKILL.md`: Standardized Compose Multiplatform MVI pattern (`UiState`, `UiIntent`, `UiEffect`), Screen/Content separation, and Arabic typography rules.
  - `skills/sqldelight-database/SKILL.md`: Guide for `RobithohDatabase.sq` queries, reactive flows, and `.sqm` migrations.
  - `skills/glance-widgets/SKILL.md`: Guide for the 8 Android Glance home screen widgets and update broadcasting.
  - `skills/maestro-e2e/SKILL.md`: Guide for writing and executing declarative YAML test flows in `.maestro/`.
  - `skills/kmp-audio-engine/SKILL.md`: Guidelines for interacting with `KmpAudioPlayer` and managing the persistent mini audio bar.
  - `skills/adr-authoring/SKILL.md`: Standard rules for authoring, numbering, and indexing ADRs.
  - `tools/check_hardcoded_strings.sh`: Python-powered ratchet linter scanning Compose files for unextracted raw strings (baseline 700).
  - `tools/run_maestro.sh`: Helper runner for executing Maestro UI flows against connected devices/emulators.
  - `AGENTS.md` & `CLAUDE.md`: Updated with "Available Skills" and "Tools" sections.
  - `memory/STATE.md`: Marked Tahap 3 complete; all roadmap items 100% finished.
- **Validation**:
  - Verified `check_hardcoded_strings.sh` execution (exited 0).
  - Verified `verify_build.sh --fast` execution (exited 0).

---

### Agent Harness Tahap 2: Domain Knowledge & Baseline ADRs
- **Date**: 2026-09-08
- **Author**: Antigravity AI & Iqbal Fauzi
- **Scope**: Established deep domain references and foundational ADRs in `.agents/`.
- **Changes**:
  - `references/OFFLINE_FIRST.md`: SQLDelight SQLite tables, 45+ Markdown docs in `composeResources`, zero-network guarantee.
  - `references/AUDIO_ENGINE.md`: `KmpAudioPlayer` expect/actual, Media3 foreground service on Android, AVPlayer on iOS, global floating audio bar.
  - `references/PRAYER_TIME_ENGINE.md`: BatoulApps Adhan 2 astronomy engine, calculation methods, minute offsets (ihtiyat), exact AlarmManager notifications with independent volume slider.
  - `references/WIDGET_ARCHITECTURE.md`: 8 Android Glance home screen widget variants and broadcast update synchronization.
  - `references/TESTING_AND_QA.md`: Two-tier QA framework (commonTest coroutine dispatcher rules & Maestro 10-flow E2E suite).
  - `adr/ADR_TEMPLATE.md`: Standardized template for authoring new ADRs.
  - `adr/0001-offline-first-sqlite-and-markdown.md`: Documented 100% offline-first architecture decision.
  - `adr/0002-cmp-mvi-presentation-architecture.md`: Documented Compose Multiplatform MVI UDF with Screen/Content separation.
  - `adr/0003-kmp-audio-player-expect-actual.md`: Documented multiplatform audio engine decision.
  - `adr/0004-glance-home-screen-widgets.md`: Documented 8 Android Glance home screen widgets decision.
  - `AGENTS.md` & `CLAUDE.md`: Synchronized Quick Reference Index and ADR catalogue.
  - `memory/STATE.md`: Updated active session context and roadmap checklist.
- **Validation**: Executed `verify_build.sh --fast` confirming all tests pass.

---

### Initial Agent Harness Setup (Tahap 1 - Foundation)
- **Date**: 2026-09-08
- **Author**: Antigravity AI & Iqbal Fauzi
- **Scope**: Created the foundational agent harness in `.agents/`.
- **Changes**:
  - `AGENTS.md` & `CLAUDE.md`: Central agent knowledge base, quick reference index, and non-negotiable core rules.
  - `rules/security-rules.md`: Security guardrails protecting keystore files and liturgy text integrity.
  - `references/PROJECT_SETUP.md`: Tech stack matrix, module boundaries, and build variants.
  - `references/AI_WORKFLOW.md`: 8-step AI development lifecycle.
  - `references/CODE_CONVENTIONS.md`: Strict MVI guidelines, Screen/Content separation, and KMP coding rules.
  - `memory/STATE.md`: Inter-session context tracker and active roadmap.
  - `tools/verify_build.sh`: Automated build sync, test runner (`:shared:allTests`), and compilation check (`:androidApp:assembleStagingDebug`).
- **Validation**: Verified build script execution and test passes.
