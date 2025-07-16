plugins {
    alias(libs.plugins.teampatch.jvm.library)
}

dependencies {
    implementation(libs.androidx.paging.common)
    implementation(libs.hilt.core)
}