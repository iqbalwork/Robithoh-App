# 🧪 Testing & Quality Assurance Strategy

This document outlines the testing and QA infrastructure for **Robithoh App**.

---

## 🎯 1. Dual-Layer QA Strategy

Robithoh App enforces a two-tier testing methodology:
1. **Tier 1: Unit & Logic Testing (`shared/src/commonTest/`)**
   - High speed, deterministic, executes on every agent task.
   - Tests pure Kotlin logic, ViewModel state transitions, mathematical calculations, and repository contracts.
2. **Tier 2: End-to-End (E2E) UI Flow Testing (`.maestro/`)**
   - Black-box UI automation on Android emulators/devices using **Maestro**.
   - Validates user gestures, screen transitions, audio play/pause, bottom sheets, and spotlight guidelines.

---

## 🧪 2. CommonTest & Coroutine Rules

### Non-Negotiable Rule for ViewModel Tests:
Because `viewModelScope` on JVM defaults to `Dispatchers.Main.immediate`, any test exercising coroutines or flows inside a ViewModel **MUST** set the Main test dispatcher:

```kotlin
class ExampleViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFlowEmission() = runTest {
        viewModel.onIntent(...)
        advanceUntilIdle()
        assertEquals(expected, viewModel.uiState.value.something)
    }
}
```

### Running Unit Tests:
```bash
# Run all commonTest suites across targets
./gradlew :shared:allTests

# Run specific test class on JVM
./gradlew :shared:jvmTest --tests "com.iqbalwork.robithoh.feature.QuranViewModelTest"
```

---

## 🎭 3. Maestro E2E UI Testing (`.maestro/`)

Robithoh App includes 10 automated Maestro test flows located in `.maestro/flows/`:

| Flow File | User Journey Tested |
| :--- | :--- |
| `01_splash_and_home.yaml` | Splash screen animations, onboarding completion, and home screen rendering. |
| `02_modal_bottom_sheets.yaml` | Quick navigation bottom sheets and category selectors. |
| `03_salat_prayer_times.yaml` | Daily prayer timetable display and next prayer countdown. |
| `04_prayer_settings.yaml` | Calculation method selection, ihtiyat minute offsets, and adzan volume slider. |
| `05_qibla_compass.yaml` | Qibla direction calculation and sensor status. |
| `06_tasbih_digital.yaml` | Haptic/audio tasbih counter tapping, target presets (33, 99, 165), reset dialog. |
| `07_langgam_player.yaml` | Langgam TQN audio playback, track selection, and mini floating audio bar. |
| `08_quran_and_reader.yaml` | Quran Surah list, jumping to Ayah, Arabic font scaling, and bookmarking. |
| `09_document_reader.yaml` | Manaqib reading (Indonesian & Sundanese), theme toggles (Cream, Dark, White). |
| `10_settings_and_profile.yaml` | App settings, spotlight guide reset, and Sirnarasa history view. |

### Running the Full Suite:
```bash
# Ensure emulator or physical device is connected via ADB
maestro test .maestro/full_suite.yaml
```
