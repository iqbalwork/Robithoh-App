# 📝 AI Agent Activity Changelog

All changes, architectural updates, and significant refactorings made by AI agents are permanently recorded here for traceability and auditability.

---

## [Unreleased]

### Interactive Fullscreen Image Viewer with Pinch-to-Zoom & Swipe-to-Dismiss
- **Date**: 2026-09-14
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Added global fullscreen `ImageViewerManager` and `ZoomableImage` component supporting pinch-to-zoom, panning, double-tap zoom, and swipe-down-to-dismiss with background alpha fading.
- **Changes**:
  - `ImageViewerManager.kt`: Created global singleton state manager (`activeImage`, `show(model)`, `hide()`, `isVisible`) in `core/designsystem/component`.
  - `ZoomableImage.kt`: Created interactive `ZoomableImage` composable in `core/designsystem/component` supporting pinch-to-zoom (`1f` to `4f`), double-tap toggle zoom, constrained panning, velocity-based swipe-down dismiss, and asynchronous `ImageBitmap` loading from network URLs, `ImageBitmap`, `Painter`, or `DrawableResource`.
  - `App.kt`: Integrated global image viewer overlay at top-level `Box` with `AnimatedVisibility`, `BackHandler` back button interception, and status-bar padded close button (`IconButton`).
  - `WaktalAvatar.kt`: Enabled tap-to-view interaction on Wakil Talqin avatar images to trigger `ImageViewerManager.show(...)`.

### Waktal Image Base URL & IP Address Configuration (192.168.101.7:8000)
- **Date**: 2026-09-13
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Fixed `BuildKonfig` string generation formatting and image URL normalization so local dev server images (`http://192.168.101.7:8000/storage/waktal/...`) load properly on connected Android devices.
- **Changes**:
  - `shared/build.gradle.kts`: Removed escaped double-quote escaping `\"` in `buildConfigField(STRING, ...)` values so BuildKonfig generates clean String literals (`"http://192.168.101.7:8000"`) instead of strings containing literal quote characters.
  - `WakilTalqin.kt`: Enhanced `normalizedFotoUrl` logic to strip quotes, extract `/storage/` paths cleanly from `localhost`, `127.0.0.1`, `api.robithoh.id`, `api.robithoh.com`, and any local IP, replacing them with `BuildKonfig.BASE_URL` (`http://192.168.101.7:8000`).
  - `WaktalApiService.kt`: Sanitized `baseUrl` by stripping residual quote characters before constructing API endpoints (`/api/v1`).
  - `WaktalAvatar.kt`: Added space encoding (`url.replace(" ", "%20")`) to ensure filenames with spaces load without Ktor URL parsing errors.
  - `WaktalDomainTest.kt`: Added `testNormalizedFotoUrl` verifying all URL normalization scenarios.
  - `androidApp/src/main/AndroidManifest.xml` & `shared/src/androidMain/AndroidManifest.xml`: Added `android:usesCleartextTraffic="true"` to `<application>` and declared `INTERNET` permission to allow unencrypted HTTP communication with the local development server (`192.168.101.7:8000`).

### Waktal Directory Filter BottomSheet, Search Input Clear Button, & Dynamic Image Normalization
- **Date**: 2026-09-13
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Consolidated Waktal search filters into a ModalBottomSheet accessed via a button beside the search bar, added search clear (X) button, and implemented dynamic image URL normalization against BuildKonfig BASE_URL with KMP image caching.
- **Changes**:
  - `WaktalFilterSheet.kt`: Created new ModalBottomSheet component combining status, sort by distance, and province filters with reset and apply controls.
  - `WaktalListContent.kt`: Placed filter button side-by-side with search text field using `OutlinedIconButton` component and Material Icons (`Icons.Default.Tune`, `Icons.Default.Search`, `Icons.Default.Close`), added clear '✕' trailing button in search field, active filter badge & summary chips, and wired bottom sheet.
  - `shared/build.gradle.kts` & `libs.versions.toml`: Configured `material-icons-extended = { module = "org.jetbrains.compose.material:material-icons-extended", version.ref = "materialIconsExtended" }` (`1.7.3`) and `libs.material.icons.extended` matching `talangraga-umroh-mobile` dependency pattern.
  - `WakilTalqin.kt`: Added `normalizedFotoUrl` property normalizing `localhost`/`127.0.0.1` and relative paths to `BuildKonfig.BASE_URL`.
  - `WaktalAvatar.kt`: Created asynchronous network image component with thread-safe `WaktalImageLoaderCache` using Ktor and `decodeToImageBitmap()`.
  - `WaktalCard.kt` & `WaktalDetailContent.kt`: Integrated `WaktalAvatar` to display avatar images in list and detail views.
  - `WaktalListMvi.kt` & `WaktalViewModel.kt`: Added `ResetFilters` intent handling.

### Home Grid Menu Re-ordering & Tahlil & Ziyaroh Menu Restoration
- **Date**: 2026-09-13
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Re-added "Tahlil & Ziyaroh" menu item to `HomeTabContent.kt` menu grid and repositioned "Wakil Talqin" to the last position in the grid as the newest feature.
- **Changes**:
  - `shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/home/ui/HomeTabContent.kt`: Added `HomeGridMenuItem("tahlil", "Tahlil & Ziyaroh", "🌿")` back to `menuGridItems` list, and moved `HomeGridMenuItem("waktal", "Wakil Talqin", "👳‍♂️")` to the bottom of the list.
  - `.maestro/flows/01_splash_and_home.yaml`: Added `- assertVisible: "Wakil Talqin"` assertion alongside `"Tahlil & Ziyaroh"`.
- **Date**: 2026-09-13
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Implemented type-safe Kotlin Multiplatform build configuration plugin BuildKonfig (`com.codingfeline.buildkonfig`) for `stagingDebug`, `stagingRelease`, `productionDebug`, and `productionRelease` environments across `:shared` and `:androidApp`.
- **Changes**:
  - `shared/build.gradle.kts`: Updated `BASE_URL` for `defaultConfigs`, `stagingDebug`, and `stagingRelease` from `https://staging-api.robithoh.com` to `http://192.168.101.7:8000` for local server API testing.
  - `libs.versions.toml`: Added `buildkonfig = "0.22.0"` and plugin alias `buildkonfig = { id = "com.codingfeline.buildkonfig", version.ref = "buildkonfig" }`.
  - `build.gradle.kts`: Added `alias(libs.plugins.buildkonfig) apply false`.
  - `shared/build.gradle.kts`: Applied `alias(libs.plugins.buildkonfig)` plugin and configured `buildkonfig` block for `stagingDebug`, `stagingRelease`, `productionDebug`, and `productionRelease` with `BASE_URL`, `ENVIRONMENT`, and `IS_DEBUG` fields. Default fallback set to `stagingDebug` for local development.
  - `WaktalApiService.kt`: Updated default `baseUrl` parameter to use `BuildKonfig.BASE_URL`.
  - `BuildKonfigTest.kt`: Added unit test in `commonTest` verifying BuildKonfig generated properties exist and are non-empty.
  - `ADR-0009`: Authored [ADR-0009: Multiplatform Build Configuration via BuildKonfig](adr/0009-multiplatform-buildkonfig-environment-configuration.md).

### Waktal (Wakil Talqin) Directory & Ktor Network Logging
- **Date**: 2026-09-13
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Enabled auto-reseed for full 399 CMS records, added fallback API sync in WaktalSyncManager, and integrated Ktor Client Logging plugin.
- **Changes**:
  - `WaktalRepository.kt`: Updated `ensureSeedLoaded()` so if local SQLite database count < 100 (e.g. device previously held old 5 dummy items), old database is automatically cleared and re-seeded with all 399 records from `waktal_seed.json`.
  - `WaktalSyncManager.kt`: Added API sync fallback logic in `syncWaktalDirectory()` trying `fetchWaktalDelta()` first, and falling back to `fetchWaktalList(perPage = 100)` when needed to fetch live CMS records directly into SQLite.
  - `libs.versions.toml` & `shared/build.gradle.kts`: Added `ktor-client-logging` plugin dependency.
  - `KtorHttpClient.kt`: Installed Ktor `Logging` plugin with `Logger.SIMPLE` and `LogLevel.BODY` when `BuildKonfig.IS_DEBUG` is true.
  - `WaktalApiService.kt`: Added explicit `println` log tags `[WaktalApiService]` for endpoint requests (`/sync/version-manifest`, `/sync/delta/waktal`, `/waktal`, `/waktal/$id`).
  - `waktal_seed.json`: Extracted and pre-bundled complete 399 Waktal records from Robithoh-CMS (`wakil_talqins_seed.json`).
  - `WaktalViewModel.kt`: Added `checkAutoSync()` on initialization for silent background manifest version checking against CMS when online.
  - `WaktalListContent.kt`: Replaced top bar refresh action button with `PullToRefreshBox` gesture for intuitive pull-to-sync experience.
  - `ADR-0008`, `0002-waktal-directory-and-sync-engine.md`, `0002-waktal-directory-and-offline-sync.md`: Updated ADR status to Accepted and synchronized technical specification and plan.
- **Scope**: Implemented full offline-first directory module for ulama Wakil Talqin (Waktal) TQN Suryalaya - Sirnarasa, with on-device GPS proximity sorting, remote CMS sync engine, MVI presentation, and Navigation3 integration.
- **Changes**:
  - `RobithohDatabase.sq`: Added `WakilTalqinEntity` & `WaktalSyncManifestEntity` tables, indices, and SQL queries.
  - `waktal_seed.json`: Added pre-bundled baseline snapshot seed data (398 records).
  - `WaktalDto.kt` & `WaktalApiService.kt`: Added Ktor REST API models and endpoint service for CMS synchronization.
  - `HaversineDistance.kt`: Added on-device spherical Haversine distance calculator and humanized formatting (`"450 m"`, `"14.2 km"`, `"128 km"`).
  - `WakilTalqin.kt`: Added domain model, `WaktalStatus` enum, and intent URIs (`dialPhoneUri`, `whatsappUrl`, `mapIntentUri`).
  - `WaktalRepository.kt` & `WaktalSyncManager.kt`: Added repository for JSON seed loading, SQLite multi-parameter search/filters, and version-manifest sync engine.
  - `WaktalListMvi.kt`, `WaktalDetailMvi.kt`, `WaktalViewModel.kt`, `WaktalDetailViewModel.kt`: Added MVI state flows, intents, and side effects.
  - `WaktalCard.kt`, `WaktalStatusBadge.kt`, `WaktalQuickActions.kt`: Added stateless UI components.
  - `WaktalListScreen.kt`, `WaktalListContent.kt`, `WaktalDetailScreen.kt`, `WaktalDetailContent.kt`: Added Compose Multiplatform MVI screens following strict Screen/Content separation.
  - `ScreenKey.kt`, `App.kt`, `HomeTabContent.kt`: Registered Navigation3 routes and home grid menu item.
  - `HaversineDistanceTest.kt` & `WaktalDomainTest.kt`: Added unit test coverage (148/148 tests passing in `commonTest`).

### Fix Notification & App Location Display (Replace GPS Coordinates with Clean City Names)
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Resolved an issue where prayer notifications and prayer schedules displayed raw GPS coordinates like `"Lokasi GPS (-6.87, 107.57)"` instead of human-readable city names like `"Kota Bandung"`.
- **Changes**:
  - `LocationSanitizer.kt`:
    - Created a shared utility in `commonMain` with 50+ major Indonesian city/regency coordinates.
    - Added `findNearestCity(lat, lng)` calculating Haversine distance to map raw coordinates to the closest Indonesian city when reverse geocoding is offline or unavailable.
    - Added `sanitize(rawName, lat, lng)` to clean English geocoding suffixes (`"Bandung City"` -> `"Kota Bandung"`, `"Bandung Regency"` -> `"Kabupaten Bandung"`) and resolve raw GPS strings to clean city names.
  - `LocationProvider.android.kt` & `LocationProvider.ios.kt`:
    - Integrated `LocationSanitizer` in reverse geocoding fallback paths so location detection returns clean city names (e.g. `"Kota Bandung"`) instead of `"Lokasi GPS (lat, lng)"`.
  - `PrayerAlarmReceiver.kt` & `PrayerAdzanService.kt`:
    - Sanitized `EXTRA_LOCATION_NAME` before constructing pre-reminder and adzan notifications, ensuring notifications always show clean location text (e.g., `"Kota Bandung"`).
  - `PrayerAlarmScheduler.android.kt` & `PrayerWidgetHelper.kt` & `AmaliyahViewModel.kt` & `PrayerTimesCalculator.kt`:
    - Applied location sanitization across alarm scheduling, DB setting loading, widget calculations, and schedule generation.
  - `LocationSanitizerTest.kt`:
    - Added comprehensive unit tests in `commonTest` covering raw coordinate string parsing, English suffix normalization, nearest city matching, and null/blank handling (9/9 tests passing).

### Enforce Light Mode on Quran Page Reader (`QuranPageReaderScreen`)
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Ensured the Quran Page reader screen (`QuranPageReaderScreen`) always stays in light mode with authentic mushaf cream paper presentation, even when global dark mode is active.
- **Changes**:
  - `QuranPageReaderScreen.kt`:
    - Wrapped the screen in `RabithohTheme(darkTheme = false)` so the theme hierarchy, colors, and dialog sheets consistently use light mode tokens.
    - Imported `DarkSurface` explicitly to adhere to the no-inline-FQN convention.

### Symmetrical Directional Page Curl (Reverse Turn e.g. Page 5 to 4)
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Ensured backward inter-spread page turns (e.g. Page 5 to 4, 3 to 2, 7 to 6) apply 3D curl animation to the originating page (Page 5) rather than uncurling the destination page (Page 4), matching forward turn quality identically.
- **Changes**:
  - `QuranPageReaderScreen.kt`:
    - Tracked gesture start origin using `gestureStartPage` via `snapshotFlow { pagerState.isScrollInProgress }`, guaranteeing the originating page is frozen throughout the entire drag and immune to `currentPage` mid-swipe flips at offset 0.5.
    - When moving forward (e.g. 4 -> 5): `gestureStartPage <= floorPage`. Page 4 (`floorPage`) curls towards `SpineSide.RIGHT`, revealing Page 5 underneath.
    - When moving backward (e.g. 5 -> 4): `gestureStartPage > floorPage`. Page 5 (`floorPage + 1`) curls towards `SpineSide.LEFT`, revealing Page 4 underneath.
    - Inverted progress calculation for backward turns (`curlProgress = 1f - rawProgress`) so the originating leaf curls smoothly from 0.0 to 1.0 as the drag proceeds.

### Full-Page Mushaf Paper Curl Animation & Continuous Sheet Background
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Expanded 3D page curl animation from only the central Quran image bounds to the entire page (`Modifier.fillMaxSize()`).
- **Changes**:
  - `CurlRenderer.kt`:
    - Updated `draw` and `drawPageCurl` to accept `paperColor: Color` and `imageRect: Rect?`.
    - Rendered physical paper substrate (`paperColor`) across each full-height strip (`canvasH`).
    - Mapped image calligraphy slices proportionally to the strip projection using `imageRect` coordinates, ensuring calligraphy remains precisely centered and bounded on the curling leaf.
    - Extended cylindrical cast shadow, specular ridge highlight, and ambient backface dimming to span the entire screen height.
  - `MushafPageView.kt`:
    - Expanded Layer 3 (Background paper sheet) to `Modifier.fillMaxSize().background(bgTheme)` when flat.
    - Sized curl canvas to `Modifier.fillMaxSize()` during turn and passed `imageRect = Rect(offsetX, offsetY, offsetX + renderedW, offsetY + renderedH)` to `drawPageCurl`.
    - Wrapped Tier A fallback in a full-sized `Box` with `rigidPageFlip` for full-page rigid flipping.

### Dual-Page Mode (Foldable Open Mode & Tablet) 3D Paper Curl Animation
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Enabled authentic 3D paper curl book turn animation on foldable devices in open/unfolded mode and tablets (`maxWidth >= DUAL_PAGE_WIDTH_THRESHOLD`).
- **Changes**:
  - `QuranPageReaderScreen.kt`:
    - Implemented continuous spread coordinate tracking (`floorSpread`, `curlProgress`, `isTransitioning`) for dual-page spreads.
    - Added horizontal slide translation neutralization on the active pair of two-page spreads (`isTopCurlingSpread` and `isUnderlyingSpread`).
    - Wired `leftPageTurnState` to the left page (`SpineSide.RIGHT`) so the left leaf peels from the outer margin toward the central crease, revealing the destination spread's left page beneath it.
    - Cross-faded the right page of the top spread as the turning leaf crosses the spine, revealing the destination spread's right page seamlessly.
    - Suppressed `SpineEdgeShadow` on the active curling leaf.
  - `spec/0001-page-curl-book-turn-animation.md`: Added `REQ-008 (Dual-Page Foldable & Tablet Mode)`.

### Intra-Spread Slide (Odd <-> Even) vs Inter-Spread Book Curl (Even <-> Odd)
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Implemented authentic physical book spread transitions in `QuranPageReaderScreen.kt`:
  - **Intra-spread sliding (Odd $\leftrightarrow$ Even)**: Pages facing each other on the same open two-page spread (1 $\leftrightarrow$ 2, 3 $\leftrightarrow$ 4, 5 $\leftrightarrow$ 6, etc., where `floorPage % 2 == 0`) transition via horizontal slide.
  - **Inter-spread paper curl (Even $\leftrightarrow$ Odd)**: Turning over a physical paper leaf to reveal the next spread (2 $\leftrightarrow$ 3, 4 $\leftrightarrow$ 5, 6 $\leftrightarrow$ 7, etc., where `floorPage % 2 == 1 && floorPage >= 1`) transitions via 3D paper curl animation.
- **Changes**:
  - `PageTurnMath.kt`: Added `isSpreadTurn(floorPage: Int): Boolean` returning `floorPage >= 1 && (floorPage % 2 == 1)`.
  - `PageTurnMathTest.kt`: Added unit test `testSpreadTurnVersusIntraSpreadSlide()` verifying slide vs curl for all page transitions.
  - `QuranPageReaderScreen.kt`: Wired `PageTurnMath.isSpreadTurn(floorPage)` to `isCurlEligible`.
  - `spec/0001-page-curl-book-turn-animation.md`: Updated `REQ-006` specification.

### Underlying Page Visibility Fix During Quran Page Curl Transition
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Resolved issue where next/previous page was not visible underneath the curling sheet during page transitions.
- **Root Cause & Resolution**:
  - `MushafPageView.kt`: The root `BoxWithConstraints` was drawing an opaque cream background (`bgTheme = Color(0xFFFBF9F4)`), which completely occluded the underlying leaf at `zIndex = 0f` whenever the top curling page was active. Fixed by switching background to `Color.Transparent` when `pageTurnState != null && pageTurnState.progress > 0.001f`.
  - `CurlRenderer.kt`: Added solid mushaf paper backing (`Color(0xFFFBF9F4)`) beneath back-face strips before drawing image slices so the turning page remains opaque physical paper while preserving transparency everywhere the sheet has lifted.
  - `QuranPageReaderScreen.kt`: Optimized `PageTurnState` instantiation without `remember` to ensure instantaneous reactive updates on every drag tick, with accurate dynamic `TurnDirection` calculation.
  - `verify_build.sh`: Added macOS Android SDK auto-detection path (`$HOME/Library/Android/sdk`).

### Quran Mushaf Page 1 <-> 2 Slide Exemption & Google Play Books Under-Layer Revelation
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Implemented REQ-006 (slide-only transition between Page 1 and Page 2) and REQ-007 (Google Play Books-style under-layer revelation during curl animation) in `QuranPageReaderScreen.kt`.
- **Changes**:
  - `QuranPageReaderScreen.kt`:
    - Added `beyondViewportPageCount = 1` to `HorizontalPager` to pre-compose and layout adjacent pages in memory.
    - Implemented continuous page coordinate math: $\text{currentPos} = \text{currentPage} + \text{currentPageOffsetFraction}$, $\text{floorPage} = \lfloor \text{currentPos} \rfloor$, $\text{curlProgress} = \text{currentPos} - \text{floorPage}$.
    - Added Page 1 <-> 2 slide exemption (`floorPage == 0`): transitions between Page 1 and Page 2 strictly retain horizontal slide translation without neutralization or curl.
    - Added dual-layer revelation for pages $\ge 2$ (`floorPage >= 1`): top leaf (`pagerIndex == floorPage`) renders with `drawPageCurl` and `zIndex = 1f`, while the destination sheet underneath (`pagerIndex == floorPage + 1`) renders flat at `(0, 0)` with `zIndex = 0f`.
    - Slide translation neutralization pins both active sheets at `(0, 0)` during turns.
  - `spec/0001-page-curl-book-turn-animation.md`: Added `REQ-006` and `REQ-007`, and updated Section 7 with continuous page math and dual-layer architecture.
  - `plan/0001-page-curl-book-turn-animation.md`: Updated Phase 3 and Phase 4 checklist to reflect completed tasks.

### Quran Mushaf Page Curl (Book Turn) Animation Implementation
- **Date**: 2026-09-12
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Implemented Google Play Books-style paper book curl animation for the Quran Mushaf Reader (`QuranPageReaderScreen`) according to ADR-0007 and spec/0001.
- **Changes**:
  - `PageTurnMath.kt`: Created pure Kotlin cylinder curl mathematical engine with fold positioning, dynamic curvature radius decay ($18\% \to 0\%$), strip count adaptation ($24 \dots 64$), vertical perspective taper ($1.5\%$), and bidirectional RTL coordinate mirroring.
  - `PageTurnMathTest.kt`: Added 7 comprehensive unit tests in `commonTest` verifying fold positioning, radius decay to minimum 1px, strip count bounds, cylinder wrapping and apex ridge detection, back-face flagging past $90^\circ$, and RTL symmetry.
  - `PageTurnState.kt`: Defined `SpineSide`, `TurnDirection`, `PageTurnTier`, and `PageTurnState` data holders.
  - `PageTurnStyle.kt`: Defined `PageTurnStyle` enum (`CURL`, `SLIDE`, `NONE`).
  - `CurlRenderer.kt`: Implemented Tier B DrawScope canvas strip renderer with sub-pixel slicing, $+0.5\text{ px}$ seam overlap protection, dynamic horizontal gradient cast shadow, front-face cosine shading, back-face dimming, and specular ridge crest highlight.
  - `RigidFlipModifier.kt`: Implemented Tier A hardware-accelerated fallback via `Modifier.graphicsLayer` 3D rotation pinned to the spine edge.
  - `PageTurnPerformanceGuard.kt`: Implemented frame timing sampler for automatic downgrade from Tier B to Tier A on low-spec hardware.
  - `MushafPageView.kt`: Added `pageTurnState` parameter, wired `drawPageCurl` and `rigidPageFlip`, suppressed ayah highlight overlays during active curl transitions, and cleaned up inline FQN haptic feedback call.
  - `QuranPageReaderScreen.kt`: Integrated `HorizontalPager` offset tracking with neutralized slide translation (`graphicsLayer { translationX = pageOffset * size.width }`), hooked in `PageTurnState`, added page turn style quick toggle button (`📖`/`↔️`/`⚡`) to header actions, and cleaned up duplicate `SpineSide` enum and inline `BackHandler` FQN.
  - `ADR-0007`: Updated status to `Accepted`.

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

### IslamicHeader TopBar Red Color Consistency
- **Date**: 2026-03-31
- **Author**: AI Assistant & Iqbal Fauzi
- **Scope**: Ensured IslamicHeader topbar consistently uses Robithoh crimson red (`MerahMarunGelap`) as default container background in both light and dark modes, matching Khotaman, Dzikir, and other devotional screens.
- **Changes**:
  - `IslamicHeader.kt`: Set default background color when unspecified to `MerahMarunGelap` (`#8B0014`) for both light and dark modes, rendering white title text, soft gold subtitle (`EmasMuda`), white back icon, and gold gradient bottom divider.
  - `WaktalListContent.kt` & `WaktalDetailContent.kt`: Kept `IslamicHeader` usage synchronized with `RabithohTheme.colors.isDark`.

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
