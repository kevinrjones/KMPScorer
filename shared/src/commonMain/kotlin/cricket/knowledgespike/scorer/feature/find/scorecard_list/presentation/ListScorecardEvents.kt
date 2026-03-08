package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import cricket.knowledgespike.scorer.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.SortOrder

sealed interface ListScorecardUiEvent {
    data class Order(val order: SortOrder) : ListScorecardUiEvent
    data class TryDelete(val scorecard: ScorecardHeaderDetails) : ListScorecardUiEvent
}

