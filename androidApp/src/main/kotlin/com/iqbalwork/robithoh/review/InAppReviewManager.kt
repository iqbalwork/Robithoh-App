package com.iqbalwork.robithoh.review

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.iqbalwork.robithoh.BuildConfig

/**
 * Manages Google Play In-App Review for Robithoh.
 * Gate: only after MIN_APP_OPENS cold starts, at most once per versionCode,
 * one attempt per process. Safe no-op on non-Play installs (API fails silently there).
 */
class InAppReviewManager(private val activity: ComponentActivity) {

    private val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val reviewManager: ReviewManager = ReviewManagerFactory.create(activity)
    private var attemptedThisProcess = false

    /** Call once per cold start (MainActivity.onCreate). */
    fun recordAppOpen() {
        prefs.edit().putInt(KEY_APP_OPENS, prefs.getInt(KEY_APP_OPENS, 0) + 1).apply()
    }

    /** Call when the activity is in the foreground (MainActivity.onResume). */
    fun maybeRequestReview() {
        if (attemptedThisProcess) return
        attemptedThisProcess = true

        val appOpens = prefs.getInt(KEY_APP_OPENS, 0)
        val lastPromptedVersion = prefs.getInt(KEY_LAST_PROMPTED_VERSION, -1)
        val currentVersion = BuildConfig.VERSION_CODE

        if (appOpens < MIN_APP_OPENS || currentVersion == lastPromptedVersion) return

        try {
            reviewManager.requestReviewFlow()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "In-app review flow requested.")
                        reviewManager.launchReviewFlow(activity, task.result)
                            .addOnCompleteListener { Log.d(TAG, "In-app review flow finished.") }
                    } else {
                        // Non-Play install / API unavailable: mark done so we do not retry every launch.
                        Log.d(TAG, "In-app review not available: ${task.exception?.message}")
                    }
                    prefs.edit().putInt(KEY_LAST_PROMPTED_VERSION, currentVersion).apply()
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error requesting in-app review", e)
            prefs.edit().putInt(KEY_LAST_PROMPTED_VERSION, currentVersion).apply()
        }
    }

    companion object {
        private const val TAG = "InAppReviewManager"
        private const val PREFS_NAME = "robithoh_in_app_review"
        private const val KEY_APP_OPENS = "app_opens"
        private const val KEY_LAST_PROMPTED_VERSION = "last_prompted_version_code"
        private const val MIN_APP_OPENS = 3
    }
}
