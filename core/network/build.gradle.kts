plugins {
    alias(libs.plugins.teampatch.android.library)
    alias(libs.plugins.teampatch.android.hilt)
}

android {
    namespace = "com.teampatch.core.network"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:domain"))

    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit2.converter.moshi)
    implementation(libs.squareup.moshi.kotlin)
    implementation(libs.logging.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}