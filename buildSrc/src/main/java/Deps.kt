object Versions {

    const val compilerSdkVersion = 30
    const val buildToolsVersion = "30.0.3"
    const val minSdkVersion = 22
    const val targetSdkVersion = 30
    const val versionCode = 1
    const val versionName = "1.0"

    const val buildGradleVersion = "7.0.0-alpha08"

    const val kotlinVersion = "1.4.30"

    const val androidCoreVersion = "1.3.2"
    const val androidAppcompatVersion = "1.3.0-beta01"
    const val materialVersion = "1.3.0"

    const val composeVersion = "1.0.0-beta01"

    const val lifecycleVersion = "2.3.0"
    const val activityVersion = "1.3.0-alpha03"

    const val jUnitVersion = "4.13.2"

}

object Dependencies {

    const val buildGradle = "com.android.tools.build:gradle:${Versions.buildGradleVersion}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"

    const val androidCoreKtx = "androidx.core:core-ktx:${Versions.androidCoreVersion}"
    const val androidAppcompat = "androidx.appcompat:appcompat:${Versions.androidAppcompatVersion}"
    const val material = "com.google.android.material:material:${Versions.materialVersion}"

    const val composeUI = "androidx.compose.ui:ui:${Versions.composeVersion}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.composeVersion}"
    const val composeUITooling = "androidx.compose.ui:ui-tooling:${Versions.composeVersion}"

    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityVersion}"

    const val jUnit = "junit:junit:${Versions.jUnitVersion}"

}