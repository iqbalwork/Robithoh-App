package com.iqbalwork.robithoh.core.designsystem

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

class AndroidHapticFeedback(private val contextProvider: () -> Context?) : KmpHapticFeedback {

    private val audioAttributes by lazy {
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .build()
    }

    private fun getVibrator(): Vibrator? {
        val ctx = contextProvider() ?: return null
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (_: Throwable) {
            null
        }
    }

    override fun performClick() {
        val vibrator = getVibrator() ?: return
        try {
            if (!vibrator.hasVibrator()) return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    vibrator.vibrate(
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK),
                        audioAttributes
                    )
                    return
                } catch (_: Throwable) {
                    // Fallback to one shot below
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(45L, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect, audioAttributes)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(45L)
            }
        } catch (_: Throwable) {
            // Guard
        }
    }

    override fun performSuccess() {
        val vibrator = getVibrator() ?: return
        try {
            if (!vibrator.hasVibrator()) return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 60, 60, 90)
                val amplitudes = intArrayOf(0, 200, 0, 255)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vibrator.vibrate(effect, audioAttributes)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 60, 60, 90), -1)
            }
        } catch (_: Throwable) {
            // Guard
        }
    }

    override fun performMilestone() {
        val vibrator = getVibrator() ?: return
        try {
            if (!vibrator.hasVibrator()) return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 100, 80, 140, 80, 200)
                val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vibrator.vibrate(effect, audioAttributes)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 100, 80, 140, 80, 200), -1)
            }
        } catch (_: Throwable) {
            // Guard
        }
    }
}

private var globalAppContext: Context? = null

fun getGlobalAppContext(): Context? = globalAppContext

fun setGlobalAppContext(context: Context) {
    globalAppContext = context.applicationContext
}

@Composable
actual fun InitHapticContext() {
    val context = LocalContext.current
    LaunchedEffect(context) {
        setGlobalAppContext(context)
    }
}

actual fun getHapticFeedback(): KmpHapticFeedback = AndroidHapticFeedback { globalAppContext }

