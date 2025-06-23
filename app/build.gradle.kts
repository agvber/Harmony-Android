import com.android.build.api.dsl.DefaultConfig
import com.android.tools.build.jetifier.core.utils.Log
import java.util.Properties

plugins {
    id("teampatch.android.application")
    id("teampatch.android.hilt")
    id("teampatch.android.feature")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.teampatch.harmony"

    defaultConfig {
        applicationId = "com.teampatch.harmony"
        versionCode = rootProject.ext["versionCode"].toString().toInt()
        versionName = rootProject.ext["versionName"].toString()
        initVariable()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("loggedInDebug") {
            initWith(getByName("debug"))
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

fun DefaultConfig.initVariable() {
    val file = File(rootDir.absolutePath + "/.env/key.properties")
    if (file.exists()) {
        file.inputStream().use { inputStream ->
            val properties = Properties()
            properties.load(inputStream)
            setBuildConfigOrManifest(
                type = "String",
                name = "KAKAO_NATIVE_KEY",
                value = properties.getProperty("KAKAO_NATIVE_KEY")
            )
        }
        return
    }

    setBuildConfigOrManifest("String", "KAKAO_NATIVE_KEY", "null")
    Log.e(
        tag = "build.gradle.kts(:app)",
        message = "주의!!!! ${file.absolutePath} 파일이 존재하지 않습니다."
    )
}

fun DefaultConfig.setBuildConfigOrManifest(
    type: String,
    name: String,
    value: String,
) {
    if (type == "String") {
        buildConfigField(
            type = type,
            name = name,
            value = "\"$value\""
        )
    } else {
        buildConfigField(
            type = type,
            name = name,
            value = value
        )
    }
    manifestPlaceholders[name] = value
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
    implementation(libs.kakao.sdk.v2.user)

    androidTestImplementation(libs.hilt.android)
    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring(libs.android.tools.desugar)
}