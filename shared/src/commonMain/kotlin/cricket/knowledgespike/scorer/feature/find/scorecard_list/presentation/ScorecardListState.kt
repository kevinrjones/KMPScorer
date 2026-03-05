package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.foundation.UiText

data class ScorecardListState(
    val teamSearchName: String = "",
    val opponentsSearchName: String = "",
    val isLoading: Boolean = false,
    val scorecards: List<ScorecardHeaderDetails> = emptyList(),
    val error: UiText? = null
)

