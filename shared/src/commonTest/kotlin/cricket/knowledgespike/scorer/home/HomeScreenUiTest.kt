package cricket.knowledgespike.scorer.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import cricket.knowledgespike.scorer.getPlatform
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class HomeScreenUiTest {

    @Test
    fun `given home screen when new button is tapped then new match event is emitted`() {
        if (!shouldRunComposeUiTests()) return

        var emittedEvent: HomeScreenEvent? = null

        runComposeUiTest {
            setContent {
                HomeScreen(
                    screenState = HomeScreenState(listState = HomeMatchListState.Empty),
                    contentPadding = PaddingValues(),
                    onEvent = { emittedEvent = it },
                )
            }

            onNodeWithTag(HomeNewButtonTag).performClick()
        }

        assertEquals(HomeScreenEvent.NewMatchRequested, emittedEvent)
    }

    @Test
    fun `given content state when edit button is tapped then edit event is emitted`() {
        if (!shouldRunComposeUiTests()) return

        var emittedEvent: HomeScreenEvent? = null

        runComposeUiTest {
            setContent {
                HomeScreen(
                    screenState = HomeScreenState(
                        listState = HomeMatchListState.Content(
                            matches = listOf(
                                HomeMatchListItem(
                                    id = 22L,
                                    title = "Falcons vs Kings",
                                    subtitle = "Overs 20 • 2026-05-25",
                                ),
                            ),
                        ),
                    ),
                    contentPadding = PaddingValues(),
                    onEvent = { emittedEvent = it },
                )
            }

            onNodeWithTag("${HomeEditMatchButtonTagPrefix}22").performClick()
        }

        assertEquals(HomeScreenEvent.EditMatchRequested(matchId = 22L), emittedEvent)
    }

    @Test
    fun `given content state when match row is tapped then open saved match event is emitted`() {
        if (!shouldRunComposeUiTests()) return

        var emittedEvent: HomeScreenEvent? = null

        runComposeUiTest {
            setContent {
                HomeScreen(
                    screenState = HomeScreenState(
                        listState = HomeMatchListState.Content(
                            matches = listOf(
                                HomeMatchListItem(
                                    id = 22L,
                                    title = "Falcons vs Kings",
                                    subtitle = "Overs 20 • 2026-05-25",
                                ),
                            ),
                        ),
                    ),
                    contentPadding = PaddingValues(),
                    onEvent = { emittedEvent = it },
                )
            }

            onNodeWithTag("${HomeMatchRowTagPrefix}22").performClick()
        }

        assertEquals(HomeScreenEvent.OpenSavedMatchRequested(matchId = 22L), emittedEvent)
    }

    @Test
    fun `given content state when delete button is tapped then delete requested event is emitted`() {
        if (!shouldRunComposeUiTests()) return

        var emittedEvent: HomeScreenEvent? = null

        runComposeUiTest {
            setContent {
                HomeScreen(
                    screenState = HomeScreenState(
                        listState = HomeMatchListState.Content(
                            matches = listOf(
                                HomeMatchListItem(
                                    id = 22L,
                                    title = "Falcons vs Kings",
                                    subtitle = "Overs 20 • 2026-05-25",
                                ),
                            ),
                        ),
                    ),
                    contentPadding = PaddingValues(),
                    onEvent = { emittedEvent = it },
                )
            }

            onNodeWithTag("${HomeDeleteMatchButtonTagPrefix}22").performClick()
        }

        assertEquals(HomeScreenEvent.DeleteMatchRequested(matchId = 22L), emittedEvent)
    }

    @Test
    fun `given delete confirmation dialog state when delete is tapped then delete confirmed event is emitted`() {
        if (!shouldRunComposeUiTests()) return

        var emittedEvent: HomeScreenEvent? = null

        runComposeUiTest {
            setContent {
                HomeScreen(
                    screenState = HomeScreenState(
                        listState = HomeMatchListState.Empty,
                        dialogState = HomeMatchDialogState.DeleteConfirmation(
                            matchId = 22L,
                            matchTitle = "Falcons vs Kings",
                        ),
                    ),
                    contentPadding = PaddingValues(),
                    onEvent = { emittedEvent = it },
                )
            }

            onNodeWithText("Delete").performClick()
        }

        assertEquals(HomeScreenEvent.DeleteMatchConfirmed(matchId = 22L), emittedEvent)
    }

    private fun shouldRunComposeUiTests(): Boolean {
        return getPlatform().name.startsWith("iOS")
    }
}
