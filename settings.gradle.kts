pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}
gradle.startParameter.excludedTaskNames.apply {
    add(":build-logic:convention:testClasses")
}

rootProject.name = "Harmony-Android"
include(":app")
include(":core:designsystem")
include(":core:domain")
include(":core:data")
include(":core:common")
include(":core:network")
include(":core:ui-test")
include(":core:database")
include(":feature:onboarding")
include(":feature:login")
include(":feature:home")
include(":feature:memory")
include(":feature:settings")
include(":feature:question")
include(":feature:daily")