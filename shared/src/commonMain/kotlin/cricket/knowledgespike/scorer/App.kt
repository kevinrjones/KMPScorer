package cricket.knowledgespike.scorer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation.AddEditScorecardScreen
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation.AddEditScorecardScreenRoot
import cricket.knowledgespike.scorer.feature.find.match_list.presentation.components.FindMatches
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme

@Composable
@Preview
fun App() {
    ScorerTheme {
        AddEditScorecardScreenRoot(onSaveOrCancel = {})
    }

}
