package cricket.knowledgespike.scorer.navigation

sealed interface ScorerRoute {
    data object MatchSetupRoute : ScorerRoute
}
