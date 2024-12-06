import org.gradle.internal.impldep.org.jsoup.safety.Safelist.basic

pluginManagement {
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

        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1IjoicmF5ZW5ibiIsImEiOiJjbTRiYjhoNWswMWxoMmxxdm9oNWt0cDZ2In0.sIrAfAH8uOm5oqvKgvYQzQ"
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1IjoicmF5ZW5ibiIsImEiOiJjbTRiYjhoNWswMWxoMmxxdm9oNWt0cDZ2In0.sIrAfAH8uOm5oqvKgvYQzQ"
            }
        }

        maven ("https://jitpack.io" )
    }
}

rootProject.name = "PDM"
include(":app")
