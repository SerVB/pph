plugins {
    id("com.soywiz.korge")
}

korge {
    targetJvm()
    targetJs()

    bundle("https://github.com/korlibs/korlibs-bundle-source-extensions.git::korma-rectangle-experimental-ext::696a97640bb93a66f07ca008cca84b1ae4013e57##d2d9e3eb8f9f8eb5c137e847677eb8b3e9038c30d1f4457d1bd05cafc5c3f251")
}

val kotestVersion: String by project
val reflectionsVersion: String by project

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
                implementation("org.reflections:reflections:$reflectionsVersion")
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val compressedResourcesFile = rootProject.file("src/commonMain/resources/resources.zip")

tasks.create<Zip>("packResources") {
    doFirst {
        compressedResourcesFile.delete()
        compressedResourcesFile.parentFile.mkdirs()
    }

    from(rootProject.file("resourcesRoot")) {
        include("**")
    }

    archiveFileName.set(compressedResourcesFile.name)
    destinationDirectory.set(compressedResourcesFile.parentFile)
}

tasks.clean {
    doLast {
        compressedResourcesFile.delete()
    }
}
