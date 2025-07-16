plugins {
    alias(libs.plugins.teampatch.android.library)
    alias(libs.plugins.teampatch.android.hilt)
    alias(libs.plugins.teampatch.android.library.compose)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.teampatch.core"

    defaultConfig {
        buildConfigField("int", "VERSION_CODE", rootProject.ext["versionCode"].toString())
        buildConfigField("String", "VERSION_NAME", "\"${rootProject.ext["versionName"]}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.kakao.sdk.v2.user)

    implementation(libs.google.play.app.update)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.security.crypto)

    implementation(libs.squareup.moshi.kotlin)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit2.converter.moshi)
    implementation(libs.squareup.moshi.kotlin)
    implementation(libs.logging.interceptor)

    api(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}