import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

// 讀取 local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val currencyApiKey = localProperties.getProperty("cub_currency_api_key") ?: ""


android {
    namespace = "tw.nick.cubflying"
    compileSdk = 35

    defaultConfig {
        applicationId = "tw.nick.cubflying"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            buildConfigField("boolean", "DEBUG", "true")
            buildConfigField("String", "BASE_URL", "\"https://www.kia.gov.tw\"")
            buildConfigField("String", "CURRECNY_API_KEY", "\"$currencyApiKey\"")
            buildConfigField("String","CURRENCY_URL", "\"https://api.freecurrencyapi.com\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "DEBUG", "true")
            buildConfigField("String", "BASE_URL", "\"https://www.kia.gov.tw\"")
            buildConfigField("String", "CURRECNY_API_KEY", currencyApiKey)
            buildConfigField("String","CURRENCY_URL", "https://api.freecurrencyapi.com")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Third Party Library
    implementation(libs.koin.android)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    implementation(libs.lottie)
    implementation(libs.timber)

    // KSP
    ksp(libs.moshi.kotlin.codegen)
}