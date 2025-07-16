import com.android.build.gradle.LibraryExtension
import com.teampatch.convention.configureJvm
import com.teampatch.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(this)
            configureJvm(this)

            buildTypes {
                release {
                    isMinifyEnabled = true
                }
                create("loggedInDebug") {
                    initWith(getByName("debug"))
                }

            }
        }
    }
}