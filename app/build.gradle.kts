import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "unithon.helpjob"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "unithon.helpjob"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiBaseUrl = localProperties.getProperty("API_BASE_URL")
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")

//        Room 사용시 적용
//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments += "room.incremental" to "true"
//            }
//        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val apiBaseUrl = localProperties.getProperty("API_BASE_URL")
            buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    // kotlin 실험적 API 사용 시 경고 제거
    kotlin {
        compilerOptions {
            optIn.addAll(
                "kotlin.RequiresOptIn",
                "kotlin.Experimental"
            )
        }
    }
    androidResources {
        generateLocaleConfig = true
    }
}

dependencies {

    // App dependencies
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)

    // Architecture Components
//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx)
//    ksp(libs.room.compiler)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    // Hilt
    implementation(libs.hilt.android.core)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging)

    // DataStore
    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)

    // 다국어 지원
    implementation(libs.androidx.appcompat)
    
    // Jetpack Compose
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(libs.androidx.activity.compose)
    implementation(composeBom)
    implementation(libs.androidx.compose.foundation.core)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.accompanist.appcompat.theme)
    implementation(libs.accompanist.swiperefresh)

    debugImplementation(composeBom)
    debugImplementation(libs.androidx.compose.ui.tooling.core)

    // 테스트 의존성 추가
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}