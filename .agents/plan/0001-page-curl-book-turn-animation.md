# Implementation Plan: Google Play Books-Style Page Curl (Book Turn) Animation

* **Feature**: Page Curl Animation for Quran Mushaf Reader
* **Target Version**: `1.2.0`
* **ADR Reference**: [ADR-0007: Page Curl (Book Turn) Animation for the Quran Mushaf Reader](../adr/0007-page-curl-book-turn-animation.md)
* **Date**: 2026-09-12

---

## 1. Scope and Prerequisites

### In Scope
- Single-page portrait Quran mushaf reader (`QuranPageReaderScreen.kt`).
- Two-tier rendering engine:
  - **Tier B (Primary)**: Cylindrical strip projection with specular ridge highlight, dynamic cast shadow, back-face dimming, and perspective vertical taper.
  - **Tier A (Fallback)**: Rigid 3D flip using `Modifier.graphicsLayer` (`rotationY`, `cameraDistance`, spine pin).
- Dynamic synchronization with `HorizontalPager(reverseLayout = true)`.
- Pure Kotlin cylinder projection math (`PageTurnMath.kt`) with comprehensive unit tests in `commonTest`.
- Performance runtime guard (`PageTurnPerformanceGuard.kt`) to sample frame times and downgrade to Tier A if needed.
- Setting preference toggle (`PageTurnStyle`: `CURL`, `SLIDE`, `NONE`).
- Accessibility support: respect system "Reduce Motion" setting.

### Out of Scope (Deferred)
- Dual-page spread (`maxWidth >= 600.dp`) for foldables/tablets (retains horizontal slide until follow-up).
- Landscape mode (uses vertical scrolling; retains horizontal slide).
- Generic document reader (Manaqib, Dzikir, Doa, Kitab).

---

## 2. Technical Architecture

```
                    User Drag / Fling Gesture (HorizontalPager)
                                       │
                                       ▼
                     PageTurnState (Progress, Direction, Spine)
                                       │
                ┌──────────────────────┴──────────────────────┐
                ▼                                             ▼
      [Tier B: Cylindrical Curl]                    [Tier A: Rigid 3D Flip]
  - 24-64 vertical canvas strips                 - Modifier.graphicsLayer
  - Sub-pixel transform & 0.5px seam overlap     - rotationY & cameraDistance
  - Dynamic cast shadow & specular ridge         - Hardware-accelerated fallback
  - ~1.5% vertical perspective taper
```

---

## 3. Implementation Tasks Sequence

### Phase 1: Core Geometry & Math Layer (`commonMain` + `commonTest`)
- [ ] Create `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/core/designsystem/component/pageturn/PageTurnMath.kt`:
  - `foldPosition(width, progress)`: Calculates where page leaves the flat plane.
  - `curlRadius(width, progress)`: Computes cylinder radius decaying to 0 at $p = 1.0$.
  - `project(x, width, progress)`: Maps 1D source coordinate to screen space, computing $x'$, shading factor $\cos(\theta)$, back-face detection, and vertical taper ($1.5\%$).
  - `toScreenX(x, width, spineSide)`: Handles coordinate inversion for right-hinged vs left-hinged spines in RTL mushaf.
- [ ] Create `shared/src/commonTest/kotlin/com/iqbalwork/robithoh/core/designsystem/component/pageturn/PageTurnMathTest.kt`:
  - Verify fold position at progress $0.0$, $0.5$, $1.0$.
  - Verify radius decay reaches zero at $p = 1.0$.
  - Test cylindrical wrapping continuity and apex detection.
  - Test RTL symmetry for left vs right spines.

### Phase 2: Design System Components (`core/designsystem/component/pageturn`)
- [ ] Create `PageTurnStyle.kt`:
  - Enum `CURL`, `SLIDE`, `NONE`.
- [ ] Create `PageTurnState.kt`:
  - Holds `progress: Float`, `direction: TurnDirection`, `spineSide: SpineSide`, `activeTier: PageTurnTier`.
- [ ] Create `CurlRenderer.kt`:
  - `drawPageCurl(image, progress, spineSide, stripCount)`:
    - Calculates optimal strip count based on canvas width ($W / 12\text{ px}$, clamped $24..64$).
    - Renders dynamic linear gradient cast shadow onto the underlying page.
    - Draws each strip using `withTransform` with sub-pixel clipping and $+0.5\text{ px}$ overlap.
    - Applies front-face cosine shading and back-face dimming.
    - Renders linear specular highlight along the cylinder apex ridge.
- [ ] Create `RigidFlipModifier.kt`:
  - `Modifier.rigidPageFlip(progress, spineSide)`:
    - Pins transform origin to spine edge.
    - Applies `rotationY` and `cameraDistance`.
- [ ] Create `PageTurnPerformanceGuard.kt`:
  - Real-time frame timing sampler for automatic downgrade if frame drops occur.

### Phase 3: Quran Mushaf Reader Integration
- [x] Update `MushafPageView.kt`:
  - Accept `pageTurnState: PageTurnState?`.
  - When turn is active ($p > 0$):
    - Render image via `drawPageCurl` (or `rigidPageFlip`).
    - Suppress the ayah highlight `Canvas` overlay for the duration of the turn animation.
- [ ] Update `QuranPageReaderScreen.kt`:
  - Add `beyondViewportPageCount = 1` to `HorizontalPager`.
  - Compute continuous page position: $\text{currentPos} = \text{currentPage} + \text{currentPageOffsetFraction}$.
  - Implement Page 1 <-> 2 slide exemption: when $\text{floorPage} == 0$, retain classic horizontal slide without curl or translation neutralization.
  - Implement dual-layer revelation for pages $\ge 2$ ($\text{floorPage} \ge 1$):
    - Top sheet (`pagerIndex == floorPage`): renders with `drawPageCurl` with `zIndex = 1f`.
    - Underlying sheet (`pagerIndex == floorPage + 1`): renders flat with `zIndex = 0f`.
    - Neutralize slide on both active sheets: $\text{translationX} = \text{itemOffset} \times \text{size.width}$.

### Phase 4: Quality & Verification
- [x] Run mathematical test suite: `./gradlew :shared:jvmTest --tests "*PageTurnMathTest*"`
- [ ] Run full project validation: `bash .agents/tools/verify_build.sh --fast`
- [ ] Verify no regressions on sacred Arabic liturgical text and SQLite schemas: `bash .agents/tools/check_sacred_texts.sh --check`
- [ ] Verify MVI and hardcoded string linters.
- [ ] Document changes in `.agents/changelog/AI_CHANGELOG.md` and `.agents/memory/STATE.md`.
