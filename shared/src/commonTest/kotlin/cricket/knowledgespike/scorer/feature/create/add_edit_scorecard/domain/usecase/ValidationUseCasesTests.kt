package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidationUseCasesTests {

    @Test
    fun `test ValidateTitle with empty string returns failure`() {
        val validateTitle = ValidateTitle()
        val result = validateTitle("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateTitle with valid string returns success`() {
        val validateTitle = ValidateTitle()
        val result = validateTitle("Match Title")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateVenue with empty string returns failure`() {
        val validateVenue = ValidateVenue()
        val result = validateVenue("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateVenue with valid string returns success`() {
        val validateVenue = ValidateVenue()
        val result = validateVenue("Lord's")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateScorer with empty string returns failure`() {
        val validateScorer = ValidateScorer()
        val result = validateScorer("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateScorer with valid string returns success`() {
        val validateScorer = ValidateScorer()
        val result = validateScorer("John Doe")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateDuration with empty string returns failure`() {
        val validateDuration = ValidateDuration()
        val result = validateDuration("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateDuration with valid string returns success`() {
        val validateDuration = ValidateDuration()
        val result = validateDuration("1 day")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateOpponentsName with empty string returns failure`() {
        val validateOpponentsName = ValidateOpponentsName()
        val result = validateOpponentsName("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateOpponentsName with valid string returns success`() {
        val validateOpponentsName = ValidateOpponentsName()
        val result = validateOpponentsName("Australia")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateMatchDate with empty string returns failure`() {
        val validateMatchDate = ValidateMatchDate()
        val result = validateMatchDate("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateMatchDate with valid string returns success`() {
        val validateMatchDate = ValidateMatchDate()
        val result = validateMatchDate("2023-10-27")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateStartTime with empty string returns failure`() {
        val validateStartTime = ValidateStartTime()
        val result = validateStartTime("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateStartTime with valid string returns success`() {
        val validateStartTime = ValidateStartTime()
        val result = validateStartTime("10:30")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateMatchLabel with empty string returns failure`() {
        val validateMatchLabel = ValidateMatchLabel()
        val result = validateMatchLabel("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateMatchLabel with valid string returns success`() {
        val validateMatchLabel = ValidateMatchLabel()
        val result = validateMatchLabel("Test Match")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateTeamWinningToss with empty string returns failure`() {
        val validateTeamWinningToss = ValidateTeamWinningToss()
        val result = validateTeamWinningToss("", "Team A", "Team B")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateTeamWinningToss with team matching either name returns success`() {
        val validateTeamWinningToss = ValidateTeamWinningToss()
        assertTrue(validateTeamWinningToss("Team A", "Team A", "Team B").successful)
        assertTrue(validateTeamWinningToss("Team B", "Team A", "Team B").successful)
    }

    @Test
    fun `test ValidateTeamWinningToss with team matching neither name returns failure`() {
        val validateTeamWinningToss = ValidateTeamWinningToss()
        val result = validateTeamWinningToss("Team C", "Team A", "Team B")
        assertFalse(result.successful)
    }
}
