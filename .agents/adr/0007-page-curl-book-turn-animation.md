# ADR-0007: Page Curl (Book Turn) Animation for the Quran Mushaf Reader

*   **Status**: Proposed
*   **Date**: 2026-09-12
*   **Deciders**: Antigravity (AI Agent), Iqbal Fauzi
*   **Consulted**: UI/UX Designer, Tech Lead
*   **Informed**: Development Team, QA

---

## 1. Context and Problem Statement

`QuranPageReaderScreen` currently advances pages with a stock `HorizontalPager(reverseLayout = true)` translation slide (`QuranPageReaderScreen.kt`). While the screen invests considerable visual polish in evoking a physical mushaf — `SpineEdgeShadow`, `SpineCenterDivider`, outer paper cut borders, and a dual-page spread on wide screens — the flat horizontal slide breaks the physical book illusion during page turns.

We want to achieve a realistic **paper book curl animation** (comparable to Google Play Books and Apple Books) where a sheet rotates about a vertical axis, wrapping a virtual cylinder near the fold, with a specular highlight on the curl ridge, a soft cast shadow on the page beneath, a dimmed back face past 90°, and progressive reveal of the incoming page.

The technical constraints are:
1. **Multiplatform Matrix**: Compose Multiplatform 1.11+, Kotlin 2.4+, Android `minSdk 24`, plus iOS. Android AGSL `RuntimeShader` is restricted to API 33+ and unavailable on iOS; Skiko's `RuntimeEffect` is not available in `commonMain`.
2. **Existing Pager Architecture**: `HorizontalPager` owns critical features: initial page restore, `lastViewedPage` database persistence, `animateScrollToPage` from surah/ayah pickers, audio-follow auto-scrolling (`activeAudioAyah`), and neighbor page prefetching via `QuranPageManager`. These must not be broken or rewritten.
3. **Content Format**: Mushaf pages are pre-rendered into `ImageBitmap` and drawn via `Image(bitmap = pageImage)` in `MushafPageView`. This enables direct pixel slice and canvas manipulation without dynamic view-hierarchy rasterization.
4. **Sacred Liturgy & Font Integrity (Core Rule 4)**: Any resampling or strip drawing must not clip, smear, or distort Arabic harakat and tajwid marks.
5. **Form Factors**: Portrait single-page reading is the primary target. Dual-page spreads (`maxWidth >= 600.dp`) and landscape scrolling present different layout mechanics and will be scoped appropriately.

---

## 2. Decision

We decided to implement a **two-tier, shader-free page curl engine** in `commonMain`, driven directly by the existing `HorizontalPager` state, scoped to the Quran Mushaf reader.

### 2.1 Two-Tier Renderer Architecture

*   **Tier B — Cylindrical Curl (Primary)**:
    *   Models the turning page as wrapping around a virtual vertical cylinder of radius $r$.
    *   Fold line moves with turn progress $p$: $f = W \cdot (1 - p)$.
    *   Radius $r$ decays linearly towards $0$ as $p \to 1.0$, preventing any leftover curved lobe at the spine.
    *   The page is divided into 24–64 vertical strips based on display width.
    *   Each strip is rendered via `DrawScope.withTransform` with sub-pixel clipping and a $0.5\text{ px}$ horizontal seam overlap.
    *   Includes a $1.5\%$ vertical perspective taper on the curled section.
    *   Includes dynamic lighting effects:
        1. Ambient/diffuse cosine shading across the curl curve.
        2. Soft linear gradient cast shadow onto the underlying page.
        3. Specular highlight band along the apex of the curl ridge.
        4. Dimmed/translucent back face when the page turns past $90^\circ$.
*   **Tier A — Rigid 3D Flip (Fallback)**:
    *   Uses `Modifier.graphicsLayer` with `rotationY`, `cameraDistance`, and `transformOrigin` pinned to the spine edge.
    *   Provides a zero-resampling, hardware-accelerated fallback for low-spec devices, accessibility reduced motion, or if frame-rate degradation is detected.

### 2.2 Integration with Existing HorizontalPager

Rather than replacing `HorizontalPager`, the curl engine hooks into `pagerState.currentPageOffsetFraction` and neutralizes the default slide translation:
```kotlin
val offset = (pagerIndex - pagerState.currentPage) - pagerState.currentPageOffsetFraction
Modifier.graphicsLayer { translationX = -offset * size.width }
```
The resulting normalized `offset` $(0.0 \dots 1.0)$ directly drives the curl progress. All existing fling gestures, velocity tracking, snapping, and programmatic animations remain fully functional.

### 2.3 Modular Package Structure

Components are isolated under `com.iqbalwork.robithoh.core.designsystem.component.pageturn`:
*   `PageTurnMath.kt`: Pure Kotlin mathematical cylinder projection and taper geometry (zero Compose dependencies, 100% unit testable in `commonTest`).
*   `PageTurnStyle.kt`: User preference enum (`CURL`, `SLIDE`, `NONE`).
*   `PageTurnState.kt`: State holder tracking progress, direction, spine side, and active tier.
*   `CurlRenderer.kt`: `DrawScope` canvas strip renderer (Tier B) with shadows and specular highlight.
*   `RigidFlipModifier.kt`: `graphicsLayer` 3D rotation modifier (Tier A).
*   `PageTurnPerformanceGuard.kt`: Real-time frame timing sampler that automatically falls back to Tier A if consecutive dropped frames occur.

### 2.4 Scope & Guards

*   **In Scope**: Single-page portrait Quran mushaf reader (`QuranPageReaderScreen`).
*   **Deferred**: Dual-page spread (`maxWidth >= 600.dp`) and landscape mode (which utilizes vertical scrolling).
*   **Animation Timing**: $420\text{ ms}$ snap duration with `CubicBezierEasing(0.22f, 0.9f, 0.24f, 1f)`.
*   **Accessibility**: Respects system "Reduce Motion" setting via `isReducedMotionEnabled()`. If enabled, defaults to `SLIDE` or `NONE`.
*   **Ayah Highlight**: Ayah bounding-box highlight overlay is suppressed during the $420\text{ ms}$ turn animation to prevent visual distortion.

---

## 3. Rationale

1. **Why not AGSL Runtime Shaders**: AGSL requires Android API 33+, leaving older Android devices (minSdk 24) unsupported, and cannot run on iOS. A strip-based canvas renderer runs identically on all targets in `commonMain`.
2. **Why not a bespoke gesture widget**: Re-implementing fling physics, snap thresholds, velocity calculation, page caching, and audio-synchronization would introduce immense regression risk. Offsetting `HorizontalPager` leverages robust, proven infrastructure.
3. **Why 24–64 strips**: Video frame analysis of real book turns shows that deformation is primarily horizontal cylindrical wrapping with only $\sim 1.5\%$ vertical perspective taper. Slicing into $24 - 64$ strips provides silky curvature without the memory overhead of a 3D vertex mesh.
4. **Why 0.5 px overlap**: Sub-pixel canvas rendering on high-DPI displays can cause $1\text{ px}$ background bleed between strip boundaries. A $0.5\text{ px}$ overlap guarantees zero vertical seam artifacts on sacred Arabic script.

---

## 4. Consequences

*   **Good**:
    *   Delivers a premium, skeuomorphic reading experience matching Google Play Books.
    *   100% multiplatform (`commonMain`) with zero third-party dependencies.
    *   Fully unit-testable math and geometry in `commonTest`.
    *   Existing pager logic, audio tracking, and surah navigation remain untouched.
*   **Bad**:
    *   Drawing 24–64 strips per frame increases draw calls during turns; mitigated by the Tier A fallback and `PageTurnPerformanceGuard`.
    *   Ayah highlight overlay is temporarily hidden for $420\text{ ms}$ while the page is turning.
*   **Neutral**:
    *   Adds a user preference toggle (`PageTurnStyle`) in settings.

---

## 5. References

*   [Implementation Plan](../plan/0001-page-curl-book-turn-animation.md)
*   `QuranPageReaderScreen.kt` & `MushafPageView.kt`
*   Core Rule 4 (Sacred Liturgy & Font Integrity) in `.agents/AGENTS.md`
