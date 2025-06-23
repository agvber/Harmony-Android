plugins {
    id("teampatch.android.library")
    id("teampatch.android.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.teampatch.core.data"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)

    // Serializable
    implementation(libs.squareup.moshi.kotlin)

    implementation(libs.kakao.sdk.v2.user)

    implementation(libs.google.play.app.update)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.security.crypto)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}