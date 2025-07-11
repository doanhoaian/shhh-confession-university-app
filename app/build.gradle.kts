plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "vn.dihaver.tech.shhh.confession"
    compileSdk = 35

    flavorDimensions("version")

    defaultConfig {
        applicationId = "vn.dihaver.tech.shhh.confession"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // BuildConfig
        buildConfigField("int", "VERSION_CODE", versionCode.toString())
        buildConfigField("String", "VERSION_NAME", "\"$versionName\"")
        buildConfigField("String", "BASE_URL", "\"https://stunning-essence-production.up.railway.app/api/\"")
    }

    productFlavors {
        create("mock") {
            dimension = "version"
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
        }
        create("prod") {
            dimension = "version"
        }
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
        buildConfig = true
    }
}

dependencies {
    // Core
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.activity.compose)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.ui.core)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.text.google.fonts)
    implementation(libs.material3)
    implementation(libs.nav.compose)
    implementation(libs.nav.fragment)
    implementation(libs.coil.compose)
    implementation(libs.nav.ui)
    implementation(libs.lottie.compose)

    // Splash
    implementation(libs.splash)

    // Hilt Core DI
    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    // Hilt AndroidX extensions
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)
    ksp(libs.hilt.androidx.compiler)

    // Firebase BOM
    implementation(platform(libs.firebase.bom))

    // Firebase SDKs
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)

    // Google
    implementation(libs.google.signin)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Retrofit + Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    // WorkManager
    implementation(libs.work.runtime)

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)


    // Jetbrains Kotlinx
    implementation(libs.serialization.json)

    // Other
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.ohteepee)
    implementation(libs.coil.svg)
    implementation(libs.coil.gif)
    implementation(libs.compose.shimmer)
    implementation(libs.haze.jetpack.compose)
    implementation(libs.threetenbp)
    implementation(libs.compose.collapsing.top.bar)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)
}