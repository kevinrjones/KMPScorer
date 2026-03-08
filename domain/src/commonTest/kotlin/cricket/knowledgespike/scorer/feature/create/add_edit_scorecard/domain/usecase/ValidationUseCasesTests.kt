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
import cricket.knowledgespike.scorer.foundation.ValidationReason
import kotlin.test.Test
import kotlin.test.assertIs

class ValidationUseCasesTests {

    @Test
    fun `test ValidateTitle with empty string returns failure`() {
        val validateTitle =
            ValidateTitle()
        val result = validateTitle("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateTitle with valid string returns success`() {
        val validateTitle = ValidateTitle()
        val result = validateTitle("Match Title")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test ValidateVenue with empty string returns failure`() {
        val validateVenue = ValidateVenue()
        val result = validateVenue("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateVenue with valid string returns success`() {
        val validateVenue = ValidateVenue()
        val result = validateVenue("Lord's")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test ValidateScorer with empty string returns failure`() {
        val validateScorer = ValidateScorer()
        val result = validateScorer("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateScorer with valid string returns success`() {
        val validateScorer = ValidateScorer()
        val result = validateScorer("John Doe")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test ValidateDuration with empty string returns failure`() {
        val validateDuration = ValidateDuration()
        val result = validateDuration("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateDuration with valid string returns success`() {
        val validateDuration = ValidateDuration()
        val result = validateDuration("1 day")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test ValidateOpponentsName with empty string returns failure`() {
        val validateOpponentsName = ValidateOpponentsName()
        val result = validateOpponentsName("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateOpponentsName with valid string returns success`() {
        val validateOpponentsName = ValidateOpponentsName()
        val result = validateOpponentsName("Australia")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test ValidateMatchDate with empty string returns failure`() {
        val validateMatchDate = ValidateMatchDate()
        val result = validateMatchDate("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateMatchDate with valid string returns success`() {
        val validateMatchDate = ValidateMatchDate()
        val result = validateMatchDate("2023-10-27")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test ValidateStartTime with empty string returns failure`() {
        val validateStartTime = ValidateStartTime()
        val result = validateStartTime("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateStartTime with valid string returns success`() {
        val validateStartTime = ValidateStartTime()
        val result = validateStartTime("10:30")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test ValidateMatchLabel with empty string returns failure`() {
        val validateMatchLabel = ValidateMatchLabel()
        val result = validateMatchLabel("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateMatchLabel with valid string returns success`() {
        val validateMatchLabel = ValidateMatchLabel()
        val result = validateMatchLabel("Test Match")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test ValidateTeamWinningToss with empty string returns failure`() {
        val validateTeamWinningToss = ValidateTeamWinningToss()
        val result = validateTeamWinningToss("", "Team A", "Team B")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test ValidateTeamWinningToss with team matching either name returns success`() {
        val validateTeamWinningToss = ValidateTeamWinningToss()
        assertIs<ValidationReason.Succeeded>(validateTeamWinningToss("Team A", "Team A", "Team B"))
        assertIs<ValidationReason.Succeeded>(validateTeamWinningToss("Team B", "Team A", "Team B"))
    }

    @Test
    fun `test ValidateTeamWinningToss with team matching neither name returns failure`() {
        val validateTeamWinningToss = ValidateTeamWinningToss()
        val result = validateTeamWinningToss("Team C", "Team A", "Team B")
        assertIs<ValidationReason.TeamWinningToss>(result)
    }
}
