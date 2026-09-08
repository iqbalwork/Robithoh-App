# ADR-0002: Compose Multiplatform & Model-View-Intent (MVI) Presentation Architecture

*   **Status**: Accepted
*   **Date**: 2026-09-08
*   **Deciders**: Iqbal Fauzi, Antigravity AI
*   **Consulted**: Core Development Team
*   **Informed**: Engineering & QA

---

## 1. Context and Problem Statement
Robithoh App targets both Android and iOS platforms. Maintaining dual native UI codebases (Jetpack Compose on Android and SwiftUI on iOS) for 45+ complex devotional screens, Arabic typographic adjustments, and interactive dialogs doubles maintenance overhead and introduces UI parity drift.

## 2. Decision
We decided to adopt **Compose Multiplatform (1.11.1)** for 100% shared UI combined with the **Model-View-Intent (MVI)** architectural pattern:
1. Every feature screen is partitioned into:
   - `*Contract.kt`: Defining immutable `UiState`, sealed `UiIntent`, and sealed `UiEffect`.
   - `*ViewModel.kt`: Managing state transitions via Unidirectional Data Flow (UDF).
   - `*Screen.kt`: Stateful wrapper handling ViewModel injection, lifecycle state collection, and effect listening.
   - `*Content.kt`: Stateless, previewable UI container without direct ViewModel or DI dependencies.
2. iOS hosts the shared Compose UI via `ComposeUIViewController` inside `ContentView.swift`.

## 3. Rationale
* 100% visual and behavioral parity between Android and iOS.
* High testability of ViewModels in `commonTest` without UI or device dependencies.
* Clear separation of concerns between state hoisting and pure UI rendering.

## 4. Consequences
* **Good**: Write UI once, render identically on Android and iOS; easy Compose Previews.
* **Bad**: iOS startup and native integration requires careful bridging (e.g. keyboard and insets).
* **Neutral**: Developers must adhere strictly to the Screen vs Content separation pattern.

## 5. References
* [CODE_CONVENTIONS.md](../references/CODE_CONVENTIONS.md)
* [PROJECT_SETUP.md](../references/PROJECT_SETUP.md)
