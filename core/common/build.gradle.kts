plugins {
    id("teampatch.android.library")
}

android {
    namespace = "com.teampatch.core.common"

    defaultConfig {
        buildConfigField("int", "VERSION_CODE", rootProject.ext["versionCode"].toString())
        buildConfigField("String", "VERSION_NAME", "\"${rootProject.ext["versionName"]}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.serialization.json)
}