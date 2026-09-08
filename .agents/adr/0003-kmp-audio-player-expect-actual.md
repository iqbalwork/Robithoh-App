# ADR-0003: Multiplatform Audio Engine (`KmpAudioPlayer`) Bridging Media3 & AVPlayer

*   **Status**: Accepted
*   **Date**: 2026-09-08
*   **Deciders**: Iqbal Fauzi, Antigravity AI
*   **Consulted**: Core Development Team
*   **Informed**: Engineering & QA

---

## 1. Context and Problem Statement
Audio recitations (Langgam TQN, Dzikir Jahr/Khofi, Sholawat, and Adzan) are central to the Robithoh App experience. Audio must continue playing seamlessly in the background when the device screen is locked or when navigating across tabs, requiring deep integration with native OS audio subsystems.

## 2. Decision
We decided to design an `expect / actual` abstraction **`KmpAudioPlayer`**:
1. **Shared Contract (`KmpAudioPlayer.kt`)**:
   - Exposes reactive StateFlows for `currentTrack`, `playbackState`, `currentPositionMs`, and `durationMs`.
   - Exposes playback control primitives (`play`, `pause`, `resume`, `seekTo`, `stop`, `release`).
2. **Android Implementation (`AndroidAudioPlayer.kt`)**:
   - Built on **AndroidX Media3 (1.5.1)** / `ExoPlayer`.
   - Runs with foreground service notifications and lock-screen media controls.
3. **iOS Implementation (`KmpAudioPlayer.ios.kt`)**:
   - Built on **AVFoundation** (`AVPlayer`, `AVAudioSession`).
   - Configures `AVAudioSessionCategoryPlayback` for native lock-screen controls.
4. **UI Integration**:
   - A global floating mini audio bar in `App.kt` allows continuous playback across screens.

## 3. Rationale
* Native audio engines handle hardware decoding, battery optimizations, and audio focus changes much better than pure cross-platform wrappers.
* UI components remain decoupled from platform audio SDKs.

## 4. Consequences
* **Good**: High audio fidelity, background playback capability, lock screen controls on both OSes.
* **Bad**: Maintaining two platform implementations requires testing on both platforms.
* **Neutral**: Audio asset paths must be resolved appropriately per platform.

## 5. References
* [AUDIO_ENGINE.md](../references/AUDIO_ENGINE.md)
