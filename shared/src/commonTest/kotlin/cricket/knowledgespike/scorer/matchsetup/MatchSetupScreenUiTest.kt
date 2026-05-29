package cricket.knowledgespike.scorer.matchsetup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.v2.runComposeUiTest
import cricket.knowledgespike.scorer.getPlatform
import androidx.compose.ui.test.onNodeWithText
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MatchSetupScreenUiTest {

    @Test
    fun `given start is unavailable when screen is rendered then start match button is disabled`() {
        if (!shouldRunComposeUiTests()) return

        runComposeUiTest {
            setContent {
                MatchSetupScreen(
                    widthSizeClass = WindowWidthSizeClass.Compact,
                    screenState = MatchSetupScreenState(),
                    contentPadding = PaddingValues(),
                    onEvent = {},
                )
            }

            onNodeWithText("Start Match").assertIsNotEnabled()
        }
    }

    private fun shouldRunComposeUiTests(): Boolean {
        return getPlatform().name.startsWith("iOS")
    }
}
