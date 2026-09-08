package com.iqbalwork.robithoh

import platform.Foundation.NSBundle

actual fun appVersionName(): String {
    val version = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String
    return if (!version.isNullOrBlank()) version else "1.0.0"
}

