package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import cricket.knowledgespike.scorer.foundation.UiText
import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.model.ScorecardIdentifyingDetails

data class ScorecardListState(
    val teamSearchName: String = "",
    val opponentsSearchName: String = "",
    val isLoading: Boolean = false,
    val scorecards: List<ScorecardIdentifyingDetails> = emptyList(),
    val error: UiText? = null
)

