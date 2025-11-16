import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.buildkonfig)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            // optIn 설정 추가
            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlin.Experimental",
                    "-Xexpect-actual-classes"  // Beta 경고 억제
                )
            )
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.components.uiToolingPreview)

                // Kotlinx Serialization (data/model 이동을 위해 추가)
                implementation(libs.kotlinx.serialization.json)

                // Ktor & Network (공통)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.auth)

                // DataStore (공통)
                implementation(libs.androidx.dataStore.core)
                implementation(libs.androidx.dataStore.preferences)

                // KMP ViewModel (Compose 1.7.3 공식)
                implementation(libs.lifecycle.viewmodel.compose)

                // KMP Navigation (JetBrains multiplatform)
                implementation(libs.navigation.compose)
            }
        }

        val androidMain by getting {
            dependencies {
                // Android 전용 의존성
                implementation(libs.androidx.annotation)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.timber)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                // viewModelCompose는 commonMain에서 제공 (KMP용)

                // Koin
                implementation(libs.koin.core)
                implementation(libs.koin.android)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.viewmodel.navigation)

                // Ktor & Network (Android 엔진만)
                implementation(libs.ktor.client.okhttp)

                // Jetpack Compose (Android)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                val composeBom = project.dependencies.platform(libs.androidx.compose.bom)
                implementation(composeBom)
                implementation(libs.androidx.compose.foundation.core)
                implementation(libs.androidx.compose.foundation.layout)
                implementation(libs.androidx.compose.animation)
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.compose.material.iconsExtended)
                implementation(libs.androidx.compose.ui.tooling.preview)
                implementation(libs.accompanist.appcompat.theme)
                implementation(libs.accompanist.swiperefresh)

                // Firebase
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.analytics)
                implementation(libs.play.services.oss.licenses)
                implementation(libs.androidx.ui.viewbinding)
            }
        }
    }
}

android {
    namespace = "unithon.helpjob"
    compileSdk = libs.versions.compileSdk.get().toInt()

    signingConfigs {
        create("release") {
            storeFile = file(
                localProperties.getProperty("KEYSTORE_FILE") ?: "../helpjob-release.keystore.jks"
            )
            storePassword = localProperties.getProperty("KEYSTORE_PASSWORD")
            keyAlias = localProperties.getProperty("KEY_ALIAS")
            keyPassword = localProperties.getProperty("KEY_PASSWORD")
        }
    }

    defaultConfig {
        applicationId = "unithon.helpjob"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "VERSION_NAME", "\"$versionName\"")

//        Room 사용시 적용
//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments += "room.incremental" to "true"
//            }
//        }
    }

    buildTypes {
        debug {
            // debug/release 동시 설치 가능하도록 (권장)
            applicationIdSuffix = ".debug"
            // 빌드 속도 향상
            isCrunchPngs = false
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            // 서버에서 대부분 중요한 개인정보 다루므로 프론트 계열에서 난독화 하지 않았음(관리 더 용이)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isCrunchPngs = true

            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    androidResources {
        generateLocaleConfig = true
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}

dependencies {
    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling.core)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

buildkonfig {
    packageName = "unithon.helpjob"
    objectName = "BuildKonfig"

    defaultConfigs {
        val apiBaseUrl = localProperties.getProperty("API_BASE_URL")
            ?: throw GradleException("API_BASE_URL이 local.properties에 설정되지 않았습니다")
        buildConfigField(FieldSpec.Type.STRING, "API_BASE_URL", apiBaseUrl)
        buildConfigField(FieldSpec.Type.BOOLEAN, "DEBUG", "false")
    }

    targetConfigs {
        create("android") {
            buildConfigField(FieldSpec.Type.BOOLEAN, "DEBUG", "true")
        }
    }
}

apply(plugin = "com.google.android.gms.oss-licenses-plugin")

//// Compiler Reports 생성 (최적화 분석용)
//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    compilerOptions {
//        if (project.findProperty("composeCompilerReports") == "true") {
//            freeCompilerArgs.addAll(
//                listOf(
//                    "-P",
//                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
//                            layout.buildDirectory.dir("compose_compiler").get().asFile.absolutePath
//                )
//            )
//        }
//        if (project.findProperty("composeCompilerMetrics") == "true") {
//            freeCompilerArgs.addAll(
//                listOf(
//                    "-P",
//                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
//                            layout.buildDirectory.dir("compose_compiler").get().asFile.absolutePath
//                )
//            )
//        }
//    }
//}