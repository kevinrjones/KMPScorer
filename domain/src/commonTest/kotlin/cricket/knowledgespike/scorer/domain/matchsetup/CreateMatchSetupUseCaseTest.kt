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
    fun `given invalid schedule amount when create match setup then invalid schedule amount error is returned`() {
        val result = createMatchSetupUseCase(validDraft(scheduleAmount = "0"))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.InvalidScheduleAmount, result.leftOrNull())
    }

    @Test
    fun `given schedule amount above three digits when create match setup then invalid schedule amount error is returned`() {
        val result = createMatchSetupUseCase(validDraft(scheduleAmount = "1000"))

        assertTrue(result.isLeft())
        assertEquals(MatchSetupValidationError.InvalidScheduleAmount, result.leftOrNull())
    }

    @Test
    fun `given days schedule type when create match setup then setup preserves selected schedule type`() {
        val result = createMatchSetupUseCase(validDraft(scheduleType = MatchScheduleType.Days, scheduleAmount = "3"))

        assertTrue(result.isRight())
        val setup = assertNotNull(result.getOrNull())
        assertEquals(MatchScheduleType.Days, setup.schedule.type)
        assertEquals(3, setup.schedule.amount)
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
    fun `given omitted optional fields when create match setup then setup is created with null optionals`() {
        val result = createMatchSetupUseCase(
            validDraft(
                venue = "   ",
                umpireOne = "",
                umpireTwo = " ",
                weather = "\t",
            ),
        )

        assertTrue(result.isRight())
        val setup = assertNotNull(result.getOrNull())
        assertEquals(null, setup.venue)
        assertEquals(null, setup.umpireOne)
        assertEquals(null, setup.umpireTwo)
        assertEquals(null, setup.weather)
    }

    @Test
    fun `given identical valid draft when create match setup invoked repeatedly then results are deterministic`() {
        val draft = validDraft(
            teamAName = "  Falcons  ",
            teamBName = " Kings ",
            venue = " Main Ground ",
            umpireOne = "Umpire One",
            umpireTwo = "Umpire Two",
            weather = "Cloudy",
        )

        val firstResult = createMatchSetupUseCase(draft)
        val secondResult = createMatchSetupUseCase(draft)

        assertEquals(firstResult, secondResult)
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
        assertEquals(MatchScheduleType.Overs, setup.schedule.type)
        assertEquals(20, setup.schedule.amount)
        assertEquals("Main Ground", setup.venue)
        assertEquals(null, setup.umpireOne)
    }

    private fun validDraft(
        teamAName: String = "Falcons",
        teamBName: String = "Kings",
        scheduleType: MatchScheduleType = MatchScheduleType.Overs,
        scheduleAmount: String = "20",
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
            scheduleType = scheduleType,
            scheduleAmount = scheduleAmount,
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
