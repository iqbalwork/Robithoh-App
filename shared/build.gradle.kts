import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }
    jvm("jvm")
    
    android {
       namespace = "com.iqbalwork.robithoh.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(compose.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.adhan)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(compose.preview)
            implementation(libs.compose.uiTooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
            implementation(libs.sqldelight.android.driver)
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.session)
            implementation(libs.androidx.media3.common)
            implementation(libs.ktor.client.okhttp)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
            implementation(libs.ktor.client.darwin)
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
                implementation(libs.ktor.client.okhttp)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

sqldelight {
    databases {
        create("RobithohDatabase") {
            packageName.set("com.iqbalwork.robithoh.core.database")
        }
    }
}

buildkonfig {
    packageName = "com.iqbalwork.robithoh.shared"
    objectName = "BuildKonfig"
    exposeObjectWithName = "BuildKonfig"

    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", "http://192.168.101.7:8000")
        buildConfigField(STRING, "ENVIRONMENT", "stagingDebug")
        buildConfigField(BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, "VERSION_NAME", "1.2.0")
    }

    defaultConfigs("stagingDebug") {
        buildConfigField(STRING, "BASE_URL", "http://192.168.101.7:8000")
        buildConfigField(STRING, "ENVIRONMENT", "stagingDebug")
        buildConfigField(BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, "VERSION_NAME", "1.2.0")
    }

    defaultConfigs("stagingRelease") {
        buildConfigField(STRING, "BASE_URL", "http://192.168.101.7:8000")
        buildConfigField(STRING, "ENVIRONMENT", "stagingRelease")
        buildConfigField(BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(STRING, "VERSION_NAME", "1.2.0")
    }

    defaultConfigs("productionDebug") {
        buildConfigField(STRING, "BASE_URL", "https://api.robithoh.com")
        buildConfigField(STRING, "ENVIRONMENT", "productionDebug")
        buildConfigField(BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, "VERSION_NAME", "1.2.0")
    }

    defaultConfigs("productionRelease") {
        buildConfigField(STRING, "BASE_URL", "https://api.robithoh.com")
        buildConfigField(STRING, "ENVIRONMENT", "productionRelease")
        buildConfigField(BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(STRING, "VERSION_NAME", "1.2.0")
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
