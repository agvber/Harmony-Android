package com.teampatch.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 28

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions.apply {
            isCoreLibraryDesugaringEnabled = true
        }
    }

    dependencies {
        "implementation"(libs.findLibrary("androidx.core.ktx").get())
        "coreLibraryDesugaring"(libs.findLibrary("android.tools.desugar").get())
    }
}