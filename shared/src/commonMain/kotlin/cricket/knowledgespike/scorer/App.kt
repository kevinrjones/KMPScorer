package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation.AddEditScorecardScreenRoot
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme


@Composable
@Preview
fun App(widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Medium) {

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    ScorerTheme {
        AddEditScorecardScreenRoot(
            isExpandedScreen = isExpandedScreen,
            onSaveOrCancel = {})
    }

}
