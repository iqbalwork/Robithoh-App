---
name: kmp-audio-engine
description: Architecture and guidelines for working with the multiplatform audio engine (KmpAudioPlayer) in Robithoh App. Covers Android Media3, iOS AVPlayer, audio states, and the global floating audio bar.
---

# KMP Audio Engine (`KmpAudioPlayer`)

Robithoh App provides continuous background audio playback for Langgam TQN recitations, Dzikir, Sholawat, and Adzan across Android and iOS.

---

## 1. Controlling Playback via ViewModels

ViewModels should never interact directly with platform audio players. Instead, inject `KmpAudioPlayer`:

```kotlin
class ExampleAudioViewModel(
    private val audioPlayer: KmpAudioPlayer
) : ViewModel() {

    fun playTrack(track: AudioTrack) {
        audioPlayer.play(track)
    }

    fun togglePlayPause() {
        if (audioPlayer.playbackState.value == AudioPlaybackState.PLAYING) {
            audioPlayer.pause()
        } else {
            audioPlayer.resume()
        }
    }
}
```

---

## 2. Floating Mini Audio Bar

The mini audio bar rendered in `App.kt` listens directly to `audioPlayer.currentTrack` and `audioPlayer.playbackState`:
- When `currentTrack` is non-null, the floating pill animates into view above the bottom navigation bar.
- Clicking the play/pause button dispatches `audioPlayer.pause()` / `resume()`.
- Closing the bar triggers `audioPlayer.stop()`.

---

## 3. Platform Implementations

- **Android**: `AndroidAudioPlayer` wraps `ExoPlayer` inside a Foreground Service with an ongoing notification ensuring uninterrupted background playback.
- **iOS**: `KmpAudioPlayer.ios.kt` uses `AVPlayer` configured with `AVAudioSessionCategoryPlayback`.
