package cricket.knowledgespike.scorer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun given_app_launches_when_match_setup_rendered_then_header_and_disabled_start_button_are_visible() {
        composeTestRule.onNodeWithText("Match setup").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start Match").assertIsNotEnabled()
    }
}
