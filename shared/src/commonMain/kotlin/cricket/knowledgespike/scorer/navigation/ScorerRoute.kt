package cricket.knowledgespike.scorer.navigation

import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup

sealed interface ScorerRoute {
    data object MatchSetupRoute : ScorerRoute
    data class ScoringEntryRoute(val matchSetup: MatchSetup) : ScorerRoute
}
