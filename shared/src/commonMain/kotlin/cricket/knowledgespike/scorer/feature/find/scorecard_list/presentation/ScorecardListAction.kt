package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

sealed interface ScorecardListAction {
    data class OnSearchQueryChange(val team: String, val opponents: String) : ScorecardListAction
    data class onAddOrEditScorecard(val id: Int?) : ScorecardListAction
    data class onScore(val id: Int?) : ScorecardListAction
}