package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.foundation.compose.UiText

data class ScorecardListState(
    val teamSearchName: String = "",
    val opponentsSearchName: String = "",
    val isLoading: Boolean = false,
    val scorecards: List<ScorecardDetailsView> = emptyList(),
    val error: UiText? = null
)

data class ScorecardDetailsView(val scorecardHeaderDetails: ScorecardHeaderDetails, val isExpanded: Boolean = false)

