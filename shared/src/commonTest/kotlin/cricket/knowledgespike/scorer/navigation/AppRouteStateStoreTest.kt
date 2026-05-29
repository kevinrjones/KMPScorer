package cricket.knowledgespike.scorer.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AppRouteStateStoreTest {

    @Test
    fun `given new store when created then current route is home`() {
        val routeStateStore = AppRouteStateStore()

        assertEquals(ScorerRoute.HomeRoute, routeStateStore.currentRoute.value)
        assertEquals(listOf(ScorerRoute.HomeRoute), routeStateStore.routeStack.value)
    }

    @Test
    fun `given route is pushed when requested then current route is updated and route is appended to stack`() {
        val routeStateStore = AppRouteStateStore()
        val scoringEntryRoute = ScorerRoute.ScoringEntryRoute(matchId = 101L)

        routeStateStore.push(scoringEntryRoute)

        val scoringRoute = assertIs<ScorerRoute.ScoringEntryRoute>(routeStateStore.currentRoute.value)
        assertEquals(101L, scoringRoute.matchId)
        assertEquals(
            listOf(ScorerRoute.HomeRoute, scoringEntryRoute),
            routeStateStore.routeStack.value,
        )
    }

    @Test
    fun `given stack depth greater than one when pop is called then current route returns to previous`() {
        val routeStateStore = AppRouteStateStore()
        routeStateStore.push(ScorerRoute.MatchSetupRoute)
        routeStateStore.push(ScorerRoute.ScoringEntryRoute(matchId = 44L))

        val didPop = routeStateStore.pop()

        assertEquals(true, didPop)
        assertEquals(ScorerRoute.MatchSetupRoute, routeStateStore.currentRoute.value)
        assertEquals(
            listOf(ScorerRoute.HomeRoute, ScorerRoute.MatchSetupRoute),
            routeStateStore.routeStack.value,
        )
    }

    @Test
    fun `given root stack when pop is called then route remains unchanged and returns false`() {
        val routeStateStore = AppRouteStateStore()

        val didPop = routeStateStore.pop()

        assertEquals(false, didPop)
        assertEquals(ScorerRoute.HomeRoute, routeStateStore.currentRoute.value)
        assertEquals(listOf(ScorerRoute.HomeRoute), routeStateStore.routeStack.value)
    }

    @Test
    fun `given routes in stack when reset to home is called then stack is cleared to home`() {
        val routeStateStore = AppRouteStateStore()

        routeStateStore.push(ScorerRoute.MatchSetupRoute)
        routeStateStore.push(ScorerRoute.MatchSummaryRoute(matchId = 12L))

        routeStateStore.resetToHome()

        assertEquals(ScorerRoute.HomeRoute, routeStateStore.currentRoute.value)
        assertEquals(listOf(ScorerRoute.HomeRoute), routeStateStore.routeStack.value)
    }

    @Test
    fun `given stack has top route when replace top is called then top route is swapped without growing stack`() {
        val routeStateStore = AppRouteStateStore()
        routeStateStore.push(ScorerRoute.MatchSetupRoute)

        routeStateStore.replaceTop(ScorerRoute.ScoringEntryRoute(matchId = 55L))

        assertEquals(ScorerRoute.ScoringEntryRoute(matchId = 55L), routeStateStore.currentRoute.value)
        assertEquals(
            listOf(ScorerRoute.HomeRoute, ScorerRoute.ScoringEntryRoute(matchId = 55L)),
            routeStateStore.routeStack.value,
        )
    }

    @Test
    fun `given scoring entry id when helper is called then current route is scoring entry for that id`() {
        val routeStateStore = AppRouteStateStore()

        routeStateStore.showScoringEntry(matchId = 44L)

        val scoringRoute = assertIs<ScorerRoute.ScoringEntryRoute>(routeStateStore.currentRoute.value)
        assertEquals(44L, scoringRoute.matchId)
    }

    @Test
    fun `given summary helper when called then current route is summary for match id`() {
        val routeStateStore = AppRouteStateStore()

        routeStateStore.showMatchSummary(matchId = 12L)

        assertEquals(ScorerRoute.MatchSummaryRoute(matchId = 12L), routeStateStore.currentRoute.value)
    }

    @Test
    fun `given any current route when home is requested then current route is home`() {
        val routeStateStore = AppRouteStateStore(initialRoute = ScorerRoute.MatchSetupRoute)

        routeStateStore.showHome()

        assertEquals(ScorerRoute.HomeRoute, routeStateStore.currentRoute.value)
        assertEquals(listOf(ScorerRoute.HomeRoute), routeStateStore.routeStack.value)
    }
}