plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services") version "4.3.15" apply false
}

android {
    namespace = "com.example.airportmobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.airportmobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/native-image/reflect-config.json")
            excludes.add("META-INF/native-image/native-image.properties")
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/NOTICE")
            excludes.add("META-INF/NOTICE.txt")
            excludes.add("META-INF/NOTICE.md")
            excludes.add("META-INF/ASL2.0")
        }
    }
}

dependencies {
    // Firebase knjižnice
    implementation("com.google.firebase:firebase-database-ktx:20.2.0")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-messaging:24.1.0")

    // Osnovne knjižnice
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)

    // Maps in lokacija
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Kamera in obdelava slik
    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-core:1.4.1")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.apache.commons:commons-io:1.3.2")
    implementation("com.squareup.picasso:picasso:2.71828")

    // MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.3.0")
    implementation("org.mongodb:mongodb-driver-sync:4.10.0")
    implementation("org.mongodb:bson-kotlin:5.3.0")

    // Kotlin korutine in reaktivni tokovi
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.7.3")

    // Kotlin Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")

    // Podpora za desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    // Reactor Core
    implementation("io.projectreactor:reactor-core:3.5.8")

    // DNS podpora
    implementation("dnsjava:dnsjava:3.4.1")

    // Testiranje
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
