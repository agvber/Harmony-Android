plugins {
    alias(libs.plugins.teampatch.android.library)
    alias(libs.plugins.teampatch.android.library.compose)
}

android {
    namespace = "com.teampatch.core.designsystem"
}

dependencies {

    implementation(project(":core:domain"))

    api(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}