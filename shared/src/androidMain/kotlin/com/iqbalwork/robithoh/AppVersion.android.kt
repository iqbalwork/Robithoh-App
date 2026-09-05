package com.iqbalwork.robithoh

actual fun appVersionName(): String {
    val fromBuildConfig = try {
        val clazz = Class.forName("com.iqbalwork.robithoh.BuildConfig")
        val field = clazz.getField("VERSION_NAME")
        field.get(null) as? String
    } catch (_: Throwable) {
        null
    }
    if (!fromBuildConfig.isNullOrBlank()) {
        return fromBuildConfig
    }

    val fromPackageInfo = try {
        val context = com.iqbalwork.robithoh.core.designsystem.getGlobalAppContext()
        if (context != null) {
            val packageInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    android.content.pm.PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            packageInfo.versionName
        } else {
            null
        }
    } catch (_: Throwable) {
        null
    }
    if (!fromPackageInfo.isNullOrBlank()) {
        return fromPackageInfo
    }

    return "1.1.0"
}
