package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateDuration
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateMatchDate
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateMatchLabel
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateOpponentsName
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateScorer
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateStartTime
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTeamWinningToss
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTitle
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateVenue
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidationUseCasesTests {

    @Test
    fun `test ValidateTitle with empty string returns failure`() {
        val validateTitle =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTitle()
        val result = validateTitle("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateTitle with valid string returns success`() {
        val validateTitle =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTitle()
        val result = validateTitle("Match Title")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateVenue with empty string returns failure`() {
        val validateVenue =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateVenue()
        val result = validateVenue("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateVenue with valid string returns success`() {
        val validateVenue =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateVenue()
        val result = validateVenue("Lord's")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateScorer with empty string returns failure`() {
        val validateScorer =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateScorer()
        val result = validateScorer("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateScorer with valid string returns success`() {
        val validateScorer =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateScorer()
        val result = validateScorer("John Doe")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateDuration with empty string returns failure`() {
        val validateDuration =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateDuration()
        val result = validateDuration("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateDuration with valid string returns success`() {
        val validateDuration =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateDuration()
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
        val validateMatchDate =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateMatchDate()
        val result = validateMatchDate("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateMatchDate with valid string returns success`() {
        val validateMatchDate =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateMatchDate()
        val result = validateMatchDate("2023-10-27")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateStartTime with empty string returns failure`() {
        val validateStartTime =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateStartTime()
        val result = validateStartTime("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateStartTime with valid string returns success`() {
        val validateStartTime =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateStartTime()
        val result = validateStartTime("10:30")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateMatchLabel with empty string returns failure`() {
        val validateMatchLabel =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateMatchLabel()
        val result = validateMatchLabel("")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateMatchLabel with valid string returns success`() {
        val validateMatchLabel =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateMatchLabel()
        val result = validateMatchLabel("Test Match")
        assertTrue(result.successful)
    }

    @Test
    fun `test ValidateTeamWinningToss with empty string returns failure`() {
        val validateTeamWinningToss =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTeamWinningToss()
        val result = validateTeamWinningToss("", "Team A", "Team B")
        assertFalse(result.successful)
    }

    @Test
    fun `test ValidateTeamWinningToss with team matching either name returns success`() {
        val validateTeamWinningToss =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTeamWinningToss()
        assertTrue(validateTeamWinningToss("Team A", "Team A", "Team B").successful)
        assertTrue(validateTeamWinningToss("Team B", "Team A", "Team B").successful)
    }

    @Test
    fun `test ValidateTeamWinningToss with team matching neither name returns failure`() {
        val validateTeamWinningToss =
            _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTeamWinningToss()
        val result = validateTeamWinningToss("Team C", "Team A", "Team B")
        assertFalse(result.successful)
    }
}
