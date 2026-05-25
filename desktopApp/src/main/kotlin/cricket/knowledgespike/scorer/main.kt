package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() {

    application {

        Window(
            onCloseRequest = ::exitApplication,
            title = "Knowledgespike Cricket Scorer",
        ) {
            var resetMatchSetupSignal by remember { mutableIntStateOf(0) }

            MenuBar {
                Menu("Match") {
                    Item(
                        text = "New Match Setup",
                        onClick = { resetMatchSetupSignal += 1 },
                    )
                }
            }

            val widthSizeClass = calculateWindowSizeClass().widthSizeClass
            App(
                widthSizeClass = widthSizeClass,
                resetMatchSetupSignal = resetMatchSetupSignal,
            )
        }
    }
}