import com.android.build.api.dsl.ApplicationExtension
import com.teampatch.convention.CURRENT_JAVA_VERSION
import com.teampatch.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                kotlinExtension.apply {
                    version = CURRENT_JAVA_VERSION
                }
                compileSdk = 34

                defaultConfig {
                    targetSdk = 34
                    minSdk = 24
                    testInstrumentationRunner = "com.harmony.core.ui.test.HiltTestRunner"
                }

                compileOptions {
                    sourceCompatibility = CURRENT_JAVA_VERSION
                    targetCompatibility = CURRENT_JAVA_VERSION

                    isCoreLibraryDesugaringEnabled = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                dependencies {
                    "implementation"(libs.findLibrary("androidx.core.ktx").get())
                    "coreLibraryDesugaring"(libs.findLibrary("android.tools.desugar").get())
                }
            }
        }
    }
}