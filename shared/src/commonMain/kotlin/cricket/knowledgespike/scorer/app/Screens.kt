package cricket.knowledgespike.scorer.app

import kotlinx.serialization.Serializable

sealed interface ScorerRoute {
    @Serializable
    object ScorecardsListRoute : ScorerRoute
    @Serializable
    data class AddEditScorecardRoute(val scorecardId: Int?) : ScorerRoute
    @Serializable
    data class ScoreMatchRoute(val scorecardId: Int?) : ScorerRoute
}
