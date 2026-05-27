package cricket.knowledgespike.scorer.matchsetup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MatchSetupScreenDesktopUiTest {

    private companion object {
        private const val SCHEDULE_TYPE_FIELD_TAG = "match_setup_schedule_type_field"
        private const val SCHEDULE_AMOUNT_FIELD_TAG = "match_setup_schedule_amount_field"
        private const val MATCH_DATE_FIELD_TAG = "match_setup_match_date_field"
        private const val MATCH_DATE_PICK_BUTTON_TAG = "match_setup_match_date_pick_button"
        private const val VENUE_FIELD_TAG = "match_setup_venue_field"
        private const val TOSS_WINNER_TEAM_A_OPTION_TAG = "match_setup_toss_winner_team_a_option"
        private const val TOSS_WINNER_TEAM_B_OPTION_TAG = "match_setup_toss_winner_team_b_option"
        private const val TOSS_DECISION_BAT_OPTION_TAG = "match_setup_toss_decision_bat_option"
    }

    @Test
    fun given_match_date_picker_when_date_selected_then_match_date_state_updates_with_canonical_value() = runComposeUiTest {
        val emittedEvents = mutableListOf<MatchSetupScreenEvent>()
        var confirmDateSelection: ((String) -> Unit)? = null
        var latestState = MatchSetupScreenState()

        setContent {
            var screenState by remember { mutableStateOf(latestState) }

            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = screenState,
                contentPadding = PaddingValues(),
                onEvent = { event ->
                    emittedEvents += event
                    when (event) {
                        is MatchSetupScreenEvent.MatchDateChanged -> {
                            latestState = latestState.copy(
                                formState = latestState.formState.copy(matchDate = event.value),
                            )
                            screenState = latestState
                        }

                        else -> Unit
                    }
                },
                matchDatePickerDialog = { _, _, onDateConfirmed ->
                    confirmDateSelection = onDateConfirmed
                },
            )
        }

        onNodeWithTag(MATCH_DATE_PICK_BUTTON_TAG).performClick()

        runOnIdle {
            confirmDateSelection?.invoke(" 2026-06-01 ")
        }

        runOnIdle {
            assertEquals(MatchSetupScreenEvent.MatchDateChanged("2026-06-01"), emittedEvents.last())
            assertEquals("2026-06-01", latestState.formState.matchDate)
        }
    }

    @Test
    fun given_match_date_picker_when_cancelled_then_match_date_remains_unchanged() = runComposeUiTest {
        val emittedEvents = mutableListOf<MatchSetupScreenEvent>()
        var dismissDatePicker: (() -> Unit)? = null
        var latestState = MatchSetupScreenState()

        setContent {
            var screenState by remember { mutableStateOf(latestState) }

            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = screenState,
                contentPadding = PaddingValues(),
                onEvent = { event ->
                    emittedEvents += event
                    when (event) {
                        is MatchSetupScreenEvent.MatchDateChanged -> {
                            latestState = latestState.copy(
                                formState = latestState.formState.copy(matchDate = event.value),
                            )
                            screenState = latestState
                        }

                        else -> Unit
                    }
                },
                matchDatePickerDialog = { _, onDismissRequest, _ ->
                    dismissDatePicker = onDismissRequest
                },
            )
        }

        onNodeWithTag(MATCH_DATE_PICK_BUTTON_TAG).performClick()

        runOnIdle {
            dismissDatePicker?.invoke()
        }

        runOnIdle {
            assertEquals(0, emittedEvents.count { it is MatchSetupScreenEvent.MatchDateChanged })
            assertEquals("", latestState.formState.matchDate)
        }
    }

    @Test
    fun given_match_date_picker_when_same_date_is_confirmed_then_no_date_change_event_is_emitted() = runComposeUiTest {
        val emittedEvents = mutableListOf<MatchSetupScreenEvent>()
        var confirmDateSelection: ((String) -> Unit)? = null
        var latestState = MatchSetupScreenState(
            formState = MatchSetupFormState(matchDate = "2026-05-25"),
        )

        setContent {
            var screenState by remember { mutableStateOf(latestState) }

            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = screenState,
                contentPadding = PaddingValues(),
                onEvent = { event ->
                    emittedEvents += event
                    when (event) {
                        is MatchSetupScreenEvent.MatchDateChanged -> {
                            latestState = latestState.copy(
                                formState = latestState.formState.copy(matchDate = event.value),
                            )
                            screenState = latestState
                        }

                        else -> Unit
                    }
                },
                matchDatePickerDialog = { _, _, onDateConfirmed ->
                    confirmDateSelection = onDateConfirmed
                },
            )
        }

        onNodeWithTag(MATCH_DATE_PICK_BUTTON_TAG).performClick()

        runOnIdle {
            confirmDateSelection?.invoke("2026-05-25")
        }

        runOnIdle {
            assertEquals(0, emittedEvents.count { it is MatchSetupScreenEvent.MatchDateChanged })
            assertEquals("2026-05-25", latestState.formState.matchDate)
        }
    }

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
        onAllNodesWithText("Player names", substring = true).assertCountEquals(1)
        onAllNodesWithText("Rosters", substring = true).assertCountEquals(0)
    }

    @Test
    fun given_schedule_type_dropdown_when_days_selected_then_schedule_type_event_is_emitted() = runComposeUiTest {
        val emittedEvents = mutableListOf<MatchSetupScreenEvent>()

        setContent {
            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = MatchSetupScreenState(),
                contentPadding = PaddingValues(),
                onEvent = { emittedEvents += it },
            )
        }

        onNodeWithTag(SCHEDULE_TYPE_FIELD_TAG).performClick()
        onNodeWithText("Days").performClick()

        runOnIdle {
            assertEquals(MatchSetupScreenEvent.ScheduleTypeChanged(MatchScheduleType.Days), emittedEvents.last())
        }
    }

    @Test
    fun given_schedule_amount_input_when_more_than_three_digits_entered_then_emitted_value_is_limited_to_three_digits() = runComposeUiTest {
        val emittedEvents = mutableListOf<MatchSetupScreenEvent>()
        var latestState = MatchSetupScreenState()

        setContent {
            var screenState by remember { mutableStateOf(latestState) }

            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = screenState,
                contentPadding = PaddingValues(),
                onEvent = { event ->
                    emittedEvents += event
                    when (event) {
                        is MatchSetupScreenEvent.ScheduleAmountChanged -> {
                            latestState = latestState.copy(
                                formState = latestState.formState.copy(scheduleAmount = event.value),
                            )
                            screenState = latestState
                        }

                        else -> Unit
                    }
                },
            )
        }

        onNodeWithTag(SCHEDULE_AMOUNT_FIELD_TAG).performTextInput("1234")

        runOnIdle {
            assertEquals(MatchSetupScreenEvent.ScheduleAmountChanged("123"), emittedEvents.last())
            assertEquals("123", latestState.formState.scheduleAmount)
        }
    }

    @Test
    fun given_compact_layout_when_tabbing_forward_then_focus_moves_between_toss_groups_only_once() = runComposeUiTest {
        val emittedEvents = mutableListOf<MatchSetupScreenEvent>()

        setContent {
            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = MatchSetupScreenState(
                    formState = MatchSetupFormState(
                        teamAName = "Falcons",
                        teamBName = "Kings",
                    ),
                ),
                contentPadding = PaddingValues(),
                onEvent = { emittedEvents += it },
            )
        }

        onNodeWithTag(MATCH_DATE_FIELD_TAG).performClick().assertIsFocused()
        onNodeWithTag(MATCH_DATE_FIELD_TAG).performKeyInput {
            keyDown(Key.Tab)
            keyUp(Key.Tab)
        }
        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).assertIsFocused()

        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).performKeyInput {
            keyDown(Key.DirectionRight)
            keyUp(Key.DirectionRight)
        }
        runOnIdle {
            assertEquals(MatchSetupScreenEvent.TossWinnerChanged(TossWinner.TeamA), emittedEvents.last())
        }

        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).performKeyInput {
            keyDown(Key.Tab)
            keyUp(Key.Tab)
        }
        onNodeWithTag(TOSS_DECISION_BAT_OPTION_TAG).assertIsFocused()

        onNodeWithTag(TOSS_DECISION_BAT_OPTION_TAG).performKeyInput {
            keyDown(Key.DirectionRight)
            keyUp(Key.DirectionRight)
        }
        runOnIdle {
            assertEquals(MatchSetupScreenEvent.TossDecisionChanged(TossDecision.Bat), emittedEvents.last())
        }

        onNodeWithTag(TOSS_DECISION_BAT_OPTION_TAG).performKeyInput {
            keyDown(Key.Tab)
            keyUp(Key.Tab)
        }
        onNodeWithTag(VENUE_FIELD_TAG).assertIsFocused()
    }

    @Test
    fun given_compact_layout_when_shift_tabbing_then_focus_moves_back_between_toss_groups_only_once() = runComposeUiTest {
        setContent {
            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = MatchSetupScreenState(
                    formState = MatchSetupFormState(
                        teamAName = "Falcons",
                        teamBName = "Kings",
                    ),
                ),
                contentPadding = PaddingValues(),
                onEvent = {},
            )
        }

        onNodeWithTag(MATCH_DATE_FIELD_TAG).performClick().assertIsFocused()
        onNodeWithTag(MATCH_DATE_FIELD_TAG).performKeyInput {
            keyDown(Key.Tab)
            keyUp(Key.Tab)
        }
        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).assertIsFocused()

        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).performKeyInput {
            keyDown(Key.Tab)
            keyUp(Key.Tab)
        }
        onNodeWithTag(TOSS_DECISION_BAT_OPTION_TAG).assertIsFocused()

        onNodeWithTag(TOSS_DECISION_BAT_OPTION_TAG).performKeyInput {
            keyDown(Key.ShiftLeft)
            keyDown(Key.Tab)
            keyUp(Key.Tab)
            keyUp(Key.ShiftLeft)
        }
        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).assertIsFocused()

        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).performKeyInput {
            keyDown(Key.ShiftLeft)
            keyDown(Key.Tab)
            keyUp(Key.Tab)
            keyUp(Key.ShiftLeft)
        }
        onNodeWithTag(MATCH_DATE_FIELD_TAG).assertIsFocused()
    }

    @Test
    fun given_toss_winner_group_focused_when_direction_changes_selection_then_focus_moves_with_selected_option() = runComposeUiTest {
        val emittedEvents = mutableListOf<MatchSetupScreenEvent>()

        setContent {
            var screenState by remember {
                mutableStateOf(
                    MatchSetupScreenState(
                        formState = MatchSetupFormState(
                            teamAName = "Falcons",
                            teamBName = "Kings",
                            tossWinner = TossWinner.TeamA,
                        ),
                    ),
                )
            }

            MatchSetupScreen(
                widthSizeClass = WindowWidthSizeClass.Compact,
                screenState = screenState,
                contentPadding = PaddingValues(),
                onEvent = { event ->
                    emittedEvents += event
                    when (event) {
                        is MatchSetupScreenEvent.TossWinnerChanged -> {
                            screenState = screenState.copy(
                                formState = screenState.formState.copy(
                                    tossWinner = event.value,
                                ),
                            )
                        }

                        else -> Unit
                    }
                },
            )
        }

        onNodeWithTag(MATCH_DATE_FIELD_TAG).performClick().assertIsFocused()
        onNodeWithTag(MATCH_DATE_FIELD_TAG).performKeyInput {
            keyDown(Key.Tab)
            keyUp(Key.Tab)
        }
        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).assertIsFocused()

        onNodeWithTag(TOSS_WINNER_TEAM_A_OPTION_TAG).performKeyInput {
            keyDown(Key.DirectionRight)
            keyUp(Key.DirectionRight)
        }
        onNodeWithTag(TOSS_WINNER_TEAM_B_OPTION_TAG).assertIsFocused()

        onNodeWithTag(TOSS_WINNER_TEAM_B_OPTION_TAG).performKeyInput {
            keyDown(Key.Spacebar)
            keyUp(Key.Spacebar)
        }
        runOnIdle {
            assertEquals(MatchSetupScreenEvent.TossWinnerChanged(TossWinner.TeamB), emittedEvents.last())
        }
    }
}
