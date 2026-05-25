package cricket.knowledgespike.scorer.matchsetup

import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
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
        assertIs<MatchSetupStartMatchResult.ValidationError>(reducedState.startMatchResult)
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
}
