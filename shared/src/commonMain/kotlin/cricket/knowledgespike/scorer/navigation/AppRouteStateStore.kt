package cricket.knowledgespike.scorer.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppRouteStateStore(
    initialRoute: ScorerRoute = ScorerRoute.HomeRoute,
) {

    private val _routeStack = MutableStateFlow(listOf(initialRoute))
    val routeStack: StateFlow<List<ScorerRoute>> = _routeStack.asStateFlow()

    private val _currentRoute = MutableStateFlow(_routeStack.value.last())
    val currentRoute: StateFlow<ScorerRoute> = _currentRoute.asStateFlow()

    fun push(route: ScorerRoute) {
        _routeStack.update { currentStack ->
            currentStack + route
        }
        _currentRoute.update { route }
    }

    fun replaceTop(route: ScorerRoute) {
        _routeStack.update { currentStack ->
            currentStack.dropLast(1) + route
        }
        _currentRoute.update { route }
    }

    fun pop(): Boolean {
        val canPop = _routeStack.value.size > 1
        if (!canPop) {
            return false
        }

        _routeStack.update { currentStack ->
            currentStack.dropLast(1)
        }
        _currentRoute.update { _routeStack.value.last() }
        return true
    }

    fun resetToHome() {
        _routeStack.update { listOf(ScorerRoute.HomeRoute) }
        _currentRoute.update { ScorerRoute.HomeRoute }
    }

    fun showRoute(route: ScorerRoute) {
        push(route)
    }

    fun showScoringEntry(matchId: Long) {
        push(ScorerRoute.ScoringEntryRoute(matchId = matchId))
    }

    fun showMatchSummary(matchId: Long) {
        push(ScorerRoute.MatchSummaryRoute(matchId = matchId))
    }

    fun showHome() {
        resetToHome()
    }

    fun showMatchSetup() {
        push(ScorerRoute.MatchSetupRoute)
    }
}