package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import cricket.knowledgespike.scorer.data.source.InMemoryMatchRepository
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSchedule
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.home.HomeEditMatchButtonTagPrefix
import cricket.knowledgespike.scorer.home.HomeMatchRowTagPrefix
import cricket.knowledgespike.scorer.navigation.AppRouteStateStore
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class AppDesktopUiTest {

    @Test
    fun given_navigation_stack_depth_greater_than_one_when_top_back_clicked_then_route_pops() = runComposeUiTest {
        val routeStateStore = AppRouteStateStore()
        routeStateStore.push(ScorerRoute.MatchSetupRoute)

        setContent {
            App(
                appRouteStateStore = routeStateStore,
                matchRepository = InMemoryMatchRepository(),
            )
        }

        onNodeWithTag(AppBackButtonTag).performClick()

        runOnIdle {
            assertEquals(ScorerRoute.HomeRoute, routeStateStore.currentRoute.value)
            assertEquals(listOf(ScorerRoute.HomeRoute), routeStateStore.routeStack.value)
        }
    }

    @Test
    fun given_expanded_shell_when_new_match_rail_clicked_then_match_setup_is_pushed() = runComposeUiTest {
        val routeStateStore = AppRouteStateStore(initialRoute = ScorerRoute.HomeRoute)

        setContent {
            App(
                widthSizeClass = WindowWidthSizeClass.Expanded,
                appRouteStateStore = routeStateStore,
                matchRepository = InMemoryMatchRepository(),
            )
        }

        onNodeWithTag(AppNavigationNewMatchTag).performClick()

        runOnIdle {
            assertEquals(ScorerRoute.MatchSetupRoute, routeStateStore.currentRoute.value)
            assertEquals(
                listOf(ScorerRoute.HomeRoute, ScorerRoute.MatchSetupRoute),
                routeStateStore.routeStack.value,
            )
        }
    }

    @Test
    fun given_expanded_shell_with_deep_history_when_home_rail_clicked_then_stack_resets_to_home() = runComposeUiTest {
        val routeStateStore = AppRouteStateStore(initialRoute = ScorerRoute.HomeRoute)
        routeStateStore.push(ScorerRoute.MatchSetupRoute)
        routeStateStore.push(ScorerRoute.MatchSummaryRoute(matchId = 99L))

        setContent {
            App(
                widthSizeClass = WindowWidthSizeClass.Expanded,
                appRouteStateStore = routeStateStore,
                matchRepository = InMemoryMatchRepository(),
            )
        }

        onNodeWithTag(AppNavigationHomeTag).performClick()

        runOnIdle {
            assertEquals(ScorerRoute.HomeRoute, routeStateStore.currentRoute.value)
            assertEquals(listOf(ScorerRoute.HomeRoute), routeStateStore.routeStack.value)
        }
    }

    @Test
    fun given_scoring_entry_when_back_to_match_setup_clicked_then_route_returns_to_match_setup() = runComposeUiTest {
        val routeStateStore = AppRouteStateStore(
            initialRoute = ScorerRoute.ScoringEntryRoute(matchId = 12L),
        )

        setContent {
            App(
                appRouteStateStore = routeStateStore,
                matchRepository = InMemoryMatchRepository(),
            )
        }

        onNodeWithText("Back to Match setup").performClick()

        runOnIdle {
            assertEquals(ScorerRoute.MatchSetupRoute, routeStateStore.currentRoute.value)
        }
    }

    @Test
    fun given_new_match_saved_while_away_from_home_when_returning_home_then_history_list_refreshes() = runComposeUiTest {
        val routeStateStore = AppRouteStateStore(initialRoute = ScorerRoute.HomeRoute)
        val matchRepository = InMemoryMatchRepository()

        setContent {
            App(
                appRouteStateStore = routeStateStore,
                matchRepository = matchRepository,
            )
        }

        waitUntil(timeoutMillis = 5_000) {
            onAllNodesWithText("No saved matches yet. Start a new match to begin.")
                .fetchSemanticsNodes().isNotEmpty()
        }

        matchRepository.createMatchFromSetup(matchSetup())

        runOnIdle {
            routeStateStore.showMatchSetup()
        }
        waitForIdle()
        runOnIdle {
            routeStateStore.showHome()
        }

        waitUntil(timeoutMillis = 5_000) {
            onAllNodesWithText("Falcons vs Kings")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun given_home_row_inline_edit_when_clicked_then_route_moves_to_match_setup() = runComposeUiTest {
        val routeStateStore = AppRouteStateStore(initialRoute = ScorerRoute.HomeRoute)
        val matchRepository = InMemoryMatchRepository()
        val matchId = matchRepository.createMatchFromSetup(matchSetup()).fold(
            ifLeft = { failure -> error("Unexpected save failure: $failure") },
            ifRight = { it.id },
        )

        setContent {
            App(
                appRouteStateStore = routeStateStore,
                matchRepository = matchRepository,
            )
        }

        waitUntil(timeoutMillis = 5_000) {
            onAllNodesWithText("Falcons vs Kings")
                .fetchSemanticsNodes().isNotEmpty()
        }

        onNodeWithTag("$HomeEditMatchButtonTagPrefix$matchId").performClick()

        runOnIdle {
            assertEquals(ScorerRoute.MatchSetupRoute, routeStateStore.currentRoute.value)
        }
    }

    @Test
    fun given_home_row_inline_score_when_clicked_then_route_moves_to_match_summary() = runComposeUiTest {
        val routeStateStore = AppRouteStateStore(initialRoute = ScorerRoute.HomeRoute)
        val matchRepository = InMemoryMatchRepository()
        val matchId = matchRepository.createMatchFromSetup(matchSetup()).fold(
            ifLeft = { failure -> error("Unexpected save failure: $failure") },
            ifRight = { it.id },
        )

        setContent {
            App(
                appRouteStateStore = routeStateStore,
                matchRepository = matchRepository,
            )
        }

        waitUntil(timeoutMillis = 5_000) {
            onAllNodesWithText("Falcons vs Kings")
                .fetchSemanticsNodes().isNotEmpty()
        }

        onNodeWithTag("$HomeMatchRowTagPrefix$matchId").performClick()

        runOnIdle {
            assertEquals(ScorerRoute.MatchSummaryRoute(matchId = matchId), routeStateStore.currentRoute.value)
        }
    }
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
        matchDate = LocalDate.parse("2026-05-28"),
        venue = "Main Ground",
        umpireOne = null,
        umpireTwo = null,
        weather = null,
    )
}
