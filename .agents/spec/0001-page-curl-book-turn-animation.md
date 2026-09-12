---
title: "Technical Specification: Page Curl (Book Turn) Animation Engine"
version: 1.0.0
date_created: 2026-09-12
owner: "Robithoh App Engineering"
tags: ["design", "animation", "quran", "ui", "compose-multiplatform", "mvi"]
---

# Technical Specification: Page Curl (Book Turn) Animation Engine

This specification defines the functional, mathematical, rendering, and architectural requirements for the Google Play Books-style page curl animation within the Quran Mushaf Reader of **Robithoh App**.

---

## 1. Purpose & Scope

### 1.1 Purpose
To provide an authentic, skeuomorphic paper page turn experience for the digital mushaf, replacing the flat horizontal sliding transition with an organic physical curl while preserving offline capability, performance, and the absolute visual integrity of sacred Arabic scripture.

### 1.2 Scope
*   **In Scope**: Single-page portrait Quran mushaf reader (`QuranPageReaderScreen.kt`).
*   **Target Platforms**: Android (API 24+) and iOS via Compose Multiplatform.
*   **Deferred to Future Phase**:
    *   Dual-page spread (`maxWidth >= 600.dp`) for foldable unfolded / tablet form factors.
    *   Landscape reading mode (which uses continuous vertical scrolling).
    *   Generic document reader (`GenericDocumentReaderScreen.kt`).

---

## 2. Definitions & Mathematical Symbols

| Symbol / Term | Definition |
| :--- | :--- |
| **$W$, $H$** | Width and height of the page viewport in pixels (`size.width`, `size.height`). |
| **$p$** | Page turn progress normalized from $0.0$ (flat unturned) to $1.0$ (turn complete). |
| **$f$** | Fold position on the X-axis where the paper leaves the flat plane. |
| **$r$** | Curvature radius of the virtual cylinder around which the paper wraps. |
| **$\theta$** | Wrap angle around the cylinder in radians, ranging from $0$ to $\pi$ ($180^\circ$). |
| **SpineSide** | Edge where the page is bound (`LEFT` or `RIGHT`). In RTL Arabic mushaf, odd pages bind on the right, even pages on the left. |
| **Back-Face** | The reverse side of the turning sheet, visible once $\theta > \pi / 2$ ($90^\circ$). |
| **Tier B** | Primary rendering path: 24–64 vertical canvas strips with cylinder projection and lighting. |
| **Tier A** | Fallback rendering path: Rigid 3D flip using hardware-accelerated `Modifier.graphicsLayer`. |

---

## 3. Requirements & Constraints

### 3.1 Functional Requirements
- **REQ-001**: The page transition MUST simulate an organic paper curl wrapping around a vertical cylinder.
- **REQ-002**: The motion MUST be continuous and bidirectional, directly tracking finger drag gestures and flings.
- **REQ-003**: The animation MUST support programmatic page turns (e.g. from Surah/Ayah selection or audio recitation auto-scroll) with a natural physics easing curve.
- **REQ-004**: When the turn completes ($p = 1.0$), the page MUST settle perfectly flat without any residual curved edge or visual distortion.
- **REQ-005**: The user MUST be able to choose their preferred page turn style (`CURL`, `SLIDE`, `NONE`) in App Settings.

### 3.2 Sacred Text & Visual Integrity (Core Rule 4)
- **SEC-001**: Arabic liturgical script, vowel marks (harakat), and tajwid notations MUST NOT be blurred, clipped, or distorted during the curl.
- **SEC-002**: Adjacent strip boundaries MUST overlap by $+0.5\text{ px}$ (`SEAM_OVERLAP_PX`) to prevent 1-pixel rasterization background bleed on high-density displays.
- **SEC-003**: Active ayah highlight overlays MUST be temporarily suppressed during turns to prevent unnatural geometric shearing on flat bounding boxes.

### 3.3 Performance & Device Constraints
- **CON-001 (Multiplatform)**: The engine MUST execute in `commonMain` without platform-specific AGSL shaders or external C++ dependencies.
- **CON-002 (Offline-First)**: Zero network calls; all assets, bitmaps, and calculations remain 100% on-device.
- **PERF-001**: Frame budget MUST maintain $\ge 60\text{ fps}$ (frame draw time $\le 16.6\text{ ms}$).
- **PERF-002**: A runtime guard (`PageTurnPerformanceGuard`) MUST sample consecutive frame draw times during turns and automatically fall back from Tier B to Tier A if jank is detected.
- **ACC-001**: If the operating system has "Reduce Motion" enabled (`isReducedMotionEnabled() == true`), the app MUST automatically bypass the curl and use `SLIDE` or `NONE`.

---

## 4. Mathematical Model Specification

The pure Kotlin mathematical engine (`PageTurnMath`) computes coordinates without Compose UI dependencies.

```
       x <= f                     f < x <= f + pi*r              x > f + pi*r
  [ Flat Region ]              [ Cylindrical Bend ]           [ Inverted Back-Face ]
═════════════════════════════\         /‾‾‾‾\                 /─────────────────────>
                              \       /      \               /
                              Fold   Apex   Back-face       Toward Spine
```

### 4.1 Fold Position
The fold line starts at the outer margin and travels toward the spine as $p$ advances:
$$f(W, p) = W \cdot (1 - \text{clamp}(p, 0.0, 1.0))$$

### 4.2 Dynamic Radius Decay
The cylinder radius starts at $\approx 18\%$ of page width and decays linearly to near-zero as the page reaches the spine, eliminating any residual curled lobe at $p = 1.0$:
$$r(W, p) = \max\left(W \cdot 0.18 \cdot (1 - p), 1.0\right)$$

### 4.3 1D Cylinder Projection
For any flat source coordinate $x \in [0, W]$:
1. **Flat Zone ($x \le f$)**:
   $$\text{screenX} = x, \quad \text{shade} = 1.0, \quad \text{isBackFace} = \text{false}$$
2. **Cylinder Wrap ($f < x \le f + \pi \cdot r$)**:
   $$\text{arc} = x - f$$
   $$\theta = \min\left(\frac{\text{arc}}{r}, \pi\right)$$
   $$\text{screenX} = f + r \cdot \sin(\theta)$$
   $$\text{shade} = \cos(\theta)$$
   $$\text{isBackFace} = \left(\theta > \frac{\pi}{2}\right)$$
3. **Flat Back-Face ($x > f + \pi \cdot r$)**:
   $$\text{overshoot} = (x - f) - \pi \cdot r$$
   $$\text{screenX} = f - \text{overshoot}$$
   $$\text{shade} = -1.0, \quad \text{isBackFace} = \text{true}$$

### 4.4 Perspective Vertical Taper
A $1.5\%$ vertical scale is applied to curled strips based on light angle $\cos(\theta)$ to simulate perspective depth without a full 3D vertex mesh:
$$\text{verticalScale} = 1.0 - \left(0.015 \cdot \frac{1.0 - \text{shade}}{2.0}\right)$$

### 4.5 RTL Coordinate Inversion
For pages where the spine is on the right (`SpineSide.RIGHT`):
$$\text{screenX}_{\text{RTL}} = W - \text{screenX}$$

---

## 5. Visual Shading Model Specification

The renderer applies three lighting passes on top of the deformed strips:

### 5.1 Dynamic Cast Shadow
- **Target**: Projected onto the underlying stationary page.
- **Bounds**: Starts at $\text{apex} = f + r$, extends outward to $f + 2.8 \cdot r$.
- **Gradient**: `Brush.horizontalGradient` from `Color.Black.copy(alpha = 0.35f)` to `Color.Transparent`.

### 5.2 Specular Highlight (Ridge Apex)
- **Target**: Drawn along the crest of the cylinder curve where light reflects directly to the viewer.
- **Bounds**: Width is $5\%$ of page width centered at $x = f + r$.
- **Gradient**: Bell-curved horizontal gradient:
  - $0\% \to \text{Transparent}$
  - $50\% \to \text{Color.White.copy(alpha = 0.35f)}$
  - $100\% \to \text{Transparent}$

### 5.3 Surface Shading & Back-Face Dimming
- Front-face strips are shaded with ambient darkening:
  $$\alpha_{\text{front}} = (1.0 - \max(0.0, \text{shade})) \cdot 0.30$$
- Back-face strips ($\theta > \pi/2$) receive a uniform semi-translucent dimming:
  $$\alpha_{\text{back}} = 0.55$$

---

## 6. Component Architecture & Interfaces

### 6.1 Package Layout
```
com.iqbalwork.robithoh.core.designsystem.component.pageturn/
├── PageTurnMath.kt               # Pure Kotlin projection math
├── PageTurnStyle.kt              # Enum (CURL, SLIDE, NONE)
├── PageTurnState.kt              # State holder (progress, spine, tier)
├── CurlRenderer.kt               # Tier B: DrawScope canvas strip renderer
├── RigidFlipModifier.kt          # Tier A: Modifier.graphicsLayer fallback
└── PageTurnPerformanceGuard.kt   # Runtime FPS monitor & downgrade gate
```

### 6.2 Key Component Interfaces

#### `PageTurnStyle`
```kotlin
enum class PageTurnStyle {
    CURL,
    SLIDE,
    NONE
}
```

#### `PageTurnState`
```kotlin
enum class SpineSide { LEFT, RIGHT }
enum class TurnDirection { FORWARD, BACKWARD }
enum class PageTurnTier { TIER_B_CURL, TIER_A_RIGID }

class PageTurnState(
    val progress: Float,
    val spineSide: SpineSide,
    val direction: TurnDirection,
    val activeTier: PageTurnTier = PageTurnTier.TIER_B_CURL
)
```

#### `CurlRenderer.kt`
```kotlin
fun DrawScope.drawPageCurl(
    image: ImageBitmap,
    progress: Float,
    spineSide: SpineSide = SpineSide.LEFT,
    stripCount: Int = stripCountFor(size.width)
)
```

#### `RigidFlipModifier.kt`
```kotlin
fun Modifier.rigidPageFlip(
    progress: Float,
    spineSide: SpineSide
): Modifier
```

---

## 7. Gesture & Pager Integration

Integration maintains the existing `HorizontalPager(reverseLayout = true)` as the single source of truth:

```kotlin
val pagerState = rememberPagerState(pageCount = { totalPages })

HorizontalPager(
    state = pagerState,
    reverseLayout = true,
    modifier = Modifier.fillMaxSize()
) { pagerIndex ->
    val pageNum = pagerIndex + 1
    val isRightPage = (pageNum % 2 == 1)
    
    // Offset fraction relative to this page (-1.0 .. 1.0)
    val pageOffset = (pagerIndex - pagerState.currentPage) - pagerState.currentPageOffsetFraction
    
    // Neutralize standard HorizontalPager translation slide
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationX = -pageOffset * size.width
            }
    ) {
        MushafPageView(
            pageNumber = pageNum,
            pageImage = pageImage,
            pageTurnProgress = pageOffset.coerceIn(0f, 1f),
            spineSide = if (isRightPage) SpineSide.RIGHT else SpineSide.LEFT,
            // ...
        )
    }
}
```

### 7.1 Snap & Fling Curve
Programmatic transitions and fling snaps run with duration **$420\text{ ms}$** using the following easing curve:
$$\text{CubicBezierEasing}(0.22\text{f}, 0.90\text{f}, 0.24\text{f}, 1.00\text{f})$$

---

## 8. Verification & Test Acceptance Criteria

1. **Geometry Tests (`PageTurnMathTest`)**:
   - $f(W, 0.0) == W$ and $f(W, 1.0) == 0.0$.
   - $r(W, 1.0) \le 1.0\text{ px}$.
   - Monotonicity check on $x \le f$.
   - Screen apex position does not exceed $W$ by more than $r$.
   - Symmetric mirror output under `SpineSide.RIGHT`.
2. **Build & Quality Pipeline**:
   - Run `.agents/tools/verify_build.sh` (100% tests passing, zero lint regressions).
   - Sacred text SHA-256 integrity check passes with zero modifications.
