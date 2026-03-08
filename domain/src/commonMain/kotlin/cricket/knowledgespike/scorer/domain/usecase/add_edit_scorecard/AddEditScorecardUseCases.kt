package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.GetScorecardUseCase

data class AddEditScorecardUseCases(
    val validateTeamName: ValidateTeamName,
    val validateOpponentsName: ValidateOpponentsName,
    val validateVenue: ValidateVenue,
    val validateTitle: ValidateTitle,
    val validateMatchDate: ValidateMatchDate,
    val validateBattingSide: ValidateBattingSide,
    val validateScorer: ValidateScorer,
    val validateMatchLabel: ValidateMatchLabel,
    val validateDuration: ValidateDuration,
    val validateStartTime: ValidateStartTime,
    val validateTeamWinningToss: ValidateTeamWinningToss,

    val upsertScorecard: UpsertScorecard,
    val findScorecard: GetScorecardUseCase,
)