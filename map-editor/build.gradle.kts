plugins {
    id("com.soywiz.korge")
}

korge {
    jvmMainClassName = "com.github.servb.pph.pheroes.mapEditor.ExportDlgKt"

    targetJvm()

    bundle("https://github.com/korlibs/korlibs-bundle-source-extensions.git::korma-rectangle-experimental-ext::696a97640bb93a66f07ca008cca84b1ae4013e57##d2d9e3eb8f9f8eb5c137e847677eb8b3e9038c30d1f4457d1bd05cafc5c3f251")
}

kotlin {
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
        }

        getByName("commonMain") {
            dependencies {
                implementation(rootProject)
            }
        }
    }
}
