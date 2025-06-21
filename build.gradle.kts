import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.jlleitschuh.gradle.ktlint) apply false
}

subprojects {
    apply<KtlintPlugin>()
    configure<KtlintExtension> {
        android = true
        version = "1.5.0"
        ignoreFailures = true
        outputToConsole = true
        reporters {
            reporter(ReporterType.JSON)
        }
        additionalEditorconfig = mapOf(
            "indent_style" to "space",
            "end_of_line" to "lf",
            "ij_kotlin_allow_trailing_comma" to "true",
            "insert_final_newline" to "false",
            "ktlint_code_style" to "android_studio",
            "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
            "ktlint_standard_no-unused-imports" to "enabled",
            "ktlint_standard_no-wildcard-imports" to "disabled",
            "ktlint_standard_max-line-length" to "disabled",
            "ktlint_standard_type-parameter-comment" to "disabled",
            "ktlint_standard_discouraged-comment-location" to "disabled",
            "ktlint_standard_backing-property-naming" to "disabled",
        )

        tasks.named("ktlintCheck") {
            ignoreFailures = false
        }
    }
}

ext {
    set("versionCode", 1)
    set("versionName", "1.0.0")
}