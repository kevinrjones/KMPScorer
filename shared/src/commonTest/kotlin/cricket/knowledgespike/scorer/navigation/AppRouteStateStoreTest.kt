package cricket.knowledgespike.scorer.navigation

import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSchedule
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AppRouteStateStoreTest {

    @Test
    fun `given new store when created then current route is match setup`() {
        val routeStateStore = AppRouteStateStore()

        assertEquals(ScorerRoute.MatchSetupRoute, routeStateStore.currentRoute.value)
    }

    @Test
    fun `given setup is ready when scoring route is requested then current route is scoring entry`() {
        val routeStateStore = AppRouteStateStore()
        val scoringEntryRoute = ScorerRoute.ScoringEntryRoute(matchSetup())

        routeStateStore.showRoute(scoringEntryRoute)

        val scoringRoute = assertIs<ScorerRoute.ScoringEntryRoute>(routeStateStore.currentRoute.value)
        assertEquals("Falcons", scoringRoute.matchSetup.teamAName)
        assertEquals("Kings", scoringRoute.matchSetup.teamBName)
    }

    @Test
    fun `given scoring entry route when match setup is requested then current route returns to setup`() {
        val routeStateStore = AppRouteStateStore(
            initialRoute = ScorerRoute.ScoringEntryRoute(matchSetup()),
        )

        routeStateStore.showMatchSetup()

        assertEquals(ScorerRoute.MatchSetupRoute, routeStateStore.currentRoute.value)
    }

    private fun matchSetup(): MatchSetup {
        return MatchSetup(
            teamAName = "Falcons",
            teamBName = "Kings",
            schedule = MatchSchedule(
                type = MatchScheduleType.Overs,
                amount = 20,
            ),
            tossWinner = TossWinner.TeamA,
            tossDecision = TossDecision.Bat,
            matchDate = LocalDate.parse("2026-05-25"),
            venue = null,
            umpireOne = null,
            umpireTwo = null,
            weather = null,
        )
    }
}