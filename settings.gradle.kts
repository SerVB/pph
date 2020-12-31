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

    plugins {
        id("com.soywiz.korge") version korgePluginVersion
    }
}

rootProject.name = "PPH"
