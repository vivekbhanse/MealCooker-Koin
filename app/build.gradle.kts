plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.example.mykoinapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mykoinapp"
        minSdk = 24
        targetSdk = 35
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
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"

        composeCompiler {
            reportsDestination = layout.buildDirectory.dir("compose_compiler")
        }
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
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    implementation(libs.androidx.compiler.v151)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Koin for Android
    implementation(libs.insert.koin.koin.android)
    implementation (libs.koin.androidx.compose)
    //Coroutine
    implementation(libs.kotlinx.coroutines.android.v160)

    //Coil
    implementation(libs.coil.compose)

    //Biometric
    implementation(libs.androidx.biometric)
    // Logging
    implementation(libs.timber)
    //Security Crypto
    implementation(libs.androidx.security.crypto)


    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // For Kotlin coroutines support
    ksp(libs.androidx.room.compiler) // Annotation processor for Room

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //Viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Graph QL
//    implementation(libs.apollo.runtime) // Apollo runtime


    // Retrofit for making network calls
    implementation(libs.retrofit)

    // Gson converter for Retrofit to parse JSON
    implementation(libs.converter.gson)

    // OkHttp logging interceptor (optional but useful for debugging network calls)
    implementation(libs.logging.interceptor)


    //Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // SystemBars
    implementation (libs.accompanist.systemuicontroller)
    implementation (libs.accompanist.swiperefresh.v02413rc)

    implementation(libs.wave)
}
