plugins {
    alias(libs.plugins.teampatch.android.library)
    alias(libs.plugins.teampatch.android.library.compose)
    alias(libs.plugins.teampatch.android.hilt)
    alias(libs.plugins.teampatch.android.feature)
    id("kotlin-parcelize")
}

android {
    namespace = "com.teampatch.feature"
}

dependencies {

    implementation(project(":core"))
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}