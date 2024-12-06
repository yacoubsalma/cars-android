plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "tn.esprit.pdm"
    compileSdk = 35

    defaultConfig {
        applicationId = "tn.esprit.pdm"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.7.3") // Latest version as of now, adjust if needed
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.coil-kt:coil-compose:2.0.0")
    implementation ("androidx.compose.material:material-icons-extended:1.4.3")
    implementation ("androidx.activity:activity-compose:1.7.2") // Latest version
    implementation ("androidx.compose.material:material:1.6.0")


    implementation(libs.dagger.hilt.android)
    implementation(libs.firebase.crashlytics.buildtools)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt.compose)
    implementation(libs.coil)
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")

    implementation("com.squareup.okhttp3:okhttp:4.10.0") // OkHttp library
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Coroutines

    implementation ("com.mapbox.maps:android:11.9.0-beta.1")
    implementation("com.mapbox.extension:maps-compose:11.9.0-beta.1")

    implementation ("com.github.KwabenBerko:News-API-Java:1.0.2")

    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation ("com.google.accompanist:accompanist-webview:0.31.3-beta")
    implementation ("io.coil-kt:coil-compose:2.3.0") // For image loading

}