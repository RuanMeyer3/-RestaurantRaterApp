plugins {
    id("com.android.application")
    // REMOVED: id("org.jetbrains.kotlin.android") <-- This was causing the "Plugin was not found" error
}

android {
    namespace = "com.example.restaurantraterapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.restaurantraterapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    // Set Java source/target compatibility to 1.8 for the Java source files
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Enable View Binding (required by the Java fragments)
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX Core & UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Fragment (Required for FragmentManager and Fragments)
    implementation("androidx.fragment:fragment:1.6.2")

    // ViewModel and LiveData (Shared Data communication)
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")
    // ViewModel integration for Java (if needed, but 2.7.0 usually works fine without extensions)
    // implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
}
