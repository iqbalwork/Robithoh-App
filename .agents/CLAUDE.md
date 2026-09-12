# 🤖 Robithoh App - Agent Knowledge Base

Quick index of project knowledge base, architectural conventions, and AI agent protocols. Read linked reference documents in [`references/`](references/) as needed during tasks.

---

## 📌 Quick Reference Index

| Topic | Reference Document | Key Takeaway |
| :--- | :--- | :--- |
| **Project Setup** | [PROJECT_SETUP.md](references/PROJECT_SETUP.md) | Module structure (`:shared`, `:androidApp`, `:iosApp`), JDK, build flavors (`staging`, `production`). |
| **AI Workflow** | [AI_WORKFLOW.md](references/AI_WORKFLOW.md) | 8-step AI development lifecycle (Brainstorm -> ADR -> KB Sync -> Contract -> Plan -> Code -> Verify -> Changelog). |
| **Code Conventions** | [CODE_CONVENTIONS.md](references/CODE_CONVENTIONS.md) | MVI pattern (`*Contract`, `*ViewModel`, `*Screen`, `*Content`), Screen/Content separation, Compose theming. |
| **Offline-First Data** | [OFFLINE_FIRST.md](references/OFFLINE_FIRST.md) | 100% offline-first: pre-bundled SQLDelight database, 45+ Markdown docs, local audio assets. |
| **Audio Playback** | [AUDIO_ENGINE.md](references/AUDIO_ENGINE.md) | `KmpAudioPlayer` expect/actual: Android Media3 foreground service vs iOS AVPlayer, floating player bar. |
| **Prayer & Adhan** | [PRAYER_TIME_ENGINE.md](references/PRAYER_TIME_ENGINE.md) | BatoulApps Adhan 2 astronomy, Ihtiyat offsets, AlarmManager exact alarms, independent volume slider. |
| **Glance Widgets** | [WIDGET_ARCHITECTURE.md](references/WIDGET_ARCHITECTURE.md) | 8 Android Glance home screen widgets & broadcast update state synchronization. |
| **Testing & QA** | [TESTING_AND_QA.md](references/TESTING_AND_QA.md) | 30+ shared unit tests in `commonTest`, coroutine dispatcher rules, Maestro E2E test flows. |
| **Security & Integrity** | [security-rules.md](rules/security-rules.md) | Non-negotiable protection of keystore credentials and sacred Arabic liturgical texts. |

---

## ⚠️ Non-Negotiable Core Rules

1. **100% Offline-First Architecture**: Robithoh App operates with zero required network connectivity. Never introduce remote network dependencies or cloud APIs for core liturgy and prayer features.
2. **MVI UI Separation**: Every screen must have a logic/stateful wrapper (`*Screen`) and a stateless previewable UI container (`*Content`). Complex sub-views must be split into a `components/` sub-package.
3. **No Hardcoded Strings**: All user-facing text must use Compose Multiplatform string resources (`Res.string.*`) or pre-bundled Markdown content. Never leave hardcoded strings in composables.
4. **Sacred Liturgy & Font Integrity**: Teks Arab (Al-Qur'an, Dzikir, Manaqib, Doa) adalah teks sakral. Dilarang mengubah teks tanpa instruksi eksplisit. Pastikan `lineHeight` Compose cukup tinggi (≥ 32.sp) agar tanda harakat tidak terpotong.
5. **Keystore Guardrail**: Dilarang membaca, mengubah, atau menampilkan isi file `keystore.jks` dan `keystore.properties`.
6. **ADR Before Big Changes**: Any substantial architecture change, new feature, or refactoring must be documented in an ADR in [`adr/`](adr/) before modifying code.
7. **Verify Before Handover**: Run `.agents/tools/verify_build.sh` to ensure all unit tests pass and compilation succeeds before concluding any task.
8. **Memory & Changelog Sync**: Read [`memory/STATE.md`](memory/STATE.md) at session start, and record changes in [`changelog/AI_CHANGELOG.md`](changelog/AI_CHANGELOG.md) and [`memory/STATE.md`](memory/STATE.md) before finishing.

---

## 🛠️ Harness & Tools

* **Technical Specifications**: [`spec/`](spec/) (formal requirements, constraints & geometry specifications).
* **Memory**: [`memory/STATE.md`](memory/STATE.md) (active session context & pending roadmap).
* **Implementation Plans**: Granular step-by-step task lists saved in [`plan/`](plan/) before execution.
* **Verification Pipeline**: [`tools/verify_build.sh`](tools/verify_build.sh) (compilation & allTests runner).
* **Hardcoded Strings Linter**: [`tools/check_hardcoded_strings.sh`](tools/check_hardcoded_strings.sh) (ratchet scanner for raw UI strings).
* **Maestro E2E Runner**: [`tools/run_maestro.sh`](tools/run_maestro.sh) (executes UI flows on connected device).
* **Rules**: Enforcement rules in [`rules/`](rules/) (RTK proxy, security rules).
* **Audit Trail**: Record completed changes in [`changelog/AI_CHANGELOG.md`](changelog/AI_CHANGELOG.md).

---

## 🪄 Available Skills

### Presentation & UI
- **[Robithoh MVI Architecture](skills/robithoh-mvi/SKILL.md)**: Compose Multiplatform MVI pattern, `*Contract`, `*ViewModel`, `*Screen`, `*Content`, and Arabic text styling.
- **[Android Glance Widgets](skills/glance-widgets/SKILL.md)**: Developing and updating the 8 Android Glance home screen launcher widgets.

### Data & Multimedia
- **[SQLDelight Database](skills/sqldelight-database/SKILL.md)**: Schema definitions, `.sq` queries, reactive Flows, and SQLite migrations.
- **[KMP Audio Engine](skills/kmp-audio-engine/SKILL.md)**: Multiplatform background playback, Android Media3 service, iOS AVPlayer, and floating player bar.

### Process & Testing
- **[ADR Authoring](skills/adr-authoring/SKILL.md)**: Authoring, numbering, and indexing Agent Decision Records.
- **[Maestro E2E Testing](skills/maestro-e2e/SKILL.md)**: Writing, maintaining, and running declarative UI tests in `.maestro/`.

---

## 🏛️ Agent Decision Records (ADRs)

Full history documented in [`adr/`](adr/).

- **[ADR-0001: 100% Offline-First Architecture via SQLDelight & Bundled Markdown](adr/0001-offline-first-sqlite-and-markdown.md)** — Zero-network design using local SQLite and pre-packaged Markdown assets.
- **[ADR-0002: Compose Multiplatform & MVI Presentation Architecture](adr/0002-cmp-mvi-presentation-architecture.md)** — 100% shared declarative UI with strict Screen/Content separation.
- **[ADR-0003: Multiplatform Audio Engine (`KmpAudioPlayer`) Bridging Media3 & AVPlayer](adr/0003-kmp-audio-player-expect-actual.md)** — Foreground service audio on Android and AVPlayer audio session on iOS.
- **[ADR-0004: 8 Android Home Screen Widgets via Glance & AppWidgetProvider](adr/0004-glance-home-screen-widgets.md)** — Interactive prayer, tasbih, Quran bookmark, and Tanbih widgets with broadcast update synchronization.
- **[ADR-0005: System Notifications for Liturgical Document Sync Progress](adr/0005-document-sync-notifications.md)** — Replacing toast overlay UI with live activity progress system notifications.
- **[ADR-0006: Dedicated Doa List Screen with Live Search Capability](adr/0006-doa-list-screen-and-search.md)** — Transitioning Doa menu from a modal bottom sheet to a dedicated MVI screen with live search.
- **[ADR-0007: Page Curl (Book Turn) Animation for the Quran Mushaf Reader](adr/0007-page-curl-book-turn-animation.md)** — Two-tier shader-free page curl driven by HorizontalPager for the single-page Quran reader.

---

## 🪄 Versioning & License
Internal development at **Robithoh App**.  
Version: `1.2.0`  
Author: **Iqbal Fauzi**
