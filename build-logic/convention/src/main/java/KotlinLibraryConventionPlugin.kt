import com.teampatch.convention.CURRENT_JAVA_VERSION
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("java-library")
            apply("org.jetbrains.kotlin.jvm")
        }

        extensions.configure<JavaPluginExtension> {
            kotlinExtension.apply {
                version = CURRENT_JAVA_VERSION
            }
            sourceCompatibility = CURRENT_JAVA_VERSION
            targetCompatibility = CURRENT_JAVA_VERSION
        }

    }
}