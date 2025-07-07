package com.teampatch.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        includeSourceInformation.set(true)
    }

    commonExtension.apply {
        buildFeatures {
            compose = true
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx.compose.bom").get()
        "implementation"(platform(bom))
        "androidTestImplementation"(platform(bom))
        "implementation"(libs.findLibrary("androidx.ui").get())
        "implementation"(libs.findLibrary("androidx.ui.graphics").get())
        "implementation"(libs.findLibrary("androidx.ui.tooling.preview").get())
        "debugImplementation"(libs.findLibrary("androidx.ui.tooling").get())
        "androidTestImplementation"(libs.findLibrary("androidx.ui.test.junit4").get())
        "debugImplementation"(libs.findLibrary("androidx.ui.test.manifest").get())
    }
}