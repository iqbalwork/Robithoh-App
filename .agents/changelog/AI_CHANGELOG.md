# 📝 AI Agent Activity Changelog

All changes, architectural updates, and significant refactorings made by AI agents are permanently recorded here for traceability and auditability.

---

## [Unreleased]

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
