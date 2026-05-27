package cricket.knowledgespike.scorer.matchsetup

import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MatchSetupStateStoreTest {

    private val createMatchSetupUseCase = CreateMatchSetupUseCase()

    @Test
    fun `given blank screen state when start match requested then validation error is shown`() {
        val reducedState = reduceMatchSetupScreenState(
            currentState = MatchSetupScreenState(),
            event = MatchSetupScreenEvent.StartMatchRequested,
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertFalse(reducedState.canStartMatch)
        val validationError = assertIs<MatchSetupStartMatchResult.ValidationError>(reducedState.startMatchResult)
        assertEquals("Team A name is required", validationError.message)
    }

    @Test
    fun `given required fields when start match requested then match setup is ready`() {
        val stateWithRequiredFields = MatchSetupScreenState(
            formState = MatchSetupFormState(
                teamAName = "Falcons",
                teamBName = "Kings",
                scheduledOvers = "20",
                tossWinner = TossWinner.TeamA,
                tossDecision = TossDecision.Bat,
                matchDate = "2026-05-25",
            ),
            canStartMatch = true,
        )

        val reducedState = reduceMatchSetupScreenState(
            currentState = stateWithRequiredFields,
            event = MatchSetupScreenEvent.StartMatchRequested,
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertTrue(reducedState.canStartMatch)
        assertIs<MatchSetupStartMatchResult.Ready>(reducedState.startMatchResult)
    }

    @Test
    fun `given ready result when form changes then result resets to idle and form is updated`() {
        val readyState = MatchSetupScreenState(
            formState = MatchSetupFormState(
                teamAName = "Falcons",
                teamBName = "Kings",
                scheduledOvers = "20",
                tossWinner = TossWinner.TeamA,
                tossDecision = TossDecision.Bat,
                matchDate = "2026-05-25",
            ),
            canStartMatch = true,
            startMatchResult = MatchSetupStartMatchResult.Ready(matchSetup()),
        )

        val reducedState = reduceMatchSetupScreenState(
            currentState = readyState,
            event = MatchSetupScreenEvent.TeamANameChanged("Warriors"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertEquals("Warriors", reducedState.formState.teamAName)
        assertTrue(reducedState.canStartMatch)
        assertEquals(MatchSetupStartMatchResult.Idle, reducedState.startMatchResult)
    }

    @Test
    fun `given invalid form when start match requested via store then callback is not invoked`() {
        var readyMatchSetup: MatchSetup? = null
        val stateStore = MatchSetupStateStore(
            createMatchSetupUseCase = createMatchSetupUseCase,
            onMatchSetupReady = { readyMatchSetup = it },
        )

        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)

        assertNull(readyMatchSetup)
        assertIs<MatchSetupStartMatchResult.ValidationError>(stateStore.screenState.value.startMatchResult)
    }

    @Test
    fun `given valid form when start match requested via store then callback receives match setup`() {
        var readyMatchSetup: MatchSetup? = null
        val stateStore = MatchSetupStateStore(
            createMatchSetupUseCase = createMatchSetupUseCase,
            onMatchSetupReady = { readyMatchSetup = it },
        )

        stateStore.onEvent(MatchSetupScreenEvent.TeamANameChanged("Falcons"))
        stateStore.onEvent(MatchSetupScreenEvent.TeamBNameChanged("Kings"))
        stateStore.onEvent(MatchSetupScreenEvent.ScheduledOversChanged("20"))
        stateStore.onEvent(MatchSetupScreenEvent.TossWinnerChanged(TossWinner.TeamA))
        stateStore.onEvent(MatchSetupScreenEvent.TossDecisionChanged(TossDecision.Bat))
        stateStore.onEvent(MatchSetupScreenEvent.MatchDateChanged("2026-05-25"))

        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)

        val latestState = stateStore.screenState.value
        assertTrue(latestState.canStartMatch)
        assertIs<MatchSetupStartMatchResult.Ready>(latestState.startMatchResult)
        assertEquals("Falcons", readyMatchSetup?.teamAName)
        assertEquals("Kings", readyMatchSetup?.teamBName)
    }

    @Test
    fun `given started match state when reset requested then initial state is restored`() {
        val startedState = MatchSetupScreenState(
            formState = MatchSetupFormState(teamAName = "Falcons"),
            canStartMatch = true,
            startMatchResult = MatchSetupStartMatchResult.ValidationError("Sample error"),
        )

        val reducedState = reduceMatchSetupScreenState(
            currentState = startedState,
            event = MatchSetupScreenEvent.ResetRequested,
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertEquals(MatchSetupScreenState(), reducedState)
    }

    private fun matchSetup(): MatchSetup {
        return MatchSetup(
            teamAName = "Falcons",
            teamBName = "Kings",
            scheduledOvers = 20,
            tossWinner = TossWinner.TeamA,
            tossDecision = TossDecision.Bat,
            matchDate = LocalDate.parse("2026-05-25"),
            venue = null,
            umpireOne = null,
            umpireTwo = null,
            weather = null,
        )
    }
}
