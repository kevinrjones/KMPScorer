package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

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
//    val upsertScorecard: UpsertScorecard
)