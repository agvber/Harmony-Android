plugins {
    id("teampatch.android.library")
    id("teampatch.android.library.compose")
    id("teampatch.android.hilt")
    id("teampatch.android.feature")
}

android {
    namespace = "com.teampatch.feature.routine"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}