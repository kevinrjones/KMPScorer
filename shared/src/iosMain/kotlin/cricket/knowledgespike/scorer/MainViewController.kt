package cricket.knowledgespike.scorer

import androidx.compose.ui.window.ComposeUIViewController
import cricket.knowledgespike.scorer.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}