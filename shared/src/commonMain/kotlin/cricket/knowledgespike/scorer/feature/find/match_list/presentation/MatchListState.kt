package cricket.knowledgespike.scorer.feature.find.match_list.presentation

import cricket.knowledgespike.scorer.foundation.UiText
import cricket.knowledgespike.scorer.feature.find.match_list.domain.Match

data class MatchListState(
    val teamSearchName: String = "",
    val opponentsSearchName: String = "",
    val isLoading: Boolean = false,
    val scorecards: List<Match> = emptyList(),
    val error: UiText? = null
)

