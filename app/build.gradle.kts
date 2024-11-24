plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.thatwaz.whoknew"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.thatwaz.whoknew"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.thatwaz.whoknew"
            minSdk = 24
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
        implementation("androidx.compose.material:material-icons-extended:1.6.8")
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)

        implementation("androidx.room:room-runtime:2.6.1")
        implementation(libs.androidx.junit.ktx)
        kapt("androidx.room:room-compiler:2.6.0")
        implementation("androidx.room:room-ktx:2.6.1")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")

        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        implementation("com.github.bumptech.glide:glide:4.15.1")
        kapt("com.github.bumptech.glide:compiler:4.15.1")

        implementation("androidx.navigation:navigation-compose:2.7.7")

        implementation("com.google.accompanist:accompanist-insets:0.31.1-alpha")
        implementation("com.google.accompanist:accompanist-swiperefresh:0.31.1-alpha")

        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.compose.material3:material3:1.2.1")
        implementation("androidx.compose.ui:ui:1.6.8") // Ensure Compose UI library is included

        implementation("androidx.compose.ui:ui-text:1.6.8") // Include text input utilities


        implementation("com.google.dagger:hilt-android:2.50")
        kapt(libs.hilt.android.compiler)
        implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)

        androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.8")

        testImplementation("io.mockk:mockk:1.13.5")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    }
}