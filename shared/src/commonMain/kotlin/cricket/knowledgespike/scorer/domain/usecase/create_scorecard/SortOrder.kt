package cricket.knowledgespike.scorer.domain.usecase.create_scorecard

sealed class SortOrder()

data object SortByTeam : SortOrder()
data object SortByOpponents : SortOrder()
data object SortByDate : SortOrder()
