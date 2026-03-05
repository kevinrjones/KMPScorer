package cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase

data class ListScorecardUseCases(
    val getScorecards: GetScorecardsUseCase,
    val deleteScorecard: DeleteScorecardUseCase,
)
