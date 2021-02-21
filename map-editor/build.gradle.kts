plugins {
    id("com.soywiz.korge")
}

korge {
    jvmMainClassName = "com.github.servb.pph.pheroes.mapEditor.ExportDlgKt"

    targetJvm()
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
