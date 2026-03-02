package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.window.ComposeUIViewController
import cricket.knowledgespike.scorer.di.initKoin
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    App(widthSizeClass)
}