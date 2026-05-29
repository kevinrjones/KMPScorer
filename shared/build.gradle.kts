import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

kotlin {
    android {
        namespace = "cricket.knowledgespike.scorerlibrary"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }
    }


    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val iosTargets = mutableListOf(iosArm64())
    if (isArm64) {
        iosTargets.add(iosSimulatorArm64())
    } else {
        iosTargets.add(iosX64())
    }

    iosTargets.forEach { iosTarget ->
        iosTarget.compilerOptions {
            freeCompilerArgs.add("-Xoverride-konan-properties=minVersion.ios=15.0")
        }
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = false
        }
    }

    jvm()

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.compose.material.icons)
            implementation(libs.jetbrains.compose.material.icons.extended)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.compose.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.okio)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.jetbrains.compose.windowsizeclass)

            api(libs.koin.core)

            implementation(libs.bundles.ktor)

            implementation(libs.arrow.core)

            api(project(":domain")) // The name matches the folder name

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.compose.ui.test)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}


dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    if (System.getProperty("os.arch") == "aarch64") {
        add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    } else {
        add("kspIosX64", libs.androidx.room.compiler)
    }
    add("kspJvm", libs.androidx.room.compiler)
    add("kspJvmTest", libs.androidx.room.compiler)
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

// make resources public so they are available in other modules
compose.resources {
    publicResClass = true
}
