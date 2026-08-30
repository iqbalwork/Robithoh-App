package com.iqbalwork.robithoh.update

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

/**
 * Manages Google Play In-App Updates for Robithoh Android app.
 * Supports both Flexible and Immediate update flows with lifecycle handling.
 */
class InAppUpdateManager(
    private val activity: ComponentActivity,
    private val onUpdateDownloaded: (() -> Unit)? = null
) {
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)

    private val updateLauncher: ActivityResultLauncher<IntentSenderRequest> =
        activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                Log.w(TAG, "Update flow cancelled or failed with result code: ${result.resultCode}")
            }
        }

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Log.d(TAG, "In-app update package downloaded successfully.")
            if (onUpdateDownloaded != null) {
                onUpdateDownloaded.invoke()
            } else {
                showUpdateDownloadedDialog()
            }
        }
    }

    init {
        try {
            appUpdateManager.registerListener(installStateUpdatedListener)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register install state listener", e)
        }
    }

    /**
     * Checks if an update is available on Google Play Store.
     *
     * @param preferImmediate If true, requests IMMEDIATE update flow if available; otherwise uses FLEXIBLE.
     */
    fun checkForUpdates(preferImmediate: Boolean = false) {
        try {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                handleUpdateAvailability(appUpdateInfo, preferImmediate)
            }.addOnFailureListener { e ->
                // Expected in local debug builds or when installed outside Google Play Store
                Log.d(TAG, "In-app update check failed or not supported: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during checkForUpdates", e)
        }
    }

    private fun handleUpdateAvailability(appUpdateInfo: AppUpdateInfo, preferImmediate: Boolean) {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            val targetUpdateType = if (preferImmediate && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                AppUpdateType.IMMEDIATE
            } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                AppUpdateType.FLEXIBLE
            } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                AppUpdateType.IMMEDIATE
            } else {
                null
            }

            targetUpdateType?.let { updateType ->
                startUpdateFlow(appUpdateInfo, updateType)
            }
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo, updateType: Int) {
        try {
            val options = AppUpdateOptions.newBuilder(updateType).build()
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                updateLauncher,
                options
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start update flow for type $updateType", e)
        }
    }

    /**
     * Checks update status when activity resumes.
     * Resumes immediate update in progress or alerts user if flexible download finished while paused.
     */
    fun onResume() {
        try {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    startUpdateFlow(appUpdateInfo, AppUpdateType.IMMEDIATE)
                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    if (onUpdateDownloaded != null) {
                        onUpdateDownloaded.invoke()
                    } else {
                        showUpdateDownloadedDialog()
                    }
                }
            }.addOnFailureListener { e ->
                Log.d(TAG, "In-app update onResume check: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onResume check", e)
        }
    }

    /**
     * Triggers the installation of the downloaded flexible update and restarts the app.
     */
    fun completeUpdate() {
        try {
            appUpdateManager.completeUpdate()
        } catch (e: Exception) {
            Log.e(TAG, "Error completing update", e)
        }
    }

    /**
     * Displays a dialog prompting the user to install the downloaded update now.
     */
    fun showUpdateDownloadedDialog() {
        if (activity.isFinishing || activity.isDestroyed) return

        AlertDialog.Builder(activity)
            .setTitle("Pembaruan Siap Dipasang")
            .setMessage("Versi terbaru aplikasi Robithoh telah selesai diunduh. Pasang sekarang untuk menerapkan pembaruan?")
            .setPositiveButton("Pasang & Muat Ulang") { _, _ ->
                completeUpdate()
            }
            .setNegativeButton("Nanti", null)
            .setCancelable(false)
            .show()
    }

    /**
     * Unregisters listeners to prevent memory leaks.
     */
    fun onDestroy() {
        try {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering listener", e)
        }
    }

    companion object {
        private const val TAG = "InAppUpdateManager"
    }
}
