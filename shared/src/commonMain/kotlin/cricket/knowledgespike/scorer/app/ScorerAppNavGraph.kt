package cricket.knowledgespike.scorer.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation.AddEditScorecardScreenRoot
import cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation.MatchListScreenRoot

@Composable
fun ScoreAppNavGraph(
    navController: NavHostController = rememberNavController(),
    isExpandedScreen: Boolean = false,
    startRoute: ScorerRoute = ScorerRoute.ScorecardsListRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable<ScorerRoute.ScorecardsListRoute> {
            MatchListScreenRoot(
                isExpandedScreen = isExpandedScreen,
                onAddOrEditScorecard = {
                    navController.navigate(
                        ScorerRoute.AddEditScorecardRoute(it)
                    )
                },
                onScore = {
                    navController.navigate(
                        ScorerRoute.ScoreMatchRoute(it)
                    )
                })
        }

        composable<ScorerRoute.AddEditScorecardRoute> {
            AddEditScorecardScreenRoot(
                isExpandedScreen = isExpandedScreen,
                onSaveOrCancel = {
                    navController.popBackStack()
                }
            )
        }

//        composable<ScoreMatchDestination> {
//            ScoreMatchScreen(
//                isExpandedScreen = isExpandedScreen,
//                onCloseScreen = {
//                    navController.popBackStack()
//                }
//            )
//        }
    }
}