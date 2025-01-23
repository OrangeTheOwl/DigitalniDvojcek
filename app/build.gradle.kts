plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("kotlin-kapt")
}

apply(plugin = "com.google.gms.google-services")

android {

    kapt {
        correctErrorTypes = true
    }

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
    implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-firestore-ktx")

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

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.7.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    implementation("io.projectreactor:reactor-core:3.5.8")
    implementation("dnsjava:dnsjava:3.4.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room baza
    implementation("androidx.room:room-runtime:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")

    // Networking z OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
}

