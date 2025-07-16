plugins {
    alias(libs.plugins.teampatch.android.library)
    alias(libs.plugins.teampatch.android.library.compose)
    alias(libs.plugins.teampatch.android.hilt)
    alias(libs.plugins.teampatch.android.feature)
}

android {
    namespace = "com.teampatch.feature.login"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}