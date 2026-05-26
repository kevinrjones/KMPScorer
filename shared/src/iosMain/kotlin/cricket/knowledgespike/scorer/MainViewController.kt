@file:Suppress("FunctionName", "unused")

package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.window.ComposeUIViewController

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun MainViewController() = ComposeUIViewController(
    configure = {  }
) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    App(widthSizeClass)
}