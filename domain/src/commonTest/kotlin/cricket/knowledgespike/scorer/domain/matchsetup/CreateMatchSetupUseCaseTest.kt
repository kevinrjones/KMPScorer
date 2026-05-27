package cricket.knowledgespike.scorer.domain.matchsetup

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
    fun `given blank team B when create match setup then missing team B error is returned`() {
        val result = createMatchSetupUseCase(validDraft(teamBName = ""))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.MissingTeamBName, result.leftOrNull())
    }

    @Test
    fun `given matching team names when create match setup then team names must differ error is returned`() {
        val result = createMatchSetupUseCase(validDraft(teamAName = "Falcons", teamBName = "  falcons  "))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.TeamNamesMustDiffer, result.leftOrNull())
    }

    @Test
    fun `given invalid overs when create match setup then invalid overs error is returned`() {
        val result = createMatchSetupUseCase(validDraft(scheduledOvers = "0"))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.InvalidScheduledOvers, result.leftOrNull())
    }

    @Test
    fun `given missing toss winner when create match setup then missing toss winner error is returned`() {
        val result = createMatchSetupUseCase(validDraft(tossWinner = null))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.MissingTossWinner, result.leftOrNull())
    }

    @Test
    fun `given missing toss decision when create match setup then missing toss decision error is returned`() {
        val result = createMatchSetupUseCase(validDraft(tossDecision = null))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.MissingTossDecision, result.leftOrNull())
    }

    @Test
    fun `given invalid match date when create match setup then invalid match date error is returned`() {
        val result = createMatchSetupUseCase(validDraft(matchDate = "2026/05/25"))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.InvalidMatchDate, result.leftOrNull())
    }

    @Test
    fun `given valid draft when create match setup then normalized match setup is returned`() {
        val result = createMatchSetupUseCase(
            validDraft(
                teamAName = "  Falcons  ",
                teamBName = "  Kings ",
                venue = "  Main Ground  ",
                umpireOne = "   ",
            ),
        )

        assertTrue(result.isRight())
        val setup = assertNotNull(result.getOrNull())
        assertEquals("Falcons", setup.teamAName)
        assertEquals("Kings", setup.teamBName)
        assertEquals("Main Ground", setup.venue)
        assertEquals(null, setup.umpireOne)
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
