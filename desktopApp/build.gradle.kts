import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    dependencies {
        implementation(projects.shared)

        implementation(compose.desktop.currentOs)
        implementation(libs.kotlinx.coroutinesSwing)
        implementation(libs.ktor.client.okhttp)
        implementation(libs.koin.core)
        implementation(libs.koin.jvm)
        implementation(libs.jetbrains.compose.windowsizeclass)
    }
}

dependencies {
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.compose.ui.test)
}

compose.desktop {
    application {
        mainClass = "cricket.knowledgespike.scorer.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "cricket.knowledgespike.scorer"
            packageVersion = "1.0.0"
        }
    }
}
