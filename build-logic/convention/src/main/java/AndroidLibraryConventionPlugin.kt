import com.android.build.gradle.LibraryExtension
import com.teampatch.convention.CURRENT_JAVA_VERSION
import com.teampatch.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                kotlinExtension.apply {
                    version = CURRENT_JAVA_VERSION
                }

                compileSdk = 35

                buildTypes {
                    release {
                        isMinifyEnabled = true
                    }
                    create("loggedInDebug") {
                        initWith(getByName("debug"))
                    }
                }

                defaultConfig {
                    minSdk = 24

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = CURRENT_JAVA_VERSION
                    targetCompatibility = CURRENT_JAVA_VERSION
                    isCoreLibraryDesugaringEnabled = true
                }

                dependencies {
                    "implementation"(libs.findLibrary("androidx.core.ktx").get())
                    "coreLibraryDesugaring"(libs.findLibrary("android.tools.desugar").get())
                }
            }
        }
    }
}