package com.iqbalwork.robithoh.core.database

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberRobithohDatabase(): RobithohDatabase {
    return remember { createDatabase(DatabaseDriverFactory()) }
}
