package com.iqbalwork.robithoh.core.database

import androidx.compose.runtime.Composable

/** Opens (or reuses) the app's local SQLite database for the current composition. */
@Composable
expect fun rememberRobithohDatabase(): RobithohDatabase
