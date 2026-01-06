import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Load keystore properties if available
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    FileInputStream(keystorePropertiesFile).use { 
        keystoreProperties.load(it)
    }
}

android {
    namespace = "com.tictactoe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tictactoe"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // AdMob App ID (replace with your actual App ID from AdMob)
        manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                keyAlias = keystoreProperties["keyAlias"] as String?
                keyPassword = keystoreProperties["keyPassword"] as String?
                storeFile = (keystoreProperties["storeFile"] as String?)?.let { file(it) }
                storePassword = keystoreProperties["storePassword"] as String?
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            isMinifyEnabled = false
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
        viewBinding = true
    }
}

dependencies {
    // AndroidX Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    
    // Material Design
    implementation("com.google.android.material:material:1.11.0")
    
    // Constraint Layout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Google Mobile Ads SDK
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
