package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import cricket.knowledgespike.scorer.app.ScoreAppNavGraph
import cricket.knowledgespike.scorer.app.ScorerRoute
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme


@Composable
@Preview
fun App(widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Medium) {

    ScorerTheme {
        val navController = rememberNavController()

        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

        ScoreAppNavGraph(
            navController,
            startRoute = ScorerRoute.ScorecardsListRoute,
            isExpandedScreen = isExpandedScreen
        )

    }

}
