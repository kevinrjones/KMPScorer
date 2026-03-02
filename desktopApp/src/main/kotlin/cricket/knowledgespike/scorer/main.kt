package cricket.knowledgespike.scorer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cricket.knowledgespike.scorer.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP Scorer",
        ) {
            App()
        }
    }
}