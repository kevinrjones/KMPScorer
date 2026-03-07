package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase.SortOrder

sealed interface ListScorecardUiEvent {
    data class Order(val order: SortOrder) : ListScorecardUiEvent
    data class TryDelete(val scorecard: ScorecardHeaderDetails) : ListScorecardUiEvent
}

