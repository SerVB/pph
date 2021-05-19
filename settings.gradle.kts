pluginManagement {
    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/korlibs/korlibs")
        maven("https://plugins.gradle.org/m2/")
        mavenCentral()
        google()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }

    val korgePluginVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.soywiz.korge") {
                useModule("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
            }
        }
    }

    plugins {
        id("com.soywiz.korge") apply false
    }
}

rootProject.name = "PPH"
