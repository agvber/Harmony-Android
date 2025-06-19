import com.android.build.api.dsl.DefaultConfig
import com.android.tools.build.jetifier.core.utils.Log
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("teampatch.android.hilt")
    id("teampatch.android.feature")
}

android {
    namespace = "com.teampatch.harmony"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.teampatch.harmony"
        minSdk = 24
        targetSdk = 34
        versionCode = rootProject.ext["versionCode"].toString().toInt()
        versionName = rootProject.ext["versionName"].toString()

        testInstrumentationRunner = "com.harmony.core.ui.test.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(project(":core:authentication"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:login"))
    implementation(project(":feature:home"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:family-info"))
    implementation(project(":feature:profile-edit"))
    implementation(project(":feature:daily"))
    implementation(project(":feature:daily-edit"))
    implementation(project(":feature:daily-expand"))
    implementation(project(":feature:memorycard-registration"))
    implementation(project(":feature:question"))
    implementation(project(":feature:question-expand"))
    implementation(project(":feature:question-detail"))
    implementation(project(":feature:answer"))

    implementation(project(":feature:memorystorage"))
    implementation(project(":feature:memorystorage-detail"))
    androidTestImplementation(project(":core:ui-test"))

    implementation(libs.coil.compose)

    androidTestImplementation(libs.hilt.android)
    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    coreLibraryDesugaring(libs.android.tools.desugar)

    implementation(libs.kakao.sdk.v2.user)
}