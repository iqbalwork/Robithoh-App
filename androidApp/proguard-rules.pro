# ==============================================================================
# Robithoh App - Optimized ProGuard / R8 Rules
# ==============================================================================

# ------------------------------------------------------------------------------
# 1. Stack Trace Preservation & Line Numbers (Firebase Crashlytics)
# ------------------------------------------------------------------------------
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ------------------------------------------------------------------------------
# 2. Kotlin Coroutines & Standard Library
# ------------------------------------------------------------------------------
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

# Allow R8 to obfuscate and shrink GeneratedSerializer implementations
-keep,allowobfuscation,allowshrinking class * extends kotlinx.serialization.internal.GeneratedSerializer {
    <init>(...);
    public static *** INSTANCE;
}

# ------------------------------------------------------------------------------
# 4. Library Warnings Suppression & Keep Rules
# ------------------------------------------------------------------------------
-dontwarn app.cash.sqldelight.**
-dontwarn io.insert.koin.**
-dontwarn com.batoulapps.adhan.**
-dontwarn androidx.media3.**
-dontwarn com.google.firebase.**
-dontwarn com.google.android.play.core.**

# ------------------------------------------------------------------------------
# 5. Google Play In-App Updates & Review Keep Rules
# ------------------------------------------------------------------------------
-keep class com.google.android.play.core.appupdate.** { *; }
-keep class com.google.android.play.core.install.** { *; }
-keep interface com.google.android.play.core.appupdate.** { *; }
-keep interface com.google.android.play.core.install.** { *; }
-keep class com.google.android.play.core.review.** { *; }
-keep interface com.google.android.play.core.review.** { *; }

