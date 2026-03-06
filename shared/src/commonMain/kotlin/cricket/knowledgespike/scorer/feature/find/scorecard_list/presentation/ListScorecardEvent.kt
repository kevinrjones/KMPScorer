package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails

sealed interface ListScorecardEvent {
    data class Deleted(val scorecard: ScorecardHeaderDetails) : ListScorecardEvent
    data object ErrorDeletingScorecard : ListScorecardEvent
}