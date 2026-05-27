package cricket.knowledgespike.scorer.matchsetup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MatchSetupScreenDesktopUiTest {

    @Test
    fun given_compact_layout_when_screen_renders_then_match_setup_header_and_disabled_start_button_are_visible() = runComposeUiTest {
        setContent {
            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = MatchSetupScreenState(),
                contentPadding = PaddingValues(),
                onEvent = {},
            )
        }

        onNodeWithText("Match setup").assertIsDisplayed()
        onNodeWithText("Start Match").assertIsNotEnabled()
    }
}
