pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()  // For Mockito and JUnit
    }
}

rootProject.name = "KitchenMasters"
include(":app")
