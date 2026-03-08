package cricket.knowledgespike.scorer.domain.usecase.create_scorecard

sealed class SortOrder()

data object SortByTeam : cricket.knowledgespike.scorer.domain.usecase.create_scorecard.SortOrder()
data object SortByOpponents : cricket.knowledgespike.scorer.domain.usecase.create_scorecard.SortOrder()
data object SortByDate : cricket.knowledgespike.scorer.domain.usecase.create_scorecard.SortOrder()
