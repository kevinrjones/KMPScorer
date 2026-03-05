package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase.SortOrder

sealed class ListScorecardEvent {
    data class Order(val order: SortOrder) : ListScorecardEvent()
}

