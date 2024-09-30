pluginManagement {
    repositories {
        google() // Aquí defines el repositorio de Google
        mavenCentral() // También puedes definir otros como Maven Central
        gradlePluginPortal() // Es opcional pero recomendado para plugins
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Repos preferidos en settings.gradle.kts
    repositories {
        google() // Definir repositorio de Google aquí también
        mavenCentral()
    }
}

rootProject.name = "CrowFunding"
include(":app")
