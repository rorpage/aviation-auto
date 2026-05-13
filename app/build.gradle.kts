import java.util.Properties

plugins {
    id("com.android.application")
}

val localProperties = Properties().also { props ->
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { props.load(it) }
}
val openWeatherMapApiKey = localProperties.getProperty("OPEN_WEATHER_MAP_API_KEY")
    ?: (project.findProperty("OPEN_WEATHER_MAP_API_KEY") as String?)
    ?: ""

android {
    namespace = "com.rorpage.aviationauto"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rorpage.aviationauto"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "OPEN_WEATHER_MAP_API_KEY", "\"$openWeatherMapApiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.car.app:app:1.7.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.3.2")
}
