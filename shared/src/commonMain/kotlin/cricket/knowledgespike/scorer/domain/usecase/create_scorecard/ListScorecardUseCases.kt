package cricket.knowledgespike.scorer.domain.usecase.create_scorecard

data class ListScorecardUseCases(
    val getScorecards: GetScorecardsUseCase,
    val deleteScorecard: DeleteScorecardUseCase,
)
