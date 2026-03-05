package cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase

sealed class SortOrder()

data object SortByTeam : SortOrder()
data object SortByOpponents : SortOrder()
data object SortByDate : SortOrder()
