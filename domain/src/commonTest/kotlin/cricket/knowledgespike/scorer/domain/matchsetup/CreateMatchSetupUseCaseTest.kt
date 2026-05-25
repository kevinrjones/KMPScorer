package cricket.knowledgespike.scorer.domain.matchsetup

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateMatchSetupUseCaseTest {

    private val createMatchSetupUseCase = CreateMatchSetupUseCase()

    @Test
    fun `given blank team A when create match setup then missing team A error is returned`() {
        val result = createMatchSetupUseCase(validDraft(teamAName = ""))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.MissingTeamAName, result.leftOrNull())
    }

    @Test
    fun `given invalid overs when create match setup then invalid overs error is returned`() {
        val result = createMatchSetupUseCase(validDraft(scheduledOvers = "0"))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.InvalidScheduledOvers, result.leftOrNull())
    }

    @Test
    fun `given valid draft when create match setup then normalized match setup is returned`() {
        val result = createMatchSetupUseCase(
            validDraft(
                venue = "  Main Ground  ",
                umpireOne = "   ",
            ),
        )

        assertTrue(result.isRight())
        assertEquals("Main Ground", result.getOrNull()?.venue)
        assertEquals(null, result.getOrNull()?.umpireOne)
    }

    private fun validDraft(
        teamAName: String = "Falcons",
        teamBName: String = "Kings",
        scheduledOvers: String = "20",
        tossWinner: TossWinner? = TossWinner.TeamA,
        tossDecision: TossDecision? = TossDecision.Bat,
        matchDate: String = "2026-05-25",
        venue: String = "Main Ground",
        umpireOne: String = "Umpire One",
        umpireTwo: String = "Umpire Two",
        weather: String = "Sunny",
    ): MatchSetupDraft {
        return MatchSetupDraft(
            teamAName = teamAName,
            teamBName = teamBName,
            scheduledOvers = scheduledOvers,
            tossWinner = tossWinner,
            tossDecision = tossDecision,
            matchDate = matchDate,
            venue = venue,
            umpireOne = umpireOne,
            umpireTwo = umpireTwo,
            weather = weather,
        )
    }
}
