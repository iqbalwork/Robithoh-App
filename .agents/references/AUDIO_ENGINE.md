# 🔊 Multiplatform Audio Engine (`KmpAudioPlayer`)

This document details the audio playback architecture in **Robithoh App**.

---

## 🏗️ 1. Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                       Shared UI Layer                       │
│    (Mini Floating Audio Bar in App.kt & Full Player Screens)│
└──────────────────────────────┬──────────────────────────────┘
                               │ Observes playbackState, currentPositionMs, currentTrack
┌──────────────────────────────▼──────────────────────────────┐
│             Presentation & ViewModels (KMP)                 │
│         (LanggamViewModel, QuranViewModel, ManaqibViewModel) │
└──────────────────────────────┬──────────────────────────────┘
                               │ Invokes play(), pause(), seekTo()
┌──────────────────────────────▼──────────────────────────────┐
│             KmpAudioPlayer Interface (commonMain)           │
└──────────────────────────────┬──────────────────────────────┘
                               │ expect / actual
        ┌──────────────────────┴──────────────────────┐
        ▼                                             ▼
┌──────────────────────────────┐     ┌────────────────────────────────┐
│   AndroidAudioPlayer         │     │  KmpAudioPlayer.ios.kt         │
│   (AndroidX Media3 / ExoPlayer│    │  (AVFoundation / AVPlayer)     │
│   Foreground Service & Notif)│     │  (Audio Session & Now Playing) │
└──────────────────────────────┘     └────────────────────────────────┘
```

---

## 📱 2. Platform Implementations

### Android (`AndroidAudioPlayer.kt`):
- Powered by **AndroidX Media3 (1.5.1)** / `ExoPlayer`.
- Runs as a **Foreground Service** (`PrayerAdzanService` or background media player service) ensuring continuous playback when the app is minimized or screen is locked.
- Integrates system media controls with notification metadata (artwork, title, recitation artist, play/pause/skip actions).
- Supports local bundled assets (`android.resource://` or extracted cache files) and remote streaming URLs when needed.

### iOS (`KmpAudioPlayer.ios.kt`):
- Powered by Apple's native **`AVFoundation`** framework (`AVPlayer`, `AVPlayerItem`).
- Configures `AVAudioSessionCategoryPlayback` to allow background playback through the iOS Control Center and Lock Screen (`MPNowPlayingInfoCenter`).

---

## 🎛️ 3. State & Control API (`KmpAudioPlayer`)

```kotlin
interface KmpAudioPlayer {
    val currentTrack: StateFlow<AudioTrack?>
    val playbackState: StateFlow<AudioPlaybackState> // IDLE, BUFFERING, PLAYING, PAUSED, COMPLETED, ERROR
    val currentPositionMs: StateFlow<Long>
    val durationMs: StateFlow<Long>

    fun play(track: AudioTrack)
    fun pause()
    fun resume()
    fun seekTo(positionMs: Long)
    fun stop()
    fun release()
}
```

---

## 🎚️ 4. Floating Mini Audio Bar Pattern

A persistent, interactive floating audio bar is anchored at the bottom of the screen above the floating dock bar in `App.kt`:
1. **Always Synchronized**: Observes `KmpAudioPlayer.currentTrack` and `playbackState`.
2. **Interactive Controls**: Play/pause toggle, progress timeline scrubber, and close button.
3. **Seamless Navigation**: Continues playing without interruption as users switch between Home, Salat, Quran, Kitab, and Settings.
