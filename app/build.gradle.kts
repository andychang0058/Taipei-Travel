import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.plugin

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.cathaybk.travel"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.cathaybk.travel"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.compose.navigation)
    implementation(libs.androidx.ui.compose.constraint)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.datastore)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
    implementation(libs.koin.annotations)
    implementation(libs.kotlin.serialization)
    ksp(libs.koin.ksp.compiler)
    implementation(libs.facebook.flipper)
    implementation(libs.facebook.soloader)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.okhttp)
    implementation(libs.coil.svg)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logging)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.converter.kotlinx.serialization)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.facebook.flipper.network.plugin)
}