package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cricket.knowledgespike.scorer.di.initKoin
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() {
    initKoin()
    application {

        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP Scorer",
        ) {
            val widthSizeClass = calculateWindowSizeClass().widthSizeClass
            App(widthSizeClass)
        }
    }
}