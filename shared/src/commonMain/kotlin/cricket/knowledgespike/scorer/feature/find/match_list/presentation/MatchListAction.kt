package cricket.knowledgespike.scorer.feature.find.match_list.presentation

import cricket.knowledgespike.scorer.feature.find.match_list.domain.Match

sealed interface MatchListAction {
    data class OnSearchQueryChange(val team: String, val opponents: String) : MatchListAction
    data class OnMatchClick(val match: Match) : MatchListAction
}