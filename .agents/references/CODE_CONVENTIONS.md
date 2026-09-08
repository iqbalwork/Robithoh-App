# 📐 Robithoh App - Code Conventions & Architecture Guidelines

This guide defines the non-negotiable coding and architectural standards for all code written in **Robithoh App**.

---

## 🏛️ 1. Presentation Layer: MVI Architecture

Every screen in Robithoh App follows the **Model-View-Intent (MVI)** pattern with Unidirectional Data Flow (UDF):

```
User Action ──► UiIntent ──► ViewModel ──► UiState ──► Screen / Content
                                  │
                                  └──► UiEffect (One-shot event) ──► Screen
```

### File Separation Pattern:
For every screen feature (e.g., `Quran`, `Manaqib`, `Tasbih`, `Amaliyah`):
1. `*Contract.kt`:
   - `data class *UiState(...)`: Immutable state representing the complete screen state.
   - `sealed interface *UiIntent`: All possible user interactions or triggers.
   - `sealed interface *UiEffect`: One-shot side-effects (Navigation, Toast, Haptic, Dialog).
2. `*ViewModel.kt`:
   - Extends KMP-compatible ViewModel (or AndroidX Lifecycle ViewModel).
   - Exposes `val uiState: StateFlow<*UiState>`.
   - Exposes `val uiEffect: SharedFlow<*UiEffect>` / `Channel<*UiEffect>`.
   - Implements `fun onIntent(intent: *UiIntent)` to process state transitions.
3. `*Screen.kt` (Stateful Entry Composable):
   - Injects the ViewModel via Koin (`koinViewModel()`).
   - Collects `uiState` with `collectAsStateWithLifecycle()`.
   - Collects `uiEffect` in a `LaunchedEffect` block.
   - Passes pure lambdas and state down to `*Content`.
4. `*Content.kt` (Stateless UI Composable):
   - Accepts only data parameters and event callbacks (`onEvent: () -> Unit`).
   - Contains **NO** ViewModel references or DI lookups.
   - Must be previewable with Compose Previews.
   - Sub-components must be factored into a `components/` sub-package if the file exceeds ~250 lines.

---

## 🎨 2. Compose UI & Design Tokens

* **Theme Tokens**: Use `MaterialTheme.colorScheme` or custom tokens from `core/designsystem/`.
* **Primary Palette**:
  - *Primary Crimson*: `#8B1E1E` / `#A11E22`
  - *Accent Gold*: `#D4AF37` / `#C5A059`
  - *Paper Cream Background*: `#FAF7F2`
  - *Charcoal Text*: `#1C1917`
* **Text & Typography**:
  - Latin text: Plus Jakarta Sans.
  - Arabic text: Amiri Quran or Scheherazade New.
  - **Crucial**: Always configure line height generously (e.g., `lineHeight = 36.sp` for 24.sp Arabic text) to prevent harakat vowel marks from clipping.
* **Strings & Localization**:
  - All user-facing strings must use `Res.string.*` (Compose Multiplatform Resources) or `strings.xml`.
  - Never hardcode raw user-facing Indonesian strings in composables.
* **Imports & No Fully Qualified Names (FQNs)**:
  - Never write inline fully qualified names such as `com.iqbalwork.robithoh.core.database.rememberRobithohDatabase()`.
  - Always add explicit `import` statements at the top of the Kotlin file and reference classes, functions, and properties directly by their short names.

---

## 💾 3. Data & Offline-First Layer

* **SQLDelight**:
  - Schema definitions live in `.sq` files under `shared/src/commonMain/sqldelight/`.
  - Use SQLDelight Kotlin queries wrapped in Repositories.
  - Expose reactive streams via `asFlow().mapToList()` or `mapToOneOrNull()`.
* **Document Liturgy (Markdown)**:
  - 45+ liturgical guide files are stored as pre-bundled `.md` files in `composeResources/files/`.
  - Parsed and served via `AmaliyahRepository` / `MarkdownRepository`.
* **No Remote Network**:
  - Do not introduce Ktor network clients or REST/GraphQL calls for liturgy data. The app is strictly 100% offline-first.

---

## 💉 4. Dependency Injection (Koin 4)

* All modules are registered in `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/di/`:
  - `AppModule.kt`: Core utilities, Database driver, Dispatchers.
  - `ViewModelModule.kt`: ViewModels registered via `viewModel { ... }` or `viewModelOf(...)`.
  - `AudioModule.kt`: Audio player abstractions and platform adapters.
* Keep module declarations clean and centralized.

---

## 🧪 5. Testing Discipline

* Unit tests in `shared/src/commonTest/`:
  - Test ViewModels using `StandardTestDispatcher` and `runTest`.
  - Use Turbine for testing `StateFlow` and `SharedFlow` emissions (`viewModel.uiState.test { ... }`).
  - Verify edge cases, initial states, and error states.
* E2E tests in `.maestro/`:
  - Ensure declarative flows in `.maestro/flows/` remain intact when UI hierarchies or key test tags change.
