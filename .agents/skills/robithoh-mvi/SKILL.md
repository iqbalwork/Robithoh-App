---
name: robithoh-mvi
description: Standards and guidelines for creating and modifying screens in Robithoh App using Compose Multiplatform and Model-View-Intent (MVI) architecture with strict Screen vs Content separation.
---

# MVI Architecture & Screen Componentization

Robithoh App enforces a strict **Model-View-Intent (MVI)** Unidirectional Data Flow (UDF) pattern for all UI features.

---

## 1. File Structure Convention

Every screen module under `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/<feature>/presentation/` must contain:

1. **`*Contract.kt`**:
   - Immutable `data class *UiState(...) : UiState`
   - Sealed `sealed interface *UiIntent : UiIntent`
   - Sealed `sealed interface *UiEffect : UiEffect`
2. **`*ViewModel.kt`**:
   - Extends `MviViewModel<*UiState, *UiIntent, *UiEffect>`
   - Processes user intents via `override fun onIntent(intent: *UiIntent)`
   - Mutates state via `updateState { copy(...) }`
   - Emits one-shot events via `sendEffect(*UiEffect.*)`
3. **`*Screen.kt`**:
   - Stateful entry composable
   - Injects ViewModel via Koin: `val viewModel: *ViewModel = koinViewModel()`
   - Collects `uiState` via `viewModel.uiState.collectAsStateWithLifecycle()`
   - Listens to `uiEffect` in `LaunchedEffect` block
   - Delegates rendering to `*Content`
4. **`*Content.kt`**:
   - Stateless UI container
   - Accepts state as pure values and callbacks as lambdas (`onEvent: () -> Unit`)
   - **Zero** ViewModel references or DI lookups
   - Contains `@Preview` composable previews
5. **`components/` Sub-folder**:
   - If `*Content.kt` exceeds ~250 lines, factor individual cards, dialogs, and sections into sub-composables inside a `components/` package.

---

## 2. Arabic Typography & Text Rules

When rendering Arabic liturgy texts (Al-Qur'an, Dzikir, Manaqib, Doa):
- Use `Amiri Quran` or `Scheherazade New` fonts.
- **Mandatory line height padding**: Always set `lineHeight` sufficiently tall (e.g. `lineHeight = 36.sp` for 24.sp text) so that upper and lower harakat vowel marks (fathah, kasrah, dhommah, tanwin, shaddah) never clip.
- Respect user-configured font scale from `ReaderSettingsRepository` (0.85x to 1.65x).

---

## 3. Localization & Strings

- All static user-facing text must use Compose Multiplatform Resources: `stringResource(Res.string.<key>)`.
- Never hardcode raw strings like `Text("Kembali")` or `Text("Simpan")` directly in composables.

---

## 4. Import Discipline & No Inline FQNs

- Never write inline fully qualified names such as `com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()`.
- Always `import` symbols at the top of the file and reference classes, functions, and properties directly.

