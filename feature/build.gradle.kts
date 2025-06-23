plugins {
    id("teampatch.android.library")
    id("teampatch.android.library.compose")
    id("teampatch.android.hilt")
    id("teampatch.android.feature")
    id("kotlin-parcelize")
}

android {
    namespace = "com.teampatch.feature"
}

dependencies {

    // Module
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))

    // Android-Core
    implementation(libs.androidx.core.ktx)
    androidTestImplementation(libs.androidx.espresso.core)

    // Android-UI
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // ImageLoader
    implementation(libs.coil.compose)

    // Junit
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}