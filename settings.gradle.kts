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
include(":feature:onboarding")
include(":feature:home")
include(":feature:settings")
include(":feature:profile-edit")
include(":feature:family-info")
include(":feature:question")
include(":feature:question-detail")
include(":feature:question-expand")
include(":feature:answer")
include(":feature:daily")
include(":feature:memorycard-registration")
include(":core:data")
include(":core:common")
include(":core:authentication")
include(":core:network")
include(":core:ui-test")
include(":feature:daily-expand")
include(":feature:daily-edit")
include(":core:database")
include(":feature:memorystorage")
include(":feature:memorystorage-detail")
