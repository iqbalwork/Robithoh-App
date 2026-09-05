# ==============================================================================
# Robithoh App - Comprehensive ProGuard / R8 Rules
# ==============================================================================

# ------------------------------------------------------------------------------
# 1. General Optimization & Stack Trace Preservation (Firebase Crashlytics)
# ------------------------------------------------------------------------------
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ------------------------------------------------------------------------------
# 2. Kotlin Coroutines & Standard Library
# ------------------------------------------------------------------------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# ------------------------------------------------------------------------------
# 3. Kotlinx Serialization
# ------------------------------------------------------------------------------
-dontnote kotlinx.serialization.SerializationKt

# Keep companions and serializers for @Serializable classes
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
-keep,allowobfuscation,allowoptimization class * extends kotlinx.serialization.internal.GeneratedSerializer {
    <init>(...);
    public static *** INSTANCE;
}

# Keep Robithoh model & navigation serialized classes
-keep class com.iqbalwork.robithoh.navigation.ScreenKey* { *; }
-keep class com.iqbalwork.robithoh.core.location.UserLocation { *; }
-keep class com.iqbalwork.robithoh.core.model.AudioPlaybackState { *; }
-keep class com.iqbalwork.robithoh.core.model.AudioTrack { *; }
-keep class com.iqbalwork.robithoh.feature.**.model.** { *; }

# ------------------------------------------------------------------------------
# 4. Compose Multiplatform & Generated Resources
# ------------------------------------------------------------------------------
-keep class robithohapp.shared.generated.resources.** { *; }
-keepclassmembers class robithohapp.shared.generated.resources.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }

# ------------------------------------------------------------------------------
# 5. SQLDelight 2.x
# ------------------------------------------------------------------------------
-keep class app.cash.sqldelight.** { *; }
-keep class com.iqbalwork.robithoh.core.database.** { *; }
-dontwarn app.cash.sqldelight.**

# ------------------------------------------------------------------------------
# 6. Koin Dependency Injection
# ------------------------------------------------------------------------------
-keep class io.insert.koin.** { *; }
-keepclassmembers class * {
    @org.koin.core.annotation.* *;
}
-dontwarn io.insert.koin.**

# ------------------------------------------------------------------------------
# 7. Batoulapps Adhan (Prayer Calculation Engine)
# ------------------------------------------------------------------------------
-keep class com.batoulapps.adhan.** { *; }
-keepclassmembers enum com.batoulapps.adhan.** { *; }
-dontwarn com.batoulapps.adhan.**

# ------------------------------------------------------------------------------
# 8. Media3 / ExoPlayer & Audio Playback
# ------------------------------------------------------------------------------
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# ------------------------------------------------------------------------------
# 9. Firebase Crashlytics & Analytics
# ------------------------------------------------------------------------------
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**
-keep class com.google.firebase.analytics.** { *; }
-dontwarn com.google.firebase.analytics.**

# ------------------------------------------------------------------------------
# 10. Android App Components & Foreground Services
# ------------------------------------------------------------------------------
-keep class com.iqbalwork.robithoh.core.notification.PrayerAdzanService { *; }
-keep class com.iqbalwork.robithoh.core.notification.PrayerAlarmReceiver { *; }
-keep class com.iqbalwork.robithoh.MainActivity { *; }
-keep class com.iqbalwork.robithoh.widget.** { *; }

# ------------------------------------------------------------------------------
# 11. Google Play In-App Updates
# ------------------------------------------------------------------------------
-keep class com.google.android.play.core.appupdate.** { *; }
-keep class com.google.android.play.core.install.** { *; }
-dontwarn com.google.android.play.core.**