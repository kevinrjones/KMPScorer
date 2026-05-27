package cricket.knowledgespike.scorer.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppRouteStateStore(
    initialRoute: ScorerRoute = ScorerRoute.MatchSetupRoute,
) {

    private val _currentRoute = MutableStateFlow(initialRoute)
    val currentRoute: StateFlow<ScorerRoute> = _currentRoute.asStateFlow()

    fun showRoute(route: ScorerRoute) {
        _currentRoute.update { route }
    }

    fun showMatchSetup() {
        _currentRoute.update { ScorerRoute.MatchSetupRoute }
    }
}