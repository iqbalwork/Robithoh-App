package com.iqbalwork.robithoh.core.database

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberRobithohDatabase(): RobithohDatabase {
    val context = LocalContext.current
    return remember { createDatabase(DatabaseDriverFactory(context)) }
}
