plugins {
    id("com.soywiz.korge")
}

korge {
    targetJvm()
    targetJs()
}

val kotestVersion: String by project

kotlin {
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
        }

        getByName("jvmTest") {
            dependencies {
                implementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
                implementation("org.reflections:reflections:0.9.12")
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
