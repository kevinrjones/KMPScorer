package cricket.knowledgespike.scorer.navigation

sealed interface ScorerRoute {
    data object HomeRoute : ScorerRoute
    data object MatchSetupRoute : ScorerRoute
    data class ScoringEntryRoute(val matchId: Long) : ScorerRoute
    data class MatchSummaryRoute(val matchId: Long) : ScorerRoute
}
