package com.iqbalwork.robithoh

import com.iqbalwork.robithoh.shared.BuildKonfig
import platform.Foundation.NSBundle

actual fun appVersionName(): String {
    val version = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String
    return if (!version.isNullOrBlank()) version else BuildKonfig.VERSION_NAME
}


