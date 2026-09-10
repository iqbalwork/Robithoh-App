# ADR-0006: Dedicated Doa List Screen with Live Search Capability

*   **Status**: Proposed
*   **Date**: 2025-02-17
*   **Deciders**: Iqbal Fauzi
*   **Consulted**: Domain Expert, Tech Lead
*   **Informed**: Development Team, QA

---

## 1. Context and Problem Statement
Previously, tapping the **Doa** menu item on the Home grid opened a `ModalBottomSheet` (`DoaModalBottomSheet`), which displayed a static list of 4 items (`Salam Wali Mursyid`, `Doa Rijalul Ghoib`, `Ziyaroh Rosul`, and `Doa Istighotsah`). 

As the Robithoh App's collection of liturgical documents and prayers expands (including Doa Manqobah Indonesia/Sunda, Dziarah Waliyulloh, Dziarah Kubur Umum, etc.), the modal bottom sheet format presents several UX constraints:
1. Limited screen height and visibility on various device form factors.
2. Lack of search and real-time filtering capabilities to help users quickly find specific prayers or liturgical documents.
3. Inconsistency with other dedicated feature pages (such as `QuranListScreen`, `ManaqibListScreen`, and `LanggamScreen`).

We need a dedicated screen for **Doa** that includes an interactive live search field and displays all relevant prayer and ziarah documents in a clean, categorized, and searchable layout.

## 2. Decision
We will transition the **Doa** menu from a `ModalBottomSheet` to a dedicated full-screen page (`DoaListScreen`) featuring real-time search functionality.

Specifically:
1. **New Screen Key**: Introduce `ScreenKey.DoaList` in `ScreenKey.kt` and register its route entry in `App.kt`.
2. **MVI Architecture**: Create a dedicated `feature/doa/` package following the Compose Multiplatform MVI pattern:
   - `DoaMvi.kt`: Defines `DoaUiState` (holding `searchQuery`, `documents`, `filteredDocuments`, `isLoading`), `DoaUiIntent` (`SearchDoa`, `SelectDocument`), and `DoaUiEffect`.
   - `DoaViewModel.kt`: Implements `MviViewModel` to load Doa & Ziarah documents from `MarkdownDocumentRepository` and apply real-time text query filtering.
   - `DoaListScreen.kt`: Stateful wrapper handling ViewModel state, back navigation, and user intents.
   - `DoaListContent.kt`: Stateless previewable UI component with an Islamic-themed search text field, header, and searchable prayer list cards.
3. **Data Source Integration**: Query all documents under category `"Doa & Ziarah"` plus related Doa items (e.g. `salam_wali_mursyid`, `doa_rijalul_ghoib`, `sholawat_jiyaaroh`, `doa_istighotsah`, `doa_manqobah_id`, `doa_manqobah_su`, `dziarah_waliyulloh`, `dziarah_umum`) from `MarkdownDocumentRepository`.
4. **Navigation Wiring**: Update `HomeTabContent.kt`, `MainAppContainer.kt`, and `App.kt` so tapping "Doa" on the Home grid navigates directly to `ScreenKey.DoaList` instead of opening `DoaModalBottomSheet`.
5. **Sheet Deprecation**: Deprecate `DoaModalBottomSheet` in `ModalBottomSheets.kt` and remove its invocation from `MainAppContainer.kt`.

## 3. Rationale
- **User Experience**: A full screen with a search field makes finding specific prayers immediate and intuitive, especially during amaliyah routines.
- **Consistency**: Aligns with the architectural standards of Robithoh App where major liturgical collections have dedicated MVI-backed screens (e.g., `QuranListScreen`, `ManaqibListScreen`).
- **Scalability**: Allows adding future Doa documents without crowding a bottom sheet.

## 4. Consequences
*   **Good**: Real-time search by title, subtitle, or keywords; cleaner UI layout; consistent navigation pattern.
*   **Bad**: Requires minor updates to UI tests (Maestro flows) that previously expected a modal bottom sheet.
*   **Neutral**: `DoaModalBottomSheet` becomes obsolete and can be safely removed.

## 5. References
*   [ADR-0002: Compose Multiplatform & MVI Presentation Architecture](0002-cmp-mvi-presentation-architecture.md)
*   [Code Conventions Reference](../references/CODE_CONVENTIONS.md)
