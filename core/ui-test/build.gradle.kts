plugins {
    alias(libs.plugins.teampatch.android.library)
    alias(libs.plugins.teampatch.android.hilt)
}

android {
    namespace = "com.harmony.core.ui.test"
}

dependencies {

    implementation(libs.junit)
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.hilt.android.test)
}